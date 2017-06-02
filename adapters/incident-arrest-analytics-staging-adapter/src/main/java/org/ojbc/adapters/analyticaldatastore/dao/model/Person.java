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
package org.ojbc.adapters.analyticaldatastore.dao.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {

	//pk
    private Integer personID;
    
    //fk
    private Integer personSexID;
    
    //fk
    private Integer personRaceID;
        
    private String personSexDescription; 
    private String personRaceDescription;
    
    private Date personBirthDate;
    private String personUniqueIdentifier;
    
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public Date getPersonBirthDate() {
		return personBirthDate;
	}
	public void setPersonBirthDate(Date personBirthDate) {
		this.personBirthDate = personBirthDate;
	}
	public String getPersonUniqueIdentifier() {
		return personUniqueIdentifier;
	}
	public void setPersonUniqueIdentifier(String personUniqueIdentifier) {
		this.personUniqueIdentifier = personUniqueIdentifier;
	}
	public Integer getPersonSexID() {
		return personSexID;
	}
	public void setPersonSexID(Integer personSexID) {
		this.personSexID = personSexID;
	}
	public Integer getPersonRaceID() {
		return personRaceID;
	}
	public void setPersonRaceID(Integer personRaceID) {
		this.personRaceID = personRaceID;
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
