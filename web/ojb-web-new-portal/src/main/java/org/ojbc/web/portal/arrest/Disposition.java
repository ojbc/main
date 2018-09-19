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
package org.ojbc.web.portal.arrest;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

public class Disposition {
	private String arrestIdentification;
	private String arrestChargeIdentification;
	private String dispositionIdentification;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	@NotNull
	private LocalDate dispositionDate; 
	
	@NotBlank
	private String dispositionCode;
    private String courtCaseNumber;
    
    @NotBlank
    private String filedCharge;
    private String amendedCharge;
    
    private Integer fineAmount;
    private Integer fineSuspended;
    
    @Max(1)
    private Integer jailYears;
    @Max(359)
    private Integer jailDays;
    
    @Max(1)
    private Integer suspendedYears;
    @Max(359)
    private Integer suspendedDays;
    
    @Max(1)
    private Integer deferredYears;
    @Max(359)
    private Integer deferredDays;
    
    private Integer restitution; 
    private String alternateSentence; 
    private String reasonForDismissal;
    
	public String getArrestIdentification() {
		return arrestIdentification;
	}
	public void setArrestIdentification(String arrestIdentification) {
		this.arrestIdentification = arrestIdentification;
	}
	public String getArrestChargeIdentification() {
		return arrestChargeIdentification;
	}
	public void setArrestChargeIdentification(String arrestChargeIdentification) {
		this.arrestChargeIdentification = arrestChargeIdentification;
	}
	public String getDispositionIdentification() {
		return dispositionIdentification;
	}
	public void setDispositionIdentification(String dispositionIdentification) {
		this.dispositionIdentification = dispositionIdentification;
	}
	public String getDispositionCode() {
		return dispositionCode;
	}
	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	public String getCourtCaseNumber() {
		return courtCaseNumber;
	}
	public void setCourtCaseNumber(String courtCaseNumber) {
		this.courtCaseNumber = courtCaseNumber;
	}
	public String getFiledCharge() {
		return filedCharge;
	}
	public void setFiledCharge(String filedCharge) {
		this.filedCharge = filedCharge;
	}
	public String getAmendedCharge() {
		return amendedCharge;
	}
	public void setAmendedCharge(String amendedCharge) {
		this.amendedCharge = amendedCharge;
	}
	public Integer getFineAmount() {
		return fineAmount;
	}
	public void setFineAmount(Integer fineAmount) {
		this.fineAmount = fineAmount;
	}
	public Integer getFineSuspended() {
		return fineSuspended;
	}
	public void setFineSuspended(Integer fineSuspended) {
		this.fineSuspended = fineSuspended;
	}
	public Integer getJailYears() {
		return jailYears;
	}
	public void setJailYears(Integer jailYears) {
		this.jailYears = jailYears;
	}
	public Integer getJailDays() {
		return jailDays;
	}
	public void setJailDays(Integer jailDays) {
		this.jailDays = jailDays;
	}
	public Integer getSuspendedYears() {
		return suspendedYears;
	}
	public void setSuspendedYears(Integer suspendedYears) {
		this.suspendedYears = suspendedYears;
	}
	public Integer getSuspendedDays() {
		return suspendedDays;
	}
	public void setSuspendedDays(Integer suspendedDays) {
		this.suspendedDays = suspendedDays;
	}
	public Integer getDeferredYears() {
		return deferredYears;
	}
	public void setDeferredYears(Integer deferredYears) {
		this.deferredYears = deferredYears;
	}
	public Integer getDeferredDays() {
		return deferredDays;
	}
	public void setDeferredDays(Integer deferredDays) {
		this.deferredDays = deferredDays;
	}
	public Integer getRestitution() {
		return restitution;
	}
	public void setRestitution(Integer restitution) {
		this.restitution = restitution;
	}
	public String getAlternateSentence() {
		return alternateSentence;
	}
	public void setAlternateSentence(String alternateSentence) {
		this.alternateSentence = alternateSentence;
	}
	public String getReasonForDismissal() {
		return reasonForDismissal;
	}
	public void setReasonForDismissal(String reasonForDismissal) {
		this.reasonForDismissal = reasonForDismissal;
	}
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public LocalDate getDispositionDate() {
		return dispositionDate;
	}
	public void setDispositionDate(LocalDate dispositionDate) {
		this.dispositionDate = dispositionDate;
	}
}    