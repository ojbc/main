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
package org.ojbc.util.model.rapback;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

public class FbiRapbackSubscription implements Serializable{

	private static final long serialVersionUID = 5492996006114314857L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private String fbiSubscriptionId; 
	private Integer stateSubscriptionId; 
	private String rapbackCategory;
	private String subscriptionTerm;
	private LocalDate rapbackExpirationDate;
	private LocalDate rapbackTermDate;
	private LocalDate rapbackStartDate; 
	private Boolean rapbackOptOutInState; 
	private String rapbackActivityNotificationFormat; 
	private String rapbackActivityNotificationFormatDescription; 
	private String ucn;
	private String eventIdentifier;
	private DateTime timestamp;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFbiSubscriptionId() {
		return fbiSubscriptionId;
	}

	public void setFbiSubscriptionId(String fbiSubscriptionId) {
		this.fbiSubscriptionId = fbiSubscriptionId;
	}

	public String getRapbackCategory() {
		return rapbackCategory;
	}

	public void setRapbackCategory(String rapbackCategory) {
		this.rapbackCategory = rapbackCategory;
	}

	public String getSubscriptionTerm() {
		return subscriptionTerm;
	}

	public void setSubscriptionTerm(String subscriptionTerm) {
		this.subscriptionTerm = subscriptionTerm;
	}

	public LocalDate getRapbackExpirationDate() {
		return rapbackExpirationDate;
	}
	
	public String getRapbackExpirationDateString() {
		return Optional.ofNullable(rapbackExpirationDate)
					.map(date-> date.format(formatter))
					.orElse("");
	}


	public void setRapbackExpirationDate(LocalDate rapbackExpirationDate) {
		this.rapbackExpirationDate = rapbackExpirationDate;
	}

	public LocalDate getRapbackStartDate() {
		return rapbackStartDate;
	}
	
	public String getRapbackStartDateString() {
		return Optional.ofNullable(rapbackStartDate)
					.map(date-> date.format(formatter))
					.orElse("");
	}

	public void setRapbackStartDate(LocalDate rapbackStartDate) {
		this.rapbackStartDate = rapbackStartDate;
	}

	public Boolean getRapbackOptOutInState() {
		return rapbackOptOutInState;
	}

	public void setRapbackOptOutInState(Boolean rapbackOptOutInState) {
		this.rapbackOptOutInState = rapbackOptOutInState;
	}

	public String getRapbackActivityNotificationFormat() {
		return rapbackActivityNotificationFormat;
	}

	public void setRapbackActivityNotificationFormat(
			String rapbackActivityNotificationFormat) {
		this.rapbackActivityNotificationFormat = rapbackActivityNotificationFormat;
	}

	public String getUcn() {
		return ucn;
	}

	public void setUcn(String ucn) {
		this.ucn = ucn;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public LocalDate getRapbackTermDate() {
		return rapbackTermDate;
	}
	
	public String getRapbackTermDateString() {
		return Optional.ofNullable(rapbackTermDate)
					.map(date-> date.format(formatter))
					.orElse("");
	}

	public void setRapbackTermDate(LocalDate rapbackTermDate) {
		this.rapbackTermDate = rapbackTermDate;
	}

	public Integer getStateSubscriptionId() {
		return stateSubscriptionId;
	}

	public void setStateSubscriptionId(Integer stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
	}
	
	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = Integer.valueOf(stateSubscriptionId);
	}

	public String getRapbackActivityNotificationFormatDescription() {
		return rapbackActivityNotificationFormatDescription;
	}

	public void setRapbackActivityNotificationFormatDescription(
			String rapbackActivityNotificationFormatDescription) {
		this.rapbackActivityNotificationFormatDescription = rapbackActivityNotificationFormatDescription;
	}

	public String getEventIdentifier() {
		return eventIdentifier;
	}

	public void setEventIdentifier(String eventIdentifier) {
		this.eventIdentifier = eventIdentifier;
	}

}
