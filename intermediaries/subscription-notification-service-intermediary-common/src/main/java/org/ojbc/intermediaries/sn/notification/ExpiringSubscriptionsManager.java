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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.sn.SubscriptionSearchRequest;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.w3c.dom.Document;

public class ExpiringSubscriptionsManager {

	private int warningDaysBeforeExpiration;
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private static final Log log = LogFactory.getLog(ExpiringSubscriptionsManager.class);
	
	private static String EXPIRING_CRIMINAL_SUB_MESSAGE_BODY="You have <NUMBER_OF_EXPIRING_SUBSCRIPTIONS> Criminal subscription(s) that will be expiring soon.  "
			+ "You must logon to the Subscriptions Application in the HIJIS Portal to extend the subscription(s).  Failure to extend the subscription(s) "
			+ "will result in automatic cancellation of the subscription(s) and you will no longer receive notifications on the individual(s).";

	private static String EXPIRING_APP_RAPBACK_SUB_MESSAGE_BODY="You have <NUMBER_OF_EXPIRING_SUBSCRIPTIONS> Rap "
			+ "Back subscription(s) that will be expiring soon.  You must logon to the Applicant Rap Back Application "
			+ "in the HIJIS Portal to extend the subscription(s).  Failure to extend the subscription will result "
			+ "in automatic cancellation of the subscription(s) and you will no longer receive Rap Back notifications "
			+ "on the individual(s).";
	
	private static String EXPIRING_RAPBACK_EMAIL_SUBJECT = "You have Rap Back subscriptions expiring soon";
	private static String EXPIRING_CRIMINAL_EMAIL_SUBJECT = "You have Criminal subscriptions expiring soon";
	
	public List<ExpiringSubscriptionEmail> returnExpiringNotificationEmails(@Header("hostServer") String hostServer)
	{
		log.info("Checking for expiring notifications");
		
		List<String> uniqueSubscriptionOwners = subscriptionSearchQueryDAO.getUniqueSubscriptionOwners();
		Map<String, List<Subscription>> ownersToNotify = new HashMap<String, List<Subscription>>();
		
		for (String subscriptionOwner : uniqueSubscriptionOwners)
		{
			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
			subscriptionSearchRequest.setAdminSearch(true);
			subscriptionSearchRequest.setOwnerFederatedId(subscriptionOwner);
			subscriptionSearchRequest.setIncludeExpiredSubscriptions(true);
			
			List<Subscription> subscriptionsForOwner = 
					subscriptionSearchQueryDAO.findBySubscriptionSearchRequest(subscriptionSearchRequest);
			List<Subscription> expiringSubscriptionsForOwner = subscriptionsForOwner.stream()
				.filter(this::notifyOfExpiringSubscription)
				.collect(Collectors.toList()); 
			
			if ( !expiringSubscriptionsForOwner.isEmpty() ) {
				ownersToNotify.put(subscriptionOwner, expiringSubscriptionsForOwner);
			}
			
		}	
		
		List<ExpiringSubscriptionEmail> emailsToSend = new ArrayList<ExpiringSubscriptionEmail>();

		String expiringCriminalSubsEmailSubject = StringUtils.join(EXPIRING_CRIMINAL_EMAIL_SUBJECT, " on " , hostServer);
		String expiringRapbackSubsEmailSubject = StringUtils.join(EXPIRING_RAPBACK_EMAIL_SUBJECT, " on " , hostServer);
		for (Map.Entry<String, List<Subscription>> entry : ownersToNotify.entrySet()) {
			
			List<Subscription> expiringSubscriptionsForOwner = entry.getValue();
			long numberOfExpiringCriminalSubscriptions = expiringSubscriptionsForOwner.stream()
					.filter(Subscription::isCriminalRapback)
					.count();
			
			if (numberOfExpiringCriminalSubscriptions > 0) {
				ExpiringSubscriptionEmail email = new ExpiringSubscriptionEmail();
				
				email.setMessageBody(EXPIRING_CRIMINAL_SUB_MESSAGE_BODY.replace("<NUMBER_OF_EXPIRING_SUBSCRIPTIONS>", 
						String.valueOf(numberOfExpiringCriminalSubscriptions)));
				
				/*
				 * All subscriptions will have the same owner and email address so we can get the 
				 * email address from the first subscription because there will be at least 1
				 */
				email.setTo(entry.getValue().get(0).getSubscriptionOwnerEmailAddress());
				email.setSubject(expiringCriminalSubsEmailSubject);
				
				emailsToSend.add(email);
			}
		    
			long numberOfExpiringCivilSubscriptions = expiringSubscriptionsForOwner.stream()
					.filter(Subscription::isCivilRapback)
					.count();
			
			if (numberOfExpiringCivilSubscriptions > 0) {
				ExpiringSubscriptionEmail email = new ExpiringSubscriptionEmail();
				
				email.setMessageBody(EXPIRING_APP_RAPBACK_SUB_MESSAGE_BODY.replace("<NUMBER_OF_EXPIRING_SUBSCRIPTIONS>", 
						String.valueOf(numberOfExpiringCivilSubscriptions)));
				
				/*
				 * All subscriptions will have the same owner and email address so we can get the 
				 * email address from the first subscription because there will be at least 1
				 */
				email.setTo(entry.getValue().get(0).getSubscriptionOwnerEmailAddress());
				email.setSubject(expiringRapbackSubsEmailSubject);
				
				emailsToSend.add(email);
			}
			
		}
		
		return emailsToSend;
		
	}
	
