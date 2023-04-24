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
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

final class SubscriptionResultsSetExtractor implements ResultSetExtractor<List<Subscription>>{
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SubscriptionResultsSetExtractor.class);
    
    private GracePeriodStrategy gracePeriodStrategy;
    private ValidationExemptionFilter validationExemptionFilter;

    public ValidationExemptionFilter getValidationExemptionFilter() {
        return validationExemptionFilter;
    }

    public void setValidationExemptionFilter(ValidationExemptionFilter validationExemptionFilter) {
        this.validationExemptionFilter = validationExemptionFilter;
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
	            	
	            	subscription.setActive(rs.getBoolean("active"));
	       
	            	String state = rs.getString("state");
	            	if(StringUtils.isNotEmpty(state) && state != null) {
	            		subscription.setState(state);
	            	}
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
                    
                    Date creationDate = rs.getDate("creationDate");
                    if (creationDate != null)
                    {
                        subscription.setCreationDate(new DateTime(creationDate));
                    }
                    else
                    {
                    	throw new IllegalStateException("Creation date can not be null");
                    }	
                    
                    Date lastUpdatedDate = rs.getDate("lastUpdatedDate");
                    if (lastUpdatedDate != null)
                    {
                        subscription.setLastUpdatedDate(new DateTime(lastUpdatedDate));
                    }
                    else
                    {
                    	throw new IllegalStateException("Last updated date can not be null");
                    }	                    

	            	if (validationExemptionFilter.requiresValidation(subscription)) {
	                    Date validationDueDate = rs.getDate("validationDueDate");
	                    if (validationDueDate != null ||subscription.getEndDate() != null )
	                    {
	                        subscription.setValidationDueDate(new DateTime(validationDueDate));
	                        subscription.setGracePeriod(gracePeriodStrategy.getGracePeriod(subscription));
	                    }   	            	
	                } else {
	            	    subscription.setValidationDueDate(null);
	            	    subscription.setGracePeriod(null);
	            	}
	            	
	            	subscription.setSubscriptionOwnerFk(rs.getInt("SUBSCRIPTION_OWNER_ID"));
	            	
	            	subscription.setSubscriptionOwner(rs.getString("subscriptionOwner"));
	            	
	            	subscription.setSubscriptionOwnerEmailAddress(rs.getString("subscriptionOwnerEmailAddress"));
	            	
	            	subscription.setSubscriptionOwnerFirstName(rs.getString("subscriptionOwnerFirstName"));
	            	
	            	subscription.setSubscriptionOwnerLastName(rs.getString("subscriptionOwnerLastName"));
	            	
	            	subscription.setPersonFullName(rs.getString("subjectName"));
	            	
	            	subscription.setSubscriptionIdentifier(String.valueOf(id));
	            	
	            	subscription.setAgencyCaseNumber(rs.getString("agency_case_number"));
	            	subscription.setOri(rs.getString("ori"));
	            	subscription.setAgencyName(rs.getString("agency_name"));
	            	
	            	
	            	try{
	            		Date subscriptionDate = rs.getDate("last_match_date");
	            		if(subscriptionDate != null) {
	            			subscription.setLastMatchDate(new DateTime(subscriptionDate));
	            		}
	            	}
	            	catch(Exception ex) {
	            		log.info("Unable to get last match date.");
	            	}
	            	
	    			String fbiSubscriptionId = rs.getString("fbi_subscription_id"); 
	    			if (StringUtils.isNotBlank(fbiSubscriptionId)){
	    				FbiRapbackSubscription fbiSubscription = new FbiRapbackSubscription();
	    				fbiSubscription.setFbiSubscriptionId(fbiSubscriptionId);
		    			fbiSubscription.setRapbackCategory(rs.getString("rap_back_category_code"));
		    			fbiSubscription.setSubscriptionTerm(rs.getString("rap_back_subscription_term_code"));
		    			fbiSubscription.setRapbackExpirationDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_expiration_date")));
		    			fbiSubscription.setRapbackStartDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_start_date")));
		    			fbiSubscription.setRapbackTermDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_term_date")));
		    			fbiSubscription.setRapbackOptOutInState(rs.getBoolean("rap_back_opt_out_in_state_indicator"));
		    			fbiSubscription.setRapbackActivityNotificationFormat(rs.getString("rap_back_activity_notification_format_code"));
		    			fbiSubscription.setUcn(rs.getString("ucn"));
		    			fbiSubscription.setStateSubscriptionId(rs.getInt("subscription_id"));
		    			fbiSubscription.setTimestamp(OJBCDateUtils.toDateTime(rs.getTimestamp("report_timestamp")));
		    			
		    			subscription.setFbiRapbackSubscription(fbiSubscription);
		    			subscription.setFbiId(fbiSubscriptionId);
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
	            
	            String propertyName=rs.getString("propertyName");
	            String propertyValue=rs.getString("propertyValue");
	            
	            if(identifierName.equals("OffenderID")) {
	            	subscription.setOtherIdentificationId(propertyValue);
	            }
	            
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
	            if (identifierName.equals("race"))
	            {
	            	subscription.setRace(identifierValue);
	            }
	            if (identifierName.equals("sex"))
	            {
	            	subscription.setSex(identifierValue);
	            }
	            if (identifierName.equals("fbiNum")) {
	            	subscription.setFbiId(identifierValue);
	            }
	            
	        

		 }        
		
		 return new ArrayList<Subscription>(map.values());
	}

}
