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
package org.ojbc.warrant.repository.dao;

public class WarrantNotification {

	private String personLastName;
	private String ocaComplaintNumber;
	private String courtAgencyORI;
	private String lawEnforcementORI;
	private String reportingAgencyORI;
	
	public String getPersonLastName() {
		return personLastName;
	}
	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}
	public String getOcaComplaintNumber() {
		return ocaComplaintNumber;
	}
	public void setOcaComplaintNumber(String ocaComplaintNumber) {
		this.ocaComplaintNumber = ocaComplaintNumber;
	}
	public String getCourtAgencyORI() {
		return courtAgencyORI;
	}
	public void setCourtAgencyORI(String courtAgencyORI) {
		this.courtAgencyORI = courtAgencyORI;
	}
	public String getLawEnforcementORI() {
		return lawEnforcementORI;
	}
	public void setLawEnforcementORI(String lawEnforcementORI) {
		this.lawEnforcementORI = lawEnforcementORI;
	}
	public String getReportingAgencyORI() {
		return reportingAgencyORI;
	}
	public void setReportingAgencyORI(String reportingAgencyORI) {
		this.reportingAgencyORI = reportingAgencyORI;
	}
	
}
