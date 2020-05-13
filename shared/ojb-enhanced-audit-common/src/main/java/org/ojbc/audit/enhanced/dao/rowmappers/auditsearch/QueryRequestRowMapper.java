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

import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.springframework.jdbc.core.RowMapper;

public class QueryRequestRowMapper implements RowMapper<QueryRequest> {
	public QueryRequest mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		QueryRequest queryRequest = new QueryRequest();
		
		queryRequest.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
		queryRequest.setUserInfofk(rs.getInt("USER_INFO_ID"));
		queryRequest.setMessageId(rs.getString("MESSAGE_ID"));
		queryRequest.setIdentificationSourceText(rs.getString("SYSTEM_NAME"));
		queryRequest.setIdentificationId(rs.getString("IDENTIFICATION_ID"));
		
		//TODO: Include timestamp here
		
		return queryRequest;
	}
}