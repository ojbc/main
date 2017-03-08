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
package org.ojbc.intermediaries.sn.topic.rapback;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
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
        
        Message message = new DefaultMessage();
        
        message.setHeader("subscriptionOwner", "someone");
        message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
        
        message.setBody(messageDocument);
        
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		
		RapbackSubscriptionRequest sub = new RapbackSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		assertThat(sub.getSubscriptionQualifier(), is("1234578"));
		assertThat(sub.getSubjectName(), is("Test Person"));
		
		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));
		
		assertThat(sub.getSubjectIdentifiers().size(), is(6));
		assertThat(sub.getSubscriptionOwner(), is("someone"));
		assertThat(sub.getSubscriptionOwnerEmailAddress(), is("email@local.gov"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Test"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Person"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1972-08-02"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FBI_ID), is("123456789"));
		assertThat(sub.getTopic(), is("{http://ojbc.org/wsn/topics}:person/rapback"));
		
		Map<String, String> subscriptionProperties = sub.getSubscriptionProperties();
		
		assertThat(subscriptionProperties.size(), is(7));
		assertThat(subscriptionProperties.get(FederalTriggeringEventCode.ARREST.toString()), is("ARREST"));
		assertThat(subscriptionProperties.get(FederalTriggeringEventCode.DEATH.toString()), is("DEATH"));
		assertThat(subscriptionProperties.get(FederalTriggeringEventCode.NCIC_SOR.toString()), is("NCIC-SOR"));
		assertThat(subscriptionProperties.get(FederalTriggeringEventCode.NCIC_WARRANT.toString()), is("NCIC-WARRANT"));
		assertThat(subscriptionProperties.get(FederalTriggeringEventCode.DISPOSITION.toString()), is("DISPOSITION"));
		assertThat(subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR.toString()), is("true"));		
		assertThat(subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT.toString()), is("Detective George Jones"));				
		
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
