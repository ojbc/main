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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.TopicMapValidationDueDateStrategy;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.helper.ZipUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.model.saml.SamlAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
	
    @Value("${rapbackDatastoreAdapter.civilIdlePeriod:60}")
    private Integer civilIdlePeriod;
    
    @Value("${rapbackDatastoreAdapter.criminalIdlePeriod:60}")
    private Integer criminalIdlePeriod;
    
    @Value("#{'${rapbackDatastoreAdapter.agencySuperUsers:}'.split(',')}")
    private List<String> agencySuperUsers;

    @Value("#{'${rapbackDatastoreAdapter.superUsers:}'.split(',')}")
    private List<String> superUsers;
    
    @Value("#{'${rapbackDatastoreAdapter.civilAgencyOris:}'.split(',')}")
    private List<String> civilAgencyOris;
    
	@Override
	public Integer saveSubject(final Subject subject) {
        log.debug("Inserting row into IDENTIFICATION_SUBJECT table : " + subject);
        
        final String SUBJECT_INSERT="INSERT into IDENTIFICATION_SUBJECT "
        		+ "(UCN, CRIMINAL_SID, CIVIL_SID, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL, DOB, SEX_CODE) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";

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
	
	@Override
	public Subject getSubject(Integer id) {
		final String SUBJECT_SELECT="SELECT * FROM IDENTIFICATION_SUBJECT WHERE SUBJECT_ID = ?";
		
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
	
	/**
	 * @param date
	 * @return null if date is null, otherwise java.sql.Date.valueOf(date)
	 */
	private java.sql.Date toSqlDate(LocalDate date){
		return date == null? null : java.sql.Date.valueOf(date); 
	}
	
	private Date toDate(DateTime date){
		return date == null? null : date.toDate(); 
	}

	@Override
	@Transactional
	public void saveIdentificationTransaction(
			IdentificationTransaction identificationTransaction) {
        log.debug("Inserting row into IDENTIFICATION_TRANSACTION table : " + identificationTransaction.toString());
        
        final String IDENTIFICATION_TRANSACTION_INSERT="INSERT into IDENTIFICATION_TRANSACTION "
        		+ "(TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, ARCHIVED, IDENTIFICATION_CATEGORY) "
        		+ "values (?, ?, ?, ?, ?, ?, ?)";
        
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

	@Override
	public Integer saveCivilFbiSubscriptionRecord(
			final CivilFbiSubscriptionRecord civilFbiSubscriptionRecord) {
        log.debug("Inserting row into CIVIL_FBI_SUBSCRIPTION_RECORD table : " + civilFbiSubscriptionRecord.toString());

        final String CIVIL_FBI_SUBSCRIPTION_RECORD_INSERT="INSERT into CIVIL_FBI_SUBSCRIPTION_RECORD "
        		+ "(SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, CIVIL_INITIAL_RESULT_ID, LAST_MODIFIED_BY) "
        		+ "values (?, ?, ?, ?)";
        
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

	@Override
	public Integer saveCriminalFbiSubscriptionRecord(
			final CriminalFbiSubscriptionRecord criminalFbiSubscriptionRecord) {
        log.debug("Inserting row into CRIMINAL_FBI_SUBSCRIPTION_RECORD table : " + criminalFbiSubscriptionRecord.toString());
        
        final String CRIMINAL_FBI_SUBSCRIPTION_RECORD_INSERT="insert into CRIMINAL_FBI_SUBSCRIPTION_RECORD "
        		+ "(SUBSCRIPTION_ID, FBI_SUBSCRIPTION_ID, FBI_OCA) "
        		+ "values (?, ?, ?)";

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

	@Override
	public Integer saveCivilFingerPrints(final CivilFingerPrints civilFingerPrints) {
        log.debug("Inserting row into CIVIL_FINGER_PRINTS table : " + civilFingerPrints.toString());

        final String CIVIL_FINGER_PRINTS_INSERT="insert into CIVIL_FINGER_PRINTS "
        		+ "(TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) "
        		+ "values (?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_FINGER_PRINTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "FINGER_PRINTS_FILE", "FINGER_PRINTS_TYPE"});
        	            ps.setString(1, civilFingerPrints.getTransactionNumber());
        	            
        	            if (civilFingerPrints.getFingerPrintsFile() != null){
							ps.setBlob(2, new SerialBlob(ZipUtils.zip(civilFingerPrints.getFingerPrintsFile())));
        	            }
        	            ps.setInt(3, civilFingerPrints.getFingerPrintsType().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

// TODO delete this when we are 100% sure the table is not needed any more. 
//	final String CRIMINAL_FINGER_PRINTS_INSERT="insert into CRIMINAL_FINGER_PRINTS "
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

	@Override
	public Integer saveCivilInitialRapSheet(
			final CivilInitialRapSheet civilInitialRapSheet) {
        log.debug("Inserting row into CIVIL_INITIAL_RAP_SHEET table : " + civilInitialRapSheet.toString());

        final String CIVIL_INITIAL_RAP_SHEET_INSERT="insert into CIVIL_INITIAL_RAP_SHEET "
        		+ "(CIVIL_INITIAL_RESULT_ID, RAP_SHEET) "
        		+ "values (?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_INITIAL_RAP_SHEET_INSERT, 
        	                		new String[] {"CIVIL_INITIAL_RESULT_ID", "RAP_SHEET"});
        	            ps.setInt(1, civilInitialRapSheet.getCivilIntitialResultId());
						ps.setBlob(2, new SerialBlob(ZipUtils.zip(civilInitialRapSheet.getRapSheet())));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer saveCivilInitialResults(
			final CivilInitialResults civilInitialResults) {
        log.debug("Inserting row into CIVIL_INITIAL_RESULTS table : " + civilInitialResults.toString());

        final String CIVIL_INITIAL_RESULTS_INSERT="insert into CIVIL_INITIAL_RESULTS "
        		+ "(TRANSACTION_NUMBER, SEARCH_RESULT_FILE, "
        		+ " RESULTS_SENDER_ID) "
        		+ "values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CIVIL_INITIAL_RESULTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "MATCH_NO_MATCH",  
        	                			"RESULTS_SENDER_ID"});
        	            ps.setString(1, civilInitialResults.getTransactionNumber());
						ps.setBlob(2, new SerialBlob(ZipUtils.zip(civilInitialResults.getSearchResultFile())));
        	            ps.setInt(3, civilInitialResults.getResultsSender().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer saveCriminalInitialResults(
			final CriminalInitialResults criminalInitialResults) {
        log.debug("Inserting row into CRIMINAL_INITIAL_RESULTS table : " + criminalInitialResults.toString());

        final String CRIMINAL_INITIAL_RESULTS_INSERT="insert into CRIMINAL_INITIAL_RESULTS "
        		+ "(TRANSACTION_NUMBER, SEARCH_RESULT_FILE, RESULTS_SENDER_ID) "
        		+ "values (?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(CRIMINAL_INITIAL_RESULTS_INSERT, 
        	                		new String[] {"TRANSACTION_NUMBER", "SEARCH_RESULT_FILE",  
        	                			"RESULTS_SENDER_ID"});
        	            ps.setString(1, criminalInitialResults.getTransactionNumber());
						ps.setBlob(2, new SerialBlob(ZipUtils.zip(criminalInitialResults.getSearchResultFile())));
        	            ps.setInt(3, criminalInitialResults.getResultsSender().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public void saveFbiRapbackSubscription(
			final FbiRapbackSubscription fbiRapbackSubscription) {
        log.debug("Inserting row into FBI_RAP_BACK_SUBSCRIPTION table : " + fbiRapbackSubscription.toString());

        final String FBI_RAP_BACK_SUBSCRIPTION_INSERT="insert into FBI_RAP_BACK_SUBSCRIPTION "
        		+ "(FBI_SUBSCRIPTION_ID, UCN, RAP_BACK_CATEGORY_CODE, RAP_BACK_SUBSCRIPTION_TERM_CODE, "
        		+ " RAP_BACK_EXPIRATION_DATE, RAP_BACK_START_DATE, RAP_BACK_TERM_DATE, "
        		+ " RAP_BACK_OPT_OUT_IN_STATE_INDICATOR, RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE) "
        		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(FBI_RAP_BACK_SUBSCRIPTION_INSERT, 
        				fbiRapbackSubscription.getFbiSubscriptionId(),
        	            fbiRapbackSubscription.getUcn(),
        	            fbiRapbackSubscription.getRapbackCategory(),
        	            fbiRapbackSubscription.getSubscriptionTerm(),
        	            toDate(fbiRapbackSubscription.getRapbackExpirationDate()),
        	            toDate(fbiRapbackSubscription.getRapbackStartDate()),
        	            toDate(fbiRapbackSubscription.getRapbackTermDate()),
        	            fbiRapbackSubscription.getRapbackOptOutInState(),
        	            fbiRapbackSubscription.getRapbackActivityNotificationFormat());
	}

	
	@Override
	public IdentificationTransaction getIdentificationTransaction(
			String transactionNumber) {
		final String ID_TRANSACTION_SELECT_BY_TRANSACTION_NUMBER = 
				" SELECT * FROM identification_transaction i "
						+ "LEFT JOIN identification_subject s ON s.subject_id = i.subject_id "
						+ "WHERE transaction_number = ?";

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
		identificationTransaction.setTimestamp(toDateTime(rs.getTimestamp("report_timestamp")));
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
			identificationTransaction.setHavingSubsequentResults(rs.getBoolean("having_subsequent_result"));
			Integer subscriptionId = rs.getInt("id"); 
			
			if (subscriptionId != null && subscriptionId > 0){
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

	@Override
	public void updateSubject(Subject subject) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String SUBJECT_UPDATE="UPDATE identification_subject SET "
				+ "ucn = :ucn, "
				+ "criminal_sid = :criminalSid, "
				+ "civil_sid = :civilSid, "
				+ "first_name = :firstName, "
				+ "last_name = :lastName, "
				+ "middle_initial = :middelInitial, "
				+ "dob = :dob, "
				+ "sex_code = :sexCode "
				+ "WHERE subject_id = :subjectId";

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
	
	@Override
	public List<CivilInitialResults> getCivilInitialResults(String ownerOri) {
		final String CIVIL_INITIAL_RESULTS_SELECT = "SELECT c.*, t.identification_category, t.report_timestamp as timestamp_received, "
				+ "t.otn, t.owner_ori, t.owner_program_oca, t.archived, s.* "
				+ "FROM civil_initial_results c "
				+ "LEFT OUTER JOIN identification_transaction t ON t.transaction_number = c.transaction_number "
				+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id "
				+ "WHERE t.owner_ori = ?";
		
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
		civilInitialResults.setResultsSender(ResultSender.values()[rs.getInt("results_sender_id") - 1]);
		try{
			civilInitialResults.setSearchResultFile(ZipUtils.unzip(rs.getBytes("search_result_file")));
		}
		catch(Exception e){
			log.error("Got exception extracting the search result file for " + 
					civilInitialResults.getTransactionNumber(), e);
		}
		civilInitialResults.setTimestamp(toDateTime(rs.getTimestamp("report_timestamp")));
		return civilInitialResults;
	}

	@Override
	public Integer getCivilIntialResultsId(String transactionNumber,
			ResultSender resultSender) {
		final String CIVIL_INITIAL_RESULTS_ID_SELECT = "SELECT t.civiL_INITIAL_RESULT_ID  "
				+ "FROM CIVIL_INITIAL_RESULTS t "
				+ "WHERE t.TRANSACTION_NUMBER  = ? AND RESULTS_SENDER_ID = ?";
		
		List<Integer> ids = jdbcTemplate.queryForList(CIVIL_INITIAL_RESULTS_ID_SELECT, 
				Integer.class, transactionNumber, resultSender.ordinal() + 1);
		
		return DataAccessUtils.singleResult(ids);
	}

	//TODO incorporate the search criteria in the search request into the sql string. 
	@Override
	public List<IdentificationTransaction> getCivilIdentificationTransactions(
			SAMLTokenPrincipal token, IdentificationResultSearchRequest searchRequest) {
		
		StringBuilder sb = new StringBuilder();
		sb.append( "SELECT t.transaction_number, t.identification_category, "
				+ "t.report_timestamp as transaction_timestamp, t.otn, t.owner_ori,  t.owner_program_oca, t.archived, s.*, sub.*, "
				+ "(select count(*) > 0 from subsequent_results subsq where subsq.ucn = s.ucn) as having_subsequent_result "
				+ "FROM identification_transaction t "
				+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id "
				+ "LEFT OUTER JOIN subscription sub ON sub.id = t.subscription_id "
				+ "WHERE (select count(*)>0 from "
				+ "	civil_initial_results c where c.transaction_number = t.transaction_number) "
				+ "	AND (:firstName is null OR s.first_name = :firstName) "
				+ " AND (:lastName is null OR s.last_name = :lastName ) "
				+ "	AND (:otn is null OR t.otn = :otn ) "
				+ "	AND (:startDate is null OR t.report_timestamp >= :startDate ) "
				+ "	AND (:endDate is null OR t.report_timestamp <= :endDate ) "
				+ "	AND (:excludeArchived = false OR t.archived != true ) "
				+ "	AND (:excludeSubscribed = false OR (t.archived = true OR sub.id is null OR sub.id <= 0 OR sub.active = false )) "
				+ "	AND (:excludeAvailableForSubscription  = false OR (t.archived = true OR (sub.id > 0 AND sub.active = true))) "
				+ "	AND (:identificationReasonCode is null OR identification_category in (:identificationReasonCode)) ");
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		paramMap.put("firstName", searchRequest.getFirstName());
		paramMap.put("lastName", searchRequest.getLastName()); 
		paramMap.put("otn", searchRequest.getOtn()); 
		paramMap.put("startDate", toSqlDate(searchRequest.getReportedDateStartDate())); 
		paramMap.put("endDate", toSqlDate(searchRequest.getReportedDateEndDate())); 
		paramMap.put("excludeArchived", isExcluding(searchRequest.getIdentificationTransactionStatus(), IdentificationTransactionState.Archived)); 
		paramMap.put("excludeSubscribed", isExcluding(searchRequest.getIdentificationTransactionStatus(), IdentificationTransactionState.Subscribed)); 
		paramMap.put("excludeAvailableForSubscription", isExcluding(searchRequest.getIdentificationTransactionStatus(), IdentificationTransactionState.Available_for_Subscription)); 
		paramMap.put("identificationReasonCode", searchRequest.getCivilIdentificationReasonCodes()); 

        String ori = SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.EmployerORI); 
        String federationId = SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.FederationId);
        
        boolean isNotSuperUser = isNotSuperUser(ori, federationId); 
        boolean isNotAgencySuperUser = isNotAgencySuperUser(ori, federationId); 
        
		if ( isNotSuperUser){
			sb.append( "AND t.owner_ori = :ori "); 
			paramMap.put("ori", ori);
		}
		
		if ( isNotSuperUser && isNotAgencySuperUser){

			if ( isNotCivilAgencyUser(ori) ){
				sb.append ( " AND t.identification_category in ( :identificationCategoryList )");
				List<String> identificationCategorys = getViewableIdentificationCategories(token, 
						"CIVIL"); 
				paramMap.put("identificationCategoryList", identificationCategorys);
			}
		}
		
		List<IdentificationTransaction> identificationTransactions = 
				namedParameterJdbcTemplate.query( sb.toString(), paramMap,
						new FullIdentificationTransactionRowMapper());
		return identificationTransactions;
	}

	private boolean isExcluding(List<String> statusCriteria, IdentificationTransactionState state) {
		return statusCriteria!= null && statusCriteria.size() > 0 && !statusCriteria.contains(state.toString());
	}

	private boolean isSuperUser(String ori, String federationId) {
		return superUsers.contains(ori + "&" + federationId);
	}
	
	private boolean isNotSuperUser(String ori, String federationId) {
		return !isSuperUser(ori , federationId);
	}

	private boolean isAgencySuperUser(String ori, String federationId) {
		return agencySuperUsers.contains(ori + "&" + federationId);
	}
	
	private boolean isNotAgencySuperUser(String ori, String federationId) {
		return !isAgencySuperUser(ori, federationId);
	}
	
	private boolean isCivilAgencyUser(String ori) {
		return civilAgencyOris.contains(ori);
	}
	
	private boolean isNotCivilAgencyUser(String ori) {
		return !isCivilAgencyUser(ori);
	}
	
	public List<String> getViewableIdentificationCategories(
		SAMLTokenPrincipal token, String identificationCategoryType) {
		final String sql = "select i.identification_category_code from identification_category i "
				+ "left join job_title_privilege j on j.identification_category_id = i.identification_category_id "
				+ "left join job_title t on t.job_title_id = j.job_title_id "
				+ "left join department d on d.department_id = t.department_id "
				+ "left join agency_profile a on a.agency_id = d.agency_id "
				+ "where identification_category_type = :identificationCategoryType  "
				+ "		AND agency_ori = :agencyOri "
				+ "		AND department_name = :departmentName "
				+ "		AND title_description = :titleDescription ";
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("identificationCategoryType", identificationCategoryType);
		paramMap.put("agencyOri", SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.EmployerORI));
		paramMap.put("departmentName", SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.EmployerSubUnitName));
		paramMap.put("titleDescription", SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.EmployeePositionName));
		
		List<String> identificationCategories = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
		return identificationCategories;
	}

	@Override
	public List<IdentificationTransaction> getCriminalIdentificationTransactions(
			SAMLTokenPrincipal token, IdentificationResultSearchRequest searchRequest) {
		StringBuilder sqlStringBuilder = new StringBuilder("SELECT t.transaction_number, t.identification_category, "
				+ "t.report_timestamp as transaction_timestamp, t.otn, t.owner_ori,  t.owner_program_oca, t.archived, s.* "
				+ "FROM identification_transaction t "
				+ "LEFT OUTER JOIN identification_subject s ON s.subject_id = t.subject_id ");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("firstName", searchRequest.getFirstName());
		paramMap.put("lastName", searchRequest.getLastName()); 
		paramMap.put("otn", searchRequest.getOtn()); 
		paramMap.put("startDate", toSqlDate(searchRequest.getReportedDateStartDate())); 
		paramMap.put("endDate", toSqlDate(searchRequest.getReportedDateEndDate())); 
		paramMap.put("excludeArchived", isExcluding(searchRequest.getIdentificationTransactionStatus(), IdentificationTransactionState.Archived)); 
		paramMap.put("excludeAvailableForSubscription", isExcluding(searchRequest.getIdentificationTransactionStatus(), IdentificationTransactionState.Subscribed)); 
		paramMap.put("identificationReasonCode", searchRequest.getCriminalIdentificationReasonCodes()); 
		
        String ori = SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.EmployerORI); 
        String federationId = SAMLTokenUtils.getAttributeValueFromSamlToken(token, SamlAttribute.FederationId); 
        
		if ( isSuperUser(ori, federationId)){
			sqlStringBuilder.append(" WHERE " ); 
		}
		else {
			sqlStringBuilder.append(" WHERE t.owner_ori = :ori AND " ); 
			paramMap.put("ori", ori);
			
			if ( isNotAgencySuperUser(ori, federationId)){
				sqlStringBuilder.append(" t.identification_category in ( :identificationCategoryList ) AND ");
				
				List<String> identificationCategorys = 
						getViewableIdentificationCategories(token, "CRIMINAL"); 
				paramMap.put("identificationCategoryList", identificationCategorys);
			}
		}
		
		sqlStringBuilder.append(" (select count(*)>0 from criminal_initial_results c where c.transaction_number = t.transaction_number) ");
		sqlStringBuilder.append(
				  "	AND (:firstName is null OR s.first_name = :firstName) "
				+ " AND (:lastName is null OR s.last_name = :lastName ) "
				+ "	AND (:otn is null OR t.otn = :otn ) "
				+ "	AND (:startDate is null OR t.report_timestamp >= :startDate ) "
				+ "	AND (:endDate is null OR t.report_timestamp <= :endDate ) "
				+ "	AND (:excludeArchived = false OR t.archived != true ) "
				+ "	AND (:excludeAvailableForSubscription  = false OR t.archived = true) "
				+ "	AND (:identificationReasonCode is null OR identification_category in (:identificationReasonCode)) ");
		
		List<IdentificationTransaction> identificationTransactions = 
				namedParameterJdbcTemplate.query( sqlStringBuilder.toString(), paramMap,  
						new IdentificationTransactionRowMapper());
		return identificationTransactions;
	}

	@Override
	public List<CivilInitialResults> getIdentificationCivilInitialResults(
			String transactionNumber) {
		final String CIVIL_INITIAL_RESULTS_BY_TRANSACTION_NUMBER = "SELECT c.*, r.* "
				+ "FROM civil_initial_results c "
				+ "LEFT OUTER JOIN CIVIL_INITIAL_RAP_SHEET r ON r.CIVIL_INITIAL_RESULT_ID = c.CIVIL_INITIAL_RESULT_ID "
				+ "WHERE transaction_number = ?";
		
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

	@Override
	public void updateFbiRapbackSubscription(
			FbiRapbackSubscription fbiRapbackSubscription) {
		final String FBI_RAP_BACK_SUBSCRIPTION_UPDATE ="update FBI_RAP_BACK_SUBSCRIPTION SET "
				+ "RAP_BACK_SUBSCRIPTION_TERM_CODE = :rapbackSubscriptionTermCode, "
				+ "RAP_BACK_EXPIRATION_DATE = :rapbackExpirationDate , "
				+ "RAP_BACK_START_DATE = :rapbackStartDate, "
				+ "RAP_BACK_TERM_DATE = :rapbackTermDate, "
				+ "RAP_BACK_OPT_OUT_IN_STATE_INDICATOR = :rapbackOptOutInStateIndicator, "
				+ "RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE = :rapbackActivityNotificationFormatCode "
				+ "where FBI_SUBSCRIPTION_ID = :fbiSubscriptionId ";
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		paramMap.put("rapbackSubscriptionTermCode", fbiRapbackSubscription.getSubscriptionTerm()); 
		paramMap.put("rapbackExpirationDate", toDate(fbiRapbackSubscription.getRapbackExpirationDate())); 
		paramMap.put("rapbackStartDate", toDate(fbiRapbackSubscription.getRapbackStartDate())); 
		paramMap.put("rapbackTermDate", toDate(fbiRapbackSubscription.getRapbackTermDate())); 
		paramMap.put("rapbackOptOutInStateIndicator", fbiRapbackSubscription.getRapbackOptOutInState()); 
		paramMap.put("rapbackActivityNotificationFormatCode", fbiRapbackSubscription.getRapbackActivityNotificationFormat()); 
		paramMap.put("fbiSubscriptionId", fbiRapbackSubscription.getFbiSubscriptionId()); 
		
		namedParameterJdbcTemplate.update(FBI_RAP_BACK_SUBSCRIPTION_UPDATE, paramMap);
	}

	@Override
	public void consolidateSid(String currentSid, String newSid) {
		final String SID_CONSOLIDATION = "UPDATE identification_subject "
				+ "SET criminal_sid =(CASE WHEN criminal_sid = :currentSid THEN :newSid ELSE criminal_sid END), "
				+ "	   civil_sid = (CASE WHEN civil_sid=:currentSid THEN :newSid ELSE civil_sid END)";
		
		Map<String, String> paramMap = new HashMap<String, String>(); 
		paramMap.put("currentSid", currentSid);
		paramMap.put("newSid", newSid);
		
		this.namedParameterJdbcTemplate.update(SID_CONSOLIDATION, paramMap);
	}

	@Override
	@Transactional
	public void consolidateUcn(String currentUcn, String newUcn) {
		final String FBI_SUBSCRIPTION_UCN_CONSOLIDATION = "UPDATE fbi_rap_back_subscription "
				+ "SET ucn = :newUcn "
				+ "WHERE ucn = :currentUcn";
		final String IDENTIFICATION_SUBJECT_UCN_CONSOLIDATION = "UPDATE identification_subject "
				+ "SET ucn = :newUcn "
				+ "WHERE ucn = :currentUcn";
		final String SUBSEQUENT_RESULTS_UCN_CONSOLIDATION = "UPDATE subsequent_results "
				+ "SET ucn = :newUcn "
				+ "WHERE ucn = :currentUcn";
		
		Map<String, String> paramMap = new HashMap<String, String>(); 
		paramMap.put("currentUcn", currentUcn);
		paramMap.put("newUcn", newUcn);
		
		this.namedParameterJdbcTemplate.update(FBI_SUBSCRIPTION_UCN_CONSOLIDATION, paramMap); 
		this.namedParameterJdbcTemplate.update(IDENTIFICATION_SUBJECT_UCN_CONSOLIDATION, paramMap); 
		this.namedParameterJdbcTemplate.update(SUBSEQUENT_RESULTS_UCN_CONSOLIDATION, paramMap); 
	}

	@Override
	public AgencyProfile getAgencyProfile(String ori) {
		final String AGENCY_PROFILE_SELECT_BY_ORI = "SELECT * FROM agency_profile a "
				+ "LEFT JOIN agency_contact_email e ON e.agency_id = a.agency_id "
				+ "WHERE agency_ori = ?";
		
		List<AgencyProfile> agencyProfiles = jdbcTemplate.query(AGENCY_PROFILE_SELECT_BY_ORI, new AgencyProfileResultSetExtractor(), ori);
		return DataAccessUtils.singleResult(agencyProfiles);
	}

	private class AgencyProfileResultSetExtractor implements ResultSetExtractor<List<AgencyProfile>> {

		@Override
		public List<AgencyProfile> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
            Map<Integer, AgencyProfile> map = new HashMap<Integer, AgencyProfile>();
            AgencyProfile agencyProfile = null;
            while (rs.next()) {
                Integer agencyProfileId = rs.getInt("agency_id" ); 
                agencyProfile  = map.get( agencyProfileId );
                if ( agencyProfile  == null){
                	agencyProfile = new AgencyProfile();
                	agencyProfile.setId(agencyProfileId);
                	agencyProfile.setAgencyName(rs.getString("agency_name"));
                	agencyProfile.setAgencyOri(rs.getString("agency_ori"));
                	agencyProfile.setFbiSubscriptionQualified(rs.getBoolean("fbi_subscription_qualification"));

                	List<String> emails = new ArrayList<String>();
                	String email = rs.getString("agency_email");
                	if (StringUtils.isNotBlank(email)){
                		emails.add(email);
                	}
                	agencyProfile.setEmails(emails);
                	map.put(agencyProfileId, agencyProfile);
                }
                else{
                	String email = rs.getString("agency_email");
                	if (StringUtils.isNotBlank(email) && !agencyProfile.getEmails().contains(email)){
                		agencyProfile.getEmails().add(email);
                	}
                }
	              
            }
            
            return (List<AgencyProfile>) new ArrayList<AgencyProfile>(map.values());

		}

	}

	@Override
	public int archiveCivilIdentifications() {
		log.info("Archiving records that have been available "
				+ "for subscription for over " + civilIdlePeriod + " days.");
		final String sql = "UPDATE identification_transaction t "
				+ "SET t.archived = 'true' "
				+ "WHERE (select count(*)>0 FROM civil_initial_results c where c.transaction_number = t.transaction_number) "
				+ "	AND t.archived = 'false' AND t.available_for_subscription_start_date < ?";
		
		DateTime currentDate = new DateTime(); 
		DateTime comparableDate = currentDate.minusDays(civilIdlePeriod);
		log.info("Comparable Date:" + comparableDate);
		
		int updatedRows = jdbcTemplate.update(sql, comparableDate.toDate());
		log.info("Archived " + updatedRows + " rows that have been idle for over " + civilIdlePeriod + " days ");
		return updatedRows;
	}
	
	@Override
	public int archiveCriminalIdentifications() {
		log.info("Archiving records that have been available "
				+ "for subscription for over " + criminalIdlePeriod + " days.");
		final String sql = "UPDATE identification_transaction t "
				+ "SET t.archived = 'true' "
				+ "WHERE (select count(*)>0 FROM criminal_initial_results c where c.transaction_number = t.transaction_number) "
				+ "AND t.archived = 'false' AND t.available_for_subscription_start_date < ?";
		
		DateTime currentDate = new DateTime(); 
		DateTime comparableDate = currentDate.minusDays(criminalIdlePeriod);
		log.info("Comparable Date:" + comparableDate);
		
		int updatedRows = jdbcTemplate.update(sql, comparableDate.toDate());
		log.info("Archived " + updatedRows + " rows that have been idle for over " + criminalIdlePeriod + " days ");
		return updatedRows;
	}

	@Override
	public int archiveIdentificationResult(String transactionNumber) {
		log.info("Archiving record with transaction number " + transactionNumber);
		
		final String sql = "UPDATE identification_transaction t "
				+ "SET t.archived = 'true' "
				+ "WHERE t.transaction_number = ?";
		int result = jdbcTemplate.update(sql, transactionNumber);
		return result;
	}

	@Override
	public List<SubsequentResults> getSubsequentResults(String transactionNumber) {
		log.info("Retreiving subsequent results by transaction number " + transactionNumber);
		
		final String sql ="SELECT subs.* FROM subsequent_results subs "
				+ "LEFT JOIN identification_subject s ON s.ucn = subs.ucn "
				+ "LEFT JOIN identification_transaction t ON t.subject_id = s.subject_id "
				+ "WHERE t.transaction_number = ?";
		
		List<SubsequentResults> subsequentResults = 
				jdbcTemplate.query(sql, new SubsequentResultRowMapper(), transactionNumber);
		return subsequentResults;
	}

	private final class SubsequentResultRowMapper implements RowMapper<SubsequentResults> {
		public SubsequentResults mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SubsequentResults subsequentResult = new SubsequentResults();
			subsequentResult.setId(rs.getLong("subsequent_result_id"));
			subsequentResult.setUcn(rs.getString("ucn"));
			subsequentResult.setRapSheet(ZipUtils.unzip(rs.getBytes("rap_sheet")));
			subsequentResult.setResultsSender(ResultSender.values()[rs.getInt("results_sender_id") -1]);
			return subsequentResult;
		}
	}

	@Override
	public List<SubsequentResults> getSubsequentResultsByUcn(String ucn) {
		log.info("Retreiving subsequent results by FBI ID " + ucn);
		
		final String sql ="SELECT subs.* FROM subsequent_results subs "
				+ "WHERE subs.ucn = ?";
		
		List<SubsequentResults> subsequentResults = 
				jdbcTemplate.query(sql, new SubsequentResultRowMapper(), ucn);
		return subsequentResults;
	}

	@Override
	public List<CriminalInitialResults> getIdentificationCriminalInitialResults(
			String transactionNumber) {
		log.info("Retrieving criminal initial results by transaction number : " + transactionNumber);
		
		final String sql = "SELECT * FROM criminal_initial_results t WHERE t.transaction_number = ?";
		List<CriminalInitialResults> criminalIntialResults = 
				jdbcTemplate.query(sql, new CriminalInitialResultsRowMapper(), transactionNumber);
		return criminalIntialResults;
	}

	private final class CriminalInitialResultsRowMapper implements
			RowMapper<CriminalInitialResults> {
		public CriminalInitialResults mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			CriminalInitialResults criminalInitialResults = new CriminalInitialResults();

			criminalInitialResults.setId(rs.getLong("criminal_initial_result_id"));
			criminalInitialResults.setTransactionNumber(rs
					.getString("transaction_number"));
			criminalInitialResults.setResultsSender(ResultSender.values()[rs
					.getInt("results_sender_id") - 1]);
			try {
				criminalInitialResults.setSearchResultFile(ZipUtils.unzip(rs
						.getBytes("search_result_file")));
			} catch (Exception e) {
				log.error("Got exception extracting the search result file for "
						+ criminalInitialResults.getTransactionNumber(), e);
			}
			criminalInitialResults.setTimestamp(toDateTime(rs
					.getTimestamp("timestamp")));

			return criminalInitialResults;
		}

	}

	@Override
	public String getIdentificationCategoryType(String transactionNumber) {
		log.info("Retrieving identification category by transaction number : " + transactionNumber);
		
		final String sql = "SELECT identification_category_type FROM identification_transaction t "
				+ "LEFT JOIN identification_category c ON c.identification_category_code = t.identification_category "
				+ "WHERE t.transaction_number = ?"; 
		
		List<String> results = jdbcTemplate.queryForList(sql, String.class, transactionNumber);
		
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public List<AgencyProfile> getAgencyProfiles(Set<String> oris) {
		final String AGENCY_PROFILE_SELECT_BY_ORIS = "SELECT * FROM agency_profile a "
				+ "LEFT JOIN agency_contact_email e ON e.agency_id = a.agency_id "
				+ "WHERE agency_ori in (:oris)";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("oris", oris);
		
		List<AgencyProfile> agencyProfiles = namedParameterJdbcTemplate.query(AGENCY_PROFILE_SELECT_BY_ORIS, parameters, new AgencyProfileResultSetExtractor());
		return agencyProfiles;
	}


}