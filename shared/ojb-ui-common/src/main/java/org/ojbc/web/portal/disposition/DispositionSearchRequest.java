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
package org.ojbc.web.portal.disposition;

import java.time.LocalDate;

import org.ojbc.web.SearchFieldMetadata;
import org.springframework.format.annotation.DateTimeFormat;

public class DispositionSearchRequest {

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeEndDate;
	
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
    
	public LocalDate getDispositionDateRangeStartDate() {
		return dispositionDateRangeStartDate;
	}
	public void setDispositionDateRangeStartDate(LocalDate dispositionDateRangeStartDate) {
		this.dispositionDateRangeStartDate = dispositionDateRangeStartDate;
	}
	public LocalDate getDispositionDateRangeEndDate() {
		return dispositionDateRangeEndDate;
	}
	public void setDispositionDateRangeEndDate(LocalDate dispositionDateRangeEndDate) {
		this.dispositionDateRangeEndDate = dispositionDateRangeEndDate;
	}
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
	
	@Override
	public String toString() {
		return "DispositionSearchRequest [dispositionDateRangeStartDate=" + dispositionDateRangeStartDate
				+ ", dispositionDateRangeEndDate=" + dispositionDateRangeEndDate + ", arrestDateRangeStartDate="
				+ arrestDateRangeStartDate + ", arrestDateRangeEndDate=" + arrestDateRangeEndDate + ", firstName="
				+ firstName + ", firstNameSearchMetadata=" + firstNameSearchMetadata + ", lastName=" + lastName
				+ ", lastNameSearchMetadata=" + lastNameSearchMetadata + ", dob=" + dob + ", ssn=" + ssn + ", otn="
				+ otn + ", arrestIdentification=" + arrestIdentification + ", ori=" + ori + "]";
	}
}