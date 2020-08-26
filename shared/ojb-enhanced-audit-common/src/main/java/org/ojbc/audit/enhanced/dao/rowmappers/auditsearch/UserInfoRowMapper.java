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
package org.ojbc.audit.enhanced.dao.rowmappers.auditsearch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

public class UserInfoRowMapper implements RowMapper<UserInfo> {
	public UserInfo mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		UserInfo userInfo = buildUserInfo(rs);
		return userInfo;
	}

	public UserInfo buildUserInfo(
			ResultSet rs) throws SQLException{

		UserInfo userInfo = new UserInfo();
		
		userInfo.setUserInfoId(rs.getInt("USER_INFO_ID"));
		userInfo.setEmployerName(rs.getString("EMPLOYER_NAME"));
		userInfo.setEmployerOri(rs.getString("EMPLOYER_ORI"));
		userInfo.setUserFirstName(rs.getString("USER_FIRST_NAME"));
		userInfo.setIdentityProviderId(rs.getString("IDENTITY_PROVIDER_ID"));
		userInfo.setUserEmailAddress(rs.getString("USER_EMAIL_ADDRESS"));
		userInfo.setUserLastName(rs.getString("USER_LAST_NAME"));
		userInfo.setEmployerSubunitName(rs.getString("EMPLOYER_SUBUNIT_NAME"));
		userInfo.setFederationId(rs.getString("FEDERATION_ID"));
		
		return userInfo;
	}
}	