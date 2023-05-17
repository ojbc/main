package org.ojbc.booking.common.dao.model;

import java.util.Date;


public class Charge {
		
	private int id;
	
	private Integer arrestId;

	private Float bondAmount;

	private String bondType;

	private String bondStatus;

	private String nextCourtEventCourtName;

	private Date nextCourtDate;

	private Integer chargeSequenceNumber;

	private String chargeDescription;

	private String statuteOrOrdinanceNumber;

	private String chargeCategoryClassification;

	private String holdingForAgency;

	private String caseJurisdictionCourt;

	private Date sentenceDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getArrestId() {
		return arrestId;
	}

	public void setArrestId(Integer arrestId) {
		this.arrestId = arrestId;
	}

	public String getBondType() {
		return bondType;
	}

	public void setBondType(String bondType) {
		this.bondType = bondType;
	}

	public String getBondStatus() {
		return bondStatus;
	}

	public void setBondStatus(String bondStatus) {
		this.bondStatus = bondStatus;
	}

	public String getNextCourtEventCourtName() {
		return nextCourtEventCourtName;
	}

	public void setNextCourtEventCourtName(String nextCourtEventCourtName) {
		this.nextCourtEventCourtName = nextCourtEventCourtName;
	}

	public Date getNextCourtDate() {
		return nextCourtDate;
	}

	public void setNextCourtDate(Date nextCourtDate) {
		this.nextCourtDate = nextCourtDate;
	}

	public Integer getChargeSequenceNumber() {
		return chargeSequenceNumber;
	}

	public void setChargeSequenceNumber(Integer chargeSequenceNumber) {
		this.chargeSequenceNumber = chargeSequenceNumber;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public String getStatuteOrOrdinanceNumber() {
		return statuteOrOrdinanceNumber;
	}

	public void setStatuteOrOrdinanceNumber(String statuteOrOrdinanceNumber) {
		this.statuteOrOrdinanceNumber = statuteOrOrdinanceNumber;
	}

	public String getChargeCategoryClassification() {
		return chargeCategoryClassification;
	}

	public void setChargeCategoryClassification(String chargeCategoryClassification) {
		this.chargeCategoryClassification = chargeCategoryClassification;
	}

	public String getHoldingForAgency() {
		return holdingForAgency;
	}

	public void setHoldingForAgency(String holdingForAgency) {
		this.holdingForAgency = holdingForAgency;
	}

	public String getCaseJurisdictionCourt() {
		return caseJurisdictionCourt;
	}

	public void setCaseJurisdictionCourt(String caseJurisdictionCourt) {
		this.caseJurisdictionCourt = caseJurisdictionCourt;
	}

	public Float getBondAmount() {
		return bondAmount;
	}

	public void setBondAmount(Float bondAmount) {
		this.bondAmount = bondAmount;
	}

	public Date getSentenceDate() {
		return sentenceDate;
	}

	public void setSentenceDate(Date sentenceDate) {
		this.sentenceDate = sentenceDate;
	}

	@Override
	public String toString() {
		return "Charge [id=" + id + ", arrestId=" + arrestId + ", bondAmount="
				+ bondAmount + ", bondType=" + bondType + ", bondStatus="
				+ bondStatus + ", nextCourtEventCourtName="
				+ nextCourtEventCourtName + ", nextCourtDate=" + nextCourtDate
				+ ", chargeSequenceNumber=" + chargeSequenceNumber
				+ ", chargeDescription=" + chargeDescription
				+ ", statuteOrOrdinanceNumber=" + statuteOrOrdinanceNumber
				+ ", chargeCategoryClassification="
				+ chargeCategoryClassification + ", holdingForAgency="
				+ holdingForAgency + ", caseJurisdictionCourt="
				+ caseJurisdictionCourt + ", sentenceDate=" + sentenceDate
				+ "]";
	}

	

}

