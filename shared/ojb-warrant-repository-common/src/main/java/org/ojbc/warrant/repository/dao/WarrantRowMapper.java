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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.warrant.repository.model.Warrant;
import org.springframework.jdbc.core.RowMapper;

public class WarrantRowMapper implements RowMapper<Warrant> {

	private static final Log log = LogFactory.getLog(WarrantRowMapper.class);
	
	@Override
	public Warrant mapRow(ResultSet rs, int rowNum) throws SQLException {
		Warrant warrant = new Warrant();

		warrant.setBondAmount(rs.getString("BondAmount"));
		warrant.setBroadcastArea(rs.getString("BroadcastArea"));
		warrant.setCourtAgencyORI(rs.getString("CourtAgencyORI"));
		warrant.setCourtDocketNumber(rs.getString("CourtDocketNumber"));
		warrant.setCriminalTrackingNumber(rs.getString("CriminalTrackingNumber"));
		
		Date dateOfExpiration = rs.getDate("DateOfExpiration");
		
		if (dateOfExpiration != null)
		{
			warrant.setDateOfExpiration(dateOfExpiration.toLocalDate());	
		}	

		Date dateOfWarrant = rs.getDate("DateOfWarrant");
		
		if (dateOfWarrant != null)
		{
			warrant.setDateOfWarrantRequest(dateOfWarrant.toLocalDate());	
		}	

		warrant.setExtradite(rs.getBoolean("Extradite"));
		
		warrant.setExtraditionLimits(rs.getString("ExtraditionLimits"));
		warrant.setGeneralOffenseCharacter(rs.getString("GeneralOffenseCharacter"));
		warrant.setLawEnforcementORI(rs.getString("LawEnforcementORI"));
		warrant.setOcaComplaintNumber(rs.getString("OCAComplaintNumber"));
		warrant.setOffenseCode(rs.getString("OffenseCode"));
		warrant.setOperator(rs.getString("Operator"));
		warrant.setOriginalOffenseCode(rs.getString("OriginalOffenseCode"));
		warrant.setPaccCode(rs.getString("PACCCode"));
		warrant.setPickupLimits(rs.getString("PickupLimits"));
		warrant.setWarrantEntryType(rs.getString("WarrantEntryType"));
		
    	return warrant;
	}
}
