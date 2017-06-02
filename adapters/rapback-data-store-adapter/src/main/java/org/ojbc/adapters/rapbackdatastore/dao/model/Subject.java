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
package org.ojbc.adapters.rapbackdatastore.dao.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

public class Subject implements Serializable{

	private static final long serialVersionUID = -3468102170137653133L;
	private Integer subjectId; 
	private String ucn; //FBI ID 
	private String criminalSid; 
	private String civilSid; 
	private String firstName; 
	private String lastName; 
	private String middleInitial; 
	private DateTime dob;
	private String sexCode; 

	public Subject(){
		super();
	}
	
	public Subject(Integer subjectId, String ucn, String criminalSid, 
			String civilSid, String firstName, String lastName, String middleInitial, 
			String sexCode, DateTime dob){
		this();
		this.subjectId = subjectId; 
		this.ucn = ucn; 
		this.criminalSid = criminalSid; 
		this.civilSid = civilSid; 
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.middleInitial = middleInitial; 
		this.dob = dob; 
		this.sexCode = sexCode;
	}
	
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectID) {
		this.subjectId = subjectID;
	}
	public String getUcn() {
		return ucn;
	}
	public void setUcn(String ucn) {
		this.ucn = ucn;
	}
	public String getCriminalSid() {
		return criminalSid;
	}
	public void setCriminalSid(String criminalSid) {
		this.criminalSid = criminalSid;
	}
	public String getCivilSid() {
		return civilSid;
	}
	public void setCivilSid(String civilSid) {
		this.civilSid = civilSid;
	}
	public DateTime getDob() {
		return dob;
	}
	public void setDob(DateTime dob) {
		this.dob = dob;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	
	/**
     * Builds and returns the formatted person full name.
     */
	public String getFullName() {
    	return getFullName(false);
    }

	/**
     * Builds and returns a formatted person full name.
	 * Note: first and last name are required.
	 * @param lastNameFirst
	 * @return
	 */
	public String getFullName(boolean lastNameFirst) {
    	StringBuilder sb = new StringBuilder(64);

    	if (lastNameFirst) {
        	sb.append(lastName);
        	sb.append(", ");
        	sb.append(firstName);
        	if (StringUtils.isNotEmpty(middleInitial)) {
        		sb.append(" ");
        		sb.append(middleInitial);
        	}
    	}
    	else {
        	sb.append(firstName);
        	if (StringUtils.isNotEmpty(middleInitial)) {
        		sb.append(" ");
        		sb.append(middleInitial);
        	}
        	sb.append(" ");
        	sb.append(lastName);
    	}

    	return sb.toString();
    }
}
