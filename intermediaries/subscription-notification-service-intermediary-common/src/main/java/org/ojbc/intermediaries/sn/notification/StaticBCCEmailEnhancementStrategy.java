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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An email edit strategy that adds a static BCC to every email notification. The bccAddress property is a comma-separated string of email addresses.
 * 
 */
public class StaticBCCEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(StaticBCCEmailEnhancementStrategy.class);

    private String bccAddress;

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        if (bccAddress != null) {
            log.debug("Adding bcc=" + bccAddress + " to email with to=" + ret.getToAddressees());
            if (bccAddress.contains(",")) {
                String[] addresses = bccAddress.split(",");
                for (String address : addresses) {
                    ret.addBccAddressee(address);
                }
            } else {
                ret.addBccAddressee(bccAddress);
            }
        }
        else {
            log.warn("BCC email decorator called but no address has been set. No bcc added to email notification.");
        }
        return ret;
    }

}
