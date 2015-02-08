package org.ojbc.intermediaries.sn.dao;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Interval;

/**
 * A strategy that looks up the appropriate child strategy by the subscription's topic, and returns the result of executing that strategy on the subscription.  This allows
 * for different strategies for different topics.
 */
public class TopicMapGracePeriodStrategy implements GracePeriodStrategy {
    
    private Map<String, GracePeriodStrategy> map = new HashMap<String, GracePeriodStrategy>();

    /**
     * Create the composite strategy by passing in a map that associates topics with child strategies.
     * @param map
     */
    public TopicMapGracePeriodStrategy(Map<String, GracePeriodStrategy> map) {
        this.map = map;
    }

    @Override
    public Interval getGracePeriod(Subscription subscription) {
        String topic = subscription.getTopic();
        if (topic != null) {
            GracePeriodStrategy s = map.get(topic);
            if (s != null) {
                return s.getGracePeriod(subscription);
            }
            throw new IllegalArgumentException("Topic " + topic + " not found in validation grace period strategy map");
        }
        throw new IllegalArgumentException("Subscription does not have a topic");
    }

}
