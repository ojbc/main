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

public class ESupplemental {

	//Table name where esupplemental came from
	private String supplementalType;
	
	//Value of Esupplemental, for example a Scar/Mark/Tattoo
	private String supplementalValue;
	
	//Value of Esupplemental additional value, for example an operating license state
	private String supplementalSecondaryValue;
	
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

	public String getSupplementalSecondaryValue() {
		return supplementalSecondaryValue;
	}

	public void setSupplementalSecondaryValue(String supplementalSecondaryValue) {
		this.supplementalSecondaryValue = supplementalSecondaryValue;
	}

	
}
