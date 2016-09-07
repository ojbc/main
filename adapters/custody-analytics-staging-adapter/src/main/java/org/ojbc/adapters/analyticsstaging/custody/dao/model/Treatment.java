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
	private LocalDate treatmentStartDate; 
	private Integer treatmentAdmissionReasonTypeId; 
	private Integer treatmentStatusTypeId; 
	private String TreatmentProviderName; 

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

	public Integer getTreatmentAdmissionReasonTypeId() {
		return treatmentAdmissionReasonTypeId;
	}

	public void setTreatmentAdmissionReasonTypeId(
			Integer treatmentAdmissionReasonTypeId) {
		this.treatmentAdmissionReasonTypeId = treatmentAdmissionReasonTypeId;
	}

	public Integer getTreatmentStatusTypeId() {
		return treatmentStatusTypeId;
	}

	public void setTreatmentStatusTypeId(Integer treatmentStatusTypeId) {
		this.treatmentStatusTypeId = treatmentStatusTypeId;
	}

	public LocalDate getTreatmentStartDate() {
		return treatmentStartDate;
	}

	public void setTreatmentStartDate(LocalDate treatmentStartDate) {
		this.treatmentStartDate = treatmentStartDate;
	}

	public String getTreatmentProviderName() {
		return TreatmentProviderName;
	}

	public void setTreatmentProviderName(String treatmentProviderName) {
		TreatmentProviderName = treatmentProviderName;
	}

}
