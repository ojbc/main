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
