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

import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.jdbc.core.RowMapper;

public class WildlifeQueryResponseRowMapper implements RowMapper<WildlifeQueryResponse> {
	public WildlifeQueryResponse mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		WildlifeQueryResponse wildlifeQueryResponse = new WildlifeQueryResponse();
		
		wildlifeQueryResponse.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
		
		wildlifeQueryResponse.setMessageId(rs.getString("MESSAGE_ID"));
		wildlifeQueryResponse.setQueryResultsErrorIndicator(rs.getBoolean("QUERY_RESULTS_ERROR_INDICATOR"));
		wildlifeQueryResponse.setQueryResultsErrorText(rs.getString("QUERY_RESULTS_ERROR_TEXT"));
		wildlifeQueryResponse.setQueryResultsTimeoutIndicator(rs.getBoolean("QUERY_RESULTS_TIMEOUT_INDICATOR"));
		wildlifeQueryResponse.setQueryResultsTimeoutIndicator(rs.getBoolean("QUERY_RESULTS_ACCESS_DENIED"));
		wildlifeQueryResponse.setSystemName(rs.getString("SYSTEM_NAME"));
		wildlifeQueryResponse.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
		
		wildlifeQueryResponse.setResidenceCity(rs.getString("RESIDENCE_CITY"));
		
		return wildlifeQueryResponse;
	}
	
}