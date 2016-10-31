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
package org.ojbc.intermediaries.sn.topic.arrest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.exception.InvalidEmailAddressesException;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
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

	    Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("Test Person"));
		
		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));
		
		assertThat(sub.getSubjectIdentifiers().size(), is(5));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));
	}
	
	@Test
	public void testSubscriptionWithSidNameDOB() throws Exception {

	    Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest_arrestWithSIDNameDOB.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("John Doe"));
		
		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));
		
		assertThat(sub.getSubjectIdentifiers().size(), is(5));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("John"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Doe"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1990-10-20"));
	}

	/**
	 * This method will also test the email address pattern processor failure
	 * 
	 * @throws Exception
	 */
	@Test(expected=InvalidEmailAddressesException.class)
	public void testEmailPatternFailure() throws Exception {
		
		Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
        
        String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(test.com)";
		
		@SuppressWarnings("unused")
        ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
	}
	
	@Test
	public void testWithStartAndEndDate() throws Exception {

	    Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithStartAndEndDate.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
        
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertNull(sub.getSubscriptionSystemId());
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("Test Person"));

		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));

		assertThat(sub.getSubjectIdentifiers().size(), is(5));
		assertThat(sub.getStartDateString(), is("2014-04-14"));
		assertThat(sub.getEndDateString(), is("2014-04-21"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));

		
	}

	@Test(expected=Exception.class)
	public void testWithEndDateBeforeStartDate() throws Exception {
		
		Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithEndDateBeforeStartDate.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
        
        @SuppressWarnings("unused")
        ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
	}
	
	@Test
	public void testWithSubscriptionSystemID() throws Exception {
	    
	    Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithSubscriptionSystemID.xml");
		
	    Message message = new DefaultMessage();
	    message.setBody(messageDocument);
	    
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertThat(sub.getSubscriptionSystemId(), is("60109"));
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("Test Person"));
		
		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));
		
		assertThat(sub.getSubjectIdentifiers().size(), is(5));
		assertThat(sub.getStartDateString(), is("2014-04-14"));
		assertThat(sub.getEndDateString(), is("2014-04-21"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));

		
	}
	
	@Test
	public void testWithDuplicateEmails() throws Exception {
		
		Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest_duplicateEmails.xml");
        
        Message message = new DefaultMessage();
        message.setBody(messageDocument);
		
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, null);
		
		assertNull(sub.getSubscriptionSystemId());
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("Test Person"));

		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));

		assertThat(sub.getSubjectIdentifiers().size(), is(5));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));


		
	}
	
	private Document getMessageBody(String filePath) throws Exception {
		File inputFile = new File(filePath);

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;
	}
}
