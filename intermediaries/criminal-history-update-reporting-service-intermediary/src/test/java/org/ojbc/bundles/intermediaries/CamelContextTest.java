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
package org.ojbc.bundles.intermediaries;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.crimhistoryupdate.CriminalHistoryUpdateReportingServiceApplication;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import jakarta.annotation.Resource;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=CriminalHistoryUpdateReportingServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class CamelContextTest {

    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:cxf:bean:notificationBrokerServiceEndpoint")
    protected MockEndpoint notificationBrokerMockEndpoint;

    @EndpointInject(value = "mock:log:org.ojbc.intermediaries.crimhistoryupdate")
    protected MockEndpoint loggingEndpoint;

	public static final String CXF_OPERATION_NAME_CRIMINAL_HISTORY = "ReportCycleTrackingIdentifierAssignment";
	public static final String CXF_OPERATION_NAMESPACE_CRIMINAL_HISTORY = "http://ojbc.org/Services/WSDL/CriminalHistoryUpdateReportingService/1.0";

	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
    @Test
    public void testApplicationStartup()
    {
    	assertTrue(true);
    }	
    
    @BeforeEach
    public void setUp() throws Exception {
        AdviceWith.adviceWith(context, "CriminalHistoryUpdateReportingServiceHandlerRoute", route -> {
            route.replaceFromWith("direct:criminalHistoryUpdatedReportingService");
        });
        
        AdviceWith.adviceWith(context, "CriminalHistoryUpdateReportingServiceDirectRoute", route -> {
            route.weaveByToUri("direct:memberSpecificRoutes").replace().to("mock:log:org.ojbc.intermediaries.crimhistoryupdate");
        });

        AdviceWith.adviceWith(context, "callNotificationBrokerRoute", route -> {
            route.weaveByToUri("notificationBrokerServiceEndpoint").replace().to("mock:cxf:bean:notificationBrokerServiceEndpoint");
        });

        context.start();
    }
    
    @AfterEach
    public void after() throws Exception {
        context.stop();
    }
	
    @Test
    public void testNotificationBroker() throws Exception {
    	notificationBrokerMockEndpoint.expectedMessageCount(1);
    	loggingEndpoint.expectedMessageCount(1);
    	
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
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_CRIMINAL_HISTORY);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_CRIMINAL_HISTORY);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/cycleTrackingIdentifierAssignmentReport/Cycle-Tracking-Identifier-Assignment-Report.xml");
	    String inputStr = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
	    log.debug("Before sending message");
	    log.debug("Camel context started? " + context.isStarted());

	    Exchange returnExchange = template.send("direct:criminalHistoryUpdatedReportingService", senderExchange);

	    log.debug("After sending message");
	    log.debug("Camel context started? " + context.isStarted());
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		notificationBrokerMockEndpoint.assertIsSatisfied();
		loggingEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = notificationBrokerMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("Notify", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://docs.oasis-open.org/wsn/brw-2", opNamespace);

		Document returnDocumentNotificationBroker = ex.getIn().getBody(Document.class);

		//Do some very basic assertions to assure the message is transformed.
		//The XSLT test does a more complete examination of the transformation.
		Node notifyNode = XmlUtils.xPathNodeSearch(returnDocumentNotificationBroker, "/b-2:Notify");
		Node notifyMesssageNode = XmlUtils.xPathNodeSearch(notifyNode, "b-2:NotificationMessage");
		
		Node messageNode = XmlUtils.xPathNodeSearch(notifyMesssageNode, "b-2:Message");
		assertNotNull(messageNode);

		//Get the first exchange (the only one) to the logger
		//This is what would be sent to the implementation specific route
		Exchange exImplementationSpecific = loggingEndpoint.getExchanges().get(0);

		Document returnDocumentImplementationSpecific = exImplementationSpecific.getIn().getBody(Document.class);

		//Make sure the root node here is the message to the original exchange
		Node rootNode = XmlUtils.xPathNodeSearch(returnDocumentImplementationSpecific, "/crimhistory-update-exch:CycleTrackingIdentifierAssignmentReport");
		assertNotNull(rootNode);

		//XmlUtils.printNode(returnDocumentImplementationSpecific);
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
