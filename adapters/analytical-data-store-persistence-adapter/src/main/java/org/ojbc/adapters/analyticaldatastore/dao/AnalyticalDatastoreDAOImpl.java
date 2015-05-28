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
package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.model.Agency;
import org.ojbc.adapters.analyticaldatastore.dao.model.County;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.dao.model.IncidentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class AnalyticalDatastoreDAOImpl implements AnalyticalDatastoreDAO{

	private static final Log log = LogFactory.getLog(AnalyticalDatastoreDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public int saveAgency(final Agency agency) {

        log.debug("Inserting row into subscription table");

        final String agencyInsertStatement="INSERT into AGENCY (AgencyName) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(agencyInsertStatement, new String[] {"AgencyName"});
        	            ps.setString(1, agency.getAgencyName());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int saveIncidentType(final IncidentType incidentType) {
		
        log.debug("Inserting row into Incident Type table");

        final String incidentTypeInsertStatement="INSERT into INCIDENTTYPE (IncidentTypeDescription) values (?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentTypeInsertStatement, new String[] {"IncidentTypeDescription"});
        	            ps.setString(1, incidentType.getIncidentTypeDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }

	@Override
	public int saveIncident(final Incident incident) {
        log.debug("Inserting row into Incident table");

        //TODO: map latitute and longitude
        
        final String incidentInsertStatement="INSERT into INCIDENT (ReportingAgencyID, IncidentCaseNumber, IncidentTypeID, CountyID,"
        		+ "IncidentLocationStreetAddress,IncidentLocationTown,IncidentDate,IncidentTime,RecordType) values (?,?,?,?,?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentInsertStatement, new String[] {"ReportingAgencyID", "IncidentCaseNumber", "IncidentTypeID", "CountyID,"
        	                		+ "IncidentLocationStreetAddress","IncidentLocationTown","IncidentDate","IncidentTime","RecordType"});
        	            ps.setInt(1, incident.getReportingAgencyID());
        	            ps.setString(2, incident.getIncidentCaseNumber());
        	            ps.setInt(3, incident.getIncidentTypeID());
        	            ps.setInt(4, incident.getCountyID());
        	            ps.setString(5, incident.getIncidentLocationStreetAddress());
        	            ps.setString(6, incident.getIncidentLocationTown());
        	            ps.setDate(7, new java.sql.Date(incident.getIncidentDate().getTime()));
        	            ps.setTime(8, new java.sql.Time(incident.getIncidentDate().getTime()));
        	            ps.setString(9, String.valueOf(incident.getRecordType()));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}

	@Override
	public int saveCounty(final County county) {

        log.debug("Inserting row into county table");

        final String countyInsertStatement="INSERT into COUNTY (CountyName) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(countyInsertStatement, new String[] {"CountyName"});
        	            ps.setString(1, county.getCountyName());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
}
