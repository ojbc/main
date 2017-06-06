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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.warrant.repository.model.ChargeReferral;
import org.ojbc.warrant.repository.model.ChargeReferralReport;
import org.ojbc.warrant.repository.model.Person;
import org.springframework.jdbc.core.RowMapper;

public class ChargeReferralMapper implements RowMapper<ChargeReferralReport> {

	private static final Log log = LogFactory.getLog(ChargeReferralMapper.class);
	
	@Override
	public ChargeReferralReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChargeReferralReport chargeReferralReport = new ChargeReferralReport();

		//Only mapping the minimum number of fields required for the implementation
		//A more extensive mapping could be performed if additional fields are needed
		ChargeReferral chargeReferral = new ChargeReferral();
		chargeReferral.setChargeReferralID(rs.getInt("ChargeReferralID"));
		chargeReferralReport.setChargeReferral(chargeReferral);

		Person person = new Person();
		person.setPersonID(rs.getInt("PersonID"));
		chargeReferralReport.setPerson(person);
		
		try {
			chargeReferralReport.setWarrantId(rs.getInt("WarrantID"));
		} catch (Exception e) {
			log.error("Unable to map warrant ID.");
		}
		
    	return chargeReferralReport;
	}
}
