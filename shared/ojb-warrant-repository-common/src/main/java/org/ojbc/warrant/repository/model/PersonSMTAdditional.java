package org.ojbc.warrant.repository.model;

public class PersonSMTAdditional {

	private Integer personSMTSupplementalID;
	
	private Integer personID;
	
	private String personScarsMarksTattoos;

	public Integer getPersonSMTAdditionalID() {
		return personSMTSupplementalID;
	}

	public void setPersonSMTAdditionalID(Integer personSMTSupplementalID) {
		this.personSMTSupplementalID = personSMTSupplementalID;
	}

	public String getPersonScarsMarksTattoos() {
		return personScarsMarksTattoos;
	}

	public void setPersonScarsMarksTattoos(String personScarsMarksTattoos) {
		this.personScarsMarksTattoos = personScarsMarksTattoos;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

}
