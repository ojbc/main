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
package org.ojbc.warrant.repository.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PersonOLNAdditional {

	private Integer personOLNID;

	private Integer personID;
	
	private String operatorLicenseNumber;

	private String operatorLicenseState;

	public Integer getPersonOLNID() {
		return personOLNID;
	}

	public void setPersonOLNID(Integer personOLNID) {
		this.personOLNID = personOLNID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getOperatorLicenseNumber() {
		return operatorLicenseNumber;
	}

	public void setOperatorLicenseNumber(String operatorLicenseNumber) {
		this.operatorLicenseNumber = operatorLicenseNumber;
	}

	public String getOperatorLicenseState() {
		return operatorLicenseState;
	}

	public void setOperatorLicenseState(String operatorLicenseState) {
		this.operatorLicenseState = operatorLicenseState;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
