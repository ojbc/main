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

import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class PersonSearchRequestRowMapper implements
	ResultSetExtractor<List<PersonSearchRequest>> {
	
	@Override
	public List<PersonSearchRequest> extractData(ResultSet rs)
			throws SQLException, DataAccessException {
		
		Map<Integer, PersonSearchRequest> map = new LinkedHashMap<Integer, PersonSearchRequest>();
		
		PersonSearchRequest personSearchRequest = null;
	
		while (rs.next()) {
			Integer id = rs.getInt("PERSON_SEARCH_REQUEST_ID");
			String systemName = rs.getString("SYSTEM_NAME");
			
			personSearchRequest = map.get(id);

			if (personSearchRequest == null) {
				personSearchRequest = new PersonSearchRequest();
			
				personSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
				personSearchRequest.setPersonSearchRequestID(rs.getInt("PERSON_SEARCH_REQUEST_ID"));
				
				if (rs.getDate("DOB_START_DATE") != null)
				{	
					personSearchRequest.setDobFrom(rs.getDate("DOB_START_DATE").toLocalDate());
				}	
	
				if (rs.getDate("DOB_END_DATE") != null)
				{	
					personSearchRequest.setDobTo(rs.getDate("DOB_END_DATE").toLocalDate());
				}	
	
				personSearchRequest.setDriverLicenseId(rs.getString("DRIVERS_LICENSE_NUMBER"));
				personSearchRequest.setDriverLiscenseIssuer(rs.getString("DRIVERS_LICENSE_ISSUER"));
				
				personSearchRequest.setEyeCode(rs.getString("EYE_COLOR"));
				personSearchRequest.setFbiNumber(rs.getString("FBI_ID"));
				personSearchRequest.setFirstName(rs.getString("FIRST_NAME"));
				personSearchRequest.setGenderCode(rs.getString("GENDER"));
				personSearchRequest.setHairCode(rs.getString("HAIR_COLOR"));
				personSearchRequest.setHeight(rs.getInt("HEIGHT"));
				personSearchRequest.setHeightMin(rs.getInt("HEIGHT_MIN"));
				personSearchRequest.setHeightMax(rs.getInt("HEIGHT_MAX"));
				
				personSearchRequest.setLastName(rs.getString("LAST_NAME"));
				personSearchRequest.setMiddleName(rs.getString("MIDDLE_NAME"));
				personSearchRequest.setOnBehalfOf(rs.getString("ON_BEHALF_OF"));
				personSearchRequest.setPurpose(rs.getString("PURPOSE"));
				personSearchRequest.setRaceCode(rs.getString("RACE"));
				personSearchRequest.setSsn(rs.getString("SSN"));
				personSearchRequest.setStateId(rs.getString("SID"));
				personSearchRequest.setMessageId(rs.getString("MESSAGE_ID"));
				
				//TODO: figure out how to map these fields
				//personSearchRequest.setLastNameQualifierCode(rs.getString(""));
				//personSearchRequest.setFirstNameQualifierCode(rs.getString(""));
				
				List<String> sourceSystems = personSearchRequest
						.getSystemsToSearch();

				if (sourceSystems == null) {
					sourceSystems = new ArrayList<String>();
					sourceSystems.add(systemName);
					personSearchRequest.setSystemsToSearch(sourceSystems);
				} 
				
				map.put(id, personSearchRequest);

			} else {
				personSearchRequest.getSystemsToSearch().add(systemName);
			}

			personSearchRequest.setPersonSearchRequestID(id);
		}

		return (List<PersonSearchRequest>) new ArrayList<PersonSearchRequest>(
				map.values());
	}
}
