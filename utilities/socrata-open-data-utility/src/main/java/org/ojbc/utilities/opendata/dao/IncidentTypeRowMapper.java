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
package org.ojbc.utilities.opendata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.utilities.opendata.dao.model.IncidentType;
import org.springframework.jdbc.core.RowMapper;

public class IncidentTypeRowMapper implements RowMapper<IncidentType> {


	@Override
	public IncidentType mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		IncidentType incidentType = new IncidentType();
		
		incidentType.setIncidentCategoryDescription(rs.getString("incidentCategoryDescription"));
		incidentType.setIncidentTypeDescription(rs.getString("incidentTypeDescription"));
		
    	return incidentType;
	}

}
