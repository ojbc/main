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
package org.ojbc.intermediaries.sn.topic.incident;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.w3c.dom.Document;

public class IncidentSubscriptionRequestTest {
	
	/* NOTE: Most of the tests of the abstract SubscriptionRequest class reside in ArrestSubscriptionRequestTest */
	
	Map<String, String> namespaceUris;
	
	@BeforeEach
	public void setup() {
		namespaceUris = new HashMap<String, String>();
		namespaceUris.put("wsnb2", "http://docs.oasis-open.org/wsn/b-2");
		namespaceUris.put("sm", "http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0");
		namespaceUris.put("smext", "http://ojbc.org/IEPD/Extensions/Subscription/1.0");
		namespaceUris.put("nc20", "http://niem.gov/niem/niem-core/2.0");
	}

	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		IncidentSubscriptionRequest sub = new IncidentSubscriptionRequest(message, null);
		
		assertEquals(sub.getSubscriptionQualifier(), "302593");
		assertEquals(sub.getSubjectName(), "John Doe");
		
		//Assert size of set and only entry
		assertEquals(sub.getEmailAddresses().size(), 1);
		assertEquals(sub.getEmailAddresses().contains("po6@localhost"), true);
		
		assertEquals(sub.getSubjectIdentifiers().size(), 4);
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "John");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Doe");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1980-01-01");
		assertEquals(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "302593");
	}
	
	private Document getMessageBody() throws Exception {

		File inputFile = new File("src/test/resources/xmlInstances/subscribeSoapRequest-incident.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;	
	}

}
