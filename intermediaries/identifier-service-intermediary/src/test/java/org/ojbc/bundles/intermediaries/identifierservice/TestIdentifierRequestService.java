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
package org.ojbc.bundles.intermediaries.identifierservice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.test.util.SoapMessageUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@UseAdviceWith
// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",
        "classpath:META-INF/spring/properties-context.xml"
		})
@DirtiesContext
@Ignore
public class TestIdentifierRequestService {
	
	private static final Log log = LogFactory.getLog( TestIdentifierRequestService.class );
	
    @Autowired
    private ModelCamelContext context;
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:identifierResponseReplyToEndPoint")
    protected MockEndpoint identifierResponseReplyToEndPointMock;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }
 
	@Before
	public void setUp() throws Exception {
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("identifierRequestRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:identfierRequestServiceEndpoint");
    	    }              
    	});

		context.getRouteDefinition("identifierServiceResponseRoute").adviceWith(
				context, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						mockEndpointsAndSkip("cxf:bean:identifierResponseReplyTo*");
					}
				});

    	context.start();
	}	
	
	@Test
	public void testIdentifierRequestService() throws Exception
	{
		testIdentifierRequestServiceRoute();	
	}

	private void testIdentifierRequestServiceRoute() throws Exception, IOException {
		Exchange senderExchange = SoapMessageUtils.createSenderExchange("src/test/resources/xmlInstances/identifierRequest.xml", context);
		senderExchange.getIn().setHeader("WSAddressingReplyTo", "https://endpointToBeDetermined");

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identfierRequestServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		identifierResponseReplyToEndPointMock.expectedMessageCount(1);
		identifierResponseReplyToEndPointMock.assertIsSatisfied();

		Exchange receivedExchange = identifierResponseReplyToEndPointMock.getExchanges().get(0);
		Document bodyDocument = receivedExchange.getIn().getBody(Document.class);
		String bodyString = OJBUtils.getStringFromDocument(bodyDocument);
		log.info("body: \n" + bodyString);
		
		String uniqueId = XmlUtils.xPathStringSearch(bodyDocument, "/i-resp-doc:IdentifierResponse/nc30:Identification/nc30:IdentificationID");
		assertNotNull(StringUtils.trimToNull(uniqueId));

	}
	

}
