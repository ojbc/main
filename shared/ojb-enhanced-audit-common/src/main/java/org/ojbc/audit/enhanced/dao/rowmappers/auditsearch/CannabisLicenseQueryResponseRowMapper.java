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

import org.ojbc.audit.enhanced.dao.model.CannabisLicensingQueryResponse;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.jdbc.core.RowMapper;

public class CannabisLicenseQueryResponseRowMapper implements RowMapper<CannabisLicensingQueryResponse> {
	public CannabisLicensingQueryResponse mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		CannabisLicensingQueryResponse cannabisLicensingQueryResponse = new CannabisLicensingQueryResponse();
		
		cannabisLicensingQueryResponse.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
		
		cannabisLicensingQueryResponse.setMessageId(rs.getString("MESSAGE_ID"));
		cannabisLicensingQueryResponse.setQueryResultsErrorIndicator(rs.getBoolean("QUERY_RESULTS_ERROR_INDICATOR"));
		cannabisLicensingQueryResponse.setQueryResultsErrorText(rs.getString("QUERY_RESULTS_ERROR_TEXT"));
		cannabisLicensingQueryResponse.setQueryResultsTimeoutIndicator(rs.getBoolean("QUERY_RESULTS_TIMEOUT_INDICATOR"));
		cannabisLicensingQueryResponse.setQueryResultsTimeoutIndicator(rs.getBoolean("QUERY_RESULTS_ACCESS_DENIED"));
		cannabisLicensingQueryResponse.setSystemName(rs.getString("SYSTEM_NAME"));
		cannabisLicensingQueryResponse.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
		
		cannabisLicensingQueryResponse.setLicenseNumber(rs.getString("LICENSE_NUMBER"));
		cannabisLicensingQueryResponse.setExpirationDate(EnhancedAuditUtils.toLocalDate(rs.getDate("EXPIRATION_DATE")));
		
		return cannabisLicensingQueryResponse;
	}
	
}