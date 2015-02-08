package org.ojbc.intermediaries.sn.dao;

import org.joda.time.Interval;

/**
 * A default (Null Object) implementation of the strategy.
 *
 */
public class DefaultGracePeriodStrategy implements GracePeriodStrategy {

    /**
     * The default implementation returns a null interval, indicating that there is no grace period.
     */
    @Override
    public Interval getGracePeriod(Subscription subscription) {
        return null;
    }

}
