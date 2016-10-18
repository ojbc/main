package org.ojbc.utilities.opendata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.utilities.opendata.dao.model.IncidentArrest;

public class DaoUtils {

	public static IncidentArrest processResultSet(ResultSet rs) throws SQLException
	{
		IncidentArrest incidentArrest = new IncidentArrest();
    	
		incidentArrest.setIncidentID(rs.getInt("IncidentID"));
		incidentArrest.setIncidentCaseNumber(rs.getString("incidentCaseNumber"));
		incidentArrest.setReportingAgency(rs.getString("reportingIncidentAgency"));
		incidentArrest.setIncidentDateTime(rs.getString("IncidentDateTime"));
		incidentArrest.setIncidentTown(rs.getString("incidentTown"));
		incidentArrest.setIncidentCountyName(rs.getString("incidentCountyName"));
		incidentArrest.setArrestCountyName(rs.getString("arrestCountyName"));
		incidentArrest.setAgeInYears(rs.getString("ageInYears"));
		incidentArrest.setPersonRaceDescription(rs.getString("personRaceDescription"));
		incidentArrest.setArrestDrugInvolved(rs.getString("arrestDrugInolved"));
		incidentArrest.setArresteeSexDescription(rs.getString("arresteeSexDescription"));
		incidentArrest.setArrestingAgency(rs.getString("arrestingAgency"));
		incidentArrest.setIncidentLoadTimeStamp(rs.getString("incidentLoadtimestamp"));
		incidentArrest.setIncidentLocationAddress(rs.getString("incidentLocationStreetAddress"));
		incidentArrest.setArrestId(rs.getString("arrestID"));

		if ((incidentArrest.getIncidentID() != null) && (incidentArrest.getArrestId() != null))
		{
			incidentArrest.setRowIdentifier(incidentArrest.getIncidentID().toString() + "_" + incidentArrest.getArrestId());
		}	
		else
		{
			incidentArrest.setRowIdentifier(incidentArrest.getIncidentID().toString());
		}	
		
		return incidentArrest;
	}
	
	
}
