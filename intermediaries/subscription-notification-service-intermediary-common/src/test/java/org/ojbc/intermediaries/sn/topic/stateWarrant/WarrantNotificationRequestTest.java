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
package org.ojbc.intermediaries.sn.topic.stateWarrant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.camel.Message;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.intermediaries.sn.topic.warrantfile.WarrantFileNotificationRequest;
import org.w3c.dom.Document;

public class WarrantNotificationRequestTest {
	
	@Test
	@Disabled
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/stateWarrant/Test_Notification_Response.xml"));
		
		WarrantFileNotificationRequest request = new WarrantFileNotificationRequest(message);
		
		assertEquals(request.isNotificationEventDateInclusiveOfTime(), false);
		assertEquals(request.getNotificationEventDate().toString("yyyy-MM-dd"), "2013-09-06");
		assertEquals(request.getPersonFirstName(), "JOHN");
		assertNull(request.getPersonMiddleName());
		assertEquals(request.getPersonLastName(), "Simpson");
		assertNull(request.getPersonNameSuffix());
		assertEquals(request.getNotifyingAgencyName(), "Honolulu PD");
		assertNull(request.getNotificationEventIdentifier());
		//assertEquals(request.getAttorneyGeneralIndicator(), "false");
		assertEquals(request.getSubjectIdentifiers().size(), 4);
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "Homer");
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Simpson");
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1955-01-15");
		
		assertEquals(request.getTopic(), "{http://ojbc.org/wsn/topics}:person/arrest");
		//assertEquals(request.getPersonBookingName(), "Simpson, Homer");
		//assertEquals(request.getBookingDateTimeDisplay(), "2013-09-06");
		
        assertEquals(2013, request.getNotificationEventDate().getYear());
        assertEquals(9, request.getNotificationEventDate().getMonthOfYear());
        assertEquals(6, request.getNotificationEventDate().getDayOfMonth());
        
	}
}