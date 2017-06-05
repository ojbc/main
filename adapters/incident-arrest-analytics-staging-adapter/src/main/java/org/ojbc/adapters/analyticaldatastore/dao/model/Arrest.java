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

import java.sql.Time;
import java.util.Date;

public class Arrest {

	//pk
    private Integer arrestID; 
    
    //fk
    private Integer personID;
    
    //fk
    private Integer incidentID;

	private Date arrestDate;
	private Time arrestTime;

	private String arrestingAgencyName;

	private String reportingSystem;
	
	public Integer getArrestID() {
		return arrestID;
	}

	public void setArrestID(Integer arrestID) {
		this.arrestID = arrestID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(Integer incidentID) {
		this.incidentID = incidentID;
	}

	public Date getArrestDate() {
		return arrestDate;
	}

	public void setArrestDate(Date arrestDate) {
		this.arrestDate = arrestDate;
	}

	public Time getArrestTime() {
		return arrestTime;
	}

	public void setArrestTime(Time arrestTime) {
		this.arrestTime = arrestTime;
	}

	public String getArrestingAgencyName() {
		return arrestingAgencyName;
	}

	public void setArrestingAgencyName(String arrestingAgencyName) {
		this.arrestingAgencyName = arrestingAgencyName;
	}

	public String getReportingSystem() {
		return reportingSystem;
	}

	public void setReportingSystem(String reportingSystem) {
		this.reportingSystem = reportingSystem;
	}

	
}
