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
package org.ojbc.web.portal.arrest;

import java.time.LocalDate;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.SearchFieldMetadata;
import org.springframework.format.annotation.DateTimeFormat;

public class ArrestSearchRequest {

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeEndDate;
	
	private String firstName;
    private SearchFieldMetadata firstNameSearchMetadata;
    
    private String lastName;
    private SearchFieldMetadata lastNameSearchMetadata;
    
	@DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dob;
    
    private String ssn;
    private String otn;
    
    private String arrestIdentification;
    private String ori;
    
    private ArrestType arrestType; 
    
	public LocalDate getArrestDateRangeStartDate() {
		return arrestDateRangeStartDate;
	}
	public void setArrestDateRangeStartDate(LocalDate arrestDateRangeStartDate) {
		this.arrestDateRangeStartDate = arrestDateRangeStartDate;
	}
	public LocalDate getArrestDateRangeEndDate() {
		return arrestDateRangeEndDate;
	}
	public void setArrestDateRangeEndDate(LocalDate arrestDateRangeEndDate) {
		this.arrestDateRangeEndDate = arrestDateRangeEndDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public SearchFieldMetadata getFirstNameSearchMetadata() {
		return firstNameSearchMetadata;
	}
	public void setFirstNameSearchMetadata(SearchFieldMetadata firstNameSearchMetadata) {
		this.firstNameSearchMetadata = firstNameSearchMetadata;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public SearchFieldMetadata getLastNameSearchMetadata() {
		return lastNameSearchMetadata;
	}
	public void setLastNameSearchMetadata(SearchFieldMetadata lastNameSearchMetadata) {
		this.lastNameSearchMetadata = lastNameSearchMetadata;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getOtn() {
		return otn;
	}
	public void setOtn(String otn) {
		this.otn = otn;
	}
	public String getArrestIdentification() {
		return arrestIdentification;
	}
	public void setArrestIdentification(String arrestIdentification) {
		this.arrestIdentification = arrestIdentification;
	}
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	public boolean isEmpty() {
		return this.getArrestDateRangeEndDate() == null &&
				this.getArrestDateRangeStartDate() == null && 
				StringUtils.isBlank(this.getArrestIdentification()) && 
				this.getDob() == null && 
				StringUtils.isBlank(this.getFirstName()) && 
				StringUtils.isBlank(this.getLastName()) && 
				StringUtils.isBlank(this.getSsn()) && 
				StringUtils.isBlank(this.getOtn());
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}
	
	public boolean isSubjectInfoNotEmpty() {
		return this.getDob() != null || 
				Arrays.asList(this.getFirstName(), this.getLastName(), this.getOtn(), this.getSsn())
					  .stream().anyMatch(StringUtils::isNotBlank);
	}
	
	public boolean isPersonInfoNotEmpty() {
		return this.getDob() != null || 
				Arrays.asList(this.getFirstName(), this.getLastName(), this.getSsn())
				.stream().anyMatch(StringUtils::isNotBlank);
	}
	
	@Override
	public String toString() {
		return "ArrestSearchRequest [arrestDateRangeStartDate=" + arrestDateRangeStartDate + ", arrestDateRangeEndDate="
				+ arrestDateRangeEndDate + ", firstName=" + firstName + ", firstNameSearchMetadata="
				+ firstNameSearchMetadata + ", lastName=" + lastName + ", lastNameSearchMetadata="
				+ lastNameSearchMetadata + ", dob=" + dob + ", ssn=" + ssn + ", otn=" + otn + ", arrestIdentification="
				+ arrestIdentification + ", ori=" + ori + ", arrestType=" + getArrestType() + "]";
	}
	public ArrestType getArrestType() {
		return arrestType;
	}
	public void setArrestType(ArrestType arrestType) {
		this.arrestType = arrestType;
	}
}