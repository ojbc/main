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

import org.ojbc.audit.enhanced.dao.model.CrashVehicle;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.util.EnhancedAuditUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class VehicleCrashQueryResponseRowMapper implements ResultSetExtractor<List<VehicleCrashQueryResponse>>  {
	
	@Override
	public List<VehicleCrashQueryResponse> extractData(ResultSet rs)
			throws SQLException, DataAccessException {
		
		Map<Integer, VehicleCrashQueryResponse> map = new LinkedHashMap<Integer, VehicleCrashQueryResponse>();
		
		VehicleCrashQueryResponse vehicleCrashQueryResponse = null;
	
		while (rs.next()) {
			Integer id = rs.getInt("VEHICLE_CRASH_QUERY_RESULTS_ID");
			
			vehicleCrashQueryResponse = map.get(id);

			if (vehicleCrashQueryResponse == null) {
				vehicleCrashQueryResponse = new VehicleCrashQueryResponse();
			
				//Set common info
				vehicleCrashQueryResponse.setQueryRequestId(rs.getInt("QUERY_REQUEST_ID"));
				vehicleCrashQueryResponse.setMessageId(rs.getString("MESSAGE_ID"));
				vehicleCrashQueryResponse.setQueryResultsErrorIndicator(rs.getBoolean("QUERY_RESULTS_ERROR_INDICATOR"));
				vehicleCrashQueryResponse.setQueryResultsErrorText(rs.getString("QUERY_RESULTS_ERROR_TEXT"));
				vehicleCrashQueryResponse.setQueryResultsTimeoutIndicator(rs.getBoolean("QUERY_RESULTS_TIMEOUT_INDICATOR"));
				vehicleCrashQueryResponse.setSystemName(rs.getString("SYSTEM_NAME"));
				vehicleCrashQueryResponse.setTimestamp(EnhancedAuditUtils.toLocalDateTime(rs.getTimestamp("timestamp")));
				
				//Set vehicle crash info
				List<CrashVehicle> crashVehicles = new ArrayList<CrashVehicle>();
				
				addCrashVehicle(rs, crashVehicles);
				
				vehicleCrashQueryResponse.setCrashVehicles(crashVehicles);
				
				map.put(id, vehicleCrashQueryResponse);

			} else {
				addCrashVehicle(rs, vehicleCrashQueryResponse.getCrashVehicles());
			}

			vehicleCrashQueryResponse.setVehicleCrashQueryResultsId(rs.getInt("VEHICLE_CRASH_QUERY_RESULTS_ID"));
		}

		return (List<VehicleCrashQueryResponse>) new ArrayList<VehicleCrashQueryResponse>(
				map.values());
		
	}

	private void addCrashVehicle(ResultSet rs, List<CrashVehicle> crashVehicles) throws SQLException {
		CrashVehicle crashVehicle = new CrashVehicle();
		crashVehicle.setVehicleIdentificationNumber(rs.getString("VEHICLE_IDENTIFICATION_NUMBER"));
		crashVehicle.setVehicleMake(rs.getString("VEHICLE_MAKE"));
		crashVehicle.setVehicleModel(rs.getString("VEHICLE_MODEL"));
		
		crashVehicles.add(crashVehicle);
	}

	
}