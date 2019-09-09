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
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

public class Disposition {
	private ArrestType dispositionType; 
	private String arrestIdentification;
	private String arrestChargeIdentification;
	private String dispositionIdentification;
	private String arrestOri;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	@NotNull
	@PastOrPresent 
	private LocalDate dispositionDate; 
	
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate arrestDate; 
	
	@NotBlank
	private String dispositionCode;
	@Min(1)
	@Max(999)
	@NotNull
	private Integer counts; 
	private String dispositionDescription;
    private String courtCaseNumber;
    
    @NotBlank
    private String filedCharge;
    private String filedChargeDescription; 
    private String amendedCharge;
    private String amendedChargeDescription; 
    private String chargeSeverityCode;
    private String amendedChargeSeverityCode;
    private String generalOffenseCode;
    
    @Min(1)
    @NumberFormat(pattern = "#,###,###,###,###")
    private Integer fineAmount;
    @Min(1)
    @NumberFormat(pattern = "#,###,###,###,###")
    private Integer fineSuspended;
    
    private Integer jailYears;
    @Max(359)
    private Integer jailDays;
    
    private Integer prisonYears;
    @Max(359)
    private Integer prisonDays;
    
    private Integer suspendedYears;
    @Max(359)
    private Integer suspendedDays;
    
    private Integer deferredYears;
    @Max(359)
    private Integer deferredDays;
    
    @Min(1)
    @NumberFormat(pattern = "#,###,###,###,###")
    private Integer restitution; 
    private List<String> alternateSentences; 
    private List<String> alternateSentenceDescriptions; 
    private String reasonForDismissal;
    private String reasonForDismissalDescripiton;
    private String provisionCode;
    
    private String county; 
    private String caseType; 
    private String year; 
    private String caseNumber; 

    public Disposition() {
    	super();
    	this.counts = 1; 
    }
	public Disposition(ArrestType dispositionType) {
		this();
		this.dispositionType = dispositionType;
	}
    public boolean containsSentenceInfo() {
    	return (Objects.nonNull(jailYears) && jailYears > 0) 
    		||  (Objects.nonNull(jailDays) && jailDays > 0) 
    		||  (Objects.nonNull(deferredDays) && deferredDays > 0) 
    		||  (Objects.nonNull(deferredYears) && deferredYears > 0) 
    		||  (Objects.nonNull(suspendedYears) && suspendedYears > 0) 
    		||  (Objects.nonNull(suspendedDays) && suspendedDays > 0) 
    		||  (Objects.nonNull(fineAmount) && fineAmount > 0) 
    		||  (Objects.nonNull(fineSuspended) && fineSuspended > 0) 
    		||  (alternateSentences != null && !alternateSentences.isEmpty())
    		|| (Objects.nonNull(restitution) && restitution > 0) 
    		|| (dispositionType == ArrestType.DA 
    			&& ((Objects.nonNull(prisonYears) && prisonYears > 0) 
					|| (Objects.nonNull(prisonDays) && prisonDays > 0))); 
    }
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
	public void assembleCourtCaseNumber() {
		boolean isCourtCaseNumberPartsReady = StringUtils.isNoneBlank(this.county, this.caseType, this.year, this.caseNumber);
		if (isCourtCaseNumberPartsReady) {
			this.courtCaseNumber = StringUtils.join(this.county, this.caseType, this.year, this.caseNumber);
		}
	}
	
