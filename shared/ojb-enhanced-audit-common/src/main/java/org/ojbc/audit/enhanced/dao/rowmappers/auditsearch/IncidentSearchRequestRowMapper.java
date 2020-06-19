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
package org.ojbc.audit.enhanced.dao.rowmappers.auditsearch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class IncidentSearchRequestRowMapper implements
		ResultSetExtractor<List<IncidentSearchRequest>> {

	@Override
	public List<IncidentSearchRequest> extractData(ResultSet rs)
			throws SQLException, DataAccessException {

		Map<Integer, IncidentSearchRequest> map = new LinkedHashMap<Integer, IncidentSearchRequest>();

		IncidentSearchRequest incidentSearchRequest = null;

		while (rs.next()) {
			Integer id = rs.getInt("INCIDENT_SEARCH_REQUEST_ID");
			String systemName = rs.getString("SYSTEM_NAME");
			
			incidentSearchRequest = map.get(id);

			if (incidentSearchRequest == null) {
				incidentSearchRequest = new IncidentSearchRequest();

				incidentSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));

				incidentSearchRequest.setIncidentNumber(rs.getString("INCIDENT_NUMBER"));
				incidentSearchRequest.setCityTown(rs.getString("CITY_TOWN"));
				incidentSearchRequest.setStartDate(EnhancedAuditUtils.toLocalDate(rs.getDate("INCIDENT_START_DATE")));
				incidentSearchRequest.setEndDate(EnhancedAuditUtils.toLocalDate(rs.getDate("INCIDENT_END_DATE")));
				incidentSearchRequest.setOnBehalfOf(rs.getString("ON_BEHALF_OF"));
				incidentSearchRequest.setPurpose(rs.getString("PURPOSE"));
				incidentSearchRequest.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
				incidentSearchRequest.setUserInfofk(rs.getInt("USER_INFO_ID"));
				
				List<String> sourceSystems = incidentSearchRequest
						.getSystemsToSearch();

				if (sourceSystems == null) {
					sourceSystems = new ArrayList<String>();
					sourceSystems.add(systemName);
					incidentSearchRequest.setSystemsToSearch(sourceSystems);
				} 
				
				map.put(id, incidentSearchRequest);

			} else {
				incidentSearchRequest.getSystemsToSearch().add(systemName);
			}

			incidentSearchRequest.setIncidentSearchRequestId(id);
		}

		return (List<IncidentSearchRequest>) new ArrayList<IncidentSearchRequest>(
				map.values());
	}

}
