package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantIssueReport {

	private Warrant warrant;
	private PersonVehicle personVehicle;
	private Person person;

	public Warrant getWarrant() {
		return warrant;
	}

	public void setWarrant(Warrant warrant) {
		this.warrant = warrant;
	}

	public PersonVehicle getPersonVehicle() {
		return personVehicle;
	}

	public void setPersonVehicle(PersonVehicle personVehicle) {
		this.personVehicle = personVehicle;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
