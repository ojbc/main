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
package org.ojbc.adapters.rapbackdatastore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.FbiRapbackSubscription;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.adapters.rapbackdatastore.dao.model.SubsequentResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("rapbackDAO")
public class RapbackDAOImpl implements RapbackDAO {
	
	private final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public Iterable<Object> getRapbackReports(String federationId,
			String employerOri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	final static String SUBJECT_INSERT="INSERT into FBI_RAP_BACK_SUBJECT "
			+ "(UCN, CRIMINAL_SID, CIVIL_SID, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL, DOB, SEX_CODE) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveSubject(final Subject subject) {
        log.debug("Inserting row into FBI_RAP_BACK_SUBJECT table : " + subject);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBJECT_INSERT, new String[] {"UCN","CRIMINAL_SID", "CIVIL_SID", "FIRST_NAME", "LAST_NAME", "MIDDLE_INITIAL", "DOB"});
        	            ps.setString(1, subject.getUcn());
        	            ps.setString(2, subject.getCriminalSid());
        	            ps.setString(3, subject.getCivilSid()); 
        	            ps.setString(4, subject.getFirstName());
        	            ps.setString(5, subject.getLastName());
        	            ps.setString(6, subject.getMiddleInitial());
        	            ps.setDate(7, toSqlDate(subject.getDob()));
        	            ps.setString(8, subject.getSexCode());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	final static String SUBJECT_SELECT="SELECT * FROM FBI_RAP_BACK_SUBJECT WHERE SUBJECT_ID = ?";
	@Override
	public Subject getSubject(Integer id) {
		List<Subject> subjects = jdbcTemplate.query(SUBJECT_SELECT, new SubjectRowMapper(), id);
		return DataAccessUtils.singleResult(subjects);
	} 
	
	private final class SubjectRowMapper implements RowMapper<Subject> {
		public Subject mapRow(ResultSet rs, int rowNum)
				throws SQLException {
		Subject subject = buildSubject(rs);
	    return subject;
		}

	}

	private DateTime toDateTime(Date date){
		return date == null? null : new DateTime(date); 
	}
	
	private java.sql.Date toSqlDate(DateTime date){
		return date == null? null : new java.sql.Date(date.getMillis()); 
	}

	final static String IDENTIFICATION_TRANSACTION_INSERT="INSERT into IDENTIFICATION_TRANSACTION "
			+ "(TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA) "
			+ "values (?, ?, ?, ?, ?)";
	@Override
	@Transactional
	public void saveIdentificationTransaction(
			IdentificationTransaction identificationTransaction) {
        log.debug("Inserting row into IDENTIFICATION_TRANSACTION table : " + identificationTransaction.toString());
        
        Integer subjectId  = null; 
        if ( identificationTransaction.getSubject() != null){
	        subjectId = saveSubject(identificationTransaction.getSubject());
	        identificationTransaction.getSubject().setSubjectId(subjectId);
        }
        
        jdbcTemplate.update(IDENTIFICATION_TRANSACTION_INSERT, 
        		identificationTransaction.getTransactionNumber(), 
        		subjectId, 
        		identificationTransaction.getOtn(),
        		identificationTransaction.getOwnerOri(),
        		identificationTransaction.getOwnerProgramOca()); 
	}

