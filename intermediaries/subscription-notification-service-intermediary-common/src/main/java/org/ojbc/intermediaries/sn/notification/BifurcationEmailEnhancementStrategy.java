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
public class BifurcationEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(BifurcationEmailEnhancementStrategy.class);
    
    private String probationEmail; 
    
	private String paroleEmail;
	
	 public String getProbationEmail() {
			return probationEmail;
		}

		public void setProbationEmail(String probationEmail) {
			this.probationEmail = probationEmail;
		}

		public String getParoleEmail() {
			return paroleEmail;
		}

		public void setParoleEmail(String paroleEmail) {
			this.paroleEmail = paroleEmail;
		}
    
	@Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        if(StringUtils.isNotEmpty(ret.getSubscription().getSubscriptionCategoryCode()) && ret.getSubscription().getSubscriptionCategoryCode().equals("Parole")) {
            log.info("Subscription ID: " + ret.getSubscription().getId() + " is this type of subscription: " + ret.getSubscription().getSubscriptionCategoryCode() + ". Updating email to be "
            		+ paroleEmail);
        	ret.removeAllToEmailAddresses();
        	ret.addToAddressee(paroleEmail);
        }
        else if(StringUtils.isNotEmpty(ret.getSubscription().getSubscriptionCategoryCode()) && ret.getSubscription().getSubscriptionCategoryCode().equals("Probation")) {
        	log.info("Subscription ID: " + ret.getSubscription().getId() + " is this type of subscription: " + ret.getSubscription().getSubscriptionCategoryCode() + ". Updating email to be "
            		+ probationEmail);
        	ret.removeAllToEmailAddresses();
        	ret.addToAddressee(probationEmail);
        }
        else {
        	log.info("Unable to get subscription category");
        }
        
        return ret;
       
    }

}
