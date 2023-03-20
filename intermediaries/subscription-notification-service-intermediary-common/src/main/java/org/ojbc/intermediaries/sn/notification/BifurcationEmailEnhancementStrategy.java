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

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An email edit strategy that adds a static BCC to every email notification. The bccAddress property is a comma-separated string of email addresses.
 * 
 */
public class ArizonaICTEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(ArizonaICTEmailEnhancementStrategy.class);

    private final String PROBATION_EMAIL = "az-isc-prob@courts.az.gov";
    
    private final String PAROLE_EMAIL = "ISC_PAROLE@azadc.gov";
    
	@Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        String sendingStatePOEmail = ret.getSubscription().getSubscriptionSubjectIdentifiers().get("sendingStatePO");
        log.info(sendingStatePOEmail);
        if(StringUtils.isNotEmpty(ret.getSubscription().getSubscriptionCategoryCode()) && ret.getSubscription().getSubscriptionCategoryCode().equals("Parole")) {
        	ret.removeAllToEmailAddresses();
        	ret.addToAddressee(PAROLE_EMAIL);
        }
        else if(StringUtils.isNotEmpty(ret.getSubscription().getSubscriptionCategoryCode()) && ret.getSubscription().getSubscriptionCategoryCode().equals("Probation")) {
        	ret.removeAllToEmailAddresses();
        	ret.addToAddressee(PROBATION_EMAIL);
        }
        else {
        	log.info("Unable to get subscription category");
        }
        
        return ret;
       
    }

}
