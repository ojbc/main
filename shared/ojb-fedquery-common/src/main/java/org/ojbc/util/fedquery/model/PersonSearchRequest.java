/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.fedquery.model;


public class PersonSearchRequest {

	private String firstName;
	
	private String firstNameQualifier;
	
	private String middleName;
	
	private String lastName;
	
	private String lastNameQualifier;
	
	private String dobFrom;
	
	private String dobTo;
	
	private String dob;
	
	private String ageFrom;
	
	private String ageTo;
	
	private String ssn;
	
	private String driverLicenseId;
	
	private String driverLiscenseIssuer;
	
	private String fbiNumber;
	
	private String stateId;
	
	private String raceCode;
	
	private String genderCode;
	
	private String eyeCode;
	
	private String hairCode;
	
	private String heightMin;
	
	private String heightMax;
	
	private String weightMin;
	
	private String weightMax;
	
	
	public String getFirstName() {
		return firstName;
	}


	public String getFirstNameQualifier() {
		return firstNameQualifier;
	}


	public String getMiddleName() {
		return middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getLastNameQualifier() {
		return lastNameQualifier;
	}


	public String getDobFrom() {
		return dobFrom;
	}


	public String getDobTo() {
		return dobTo;
	}


	public String getAgeFrom() {
		return ageFrom;
	}


	public String getAgeTo() {
		return ageTo;
	}


	public String getSsn() {
		return ssn;
	}


	public String getDriverLicenseId() {
		return driverLicenseId;
	}


	public String getDriverLiscenseIssuer() {
		return driverLiscenseIssuer;
	}


	public String getFbiNumber() {
		return fbiNumber;
	}


	public String getStateId() {
		return stateId;
	}


	public String getRaceCode() {
		return raceCode;
	}


	public String getGenderCode() {
		return genderCode;
	}


	public String getEyeCode() {
		return eyeCode;
	}


	public String getHairCode() {
		return hairCode;
	}


	public String getHeightMin() {
		return heightMin;
	}


	public String getHeightMax() {
		return heightMax;
	}


	public String getWeightMin() {
		return weightMin;
	}


	public String getWeightMax() {
		return weightMax;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setFirstNameQualifier(String firstNameQualifier) {
		this.firstNameQualifier = firstNameQualifier;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setLastNameQualifier(String lastNameQualifier) {
		this.lastNameQualifier = lastNameQualifier;
	}


	public void setDobFrom(String dobFrom) {
		this.dobFrom = dobFrom;
	}


	public void setDobTo(String dobTo) {
		this.dobTo = dobTo;
	}


	public void setAgeFrom(String ageFrom) {
		this.ageFrom = ageFrom;
	}


	public void setAgeTo(String ageTo) {
		this.ageTo = ageTo;
	}


	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


	public void setDriverLicenseId(String driverLicenseId) {
		this.driverLicenseId = driverLicenseId;
	}


	public void setDriverLiscenseIssuer(String driverLiscenseIssuer) {
		this.driverLiscenseIssuer = driverLiscenseIssuer;
	}


	public void setFbiNumber(String fbiNumber) {
		this.fbiNumber = fbiNumber;
	}


	public void setStateId(String stateId) {
		this.stateId = stateId;
	}


	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}


	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}


	public void setEyeCode(String eyeCode) {
		this.eyeCode = eyeCode;
	}


	public void setHairCode(String hairCode) {
		this.hairCode = hairCode;
	}


	public void setHeightMin(String heightMin) {
		this.heightMin = heightMin;
	}


	public void setHeightMax(String heightMax) {
		this.heightMax = heightMax;
	}


	public void setWeightMin(String weightMin) {
		this.weightMin = weightMin;
	}


	public void setWeightMax(String weightMax) {
		this.weightMax = weightMax;
	}


	@Override
	public String toString() {

		return "firstName: " + firstName + "\n"
				+ "firstNameQualifier: " + firstNameQualifier + "\n"
				+ "middleName: " + middleName+ "\n"
				+ "lastName: " + lastName + "\n"
				+ "lastNameQualifier: " + lastNameQualifier+ "\n"
				+ "dobFrom: " + dobFrom+ "\n"
				+ "dobTo: " + dobTo+ "\n"
				+ "ageFrom: " + ageFrom+ "\n"
				+ "ageTo: " + ageTo+ "\n"
				+ "ssn: " + ssn+ "\n"
				+ "driverLicenseId: " + driverLicenseId+ "\n"
				+ "driverLiscenseIssuer: " + driverLiscenseIssuer+ "\n"
				+ "fbiNumber: " + fbiNumber+ "\n"
				+ "stateId:" + stateId + "\n"
				+ "raceCode: " + raceCode+ "\n"
				+ "genderCode: " + genderCode+ "\n"
				+ "eyeCode: " + eyeCode+ "\n"
				+ "hairCode: " + hairCode+ "\n"
				+ "heightMin: " + heightMin+ "\n"
				+ "heightMax: " + heightMax+ "\n"
				+ "weightMin: " + weightMin+ "\n"
				+ "weightMax: " + weightMax+ "\n";
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}
	
}

