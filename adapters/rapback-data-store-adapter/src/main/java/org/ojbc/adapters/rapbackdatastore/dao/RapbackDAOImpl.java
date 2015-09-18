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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.ResultSender;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.adapters.rapbackdatastore.dao.model.SubsequentResults;
import org.ojbc.adapters.rapbackdatastore.util.ZipUtils;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.TopicMapValidationDueDateStrategy;
import org.ojbc.intermediaries.sn.fbi.rapback.FbiRapbackSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
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
    @Autowired
    private TopicMapValidationDueDateStrategy validationDueDateStrategy;
	
	final static String SUBJECT_INSERT="INSERT into IDENTIFICATION_SUBJECT "
			+ "(UCN, CRIMINAL_SID, CIVIL_SID, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL, DOB, SEX_CODE) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveSubject(final Subject subject) {
        log.debug("Inserting row into IDENTIFICATION_SUBJECT table : " + subject);

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
	
	final static String SUBJECT_SELECT="SELECT * FROM IDENTIFICATION_SUBJECT WHERE SUBJECT_ID = ?";
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
			+ "(TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, ARCHIVED, IDENTIFICATION_CATEGORY) "
			+ "values (?, ?, ?, ?, ?, ?, ?)";
	@Override
	@Transactional
	public void saveIdentificationTransaction(
			IdentificationTransaction identificationTransaction) {
        log.debug("Inserting row into IDENTIFICATION_TRANSACTION table : " + identificationTransaction.toString());
        
        Integer subjectId  = null; 
        if ( identificationTransaction.getSubject() == null){
        	throw new IllegalArgumentException("The subject should not be null when saving Identification Transaction :" + 
        			identificationTransaction.toString()); 
        }
        else{
        	subjectId = saveSubject(identificationTransaction.getSubject());
        	identificationTransaction.getSubject().setSubjectId(subjectId);
        	
        }
        
        jdbcTemplate.update(IDENTIFICATION_TRANSACTION_INSERT, 
        		identificationTransaction.getTransactionNumber(), 
        		subjectId, 
        		identificationTransaction.getOtn(),
        		identificationTransaction.getOwnerOri(),
        		identificationTransaction.getOwnerProgramOca(), 
        		BooleanUtils.isTrue(identificationTransaction.getArchived()),
        		identificationTransaction.getIdentificationCategory()); 
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
        	                		new String[] {"SUBSCRIPTION_ID", "FBI_SUBSCRIPTION_ID"});
        	            ps.setInt(1, criminalFbiSubscriptionRecord.getSubscriptionId());
        	            ps.setString(2, criminalFbiSubscriptionRecord.getFbiSubscriptionId());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CIVIL_FINGER_PRINTS_INSERT="insert into CIVIL_FINGER_PRINTS "
			+ "(TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) "
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
        	            
        	            if (civilFingerPrints.getFingerPrintsFile() != null){
        	            	ps.setBlob(2, new SerialBlob(civilFingerPrints.getFingerPrintsFile()));
        	            }
        	            ps.setString(3, civilFingerPrints.getTransactionType());
        	            ps.setInt(4, civilFingerPrints.getFingerPrintsType().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

// TODO delete this when we are 100% sure the table is not needed any more. 
//	final static String CRIMINAL_FINGER_PRINTS_INSERT="insert into CRIMINAL_FINGER_PRINTS "
//			+ "(TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE) "
//			+ "values (?, ?, ?, ?)";
//	@Override
//	public Integer saveCriminalFingerPrints(
//			final CriminalFingerPrints criminalFingerPrints) {
//        log.debug("Inserting row into CRIMINAL_FINGER_PRINTS table : " + criminalFingerPrints.toString());
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(
//        	    new PreparedStatementCreator() {
//        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//        	            PreparedStatement ps =
//        	                connection.prepareStatement(CRIMINAL_FINGER_PRINTS_INSERT, 
//        	                		new String[] {"TRANSACTION_NUMBER", "FINGER_PRINTS_FILE", "TRANSACTION_TYPE", "FINGER_PRINTS_TYPE"});
//        	            ps.setString(1, criminalFingerPrints.getTransactionNumber());
//        	            ps.setBlob(2, new SerialBlob(criminalFingerPrints.getFingerPrintsFile()));
//        	            ps.setString(3, criminalFingerPrints.getTransactionType());
//        	            ps.setString(4, criminalFingerPrints.getFingerPrintsType());
//        	            return ps;
//        	        }
//        	    },
//        	    keyHolder);
//
//         return keyHolder.getKey().intValue();
//	}

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

	//TODO solve the current_state change. 
	final static String CIVIL_INITIAL_RESULTS_INSERT="insert into CIVIL_INITIAL_RESULTS "
			+ "(TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, "
			+ " RESULTS_SENDER_ID) "
			+ "values (?, ?, ?, ?)";
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
        	                		new String[] {"TRANSACTION_NUMBER", "MATCH_NO_MATCH",  
        	                		"TRANSACTION_TYPE", "RESULTS_SENDER_ID"});
        	            ps.setString(1, civilInitialResults.getTransactionNumber());
        	            ps.setBlob(2, new SerialBlob(civilInitialResults.getSearchResultFile()));
        	            ps.setString(3, civilInitialResults.getTransactionType());
        	            ps.setInt(4, civilInitialResults.getResultsSender().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String CRIMINAL_INITIAL_RESULTS_INSERT="insert into CRIMINAL_INITIAL_RESULTS "
			+ "(TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, "
			+ " RESULTS_SENDER_ID) "
			+ "values (?, ?, ?, ?)";
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
        	                		new String[] {"TRANSACTION_NUMBER", "SEARCH_RESULT_FILE",  
        	                		"TRANSACTION_TYPE", "RESULTS_SENDER_ID"});
        	            ps.setString(1, criminalInitialResults.getTransactionNumber());
        	            ps.setBlob(2, new SerialBlob(criminalInitialResults.getSearchResultFile()));
        	            ps.setString(3, criminalInitialResults.getTransactionType());
        	            ps.setInt(4, criminalInitialResults.getResultsSender().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String SUBSEQUENT_RESULTS_INSERT="insert into SUBSEQUENT_RESULTS "
			+ "(TRANSACTION_NUMBER, FBI_SUBSCRIPTION_ID, RAP_BACK_SUBSCRIPTION_IDENTIFIER, "
			+ " MATCH_NO_MATCH, RAPSHEET, TRANSACTION_TYPE ) "
			+ "values (?, ?, ?, ?, ?, ?)";
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
        	                		"MATCH_NO_MATCH",  "RAP_SHEET", "TRANSACTION_TYPE" });
        	            ps.setString(1, subsequentResults.getTransactionNumber());
        	            ps.setString(2, subsequentResults.getFbiSubscriptionId());
        	            ps.setString(3, subsequentResults.getRapbackSubscriptionIdentifier());
        	            ps.setBoolean(4, subsequentResults.getMatch());
        	            ps.setBlob(5, new SerialBlob(subsequentResults.getRapSheet()));
        	            ps.setString(6, subsequentResults.getTransactionType());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final static String FBI_RAP_BACK_SUBSCRIPTION_INSERT="insert into FBI_RAP_BACK_SUBSCRIPTION "
			+ "(FBI_SUBSCRIPTION_ID, UCN, RAP_BACK_CATEGORY, SUBSCRIPTION_TERM, "
			+ " RAP_BACK_EXPIRATION_DATE, RAP_BACK_START_DATE, "
			+ " RAP_BACK_OPT_OUT_IN_STATE_INDICATOR, RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT,FBI_OCA, UCN) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        	                		new String[] {"FBI_SUBSCRIPTION_ID", "UCN", "RAP_BACK_CATEGORY", 
        	                		"SUBSCRIPTION_TERM",  "RAP_BACK_EXPIRATION_DATE",
        	                		"RAP_BACK_START_DATE", "RAP_BACK_OPT_OUT_IN_STATE_INDICATOR", 
        	                		"RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT","FBI_OCA" });
        	            ps.setString(1, fbiRapbackSubscription.getFbiSubscriptionId());
        	            ps.setString(2, fbiRapbackSubscription.getUcn());
        	            ps.setString(3, fbiRapbackSubscription.getRapbackCategory());
        	            ps.setString(4, fbiRapbackSubscription.getSubscriptionTerm());
        	            ps.setDate(5, new java.sql.Date(fbiRapbackSubscription.getRapbackExpirationDate().getMillis()));
        	            ps.setDate(6, new java.sql.Date(fbiRapbackSubscription.getRapbackStartDate().getMillis()));
        	            ps.setBoolean(7, fbiRapbackSubscription.getRapbackOptOutInState());
        	            ps.setString(8, fbiRapbackSubscription.getRapbackActivityNotificationFormat());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	final static String ID_TRANSACTION_SELECT_BY_TRANSACTION_NUMBER = 
			" SELECT * FROM identification_transaction i "
			+ "LEFT JOIN identification_subject s ON s.subject_id = i.subject_id "
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
			
			IdentificationTransaction identificationTransaction = buildIdentificationTransaction(rs, false);
			
			return identificationTransaction;
		}
	}
	
	private final class FullIdentificationTransactionRowMapper 
	implements RowMapper<IdentificationTransaction> {
		public IdentificationTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			IdentificationTransaction identificationTransaction = buildIdentificationTransaction(rs, true);
			
			return identificationTransaction;
		}
	}
	
	private IdentificationTransaction buildIdentificationTransaction(ResultSet rs, boolean includeSubscription)
			throws SQLException {
		IdentificationTransaction identificationTransaction = new IdentificationTransaction();
		identificationTransaction.setTransactionNumber( rs.getString("transaction_number") );
		identificationTransaction.setOtn(rs.getString("otn"));
		identificationTransaction.setTimestamp(toDateTime(rs.getTimestamp("timestamp")));
		identificationTransaction.setOwnerOri(rs.getString("owner_ori"));
		identificationTransaction.setOwnerProgramOca(rs.getString("owner_program_oca"));
		identificationTransaction.setIdentificationCategory(rs.getString("identification_category"));
		identificationTransaction.setArchived(BooleanUtils.isTrue(rs.getBoolean("archived")));

		Integer subjectId = rs.getInt("subject_id");
		
		if (subjectId != null){
			Subject subject = buildSubject(rs);
			identificationTransaction.setSubject(subject);
		}
		
		if (includeSubscription){
			Integer subscriptionId = rs.getInt("id"); 
			
			if (subscriptionId != null){
				Subscription subscription = buildSubscription(rs); 
				identificationTransaction.setSubscription(subscription);
			}
		}
		return identificationTransaction;
	}
	
	private Subscription buildSubscription(ResultSet rs) throws SQLException {
		Subscription subscription = new Subscription(); 
		subscription.setId(rs.getInt("id"));
		subscription.setStartDate(toDateTime(rs.getDate("startDate")));
		subscription.setEndDate(toDateTime(rs.getDate("endDate")));
		subscription.setLastValidationDate(toDateTime(rs.getDate("lastValidationDate")));
		subscription.setActive(rs.getInt("active"));
		subscription.setTopic(rs.getString("topic"));
		subscription.setValidationDueDate(validationDueDateStrategy.getValidationDueDate(subscription));
		return subscription;
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

	final static String SUBJECT_UPDATE="UPDATE identification_subject SET "
			+ "ucn = :ucn, "
			+ "criminal_sid = :criminalSid, "
			+ "civil_sid = :civilSid, "
			+ "first_name = :firstName, "
			+ "last_name = :lastName, "
			+ "middle_initial = :middelInitial, "
			+ "dob = :dob, "
			+ "sex_code = :sexCode "
			+ "WHERE subject_id = :subjectId";
	@Override
	public void updateSubject(Subject subject) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		paramMap.put("ucn", subject.getUcn()); 
		paramMap.put("criminalSid", subject.getCriminalSid()); 
		paramMap.put("civilSid", subject.getCivilSid()); 
		paramMap.put("firstName", subject.getFirstName()); 
		paramMap.put("lastName", subject.getLastName()); 
		paramMap.put("middelInitial", subject.getMiddleInitial()); 
		paramMap.put("dob", subject.getDob() == null? null:subject.getDob().toDate()); 
		paramMap.put("sexCode", subject.getSexCode()); 
		paramMap.put("subjectId", subject.getSubjectId()); 
		
		namedParameterJdbcTemplate.update(SUBJECT_UPDATE, paramMap);
	}
	
	final static String CIVIL_INITIAL_RESULTS_SELECT = "SELECT c.*, t.identification_category, t.timestamp as timestamp_received, "
			+ "t.otn, t.owner_ori, t.owner_program_oca, t.archived, s.* "
			+ "FROM civil_initial_results c "
			+ "LEFT OUTER JOIN identification_transaction t ON t.transaction_number = c.transaction_number "
			+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id "
			+ "WHERE t.owner_ori = ?";

	@Override
	public List<CivilInitialResults> getCivilInitialResults(String ownerOri) {
		List<CivilInitialResults> civilIntialResults = 
				jdbcTemplate.query(CIVIL_INITIAL_RESULTS_SELECT, 
						new CivilInitialResultsRowMapper(), ownerOri);
		return civilIntialResults;
	}

	private final class CivilInitialResultsRowMapper implements
			RowMapper<CivilInitialResults> {
		public CivilInitialResults mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			CivilInitialResults civilInitialResults = buildCivilIntialResult(rs);
			
			civilInitialResults.setIdentificationTransaction(buildIdentificationTransaction(rs, false));

			return civilInitialResults;
		}

	}
	
	private CivilInitialResults buildCivilIntialResult(ResultSet rs) throws SQLException {
		CivilInitialResults civilInitialResults = new CivilInitialResults();
		civilInitialResults.setId(rs.getInt("civil_initial_result_id"));
		civilInitialResults.setTransactionNumber(rs.getString("transaction_number"));
		civilInitialResults.setTransactionType(rs.getString("transaction_type"));
		civilInitialResults.setResultsSender(ResultSender.values()[rs.getInt("results_sender_id") - 1]);
		try{
			civilInitialResults.setSearchResultFile(ZipUtils.unzip(rs.getBytes("search_result_file")));
		}
		catch(Exception e){
			log.error("Got exception extracting the search result file for " + 
					civilInitialResults.getTransactionNumber(), e);
		}
		civilInitialResults.setTimestamp(toDateTime(rs.getTimestamp("timestamp")));
		return civilInitialResults;
	}

	private final String CIVIL_INITIAL_RESULTS_ID_SELECT = "SELECT t.civiL_INITIAL_RESULT_ID  "
			+ "FROM RAPBACK_DATASTORE.CIVIL_INITIAL_RESULTS t "
			+ "WHERE t.TRANSACTION_NUMBER  = ? AND RESULTS_SENDER_ID = ?";

	@Override
	public Integer getCivilIntialResultsId(String transactionNumber,
			ResultSender resultSender) {
		return jdbcTemplate.queryForInt(CIVIL_INITIAL_RESULTS_ID_SELECT, transactionNumber, resultSender.ordinal() + 1);
	}

	final static String CIVIL_IDENTIFICATION_TRANSACTION_SELECT = "SELECT t.transaction_number, t.identification_category, "
			+ "t.timestamp as transaction_timestamp, t.otn, t.owner_ori,  t.owner_program_oca, t.archived, s.*, sub.* "
			+ "FROM identification_transaction t "
			+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id "
			+ "LEFT OUTER JOIN subscription sub ON sub.id = t.subscription_id "
			+ "WHERE t.owner_ori = ? and (select count(*)>0 from "
			+ "	civil_initial_results c where c.transaction_number = t.transaction_number)"; 

	@Override
	public List<IdentificationTransaction> getCivilIdentificationTransactions(
			String ori) {
		List<IdentificationTransaction> identificationTransactions = 
				jdbcTemplate.query(CIVIL_IDENTIFICATION_TRANSACTION_SELECT, 
						new FullIdentificationTransactionRowMapper(), ori);
		return identificationTransactions;
	}

	final static String CRIMINAL_IDENTIFICATION_TRANSACTION_SELECT = "SELECT t.transaction_number, t.identification_category, "
			+ "t.timestamp as transaction_timestamp, t.otn, t.owner_ori,  t.owner_program_oca, t.archived, s.* "
			+ "FROM identification_transaction t "
			+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id "
			+ "WHERE t.owner_ori = ? and (select count(*)>0 from "
			+ "	criminal_initial_results c where c.transaction_number = t.transaction_number)"; 
	@Override
	public List<IdentificationTransaction> getCriminalIdentificationTransactions(
			String ori) {
		List<IdentificationTransaction> identificationTransactions = 
				jdbcTemplate.query(CRIMINAL_IDENTIFICATION_TRANSACTION_SELECT, 
						new IdentificationTransactionRowMapper(), ori);
		return identificationTransactions;
	}

	final static String CIVIL_INITIAL_RESULTS_BY_TRANSACTION_NUMBER = "SELECT c.*, r.* "
			+ "FROM civil_initial_results c "
			+ "LEFT OUTER JOIN CIVIL_INITIAL_RAP_SHEET r ON r.CIVIL_INITIAL_RESULT_ID = c.CIVIL_INITIAL_RESULT_ID "
			+ "WHERE transaction_number = ?";
	
	@Override
	public List<CivilInitialResults> getIdentificationCivilInitialResults(
			String transactionNumber) {
		List<CivilInitialResults> results= 
				jdbcTemplate.query(CIVIL_INITIAL_RESULTS_BY_TRANSACTION_NUMBER, 
						new CivilInitialResultsResultSetExtractor(), transactionNumber);
		return results;
	}

	private class CivilInitialResultsResultSetExtractor implements ResultSetExtractor<List<CivilInitialResults>> {

		@Override
		public List<CivilInitialResults> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
            Map<Integer, CivilInitialResults> map = new HashMap<Integer, CivilInitialResults>();
            CivilInitialResults civilInitialResults = null;
            while (rs.next()) {
                Integer civilIntialResultId = rs.getInt("civil_initial_result_id" ); 
                civilInitialResults  = map.get( civilIntialResultId );
                if ( civilInitialResults  == null){
                	civilInitialResults = buildCivilIntialResult(rs); 
                	map.put(civilIntialResultId, civilInitialResults); 
                }
	              
               byte[] rapSheet = rs.getBytes("rap_sheet" );
               
               if (rapSheet != null){
            	   try{
            		   civilInitialResults.getRapsheets().add( ZipUtils.unzip(rapSheet) );
            	   }
            	   catch(Exception e){
            		   log.error("Got exception extracting the rapsheet for " + 
            			   civilInitialResults.getTransactionNumber(), e);
            	   }
	           }
            }
            return (List<CivilInitialResults>) new ArrayList<CivilInitialResults>(map.values());
		}

	}

}