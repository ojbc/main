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
import static org.junit.Assert.assertNull;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.RapbackTriggeringEvent;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.w3c.dom.Document;

public class RapbackNotificationRequestTest {
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/fbi/Federal_CH_Notication_Message.xml"));
		
		RapbackNotificationRequest request = new RapbackNotificationRequest(message);
		
		assertEquals(request.isNotificationEventDateInclusiveOfTime(), false);
		assertEquals(request.getPersonFirstName(), "Charlie");
		assertNull(request.getPersonMiddleName());
		assertEquals(request.getPersonLastName(), "Macdonald");
		assertNull(request.getPersonNameSuffix());
		assertEquals(request.getNotifyingAgencyName(), "FBI");
		assertEquals(request.getNotificationEventIdentifier(), "123456");
		assertEquals(request.getSID(), "A5186353");
		assertEquals(request.getUCN(), "223276SG5");
		assertEquals(request.getSubjectIdentifiers().size(), 1);
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A5186353");
		
		assertEquals(request.getTriggeringEvents().size(), 2);
		
		RapbackTriggeringEvent event1 = request.getTriggeringEvents().get(0);
		RapbackTriggeringEvent event2 = request.getTriggeringEvents().get(1);
		
		assertEquals(event1.getTriggeringEventCode(), "ARREST");
		assertEquals(event1.getTriggeringEventDate(), "01-29-2013");
		assertEquals(event1.getTriggeringEventText(), "Event 1 that triggered the rap back");
		
		assertEquals(event2.getTriggeringEventCode(), "DISPOSITION");
		assertEquals(event2.getTriggeringEventDate(), "02-28-2013");
		assertEquals(event2.getTriggeringEventText(), "Event 2 that triggered the rap back");
		
		
		assertEquals(request.getTopic(), "{http://ojbc.org/wsn/topics}:person/rapback");
		
		
	}

}
