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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Booking implements Serializable{

	private static final long serialVersionUID = 714167300178163559L;
	//pk
	private Integer bookingId;
	private Integer jurisdictionId; 
	private LocalDateTime bookingReportDate;
	private Integer sendingAgencyId;
	private LocalDateTime bookingDate; 
	private LocalDate commitDate; 
	private LocalDateTime supervisionReleaseDate; 
	private String bookingReportId;
	private Integer caseStatusId; 
	private Integer pretrialStatusId; 
	private Integer facilityId; 
	private Integer bedTypeId; 
	private BigDecimal arrestLocationLatitude; 
	private BigDecimal arrestLocationLongitude; 
	private Integer bookingSubjectId; 
	private String bookingNumber; 
    private BigDecimal bondAmount; 
    private KeyValue bondType; 
	
	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getJurisdictionId() {
		return jurisdictionId;
	}

	public void setJurisdictionId(Integer jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	public LocalDateTime getBookingReportDate() {
		return bookingReportDate;
	}

	public void setBookingReportDate(LocalDateTime bookingReportDate) {
		this.bookingReportDate = bookingReportDate;
	}

	public Integer getSendingAgencyId() {
		return sendingAgencyId;
	}

	public void setSendingAgencyId(Integer sendingAgencyId) {
		this.sendingAgencyId = sendingAgencyId;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

	public LocalDateTime getSupervisionReleaseDate() {
		return supervisionReleaseDate;
	}

	public void setSupervisionReleaseDate(LocalDateTime supervisionReleaseDate) {
		this.supervisionReleaseDate = supervisionReleaseDate;
	}

	public String getBookingReportId() {
		return bookingReportId;
	}

	public void setBookingReportId(String bookingReportId) {
		this.bookingReportId = bookingReportId;
	}

	public Integer getCaseStatusId() {
		return caseStatusId;
	}

	public void setCaseStatusId(Integer caseStatusId) {
		this.caseStatusId = caseStatusId;
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

	public Integer getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public BigDecimal getArrestLocationLatitude() {
		return arrestLocationLatitude;
	}

	public void setArrestLocationLatitude(BigDecimal arrestLocationLatitude) {
		this.arrestLocationLatitude = arrestLocationLatitude;
	}

	public BigDecimal getArrestLocationLongitude() {
		return arrestLocationLongitude;
	}

	public void setArrestLocationLongitude(BigDecimal arrestLocationLongitude) {
		this.arrestLocationLongitude = arrestLocationLongitude;
	}

	public Integer getBookingSubjectId() {
		return bookingSubjectId;
	}

	public void setBookingSubjectId(Integer bookingSubjectId) {
		this.bookingSubjectId = bookingSubjectId;
	}

	public LocalDate getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(LocalDate commitDate) {
		this.commitDate = commitDate;
	}
	
	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public BigDecimal getBondAmount() {
		return bondAmount;
	}

	public void setBondAmount(BigDecimal bondAmount) {
		this.bondAmount = bondAmount;
	}

	public KeyValue getBondType() {
		return bondType;
	}

	public void setBondType(KeyValue bondType) {
		this.bondType = bondType;
	}


}
