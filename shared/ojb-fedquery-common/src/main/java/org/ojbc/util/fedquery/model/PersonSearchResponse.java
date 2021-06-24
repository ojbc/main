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
package org.ojbc.util.fedquery.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PersonSearchResponse {
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String fbiNumber;
	
	private String stateId;
	
	private String ssn;
	
	private String driverLicenseId;
	
	private String sex;
	
	private String race;
	
	private Date dob;
	
	private String sourceSystemNameText;
	
	private String systemId;
	
	private String systemName;
	
	private String searchResultCategoryText;

	public String getFbiNumber() {
		return fbiNumber;
	}

	public String getStateId() {
		return stateId;
	}

	public String getSsn() {
		return ssn;
	}

	public String getDriverLicenseId() {
		return driverLicenseId;
	}

	public String getSex() {
		return sex;
	}

	public String getRace() {
		return race;
	}

	public Date getDob() {
		return dob;
	}

	public String getSourceSystemNameText() {
		return sourceSystemNameText;
	}

	public String getSearchResultCategoryText() {
		return searchResultCategoryText;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getSystemName() {
		return systemName;
	}
	
	public void setFbiNumber(String fbiNumber) {
		this.fbiNumber = fbiNumber;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setDriverLicenseId(String driverLicenseId) {
		this.driverLicenseId = driverLicenseId;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public void setSourceSystemNameText(String sourceSystemNameText) {
		this.sourceSystemNameText = sourceSystemNameText;
	}

	public void setSearchResultCategoryText(String searchResultCategoryText) {
		this.searchResultCategoryText = searchResultCategoryText;
	}


	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

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
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
}

