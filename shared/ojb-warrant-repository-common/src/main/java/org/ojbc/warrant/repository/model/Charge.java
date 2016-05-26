package org.ojbc.warrant.repository.model;

public class Charge {

	private Integer chargeID;
	
	private Integer chargeReferralID;
	
	private String chargeSeverityLevel;

	public Integer getChargeID() {
		return chargeID;
	}

	public void setChargeID(Integer chargeID) {
		this.chargeID = chargeID;
	}

	public String getChargeSeverityLevel() {
		return chargeSeverityLevel;
	}

	public void setChargeSeverityLevel(String chargeSeverityLevel) {
		this.chargeSeverityLevel = chargeSeverityLevel;
	}

	public Integer getChargeReferralID() {
		return chargeReferralID;
	}

	public void setChargeReferralID(Integer chargeReferralID) {
		this.chargeReferralID = chargeReferralID;
	}

}
