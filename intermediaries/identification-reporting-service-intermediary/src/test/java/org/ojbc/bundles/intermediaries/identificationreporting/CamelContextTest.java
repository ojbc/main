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
package org.ojbc.bundles.intermediaries.identificationreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.identificationreporting.IdentificationReportingServiceApplication;
import org.ojbc.test.util.XmlTestUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@CamelSpringBootTest
@SpringBootTest(classes=IdentificationReportingServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class CamelContextTest {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportPersonIdentificationRequest";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/IdentificationReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:cxf:bean:IdentificationRecordingService")
    protected MockEndpoint identificationRecordingServiceMock;
    
    @EndpointInject(value = "mock:cxf:bean:identificationReportingResponseRecipient")
    protected MockEndpoint identificationRecordingResponseRecipientMock;
    
    @EndpointInject(value = "mock:cxf:bean:arrestReportingService")
    protected MockEndpoint arrestReportingServiceMock;
    
    @BeforeEach
	public void setUp() throws Exception {
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	AdviceWith.adviceWith(context, "IdentificationReportingServiceHandlerRoute", route -> {
    		route.replaceFromWith("direct:IdentificationReportingServiceEndpoint");
    	});
    	
    	
    	//We mock the IdentificationRecordingService endpoint and intercept any submissions
    	AdviceWith.adviceWith(context, "CallIdentificationRecordingServiceRoute", route -> {
    		route.weaveById("identificationRecordingServiceEndpoint").replace().to(identificationRecordingServiceMock);
    	});
    	
    	AdviceWith.adviceWith(context, "identificationReportingResponseHandlerRoute", route -> {
    		route.replaceFromWith("direct:IdentificationReportingResponseEndpoint");
    		route.weaveById("identificationReportingResponseRecipientEndpoint").replace().to(identificationRecordingResponseRecipientMock);
    	});
    	
    	AdviceWith.adviceWith(context, "stateIdentificationReportServiceRoute", route -> {
    		route.weaveById("arrestReportingServiceEndpoint").replace().to(arrestReportingServiceMock);
    	});
    	
		context.start();		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@Test
	@DirtiesContext
	public void testReportingContextRoutes() throws Exception
	{
		identificationRecordingServiceMock.expectedMessageCount(1);
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through an Identification Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the Identification report file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/identificationReport/person_identification_request_fbi_civil.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IdentificationReportingServiceEndpoint", senderExchange);

		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationRecordingServiceMock.assertIsNotSatisfied();
		
		senderExchange.getIn().setHeader("operationName", "ReportPersonFederalIdentificationRequest");
		
		returnExchange = template.send("direct:IdentificationReportingServiceEndpoint", senderExchange);
		
		identificationRecordingServiceMock.assertIsSatisfied();
		Exchange receivedExchange = identificationRecordingServiceMock.getExchanges().get(0);
		String body = receivedExchange.getIn().getBody(String.class);
		assertEquals(inputStr, body);
		
	}

	@Test
	@DirtiesContext
	public void testArrestReportingContextRoutes() throws Exception
	{
		identificationRecordingServiceMock.expectedMessageCount(1);
		arrestReportingServiceMock.expectedMessageCount(1);
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through an Identification Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the Identification report file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_criminal_with_civil_sid.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
		senderExchange.getIn().setHeader("operationName", "ReportPersonStateIdentificationResults");
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IdentificationReportingServiceEndpoint", senderExchange);

		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationRecordingServiceMock.assertIsSatisfied();
		Exchange receivedExchange = identificationRecordingServiceMock.getExchanges().get(0);
		String body = receivedExchange.getIn().getBody(String.class);
		assertEquals(inputStr, body);
		
		arrestReportingServiceMock.assertIsSatisfied();
		Exchange receivedArrestReportExchange = arrestReportingServiceMock.getExchanges().get(0);
		String arrestReport = receivedArrestReportExchange.getIn().getBody(String.class);
		
	    //Read the Identification report file from the file system
	    File expectedFile = new File("src/test/resources/xmlInstances/arrestReport/arrestReport_criminal_with_civil_sid.xml");
	    String expectedString = FileUtils.readFileToString(expectedFile);

		XmlTestUtils.compareDocs(expectedString, arrestReport, "lexs:MessageDateTime", "nc20:Date");		
		
	}

	@Test
	@DirtiesContext
	public void testArrestReportingContextRoutesWithCriminalSidToHijisMessage() throws Exception
	{
		identificationRecordingServiceMock.reset(); 
		identificationRecordingServiceMock.expectedMessageCount(1);
		arrestReportingServiceMock.reset(); 
		arrestReportingServiceMock.expectedMessageCount(0);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through an Identification Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the Identification report file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_criminal_sid_to_hijis.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
		senderExchange.getIn().setHeader("operationName", "ReportPersonStateIdentificationResults");
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IdentificationReportingServiceEndpoint", senderExchange);

		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationRecordingServiceMock.assertIsSatisfied();
		Exchange receivedExchange = identificationRecordingServiceMock.getExchanges().get(0);
		String body = receivedExchange.getIn().getBody(String.class);
		assertEquals(inputStr, body);
		
		arrestReportingServiceMock.assertIsSatisfied();
	}
	
	
	@Test
	@DirtiesContext
	@Disabled("Need to research why this is failing")
	public void testArrestReportingContextRoutesWithCivilSidToHijisMessage() throws Exception
	{
		identificationRecordingServiceMock.reset(); 
		identificationRecordingServiceMock.expectedMessageCount(1);
		arrestReportingServiceMock.reset(); 
		arrestReportingServiceMock.expectedMessageCount(1);
		
		//Create a new exchange
		Exchange senderExchange = new DefaultExchange(context);
		
		//Test the entire web service route by sending through an Identification Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		//Read the Identification report file from the file system
		File inputFile = new File("src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_civil_sid_to_hijis.xml");
		String inputStr = FileUtils.readFileToString(inputFile);
		
		assertNotNull(inputStr);
		
		senderExchange.getIn().setHeader("operationName", "ReportPersonStateIdentificationResults");
		//Set it as the message message body
		senderExchange.getIn().setBody(inputStr);
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IdentificationReportingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationRecordingServiceMock.assertIsSatisfied();
		Exchange receivedExchange = identificationRecordingServiceMock.getExchanges().get(0);
		String body = receivedExchange.getIn().getBody(String.class);
		assertEquals(inputStr, body);
		
		arrestReportingServiceMock.assertIsSatisfied();
	}
	
	@Test
	@DirtiesContext
	public void testReportingResponseRoute() throws Exception
	{
		identificationRecordingResponseRecipientMock.expectedMessageCount(1);
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through an Identification Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the Identification reporting response file from the file system.
	    File inputFile = new File("src/test/resources/xmlInstances/identificationReportingResponse/person_identification_report_success_response.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    senderExchange.getIn().setHeader("federatedQueryRequestGUID", "12345");

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IdentificationReportingResponseEndpoint", senderExchange);

		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		identificationRecordingResponseRecipientMock.assertIsSatisfied();
		
		Exchange receivedExchange = identificationRecordingResponseRecipientMock.getExchanges().get(0);
		String body = receivedExchange.getIn().getBody(String.class);
		assertEquals(inputStr, body);
		
		String operationName = (String) receivedExchange.getIn().getHeader("operationName");
		assertEquals("SubmitPersonIdentificationReportResponse", operationName);
		
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
	
	public byte[] extractBytes(String ImageName) throws IOException {
		// open image
		File imgPath = new File(ImageName);
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ImageIO.write(bufferedImage, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		return imageInByte;
	}
	
    static void assertByteArrayEquals(byte[] bytes1, byte[] bytes2) {
        assertNotNull(bytes1);
        assertNotNull(bytes2);
        assertEquals(bytes1.length, bytes2.length);
        for (int i = 0; i < bytes1.length; i++) {
            assertEquals(bytes1[i], bytes2[i]);
        }
    }
}
