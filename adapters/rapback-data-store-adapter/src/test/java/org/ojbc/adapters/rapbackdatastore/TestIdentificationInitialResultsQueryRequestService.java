/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.rapbackdatastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.MessageImpl;
import org.apache.ws.security.SAMLTokenPrincipal;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAOImpl;
import org.ojbc.adapters.rapbackdatastore.processor.IdentificationReportingResponseProcessorTest;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@UseAdviceWith
// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml"
		})
@DirtiesContext
public class TestIdentificationInitialResultsQueryRequestService {
	private final Log log = LogFactory.getLog( TestIdentficationRecordingAndResponse.class );

    @Autowired
    private ModelCamelContext context;
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:identificationInitialResultsQueryResponseService")
    protected MockEndpoint identificationInitialResultsQueryResponseServiceMock;

    @Autowired
    private RapbackDAOImpl rapbackDAO;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @Before
    public void setUp() throws Exception {
    	
        // Advise the Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
        context.getRouteDefinition("identificationInitialResultsQueryRequestRoute")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:identificationInitialResultsQueryRequest");
                    }
                });

        context.getRouteDefinition("identificationInitialResultsQueryResponseRoute")
        .adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
            	mockEndpointsAndSkip("cxf:bean:identificationInitialResultsQueryResponseService*");
            }
        });

        context.start();
    }

    @Test
    @DirtiesContext
    public void testRoute() throws Exception {
    	
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/initialResultsQuery/OrganizationIdentificationInitialResultsQueryRequest.xml");
		
		senderExchange.getIn().setHeader("operationName", "SubmitOrganizationIdentificationInitialResultsQueryRequest");

		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationInitialResultsQueryRequest", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		identificationInitialResultsQueryResponseServiceMock.expectedMessageCount(1);
		identificationInitialResultsQueryResponseServiceMock.assertIsSatisfied();
		
		Exchange receivedExchange = identificationInitialResultsQueryResponseServiceMock.getExchanges().get(0);
		Document bodyDocument = receivedExchange.getIn().getBody(Document.class);
		String bodyString = OJBUtils.getStringFromDocument(bodyDocument);
		log.info("body: \n" + bodyString);
		
        IdentificationReportingResponseProcessorTest.assertAsExpected(
        		bodyString, "src/test/resources/xmlInstances/initialResultsQuery/OrganizationIdentificationInitialResultsQueryResults.xml");

		DataHandler dataHandler = receivedExchange.getIn().getAttachment("http://ojbc.org/identification/results/fbiSearchResultDocument");
		assertEquals("text/plain", dataHandler.getContentType());
		
		byte[] receivedData = IOUtils.readBytesFromStream(dataHandler.getInputStream());
		assertTrue(Arrays.equals("Found a Match".getBytes(), receivedData));
    }

    
    public static org.apache.cxf.message.Message createSamlAssertionMessageWithAttributes(
            Map<SamlAttribute, String> customAttributes) throws Exception {
        org.apache.cxf.message.Message message = new MessageImpl();

        Assertion samlToken = SAMLTokenUtils
                .createStaticAssertionWithCustomAttributes(
                        "https://idp.ojbc-local.org:9443/idp/shibboleth",
                        SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS,
                        SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true,
                        true, customAttributes);
        SAMLTokenPrincipal principal = new SAMLTokenPrincipal(
                new AssertionWrapper(samlToken));
        message.put("wss4j.principal.result", principal);
        return message;
    }

}
