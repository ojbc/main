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

public class VehicleSearchRequest {

	private Integer vehicleSearchRequestId;
	
	private String vehicleColor;
	private String vehicleModel;
	private String vehicleMake;
	private String vehicleLicensePlate;
	private String vehicleIdentificationNumber;
	private String vehicleYearRangeStart;
	private String vehicleYearRangeEnd;
	
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystemsList;
	
	private Integer userInfofk;
	
	private String messageId;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;
	
	public String getVehicleColor() {
		return vehicleColor;
	}
	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getVehicleMake() {
		return vehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	public String getVehicleLicensePlate() {
		return vehicleLicensePlate;
	}
	public void setVehicleLicensePlate(String vehicleLicensePlate) {
		this.vehicleLicensePlate = vehicleLicensePlate;
	}
	public String getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}
	public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}
	public String getVehicleYearRangeStart() {
		return vehicleYearRangeStart;
	}
	public void setVehicleYearRangeStart(String vehicleYearRangeStart) {
		this.vehicleYearRangeStart = vehicleYearRangeStart;
	}
	public String getVehicleYearRangeEnd() {
		return vehicleYearRangeEnd;
	}
	public void setVehicleYearRangeEnd(String vehicleYearRangeEnd) {
		this.vehicleYearRangeEnd = vehicleYearRangeEnd;
	}
	public List<String> getSourceSystemsList() {
		return sourceSystemsList;
	}
	public void setSourceSystemsList(List<String> sourceSystemsList) {
		this.sourceSystemsList = sourceSystemsList;
	}
	public Integer getVehicleSearchRequestId() {
		return vehicleSearchRequestId;
	}
	public void setVehicleSearchRequestId(Integer vehicleSearchRequestId) {
		this.vehicleSearchRequestId = vehicleSearchRequestId;
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
	public Integer getUserInfofk() {
		return userInfofk;
	}
	public void setUserInfofk(Integer userInfofk) {
		this.userInfofk = userInfofk;
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
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
