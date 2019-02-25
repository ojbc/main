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

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestSubscriptionRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtilsTest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.test.annotation.DirtiesContext;
import org.w3c.dom.Document;
@DirtiesContext
public class TestTopicMapValidationDueDateStrategy {
    
    @Test
    public void testTopicMapValidationDueDateStrategy() throws Exception{
        
        StaticValidationDueDateStrategy staticStrategy1 = new StaticValidationDueDateStrategy();
        StaticValidationDueDateStrategy staticStrategy2 = new StaticValidationDueDateStrategy();
        staticStrategy1.setValidDays(10);
        staticStrategy2.setValidDays(20);
        
        Map<String, ValidationDueDateStrategy> map = new HashMap<String, ValidationDueDateStrategy>();
        map.put("t1", staticStrategy1);
        map.put("t2", staticStrategy2);
        
        TopicMapValidationDueDateStrategy strategy = new TopicMapValidationDueDateStrategy(map);
        
		Document messageDocument = NotificationBrokerUtilsTest.getMessageBody("src/test/resources/xmlInstances/subscribeSoapRequest.xml");
		
		Message message = new DefaultMessage();
		
		message.setHeader("subscriptionOwner", "someone");
		message.setHeader("subscriptionOwnerEmailAddress", "email@local.gov");
		
		message.setBody(messageDocument);
		
		String allowedEmailAddressPatterns = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(localhost)";
		ArrestSubscriptionRequest subRequest = new ArrestSubscriptionRequest(message, allowedEmailAddressPatterns);
        
		Subscription subscription = new Subscription();
		
        DateTime baseDate = DateTime.now();
        
        subRequest.setTopic("t1");
        DateTime validationDueDate = strategy.getValidationDueDate(subRequest, new LocalDate());
        assertEquals(10, Days.daysBetween(baseDate, validationDueDate).getDays());

        subscription.setTopic("t1");
        validationDueDate = strategy.getValidationDueDate(subscription, new LocalDate());
        assertEquals(10, Days.daysBetween(baseDate, validationDueDate).getDays());

        
        subRequest.setTopic("t2");
        validationDueDate = strategy.getValidationDueDate(subRequest,new LocalDate());
        assertEquals(20, Days.daysBetween(baseDate, validationDueDate).getDays());

        subscription.setTopic("t2");
        validationDueDate = strategy.getValidationDueDate(subscription,new LocalDate());
        assertEquals(20, Days.daysBetween(baseDate, validationDueDate).getDays());

    }

}
