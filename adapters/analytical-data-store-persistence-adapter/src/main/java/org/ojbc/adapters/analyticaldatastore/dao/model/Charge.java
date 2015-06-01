package org.ojbc.adapters.analyticaldatastore.dao.model;

public class Charge {

	//pk
	private int chargeID;
	
	//fk
	private int arrestOffenseTypeID;
	
	//fk
	private int arrestID;

	public int getChargeID() {
		return chargeID;
	}

	public void setChargeID(int chargeID) {
		this.chargeID = chargeID;
	}

	public int getArrestOffenseTypeID() {
		return arrestOffenseTypeID;
	}

	public void setArrestOffenseTypeID(int arrestOffenseTypeID) {
		this.arrestOffenseTypeID = arrestOffenseTypeID;
	}

	public int getArrestID() {
		return arrestID;
	}

	public void setArrestID(int arrestID) {
		this.arrestID = arrestID;
	}
}
