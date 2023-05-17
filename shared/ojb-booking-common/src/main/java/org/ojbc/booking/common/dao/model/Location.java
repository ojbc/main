package org.ojbc.booking.common.dao.model;


public class Location {
		
	
	private Integer arrestId;
	
	private String streetNumber;
	
	private String streetName;
	
	private String addressSecondaryUnit;
	
	private String city;
	
	private String stateCode;
	
	private String postalCode;

	
	public Integer getArrestId() {
		return arrestId;
	}

	public void setArrestId(Integer arrestId) {
		this.arrestId = arrestId;
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

	public String getAddressSecondaryUnit() {
		return addressSecondaryUnit;
	}

	public void setAddressSecondaryUnit(String addressSecondaryUnit) {
		this.addressSecondaryUnit = addressSecondaryUnit;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public String toString() {
		return "Location [arrestId=" + arrestId + ", streetNumber="
				+ streetNumber + ", streetName=" + streetName
				+ ", addressSecondaryUnit=" + addressSecondaryUnit + ", city="
				+ city + ", stateCode=" + stateCode + ", postalCode="
				+ postalCode + "]";
	}
		
}
