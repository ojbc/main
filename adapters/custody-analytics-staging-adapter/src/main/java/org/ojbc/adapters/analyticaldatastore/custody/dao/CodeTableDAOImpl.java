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
package org.ojbc.adapters.analyticaldatastore.custody.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.ojbc.adapters.analyticaldatastore.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.custody.dao.model.KeyValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Data access implementations of all database code tables.
 */
@Repository
public class CodeTableDAOImpl implements CodeTableDAO
{

	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public List<KeyValue> retrieveCodeDescriptions(CodeTable codeTable) {
		String sql = null;
		/*
		 * To developers: The KeyValueMapper is based on the query results column index, 
		 * so if the table struture has more columns than just the pkID and *description, 
		 * use a different sql statement in that case. Make sure the first column of the 
		 * query result is pkId and the second is the one to be used to query the pkID.  
		 */
		switch (codeTable) {
		
			case PersonRace:
			case PersonSex:
				sql = "SELECT * FROM " + codeTable.name();
				return jdbcTemplate.query(sql, new KeyValueRowMapper());
			case Agency:				
				sql = "select AgencyID, AgencyName from Agency";	
				return jdbcTemplate.query(sql, new KeyValueRowMapper());
			default:
				return null;
		}
	}

	public class KeyValueRowMapper implements RowMapper<KeyValue> {
		public KeyValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new KeyValue(rs.getInt(1), rs.getString(2));
		}
	}


}
