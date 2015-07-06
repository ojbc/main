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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonRace;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonSex;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialService;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialServiceParticipation;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class AnalyticalDatastoreDAOImpl implements AnalyticalDatastoreDAO{

	private static final Log log = LogFactory.getLog(AnalyticalDatastoreDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public Integer saveAgency(final Agency agency) {

        log.debug("Inserting row into Agency table");

        final String agencyInsertStatement="INSERT into AGENCY (AgencyName, AgencyORI) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(agencyInsertStatement, new String[] {"AgencyName","AgencyORI"});
        	            ps.setString(1, agency.getAgencyName());
        	            ps.setString(2, agency.getAgencyOri());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer saveIncident(final Incident incident) {
        log.debug("Inserting row into Incident table");

        final String incidentInsertStatement="INSERT into INCIDENT (ReportingAgencyID, IncidentCaseNumber,"
        		+ "IncidentLocationLatitude, IncidentLocationLongitude, IncidentLocationStreetAddress,IncidentLocationTown,IncidentDate,IncidentTime,RecordType) values (?,?,?,?,?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentInsertStatement, new String[] {"ReportingAgencyID", "IncidentCaseNumber"
        	                		+ "IncidentLocationLatitude", "IncidentLocationLongitude","IncidentLocationStreetAddress","IncidentLocationTown","IncidentDate","IncidentTime","RecordType"});
        	            ps.setInt(1, incident.getReportingAgencyID());
        	            ps.setString(2, incident.getIncidentCaseNumber());
        	            ps.setBigDecimal(3, incident.getIncidentLocationLatitude());
        	            ps.setBigDecimal(4, incident.getIncidentLocationLongitude());
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
	public Integer saveCounty(final County county) {

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
	public Integer saveArrest(final Arrest arrest) {
        log.debug("Inserting row into Arrest table");

        final String arrestInsertStatement="INSERT into ARREST ( PersonID,IncidentID,ArrestDate,ArrestTime,ArrestingAgencyName) values (?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(arrestInsertStatement, new String[] {"PersonID","IncidentID","ArrestDate","ArrestTime","ArrestingAgencyName"});
        	            ps.setInt(1, arrest.getPersonID());
        	            ps.setInt(2, arrest.getIncidentID());
        	            ps.setDate(3, new java.sql.Date(arrest.getArrestDate().getTime()));
        	            ps.setTime(4, new java.sql.Time(arrest.getArrestDate().getTime()));
        	            ps.setString(5, arrest.getArrestingAgencyName());

        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public Integer saveAssessedNeed(final AssessedNeed assessedNeed) {

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
	public Integer savePreTrialService(final PretrialService preTrialService) {
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
	public Integer saveDispositionType(final DispositionType dispositionType) {
        log.debug("Inserting row into DispositionType table");

        final String dispositionTypeInsertStatement="INSERT into DispositionType (DispositionDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(dispositionTypeInsertStatement, new String[] {"DispositionDescription","IsConviction"});
        	            ps.setString(1, dispositionType.getDispositionDescription());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer savePersonSex(final PersonSex personSex) {
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
	public Integer savePersonRace(final PersonRace personRace) {
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
	public Integer savePerson(final Person person) {
        log.debug("Inserting row into Person table");

        final String personStatement="INSERT into Person (PersonSexID, PersonRaceID, PersonBirthDate, PersonUniqueIdentifier) values (?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personStatement, new String[] {"PersonSexID", "PersonRaceID", "PersonBirthDate", "PersonUniqueIdentifier"});
        	            
        	            if (person.getPersonSexID() != null)
        	            {	
        	            	ps.setInt(1, person.getPersonSexID());
        	            }
        	            else
        	            {
        	            	ps.setNull(1, java.sql.Types.NULL);
        	            }	
        	            
        	            if (person.getPersonRaceID() != null)
        	            {	
        	            	ps.setInt(2, person.getPersonRaceID());
        	            }
        	            else
        	            {
        	            	ps.setNull(2, java.sql.Types.NULL);
        	            }	

        	            if (person.getPersonBirthDate() != null)
        	            {	
        	            	ps.setDate(3, new java.sql.Date(person.getPersonBirthDate().getTime()));
        	            }
        	            else
        	            {
        	            	ps.setNull(3, java.sql.Types.NULL);
        	            }	
        	            
        	            ps.setString(4, String.valueOf(person.getPersonUniqueIdentifier()));

        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	final String pretrialServiceParticipationStatement="INSERT into PretrialServiceParticipation (PersonID, CountyID,RiskScore,IntakeDate,RecordType,ArrestingAgencyORI,ArrestIncidentCaseNumber) values (?,?,?,?,?,?,?)";
	@Override
	public Integer savePretrialServiceParticipation(
			final PretrialServiceParticipation pretrialServiceParticipation) {

        log.debug("Inserting row into PretrialServiceParticipation table: " + pretrialServiceParticipation);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(pretrialServiceParticipationStatement, new String[] {"PersonID", "CountyID", 
        	                		"RiskScoreID" , "AssessedNeedID","RiskScore", "IntakeDate", "ArrestingAgencyORI","ArrestIncidentCaseNumber","RecordType"});
        	            ps.setInt(1, pretrialServiceParticipation.getPersonID());
        	            ps.setInt(2, pretrialServiceParticipation.getCountyID());
        	            ps.setInt(3, pretrialServiceParticipation.getRiskScore());
        	            
        	            if (pretrialServiceParticipation.getIntakeDate() != null){
        	            	ps.setDate(4, new java.sql.Date(pretrialServiceParticipation.getIntakeDate().getTime()));
        	            }
        	            else{
        	            	ps.setNull(4, java.sql.Types.NULL);
        	            }
        	            
        	            if (pretrialServiceParticipation.getRecordType() != null){
        	            	ps.setString(5, String.valueOf(pretrialServiceParticipation.getRecordType()));
        	            }
        	            else{
        	            	ps.setNull(5, java.sql.Types.NULL);
        	            }
        	            ps.setString(6, pretrialServiceParticipation.getArrestingAgencyORI());
        	            ps.setString(7, pretrialServiceParticipation.getArrestIncidentCaseNumber());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }

	@Override
	public Integer saveCharge(final Charge charge) {
        log.debug("Inserting row into Charge table");

        final String chargeInsertStatement="INSERT into Charge (OffenseDescriptionText,ArrestID) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(chargeInsertStatement, new String[] {"OffenseDescriptionText","ArrestID"});
        	            ps.setString(1, charge.getOffenseDescriptionText());
        	            ps.setInt(2, charge.getArrestID());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public Integer saveDisposition(final Disposition disposition) {
        log.debug("Inserting row into Disposition table");

        final String dispositionInsertStatement="INSERT into Disposition (PersonID,DispositionTypeID,IncidentCaseNumber,DispositionDate,ArrestingAgencyORI,"
        		+ "SentenceTermDays,SentenceFineAmount,InitialChargeCode, FinalChargeCode, RecordType,IsProbationViolation,IsProbationViolationOnOldCharge,RecidivismEligibilityDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(dispositionInsertStatement, new String[] {"PersonID","DispositionTypeID","IncidentCaseNumber","DispositionDate,ArrestingAgencyORI,"
        	                		+ "SentenceTermDays","SentenceFineAmount","InitialChargeCode", "FinalChargeCode","RecordType","IsProbationViolation","IsProbationViolationOnOldCharge","RecidivismEligibilityDate"});
        	            ps.setInt(1, disposition.getPersonID());
        	            ps.setInt(2, disposition.getDispositionTypeID());
        	            ps.setString(3, disposition.getIncidentCaseNumber());
        	            ps.setDate(4, new java.sql.Date(disposition.getDispositionDate().getTime()));
        	            ps.setString(5, disposition.getArrestingAgencyORI());
        	            
        	            if (disposition.getSentenceTermDays() != null)
        	            {	
        	            	ps.setBigDecimal(6, disposition.getSentenceTermDays());
        	            }
        	            else
        	            {
        	            	ps.setNull(6, java.sql.Types.NULL);
        	            }	

        	            if (disposition.getSentenceFineAmount() != null)
        	            {	
        	            	ps.setFloat(7, disposition.getSentenceFineAmount());
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.NULL);
        	            }
        	            
        	            ps.setString(8, disposition.getInitialChargeCode());
        	            ps.setString(9, disposition.getFinalChargeCode());
        	            
        	            ps.setString(10, String.valueOf(disposition.getRecordType()));

        	            if (disposition.getIsProbationViolation() != null)
        	            {	
        	            	ps.setString(11, String.valueOf(disposition.getIsProbationViolation()));
        	            }
        	            else
        	            {
        	            	ps.setNull(11, java.sql.Types.NULL);
        	            }	

        	            if (disposition.getIsProbationViolationOnOldCharge() != null)
        	            {	
        	            	ps.setString(12, String.valueOf(disposition.getIsProbationViolationOnOldCharge()));
        	            }
        	            else
        	            {
        	            	ps.setNull(12, java.sql.Types.NULL);
        	            }	

        	            if (disposition.getRecidivismEligibilityDate() != null)
        	            {	
        	            	ps.setDate(13, new java.sql.Date(disposition.getRecidivismEligibilityDate().getTime()));
        	            }
        	            else
        	            {
        	            	ps.setNull(13, java.sql.Types.NULL);
        	            }	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		}

	@Override
	public List<Incident> searchForIncidentsByIncidentNumber(String incidentNumber) {
		
		String sql = "select * from Incident where IncidentCaseNumber = ?";
		 
		List<Incident> incidents = this.jdbcTemplate.query(sql, new Object[] { incidentNumber }, new IncidentRowMapper());
		
		return incidents;
		
	}


	@Override
	public List<Arrest> searchForArrestsByIncidentPk(Integer incidentPk) {
		String sql = "select * from Arrest where IncidentID = ?";
		 
		List<Arrest> arrests = this.jdbcTemplate.query(sql, new Object[] { incidentPk },new ArrestRowMapper());
		
		return arrests;
	}

	@Override
	public List<Charge> returnChargesFromArrest(Integer arrestId) {
		String sql = "select * from Charge where ArrestID = ?";
		 
		List<Charge> charges = this.jdbcTemplate.query(sql, new Object[] { arrestId },new ChargeRowMapper());
		
		return charges;
	}	

	private final String PRETRIAL_SERVICE_NEED_ASSOCIATION_INSERT=
			"INSERT INTO pretrialServiceNeedAssociation (assessedNeedID, pretrialServiceParticipationID) values (?,?)";

	@Override
	public void savePretrialServiceNeedAssociations(
			final List<Integer> assessedNeedIds, final int pretrialServiceParticipationId) {
        jdbcTemplate.batchUpdate(PRETRIAL_SERVICE_NEED_ASSOCIATION_INSERT, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                    ps.setInt(1, assessedNeedIds.get(i));
                    ps.setLong(2, pretrialServiceParticipationId);
                }
    	            
                public int getBatchSize() {
                    return assessedNeedIds.size();
                }
            });
		
	}
	
	private final String DISPOSITION_BY_INCIDENT_CASE_NUMBER = 
			"SELECT * FROM Disposition WHERE IncidentCaseNumber = ?";
	
	@Override
	public List<Disposition> searchForDispositionsByIncidentCaseNumber(
			String incidentCaseNumber) {
		List<Disposition> dispositions = 
				jdbcTemplate.query(DISPOSITION_BY_INCIDENT_CASE_NUMBER, 
						new DispositionRowMapper(), incidentCaseNumber);
		
		return dispositions;	
	
	}

	private final String PRETRIAL_SERVICE_PARTICIPATION_BY_INCIDENT_NUMBER = 
			"SELECT * FROM PretrialServiceParticipation WHERE ArrestIncidentCaseNumber = ?";
	@Override
	public PretrialServiceParticipation getPretrialServiceParticipationByIncidentNumber(
			String incidentNumber) {
		 
		List<PretrialServiceParticipation> pretrialServiceParticipations = 
				jdbcTemplate.query(PRETRIAL_SERVICE_PARTICIPATION_BY_INCIDENT_NUMBER, 
						new PretrialServiceParticipationRowMapper(), incidentNumber);
		
		return DataAccessUtils.singleResult(pretrialServiceParticipations);
	}

	public class PretrialServiceParticipationRowMapper implements RowMapper<PretrialServiceParticipation>
	{
		@Override
		public PretrialServiceParticipation mapRow(ResultSet rs, int rowNum) throws SQLException {
			PretrialServiceParticipation pretrialServiceParticipation = new PretrialServiceParticipation();
	    	
			pretrialServiceParticipation.setArrestIncidentCaseNumber(rs.getString("ArrestIncidentCaseNumber"));
			pretrialServiceParticipation.setArrestingAgencyORI(rs.getString("arrestingAgencyORI"));
			pretrialServiceParticipation.setCountyID(rs.getInt("CountyID"));
			pretrialServiceParticipation.setIntakeDate(rs.getTimestamp("IntakeDate"));
			pretrialServiceParticipation.setPersonID(rs.getInt("personID"));
			pretrialServiceParticipation.setPretrialServiceParticipationID(rs.getInt("pretrialServiceParticipationID"));
			
			String recordType = rs.getString("recordType"); 
			if (recordType!= null){
				pretrialServiceParticipation.setRecordType(rs.getString("recordType").charAt(0));
			}
			pretrialServiceParticipation.setRiskScore(rs.getInt("riskScore"));
	    	
	    	return pretrialServiceParticipation;
		}

	}

	private final String ASSOCIATED_NEEDS_SELECT = "SELECT a.* FROM PretrialServiceNeedAssociation p "
			+ "LEFT JOIN AssessedNeed a ON a.AssessedNeedID = p.AssessedNeedID "
			+ "WHERE p.PretrialServiceParticipationID = ?"; 
	@Override
	public List<AssessedNeed> getAssociatedNeeds(Integer pretrialServiceParticipationId) {
		List<AssessedNeed> assessedNeeds = 
				jdbcTemplate.query(ASSOCIATED_NEEDS_SELECT, 
						new AssessedNeedRowMapper(), pretrialServiceParticipationId);
		return assessedNeeds;
	}

	public class AssessedNeedRowMapper implements RowMapper<AssessedNeed>
	{
		@Override
		public AssessedNeed mapRow(ResultSet rs, int rowNum) throws SQLException {
			AssessedNeed assessedNeed = new AssessedNeed();
	    	
			assessedNeed.setAssessedNeedID(rs.getInt("AssessedNeedID"));
			assessedNeed.setAssessedNeedDescription(rs.getString("AssessedNeedDescription"));
			
	    	return assessedNeed;
		}

	}

	private final String PERSON_SELECT = "SELECT * FROM Person p "
			+ "LEFT JOIN PersonSex s ON s.PersonSexID = p.PersonSexID "
			+ "LEFT JOIN PersonRace r ON r.PersonRaceID = p.PersonRaceID "
			+ "WHERE p.PersonID = ?"; 
	@Override
	public Person getPerson(Integer personId) {
		List<Person> persons = 
				jdbcTemplate.query(PERSON_SELECT, 
						new PersonRowMapper(), personId);
		return DataAccessUtils.singleResult(persons);
	}

	public class PersonRowMapper implements RowMapper<Person>
	{
		@Override
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person();
	    	
			person.setPersonBirthDate(rs.getDate("PersonBirthDate"));
			person.setPersonID(rs.getInt("PersonID"));
			person.setPersonRaceDescription(rs.getString("PersonRaceDescription"));
			person.setPersonSexDescription(rs.getString("PersonSexDescription"));
			person.setPersonRaceID(rs.getInt("PersonRaceID"));
			person.setPersonSexID(rs.getInt("PersonSexID"));
			person.setPersonUniqueIdentifier(rs.getString("PersonUniqueIdentifier"));
			
	    	return person;
		}

	}

	private final String PRETRIAL_SERVICE_ASSOCIATION_INSERT=
			"INSERT INTO PretrialServiceAssociation (PretrialServiceID, pretrialServiceParticipationID) values (?,?)";
	@Override
	public void savePretrialServiceAssociations(
			final List<Integer> pretrialServiceIds,
			final int pretrialServiceParticipationPkId) {
		
        jdbcTemplate.batchUpdate(PRETRIAL_SERVICE_ASSOCIATION_INSERT, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setInt(1, pretrialServiceIds.get(i));
                ps.setLong(2, pretrialServiceParticipationPkId);
            }
	            
            public int getBatchSize() {
                return pretrialServiceIds.size();
            }
        });
	
		
	}

	private final String ASSOCIATED_PRETRIAL_SERVICE_SELECT = "SELECT ps.* FROM PretrialServiceAssociation p "
			+ "LEFT JOIN PretrialService ps ON ps.PretrialServiceID = p.PretrialServiceID "
			+ "WHERE p.PretrialServiceParticipationID = ?"; 
	@Override
	public List<PretrialService> getAssociatedPretrialServices(
			Integer pretrialServiceParticipationId) {
		List<PretrialService> pretrialServices = 
				jdbcTemplate.query(ASSOCIATED_PRETRIAL_SERVICE_SELECT, 
						new PretrialServiceRowMapper(), pretrialServiceParticipationId);
		return pretrialServices;
	}

	public class PretrialServiceRowMapper implements RowMapper<PretrialService>
	{
		@Override
		public PretrialService mapRow(ResultSet rs, int rowNum) throws SQLException {
			PretrialService pretrialService = new PretrialService();
	    	
			pretrialService.setPretrialServiceID(rs.getInt("PretrialServiceID"));
			pretrialService.setPretrialServiceDescription(rs.getString("PretrialServiceDescription"));
			
	    	return pretrialService;
		}

	}

	@Override
	public Integer searchForAgenyIDbyAgencyORI(String agencyORI) {
			String sql = "select * from Agency where AgencyORI = ?";
			 
			List<Agency> agencies = 
					jdbcTemplate.query(sql, 
							new AgencyRowMapper(), agencyORI);
			
			Agency agency = DataAccessUtils.singleResult(agencies);
			
			if (agency == null)
			{
				return null;
			}	
			
			return agency.getAgencyID();
			
	}
	
	public class AgencyRowMapper implements RowMapper<Agency>
	{
		@Override
		public Agency mapRow(ResultSet rs, int rowNum) throws SQLException {
			Agency agency = new Agency();
	    	
			agency.setAgencyID(rs.getInt("AgencyID"));
			agency.setAgencyName(rs.getString("AgencyName"));
			agency.setAgencyName(rs.getString("AgencyORI"));
			
	    	return agency;
		}

	}

	@Override
	public Integer saveIncidentType(final IncidentType incidentType) {
        log.debug("Inserting row into IncidentType table");

        final String incidentTypeInsertStatement="INSERT into IncidentType (IncidentDescriptionText,IncidentID) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentTypeInsertStatement, new String[] {"IncidentDescriptionText","IncidentID"});
        	            ps.setString(1, incidentType.getIncidentDescriptionText());
        	            ps.setInt(2, incidentType.getIncidentID());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public Integer saveIncidentCircumstance(final IncidentCircumstance incidentCircumstance) {
        log.debug("Inserting row into IncidentCircumstance table");

        final String incidentCircumstanceInsertStatement="INSERT into IncidentCircumstance (IncidentCircumstanceText,IncidentID) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentCircumstanceInsertStatement, new String[] {"IncidentCircumstanceText","IncidentID"});
        	            ps.setString(1, incidentCircumstance.getIncidentCircumstanceText());
        	            ps.setInt(2, incidentCircumstance.getIncidentID());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();			
   }

	@Override
	public List<IncidentCircumstance> returnCircumstancesFromIncident(
			Integer incidentPk) {
		String sql = "select * from IncidentCircumstance where IncidentID = ?";
		 
		List<IncidentCircumstance> circumstances = this.jdbcTemplate.query(sql, new Object[] { incidentPk },new IncidentCircumstanceRowMapper());
		
		return circumstances;
	}

	public class IncidentCircumstanceRowMapper implements RowMapper<IncidentCircumstance>
	{
		@Override
		public IncidentCircumstance mapRow(ResultSet rs, int rowNum) throws SQLException {
			IncidentCircumstance incidentCircumstance = new IncidentCircumstance();
	    	
			incidentCircumstance.setIncidentCircumstanceID(rs.getInt("IncidentCircumstanceID"));
			incidentCircumstance.setIncidentID(rs.getInt("IncidentID"));
			incidentCircumstance.setIncidentCircumstanceText(rs.getString("IncidentCircumstanceText"));
			
	    	return incidentCircumstance;
		}

	}

	@Override
	public List<IncidentType> returnIncidentDescriptionsFromIncident(
			Integer incidentPk) {
		String sql = "select * from IncidentType where IncidentID = ?";
		 
		List<IncidentType> incidentTypes = this.jdbcTemplate.query(sql, new Object[] { incidentPk },new IncidentTypeRowMapper());
		
		return incidentTypes;
	}
	
	public class IncidentTypeRowMapper implements RowMapper<IncidentType>
	{
		@Override
		public IncidentType mapRow(ResultSet rs, int rowNum) throws SQLException {
			IncidentType incidentType = new IncidentType();
	    	
			incidentType.setIncidentID(rs.getInt("IncidentID"));
			incidentType.setIncidentDescriptionText(rs.getString("IncidentDescriptionText"));
			incidentType.setIncidentTypeID(rs.getInt("IncidentTypeID"));
			
	    	return incidentType;
		}

	}
}
