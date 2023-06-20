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
public class SendingStatePOEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(SendingStatePOEmailEnhancementStrategy.class);

    private String sendingStateEmail;
    
    public String getSendingStateEmail() {
		return sendingStateEmail;
	}

	public void setSendingStateEmail(String sendingStateEmail) {
		this.sendingStateEmail = sendingStateEmail;
	}

	@Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        String sendingStatePOEmail = ret.getSubscription().getSubscriptionSubjectIdentifiers().get("sendingStatePO");
        log.info(sendingStatePOEmail);
        if(StringUtils.isNotBlank(sendingStatePOEmail)) {
        	String[] splitArr = StringUtils.split(sendingStatePOEmail, ":");
        	if(splitArr.length == 2) {
        		sendingStateEmail = splitArr[1].trim().substring(0, splitArr[1].length()-5);
        		log.info(sendingStateEmail);
        		ret.removeAllToEmailAddresses();
        		ret.addToAddressee(sendingStateEmail);
        		if(ret.getToAddresseeSet().size() != 1)
        		{
        			log.info("Email address not found in notification mechanism table");
        		}
        	}
        	else {
        		log.info("Invalid Subscription Identifier format for Sending State PO. ");
        	}
        }
        else {
        	log.info("Sending State PO Email Address not found");
        }
        return ret;
       
    }

}
