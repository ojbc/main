package org.ojbc.incidentReporting.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.incidentReporting.dao.model.PersonInvolvement;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * This is a RDBMS specific implementation of the Incident Reporting DAO.
 * It uses a MySQL database.
 * 
 */
public class IncidentReportingDaoImpl implements IncidentReportingDao {

	private static final Log log = LogFactory.getLog(IncidentReportingDao.class);
	
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public PersonInvolvement isThereAPriorPersonInvolvment(String incidentId, String incidentOriginatingSystemUri, String personInvolvementHash, String personInvolvementActivity) {
		String queryString = "SELECT * FROM PERSON_INVOLVEMENT_STATE where incident_id =? and incident_originating_system_uri=? and person_involvement_hash = ? and person_involvement_activity = ?";

		Object[] criteriaArray = new Object[] { incidentId,incidentOriginatingSystemUri,personInvolvementHash,personInvolvementActivity};

		PersonInvolvement personInvolvement = null;
		
		try
		{
		personInvolvement = (PersonInvolvement)this.jdbcTemplate.queryForObject(queryString, criteriaArray, new PersonInvolvementRowMapper());
		} 
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		
		return personInvolvement;
	}

	public void addPersonInvolvement(String incidentId, String incidentOriginatingSystemUri, String personInvolvementHash, String personInvolvementActivity) throws Exception {
		
		//insert into database here
		int rowsInserted = this.jdbcTemplate.update("insert into Person_Involvement_State (incident_id,incident_originating_system_uri,person_involvement_hash,person_involvement_activity) values (?,?,?,?)",
				incidentId, incidentOriginatingSystemUri,personInvolvementHash,personInvolvementActivity);

		log.debug("Added this many person involvment rows to the table: "  + rowsInserted);
	}
	
	/**
	 * This method will map the result set row to a POJO. It is the
	 * standard way that Spring JDBC template is used.
	 * 
	 */
	private static final class PersonInvolvementRowMapper implements
			RowMapper<PersonInvolvement> {

		public PersonInvolvement mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			
			PersonInvolvement personInvolvment = new PersonInvolvement();
			
			personInvolvment.setIncidentId(rs.getString("incident_id"));
			personInvolvment.setPersonInvolvementHash(rs.getString("person_involvement_hash"));
			personInvolvment.setIncidentOriginatingSystemUri(rs.getString("incident_originating_system_uri"));
			
			return personInvolvment;
		}
	}	

}
