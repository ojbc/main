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
package org.ojbc.web.model.consealedCarry.search;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ojbc.web.SearchFieldMetadata;

public class ConcealedCarrySearchRequest {

	private String licenseNumber;
    private SearchFieldMetadata licenseNumberMetadata = SearchFieldMetadata.Partial;
    private String registrationNumber;
    
	private String purpose;
	private String onBehalfOf;

	@NotEmpty(message = "No Source Systems to search are selected.")
	private List<String> sourceSystems;

    public ConcealedCarrySearchRequest() {
    	super();
    }

	
	public boolean isEmpty() {
		return	StringUtils.isAllBlank(this.getLicenseNumber(), this.getRegistrationNumber());
	}

	@AssertTrue(message = "License number or registration number is required")
	public boolean isNotEmpty() {
		return !isEmpty();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}


	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}


	public SearchFieldMetadata getLicenseNumberMetadata() {
		return licenseNumberMetadata;
	}


	public void setLicenseNumberMetadata(SearchFieldMetadata licenseNumberMetadata) {
		this.licenseNumberMetadata = licenseNumberMetadata;
	}


	public String getRegistrationNumber() {
		return registrationNumber;
	}


	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}


	public List<String> getSourceSystems() {
		return sourceSystems;
	}


	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
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
}