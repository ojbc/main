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
package org.ojbc.warrant.repository.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.warrant.repository.model.PersonVehicle;
import org.springframework.jdbc.core.RowMapper;

public class PersonVehicleMapper implements RowMapper<PersonVehicle> {

	@Override
	public PersonVehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		PersonVehicle personVehicle = new PersonVehicle();
		
		personVehicle.setPersonID(rs.getInt("PersonID"));
		personVehicle.setLicensePlateType(rs.getString("LicensePlateType"));
		personVehicle.setVehicleIdentificationNumber(rs.getString("VehicleIdentificationNumber"));
		personVehicle.setVehicleLicensePlateExpirationDate(rs.getString("VehicleLicensePlateExpirationD"));
		personVehicle.setVehicleLicenseStateCode(rs.getString("VehicleLicenseStateCode"));
		personVehicle.setVehicleMake(rs.getString("VehicleMake"));
		personVehicle.setVehicleModel(rs.getString("VehicleModel"));
		personVehicle.setVehicleNonExpiringIndicator(rs.getBoolean("VehicleNonExpiringIndicator"));
		personVehicle.setVehiclePrimaryColor(rs.getString("VehiclePrimaryColor"));
		personVehicle.setVehicleSecondaryColor(rs.getString("VehicleSecondaryColor"));
		personVehicle.setVehicleStyle(rs.getString("VehicleStyle"));
		personVehicle.setVehicleYear(rs.getString("VehicleYear"));
		personVehicle.setVehicleLicensePlateNumber(rs.getString("VehicleLicensePlateNumber"));
		personVehicle.setPersonVehicleID(rs.getInt("VehicleID"));
		
    	return personVehicle;
	}
}
