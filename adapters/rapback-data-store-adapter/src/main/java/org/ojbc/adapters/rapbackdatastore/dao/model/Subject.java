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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((civilSid == null) ? 0 : civilSid.hashCode());
		result = prime * result
				+ ((criminalSid == null) ? 0 : criminalSid.hashCode());
		result = prime * result + ((dob == null) ? 0 : dob.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleInitial == null) ? 0 : middleInitial.hashCode());
		result = prime * result + ((sexCode == null) ? 0 : sexCode.hashCode());
		result = prime * result
				+ ((subjectId == null) ? 0 : subjectId.hashCode());
		result = prime * result + ((ucn == null) ? 0 : ucn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		if (civilSid == null) {
			if (other.civilSid != null)
				return false;
		} else if (!civilSid.equals(other.civilSid))
			return false;
		if (criminalSid == null) {
			if (other.criminalSid != null)
				return false;
		} else if (!criminalSid.equals(other.criminalSid))
			return false;
		if (dob == null) {
			if (other.dob != null)
				return false;
		} else if (!dob.equals(other.dob))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleInitial == null) {
			if (other.middleInitial != null)
				return false;
		} else if (!middleInitial.equals(other.middleInitial))
			return false;
		if (sexCode == null) {
			if (other.sexCode != null)
				return false;
		} else if (!sexCode.equals(other.sexCode))
			return false;
		if (subjectId == null) {
			if (other.subjectId != null)
				return false;
		} else if (!subjectId.equals(other.subjectId))
			return false;
		if (ucn == null) {
			if (other.ucn != null)
				return false;
		} else if (!ucn.equals(other.ucn))
			return false;
		return true;
	}
}
