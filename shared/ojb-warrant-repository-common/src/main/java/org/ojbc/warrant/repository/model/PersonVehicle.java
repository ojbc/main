package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PersonVehicle {

	private Integer personVehicleID;
	
	private Integer personID;

	private String licensePlateType;
	
	private String vehicleLicensePlateExpirationDate;

	private Boolean vehicleNonExpiringIndicator;
	
	private String vehicleLicensePlateNumber;

	private String vehicleLicenseStateCode;
	
	private String vehicleIdentificationNumber;
	
	private String vehicleYear;
	
	private String vehicleMake;
	
	private String vehicleModel;
	
	private String vehicleStyle;
	
	private String vehiclePrimaryColor;
	
	private String vehicleSecondaryColor;

	public Integer getPersonVehicleID() {
		return personVehicleID;
	}

	public void setPersonVehicleID(Integer personVehicleID) {
		this.personVehicleID = personVehicleID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}



	public String getVehicleLicensePlateNumber() {
		return vehicleLicensePlateNumber;
	}

	public void setVehicleLicensePlateNumber(String vehicleLicensePlateNumber) {
		this.vehicleLicensePlateNumber = vehicleLicensePlateNumber;
	}

	public String getVehicleLicenseStateCode() {
		return vehicleLicenseStateCode;
	}

	public void setVehicleLicenseStateCode(String vehicleLicenseStateCode) {
		this.vehicleLicenseStateCode = vehicleLicenseStateCode;
	}

	public String getVehicleIdentificationNumber() {
		return vehicleIdentificationNumber;
	}

	public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}

	public String getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(String vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

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

	public String getVehicleStyle() {
		return vehicleStyle;
	}

	public void setVehicleStyle(String vehicleStyle) {
		this.vehicleStyle = vehicleStyle;
	}

	public String getVehiclePrimaryColor() {
		return vehiclePrimaryColor;
	}

	public void setVehiclePrimaryColor(String vehiclePrimaryColor) {
		this.vehiclePrimaryColor = vehiclePrimaryColor;
	}

	public String getVehicleSecondaryColor() {
		return vehicleSecondaryColor;
	}

	public void setVehicleSecondaryColor(String vehicleSecondaryColor) {
		this.vehicleSecondaryColor = vehicleSecondaryColor;
	}

	public String getVehicleLicensePlateExpirationDate() {
		return vehicleLicensePlateExpirationDate;
	}

	public void setVehicleLicensePlateExpirationDate(
			String vehicleLicensePlateExpirationDate) {
		this.vehicleLicensePlateExpirationDate = vehicleLicensePlateExpirationDate;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Boolean getVehicleNonExpiringIndicator() {
		return vehicleNonExpiringIndicator;
	}

	public void setVehicleNonExpiringIndicator(
			Boolean vehicleNonExpiringIndicator) {
		this.vehicleNonExpiringIndicator = vehicleNonExpiringIndicator;
	}


}
