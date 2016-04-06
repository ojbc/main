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
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BookingSubject implements Serializable
{
	private static final long serialVersionUID = 3530940339345199825L;
	private Integer bookingSubjectId;
	private Integer recidivistIndicator = 0; 
	private Integer personId; 
	private String bookingNumber; 
	private Integer personAge; 
	private Integer educationLevelId; 
	private Integer occupationId; 
	private Integer incomeLevelId; 
	private Integer housingStatusId; 

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getBookingSubjectId() {
		return bookingSubjectId;
	}

	public void setBookingSubjectId(Integer bookingSubjectId) {
		this.bookingSubjectId = bookingSubjectId;
	}

	public Integer getRecidivistIndicator() {
		return recidivistIndicator;
	}

	public void setRecidivistIndicator(Integer recidivistIndicator) {
		this.recidivistIndicator = recidivistIndicator;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public Integer getPersonAge() {
		return personAge;
	}

	public void setPersonAge(Integer personAge) {
		this.personAge = personAge;
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

	public Integer getIncomeLevelId() {
		return incomeLevelId;
	}

	public void setIncomeLevelId(Integer incomeLevelId) {
		this.incomeLevelId = incomeLevelId;
	}

	public Integer getHousingStatusId() {
		return housingStatusId;
	}

	public void setHousingStatusId(Integer housingStatusId) {
		this.housingStatusId = housingStatusId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
}