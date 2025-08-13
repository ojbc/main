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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.adapters.rapbackdatastore;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.adapters.rapbackdatastore.processor.IdentificationReportingResponseProcessorTest;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.helper.ZipUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

import jakarta.annotation.Resource;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"})
public class TestIdentificationResultsQueryRequestService {
	private final Log log = LogFactory.getLog( TestIdentficationRecordingAndResponse.class );

    @Autowired
    private ModelCamelContext context;
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:cxf:bean:identificationResultsQueryResponseService")
    protected MockEndpoint identificationInitialResultsQueryResponseServiceMock;
    
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @BeforeEach
    public void setUp() throws Exception {
    	
    	// Advise the Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
    	AdviceWith.adviceWith(context, "identificationResultsQueryRequestRoute", route -> {
    		route.replaceFromWith("direct:identificationResultsQueryRequest");
    	});
    	
    	AdviceWith.adviceWith(context, "identificationInitialResultsQueryResponseRoute", route -> {
    		route.weaveByToUri("identificationResultsQueryResponseServiceEndpoint").replace().to("mock:cxf:bean:identificationResultsQueryResponseService");
    	});
    	
        context.start();
    }
    
    final String CIVIL_FINGER_PRINTS_UPDATE = "update rapback_datastore.CIVIL_FINGER_PRINTS "
            + "set FINGER_PRINTS_FILE = ? where TRANSACTION_NUMBER = ? and FINGER_PRINTS_TYPE_ID = ?";
    
    final String CIVIL_INITIAL_RESULTS_UPDATE = "update rapback_datastore.CIVIL_INITIAL_RESULTS "
            + "set SEARCH_RESULT_FILE = ? where TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID = ?";
    
    final String CIVIL_RAPSHEET_UPDATE = "update rapback_datastore.CIVIL_INITIAL_RAP_SHEET "
            + "set RAP_SHEET = ? where CIVIL_INITIAL_RESULT_ID = ?";

    final String NSOR_DEMOGRAPHICS_UPDATE = "update rapback_datastore.NSOR_DEMOGRAPHICS "
            + "set DEMOGRAPHICS_FILE = ? where TRANSACTION_NUMBER = ? and NSOR_DEMOGRAPHICS_ID = ?";
    
    final String NSOR_SEARCH_RESULTS_UPDATE = "update rapback_datastore.NSOR_SEARCH_RESULT "
            + "set SEARCH_RESULT_FILE = ? where TRANSACTION_NUMBER = ? and NSOR_SEARCH_RESULT_ID = ?";
    
    @Test
    @DirtiesContext
    public void testInitialResultsRoute() throws Exception {
        byte[] fbiCivilFingerprints = ZipUtils.zip("FBICivilFingerprints".getBytes());
        jdbcTemplate.update(CIVIL_FINGER_PRINTS_UPDATE, fbiCivilFingerprints, "000001820140729014008339990", 1);
        
        byte[] stateCivilFingerprints = ZipUtils.zip("StateCivilFingerprints".getBytes());
        jdbcTemplate.update(CIVIL_FINGER_PRINTS_UPDATE, stateCivilFingerprints, "000001820140729014008339990", 2);
        
        byte[] fbiCivilResults = ZipUtils.zip("FBICivilInitialResults".getBytes());
        jdbcTemplate.update(CIVIL_INITIAL_RESULTS_UPDATE, fbiCivilResults, "000001820140729014008339990", 1);
        
        byte[] stateCivilResults = ZipUtils.zip("StateCivilInitialResults".getBytes());
        jdbcTemplate.update(CIVIL_INITIAL_RESULTS_UPDATE, stateCivilResults, "000001820140729014008339990", 2);
        
        byte[] civilRapSheet = ZipUtils.zip("CivilInitialResultsRapsheet1".getBytes());
        jdbcTemplate.update(CIVIL_RAPSHEET_UPDATE, civilRapSheet, 1);
        
        byte[] civilRapSheet2 = ZipUtils.zip("CivilInitialResultsRapsheet2".getBytes());
        jdbcTemplate.update(CIVIL_RAPSHEET_UPDATE, civilRapSheet2, 2);
        
        byte[] nsorDemo = ZipUtils.zip("NsorDemographics".getBytes());
        jdbcTemplate.update(NSOR_DEMOGRAPHICS_UPDATE, nsorDemo, "000001820140729014008339990", 1);
        
        byte[] nsorDemo1 = ZipUtils.zip("NsorDemographics1".getBytes());
        jdbcTemplate.update(NSOR_DEMOGRAPHICS_UPDATE, nsorDemo1, "000001820140729014008339990", 2);
        
        byte[] nsorSearch = ZipUtils.zip("NsorSearchResults".getBytes());
        jdbcTemplate.update(NSOR_SEARCH_RESULTS_UPDATE, nsorSearch, "000001820140729014008339990", 1);
        
        byte[] nsorSearch1 = ZipUtils.zip("NsorSearchResults1".getBytes());
        jdbcTemplate.update(NSOR_SEARCH_RESULTS_UPDATE, nsorSearch1, "000001820140729014008339990", 2);
        
        
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationResultsQuery/OrganizationIdentificationInitialResultsQueryRequest.xml");
		
		senderExchange.getIn().setHeader("operationName", "SubmitOrganizationIdentificationInitialResultsQueryRequest");

		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationResultsQueryRequest", senderExchange);
		
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
        		bodyString, "src/test/resources/xmlInstances/identificationResultsQuery/OrganizationIdentificationInitialResultsQueryResults.xml");

//        identificationInitialResultsQueryResponseServiceMock.reset();
//		senderExchange = MessageUtils.createSenderExchange(context, 
//				"src/test/resources/xmlInstances/identificationResultsQuery/CriminalIdentificationInitialResultsQueryRequest.xml");
//		
//		senderExchange.getIn().setHeader("operationName", "SubmitOrganizationIdentificationInitialResultsQueryRequest");
//
//		//Send the one-way exchange.  Using template.send will send an one way message
//		returnExchange = template.send("direct:identificationResultsQueryRequest", senderExchange);
//		
//		//Use getException to see if we received an exception
//		if (returnExchange.getException() != null)
//		{	
//			throw new Exception(returnExchange.getException());
//		}	
//		identificationInitialResultsQueryResponseServiceMock.expectedMessageCount(1);
//		identificationInitialResultsQueryResponseServiceMock.assertIsSatisfied();
//
//		receivedExchange = identificationInitialResultsQueryResponseServiceMock.getExchanges().get(0);
//		bodyDocument = receivedExchange.getIn().getBody(Document.class);
//		bodyString = OJBUtils.getStringFromDocument(bodyDocument);
//		log.info("body: \n" + bodyString);
//        IdentificationReportingResponseProcessorTest.assertAsExpected(
//        		bodyString, "src/test/resources/xmlInstances/identificationResultsQuery/CriminalIdentificationInitialResultsQueryResults.xml");

    }

    @Test
    @DirtiesContext
    public void testSubsequentResultsRoute() throws Exception {
    	
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationResultsQuery/OrganizationIdentificationSubsequentResultsQueryRequest.xml");
		
		senderExchange.getIn().setHeader("operationName", "SubmitOrganizationIdentificationSubsequentResultsQueryRequest");

		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationResultsQueryRequest", senderExchange);
		
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
        		bodyString, "src/test/resources/xmlInstances/identificationResultsQuery/OrganizationIdentificationSubsequentResultsQueryResults.xml");

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
        SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(
                new SamlAssertionWrapper(samlToken));
        message.put("wss4j.principal.result", principal);
        return message;
    }

}
