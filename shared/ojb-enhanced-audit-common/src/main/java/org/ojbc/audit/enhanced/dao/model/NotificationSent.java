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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ojbc.util.rest.jackson.LocalDateTimeDeserializer;
import org.ojbc.util.rest.jackson.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationSent {

	private Integer notificationSentId;
	
	private String subscriptionType;
	
	private String topic;
	
	private String subscriptionIdentifier;
	
	private String subscriptionOwnerAgency;
	
	private String subscriptionOwnerAgencyType;

	private String subscriptionOwnerEmailAddress;
	
	private String subscriptionOwner;
	
	private String notifyingSystemName;
	
	private String subscribingSystemIdentifier;
	
	private String subscriptionSubject;
	
	private List<String> triggeringEvents;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime notificationSentTimestamp;

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

	public Integer getNotificationSentId() {
		return notificationSentId;
	}

	public void setNotificationSentId(Integer notificationSentId) {
		this.notificationSentId = notificationSentId;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSubscriptionIdentifier() {
		return subscriptionIdentifier;
	}

	public void setSubscriptionIdentifier(String subscriptionIdentifier) {
		this.subscriptionIdentifier = subscriptionIdentifier;
	}

	public String getSubscriptionOwnerAgency() {
		return subscriptionOwnerAgency;
	}

	public void setSubscriptionOwnerAgency(String subscriptionOwnerAgency) {
		this.subscriptionOwnerAgency = subscriptionOwnerAgency;
	}

	public String getSubscriptionOwnerEmailAddress() {
		return subscriptionOwnerEmailAddress;
	}

	public void setSubscriptionOwnerEmailAddress(
			String subscriptionOwnerEmailAddress) {
		this.subscriptionOwnerEmailAddress = subscriptionOwnerEmailAddress;
	}

	public String getSubscriptionOwner() {
		return subscriptionOwner;
	}

	public void setSubscriptionOwner(String subscriptionOwner) {
		this.subscriptionOwner = subscriptionOwner;
	}

	public LocalDateTime getNotificationSentTimestamp() {
		return notificationSentTimestamp;
	}

	public void setNotificationSentTimestamp(LocalDateTime notificationSentTimestamp) {
		this.notificationSentTimestamp = notificationSentTimestamp;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getSubscriptionOwnerAgencyType() {
		return subscriptionOwnerAgencyType;
	}

	public void setSubscriptionOwnerAgencyType(String subscriptionOwnerAgencyType) {
		this.subscriptionOwnerAgencyType = subscriptionOwnerAgencyType;
	}

	public String getNotifyingSystemName() {
		return notifyingSystemName;
	}

	public void setNotifyingSystemName(String notifyingSystemName) {
		this.notifyingSystemName = notifyingSystemName;
	}

	public String getSubscribingSystemIdentifier() {
		return subscribingSystemIdentifier;
	}

	public void setSubscribingSystemIdentifier(String subscribingSystemIdentifier) {
		this.subscribingSystemIdentifier = subscribingSystemIdentifier;
	}

	public String getSubscriptionSubject() {
		return subscriptionSubject;
	}

	public void setSubscriptionSubject(String subscriptionSubject) {
		this.subscriptionSubject = subscriptionSubject;
	}

}
