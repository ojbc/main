package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PersonOLNAdditional {

	private Integer personOLNID;

	private Integer personID;
	
	private String operatorLicenseNumber;

	private String operatorLicenseState;

	public Integer getPersonOLNID() {
		return personOLNID;
	}

	public void setPersonOLNID(Integer personOLNID) {
		this.personOLNID = personOLNID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getOperatorLicenseNumber() {
		return operatorLicenseNumber;
	}

	public void setOperatorLicenseNumber(String operatorLicenseNumber) {
		this.operatorLicenseNumber = operatorLicenseNumber;
	}

	public String getOperatorLicenseState() {
		return operatorLicenseState;
	}

	public void setOperatorLicenseState(String operatorLicenseState) {
		this.operatorLicenseState = operatorLicenseState;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
