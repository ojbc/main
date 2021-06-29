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
package org.ojbc.intermediaries.sn.topic.prosecutiondecisionupdate;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class ProsecutionDecisionUpdateSubscriptionRequestTest {
		
	@Test
	public void testCourtDispositionUpdateSubscriptionRequest() throws Exception {
		
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		ProsecutionDecisionUpdateSubscriptionRequest subscription = new ProsecutionDecisionUpdateSubscriptionRequest(message, null);		
		
		assertEquals(subscription.getSubscriptionQualifier(), "302593");
		
		assertEquals(subscription.getSubjectName(), "John Doe");
		
		assertEquals(subscription.getEmailAddresses().size(), 1);
		assertEquals(subscription.getEmailAddresses().contains("po6@localhost"), true);
		
		assertEquals(subscription.getSubjectIdentifiers().size(), 5);
		assertEquals(subscription.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "John");
		assertEquals(subscription.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Doe");
		assertEquals(subscription.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1990-10-20");
		assertEquals(subscription.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), "302593");
	}
	
	private Document getMessageBody() throws Exception {
		return XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/prosecutionDecisionUpdate/subscribeSoapRequest-prosecutionDecisionUpdate.xml"));	
	}

}
