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
package org.ojbc.adapters.analyticaldatastore.dao.model;

import java.util.Date;

public class Disposition {

	private Integer dispositionID;
	
	private Integer personID;
	
	private Integer dispositionTypeID;
	
	private Integer offenseTypeID;
	
	private String incidentCaseNumber;
	
	private Date dispositionDate;
	
	private String arrestingAgencyORI;
	
	private Integer sentenceTermDays;
	
	private Float sentenceFineAmount;
	
	private Character recordType;
	
	private Character isProbationViolation;
	
	private Character IsProbationViolationOnOldCharge;
	
	private Date recidivismEligibilityDate;

	public Integer getDispositionID() {
		return dispositionID;
	}

	public void setDispositionID(Integer dispositionID) {
		this.dispositionID = dispositionID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getDispositionTypeID() {
		return dispositionTypeID;
	}

	public void setDispositionTypeID(Integer dispositionTypeID) {
		this.dispositionTypeID = dispositionTypeID;
	}

	public Integer getOffenseTypeID() {
		return offenseTypeID;
	}

	public void setOffenseTypeID(Integer offenseTypeID) {
		this.offenseTypeID = offenseTypeID;
	}

	public String getIncidentCaseNumber() {
		return incidentCaseNumber;
	}

	public void setIncidentCaseNumber(String incidentCaseNumber) {
		this.incidentCaseNumber = incidentCaseNumber;
	}

	public Date getDispositionDate() {
		return dispositionDate;
	}

	public void setDispositionDate(Date dispositionDate) {
		this.dispositionDate = dispositionDate;
	}

	public Integer getSentenceTermDays() {
		return sentenceTermDays;
	}

	public void setSentenceTermDays(Integer sentenceTermDays) {
		this.sentenceTermDays = sentenceTermDays;
	}

	public Float getSentenceFineAmount() {
		return sentenceFineAmount;
	}

	public void setSentenceFineAmount(Float sentenceFineAmount) {
		this.sentenceFineAmount = sentenceFineAmount;
	}

	public Character getRecordType() {
		return recordType;
	}

	public void setRecordType(Character recordType) {
		this.recordType = recordType;
	}

	public Character getIsProbationViolation() {
		return isProbationViolation;
	}

	public void setIsProbationViolation(Character isProbationViolation) {
		this.isProbationViolation = isProbationViolation;
	}

	public Date getRecidivismEligibilityDate() {
		return recidivismEligibilityDate;
	}

	public void setRecidivismEligibilityDate(Date recidivismEligibilityDate) {
		this.recidivismEligibilityDate = recidivismEligibilityDate;
	}

	public Character getIsProbationViolationOnOldCharge() {
		return IsProbationViolationOnOldCharge;
	}

	public void setIsProbationViolationOnOldCharge(
			Character isProbationViolationOnOldCharge) {
		IsProbationViolationOnOldCharge = isProbationViolationOnOldCharge;
	}

	public String getArrestingAgencyORI() {
		return arrestingAgencyORI;
	}

	public void setArrestingAgencyORI(String arrestingAgencyORI) {
		this.arrestingAgencyORI = arrestingAgencyORI;
	}

}
