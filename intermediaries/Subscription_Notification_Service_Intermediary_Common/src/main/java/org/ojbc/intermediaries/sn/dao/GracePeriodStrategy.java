package org.ojbc.intermediaries.sn.dao;

import org.joda.time.Interval;

/**
 * The interface for objects that figure out a grace period for a subscription.
 */
public interface GracePeriodStrategy {
    
    /**
     * Figure out the grace period in which a subscription must be validated prior to inactivation.
     * @param subscription the subscription
     * @return the grace period
     * @throws Exception
     */
    public Interval getGracePeriod(Subscription subscription);

}
