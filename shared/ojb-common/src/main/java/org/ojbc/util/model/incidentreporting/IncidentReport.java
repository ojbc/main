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
