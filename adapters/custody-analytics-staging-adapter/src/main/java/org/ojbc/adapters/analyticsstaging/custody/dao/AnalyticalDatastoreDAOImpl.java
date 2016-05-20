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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Agency;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
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
        	            
        	            setPreparedStatementVariable(person.getPersonSexID(), ps, 1);
        	            setPreparedStatementVariable(person.getPersonRaceID(), ps, 2);
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
			person.setPersonID(rs.getInt("PersonID"));
			person.setPersonRaceDescription(rs.getString("PersonRaceDescription"));
			person.setPersonSexDescription(rs.getString("PersonSexDescription"));
			person.setPersonRaceID(rs.getInt("PersonRaceID"));
			person.setPersonSexID(rs.getInt("PersonSexID"));
			person.setPersonUniqueIdentifier(rs.getString("PersonUniqueIdentifier"));
			person.setLanguageId(rs.getInt("LanguageID"));
			person.setLanguage(rs.getString("Language"));
			
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
		log.info("Inserting row into BookingCharge table: " + bookingCharges);
		final String sqlString=
				"INSERT INTO BookingCharge (BookingID, ChargeTypeID) values (?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setInt(1, bookingCharges.get(i).getBookingId());
                ps.setInt(2, bookingCharges.get(i).getChargeType().getKey());
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
        		+ "HousingStatusID" //7
        		+ ") values (?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, new String[] {"RecidivistIndicator", "PersonID", 
        	                		"PersonAge", "EducationLevelID", "OccupationID", "IncomeLevelID",
        	                		"HousingStatusID"});
        	            
        	            ps.setShort(1, bookingSubject.getRecidivistIndicator().shortValue());
    	            	ps.setInt(2, bookingSubject.getPersonId());
    	            	setPreparedStatementVariable(bookingSubject.getPersonAge(), ps, 3);
    	            	setPreparedStatementVariable(bookingSubject.getEducationLevelId(), ps, 4);
    	            	setPreparedStatementVariable(bookingSubject.getOccupationId(), ps, 5);
    	            	setPreparedStatementVariable(bookingSubject.getIncomeLevelId(), ps, 6);
    	            	setPreparedStatementVariable(bookingSubject.getHousingStatusId(), ps, 7);
    	            	
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
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "BondAmount", "BondTypeID", "BookingID"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, BondAmount, BondTypeID, BookingID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"JurisdictionID", "BookingReportDate", 
        	                		"BookingReportID" , "SendingAgencyID","CaseStatusID", 
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "BondAmount", "BondTypeID"};

        	        		sqlString="INSERT into booking (JurisdictionID, BookingReportDate,"
        	        				+ "BookingReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, BondAmount, BondTypeID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, booking.getJurisdictionId());
        	            
        	            setPreparedStatementVariable(booking.getBookingReportDate(), ps, 2);
        	            
        	            ps.setString(3, booking.getBookingReportId());

        	            setPreparedStatementVariable(booking.getSendingAgencyId(), ps, 4);
        	            setPreparedStatementVariable(booking.getCaseStatusId(), ps, 5);
        	            setPreparedStatementVariable(booking.getBookingDate(), ps, 6);
        	            setPreparedStatementVariable(booking.getPretrialStatusId(), ps, 7);
        	            setPreparedStatementVariable(booking.getFacilityId(), ps, 8);
        	            setPreparedStatementVariable(booking.getBedTypeId(), ps, 9);
        	            setPreparedStatementVariable(booking.getArrestLocationLatitude(), ps, 10);
        	            setPreparedStatementVariable(booking.getArrestLocationLongitude(), ps, 11);
        	            setPreparedStatementVariable(booking.getBookingSubjectId(), ps, 12);
        	            setPreparedStatementVariable(booking.getCommitDate(), ps, 13);
        	            setPreparedStatementVariable(booking.getBookingNumber(), ps, 14);
                        setPreparedStatementVariable(booking.getBondAmount(), ps, 15);
                        
                        if (booking.getBondType() != null){
                        	setPreparedStatementVariable(booking.getBondType().getKey(), ps,16);
                        }

        	            if (booking.getBookingId() != null){
        	            	setPreparedStatementVariable(booking.getBookingId(), ps, 17);
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
		final String sql = "SELECT * FROM Booking b "
				+ "LEFT JOIN BondType e ON e.BondTypeID = b.BondTypeID "
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
			booking.setSendingAgencyId(rs.getInt("SendingAgencyID"));
			booking.setCaseStatusId(rs.getInt("CaseStatusID"));
			booking.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
			booking.setCommitDate(rs.getDate("CommitDate").toLocalDate());
			booking.setPretrialStatusId(rs.getInt("PretrialStatusID"));
			booking.setFacilityId(rs.getInt("FacilityID"));
			booking.setBedTypeId(rs.getInt("BedTypeID"));
			booking.setArrestLocationLatitude(rs.getBigDecimal("ArrestLocationLatitude"));
			booking.setArrestLocationLongitude(rs.getBigDecimal("ArrestLocationLongitude"));
			booking.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			booking.setBookingNumber(rs.getString("BookingNumber"));
			booking.setBondAmount(rs.getBigDecimal("bondAmount"));
			KeyValue bondType = new KeyValue( rs.getInt("bondTypeId"), rs.getString("bondType"));
			booking.setBondType( bondType );
			

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
			
	    	return bookingSubject;
		}

	}

	@Override
	public List<BookingCharge> getBookingCharges(Integer bookingId) {
		final String sql = "SELECT * FROM BookingCharge b "
				+ "LEFT JOIN ChargeType c ON c.ChargeTypeID = b.ChargeTypeID "
				+ "WHERE b.bookingID = ?"; 
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
			bookingCharge.setBookingId(rs.getInt("bookingId"));
			
			KeyValue chargeType = new KeyValue( rs.getInt("chargeTypeId"), rs.getString("chargeType"));
			bookingCharge.setChargeType( chargeType );
			
	    	return bookingCharge;
		}

	}

	@Override
	public List<BehavioralHealthAssessment> getBehavioralHealthAssessments(
			Integer personId) {
		final String sql = "SELECT * FROM BehavioralHealthAssessment b "
				+ "LEFT JOIN BehavioralHealthType t ON t.BehavioralHealthTypeID = b.BehavioralHealthTypeID "
				+ "WHERE b.PersonID = ?"; 
		List<BehavioralHealthAssessment> behavioralHealthAssessments = 
				jdbcTemplate.query(sql, new BehavioralHealthAssessmentRowMapper(), personId);
		return behavioralHealthAssessments;
	}

	public class BehavioralHealthAssessmentRowMapper implements RowMapper<BehavioralHealthAssessment>
	{
		@Override
		public BehavioralHealthAssessment mapRow(ResultSet rs, int rowNum) throws SQLException {
			BehavioralHealthAssessment behavioralHealthAssessment = new BehavioralHealthAssessment();
	    	
			behavioralHealthAssessment.setBehavioralHealthAssessmentId(rs.getInt("behavioralHealthAssessmentId"));
			behavioralHealthAssessment.setPersonId(rs.getInt("PersonId"));
			behavioralHealthAssessment.setEvaluationDate(rs.getDate("EvaluationDate").toLocalDate());
			
			KeyValue  behavioralHealthType = new KeyValue( rs.getInt("behavioralHealthTypeId"), rs.getString("BehavioralHealthDescription"));
			behavioralHealthAssessment.setBehavioralHealthType(behavioralHealthType);
			
	    	return behavioralHealthAssessment;
		}

	}

	@Override
	public void saveCustodyRelease(CustodyRelease custodyRelease) {

		saveCustodyRelease(custodyRelease.getBookingNumber(),
				custodyRelease.getReleaseDate(), custodyRelease.getReportDate());
	}

	@Override
	public void saveCustodyRelease(String bookingNumber,
			LocalDateTime releaseDate, LocalDateTime reportDate) {

		final String sql = "Insert into CustodyRelease (BookingNumber, ReleaseDate, ReportDate) "
				+ "values (:bookingNumber, :releaseDate, :reportDate)";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("releaseDate", Timestamp.valueOf(releaseDate) );
		params.put("bookingNumber", bookingNumber );
		params.put("reportDate", Timestamp.valueOf(reportDate));
		
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
			custodyRelease.setReleaseDate(rs.getTimestamp("ReleaseDate").toLocalDateTime());
			custodyRelease.setReportDate(rs.getTimestamp("ReportDate").toLocalDateTime());
			
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
        	                		"ReportID" , "SendingAgencyID","CaseStatusID", 
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "BondAmount", "BondTypeID", 
        	                		"CustodyStatusChangeID"};

        	        		sqlString="INSERT into custodyStatusChange (JurisdictionID, ReportDate,"
        	        				+ "ReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, BondAmount, BondTypeID, "
        	        				+ "CustodyStatusChangeID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        	}	
        	        	else{
        	        		insertArgs = new String[] {"JurisdictionID", "ReportDate", 
        	                		"ReportID" , "SendingAgencyID","CaseStatusID", 
        	                		"BookingDate", "PretrialStatusID",
        	                		"FacilityID","BedTypeID", "ArrestLocationLatitude", "ArrestLocationLongitude",
        	                		"BookingSubjectID", "CommitDate", "BookingNumber", "BondAmount", "BondTypeID"};

        	        		sqlString="INSERT into custodyStatusChange (JurisdictionID, ReportDate,"
        	        				+ "ReportID, SendingAgencyID, CaseStatusID, "
        	        				+ "BookingDate, PretrialStatusID, "
        	        				+ "FacilityID, BedTypeID, ArrestLocationLatitude, ArrestLocationLongitude, "
        	        				+ "BookingSubjectID, CommitDate, BookingNumber, BondAmount, BondTypeID) "
        	        				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	        		
        	        	}	
        	        			
        	        	
        	            PreparedStatement ps =
        	                connection.prepareStatement(sqlString, insertArgs);
        	            ps.setInt(1, custodyStatusChange.getJurisdictionId());
        	            
        	            setPreparedStatementVariable(custodyStatusChange.getReportDate(), ps, 2);
        	            
        	            ps.setString(3, custodyStatusChange.getReportId());

        	            setPreparedStatementVariable(custodyStatusChange.getSendingAgencyId(), ps, 4);
        	            setPreparedStatementVariable(custodyStatusChange.getCaseStatusId(), ps, 5);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingDate(), ps, 6);
        	            setPreparedStatementVariable(custodyStatusChange.getPretrialStatusId(), ps, 7);
        	            setPreparedStatementVariable(custodyStatusChange.getFacilityId(), ps, 8);
        	            setPreparedStatementVariable(custodyStatusChange.getBedTypeId(), ps, 9);
        	            setPreparedStatementVariable(custodyStatusChange.getArrestLocationLatitude(), ps, 10);
        	            setPreparedStatementVariable(custodyStatusChange.getArrestLocationLongitude(), ps, 11);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingSubjectId(), ps, 12);
        	            setPreparedStatementVariable(custodyStatusChange.getCommitDate(), ps, 13);
        	            setPreparedStatementVariable(custodyStatusChange.getBookingNumber(), ps, 14);
                        setPreparedStatementVariable(custodyStatusChange.getBondAmount(), ps, 15);
                        
                        if (custodyStatusChange.getBondType() != null){
                        	setPreparedStatementVariable(custodyStatusChange.getBondType().getKey(), ps,16);
                        }

        	            if (custodyStatusChange.getCustodyStatusChangeId() != null){
        	            	setPreparedStatementVariable(custodyStatusChange.getCustodyStatusChangeId(), ps, 17);
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
				"INSERT INTO CustodyStatusChangeCharge (CustodyStatusChangeID, ChargeTypeID) values (?,?)";
		
        jdbcTemplate.batchUpdate(sqlString, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setInt(1, custodyStatusChangeCharges.get(i).getCustodyStatusChangeId());
                ps.setInt(2, custodyStatusChangeCharges.get(i).getChargeType().getKey());
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
			custodyStatusChange.setSendingAgencyId(rs.getInt("SendingAgencyID"));
			custodyStatusChange.setCaseStatusId(rs.getInt("CaseStatusID"));
			custodyStatusChange.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
			custodyStatusChange.setCommitDate(rs.getDate("CommitDate").toLocalDate());
			custodyStatusChange.setPretrialStatusId(rs.getInt("PretrialStatusID"));
			custodyStatusChange.setFacilityId(rs.getInt("FacilityID"));
			custodyStatusChange.setBedTypeId(rs.getInt("BedTypeID"));
			custodyStatusChange.setArrestLocationLatitude(rs.getBigDecimal("ArrestLocationLatitude"));
			custodyStatusChange.setArrestLocationLongitude(rs.getBigDecimal("ArrestLocationLongitude"));
			custodyStatusChange.setBookingSubjectId(rs.getInt("BookingSubjectID"));
			custodyStatusChange.setBookingNumber(rs.getString("BookingNumber"));
			custodyStatusChange.setBondAmount(rs.getBigDecimal("bondAmount"));
			KeyValue bondType = new KeyValue( rs.getInt("bondTypeId"), rs.getString("bondType"));
			custodyStatusChange.setBondType( bondType );

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
			custodyStatusChangeCharge.setCustodyStatusChangeId(rs.getInt("CustodyStatusChangeId"));
			
			KeyValue chargeType = new KeyValue( rs.getInt("chargeTypeId"), rs.getString("chargeType"));
			custodyStatusChangeCharge.setChargeType( chargeType );
			
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

}
