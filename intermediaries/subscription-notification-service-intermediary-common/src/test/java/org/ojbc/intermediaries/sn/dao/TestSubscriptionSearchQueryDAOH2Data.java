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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.topic.rapback.RapbackSubscriptionRequest;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Basic unit test for the subscription search query DAO. This class will use H2 database as is 
 * defined in h2-mock-database-context-rapback-datastore.xml 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class TestSubscriptionSearchQueryDAOH2Data {

	private static final Log log = LogFactory
			.getLog(TestSubscriptionSearchQueryDAOH2Data.class);

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

	private ValidationDueDateStrategy springConfiguredStrategy;

    //This is used to update database to achieve desired state for test
    @SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
		springConfiguredStrategy = subscriptionSearchQueryDAO
				.getValidationDueDateStrategy();
	}

	@After
	public void tearDown() {
		subscriptionSearchQueryDAO
				.setValidationDueDateStrategy(springConfiguredStrategy);
	}


	@Test
	@DirtiesContext
	public void testSubscribe_noExistingSubscriptions() throws Exception {

		Statement s = dataSource.getConnection().createStatement();

		Map<String, String> subjectIds = new HashMap<String, String>();
		subjectIds.put(SubscriptionNotificationConstants.SID, "1234");
		subjectIds.put(
				SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
				"ABCDE");

		Map<String, String> subscriptionProperties = new HashMap<String, String>();
		subscriptionProperties.put("prop 1", "value 1");
		subscriptionProperties.put("prop 2", "value 2");
		
		ResultSet rs = s.executeQuery("select * from subscription");

		int recordCount = 0;
		while (rs.next()) {
			recordCount++;
		}

		SubscriptionRequest request = buildSubscriptionRequest(subjectIds,
				subscriptionProperties);
		
		request.setSubscriptionOwnerOri("1234567890");
		
		LocalDate currentDate = new LocalDate();
		int subscriptionId = subscriptionSearchQueryDAO.subscribe(request, currentDate).intValue();

		rs = s.executeQuery("select s.*, so.federation_id as subscriptionOwner, so.email_address as  subscriptionOwnerEmailAddress, ap.agency_ori as ori from subscription s, "
				+ "subscription_owner so, agency_profile ap where  s.subscription_owner_id = so.subscription_owner_id"
				+ " and ap.AGENCY_ID = so.AGENCY_ID");

		int postRecordCount = 0;
		while (rs.next()) {
			postRecordCount++;
			int id = rs.getInt("ID");
			if (id == subscriptionId) {
				assertEquals("topic", rs.getString("TOPIC"));
				DateTime d = new DateTime(rs.getDate("STARTDATE"));
				assertEquals(2013, d.getYear());
				assertEquals(1, d.getMonthOfYear());
				assertEquals(1, d.getDayOfMonth());
				d = new DateTime(rs.getDate("ENDDATE"));
				assertEquals(2013, d.getYear());
				assertEquals(1, d.getMonthOfYear());
				assertEquals(1, d.getDayOfMonth());
				assertEquals("systemName",
						rs.getString("SUBSCRIBINGSYSTEMIDENTIFIER"));
				assertEquals("offenderName", rs.getString("SUBJECTNAME"));
				assertEquals(1, rs.getByte("ACTIVE"));
				Date lastValidationDateColValue = rs
						.getDate("lastValidationDate");
				assertNotNull(lastValidationDateColValue);
				DateTime lastValidationDate = new DateTime(
						lastValidationDateColValue);
				assertEquals(currentDate.toDateTimeAtStartOfDay().toDate(),
						lastValidationDate.toDate());
				assertEquals("0123ABC", rs.getString("agency_case_number"));
				assertEquals("SYSTEM", rs.getString("subscriptionOwner"));
				assertEquals("admin@local.gov", rs.getString("subscriptionOwnerEmailAddress"));
				assertEquals("1234567890", rs.getString("ori"));
				
			}
		}

		assertEquals(1, postRecordCount - recordCount);

		rs.close();
		rs = s.executeQuery("select * from notification_mechanism where subscriptionid="
				+ subscriptionId);

		postRecordCount = 0;
		while (rs.next()) {
			postRecordCount++;
			assertEquals(NotificationConstants.NOTIFICATION_MECHANISM_EMAIL,
					rs.getString("NOTIFICATIONMECHANISMTYPE"));
			assertEquals("none@none.com", rs.getString("NOTIFICATIONADDRESS"));
		}

		assertEquals(1, postRecordCount);

		rs.close();
		rs = s.executeQuery("select * from subscription_subject_identifier where subscriptionid="
				+ subscriptionId);
		// ResultSetMetaData rsmd = rs.getMetaData();
		// for (int i=0;i < rsmd.getColumnCount();i++) {
		// log.info(rsmd.getColumnLabel(i+1) + ", " +
		// rsmd.getColumnClassName(i+1));
		// }

		postRecordCount = 0;
		while (rs.next()) {
			postRecordCount++;
			String identifierName = rs.getString("IdentifierName");
			if ("SID".equals(identifierName)) {
				assertEquals("1234", rs.getString("IdentifierValue"));
			} else if ("subscriptionQualifier".equals(identifierName)) {
				assertEquals("ABCDE", rs.getString("IdentifierValue"));
			} else {
				throw new IllegalStateException("Unexpected identifier: "
						+ identifierName);
			}
		}

		assertEquals(2, postRecordCount);
		
		rs = s.executeQuery("select * from subscription_properties where subscriptionid="
				+ subscriptionId);

		postRecordCount = 0;
		while (rs.next()) {
			postRecordCount++;
			String identifierName = rs.getString("PROPERTYNAME");
			if ("prop 1".equals(identifierName)) {
				assertEquals("value 1", rs.getString("PROPERTYVALUE"));
			} else if ("prop 2".equals(identifierName)) {
				assertEquals("value 2", rs.getString("PROPERTYVALUE"));
			} else {
				throw new IllegalStateException("Unexpected identifier: "
						+ identifierName);
			}
		}

		assertEquals(2, postRecordCount);

		s.close();

	}

	private SubscriptionRequest buildSubscriptionRequest(Map<String, String> subjectIds,
			Map<String, String> subscriptionProperties) {
		SubscriptionRequest request = new RapbackSubscriptionRequest();
        request.setSubscriptionSystemId(null) ;
		request.setTopic("topic");
		request.setStartDateString("2013-01-01");
		request.setEndDateString("2013-01-01"); 
		request.setSubjectIdentifiers(subjectIds); 
		request.setSubscriptionProperties(subscriptionProperties); 
		request.setEmailAddresses(new HashSet<String>(Arrays.asList("none@none.com")));
        request.setSubjectName("offenderName"); 
        request.setSystemName("systemName"); 
        request.setSubscriptionQualifier("ABCDE"); 
        request.setReasonCategoryCode("CI");  
        request.setSubscriptionOwner("SYSTEM"); 
        request.setSubscriptionOwnerEmailAddress("ownerEmail@local.gov");
        request.setSubscriptionOwnerOri("1234567890");
        request.setSubscriptionOwnerFirstName("bill");
        request.setSubscriptionOwnerLastName("smith");
        request.setAgencyCaseNumber("0123ABC");
        request.setOri("1234567890");
		return request;
	}

	@Test
	@DirtiesContext
	public void testUnsubscribeCivilSubscription()
			throws Exception {
		
		Statement statement = dataSource.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("select * from identification_transaction where subscription_id = '62724'");
		assertTrue(rs.next());
		Date availableForSubscriptionStartDate = rs.getDate("AVAILABLE_FOR_SUBSCRIPTION_START_DATE");
		log.info("availableForSubscriptionStartDate before unsubscribe: " + availableForSubscriptionStartDate);
		assertTrue(DateUtils.isSameDay(availableForSubscriptionStartDate, XmlUtils.parseXmlDate("2015-10-16").toDate()));
		
		subscriptionSearchQueryDAO
			.unsubscribe("62724","{http://ojbc.org/wsn/topics}:person/arrest", null, null, null);
		
		ResultSet rsAfter = statement.executeQuery("select * from identification_transaction where subscription_id = '62724'");
		assertTrue(rsAfter.next());
		Date availableForSubscriptionStartDateAfter = rsAfter.getDate("AVAILABLE_FOR_SUBSCRIPTION_START_DATE");
		log.info("availableForSubscriptionStartDate after unsubscribe: " + availableForSubscriptionStartDateAfter);
		assertTrue(DateUtils.isSameDay(availableForSubscriptionStartDateAfter, Calendar.getInstance().getTime()));
	}
	
	@Test
	public void testReturnAllAgencies() throws Exception
	{
		List<AgencyProfile> agencyProfiles = subscriptionSearchQueryDAO.returnAllAgencies();
		
		assertEquals(3, agencyProfiles.size());
		
		AgencyProfile agency1 = agencyProfiles.get(0);
		
		assertEquals("Demo Agency",agency1.getAgencyName());
		assertEquals("1234567890",agency1.getAgencyOri());
		assertEquals(false,agency1.getCivilAgencyIndicator());
		assertEquals(true,agency1.getFbiSubscriptionQualification());
		
	}
	
	@Test
	@DirtiesContext
	public void testSubscribe_noExistingCivilSubscriptions() throws Exception {

		Statement s = dataSource.getConnection().createStatement();
		
		s.execute("insert into AGENCY_PROFILE(AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION, CIVIL_AGENCY_INDICATOR ) values ('B23456789', 'Demo Agency', true, false)");
		
		ResultSet rs = s.executeQuery("select * from identification_transaction where TRANSACTION_NUMBER = '000001820140729014008339997'");
		assertTrue(rs.next());
		Date availableForSubscriptionStartDate = rs.getDate("AVAILABLE_FOR_SUBSCRIPTION_START_DATE");
		log.info("availableForSubscriptionStartDate before subscribe: " + availableForSubscriptionStartDate);
		assertTrue(DateUtils.isSameDay(availableForSubscriptionStartDate, Calendar.getInstance().getTime()));

		Map<String, String> subjectIds = new HashMap<String, String>();
		subjectIds.put(SubscriptionNotificationConstants.SID, "A023460");
		subjectIds.put(
				SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
				"ABCDE");

		ResultSet rsCountBefore = s.executeQuery("select count(*) as count from subscription");
		assertTrue(rsCountBefore.next());
		int recordCount = rsCountBefore.getInt("count");

		LocalDate currentDate = new LocalDate();
		SubscriptionRequest request = buildSubscriptionRequest(subjectIds, null);
		request.setSubscriptionOwnerOri("B23456789");
		request.setTopic("{http://ojbc.org/wsn/topics}:person/arrest");
		request.setStartDateString("2015-11-03");
		request.setEndDateString("2016-11-02");
		request.setReasonCategoryCode("I");
		request.setAgencyCaseNumber("000001820140729014008339997");
		
		subscriptionSearchQueryDAO.subscribe(request, currentDate).intValue();

		ResultSet rsCountAfter = s.executeQuery("select count(*) as count from subscription");
		assertTrue(rsCountAfter.next());

		int postRecordCount = rsCountAfter.getInt("count");
		assertEquals(1, postRecordCount - recordCount);
		rs.close();
		
		ResultSet rsAvalibaleDateAfterSubscribe = s.executeQuery("select * from identification_transaction where TRANSACTION_NUMBER = '000001820140729014008339997'");
		assertTrue(rsAvalibaleDateAfterSubscribe.next());
		Date availableForSubscriptionStartDateAfter = rsAvalibaleDateAfterSubscribe.getDate("AVAILABLE_FOR_SUBSCRIPTION_START_DATE");
		log.info("availableForSubscriptionStartDate after subscribe: " + availableForSubscriptionStartDateAfter);
		assertTrue(DateUtils.isSameDay(availableForSubscriptionStartDateAfter, XmlUtils.parseXmlDate("2016-11-03").toDate()));
	}


}
