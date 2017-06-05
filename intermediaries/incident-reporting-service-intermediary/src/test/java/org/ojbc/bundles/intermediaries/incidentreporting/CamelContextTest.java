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
package org.ojbc.bundles.intermediaries.incidentreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.incidentreporting.IncidentReportProcessor;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",		
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/jetty-server.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-incident-reporting-state-cache.xml"		
		})
public class CamelContextTest {

	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportIncident";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/IncidentReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:N-DexSubmissionServiceFacade")
    protected MockEndpoint ndexServiceMock;
    
    @EndpointInject(uri = "mock:cxf:bean:ChargeReferralService")
    protected MockEndpoint chargeReferralServiceMock;
    
    @EndpointInject(uri = "mock:cxf:bean:ChargeReferralReportingService")
    protected MockEndpoint chargeReferralReportingServiceMock;     
    
    @EndpointInject(uri = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerServiceMock;

    @EndpointInject(uri = "mock:cxf:bean:arrestReportingService")
    protected MockEndpoint arrestReportingBrokerServiceMock;
    

    @Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("IncidentReportingServiceHandlerRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:IncidentReportingServiceEndpoint");
    	    }              
    	});

    	//We mock the 'N-DEx' web service endpoint and intercept any submissions
    	context.getRouteDefinition("CallNDExSubmissionServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:N-DexSubmissionServiceFacade*");
    	    }              
    	});

    	//We mock the Charge Referral web service endpoint and intercept any submissions
    	context.getRouteDefinition("CallChargeReferralServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:ChargeReferralService*");
    	    }              
    	});
    	
    	    
    	//We mock the Charge Referral Reporting web service endpoint and intercept any submissions
    	context.getRouteDefinition("CallChargeReferralReportingService_Route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:ChargeReferralReportingService*");
    	    }              
    	});    	    	    	

    	//We mock the Notification Broker web service endpoint and intercept any submissions
    	context.getRouteDefinition("callNotificationBrokerServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:notificationBrokerService*");
    	    }              
    	});

    	//We mock the Arrest Reporting web service endpoint and intercept any submissions
    	context.getRouteDefinition("callArrestReportingServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:arrestReportingService*");
    	    }              
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
	public void testContextRoutes() throws Exception
	{
		
    	//We should get one message for all endpoints
		ndexServiceMock.expectedMessageCount(1);
		chargeReferralServiceMock.expectedMessageCount(1);
		chargeReferralReportingServiceMock.expectedMessageCount(1);		
		arrestReportingBrokerServiceMock.expectedMessageCount(1);
		
		//Notification broker will get two exchanges due to the splitter in there
		notificationBrokerServiceMock.expectedMessageCount(2);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
					
    	//Test Incident Report Processor bean that sets 'call ndex' header
    	IncidentReportProcessor incidentReportProcessor = new IncidentReportProcessor("123", "456,789");
    	
    	incidentReportProcessor.confirmNdexAuthorizedOri(senderExchange, "123");
    	assertEquals(senderExchange.getIn().getHeader("callNDExSubmissionService"), true);

    	senderExchange.getIn().removeHeader("callNDExSubmissionService");
    	
    	incidentReportProcessor.confirmNdexAuthorizedOri(senderExchange, "456");
    	assertEquals(senderExchange.getIn().getHeader("callNDExSubmissionService"), false);
    	
    	senderExchange.getIn().removeHeader("callNDExSubmissionService");
    	
    	//Test the entire web service route by sending through an Incident Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the incident report file from the file system, this example has an as an approved submitter
	    File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IncidentReportingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);

		//Assert that the mock endpoint expectations are satisfied
		ndexServiceMock.assertIsSatisfied();
		chargeReferralServiceMock.assertIsSatisfied();
		chargeReferralReportingServiceMock.assertIsSatisfied();
		arrestReportingBrokerServiceMock.assertIsSatisfied();
		notificationBrokerServiceMock.assertIsSatisfied();
		
		//Retrieve Charge Referral Message and assert that the proper root and child elements are set
		Exchange chargeReferralExchange = chargeReferralServiceMock.getExchanges().get(0);
		Document chargeReferralDocument = chargeReferralExchange.getIn().getBody(Document.class);
		XmlUtils.printNode(chargeReferralDocument);
		assertNotNull(XmlUtils.xPathNodeSearch(chargeReferralDocument, "/cr-doc:ChargeReferral"));
		assertNotNull(XmlUtils.xPathNodeSearch(chargeReferralDocument, "/cr-doc:ChargeReferral/lexspd:doPublish"));
		
    	//We should get one message 
		ndexServiceMock.reset();
		chargeReferralServiceMock.reset();
		chargeReferralReportingServiceMock.reset();
		arrestReportingBrokerServiceMock.reset();
		notificationBrokerServiceMock.reset();

		//Resend but ndex and charge referral will not get a message
		ndexServiceMock.expectedMessageCount(0);
		chargeReferralServiceMock.expectedMessageCount(0);
		chargeReferralReportingServiceMock.expectedMessageCount(0);
		
		//We don't set duplicate arrests or notifications so these get filtered
		arrestReportingBrokerServiceMock.expectedMessageCount(0);
		notificationBrokerServiceMock.expectedMessageCount(0);
		

    	//Create a new exchange
    	Exchange senderExchangeNotSendToNDEx = new DefaultExchange(context);
					    	
    	//Test the entire web service route by sending through an Incident Report
		doc = createDocument();
		soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchangeNotSendToNDEx.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the incident report file from the file system, this submitter is not an approved submitter
	    inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReportNotSendToNDex.xml");
	    inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchangeNotSendToNDEx.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:IncidentReportingServiceEndpoint", senderExchangeNotSendToNDEx);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);
		
		ndexServiceMock.assertIsSatisfied();
		chargeReferralServiceMock.assertIsSatisfied();
		chargeReferralReportingServiceMock.assertIsSatisfied();
		arrestReportingBrokerServiceMock.assertIsSatisfied();
		notificationBrokerServiceMock.assertIsSatisfied();		
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
