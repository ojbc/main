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

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

public class Incident {

	//pk
	private int incidentID;
	
	//fk to Agency table
	private int reportingAgencyID;
	
	//fk to IncidentType table
	private int incidentTypeID;
	
	//fk to County table	
	private int countyID;
	
	private Date incidentDate;
	
	private String incidentCaseNumber;
	
	private BigDecimal incidentLocationLatitude;
	private BigDecimal IncidentLocationLongitude;
    private String incidentLocationStreetAddress;
    private String incidentLocationTown;
	
    private Time incidentTime;
    
	private char recordType;

	public int getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(int incidentID) {
		this.incidentID = incidentID;
	}

	public int getReportingAgencyID() {
		return reportingAgencyID;
	}

	public void setReportingAgencyID(int reportingAgencyID) {
		this.reportingAgencyID = reportingAgencyID;
	}

	public int getIncidentTypeID() {
		return incidentTypeID;
	}

	public void setIncidentTypeID(int incidentTypeID) {
		this.incidentTypeID = incidentTypeID;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public int getCountyID() {
		return countyID;
	}

	public void setCountyID(int countyID) {
		this.countyID = countyID;
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
	
}
