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
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository("rapbackDAO")
public class RapbackDAOImpl implements RapbackDAO {
	
	private final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public Iterable<Object> getRapbackReports(String federationId,
			String employerOri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	final static String SUBJECT_INSERT="INSERT into FBI_RAP_BACK_SUBJECT (UCN, CRIMINAL_SID, CIVIL_SID, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL, DOB) values (?, ?, ?, ?, ?, ?, ?)";
	@Override
	public Integer saveSubject(final Subject subject) {
        log.debug("Inserting row into FBI_RAP_BACK_SUBJECT table : " + subject);

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
        	            ps.setDate(7, new java.sql.Date(subject.getDob().getMillis()));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	final static String SUBJECT_SELECT="SELECT * FROM FBI_RAP_BACK_SUBJECT WHERE SUBJECT_ID = ?";
	@Override
	public Subject getSubject(Integer id) {
		List<Subject> subjects = jdbcTemplate.query(SUBJECT_SELECT, new SubjectRowMapper(), id);
		return DataAccessUtils.singleResult(subjects);
	} 
	
	private final class SubjectRowMapper implements RowMapper<Subject> {
		public Subject mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Subject subject = new Subject();
	
	    subject.setSubjectId(rs.getInt("subject_id"));
	    subject.setUcn(rs.getString("ucn"));
	    subject.setCriminalSid(rs.getString("CRIMINAL_SID"));
	    subject.setCivilSid(rs.getString("CIVIL_SID"));
	    subject.setFirstName(rs.getString("FIRST_NAME")); 
	    subject.setLastName(rs.getString("LAST_NAME")); 
	    subject.setMiddleInitial(rs.getString("MIDDLE_INITIAL")); 
	    subject.setDob(toDateTime(rs.getDate("DOB")));
	
	    return subject;
		}
	}

	private DateTime toDateTime(Date date){
		return date == null? null : new DateTime(date); 
	}

}
