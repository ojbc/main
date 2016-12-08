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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantAcceptedReport {

	private String ocaComplaintNumber;
	private String lawEnforcementORI;
	private String warrantStatus;
	private String stateWarrantRepositoryID;
	private String operator;
	private Person person;
	
	public String getOcaComplaintNumber() {
		return ocaComplaintNumber;
	}
	public void setOcaComplaintNumber(
			String ocaComplaintNumber) {
		this.ocaComplaintNumber = ocaComplaintNumber;
	}
	public String getLawEnforcementORI() {
		return lawEnforcementORI;
	}
	public void setLawEnforcementORI(String lawEnforcementORI) {
		this.lawEnforcementORI = lawEnforcementORI;
	}
	public String getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(String warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public String getStateWarrantRepositoryID() {
		return stateWarrantRepositoryID;
	}
	public void setStateWarrantRepositoryID(String stateWarrantRepositoryID) {
		this.stateWarrantRepositoryID = stateWarrantRepositoryID;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

}
