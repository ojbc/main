package org.ojbc.booking.common.dao.model;

import java.time.LocalDateTime;

public class BookingPerson {
	private LocalDateTime actualReleaseDateTime;
	private String bookingNumber;
	private String personUniqueIdentifier;
	private Integer bookingPk;

	public LocalDateTime getActualReleaseDateTime() {
		return actualReleaseDateTime;
	}

	public void setActualReleaseDateTime(LocalDateTime actualReleaseDateTime) {
		this.actualReleaseDateTime = actualReleaseDateTime;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public String getPersonUniqueIdentifier() {
		return personUniqueIdentifier;
	}

	public void setPersonUniqueIdentifier(String personUniqueIdentifier) {
		this.personUniqueIdentifier = personUniqueIdentifier;
	}

	public Integer getBookingPk() {
		return bookingPk;
	}

	public void setBookingPk(Integer bookingPk) {
		this.bookingPk = bookingPk;
	}
}
