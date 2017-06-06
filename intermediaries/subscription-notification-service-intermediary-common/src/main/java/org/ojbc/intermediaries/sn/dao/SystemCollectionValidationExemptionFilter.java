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
