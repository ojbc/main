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
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PersonSearchRequest {

	private Integer personSearchRequestID;
	
	private String firstName;
	
	private Integer firstNameQualifier;
	
	private String firstNameQualifierCode;
	
	private String middleName;
	
	private String lastName;
	
	private Integer lastNameQualifier;
	
	private String lastNameQualifierCode;
	
	private LocalDate dobFrom;
	
	private LocalDate dobTo;
	
	private String ssn;
	
	private String driverLicenseId;
	
	private String driverLiscenseIssuer;
	
	private String fbiNumber;
	
	private String stateId;
	
	private String raceCode;
	
	private String genderCode;
	
	private String eyeCode;
	
	private String hairCode;
	
	private List<String> systemsToSearch;
	
	private String purpose;
	
	private String onBehalfOf;
	
	private String messageId;
	
	private Integer userInfofk;
	
	private Integer height;
	
	private Integer heightMin;
	
	private Integer heightMax;
	
	private LocalDateTime timestamp;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getDriverLicenseId() {
		return driverLicenseId;
	}

	public void setDriverLicenseId(String driverLicenseId) {
		this.driverLicenseId = driverLicenseId;
	}

	public String getDriverLiscenseIssuer() {
		return driverLiscenseIssuer;
	}

	public void setDriverLiscenseIssuer(String driverLiscenseIssuer) {
		this.driverLiscenseIssuer = driverLiscenseIssuer;
	}

	public String getFbiNumber() {
		return fbiNumber;
	}

	public void setFbiNumber(String fbiNumber) {
		this.fbiNumber = fbiNumber;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getEyeCode() {
		return eyeCode;
	}

	public void setEyeCode(String eyeCode) {
		this.eyeCode = eyeCode;
	}

	public String getHairCode() {
		return hairCode;
	}

	public void setHairCode(String hairCode) {
		this.hairCode = hairCode;
	}

	public LocalDate getDobFrom() {
		return dobFrom;
	}

	public void setDobFrom(LocalDate dobFrom) {
		this.dobFrom = dobFrom;
	}

	public LocalDate getDobTo() {
		return dobTo;
	}

	public void setDobTo(LocalDate dobTo) {
		this.dobTo = dobTo;
	}

	public List<String> getSystemsToSearch() {
		return systemsToSearch;
	}

	public void setSystemsToSearch(List<String> systemsToSearch) {
		this.systemsToSearch = systemsToSearch;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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

	public Integer getFirstNameQualifier() {
		return firstNameQualifier;
	}

	public void setFirstNameQualifier(Integer firstNameQualifier) {
		this.firstNameQualifier = firstNameQualifier;
	}

	public Integer getLastNameQualifier() {
		return lastNameQualifier;
	}

	public void setLastNameQualifier(Integer lastNameQualifier) {
		this.lastNameQualifier = lastNameQualifier;
	}

	public String getFirstNameQualifierCode() {
		return firstNameQualifierCode;
	}

	public void setFirstNameQualifierCode(String firstNameQualifierCode) {
		this.firstNameQualifierCode = firstNameQualifierCode;
	}

	public String getLastNameQualifierCode() {
		return lastNameQualifierCode;
	}

	public void setLastNameQualifierCode(String lastNameQualifierCode) {
		this.lastNameQualifierCode = lastNameQualifierCode;
	}

	public Integer getPersonSearchRequestID() {
		return personSearchRequestID;
	}

	public void setPersonSearchRequestID(Integer personSearchRequestID) {
		this.personSearchRequestID = personSearchRequestID;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getHeightMax() {
		return heightMax;
	}

	public void setHeightMax(Integer heightMax) {
		this.heightMax = heightMax;
	}

	public Integer getHeightMin() {
		return heightMin;
	}

	public void setHeightMin(Integer heightMin) {
		this.heightMin = heightMin;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
