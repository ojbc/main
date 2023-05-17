package org.ojbc.booking.common.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.booking.common.dao.model.Arrest;
import org.ojbc.booking.common.dao.model.Booking;
import org.ojbc.booking.common.dao.model.BookingPerson;
import org.ojbc.booking.common.dao.model.Charge;
import org.ojbc.booking.common.dao.model.Conditions;
import org.ojbc.booking.common.dao.model.Location;
import org.ojbc.booking.common.dao.model.Person;
import org.ojbc.booking.common.dao.model.PersonAlias;
import org.ojbc.booking.common.dao.model.ScarsMarksTattoos;
import org.ojbc.booking.common.dao.model.request.CustodyPersonSearchRequest;
import org.ojbc.booking.common.util.CustodyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class CustodyDatastoreDAOImpl implements CustodyDatastoreDAO {

	private static final Log log = LogFactory.getLog(CustodyDatastoreDAOImpl.class);
	
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public void updatePerson(String firstName, String middleName,
			String lastName, LocalDate dateOfBirth, String gender, String race,
			String ethnicity, Boolean sexOffender, String education,
			String primaryLanguage, String occupation, String militaryService,
			String sid, String eyeColor, String hairColor, Integer height, Integer weight, 
			Boolean allowDeposits,String uniquePersonID) {

        log.debug("Updating row into Person table");

        final String personInsertStatement="update PERSON set first_name =?, middle_name =?, last_name=?, dob=?, sex=?, race=?, ethnicity=?, sex_offender=?, education=?, primary_language=?, occupation=?, military_service=?, sid=?, eye_color=?, hair_color =?, height=?, weight =?, allow_deposits =? where person_unique_identifier=?";
        
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personInsertStatement);
        	            ps.setString(1, firstName);
        	            ps.setString(2, middleName);
        	            ps.setString(3, lastName);
        	            
        	            if (dateOfBirth != null)
        	            {	
        	            	ps.setDate(4, Date.valueOf(dateOfBirth));
        	            }	
        	            else
        	            {	
        	            	ps.setNull(4, java.sql.Types.DATE);
        	            }	        	            
        	            
        	            ps.setString(5, gender);
        	            ps.setString(6, race);
        	            ps.setString(7, ethnicity);
        	            
        	            if (sexOffender != null)
        	            {	
        	            	ps.setBoolean(8, sexOffender);
        	            }
        	            else
        	            {
        	            	ps.setNull(8, java.sql.Types.BOOLEAN);
        	            }	
        	            
        	            ps.setString(9, education);
        	            ps.setString(10, primaryLanguage);
        	            ps.setString(11, occupation);
        	            ps.setString(12, militaryService);
        	            ps.setString(13, sid);
        	            ps.setString(14, eyeColor);
        	            ps.setString(15, hairColor);
        	            
        	            if (height != null)
        	            {	
        	            	ps.setInt(16, height);
        	            }
        	            else
        	            {
        	            	ps.setNull(16, java.sql.Types.INTEGER);
        	            }	        	            
        	            
        	            if (weight != null)
        	            {	
        	            	ps.setInt(17, weight);
        	            }
        	            else
        	            {
        	            	ps.setNull(17, java.sql.Types.INTEGER);
        	            }	  
        	            
        	            if (allowDeposits != null)
        	            {	
        	            	ps.setBoolean(18, allowDeposits);
        	            }
        	            else
        	            {
        	            	ps.setNull(18, java.sql.Types.BOOLEAN);
        	            }	
        	            
        	            ps.setString(19, uniquePersonID);
        	            
        	            return ps;
        	        }
        	    }
          );

	}
	
	@Override
	public int savePerson(String firstName, String middleName, String lastName, LocalDate dateOfBirth, String gender, String race, String ethnicity, Boolean sexOffender, String education, String primaryLanguage, String occupation, String militaryService, String sid, String eyeColor, String hairColor, Integer height, Integer weight, Boolean allowDeposits, String uniquePersonID) {

        log.debug("Inserting row into Person table");

        final String personInsertStatement="INSERT into PERSON (first_name, middle_name, last_name, dob, sex, race, ethnicity, sex_offender, education, primary_language, occupation, military_service, sid, eye_color, hair_color,height,weight,allow_deposits, person_unique_identifier) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personInsertStatement, new String[] {"id"});
        	            ps.setString(1, firstName);
        	            ps.setString(2, middleName);
        	            ps.setString(3, lastName);
        	            
        	            if (dateOfBirth != null)
        	            {	
        	            	ps.setDate(4, Date.valueOf(dateOfBirth));
        	            }	
        	            else
        	            {	
        	            	ps.setNull(4, java.sql.Types.DATE);
        	            }	        	            
        	            
        	            ps.setString(5, gender);
        	            ps.setString(6, race);
        	            ps.setString(7, ethnicity);
        	            
        	            if (sexOffender != null)
        	            {	
        	            	ps.setBoolean(8, sexOffender);
        	            }
        	            else
        	            {
        	            	ps.setNull(8, java.sql.Types.BOOLEAN);
        	            }	
        	            
        	            ps.setString(9, education);
        	            ps.setString(10, primaryLanguage);
        	            ps.setString(11, occupation);
        	            ps.setString(12, militaryService);
        	            ps.setString(13, sid);
        	            ps.setString(14, eyeColor);
        	            ps.setString(15, hairColor);
        	            
        	            if (height != null)
        	            {	
        	            	ps.setInt(16, height);
        	            }
        	            else
        	            {
        	            	ps.setNull(16, java.sql.Types.INTEGER);
        	            }	        	            
        	            
        	            if (weight != null)
        	            {	
        	            	ps.setInt(17, weight);
        	            }
        	            else
        	            {
        	            	ps.setNull(17, java.sql.Types.INTEGER);
        	            }	  
        	            
        	            if (allowDeposits != null)
        	            {	
        	            	ps.setBoolean(18, allowDeposits);
        	            }
        	            else
        	            {
        	            	ps.setNull(18, java.sql.Types.BOOLEAN);
        	            }	
        	            
        	            ps.setString(19, uniquePersonID);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();
	}

	@Override
	public int savePersonAlias(int personID, String nameType, String firstName, String middleName, String lastName, String sex, LocalDate dateOfBirth) {
        log.debug("Inserting row into Person table");

        final String personInsertStatement="INSERT into PERSON_ALIAS (person_id, name_type, alias_last_name, alias_first_name, alias_middle, alias_sex, alias_dob) values (?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personInsertStatement, new String[] {"id"});
        	            ps.setInt(1, personID);
        	            ps.setString(2, nameType);
        	            ps.setString(3, lastName);
        	            ps.setString(4, firstName);
        	            ps.setString(5, middleName);
        	            ps.setString(6, sex);
        	            
        	            if (dateOfBirth != null)
        	            {	
        	            	ps.setDate(7, Date.valueOf(dateOfBirth));
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.DATE);
        	            }	
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }

	@Override
	public void updateBooking(LocalDateTime bookingDatetime, String block, String bed,
			String cell, String facility, String bookingNumber,
			String caseStatus, Boolean inmateWorkerIndicator,
			Boolean inmateWorkReleaseIndicator, Boolean probationerIndicator,
			Boolean incarceratedIndicator, LocalDateTime actualReleaseDateTime,
			LocalDate commitDate, LocalDate scheduledReleaseDate,
			Boolean inProcessIndicator, Boolean mistakenBookingIndicator) {

		log.debug("Updating row into booking table");
		
		final String bookingUpdateStatement="update BOOKING set booking_datetime =?, block =?, bed=?, cell=?, facility=?, case_status=?, inmate_worker_indicator=?, inmate_work_release_indicator=?, probationer_indicator=?, incarcerated_indicator=?, actual_release_datetime=?, commit_date=?, scheduled_release_date=?, in_process_indicator=?, mistaken_booking_indicator=? where booking_number=?";
		
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(bookingUpdateStatement, new String[] {"booking_datetime","block", "bed", "cell","booking_number","facility","case_status","inmate_worker_indicator", "inmate_work_release_indicator", "probationer_indicator", "incarcerated_indicator", "actual_release_datetime","commit_date", "scheduled_release_date"});
        	            
        	            Timestamp bookingDatetimeTimestamp = null;
        	            
        	            if (bookingDatetime != null)
        	            {	
        	            	bookingDatetimeTimestamp = Timestamp.valueOf(bookingDatetime);
        	            	ps.setTimestamp(1, bookingDatetimeTimestamp);
        	            }
        	            else
        	            {
        	            	ps.setNull(1, java.sql.Types.TIMESTAMP);
        	            }        	            
        	            
        	            ps.setString(2, block);
        	            ps.setString(3, bed);
        	            ps.setString(4, cell);
        	            ps.setString(5, facility);
        	            ps.setString(6, caseStatus);
        	            
        	            if (inmateWorkerIndicator != null)
        	            {	
        	            	ps.setBoolean(7, inmateWorkerIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.BOOLEAN);
        	            }	

        	            if (inmateWorkReleaseIndicator != null)
        	            {	
        	            	ps.setBoolean(8, inmateWorkReleaseIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(8, java.sql.Types.BOOLEAN);
        	            }	

        	            if (probationerIndicator != null)
        	            {	
        	            	ps.setBoolean(9, probationerIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(9, java.sql.Types.BOOLEAN);
        	            }	

        	            if (incarceratedIndicator != null)
        	            {	
        	            	ps.setBoolean(10, incarceratedIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(10, java.sql.Types.BOOLEAN);
        	            }	

        	            Timestamp actualReleaseDateTimestamp = null;
        	            
        	            if (actualReleaseDateTime != null)
        	            {	
        	            	actualReleaseDateTimestamp = Timestamp.valueOf(actualReleaseDateTime);
        	            	ps.setTimestamp(11, actualReleaseDateTimestamp);
        	            }
        	            else
        	            {
        	            	ps.setNull(11, java.sql.Types.TIMESTAMP);
        	            }
        	            
        	            
        	            if (commitDate != null)
        	            {	
        	            	ps.setDate(12, Date.valueOf(commitDate));
        	            }	
        	            else
        	            {	
        	            	ps.setNull(12, java.sql.Types.DATE);
        	            }	
        	            
        	            if (scheduledReleaseDate != null)
        	            {	
        	            	ps.setDate(13, Date.valueOf(scheduledReleaseDate));
        	            }
        	            else
        	            {
        	            	ps.setNull(13, java.sql.Types.DATE);
        	            }
        	            
        	            if (inProcessIndicator != null)
        	            {	
        	            	ps.setBoolean(14, inProcessIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(14, java.sql.Types.BOOLEAN);
        	            }
        	            
        	            if (mistakenBookingIndicator != null)
        	            {	
        	            	ps.setBoolean(15, mistakenBookingIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(15, java.sql.Types.BOOLEAN);
        	            }        	            
        	            
        	            ps.setString(16, bookingNumber);
        	            
        	            return ps;
        	        }
        	    });
	}
	
	@Override
	public int saveBooking(int personID, LocalDateTime bookingDateTime, String block, String bed, String cell, String facility, String bookingNumber, String caseStatus, Boolean inmateWorkerIndicator, Boolean inmateWorkReleaseIndicator, Boolean probationerIndicator, Boolean incarceratedIndicator, LocalDateTime actualReleaseDateTime, LocalDate commitDate, LocalDate scheduledReleaseDate, Boolean inProcessIndicator, Boolean mistakenBookingIndicator ) {
        log.debug("Inserting row into booking table");

        final String personInsertStatement="INSERT into BOOKING (person_id, booking_datetime, block, bed, cell, booking_number, facility, case_status, inmate_worker_indicator, inmate_work_release_indicator, probationer_indicator, incarcerated_indicator, actual_release_datetime, commit_date, scheduled_release_date, in_process_indicator, mistaken_booking_indicator) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(personInsertStatement, new String[] {"id"});
        	            ps.setInt(1, personID);
        	            
        	            Timestamp bookingDateTimeTimestamp = null;
        	            
        	            if (bookingDateTime != null)
        	            {	
        	            	bookingDateTimeTimestamp = Timestamp.valueOf(bookingDateTime);
        	            }

        	            ps.setTimestamp(2, bookingDateTimeTimestamp);
        	            
        	            ps.setString(3, block);
        	            ps.setString(4, bed);
        	            ps.setString(5, cell);
        	            ps.setString(6, bookingNumber);
        	            ps.setString(7, facility);
        	            ps.setString(8, caseStatus);
        	            
        	            if (inmateWorkerIndicator != null)
        	            {	
        	            	ps.setBoolean(9, inmateWorkerIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(9, java.sql.Types.BOOLEAN);
        	            }	

        	            if (inmateWorkReleaseIndicator != null)
        	            {	
        	            	ps.setBoolean(10, inmateWorkReleaseIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(10, java.sql.Types.BOOLEAN);
        	            }	

        	            if (probationerIndicator != null)
        	            {	
        	            	ps.setBoolean(11, probationerIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(11, java.sql.Types.BOOLEAN);
        	            }	

        	            if (incarceratedIndicator != null)
        	            {	
        	            	ps.setBoolean(12, incarceratedIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(12, java.sql.Types.BOOLEAN);
        	            }	

        	            Timestamp actualReleaseDateTimestamp = null;
        	            
        	            if (actualReleaseDateTime != null)
        	            {	
        	            	actualReleaseDateTimestamp = Timestamp.valueOf(actualReleaseDateTime);
        	            }

        	            ps.setTimestamp(13, actualReleaseDateTimestamp);
        	            
        	            if (commitDate != null)
        	            {	
        	            	ps.setDate(14, Date.valueOf(commitDate));
        	            }	
        	            else
        	            {	
        	            	ps.setNull(14, java.sql.Types.DATE);
        	            }	
        	            
        	            if (scheduledReleaseDate != null)
        	            {	
        	            	ps.setDate(15, Date.valueOf(scheduledReleaseDate));
        	            }
        	            else
        	            {
        	            	ps.setNull(15, java.sql.Types.DATE);
        	            }
        	            
        	            if (inProcessIndicator != null)
        	            {	
        	            	ps.setBoolean(16, inProcessIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(16, java.sql.Types.BOOLEAN);
        	            }
        	            
        	            if (mistakenBookingIndicator != null)
        	            {	
        	            	ps.setBoolean(17, mistakenBookingIndicator);
        	            }
        	            else
        	            {
        	            	ps.setNull(17, java.sql.Types.BOOLEAN);
        	            }          	            
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		}

	@Override
	public int saveCharge(int arrestID, Float bondAmount,
			String bondType, String bondStatus, String nextCourtEventCourtName,
			LocalDateTime nextCourtDate, Integer chargeSequenceNumber,
			String chargeDescription, String chargeStatute, 
			String chargeClassification, String holdingForAgency, LocalDate sentenceDate, String caseJurisdictionCourt) {
        log.debug("Inserting row into charge table");

        final String chargeInsertStatement="INSERT into CHARGE (arrest_id, bond_amount, bond_type, bond_status, next_court_event_court_name, next_court_date, charge_sequence_number, charge_description,statute_or_ordinance_number,charge_category_classification,holding_for_agency,sentence_date, case_jurisdiction_court) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(chargeInsertStatement, new String[] {"arrest_id"," bond_amount","bond_type","bond_status","next_court_event_court_name","next_court_date","charge_sequence_number","charge_description","statute_or_ordinance_number","charge_category_classification","holding_for_agency","sentence_date","case_jurisdiction_court"});
        	            ps.setInt(1, arrestID);
        	            
        	            if (bondAmount != null)
        	            {	
        	            	ps.setFloat(2, bondAmount);
        	            }
        	            else
        	            {
        	            	ps.setNull(2, java.sql.Types.FLOAT);
        	            }	
        	            
        	            ps.setString(3, bondType);
        	            ps.setString(4, bondStatus);
        	            ps.setString(5, nextCourtEventCourtName);
        	            
        	            if (nextCourtDate != null)
        	            {	
        	            	ps.setTimestamp(6, Timestamp.valueOf(nextCourtDate));
        	            }
        	            else
        	            {
        	            	ps.setNull(6, java.sql.Types.DATE);
        	            }	
           	            
        	            if (chargeSequenceNumber != null)
        	            {	
        	            	ps.setInt(7, chargeSequenceNumber);
        	            }
        	            else
        	            {
        	            	ps.setNull(7, java.sql.Types.INTEGER);
        	            }	
        	            	
           	            ps.setString(8, chargeDescription);
           	            ps.setString(9, chargeStatute);
           	            ps.setString(10, chargeClassification);
           	            ps.setString(11, holdingForAgency);
           	            
        	            if (sentenceDate != null)
        	            {	
        	            	ps.setDate(12, Date.valueOf(sentenceDate));
        	            }	
        	            else
        	            {	
        	            	ps.setNull(12, java.sql.Types.DATE);
        	            }	
           	            
           	            ps.setString(13, caseJurisdictionCourt);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();		
	}

	@Override
	public int saveArrest(int bookingID, String arrestUniqueIdentifier, String arrestingAgency) {
		log.debug("Inserting row into arrest table");

        final String arrestInsertStatement="INSERT into Arrest (booking_id, arrest_unique_identifier, arrest_agency) values (?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(arrestInsertStatement, new String[] {"id"});
        	            ps.setInt(1, bookingID);
        	            ps.setString(2, arrestUniqueIdentifier);
        	            ps.setString(3, arrestingAgency);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
		
	}

	@Override
	public int saveScarsMarksTattoos(int personID,
			String scarsMarksTattoosDescription) {
		
        log.debug("Inserting row into scars/marks/tattoos table");

        final String smtInsertStatement="INSERT into SCARS_MARKS_TATTOOS (person_id, scars_marks_tattoos_description) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(smtInsertStatement, new String[] {"person_id", "scars_marks_tattoos_description"});
        	            ps.setInt(1, personID);
        	            ps.setString(2, scarsMarksTattoosDescription);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}
	
	@Override
	public int saveConditions(int personID, String conditions) {
        log.debug("Inserting row into conditions table");

        final String conditionsInsertStatement="INSERT into CONDITIONS (person_id, conditions_description) values (?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(conditionsInsertStatement, new String[] {"person_id", "conditions_description"});
        	            ps.setInt(1, personID);
        	            ps.setString(2, conditions);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}	
	
	@Override
	public void updateReleaseDateTime(String bookingNumber,
			LocalDateTime releaseDateTime) {
		String sql = "UPDATE booking set actual_release_datetime = ? where booking_number = ?";
		
		Timestamp releaseDateTimestamp = Timestamp.valueOf(releaseDateTime);
		
		jdbcTemplate.update(sql, releaseDateTimestamp, bookingNumber);
		
		log.debug("Updating booking number: " + bookingNumber + " with release date time: " + releaseDateTime.toString());
	}

	@Override
	public void updateBookingStatusIndicators(String bookingNumber, Boolean inProcessIndicator,
			Boolean mistakenBookingIndicator) {
		String sql = "UPDATE booking set in_process_indicator = ?, mistaken_booking_indicator=? where booking_number = ?";
		
		jdbcTemplate.update(sql, inProcessIndicator, mistakenBookingIndicator, bookingNumber);
		
		log.debug("Updating booking number: " + bookingNumber + " with indicators in process: " + inProcessIndicator + ", mistaken: " + mistakenBookingIndicator);
		
	}

	@Override
	public List<BookingPerson> returnBookingsWithNoReleaseDate() {
		String sql = "SELECT p.person_unique_identifier, b.booking_number, b.actual_release_datetime, b.id as bookingId " +
						"FROM person p, booking b " +
						"where p.id = b.person_id " +
						"and b.actual_release_datetime is null OR b.actual_release_datetime = ''";
		
		List<BookingPerson> bookings = this.jdbcTemplate.query(sql, new BookingPersonRowMapper());
		
		return bookings;
	}

	@Override
	public int saveArrestLocation(int arrestID, String addressSecondaryUnit,
			String streetNumber, String streetName, String city,
			String stateCode, String postalCode) {

		log.debug("Inserting row into arrest location table");

        final String arrestInsertStatement="INSERT into Location (arrest_id, address_secondary_unit, street_number, street_name, city, state_code, postal_code) values (?,?,?,?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(arrestInsertStatement, new String[] {"id"});
        	            ps.setInt(1, arrestID);
        	            ps.setString(2, addressSecondaryUnit);
        	            ps.setString(3, streetNumber);
        	            ps.setString(4, streetName);
        	            ps.setString(5, city);
        	            ps.setString(6, stateCode);
        	            ps.setString(7, postalCode);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
	}

	@Override
	public List<Person> getPersonList(CustodyPersonSearchRequest personSearchRequest) {
		
		String sql = "SELECT p1.* FROM person p1 WHERE p1.id IN"
				+ " (SELECT DISTINCT (p.id) FROM person p LEFT JOIN person_alias pa"
				+ " ON pa.person_id = p.id"
				+ " WHERE (:first_name IS NULL OR p.first_name LIKE :first_name)"
				+ " AND (:last_name IS NULL OR p.last_name LIKE :last_name)"
				+ " AND (:middle_name IS NULL OR p.middle_name = :middle_name)"
				+ " AND (:sex IS NULL OR p.sex = :sex)"
				+ " AND (:sid IS NULL OR p.sid = :sid)"
				+ " AND (:race IS NULL OR p.race = :race)"								
				+ " AND (:eye_color IS NULL OR p.eye_color = :eye_color)"
				+ " AND (:hair_color IS NULL OR p.hair_color = :hair_color)"				
				+ " AND (:height IS NULL OR p.height = :height)"
				+ " AND (:weight IS NULL OR p.weight = :weight)"				
				+ " AND (:heightRangeMin IS NULL OR p.height >= :heightRangeMin)"
				+ " AND (:heightRangeMax IS NULL OR p.height <= :heightRangeMax)"
				+ " AND (:weightRangeMin IS NULL OR p.weight >= :weightRangeMin)"
				+ " AND (:weightRangeMax IS NULL OR p.weight <= :weightRangeMax)"
				+ " AND (:dob IS NULL OR p.dob = :dob)"
				+ " AND (:dobRangeStart IS NULL OR p.dob >= :dobRangeStart)"
				+ " AND (:dobRangeEnd IS NULL OR p.dob <= :dobRangeEnd)"
				+ " AND (:alternateSurName IS NULL OR pa.alias_last_name = :alternateSurName)"
				+ " AND (:alternateGivenName IS NULL OR pa.alias_first_name = :alternateGivenName))";
				
		Map<String, Object> namedParamMap = new HashMap<String, Object>();

		boolean givenNameHasStartsWithQualifier = personSearchRequest.isGivenNameHasStartsWithQualifier();
		
		if(givenNameHasStartsWithQualifier){		
			namedParamMap.put("first_name", personSearchRequest.getGivenName() + "%");
		}else{
			namedParamMap.put("first_name", personSearchRequest.getGivenName());
		}
				
		boolean surNameHasStartsWithQualifier = personSearchRequest.isSurNameHasStartsWithQualifier();
		
		if(surNameHasStartsWithQualifier){		
			namedParamMap.put("last_name", personSearchRequest.getSurName() + "%");
		}else{
			namedParamMap.put("last_name", personSearchRequest.getSurName());
		}
								
		namedParamMap.put("middle_name", personSearchRequest.getMiddleName());		
		namedParamMap.put("sex", personSearchRequest.getSexCode());
		namedParamMap.put("sid", personSearchRequest.getStateId());
		namedParamMap.put("race", personSearchRequest.getRaceCode());		
		namedParamMap.put("eye_color", personSearchRequest.getEyeColor());
		namedParamMap.put("hair_color", personSearchRequest.getHairColor());		
		namedParamMap.put("height", personSearchRequest.getHeight());
		namedParamMap.put("weight", personSearchRequest.getWeight());				
		namedParamMap.put("heightRangeMin", personSearchRequest.getHeightRangeMin());
		namedParamMap.put("heightRangeMax", personSearchRequest.getHeightRangeMax());		
		namedParamMap.put("weightRangeMin", personSearchRequest.getWeightRangeMin());
		namedParamMap.put("weightRangeMax", personSearchRequest.getWeightRangeMax());				
		namedParamMap.put("dob", personSearchRequest.getDob());	
		namedParamMap.put("dobRangeStart", personSearchRequest.getDobRangeStart());
		namedParamMap.put("dobRangeEnd", personSearchRequest.getDobRangeEnd());		
		namedParamMap.put("alternateGivenName", personSearchRequest.getAlternateGivenName());
		namedParamMap.put("alternateSurName", personSearchRequest.getAlternateSurName());				
				
		List<Person> personList = namedParameterJdbcTemplate.query(sql, namedParamMap, 
				new BeanPropertyRowMapper<Person>(Person.class));
						
		// simpler than vendor-neutral sql
		List<Person> rFilteredPersonList = filterPesonResultsOnAgeRange(personList, personSearchRequest);
		
		populatePersonSupportingObjects(rFilteredPersonList);
		
		return rFilteredPersonList;
	}
	
		
	public Person custodyQuery(int bookingId){
						
		Booking booking = getBooking(bookingId);
		
		Integer personId = booking.getPersonId();
		
		String personSql = "SELECT * FROM person WHERE id = ?";		
		
		Person person = (Person)jdbcTemplate.query(personSql,
				new BeanPropertyRowMapper<Person>(Person.class), personId);
		
		List<PersonAlias> personAliasList = getPersonAliasList(personId);		
		person.setPersonAliasList(personAliasList);
		
		List<Booking> bookingList = Arrays.asList(booking);						
		person.setBookingList(bookingList);		
		
		List<ScarsMarksTattoos> scarsMarksTattoosList = getScarsMarksTatoosListForPersonId(personId);
		person.setScarsMarksTatoosList(scarsMarksTattoosList);
		
		return person;
	}
	
	
	@Override
	public Person getPersonFromUniqueId(String personUniqueIdentifier) {
		
		String sql = "SELECT * FROM person WHERE person_unique_identifier = ?";
		
		List<Person> personList = (List<Person>)jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Person>(Person.class), personUniqueIdentifier);
				
		Person person = null;
		
		if(personList != null && !personList.isEmpty() && personList.size() == 1){
			
			person = personList.get(0);
			
			populatePersonSupportingObjects(person);				
		
		}else{
		
			log.warn("Person Search Result Size not equal to one!");
		}
								
		return person;
	}
	
	
	@Override
	public Booking getBooking(int bookingId) {
		
		String sql = "SELECT * FROM booking WHERE id = ?";
		
		Booking booking = (Booking)jdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<Booking>(Booking.class), bookingId);
		
		if(booking != null){
			
			List<Arrest> arrestList = getArrestListForBookingId(booking.getId());
			
			booking.setArrestList(arrestList);
		}
		
		return booking;
	}	
	
	@Override
	public List<ScarsMarksTattoos> getScarsMarksTatoosListForPersonId(
			int personId) {

		String sql = "SELECT * FROM scars_marks_tattoos WHERE person_id = ? order by scars_marks_tattoos_description";
		
		List<ScarsMarksTattoos> scarsMarksTattoosList =  jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<ScarsMarksTattoos>(ScarsMarksTattoos.class), personId);
		
		return scarsMarksTattoosList;
	}	
	
	@Override
	public List<Conditions> getConditionsListForPersonId(
			int personId) {

		String sql = "SELECT * FROM conditions WHERE person_id = ? order by conditions_description";
		
		List<Conditions> conditionsList =  jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<Conditions>(Conditions.class), personId);
		
		return conditionsList;
	}	
	
	@Override
	public List<Booking> getBookingListForPersonId(int personId) {

		String sql = "SELECT * FROM booking WHERE person_id = ? order by booking_number";
		
		List<Booking> bookingList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Booking>(Booking.class), personId);
		
		for(Booking iBooking : bookingList){
			
			List<Arrest> arrestList = getArrestListForBookingId(iBooking.getId());
			
			iBooking.setArrestList(arrestList);							
		}		
						
		return bookingList;
	}
	
	@Override
	public List<Arrest> getArrestListForBookingId(int bookingId) {

		String sql = "SELECT * FROM arrest WHERE booking_id = ?";
		
		List<Arrest> arrestList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Arrest>(Arrest.class), bookingId);
				
		if (arrestList != null)
		{	
			for(Arrest arrest : arrestList){
				
				List<Charge> chargeList = getChargeListForArrestId(arrest.getId());
				
				arrest.setChargeList(chargeList);
				
				Location location = getLocationForArrestId(arrest.getId());
				arrest.setLocation(location);
			}
		}	
			
		return arrestList;
	}	
	
	
	public Location getLocationForArrestId(Integer arrestId) {
		String sql = "SELECT * FROM location WHERE arrest_id = ?";
		
		List<Location> locationList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Location>(Location.class), arrestId);
		
		if (locationList == null  || locationList.size() !=1)
		{
			return null;
		}	
		
		return locationList.get(0);
	}


	@Override
	public List<Charge> getChargeListForArrestId(int arrestId) {

		String sql = "SELECT * FROM charge WHERE arrest_id = ? order by charge_sequence_number";
		
		List<Charge> chargeList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Charge>(Charge.class), arrestId);
		
		return chargeList;
	}
	

	@Override
	public List<PersonAlias> getPersonAliasList(int personId) {

		String sql = "SELECT * FROM person_alias WHERE person_id = ?";
		
		List<PersonAlias> personAliasList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<PersonAlias>(PersonAlias.class), personId);
		
		return personAliasList;
	}	
	
	String ALIAS_DELETE_QUERY = "delete from person_alias where person_id = ?";
	@Override
	public int deleteAliasesForPersonID(int personId) {
		int resultSize = this.jdbcTemplate.update(ALIAS_DELETE_QUERY, personId);
	
		return resultSize;
	}
	
	@Override
	public Integer getBookingIDFromBookingNumber(String bookingNumber) {
		String sql = "SELECT id FROM booking WHERE booking_number = ?";
		
        Integer bookingId;
		try {
			bookingId = getJdbcTemplate().queryForObject(sql, Integer.class, bookingNumber);
		} catch (DataAccessException e) {
			return null;
		}

		return bookingId;
	}
	
	@Override
	public void deleteBookingRecord(String bookingNumber) {
		
		jdbcTemplate.update("DELETE FROM booking WHERE booking_number = ?", bookingNumber);

	}	
	
	private void populatePersonSupportingObjects(List<Person> personList){
		
		for(Person person : personList){
			
			populatePersonSupportingObjects(person);			
		}		
	}
		
	private void populatePersonSupportingObjects(Person person){
					
		List<PersonAlias> personAliasList = getPersonAliasList(person.getId());			
		person.setPersonAliasList(personAliasList);
		
		List<Booking> bookingList = getBookingListForPersonId(person.getId());
		person.setBookingList(bookingList);		
		
		List<ScarsMarksTattoos> scarsMarksTatoosList = getScarsMarksTatoosListForPersonId(person.getId());		
		person.setScarsMarksTatoosList(scarsMarksTatoosList);
		
		List<Conditions> conditionsList = getConditionsListForPersonId(person.getId());
		person.setConditionsList(conditionsList);
	}	
	
	
	List<Person> filterPesonResultsOnAgeRange(List<Person> personList, CustodyPersonSearchRequest personSearchRequest){
		
		List<Person> rPersonResultsList = new ArrayList<Person>();
		
		Integer ageMinSearch = personSearchRequest.getAgeMin();
		boolean hasAgeMinSearch = ageMinSearch != null;
		
		Integer ageMaxSearch = personSearchRequest.getAgeMax();
		boolean hasAgeMaxSearch = ageMaxSearch != null;
		
		for(Person person : personList){
		
			Integer personAge = CustodyUtils.getAge(person.getDob());
			
			if((hasAgeMinSearch && (personAge == null || personAge < ageMinSearch)) 
					|| (hasAgeMaxSearch && (personAge == null || personAge > ageMaxSearch))){
				
				//exclude from results
				continue;				
			}else{
				rPersonResultsList.add(person);
			}
		}
		return rPersonResultsList;
	}
	
    public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void truncateAllTables() {
		jdbcTemplate.update("DELETE FROM person");
		
	}

	String CONDITIONS_DELETE_QUERY = "delete from conditions where person_id = ?";
	@Override
	public int deleteConditionsForPersonID(int personID) {
		int resultSize = this.jdbcTemplate.update(CONDITIONS_DELETE_QUERY, personID);
		
		return resultSize;
	}

	String SMT_DELETE_QUERY = "delete from scars_marks_tattoos where person_id = ?";
	@Override
	public int deleteScarsMarksTattoosForPersonID(int personID) {
		int resultSize = this.jdbcTemplate.update(SMT_DELETE_QUERY, personID);
		
		return resultSize;
	}

	@Override
	public Charge getChargeForArrestIdAndChargeSequenceNumberAndSentenceDate(int arrestId, int chargeSequenceNumber, LocalDate sentenceDate) throws Exception {

		String sql = "SELECT * FROM charge WHERE arrest_id = ? and charge_sequence_number = ? and sentence_date = ?";
		
		List<Charge> chargeList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Charge>(Charge.class), arrestId, chargeSequenceNumber,  Date.valueOf(sentenceDate));
	
		if (chargeList == null)
		{
			return null;
		}	
		
		if (chargeList.size() == 0)
		{
			return null;
		}	
		
		if (chargeList.size() > 1)
		{
			log.error("There should only one charge per arrest ID and charge sequence number");
			throw new Exception("There should only one charge per arrest ID and charge sequence number");
		}	
		
		return chargeList.get(0);	
		
	}


}
