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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.model.SubscriptionCategoryCode;
import org.ojbc.util.model.rapback.Subscription;

public class SubscriptionCategoryValidationDueDateStrategy implements ValidationDueDateStrategy {

	private int criminalSupervisionLengthInYears =5;
	private int criminalInvestigationLengthInYears =1;
	private int civilLengthInYears = 5;
    
    /**
     * List of exempt subscription owners who don't need validation due dates
     */
    private List<String> exemptSubscriptionOwners;


    /**
     * Default constructor so exempt list property not required
     */
    public SubscriptionCategoryValidationDueDateStrategy ()
    {
    	
    }
    
    /**
     * This constructor takes a comma separated list of exempt owners for comparison later
     */
    public SubscriptionCategoryValidationDueDateStrategy (String exemptSubscriptionOwnersAsCommaSeperatedList) {
		
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
    	String subscriptionCategoryCode = request.getReasonCategoryCode();
    	
    	String requestEndDateString = request.getEndDateString();
    	
    	LocalDate endDate = null;
    	
    	if (StringUtils.isNotBlank(requestEndDateString))
    	{
    		//ISO_LOCAL_DATE
    		endDate = LocalDate.parse(requestEndDateString);
    	}
    		
    	DateTime ret = getCommonValidationDueDate(validationDate, endDate,
				subscriptionOwner, subscriptionCategoryCode, true);
        
        return ret;
    }
    
	@Override
	public DateTime getValidationDueDate(Subscription subscription,
			LocalDate validationDate) {
    	String subscriptionOwner = subscription.getSubscriptionOwner();
    	String subscriptionCategoryCode = subscription.getSubscriptionCategoryCode();
    	
    	DateTime ret = getCommonValidationDueDate(validationDate, null,
				subscriptionOwner, subscriptionCategoryCode, false);
        
        return ret;	
    }

	private DateTime getCommonValidationDueDate(LocalDate validationDate, LocalDate subscriptionEndDate,
			String subscriptionOwner, String subscriptionCategoryCode, boolean isSubscriptionRequest) {
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
		LocalDate retAsLocalDate = null;

        if (validationDate != null) {
        	if (StringUtils.isNotBlank(subscriptionCategoryCode))
        	{	
        		if (subscriptionCategoryCode.equals(SubscriptionCategoryCode.CS.name()))
        		{	
        			retAsLocalDate  = validationDate.plusYears(criminalSupervisionLengthInYears);
        			ret = retAsLocalDate.toDateTimeAtCurrentTime();
        		}
        		
        		if (subscriptionCategoryCode.equals(SubscriptionCategoryCode.CI.name()))
        		{	
        			retAsLocalDate = validationDate.plusYears(criminalInvestigationLengthInYears); 
        			ret = retAsLocalDate.toDateTimeAtCurrentTime();
        		}	

        		if (SubscriptionCategoryCode.getCivilCodes().contains(subscriptionCategoryCode))
        		{	
        			retAsLocalDate = validationDate.plusYears(civilLengthInYears);
        			ret = retAsLocalDate.toDateTimeAtCurrentTime();
        		}	

        		if (subscriptionCategoryCode.equals(SubscriptionCategoryCode.CI.name()) || subscriptionCategoryCode.equals(SubscriptionCategoryCode.CS.name()))
        		{	

	        		if (isSubscriptionRequest)
	        		{
	        			if (subscriptionEndDate != null)
	        			{	
	    	    			if (subscriptionEndDate.isBefore(retAsLocalDate))
	    	    			{
	    	    				return subscriptionEndDate.toDateTimeAtCurrentTime();
	    	    			}
	        			}
	        		}
	        	}
        	}
        	
        }
        
        return ret;
    }

	public int getCriminalSupervisionLengthInYears() {
		return criminalSupervisionLengthInYears;
	}

	public void setCriminalSupervisionLengthInYears(
			int criminalSupervisionLengthInYears) {
		this.criminalSupervisionLengthInYears = criminalSupervisionLengthInYears;
	}

	public int getCriminalInvestigationLengthInYears() {
		return criminalInvestigationLengthInYears;
	}

	public void setCriminalInvestigationLengthInYears(
			int criminalInvestigationLengthInYears) {
		this.criminalInvestigationLengthInYears = criminalInvestigationLengthInYears;
	}

	public List<String> getExemptSubscriptionOwners() {
		return exemptSubscriptionOwners;
	}

	public void setExemptSubscriptionOwners(List<String> exemptSubscriptionOwners) {
		this.exemptSubscriptionOwners = exemptSubscriptionOwners;
	}

}
