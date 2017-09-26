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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TrafficStop {
	
	//traffic stop pk
	private Integer trafficStopID;

	//fk to incident table
	private Integer incidentID;
	
	private String trafficStopReasonDescription;
	private Integer driverAge;
	private String driverRace;
	private String driverResidenceTown;
	private String trafficStopSearchTypeDescription;
	private String vehicleModel;
	private String trafficStopOutcomeDescription;
	private String driverResidenceState;
	private Integer vehicleYear;
	private String vehicleMake;
	private String trafficStopContrabandStatus;
	private String driverSex;
	private String vehicleRegistrationState;

	public Integer getTrafficStopID() {
		return trafficStopID;
	}


	public void setTrafficStopID(Integer trafficStopID) {
		this.trafficStopID = trafficStopID;
	}


	public Integer getIncidentID() {
		return incidentID;
	}


	public void setIncidentID(Integer incidentID) {
		this.incidentID = incidentID;
	}


	public String getTrafficStopReasonDescription() {
		return trafficStopReasonDescription;
	}


	public void setTrafficStopReasonDescription(String trafficStopReasonDescription) {
		this.trafficStopReasonDescription = trafficStopReasonDescription;
	}


	public Integer getDriverAge() {
		return driverAge;
	}


	public void setDriverAge(Integer driverAge) {
		this.driverAge = driverAge;
	}


	public String getDriverRace() {
		return driverRace;
	}


	public void setDriverRace(String driverRace) {
		this.driverRace = driverRace;
	}


	public String getDriverResidenceTown() {
		return driverResidenceTown;
	}


	public void setDriverResidenceTown(String driverResidenceTown) {
		this.driverResidenceTown = driverResidenceTown;
	}


	public String getTrafficStopSearchTypeDescription() {
		return trafficStopSearchTypeDescription;
	}


	public void setTrafficStopSearchTypeDescription(
			String trafficStopSearchTypeDescription) {
		this.trafficStopSearchTypeDescription = trafficStopSearchTypeDescription;
	}


	public String getVehicleModel() {
		return vehicleModel;
	}


	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}


	public String getTrafficStopOutcomeDescription() {
		return trafficStopOutcomeDescription;
	}


	public void setTrafficStopOutcomeDescription(
			String trafficStopOutcomeDescription) {
		this.trafficStopOutcomeDescription = trafficStopOutcomeDescription;
	}


	public String getDriverResidenceState() {
		return driverResidenceState;
	}


	public void setDriverResidenceState(String driverResidenceState) {
		this.driverResidenceState = driverResidenceState;
	}


	public Integer getVehicleYear() {
		return vehicleYear;
	}


	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}


	public String getVehicleMake() {
		return vehicleMake;
	}


	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}


	public String getTrafficStopContrabandStatus() {
		return trafficStopContrabandStatus;
	}


	public void setTrafficStopContrabandStatus(String trafficStopContrabandStatus) {
		this.trafficStopContrabandStatus = trafficStopContrabandStatus;
	}


	public String getDriverSex() {
		return driverSex;
	}


	public void setDriverSex(String driverSex) {
		this.driverSex = driverSex;
	}


	public String getVehicleRegistrationState() {
		return vehicleRegistrationState;
	}


	public void setVehicleRegistrationState(String vehicleRegistrationState) {
		this.vehicleRegistrationState = vehicleRegistrationState;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
