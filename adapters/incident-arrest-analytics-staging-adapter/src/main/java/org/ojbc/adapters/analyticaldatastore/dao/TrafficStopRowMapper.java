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
package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.adapters.analyticaldatastore.dao.model.TrafficStop;
import org.springframework.jdbc.core.RowMapper;

public class TrafficStopRowMapper implements  RowMapper<TrafficStop> {

	@Override
	public TrafficStop mapRow(ResultSet rs, int rowNum) throws SQLException {
		TrafficStop trafficStop = new TrafficStop();
    	
		trafficStop.setDriverAge(rs.getInt("DriverAge"));
		trafficStop.setDriverRace(rs.getString("DriverRace"));
		trafficStop.setDriverResidenceState(rs.getString("DriverResidenceState"));
		trafficStop.setDriverResidenceTown(rs.getString("DriverResidenceTown"));
		trafficStop.setDriverSex(rs.getString("DriverSex"));
		trafficStop.setIncidentID(rs.getInt("IncidentID"));
		trafficStop.setTrafficStopContrabandStatus(rs.getString("TrafficStopContrabandStatus"));
		trafficStop.setTrafficStopOutcomeDescription(rs.getString("TrafficStopOutcomeDescription"));
		trafficStop.setTrafficStopReasonDescription(rs.getString("TrafficStopReasonDescription"));
		trafficStop.setTrafficStopSearchTypeDescription(rs.getString("TrafficStopSearchTypeDescription"));
		trafficStop.setVehicleMake(rs.getString("VehicleMake"));
		trafficStop.setVehicleModel(rs.getString("VehicleModel"));
		trafficStop.setVehicleRegistrationState(rs.getString("VehicleRegistrationState"));
		trafficStop.setVehicleYear(rs.getInt("vehicleYear"));
		
    	return trafficStop;
	}

}
