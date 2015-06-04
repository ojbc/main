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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.model.Agency;
import org.ojbc.adapters.analyticaldatastore.dao.model.Arrest;
import org.ojbc.adapters.analyticaldatastore.dao.model.AssessedNeed;
import org.ojbc.adapters.analyticaldatastore.dao.model.Charge;
import org.ojbc.adapters.analyticaldatastore.dao.model.County;
import org.ojbc.adapters.analyticaldatastore.dao.model.Disposition;
import org.ojbc.adapters.analyticaldatastore.dao.model.DispositionType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.dao.model.IncidentType;
import org.ojbc.adapters.analyticaldatastore.dao.model.OffenseType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonRace;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonSex;
import org.ojbc.adapters.analyticaldatastore.dao.model.PreTrialService;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialServiceParticipation;
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

        final String incidentInsertStatement="INSERT into INCIDENT (ReportingAgencyID, IncidentCaseNumber, IncidentTypeID,"
        		+ "IncidentLocationLatitude, IncidentLocationLongitude, IncidentLocationStreetAddress,IncidentLocationTown,IncidentDate,IncidentTime,RecordType) values (?,?,?,?,?,?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentInsertStatement, new String[] {"ReportingAgencyID", "IncidentCaseNumber", "IncidentTypeID"
        	                		+ "IncidentLocationLatitude", "IncidentLocationLongitude","IncidentLocationStreetAddress","IncidentLocationTown","IncidentDate","IncidentTime","RecordType"});
        	            ps.setInt(1, incident.getReportingAgencyID());
        	            ps.setString(2, incident.getIncidentCaseNumber());
        	            ps.setInt(3, incident.getIncidentTypeID());
        	            
        	            //TODO: fix this to use actual lat and long
        	            ps.setBigDecimal(4, new BigDecimal("78.834163"));
        	            ps.setBigDecimal(5, new BigDecimal("107.774506"));
        	            ps.setString(6, incident.getIncidentLocationStreetAddress());
        	            ps.setString(7, incident.getIncidentLocationTown());
        	            ps.setDate(8, new java.sql.Date(incident.getIncidentDate().getTime()));
        	            ps.setTime(9, new java.sql.Time(incident.getIncidentDate().getTime()));
        	            ps.setString(10, String.valueOf(incident.getRecordType()));
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
	
	@Override
	public int saveArrest(final Arrest arrest) {
        log.debug("Inserting row into Arrest table");

        final String arrestInsertStatement="INSERT into ARREST ( PersonID,IncidentID,ArrestingAgencyID,ArrestDate,ArrestTime,ArrestDrugRelated ) values (?,?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(arrestInsertStatement, new String[] {"PersonID","IncidentID","ArrestingAgencyID","ArrestDate","ArrestTime","ArrestDrugRelated"});
        	            ps.setInt(1, arrest.getPersonID());
        	            ps.setInt(2, arrest.getIncidentID());
        	            ps.setInt(3, arrest.getArrestingAgencyID());
        	            ps.setDate(4, new java.sql.Date(arrest.getArrestDate().getTime()));
        	            ps.setTime(5, new java.sql.Time(arrest.getArrestDate().getTime()));
        	            ps.setString(6, String.valueOf(arrest.getArrestDrugRelated()));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public int saveAssessedNeed(final AssessedNeed assessedNeed) {

        log.debug("Inserting row into Assessed Need table");

        final String assessedNeedInsertStatement="INSERT into AssessedNeed (AssessedNeedDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(assessedNeedInsertStatement, new String[] {"AssessedNeedDescription"});
        	            ps.setString(1, assessedNeed.getAssessedNeedDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int savePreTrialService(final PreTrialService preTrialService) {
        log.debug("Inserting row into PreTrialService table");

        final String pretrialServiceInsertStatement="INSERT into PreTrialService (PretrialServiceDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(pretrialServiceInsertStatement, new String[] {"PretrialServiceDescription"});
        	            ps.setString(1, preTrialService.getPretrialServiceDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int saveDispositionType(final DispositionType dispositionType) {
        log.debug("Inserting row into DispositionType table");

        final String dispositionTypeInsertStatement="INSERT into DispositionType (DispositionDescription,IsConviction) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(dispositionTypeInsertStatement, new String[] {"DispositionDescription","IsConviction"});
        	            ps.setString(1, dispositionType.getDispositionDescription());
        	            ps.setString(2, String.valueOf(dispositionType.getIsConviction()));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int savePersonSex(final PersonSex personSex) {
        log.debug("Inserting row into PersonSex table");

        final String personSexInsertStatement="INSERT into PersonSex (PersonSexDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personSexInsertStatement, new String[] {"PersonSexDescription"});
        	            ps.setString(1, personSex.getPersonSexDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}
	
	@Override
	public int savePersonRace(final PersonRace personRace) {
        log.debug("Inserting row into PersonRace table");

        final String personRaceInsertStatement="INSERT into PersonRace (PersonRaceDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personRaceInsertStatement, new String[] {"PersonRaceDescription"});
        	            ps.setString(1, personRace.getPersonRaceDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int savePerson(final Person person) {
        log.debug("Inserting row into Person table");

        final String personStatement="INSERT into Person (PersonSexID, PersonRaceID, PersonBirthDate, PersonUniqueIdentifier) values (?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personStatement, new String[] {"PersonSexID", "PersonRaceID", "PersonBirthDate", "PersonUniqueIdentifier"});
        	            ps.setInt(1, person.getPersonSexID());
        	            ps.setInt(2, person.getPersonRaceID());
        	            ps.setDate(3, new java.sql.Date(person.getPersonBirthDate().getTime()));
        	            ps.setString(4, String.valueOf(person.getPersonUniqueIdentifier()));

        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int savePretrialServiceParticipation(
			final PretrialServiceParticipation pretrialServiceParticipation) {

        log.debug("Inserting row into PretrialServiceParticipation table");

        final String pretrialServiceParticipationStatement="INSERT into PretrialServiceParticipation (PretrialServiceCaseNumber, PersonID, CountyID,RiskScore,IntakeDate,RecordType) values (?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(pretrialServiceParticipationStatement, new String[] {"PretrialServiceCaseNumber",  "PersonID", "CountyID", 
        	                		"RiskScoreID" , "AssessedNeedID","RiskScore", "IntakeDate", "RecordType"});
        	            ps.setString(1, pretrialServiceParticipation.getPretrialServiceCaseNumber());
        	            ps.setInt(2, pretrialServiceParticipation.getPersonID());
        	            ps.setInt(3, pretrialServiceParticipation.getCountyID());
        	            ps.setInt(4, pretrialServiceParticipation.getRiskScore());
        	            ps.setDate(5, new java.sql.Date(pretrialServiceParticipation.getIntakeDate().getTime()));
        	            ps.setString(6, String.valueOf(pretrialServiceParticipation.getRecordType()));

        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }

	@Override
	public int saveOffenseType(final OffenseType offenseType) {
        log.debug("Inserting row into OffenseType table");

        final String offenseTypeStatement="INSERT into OffenseType (OffenseDescription,IsDrugOffense,OffenseSeverity) values (?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(offenseTypeStatement, new String[] {"OffenseDescription","IsDrugOffense","OffenseSeverity"});
        	            ps.setString(1, offenseType.getOffenseDescription());
        	            ps.setString(2, offenseType.getIsDrugOffense());
        	            ps.setString(3, offenseType.getOffenseSeverity());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
     }

	@Override
	public int saveCharge(final Charge charge) {
        log.debug("Inserting row into Charge table");

        final String chargeInsertStatement="INSERT into Charge (arrestOffenseTypeID,arrestID) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(chargeInsertStatement, new String[] {"ArrestOffenseTypeID","ArrestID"});
        	            ps.setInt(1, charge.getArrestOffenseTypeID());
        	            ps.setInt(2, charge.getArrestID());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public int saveDisposition(final Disposition disposition) {
        log.debug("Inserting row into Disposition table");

        final String dispositionInsertStatement="INSERT into Disposition (PersonID,DispositionTypeID,OffenseTypeID,IncidentCaseNumber,DispositionDate,"
        		+ "SentenceTermDays,SentenceFineAmount,RecordType,IsProbationViolation,RecidivismEligibilityDate) values (?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(dispositionInsertStatement, new String[] {"PersonID","DispositionTypeID","OffenseTypeID","IncidentCaseNumber","DispositionDate,"
        	                		+ "SentenceTermDays","SentenceFineAmount","RecordType","IsProbationViolation","RecidivismEligibilityDate"});
        	            ps.setInt(1, disposition.getPersonID());
        	            ps.setInt(2, disposition.getDispositionTypeID());
        	            ps.setInt(3, disposition.getOffenseTypeID());
        	            ps.setString(4, disposition.getIncidentCaseNumber());
        	            ps.setDate(5, new java.sql.Date(disposition.getDispositionDate().getTime()));
        	            ps.setInt(6, disposition.getSentenceTermDays());
        	            ps.setFloat(7, disposition.getSentenceFineAmount());
        	            ps.setString(8, String.valueOf(disposition.getRecordType()));
        	            ps.setString(9, String.valueOf(disposition.getIsProbationViolation()));
        	            ps.setDate(10, new java.sql.Date(disposition.getRecidivismEligibilityDate().getTime()));
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		}

	@Override
	public List<Incident> searchForIncidentsByIncidentNumber(String incidentNumber) {
		
		String sql = "select * from Incident where IncidentCaseNumber = ?";
		 
		List<Incident> incidents = this.jdbcTemplate.query(sql, new Object[] { incidentNumber },new IncidentRowMapper());
		
		return incidents;
		
	}

	@Override
	public int returnPersonSexKeyfromSexDescription(String personSexDescription){
		String sql = "select PersonSexID from PersonSex where PersonSexDescription = ?";
		 
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql,new Object[] { personSexDescription });
		
		if (rows.size() != 1)
		{
			log.info("Person Sex Query did not return the proper resultset.");
			
			//TODO: maybe query for 'unknown' once and then store it in a member variable instead of hardcoding?
			//FIXME
			return 3;
		}	
		
		Long personSexKey = (Long)rows.get(0).get("PersonSexID");
		
		return personSexKey.intValue();
	}
	
	@Override
	public int returnPersonRaceKeyfromRaceDescription(String personRaceDescription){
		String sql = "select PersonRaceID from PersonRace where PersonRaceDescription = ?";
		 
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql,new Object[] { personRaceDescription });
		
		if (rows.size() != 1)
		{
			log.info("Person Race Query did not return the proper resultset.");
			
			//TODO: maybe query for 'unknown' once and then store it in a member variable instead of hardcoding?
			
			return 4;
		}	
		
		Long personRaceKey = (Long)rows.get(0).get("PersonRaceID");
		
		return personRaceKey.intValue();
	}

	@Override
	public List<Arrest> searchForArrestsByIncidentPk(int incidentPk) {
		String sql = "select * from Arrest where IncidentID = ?";
		 
		List<Arrest> arrests = this.jdbcTemplate.query(sql, new Object[] { incidentPk },new ArrestRowMapper());
		
		return arrests;
	}

	@Override
	public int returnAgencyKeyfromAgencyName(String agencyName) {
		String sql = "select AgencyID from Agency where AgencyName = ?";
		 
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql,new Object[] { agencyName });
		
		if (rows.size() != 1)
		{
			log.info("Agency did not return the proper resultset.");
			
			//TODO: maybe we should have a code for 'unknown' or 'unmapped' agency
			return 0;
		}	
		
		Long agencyIdKey = (Long)rows.get(0).get("AgencyID");
		
		return agencyIdKey.intValue();	
	}	
	
}
