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

	private int dispositionID;
	
	private int personID;
	
	private int dispositionTypeID;
	
	private int offenseTypeID;
	
	private String incidentCaseNumber;
	
	private Date dispositionDate;
	
	private int sentenceTermDays;
	
	private float sentenceFineAmount;
	
	private char recordType;
	
	private char isProbationViolation;
	
	private Date recidivismEligibilityDate;

	public int getDispositionID() {
		return dispositionID;
	}

	public void setDispositionID(int dispositionID) {
		this.dispositionID = dispositionID;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public int getDispositionTypeID() {
		return dispositionTypeID;
	}

	public void setDispositionTypeID(int dispositionTypeID) {
		this.dispositionTypeID = dispositionTypeID;
	}

	public int getOffenseTypeID() {
		return offenseTypeID;
	}

	public void setOffenseTypeID(int offenseTypeID) {
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

	public int getSentenceTermDays() {
		return sentenceTermDays;
	}

	public void setSentenceTermDays(int sentenceTermDays) {
		this.sentenceTermDays = sentenceTermDays;
	}

	public float getSentenceFineAmount() {
		return sentenceFineAmount;
	}

	public void setSentenceFineAmount(float sentenceFineAmount) {
		this.sentenceFineAmount = sentenceFineAmount;
	}

	public char getRecordType() {
		return recordType;
	}

	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	public char getIsProbationViolation() {
		return isProbationViolation;
	}

	public void setIsProbationViolation(char isProbationViolation) {
		this.isProbationViolation = isProbationViolation;
	}

	public Date getRecidivismEligibilityDate() {
		return recidivismEligibilityDate;
	}

	public void setRecidivismEligibilityDate(Date recidivismEligibilityDate) {
		this.recidivismEligibilityDate = recidivismEligibilityDate;
	}
}
