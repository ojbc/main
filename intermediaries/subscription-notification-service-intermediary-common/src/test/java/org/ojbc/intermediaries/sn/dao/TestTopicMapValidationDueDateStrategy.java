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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

public class TestTopicMapValidationDueDateStrategy {
    
    @Test
    public void testTopicMapValidationDueDateStrategy() {
        
        StaticValidationDueDateStrategy staticStrategy1 = new StaticValidationDueDateStrategy();
        StaticValidationDueDateStrategy staticStrategy2 = new StaticValidationDueDateStrategy();
        staticStrategy1.setValidDays(10);
        staticStrategy2.setValidDays(20);
        
        Map<String, ValidationDueDateStrategy> map = new HashMap<String, ValidationDueDateStrategy>();
        map.put("t1", staticStrategy1);
        map.put("t2", staticStrategy2);
        
        TopicMapValidationDueDateStrategy strategy = new TopicMapValidationDueDateStrategy(map);
        
        Subscription subscription = new Subscription();
        DateTime baseDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2014-10-03");
        subscription.setLastValidationDate(baseDate);
        subscription.setTopic("t1");
        DateTime validationDueDate = strategy.getValidationDueDate(subscription);
        assertEquals(10, Days.daysBetween(baseDate, validationDueDate).getDays());
        
    }

}
