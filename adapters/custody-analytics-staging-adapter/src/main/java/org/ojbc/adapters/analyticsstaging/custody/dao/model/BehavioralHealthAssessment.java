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
package org.ojbc.adapters.analyticsstaging.custody.dao.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BehavioralHealthAssessment implements Serializable{
	private static final long serialVersionUID = 3070572325599615722L;
	private Integer behavioralHealthAssessmentId;
    private Integer personId; 
    private Boolean seriousMentalIllness;
    private LocalDate careEpisodeStartDate;
    private LocalDate careEpisodeEndDate;
    private Integer MedicaidStatusTypeId;
    private String enrolledProviderName;
    
    private List<String> behavioralHealthDiagnoses; 
    private List<String> BehavioralHealthCategoryTexts; 
    private List<KeyValue> assessmentCategories = new ArrayList<KeyValue>(); 
    private List<PrescribedMedication> prescribedMedications; 
    private List<Treatment> treatments;
    
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getBehavioralHealthAssessmentId() {
		return behavioralHealthAssessmentId;
	}

	public void setBehavioralHealthAssessmentId(
			Integer behavioralHealthAssessmentId) {
		this.behavioralHealthAssessmentId = behavioralHealthAssessmentId;
	}

	public Boolean getSeriousMentalIllness() {
		return seriousMentalIllness;
	}

	public void setSeriousMentalIllness(Boolean seriousMentalIllness) {
		this.seriousMentalIllness = seriousMentalIllness;
	}

	public LocalDate getCareEpisodeStartDate() {
		return careEpisodeStartDate;
	}

	public void setCareEpisodeStartDate(LocalDate careEpisodeStartDate) {
		this.careEpisodeStartDate = careEpisodeStartDate;
	}

	public LocalDate getCareEpisodeEndDate() {
		return careEpisodeEndDate;
	}

	public void setCareEpisodeEndDate(LocalDate careEpisodeEndDate) {
		this.careEpisodeEndDate = careEpisodeEndDate;
	}

	public List<PrescribedMedication> getPrescribedMedications() {
		return prescribedMedications;
	}

	public void setPrescribedMedications(List<PrescribedMedication> prescribedMedications) {
		this.prescribedMedications = prescribedMedications;
	}

	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	public Integer getMedicaidStatusTypeId() {
		return MedicaidStatusTypeId;
	}

	public void setMedicaidStatusTypeId(Integer medicaidStatusTypeId) {
		MedicaidStatusTypeId = medicaidStatusTypeId;
	}

	public List<String> getBehavioralHealthDiagnoses() {
		return behavioralHealthDiagnoses;
	}

	public void setBehavioralHealthDiagnoses(
			List<String> behavioralHealthDiagnoses) {
		this.behavioralHealthDiagnoses = behavioralHealthDiagnoses;
	}

	public String getEnrolledProviderName() {
		return enrolledProviderName;
	}

	public void setEnrolledProviderName(String enrolledProviderName) {
		this.enrolledProviderName = enrolledProviderName;
	}

	public List<KeyValue> getAssessmentCategories() {
		return assessmentCategories;
	}

	public void setAssessmentCategories(List<KeyValue> assessmentCategories) {
		this.assessmentCategories = assessmentCategories;
	}

	public List<String> getBehavioralHealthCategoryTexts() {
		return BehavioralHealthCategoryTexts;
	}

	public void setBehavioralHealthCategoryTexts(
			List<String> behavioralHealthCategoryTexts) {
		BehavioralHealthCategoryTexts = behavioralHealthCategoryTexts;
	}

}