	public List<Document> returnExpiredSubscriptionsToUnsubscribe()
	{
		log.info("Checking for expired subscriptions to unsubscribe");
		
		List<String> uniqueSubscriptionOwners = subscriptionSearchQueryDAO.getUniqueSubscriptionOwners();
		
		Set<Subscription> expiredSubscriptions = new HashSet<Subscription>();
		List<Document> subscriptionsToUnsubscribe = new ArrayList<Document>();
		
		for (String subscriptionOwner : uniqueSubscriptionOwners)
		{
			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
			subscriptionSearchRequest.setAdminSearch(true);
			subscriptionSearchRequest.setOwnerFederatedId(subscriptionOwner);
			subscriptionSearchRequest.setIncludeExpiredSubscriptions(true);
			subscriptionSearchRequest.setActive(true);
			
			//This method will only return active subscriptions
			List<Subscription> subscriptionsForOwner = 
					subscriptionSearchQueryDAO.findBySubscriptionSearchRequest(subscriptionSearchRequest);
			
			for (Subscription subscription : subscriptionsForOwner)
			{
				
				if (subscription.isExpired() || DateTime.now().isAfter(subscription.getEndDate()))
				{
					expiredSubscriptions.add(subscription);
				}	
				
			}	
			
		}	

		log.info("Found " + expiredSubscriptions.size() + " expired subscriptions to unsubscribe. ");
		expiredSubscriptions.forEach((expiredSubscription) -> {
		    
			//handle unsubscription here
			Unsubscription unsubscription = new Unsubscription(String.valueOf(expiredSubscription.getId()), expiredSubscription.getTopic(), expiredSubscription.getSubscriptionCategoryCode(), null, null, null, null);
			
			Document unsubscriptionDoc;
			try {
				unsubscriptionDoc = SubscriptionNotificationDocumentBuilderUtils.createUnubscriptionRequest(unsubscription);
				subscriptionsToUnsubscribe.add(unsubscriptionDoc);
			} catch (Exception e) {
				log.error("Unable to creat subscription for: " + expiredSubscription.getId(), e);
			}
			
		});
		
		return subscriptionsToUnsubscribe;
	}

	public String createCamelEmail(Exchange in, @Body ExpiringSubscriptionEmail expiringSubscriptionEmail)
	{
		in.getIn().setHeader("to", expiringSubscriptionEmail.getTo());
		in.getIn().setHeader("subject", expiringSubscriptionEmail.getSubject());
		
		return expiringSubscriptionEmail.getMessageBody();
	}
	
	boolean notifyOfExpiringSubscription(Subscription subscription) {
		boolean isSubscriptionExpiring = false;
		
		if (subscription.getEndDate() == null || BooleanUtils.isNotTrue(subscription.getActive()))
		{
			return false;
		}	
		
		DateTime subscriptionEndDate = subscription.getEndDate();
		DateTime now = new DateTime();
		
		if (now.plusDays(warningDaysBeforeExpiration).isAfter(subscriptionEndDate))
		{
			return true;
		}	
		
		return isSubscriptionExpiring;
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
