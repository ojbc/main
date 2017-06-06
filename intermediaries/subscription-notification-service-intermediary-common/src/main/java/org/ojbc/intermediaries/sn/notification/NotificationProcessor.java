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
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.notification.filter.DefaultNotificationFilterStrategy;
import org.ojbc.intermediaries.sn.notification.filter.NotificationFilterStrategy;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;

/**
 * The abstract base class for topic-specific notification processors...there will be a concrete derivation of this class for each topic.
 *
 */
public abstract class NotificationProcessor {

    private static final Log log = LogFactory.getLog(NotificationProcessor.class);

    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    private NotificationFilterStrategy notificationFilterStrategy = new DefaultNotificationFilterStrategy();
    private EmailEnhancementStrategy emailEnhancementStrategy = new DefaultEmailEnhancementStrategy();
    private boolean consolidateEmailAddresses = false;
    private EmailFormatter emailFormatter = new EmailFormatter.DefaultEmailFormatter();
    
    /**
     * Create the topic-specific notification request from the inbound message
     * @param msg the inbound Camel message
     * @return the request
     * @throws Exception
     */
    abstract protected NotificationRequest makeNotificationRequestFromIncomingMessage(Message msg) throws Exception;

    /**
     * Property to determine the strategy for formatting notification emails.
     * @return the formatter
     */
    public EmailFormatter getEmailFormatter() {
        return emailFormatter;
    }

    /**
     * Property to determine the strategy for formatting notification emails.
     * @param emailFormatter the formatter
     */
    public void setEmailFormatter(EmailFormatter emailFormatter) {
        this.emailFormatter = emailFormatter;
    }

    /**
     * Property to determine if the processor combines the "to" addresses for each subscription into a single email, or multiple emails
     * @return true if single email, false if a separate email for each address (default is false)
     */
    public boolean isConsolidateEmailAddresses() {
        return consolidateEmailAddresses;
    }

    /**
     * Property to determine if the processor combines the "to" addresses for each subscription into a single email, or multiple emails
     * @param consolidateEmailAddresses true if single email, false if a separate email for each address (default is false)
     */
    public void setConsolidateEmailAddresses(boolean consolidateEmailAddresses) {
        this.consolidateEmailAddresses = consolidateEmailAddresses;
    }

    /**
     * Property establishing a filtering strategy for notifications
     * @return
     */
    public NotificationFilterStrategy getNotificationFilterStrategy() {
        return notificationFilterStrategy;
    }

    /**
     * Property establishing a filtering strategy for notifications
     * @param notificationFilterStrategy
     */
    public void setNotificationFilterStrategy(NotificationFilterStrategy notificationFilterStrategy) {
        this.notificationFilterStrategy = notificationFilterStrategy;
    }

    /**
     * Property establishing a strategy for editing of emails sent by the processor
     * @return the strategy
     */
    public EmailEnhancementStrategy getEmailEnhancementStrategy() {
        return emailEnhancementStrategy;
    }

    /**
     * Property establishing a strategy for editing of emails sent by the processor
     * @param emailStrategy the strategy
     */
    public void setEmailEnhancementStrategy(EmailEnhancementStrategy emailEnhancementStrategy) {
        this.emailEnhancementStrategy = emailEnhancementStrategy;
    }
    
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
        log.debug("Entering notify camel method");
        Message inMessage = exchange.getIn();

        NotificationRequest request = makeNotificationRequestFromIncomingMessage(inMessage);
        List<EmailNotification> emailNotifications = processNotificationRequest(request, inMessage);

        if (emailNotifications.size() > 0) {

            if (notificationFilterStrategy == null) {
                notificationFilterStrategy = new DefaultNotificationFilterStrategy();
            }

            if (notificationFilterStrategy.shouldMessageBeFiltered(request)) {
            	log.warn("Notification filter strategy indicates that message should be filtered.");
            	emailNotifications.clear();
            }
        }
        
        if (emailNotifications.size() == 0) {
            log.info("No subscriptions found for subject " + request.getSubjectIdentifiers() + " and event date of " + NotificationBrokerUtils.returnFormattedNotificationEventDate(request.getNotificationEventDate(), request.isNotificationEventDateInclusiveOfTime()));
        }
        
