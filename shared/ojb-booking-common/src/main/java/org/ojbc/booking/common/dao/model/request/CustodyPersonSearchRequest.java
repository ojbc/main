package org.ojbc.booking.common.dao.model.request;

import java.util.Date;

public class CustodyPersonSearchRequest {
	
	private Integer ageMin;
	
	private Integer ageMax;
	
	private String alternateGivenName;
	
	private String alternateSurName;
	
	private Date dob;
	
	private String eyeColor;
	
	private String hairColor;
	
	private Integer height;
	
	private Integer heightRangeMin;
	
	private Integer heightRangeMax;
	
	private Integer weight;
	
	private Integer weightRangeMin;
	
	private Integer weightRangeMax;
	
	private String givenName;
	
	private boolean givenNameHasStartsWithQualifier;
	
	private String middleName;
	
	private String surName;
	
	private boolean surNameHasStartsWithQualifier;

	private String raceCode;
	
	private String sexCode;
		
	private Date dobRangeStart;
	
	private Date dobRangeEnd;	
	
	private String stateId;

	private String sourceSystemNameTxt;
		
	private String searchRequestOnBehalfOf;
	
	private String searchPurpose;

	public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public String getAlternateGivenName() {
		return alternateGivenName;
	}

	public void setAlternateGivenName(String alternateGivenName) {
		this.alternateGivenName = alternateGivenName;
	}

	public String getAlternateSurName() {
		return alternateSurName;
	}

	public void setAlternateSurName(String alternateSurName) {
		this.alternateSurName = alternateSurName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(String eyeColor) {
		this.eyeColor = eyeColor;
	}

	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getHeightRangeMin() {
		return heightRangeMin;
	}

	public void setHeightRangeMin(Integer heightRangeMin) {
		this.heightRangeMin = heightRangeMin;
	}

	public Integer getHeightRangeMax() {
		return heightRangeMax;
	}

	public void setHeightRangeMax(Integer heightRangeMax) {
		this.heightRangeMax = heightRangeMax;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getWeightRangeMin() {
		return weightRangeMin;
	}

	public void setWeightRangeMin(Integer weightRangeMin) {
		this.weightRangeMin = weightRangeMin;
	}

	public Integer getWeightRangeMax() {
		return weightRangeMax;
	}

	public void setWeightRangeMax(Integer weightRangeMax) {
		this.weightRangeMax = weightRangeMax;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public boolean isGivenNameHasStartsWithQualifier() {
		return givenNameHasStartsWithQualifier;
	}

	public void setGivenNameHasStartsWithQualifier(
			boolean givenNameHasStartsWithQualifier) {
		this.givenNameHasStartsWithQualifier = givenNameHasStartsWithQualifier;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public boolean isSurNameHasStartsWithQualifier() {
		return surNameHasStartsWithQualifier;
	}

	public void setSurNameHasStartsWithQualifier(
			boolean surNameHasStartsWithQualifier) {
		this.surNameHasStartsWithQualifier = surNameHasStartsWithQualifier;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}

	public Date getDobRangeStart() {
		return dobRangeStart;
	}

	public void setDobRangeStart(Date dobRangeStart) {
		this.dobRangeStart = dobRangeStart;
	}

	public Date getDobRangeEnd() {
		return dobRangeEnd;
	}

	public void setDobRangeEnd(Date dobRangeEnd) {
		this.dobRangeEnd = dobRangeEnd;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getSourceSystemNameTxt() {
		return sourceSystemNameTxt;
	}

	public void setSourceSystemNameTxt(String sourceSystemNameTxt) {
		this.sourceSystemNameTxt = sourceSystemNameTxt;
	}

	public String getSearchRequestOnBehalfOf() {
		return searchRequestOnBehalfOf;
	}

	public void setSearchRequestOnBehalfOf(String searchRequestOnBehalfOf) {
		this.searchRequestOnBehalfOf = searchRequestOnBehalfOf;
	}

	public String getSearchPurpose() {
		return searchPurpose;
	}

	public void setSearchPurpose(String searchPurpose) {
		this.searchPurpose = searchPurpose;
	}

	@Override
	public String toString() {
		return "CustodyPersonSearchRequest [ageMin=" + ageMin + ", ageMax="
				+ ageMax + ", alternateGivenName=" + alternateGivenName
				+ ", alternateSurName=" + alternateSurName + ", dob=" + dob
				+ ", eyeColor=" + eyeColor + ", hairColor=" + hairColor
				+ ", height=" + height + ", heightRangeMin=" + heightRangeMin
				+ ", heightRangeMax=" + heightRangeMax + ", weight=" + weight
				+ ", weightRangeMin=" + weightRangeMin + ", weightRangeMax="
				+ weightRangeMax + ", givenName=" + givenName
				+ ", givenNameHasStartsWithQualifier="
				+ givenNameHasStartsWithQualifier + ", middleName="
				+ middleName + ", surName=" + surName
				+ ", surNameHasStartsWithQualifier="
				+ surNameHasStartsWithQualifier + ", raceCode=" + raceCode
				+ ", sexCode=" + sexCode + ", dobRangeStart=" + dobRangeStart
				+ ", dobRangeEnd=" + dobRangeEnd + ", stateId=" + stateId
				+ ", sourceSystemNameTxt=" + sourceSystemNameTxt
				+ ", searchRequestOnBehalfOf=" + searchRequestOnBehalfOf
				+ ", searchPurpose=" + searchPurpose + "]";
	}

}
