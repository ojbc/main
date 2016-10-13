package org.ojbc.utilities.opendata.dao.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IncidentArrest {

	@JsonProperty("row_identifier")
	private String rowIdentifier;
	
	@JsonIgnore
	private Integer incidentID;
	
	@JsonProperty("incident_case_number")
	private String incidentCaseNumber;
	
	@JsonProperty("reporting_agency")
	private String reportingAgency;
	
	@JsonProperty("incident_date_time")
	private String incidentDateTime;
	
	@JsonProperty("incident_location_town")
	private String incidentTown;
	
	@JsonProperty("incident_location_county")
	private String incidentCountyName;
	
	@JsonProperty("arrest_county")
	private String arrestCountyName;
	
	@JsonProperty("age_in_years")
	private String ageInYears;
	
	@JsonProperty("person_race_description")
	private String personRaceDescription;
	
	@JsonProperty("arrest_drug_involved")
	private String arrestDrugInvolved;
				   
	@JsonProperty("arrest_drug_involved_description")
	private String arrestDrugInvolvedDescription;

	@JsonProperty("arrestee_sex_description")
	private String arresteeSexDescription;
	
	@JsonProperty("arresting_agency")
	private String arrestingAgency;
	
	@JsonIgnore
	private String arrestId;

	@JsonProperty("incident_load_timestamp")
	private String incidentLoadTimeStamp;
	
	@JsonIgnore
	private String incidentLocationAddress;
		
	@JsonIgnore
	private List<Charge> charges;
	
	@JsonIgnore
	private List<IncidentType> incidentTypes;
	
	@JsonProperty("arrest_charges")
	private String chargesDelimited;
	
	@JsonProperty("incident_type_description")
	private String incidentTypeDelimited;
	
	@JsonProperty("incident_category_description")
	private String incidentCategoryDescriptionDelimited;

	public String getIncidentCaseNumber() {
		return incidentCaseNumber;
	}
	public void setIncidentCaseNumber(String incidentCaseNumber) {
		this.incidentCaseNumber = incidentCaseNumber;
	}
	public String getReportingAgency() {
		return reportingAgency;
	}
	public void setReportingAgency(String reportingAgency) {
		this.reportingAgency = reportingAgency;
	}
	public String getIncidentDateTime() {
		return incidentDateTime;
	}
	public void setIncidentDateTime(String incidentDateTime) {
		this.incidentDateTime = incidentDateTime;
	}
	public String getIncidentTown() {
		return incidentTown;
	}
	public void setIncidentTown(String incidentTown) {
		this.incidentTown = incidentTown;
	}
	public String getIncidentCountyName() {
		return incidentCountyName;
	}
	public void setIncidentCountyName(String incidentCountyName) {
		this.incidentCountyName = incidentCountyName;
	}
	public String getArrestCountyName() {
		return arrestCountyName;
	}
	public void setArrestCountyName(String arrestCountyName) {
		this.arrestCountyName = arrestCountyName;
	}
	public String getAgeInYears() {
		return ageInYears;
	}
	public void setAgeInYears(String ageInYears) {
		this.ageInYears = ageInYears;
	}
	public String getPersonRaceDescription() {
		return personRaceDescription;
	}
	public void setPersonRaceDescription(String personRaceDescription) {
		this.personRaceDescription = personRaceDescription;
	}
	public String getArrestDrugInvolved() {
		return arrestDrugInvolved;
	}
	public void setArrestDrugInvolved(String arrestDrugInvolved) {
		this.arrestDrugInvolved = arrestDrugInvolved;
	}
	public String getArresteeSexDescription() {
		return arresteeSexDescription;
	}
	public void setArresteeSexDescription(String arresteeSexDescription) {
		this.arresteeSexDescription = arresteeSexDescription;
	}
	public String getArrestingAgency() {
		return arrestingAgency;
	}
	public void setArrestingAgency(String arrestingAgency) {
		this.arrestingAgency = arrestingAgency;
	}

	public String getIncidentLoadTimeStamp() {
		return incidentLoadTimeStamp;
	}
	public void setIncidentLoadTimeStamp(String incidentLoadTimeStamp) {
		this.incidentLoadTimeStamp = incidentLoadTimeStamp;
	}
	
	@Override
	public String toString() {
		return "IncidentArrest [incidentCaseNumber=" + incidentCaseNumber
				+ ", reportingAgency=" + reportingAgency
				+ ", incidentDateTime=" + incidentDateTime + ", incidentTown="
				+ incidentTown + ", incidentCountyName=" + incidentCountyName
				+ ", arrestCountyName=" + arrestCountyName + ", ageInYears="
				+ ageInYears + ", personRaceDescription="
				+ personRaceDescription + ", arrestDrugInolved="
				+ arrestDrugInvolved + ", arresteeSexDescription="
				+ arresteeSexDescription + ", arrestingAgency="
				+ arrestingAgency + ", incidentLoadTimeStamp="
				+ incidentLoadTimeStamp + "]";
	}
	public String getIncidentLocationAddress() {
		return incidentLocationAddress;
	}
	public void setIncidentLocationAddress(String incidentLocationAddress) {
		this.incidentLocationAddress = incidentLocationAddress;
	}
	public String getArrestId() {
		return arrestId;
	}
	public void setArrestId(String arrestId) {
		this.arrestId = arrestId;
	}
	public List<Charge> getCharges() {
		return charges;
	}
	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}
	public List<IncidentType> getIncidentTypes() {
		return incidentTypes;
	}
	public void setIncidentTypes(List<IncidentType> incidentTypes) {
		this.incidentTypes = incidentTypes;
	}
	public Integer getIncidentID() {
		return incidentID;
	}
	public void setIncidentID(Integer incidentID) {
		this.incidentID = incidentID;
	}
	public String getChargesDelimited() {
		return chargesDelimited;
	}
	public void setChargesDelimited(String chargesDelimited) {
		this.chargesDelimited = chargesDelimited;
	}
	public String getIncidentTypeDelimited() {
		return incidentTypeDelimited;
	}
	public void setIncidentTypeDelimited(String incidentTypeDelimited) {
		this.incidentTypeDelimited = incidentTypeDelimited;
	}
	public String getIncidentCategoryDescriptionDelimited() {
		return incidentCategoryDescriptionDelimited;
	}
	public void setIncidentCategoryDescriptionDelimited(
			String incidentCategoryDescriptionDelimited) {
		this.incidentCategoryDescriptionDelimited = incidentCategoryDescriptionDelimited;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	public String getArrestDrugInvolvedDescription() {
		return arrestDrugInvolvedDescription;
	}
	public void setArrestDrugInvolvedDescription(
			String arrestDrugInvolvedDescription) {
		this.arrestDrugInvolvedDescription = arrestDrugInvolvedDescription;
	}
		
}
