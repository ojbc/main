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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.annotation.Resource;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml" })
public class TestCriminalHistoryConsolidationService {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( TestCriminalHistoryConsolidationService.class );

	private static final Object CXF_OPERATION_NAME_NOTIFICATION = "ReportCriminalHistoryIdentifierUpdate";
	private static final Object CXF_OPERATION_NAMESPACE_NOTIFICATION = "http://ojbc.org/Services/WSDL/CriminalHistoryUpdateReportingService/1.0";
	
	private static final String UCN_BASE_QUERY="select count(*) as rowcount from rapback_datastore.identification_subject where ucn = 'UCN_PLACEHOLDER'";
	private static final String SID_BASE_QUERY="select count(*) as rowcount from rapback_datastore.identification_subject where civil_sid = 'SID_PLACEHOLDER' or criminal_Sid = 'SID_PLACEHOLDER'";
	private static final String FBI_BASE_QUERY="select count(*) as rowcount from rapback_datastore.fbi_rap_back_subscription where ucn = 'UCN_PLACEHOLDER'";
	
	
    @Resource  
    private DataSource dataSource;  

    @Resource  
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;  

    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerServiceEndpointMock;
    
	@BeforeEach
	public void setUp() throws Exception {
			
		//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		AdviceWith.adviceWith(context, "criminalHistoryUpdateReportingServiceRoute", route -> {
			route.replaceFromWith("direct:criminalHistoryConsolidationRequest");
		});
		
    	AdviceWith.adviceWith(context, "directUpdateSubscriptionsRoute", route -> {
    		route.weaveByToUri("notificationBrokerServiceEndpoint").replace().to("mock:cxf:bean:notificationBrokerService");
    	});

    	context.start();
	}	

	@Test
	public void testCriminalConsolidationService() throws Exception
	{
		notificationBrokerServiceEndpointMock.reset();
		notificationBrokerServiceEndpointMock.expectedMessageCount(2);
		
		//Initial database setup
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123458"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123457"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222201"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222201"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		
		Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "A123457");
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(1, subscriptions.size());

    	//Create a new exchange
    	Exchange senderExchange = createSenderExchangeNotification();
    	
	    //Read the criminal history update file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-Consolidation-Report.xml");
	    String inputStr = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
	    
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
		
		//Database after consolidation
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123457"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123458"));
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		//On a state consolidation, we don't update the UCN, just notify
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222201"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222201"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		
		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "A123457");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(0, subscriptions.size());

		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "A123458");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(2, subscriptions.size());

		
	    //Read the criminal history update file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-IdentifierUpdate-Report.xml");
	    inputStr = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:criminalHistoryConsolidationRequest", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Database after identifier update
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123458"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "XX123123"));
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "FBI123123"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "FBI123123"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));		
		
		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "A123458");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(0, subscriptions.size());

		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "XX123123");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(2, subscriptions.size());
		
		
	    //Read the criminal history expungement file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-Expungement-Report.xml");
	    inputStr = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:criminalHistoryConsolidationRequest", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Database after expungement, no changes expected
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "A123458"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(SID_BASE_QUERY, "SID_PLACEHOLDER", "XX123123"));
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(UCN_BASE_QUERY, "UCN_PLACEHOLDER", "FBI123123"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "9222202"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(StringUtils.replace(FBI_BASE_QUERY, "UCN_PLACEHOLDER", "FBI123123"));
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));			
		
		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "A123458");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(0, subscriptions.size());

		subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, "XX123123");
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
		assertEquals(2, subscriptions.size());
		
		notificationBrokerServiceEndpointMock.assertIsSatisfied();

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
