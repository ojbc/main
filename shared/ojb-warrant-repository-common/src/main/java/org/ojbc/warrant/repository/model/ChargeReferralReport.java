package org.ojbc.warrant.repository.model;

import java.util.List;

public class ChargeReferralReport {

	private List<Officer> officers;
	
	private ChargeReferral chargeReferral;
	
	private List<Charge> charges;
	
	private Person person;
	
	private Integer warrantId;
	
	public List<Officer> getOfficers() {
		return officers;
	}

	public void setOfficers(List<Officer> officers) {
		this.officers = officers;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ChargeReferral getChargeReferral() {
		return chargeReferral;
	}

	public void setChargeReferral(ChargeReferral chargeReferral) {
		this.chargeReferral = chargeReferral;
	}

	public Integer getWarrantId() {
		return warrantId;
	}

	public void setWarrantId(Integer warrantId) {
		this.warrantId = warrantId;
	}

	
}
