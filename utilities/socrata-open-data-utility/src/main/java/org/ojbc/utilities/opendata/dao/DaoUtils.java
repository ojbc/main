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
