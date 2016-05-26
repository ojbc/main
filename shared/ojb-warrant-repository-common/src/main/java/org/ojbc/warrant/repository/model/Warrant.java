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
package org.ojbc.warrant.repository.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Warrant {

    private Integer warrantID;
    private LocalDate dateOfWarrantRequest;
    private LocalDate dateOfExpiration;
    private String broadcastArea;
    private String warrantEntryType;
    private String courtAgencyORI;
    private String lawEnforcementORI;
    private String courtDocketNumber;
    private String ocaComplaintNumber;
    private String operator;
    private String paccCode;
    private String originalOffenseCode;
    private String offenseCode;
    private String generalOffenseCharacter;
    private String criminalTrackingNumber;
    private boolean extradite;
    private String extraditionLimits;
    private String bondAmount;
    private String warrantStatus;
    private String pickupLimits;
    private LocalDateTime warrantStatusTimestamp;
    private List<WarrantRemarks> warrantRemarks;
    
	public Integer getWarrantID() {
		return warrantID;
	}
	public void setWarrantID(Integer warrantID) {
		this.warrantID = warrantID;
	}
	public LocalDate getDateOfWarrantRequest() {
		return dateOfWarrantRequest;
	}
	public void setDateOfWarrantRequest(LocalDate dateOfWarrantRequest) {
		this.dateOfWarrantRequest = dateOfWarrantRequest;
	}
	public LocalDate getDateOfExpiration() {
		return dateOfExpiration;
	}
	public void setDateOfExpiration(LocalDate dateOfExpiration) {
		this.dateOfExpiration = dateOfExpiration;
	}
	public String getWarrantEntryType() {
		return warrantEntryType;
	}
	public void setWarrantEntryType(String warrantEntryType) {
		this.warrantEntryType = warrantEntryType;
	}
	public String getCourtAgencyORI() {
		return courtAgencyORI;
	}
	public void setCourtAgencyORI(
			String courtAgencyORI) {
		this.courtAgencyORI = courtAgencyORI;
	}
	public String getLawEnforcementORI() {
		return lawEnforcementORI;
	}
	public void setLawEnforcementORI(String lawEnforcementORI) {
		this.lawEnforcementORI = lawEnforcementORI;
	}
	public String getCourtDocketNumber() {
		return courtDocketNumber;
	}
	public void setCourtDocketNumber(String courtDocketNumber) {
		this.courtDocketNumber = courtDocketNumber;
	}
	public String getOcaComplaintNumber() {
		return ocaComplaintNumber;
	}
	public void setOcaComplaintNumber(
			String ocaComplaintNumber) {
		this.ocaComplaintNumber = ocaComplaintNumber;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPaccCode() {
		return paccCode;
	}
	public void setPaccCode(String paccCode) {
		this.paccCode = paccCode;
	}
	public String getOriginalOffenseCode() {
		return originalOffenseCode;
	}
	public void setOriginalOffenseCode(String originalOffenseCode) {
		this.originalOffenseCode = originalOffenseCode;
	}
	public String getOffenseCode() {
		return offenseCode;
	}
	public void setOffenseCode(String offenseCode) {
		this.offenseCode = offenseCode;
	}
	public String getGeneralOffenseCharacter() {
		return generalOffenseCharacter;
	}
	public void setGeneralOffenseCharacter(String generalOffenseCharacter) {
		this.generalOffenseCharacter = generalOffenseCharacter;
	}
	public String getCriminalTrackingNumber() {
		return criminalTrackingNumber;
	}
	public void setCriminalTrackingNumber(String criminalTrackingNumber) {
		this.criminalTrackingNumber = criminalTrackingNumber;
	}
	public String getExtraditionLimits() {
		return extraditionLimits;
	}
	public void setExtraditionLimits(String extraditionLimits) {
		this.extraditionLimits = extraditionLimits;
	}
	public String getBondAmount() {
		return bondAmount;
	}
	public void setBondAmount(String bondAmount) {
		this.bondAmount = bondAmount;
	}
	public String getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(String warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public LocalDateTime getWarrantStatusTimestamp() {
		return warrantStatusTimestamp;
	}
	public void setWarrantStatusTimestamp(LocalDateTime warrantStatusTimestamp) {
		this.warrantStatusTimestamp = warrantStatusTimestamp;
	}
	public boolean isExtradite() {
		return extradite;
	}
	public void setExtradite(boolean extradite) {
		this.extradite = extradite;
	}
	public String getPickupLimits() {
		return pickupLimits;
	}
	public void setPickupLimits(String pickupLimits) {
		this.pickupLimits = pickupLimits;
	}
	public String getBroadcastArea() {
		return broadcastArea;
	}
	public void setBroadcastArea(String broadcastArea) {
		this.broadcastArea = broadcastArea;
	}
	public List<WarrantRemarks> getWarrantRemarks() {
		return warrantRemarks;
	}
	public void setWarrantRemarks(List<WarrantRemarks> warrantRemarks) {
		this.warrantRemarks = warrantRemarks;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
