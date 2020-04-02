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
package org.ojbc.warrant.repository.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.ojbc.warrant.repository.model.ESupplemental;
import org.springframework.jdbc.core.RowMapper;

public class ESupplementalRowMapper implements RowMapper<ESupplemental> {

	@Override
	public ESupplemental mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ESupplemental eSupplemental = new ESupplemental();
		
		eSupplemental.setIdentifier(rs.getInt("pkid"));
		eSupplemental.setSupplementalType(rs.getString("supplementalType"));

		if (hasColumn(rs, "supplementalValue"))
		{
			eSupplemental.setSupplementalValue(rs.getString("supplementalValue"));
		}
			
		if (hasColumn(rs, "operatorLicenseState"))
		{
			eSupplemental.setOperatorLicenseState(rs.getString("operatorLicenseState"));
		}	

		if (hasColumn(rs, "firstName"))
		{
			eSupplemental.setFirstName(rs.getString("firstName"));
		}	

		if (hasColumn(rs, "middleName"))
		{
			eSupplemental.setMiddleName(rs.getString("middleName"));
		}	

		if (hasColumn(rs, "lastName"))
		{
			eSupplemental.setLastName(rs.getString("lastName"));
		}	

		if (hasColumn(rs, "nameSuffix"))
		{
			eSupplemental.setNameSuffix(rs.getString("nameSuffix"));
		}	

		if (hasColumn(rs, "fullPersonName"))
		{
			eSupplemental.setFullPersonName(rs.getString("fullPersonName"));
		}	

		return eSupplemental;
	}


	public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.equals(rsmd.getColumnName(x))) {
	            return true;
	        }
	        if (columnName.toLowerCase().equals(rsmd.getColumnLabel(x).toLowerCase())) {
	            return true;
	        }
	    }
	    return false;
	}
}
