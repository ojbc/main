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
package org.ojbc.adapters.analyticaldatastore.dao.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Incident {

	//pk
	private Integer incidentID;
	
	//fk to Agency table
	private Integer reportingAgencyID;
		
	private Date incidentDate;
	private Time incidentTime;
	
	private String incidentCaseNumber;
	
	private BigDecimal incidentLocationLatitude;
	private BigDecimal IncidentLocationLongitude;
    private String incidentLocationStreetAddress;
    private String incidentLocationTown;
	
    private String incidentDescriptionText;
    
	private char recordType;

	private String reportingSystem;
	
	public Integer getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(Integer incidentID) {
		this.incidentID = incidentID;
	}

	public Integer getReportingAgencyID() {
		return reportingAgencyID;
	}

	public void setReportingAgencyID(Integer reportingAgencyID) {
		this.reportingAgencyID = reportingAgencyID;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentCaseNumber() {
		return incidentCaseNumber;
	}

	public void setIncidentCaseNumber(String incidentCaseNumber) {
		this.incidentCaseNumber = incidentCaseNumber;
	}

	public BigDecimal getIncidentLocationLatitude() {
		return incidentLocationLatitude;
	}

	public void setIncidentLocationLatitude(BigDecimal incidentLocationLatitude) {
		this.incidentLocationLatitude = incidentLocationLatitude;
	}

	public BigDecimal getIncidentLocationLongitude() {
		return IncidentLocationLongitude;
	}

	public void setIncidentLocationLongitude(BigDecimal incidentLocationLongitude) {
		IncidentLocationLongitude = incidentLocationLongitude;
	}

	public String getIncidentLocationStreetAddress() {
		return incidentLocationStreetAddress;
	}

	public void setIncidentLocationStreetAddress(
			String incidentLocationStreetAddress) {
		this.incidentLocationStreetAddress = incidentLocationStreetAddress;
	}

	public String getIncidentLocationTown() {
		return incidentLocationTown;
	}

	public void setIncidentLocationTown(String incidentLocationTown) {
		this.incidentLocationTown = incidentLocationTown;
	}

	public Time getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(Time incidentTime) {
		this.incidentTime = incidentTime;
	}

	public char getRecordType() {
		return recordType;
	}

	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	public String getIncidentDescriptionText() {
		return incidentDescriptionText;
	}

	public void setIncidentDescriptionText(String incidentDescriptionText) {
		this.incidentDescriptionText = incidentDescriptionText;
	}

	public String getReportingSystem() {
		return reportingSystem;
	}

	public void setReportingSystem(String reportingSystem) {
		this.reportingSystem = reportingSystem;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
