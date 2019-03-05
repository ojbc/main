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

public class ValidationRequest {

	private Integer subscriptionValidationId;
	
	private String stateSubscriptionId;
	
	private LocalDate validationDueDate;
	
	private Integer userInfoFK;
	
	public Integer getSubscriptionValidationId() {
		return subscriptionValidationId;
	}

	public void setSubscriptionValidationId(Integer subscriptionValidationId) {
		this.subscriptionValidationId = subscriptionValidationId;
	}

	public String getStateSubscriptionId() {
		return stateSubscriptionId;
	}

	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
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

	public Integer getUserInfoFK() {
		return userInfoFK;
	}

	public void setUserInfoFK(Integer userInfoFK) {
		this.userInfoFK = userInfoFK;
	}

}
