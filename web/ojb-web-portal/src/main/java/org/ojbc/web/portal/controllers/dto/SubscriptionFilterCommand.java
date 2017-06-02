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
