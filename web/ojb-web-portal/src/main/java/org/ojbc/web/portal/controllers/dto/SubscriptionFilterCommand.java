package org.ojbc.web.portal.controllers.dto;

import java.util.Date;


public class SubscriptionFilterCommand {
	
	private String subscriptionStatus;	
	
	private int validationDueWarningDays;
	
	private Date currentDate;
	
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public int getValidationDueWarningDays() {
		return validationDueWarningDays;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public void setValidationDueWarningDays(int validationDueWarningDays) {
		this.validationDueWarningDays = validationDueWarningDays;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	@Override
	public String toString() {
		return "SubscriptionFilterCommand [subscriptionStatus="
				+ subscriptionStatus + ", validationDueWarningDays="
				+ validationDueWarningDays + ", currentDate=" + currentDate
				+ "]";
	}		
	
}
