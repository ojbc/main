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

import java.util.List;

public class ChargeReferral {

	private Integer chargeReferralID; 
	
	private Integer personID;
	
	private String chargeReferralCaseAgencyComplaintNumber;
	
	private String transactionControlNumber;

	private String reportingAgencyORI;
	
	private String reportingAgencyName;
	
	private List<Charge> charges;
	
	private Person person; 
	
	private List<Officer> officers;
	
	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getChargeReferralID() {
		return chargeReferralID;
	}

	public void setChargeReferralID(Integer chargeReferralID) {
		this.chargeReferralID = chargeReferralID;
	}

	public String getChargeReferralCaseAgencyComplaintNumber() {
		return chargeReferralCaseAgencyComplaintNumber;
	}

	public void setChargeReferralCaseAgencyComplaintNumber(
			String chargeReferralCaseAgencyComplaintNumber) {
		this.chargeReferralCaseAgencyComplaintNumber = chargeReferralCaseAgencyComplaintNumber;
	}

	public String getTransactionControlNumber() {
		return transactionControlNumber;
	}

	public void setTransactionControlNumber(String transactionControlNumber) {
		this.transactionControlNumber = transactionControlNumber;
	}

	public String getReportingAgencyORI() {
		return reportingAgencyORI;
	}

	public void setReportingAgencyORI(String reportingAgencyORI) {
		this.reportingAgencyORI = reportingAgencyORI;
	}

	public String getReportingAgencyName() {
		return reportingAgencyName;
	}

	public void setReportingAgencyName(String reportingAgencyName) {
		this.reportingAgencyName = reportingAgencyName;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Officer> getOfficers() {
		return officers;
	}

	public void setOfficers(List<Officer> officers) {
		this.officers = officers;
	}
}
