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
package org.ojbc.intermediaries.sn.notification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * An email enhancement strategy that removes "to:" addresses from the email if they do not appear in a whitelist.  Note that it does *not* whitelist cc or bcc addresses.
 *
 */
public class WhitelistFilteringEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private Set<String> whitelist = new HashSet<String>();
    
    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        ret.applyAddressWhitelist(whitelist);
        return ret;
    }

    public Set<String> getWhitelist() {
        return Collections.unmodifiableSet(whitelist);
    }

    public void setWhitelist(Set<String> whitelist) {
        this.whitelist = whitelist;
    }
    
    public String addAddressToWhitelist(String address) {
        whitelist.add(address);
        return address;
    }

}
