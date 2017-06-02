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
package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.springframework.jdbc.core.RowMapper;

public class IncidentRowMapper implements RowMapper<Incident>
{
	@Override
	public Incident mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Incident incident = new Incident();
    	
    	incident.setIncidentCaseNumber(rs.getString("IncidentCaseNumber"));
    	incident.setIncidentDate(rs.getDate("IncidentDate"));
    	incident.setIncidentID(rs.getInt("IncidentID"));
    	incident.setIncidentLocationStreetAddress(rs.getString("IncidentLocationStreetAddress"));
    	incident.setIncidentLocationTown(rs.getString("IncidentLocationTown"));
    	incident.setIncidentTime(rs.getTime("IncidentTime"));
    	incident.setReportingAgencyID(rs.getInt("ReportingAgencyID"));
    	incident.setIncidentLocationLatitude(rs.getBigDecimal("IncidentLocationLatitude"));
    	incident.setIncidentLocationLongitude(rs.getBigDecimal("IncidentLocationLongitude"));
    	incident.setReportingSystem(rs.getString("ReportingSystem"));
    	
    	String recordType = rs.getString("RecordType"); 
    	
    	if (StringUtils.isNotBlank(recordType)){
    		incident.setRecordType(recordType.charAt(0));
    	}
    	
    	return incident;
	}

}
