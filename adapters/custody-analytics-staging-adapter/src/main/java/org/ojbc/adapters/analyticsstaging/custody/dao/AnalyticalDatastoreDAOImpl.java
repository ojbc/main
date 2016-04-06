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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Agency;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonRace;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonSex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("analyticalDatastoreDAO")
public class AnalyticalDatastoreDAOImpl implements AnalyticalDatastoreDAO{

	private static final Log log = LogFactory.getLog(AnalyticalDatastoreDAOImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;  
	
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
        log.debug("Inserting row into Person table: " + person.toString());

        final String personStatement="INSERT into Person (PersonSexID, PersonRaceID, PersonBirthDate, PersonUniqueIdentifier, LanguageID) values (?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personStatement, new String[] {"PersonSexID", "PersonRaceID", "PersonBirthDate", "PersonUniqueIdentifier", "LanguageID"});
        	            
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
        	            
        	            setPreparedStatementVariable(person.getPersonBirthDate(), ps, 3);
        	            
        	            ps.setString(4, String.valueOf(person.getPersonUniqueIdentifier()));

        	            setPreparedStatementVariable(person.getLanguageId(), ps, 5);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Person getPerson(Integer personId) {
		final String PERSON_SELECT = "SELECT * FROM Person p "
				+ "LEFT JOIN PersonSex s ON s.PersonSexID = p.PersonSexID "
				+ "LEFT JOIN PersonRace r ON r.PersonRaceID = p.PersonRaceID "
				+ "WHERE p.PersonID = ?"; 
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
	    	
			person.setPersonBirthDate(rs.getDate("PersonBirthDate").toLocalDate());
			person.setPersonID(rs.getInt("PersonID"));
			person.setPersonRaceDescription(rs.getString("PersonRaceDescription"));
			person.setPersonSexDescription(rs.getString("PersonSexDescription"));
			person.setPersonRaceID(rs.getInt("PersonRaceID"));
			person.setPersonSexID(rs.getInt("PersonSexID"));
			person.setPersonUniqueIdentifier(rs.getString("PersonUniqueIdentifier"));
			
	    	return person;
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
	public void saveBehavioralHealthAssessments(
			List<BehavioralHealthAssessment> behavioralHealthAssessments) {
		final String sqlString=
				"INSERT INTO BehavioralHealthAssessment (PersonID, BehavioralHealthTypeID, EvaluationDate) values (?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setInt(1, behavioralHealthAssessments.get(i).getPersonId());
                ps.setInt(2, behavioralHealthAssessments.get(i).getBehavioralHealthType().getKey());
                ps.setDate(3, java.sql.Date.valueOf(behavioralHealthAssessments.get(i).getEvaluationDate()));
            }
	            
            public int getBatchSize() {
                return behavioralHealthAssessments.size();
            }
        });

	}

	@Override
	public void saveBookingCharges(List<BookingCharge> bookingCharges) {
		log.info("Inserting row into BookingSubject table: " + bookingCharges);
		final String sqlString=
				"INSERT INTO BookingCharge (BookingID, ChargeTypeID, BondAmount, BondTypeID) values (?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setInt(1, bookingCharges.get(i).getBookingId());
                ps.setInt(2, bookingCharges.get(i).getChargeType().getKey());
                
                setPreparedStatementVariable(bookingCharges.get(i).getBondAmount(), ps, 3);
                setPreparedStatementVariable(bookingCharges.get(i).getBondType(), ps, 4);
            }
	            
            public int getBatchSize() {
                return bookingCharges.size();
            }
        });

	}

	@Override
	public Integer saveBookingSubject(BookingSubject bookingSubject) {
        log.info("Inserting row into BookingSubject table: " + bookingSubject.toString());

        final String sqlString="INSERT into BookingSubject ("
        		+ "RecidivistIndicator, " //1
        		+ "PersonID, " //2	
        		+ "BookingNumber, " //3
        		+ "PersonAge, " //4
        		+ "EducationLevelID, " //5
        		+ "OccupationID, " //6
        		+ "IncomeLevelID, " //7
        		+ "HousingStatusID" //8
        		+ ") values (?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, new String[] {"RecidivistIndicator", "PersonID", 
        	                		"BookingNumber", "PersonAge", "EducationLevelID", "OccupationID", "IncomeLevelID",
        	                		"HousingStatusID"});
        	            
        	            ps.setShort(1, bookingSubject.getRecidivistIndicator().shortValue());
        	            
    	            	ps.setInt(2, bookingSubject.getPersonId());

    	            	ps.setString(3, bookingSubject.getBookingNumber());
        	            
        	            if ( bookingSubject.getPersonAge() != null )
        	            {	
        	            	ps.setInt(4, bookingSubject.getPersonAge());
        	            }
        	            else
        	            {
        	            	ps.setNull(4, java.sql.Types.NULL);
        	            }	
    	            	
        	            if ( bookingSubject.getEducationLevelId() != null )
        	            {	
        	            	ps.setInt(5, bookingSubject.getEducationLevelId());
        	            }
        	            else
        	            {
        	            	ps.setNull(5, java.sql.Types.NULL);
        	            }	
        	            
        	            if ( bookingSubject.getOccupationId() != null )
        	            {	
        	            	ps.setInt(6, bookingSubject.getOccupationId());
        	            }
        	            else
        	            {
        	            	ps.setNull(6, java.sql.Types.NULL);
        	            }	
        	            
        	            if ( bookingSubject.getIncomeLevelId()!= null )
        	            {	
        	            	ps.setInt(7, bookingSubject.getIncomeLevelId());
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.NULL);
        	            }	
        	            
        	            if ( bookingSubject.getHousingStatusId()!= null )
        	            {	
        	            	ps.setInt(8, bookingSubject.getHousingStatusId());
        	            }
        	            else
        	            {
        	            	ps.setNull(8, java.sql.Types.NULL);
        	            }	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer saveBooking(Booking booking) {
        log.debug("Inserting row into Booking table: " + booking.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String sqlString="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (booking.getBookingId() != null){
        	        		insertArgs = new String[] {"JurisdictionID", "BookingReportDate", 
        	                		"BookingReportID" , "SendingAgencyID","CaseStatusID", 
        	                		"BookingDate", "SupervisionReleaseDate","PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate", "BookingID"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, SupervisionReleaseDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate, BookingID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"JurisdictionID", "BookingReportDate", 
        	                		"BookingReportID" , "SendingAgencyID","CaseStatusID", 
        	                		"BookingDate", "SupervisionReleaseDate","PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, SupervisionReleaseDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, booking.getJurisdictionId());
        	            
        	            setPreparedStatementVariable(booking.getBookingReportDate(), ps, 2);
        	            
        	            ps.setString(3, booking.getBookingReportId());

        	            setPreparedStatementVariable(booking.getSendingAgencyId(), ps, 4);
        	            setPreparedStatementVariable(booking.getCaseStatusId(), ps, 5);
        	            setPreparedStatementVariable(booking.getBookingDate(), ps, 6);
        	            setPreparedStatementVariable(booking.getSupervisionReleaseDate(), ps, 7);
        	            setPreparedStatementVariable(booking.getPretrialStatusId(), ps, 8);
        	            setPreparedStatementVariable(booking.getFacilityId(), ps, 9);
        	            setPreparedStatementVariable(booking.getBedTypeId(), ps, 10);
        	            setPreparedStatementVariable(booking.getArrestLocationLatitude(), ps, 11);
        	            setPreparedStatementVariable(booking.getArrestLocationLongitude(), ps, 12);
        	            setPreparedStatementVariable(booking.getBookingSubjectId(), ps, 13);
        	            setPreparedStatementVariable(booking.getCommitDate(), ps, 14);
        	            
        	            if (booking.getBookingId() != null){
        	            	setPreparedStatementVariable(booking.getBookingId(), ps, 15);
        	            }
        	            
        	            return ps;
        	        }
        	    },keyHolder);

        Integer returnValue = null;
        
        if (booking.getBookingId() != null)
        {
       	 	returnValue = booking.getBookingId();
        }	 
        else
        {
       	 	returnValue = keyHolder.getKey().intValue();
        }	 
        
        return returnValue;	
	}

	private void setPreparedStatementVariable(Object object, PreparedStatement ps, int index)
			throws SQLException {
		
		if (object != null){
			if (object instanceof Integer){
				ps.setInt(index, (Integer) object);
			}
			else if (object instanceof String){
				ps.setString(index, (String) object);
			}
			else if (object instanceof LocalDate){
				ps.setDate(index, java.sql.Date.valueOf((LocalDate) object));
			}
			else if (object instanceof LocalDateTime){
				ps.setTimestamp(index, java.sql.Timestamp.valueOf((LocalDateTime) object));
			}
			else if (object instanceof BigDecimal){
				ps.setBigDecimal(index, (BigDecimal) object);
			}
        }
        else{
        	ps.setNull(index, java.sql.Types.NULL);
        }
	}

	@Override
	@Transactional
	public void deleteBooking(Integer bookingId) {
		String bookingDeleteSql = "DELETE FROM booking WHERE bookingID = ?";
		String bookingChargeDeleteSql = "DELETE FROM BookingCharge WHERE bookingId = ?";
		String bookingSubjectIdSelectSql = "SELECT bookingSubjectId FROM booking WHERE bookingID = ?";
		String bookingSubjectDeleteSql="delete from BookingSubject where bookingSubjectId = ?";
		
		Integer bookingSubjectId = jdbcTemplate.queryForObject(bookingSubjectIdSelectSql, Integer.class, bookingId);
		jdbcTemplate.update(bookingChargeDeleteSql, bookingId);
		jdbcTemplate.update(bookingDeleteSql, bookingId);
		
		if (bookingSubjectId != null){
			jdbcTemplate.update(bookingSubjectDeleteSql, bookingSubjectId);
		}
		
	}

	@Override
	public Booking getBookingByBookingReportId(String bookingReportId) {
		final String sql = "SELECT * FROM Booking WHERE bookingReportId = ?";
		
		List<Booking> bookings = 
				jdbcTemplate.query(sql, 
						new BookingRowMapper(), bookingReportId);
		
		return DataAccessUtils.singleResult(bookings);
	}

	public class BookingRowMapper implements RowMapper<Booking>
	{
		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
	    	
			booking.setBookingId(rs.getInt("BookingID"));
			booking.setBookingReportDate(rs.getTimestamp("BookingReportDate").toLocalDateTime());
			booking.setBookingReportId(rs.getString("BookingReportID"));
			booking.setSendingAgencyId(rs.getInt("SendingAgencyID"));
			booking.setCaseStatusId(rs.getInt("CaseStatusID"));
			booking.setBookingDate(rs.getTimestamp("BookingReportDate").toLocalDateTime());
			booking.setSupervisionReleaseDate(rs.getTimestamp("SupervisionReleaseDate").toLocalDateTime());
			booking.setCommitDate(rs.getDate("CommitDate").toLocalDate());
			booking.setPretrialStatusId(rs.getInt("PretrialStatusID"));
			booking.setFacilityId(rs.getInt("FacilityID"));
			booking.setBedTypeId(rs.getInt("BedTypeID"));
			booking.setArrestLocationLatitude(rs.getBigDecimal("ArrestLocationLatitude"));
			booking.setArrestLocationLongitude(rs.getBigDecimal("ArrestLocationLongitude"));
			booking.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			
	    	return booking;
		}

	}

	@Override
	public Integer getPersonIdByUniqueId(String uniqueId) {
		String sqlString = "SELECT PersonID FROM Person WHERE PersonUniqueIdentifier = ?";
		
		List<Integer> personIds = jdbcTemplate.queryForList(sqlString, Integer.class, uniqueId);

		return DataAccessUtils.uniqueResult(personIds);
	}


}
