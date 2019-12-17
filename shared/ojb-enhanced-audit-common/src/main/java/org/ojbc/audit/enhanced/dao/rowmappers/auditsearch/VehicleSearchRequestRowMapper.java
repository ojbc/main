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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class VehicleSearchRequestRowMapper implements
	ResultSetExtractor<List<VehicleSearchRequest>> {
	
	@Override
	public List<VehicleSearchRequest> extractData(ResultSet rs)
			throws SQLException, DataAccessException {
		
		Map<Integer, VehicleSearchRequest> map = new LinkedHashMap<Integer, VehicleSearchRequest>();
		
		VehicleSearchRequest vehicleSearchRequest = null;

		while (rs.next()) {
			Integer id = rs.getInt("VEHICLE_SEARCH_REQUEST_ID");
			String systemName = rs.getString("SYSTEM_NAME");
			
			vehicleSearchRequest = map.get(id);
			
			if (vehicleSearchRequest == null) {
				vehicleSearchRequest = new VehicleSearchRequest();
				
				vehicleSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
				vehicleSearchRequest.setOnBehalfOf(rs.getString("ON_BEHALF_OF"));
				vehicleSearchRequest.setPurpose(rs.getString("PURPOSE"));
				vehicleSearchRequest.setUserInfofk(rs.getInt("USER_INFO_ID"));
				vehicleSearchRequest.setVehicleColor(rs.getString("COLOR"));
				vehicleSearchRequest.setVehicleIdentificationNumber(rs.getString("VIN"));
				vehicleSearchRequest.setVehicleMake(rs.getString("MAKE"));
				vehicleSearchRequest.setVehicleModel(rs.getString("MODEL"));
				vehicleSearchRequest.setVehicleLicensePlate(rs.getString("PLATE_NUMBER"));
				vehicleSearchRequest.setVehicleYearRangeStart(rs.getString("YEAR_RANGE_START"));
				vehicleSearchRequest.setVehicleYearRangeStart(rs.getString("YEAR_RANGE_END"));

				vehicleSearchRequest.setTimestamp(toLocalDateTime(rs.getTimestamp("TIMESTAMP")));
				
				List<String> sourceSystems = vehicleSearchRequest.getSourceSystemsList();
				
				
				if (sourceSystems == null)
				{
					sourceSystems = new ArrayList<String>();
					sourceSystems.add(systemName);
					vehicleSearchRequest.setSourceSystemsList(sourceSystems);
				}	
				
				map.put(id, vehicleSearchRequest);
				
			}
			else
			{
				vehicleSearchRequest.getSourceSystemsList().add(systemName);
				
			}
			
			vehicleSearchRequest.setVehicleSearchRequestID(id);
		}
		
		
		return (List<VehicleSearchRequest>) new ArrayList<VehicleSearchRequest>(
				map.values());

	}
	
	private LocalDateTime toLocalDateTime(Timestamp timestamp){
		return timestamp == null? null : timestamp.toLocalDateTime();
	}
}