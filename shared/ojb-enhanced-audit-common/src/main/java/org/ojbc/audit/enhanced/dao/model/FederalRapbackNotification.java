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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
@JsonIgnoreProperties(ignoreUnknown = true)
public class FederalRapbackNotification {

	private Integer federalRapbackNotificationId;
	
	private String pathToNotificationFile;
	
	private String stateSubscriptionId;
	
	private String originalIdentifier;
	
	private String updatedIdentifier;
	
	private String rapBackEventText;

	private String transactionType;
	
	private String recordRapBackActivityNotificationId;
	
	private List<String> triggeringEvents;
	
	@JsonFormat(shape = Shape.STRING)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime notificationRecievedTimestamp;

	public Integer getFederalRapbackNotificationId() {
		return federalRapbackNotificationId;
	}

	public void setFederalRapbackNotificationId(Integer federalRapbackNotificationId) {
		this.federalRapbackNotificationId = federalRapbackNotificationId;
	}

	public String getStateSubscriptionId() {
		return stateSubscriptionId;
	}

	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
	}

	public String getOriginalIdentifier() {
		return originalIdentifier;
	}

	public void setOriginalIdentifier(String originalIdentifier) {
		this.originalIdentifier = originalIdentifier;
	}

	public String getUpdatedIdentifier() {
		return updatedIdentifier;
	}

	public void setUpdatedIdentifier(String updatedIdentifier) {
		this.updatedIdentifier = updatedIdentifier;
	}

	public String getRapBackEventText() {
		return rapBackEventText;
	}

	public void setRapBackEventText(String rapBackEventText) {
		this.rapBackEventText = rapBackEventText;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public LocalDateTime getNotificationRecievedTimestamp() {
		return notificationRecievedTimestamp;
	}

	public void setNotificationRecievedTimestamp(
			LocalDateTime notificationRecievedTimestamp) {
		this.notificationRecievedTimestamp = notificationRecievedTimestamp;
	}

	public String getPathToNotificationFile() {
		return pathToNotificationFile;
	}

	public void setPathToNotificationFile(String pathToNotificationFile) {
		this.pathToNotificationFile = pathToNotificationFile;
	}

	public List<String> getTriggeringEvents() {
		return triggeringEvents;
	}
	
	public String getTriggeringEventsString() {
		if (triggeringEvents == null || triggeringEvents.isEmpty()){
			return StringUtils.EMPTY; 
		}
		else{
			return StringUtils.join(
					triggeringEvents.stream().filter(Objects::nonNull).collect(Collectors.toList()), 
					"<br/>");
		}
	}
	
	public void setTriggeringEvents(List<String> triggeringEvents) {
		this.triggeringEvents = triggeringEvents;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getRecordRapBackActivityNotificationId() {
		return recordRapBackActivityNotificationId;
	}

	public void setRecordRapBackActivityNotificationId(
			String recordRapBackActivityNotificationId) {
		this.recordRapBackActivityNotificationId = recordRapBackActivityNotificationId;
	}
	
}
