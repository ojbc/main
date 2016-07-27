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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Treatment implements Serializable{

	private static final long serialVersionUID = -7903847836969994396L;
	
	private Integer treatmentId;
	private Integer behavioralHealthAssessmentID;
	private LocalDate startDate; 
	private LocalDate endDate; 
	private Boolean treatmentCourtOrdered; 
	private Boolean treatmentActive; 
	private String treatmentText; 
	private String treatmentProvider; 

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(Integer treatmentId) {
		this.treatmentId = treatmentId;
	}

	public Integer getBehavioralHealthAssessmentID() {
		return behavioralHealthAssessmentID;
	}

	public void setBehavioralHealthAssessmentID(
			Integer behavioralHealthAssessmentID) {
		this.behavioralHealthAssessmentID = behavioralHealthAssessmentID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Boolean getTreatmentCourtOrdered() {
		return treatmentCourtOrdered;
	}

	public void setTreatmentCourtOrdered(Boolean treatmentCourtOrdered) {
		this.treatmentCourtOrdered = treatmentCourtOrdered;
	}

	public Boolean getTreatmentActive() {
		return treatmentActive;
	}

	public void setTreatmentActive(Boolean treatmentActive) {
		this.treatmentActive = treatmentActive;
	}

	public String getTreatmentText() {
		return treatmentText;
	}

	public void setTreatmentText(String treatmentText) {
		this.treatmentText = treatmentText;
	}

	public String getTreatmentProvider() {
		return treatmentProvider;
	}

	public void setTreatmentProvider(String treatmentProvider) {
		this.treatmentProvider = treatmentProvider;
	}

}
