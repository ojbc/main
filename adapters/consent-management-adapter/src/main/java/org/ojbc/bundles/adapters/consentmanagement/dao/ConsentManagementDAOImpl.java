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
package org.ojbc.bundles.adapters.consentmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.adapters.consentmanagement.dao.rowmapper.ConsentRowMapper;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.helper.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ConsentManagementDAOImpl implements ConsentManagementDAO {
	
	private static final Log log = LogFactory.getLog(ConsentManagementDAOImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Consent> returnConsentRecordsFromLast24hours() {
        LocalDateTime now = LocalDateTime.now();
        
        LocalDateTime date24hoursAgo = now.minusHours(24);
        
        log.info("Returning records with no inmate interview, from 24 hours ago: " + date24hoursAgo.toString());        
        
		String sql = "SELECT * from consent_decision where RecordCreationTimestamp < ? and ConsentDecisionTypeID is null";
		
		Date dateCompare = java.sql.Timestamp.valueOf( date24hoursAgo );
		
		List<Consent> consentRecords = 
				jdbcTemplate.query(sql, new Object[] {dateCompare}, new ConsentRowMapper());
		
		return consentRecords;
	}

	@Override
	public Integer saveConsentDecision(Consent consent) {
		log.debug("Inserting row into consent table: " + consent.toString());

		if (consent.getRecordCreationTimestamp() == null)
		{
			consent.setRecordCreationTimestamp(LocalDateTime.now());
		}	
		
		final String consentInsertStatement = "INSERT into consent_decision "
				+ "(BookingNumber, NameNumber, PersonDOB, PersonFirstName, PersonGender, PersonLastName, PersonMiddleName, RecordCreationTimestamp, ConsentDecisionTypeID)"
				+ "values (?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(
						consentInsertStatement, 
						new String[] {"ConsentID" });
				ps.setString(1, consent.getBookingNumber());
				ps.setString(2, consent.getNameNumber());
				DaoUtils.setPreparedStatementVariable(consent.getPersonDOB(), ps, 3);
				ps.setString(4, consent.getPersonFirstName());
				ps.setString(5, consent.getPersonGender());
				ps.setString(6, consent.getPersonLastName());
				ps.setString(7, consent.getPersonMiddleName());
				DaoUtils.setPreparedStatementVariable(consent.getRecordCreationTimestamp(), ps, 8);
				DaoUtils.setPreparedStatementVariable(consent.getConsentDecisionTypeID(), ps, 9);
				return ps;
			}
		}, keyHolder);

        return keyHolder.getKey().intValue();
		
	}

	@Override
	public void updateConsentDecision(Integer consentDecisionID, Integer consentDecisionTypeID, String consenterUserID, String consenterUserFirstName, String consenterUserLastName, String consentDocumentControlNumber, LocalDateTime consentDecisionTimestamp) {
		String sql = "UPDATE consent_decision set ConsentDecisionTypeID = ?, ConsenterUserID = ?, ConsentDocumentControlNumber=?, ConsentDecisionTimestamp =?, ConsenterUserFirstName=?, ConsenterUserLastName=? where consentDecisionID =?";
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(
						sql, 
						new String[] {"ConsentID" });
				ps.setInt(1, consentDecisionTypeID);
				ps.setString(2, consenterUserID);
				ps.setString(3, consentDocumentControlNumber);
				DaoUtils.setPreparedStatementVariable(consentDecisionTimestamp, ps, 4);
				ps.setString(5, consenterUserFirstName);
				ps.setString(6, consenterUserLastName);
				ps.setInt(7, consentDecisionID);
				
				return ps;
			}
		});		
	}

	@Override
	public Integer retrieveConsentDecisionType(String consentDecision) throws Exception {
		String sql = "SELECT ConsentDecisionTypeID from consent_decision_type where ConsentDecisionDescription =?";
		
		Integer consentDecisionTypeID = jdbcTemplate.queryForObject(sql, new Object[] {consentDecision}, Integer.class);

		if (consentDecisionTypeID == null)
		{
			throw new Exception("Unable to retrieve consent decision type ID from description: "  + consentDecision);
		}	
		
		return consentDecisionTypeID;
	}
	
	@Override
	public Consent returnConsentRecordfromId(Integer consentDecisionID) {
        
		String sql = "SELECT * from consent_decision where consentDecisionID =?";
		
		Consent consentRecord = 
				jdbcTemplate.queryForObject(sql, new Object[] {consentDecisionID}, new ConsentRowMapper());
		
		return consentRecord;
	}

	@Override
	public void deleteAllConsentRecords() {
		String sql = "delete from consent_decision";
		
		jdbcTemplate.update(sql);
		
	}

	@Override
	public void updateConsentRecordsWithNoInterview() throws Exception {
		
		List<Consent> recordsNotUpdated = this.returnConsentRecordsFromLast24hours();
		
		Integer consentDecisionTypeID = this.retrieveConsentDecisionType(ConsentManagementConstants.INMATE_NOT_INTERVIEWED);
		
		for (Consent record : recordsNotUpdated)
		{
			log.info("Person not interviewed in 24 hours.  Updating record: "  + record.getConsentId());
			
			this.updateConsentDecision(record.getConsentId(), consentDecisionTypeID, null, null, null, null, LocalDateTime.now());
		}	
		
	}

	@Override
	public List<Consent> searchForConsentRecords() {
        
        log.info("Returning consent records without consent decisions");        
        
		String sql = "SELECT * from consent_decision where ConsentDecisionTypeID is null";
		
		List<Consent> consentRecords = 
				jdbcTemplate.query(sql, new ConsentRowMapper());
		
		return consentRecords;
	}

}
