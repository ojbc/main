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
package org.ojbc.intermediaries.sn.dao.rapback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.helper.ZipUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class FbiRapbackDao {
	
	private final Log log = LogFactory.getLog(this.getClass());
    
	private final static String FBI_SUBSCRIPTION_SELECT = "SELECT * FROM fbi_rap_back_subscription "
			+ "WHERE rap_back_category_code = ? AND ucn=?;";
		
	// TODO add dbunit test coverage
	private final static String STATE_SUB_COUNT = "select count(sub.id) from identification_transaction idtrx "
		+ "left join identification_subject idsubj on idsubj.subject_id = idtrx.subject_id " 
		+ "inner join subscription sub on sub.id = idtrx.subscription_id " 	
		+ "inner join fbi_rap_back_subscription fbisub on fbisub.ucn = idsubj.ucn " 
		+ "where sub.active = 1" 
		+ "and idsubj.ucn=?" 
		+ "and fbisub.rap_back_category_code=?;";
		
	// TODO add dbunit test coverage
	private final static String STATE_SUBSCRIPTION_QUERY = "select sub.id, sub.enddate, fbisub.ucn, fbisub.rap_back_category_code "
		+ "from identification_transaction idtrx "
		+ "left join identification_subject idsubj on idsubj.subject_id = idtrx.subject_id " 
		+ "inner join subscription sub on sub.id = idtrx.subscription_id " 	
		+ "inner join fbi_rap_back_subscription fbisub on fbisub.ucn = idsubj.ucn " 
		+ "where sub.active = 1 " 
		+ "and idsubj.ucn=? " 
		+ "and fbisub.rap_back_category_code=?;";	
	
	// TODO add dbunit test coverage
	private final static String FBI_UCN_ID_SELECT = "select fbidsub.ucn " +
	  "from fbi_rap_back_subscription fbidsub inner join identification_subject idsub on idsub.ucn = fbidsub.ucn " + 
	  "inner join identification_transaction idtrx on idtrx.subject_id = idsub.subject_id " +
	  "where idtrx.subscription_id=? " +
	  "and fbidsub.rap_back_category_code=?;";
	
	
	private static final Logger logger = Logger.getLogger(FbiRapbackDao.class);
	
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
	public byte[] getCivilFingerPrints(String transactionNumber) {
		final String CIVIL_FINGERPRINT_SELECT="SELECT FINGER_PRINTS_FILE FROM CIVIL_FINGER_PRINTS WHERE TRANSACTION_NUMBER = ? and FINGER_PRINTS_TYPE_ID=2";
		
		byte[] fingerPrintsFileZipped = null;
		
		try {
			fingerPrintsFileZipped = jdbcTemplate.queryForObject(CIVIL_FINGERPRINT_SELECT, byte[].class, transactionNumber);
		} catch (DataAccessException e) {
			log.error("Query returned no results");
		}
		
		byte[] fingerPrintsFileUnzipped = null;
				
		if (fingerPrintsFileZipped != null)
		{	
			fingerPrintsFileUnzipped = ZipUtils.unzip(fingerPrintsFileZipped);
		}	
		
		return fingerPrintsFileUnzipped;
	}    
    
    
    public List<Subscription> getStateSubscriptions(String fbiUcnId, String reasonCode){
    	
    	List<Subscription> subscriptionList = null;
    	
    	try{
    		subscriptionList = jdbcTemplate.query(STATE_SUBSCRIPTION_QUERY, new StateSubscriptionRowMapper(), fbiUcnId, reasonCode);
    		
    	}catch(Exception e){
    		logger.error("Exception occurred querying state subscriptions: " + e.getMessage());
    	}    	
    	return subscriptionList;    	
    }
    
    
    public String getFbiUcnIdFromSubIdAndReasonCode(String subscriptionId, String reasonCode){
    	    	
    	String fbiUcnId = null;
    	
    	try{
    		fbiUcnId = jdbcTemplate.queryForObject(FBI_UCN_ID_SELECT, new Object[]{subscriptionId, reasonCode}, String.class);
    	}catch(Exception e){
    		logger.error("\n\n\n Exception while querying to get ucn: " + e.getMessage() + "\n\n\n");
    	}
    	
    	return fbiUcnId;
    }
    

    public int countStateSubscriptions(String fbiUcnId, String reasonCategoryCode){
    	
    	int stateSubCount = jdbcTemplate.queryForObject(STATE_SUB_COUNT, new Object[] {fbiUcnId, reasonCategoryCode}, Integer.class);
    	
    	logger.info("\n\n\n fbidao, stateSubCount = " + stateSubCount + "\n\n\n");
    	    	
    	return stateSubCount;
    }
        
    
	public FbiRapbackSubscription getFbiRapbackSubscription(String category,
			String ucn) {
		
		logger.info("\n\n\n Using category: " + category + ", and ucn: " + ucn + "\n\n\n");
								
		if ( StringUtils.isEmpty(category) || StringUtils.isEmpty(ucn)){
			throw new IllegalArgumentException("category and ucn cannot be null."); 
		}				
		
		List<FbiRapbackSubscription> fbiSubscriptions = 
				jdbcTemplate.query(FBI_SUBSCRIPTION_SELECT, new FbiSubscriptionRowMapper(), category, ucn);
		
		return DataAccessUtils.singleResult(fbiSubscriptions);
	}
	
	/**
	 * get FBI rapback subscription by subscription ID.
	 * @param subscriptionId
	 * @return
	 */
	public FbiRapbackSubscription getFbiRapbackSubscription(Integer subscriptionId) {
		
		logger.info("\n Using subscription ID: " + subscriptionId + " to find FBI subscription");
		if ( subscriptionId == null || subscriptionId <= 0){
			throw new IllegalArgumentException("Invalid subscription ID."); 
		}
		
		final String sql = "SELECT * FROM fbi_rap_back_subscription "
				+ "WHERE subscription_id = ? "
				+ "ORDER BY report_timestamp DESC;";
		
		
		List<FbiRapbackSubscription> fbiSubscriptions = 
				jdbcTemplate.query(sql, new FbiSubscriptionRowMapper(), subscriptionId);
		
		if (fbiSubscriptions != null && fbiSubscriptions.size() > 1){
			return fbiSubscriptions.get(0);
		}
		
		return DataAccessUtils.singleResult(fbiSubscriptions);
	}
	
	/**
	 * get FBI rapback subscription by subscription ID.
	 * @param subscriptionId
	 * @return
	 */
	public String getUcnByFbiSubscritionId(String fbiSubscriptionId) {
		
		logger.info("\n Using FBI subscription ID: " + fbiSubscriptionId + " to find ucn");
		
		final String sql = "SELECT ucn FROM fbi_rap_back_subscription "
				+ "WHERE FBI_SUBSCRIPTION_ID = ? "
				+ "ORDER BY report_timestamp DESC;";
		
		String ucn = jdbcTemplate.queryForObject(sql, String.class, fbiSubscriptionId);
		
		return ucn;
	}
	
	
	public Integer saveSubsequentResults(final SubsequentResults subsequentResults) {
        log.debug("Inserting row into SUBSEQUENT_RESULTS table : " + subsequentResults.toString());

        final String SUBSEQUENT_RESULTS_INSERT="insert into SUBSEQUENT_RESULTS "
    			+ "(TRANSACTION_NUMBER, UCN, CIVIL_SID, RAP_SHEET, RESULTS_SENDER_ID, NOTIFICATION_INDICATOR) "
    			+ "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSEQUENT_RESULTS_INSERT, 
        	                		new String[] {"SUBSEQUENT_RESULT_ID"});
        	            ps.setString(1, subsequentResults.getTransactionNumber());
        	            ps.setString(2, subsequentResults.getUcn());
        	            ps.setString(3, subsequentResults.getCivilSid());
						ps.setBlob(4, new SerialBlob(ZipUtils.zip(subsequentResults.getRapSheet())));
        	            ps.setInt(5, subsequentResults.getResultsSender().ordinal()+1);
        	            ps.setBoolean(6, subsequentResults.getNotificationIndicator());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	public Integer deleteSubsequentResults(final SubsequentResults subsequentResults) {
		
		Integer rowsDeleted = 0;
		
		final String sql = "SELECT count(*) FROM SUBSEQUENT_RESULTS WHERE NOTIFICATION_INDICATOR = true and  TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID =?";	
		Integer subsequentResultsCount = jdbcTemplate.queryForObject(sql, Integer.class, subsequentResults.getTransactionNumber(), subsequentResults.getResultsSender().ordinal()+1); 
		
		
        log.debug("Total Subsequent Results: " + subsequentResultsCount + ", for transaction number: " + subsequentResults.getTransactionNumber());

        if (subsequentResultsCount != null && subsequentResultsCount > 0)
        {
            String sqlQuery = "DELETE FROM SUBSEQUENT_RESULTS WHERE NOTIFICATION_INDICATOR = true and  TRANSACTION_NUMBER = ? and RESULTS_SENDER_ID =?";

            int rowsUpdated = jdbcTemplate.update(sqlQuery, new Object[] {subsequentResults.getTransactionNumber(), subsequentResults.getResultsSender().ordinal()+1});
            		
            rowsDeleted = rowsUpdated;
        }	
        
        return rowsDeleted;
	}	
	
	private final class FbiSubscriptionRowMapper implements RowMapper<FbiRapbackSubscription> {
		
		public FbiRapbackSubscription mapRow(ResultSet rs, int rowNum)
				throws SQLException {
		
			FbiRapbackSubscription fbiSubscription = new FbiRapbackSubscription();
			fbiSubscription.setFbiSubscriptionId(rs.getString("fbi_subscription_id"));
			fbiSubscription.setRapbackCategory(rs.getString("rap_back_category_code"));
			fbiSubscription.setSubscriptionTerm(rs.getString("rap_back_subscription_term_code"));
			fbiSubscription.setRapbackExpirationDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_expiration_date")));
			fbiSubscription.setRapbackStartDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_start_date")));
			fbiSubscription.setRapbackTermDate(OJBCDateUtils.toLocalDate(rs.getDate("rap_back_term_date")));
			fbiSubscription.setRapbackOptOutInState(rs.getBoolean("rap_back_opt_out_in_state_indicator"));
			fbiSubscription.setRapbackActivityNotificationFormat(rs.getString("rap_back_activity_notification_format_code"));
			fbiSubscription.setUcn(rs.getString("ucn"));
			fbiSubscription.setEventIdentifier(rs.getString("event_identifier"));
			fbiSubscription.setStateSubscriptionId(rs.getInt("subscription_id"));
			fbiSubscription.setTimestamp(OJBCDateUtils.toDateTime(rs.getTimestamp("report_timestamp")));
		
			return fbiSubscription;
		}
	}
	
	
	private final class StateSubscriptionRowMapper implements RowMapper<Subscription>{

		@Override
		public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Subscription stateSubscription = new Subscription();
			
			stateSubscription.setId(-1);
			
			stateSubscription.setSubscriptionCategoryCode(rs.getString("rap_back_category_code"));
						
			Date endDate = rs.getDate("enddate");
			DateTime jtEndDate = new DateTime(endDate);
			
			stateSubscription.setEndDate(jtEndDate);			
			
			return stateSubscription;
		}		
	}		
	

	/**
	 * Takes a civilSid from the arresting notification, check if there is active state subscription(s) with 
	 * the SID. If yes, return the related FBI Id(s);
	 * @param civilSid
	 * @return
	 */
	public List<String> getFbiIds(String civilSid){
		final String FBI_ID_BY_CIVIL_SID = "SELECT s.ucn FROM identification_subject s "
				+ "LEFT JOIN identification_transaction t ON t.subject_id = s.subject_id "
				+ "LEFT JOIN subscription  r on r.id = t.subscription_id "
				+ "WHERE r.active = 1 AND s.civil_sid = ? ;";	
		List<String> fbiSubscriptionIds = jdbcTemplate.queryForList(FBI_ID_BY_CIVIL_SID, String.class, civilSid); 
		return fbiSubscriptionIds;
	}

	/**
	 * Decide whether the owner ORI of the transaction with the transaction number has FBI subscription qualification. 
	 * @param transactionNumber
	 * @return
	 */
	public Boolean getfbiSubscriptionQualification(String transactionNumber){
		final String sql = "SELECT fbi_subscription_qualification FROM agency_profile "
				+ "WHERE agency_ori = (SELECT owner_ori FROM identification_transaction t WHERE t.transaction_number= ?)";	
		List<Boolean> fbiSubscriptionQualifications = jdbcTemplate.queryForList(sql, Boolean.class, transactionNumber); 
		return DataAccessUtils.singleResult(fbiSubscriptionQualifications);
	}
	
	/**
	 * Decide whether the owner ORI of the transaction with the subscription ID has FBI subscription qualification. 
	 * @param subscriptionId
	 * @return
	 * TODO do we need this method anymore? -hw
	 */
	public Boolean getfbiSubscriptionQualification(Integer subscriptionId){
		final String sql = "SELECT fbi_subscription_qualification FROM agency_profile "
				+ "WHERE agency_ori = (SELECT owner_ori FROM identification_transaction t WHERE t.subscription_id= ?)";	
		List<Boolean> fbiSubscriptionQualifications = jdbcTemplate.queryForList(sql, Boolean.class, subscriptionId); 
		return DataAccessUtils.singleResult(fbiSubscriptionQualifications);
	}
	
	/**
	 * Decide whether the owner ORI of the transaction with the subscription ID has FBI subscription qualification. 
	 * @param subscriptionId
	 * @return
	 * TODO do we need this method anymore? -hw
	 */
	public Boolean hasFbiSubscription(Integer subscriptionId){
		final String sql = "SELECT count(*) > 0 FROM fbi_rap_back_subscription t "
				+ "WHERE t.subscription_id= ? ";	
		Boolean hasFbiSubscription = jdbcTemplate.queryForObject(sql, Boolean.class, subscriptionId); 
		return BooleanUtils.isTrue(hasFbiSubscription);
	}
	
	public String getTransactionNumberBySubscriptionId(long stateSubscriptionId) {
		final String sql ="SELECT t.TRANSACTION_NUMBER FROM identification_transaction t "
				+ "WHERE SUBSCRIPTION_ID = ? ";
		
		String transactionNumber = null;
		
		try
		{
			transactionNumber = jdbcTemplate.queryForObject(sql, String.class, stateSubscriptionId);
		}
		catch (Exception ex)
		{
			log.error("Transaction number not found for state subscription id: " + stateSubscriptionId);
		}
		
		return transactionNumber;
	}
	
	public String getTransactionNumberByFbiSubscriptionId(
			String fbiSubscriptionId) {
		final String sql ="SELECT t.TRANSACTION_NUMBER FROM identification_transaction t "
				+ "LEFT JOIN FBI_RAP_BACK_SUBSCRIPTION fs ON fs.SUBSCRIPTION_ID = t.SUBSCRIPTION_ID "
				+ "WHERE FBI_SUBSCRIPTION_ID = ? "; 
		return jdbcTemplate.queryForObject(sql, String.class, fbiSubscriptionId);
	}
	
	public static String getFbiSubscriptionSelect() {
		return FBI_SUBSCRIPTION_SELECT;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


}
