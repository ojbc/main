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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.intermediaries.sn.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;

public class ExpiringSubscriptionsManager {

	private int warningDaysBeforeExpiration;
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private static final Log log = LogFactory.getLog(ExpiringSubscriptionsManager.class);
	
	private static String MESSAGE_BODY="You have <NUMBER_OF_EXPIRING_SUBSCRIPTIONS> Rap Back subscription(s) that will be expiring soon.  You must logon to the Subscriptions Application in the HIJIS Portal to extend the subscription.  Failure to extend the subscription will result in automatic cancellation of the subscription and you will no longer receive Rap Back notifications on the individual.";
	private static String EMAIL_SUBJECT = "You have subscriptions expiring soon";
	
	public List<ExpiringSubscriptionEmail> returnExpiringNotificationEmails()
	{
		log.info("Checking for expired notifications");
		
		List<String> uniqueSubscriptionOwners = subscriptionSearchQueryDAO.getUniqueSubscriptionOwners();
		Map<String, List<Subscription>> ownersToNotify = new HashMap<String, List<Subscription>>();
		
		for (String subscriptionOwner : uniqueSubscriptionOwners)
		{
			List<Subscription> subscriptionsForOwner = subscriptionSearchQueryDAO.searchForSubscriptionsBySubscriptionOwner(subscriptionOwner);
			
			for (Subscription subscription : subscriptionsForOwner)
			{
				if (notifyOfExpiredSubscription(subscription))
				{
					List<Subscription> expiredSubscriptionsForOwner = ownersToNotify.get(subscriptionOwner);
					
					if (expiredSubscriptionsForOwner == null)
					{
						expiredSubscriptionsForOwner = new ArrayList<Subscription>();
					}	

					expiredSubscriptionsForOwner.add(subscription);
					
					ownersToNotify.put(subscriptionOwner, expiredSubscriptionsForOwner);
				}	
			}	
			
		}	
		
		List<ExpiringSubscriptionEmail> emailsToSend = new ArrayList<ExpiringSubscriptionEmail>();

		for (Map.Entry<String, List<Subscription>> entry : ownersToNotify.entrySet()) {
			
			List<Subscription> expiredSubscriptionsForOwner = entry.getValue();
			int numberOfExpiringSubscriptions = expiredSubscriptionsForOwner.size();
			
			ExpiringSubscriptionEmail email = new ExpiringSubscriptionEmail();
			
			email.setMessageBody(MESSAGE_BODY.replace("<NUMBER_OF_EXPIRING_SUBSCRIPTIONS>", String.valueOf(numberOfExpiringSubscriptions)));
			
			//TODO: we will need to change this to the subscription owner email which needs to be added to the database.
			email.setTo(entry.getKey());
			email.setSubject(EMAIL_SUBJECT);
			
			emailsToSend.add(email);
		    
		}
		
		return emailsToSend;
		
	}

	public String createCamelEmail(Exchange in, @Body ExpiringSubscriptionEmail expiringSubscriptionEmail)
	{
		in.getIn().setHeader("to", expiringSubscriptionEmail.getTo());
		in.getIn().setHeader("subject", expiringSubscriptionEmail.getSubject());
		
		return expiringSubscriptionEmail.getMessageBody();
	}
	
	boolean notifyOfExpiredSubscription(Subscription subscription) {
		boolean isSubscriptionExpired = false;
		
		if (subscription.getEndDate() == null)
		{
			return false;
		}	
		
		DateTime subscriptionEndDate = subscription.getEndDate();
		DateTime now = new DateTime();
		
		if (now.plusDays(warningDaysBeforeExpiration).isAfter(subscriptionEndDate))
		{
			return true;
		}	
		
		return isSubscriptionExpired;
	}


	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

	public int getWarningDaysBeforeExpiration() {
		return warningDaysBeforeExpiration;
	}

	public void setWarningDaysBeforeExpiration(int warningDaysBeforeExpiration) {
		this.warningDaysBeforeExpiration = warningDaysBeforeExpiration;
	}
	
}
