package org.ojbc.warrant.repository.model;

public class ChargeReferral {

	private Integer chargeReferralID; 
	
	private Integer personID;
	
	private String chargeReferralCaseAgencyComplaintNumber;
	
	private String transactionControlNumber;

	private String reportingAgencyORI;
	
	private String reportingAgencyName;
	
	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getChargeReferralID() {
		return chargeReferralID;
	}

	public void setChargeReferralID(Integer chargeReferralID) {
		this.chargeReferralID = chargeReferralID;
	}

	public String getChargeReferralCaseAgencyComplaintNumber() {
		return chargeReferralCaseAgencyComplaintNumber;
	}

	public void setChargeReferralCaseAgencyComplaintNumber(
			String chargeReferralCaseAgencyComplaintNumber) {
		this.chargeReferralCaseAgencyComplaintNumber = chargeReferralCaseAgencyComplaintNumber;
	}

	public String getTransactionControlNumber() {
		return transactionControlNumber;
	}

	public void setTransactionControlNumber(String transactionControlNumber) {
		this.transactionControlNumber = transactionControlNumber;
	}

	public String getReportingAgencyORI() {
		return reportingAgencyORI;
	}

	public void setReportingAgencyORI(String reportingAgencyORI) {
		this.reportingAgencyORI = reportingAgencyORI;
	}

	public String getReportingAgencyName() {
		return reportingAgencyName;
	}

	public void setReportingAgencyName(String reportingAgencyName) {
		this.reportingAgencyName = reportingAgencyName;
	}
}
