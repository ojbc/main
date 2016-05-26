package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantAcceptedReport {

	private String ocaComplaintNumber;
	private String lawEnforcementORI;
	private String warrantStatus;
	private String personEntryNumber;
	
	public String getOcaComplaintNumber() {
		return ocaComplaintNumber;
	}
	public void setOcaComplaintNumber(
			String ocaComplaintNumber) {
		this.ocaComplaintNumber = ocaComplaintNumber;
	}
	public String getLawEnforcementORI() {
		return lawEnforcementORI;
	}
	public void setLawEnforcementORI(String lawEnforcementORI) {
		this.lawEnforcementORI = lawEnforcementORI;
	}
	public String getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(String warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public String getPersonEntryNumber() {
		return personEntryNumber;
	}
	public void setPersonEntryNumber(String personEntryNumber) {
		this.personEntryNumber = personEntryNumber;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
