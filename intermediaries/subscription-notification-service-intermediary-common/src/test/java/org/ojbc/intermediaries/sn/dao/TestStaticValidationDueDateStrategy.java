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
package org.ojbc.intermediaries.sn.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestSubscriptionRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtilsTest;
import org.w3c.dom.Document;

/**
 * Basic unit test for static validation due date strategy.
 *
 */
public class TestStaticValidationDueDateStrategy {

	@Test
	public void testStaticValidationDueDateStrategy() throws Exception
	{
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		StaticValidationDueDateStrategy staticValidationDueDateStrategy = new StaticValidationDueDateStrategy("SYSTEM");
		
		DateTime currentDate = new DateTime();

		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
		
		Message message = new DefaultMessage();
		
		message.setHeader("subscriptionOwner", "someone");
		message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
		
		message.setBody(messageDocument);
		
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		ArrestSubscriptionRequest sub = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		sub.setSubscriptionOwner("SYSTEM");
		
		assertNull(staticValidationDueDateStrategy.getValidationDueDate(sub.getSubscriptionOwner(), "", "", new LocalDate()));
		
		sub.setSubscriptionOwner("NOT_SYSTEM");
		assertEquals(fmt.print(currentDate), fmt.print(staticValidationDueDateStrategy.getValidationDueDate(sub.getSubscriptionOwner(), "", "", new LocalDate())));
		
		staticValidationDueDateStrategy.setValidDays(30);
		assertEquals(fmt.print(currentDate.plusDays(30)), fmt.print(staticValidationDueDateStrategy.getValidationDueDate(sub.getSubscriptionOwner(), "", "",new LocalDate())));
	}
	
}
