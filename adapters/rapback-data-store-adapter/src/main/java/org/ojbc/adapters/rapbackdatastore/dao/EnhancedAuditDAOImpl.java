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
package org.ojbc.adapters.rapbackdatastore.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("enhancedAuditDAO")
public class EnhancedAuditDAOImpl implements EnhancedAuditDAO {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
    
    @Resource(name="enhancedAuditJdbcTemplate")
	private JdbcTemplate enhancedAuditJdbcTemplate;

	@Override
	public String getRapbackActivityNotificationId(Long stateSubscriptionId) {
		final String sql = "SELECT t1.RECORD_RAPBACK_ACTIVITY_NOTIFICATION_ID "
				+ "FROM FEDERAL_RAPBACK_NOTIFICATION t1 "
				+ "WHERE t1.STATE_SUBSCRIPTION_ID = ? AND "
				+ "	t1.NOTIFICATION_RECIEVED_TIMESTAMP = "
				+ "		(SELECT MAX(NOTIFICATION_RECIEVED_TIMESTAMP) FROM FEDERAL_RAPBACK_NOTIFICATION t2 WHERE t2.STATE_SUBSCRIPTION_ID = ? )";
		List<String> results = 
				enhancedAuditJdbcTemplate.queryForList(sql, String.class, stateSubscriptionId, stateSubscriptionId); 
		
		return DataAccessUtils.singleResult(results);
	}
    

}
