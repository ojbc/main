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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class FirearmsSearchRequest {

	private Integer firearmSearchRequestId;
	
	private String serialNumber;
	
	private String serialNumberQualifierCode;
	
	private Integer serialNumberQualifierCodeId;
	
	private String registrationNumber;
	
	private String model;
	
	private String make;
	
	private String firearmsType;

	private boolean currentRegistrationsOnly;
	
	private List<String> systemsToSearch;
	
	private String purpose;
	
	private String onBehalfOf;
	
	private String messageId;
	
	private Integer userInfofk;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;
	
	public Integer getFirearmSearchRequestId() {
		return firearmSearchRequestId;
	}

	public void setFirearmSearchRequestId(Integer firearmSearchRequestId) {
		this.firearmSearchRequestId = firearmSearchRequestId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSerialNumberQualifierCode() {
		return serialNumberQualifierCode;
	}

	public void setSerialNumberQualifierCode(String serialNumberQualifierCode) {
		this.serialNumberQualifierCode = serialNumberQualifierCode;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getFirearmsType() {
		return firearmsType;
	}

	public void setFirearmsType(String firearmsType) {
		this.firearmsType = firearmsType;
	}

	public boolean isCurrentRegistrationsOnly() {
		return currentRegistrationsOnly;
	}

	public void setCurrentRegistrationsOnly(boolean currentRegistrationsOnly) {
		this.currentRegistrationsOnly = currentRegistrationsOnly;
	}

	public List<String> getSystemsToSearch() {
		return systemsToSearch;
	}

	public void setSystemsToSearch(List<String> systemsToSearch) {
		this.systemsToSearch = systemsToSearch;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Integer getUserInfofk() {
		return userInfofk;
	}

	public void setUserInfofk(Integer userInfofk) {
		this.userInfofk = userInfofk;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getSerialNumberQualifierCodeId() {
		return serialNumberQualifierCodeId;
	}

	public void setSerialNumberQualifierCodeId(Integer serialNumberQualifierCodeId) {
		this.serialNumberQualifierCodeId = serialNumberQualifierCodeId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