        return emailNotifications;
        
    }

    /**
     * Invoked by Camel to create the actual specific email message sent as the notification.  It assumes that the body of the inbound
     * Exchange object is an individual EmailNotification object, and that a header (NotificationConstants.HEADER_NOTIFICATION_TOPIC) is present in the inbound, containing
     * the topic.  Finally, it expects a header (NotificationConstants.HEADER_EMAIL_BODY) to contain the email template, created earlier in the route.  This method
     * replaces placeholders with actual text pertinent to this notification.
     * 
     * Note that each email is decorated here by the specified email decorator.
     * 
     * Note that it is quite possible that decorators will remove all "to:" addressees from the email.  This means that it will not be possible to send the email.  So the Camel
     * route must test for a null value of the NotificationConstants.TO header, and do not try to email it if this is null.
     * 
     * @param exchange
     */
    public void createNotificationEmail(Exchange exchange) throws Exception {

        EmailNotification emailNotification = (EmailNotification) exchange.getIn().getBody();
        
        emailNotification = emailEnhancementStrategy.enhanceEmail(emailNotification);

        String toAddressees = emailNotification.getToAddressees();
        String ccAddressees = emailNotification.getCcAddressees();
        String bccAddressees = emailNotification.getBccAddressees();
        Set<String> blockedAddressees = emailNotification.getBlockedAddresseeSet();
        
        String emailBody = emailFormatter.getEmailBody(emailNotification);
        String subject = emailFormatter.getEmailSubject(emailNotification);
        
        exchange.getOut().setBody(emailBody);
        exchange.getOut().setHeader(NotificationConstants.HEADER_SUBSCRIBING_SYSTEM_IDENTIFIER, emailNotification.getSubscribingSystemIdentifier());
        exchange.getOut().setHeader(NotificationConstants.HEADER_SUBJECT, subject);
        exchange.getOut().setHeader(NotificationConstants.HEADER_TO, toAddressees);
        if (ccAddressees != null && !ccAddressees.isEmpty()) {
            exchange.getOut().setHeader(NotificationConstants.HEADER_CC, ccAddressees);
        }
        if (bccAddressees != null && !bccAddressees.isEmpty()) {
            exchange.getOut().setHeader(NotificationConstants.HEADER_BCC, bccAddressees);
        }
        exchange.getOut().setHeader(NotificationConstants.HEADER_BLOCKED, blockedAddressees);

    }

    List<EmailNotification> processNotificationRequest(NotificationRequest request, Message inMessage) {

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
        
        // We call this method to remove duplicate subscriptions from the notification list so officers don't get duplicate emails.
        // This was originally for HI Probation where one SID can have multiple cases with the same officer
        List<EmailNotification> emailNotifications = createUniqueNotifications(subscriptions, request);

        return emailNotifications;
        
    }
    
    protected List<EmailNotification> createUniqueNotifications(List<Subscription> subscriptions, NotificationRequest request) {
        
        List<EmailNotification> emailNotifications = new ArrayList<EmailNotification>();

        for (Subscription subscription : subscriptions) {
            
            EmailNotification en = null;

            String subscriptionSubscribingSystemName = subscription.getSubscribingSystemIdentifier();
            
            for (String emailAddress : subscription.getEmailAddressesToNotify()) {
                
                boolean add = true;
                
                for (EmailNotification emailNotification : emailNotifications) {
                    
                    Set<String> emailAddresses = emailNotification.getToAddresseeSet();
                    if (emailAddresses.contains(emailAddress) && emailNotification.getSubscribingSystemIdentifier().equals(subscriptionSubscribingSystemName)) {
                        add = false;
                        break;
                    }
                    
                }
                
                if (add) {
                    if (!consolidateEmailAddresses || (consolidateEmailAddresses && en == null)) {
                        en = new EmailNotification();
                        emailNotifications.add(en);
                    }
                    
                    en.setSubscriptionSubjectIdentifiers(subscription.getSubscriptionSubjectIdentifiers());
                    en.setSubjectName(subscription.getPersonFullName());
                    en.setSubscribingSystemIdentifier(subscriptionSubscribingSystemName);
                    
                    en.setSubscriptionCategoryCode(subscription.getSubscriptionCategoryCode());
                    en.addToAddressee(emailAddress);
                    en.setNotificationRequest(request);
                }
                
            }
            
        }

        log.debug("Original Subscriptions Size: " + subscriptions.size() + " Output email notification size: " + emailNotifications.size());

        return emailNotifications;
        
    }

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

}
