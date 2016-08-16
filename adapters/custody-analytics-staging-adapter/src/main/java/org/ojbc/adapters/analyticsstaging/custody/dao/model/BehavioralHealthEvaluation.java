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

public class BehavioralHealthEvaluation implements Serializable
{
	private static final long serialVersionUID = 6669243492957009481L;
	private Integer behavioralHealthEvaluationId;
	private Integer behavioralHealthDiagnosisTypeId;
    private Integer behavioralHealthAssessmentId; 

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getBehavioralHealthEvaluationId() {
		return behavioralHealthEvaluationId;
	}

	public void setBehavioralHealthEvaluationId(
			Integer behavioralHealthEvaluationId) {
		this.behavioralHealthEvaluationId = behavioralHealthEvaluationId;
	}

	public Integer getBehavioralHealthDiagnosisTypeId() {
		return behavioralHealthDiagnosisTypeId;
	}

	public void setBehavioralHealthDiagnosisTypeId(Integer behavioralHealthDiagnosisTypeId) {
		this.behavioralHealthDiagnosisTypeId = behavioralHealthDiagnosisTypeId;
	}

	public Integer getBehavioralHealthAssessmentId() {
		return behavioralHealthAssessmentId;
	}

	public void setBehavioralHealthAssessmentId(
			Integer behavioralHealthAssessmentId) {
		this.behavioralHealthAssessmentId = behavioralHealthAssessmentId;
	}


}