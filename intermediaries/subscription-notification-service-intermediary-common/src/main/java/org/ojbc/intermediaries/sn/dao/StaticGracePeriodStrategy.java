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
