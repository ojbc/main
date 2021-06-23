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
package org.ojbc.util.model.incidentsearch;

import java.time.LocalDateTime;


public class IncidentSearchResult {

	private String incidentID;
	
	//Either an incident date range will be entered or a specific incident date
	private LocalDateTime incidentStartDateRange;
	private LocalDateTime incidentEndDateRange;

	//We add a mapping in the JSONProcessor to allow for the valcour date format
	private LocalDateTime incidentDate;
	
	private String categoryCode;

	private String streetNumber;
	
	private String streetName;
	
	private String state;
	
	private String zipCode;
	
	private String cityTown;
	
	private String agency;

	private String type;
	
	public String getIncidentID() {
		return incidentID;
	}
	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCityTown() {
		return cityTown;
	}
	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}
	public LocalDateTime getIncidentDate() {
		return incidentDate;
	}
	public void setIncidentDate(LocalDateTime incidentDate) {
		this.incidentDate = incidentDate;
	}
	public LocalDateTime getIncidentStartDateRange() {
		return incidentStartDateRange;
	}
	public void setIncidentStartDateRange(LocalDateTime incidentStartDateRange) {
		this.incidentStartDateRange = incidentStartDateRange;
	}
	public LocalDateTime getIncidentEndDateRange() {
		return incidentEndDateRange;
	}
	public void setIncidentEndDateRange(LocalDateTime incidentEndDateRange) {
		this.incidentEndDateRange = incidentEndDateRange;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}