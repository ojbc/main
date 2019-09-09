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
package org.ojbc.audit.enhanced.dao.model;

import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SubscriptionAction {

	public static final String SUBSCRIBE_ACTION="subscribe";
	
	public static final String SUBSCRIPTION_MODIFICATION_ACTION="subscription_modification";
	
	public static final String UNSUBSCRIBE_ACTION="unsubscribe";
	
	public static final String VALIDATION_ACTION="validate";
	
	private Integer subscriptionActionId;
	
	private String stateSubscriptionId;
	
	private String fbiSubscriptionId;
	
	private String action;
	
	private Integer userInfoFK;
	
	private LocalDate validationDueDate;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private boolean successIndicator;

	public Integer getSubscriptionActionId() {
		return subscriptionActionId;
	}

	public void setSubscriptionActionId(Integer subscriptionActionId) {
		this.subscriptionActionId = subscriptionActionId;
	}

	public String getStateSubscriptionId() {
		return stateSubscriptionId;
	}

	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
	}

	public String getFbiSubscriptionId() {
		return fbiSubscriptionId;
	}

	public void setFbiSubscriptionId(String fbiSubscriptionId) {
		this.fbiSubscriptionId = fbiSubscriptionId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getUserInfoFK() {
		return userInfoFK;
	}

	public void setUserInfoFK(Integer userInfoFK) {
		this.userInfoFK = userInfoFK;
	}

	public LocalDate getValidationDueDate() {
		return validationDueDate;
	}

	public void setValidationDueDate(LocalDate validationDueDate) {
		this.validationDueDate = validationDueDate;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean isSuccessIndicator() {
		return successIndicator;
	}

	public void setSuccessIndicator(boolean successIndicator) {
		this.successIndicator = successIndicator;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
