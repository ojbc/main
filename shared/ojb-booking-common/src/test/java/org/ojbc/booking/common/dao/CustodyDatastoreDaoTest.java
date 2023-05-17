package org.ojbc.booking.common.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.booking.common.dao.model.Arrest;
import org.ojbc.booking.common.dao.model.Booking;
import org.ojbc.booking.common.dao.model.Charge;
import org.ojbc.booking.common.dao.model.Conditions;
import org.ojbc.booking.common.dao.model.Person;
import org.ojbc.booking.common.dao.model.PersonAlias;
import org.ojbc.booking.common.dao.model.ScarsMarksTattoos;
import org.ojbc.booking.common.dao.model.request.CustodyPersonSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class CustodyDatastoreDaoTest {
		
	private SimpleDateFormat sdfyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource
	private CustodyDatastoreDAO custodyDatastoreDao;
	
    @SuppressWarnings("unused")
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void init(){
	
		Assert.assertNotNull(custodyDatastoreDao);
	}
	
	@Test
	public void getConditionsListTest(){
		
		List<Conditions> conditionsList = custodyDatastoreDao.getConditionsListForPersonId(1);
		
		Assert.assertNotNull(conditionsList);
		
		Assert.assertTrue(!conditionsList.isEmpty());
	}	
	
	@Test
	public void getScarsMarksTatoosTest(){
		
		List<ScarsMarksTattoos> scarsMarksTattoosList = custodyDatastoreDao.getScarsMarksTatoosListForPersonId(1);
		
		Assert.assertNotNull(scarsMarksTattoosList);
		
		Assert.assertTrue(!scarsMarksTattoosList.isEmpty());
	}
	
	@Test
	public void getBookingListTest(){
		
		List<Booking> bookingList = custodyDatastoreDao.getBookingListForPersonId(1);
		
		Assert.assertNotNull(bookingList);
	}
	
	@Test
	public void getBookingIDFromBookingNumber(){
		
		Integer bookingId = custodyDatastoreDao.getBookingIDFromBookingNumber("1234");
		
		Assert.assertEquals(1, bookingId.intValue());
		
		Assert.assertNotNull(bookingId);
	}	
	
	@Test
	public void testAliasFunctions(){
		
		List<PersonAlias> personAliasList = custodyDatastoreDao.getPersonAliasList(1);
		
		Assert.assertNotNull(personAliasList);		
		
		custodyDatastoreDao.deleteAliasesForPersonID(1);

		personAliasList = custodyDatastoreDao.getPersonAliasList(1);
		Assert.assertNotNull(personAliasList);		

		Assert.assertEquals(0, personAliasList.size());
		
		//Re-add deleted alias
		custodyDatastoreDao.savePersonAlias(1, "screen name", "homy", "jay", "simpson", "M",LocalDate.of(2000, Month.JANUARY, 1));
	}
	
	@Test
	public void getChargeListTest(){
		
		List<Charge> chargeList = custodyDatastoreDao.getChargeListForArrestId(1);
		
		Assert.assertNotNull(chargeList);
		
		Date sentenceDate = chargeList.get(0).getSentenceDate();		
		String sSentenceDate = sdfyyyyMMdd.format(sentenceDate);		
		Assert.assertEquals("2000-01-01", sSentenceDate);
	}
	
		
	@Test
	public void getBookingTest(){
		
		Booking booking = custodyDatastoreDao.getBooking(1);
		
		Assert.assertNotNull(booking);
		
		Assert.assertEquals(1, booking.getPersonId().intValue());
		Assert.assertEquals("1234", booking.getBookingNumber());
		
		Date bookingDate = booking.getBookingDatetime();		
		String sBookingDate = sdfyyyyMMdd.format(bookingDate);		
		Assert.assertEquals("2000-01-01", sBookingDate);
		
		Assert.assertEquals("county jail", booking.getFacility());
		Assert.assertTrue(Arrays.equals("hello".getBytes(), booking.getBookingPhoto()));
		
		Date dReleaseDateTime = booking.getActualReleaseDatetime();		
		String sReleaseDateTime = sdfyyyyMMdd.format(dReleaseDateTime);		
		Assert.assertEquals("2000-01-01", sReleaseDateTime);
		
		Date dCommitDate = booking.getCommitDate();		
		String sCommitDate = sdfyyyyMMdd.format(dCommitDate);		
		Assert.assertEquals("2000-01-01", sCommitDate);
		
		Date scheduledReleaseDate = booking.getScheduledReleaseDate();
		String sScheduledReleaseDate = sdfyyyyMMdd.format(scheduledReleaseDate);		
		Assert.assertEquals("2000-01-01", sScheduledReleaseDate);
		
		Assert.assertEquals("a", booking.getBlock());
		Assert.assertEquals("23", booking.getBed());
		Assert.assertEquals("7", booking.getCell());
		Assert.assertEquals("1", booking.getCaseStatus());
		Assert.assertTrue(booking.getInmateWorkReleaseIndicator());
		Assert.assertTrue(booking.getInmateWorkerIndicator());
		Assert.assertTrue(booking.getProbationerIndicator());
		Assert.assertFalse(booking.getIncarceratedIndicator());
	}
	

	@Test
	public void getPersonListWithPrsnSrchReqNullParamsTest() throws ParseException{
	
		CustodyPersonSearchRequest prsnSrchReq = new CustodyPersonSearchRequest();
		
		prsnSrchReq.setGivenName("homer");

		// leaving many column/params null here on purpose to ensure results still received 
		
		List<Person> personList = custodyDatastoreDao.getPersonList(prsnSrchReq);
		
		Assert.assertNotNull(personList);
		
		Assert.assertTrue(!personList.isEmpty());
	}	

	@Test
	public void saveAndUpdatePerson() throws Exception {
		
		custodyDatastoreDao.savePerson("first", "middle", "last", LocalDate.now(), "M", "A", "W", false, "HSD", "ENG", "student", "HD", "123",  "eye", "hair", 60, 100, true, "1");
		
		custodyDatastoreDao.updatePerson("first1", "middle1", "last1", LocalDate.now(), "F", "D", "W", true, "HSD", "CAN", "IT", "HSD", "1234","eye", "hair", 60, 100, true, "1");
		
		Person person = custodyDatastoreDao.getPersonFromUniqueId("1");
		
		assertEquals("first1", person.getFirstName());
		assertEquals("middle1", person.getMiddleName());
		assertEquals("last1", person.getLastName());
		assertEquals("F", person.getSex());
		assertEquals("D", person.getRace());
		assertEquals(true, person.getSexOffender());
		assertEquals("CAN", person.getPrimaryLanguage());
		assertEquals("IT", person.getOccupation());
		assertEquals("HSD", person.getMilitaryService());
		assertEquals("1234", person.getSid());
		assertEquals("1", person.getPersonUniqueIdentifier());
		
		assertEquals("eye", person.getEyeColor());
		assertEquals("hair", person.getHairColor());
		assertEquals("60", person.getHeight().toString());
		assertEquals("100", person.getWeight().toString());
		assertEquals(true, person.getAllowDeposits());
	}

	@Test
	public void saveAndUpdateBooking() throws Exception {

		Integer bookingNumber = custodyDatastoreDao.saveBooking(1, LocalDateTime.of(2015, 12, 1, 0, 0, 0), "block", "bed", "cell", "facility", "bookingNumber", "caseStatus", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, LocalDateTime.of(2016, 10, 20, 1, 1), LocalDate.of(2015, 12, 2), LocalDate.of(2019, 12, 7),Boolean.TRUE,Boolean.TRUE);
			
		List<Booking> bookings = custodyDatastoreDao.getBookingListForPersonId(1);
		
		assertEquals(2, bookings.size());
		
		Booking bookingToAssert = null;
		
		for (Booking booking : bookings)
		{
			if (booking.getBookingNumber().equals("bookingNumber"))
			{
				bookingToAssert = booking;
			}	
		}	
		
		assertEquals("block",bookingToAssert.getBlock());
		assertEquals("bed",bookingToAssert.getBed());
		assertEquals("cell",bookingToAssert.getCell());
		assertEquals("facility",bookingToAssert.getFacility());
		assertEquals("caseStatus",bookingToAssert.getCaseStatus());
		assertEquals("caseStatus",bookingToAssert.getCaseStatus());
		assertEquals(true,bookingToAssert.getInmateWorkerIndicator());
		assertEquals(false,bookingToAssert.getInmateWorkReleaseIndicator());
		assertEquals(true,bookingToAssert.getProbationerIndicator());
		assertEquals(false,bookingToAssert.getIncarceratedIndicator());
		assertEquals("2015-12-01 00:00:00.0",bookingToAssert.getBookingDatetime().toString());
		assertEquals("2016-10-20 01:01:00.0",bookingToAssert.getActualReleaseDatetime().toString());
		assertEquals("2015-12-02 00:00:00.0",bookingToAssert.getCommitDate().toString());
		assertEquals("2019-12-07 00:00:00.0",bookingToAssert.getScheduledReleaseDate().toString());
		assertEquals(true,bookingToAssert.getInProcessIndicator());
		assertEquals(true,bookingToAssert.getMistakenBookingIndicator());
		
		custodyDatastoreDao.updateBookingStatusIndicators("bookingNumber", false, false);
		
		bookings = custodyDatastoreDao.getBookingListForPersonId(1);
		
		for (Booking booking : bookings)
		{
			if (booking.getBookingNumber().equals("bookingNumber"))
			{
				bookingToAssert = booking;
			}	
		}	
		
		assertEquals(false,bookingToAssert.getInProcessIndicator());
		assertEquals(false,bookingToAssert.getMistakenBookingIndicator());
		
		custodyDatastoreDao.updateBooking(LocalDateTime.of(2014, 12, 1, 0, 0, 0), "block1", "bed1", "cell1", "facility1", "bookingNumber", "caseStatus1", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, LocalDateTime.of(2014, 10, 20, 1, 1),LocalDate.of(2014, 12, 2), LocalDate.of(2014, 12, 7),Boolean.FALSE,Boolean.FALSE);
		bookings = custodyDatastoreDao.getBookingListForPersonId(1);
		
		bookingToAssert = null;
		
		for (Booking booking : bookings)
		{
			if (booking.getBookingNumber().equals("bookingNumber"))
			{
				bookingToAssert = booking;
			}	
		}	
		
		assertEquals("block1",bookingToAssert.getBlock());
		assertEquals("bed1",bookingToAssert.getBed());
		assertEquals("cell1",bookingToAssert.getCell());
		assertEquals("facility1",bookingToAssert.getFacility());
		assertEquals("caseStatus1",bookingToAssert.getCaseStatus());
		assertEquals(false,bookingToAssert.getInmateWorkerIndicator());
		assertEquals(true,bookingToAssert.getInmateWorkReleaseIndicator());
		assertEquals(false,bookingToAssert.getProbationerIndicator());
		assertEquals(true,bookingToAssert.getIncarceratedIndicator());
		assertEquals("2014-12-01 00:00:00.0",bookingToAssert.getBookingDatetime().toString());
		assertEquals("2014-10-20 01:01:00.0",bookingToAssert.getActualReleaseDatetime().toString());
		assertEquals("2014-12-02 00:00:00.0",bookingToAssert.getCommitDate().toString());
		assertEquals("2014-12-07 00:00:00.0",bookingToAssert.getScheduledReleaseDate().toString());
		
		Integer arrestID= custodyDatastoreDao.saveArrest(bookingNumber, "9999999", "Some PD");
		
		custodyDatastoreDao.saveArrestLocation(arrestID, null, null, null, null, null, null);

		List<Arrest> arrests = custodyDatastoreDao.getArrestListForBookingId(bookingNumber);
		
		assertEquals(1, arrests.size());
		
		Arrest arrest = arrests.get(0);
		
		assertEquals("9999999", arrest.getArrestUniqueIdentifier());
		
		assertNotNull(arrest.getLocation());
		
		assertNull(arrest.getLocation().getAddressSecondaryUnit());
		assertNull(arrest.getLocation().getCity());
		assertNull(arrest.getLocation().getPostalCode());
		assertNull(arrest.getLocation().getStateCode());
		assertNull(arrest.getLocation().getStreetName());
		assertNull(arrest.getLocation().getStreetNumber());
		
	}	
}

