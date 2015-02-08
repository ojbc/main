package org.ojbc.intermediaries.sn.dao;

/**
 * A default implementation of the exemption interface that exempts no Subscriptions from validation requirements.
 */
public class DefaultValidationExemptionFilter implements ValidationExemptionFilter {

    @Override
    public boolean requiresValidation(Subscription subscription) {
        return true;
    }

}
