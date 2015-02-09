package org.ojbc.intermediaries.sn.dao;

import java.util.Collection;
import java.util.HashSet;

/**
 * An implementation of the exemption interface that determines exemption based on membership in a configured list of subscribing systems.
 *
 */
public class SystemCollectionValidationExemptionFilter implements ValidationExemptionFilter {
    
    private Collection<String> exemptSystems = new HashSet<String>();

    public Collection<String> getExemptSystems() {
        return exemptSystems;
    }

    public void setExemptSystems(Collection<String> exemptSystems) {
        this.exemptSystems = exemptSystems;
    }

    @Override
    public boolean requiresValidation(Subscription subscription) {
        String owner = subscription.getSubscribingSystemIdentifier();
        return !exemptSystems.contains(owner);
    }

}
