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
    private String language;
    
    private LocalDate personBirthDate;
    private String personUniqueIdentifier;
    private String bookingSubjectNumber;
    private Integer languageId; //primary language 
    
    private String personSsn; 
    private String personSid; 
    private String personEyeColor; 
    private String personHairColor; 
    private String personHeight; 
    private String personHeightMeasureUnit; 
    private String personWeight; 
    private String personWeightMeasureUnit; 
    private Boolean registeredSexOffender;
    
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
	public String getBookingSubjectNumber() {
		return bookingSubjectNumber;
	}
	public void setBookingSubjectNumber(String bookingSubjectNumber) {
		this.bookingSubjectNumber = bookingSubjectNumber;
	}
	public String getPersonSsn() {
		return personSsn;
	}
	public void setPersonSsn(String personSsn) {
		this.personSsn = personSsn;
	}
	public String getPersonSid() {
		return personSid;
	}
	public void setPersonSid(String personSid) {
		this.personSid = personSid;
	}
	public String getPersonEyeColor() {
		return personEyeColor;
	}
	public void setPersonEyeColor(String personEyeColor) {
		this.personEyeColor = personEyeColor;
	}
	public String getPersonHairColor() {
		return personHairColor;
	}
	public void setPersonHairColor(String personHairColor) {
		this.personHairColor = personHairColor;
	}
	public String getPersonHeight() {
		return personHeight;
	}
	public void setPersonHeight(String personHeight) {
		this.personHeight = personHeight;
	}
	public String getPersonHeightMeasureUnit() {
		return personHeightMeasureUnit;
	}
	public void setPersonHeightMeasureUnit(String personHeightMeasureUnit) {
		this.personHeightMeasureUnit = personHeightMeasureUnit;
	}
	public String getPersonWeight() {
		return personWeight;
	}
	public void setPersonWeight(String personWeight) {
		this.personWeight = personWeight;
	}
	public String getPersonWeightMeasureUnit() {
		return personWeightMeasureUnit;
	}
	public void setPersonWeightMeasureUnit(String personWeightMeasureUnit) {
		this.personWeightMeasureUnit = personWeightMeasureUnit;
	}
	public Boolean getRegisteredSexOffender() {
		return registeredSexOffender;
	}
	public void setRegisteredSexOffender(Boolean registeredSexOffender) {
		this.registeredSexOffender = registeredSexOffender;
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
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
}
