package org.ojbc.intermediaries.sn.notification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An enhancement strategy implementation that adds cc addresses that appear as the values in a map, keyed by the to addresses. This allows a notification processor to automatically cc a particular
 * address anytime a to: address appears.
 * 
 */
public class CCLookupEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(CCLookupEmailEnhancementStrategy.class);

    private Map<String, String> ccLookupMap = new HashMap<String, String>();

    public Map<String, String> getCcLookupMap() {
        return Collections.unmodifiableMap(ccLookupMap);
    }

    public void setCcLookupMap(Map<String, String> ccLookupMap) {
        this.ccLookupMap = ccLookupMap;
    }

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        for (String to : ret.getToAddresseeSet()) {
            String ccAddressee = ccLookupMap.get(to);
            if (ccAddressee != null) {
                log.debug("Adding cc=" + ccAddressee + " for to=" + to);
                ret.addCcAddressee(ccAddressee);
            }
        }
        return ret;
    }

}
