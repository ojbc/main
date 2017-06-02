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
package org.ojbc.web.model.vehicle.search;

import java.io.Serializable;
import java.util.List;

public class VehicleSearchRequest implements Serializable {
    private static final long serialVersionUID = 6866172377089832129L;
    private String vehicleMake;
	private String vehicleModel;
	private String vehicleColor;
	private String vehiclePlateNumber;
	private String vehicleVIN;
	private Integer vehicleYearRangeStart;
	private Integer vehicleYearRangeEnd;
	
	private List<String> sourceSystems;
	
	public String getVehicleMake() {
		return vehicleMake;
	}
	
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	
	public String getVehicleModel() {
		return vehicleModel;
	}
	
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	
	public String getVehicleColor() {
		return vehicleColor;
	}
	
	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}
	
	public String getVehiclePlateNumber() {
		return vehiclePlateNumber;
	}
	
	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}
	
	public String getVehicleVIN() {
		return vehicleVIN;
	}
	
	public void setVehicleVIN(String vehicleVIN) {
		this.vehicleVIN = vehicleVIN;
	}
	
	public Integer getVehicleYearRangeStart() {
		return vehicleYearRangeStart;
	}
	
	public void setVehicleYearRangeStart(Integer vehicleYearRangeStart) {
		this.vehicleYearRangeStart = vehicleYearRangeStart;
	}
	
	public Integer getVehicleYearRangeEnd() {
		return vehicleYearRangeEnd;
	}
	
	public void setVehicleYearRangeEnd(Integer vehicleYearRangeEnd) {
		this.vehicleYearRangeEnd = vehicleYearRangeEnd;
	}

	public List<String> getSourceSystems() {
		return sourceSystems;
	}

	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	@Override
	public String toString() {
		return "VehicleSearchRequest [vehicleMake=" + vehicleMake
				+ ", vehicleModel=" + vehicleModel + ", vehicleColor="
				+ vehicleColor + ", vehiclePlateNumber=" + vehiclePlateNumber
				+ ", vehicleVIN=" + vehicleVIN + ", vehicleYearRangeStart="
				+ vehicleYearRangeStart + ", vehicleYearRangeEnd="
				+ vehicleYearRangeEnd + ", sourceSystems=" + sourceSystems + "]";
	}
	
}
