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

import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.jdbc.core.RowMapper;

public class IdentificationQueryResponseRowMapper implements RowMapper<IdentificationQueryResponse> {
	public IdentificationQueryResponse mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		IdentificationQueryResponse identificationQueryResponse = new IdentificationQueryResponse();
		
		identificationQueryResponse.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
		
		identificationQueryResponse.setFbiId(rs.getString("FBI_ID"));
		identificationQueryResponse.setSid(rs.getString("SID"));
		identificationQueryResponse.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("timestamp")));
		identificationQueryResponse.setIdDate(EnhancedAuditUtils.toLocalDate(rs.getDate("ID_DATE")));
		
		identificationQueryResponse.setPersonFirstName(rs.getString("PERSON_FIRST_NAME"));
		identificationQueryResponse.setPersonMiddleName(rs.getString("PERSON_MIDDLE_NAME"));
		identificationQueryResponse.setPersonLastName(rs.getString("PERSON_LAST_NAME"));
		
		identificationQueryResponse.setOca(rs.getString("OCA"));
		identificationQueryResponse.setOri(rs.getString("ORI"));
		identificationQueryResponse.setOtn(rs.getString("OTN"));
		
		return identificationQueryResponse;
	}
	
}