	final static String CIVIL_FBI_SUBSCRIPTION_RECORD_INSERT="INSERT into CIVIL_FBI_SUBSCRIPTION_RECORD "
			+ "(SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, CIVIL_INITIAL_RESULT_ID, LAST_MODIFIED_BY) "
			+ "values (?, ?, ?, ?)";
	@Override
	public Integer saveCivilFbiSubscriptionRecord(
			final CivilFbiSubscriptionRecord civilFbiSubscriptionRecord) {
        log.debug("Inserting row into CIVIL_FBI_SUBSCRIPTION_RECORD table : " + civilFbiSubscriptionRecord.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_FBI_SUBSCRIPTION_RECORD_INSERT, 
        	                		new String[] {"SUBSCRIPTION_ID", "FBI_SUBSCRIPTION_ID", "CIVIL_INITIAL_RESULT_ID", "LAST_MODIFIED_BY"});
        	            ps.setInt(1, civilFbiSubscriptionRecord.getSubscriptionId());
        	            ps.setString(2, civilFbiSubscriptionRecord.getFbiSubscriptionId());
        	            ps.setInt(3, civilFbiSubscriptionRecord.getCivilInitialResultId()); 
        	            ps.setString(4, civilFbiSubscriptionRecord.getLastModifiedBy());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CRIMINAL_FBI_SUBSCRIPTION_RECORD_INSERT="insert into CRIMINAL_FBI_SUBSCRIPTION_RECORD "
			+ "(SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, FBI_OCA) "
			+ "values (?, ?, ?)";
	@Override
	public Integer saveCriminalFbiSubscriptionRecord(
			final CriminalFbiSubscriptionRecord criminalFbiSubscriptionRecord) {
        log.debug("Inserting row into CRIMINAL_FBI_SUBSCRIPTION_RECORD table : " + criminalFbiSubscriptionRecord.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRIMINAL_FBI_SUBSCRIPTION_RECORD_INSERT, 
        	                		new String[] {"SUBSCRIPTION_ID", "FBI_SUBSCRIPTION_ID", "FBI_OCA"});
        	            ps.setInt(1, criminalFbiSubscriptionRecord.getSubscriptionId());
        	            ps.setString(2, criminalFbiSubscriptionRecord.getFbiSubscriptionId());
        	            ps.setString(3, criminalFbiSubscriptionRecord.getFbiOca());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CIVIL_FINGER_PRINTS_INSERT="insert into CIVIL_FINGER_PRINTS "
			+ "(TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE) "
			+ "values (?, ?, ?, ?)";
	@Override
	public Integer saveCivilFingerPrints(final CivilFingerPrints civilFingerPrints) {
        log.debug("Inserting row into CIVIL_FINGER_PRINTS table : " + civilFingerPrints.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_FINGER_PRINTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "FINGER_PRINTS_FILE", "TRANSACTION_TYPE", "FINGER_PRINTS_TYPE"});
        	            ps.setString(1, civilFingerPrints.getTransactionNumber());
        	            ps.setBlob(2, new SerialBlob(civilFingerPrints.getFingerPrintsFile()));
        	            ps.setString(3, civilFingerPrints.getTransactionType());
        	            ps.setString(4, civilFingerPrints.getFingerPrintsType());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CRIMINAL_FINGER_PRINTS_INSERT="insert into CRIMINAL_FINGER_PRINTS "
			+ "(TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE) "
			+ "values (?, ?, ?, ?)";
	@Override
	public Integer saveCriminalFingerPrints(
			final CriminalFingerPrints criminalFingerPrints) {
        log.debug("Inserting row into CRIMINAL_FINGER_PRINTS table : " + criminalFingerPrints.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRIMINAL_FINGER_PRINTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "FINGER_PRINTS_FILE", "TRANSACTION_TYPE", "FINGER_PRINTS_TYPE"});
        	            ps.setString(1, criminalFingerPrints.getTransactionNumber());
        	            ps.setBlob(2, new SerialBlob(criminalFingerPrints.getFingerPrintsFile()));
        	            ps.setString(3, criminalFingerPrints.getTransactionType());
        	            ps.setString(4, criminalFingerPrints.getFingerPrintsType());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CIVIL_INITIAL_RAP_SHEET_INSERT="insert into CIVIL_INITIAL_RAP_SHEET "
			+ "(CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) "
			+ "values (?, ?, ?)";
	@Override
	public Integer saveCivilInitialRapSheet(
			final CivilInitialRapSheet civilInitialRapSheet) {
        log.debug("Inserting row into CIVIL_INITIAL_RAP_SHEET table : " + civilInitialRapSheet.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_INITIAL_RAP_SHEET_INSERT, 
        	                		new String[] {"CIVIL_INITIAL_RESULT_ID", "RAP_SHEET", "TRANSACTION_TYPE"});
        	            ps.setInt(1, civilInitialRapSheet.getCivilIntitialResultId());
        	            ps.setBlob(2, new SerialBlob(civilInitialRapSheet.getRapSheet()));
        	            ps.setString(3, civilInitialRapSheet.getTransactionType());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CIVIL_INITIAL_RESULTS_INSERT="insert into CIVIL_INITIAL_RESULTS "
			+ "(TRANSACTION_NUMBER, SUBJECT_ID, MATCH_NO_MATCH, CURRENT_STATE, TRANSACTION_TYPE, "
			+ " CIVIL_RAP_BACK_CATEGORY, RESULTS_SENDER) "
			+ "values (?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveCivilInitialResults(
			final CivilInitialResults civilInitialResults) {
        log.debug("Inserting row into CIVIL_INITIAL_RESULTS table : " + civilInitialResults.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_INITIAL_RESULTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "SUBJECT_ID", "MATCH_NO_MATCH", "CURRENT_STATE", 
        	                		"TRANSACTION_TYPE", "CIVIL_RAP_BACK_CATEGORY", "RESULTS_SENDER", });
        	            ps.setString(1, civilInitialResults.getTransactionNumber());
        	            ps.setInt(2, civilInitialResults.getSubject().getSubjectId());
        	            ps.setBoolean(3, civilInitialResults.getMatch());
        	            ps.setString(4, civilInitialResults.getCurrentState());
        	            ps.setString(5, civilInitialResults.getTransactionType());
        	            ps.setString(6, civilInitialResults.getCivilRapBackCategory());
        	            ps.setString(7, civilInitialResults.getResultsSender());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CRIMINAL_INITIAL_RESULTS_INSERT="insert into CRIMINAL_INITIAL_RESULTS "
			+ "(TRANSACTION_NUMBER, SUBJECT_ID, MATCH_NO_MATCH, TRANSACTION_TYPE, "
			+ " RESULTS_SENDER) "
			+ "values (?, ?, ?, ?, ?)";
	@Override
	public Integer saveCriminalInitialResults(
			final CriminalInitialResults criminalInitialResults) {
        log.debug("Inserting row into CRIMINAL_INITIAL_RESULTS table : " + criminalInitialResults.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRIMINAL_INITIAL_RESULTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "SUBJECT_ID", "MATCH_NO_MATCH",  
        	                		"TRANSACTION_TYPE", "RESULTS_SENDER", });
        	            ps.setString(1, criminalInitialResults.getTransactionNumber());
        	            ps.setInt(2, criminalInitialResults.getSubject().getSubjectId());
        	            ps.setBoolean(3, criminalInitialResults.getMatch());
        	            ps.setString(4, criminalInitialResults.getTransactionType());
        	            ps.setString(5, criminalInitialResults.getResultsSender());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String SUBSEQUENT_RESULTS_INSERT="insert into SUBSEQUENT_RESULTS "
			+ "(TRANSACTION_NUMBER, FBI_SUBSCRIPTION_ID, SUBJECT_ID, RAP_BACK_SUBSCRIPTION_IDENTIFIER, "
			+ " MATCH_NO_MATCH, RAPSHEET, TRANSACTION_TYPE ) "
			+ "values (?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveSubsequentResults(final SubsequentResults subsequentResults) {
        log.debug("Inserting row into SUBSEQUENT_RESULTS table : " + subsequentResults.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSEQUENT_RESULTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "FBI_SUBSCRIPTION_ID", "SUBJECT_ID", "RAP_BACK_SUBSCRIPTION_IDENTIFIER", 
        	                		"MATCH_NO_MATCH",  "RAP_SHEET", "TRANSACTION_TYPE"  });
        	            ps.setString(1, subsequentResults.getTransactionNumber());
        	            ps.setString(2, subsequentResults.getFbiSubscriptionId());
        	            ps.setInt(3, subsequentResults.getSubject().getSubjectId());
        	            ps.setString(4, subsequentResults.getRapbackSubscriptionIdentifier());
        	            ps.setBoolean(5, subsequentResults.getMatch());
        	            ps.setBlob(6, new SerialBlob(subsequentResults.getRapSheet()));
        	            ps.setString(7, subsequentResults.getTransactionType());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String FBI_RAP_BACK_SUBSCRIPTION_INSERT="insert into FBI_RAP_BACK_SUBSCRIPTION "
			+ "(FBI_SUBSCRIPTION_ID, SUBJECT_ID, RAP_BACK_CATEGORY, SUBSCRIPTION_TERM, "
			+ " RAP_BACK_SUBSCRIPTION_IDENTIFIER, RAP_BACK_EXPIRATION_DATE, RAP_BACK_START_DATE, "
			+ " RAP_BACK_OPT_OUT_IN_STATE_INDICATOR, RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT ) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveFbiRapbackSubscription(
			final FbiRapbackSubscription fbiRapbackSubscription) {
        log.debug("Inserting row into FBI_RAP_BACK_SUBSCRIPTION table : " + fbiRapbackSubscription.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FBI_RAP_BACK_SUBSCRIPTION_INSERT, 
        	                		new String[] {"FBI_SUBSCRIPTION_ID", "SUBJECT_ID", "RAP_BACK_CATEGORY", 
        	                		"SUBSCRIPTION_TERM",  "RAP_BACK_SUBSCRIPTION_IDENTIFIER", "RAP_BACK_EXPIRATION_DATE",
        	                		"RAP_BACK_START_DATE", "RAP_BACK_OPT_OUT_IN_STATE_INDICATOR", 
        	                		"RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT"});
        	            ps.setString(1, fbiRapbackSubscription.getFbiSubscriptionId());
        	            ps.setInt(2, fbiRapbackSubscription.getSubjectId());
        	            ps.setString(3, fbiRapbackSubscription.getRapbackCategory());
        	            ps.setString(4, fbiRapbackSubscription.getSubscriptionTerm());
        	            ps.setString(5, fbiRapbackSubscription.getRapbackSubscriptionIdentifier());
        	            ps.setDate(6, new java.sql.Date(fbiRapbackSubscription.getRapbackExpirationDate().getMillis()));
        	            ps.setDate(7, new java.sql.Date(fbiRapbackSubscription.getRapbackStartDate().getMillis()));
        	            ps.setBoolean(8, fbiRapbackSubscription.getRapbackOptOutInState());
        	            ps.setString(9, fbiRapbackSubscription.getRapbackActivityNotificationFormat());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	final static String ID_TRANSACTION_SELECT_BY_TRANSACTION_NUMBER = 
			" SELECT * FROM identification_transaction i "
			+ "LEFT JOIN fbi_rap_back_subject s ON s.subject_id = i.subject_id "
			+ "WHERE transaction_number = ?";
	
	@Override
	public IdentificationTransaction getIdentificationTransaction(
			String transactionNumber) {
		List<IdentificationTransaction> transactions = 
				jdbcTemplate.query(ID_TRANSACTION_SELECT_BY_TRANSACTION_NUMBER, 
						new IdentificationTransactionRowMapper(), transactionNumber);
		return DataAccessUtils.singleResult(transactions);
	}

	private final class IdentificationTransactionRowMapper 
		implements RowMapper<IdentificationTransaction> {
		public IdentificationTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			IdentificationTransaction identificationTransaction = new IdentificationTransaction();
			identificationTransaction.setTransactionNumber( rs.getString("transaction_number") );
			identificationTransaction.setOtn(rs.getString("otn"));
			identificationTransaction.setTimestamp(toDateTime(rs.getTimestamp("timestamp_received")));
			identificationTransaction.setOwnerOri(rs.getString("owner_ori"));
			identificationTransaction.setOwnerProgramOca(rs.getString("owner_program_oca"));

			Integer subjectId = rs.getInt("subject_id");
			
			if (subjectId != null){
				Subject subject = buildSubject(rs);
				identificationTransaction.setSubject(subject);
			}
			
			return identificationTransaction;
		}
	}
	
	private Subject buildSubject(ResultSet rs) throws SQLException {
		Subject subject = new Subject();

		subject.setSubjectId(rs.getInt("subject_id"));
		subject.setUcn(rs.getString("ucn"));
		subject.setCriminalSid(rs.getString("CRIMINAL_SID"));
		subject.setCivilSid(rs.getString("CIVIL_SID"));
		subject.setFirstName(rs.getString("FIRST_NAME")); 
		subject.setLastName(rs.getString("LAST_NAME")); 
		subject.setMiddleInitial(rs.getString("MIDDLE_INITIAL")); 
		subject.setDob(toDateTime(rs.getDate("DOB")));
		subject.setSexCode(rs.getString("SEX_CODE"));
		return subject;
	}
	
}