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
package org.ojbc.warrant.repository.model;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Person {

	private Integer personID;

    private String firstName;;
    private String middleName; 
    private String lastName;
    private String nameSuffix; 
    private String fullPersonName; 
    private String addressStreetFullText; 
    private String addressStreetNumber; 
    private String addressStreetName; 
    private String addressCity; 
    private String addressCounty; 
    private String addressState; 
    private String addressZip; 
    private String socialSecurityNumberBase; 
    private LocalDate dateOfBirth;
    private String placeOfBirth; 
    private String personAge; 
    private String operatorLicenseNumberBase; 
    private String operatorLicenseStateBase; 
    private String personEthnicityDescription; 
    private String personEyeColorDescription; 
    private String personHairColorDescription; 
    private String personSexDescription; 
    private String personRaceDescription; 
    private String personSkinToneDescription; 
    private String personHeight; 
    private String personWeight; 
    private String personScarsMarksTattosBase; 
    private String personCitizenshipCountry; 
    private String personStateIdentification; 
    private String fbiIdentificationNumber; 
    private String miscellaneousIDBase;
    private String prisonRecordNumber; 
    private String personCautionDescription;
    private Boolean usCitizenshipIndicator;
    private Boolean personImmigrationAlienQueryIndicator;
    
    private List<PersonAlternateName> personAlternateName;
    private List<PersonSSNAdditional> personSSNSupplemental;
    private List<PersonOLNAdditional> personOLNSupplementals;
    private List<PersonIDAdditional> personIDAdditionals;
    private List<PersonVehicle> personVehicles;
    private List<PersonSMTAdditional> personSMTSupplementals;
    private List<PersonDOBAdditional> personDOBSupplementals;
    
	private boolean personProcessingWarnings; 
	
	private StringBuffer personProcessingWarningMessage = new StringBuffer();
    
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	public String getFullPersonName() {
		return fullPersonName;
	}
	public void setFullPersonName(String fullPersonName) {
		this.fullPersonName = fullPersonName;
	}
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	public String getSocialSecurityNumberBase() {
		return socialSecurityNumberBase;
	}
	public void setSocialSecurityNumberBase(String socialSecurityNumberBase) {
		this.socialSecurityNumberBase = socialSecurityNumberBase;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	public String getPersonAge() {
		return personAge;
	}
	public void setPersonAge(String personAge) {
		this.personAge = personAge;
	}
	public String getOperatorLicenseNumberBase() {
		return operatorLicenseNumberBase;
	}
	public void setOperatorLicenseNumberBase(String operatorLicenseNumberBase) {
		this.operatorLicenseNumberBase = operatorLicenseNumberBase;
	}
	public String getOperatorLicenseStateBase() {
		return operatorLicenseStateBase;
	}
	public void setOperatorLicenseStateBase(String operatorLicenseStateBase) {
		this.operatorLicenseStateBase = operatorLicenseStateBase;
	}
	public String getPersonHeight() {
		return personHeight;
	}
	public void setPersonHeight(String personHeight) {
		this.personHeight = personHeight;
	}
	public String getPersonWeight() {
		return personWeight;
	}
	public void setPersonWeight(String personWeight) {
		this.personWeight = personWeight;
	}
	public String getPersonScarsMarksTattosBase() {
		return personScarsMarksTattosBase;
	}
	public void setPersonScarsMarksTattosBase(String personScarsMarksTattosBase) {
		this.personScarsMarksTattosBase = personScarsMarksTattosBase;
	}
	public String getPersonStateIdentification() {
		return personStateIdentification;
	}
	public void setPersonStateIdentification(String personStateIdentification) {
		this.personStateIdentification = personStateIdentification;
	}
	public String getFbiIdentificationNumber() {
		return fbiIdentificationNumber;
	}
	public void setFbiIdentificationNumber(String fbiIdentificationNumber) {
		this.fbiIdentificationNumber = fbiIdentificationNumber;
	}
	public String getMiscellaneousIDBase() {
		return miscellaneousIDBase;
	}
	public void setMiscellaneousIDBase(String miscellaneousIDBase) {
		this.miscellaneousIDBase = miscellaneousIDBase;
	}
	public String getPrisonRecordNumber() {
		return prisonRecordNumber;
	}
	public void setPrisonRecordNumber(String prisonRecordNumber) {
		this.prisonRecordNumber = prisonRecordNumber;
	}
	public List<PersonAlternateName> getPersonAlternateName() {
		return personAlternateName;
	}
	public void setPersonAlternateName(List<PersonAlternateName> personAlternateName) {
		this.personAlternateName = personAlternateName;
	}
	public List<PersonSSNAdditional> getPersonSSNAdditional() {
		return personSSNSupplemental;
	}
	public void setPersonSSNAdditional(
			List<PersonSSNAdditional> personSSNSupplemental) {
		this.personSSNSupplemental = personSSNSupplemental;
	}
	public List<PersonOLNAdditional> getPersonOLNAdditionals() {
		return personOLNSupplementals;
	}
	public void setPersonOLNAdditionals(
			List<PersonOLNAdditional> personOLNSupplementals) {
		this.personOLNSupplementals = personOLNSupplementals;
	}
	public List<PersonIDAdditional> getPersonIDAdditionals() {
		return personIDAdditionals;
	}
	public void setPersonIDAdditionals(
			List<PersonIDAdditional> personIDAdditionals) {
		this.personIDAdditionals = personIDAdditionals;
	}
	public List<PersonVehicle> getPersonVehicles() {
		return personVehicles;
	}
	public void setPersonVehicles(List<PersonVehicle> personVehicles) {
		this.personVehicles = personVehicles;
	}
	public List<PersonSMTAdditional> getPersonSMTAdditionals() {
		return personSMTSupplementals;
	}
	public void setPersonSMTAdditionals(
			List<PersonSMTAdditional> personSMTSupplementals) {
		this.personSMTSupplementals = personSMTSupplementals;
	}
	public Boolean getUsCitizenshipIndicator() {
		return usCitizenshipIndicator;
	}
	public void setUsCitizenshipIndicator(Boolean usCitizenshipIndicator) {
		this.usCitizenshipIndicator = usCitizenshipIndicator;
	}
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	public String getPersonEthnicityDescription() {
		return personEthnicityDescription;
	}
	public void setPersonEthnicityDescription(String personEthnicityDescription) {
		this.personEthnicityDescription = personEthnicityDescription;
	}
	public String getPersonEyeColorDescription() {
		return personEyeColorDescription;
	}
	public void setPersonEyeColorDescription(String personEyeColorDescription) {
		this.personEyeColorDescription = personEyeColorDescription;
	}
	public String getPersonHairColorDescription() {
		return personHairColorDescription;
	}
	public void setPersonHairColorDescription(String personHairColorDescription) {
		this.personHairColorDescription = personHairColorDescription;
	}
	public String getPersonSexDescription() {
		return personSexDescription;
	}
	public void setPersonSexDescription(String personSexDescription) {
		this.personSexDescription = personSexDescription;
	}
	public String getPersonRaceDescription() {
		return personRaceDescription;
	}
	public void setPersonRaceDescription(String personRaceDescription) {
		this.personRaceDescription = personRaceDescription;
	}
	public String getPersonSkinToneDescription() {
		return personSkinToneDescription;
	}
	public void setPersonSkinToneDescription(String personSkinToneDescription) {
		this.personSkinToneDescription = personSkinToneDescription;
	}
	public String getPersonCitizenshipCountry() {
		return personCitizenshipCountry;
	}
	public void setPersonCitizenshipCountry(String personCitizenshipCountry) {
		this.personCitizenshipCountry = personCitizenshipCountry;
	}
	public String getPersonCautionDescription() {
		return personCautionDescription;
	}
	public void setPersonCautionDescription(String personCautionDescription) {
		this.personCautionDescription = personCautionDescription;
	} 
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public Boolean getPersonImmigrationAlienQueryIndicator() {
		return personImmigrationAlienQueryIndicator;
	}
	public void setPersonImmigrationAlienQueryIndicator(
			Boolean personImmigrationAlienQueryIndicator) {
		this.personImmigrationAlienQueryIndicator = personImmigrationAlienQueryIndicator;
	}
	public String getAddressStreetName() {
		return addressStreetName;
	}
	public void setAddressStreetName(String addressStreetName) {
		this.addressStreetName = addressStreetName;
	}
	public String getAddressStreetNumber() {
		return addressStreetNumber;
	}
	public void setAddressStreetNumber(String addressStreetNumber) {
		this.addressStreetNumber = addressStreetNumber;
	}
	public String getAddressCounty() {
		return addressCounty;
	}
	public void setAddressCounty(String addressCounty) {
		this.addressCounty = addressCounty;
	}
	public String getAddressStreetFullText() {
		return addressStreetFullText;
	}
	public void setAddressStreetFullText(String addressStreetFullText) {
		this.addressStreetFullText = addressStreetFullText;
	}
	public List<PersonDOBAdditional> getPersonDOBSupplementals() {
		return personDOBSupplementals;
	}
	public void setPersonDOBSupplementals(
			List<PersonDOBAdditional> personDOBSupplementals) {
		this.personDOBSupplementals = personDOBSupplementals;
	}
	public boolean isPersonProcessingWarnings() {
		return personProcessingWarnings;
	}
	public void setPersonProcessingWarnings(boolean personProcessingWarnings) {
		this.personProcessingWarnings = personProcessingWarnings;
	}
	public StringBuffer getPersonProcessingWarningMessage() {
		return personProcessingWarningMessage;
	}
	public void setPersonProcessingWarningMessage(StringBuffer personProcessingWarningMessage) {
		this.personProcessingWarningMessage = personProcessingWarningMessage;
	}
}
