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
package org.ojbc.intermediaries.sn.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.util.helper.OJBCDateUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

final class SubscriptionResultsSetExtractor implements ResultSetExtractor<List<Subscription>>{
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SubscriptionResultsSetExtractor.class);
    
    private ValidationDueDateStrategy validationDueDateStrategy;
    private GracePeriodStrategy gracePeriodStrategy;
    private ValidationExemptionFilter validationExemptionFilter;

    public ValidationExemptionFilter getValidationExemptionFilter() {
        return validationExemptionFilter;
    }

    public void setValidationExemptionFilter(ValidationExemptionFilter validationExemptionFilter) {
        this.validationExemptionFilter = validationExemptionFilter;
    }

    public ValidationDueDateStrategy getValidationDueDateStrategy() {
        return validationDueDateStrategy;
    }

    public void setValidationDueDateStrategy(ValidationDueDateStrategy validationDueDateStrategy) {
        this.validationDueDateStrategy = validationDueDateStrategy;
    }

    public GracePeriodStrategy getGracePeriodStrategy() {
        return gracePeriodStrategy;
    }

    public void setGracePeriodStrategy(GracePeriodStrategy gracePeriodStrategy) {
        this.gracePeriodStrategy = gracePeriodStrategy;
    }

    @Override
	public List<Subscription> extractData(ResultSet rs) throws SQLException, DataAccessException, IllegalStateException {
		
		 Map<Long, Subscription> map = new HashMap<Long, Subscription>();
		
		 while (rs.next()) {
	            Long id = rs.getLong("id");
	            
	            Subscription subscription = map.get(id);
	            
	            //Add subscription top level elements that are in subscription table
	            if (subscription == null)
	            {
	            	subscription = new Subscription();
	            	
	            	subscription.setId(id);
	            	
	            	subscription.setTopic(rs.getString("topic"));
	            		            	
	            	subscription.setSubscriptionCategoryCode(rs.getString("subscription_category_code"));	            	
	            	
	            	subscription.setSubscribingSystemIdentifier(rs.getString("subscribingSystemIdentifier"));
	            	
	            	subscription.setStartDate(new DateTime(rs.getDate("startDate")));
	            	
                    Date endDate = rs.getDate("endDate");
                    if (endDate != null)
                    {
                        subscription.setEndDate(new DateTime(endDate));
                    }   
                    
                    Date lastValidationDate = rs.getDate("lastValidationDate");
                    if (lastValidationDate != null)
                    {
                        subscription.setLastValidationDate(new DateTime(lastValidationDate));
                    }
                    else
                    {
                    	throw new IllegalStateException("Last validation date can not be null");
                    }	
                    
	            	subscription.setSubscriptionOwner(rs.getString("subscriptionOwner"));
	            	
	            	subscription.setSubscriptionOwnerEmailAddress(rs.getString("subscriptionOwnerEmailAddress"));
	            	
	            	subscription.setPersonFullName(rs.getString("subjectName"));
	            	
	            	subscription.setSubscriptionIdentifier(String.valueOf(id));
	            	
	            	if (validationExemptionFilter.requiresValidation(subscription)) {
	            	    subscription.setValidationDueDate(validationDueDateStrategy.getValidationDueDate(subscription));
	            	    subscription.setGracePeriod(gracePeriodStrategy.getGracePeriod(subscription));
	            	} else {
	            	    subscription.setValidationDueDate(null);
	            	    subscription.setGracePeriod(null);
	            	}
	            	
	            	subscription.setAgencyCaseNumber(rs.getString("agency_case_number"));
	            	
	    			String fbiSubscriptionId = rs.getString("fbi_subscription_id"); 
	    			if (StringUtils.isNotBlank(fbiSubscriptionId)){
	    				FbiRapbackSubscription fbiSubscription = new FbiRapbackSubscription();
	    				fbiSubscription.setFbiSubscriptionId(fbiSubscriptionId);
		    			fbiSubscription.setRapbackCategory(rs.getString("rap_back_category_code"));
		    			fbiSubscription.setSubscriptionTerm(rs.getString("rap_back_subscription_term_code"));
		    			fbiSubscription.setRapbackExpirationDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_expiration_date")));
		    			fbiSubscription.setRapbackStartDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_start_date")));
		    			fbiSubscription.setRapbackTermDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_term_date")));
		    			fbiSubscription.setRapbackOptOutInState(rs.getBoolean("rap_back_opt_out_in_state_indicator"));
		    			fbiSubscription.setRapbackActivityNotificationFormat(rs.getString("rap_back_activity_notification_format_code"));
		    			fbiSubscription.setUcn(rs.getString("ucn"));
		    			fbiSubscription.setStateSubscriptionId(rs.getInt("subscription_id"));
		    			fbiSubscription.setTimestamp(OJBCDateUtils.toDateTime(rs.getTimestamp("report_timestamp")));
		    			
		    			subscription.setFbiRapbackSubscription(fbiSubscription);
	    			}
	            	map.put(id, subscription);
	            	
	            }	
	            
	            //Add email addresses that are in notification mechanism table
	            Set<String> emailAddresses = subscription.getEmailAddressesToNotify();
	            
	            if (emailAddresses == null)
	            {
	            	emailAddresses = new LinkedHashSet<String>();
	            	subscription.setEmailAddressesToNotify(emailAddresses);
	            }	
	            
	            emailAddresses.add(rs.getString("notificationAddress"));
	            
	            Map<String, String> subscriptionSubjectIdentifiers = subscription.getSubscriptionSubjectIdentifiers();

	            if (subscriptionSubjectIdentifiers == null)
	            {
	            	subscriptionSubjectIdentifiers = new HashMap<String, String>();
	            	subscription.setSubscriptionSubjectIdentifiers(subscriptionSubjectIdentifiers);
	            }	
	            
	            String identifierName = rs.getString("identifierName");
	            String identifierValue = rs.getString("identifierValue");
	            
	            subscriptionSubjectIdentifiers.put(identifierName, identifierValue);
	            
	            if (identifierName.equals(SubscriptionNotificationConstants.LAST_NAME))
	            {
	            	subscription.setPersonLastName(identifierValue);
	            }	

	            if (identifierName.equals(SubscriptionNotificationConstants.FIRST_NAME))
	            {
	            	subscription.setPersonFirstName(identifierValue);
	            }	

	            if (identifierName.equals(SubscriptionNotificationConstants.DATE_OF_BIRTH))
	            {
	            	subscription.setDateOfBirth(identifierValue);
	            }	

		 }        
		
		 return new ArrayList<Subscription>(map.values());
	}

}
