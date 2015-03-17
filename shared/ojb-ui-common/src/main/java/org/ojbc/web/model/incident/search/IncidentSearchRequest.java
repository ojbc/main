package org.ojbc.web.model.incident.search;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class IncidentSearchRequest implements Serializable{
    private static final long serialVersionUID = -2709378989656940290L;
    private String incidentNumber;
	private String incidentCityTown;
	private String incidentType;
	private DateTime incidentDateRangeStart;
	private DateTime incidentDateRangeEnd;
	
	//Logging
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystems;
	
	public String getIncidentNumber() {
		return incidentNumber;
	}
	
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	public String getIncidentCityTown() {
		return incidentCityTown;
	}
	
	public void setIncidentCityTown(String incidentCityTown) {
		this.incidentCityTown = incidentCityTown;
	}
	
	public String getIncidentType() {
		return incidentType;
	}
	
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	
	public DateTime getIncidentDateRangeStart() {
		return incidentDateRangeStart;
	}
	
	public void setIncidentDateRangeStart(DateTime incidentDateRangeStart) {
		this.incidentDateRangeStart = incidentDateRangeStart;
	}
	
	public DateTime getIncidentDateRangeEnd() {
		return incidentDateRangeEnd;
	}
	
	public void setIncidentDateRangeEnd(DateTime incidentDateRangeEnd) {
		this.incidentDateRangeEnd = incidentDateRangeEnd;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public List<String> getSourceSystems() {
		return sourceSystems;
	}

	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	@Override
	public String toString() {
		return "IncidentSearchRequest [incidentNumber=" + incidentNumber
				+ ", incidentCityTown=" + incidentCityTown + ", incidentType="
				+ ", incidentDateRangeStart=" + incidentDateRangeStart
				+ ", incidentDateRangeEnd=" + incidentDateRangeEnd + ", sourceSystems=" + sourceSystems + "]";
	}
	
	
}
