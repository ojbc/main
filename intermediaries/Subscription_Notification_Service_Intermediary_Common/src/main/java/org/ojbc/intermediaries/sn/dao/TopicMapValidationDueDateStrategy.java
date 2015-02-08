package org.ojbc.intermediaries.sn.dao;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * A strategy that looks up the appropriate child strategy by the subscription's topic, and returns the result of executing that strategy on the subscription.  This allows
 * for different strategies for different topics.
 */
public class TopicMapValidationDueDateStrategy implements ValidationDueDateStrategy {

    private Map<String, ValidationDueDateStrategy> map = new HashMap<String, ValidationDueDateStrategy>();

    /**
     * Create the composite strategy by passing in a map that associates topics with child strategies.
     * @param map
     */
    public TopicMapValidationDueDateStrategy(Map<String, ValidationDueDateStrategy> map) {
        this.map = map;
    }

    @Override
    public DateTime getValidationDueDate(Subscription subscription) {
        String topic = subscription.getTopic();
        if (topic != null) {
            ValidationDueDateStrategy s = map.get(topic);
            if (s != null) {
                return s.getValidationDueDate(subscription);
            }
            throw new IllegalArgumentException("Topic " + topic + " not found in validation due date strategy map");
        }
        throw new IllegalArgumentException("Subscription does not have a topic");
    }

}
