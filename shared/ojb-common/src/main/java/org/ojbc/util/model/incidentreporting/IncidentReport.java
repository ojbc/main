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
package org.ojbc.util.model.incidentreporting;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IncidentReport {
	
	private boolean officerFlag = false;
	
	private int responsePersonCount;
	
	private int involvedPersonCount;
	
	private int enforcedOfficialCount;
	
	private String organizationName;
	
	private String ori;
	
	private String incidentNumber;
	
	private String county;
	
	private String township;
	
	private int arrestOffenseAssociationCount;
	
	private int incidentOffenseAssociationCount;
	
	private int incidentSubjectPersonAssociationCount;
	
	private int arrestSubjectAssociationCount;
	
	public boolean isOfficerFlag() {
		return officerFlag;
	}

	public void setOfficerFlag(boolean officerFlag) {
		this.officerFlag = officerFlag;
	}

	public int getResponsePersonCount() {
		return responsePersonCount;
	}

	public void setResponsePersonCount(int responsePersonCount) {
		this.responsePersonCount = responsePersonCount;
	}

	public int getInvolvedPersonCount() {
		return involvedPersonCount;
	}

	public void setInvolvedPersonCount(int involvedPersonCount) {
		this.involvedPersonCount = involvedPersonCount;
	}

	public int getEnforcedOfficialCount() {
		return enforcedOfficialCount;
	}

	public void setEnforcedOfficialCount(int enforcedOfficialCount) {
		this.enforcedOfficialCount = enforcedOfficialCount;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public int getArrestOffenseAssociationCount() {
		return arrestOffenseAssociationCount;
	}

	public void setArrestOffenseAssociationCount(int arrestOffenseAssociationCount) {
		this.arrestOffenseAssociationCount = arrestOffenseAssociationCount;
	}

	public int getIncidentOffenseAssociationCount() {
		return incidentOffenseAssociationCount;
	}

	public void setIncidentOffenseAssociationCount(int incidentOffenseAssociationCount) {
		this.incidentOffenseAssociationCount = incidentOffenseAssociationCount;
	}

	public int getIncidentSubjectPersonAssociationCount() {
		return incidentSubjectPersonAssociationCount;
	}

	public void setIncidentSubjectPersonAssociationCount(int incidentSubjectPersonAssociationCount) {
		this.incidentSubjectPersonAssociationCount = incidentSubjectPersonAssociationCount;
	}

	public int getArrestSubjectAssociationCount() {
		return arrestSubjectAssociationCount;
	}

	public void setArrestSubjectAssociationCount(int arrestSubjectAssociationCount) {
		this.arrestSubjectAssociationCount = arrestSubjectAssociationCount;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
