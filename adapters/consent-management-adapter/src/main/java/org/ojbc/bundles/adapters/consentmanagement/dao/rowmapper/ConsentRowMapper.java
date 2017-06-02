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
package org.ojbc.bundles.adapters.consentmanagement.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.helper.DaoUtils;
import org.springframework.jdbc.core.RowMapper;

public class ConsentRowMapper implements RowMapper<Consent> {

	private static final Log log = LogFactory.getLog(ConsentRowMapper.class);
	
	@Override
	public Consent mapRow(ResultSet rs, int rowNum) throws SQLException {
		Consent consent = new Consent();

		consent.setConsentId(rs.getInt("ConsentDecisionID"));
		consent.setConsentDecisionTypeID(rs.getInt("ConsentDecisionTypeID"));
		consent.setPersonGender(rs.getString("PersonGender"));
		consent.setPersonFirstName(rs.getString("PersonFirstName"));
		consent.setPersonMiddleName(rs.getString("PersonMiddleName"));
		consent.setPersonLastName(rs.getString("PersonLastName"));
		consent.setBookingNumber(rs.getString("BookingNumber"));
		consent.setConsentDocumentControlNumber(rs.getString("ConsentDocumentControlNumber"));
		consent.setConsenterUserID(rs.getString("ConsenterUserID"));
		consent.setConsentUserFirstName(rs.getString("ConsenterUserFirstName"));
		consent.setConsentUserLastName(rs.getString("ConsenterUserLastName"));
		consent.setNameNumber(rs.getString("NameNumber"));
		consent.setPersonDOB(DaoUtils.getLocalDate(rs, "PersonDOB"));
		consent.setRecordCreationTimestamp(DaoUtils.getLocalDateTime(rs, "RecordCreationTimestamp"));
		consent.setConsentDecisionTimestamp(DaoUtils.getLocalDateTime(rs, "ConsentDecisionTimestamp"));
		
    	return consent;
	}
}