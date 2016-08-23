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

public class PrescribedMedication implements Serializable{

	private static final long serialVersionUID = 5315892476825533947L;
	private Integer prescribedMedicationID; 
	private Integer behavioralHealthAssessmentID; 
	private String medicationDescription;
	private LocalDate medicationDispensingDate;
	private String medicationDoseMeasure; 
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getPrescribedMedicationID() {
		return prescribedMedicationID;
	}

	public void setPrescribedMedicationID(Integer prescribedMedicationID) {
		this.prescribedMedicationID = prescribedMedicationID;
	}

	public Integer getBehavioralHealthAssessmentID() {
		return behavioralHealthAssessmentID;
	}

	public void setBehavioralHealthAssessmentID(
			Integer behavioralHealthAssessmentID) {
		this.behavioralHealthAssessmentID = behavioralHealthAssessmentID;
	}

	public LocalDate getMedicationDispensingDate() {
		return medicationDispensingDate;
	}

	public void setMedicationDispensingDate(LocalDate medicationDispensingDate) {
		this.medicationDispensingDate = medicationDispensingDate;
	}

	public String getMedicationDoseMeasure() {
		return medicationDoseMeasure;
	}

	public void setMedicationDoseMeasure(String medicationDoseMeasure) {
		this.medicationDoseMeasure = medicationDoseMeasure;
	}

	public String getMedicationDescription() {
		return medicationDescription;
	}

	public void setMedicationDescription(String medicationDescription) {
		this.medicationDescription = medicationDescription;
	}

}
