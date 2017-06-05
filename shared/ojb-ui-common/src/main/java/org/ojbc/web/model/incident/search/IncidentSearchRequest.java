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
package org.ojbc.web.model.incident.search;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class IncidentSearchRequest implements Serializable{
    private static final long serialVersionUID = -2709378989656940290L;
    private String incidentNumber;
	private String incidentCityTown;
	private String incidentType;
	private DateTime incidentDateRangeStart;
	private DateTime incidentDateRangeEnd;
	
	//Logging
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystems;
	
	public String getIncidentNumber() {
		return incidentNumber;
	}
	
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	public String getIncidentCityTown() {
		return incidentCityTown;
	}
	
	public void setIncidentCityTown(String incidentCityTown) {
		this.incidentCityTown = incidentCityTown;
	}
	
	public String getIncidentType() {
		return incidentType;
	}
	
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	
	public DateTime getIncidentDateRangeStart() {
		return incidentDateRangeStart;
	}
	
	public void setIncidentDateRangeStart(DateTime incidentDateRangeStart) {
		this.incidentDateRangeStart = incidentDateRangeStart;
	}
	
	public DateTime getIncidentDateRangeEnd() {
		return incidentDateRangeEnd;
	}
	
	public void setIncidentDateRangeEnd(DateTime incidentDateRangeEnd) {
		this.incidentDateRangeEnd = incidentDateRangeEnd;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public List<String> getSourceSystems() {
		return sourceSystems;
	}

	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	@Override
	public String toString() {
		return "IncidentSearchRequest [incidentNumber=" + incidentNumber
				+ ", incidentCityTown=" + incidentCityTown + ", incidentType="
				+ ", incidentDateRangeStart=" + incidentDateRangeStart
				+ ", incidentDateRangeEnd=" + incidentDateRangeEnd + ", sourceSystems=" + sourceSystems + "]";
	}
	
	
}
