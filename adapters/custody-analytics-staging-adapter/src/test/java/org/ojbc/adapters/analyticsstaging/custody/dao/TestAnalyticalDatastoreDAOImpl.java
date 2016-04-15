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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
@DirtiesContext
public class TestAnalyticalDatastoreDAOImpl {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(TestAnalyticalDatastoreDAOImpl.class);
	
    @Resource  
    private DataSource dataSource;  
	
	@Autowired
	private AnalyticalDatastoreDAOImpl analyticalDatastoreDAO;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(analyticalDatastoreDAO);
		Assert.assertNotNull(dataSource);
	}
	
	@Test
	public void testSaveBooking() throws Exception
	{
		int personPk = analyticalDatastoreDAO.savePerson(getStaticPerson());
		assertEquals(1, personPk);
		
		BookingSubject bookingSubject = getStaticBookingSubject(); 
		bookingSubject.setPersonId(personPk);
		
		int bookingSubjectPk = analyticalDatastoreDAO.saveBookingSubject(bookingSubject);
		
		Booking booking = new Booking();
		
		booking.setJurisdictionId(1);
		booking.setBookingReportDate(LocalDateTime.parse("2016-02-13T10:23:23"));
		booking.setBookingReportId("bookingReportId");
		booking.setSendingAgencyId(5);
		booking.setCaseStatusId(3);
		booking.setBookingDate(LocalDateTime.parse("2013-12-17T09:30:00"));
		booking.setCommitDate(LocalDate.parse("2013-12-17"));
		booking.setSupervisionReleaseDate(LocalDateTime.parse("2014-12-17T10:30:00"));
		booking.setPretrialStatusId(3);
		booking.setFacilityId(1);
		booking.setBedTypeId(2);
		booking.setBookingSubjectId(bookingSubjectPk);
		booking.setBookingNumber("bookingNumber");
		booking.setBondAmount(new BigDecimal("500.00"));
		booking.setBondType(new KeyValue(1, "Cash"));
		
		int bookingPk = analyticalDatastoreDAO.saveBooking( booking );
		assertEquals(1, bookingPk);
		
		analyticalDatastoreDAO.deleteBooking(bookingPk);
		
		Booking matchingBooking = analyticalDatastoreDAO.getBookingByBookingReportId("bookingReportId");
		assertNull(matchingBooking);

		bookingSubjectPk = analyticalDatastoreDAO.saveBookingSubject(bookingSubject);
		booking.setBookingSubjectId(bookingSubjectPk);
		
		//Perform an subsequent save and confirm the same PK
		booking.setBookingId(bookingPk);
		int updatedbookingId = analyticalDatastoreDAO.saveBooking(booking);
		assertEquals(updatedbookingId, bookingPk);
		
	}
	
	
	protected Person getStaticPerson() {
		Person person = new Person();
		
		person.setPersonRaceID(1);
		person.setPersonSexID(2);
		person.setPersonBirthDate(LocalDate.parse("1966-06-01"));
		person.setPersonUniqueIdentifier("123332123123unique");
		person.setLanguageId(2);
		return person;
	}
	
	private BookingSubject getStaticBookingSubject(){
		BookingSubject bookingSubject = new BookingSubject();
		bookingSubject.setRecidivistIndicator(0);
		bookingSubject.setPersonAge(50);
		bookingSubject.setEducationLevelId(3);
		bookingSubject.setOccupationId(2);
		bookingSubject.setIncomeLevelId(2);
		bookingSubject.setHousingStatusId(3);
		return bookingSubject;
	}


}
