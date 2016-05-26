package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PersonAlternateName {
	
	private Integer personAlternateNameID;
	
	private Integer personID;
	
	private String firstName;
	
	private String fullPersonName;
	
	private String lastName;
	
	private String nameSuffix;
	
	private String middleName;

	public Integer getPersonAlternateNameID() {
		return personAlternateNameID;
	}

	public void setPersonAlternateNameID(Integer personAlternateNameID) {
		this.personAlternateNameID = personAlternateNameID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullPersonName() {
		return fullPersonName;
	}

	public void setFullPersonName(String fullPersonName) {
		this.fullPersonName = fullPersonName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