	public void disassembleCourtCaseNumber() {
		if (StringUtils.isNotBlank(this.courtCaseNumber) && this.dispositionType == ArrestType.DA) {
			this.county = StringUtils.substring(this.courtCaseNumber, 0, 2);
			this.caseType = StringUtils.substring(this.courtCaseNumber, 2, 3);
			this.year = StringUtils.substring(this.courtCaseNumber, 3, 7);
			this.caseNumber = StringUtils.substring(this.courtCaseNumber, 7);
		}
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
	public String getDispositionDescription() {
		return dispositionDescription;
	}
	public void setDispositionDescription(String dispositionDescription) {
		this.dispositionDescription = dispositionDescription;
	}
	public String getFiledChargeDescription() {
		return filedChargeDescription;
	}
	public void setFiledChargeDescription(String filedChargeDescription) {
		this.filedChargeDescription = filedChargeDescription;
	}
	public String getAmendedChargeDescription() {
		return amendedChargeDescription;
	}
	public void setAmendedChargeDescription(String amendedChargeDescription) {
		this.amendedChargeDescription = amendedChargeDescription;
	}
	public String getReasonForDismissalDescripiton() {
		return reasonForDismissalDescripiton;
	}
	public void setReasonForDismissalDescripiton(String reasonForDismissalDescripiton) {
		this.reasonForDismissalDescripiton = reasonForDismissalDescripiton;
	}
	public String getProvisionCode() {
		return provisionCode;
	}
	public void setProvisionCode(String provisionCode) {
		this.provisionCode = provisionCode;
	}
	public String getChargeSeverityCode() {
		return chargeSeverityCode;
	}
	public void setChargeSeverityCode(String chargeSeverityCode) {
		this.chargeSeverityCode = chargeSeverityCode;
	}
	public ArrestType getDispositionType() {
		return dispositionType;
	}
	public void setDispositionType(ArrestType dispositionType) {
		this.dispositionType = dispositionType;
	}
	public LocalDate getArrestDate() {
		return arrestDate;
	}
	public void setArrestDate(LocalDate arrestDate) {
		this.arrestDate = arrestDate;
	}
	public Integer getPrisonYears() {
		return prisonYears;
	}
	public void setPrisonYears(Integer prisonYears) {
		this.prisonYears = prisonYears;
	}
	public Integer getPrisonDays() {
		return prisonDays;
	}
	public void setPrisonDays(Integer prisonDays) {
		this.prisonDays = prisonDays;
	}
	public String getAmendedChargeSeverityCode() {
		return amendedChargeSeverityCode;
	}
	public void setAmendedChargeSeverityCode(String amendedChargeSeverityCode) {
		this.amendedChargeSeverityCode = amendedChargeSeverityCode;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		if (StringUtils.isNotBlank(county)) {
			this.county = StringUtils.leftPad(county, 2, '0');
		}
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		if (StringUtils.isNotBlank(year)) {
			this.year = StringUtils.leftPad(year, 2, '0');
		}
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		if (StringUtils.isNotBlank(caseNumber)) {
			this.caseNumber = StringUtils.leftPad(caseNumber, 5, '0');
		}
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public String getGeneralOffenseCode() {
		return generalOffenseCode;
	}
	public void setGeneralOffenseCode(String generalOffenseCode) {
		this.generalOffenseCode = generalOffenseCode;
	}
	public List<String> getAlternateSentences() {
		return alternateSentences;
	}
	public void setAlternateSentences(List<String> alternateSentences) {
		if (alternateSentences == null || alternateSentences.isEmpty()) {
			this.alternateSentences = null;
		}
		else {
			this.alternateSentences = alternateSentences;
		}
	}
	public List<String> getAlternateSentenceDescriptions() {
		return alternateSentenceDescriptions;
	}
	public void setAlternateSentenceDescriptions(List<String> alternateSentenceDescriptions) {
		this.alternateSentenceDescriptions = alternateSentenceDescriptions;
	}
	public String getArrestOri() {
		return arrestOri;
	}
	public void setArrestOri(String arrestOri) {
		this.arrestOri = arrestOri;
		if (this.getDispositionType() == ArrestType.DA 
				&& StringUtils.isBlank(this.getCounty()) 
				&& StringUtils.isNotBlank(arrestOri)) {
			this.setCounty(StringUtils.substring(arrestOri, 2,4));
		}
	}
}    
