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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.adapter.analyticsstaging.custody.processor;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.TestAnalyticalDatastoreDAOImpl;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.processor.LastUpdateVerificationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao-adams.xml",
		"classpath:META-INF/spring/properties-context-adams.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
@DirtiesContext
public class TestLastUpdateVerificationProcessor {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(TestAnalyticalDatastoreDAOImpl.class);
	
    @Resource  
    private DataSource dataSource;  
	
	@Autowired
	private AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
	@Autowired
	private LastUpdateVerificationProcessor lastUpdateVerificationProcessor;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(analyticalDatastoreDAO);
	}

	@Test
	public void testHasThereBeenABookingInTheLast24Hours()
	{
		assertFalse(lastUpdateVerificationProcessor.hasThereBeenABookingInLast24hours());
		
		int personPk = analyticalDatastoreDAO.savePerson(TestAnalyticalDatastoreDAOImpl.getStaticPerson());
		assertEquals(1, personPk);
		
		Booking booking = new Booking();
		
		booking.setPersonId(personPk);
		booking.setBookingDate(LocalDate.parse("2013-12-17"));
		booking.setBookingTime(LocalTime.parse("09:30:00"));
		booking.setScheduledReleaseDate(LocalDate.parse("2014-12-17"));
		booking.setFacilityId(1);
		booking.setSupervisionUnitTypeId(2);
		booking.setBookingNumber("bookingNumber");
		booking.setInmateJailResidentIndicator(true);
		booking.setBookingStatus("completed");

		int bookingPk = analyticalDatastoreDAO.saveBooking( booking );
		assertEquals(1, bookingPk);
		
		assertTrue(lastUpdateVerificationProcessor.hasThereBeenABookingInLast24hours());
	}

}
