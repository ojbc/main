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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;

/**
 * A due date strategy that determines the due date based on a fixed number of days from the last validated date.
 */
public class StaticValidationDueDateStrategy implements ValidationDueDateStrategy {
    
    private int validDays;
    
    /**
     * List of exempt subscription owners who don't need validation due dates
     */
    private List<String> exemptSubscriptionOwners;

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }

    /**
     * Default constructor so exempt list property not required
     */
    public StaticValidationDueDateStrategy ()
    {
    	
    }
    
    /**
     * This constructor takes a comma separated list of exempt owners for comparison later
     */
    public StaticValidationDueDateStrategy (String exemptSubscriptionOwnersAsCommaSeperatedList) {
		
		if (StringUtils.isNotBlank(exemptSubscriptionOwnersAsCommaSeperatedList))
		{	
			exemptSubscriptionOwners = Arrays.asList(exemptSubscriptionOwnersAsCommaSeperatedList.split(","));
		}
		
	}
    
    /**
     * This method will return the validation due date which is the last validated date from the
     * subscription plus a configurable number of days.
     * 
     * If the subscription owner is in the exempt list, it will return null indicating that 
     * there is no validation due date.
     */
    @Override
    public DateTime getValidationDueDate(SubscriptionRequest request, LocalDate validationDate)  {
    	
    	String subscriptionOwner = request.getSubscriptionOwner();
    	
    	DateTime ret = getCommonValidationDueDate(validationDate,
				subscriptionOwner);
        
        return ret;
    }

	@Override
	public DateTime getValidationDueDate(Subscription subscription,
			LocalDate validationDate) {
		
    	String subscriptionOwner = subscription.getSubscriptionOwner();
    	
    	DateTime ret = getCommonValidationDueDate(validationDate,
				subscriptionOwner);
        
        return ret;
	}
    
	private DateTime getCommonValidationDueDate(LocalDate validationDate,
			String subscriptionOwner) {
		//If an exempt subscriber list is defined, see if the subscription owner is in that list.
    	//The 'exempt' subscription owner is allowed to have no validation due date
		if (exemptSubscriptionOwners != null)
		{
			//determine if submitting ORI exists in the list of authorized ORIs
			for(String s:exemptSubscriptionOwners){
				if(s.replaceAll("\\s","").equalsIgnoreCase(subscriptionOwner)){
					return null;
				}
			}
		}	
    	
		DateTime ret = null;

        if (validationDate != null) {
            ret = validationDate.plusDays(validDays).toDateTimeAtCurrentTime();
        }
        
        return ret;
    }


}
