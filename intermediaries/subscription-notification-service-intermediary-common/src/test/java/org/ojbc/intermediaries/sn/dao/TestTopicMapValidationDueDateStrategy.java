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
