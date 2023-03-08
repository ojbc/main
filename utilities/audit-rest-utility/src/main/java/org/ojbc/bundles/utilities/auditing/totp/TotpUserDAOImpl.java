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
package org.ojbc.bundles.utilities.auditing.totp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.DaoUtils;
import org.ojbc.util.model.TotpUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor=Exception.class)
public class TotpUserDAOImpl implements TotpUserDAO {

	private static final Log log = LogFactory.getLog(TotpUserDAOImpl.class);

	@Resource(name="jdbcTemplateRapback")
	private JdbcTemplate jdbcTemplate;

	@Override
	public Integer saveTotpUser(TotpUser totpUser) {
		log.debug("Inserting row into TOTP user table");
		
        final String totpInsertStatement="INSERT INTO TOTP_USER "
        		+ "(USER_NAME, SECRET_KEY, VALIDATION_CODE, SCRATCH_CODES) values (?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(totpInsertStatement, new String[] {"ID"});
        	            DaoUtils.setPreparedStatementVariable(totpUser.getUserName(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(totpUser.getSecretKey(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(totpUser.getValidationCode(), ps, 3);
        	            DaoUtils.setPreparedStatementVariable(StringUtils.join(totpUser.getScratchCodes(), ","), ps, 4);
        	            return ps;
        	        }
        	    },
        	    keyHolder);
		
        return keyHolder.getKey().intValue();
		
	}

	@Override
	public Integer deleteTotpUserByUserName(String userName) {
		log.info("Deleting TOTP user with user's email of: " + userName);
		
		String sql = "Delete from TOTP_USER where user_name=?";
		
		Integer rowsDeleted = jdbcTemplate.update(sql, new Object[]{userName});	
		
		return rowsDeleted;
	}

	@Override
	public TotpUser getTotpUserByUserName(String userName) {
		log.info("Get TOTP user with user's email of: " + userName);
		String sql = "SELECT * from TOTP_USER where USER_NAME = ?";
		
		TotpUser totpUser = null;
		try {
			totpUser = jdbcTemplate.queryForObject(sql, new TotpUserRowMapper(), userName);
		} catch (DataAccessException e) {
			log.error("No TOTP user entry found (or multiple entries) for user email: " + userName);
		}
		
		return totpUser;	
	}

	@Override
	public List<TotpUser> retrieveAllTotpUsers() {
		String sql = " select * from TOTP_USER order by ID";
		
		List<TotpUser> totpUsers = 
				jdbcTemplate.query(sql, new TotpUserRowMapper());
		
		return totpUsers;	
	}
	
}