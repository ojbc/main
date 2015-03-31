package org.ojbc.incidentReporting.dao.model;

public class PersonInvolvement {

	private String incidentId;
	private String personInvolvementHash;
	private String incidentOriginatingSystemUri;
	
	public String getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}
	public String getPersonInvolvementHash() {
		return personInvolvementHash;
	}
	public void setPersonInvolvementHash(String personInvolvementHash) {
		this.personInvolvementHash = personInvolvementHash;
	}
	public String getIncidentOriginatingSystemUri() {
		return incidentOriginatingSystemUri;
	}
	public void setIncidentOriginatingSystemUri(String incidentOriginatingSystemUri) {
		this.incidentOriginatingSystemUri = incidentOriginatingSystemUri;
	}
	
}
