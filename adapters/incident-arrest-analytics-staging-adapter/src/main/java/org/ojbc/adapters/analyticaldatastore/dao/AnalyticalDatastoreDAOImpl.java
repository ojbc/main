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
package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

public class AnalyticalDatastoreDAOImpl implements AnalyticalDatastoreDAO{

	private static final Log log = LogFactory.getLog(AnalyticalDatastoreDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;  
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
	public Integer saveIncident(final Incident inboundIncident) {
        log.debug("Inserting row into Incident table: " + inboundIncident.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String incidentInsertStatement="";
        	        	String[] insertArgs = null;	
        	        	
        	            if (inboundIncident.getIncidentID() == null){	
        	            	incidentInsertStatement="INSERT into INCIDENT (ReportingAgencyID, IncidentCaseNumber,"
        	            			+ "IncidentLocationLatitude, IncidentLocationLongitude, IncidentLocationStreetAddress,IncidentLocationTown,IncidentDate,IncidentTime,ReportingSystem,RecordType) values (?,?,?,?,?,?,?,?,?,?)";
        	            	
            	        	insertArgs = new String[] {"ReportingAgencyID", "IncidentCaseNumber"
        	                		+ "IncidentLocationLatitude", "IncidentLocationLongitude","IncidentLocationStreetAddress","IncidentLocationTown","IncidentDate","IncidentTime","ReportingSystem","RecordType"};	
        	            }	
        	            else{
        	            	incidentInsertStatement="INSERT into INCIDENT (ReportingAgencyID, IncidentCaseNumber,"
        	            			+ "IncidentLocationLatitude, IncidentLocationLongitude, IncidentLocationStreetAddress,IncidentLocationTown,IncidentDate,IncidentTime,ReportingSystem,RecordType, IncidentID) values (?,?,?,?,?,?,?,?,?,?,?)";
        	            	
            	        	insertArgs = new String[] {"ReportingAgencyID", "IncidentCaseNumber"
        	                		+ "IncidentLocationLatitude", "IncidentLocationLongitude","IncidentLocationStreetAddress","IncidentLocationTown","IncidentDate","IncidentTime","ReportingSystem","RecordType", "IncidentID"};	
        	            }	
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(incidentInsertStatement, insertArgs);
        	            if (inboundIncident.getReportingAgencyID() != null){
        	            	ps.setInt(1, inboundIncident.getReportingAgencyID());
        	            }
        	            else
        	            {
        	            	ps.setNull(1, java.sql.Types.NULL);
        	            }	

        	            ps.setString(2, inboundIncident.getIncidentCaseNumber());
        	            ps.setBigDecimal(3, inboundIncident.getIncidentLocationLatitude());
        	            ps.setBigDecimal(4, inboundIncident.getIncidentLocationLongitude());
        	            ps.setString(5, inboundIncident.getIncidentLocationStreetAddress());
        	            ps.setString(6, inboundIncident.getIncidentLocationTown());
        	            ps.setDate(7, new java.sql.Date(inboundIncident.getIncidentDate().getTime()));
        	            ps.setTime(8, new java.sql.Time(inboundIncident.getIncidentDate().getTime()));
        	            ps.setString(9, inboundIncident.getReportingSystem());
        	            ps.setString(10, String.valueOf(inboundIncident.getRecordType()));
        	            
        	            if (inboundIncident.getIncidentID() != null)
        	            {	
        	            	ps.setInt(11, inboundIncident.getIncidentID());
        	            }	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);
        
         Integer returnValue = null;
         
         if (inboundIncident.getIncidentID() != null)
         {
        	 returnValue = inboundIncident.getIncidentID();
         }	 
         else
         {
        	 returnValue = keyHolder.getKey().intValue();
         }	 
         
         return returnValue;	
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

        final String arrestInsertStatement="INSERT into ARREST ( PersonID,IncidentID,ArrestDate,ArrestTime,ArrestingAgencyName, ReportingSystem) values (?,?,?,?,?,?)";
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(arrestInsertStatement, new String[] {"PersonID","IncidentID","ArrestDate","ArrestTime","ArrestingAgencyName","ReportingSystem"});
        	            ps.setInt(1, arrest.getPersonID());
        	            ps.setInt(2, arrest.getIncidentID());
        	            ps.setDate(3, new java.sql.Date(arrest.getArrestDate().getTime()));
        	            ps.setTime(4, new java.sql.Time(arrest.getArrestDate().getTime()));
        	            ps.setString(5, arrest.getArrestingAgencyName());
        	            ps.setString(6, arrest.getReportingSystem());

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

	@Override
	public Integer savePretrialServiceParticipation(
			final PretrialServiceParticipation pretrialServiceParticipation) {

        log.debug("Inserting row into PretrialServiceParticipation table: " + pretrialServiceParticipation.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String pretrialInsertStatement="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (pretrialServiceParticipation.getPretrialServiceParticipationID() != null)
        	        	{
        	        		insertArgs = new String[] {"PersonID", "CountyID", 
        	                		"RiskScoreID" , "AssessedNeedID","RiskScore", "IntakeDate", "ArrestingAgencyORI","ArrestIncidentCaseNumber","RecordType","PretrialServiceUniqueID","PretrialServiceParticipationID"};

        	        		pretrialInsertStatement="INSERT into PretrialServiceParticipation (PersonID, CountyID,RiskScore,IntakeDate,RecordType,ArrestingAgencyORI,ArrestIncidentCaseNumber,PretrialServiceUniqueID,PretrialServiceParticipationID) values (?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else
        	        	{
        	        		insertArgs = new String[] {"PersonID", "CountyID", 
        	                		"RiskScoreID" , "AssessedNeedID","RiskScore", "IntakeDate", "ArrestingAgencyORI","ArrestIncidentCaseNumber","RecordType","PretrialServiceUniqueID"};

        	        		pretrialInsertStatement="INSERT into PretrialServiceParticipation (PersonID, CountyID,RiskScore,IntakeDate,RecordType,ArrestingAgencyORI,ArrestIncidentCaseNumber,PretrialServiceUniqueID) values (?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(pretrialInsertStatement, insertArgs);
        	            ps.setInt(1, pretrialServiceParticipation.getPersonID());
        	            
        	            if (pretrialServiceParticipation.getCountyID() != null){
        	            	ps.setInt(2, pretrialServiceParticipation.getCountyID());
        	            }
        	            else{
        	            	ps.setNull(2, java.sql.Types.NULL);
        	            }
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
        	            ps.setString(8, pretrialServiceParticipation.getPretrialServiceUniqueID());
        	            
        	        	if (pretrialServiceParticipation.getPretrialServiceParticipationID() != null)
        	        	{
        	        		ps.setInt(9, pretrialServiceParticipation.getPretrialServiceParticipationID());
        	        	}	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

		        Integer returnValue = null;
		        
		        if (pretrialServiceParticipation.getPretrialServiceParticipationID() != null)
		        {
		       	 	returnValue = pretrialServiceParticipation.getPretrialServiceParticipationID();
		        }	 
		        else
		        {
		       	 	returnValue = keyHolder.getKey().intValue();
		        }	 
		        
		        return returnValue;	

    }

	@Override
	public Integer saveCharge(final Charge charge) {
        log.debug("Inserting row into Charge table");

        final String chargeInsertStatement="INSERT into Charge (OffenseDescriptionText,OffenseDescriptionText1,ArrestID) values (?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(chargeInsertStatement, new String[] {"OffenseDescriptionText","OffenseDescriptionText1","ArrestID"});
        	            ps.setString(1, charge.getOffenseDescriptionText());
        	            ps.setString(2, charge.getOffenseDescriptionText1());
        	            ps.setInt(3, charge.getArrestID());
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
    }

	@Override
	public Integer saveDisposition(final Disposition inboundDisposition) {
        log.debug("Inserting row into Disposition table");
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String dispositionInsertStatement="";
        	        	String[] insertArgs = null;	
        	        	
        	        	//No disposition ID provided in POJO
        	            if (inboundDisposition.getDispositionID() == null)
        	            {	
        	            	dispositionInsertStatement="INSERT into Disposition (PersonID,DispositionTypeID,IncidentCaseNumber,DispositionDate,ArrestingAgencyORI,"
        	                		+ "SentenceTermDays,SentenceFineAmount,InitialChargeCode, FinalChargeCode, RecordType,IsProbationViolation,IsProbationViolationOnOldCharge,RecidivismEligibilityDate, DocketChargeNumber,InitialChargeCode1, FinalChargeCode1) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	            	
            	        	insertArgs = new String[] {"PersonID","DispositionTypeID","IncidentCaseNumber","DispositionDate,ArrestingAgencyORI,"
        	                		+ "SentenceTermDays","SentenceFineAmount","InitialChargeCode", "FinalChargeCode","RecordType","IsProbationViolation","IsProbationViolationOnOldCharge","RecidivismEligibilityDate","DocketChargeNumber","InitialChargeCode1", "FinalChargeCode1"};	
        	            }	
        	            //Disposition ID provided in POJO
        	            else
        	            {
        	            	dispositionInsertStatement="INSERT into Disposition (PersonID,DispositionTypeID,IncidentCaseNumber,DispositionDate,ArrestingAgencyORI,"
        	                		+ "SentenceTermDays,SentenceFineAmount,InitialChargeCode, FinalChargeCode, RecordType,IsProbationViolation,IsProbationViolationOnOldCharge,RecidivismEligibilityDate, DocketChargeNumber, InitialChargeCode1, FinalChargeCode1,DispositionID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	            	
            	        	insertArgs = new String[] {"PersonID","DispositionTypeID","IncidentCaseNumber","DispositionDate,ArrestingAgencyORI,"
        	                		+ "SentenceTermDays","SentenceFineAmount","InitialChargeCode", "FinalChargeCode","RecordType","IsProbationViolation","IsProbationViolationOnOldCharge","RecidivismEligibilityDate","DocketChargeNumber","InitialChargeCode1", "FinalChargeCode1","DispositionID"};
        	            }	
        	        	
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(dispositionInsertStatement, insertArgs);
        	            ps.setInt(1, inboundDisposition.getPersonID());
        	            ps.setInt(2, inboundDisposition.getDispositionTypeID());
        	            ps.setString(3, inboundDisposition.getIncidentCaseNumber());
        	            ps.setDate(4, new java.sql.Date(inboundDisposition.getDispositionDate().getTime()));
        	            ps.setString(5, inboundDisposition.getArrestingAgencyORI());
        	            
        	            if (inboundDisposition.getSentenceTermDays() != null)
        	            {	
        	            	ps.setBigDecimal(6, inboundDisposition.getSentenceTermDays());
        	            }
        	            else
        	            {
        	            	ps.setNull(6, java.sql.Types.NULL);
        	            }	

        	            if (inboundDisposition.getSentenceFineAmount() != null)
        	            {	
        	            	ps.setFloat(7, inboundDisposition.getSentenceFineAmount());
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.NULL);
        	            }
        	            
        	            ps.setString(8, inboundDisposition.getInitialChargeCode());
        	            ps.setString(9, inboundDisposition.getFinalChargeCode());
        	            
        	            ps.setString(10, String.valueOf(inboundDisposition.getRecordType()));

        	            if (inboundDisposition.getIsProbationViolation() != null)
        	            {	
        	            	ps.setString(11, String.valueOf(inboundDisposition.getIsProbationViolation()));
        	            }
        	            else
        	            {
        	            	ps.setNull(11, java.sql.Types.NULL);
        	            }	

        	            if (inboundDisposition.getIsProbationViolationOnOldCharge() != null)
        	            {	
        	            	ps.setString(12, String.valueOf(inboundDisposition.getIsProbationViolationOnOldCharge()));
        	            }
        	            else
        	            {
        	            	ps.setNull(12, java.sql.Types.NULL);
        	            }	

        	            if (inboundDisposition.getRecidivismEligibilityDate() != null)
        	            {	
        	            	ps.setDate(13, new java.sql.Date(inboundDisposition.getRecidivismEligibilityDate().getTime()));
        	            }
        	            else
        	            {
        	            	ps.setNull(13, java.sql.Types.NULL);
        	            }	
        	            
        	            ps.setString(14, inboundDisposition.getDocketChargeNumber());
        	            
        	            ps.setString(15, inboundDisposition.getInitialChargeRank());
        	            
        	            ps.setString(16, inboundDisposition.getFinalChargeRank());
        	            
        	            if (inboundDisposition.getDispositionID() != null)
        	            {	
        	            	ps.setInt(17, inboundDisposition.getDispositionID());
        	            }	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

		        Integer returnValue = null;
		        
		        if (inboundDisposition.getDispositionID() != null)
		        {
		       	 	returnValue = inboundDisposition.getDispositionID();
		        }	 
		        else
		        {
		       	 	returnValue = keyHolder.getKey().intValue();
		        }	 
		        
		        return returnValue;	
    }

	@Override
	public List<Incident> searchForIncidentsByIncidentNumberAndReportingAgencyID(String incidentNumber, Integer reportingAgencyID) {
		
		String sql = "select * from Incident where IncidentCaseNumber = ? and ReportingAgencyID = ?";
		 
		List<Incident> incidents = this.jdbcTemplate.query(sql, new Object[] { incidentNumber,  reportingAgencyID}, new IncidentRowMapper());
		
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
	
	private final String DISPOSITION_BY_DOCKET_CHARGE_NUMBER = 
			"SELECT * FROM Disposition WHERE DocketChargeNumber = ?";
	
	@Override
	public List<Disposition> searchForDispositionsByDocketChargeNumber(
			String docketChargeNumber) {
		List<Disposition> dispositions = 
				jdbcTemplate.query(DISPOSITION_BY_DOCKET_CHARGE_NUMBER, 
						new DispositionRowMapper(), docketChargeNumber);
		
		return dispositions;	
	
	}

	private final String PRETRIAL_SERVICE_PARTICIPATION_BY_UNIQUE_ID = 
			"SELECT * FROM PretrialServiceParticipation WHERE PretrialServiceUniqueID = ?";
	@Override
	public PretrialServiceParticipation searchForPretrialServiceParticipationByUniqueID(
			String uniqueID) {
		 
		List<PretrialServiceParticipation> pretrialServiceParticipations = 
				jdbcTemplate.query(PRETRIAL_SERVICE_PARTICIPATION_BY_UNIQUE_ID, 
						new PretrialServiceParticipationRowMapper(), uniqueID);
		
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
	    	
			pretrialServiceParticipation.setPretrialServiceUniqueID(rs.getString("PretrialServiceUniqueID"));
			
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

	String INCIDENT_DELETE = "delete from Incident where IncidentID = ?";
	String INCIDENT_PERSON_DELETE = "delete from Person where personId in (:personIds)";
	String INCIDENT_PERSON_ID_SELECT = "select personId FROM Arrest where IncidentID = ?";
	String INCIDENT_CHARGE_DELETE = "delete from Charge where arrestID in "
			+ "(SELECT arrestId FROM Arrest WHERE IncidentID = ?)";
	String INCIDENT_ARREST_DELETE = "delete from Arrest where IncidentID = ?";
	String INCIDENT_CIRCUMSTANCE_DELETE = "delete from IncidentCircumstance where IncidentID = ?";
	String INCIDENT_TYPE_DELETE = "delete from IncidentType where IncidentID = ?";
	@Override
	public void deleteIncident(Integer incidentID) throws Exception{
		 
		jdbcTemplate.update(INCIDENT_CHARGE_DELETE, incidentID);

		List<Integer> personIds = jdbcTemplate.queryForList(INCIDENT_PERSON_ID_SELECT, Integer.class, incidentID); 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personIds", personIds);
		jdbcTemplate.update(INCIDENT_ARREST_DELETE, incidentID);
		
		/*
		 * If the incident has no arrest, the personIds list will be empty.
		 */
		if (personIds.size() > 0){
			namedParameterJdbcTemplate.update(INCIDENT_PERSON_DELETE, params);
		}
		jdbcTemplate.update(INCIDENT_CIRCUMSTANCE_DELETE, incidentID);
		jdbcTemplate.update(INCIDENT_TYPE_DELETE,incidentID);
		int resultSize = this.jdbcTemplate.update(INCIDENT_DELETE, incidentID);
		
		if (resultSize == 0)
		{
			throw new Exception("No incident found with IncidentID of: " + incidentID);
		}	
		
	}

	String DISPOSITION_PERSON_DELETE = "DELETE FROM Person WHERE personId = ?";
	String DISPOSTION_DELETE = "delete from Disposition where DispositionID = ?";
	String DISPOSITION_PERSON_ID_SELECT = "SELECT personId FROM Disposition WHERE DispositionID = ?";
 
	
	@Override
	@Transactional
	public void deleteDisposition(Integer dispositionID) throws Exception {

		Integer personId = jdbcTemplate.queryForObject(DISPOSITION_PERSON_ID_SELECT, Integer.class, dispositionID);
		int resultSize = this.jdbcTemplate.update(DISPOSTION_DELETE, new Object[] { dispositionID });
		if (resultSize == 0)
		{
			throw new Exception("No disposition found with DispositionID of: " + dispositionID);
		}	
		
		if (personId != null){
			jdbcTemplate.update(DISPOSITION_PERSON_DELETE, personId);
		}
		
	}

	String PRETRIAL_SERVICE_PARTICIPATION_DELETE = "DELETE FROM PretrialServiceParticipation WHERE PretrialServiceParticipationID = ?";
	String PRETRIAL_SERVICE_PERSON_DELETE = "DELETE FROM Person WHERE personId = ?";
	String PRETRIAL_PERSON_ID_SELECT = "SELECT personId FROM PretrialServiceParticipation WHERE PretrialServiceParticipationID = ?";
	String PRETRIAL_SERVICE_ASSOCIATION_DELETE="delete from PretrialServiceAssociation where PretrialServiceParticipationID = ?";
	String PRETRIAL_SERVICE_NEED_ASSOCIATION_DELETE="delete from PretrialServiceNeedAssociation where PretrialServiceParticipationID = ?";
	@Override
	@Transactional
	public void deletePretrialServiceParticipation(
			Integer pretrialServiceParticipationID) throws Exception {
		
			jdbcTemplate.update(PRETRIAL_SERVICE_ASSOCIATION_DELETE, pretrialServiceParticipationID);
			jdbcTemplate.update(PRETRIAL_SERVICE_NEED_ASSOCIATION_DELETE, pretrialServiceParticipationID); 
			Integer personId = jdbcTemplate.queryForObject(PRETRIAL_PERSON_ID_SELECT, Integer.class, pretrialServiceParticipationID);
			int resultSize = this.jdbcTemplate.update(PRETRIAL_SERVICE_PARTICIPATION_DELETE, new Object[] { pretrialServiceParticipationID });
			
			if (resultSize == 0){
				throw new Exception("No Pretrial Service Participation found with PretrialServiceParticipationID of: " + pretrialServiceParticipationID);
			}	
			
			if (personId != null){
				jdbcTemplate.update(PRETRIAL_SERVICE_PERSON_DELETE, personId);
			}

		
	}
}
