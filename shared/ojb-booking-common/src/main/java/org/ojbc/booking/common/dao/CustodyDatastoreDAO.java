package org.ojbc.booking.common.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

public interface CustodyDatastoreDAO {

	public int savePerson(String firstName, String middleName, String lastName, LocalDate dateOfBirth, String gender, String race, String ethnicity, Boolean sexOffender, String education, String primaryLanguage, String occupation, String militaryService, String sid, String eyeColor, String hairColor, Integer height, Integer weight, Boolean allowDeposits, String uniquePersonID);
	
	public void updatePerson(String firstName, String middleName, String lastName, LocalDate dateOfBirth, String gender, String race, String ethnicity, Boolean sexOffender, String education, String primaryLanguage, String occupation, String militaryService, String sid, String eyeColor, String hairColor, Integer height, Integer weight, Boolean allowDeposits, String uniquePersonID);
	
	public int savePersonAlias(int personID, String nameType, String firstName, String middleName, String lastName, String sex, LocalDate dateOfBirth);
	
	public int saveBooking(int personID, LocalDateTime bookingDateTime, String block, String bed, String cell, String facility, String bookingNumber, String caseStatus, Boolean inmateWorkerIndicator, Boolean inmateWorkReleaseIndicator, Boolean probationerIndicator, Boolean incarceratedIndicator,  LocalDateTime actualReleaseDate, LocalDate commitDate, LocalDate scheduledReleaseDate, Boolean inProcessIndicator, Boolean mistakenBookingIndicator);
	
	public void updateBooking(LocalDateTime bookingDatetime, String block, String bed, String cell, String facility, String bookingNumber, String caseStatus, Boolean inmateWorkerIndicator, Boolean inmateWorkReleaseIndicator, Boolean probationerIndicator, Boolean incarceratedIndicator,  LocalDateTime actualReleaseDate, LocalDate commitDate, LocalDate scheduledReleaseDate, Boolean inProcessIndicator, Boolean mistakenBookingIndicator);
	
	public int saveCharge(int arrestID, Float bondAmount, String bondType, String bondStatus, String nextCourtEventCourtName, LocalDateTime nextCourtDate, Integer chargeSequenceNumber, String chargeDescription, String chargeStatute, String chargeClassification, String holdingForAgency, LocalDate sentenceDate, String caseJurisdictionCourt);

	public int saveArrest(int bookingID, String arrestUniqueIdentifier, String arrestingAgency);
	
	public int saveArrestLocation(int arrestID, String addressSecondaryUnit, String streetNumber, String streetName, String city, String stateCode, String postalCode);
	
	public int saveScarsMarksTattoos(int personID, String scarsMarksTattoosDescription);
	
	public int saveConditions(int personID, String conditions);
	
	public void updateReleaseDateTime(String bookingNumber, LocalDateTime releaseDateTime);
	
	public List<BookingPerson> returnBookingsWithNoReleaseDate();
	
	public List<Person> getPersonList(CustodyPersonSearchRequest personSearchRequest);
	
	public Person getPersonFromUniqueId(String personUniqueIdentifier);
			
	public List<Booking> getBookingListForPersonId(int personId);
	
	public List<ScarsMarksTattoos> getScarsMarksTatoosListForPersonId(int personId);
	
	public List<Arrest> getArrestListForBookingId(int bookingId);
	
	public List<Charge> getChargeListForArrestId(int arrestId);
	public Charge getChargeForArrestIdAndChargeSequenceNumberAndSentenceDate(int arrestId, int chargeSequenceNumber, LocalDate sentenceDate) throws Exception;
	
	public List<Conditions> getConditionsListForPersonId(int personId);
	
	public List<PersonAlias> getPersonAliasList(int personId);
	
	public Location getLocationForArrestId(Integer arrestId);
	
	public int deleteAliasesForPersonID(int personId);
	
	public int deleteConditionsForPersonID(int personID);
	
	public int deleteScarsMarksTattoosForPersonID(int personID);
	
	public Person custodyQuery(int bookingId);			
			
	public Booking getBooking(int id);
	
	public Integer getBookingIDFromBookingNumber(String bookingNumber);
	
	public void deleteBookingRecord(String bookingNumber);
	
	public void truncateAllTables();

	public void updateBookingStatusIndicators(String bookingNumber,Boolean inProcessIndicator,
			Boolean mistakenBookingIndicator);
}
