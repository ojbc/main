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

import org.ojbc.audit.enhanced.dao.model.IncidentSearchResult;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.jdbc.core.RowMapper;

public class IncidentSearchResultRowMapper implements RowMapper<IncidentSearchResult> {
	public IncidentSearchResult mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		IncidentSearchResult incidentSearchResult = new IncidentSearchResult();
			
		incidentSearchResult.setIncidentSearchRequestId(rs.getInt("incident_search_request_id"));
		incidentSearchResult.setSearchResultsCount(rs.getInt("search_results_count"));
		incidentSearchResult.setSystemName(rs.getString("SYSTEM_NAME"));
		incidentSearchResult.setSystemURI(rs.getString("system_uri"));
		incidentSearchResult.setSearchResultsAccessDeniedIndicator(rs.getBoolean("search_results_access_denied_indicator"));
		incidentSearchResult.setSearchResultsTimeoutIndicator(rs.getBoolean("search_results_timeout_indicator"));
		incidentSearchResult.setSearchResultsErrorIndicator(rs.getBoolean("search_results_error_indicator"));
		incidentSearchResult.setSearchResultsErrorText(rs.getString("search_results_error_text"));
		incidentSearchResult.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("TIMESTAMP")));

		return incidentSearchResult;
	}
	
}