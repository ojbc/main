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

import java.util.List;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
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
	
	
	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}
}
