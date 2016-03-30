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
package org.ojbc.adapters.analyticsstaging.custody.dao;

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
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Agency;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonRace;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonSex;
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
