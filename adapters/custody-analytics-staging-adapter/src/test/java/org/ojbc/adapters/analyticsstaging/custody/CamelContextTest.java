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
package org.ojbc.adapters.analyticsstaging.custody;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAOImpl;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        })
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class CamelContextTest {
	
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;

	@Autowired
	private AnalyticalDatastoreDAOImpl analyticalDatastoreDAOImpl;
	
	@EndpointInject(uri = "mock:direct:failedInvocation")
	protected MockEndpoint failedInvocationEndpoint;
	
	@Test
	@DirtiesContext
	public void contextStartup() {
		assertTrue(true);
	}

	@Before
	public void setUp() throws Exception {
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("booking_reporting_service").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:bookingReportServiceEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("booking_reporting_service_process_booking_report").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	//This assists testing an invocation failure
    	    	interceptSendToEndpoint("direct:failedInvocation").to("mock:direct:failedInvocation").stop();
    	    }              
    	});
    	
    	context.getRouteDefinition("behavioral_health_reporting_service").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:behavioralHealthServiceEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("behavioral_health_reporting_service_process_report").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	//This assists testing an invocation failure
    	    	interceptSendToEndpoint("direct:failedInvocation").to("mock:direct:failedInvocation").stop();
    	    }              
    	});
    	
    	context.getRouteDefinition("custody_release_reporting_service").adviceWith(context, new AdviceWithRouteBuilder() {
    		@Override
    		public void configure() throws Exception {
    			// The line below allows us to bypass CXF and send a message directly into the route
    			replaceFromWith("direct:custodyReleaseServiceEndpoint");
    		}              
    	});
    	
    	context.getRouteDefinition("custody_release_reporting_service_process_report").adviceWith(context, new AdviceWithRouteBuilder() {
    		@Override
    		public void configure() throws Exception {
    			//This assists testing an invocation failure
    			interceptSendToEndpoint("direct:failedInvocation").to("mock:direct:failedInvocation").stop();
    		}              
    	});
    	
    	context.getRouteDefinition("custody_status_change_reporting_service_process_report").adviceWith(context, new AdviceWithRouteBuilder() {
    		@Override
    		public void configure() throws Exception {
    			// The line below allows us to bypass CXF and send a message directly into the route
    			replaceFromWith("direct:custodyStatusChangeReportingService");
    		}              
    	});
    	
    	context.getRouteDefinition("custody_status_change_reporting_service_process_report").adviceWith(context, new AdviceWithRouteBuilder() {
    		@Override
    		public void configure() throws Exception {
    			// The line below allows us to bypass CXF and send a message directly into the route
    			interceptSendToEndpoint("direct:failedInvocation").to("mock:direct:failedInvocation").stop();
    		}              
    	});
    	
    	context.start();
	}	
	
	@Test
	@DirtiesContext
	public void testBookingReportService() throws Exception
	{
		testBookingReportServiceRoute();	
//		testBehavioralHealthReportServiceRoute();
//		testCustodyReleaseReportServiceRoute();
	}
	
	@Test
	@Ignore
	public void testCustodyStatusChangeReportService() throws Exception
	{
		Exchange senderExchange = createSenderExchange("src/test/resources/xmlInstances/custodyStatusChangeReport/CustodyStatusChangeReport.xml");

		Person person = analyticalDatastoreDAOImpl.getPerson(1);
		Assert.assertNotNull(person);
		
		BookingSubject bookingSubject = analyticalDatastoreDAOImpl.getBookingSubject(1);
		Assert.assertNotNull(bookingSubject);
		
		CustodyStatusChange custodyStatusChange = analyticalDatastoreDAOImpl.getCustodyStatusChangeByReportId("eDocumentID");
		assertNull(custodyStatusChange);
		
		List<CustodyStatusChangeCharge> custodyStatusChangeCharges = analyticalDatastoreDAOImpl.getCustodyStatusChangeCharges( 1 ); 
		assertTrue(custodyStatusChangeCharges.isEmpty());
		
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:custodyStatusChangeReportingService", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		person = analyticalDatastoreDAOImpl.getPerson(1);
		Assert.assertNotNull(person);

		Person person2 = analyticalDatastoreDAOImpl.getPerson(2);
		Assert.assertNull(person2);

		bookingSubject = analyticalDatastoreDAOImpl.getBookingSubject(2);
		Assert.assertNotNull(bookingSubject);

		Assert.assertEquals(Integer.valueOf(1), bookingSubject.getPersonId());
		Assert.assertEquals(Integer.valueOf(0), bookingSubject.getRecidivistIndicator());
		Assert.assertTrue(person.getPersonBirthDate().until(java.time.LocalDate.now(), ChronoUnit.YEARS ) == 
				bookingSubject.getPersonAge().longValue());
		Assert.assertTrue(bookingSubject.getHousingStatusId() == 0);
		Assert.assertTrue(bookingSubject.getEducationLevelId() == 0);
		Assert.assertTrue(bookingSubject.getOccupationId() == 0);
		Assert.assertTrue(bookingSubject.getIncomeLevelId() == 0);
		
		custodyStatusChange = analyticalDatastoreDAOImpl.getCustodyStatusChangeByReportId("eDocumentID");
		assertNotNull(custodyStatusChange);

		assertTrue(custodyStatusChange.getJurisdictionId() == 2);
		assertEquals(LocalDateTime.parse("2012-12-17T09:30:47"), custodyStatusChange.getReportDate());
		assertEquals(LocalDateTime.parse("2013-12-17T09:30"), custodyStatusChange.getBookingDate());
		assertEquals(LocalDate.parse("2013-12-17"), custodyStatusChange.getCommitDate());
		assertEquals("eDocumentID", custodyStatusChange.getReportId());
		assertTrue(custodyStatusChange.getCaseStatusId() == 1); 
		assertTrue(custodyStatusChange.getPretrialStatusId() == 3);
		assertTrue(custodyStatusChange.getFacilityId() == 2);
		assertTrue(custodyStatusChange.getBedTypeId() == 3); 
		assertEquals("Booking Number", custodyStatusChange.getBookingNumber());
		
		custodyStatusChangeCharges = analyticalDatastoreDAOImpl.getCustodyStatusChangeCharges( 1 ); 
		assertFalse(custodyStatusChangeCharges.isEmpty());
		
		CustodyStatusChangeCharge custodyStatusChangeCharge = custodyStatusChangeCharges.get(0);
		assertThat(custodyStatusChangeCharge.getChargeType().getValue(), is("Felony"));
//		assertTrue(custodyStatusChangeCharge.getCustodyStatusChangeId() == 1);
		
	}
	
	@Test
	public void testBookingReportServiceRoute() throws Exception, IOException {
		Exchange senderExchange = createSenderExchange("src/test/resources/xmlInstances/bookingReport/BookingReportJail_multiple.xml");

		Person person = analyticalDatastoreDAOImpl.getPerson(1);
		Assert.assertNull(person);
		
		BookingSubject bookingSubject = analyticalDatastoreDAOImpl.getBookingSubject(1);
		Assert.assertNull(bookingSubject);
		
		Booking booking = analyticalDatastoreDAOImpl.getBookingByBookingReportId("eDocumentID");
		assertNull(booking);
		
		List<BookingCharge> bookingCharges = analyticalDatastoreDAOImpl.getBookingCharges( 1 ); 
		assertTrue(bookingCharges.isEmpty());
		
		List<BookingArrest> bookingArrests = analyticalDatastoreDAOImpl.getBookingArrests( 1 ); 
		assertTrue(bookingArrests.isEmpty());
		
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:bookingReportServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		person = analyticalDatastoreDAOImpl.getPerson(1);
		Assert.assertNotNull(person);
		
		Assert.assertEquals(Integer.valueOf(1), person.getPersonId());
		assertThat(person.getPersonSexId(), is(2));
		assertThat(person.getPersonRaceId(), is(2));
		assertThat(person.getPersonSexCode(), is("F"));
		assertThat(person.getPersonRaceCode(), is("A"));
		assertThat(person.getLanguage(), is("English"));
		assertThat(person.getPersonBirthDate(), is(LocalDate.parse("1968-12-17")));
		Assert.assertEquals("e807f1fcf82d132f9bb018ca6738a19f", person.getPersonUniqueIdentifier());
		assertThat(person.getBookingSubjectNumber(), is("Booking Subject Number"));
		assertThat(person.getLanguageId(), is(1));
		Assert.assertEquals("Female", person.getPersonSexDescription());
		assertThat(person.getPersonSsn(), is("123-45-6789"));
		assertThat(person.getPersonSid(), is("XX0120010324"));
		assertThat(person.getPersonEyeColor(), is("Blue"));
		assertThat(person.getPersonHairColor(), is("Blonde"));
		assertThat(person.getPersonHeight(), is("110"));
		assertThat(person.getPersonHeightMeasureUnit(), is("4H"));
		assertThat(person.getPersonWeight(), is("110"));
		assertThat(person.getPersonWeightMeasureUnit(), is("4H"));
		assertThat(person.getRegisteredSexOffender(), is(false));
		
		bookingSubject = analyticalDatastoreDAOImpl.getBookingSubject(1);
		Assert.assertNotNull(bookingSubject);
		
		Assert.assertEquals(Integer.valueOf(1), bookingSubject.getPersonId());
		Assert.assertEquals(Integer.valueOf(0), bookingSubject.getRecidivistIndicator());
		Assert.assertTrue(person.getPersonBirthDate().until(java.time.LocalDate.now(), ChronoUnit.YEARS ) == 
				bookingSubject.getPersonAge().longValue());
		Assert.assertTrue(bookingSubject.getHousingStatusId() == 1);
		Assert.assertTrue(bookingSubject.getEducationLevelId() == 1);
		Assert.assertTrue(bookingSubject.getOccupationId() == 1);
		Assert.assertTrue(bookingSubject.getIncomeLevelId() == 1);
		
		
		List<BehavioralHealthAssessment> behavioralHealthAssessments = analyticalDatastoreDAOImpl.getBehavioralHealthAssessments(1);
		assertFalse(behavioralHealthAssessments.isEmpty());
		
		BehavioralHealthAssessment behavioralHealthAssessment = behavioralHealthAssessments.get(0);
		
		assertTrue(behavioralHealthAssessment.getBehavioralHealthTypes().size() == 1);
		assertThat(behavioralHealthAssessment.getBehavioralHealthTypes().get(0).getValue(), is("Schizophrenia 295.10"));
		assertThat(behavioralHealthAssessment.getPersonId(), is(1));
		assertThat(behavioralHealthAssessment.getBehavioralHealthAssessmentId(), is(1));
		assertThat(behavioralHealthAssessment.getSeriousMentalIllness(), is(true));
		assertThat(behavioralHealthAssessment.getHighRiskNeeds(), is(true));
		assertThat(behavioralHealthAssessment.getGeneralMentalHealthCondition(), is(false));
		assertThat(behavioralHealthAssessment.getCareEpisodeStartDate(), is(LocalDate.parse("2016-01-01")));
		assertThat(behavioralHealthAssessment.getCareEpisodeEndDate(), is(LocalDate.parse("2016-04-01")));
		assertThat(behavioralHealthAssessment.getRegionalAuthorityAssignmentText(), is("79"));

		List<Treatment> treatments = analyticalDatastoreDAOImpl.getTreatments(1);
		assertThat(treatments.size(), is(1));
		
		Treatment treatment = treatments.get(0);
		assertThat(treatment.getBehavioralHealthAssessmentID(), is(1));
		assertThat(treatment.getStartDate(), is(LocalDate.parse("2016-01-01"))); 
		assertNull(treatment.getEndDate()); 
		assertThat(treatment.getTreatmentCourtOrdered(), is(true));
		assertThat(treatment.getTreatmentActive(), is(true));
		assertThat(treatment.getTreatmentText(), is("person was treated"));
		assertThat(treatment.getTreatmentProvider(), is("Treatment Providing Organization Name"));
		
		
		List<PrescribedMedication> prescribedMedications = analyticalDatastoreDAOImpl.getPrescribedMedication(1);
		assertThat(prescribedMedications.size(), is(1));
		
		PrescribedMedication  prescribedMedication = prescribedMedications.get(0);
		assertThat(prescribedMedication.getBehavioralHealthAssessmentID(), is(1));
		assertThat(prescribedMedication.getMedicationId(), is(1));
		assertThat(prescribedMedication.getMedicationDispensingDate(), is(LocalDate.parse("2016-01-01"))); 
		assertThat(prescribedMedication.getMedicationDoseMeasure(), is("3mg"));
		assertThat(prescribedMedication.getMedication().getGeneralProductId(), is("58-20-00-60-10-01-05"));
		assertThat(prescribedMedication.getMedication().getItemName(), is("Zyprexa"));
		
		booking = analyticalDatastoreDAOImpl.getBookingByBookingReportId("eDocumentID");
		assertNotNull(booking);

		assertTrue(booking.getJurisdictionId() == 4);
		assertEquals(LocalDateTime.parse("2012-12-17T09:30:47"), booking.getBookingReportDate());
		assertEquals(LocalDateTime.parse("2013-12-17T09:30"), booking.getBookingDate());
		assertEquals(LocalDate.parse("2013-12-17"), booking.getCommitDate());
		assertEquals("eDocumentID", booking.getBookingReportId());
		assertTrue(booking.getCaseStatusId() == 1); 
		assertTrue(booking.getFacilityId() == 1);
		assertTrue(booking.getBedTypeId() == 2); 
		assertEquals("Booking Number", booking.getBookingNumber());
		
		assertNotNull(booking.getCustodyRelease());
		assertEquals(LocalDate.parse("2014-12-17"), booking.getCustodyRelease().getScheduledReleaseDate());
		
		bookingArrests = analyticalDatastoreDAOImpl.getBookingArrests(1);
		assertFalse(bookingArrests.isEmpty());
		BookingArrest bookingArrest = bookingArrests.get(0);
		
		assertTrue(bookingArrest.getBookingId() == 1); 
		assertTrue(bookingArrest.getBookingArrestId() == 1); 
		assertEquals("392", bookingArrest.getAddress().getStreetNumber()); 
		assertEquals("Woodlawn Ave", bookingArrest.getAddress().getStreetName()); 
		assertEquals("Burlington", bookingArrest.getAddress().getCity()); 
		assertEquals("NY", bookingArrest.getAddress().getState()); 
		assertEquals("05408", bookingArrest.getAddress().getPostalcode()); 
		assertTrue(bookingArrest.getAddress().getArrestLocationLatitude().doubleValue() == 56.1111 ); 
		assertTrue(bookingArrest.getAddress().getArrestLocationLongitude().doubleValue() == 32.1111 );
		
		bookingCharges = analyticalDatastoreDAOImpl.getBookingCharges( 1 ); 
		assertFalse(bookingCharges.isEmpty());
		
		BookingCharge bookingCharge = bookingCharges.get(0);
		assertThat(bookingCharge.getChargeType().getValue(), is("Felony"));
		assertTrue(bookingCharge.getBookingArrestId() == 1);
		assertTrue(bookingCharge.getBondAmount().doubleValue() == 500.00); 
		assertThat(bookingCharge.getBondType().getValue(), is("Unknown"));
		assertThat(bookingCharge.getNextCourtName(), is("Court Name"));
		assertThat(bookingCharge.getNextCourtDate(), is(LocalDate.parse("1968-12-17")));
		assertThat(bookingCharge.getAgencyId(), is(9));
		
		
		CustodyRelease custodyRelease = analyticalDatastoreDAOImpl.getCustodyReleaseByBookingNumber("Booking Number");
		log.info(custodyRelease.toString());
		assertEquals(LocalDateTime.parse("2014-12-17T10:30"), custodyRelease.getReleaseDate());
		assertEquals(LocalDateTime.parse("2012-12-17T09:30:47"), custodyRelease.getReportDate());
		assertEquals(LocalDate.parse("2014-12-17"), custodyRelease.getScheduledReleaseDate());
		
	}
	
	public void testBehavioralHealthReportServiceRoute() throws Exception
	{
		List<BehavioralHealthAssessment> behavioralHealthAssessments = analyticalDatastoreDAOImpl.getBehavioralHealthAssessments( 1 ); 
		assertTrue( behavioralHealthAssessments.isEmpty());
		
    	Exchange senderExchange = createSenderExchange("src/test/resources/xmlInstances/behavioralHealthReport/PersonIndexRecord.xml");
	    
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:behavioralHealthServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		behavioralHealthAssessments = analyticalDatastoreDAOImpl.getBehavioralHealthAssessments( 1 ); 
		assertFalse( behavioralHealthAssessments.isEmpty());
		
		BehavioralHealthAssessment behavioralHealthAssessment = behavioralHealthAssessments.get(0);
		assertTrue(behavioralHealthAssessment.getBehavioralHealthAssessmentId() == 1); 
//		assertThat(behavioralHealthAssessment.getBehavioralHealthType().getValue(), is("Illness 1")); 
		assertTrue(behavioralHealthAssessment.getPersonId() == 1); 
//		assertThat(behavioralHealthAssessment.getEvaluationDate(), is(LocalDate.parse("2015-08-13")));

	}
	
	public void testCustodyReleaseReportServiceRoute() throws Exception
	{
		CustodyRelease custodyRelease = analyticalDatastoreDAOImpl.getCustodyReleaseByBookingNumber("Booking Number"); 
		assertEquals(LocalDateTime.parse("2014-12-17T10:30"), custodyRelease.getReleaseDate());
		assertEquals(LocalDateTime.parse("2012-12-17T09:30:47"), custodyRelease.getReportDate());
		
		Exchange senderExchange = createSenderExchange("src/test/resources/xmlInstances/custodyReleaseReport/CustodyReleaseReport.xml");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:custodyReleaseServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		custodyRelease = analyticalDatastoreDAOImpl.getCustodyReleaseByBookingNumber("Booking Number");
		assertEquals( LocalDateTime.parse("2015-12-17T09:30:47"), custodyRelease.getReleaseDate());
		assertEquals( LocalDateTime.parse("2015-12-17T09:30:47"), custodyRelease.getReportDate());
	}
	
	protected Exchange createSenderExchange(String inputFilePath) throws Exception, IOException {
		//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File(inputFilePath);
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
		return senderExchange;
	}
	
	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}	
	
	public static Document createDocument() throws Exception{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();

		return doc;
	}

}
