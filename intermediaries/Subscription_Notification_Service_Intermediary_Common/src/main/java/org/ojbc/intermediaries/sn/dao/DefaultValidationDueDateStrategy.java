package org.ojbc.intermediaries.sn.dao;

import org.joda.time.DateTime;

/**
 * A default (Null Object) implementation of the strategy.
 */
public class DefaultValidationDueDateStrategy implements ValidationDueDateStrategy {

    /**
     * The default implementation returns a null date, indicating that validation is never due.
     */
    @Override
    public DateTime getValidationDueDate(Subscription subscription) {
        return null;
    }

}
