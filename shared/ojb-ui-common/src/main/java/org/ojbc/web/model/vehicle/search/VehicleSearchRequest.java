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
