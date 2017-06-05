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
