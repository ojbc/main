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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticsstaging.custody.dao.model;

import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {

	//pk
    private Integer personId;
    
    //fk
    private Integer personSexId; 
    //fk
    private Integer personRaceId;     
    
    private String personSexCode;
    private String personRaceCode;
    private String personSexDescription; 
    private String personRaceDescription;
    
    private Integer personEthnicityTypeId;
    private String personEthnicityTypeDescription;
    private String language;
    
    private LocalDate personBirthDate;
    private String personUniqueIdentifier;
    private Integer languageId; //primary language 
    
    private Integer sexOffenderStatusTypeId;
	private Integer personAgeAtBooking; 
	private Integer educationLevelId; 
	private Integer occupationId; 
	private Integer domicileStatusTypeId; 
	private Integer workReleaseStatusTypeId; 
	private Integer programEligibilityTypeId; 
	private Boolean inmateTemporarilyReleasedIndicator; 
	private KeyValue militaryServiceStatusType; 
    
	public LocalDate getPersonBirthDate() {
		return personBirthDate;
	}
	public void setPersonBirthDate(LocalDate personBirthDate) {
		this.personBirthDate = personBirthDate;
	}
	public String getPersonUniqueIdentifier() {
		return personUniqueIdentifier;
	}
	public void setPersonUniqueIdentifier(String personUniqueIdentifier) {
		this.personUniqueIdentifier = personUniqueIdentifier;
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
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPersonSexCode() {
		return personSexCode;
	}
	public void setPersonSexCode(String personSexCode) {
		this.personSexCode = personSexCode;
	}
	public String getPersonRaceCode() {
		return personRaceCode;
	}
	public void setPersonRaceCode(String personRaceCode) {
		this.personRaceCode = personRaceCode;
	}
	public Integer getPersonSexId() {
		return personSexId;
	}
	public void setPersonSexId(Integer personSexId) {
		this.personSexId = personSexId;
	}
	public Integer getPersonRaceId() {
		return personRaceId;
	}
	public void setPersonRaceId(Integer personRaceId) {
		this.personRaceId = personRaceId;
	}
	
	public Integer getPersonAgeAtBooking() {
		return personAgeAtBooking;
	}

	public void setPersonAgeAtBooking(Integer personAgeAtBooking) {
		this.personAgeAtBooking = personAgeAtBooking;
	}

	public Integer getEducationLevelId() {
		return educationLevelId;
	}

	public void setEducationLevelId(Integer educationLevelId) {
		this.educationLevelId = educationLevelId;
	}

	public Integer getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Boolean getInmateTemporarilyReleasedIndicator() {
		return inmateTemporarilyReleasedIndicator;
	}

	public void setInmateTemporarilyReleasedIndicator(
			Boolean inmateTemporarilyReleasedIndicator) {
		this.inmateTemporarilyReleasedIndicator = inmateTemporarilyReleasedIndicator;
	}

	public KeyValue getMilitaryServiceStatusType() {
		return militaryServiceStatusType;
	}

	public void setMilitaryServiceStatusType(KeyValue militaryServiceStatusType) {
		this.militaryServiceStatusType = militaryServiceStatusType;
	}
	public Integer getDomicileStatusTypeId() {
		return domicileStatusTypeId;
	}
	public void setDomicileStatusTypeId(Integer domicileStatusTypeId) {
		this.domicileStatusTypeId = domicileStatusTypeId;
	}
	public Integer getWorkReleaseStatusTypeId() {
		return workReleaseStatusTypeId;
	}
	public void setWorkReleaseStatusTypeId(Integer workReleaseStatusTypeId) {
		this.workReleaseStatusTypeId = workReleaseStatusTypeId;
	}
	public Integer getProgramEligibilityTypeId() {
		return programEligibilityTypeId;
	}
	public void setProgramEligibilityTypeId(Integer programEligibilityTypeId) {
		this.programEligibilityTypeId = programEligibilityTypeId;
	}
	public String getPersonEthnicityTypeDescription() {
		return personEthnicityTypeDescription;
	}
	public void setPersonEthnicityTypeDescription(
			String personEthnicityTypeDescription) {
		this.personEthnicityTypeDescription = personEthnicityTypeDescription;
	}
	public Integer getPersonEthnicityTypeId() {
		return personEthnicityTypeId;
	}
	public void setPersonEthnicityTypeId(Integer personEthnicityTypeId) {
		this.personEthnicityTypeId = personEthnicityTypeId;
	}
	public Integer getSexOffenderStatusTypeId() {
		return sexOffenderStatusTypeId;
	}
	public void setSexOffenderStatusTypeId(Integer sexOffenderStatusTypeId) {
		this.sexOffenderStatusTypeId = sexOffenderStatusTypeId;
	}
	
}
