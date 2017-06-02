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

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * A strategy that sets the grace period with a start date of the validation due date (as determined by a passed-in ValidationDueDateStrategy) or subscription end date, and an end date some 
 * static number of days beyond that.  A grace period "start" date would be the day after a subscription end date or the day after a validation due date  
 * whichever of these dates come first.
 */
public class StaticGracePeriodStrategy implements GracePeriodStrategy {

    private ValidationDueDateStrategy validationDueDateStrategy;
    private int gracePeriodDays;

    public int getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(int gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    public StaticGracePeriodStrategy(ValidationDueDateStrategy validationDueDateStrategy) {
        this.validationDueDateStrategy = validationDueDateStrategy;
    }

    @Override
    public Interval getGracePeriod(Subscription subscription) {
        Interval ret = null;
        
        DateTime validationDueDate = validationDueDateStrategy.getValidationDueDate(subscription);
        DateTime subscriptionEndDate = subscription.getEndDate();
        DateTime gracePeriodStart = null;
        
        if (subscriptionEndDate == null && validationDueDate == null)
        {
        	return null;
        } 
        
        if (subscriptionEndDate == null && validationDueDate != null)
        {
        	gracePeriodStart = validationDueDateStrategy.getValidationDueDate(subscription);
        } 
        
        if (subscriptionEndDate != null && validationDueDate == null)
        {
        	gracePeriodStart = subscription.getEndDate();
        }	
        
        if (subscriptionEndDate != null && validationDueDate != null)
        {
        	if (subscriptionEndDate.isBefore(validationDueDate))
        	{
        		gracePeriodStart = subscription.getEndDate();
        	}	
        	else
        	{
        		gracePeriodStart = validationDueDateStrategy.getValidationDueDate(subscription);	
        	}
        }
        	
        if (gracePeriodStart != null) {
            ret = new Interval(gracePeriodStart.plusDays(1), gracePeriodStart.plusDays(gracePeriodDays + 1));
        }
        
        return ret;
    }

}
