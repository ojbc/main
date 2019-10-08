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
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.SearchFieldMetadata;
import org.springframework.format.annotation.DateTimeFormat;

public class ArrestSearchRequest {

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDateRangeEndDate;
	
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dispositionDateRangeEndDate;
	
	private String firstName;
    private SearchFieldMetadata firstNameSearchMetadata;
    private Boolean firstNameExactMatch; 
    
    private String lastName;
    private SearchFieldMetadata lastNameSearchMetadata;
    private Boolean lastNameExactMatch; 
    
	@DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dob;
    
    private String ssn;
    private String otn;
    
    private String arrestIdentification;
    private List<String> userSelectedOris;
    private List<String> authorizedOris;
    
    private ArrestType arrestType; 
    private Boolean arrestWithDeferredDispositions; 
    private Boolean includeHiddenArrestIndicator; 
    private Boolean includeOnlyAdminOwnedCharges; 
    
    public ArrestSearchRequest() {
    	super();
		this.setArrestDateRangeStartDate(LocalDate.now().minusDays(30));
//		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.of(2018, 2, 1));
		this.setArrestDateRangeEndDate(LocalDate.now());
		this.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		this.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
    }
    
    public ArrestSearchRequest(ArrestType arrestType) {
    	this();
		this.setArrestType(arrestType);
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public ArrestType getArrestType() {
		return arrestType;
	}
	public void setArrestType(ArrestType arrestType) {
		this.arrestType = arrestType;
	}
	public Boolean getArrestWithDeferredDispositions() {
		return arrestWithDeferredDispositions;
	}
	public void setArrestWithDeferredDispositions(Boolean arrestWithDeferredDispositions) {
		this.arrestWithDeferredDispositions = arrestWithDeferredDispositions;
	}
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
	public Boolean getIncludeHiddenArrestIndicator() {
		return includeHiddenArrestIndicator;
	}
	public void setIncludeHiddenArrestIndicator(Boolean includeHiddenArrestIndicator) {
		this.includeHiddenArrestIndicator = includeHiddenArrestIndicator;
	}
	public Boolean getFirstNameExactMatch() {
		return firstNameExactMatch;
	}
	public void setFirstNameExactMatch(Boolean firstNameExactMatch) {
		this.firstNameExactMatch = firstNameExactMatch;
	}
	public Boolean getLastNameExactMatch() {
		return lastNameExactMatch;
	}
	public void setLastNameExactMatch(Boolean lastNameExactMatch) {
		this.lastNameExactMatch = lastNameExactMatch;
	}
	
	public void setFirstNameSearchMetaData() {
		if (BooleanUtils.isNotTrue(this.getFirstNameExactMatch())) {
			this.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		}
		else {
			this.setFirstNameSearchMetadata(SearchFieldMetadata.ExactMatch);
		}
	}
	public void setLastNameSearchMetaData() {
		if (BooleanUtils.isNotTrue(this.getLastNameExactMatch())) {
			this.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
		}
		else {
			this.setLastNameSearchMetadata(SearchFieldMetadata.ExactMatch);
		}
	}

	public List<String> getUserSelectedOris() {
		return userSelectedOris;
	}

	public void setUserSelectedOris(List<String> userSelectedOris) {
		this.userSelectedOris = userSelectedOris;
	}

	public List<String> getAuthorizedOris() {
		return authorizedOris;
	}

	public void setAuthorizedOris(List<String> authorizedOris) {
		this.authorizedOris = authorizedOris;
	}

	public Boolean getIncludeOnlyAdminOwnedCharges() {
		return includeOnlyAdminOwnedCharges;
	}

	public void setIncludeOnlyAdminOwnedCharges(Boolean includeOnlyAdminOwnedCharges) {
		this.includeOnlyAdminOwnedCharges = includeOnlyAdminOwnedCharges;
	}
}