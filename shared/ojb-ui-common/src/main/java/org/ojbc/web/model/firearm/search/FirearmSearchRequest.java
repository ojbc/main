package org.ojbc.web.model.firearm.search;

import java.io.Serializable;
import java.util.List;

import org.ojbc.firearm.search.SearchFieldMetadata;

public class FirearmSearchRequest implements Serializable{
    private static final long serialVersionUID = -766530557462573052L;
    private String firearmSerialNumber;
	private SearchFieldMetadata firearmSerialNumberMetaData;
	private String firearmRegistrationNumber;
	private String firearmMake;
	private String firearmModel;
	private String firearmCounty;
	private String firearmType;
	private Boolean firearmCurrentRegOnly;
	
	//Logging
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystems;
	
	public String getFirearmSerialNumber() {
		return firearmSerialNumber;
	}

	public void setFirearmSerialNumber(String serialNumber) {
		this.firearmSerialNumber = serialNumber;
	}

	public String getFirearmRegistrationNumber() {
		return firearmRegistrationNumber;
	}

	public void setFirearmRegistrationNumber(String registrationNumber) {
		this.firearmRegistrationNumber = registrationNumber;
	}

	public String getFirearmMake() {
		return firearmMake;
	}

	public void setFirearmMake(String make) {
		this.firearmMake = make;
	}

	public String getFirearmModel() {
		return firearmModel;
	}

	public void setFirearmModel(String model) {
		this.firearmModel = model;
	}

	public String getFirearmCounty() {
		return firearmCounty;
	}

	public void setFirearmCounty(String county) {
		this.firearmCounty = county;
	}

	public String getFirearmType() {
		return firearmType;
	}

	public void setFirearmType(String firearmType) {
		this.firearmType = firearmType;
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

	public Boolean getFirearmCurrentRegOnly() {
		return firearmCurrentRegOnly;
	}

	public void setFirearmCurrentRegOnly(Boolean currentRegOnly) {
		this.firearmCurrentRegOnly = currentRegOnly;
	}

	public SearchFieldMetadata getFirearmSerialNumberMetaData() {
		return firearmSerialNumberMetaData;
	}

	public void setFirearmSerialNumberMetaData(
			SearchFieldMetadata firearmSerialNumberMetaData) {
		this.firearmSerialNumberMetaData = firearmSerialNumberMetaData;
	}

	@Override
	public String toString() {
		return "FirearmSearchRequest [firearmSerialNumber="
				+ firearmSerialNumber + ", firearmSerialNumberMetaData="
				+ firearmSerialNumberMetaData + ", firearmRegistrationNumber="
				+ firearmRegistrationNumber + ", firearmMake=" + firearmMake
				+ ", firearmModel=" + firearmModel + ", firearmCounty="
				+ firearmCounty + ", firearmType=" + firearmType
				+ ", firearmCurrentRegOnly=" + firearmCurrentRegOnly
				+ ", purpose=" + purpose + ", onBehalfOf=" + onBehalfOf
				+ ", sourceSystems=" + sourceSystems + "]";
	}

}
