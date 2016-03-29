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
package org.ojbc.adapters.analyticaldatastore.custody.dao.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PretrialServiceParticipation {

	//pk
    private Integer pretrialServiceParticipationID;

    //fk
    private Integer personID;
    
    //fk
    private Integer countyID;
    
    private Integer riskScore;
    
    private Date intakeDate;
    
    private Character recordType;
    
    private String arrestingAgencyORI;
    
    private String arrestIncidentCaseNumber;
    
    private String pretrialServiceUniqueID;

	public Integer getPretrialServiceParticipationID() {
		return pretrialServiceParticipationID;
	}

	public void setPretrialServiceParticipationID(Integer pretrialServiceParticipationID) {
		this.pretrialServiceParticipationID = pretrialServiceParticipationID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getCountyID() {
		return countyID;
	}

	public void setCountyID(Integer countyID) {
		this.countyID = countyID;
	}

	public Date getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(Date intakeDate) {
		this.intakeDate = intakeDate;
	}

	public Character getRecordType() {
		return recordType;
	}

	public void setRecordType(Character recordType) {
		this.recordType = recordType;
	}

	public Integer getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(Integer riskScore) {
		this.riskScore = riskScore;
	}

	public String getArrestingAgencyORI() {
		return arrestingAgencyORI;
	}

	public void setArrestingAgencyORI(String arrestingAgencyORI) {
		this.arrestingAgencyORI = arrestingAgencyORI;
	}

	public String getArrestIncidentCaseNumber() {
		return arrestIncidentCaseNumber;
	}

	public void setArrestIncidentCaseNumber(String arrestIncidentCaseNumber) {
		this.arrestIncidentCaseNumber = arrestIncidentCaseNumber;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getPretrialServiceUniqueID() {
		return pretrialServiceUniqueID;
	}

	public void setPretrialServiceUniqueID(String pretrialServiceUniqueID) {
		this.pretrialServiceUniqueID = pretrialServiceUniqueID;
	}
	
}
