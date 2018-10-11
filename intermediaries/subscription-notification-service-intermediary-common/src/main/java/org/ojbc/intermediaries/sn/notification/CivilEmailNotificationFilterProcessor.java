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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.Subscription;

public class CivilEmailNotificationFilterProcessor implements EmailEnhancementStrategy {
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private String civilNotificationDefaultEmailAddress="administrator@local.gov";
	
	private static final Log log = LogFactory.getLog( CivilEmailNotificationFilterProcessor.class );
	
	@Override
	public EmailNotification enhanceEmail(EmailNotification emailNotification) {
		Subscription subscription = emailNotification.getSubscription();
		
		String subscriptionCategoryCode = subscription.getSubscriptionCategoryCode();
		String subscriptionId = subscription.getSubscriptionIdentifier();

		if (subscriptionCategoryCode.equals("F") || subscriptionCategoryCode.equals("J") || subscriptionCategoryCode.equals("I") || subscriptionCategoryCode.equals("S"))
		{	

			emailNotification.removeAllToEmailAddresses();
			
			List<String> emailAddressesToAdd = new ArrayList<String>();
			
            emailAddressesToAdd = subscriptionSearchQueryDAO.returnAgencyProfileEmailForSubscription(subscriptionId, subscriptionCategoryCode);

            if (emailAddressesToAdd.size() == 0)
            {
            	log.error("No email addresses for found for subscription ID: " + subscriptionId + ", and category: " + subscriptionCategoryCode +", using default email address: " + civilNotificationDefaultEmailAddress);
            	emailAddressesToAdd.add(civilNotificationDefaultEmailAddress);
            }	
            
            for (String emailAddress : emailAddressesToAdd)
            {
            	emailNotification.addToAddressee(emailAddress);
            }	
		}
		
		return emailNotification;
	}
	
	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

	public String getCivilNotificationDefaultEmailAddress() {
		return civilNotificationDefaultEmailAddress;
	}

	public void setCivilNotificationDefaultEmailAddress(
			String civilNotificationDefaultEmailAddress) {
		this.civilNotificationDefaultEmailAddress = civilNotificationDefaultEmailAddress;
	}

}
