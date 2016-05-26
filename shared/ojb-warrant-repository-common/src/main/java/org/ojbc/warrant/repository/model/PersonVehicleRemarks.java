package org.ojbc.warrant.repository.model;

public class PersonVehicleRemarks {

	private Integer personVehicleRemarksID;
    private Integer personVehicleID;
    private String vehicleRemarkCategoryCode;
    private String vehicleRemarkText;
    

	public Integer getPersonVehicleID() {
		return personVehicleID;
	}
	public void setPersonVehicleID(Integer personVehicleID) {
		this.personVehicleID = personVehicleID;
	}
	public String getVehicleRemarkCategoryCode() {
		return vehicleRemarkCategoryCode;
	}
	public void setVehicleRemarkCategoryCode(String vehicleRemarkCategoryCode) {
		this.vehicleRemarkCategoryCode = vehicleRemarkCategoryCode;
	}
	public String getVehicleRemarkText() {
		return vehicleRemarkText;
	}
	public void setVehicleRemarkText(String vehicleRemarkText) {
		this.vehicleRemarkText = vehicleRemarkText;
	}
	public Integer getPersonVehicleRemarksID() {
		return personVehicleRemarksID;
	}
	public void setPersonVehicleRemarksID(Integer personVehicleRemarksID) {
		this.personVehicleRemarksID = personVehicleRemarksID;
	}
}
