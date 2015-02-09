package org.ojbc.intermediaries.sn.dao;

/**
 * The interface for objects that express whether a Subscription is exempt from requirements to be validated.
 */
public interface ValidationExemptionFilter {
    
    /**
     * Return whether the specified Subscription is exempt from requirements that it be validated to remain active.
     * @param subscription the subscription
     * @return true if it has to be validated to remain active (i.e., subject to notifications), false otherwise
     */
    public boolean requiresValidation(Subscription subscription);

}
