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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.util.model.TotpUser;
import org.springframework.jdbc.core.RowMapper;

public class TotpUserRowMapper implements RowMapper<TotpUser> {

	@Override
	public TotpUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		TotpUser totpUser = new TotpUser();
		
		totpUser.setId(rs.getInt("ID"));
		totpUser.setValidationCode(rs.getInt("VALIDATION_CODE"));
		totpUser.setUserName(rs.getString("USER_NAME"));
		totpUser.setSecretKey(rs.getString("SECRET_KEY"));
		
		List<Integer> scratchCodesList = Arrays.stream(StringUtils.split(rs.getString("SCRATCH_CODES"), ","))
				.map(Integer::valueOf)
                .collect(Collectors.toList());
				
		totpUser.setScratchCodes(scratchCodesList);
		totpUser.setDateCreated(rs.getTimestamp("DATE_CREATED").toLocalDateTime());
		
		return totpUser;
		
	}
	
}
