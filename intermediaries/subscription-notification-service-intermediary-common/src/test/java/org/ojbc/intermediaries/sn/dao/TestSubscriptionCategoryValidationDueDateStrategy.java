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

import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultMessage;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestSubscriptionRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtilsTest;
import org.ojbc.util.model.rapback.Subscription;
import org.w3c.dom.Document;

/**
 * Basic unit test for static validation due date strategy.
 *
 */
public class TestSubscriptionCategoryValidationDueDateStrategy {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Test
	public void testStaticValidationDueDateStrategy() throws Exception
	{
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		SubscriptionCategoryValidationDueDateStrategy strategy = new SubscriptionCategoryValidationDueDateStrategy();
		
		DateTime currentDate = new DateTime();

		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
		
		CamelContext context = new DefaultCamelContext();
		Message message = new DefaultMessage(context);
		
		message.setHeader("subscriptionOwner", "someone");
		message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
		
		message.setBody(messageDocument);
		
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		ArrestSubscriptionRequest subRequest = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
		
		Subscription subscription = new Subscription();
		
		//If the end date is less than the validation due date then the validation due date should be the end date
		DateTime endDate = DateTime.now().plusDays(10);
		subRequest.setEndDateString(DATE_FORMATTER.print(endDate));

		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_INVESTIGATIVE);
		assertEquals(fmt.print(endDate), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		//Set the end date back to null to resume normal behavior
		subRequest.setEndDateString(null);
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_INVESTIGATIVE);
		assertEquals(fmt.print(currentDate.plusYears(1)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));

		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_INVESTIGATIVE);
		assertEquals(fmt.print(currentDate.plusYears(1)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_SUPERVISION);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_SUPERVISION);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.FIREARMS);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.FIREARMS);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.NON_CRIMINAL_JUSTICE_EMPLOYMENT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.NON_CRIMINAL_JUSTICE_EMPLOYMENT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_EMPLOYMENT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.CRIMINAL_JUSTICE_EMPLOYMENT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
		subRequest.setReasonCategoryCode(SubscriptionNotificationConstants.SECURITY_CLEARANCE_INFORMATION_ACT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subRequest, new LocalDate())));
		
		subscription.setSubscriptionCategoryCode(SubscriptionNotificationConstants.SECURITY_CLEARANCE_INFORMATION_ACT);
		assertEquals(fmt.print(currentDate.plusYears(5)), fmt.print(strategy.getValidationDueDate(subscription, new LocalDate())));
		
	}
	
}
