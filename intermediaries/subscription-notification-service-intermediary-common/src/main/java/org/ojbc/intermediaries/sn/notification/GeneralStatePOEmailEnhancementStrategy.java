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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An email edit strategy that sets the to address as the Receiving State PO for the email notification. This email strategy is used primarily for ICT notifications.
 * 
 */
public class GeneralStatePOEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(GeneralStatePOEmailEnhancementStrategy.class);
    
    private String stateName;

	@Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        String sendingState = ret.getSubscription().getSendingState();
        String receivingState = ret.getSubscription().getReceivingState();
        String receivingStatePOEmail = ret.getSubscription().getSubscriptionSubjectIdentifiers().get("receivingStatePO");
        String sendingStatePOEmail = ret.getSubscription().getSubscriptionSubjectIdentifiers().get("sendingStatePO");
        log.info("Full receiving state Email and Name Info from Subscription:" +  receivingStatePOEmail);
        if(sendingState.equals(stateName)) {
        	 if(StringUtils.isNotBlank(sendingStatePOEmail)) {
             	String[] splitArr = StringUtils.split(sendingStatePOEmail, ":");
             	if(splitArr.length == 2) {
             		String sendingStateEmail = splitArr[1].trim().substring(0, splitArr[1].length()-5);
             		log.info(sendingStateEmail);
             		ret.removeAllToEmailAddresses();
             		ret.addToAddressee(sendingStateEmail);
             		if(ret.getToAddresseeSet().size() != 1)
             		{
             			log.info("Email address not found in subscription subject identifier");
             		}
             	}
             	else {
             		log.error("Invalid Subscription Identifier format for Sending State PO. ");
             	}
             }
             else {
             	log.error("Sending State PO Email Address not found");
             }
        }
        else if(receivingState.equals(stateName)) {
       	 	if(StringUtils.isNotBlank(receivingStatePOEmail)) {
            	String[] splitArr = StringUtils.split(receivingStatePOEmail, ":");
            	if(splitArr.length == 2) {
            		String receivingStateEmail = splitArr[1].trim().substring(0, splitArr[1].length()-5);
            		log.info(receivingStateEmail);
            		ret.removeAllToEmailAddresses();
            		ret.addToAddressee(receivingStateEmail);
            		if(ret.getToAddresseeSet().size() != 1)
            		{
            			log.info("Email address not found in subscription subject identifier");
            		}
            	}
            	else {
            		log.error("Invalid Subscription Identifier format for Receiving State PO. ");
            	}
            }
            else {
            	log.error("Receiving State PO Email Address not found");
            }
       }
        else {
        	log.error("No valid sending or receiving state for this email enhancement strategy. ");
        }
        return ret;
       
    }

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
}
