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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticaldatastore.booking.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.adapters.analyticaldatastore.booking.dao.model.Disposition;
import org.springframework.jdbc.core.RowMapper;

public class DispositionRowMapper implements  RowMapper<Disposition> {

	@Override
	public Disposition mapRow(ResultSet rs, int rowNum) throws SQLException {
		Disposition disposition = new Disposition();
    	
		disposition.setArrestingAgencyORI(rs.getString("ArrestingAgencyORI"));
		disposition.setIncidentCaseNumber(rs.getString("IncidentCaseNumber"));
		disposition.setDispositionDate(rs.getDate("DispositionDate"));
		disposition.setRecidivismEligibilityDate(rs.getDate("RecidivismEligibilityDate"));
		disposition.setSentenceTermDays(rs.getBigDecimal("SentenceTermDays"));
		disposition.setSentenceFineAmount(rs.getFloat("SentenceFineAmount"));
		disposition.setDocketChargeNumber(rs.getString("DocketChargeNumber"));
		
		if (rs.getString("IsProbationViolation") != null)
		{	
			disposition.setIsProbationViolation(rs.getString("IsProbationViolation").charAt(0));
		}		
		
		if (rs.getString("IsProbationViolationOnOldCharge") != null)
		{	
			disposition.setIsProbationViolationOnOldCharge(rs.getString("IsProbationViolationOnOldCharge").charAt(0));
		}
			
		disposition.setDispositionTypeID(rs.getInt("DispositionTypeID"));
		disposition.setPersonID(rs.getInt("PersonID"));
		
		disposition.setInitialChargeCode(rs.getString("InitialChargeCode"));
		disposition.setFinalChargeCode(rs.getString("FinalChargeCode"));
		
		disposition.setInitialChargeRank(rs.getString("InitialChargeCode1"));
		disposition.setFinalChargeRank(rs.getString("FinalChargeCode1"));
		
		disposition.setDispositionID(rs.getInt("DispositionID"));
		
    	return disposition;
	}

}
