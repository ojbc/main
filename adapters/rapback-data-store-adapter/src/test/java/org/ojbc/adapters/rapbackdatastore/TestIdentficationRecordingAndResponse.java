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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.ojbc.adapters.rapbackdatastore.processor.IdentificationReportingResponseProcessorTest.assertAsExpected;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.adapters.rapbackdatastore.processor.IdentificationRequestReportProcessor;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/subscription-management-routes.xml"
      })
@DirtiesContext
public class TestIdentficationRecordingAndResponse {
	
	private static final Log log = LogFactory.getLog( TestIdentficationRecordingAndResponse.class );

	private static final String TRANSACTION_NUMBER = "000001820140729014008340000";
	
	@Autowired
	RapbackDAO rapbackDAO;

    @Resource  
    private DataSource dataSource;  

    //This is used to update database to achieve desired state for test
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IdentificationRequestReportProcessor identificationRequestReportProcessor;
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
	@EndpointInject(uri = "mock:failedInvocation")
    protected MockEndpoint failedInvocationEndpoint;
	
    @EndpointInject(uri = "mock:cxf:bean:identificationReportingResponseService")
    protected MockEndpoint identificationReportingResponseService;
    
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@Before
	public void setUp() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		assertNotNull(identificationRequestReportProcessor);

    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("identification_recording_service").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:identificationRecordingServiceEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("send_identification_reporting_response_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:identificationReportingResponseService*");
    	    }              
    	});
    	context.start();
	}	
	
	
	@Test
	@DirtiesContext
	public void testIdentificationRecordingServiceError() throws Exception
	{
		identificationReportingResponseService.reset();
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction("000001820140729014008340000"); 
		assertNull(identificationTransaction);
		
    	Exchange senderExchange = MessageUtils.createSenderExchange(context, 
    			"src/test/resources/xmlInstances/identificationReport/person_identification_request_fbi_civil.xml");
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_failure_response.xml");
		
		identificationTransaction = rapbackDAO.getIdentificationTransaction("000001820140729014008340000");
		assertNull(identificationTransaction);

	}
	
	@Test
	@DirtiesContext
	public void testIdentificationRecordingRequestAndResultsSuccess() throws Exception
	{
		civilRecordingRequestSuccess(); 
//		civilRecordingResultServiceSuccess();
	}

	private void civilRecordingRequestSuccess() throws Exception, IOException,
			InterruptedException, SAXException {
		final String CIVIL_STATE_FINGERPRINT_SELECT="SELECT FINGER_PRINTS_FILE FROM CIVIL_FINGER_PRINTS WHERE TRANSACTION_NUMBER = ? and FINGER_PRINTS_TYPE_ID=2";
		final String CIVIL_FBI_FINGERPRINT_SELECT="SELECT count(*) FROM CIVIL_FINGER_PRINTS WHERE TRANSACTION_NUMBER = ? and FINGER_PRINTS_TYPE_ID=1";
		identificationReportingResponseService.reset();
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNull(identificationTransaction);
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_state_civil.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationRequest");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER);
		
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("68796860"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("ID23457"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("I"));
		assertThat(identificationTransaction.getArchived(), is(false));
		assertNotNull(identificationTransaction.getAvailableForSubscriptionStartDate());
		
		log.info(identificationTransaction.toString());
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject());
		
		Subject subject = identificationTransaction.getSubject(); 
		assertNull(subject.getUcn());
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
		senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_fbi_civil.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationRequest");
		returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		identificationReportingResponseService.expectedMessageCount(2);
		identificationReportingResponseService.assertIsSatisfied();
		identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER);
		
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("68796860"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("ID23457"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("J"));
		assertThat(identificationTransaction.getArchived(), is(false));
		
		log.info(identificationTransaction.toString());
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject());
		
		subject = identificationTransaction.getSubject(); 
		assertNull(subject.getUcn());
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
		byte[] stateFingerPrintsFileZipped = jdbcTemplate.queryForObject(CIVIL_STATE_FINGERPRINT_SELECT, new Object[] {TRANSACTION_NUMBER}, byte[].class);
		assertNotNull(stateFingerPrintsFileZipped);
		Integer fbiFingerPrintsCount = jdbcTemplate.queryForObject(CIVIL_FBI_FINGERPRINT_SELECT, new Object[] {TRANSACTION_NUMBER}, Integer.class);
		assertThat(fbiFingerPrintsCount, is(0)); 
		
	}
	
	@Test
	@DirtiesContext
	public void testCivilVechsRecordingRequestSuccess() throws Exception, IOException,
	InterruptedException, SAXException {
		
		identificationReportingResponseService.reset();
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNull(identificationTransaction);
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_fbi_civil_VECHS.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationRequest");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER);
		
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("VECHS0002"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("F-VECHS0002-VOL"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("I"));
		assertThat(identificationTransaction.getArchived(), is(false));
		
		log.info(identificationTransaction.toString());
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject());
		
		Subject subject = identificationTransaction.getSubject(); 
		assertNull(subject.getUcn());
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
	}

	@Test
	@DirtiesContext
	public void testCivilVechsNoHCJDCRecordingRequestSuccess() throws Exception, IOException,
	InterruptedException, SAXException {
		
		identificationReportingResponseService.reset();
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNull(identificationTransaction);
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_fbi_civil_VECHS_NoHCJDC.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationRequest");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER);
		
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("HI0010200"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("F-VECHS0002-VOL"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("I"));
		assertThat(identificationTransaction.getArchived(), is(false));
		
		log.info(identificationTransaction.toString());
		assertNotNull(identificationTransaction); 
		assertNotNull(identificationTransaction.getSubject());
		
		Subject subject = identificationTransaction.getSubject(); 
		assertNull(subject.getUcn());
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
	}
	
	public void civilRecordingResultServiceSuccess() throws Exception
	{
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_rapsheet_results_fbi_civil.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(3);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(2);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_failure_response.xml");
		
		identificationReportingResponseService.reset();
		Exchange searchResultSenderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_search_results_fbi_civil.xml");
		                                                              
		searchResultSenderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:identificationRecordingServiceEndpoint", searchResultSenderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNotNull(identificationTransaction);
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("68796860"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("ID23457"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("J"));
		assertThat(identificationTransaction.getArchived(), is(false));
		
		assertNotNull(identificationTransaction.getSubject());
		
		Subject subject = identificationTransaction.getSubject(); 
		assertThat(subject.getUcn(), is("B1234567"));
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
		List<CivilInitialResults> civilInitialResults = rapbackDAO.getCivilInitialResults(TRANSACTION_NUMBER, ResultSender.FBI);
		
		assertThat(civilInitialResults.size(), is(1));
		
		CivilInitialResults civilInitialResult = civilInitialResults.get(0);
		assertTrue(civilInitialResult.getRapsheets().isEmpty());
		assertThat(civilInitialResult.getSearchResultFile(), is(Base64.decodeBase64("VGhpcyBpcyBhIGNyaW1pbmFsIGhpc3Rvcnk")));
		
		
		//Send the rap sheet exchange.  
		identificationReportingResponseService.reset();
		senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_rapsheet_results_fbi_civil.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNotNull(identificationTransaction);
		assertThat(identificationTransaction.getTransactionNumber(), is(TRANSACTION_NUMBER));
		assertThat(identificationTransaction.getOtn(), is("OTN12345"));
		assertThat(identificationTransaction.getOwnerOri(), is("68796860"));
		assertThat(identificationTransaction.getOwnerProgramOca(), is("ID23457"));
		assertThat(identificationTransaction.getIdentificationCategory(), is("J"));
		assertThat(identificationTransaction.getArchived(), is(false));
		
		assertNotNull(identificationTransaction.getSubject());
		
		subject = identificationTransaction.getSubject(); 
		assertThat(subject.getUcn(), is("B1234567"));
		assertNull(subject.getCriminalSid());
		assertNull(subject.getCivilSid());
		assertThat(subject.getFirstName(), is("Joe"));
		assertThat(subject.getLastName(), is("Smith"));
		assertThat(subject.getMiddleInitial(), is("D"));
		assertThat( subject.getDob(), is(DateTime.parse("1900-01-01")));
		assertThat(subject.getSexCode(), is("M"));
		
		civilInitialResults = rapbackDAO.getIdentificationCivilInitialResults(TRANSACTION_NUMBER);
		
		assertThat(civilInitialResults.size(), is(1));
		
		civilInitialResult = civilInitialResults.get(0);
		assertThat(civilInitialResult.getSearchResultFile(), is(Base64.decodeBase64("VGhpcyBpcyBhIGNyaW1pbmFsIGhpc3Rvcnk")));
		assertThat(civilInitialResult.getRapsheets().size(), is(1));
		assertThat(civilInitialResult.getRapsheets().get(0), is(Base64.decodeBase64("VGhpcyBpcyBhIGNyaW1pbmFsIGhpc3Rvcnk")));
		
	}
	
	@Test
	@DirtiesContext
	public void testIdentificationRecordingCriminalResultsSuccess() throws Exception
	{
		criminalRecordingResultServiceSuccess();
	}
	
	public void criminalRecordingResultServiceSuccess() throws Exception
	{
		identificationReportingResponseService.reset();
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_search_results_fbi_criminal.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction("000001820140729014008340000");
		assertThat(identificationTransaction, notNullValue());
		assertThat(identificationTransaction.getSubject(), notNullValue());
		assertThat(identificationTransaction.getSubject().getUcn(), is("B1234567"));
		assertThat(identificationTransaction.getSubject().getCivilSid(), nullValue());
		assertThat(identificationTransaction.getSubject().getCriminalSid(), nullValue());
		assertThat(identificationTransaction.getSubject().getFirstName(), is("Joe"));
		assertThat(identificationTransaction.getSubject().getLastName(), is("Smith"));
		assertThat(identificationTransaction.getSubject().getMiddleInitial(), is("D"));
		assertThat(identificationTransaction.getSubject().getSexCode(), is("M"));
		assertThat(identificationTransaction.getSubject().getDob(), is(DateTime.parse("1950-01-02")));
				
		senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_criminal.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonStateIdentificationResults");
		identificationReportingResponseService.reset();
		returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
		
		identificationTransaction = rapbackDAO.getIdentificationTransaction("000001820140729014008340000");
		assertThat(identificationTransaction, notNullValue());
		assertThat(identificationTransaction.getSubject(), notNullValue());
		assertThat(identificationTransaction.getSubject().getUcn(), is("B1234567"));
		assertThat(identificationTransaction.getSubject().getCivilSid(), is("A123456"));
		assertThat(identificationTransaction.getSubject().getCriminalSid(), nullValue());
		assertThat(identificationTransaction.getSubject().getFirstName(), is("Joe"));
		assertThat(identificationTransaction.getSubject().getLastName(), is("Smith"));
		assertThat(identificationTransaction.getSubject().getMiddleInitial(), is("D"));
		assertThat(identificationTransaction.getSubject().getSexCode(), is("M"));
		assertThat(identificationTransaction.getSubject().getDob(), is(DateTime.parse("1950-01-02")));
				
	}

	@Test
	public void criminalSidToHijisResultServiceSuccess() throws Exception
	{
		identificationReportingResponseService.reset();
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_criminal_sid_to_hijis.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/criminal_sid_to_hijis_report_success_response.xml");
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction("100A00720180726080727295739");
		assertThat(identificationTransaction, nullValue());
	}
	
	@Test
	public void civilSidToHijisResultServiceSuccess() throws Exception
	{
		identificationReportingResponseService.reset();
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_civil_sid_to_hijis.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationResults");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(1);
		
		identificationReportingResponseService.assertIsSatisfied();
		
		Exchange receivedExchange = identificationReportingResponseService.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		assertAsExpected(body, "src/test/resources/xmlInstances/identificationReportingResponse/civil_sid_to_hijis_report_success_response.xml");
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction("100A00720180726080727295739");
		assertThat(identificationTransaction, nullValue());
	}
	@Test
	@DirtiesContext
	public void testIgnoreCriminalRecordingRequest() throws Exception
	{
		identificationReportingResponseService.reset();
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_state_criminal.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonStateIdentificationRequest");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationReportingResponseService.expectedMessageCount(0);
		
		identificationReportingResponseService.assertIsSatisfied();
		
	}
}
