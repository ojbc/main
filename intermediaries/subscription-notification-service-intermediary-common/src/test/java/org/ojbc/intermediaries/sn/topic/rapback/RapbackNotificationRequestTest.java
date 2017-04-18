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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.w3c.dom.Document;

public class RapbackNotificationRequestTest {
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/fbi/Federal_CH_Notication_Message.xml"));
		
		RapbackNotificationRequest request = new RapbackNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(false));
		assertThat(request.getNotificationEventDate().toString("yyyy-MM-dd"), is("2013-03-29"));
		assertThat(request.getPersonFirstName(), is("Charlie"));
		assertNull(request.getPersonMiddleName());
		assertThat(request.getPersonLastName(), is("Macdonald"));
		assertNull(request.getPersonNameSuffix());
		assertThat(request.getNotifyingAgencyName(), is("FBI"));
		assertThat(request.getNotificationEventIdentifier(), is("123456"));
		assertThat(request.getSID(), is("A5186353"));
		assertThat(request.getUCN(), is("223276SG5"));
		assertThat(request.getSubjectIdentifiers().size(), is(1));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A5186353"));
		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/rapback"));
		
        assertEquals(2013, request.getNotificationEventDate().getYear());
        assertEquals(3, request.getNotificationEventDate().getMonthOfYear());
        assertEquals(29, request.getNotificationEventDate().getDayOfMonth());
		
	}

}
