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
package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.adapters.analyticaldatastore.dao.model.Arrest;
import org.springframework.jdbc.core.RowMapper;

public class ArrestRowMapper implements RowMapper<Arrest>
{
	@Override
	public Arrest mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Arrest arrest = new Arrest();
    	
    	arrest.setArrestDate(rs.getDate("ArrestDate"));
    	arrest.setArrestTime(rs.getTime("ArrestTime"));
    	arrest.setArrestingAgencyName(rs.getString("ArrestingAgencyName"));
    	arrest.setIncidentID(rs.getInt("IncidentID"));
    	arrest.setPersonID(rs.getInt("PersonID"));
    	arrest.setArrestID(rs.getInt("ArrestID"));
    	arrest.setReportingSystem(rs.getString("ReportingSystem"));
    	
    	return arrest;
	}

	
}
