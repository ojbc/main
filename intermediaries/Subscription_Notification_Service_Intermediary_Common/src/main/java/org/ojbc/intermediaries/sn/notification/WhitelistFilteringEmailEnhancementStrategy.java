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
