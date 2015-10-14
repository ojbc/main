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
package org.ojbc.intermediaries.sn.fbi.rapback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class FbiRapbackDao {
	
	private final static String FBI_SUBSCRIPTION_SELECT = "SELECT * FROM fbi_rap_back_subscription "
			+ "WHERE rap_back_category_code = ? AND ucn=?;";
		
	private final static String STATE_SUB_COUNT = "select count(subscription_id) from identification_transaction i "
			+ "left join identification_subject s on s.subject_id = i.subject_id where s.ucn =?;";	
	
	private final static String FBI_UCN_ID_SELECT = "select frs.ucn from " 
	 + "criminal_fbi_subscription_record cfc inner join identification_transaction it on cfc.subscription_id = it.subscription_id " 
	 + "inner join fbi_rap_back_subscription frs on frs.fbi_subscription_id = cfc.fbi_subscription_id "
	 + "where it.subscription_id=?"
	 + "and frs.rap_back_category_code=?;";
	

	private static final Logger logger = Logger.getLogger(FbiRapbackDao.class);
	
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
        
    public String getFbiUcnIdFromSubIdAndReasonCode(int subscriptionId, String reasonCode){
    	    	
    	String fbiUcnId = jdbcTemplate.queryForObject(FBI_UCN_ID_SELECT, new Object[]{subscriptionId, reasonCode}, String.class);
    	
    	return fbiUcnId;
    }
    
    public int countStateSubscriptionsHavingFbiUcnId(String fbiUcnId){
    	
    	int stateSubCount = jdbcTemplate.queryForObject(STATE_SUB_COUNT, new Object[] {fbiUcnId}, Integer.class);
    	
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
	
	
	private final class FbiSubscriptionRowMapper implements RowMapper<FbiRapbackSubscription> {
		
		public FbiRapbackSubscription mapRow(ResultSet rs, int rowNum)
				throws SQLException {
		
			FbiRapbackSubscription fbiSubscription = new FbiRapbackSubscription();
			fbiSubscription.setFbiSubscriptionId(rs.getString("fbi_subscription_id"));
			fbiSubscription.setRapbackCategory(rs.getString("rap_back_category_code"));
			fbiSubscription.setSubscriptionTerm(rs.getString("rap_back_subscription_term_code"));
			fbiSubscription.setRapbackExpirationDate(toDateTime(rs.getDate("rap_back_expiration_date")));
			fbiSubscription.setRapbackStartDate(toDateTime(rs.getDate("rap_back_start_date")));
			fbiSubscription.setRapbackTermDate(toDateTime(rs.getDate("rap_back_term_date")));
			fbiSubscription.setRapbackOptOutInState(rs.getBoolean("rap_back_opt_out_in_state_indicator"));
			fbiSubscription.setRapbackActivityNotificationFormat(rs.getString("rap_back_activity_notification_format_code"));
			fbiSubscription.setUcn(rs.getString("ucn"));
			fbiSubscription.setTimestamp(toDateTime(rs.getTimestamp("timestamp")));
		
			return fbiSubscription;
		}
	}
	
	private DateTime toDateTime(Date date){
		return date == null? null : new DateTime(date); 
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
