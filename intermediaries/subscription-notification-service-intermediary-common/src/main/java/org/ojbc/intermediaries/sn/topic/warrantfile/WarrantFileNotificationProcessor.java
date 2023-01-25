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
package org.ojbc.intermediaries.sn.topic.warrantfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.notification.filter.DefaultNotificationFilterStrategy;
import org.ojbc.intermediaries.sn.topic.rapback.RapbackNotificationRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.model.rapback.Subscription;

public class WarrantFileNotificationProcessor extends NotificationProcessor{
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

	private static final Log log = LogFactory.getLog(NotificationProcessor.class);
	
	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new WarrantFileNotificationRequest(msg);
	}
	
	@Override
	public List<EmailNotification> processNotificationRequest(NotificationRequest request, Message inMessage) {

    	//Search for subscription using the subject identifiers
        List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForSubscriptionsMatchingNotificationRequest(request);

        //If no subscriptions found, try searching using the context defined alternate subject identifiers
        //We break the search as soon as we get a subscription from a set of subject identifiers
        if (subscriptions.size() == 0 && request.getAlternateSubjectIdentifiers() != null)
        {
        	log.info("No subscriptions found using primary subject identifiers: " + request.getSubjectIdentifiers().toString()  +  ", try with alternate identifiers");
        	
        	for (Map<String, String> alternateSubjectIdentifiers : request.getAlternateSubjectIdentifiers())
        	{
        		log.info("Search using alternate identifers: "  + alternateSubjectIdentifiers.toString());
        		
        		subscriptions = subscriptionSearchQueryDAO.searchForSubscriptionsMatchingNotificationRequest(request, alternateSubjectIdentifiers);
        		
        		if (subscriptions.size() != 0)
        		{
        			break;
        		}	
        		
        	}	
        }
        
        for(Subscription subscription : subscriptions) {
        	if(StringUtils.isNotEmpty(request.getNotifyingAgencyOri())) {
        		subscription.setOri(request.getNotifyingAgencyOri());
        	}
        }
        
        // We call this method to remove duplicate subscriptions from the notification list so officers don't get duplicate emails.
        // This was originally for HI Probation where one SID can have multiple cases with the same officer
        List<EmailNotification> emailNotifications = createUniqueNotifications(subscriptions, request);

        return emailNotifications;
        
    }
	
	@Override
	 /**
    * Invoked by Camel to find the subscriptions for the inbound message.  It places these in a header (NotificationConstants.HEADER_EMAIL_NOTIFICATIONS) as a List of EmailNotification
    * objects.  This method also filters out any notifications, per the provided NotificationFilterStrategy (using a null object / no-op strategy by default).  In doing so, it automatically
    * removes duplicate notifications, so that a given email address only receives one email per notification event, even if multiple subscriptions for that email address are a match
    * for that event.
    * 
    * It also observes the
    * consolidateEmailAddresses property, which controls whether all the "to" addresses for a given subscription are sent in the same email, or in separate emails with one
    * "to" addressee each.  In implements this functionality in a "first match" manner, with respect to handling of duplicates.  If multiple subscriptions' email addresses match a notification
    * event, then the first subscription encountered is the one that generates the email.  If that subscription has multiple "to" addresses, then the recipient will receive the email
    * with multiple "to" addressees.  If the first matching subscription, on the other hand, contains only one email, then the recipient will receive an email with only him/herself as
    * the "to" addressee, and that "to" addressee will be removed from subsequent matching email notifications.
    * 
    * @param exchange the inbound exchange containing the notification request
    * @throws Exception
    */
   public List<EmailNotification> findSubscriptionsForNotification(Exchange exchange) throws Exception {
       Message inMessage = exchange.getIn();
       
       String ori = (String) exchange.getMessage().getHeader("ori");

       NotificationRequest request = makeNotificationRequestFromIncomingMessage(inMessage);
       List<EmailNotification> emailNotifications = processNotificationRequest(request, inMessage, ori);

       if (emailNotifications.size() > 0) {

           if (getNotificationFilterStrategy() == null) {
           	setNotificationFilterStrategy(new DefaultNotificationFilterStrategy());
           }

           if (getNotificationFilterStrategy().shouldMessageBeFiltered(request)) {
           	emailNotifications.clear();
           }
       }
       
       if (emailNotifications.size() == 0) {
           log.info("No subscriptions found for subject " + request.getSubjectIdentifiers() + " and event date of " + NotificationBrokerUtils.returnFormattedNotificationEventDate(request.getNotificationEventDate(), request.isNotificationEventDateInclusiveOfTime()));
       }
       
       return emailNotifications;
       
   }
	
	public List<EmailNotification> processNotificationRequest(NotificationRequest request, Message inMessage, String ori) {

   	//Search for subscription using the subject identifiers
       List<Subscription> subscriptions = getSubscriptionSearchQueryDAO().searchForSubscriptionsMatchingNotificationRequest(request);

       //If no subscriptions found, try searching using the context defined alternate subject identifiers
       //We break the search as soon as we get a subscription from a set of subject identifiers
       if (subscriptions.size() == 0 && request.getAlternateSubjectIdentifiers() != null)
       {
       	log.info("No subscriptions found using primary subject identifiers: " + request.getSubjectIdentifiers().toString()  +  ", try with alternate identifiers");
       	
       	for (Map<String, String> alternateSubjectIdentifiers : request.getAlternateSubjectIdentifiers())
       	{
       		log.info("Search using alternate identifers: "  + alternateSubjectIdentifiers.toString());
       		
       		subscriptions = getSubscriptionSearchQueryDAO().searchForSubscriptionsMatchingNotificationRequest(request, alternateSubjectIdentifiers);
       		
       		if (subscriptions.size() != 0)
       		{
       			break;
       		}	
       		
       	}	
       }	
       
       // We call this method to remove duplicate subscriptions from the notification list so officers don't get duplicate emails.
       // This was originally for HI Probation where one SID can have multiple cases with the same officer
       List<EmailNotification> emailNotifications = createUniqueNotifications(subscriptions, request, ori);

       return emailNotifications;
       
   }
	
	protected List<EmailNotification> createUniqueNotifications(List<Subscription> subscriptions, NotificationRequest request, String ori) {
       
       List<EmailNotification> emailNotifications = new ArrayList<EmailNotification>();

       for (Subscription subscription : subscriptions) {
           
           EmailNotification en = null;

           String subscriptionSubscribingSystemName = subscription.getSubscribingSystemIdentifier();
           
           for (String emailAddress : subscription.getEmailAddressesToNotify()) {
               
               en = createEmailNotification(request, emailNotifications,
						subscription, en, subscriptionSubscribingSystemName,
						emailAddress, ori);
               
           }
           
           if (isSendNotificationToSubscriptionOwner())
           {
               en = createEmailNotification(request, emailNotifications,
						subscription, en, subscriptionSubscribingSystemName,
						subscription.getSubscriptionOwnerEmailAddress(), ori);
           }	
           
       }

       log.debug("Original Subscriptions Size: " + subscriptions.size() + " Output email notification size: " + emailNotifications.size());

       return emailNotifications;
       
   }
	
	private EmailNotification createEmailNotification(
			NotificationRequest request,
			List<EmailNotification> emailNotifications,
			Subscription subscription, EmailNotification en,
			String subscriptionSubscribingSystemName, String emailAddress, String ori) {
		boolean add = true;
		
		for (EmailNotification emailNotification : emailNotifications) {
		    
		    Set<String> emailAddresses = emailNotification.getToAddresseeSet();
		    if (emailAddresses.contains(emailAddress) && emailNotification.getSubscribingSystemIdentifier().equals(subscriptionSubscribingSystemName)) {
		        add = false;
		        break;
		    }
		    
		}
		
		if (add) {
		    if (!isConsolidateEmailAddresses() || (isConsolidateEmailAddresses() && en == null)) {
		        en = new EmailNotification();
		        emailNotifications.add(en);
		    }
		    
		    en.setSubscriptionSubjectIdentifiers(subscription.getSubscriptionSubjectIdentifiers());
		    en.setSubjectName(subscription.getPersonFullName());
		    en.setSubscribingSystemIdentifier(subscriptionSubscribingSystemName);
		    
		    en.setSubscriptionCategoryCode(subscription.getSubscriptionCategoryCode());
		    en.addToAddressee(emailAddress);
		    en.setNotificationRequest(request);
		    subscription.setOri(ori);
		    en.setSubscription(subscription);
		    
		    if (request instanceof RapbackNotificationRequest)
		    {
		    	en.setTriggeringEvents(((RapbackNotificationRequest)request).getTriggeringEvents());
		    }	
		}
		return en;
	}
	
	
	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}
}
