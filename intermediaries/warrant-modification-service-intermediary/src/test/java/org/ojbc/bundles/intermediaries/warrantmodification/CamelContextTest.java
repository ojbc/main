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
package org.ojbc.bundles.intermediaries.warrantmodification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

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
import org.apache.camel.test.spring.UseAdviceWith;
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
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/extensible-beans.xml",	
		"classpath:META-INF/spring/jetty-server.xml",
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/properties-context.xml"})
public class CamelContextTest {
	
	public static final String CXF_OPERATION_NAME = "SubmitWarrantModificationRequest";
	
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/WarrantModificationRequestService/1.0";
	
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:warrantModificationAdapterRequestService")
    protected MockEndpoint warrantModRequestMockEndpoint;
    
    @EndpointInject(uri = "mock:warrantModResponseMock")
    protected MockEndpoint warrantModResponseMockEndpoint;
	
    
    @Test
    public void testApplicationStartup() {
    	assertTrue(true);
    }	
    
	@Before
	public void setUp() throws Exception {
		
    	context.getRouteDefinition("warrantModRequest_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:warrantModReqRouteMockEntry");
    	    	
    	    	mockEndpointsAndSkip("warrantModAdapterRequestEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("warrantModResultsHandler_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {

    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:warrantModResponseRouteMockEntry");    	    	
    	    	
    	    	interceptSendToEndpoint("direct:aggregateWarrantModResponse").to("mock:warrantModResponseMock").stop();    	
    	    }              
    	});
    	
    	context.start();
	}
    
	
    @Test
    public void testWarrantModRequest() throws Exception {
        	
    	//We should get one message
    	warrantModRequestMockEndpoint.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("12345");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = OJBCXMLUtils.createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB//intermediary/WarrantModificationResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE);
	    
	    File inputFile = new File("src/test/resources/xmlInstances/warrantModResults/warrantModRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:warrantModReqRouteMockEntry", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null){	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		warrantModRequestMockEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = warrantModRequestMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitWarrantModificationRequest", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/WarrantModificationRequestService/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(actualResponseDocument);
    }
    
    
    @Ignore
    public void testWarrantModResponseHandler() throws Exception {
    	
    	warrantModResponseMockEndpoint.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/warrantModResults/warrantModResults.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:warrantModResponseRouteMockEntry", senderExchange);
    	
		warrantModResponseMockEndpoint.assertIsSatisfied();		
    }	
    

	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		
		return soapHeader;
	}	
}

