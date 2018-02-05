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
package org.ojbc.intermediaries.sn.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml", })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class TestRetreiveExpiringSubscriptions {

	private static final Log log = LogFactory
			.getLog(TestSubscriptionSearchQueryDAO.class);

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;


    //This is used to update database to achieve desired state for test
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}
	
	@Test
	public void testSearchForExpiringAndInvalidSubscriptions()
	{
		List<String> oris = new ArrayList<String>();
		
		oris.add("1234567890");
		
		int dayThreshold = 2;
		
    	DateTime now = new DateTime();
    	DateTime updatedEndDate= now.plusDays(1);
    	String updatedEndDateAsString = updatedEndDate.toString("yyyy-MM-dd");

    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set enddate='" + updatedEndDateAsString  + "', agency_case_number=12345 where ID ='62724'");
    	assertEquals(1, rowsUpdated);

    	rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set validationduedate='" + updatedEndDateAsString  + "', agency_case_number=56789 where ID ='62725'");
    	assertEquals(1, rowsUpdated);

		List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiringAndInvalidSubscriptions(oris, dayThreshold,"{http://demostate.gov/SystemNames/1.0}SystemC");
		
		log.info(subscriptions.size());
		assertEquals(2, subscriptions.size());
		
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());
		assertEquals("56789",subscriptions.get(1).getAgencyCaseNumber());
	}
	
	@Test
	public void testSearchForExpiredAndInvalidSubscriptions()
	{
		List<String> oris = new ArrayList<String>();
		
		oris.add("1234567890");
		
		int dayThreshold = 2;
		
    	DateTime now = new DateTime();
    	DateTime updatedEndDate= now.minusDays(1);
    	String updatedEndDateAsString = updatedEndDate.toString("yyyy-MM-dd");

    	//We will use these subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set enddate='" + updatedEndDateAsString  + "', agency_case_number='12345' where ID ='62724'");
    	assertEquals(1, rowsUpdated);
    	
    	rowsUpdated = this.jdbcTemplate.update("update SUBSCRIPTION set validationduedate='" + updatedEndDateAsString  + "', agency_case_number=56789 where ID ='62725'");
    	assertEquals(1, rowsUpdated);    	
    	
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiredAndInvalidSubscriptions(oris, dayThreshold,"{http://demostate.gov/SystemNames/1.0}SystemC");
		
		log.info(subscriptions.size());
		
		assertEquals(2, subscriptions.size());
		assertEquals("12345",subscriptions.get(0).getAgencyCaseNumber());
		assertEquals("56789",subscriptions.get(1).getAgencyCaseNumber());
		
	}
}
