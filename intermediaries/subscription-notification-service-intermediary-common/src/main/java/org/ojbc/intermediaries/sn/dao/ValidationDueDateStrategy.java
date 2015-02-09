package org.ojbc.intermediaries.sn.dao;

import org.joda.time.DateTime;

/**
 * Interface for objects that compute a validation due date from a subscription
 */
public interface ValidationDueDateStrategy {
    
    /**
     * Given a subscription object, figure out what the validation due date is.
     * @param subscription the subscription
     * @return the date on which the subscription must be validated
     * @throws Exception
     */
    public DateTime getValidationDueDate(Subscription subscription);

}
