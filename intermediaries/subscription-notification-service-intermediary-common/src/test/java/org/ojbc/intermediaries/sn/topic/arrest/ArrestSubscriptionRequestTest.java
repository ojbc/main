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
package org.ojbc.intermediaries.sn.topic.arrest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.exception.InvalidEmailAddressesException;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtilsTest;
import org.w3c.dom.Document;

public class ArrestSubscriptionRequestTest {

	Map<String, String> namespaceUris;
	
	@Before
	public void setup() {
		namespaceUris = new HashMap<String, String>();
		namespaceUris.put("wsnb2", "http://docs.oasis-open.org/wsn/b-2");
		namespaceUris.put("sm", "http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0");
		namespaceUris.put("smext", "http://ojbc.org/IEPD/Extensions/Subscription/1.0");
		namespaceUris.put("nc20", "http://niem.gov/niem/niem-core/2.0");
		namespaceUris.put("jxdm41", "http://niem.gov/niem/domains/jxdm/4.1");
	}

	/**
	 * This method will also test the email address pattern processor
	 * 
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {

	    Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        
        message.setHeader("subscriptionOwner", "someone");
        message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
        
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "Test Person");
		
		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertTrue(sub.getEmailAddresses().contains("po6@localhost"));
		
		assertTrue(sub.getSubjectIdentifiers().size()== 5);
		assertEquals(sub.getSubscriptionOwner(), "someone");
		assertEquals(sub.getSubscriptionOwnerEmailAddress(), "email@local.gov");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));
	}
	
	@Test
	public void testSubscriptionWithSidNameDOB() throws Exception {

	    Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest_arrestWithSIDNameDOB.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "John Doe");
		
		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertEquals(sub.getEmailAddresses().contains("po6@localhost"), true);
		
		assertEquals(sub.getSubjectIdentifiers().size(), 5);
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "John");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Doe");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1990-10-20");
	}

	/**
	 * This method will also test the email address pattern processor failure
	 * 
	 * @throws Exception
	 */
	@Test(expected=InvalidEmailAddressesException.class)
	public void testEmailPatternFailure() throws Exception {
		
		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        message.setBody(messageDocument);
        
        String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(test.com)";
		
		@SuppressWarnings("unused")
        ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
	}
	
	@Test
	public void testWithStartAndEndDate() throws Exception {

	    Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithStartAndEndDate.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        message.setBody(messageDocument);
        
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertNull(sub.getSubscriptionSystemId());
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "Test Person");

		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertEquals(sub.getEmailAddresses().contains("po6@localhost"), true);

		assertEquals(sub.getSubjectIdentifiers().size(), 5);
		assertEquals(sub.getStartDateString(), "2014-04-14");
		assertEquals(sub.getEndDateString(), "2014-04-21");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));

		
	}

	@Test(expected=Exception.class)
	public void testWithEndDateBeforeStartDate() throws Exception {
		
		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithEndDateBeforeStartDate.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        message.setBody(messageDocument);
        
        @SuppressWarnings("unused")
        ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
	}
	
	@Test
	public void testWithSubscriptionSystemID() throws Exception {
	    
	    Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithSubscriptionSystemID.xml");
		
        Message message = new DefaultMessage(new DefaultCamelContext() );
	    message.setBody(messageDocument);
	    
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertEquals(sub.getSubscriptionSystemId(), "60109");
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "Test Person");
		
		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertEquals(sub.getEmailAddresses().contains("po6@localhost"), true);
		
		assertEquals(sub.getSubjectIdentifiers().size(), 5);
		assertEquals(sub.getStartDateString(), "2014-04-14");
		assertEquals(sub.getEndDateString(), "2014-04-21");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));

		
	}
	
	@Test
	public void testWithDuplicateEmails() throws Exception {
		
		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest_duplicateEmails.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext() );
        message.setBody(messageDocument);
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertNull(sub.getSubscriptionSystemId());
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "Test Person");

		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertEquals(sub.getEmailAddresses().contains("po6@localhost"), true);

		assertEquals(sub.getSubjectIdentifiers().size(), 5);
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));


		
	}
	

}
