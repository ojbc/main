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

import javax.sql.DataSource;

import org.apache.camel.Body;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CivilEmailNotificationFilterProcessor implements EmailEnhancementStrategy {
	
	private String civilNotificationDefaultEmailAddress="administrator@local.gov";
	
	private static final Log log = LogFactory.getLog( CivilEmailNotificationFilterProcessor.class );
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public EmailNotification enhanceEmail(@Body EmailNotification emailNotification) {
		Subscription subscription = emailNotification.getSubscription();
		
		log.debug("Email Notification: " + emailNotification);
		log.debug("Subscription: " + subscription);
		
		String subscriptionCategoryCode = subscription.getSubscriptionCategoryCode();

		if (subscriptionCategoryCode.equals("F") || subscriptionCategoryCode.equals("J") || subscriptionCategoryCode.equals("I") || subscriptionCategoryCode.equals("S"))
		{	
			emailNotification.removeAllToEmailAddresses();
			
			List<String> emailAddressesToAdd = returnEmailAddresses(subscription);
            
            emailNotification.removeAllToEmailAddresses();
            
            for (String emailAddress : emailAddressesToAdd)
            {
            	emailNotification.addToAddressee(emailAddress);
            }	
		}
		
		return emailNotification;
	}
	
	public List<String> returnEmailAddresses(Subscription subscription)
	{
		String subscriptionCategoryCode = subscription.getSubscriptionCategoryCode();
		String subscriptionId = subscription.getSubscriptionIdentifier();

		List<String> emailAddressesToAdd = new ArrayList<String>();
		
		if (subscriptionCategoryCode.equals("F") || subscriptionCategoryCode.equals("J") || subscriptionCategoryCode.equals("I") || subscriptionCategoryCode.equals("S"))
		{	
            emailAddressesToAdd = returnAgencyProfileEmailForSubscription(subscriptionId, subscriptionCategoryCode);
            
            if (emailAddressesToAdd.size() == 0)
            {
            	log.error("No email addresses for found for subscription ID: " + subscriptionId + ", and category: " + subscriptionCategoryCode +", using default email address: " + civilNotificationDefaultEmailAddress);
            	emailAddressesToAdd.add(civilNotificationDefaultEmailAddress);
            }	
		}

		return emailAddressesToAdd;
	}
	
	
    public List<String> returnAgencyProfileEmailForSubscription(String subscriptionId, String subscriptionCategory)
    {
		String sql = "select ace.AGENCY_EMAIL from subscription s, subscription_owner so, agency_profile ap, agency_contact_email ace, AGENCY_CONTACT_EMAIL_JOINER acej,"
				+ " AGENCY_EMAIL_CATEGORY aec "
				+ " where s.SUBSCRIPTION_OWNER_ID = so.SUBSCRIPTION_OWNER_ID"
				+ " and so.AGENCY_ID = ap.AGENCY_ID"
				+ " and ap.AGENCY_ID = ace.AGENCY_ID"
				+ " and ace.AGENCY_CONTACT_EMAIL_ID = acej.AGENCY_CONTACT_EMAIL_ID"
				+ " and aec.AGENCY_EMAIL_CATEGORY_ID = acej.AGENCY_EMAIL_CATEGORY_ID"
				+ " and s.id=? and aec.code=?";
		
        List<String> emailAddresses = this.jdbcTemplate.queryForList(sql, String.class, new Object[]{subscriptionId, subscriptionCategory});
    	
    	return emailAddresses;
    }
	
	public String getCivilNotificationDefaultEmailAddress() {
		return civilNotificationDefaultEmailAddress;
	}

	public void setCivilNotificationDefaultEmailAddress(
			String civilNotificationDefaultEmailAddress) {
		this.civilNotificationDefaultEmailAddress = civilNotificationDefaultEmailAddress;
	}

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
