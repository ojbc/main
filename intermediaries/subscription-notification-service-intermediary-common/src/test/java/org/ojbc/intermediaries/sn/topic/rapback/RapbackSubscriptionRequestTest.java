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
package org.ojbc.intermediaries.sn.topic.rapback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.w3c.dom.Document;

public class RapbackSubscriptionRequestTest {

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


	
	@Test
	public void testSubscriptionWithFederalTriggeringEventCode() throws Exception {

	    Document messageDocument = getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequestWithRapbackData.xml");
        
        Message message = new DefaultMessage(new DefaultCamelContext());
        
        message.setHeader("subscriptionOwner", "someone");
        message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
        
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		RapbackSubscriptionRequest sub = new RapbackSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertEquals(sub.getSubscriptionQualifier(), "1234578");
		assertEquals(sub.getSubjectName(), "Test Person");
		
		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertTrue(sub.getEmailAddresses().contains("po6@localhost"));
		
		assertEquals(sub.getSubjectIdentifiers().size(), 6);
		assertEquals(sub.getSubscriptionOwner(), "someone");
		assertEquals(sub.getSubscriptionOwnerEmailAddress(), "email@local.gov");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "1234578");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "Test");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Person");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1972-08-02");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FBI_ID), "123456789");
		assertEquals(sub.getTopic(), "{http://ojbc.org/wsn/topics}:person/rapback");
		
		Map<String, String> subscriptionProperties = sub.getSubscriptionProperties();
		
		assertEquals(subscriptionProperties.size(), 7);
		assertEquals(subscriptionProperties.get(FederalTriggeringEventCode.ARREST.toString()), "ARREST");
		assertEquals(subscriptionProperties.get(FederalTriggeringEventCode.DEATH.toString()), "DEATH");
		assertEquals(subscriptionProperties.get(FederalTriggeringEventCode.NCIC_SOR_ENTRY.toString()), "NCIC-SOR-ENTRY");
		assertEquals(subscriptionProperties.get(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString()), "NCIC-WARRANT-ENTRY");
		assertEquals(subscriptionProperties.get(FederalTriggeringEventCode.DISPOSITION.toString()), "DISPOSITION");
		assertEquals(subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR.toString()), "true");		
		assertEquals(subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT.toString()), "Detective George Jones");				
		
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
