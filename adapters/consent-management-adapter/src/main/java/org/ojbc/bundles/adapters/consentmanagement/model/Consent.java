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
package org.ojbc.bundles.adapters.consentmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Consent {

	private Integer consentId;
	
	private Integer consentDecisionTypeID;
	
	private String bookingNumber;
	
	private String nameNumber;
	
	private String personFirstName;
	
	private String personMiddleName;
	
	private String personLastName;
	
	private String personGender;
	
	@JsonIgnore
	private LocalDate personDOB;
	
	private String personDOBString;
	
	private String consenterUserID;
	
	private String consentUserFirstName;
	
	private String consentUserLastName;
	
	private String consentDocumentControlNumber;
	
	@JsonIgnore
	private LocalDateTime recordCreationTimestamp;
	
	@JsonIgnore
	private LocalDateTime consentDecisionTimestamp;

	public String getConsenterUserID() {
		return consenterUserID;
	}

	public void setConsenterUserID(String consenterUserID) {
		this.consenterUserID = consenterUserID;
	}

	public Integer getConsentId() {
		return consentId;
	}

	public void setConsentId(Integer consentId) {
		this.consentId = consentId;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public String getNameNumber() {
		return nameNumber;
	}

	public void setNameNumber(String nameNumber) {
		this.nameNumber = nameNumber;
	}

	public String getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonMiddleName() {
		return personMiddleName;
	}

	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}

	public String getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}

	public String getPersonGender() {
		return personGender;
	}

	public void setPersonGender(String personGender) {
		this.personGender = personGender;
	}

	public LocalDate getPersonDOB() {
		return personDOB;
	}

	public void setPersonDOB(LocalDate personDOB) {
		this.personDOB = personDOB;
	}

	public String getConsentDocumentControlNumber() {
		return consentDocumentControlNumber;
	}

	public void setConsentDocumentControlNumber(String consentDocumentControlNumber) {
		this.consentDocumentControlNumber = consentDocumentControlNumber;
	}

	public LocalDateTime getRecordCreationTimestamp() {
		return recordCreationTimestamp;
	}

	public void setRecordCreationTimestamp(LocalDateTime recordCreationTimestamp) {
		this.recordCreationTimestamp = recordCreationTimestamp;
	}

	public LocalDateTime getConsentDecisionTimestamp() {
		return consentDecisionTimestamp;
	}

	public void setConsentDecisionTimestamp(LocalDateTime consentDecisionTimestamp) {
		this.consentDecisionTimestamp = consentDecisionTimestamp;
	}

	public Integer getConsentDecisionTypeID() {
		return consentDecisionTypeID;
	}

	public void setConsentDecisionTypeID(Integer consentDecisionTypeID) {
		this.consentDecisionTypeID = consentDecisionTypeID;
	}

	public String getPersonDOBString() {
		 if (StringUtils.isNotBlank(personDOBString))
		 {
			 return personDOBString;
		 }	 
		
		 if (getPersonDOB() != null)
		 {	 
			 return getPersonDOB().toString();
		 }
		 else
		 {
			 return null;
		 }	 
	}

	public String getConsentUserFirstName() {
		return consentUserFirstName;
	}

	public void setConsentUserFirstName(String consentUserFirstName) {
		this.consentUserFirstName = consentUserFirstName;
	}

	public String getConsentUserLastName() {
		return consentUserLastName;
	}

	public void setConsentUserLastName(String consentUserLastName) {
		this.consentUserLastName = consentUserLastName;
	}

	@Override
	public String toString() {
		return "Consent [consentId=" + consentId + ", consentDecisionTypeID="
				+ consentDecisionTypeID + ", bookingNumber=" + bookingNumber
				+ ", nameNumber=" + nameNumber + ", personFirstName="
				+ personFirstName + ", personMiddleName=" + personMiddleName
				+ ", personLastName=" + personLastName + ", personGender="
				+ personGender + ", personDOB=" + personDOB
				+ ", personDOBString=" + personDOBString + ", consenterUserID="
				+ consenterUserID + ", consentUserFirstName="
				+ consentUserFirstName + ", consentUserLastName="
				+ consentUserLastName + ", consentDocumentControlNumber="
				+ consentDocumentControlNumber + ", recordCreationTimestamp="
				+ recordCreationTimestamp + ", consentDecisionTimestamp="
				+ consentDecisionTimestamp + "]";
	}
	
}
