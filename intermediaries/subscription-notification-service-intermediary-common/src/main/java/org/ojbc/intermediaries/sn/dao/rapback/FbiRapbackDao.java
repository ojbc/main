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
package org.ojbc.intermediaries.sn.dao.rapback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.helper.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	public Integer saveSubsequentResults(final SubsequentResults subsequentResults) {
        log.debug("Inserting row into SUBSEQUENT_RESULTS table : " + subsequentResults.toString());

        final String SUBSEQUENT_RESULTS_INSERT="insert into SUBSEQUENT_RESULTS "
    			+ "(ucn, RAP_SHEET, RESULTS_SENDER_ID) "
    			+ "values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(SUBSEQUENT_RESULTS_INSERT, 
        	                		new String[] {"ucn", "RAP_SHEET", "RESULTS_SENDER_ID" });
        	            ps.setString(1, subsequentResults.getUcn());
						ps.setBlob(2, new SerialBlob(ZipUtils.zip(subsequentResults.getRapSheet())));
        	            ps.setInt(3, subsequentResults.getResultsSender().ordinal()+1);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	
	private final class FbiSubscriptionRowMapper implements RowMapper<FbiRapbackSubscription> {
		
		public FbiRapbackSubscription mapRow(ResultSet rs, int rowNum)
				throws SQLException {
		
			FbiRapbackSubscription fbiSubscription = new FbiRapbackSubscription();
			fbiSubscription.setFbiSubscriptionId(rs.getString("fbi_subscription_id"));
			fbiSubscription.setRapbackCategory(rs.getString("rap_back_category_code"));
			fbiSubscription.setSubscriptionTerm(rs.getString("rap_back_subscription_term_code"));
			fbiSubscription.setRapbackExpirationDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_expiration_date")));
			fbiSubscription.setRapbackStartDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_start_date")));
			fbiSubscription.setRapbackTermDate(OJBCDateUtils.toDateTime(rs.getDate("rap_back_term_date")));
			fbiSubscription.setRapbackOptOutInState(rs.getBoolean("rap_back_opt_out_in_state_indicator"));
			fbiSubscription.setRapbackActivityNotificationFormat(rs.getString("rap_back_activity_notification_format_code"));
			fbiSubscription.setUcn(rs.getString("ucn"));
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
	 */
	public Boolean getfbiSubscriptionQualification(Integer subscriptionId){
		final String sql = "SELECT fbi_subscription_qualification FROM agency_profile "
				+ "WHERE agency_ori = (SELECT owner_ori FROM identification_transaction t WHERE t.subscription_id= ?)";	
		List<Boolean> fbiSubscriptionQualifications = jdbcTemplate.queryForList(sql, Boolean.class, subscriptionId); 
		return DataAccessUtils.singleResult(fbiSubscriptionQualifications);
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
