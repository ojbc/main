package org.ojbc.booking.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.booking.common.dao.model.BookingPerson;
import org.springframework.jdbc.core.RowMapper;

public class BookingPersonRowMapper implements RowMapper<BookingPerson> {

	private static final Log log = LogFactory.getLog(BookingPersonRowMapper.class);
	
	@Override
	public BookingPerson mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BookingPerson booking = new BookingPerson();
		
		if (rs.getTimestamp("actual_release_datetime") != null)
		{	
			booking.setActualReleaseDateTime(rs.getTimestamp("actual_release_datetime").toLocalDateTime());
		}
		
		booking.setBookingNumber(rs.getString("booking_number"));
		
		try {
			booking.setPersonUniqueIdentifier(rs.getString("person_unique_identifier"));
		} catch (Exception e) {
			log.debug("Person unique identifier not defined for this query.");
		}
		
		booking.setBookingPk(rs.getInt("bookingId"));
		
		return booking;
		
	}
}
