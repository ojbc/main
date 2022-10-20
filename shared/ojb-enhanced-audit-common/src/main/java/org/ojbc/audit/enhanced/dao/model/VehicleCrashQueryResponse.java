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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


public class VehicleCrashQueryResponse {

	private Integer vehicleCrashQueryResultsId;
	private Integer queryRequestId;
	private String queryResultsErrorText;
	private boolean queryResultsTimeoutIndicator;
	private boolean queryResultsErrorIndicator;
	private String systemName;
	private String messageId;
	@JsonFormat(shape = Shape.STRING)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;

	private List<CrashVehicle> crashVehicles;
	
	public Integer getQueryRequestId() {
		return queryRequestId;
	}
	public void setQueryRequestId(Integer queryRequestId) {
		this.queryRequestId = queryRequestId;
	}
	public String getQueryResultsErrorText() {
		return queryResultsErrorText;
	}
	public void setQueryResultsErrorText(String queryResultsErrorText) {
		this.queryResultsErrorText = queryResultsErrorText;
	}
	public boolean isQueryResultsTimeoutIndicator() {
		return queryResultsTimeoutIndicator;
	}
	public void setQueryResultsTimeoutIndicator(boolean queryResultsTimeoutIndicator) {
		this.queryResultsTimeoutIndicator = queryResultsTimeoutIndicator;
	}
	public boolean isQueryResultsErrorIndicator() {
		return queryResultsErrorIndicator;
	}
	public void setQueryResultsErrorIndicator(boolean queryResultsErrorIndicator) {
		this.queryResultsErrorIndicator = queryResultsErrorIndicator;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public List<CrashVehicle> getCrashVehicles() {
		return crashVehicles;
	}
	public void setCrashVehicles(List<CrashVehicle> crashVehicles) {
		this.crashVehicles = crashVehicles;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getVehicleCrashQueryResultsId() {
		return vehicleCrashQueryResultsId;
	}
	public void setVehicleCrashQueryResultsId(Integer vehicleCrashQueryResultsId) {
		this.vehicleCrashQueryResultsId = vehicleCrashQueryResultsId;
	}
	
}