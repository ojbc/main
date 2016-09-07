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
package org.ojbc.bundles.intermediaries.personhealthsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
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
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/properties-context.xml"})
public class CamelContextTest {
		
	public static final String CXF_OPERATION_NAME = 
			"SubmitPersonHealthInformationSearchResults";
	
	public static final String CXF_OPERATION_NAMESPACE = 
			"http://ojbc.org/Services/WSDL/PersonHealthInformationSearchResultsService/1.0";
	
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;	

    
    @EndpointInject(uri = "mock:cxf:bean:personHealthSearchAdapterRequestService")
    protected MockEndpoint personHealthRequestAdapterMockEndpoint;
    
    @EndpointInject(uri = "mock:personHealthResponseMock")
    protected MockEndpoint personHealthResponseMockEndpoint;    
            
    @EndpointInject(uri = "mock:personHealthClientResponseMock")
    protected MockEndpoint personHealthServiceClientResultsMockEndpoint;
    
    @Test
    public void testApplicationStartup() {
    	assertTrue(true);
    }	
    
	@Before
	public void setUp() throws Exception {
		
    	context.getRouteDefinition("personHealthRequest_webservice_Route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:personHealthReqRouteMockEntry");
    	    	
    	    	mockEndpointsAndSkip("personHealthSearchAdapterRequestEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("personHealthResultsHandler_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {

    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:personHealthResponseRouteMockEntry");    	    	
    	    	
    	    	interceptSendToEndpoint("direct:aggregatePersonHealthResponse")
    	    		.to("mock:personHealthClientResponseMock").stop();    	
    	    }              
    	});
    	
    	context.getRouteDefinition("personHealthResultsHandlerNoWsSecurity_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {

    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:personHealthResponseRouteMockEntryNoWsSecurity");    	    	
    	    	
    	    	interceptSendToEndpoint("direct:aggregatePersonHealthResponse")
    	    		.to("mock:personHealthClientResponseMock").stop();    	
    	    }              
    	});
    	
    	context.start();
	}  
	
	
    @Test
    public void testPersonHealthSearchRequest() throws Exception {
        	
    	//We should get one message
    	personHealthRequestAdapterMockEndpoint.reset();
    	personHealthRequestAdapterMockEndpoint.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("12345");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = OJBCXMLUtils.createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(createSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(createSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", 
				"https://localhost:8443/OJB/connector/PersonHealthSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE);
	    
	    File inputFile = new File("src/test/resources/xmlInstances/PersonHealthInformationSearchRequest-PII.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:personHealthReqRouteMockEntry", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null){	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
//		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		personHealthRequestAdapterMockEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = personHealthRequestAdapterMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitPersonHealthInformationSearchRequest", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/PersonHealthInformationSearchRequestService/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(actualResponseDocument);
    }	
    
    @Test
    public void testPersonHealthSearchResultsNoWsSecurity() throws Exception {
        		    	    	
    	//We should get one message
    	personHealthServiceClientResultsMockEndpoint.reset();
        personHealthServiceClientResultsMockEndpoint.expectedMessageCount(1);

    	setupResponseServiceTest("direct:personHealthResponseRouteMockEntryNoWsSecurity");
		
		//Assert that the mock endpoint expectations are satisfied
		personHealthServiceClientResultsMockEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = personHealthServiceClientResultsMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitPersonHealthInformationSearchResults", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/PersonHealthInformationSearchResultsService/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(actualResponseDocument);
    }
    
    @Test
    public void testPersonHealthSearchResults() throws Exception {
        		    	    	
    	//We should get one message
    	personHealthServiceClientResultsMockEndpoint.reset();
        personHealthServiceClientResultsMockEndpoint.expectedMessageCount(1);

    	setupResponseServiceTest("direct:personHealthResponseRouteMockEntry");
		
		//Assert that the mock endpoint expectations are satisfied
		personHealthServiceClientResultsMockEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = personHealthServiceClientResultsMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitPersonHealthInformationSearchResults", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/PersonHealthInformationSearchResultsService/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(actualResponseDocument);
    }

	private void setupResponseServiceTest(String directRouteEndpoint) throws Exception, IOException {
		//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("12345");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = OJBCXMLUtils.createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(createSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(createSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", 
				"https://localhost:8443/OJB/connector/PersonHealthSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE);
	    
	    File inputFile = new File("src/test/resources/xmlInstances/PersonHealthInformationSearchResults-full-detail.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send(directRouteEndpoint, senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null){	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
//		Thread.sleep(3000);
	}	    
    
    
	private SoapHeader createSoapHeader(Document doc, String namespace, String localName, String value) {
		
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		
		return soapHeader;
	}	
}

