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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
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
        "classpath:META-INF/spring/properties-context-pima.xml",
        "classpath:META-INF/spring/dao-pima.xml",
        })
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class CamelContextPimaTest {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( CamelContextPimaTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;

	@Autowired
	private AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
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
    	
    	context.start();
	}	
	
	@Test
	public void testBookingReportServiceRoute() throws Exception, IOException {
		Exchange senderExchange = createSenderExchange("src/test/resources/xmlInstances/bookingReport/BookingReport-Pima.xml");

		Person person = analyticalDatastoreDAO.getPerson(1);
		Assert.assertNull(person);
		
		Booking booking = analyticalDatastoreDAO.getBookingByBookingNumber("eDocumentID");
		assertNull(booking);
		
		List<BookingCharge> bookingCharges = analyticalDatastoreDAO.getBookingCharges( 1 ); 
		assertTrue(bookingCharges.isEmpty());
		
		List<BookingArrest> bookingArrests = analyticalDatastoreDAO.getBookingArrests( 1 ); 
		assertTrue(bookingArrests.isEmpty());
		
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:bookingReportServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		person = analyticalDatastoreDAO.getPerson(1);
		Assert.assertNotNull(person);
		
		Assert.assertEquals(Integer.valueOf(1), person.getPersonId());
		assertThat(person.getPersonSexId(), is(3));
		assertThat(person.getPersonRaceId(), is(5));
		assertThat(person.getPersonSexDescription(), is("Unknown"));
		assertThat(person.getPersonRaceDescription(), is("Caucasian"));
		assertThat(person.getLanguage(), is("English Speaker"));
		assertThat(person.getPersonBirthDate(), is(LocalDate.parse("1969-01-01")));
		Assert.assertEquals("e807f1fcf82d132f9bb018ca6738a19f", person.getPersonUniqueIdentifier());
		assertThat(person.getLanguageId(), is(41));
		assertThat(person.getSexOffenderStatusTypeId(), nullValue());
		assertNull(person.getMilitaryServiceStatusType().getValue());
		
		assertNull(person.getEducationLevel());
		assertNull(person.getOccupation());
		assertThat(person.getDomicileStatusTypeId(), is(2));
		assertThat(person.getProgramEligibilityTypeId(), is(2));

		
		List<BehavioralHealthAssessment> behavioralHealthAssessments = analyticalDatastoreDAO.getBehavioralHealthAssessments(1);
		assertFalse(behavioralHealthAssessments.isEmpty());
		
		BehavioralHealthAssessment behavioralHealthAssessment = behavioralHealthAssessments.get(0);
		
		assertTrue(behavioralHealthAssessment.getBehavioralHealthDiagnoses().size() == 1);
		assertThat(behavioralHealthAssessment.getBehavioralHealthDiagnoses().get(0), is("Schizophrenia 295.10"));
		assertThat(behavioralHealthAssessment.getPersonId(), is(1));
		assertThat(behavioralHealthAssessment.getBehavioralHealthAssessmentId(), is(1));
		assertThat(behavioralHealthAssessment.getSeriousMentalIllness(), is(true));
		assertThat(behavioralHealthAssessment.getCareEpisodeStartDate(), is(LocalDate.parse("2016-01-01")));
		assertThat(behavioralHealthAssessment.getCareEpisodeEndDate(), is(LocalDate.parse("2016-04-01")));
		assertThat(behavioralHealthAssessment.getEnrolledProviderName(), is("79"));
		assertThat(behavioralHealthAssessment.getMedicaidStatusTypeId(), is(2));

		List<Treatment> treatments = analyticalDatastoreDAO.getTreatments(1);
		assertThat(treatments.size(), is(1));
		
		Treatment treatment = treatments.get(0);
		assertThat(treatment.getBehavioralHealthAssessmentID(), is(1));
		assertThat(treatment.getTreatmentStartDate(), is(LocalDate.parse("2016-01-01"))); 
		assertThat(treatment.getTreatmentAdmissionReasonTypeId(), nullValue());
		assertThat(treatment.getTreatmentStatusTypeId(), nullValue());
		assertThat(treatment.getTreatmentProviderName(), is("Treatment Providing Organization Name"));
		
		
		List<PrescribedMedication> prescribedMedications = analyticalDatastoreDAO.getPrescribedMedication(1);
		assertThat(prescribedMedications.size(), is(1));
		
		PrescribedMedication  prescribedMedication = prescribedMedications.get(0);
		assertThat(prescribedMedication.getBehavioralHealthAssessmentID(), is(1));
		assertThat(prescribedMedication.getMedicationDescription(), is("Zyprexa"));
		assertThat(prescribedMedication.getMedicationDispensingDate(), is(LocalDate.parse("2016-01-01"))); 
		assertThat(prescribedMedication.getMedicationDoseMeasure(), is("3mg"));
		
		booking = analyticalDatastoreDAO.getBookingByBookingNumber("234567890");
		assertNotNull(booking);

		assertEquals(LocalDate.parse("2016-05-12"), booking.getBookingDate());
		assertEquals(LocalTime.parse("00:36:00"), booking.getBookingTime());
		assertThat(booking.getFacilityId(), is(1));
		assertThat(booking.getSupervisionUnitTypeId(), is(84)); 
		assertEquals("234567890", booking.getBookingNumber());
		assertNull(booking.getScheduledReleaseDate());
		assertThat(booking.getInmateJailResidentIndicator(), is(false)); 
		assertThat(booking.getInmateCurrentLocation(), is("Weekender Release")); 
		
		bookingArrests = analyticalDatastoreDAO.getBookingArrests(1);
		assertThat(bookingArrests.size(), is(1));
		BookingArrest bookingArrest = bookingArrests.get(0);
		
		assertTrue(bookingArrest.getBookingId() == 1); 
		assertTrue(bookingArrest.getBookingArrestId() == 1); 
		assertTrue(bookingArrest.getAddress().isEmpty()); 
		assertThat(bookingArrest.getArrestAgencyId(), is(16));

		bookingCharges = analyticalDatastoreDAO.getBookingCharges( 1 ); 
		assertThat(bookingCharges.size(), is(1));
		
		BookingCharge bookingCharge = bookingCharges.get(0);
		assertThat(bookingCharge.getChargeCode(), is("ARS13-1105"));
		assertNull(bookingCharge.getChargeDisposition());
		assertTrue(bookingCharge.getBookingArrestId() == 1);
		assertTrue(bookingCharge.getBondAmount().doubleValue() == 250000.00); 
		assertThat(bookingCharge.getBondType(), nullValue());
		assertThat(bookingCharge.getAgencyId(), nullValue());
		assertThat(bookingCharge.getChargeClassTypeId(), is(2));
		assertThat(bookingCharge.getBondStatusTypeId(), is(1));
		assertThat(bookingCharge.getChargeJurisdictionTypeId(), is(8));
		
		CustodyRelease custodyRelease = analyticalDatastoreDAO.getCustodyReleaseByBookingId(1);
		assertNull(custodyRelease);
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
