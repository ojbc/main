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

public class ESupplemental {

	//Table name where esupplemental came from
	private String supplementalType;
	
	//Value of Esupplemental, used for Scar/Mark/Tattoo, SSN, DOB, operator license number
	private String supplementalValue;
	
	//Used for OLN additional
	private String operatorLicenseState;
	
	//Fields below are used for person altnernate names
	private String firstName;
	
	private String middleName;
	
	private String lastName;

	private String nameSuffix;
	
	private String fullPersonName;
	
	//PK from this table
	private Integer identifier;

	private Integer personPk;
	
	public String getSupplementalType() {
		return supplementalType;
	}

	public void setSupplementalType(String supplementalType) {
		this.supplementalType = supplementalType;
	}

	public String getSupplementalValue() {
		return supplementalValue;
	}

	public void setSupplementalValue(String supplementalValue) {
		this.supplementalValue = supplementalValue;
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public Integer getPersonPk() {
		return personPk;
	}

	public void setPersonPk(Integer personPk) {
		this.personPk = personPk;
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

	public String getFullPersonName() {
		return fullPersonName;
	}

	public void setFullPersonName(String fullPersonName) {
		this.fullPersonName = fullPersonName;
	}

	public String getOperatorLicenseState() {
		return operatorLicenseState;
	}

	public void setOperatorLicenseState(String operatorLicenseState) {
		this.operatorLicenseState = operatorLicenseState;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);    
    }

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

}
