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

import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class FirearmSearchRequestRowMapper implements
		ResultSetExtractor<List<FirearmsSearchRequest>> {

	@Override
	public List<FirearmsSearchRequest> extractData(ResultSet rs)
			throws SQLException, DataAccessException {

		Map<Integer, FirearmsSearchRequest> map = new LinkedHashMap<Integer, FirearmsSearchRequest>();

		FirearmsSearchRequest firearmsSearchRequest = null;

		while (rs.next()) {
			Integer id = rs.getInt("FIREARMS_SEARCH_REQUEST_ID");
			String systemName = rs.getString("SYSTEM_NAME");
			
			firearmsSearchRequest = map.get(id);

			if (firearmsSearchRequest == null) {
				firearmsSearchRequest = new FirearmsSearchRequest();

				firearmsSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));

				firearmsSearchRequest.setCurrentRegistrationsOnly(rs.getBoolean("CURRENT_REGISTRATIONS_ONLY"));
				firearmsSearchRequest.setFirearmsType(rs.getString("FIREARMS_TYPE"));
				firearmsSearchRequest.setMake(rs.getString("MAKE"));
				firearmsSearchRequest.setModel(rs.getString("MODEL"));
				firearmsSearchRequest.setOnBehalfOf(rs.getString("ON_BEHALF_OF"));
				firearmsSearchRequest.setPurpose(rs.getString("PURPOSE"));
				firearmsSearchRequest.setRegistrationNumber(rs.getString("REGISTRATION_NUMBER"));
				firearmsSearchRequest.setSerialNumber(rs.getString("SERIAL_NUMBER"));
				firearmsSearchRequest.setSerialNumberQualifierCodeId(rs.getInt("SERIAL_NUMBER_QUALIFIER_CODE_ID"));
				firearmsSearchRequest.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
				firearmsSearchRequest.setFirearmsType(rs.getString("FIREARMS_TYPE"));
				firearmsSearchRequest.setUserInfofk(rs.getInt("USER_INFO_ID"));
				
				List<String> sourceSystems = firearmsSearchRequest
						.getSystemsToSearch();

				if (sourceSystems == null) {
					sourceSystems = new ArrayList<String>();
					sourceSystems.add(systemName);
					firearmsSearchRequest.setSystemsToSearch(sourceSystems);
				} 
				
				map.put(id, firearmsSearchRequest);

			} else {
				firearmsSearchRequest.getSystemsToSearch().add(systemName);
			}

			firearmsSearchRequest.setFirearmSearchRequestId(id);
		}

		return (List<FirearmsSearchRequest>) new ArrayList<FirearmsSearchRequest>(
				map.values());
	}

}
