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

import static org.ojbc.util.helper.DaoUtils.setPreparedStatementVariable;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Agency;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Medication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonRace;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonSex;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;
import org.ojbc.util.helper.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
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

        final String personStatement="INSERT into Person (PersonSexID, PersonRaceID, PersonBirthDate, "
        		+ "PersonUniqueIdentifier, BookingSubjectNumber, LanguageID, PersonSSN, PersonSID, "
        		+ "PersonEyeColor, PersonHairColor, PersonHeight, personHeightMeasureUnit,personWeight, "
        		+ "personWeightMeasureUnit, RegisteredSexOffender ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personStatement, 
        	                		new String[] {"PersonSexID", "PersonRaceID", "PersonBirthDate", 
        	                		"PersonUniqueIdentifier", "BookingSubjectNumber", "LanguageID", 
        	                		"PersonSSN", "PersonSID", "PersonEyeColor", "PersonHairColor", 
        	                		"PersonHeight", "personHeightMeasureUnit", "PersonWeight", 
        	                		"personWeightMeasureUnit", "RegisteredSexOffender"});
        	            
        	            setPreparedStatementVariable(person.getPersonSexId(), ps, 1);
        	            setPreparedStatementVariable(person.getPersonRaceId(), ps, 2);
        	            setPreparedStatementVariable(person.getPersonBirthDate(), ps, 3);
        	            
        	            ps.setString(4, String.valueOf(person.getPersonUniqueIdentifier()));
        	            setPreparedStatementVariable(person.getBookingSubjectNumber(), ps, 5);
        	            setPreparedStatementVariable(person.getLanguageId(), ps, 6);
        	            setPreparedStatementVariable(person.getPersonSsn(), ps, 7);
        	            setPreparedStatementVariable(person.getPersonSid(), ps, 8);
        	            setPreparedStatementVariable(person.getPersonEyeColor(), ps, 9);
        	            setPreparedStatementVariable(person.getPersonHairColor(), ps, 10);
        	            setPreparedStatementVariable(person.getPersonHeight(), ps, 11);
        	            setPreparedStatementVariable(person.getPersonHeightMeasureUnit(), ps, 12);
        	            setPreparedStatementVariable(person.getPersonWeight(), ps, 13);
        	            setPreparedStatementVariable(person.getPersonWeightMeasureUnit(), ps, 14);
        	            setPreparedStatementVariable(person.getRegisteredSexOffender(), ps, 15);
        	            
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
				+ "LEFT JOIN Language l on l.languageID = p.languageID "
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
			person.setPersonId(rs.getInt("PersonID"));
			person.setPersonRaceDescription(rs.getString("PersonRaceDescription"));
			person.setPersonSexDescription(rs.getString("PersonSexDescription"));
			person.setPersonRaceCode(rs.getString("PersonRaceCode"));
			person.setPersonSexCode(rs.getString("PersonSexCode"));
			person.setPersonRaceId(rs.getInt("PersonRaceId"));
			person.setPersonSexId(rs.getInt("PersonSexId"));
			person.setPersonUniqueIdentifier(rs.getString("PersonUniqueIdentifier"));
			person.setBookingSubjectNumber(rs.getString("BookingSubjectNumber"));
			person.setLanguageId(rs.getInt("LanguageID"));
			person.setLanguage(rs.getString("Language"));
			person.setPersonSsn(rs.getString("PersonSSN"));
			person.setPersonSid(rs.getString("PersonSid"));
			person.setPersonEyeColor(rs.getString("personEyeColor"));
			person.setPersonHairColor(rs.getString("personHairColor"));
			person.setPersonHeight(rs.getString("personHeight"));
			person.setPersonHeightMeasureUnit(rs.getString("personHeightMeasureUnit"));
			person.setPersonWeight(rs.getString("personWeight"));
			person.setPersonWeightMeasureUnit(rs.getString("personWeightMeasureUnit"));
			person.setRegisteredSexOffender(rs.getBoolean("RegisteredSexOffender"));
			
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
		log.debug("Inserting row into BehavioralHealthAssessment table: " + behavioralHealthAssessments);
		final String sqlString=
				"INSERT INTO BehavioralHealthAssessment (PersonID, SeriousMentalIllness,"
				+ "HighRiskNeeds, CareEpisodeStartDate, CareEpisodeEndDate, SubstanceAbuse, GeneralMentalHealthCondition,"
				+ "MedicaidIndicator, RegionalAuthorityAssignmentText ) values (?,?,?,?,?,?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	BehavioralHealthAssessment behavioralHealthAssessment = behavioralHealthAssessments.get(i);
                ps.setInt(1, behavioralHealthAssessment.getPersonId());
                setPreparedStatementVariable(behavioralHealthAssessment.getSeriousMentalIllness(), ps, 2);
                setPreparedStatementVariable(behavioralHealthAssessment.getHighRiskNeeds(), ps, 3);
                setPreparedStatementVariable(behavioralHealthAssessment.getCareEpisodeStartDate(), ps, 4);
                setPreparedStatementVariable(behavioralHealthAssessment.getCareEpisodeEndDate(), ps, 5);
                setPreparedStatementVariable(behavioralHealthAssessment.getSubstanceAbuse(), ps, 6);
                setPreparedStatementVariable(behavioralHealthAssessment.getGeneralMentalHealthCondition(), ps, 7);
                setPreparedStatementVariable(behavioralHealthAssessment.getMedicaidIndicator(), ps, 8);
                setPreparedStatementVariable(behavioralHealthAssessment.getRegionalAuthorityAssignmentText(), ps, 9);
            }
	            
            public int getBatchSize() {
                return behavioralHealthAssessments.size();
            }
        });

	}

	@Override
	public void saveBookingCharges(List<BookingCharge> bookingCharges) {
		log.info("Inserting row into BookingCharge table: " + bookingCharges);
		final String sqlString=
				"INSERT INTO BookingCharge (BookingArrestID, ChargeTypeID, AgencyID, BondAmount, BondTypeID, NextCourtDate, NextCourtName) "
				+ "values (?,?,?,?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	BookingCharge bookingCharge = bookingCharges.get(i);
                ps.setInt(1, bookingCharge.getBookingArrestId());
                ps.setInt(2, bookingCharge.getChargeType().getKey());
                ps.setInt(3, bookingCharge.getAgencyId());
                setPreparedStatementVariable(bookingCharge.getBondAmount(), ps, 4);
                
                if (bookingCharge.getBondType() != null){
                    ps.setInt(5, bookingCharge.getBondType().getKey());
                }
                else{
                	ps.setNull(5, java.sql.Types.NULL);
                }
                setPreparedStatementVariable(bookingCharge.getNextCourtDate(), ps, 6);
                setPreparedStatementVariable(bookingCharge.getNextCourtName(), ps, 7);
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
        		+ "PersonAge, " //3
        		+ "EducationLevelID, " //4
        		+ "OccupationID, " //5
        		+ "IncomeLevelID, " //6
        		+ "HousingStatusID, "
        		+ "militaryServiceStatusCode " //7
        		+ ") values (?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, new String[] {"RecidivistIndicator", "PersonID", 
        	                		"PersonAge", "EducationLevelID", "OccupationID", "IncomeLevelID",
        	                		"HousingStatusID", "militaryServiceStatusCode"});
        	            
        	            ps.setShort(1, bookingSubject.getRecidivistIndicator().shortValue());
    	            	ps.setInt(2, bookingSubject.getPersonId());
    	            	setPreparedStatementVariable(bookingSubject.getPersonAge(), ps, 3);
    	            	setPreparedStatementVariable(bookingSubject.getEducationLevelId(), ps, 4);
    	            	setPreparedStatementVariable(bookingSubject.getOccupationId(), ps, 5);
    	            	setPreparedStatementVariable(bookingSubject.getIncomeLevelId(), ps, 6);
    	            	setPreparedStatementVariable(bookingSubject.getHousingStatusId(), ps, 7);
    	            	setPreparedStatementVariable(bookingSubject.getMilitaryServiceStatusCode(), ps, 8);
    	            	
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public Integer saveBooking(final Booking booking) {
        log.debug("Inserting row into Booking table: " + booking.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String sqlString="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (booking.getBookingId() != null){
        	        		insertArgs = new String[] {"JurisdictionID", "BookingReportDate", 
        	                		"BookingReportID" , "CaseStatusID", "BookingDate", "FacilityID","BedTypeID",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "ScheduledReleaseDate", "BookingID"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, CaseStatusID, "
        	        				+ "BookingDate, FacilityID, BedTypeID, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, ScheduledReleaseDate, BookingID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"JurisdictionID", "BookingReportDate", 
        	                		"BookingReportID" , "CaseStatusID", 
        	                		"BookingDate", "FacilityID","BedTypeID", 
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "ScheduledReleaseDate"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, CaseStatusID, "
        	        				+ "BookingDate,"
        	        				+ "FacilityID, BedTypeID,  "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, ScheduledReleaseDate) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, booking.getJurisdictionId());
        	            
        	            setPreparedStatementVariable(booking.getBookingReportDate(), ps, 2);
        	            
        	            ps.setString(3, booking.getBookingReportId());

        	            setPreparedStatementVariable(booking.getCaseStatusId(), ps, 4);
        	            setPreparedStatementVariable(booking.getBookingDate(), ps, 5);
        	            setPreparedStatementVariable(booking.getFacilityId(), ps, 6);
        	            setPreparedStatementVariable(booking.getBedTypeId(), ps, 7);
        	            setPreparedStatementVariable(booking.getBookingSubjectId(), ps, 8);
        	            setPreparedStatementVariable(booking.getCommitDate(), ps, 9);
        	            setPreparedStatementVariable(booking.getBookingNumber(), ps, 10);
        	            
        	            if (booking.getCustodyRelease() != null){
        	            	setPreparedStatementVariable(booking.getCustodyRelease().getScheduledReleaseDate(), ps, 11);
        	            }
        	            else{
        	            	ps.setNull(11, java.sql.Types.NULL);
        	            }
                        
        	            if (booking.getBookingId() != null){
        	            	setPreparedStatementVariable(booking.getBookingId(), ps, 12);
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

	@Override
	@Transactional
	public void deleteBooking(Integer bookingId) {
		String bookingDeleteSql = "DELETE FROM booking WHERE bookingID = ?";
		String bookingArrestDeleteSql = "DELETE FROM BookingArrest WHERE bookingId = ?";
		String bookingChargeDeleteSql = "DELETE FROM BookingCharge WHERE bookingArrestId in (select bookingArrestID from BookingArrest a where a.bookingId = ?) ";
		String bookingSubjectIdSelectSql = "SELECT bookingSubjectId FROM booking WHERE bookingID = ?";
		String bookingSubjectDeleteSql="delete from BookingSubject where bookingSubjectId = ?";
		
		Integer bookingSubjectId = jdbcTemplate.queryForObject(bookingSubjectIdSelectSql, Integer.class, bookingId);
		jdbcTemplate.update(bookingChargeDeleteSql, bookingId);
		jdbcTemplate.update(bookingArrestDeleteSql, bookingId);
		jdbcTemplate.update(bookingDeleteSql, bookingId);
		
		if (bookingSubjectId != null){
			jdbcTemplate.update(bookingSubjectDeleteSql, bookingSubjectId);
		}
		
	}

	@Override
	public Booking getBookingByBookingReportId(String bookingReportId) {
		final String sql = "SELECT * FROM Booking b "
				+ "WHERE bookingReportId = ?";
		
		List<Booking> bookings = 
				jdbcTemplate.query(sql, 
						new BookingRowMapper(), bookingReportId);
		
		return DataAccessUtils.singleResult(bookings);
	}
	
	@Override
	public Booking getBookingByBookingNumber(String bookingNumber) {
		final String sql = "SELECT * FROM Booking b "
				+ "LEFT JOIN BondType e ON e.BondTypeID = b.BondTypeID "
				+ "WHERE bookingNumber = ?";
		
		List<Booking> bookings = 
				jdbcTemplate.query(sql, 
						new BookingRowMapper(), bookingNumber);
		
		return DataAccessUtils.singleResult(bookings);
	}

	public class BookingRowMapper implements RowMapper<Booking>
	{
		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
	    	
			booking.setBookingId(rs.getInt("BookingID"));
			booking.setJurisdictionId(rs.getInt("JurisdictionID"));
			booking.setBookingReportDate(rs.getTimestamp("BookingReportDate").toLocalDateTime());
			booking.setBookingReportId(rs.getString("BookingReportID"));
			booking.setCaseStatusId(rs.getInt("CaseStatusID"));
			booking.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
			booking.setCommitDate(rs.getDate("CommitDate").toLocalDate());
			booking.setFacilityId(rs.getInt("FacilityID"));
			booking.setBedTypeId(rs.getInt("BedTypeID"));
			booking.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			booking.setBookingNumber(rs.getString("BookingNumber"));
			
			
			CustodyRelease custodyRelease = new CustodyRelease();
			custodyRelease.setScheduledReleaseDate( DaoUtils.getLocalDate(rs, "ScheduledReleaseDate"));
			booking.setCustodyRelease(custodyRelease);
			
	    	return booking;
		}

	}

	@Override
	public Integer getPersonIdByUniqueId(String uniqueId) {
		String sqlString = "SELECT PersonID FROM Person WHERE PersonUniqueIdentifier = ?";
		
		List<Integer> personIds = jdbcTemplate.queryForList(sqlString, Integer.class, uniqueId);

		return DataAccessUtils.uniqueResult(personIds);
	}

	@Override
	public BookingSubject getBookingSubject(Integer bookingSubjectId) {
		final String sql = "SELECT * FROM BookingSubject b "
				+ "LEFT JOIN HousingStatus s ON s.HousingStatusID = b.HousingStatusID "
				+ "LEFT JOIN EducationLevel e ON e.EducationLevelID = b.EducationLevelID "
				+ "LEFT JOIN Occupation o on o.OccupationID = b.OccupationID "
				+ "LEFT JOIN IncomeLevel i on i.IncomeLevelID = b.IncomeLevelID "
				+ "WHERE b.bookingSubjectId = ?"; 
		List<BookingSubject> bookingSubjects = 
				jdbcTemplate.query(sql, new BookingSubjectRowMapper(), bookingSubjectId);
		return DataAccessUtils.uniqueResult(bookingSubjects);
	}

	public class BookingSubjectRowMapper implements RowMapper<BookingSubject>
	{
		@Override
		public BookingSubject mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookingSubject bookingSubject = new BookingSubject();
	    	
			bookingSubject.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			bookingSubject.setRecidivistIndicator(rs.getInt("RecidivistIndicator"));
			bookingSubject.setPersonId(rs.getInt("PersonId"));
			bookingSubject.setPersonAge(rs.getInt("PersonAge"));
			bookingSubject.setHousingStatusId(rs.getInt("HousingStatusID"));
			bookingSubject.setEducationLevelId(rs.getInt("EducationLevelID"));
			bookingSubject.setOccupationId(rs.getInt("OccupationID"));
			bookingSubject.setIncomeLevelId(rs.getInt("IncomeLevelID"));
			bookingSubject.setMilitaryServiceStatusCode(rs.getString("militaryServiceStatusCode"));
			
	    	return bookingSubject;
		}

	}

	@Override
	public List<BookingCharge> getBookingCharges(Integer bookingId) {
		final String sql = "SELECT * FROM BookingCharge b "
				+ "LEFT JOIN ChargeType c ON c.ChargeTypeID = b.ChargeTypeID "
				+ "LEFT JOIN BookingArrest a ON a.BookingArrestID = b.BookingArrestID "
				+ "LEFT JOIN Booking bk ON bk.BookingID = a.BookingID "
				+ "LEFT JOIN BondType bt ON bt.BondTypeID = b.BondTypeID "
				+ "WHERE bk.bookingID = ?"; 
		List<BookingCharge> bookingCharges = 
				jdbcTemplate.query(sql, new BookingChargeRowMapper(), bookingId);
		return bookingCharges;
	}

	public class BookingChargeRowMapper implements RowMapper<BookingCharge>
	{
		@Override
		public BookingCharge mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookingCharge bookingCharge = new BookingCharge();
	    	
			bookingCharge.setBookingChargeId(rs.getInt("bookingChargeId"));
			bookingCharge.setBookingArrestId(rs.getInt("bookingArrestId"));
			
			KeyValue chargeType = new KeyValue( rs.getInt("chargeTypeId"), rs.getString("chargeType"));
			bookingCharge.setChargeType( chargeType );
			
			bookingCharge.setAgencyId(rs.getInt("agencyId"));
			bookingCharge.setBondAmount(rs.getBigDecimal("bondAmount"));
			
			Integer bondTypeId = rs.getInt("bondTypeId"); 
			if (bondTypeId != null){
				KeyValue bondType = new KeyValue( rs.getInt("bondTypeId"), rs.getString("bondType"));
				bookingCharge.setBondType( bondType );
			}
			
			bookingCharge.setNextCourtName( rs.getString("NextCourtName"));
			bookingCharge.setNextCourtDate( DaoUtils.getLocalDate(rs, "NextCourtDate"));
	    	return bookingCharge;
		}

	}

	@Override
	public List<BehavioralHealthAssessment> getBehavioralHealthAssessments(
			Integer personId) {
		final String sql = "SELECT * FROM BehavioralHealthAssessment b "
				+ "LEFT JOIN BehavioralHealthEvaluation e ON e.BehavioralHealthAssessmentID = b.BehavioralHealthAssessmentID "
				+ "LEFT JOIN BehavioralHealthType t ON t.BehavioralHealthTypeID = e.BehavioralHealthTypeID "
				+ "WHERE b.PersonID = ?"; 
		List<BehavioralHealthAssessment> behavioralHealthAssessments = 
				jdbcTemplate.query(sql, new BehavioralHealthAssessmentResultSetExtractor(), personId);
		return behavioralHealthAssessments;
	}

	public class BehavioralHealthAssessmentResultSetExtractor implements ResultSetExtractor<List<BehavioralHealthAssessment>>
	{
		@Override
		public List<BehavioralHealthAssessment> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, BehavioralHealthAssessment> map = new HashMap<Integer, BehavioralHealthAssessment>();
			BehavioralHealthAssessment behavioralHealthAssessment = null;
            while (rs.next()) {
	            Integer behavioralHealthAssessmentId = rs.getInt("behavioralHealthAssessmentId" ); 
	            behavioralHealthAssessment  = map.get( behavioralHealthAssessmentId );
	            if ( behavioralHealthAssessment  == null){
	            	behavioralHealthAssessment = new BehavioralHealthAssessment();; 
	    			behavioralHealthAssessment.setBehavioralHealthAssessmentId(rs.getInt("behavioralHealthAssessmentId"));
	    			behavioralHealthAssessment.setPersonId(rs.getInt("PersonId"));
	    			behavioralHealthAssessment.setSeriousMentalIllness(rs.getBoolean("seriousMentalIllness"));
	    			behavioralHealthAssessment.setHighRiskNeeds(rs.getBoolean("highRiskNeeds"));
	    			behavioralHealthAssessment.setSubstanceAbuse(rs.getBoolean("substanceAbuse"));
	    			behavioralHealthAssessment.setGeneralMentalHealthCondition(rs.getBoolean("generalMentalHealthCondition"));
	    			behavioralHealthAssessment.setMedicaidIndicator(rs.getBoolean("medicaidIndicator"));
	    			behavioralHealthAssessment.setCareEpisodeStartDate(DaoUtils.getLocalDate(rs, "careEpisodeStartDate") );
	    			behavioralHealthAssessment.setCareEpisodeEndDate(DaoUtils.getLocalDate(rs, "careEpisodeEndDate"));
	    			behavioralHealthAssessment.setRegionalAuthorityAssignmentText(rs.getString("regionalAuthorityAssignmentText"));
	    			
	    			behavioralHealthAssessment.setBehavioralHealthTypes(new ArrayList<KeyValue>());
	            	map.put(behavioralHealthAssessmentId, behavioralHealthAssessment); 
	            }
	            
				KeyValue  behavioralHealthType = new KeyValue( rs.getInt("behavioralHealthTypeId"), rs.getString("BehavioralHealthDescription"));
				behavioralHealthAssessment.getBehavioralHealthTypes().add(behavioralHealthType);
            }
            return (List<BehavioralHealthAssessment>) new ArrayList<BehavioralHealthAssessment>(map.values());
			
		}

	}

	@Override
	public void saveCustodyRelease(CustodyRelease custodyRelease) {

		saveCustodyRelease(custodyRelease.getBookingNumber(),
				custodyRelease.getReleaseDate(), custodyRelease.getReportDate(), 
				custodyRelease.getScheduledReleaseDate());
	}

	@Override
	public void saveCustodyRelease(String bookingNumber,
			LocalDateTime releaseDate, LocalDateTime reportDate, LocalDate scheduledReleaseDate) {

		final String sql = "Insert into CustodyRelease (BookingNumber, ReleaseDate, ReportDate, ScheduledReleaseDate) "
				+ "values (:bookingNumber, :releaseDate, :reportDate, :scheduledReleaseDate)";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("releaseDate", Timestamp.valueOf(releaseDate) );
		params.put("bookingNumber", bookingNumber );
		params.put("reportDate", Timestamp.valueOf(reportDate));
		params.put("scheduledReleaseDate", Date.valueOf(scheduledReleaseDate));
		
		namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public CustodyRelease getCustodyReleaseByBookingNumber(String bookingNumber) {
		final String sql = "Select top 1 * from CustodyRelease where BookingNumber = ? order by ReportDate desc";
		
		List<CustodyRelease> custodyReleases = 
				jdbcTemplate.query(sql, new CustodyReleaseRowMapper(), bookingNumber);
		return DataAccessUtils.singleResult(custodyReleases);
		
	}

	public class CustodyReleaseRowMapper implements RowMapper<CustodyRelease>
	{
		@Override
		public CustodyRelease mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustodyRelease custodyRelease = new CustodyRelease();
	    	
			custodyRelease.setBookingNumber(rs.getString("bookingNumber"));
			custodyRelease.setReleaseDate(DaoUtils.getLocalDateTime(rs, "ReleaseDate"));
			custodyRelease.setReportDate( DaoUtils.getLocalDateTime(rs, "ReportDate"));
			custodyRelease.setScheduledReleaseDate(DaoUtils.getLocalDate(rs, "scheduledReleaseDate"));
			
	    	return custodyRelease;
		}

	}

	@Override
	public Integer saveCustodyStatusChange(
			CustodyStatusChange custodyStatusChange) {
        log.debug("Inserting row into CustodyStatusChange table: " + custodyStatusChange.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String sqlString="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (custodyStatusChange.getCustodyStatusChangeId() != null){
        	        		insertArgs = new String[] {"JurisdictionID", "ReportDate", 
        	                		"ReportID" , "CaseStatusID", 
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID", 
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", 
        	                		"CustodyStatusChangeID"};

        	        		sqlString="INSERT into custodyStatusChange (JurisdictionID, ReportDate,"
        	        				+ "ReportID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, "
        	        				+ "CustodyStatusChangeID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"JurisdictionID", "ReportDate", 
        	                		"ReportID" , "CaseStatusID", 
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber"};

        	        		sqlString="INSERT into custodyStatusChange (JurisdictionID, ReportDate,"
        	        				+ "ReportID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, custodyStatusChange.getJurisdictionId());
        	            
        	            setPreparedStatementVariable(custodyStatusChange.getReportDate(), ps, 2);
        	            
        	            ps.setString(3, custodyStatusChange.getReportId());

        	            setPreparedStatementVariable(custodyStatusChange.getCaseStatusId(), ps, 4);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingDate(), ps, 5);
        	            setPreparedStatementVariable(custodyStatusChange.getPretrialStatusId(), ps, 6);
        	            setPreparedStatementVariable(custodyStatusChange.getFacilityId(), ps, 7);
        	            setPreparedStatementVariable(custodyStatusChange.getBedTypeId(), ps, 8);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingSubjectId(), ps, 9);
        	            setPreparedStatementVariable(custodyStatusChange.getCommitDate(), ps, 10);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingNumber(), ps, 11);
        	            
        	            if (custodyStatusChange.getCustodyStatusChangeId() != null){
        	            	setPreparedStatementVariable(custodyStatusChange.getCustodyStatusChangeId(), ps, 12);
        	            }
        	            
        	            return ps;
        	        }
        	    },keyHolder);

        Integer returnValue = null;
        
        if (custodyStatusChange.getCustodyStatusChangeId() != null)
        {
       	 	returnValue = custodyStatusChange.getCustodyStatusChangeId();
        }	 
        else
        {
       	 	returnValue = keyHolder.getKey().intValue();
        }	 
        
        return returnValue;	
	}

	@Override
	public void saveCustodyStatusChangeCharges(
			List<CustodyStatusChangeCharge> custodyStatusChangeCharges) {
		log.info("Inserting row into CustodyStatusChangeCharge table: " + custodyStatusChangeCharges);
		final String sqlString=
				"INSERT INTO CustodyStatusChangeCharge (CustodyStatusChangeArrestID, ChargeTypeID, AgencyID, BondAmount, BondTypeID, NextCourtDate, NextCourtName) "
				+ "values (?,?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	CustodyStatusChangeCharge custodyStatusChangeCharge = custodyStatusChangeCharges.get(i);
                ps.setInt(1, custodyStatusChangeCharge.getCustodyStatusChangeArrestId());
                ps.setInt(2, custodyStatusChangeCharge.getChargeType().getKey());
                ps.setInt(3, custodyStatusChangeCharge.getAgencyId());
                setPreparedStatementVariable(custodyStatusChangeCharge.getBondAmount(), ps, 4);
                
                if (custodyStatusChangeCharge.getBondType() != null){
                	 ps.setInt(5, custodyStatusChangeCharge.getBondType().getKey());
                }
                else{
                	ps.setNull(5, java.sql.Types.NULL);
                }
                
                setPreparedStatementVariable(custodyStatusChangeCharge.getNextCourtDate(), ps, 6);
                setPreparedStatementVariable(custodyStatusChangeCharge.getNextCourtName(), ps, 7);
            }
	            
            public int getBatchSize() {
                return custodyStatusChangeCharges.size();
            }
        });

		
	}

	public CustodyStatusChange getCustodyStatusChangeByReportId(String reportId) {
		final String sql = "SELECT * FROM CustodyStatusChange c "
				+ "LEFT JOIN BondType e ON e.BondTypeID = c.BondTypeID "
				+ "WHERE ReportId = ?";
		
		List<CustodyStatusChange> custodyStatusChanges = 
				jdbcTemplate.query(sql, 
						new CustodyStatusChangeRowMapper(), reportId);
		
		return DataAccessUtils.singleResult(custodyStatusChanges);
	}

	public class CustodyStatusChangeRowMapper implements RowMapper<CustodyStatusChange>
	{
		@Override
		public CustodyStatusChange mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustodyStatusChange custodyStatusChange = new CustodyStatusChange();
	    	
			custodyStatusChange.setCustodyStatusChangeId(rs.getInt("CustodyStatusChangeId"));
			custodyStatusChange.setJurisdictionId(rs.getInt("JurisdictionID"));
			custodyStatusChange.setReportDate(rs.getTimestamp("ReportDate").toLocalDateTime());
			custodyStatusChange.setReportId(rs.getString("ReportID"));
			custodyStatusChange.setCaseStatusId(rs.getInt("CaseStatusID"));
			custodyStatusChange.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
			custodyStatusChange.setCommitDate(rs.getDate("CommitDate").toLocalDate());
			custodyStatusChange.setPretrialStatusId(rs.getInt("PretrialStatusID"));
			custodyStatusChange.setFacilityId(rs.getInt("FacilityID"));
			custodyStatusChange.setBedTypeId(rs.getInt("BedTypeID"));
			custodyStatusChange.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			custodyStatusChange.setBookingNumber(rs.getString("BookingNumber"));

	    	return custodyStatusChange;
		}

	}

	@Override
	public List<CustodyStatusChangeCharge> getCustodyStatusChangeCharges(Integer custodyStatusChangeId) {
		final String sql = "SELECT * FROM CustodyStatusChangeCharge b "
				+ "LEFT JOIN ChargeType c ON c.ChargeTypeID = b.ChargeTypeID "
				+ "WHERE b.custodyStatusChangeId = ?"; 
		List<CustodyStatusChangeCharge> custodyStatusChangeCharges = 
				jdbcTemplate.query(sql, new CustodyStatusChangeChargeRowMapper(), custodyStatusChangeId);
		return custodyStatusChangeCharges;
	}

	public class CustodyStatusChangeChargeRowMapper implements RowMapper<CustodyStatusChangeCharge>
	{
		@Override
		public CustodyStatusChangeCharge mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustodyStatusChangeCharge custodyStatusChangeCharge = new CustodyStatusChangeCharge();
	    	
			custodyStatusChangeCharge.setCustodyStatusChangeChargeId(rs.getInt("CustodyStatusChangeChargeId"));
			custodyStatusChangeCharge.setCustodyStatusChangeArrestId(rs.getInt("CustodyStatusChangeArrestId"));
			
			KeyValue chargeType = new KeyValue( rs.getInt("chargeTypeId"), rs.getString("chargeType"));
			custodyStatusChangeCharge.setChargeType( chargeType );
			
			custodyStatusChangeCharge.setAgencyId(rs.getInt("agencyId"));
			custodyStatusChangeCharge.setBondAmount(rs.getBigDecimal("bondAmount"));
			KeyValue bondType = new KeyValue( rs.getInt("bondTypeId"), rs.getString("bondType"));
			custodyStatusChangeCharge.setBondType( bondType );
			
	    	return custodyStatusChangeCharge;
		}

	}

	@Override
	public BookingSubject getBookingSubjectByBookingNumberAndPersonId(String bookingNumber, Integer personId) {
		final String sql = "SELECT b.* FROM BookingSubject b "
				+ "LEFT JOIN Booking k ON k.BookingSubjectID = b.BookingSubjectID "
				+ "LEFT JOIN HousingStatus s ON s.HousingStatusID = b.HousingStatusID "
				+ "LEFT JOIN EducationLevel e ON e.EducationLevelID = b.EducationLevelID "
				+ "LEFT JOIN Occupation o on o.OccupationID = b.OccupationID "
				+ "LEFT JOIN IncomeLevel i on i.IncomeLevelID = b.IncomeLevelID "
				+ "WHERE k.bookingNumber = ? AND b.PersonId = ?"; 
		List<BookingSubject> bookingSubjects = 
				jdbcTemplate.query(sql, new BookingSubjectRowMapper(), bookingNumber, personId);
		return DataAccessUtils.uniqueResult(bookingSubjects);
	}

	@Override
	public Integer saveBookingArrest(BookingArrest bookingArrest) {
        log.debug("Inserting row into BookingArrest table: " + bookingArrest.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String sqlString="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (bookingArrest.getBookingArrestId() != null){
        	        		insertArgs = new String[] {"bookingId", "streetNumber", 
        	                		"streetName" , "addressSecondaryUnit", "city", "state","postalcode",
        	                		"arrestLocationLatitude", "arrestLocationLongitude", "bookingArrestId"};

        	        		sqlString="INSERT into bookingArrest (bookingId, streetNumber,"
        	        				+ "streetName, addressSecondaryUnit, "
        	        				+ "city, state, postalcode, "
        	        				+ "arrestLocationLatitude, arrestLocationLongitude, bookingArrestId) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"bookingId", "streetNumber", 
        	                		"streetName" , "addressSecondaryUnit", 
        	                		"city", "state","postalcode", 
        	                		"arrestLocationLatitude", "arrestLocationLongitude"};

        	        		sqlString="INSERT into bookingArrest (bookingId, streetNumber,"
        	        				+ "streetName, addressSecondaryUnit, "
        	        				+ "city,"
        	        				+ "state, postalcode,  "
        	        				+ "arrestLocationLatitude, arrestLocationLongitude) "
        	        				+ "values (?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, bookingArrest.getBookingId());
        	            
        	            setPreparedStatementVariable(bookingArrest.getStreetNumber(), ps, 2);
        	            setPreparedStatementVariable(bookingArrest.getStreetName(), ps, 3);
        	            setPreparedStatementVariable(bookingArrest.getAddressSecondaryUnit(), ps, 4);
        	            setPreparedStatementVariable(bookingArrest.getCity(), ps, 5);
        	            setPreparedStatementVariable(bookingArrest.getState(), ps, 6);
        	            setPreparedStatementVariable(bookingArrest.getPostalcode(), ps, 7);
        	            setPreparedStatementVariable(bookingArrest.getArrestLocationLatitude(), ps, 8);
        	            setPreparedStatementVariable(bookingArrest.getArrestLocationLongitude(), ps, 9);
        	            
        	            if (bookingArrest.getBookingArrestId() != null){
        	            	setPreparedStatementVariable(bookingArrest.getBookingArrestId(), ps, 10);
        	            }
        	            
        	            return ps;
        	        }
        	    },keyHolder);

        Integer returnValue = null;
        
        if (bookingArrest.getBookingArrestId() != null)
        {
       	 	returnValue = bookingArrest.getBookingArrestId();
        }	 
        else
        {
       	 	returnValue = keyHolder.getKey().intValue();
        }	 
        
        return returnValue;	
	}

	@Override
	public Integer saveBehavioralHealthAssessment(BehavioralHealthAssessment assessment) {
        log.debug("Inserting row into BehavioralHealthAssessment table: " + assessment.toString());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	        	
        	        	String sqlString="";
        	        	String[] insertArgs = null;
        	        	
        	        	if (assessment.getBehavioralHealthAssessmentId()!= null){
        	        		insertArgs = new String[] {"personId", "seriousMentalIllness", 
        	                		"highRiskNeeds" , "careEpisodeStartDate", "careEpisodeEndDate", 
        	                		"substanceAbuse","generalMentalHealthCondition",
        	                		"medicaidIndicator", "regionalAuthorityAssignmentText", "behavioralHealthAssessmentId"};

        	        		sqlString="INSERT into BehavioralHealthAssessment (personId, seriousMentalIllness,"
        	        				+ "highRiskNeeds, careEpisodeStartDate, careEpisodeEndDate,"
        	        				+ "substanceAbuse, generalMentalHealthCondition, medicaidIndicator, "
        	        				+ "regionalAuthorityAssignmentText, behavioralHealthAssessmentId) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"personId", "seriousMentalIllness", 
        	                		"highRiskNeeds" , "careEpisodeStartDate", "careEpisodeEndDate", 
        	                		"substanceAbuse","generalMentalHealthCondition",
        	                		"medicaidIndicator", "regionalAuthorityAssignmentText"};

        	        		sqlString="INSERT into BehavioralHealthAssessment (personId, seriousMentalIllness,"
        	        				+ "highRiskNeeds, careEpisodeStartDate, careEpisodeEndDate,"
        	        				+ "substanceAbuse, generalMentalHealthCondition, medicaidIndicator, "
        	        				+ "regionalAuthorityAssignmentText) "
        	        				+ "values (?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, assessment.getPersonId());
        	            
        	            setPreparedStatementVariable(assessment.getSeriousMentalIllness(), ps, 2);
        	            setPreparedStatementVariable(assessment.getHighRiskNeeds(), ps, 3);
        	            setPreparedStatementVariable(assessment.getCareEpisodeStartDate(), ps, 4);
        	            setPreparedStatementVariable(assessment.getCareEpisodeEndDate(), ps, 5);
        	            setPreparedStatementVariable(assessment.getSubstanceAbuse(), ps, 6);
        	            setPreparedStatementVariable(assessment.getGeneralMentalHealthCondition(), ps, 7);
        	            setPreparedStatementVariable(assessment.getMedicaidIndicator(), ps, 8);
        	            setPreparedStatementVariable(assessment.getRegionalAuthorityAssignmentText(), ps, 9);
        	            
        	            if (assessment.getBehavioralHealthAssessmentId() != null){
        	            	setPreparedStatementVariable(assessment.getBehavioralHealthAssessmentId(), ps, 10);
        	            }
        	            
        	            return ps;
        	        }
        	    },keyHolder);

        Integer returnValue = null;
        
        if (assessment.getBehavioralHealthAssessmentId() != null)
        {
       	 	returnValue = assessment.getBehavioralHealthAssessmentId();
        }	 
        else
        {
       	 	returnValue = keyHolder.getKey().intValue();
        }	 
        
        return returnValue;	
	}

	@Override
	public Integer getMedicationId(String generalProductId, String itemName) {
        final String sql="SELECT medicationId FROM Medication WHERE GeneralProductID = ? AND ItemName = ?";
        
        List<Integer> medicationIds = jdbcTemplate.queryForList(sql, Integer.class, generalProductId, itemName);
        
		return DataAccessUtils.singleResult(medicationIds);
	}

	@Override
	public Integer saveMedication(String generalProductId, String itemName) {
        log.debug("Inserting row into the Medication table");

        final String sql="INSERT into Medication (GeneralProductID, ItemName) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(sql, new String[] {"GeneralProductID","ItemName"});
        	            ps.setString(1, generalProductId);
        	            ps.setString(2, itemName);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public void saveTreatments(final List<Treatment> treatments) {
		log.info("Inserting row into Treatment table: " + treatments);
		final String sqlString=
				"INSERT INTO Treatment (BehavioralHealthAssessmentID, StartDate, EndDate, TreatmentCourtOrdered, TreatmentText, TreatmentProvider, TreatmentActive) "
				+ "values (?,?,?,?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	Treatment treatment = treatments.get(i);
                ps.setInt(1, treatment.getBehavioralHealthAssessmentID());
                setPreparedStatementVariable(treatment.getStartDate(), ps, 2);
                setPreparedStatementVariable(treatment.getEndDate(), ps, 3);
                setPreparedStatementVariable(treatment.getTreatmentCourtOrdered(), ps, 4);
                setPreparedStatementVariable(treatment.getTreatmentText(), ps, 5);
                setPreparedStatementVariable(treatment.getTreatmentProvider(), ps, 6);
                setPreparedStatementVariable(treatment.getTreatmentActive(), ps, 7);
            }
	            
            public int getBatchSize() {
                return treatments.size();
            }
        });
	}

	@Override
	public void saveBehavioralHealthEvaluations(
			Integer behavioralHealthAssessmentId, List<KeyValue> behavioralHealthTypes) {
		log.info("Inserting row into BehavioralHealthEvaluation table: " + behavioralHealthTypes);
		final String sqlString=
				"INSERT INTO BehavioralHealthEvaluation (BehavioralHealthAssessmentID, BehavioralHealthTypeID) "
				+ "values (?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	KeyValue behavioralHealthType = behavioralHealthTypes.get(i);
                ps.setInt(1, behavioralHealthAssessmentId);
                ps.setInt(2, behavioralHealthType.getKey());
            }
	            
            public int getBatchSize() {
                return behavioralHealthTypes.size();
            }
        });
		
	}

	@Override
	public void savePrescribedMedications(
			List<PrescribedMedication> prescribedMedications) {
		log.info("Inserting row into PrescribedMedication table: " + prescribedMedications);
		final String sqlString=
				"INSERT INTO PrescribedMedication (BehavioralHealthAssessmentID, MedicationID, MedicationDispensingDate, MedicationDoseMeasure) "
				+ "values (?,?,?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            	PrescribedMedication prescribedMedication = prescribedMedications.get(i);
                ps.setInt(1, prescribedMedication.getBehavioralHealthAssessmentID());
                setPreparedStatementVariable(prescribedMedication.getMedicationId(), ps, 2);
                setPreparedStatementVariable(prescribedMedication.getMedicationDispensingDate(), ps, 3);
                setPreparedStatementVariable(prescribedMedication.getMedicationDoseMeasure(), ps, 4);
            }
	            
            public int getBatchSize() {
                return prescribedMedications.size();
            }
        });
	}

	@Override
	public Integer saveBehavioralHealthType(
			String evaluationDiagnosisDescriptionText) {
        log.debug("Inserting row into the BehavioralHealthType table");

        final String sql="INSERT into BehavioralHealthType (BehavioralHealthDescription) values (?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(sql, new String[] {"BehavioralHealthDescription"});
        	            ps.setString(1, evaluationDiagnosisDescriptionText);
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public List<BookingArrest> getBookingArrests(Integer bookingId) {
		final String sql = "SELECT * FROM BookingArrest a "
				+ "WHERE a.bookingID = ?"; 
		List<BookingArrest> bookingArrests = 
				jdbcTemplate.query(sql, new BookingArrestRowMapper(), bookingId);
		return bookingArrests;
	}

	public class BookingArrestRowMapper implements RowMapper<BookingArrest>
	{
		@Override
		public BookingArrest mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookingArrest bookingArrest = new BookingArrest();
	    	
			bookingArrest.setBookingId(rs.getInt("bookingId"));
			bookingArrest.setBookingArrestId(rs.getInt("bookingArrestId"));

			bookingArrest.setStreetNumber(rs.getString("streetNumber"));
			bookingArrest.setStreetName(rs.getString("streetName"));
			bookingArrest.setAddressSecondaryUnit(rs.getString("addressSecondaryUnit"));
			bookingArrest.setCity(rs.getString("city"));
			bookingArrest.setState(rs.getString("State"));
			bookingArrest.setPostalcode(rs.getString("postalcode"));
			bookingArrest.setArrestLocationLatitude(rs.getBigDecimal("ArrestLocationLatitude"));
			bookingArrest.setArrestLocationLongitude(rs.getBigDecimal("ArrestLocationLongitude"));
	    	return bookingArrest;
		}

	}

	@Override
	public List<Treatment> getTreatments(Integer behavioralHealthAssessmentId) {
		final String sql = "SELECT * FROM Treatment t "
				+ "WHERE t.behavioralHealthAssessmentId = ?"; 
		List<Treatment> treatments = 
				jdbcTemplate.query(sql, new TreatmentRowMapper(), behavioralHealthAssessmentId);
		return treatments;
	}

	public class TreatmentRowMapper implements RowMapper<Treatment>
	{
		@Override
		public Treatment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Treatment treatment = new Treatment();
	    	
			treatment.setTreatmentId(rs.getInt("treatmentId"));
			treatment.setBehavioralHealthAssessmentID(rs.getInt("behavioralHealthAssessmentID"));

			treatment.setStartDate(DaoUtils.getLocalDate(rs, "startDate"));
			treatment.setEndDate(DaoUtils.getLocalDate(rs, "endDate"));
			treatment.setTreatmentCourtOrdered(rs.getBoolean("treatmentCourtOrdered"));
			treatment.setTreatmentActive(rs.getBoolean("treatmentActive"));
			treatment.setTreatmentText(rs.getString("treatmentText"));
			treatment.setTreatmentProvider(rs.getString("treatmentProvider"));
	    	return treatment;
		}

	}


	@Override
	public List<PrescribedMedication> getPrescribedMedication(
			Integer behavioralHealthAssessmentId) {
		final String sql = "SELECT * FROM PrescribedMedication p "
				+ "LEFT OUTER JOIN Medication m ON m.MedicationID = p.MedicationID "
				+ "WHERE p.behavioralHealthAssessmentId = ?"; 
		List<PrescribedMedication> prescribedMedications = 
				jdbcTemplate.query(sql, new PrescribedMedicationRowMapper(), behavioralHealthAssessmentId);
		return prescribedMedications;
	}

	public class PrescribedMedicationRowMapper implements RowMapper<PrescribedMedication>
	{
		@Override
		public PrescribedMedication mapRow(ResultSet rs, int rowNum) throws SQLException {
			PrescribedMedication prescribedMedication = new PrescribedMedication();
	    	
			prescribedMedication.setPrescribedMedicationID(rs.getInt("prescribedMedicationID"));
			prescribedMedication.setBehavioralHealthAssessmentID(rs.getInt("behavioralHealthAssessmentID"));

			prescribedMedication.setMedicationId(rs.getInt("medicationID"));
			prescribedMedication.setMedicationDispensingDate(DaoUtils.getLocalDate(rs, "medicationDispensingDate"));
			prescribedMedication.setMedicationDoseMeasure(rs.getString("medicationDoseMeasure"));
			
			Medication medication = new Medication(); 
			
			medication.setMedicationId(prescribedMedication.getMedicationId());
			medication.setGeneralProductId(rs.getString("generalProductId"));
			medication.setItemName(rs.getString("itemName"));
			prescribedMedication.setMedication(medication);
			
	    	return prescribedMedication;
		}

	}


}
