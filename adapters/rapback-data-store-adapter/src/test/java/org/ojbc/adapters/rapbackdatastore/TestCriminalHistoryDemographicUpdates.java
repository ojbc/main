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

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class TestCriminalHistoryDemographicUpdates {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( TestCriminalHistoryDemographicUpdates.class );

	private static final Object CXF_OPERATION_NAME_NOTIFICATION = "ReportCriminalHistoryDemographicsUpdate";
	private static final Object CXF_OPERATION_NAMESPACE_NOTIFICATION = "http://ojbc.org/Services/WSDL/CriminalHistoryUpdateReportingService/1.0";
	
    @Resource  
    private DataSource dataSource;  

    @Resource  
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;  

    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerServiceEndpointMock;
    
	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("criminalHistoryUpdateReportingServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:criminalHistoryConsolidationRequest");

    	    }              
    	});

    	context.getRouteDefinition("directUpdateSubscriptionsRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	interceptSendToEndpoint("cxf:bean:notificationBrokerService*").skipSendToOriginalEndpoint().to("mock:cxf:bean:notificationBrokerService");

    	    }              
    	});

    	
    	context.start();
	}	

	@Test
	public void testCriminalHistoryDemographicUpdates() throws Exception
	{
		notificationBrokerServiceEndpointMock.reset();
		notificationBrokerServiceEndpointMock.expectedMessageCount(1);
		
		Connection conn = dataSource.getConnection();

		//Create a new exchange
    	Exchange senderExchange = createSenderExchangeNotification();
    	
	    //Read the criminal history update file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/criminalHistoryDemographicsUpdateReport/CriminalHistory-DemographicsUpdate-Report.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:criminalHistoryConsolidationRequest", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		notificationBrokerServiceEndpointMock.assertIsSatisfied();
		Document body = notificationBrokerServiceEndpointMock.getExchanges().get(0).getIn().getBody(Document.class);

		XmlUtils.printNode(body);
		
		assertEquals("Bart", XmlUtils.xPathStringSearch(body, "/b-2:Subscribe/submsg-exch:SubscriptionMessage[1]/submsg-ext:Subject[1]/nc:PersonName[1]/nc:PersonGivenName[1]"));
		assertEquals("Simpsonsonia", XmlUtils.xPathStringSearch(body, "/b-2:Subscribe/submsg-exch:SubscriptionMessage[1]/submsg-ext:Subject[1]/nc:PersonName[1]/nc:PersonSurName[1]"));
		assertEquals("Bart B Simpsonsonia", XmlUtils.xPathStringSearch(body, "/b-2:Subscribe/submsg-exch:SubscriptionMessage[1]/submsg-ext:Subject[1]/nc:PersonName[1]/nc:PersonFullName[1]"));
		
		assertEquals("1962-10-16", XmlUtils.xPathStringSearch(body, "/b-2:Subscribe/submsg-exch:SubscriptionMessage[1]/submsg-ext:Subject[1]/nc:PersonBirthDate[1]/nc:Date[1]"));
		
	}
	
	private Exchange createSenderExchangeNotification() throws Exception {
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
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_NOTIFICATION);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_NOTIFICATION);
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
