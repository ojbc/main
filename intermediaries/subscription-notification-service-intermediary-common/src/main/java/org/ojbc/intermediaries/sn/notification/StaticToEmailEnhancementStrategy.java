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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An email edit strategy that adds a static BCC to every email notification. The bccAddress property is a comma-separated string of email addresses.
 * 
 */
public class StaticToEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(StaticToEmailEnhancementStrategy.class);

    private String staticToAddress;

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        if ( StringUtils.isNotBlank(staticToAddress)) {
            log.info("Replacing the to addresses "  + ret.getToAddressees() 
            	+ " the static TO address " + staticToAddress);
            if (staticToAddress.contains(",")) {
                String[] addresses = staticToAddress.split(",");
                for (String address : addresses) {
                    ret.addToAddressee(address);;
                }
            } else {
                ret.addBccAddressee(staticToAddress);
            }
        }
        else {
            log.warn("Static TO email decorator called but no address has been set. No TO address replaced in the email notification.");
        }
        return ret;
    }

	public String getStaticToAddress() {
		return staticToAddress;
	}

	public void setStaticToAddress(String staticToAddress) {
		this.staticToAddress = staticToAddress;
	}

}
