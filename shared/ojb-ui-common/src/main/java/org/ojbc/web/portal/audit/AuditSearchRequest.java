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
package org.ojbc.web.portal.audit;

import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.SearchFieldMetadata;
import org.springframework.format.annotation.DateTimeFormat;

public class AuditSearchRequest {

	private String firstName;
    private SearchFieldMetadata firstNameSearchMetadata;
    private Boolean firstNameExactMatch; 
    
    private String lastName;
    private SearchFieldMetadata lastNameSearchMetadata;
    private Boolean lastNameExactMatch; 
    
    private String ori;
    
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate auditDateRangeStartDate; 
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate auditDateRangeEndDate;
    
    public AuditSearchRequest() {
    	super();
		this.setAuditDateRangeStartDate(LocalDate.now().minusDays(90));
		this.setAuditDateRangeEndDate(LocalDate.now());
		this.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		this.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
    }

	public LocalDate getAuditDateRangeStartDate() {
		return auditDateRangeStartDate;
	}
	public void setAuditDateRangeStartDate(LocalDate auditDateRangeStartDate) {
		this.auditDateRangeStartDate = auditDateRangeStartDate;
	}
	public LocalDate getAuditDateRangeEndDate() {
		return auditDateRangeEndDate;
	}
	public void setAuditDateRangeEndDate(LocalDate auditDateRangeEndDate) {
		this.auditDateRangeEndDate = auditDateRangeEndDate;
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
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
	
	public boolean isEmpty() {
		return this.getAuditDateRangeEndDate() == null &&
				this.getAuditDateRangeStartDate() == null && 
				StringUtils.isBlank(this.getFirstName()) && 
				StringUtils.isBlank(this.getLastName()) && 
				StringUtils.isBlank(this.getOri());
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
}