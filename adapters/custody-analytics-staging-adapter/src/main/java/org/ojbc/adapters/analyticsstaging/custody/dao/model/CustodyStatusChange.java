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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CustodyStatusChange implements Serializable{

	private static final long serialVersionUID = 5546398660510817311L;
	//pk
	private Integer custodyStatusChangeId;
	private Integer bookingId;
	private LocalDate bookingDate; 
	private LocalTime bookingTime; 
	private Integer pretrialStatusId; 
	private Integer facilityId; 
	private Integer supervisionUnitTypeId; 
	private Integer personId; 
	private String bookingNumber; 
	private LocalDate scheduledReleaseDate; 
    private Boolean inmateJailResidentIndicator;

    private CustodyRelease custodyRelease;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getPretrialStatusId() {
		return pretrialStatusId;
	}

	public void setPretrialStatusId(Integer pretrialStatusId) {
		this.pretrialStatusId = pretrialStatusId;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getSupervisionUnitTypeId() {
		return supervisionUnitTypeId;
	}

	public void setSupervisionUnitTypeId(Integer supervisionUnitTypeId) {
		this.supervisionUnitTypeId = supervisionUnitTypeId;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public CustodyRelease getCustodyRelease() {
		return custodyRelease;
	}

	public void setCustodyRelease(CustodyRelease custodyRelease) {
		this.custodyRelease = custodyRelease;
	}

	public Integer getCustodyStatusChangeId() {
		return custodyStatusChangeId;
	}

	public void setCustodyStatusChangeId(Integer custodyStatusChangeId) {
		this.custodyStatusChangeId = custodyStatusChangeId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDate getScheduledReleaseDate() {
		return scheduledReleaseDate;
	}

	public void setScheduledReleaseDate(LocalDate scheduledReleaseDate) {
		this.scheduledReleaseDate = scheduledReleaseDate;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Boolean getInmateJailResidentIndicator() {
		return inmateJailResidentIndicator;
	}

	public void setInmateJailResidentIndicator(
			Boolean inmateJailResidentIndicator) {
		this.inmateJailResidentIndicator = inmateJailResidentIndicator;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public LocalTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalTime bookingTime) {
		this.bookingTime = bookingTime;
	}


}
