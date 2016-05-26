package org.ojbc.warrant.repository.model;

public class PersonSSNAdditional {

	private Integer personSSNID;
	
	private Integer personID;
	
	private String socialSecurityNumber;

	public Integer getPersonSSNID() {
		return personSSNID;
	}

	public void setPersonSSNID(Integer personSSNID) {
		this.personSSNID = personSSNID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

}
