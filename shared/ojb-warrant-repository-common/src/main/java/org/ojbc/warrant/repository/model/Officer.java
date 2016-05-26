package org.ojbc.warrant.repository.model;

public class Officer {

	private Integer officerID;
	
	private Integer chargeReferralID;
	
	private String officerName;
	
	private String officerBadgeNumber;

	public Integer getOfficerID() {
		return officerID;
	}

	public void setOfficerID(Integer officerID) {
		this.officerID = officerID;
	}

	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public String getOfficerBadgeNumber() {
		return officerBadgeNumber;
	}

	public void setOfficerBadgeNumber(String officerBadgeNumber) {
		this.officerBadgeNumber = officerBadgeNumber;
	}

	public Integer getChargeReferralID() {
		return chargeReferralID;
	}

	public void setChargeReferralID(Integer chargeReferralID) {
		this.chargeReferralID = chargeReferralID;
	}

}
