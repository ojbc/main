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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CamelContextSecureSubscriptionTest extends AbstractSubscriptionNotificationTest {

	public static final String CXF_OPERATION_NAME_SUBSCRIBE = "Subscribe";
	public static final String CXF_OPERATION_NAMESPACE_SUBSCRIBE = "http://www.ojbc.org/SubscribeNotify/SecureNotificationBroker";

	public static final String CXF_OPERATION_NAME_UNSUBSCRIBE = "Unsubscribe";
	public static final String CXF_OPERATION_NAMESPACE_UNSUBSCRIBE = "http://www.ojbc.org/SubscribeNotify/SubscriptionManager";

	public static final String CXF_OPERATION_NAME_VALIDATE = "Validate";
	public static final String CXF_OPERATION_NAMESPACE_VALIDATE = "http://www.ojbc.org/SubscribeNotify/SubscriptionManager";

	private static final Log log = LogFactory.getLog( CamelContextSecureSubscriptionTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:direct:processSubscription")
    protected MockEndpoint subscriptionProcessorMock;

    @EndpointInject(uri = "mock:direct:processUnsubscription")
    protected MockEndpoint unsubscriptionProcessorMock;
    
    @EndpointInject(uri = "mock:faultProcessorMock")
    protected MockEndpoint subscriptionManagerServiceFaultMock;

    @EndpointInject(uri = "mock:subscriptionValidationMock")
    protected MockEndpoint subscriptionValidationMock;

    @Resource  
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;  
    
	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("subscriptionSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:subscriptionSecureEndpoint");
    	    	
    	    	interceptSendToEndpoint("bean:genericFaultProcessor?method=createFault").to("mock:faultProcessorMock");
    	    }              
    	});
    	
    	context.getRouteDefinition("fbiEbtsSubscriptionSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    
    	    	
    	    	mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
    	    }              
    	});    	    
    	
    	
    	context.getRouteDefinition("callFbiEbtsModify_Route").adviceWith(context, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionManagerService*");
			}
		});
    	
    	
    	
    	context.getRouteDefinition("subscriptionSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	interceptSendToEndpoint("direct:processSubscription").to("mock:direct:processSubscription");
    	    }              
    	});

    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("subscriptionManagerServiceSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:unsubscriptionSecureEndpoint");
    	    	interceptSendToEndpoint("bean:subscriptionValidationMessageProcessor?method=validateSubscription").to("mock:subscriptionValidationMock");
    	    	interceptSendToEndpoint("direct:processUnsubscription").to("mock:direct:processUnsubscription").stop();
    	    }	
    	});

		context.start();		
		
	}
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}
	
	//Run these tests first. Dirties context wasn't resetting database to original state.
	//This test will produce a fault.
    @Test
    public void testSubscriptionInvalidEmail() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = createSenderExchangeSubscription();
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    
	    //Replace email with invalid email
	    inputStr = inputStr.replace("po6@localhost", "po6@invalidemaildomain.com");
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://demostate.gov/SystemNames/1.0}SystemA", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get no results due to invalid email
		assertEquals(0, subscriptions.size());
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionManagerServiceFaultMock.assertIsSatisfied();

    }

	//Run these tests first. Dirties context wasn't resetting database to original state.
	//This test will produce a fault.
    @Test
    public void testSubscriptionEndDateBeforeStartDate() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = createSenderExchangeSubscription();
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.minusDays(1).toString("yyyy-MM-dd"));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://demostate.gov/SystemNames/1.0}SystemA", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get no results due to invalid email
		assertEquals(0, subscriptions.size());
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionManagerServiceFaultMock.assertIsSatisfied();

    }

	
    /**
     * We will first add a new subscription which will be successful.
     * That will be followed by attempts to modify the subscription in
     * ways that are not allowed including:
     * 
     * -End Date before start date
     * -Removing all email addresses
     * 
     * We will then attempt to add 
     * 
     * -a single email 
     * -multilple emails 
     * 
     * Those edits will succeed.
     * 
     * @throws Exception
     */
    @Test
    public void testSubscriptionSingleEmailDefaultDateFollowedByTestForEdits() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();
    	
    	subscriptionProcessorMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = createSenderExchangeSubscription();
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		Subscription subscriptionOfInterest = subscriptions.get(0);
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptionOfInterest.getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
		
		//Verify Email Address
		assertEquals(1,subscriptionOfInterest.getEmailAddressesToNotify().size());
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po6@localhost"));
		
		//Verify start and end dates
		assertEquals(today.toString("yyyy-MM-dd"), subscriptionOfInterest.getStartDate().toString("yyyy-MM-dd"));
		assertEquals(today.plusYears(1).toString("yyyy-MM-dd"), subscriptionOfInterest.getEndDate().toString("yyyy-MM-dd"));
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionProcessorMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionProcessorMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		String subscriptionOwner = (String)exSamlToken.getIn().getHeader("subscriptionOwner");
		log.info("Subscription Owner is: " + subscriptionOwner);
		assertEquals("OJBC:IDP:OJBC:USER:admin", subscriptionOwner);
		
		///////////////////////////////////////////////////////
		///// Try to edit to put start date before end date ///
		///////////////////////////////////////////////////////
		
		long subscriptionID = subscriptionOfInterest.getId();
		
		log.debug("This is the subscription ID of the subscription just added: " + subscriptionID);
		
    	//Create a new exchange
    	senderExchange = createSenderExchangeSubscription();

    	subscriptionManagerServiceFaultMock.reset();
    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);

	    //Read the subscription request file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/editSubscribeRequestTemplate.xml");
	    inputStr = FileUtils.readFileToString(inputFile);

	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.minusDays(1).toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("SUBSCRIPTION_ID", String.valueOf(subscriptionID));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);

		//We will get a fault here because start date is greater than end date
    	subscriptionManagerServiceFaultMock.assertIsSatisfied();
		
		///////////////////////////////////////////
		///// Try to remove all email addresses ///
		///////////////////////////////////////////

    	//Create a new exchange
    	senderExchange = createSenderExchangeSubscription();

    	subscriptionManagerServiceFaultMock.reset();
    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);

	    //Read the subscription request file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/editSubscribeRequestTemplate.xml");
	    inputStr = FileUtils.readFileToString(inputFile);

	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("SUBSCRIPTION_ID", String.valueOf(subscriptionID));

	    inputStr = inputStr.replace("<nc20:ContactEmailID>po6@localhost</nc20:ContactEmailID>", "");
	    

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);

		//We will get a fault here because start date is greater than end date
    	subscriptionManagerServiceFaultMock.assertIsSatisfied();

		/////////////////////////////////////////////
		///// Try to add a single email addresses ///
		/////////////////////////////////////////////

    	//Create a new exchange
    	senderExchange = createSenderExchangeSubscription();

    	subscriptionProcessorMock.reset();
    	subscriptionProcessorMock.expectedMessageCount(1);

	    //Read the subscription request file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/editSubscribeRequestTemplate.xml");
	    inputStr = FileUtils.readFileToString(inputFile);

	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("SUBSCRIPTION_ID", String.valueOf(subscriptionID));

	    //Update template to add email address
	    inputStr = inputStr.replace("<nc20:ContactEmailID>po6@localhost</nc20:ContactEmailID>", "<nc20:ContactEmailID>po6@localhost</nc20:ContactEmailID><nc20:ContactEmailID>po8@localhost</nc20:ContactEmailID>");
	    

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);

		//We will get a single message here
		subscriptionProcessorMock.assertIsSatisfied();
		
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		subscriptionOfInterest = subscriptions.get(0);
		
		//Verify Email Address
		assertEquals(2,subscriptionOfInterest.getEmailAddressesToNotify().size());
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po6@localhost"));
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po8@localhost"));

		/////////////////////////////////////////////
		///// Try to add multiple email addresses ///
		/////////////////////////////////////////////

    	//Create a new exchange
    	senderExchange = createSenderExchangeSubscription();

    	subscriptionProcessorMock.reset();
    	subscriptionProcessorMock.expectedMessageCount(1);

	    //Read the subscription request file from the file system
	    inputFile = new File("src/test/resources/xmlInstances/editSubscribeRequestTemplate.xml");
	    inputStr = FileUtils.readFileToString(inputFile);

	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("SUBSCRIPTION_ID", String.valueOf(subscriptionID));

	    //Update template to add email address
	    inputStr = inputStr.replace("<nc20:ContactEmailID>po6@localhost</nc20:ContactEmailID>", "<nc20:ContactEmailID>po6@localhost</nc20:ContactEmailID><nc20:ContactEmailID>po7@localhost</nc20:ContactEmailID><nc20:ContactEmailID>po8@localhost</nc20:ContactEmailID>");
	    

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);

		//We will get a single message here
		subscriptionProcessorMock.assertIsSatisfied();
		
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		subscriptionOfInterest = subscriptions.get(0);
		
		//Verify Email Address
		assertEquals(3,subscriptionOfInterest.getEmailAddressesToNotify().size());
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po6@localhost"));
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po7@localhost"));
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po8@localhost"));
    }
    
    
    @Test
    public void testSubscriptionSingleEmailCustomDate() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionProcessorMock.expectedMessageCount(1);
    	
    	Exchange senderExchange = createSenderExchangeSubscription();
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusMonths(12).toString("yyyy-MM-dd"));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		Subscription subscriptionOfInterest = subscriptions.get(0);
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptionOfInterest.getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
		
		//Verify Email Address
		assertEquals(1,subscriptionOfInterest.getEmailAddressesToNotify().size());
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po6@localhost"));
		
		//Verify start and end dates
		assertEquals(today.toString("yyyy-MM-dd"), subscriptionOfInterest.getStartDate().toString("yyyy-MM-dd"));
		assertEquals(today.plusMonths(12).toString("yyyy-MM-dd"), subscriptionOfInterest.getEndDate().toString("yyyy-MM-dd"));
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionProcessorMock.assertIsSatisfied();		

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionProcessorMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		String subscriptionOwner = (String)exSamlToken.getIn().getHeader("subscriptionOwner");
		log.info("Subscription Owner is: " + subscriptionOwner);
		assertEquals("OJBC:IDP:OJBC:USER:admin", subscriptionOwner);
		
    }

    
    @Test
    public void testSubscriptionSingleMultipleEmails() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();
    	
    	subscriptionProcessorMock.expectedMessageCount(1);
    	    	    	    	
    	Exchange senderExchange = createSenderExchangeSubscription();
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestMultipleEmailsTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", "OJBC:IDP:OJBC:USER:admin", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		Subscription subscriptionOfInterest = subscriptions.get(0);
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptionOfInterest.getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
		
		//Verify Email Address
		assertEquals(2,subscriptionOfInterest.getEmailAddressesToNotify().size());
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po6@localhost"));
		assertTrue(subscriptionOfInterest.getEmailAddressesToNotify().contains("po7@localhost"));
		
		//Verify start and end dates
		assertEquals(today.toString("yyyy-MM-dd"), subscriptionOfInterest.getStartDate().toString("yyyy-MM-dd"));
		assertEquals(today.plusYears(1).toString("yyyy-MM-dd"), subscriptionOfInterest.getEndDate().toString("yyyy-MM-dd"));
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionProcessorMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionProcessorMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		String subscriptionOwner = (String)exSamlToken.getIn().getHeader("subscriptionOwner");
		log.info("Subscription Owner is: " + subscriptionOwner);
		assertEquals("OJBC:IDP:OJBC:USER:admin", subscriptionOwner);

    }
    
    @Test
    public void testSubscriptionWithoutSAMLToken() throws Exception {
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);
    	
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
		
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_SUBSCRIBE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SUBSCRIBE);
    	
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/subscribeRequestTemplate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    DateTime today = new DateTime();
	    inputStr = inputStr.replace("START_DATE_TOKEN", today.toString("yyyy-MM-dd"));
	    inputStr = inputStr.replace("END_DATE_TOKEN", today.plusYears(1).toString("yyyy-MM-dd"));
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		subscriptionManagerServiceFaultMock.assertIsSatisfied();
		
		Exchange ex = subscriptionManagerServiceFaultMock.getReceivedExchanges().get(0);
		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);
		
        assertNotNull(returnDocument);
    }
    
    @Test
    public void testUnsubscription() throws Exception {
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	unsubscriptionProcessorMock.expectedMessageCount(1);
    	
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
		
		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_UNSUBSCRIBE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SUBSCRIBE);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/unSubscribeRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:unsubscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		unsubscriptionProcessorMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = unsubscriptionProcessorMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		String subscriptionOwner = (String)exSamlToken.getIn().getHeader("subscriptionOwner");
		log.info("Subscription Owner is: " + subscriptionOwner);
		assertEquals("OJBC:IDP:OJBC:USER:admin", subscriptionOwner);

    }    

    @Test
    public void testUnsubscriptionWithoutSAMLToken() throws Exception {
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionManagerServiceFaultMock.expectedMessageCount(1);
    	
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
		
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_UNSUBSCRIBE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SUBSCRIBE);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/unSubscribeRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:unsubscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		subscriptionManagerServiceFaultMock.assertIsSatisfied();
		
		Exchange ex = subscriptionManagerServiceFaultMock.getReceivedExchanges().get(0);
		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);
		
        assertNotNull(returnDocument);
        
		
    }    
    
    @Test
    public void testSubscriptionValidation() throws Exception {
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionValidationMock.expectedMessageCount(1);
    	
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
		
		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_VALIDATE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_VALIDATE);
    	
	    //Read the subscription validation request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/Validation_Request.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:unsubscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionValidationMock.assertIsSatisfied();
		
		
		Exchange ex = subscriptionValidationMock.getReceivedExchanges().get(0);
		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);

		assertEquals("62720", XmlUtils.xPathStringSearch(returnDocument, "/b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID"));

    }    

    @Test
    public void testSubscriptionValidationInvalidMessage() throws Exception {
    
    	subscriptionProcessorMock.reset();
    	unsubscriptionProcessorMock.reset();
    	subscriptionValidationMock.reset();
    	subscriptionManagerServiceFaultMock.reset();

    	subscriptionValidationMock.expectedMessageCount(1);
    	
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
		
		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_VALIDATE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_VALIDATE);
    	
	    //Read the subscription validation request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/Validation_Request_Invalid.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:unsubscriptionSecureEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionValidationMock.assertIsSatisfied();
		
		//TODO: We need to figure out how to intercept an endpoint AFTER it has processed an exchange
		//In this case we intercept as follows:
		//interceptSendToEndpoint("bean:subscriptionValidationMessageProcessor?method=validateSubscription").to("mock:subscriptionValidationMock");
		//However, we want to inspect the value of the exchange AFTER the method is called to make sure we get the proper fault message
		//See this: https://issues.apache.org/jira/browse/CAMEL-6901
		//Exchange ex = subscriptionValidationMock.getReceivedExchanges().get(0);
		//Document returnDocument = ex.getIn().getBody(Document.class);

    }    

	private Exchange createSenderExchangeSubscription() throws Exception {
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
		
		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_SUBSCRIBE);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SUBSCRIBE);
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
	
	private SAMLTokenPrincipal createSAMLToken() throws Exception {
		
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "OJBC:IDP:OJBC");
		customAttributes.put(SamlAttribute.FederationId, "OJBC:IDP:OJBC:USER:admin");
		customAttributes.put(SamlAttribute.IdentityProviderId, "OJBC");
		
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth",
				SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		return principal;
	}

}	
