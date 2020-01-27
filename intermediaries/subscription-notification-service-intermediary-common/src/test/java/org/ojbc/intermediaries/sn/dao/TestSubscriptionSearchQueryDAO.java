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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationRequest;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationRequest;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.intermediaries.sn.topic.rapback.RapbackSubscriptionRequest;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Basic unit test for the subscription search query DAO.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class TestSubscriptionSearchQueryDAO {

	private static final Log log = LogFactory
			.getLog(TestSubscriptionSearchQueryDAO.class);

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

	private void loadManualTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_manual.xml");
	}

	@SuppressWarnings("unused")
	private void loadEmptyTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/emptySubscriptionDataSet.xml");
	}

	private void loadAgencyTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/agencyProfileDataSet.xml");
	}

	private void loadWildcardTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_wildcard.xml");
	}

	private void loadBasicTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet.xml");
	}

	private void loadMultiTopicTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSetMultiTopic.xml");
	}

	private void loadValidationDateTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSetValidationDate.xml");
	}

	private void loadNullLastValidationDateTestData() throws Exception {
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_NullLastValidationDate.xml");
	}

	private void loadTestData(final String manualTestFileName)
			throws FileNotFoundException, DatabaseUnitException, SQLException,
			DataSetException {
		FileInputStream manualTestFile = new FileInputStream(manualTestFileName);
		IDatabaseConnection connection = new DatabaseConnection(
				dataSource.getConnection());
		
		FileInputStream emptyDataSetFile = new FileInputStream("src/test/resources/xmlInstances/dbUnit/emptyDataSet.xml");
		DatabaseOperation.DELETE_ALL.execute(connection, new FlatXmlDataSetBuilder().build(emptyDataSetFile));
		DatabaseOperation.CLEAN_INSERT.execute(connection,
				new FlatXmlDataSetBuilder().build(manualTestFile));
	}

	@Test
	public void testAgencyProfileEmailAddresses()
	{
		List<Subscription> subs = subscriptionSearchQueryDAO.queryForSubscription("62723");
		assertEquals(1, subs.size());
		
		Subscription sub = subs.get(0);
		assertEquals("I", sub.getSubscriptionCategoryCode());
		assertEquals("62723", sub.getSubscriptionIdentifier());
		
		List<String> emailAddresses = subscriptionSearchQueryDAO.returnAgencyProfileEmailForSubscription(sub.getSubscriptionIdentifier(), sub.getSubscriptionCategoryCode());
		
		log.info(emailAddresses.toString());
		
		assertEquals(1, emailAddresses.size());
		assertEquals("demo.agency@localhost", emailAddresses.get(0));
		
	}
	
	@Test
	public void testRetrieveSubscriptionOwner() throws Exception
	{
		loadManualTestData();
		
		Integer agencyPk = subscriptionSearchQueryDAO.returnAgencyPkFromORI("1234567890");
		assertEquals(new Integer(1), agencyPk);
		
		Number subNumber = subscriptionSearchQueryDAO.saveSubscriptionOwner("Bill", "Bradley", "bill@bradley.com", "BillUniqueFederationId", "1234567890", "Bill Employer Agency Name");
		
		assertNotNull(subNumber);
		
		Integer subOwner = subscriptionSearchQueryDAO.returnSubscriptionOwnerFromFederationId("BillUniqueFederationId");
		assertNotNull(subOwner);

		subNumber = subscriptionSearchQueryDAO.saveSubscriptionOwner("Bob", "Johnson", "bill@bradley.com", "BobUniqueFederationId", "98789878", "Bill Employer Agency Name 2");
		
		assertNotNull(subNumber);
		
		subOwner = subscriptionSearchQueryDAO.returnSubscriptionOwnerFromFederationId("BobUniqueFederationId");
		assertNotNull(subOwner);

	}
	
	@Test
	public void testUpdateSubscriptionProperties() throws Exception
	{
		loadManualTestData();
		
		Map<String, String> subscriptionPropertiesRequest = null;
		Map<String, String> subscriptionPropertiesDatabase = null;
		
		//both null, no update
		assertFalse(subscriptionSearchQueryDAO.updateSubscriptionProperties(subscriptionPropertiesRequest, subscriptionPropertiesDatabase));
		
		subscriptionPropertiesDatabase = subscriptionSearchQueryDAO.getSubscriptionProperties("1");
		
		//one is null, other isn't, update
		assertTrue(subscriptionSearchQueryDAO.updateSubscriptionProperties(subscriptionPropertiesRequest, subscriptionPropertiesDatabase));
		
		subscriptionPropertiesRequest = new HashMap<String, String>();
		subscriptionPropertiesRequest.put(FederalTriggeringEventCode.ARREST.toString(), FederalTriggeringEventCode.ARREST.toString());
		subscriptionPropertiesRequest.put(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"), FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"));
		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR, "true");
		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "Bill Padmanabhan");
		
		//Both the same, no update
		assertFalse(subscriptionSearchQueryDAO.updateSubscriptionProperties(subscriptionPropertiesRequest, subscriptionPropertiesDatabase));
		
		//Change one, update
		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "Frank Padmanabhan");
		assertTrue(subscriptionSearchQueryDAO.updateSubscriptionProperties(subscriptionPropertiesRequest, subscriptionPropertiesDatabase));

		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "Bill Padmanabhan");
		subscriptionPropertiesDatabase.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "Frank Padmanabhan");
		assertTrue(subscriptionSearchQueryDAO.updateSubscriptionProperties(subscriptionPropertiesRequest, subscriptionPropertiesDatabase));
	}
	
	@Test
	@DirtiesContext
	public void testInsertSubjectIdentifiers() throws Exception
	{
		loadManualTestData();
		
		subscriptionSearchQueryDAO.insertSubjectIdentifier(1, "test", "someValue");
		
		List<Subscription> subs= subscriptionSearchQueryDAO.queryForSubscription("1");
		
		assertEquals(1, subs.size());
		
		assertEquals("someValue",subs.get(0).getSubscriptionSubjectIdentifiers().get("test"));
		
	}	
	
	@Test
	public void testSubscriptionProperties()
			throws Exception {
		loadManualTestData();
		Map<String, String> subscriptionProperties = subscriptionSearchQueryDAO.getSubscriptionProperties("1");
				
		assertEquals(4, subscriptionProperties.size());
		
		assertEquals(FederalTriggeringEventCode.ARREST.toString(), subscriptionProperties.get(FederalTriggeringEventCode.ARREST.toString()));
		assertEquals(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"), subscriptionProperties.get(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-")));
		assertEquals("true", subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR));
		assertEquals("Bill Padmanabhan", subscriptionProperties.get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT));
	
		int rowsDeleted = subscriptionSearchQueryDAO.deleteSubscriptionProperties("1");
		assertEquals(4, rowsDeleted);
		
		subscriptionSearchQueryDAO.saveSubscriptionProperties(subscriptionProperties, 1);
		
		assertEquals(4, subscriptionProperties.size());
		
	}
	
	@Test
	public void testUniqueSubscriptionOwners()
			throws Exception {
		loadManualTestData();
		List<String> uniqueOwners = subscriptionSearchQueryDAO.getUniqueSubscriptionOwners();
				
		assertEquals(1, uniqueOwners.size());
		assertEquals("OJBC:IDP:OJBC:USER:admin", uniqueOwners.get(0));
		
	}
	
	@Test
	@DirtiesContext
	public void testSearchForSubscriptionsBySubscriptionOwner()
			throws Exception {
		loadManualTestData();
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("OJBC:IDP:OJBC:USER:admin");
		assertNotNull(subscriptions);
		assertEquals(2, subscriptions.size());
	}

	@Test
	@DirtiesContext
	public void testSubscriptionBuildWithNoValidationDate() throws Exception {
		loadValidationDateTestData();
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM3");
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		Subscription response = subscriptions.get(0);
		DateTime lastValidationDate = response.getLastValidationDate();
		assertEquals(lastValidationDate, response.getStartDate());
	}

	@Test
	@DirtiesContext
	public void testSearchForSubscriptionsBySubscriptionOwnerWithValidationDate()
			throws Exception {
		loadValidationDateTestData();
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM1");
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		Subscription response = subscriptions.get(0);
		DateTime lastValidationDate = response.getLastValidationDate();
		assertNotNull(lastValidationDate);
		assertEquals(2013, lastValidationDate.getYear());
		assertEquals(8, lastValidationDate.getMonthOfYear());
		assertEquals(27, lastValidationDate.getDayOfMonth());
		
		assertNotNull(response.getFbiRapbackSubscription());
		FbiRapbackSubscription fbiRapbackSubscription = response.getFbiRapbackSubscription();
		assertThat(fbiRapbackSubscription.getFbiSubscriptionId(), is("fbiId1"));
		assertThat(fbiRapbackSubscription.getRapbackCategory(), is("CI"));
		assertThat(fbiRapbackSubscription.getSubscriptionTerm(), is("5"));
		assertThat(fbiRapbackSubscription.getRapbackExpirationDate(), is(java.time.LocalDate.of(2017,9,27)));
		assertThat(fbiRapbackSubscription.getRapbackTermDate(), is(java.time.LocalDate.of(2017,9,27)));
		assertThat(fbiRapbackSubscription.getRapbackStartDate(), is(java.time.LocalDate.of(2012,9,27)));
		assertThat(fbiRapbackSubscription.getRapbackOptOutInState(), is(true));
		assertThat(fbiRapbackSubscription.getRapbackActivityNotificationFormat(), is("1"));
		assertThat(fbiRapbackSubscription.getUcn(), is("074644NG0"));
	}

	@Test
	@DirtiesContext
	public void testMultipleTopicsForSubject() throws Exception {

		loadMultiTopicTestData();

		ArrestNotificationRequest request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5008305Topic1.xml");
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);

		assertNotNull(subscriptions);

		assertEquals(1, subscriptions.size());

	}

	@Test
	@DirtiesContext
	public void testValidationDueDateExemption() throws Exception {

		loadValidationDateTestData();

		// normally you would configure these via Spring, but don't want to muck
		// with other tests...
		StaticValidationDueDateStrategy validationDueDateStrategy = new StaticValidationDueDateStrategy();
		validationDueDateStrategy.setValidDays(10);
		StaticGracePeriodStrategy gracePeriodStrategy = new StaticGracePeriodStrategy();
		gracePeriodStrategy.setGracePeriodDays(10);
		SystemCollectionValidationExemptionFilter validationExemptionFilter = new SystemCollectionValidationExemptionFilter();
		subscriptionSearchQueryDAO
				.setValidationDueDateStrategy(validationDueDateStrategy);
		subscriptionSearchQueryDAO.setGracePeriodStrategy(gracePeriodStrategy);

		ArrestNotificationRequest request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5008308.xml");
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);

		assertNotNull(subscriptions);

		// no subscriptions are returned b/c event date is 10/20/13, last
		// validated date on sub is 10/1/13, and validation due is 10/10/13

		assertEquals(0, subscriptions.size());

		// now we exempt one of the systems from validation, and we should get
		// one of the two subscriptions back (the one corresponding to that
		// system)

		Set<String> systemList = new HashSet<String>();
		final String systemId = "{http://hijis.hawaii.gov/ParoleCase/1.0}Foo";
		systemList.add(systemId);
		validationExemptionFilter.setExemptSystems(systemList);
		subscriptionSearchQueryDAO
				.setValidationExemptionFilter(validationExemptionFilter);

		subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		assertEquals(1, subscriptions.size());
		Subscription subscription = subscriptions.get(0);
		assertEquals(systemId, subscription.getSubscribingSystemIdentifier());

	}

	@Test
	@DirtiesContext
	public void testValidationDueDateAndGracePeriod() throws Exception {
		loadValidationDateTestData();
		// normally you would configure these via Spring, but don't want to muck
		// with other tests...
		StaticValidationDueDateStrategy validationDueDateStrategy = new StaticValidationDueDateStrategy();
		validationDueDateStrategy.setValidDays(10);
		StaticGracePeriodStrategy gracePeriodStrategy = new StaticGracePeriodStrategy();
		gracePeriodStrategy.setGracePeriodDays(10);
		subscriptionSearchQueryDAO
				.setValidationDueDateStrategy(validationDueDateStrategy);
		subscriptionSearchQueryDAO.setGracePeriodStrategy(gracePeriodStrategy);
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM1");
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		Subscription response = subscriptions.get(0);
		DateTime lastValidationDate = response.getLastValidationDate();
		assertEquals(8, lastValidationDate.getMonthOfYear());
		assertEquals(27, lastValidationDate.getDayOfMonth());
		DateTime validationDueDate = response.getValidationDueDate();
		Interval gracePeriod = response.getGracePeriod();
		assertEquals(10, Days
				.daysBetween(lastValidationDate, validationDueDate).getDays());
		assertEquals(11,
				Days.daysBetween(lastValidationDate, gracePeriod.getStart())
						.getDays());
		assertEquals(21,
				Days.daysBetween(lastValidationDate, gracePeriod.getEnd())
						.getDays());

	}

	@Test
	@DirtiesContext
	public void testValidationDueDateAndGracePeriodEndDateBeforeValidationDueDate()
			throws Exception {
		loadValidationDateTestData();
		// normally you would configure these via Spring, but don't want to muck
		// with other tests...
		StaticValidationDueDateStrategy validationDueDateStrategy = new StaticValidationDueDateStrategy();
		validationDueDateStrategy.setValidDays(10);
		StaticGracePeriodStrategy gracePeriodStrategy = new StaticGracePeriodStrategy();
		gracePeriodStrategy.setGracePeriodDays(10);
		subscriptionSearchQueryDAO
				.setValidationDueDateStrategy(validationDueDateStrategy);
		subscriptionSearchQueryDAO.setGracePeriodStrategy(gracePeriodStrategy);
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM4");
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		Subscription response = subscriptions.get(0);
		DateTime lastValidationDate = response.getLastValidationDate();
		assertEquals(8, lastValidationDate.getMonthOfYear());
		assertEquals(27, lastValidationDate.getDayOfMonth());
		DateTime validationDueDate = response.getValidationDueDate();
		Interval gracePeriod = response.getGracePeriod();
		assertEquals(10, Days
				.daysBetween(lastValidationDate, validationDueDate).getDays());
		assertEquals(11,
				Days.daysBetween(lastValidationDate, gracePeriod.getStart())
						.getDays());
		assertEquals(21,
				Days.daysBetween(lastValidationDate, gracePeriod.getEnd())
						.getDays());

	}

	@Test
	@DirtiesContext
	public void testSubscriptionCount() throws Exception {
		loadManualTestData();
		int subscriptionCount = subscriptionSearchQueryDAO
				.countSubscriptionsInSearch("OJBC:IDP:OJBC:USER:admin");
		assertEquals(2, subscriptionCount);
	}

	@Test
	@DirtiesContext
	public void testQueryForSubscriptionsByOwnerAndId() throws Exception {
		loadManualTestData();
		Subscription subscription = subscriptionSearchQueryDAO
				.queryForSubscription("OJBC:IDP:OJBC:USER:admin", "1", "false");
		assertNotNull(subscription);
		assertEquals("bill", subscription.getPersonFirstName());
		assertEquals("padmanabhan", subscription.getPersonLastName());
		assertEquals("1970-02-03", subscription.getDateOfBirth());
		
		assertNotNull(subscription.getSubscriptionProperties());
		
		assertEquals(4, subscription.getSubscriptionProperties().size());
		
		assertEquals(FederalTriggeringEventCode.ARREST.toString(), subscription.getSubscriptionProperties().get(FederalTriggeringEventCode.ARREST.toString()));
		assertEquals(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"), subscription.getSubscriptionProperties().get(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-")));
		assertEquals("true", subscription.getSubscriptionProperties().get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR));
		assertEquals("Bill Padmanabhan", subscription.getSubscriptionProperties().get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT));

		assertEquals("0123ABC",subscription.getAgencyCaseNumber());
	}

	@Test
	@DirtiesContext
	public void testSearchForSubscriptionsMatchingNotificationRequestByEventDateAndSubject()
			throws Exception {
		loadBasicTestData();

		ArrestNotificationRequest request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5008305.xml");

		// one SID, two subscriptions, each with one email address
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		assertEquals(2, subscriptions.size());
		for (Subscription subscription : subscriptions) {
			assertEquals(1, subscription.getEmailAddressesToNotify().size());
		}
		// one SID, one subscription, two email addresses
		request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5008306.xml");
		subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		assertEquals(1, subscriptions.size());
		Subscription subscription = subscriptions.get(0);
		assertEquals(2, subscription.getEmailAddressesToNotify().size());
	}

	@Test
	@DirtiesContext
	public void testSearchForSubscriptionsMatchingNotificationRequestByEventDateAndSubjectInactive()
			throws Exception {
		loadBasicTestData();

		ArrestNotificationRequest request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5012703.xml");

		// Inactive subscription
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		assertEquals(0, subscriptions.size());

	}

	@Test
	@DirtiesContext
	public void testQueryForSubscriptionById() throws Exception {
		loadBasicTestData();
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription("1");
		assertEquals(1, subscriptions.size());
	}

	@Test
	@DirtiesContext
	public void testQueryForSubscriptionBySystemAndOwner() throws Exception {
		loadBasicTestData();
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription(
						"{http://ojbc.org/wsn/topics}:person/arrest",
						"{http://demostate.gov/SystemNames/1.0}SystemA",
						"SYSTEM", Collections.singletonMap("SID", "A5008305"));
		assertEquals(1, subscriptions.size());
		
		subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription(
						null,
						null,
						null, Collections.singletonMap("SID", "A5008305"));
		assertEquals(2, subscriptions.size());

		subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription(
						"{http://ojbc.org/wsn/topics}:person/arrest",
						null,
						null, Collections.singletonMap("SID", "A5008305"));
		assertEquals(2, subscriptions.size());
		
		subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription(
						"{http://ojbc.org/wsn/topics}:person/arrest",
						"{http://demostate.gov/SystemNames/1.0}SystemB",
						null, Collections.singletonMap("SID", "A5008305"));
		assertEquals(1, subscriptions.size());
		
		subscriptions = subscriptionSearchQueryDAO
				.queryForSubscription(
						null,
						null,
						null, Collections.singletonMap("subscriptionQualifier", "20920"));
		assertEquals(1, subscriptions.size());


	}

	@Test(expected = IllegalStateException.class)
	@DirtiesContext
	public void testSearchForSubscriptionsWithNullValidationDate()
			throws Exception {
		loadNullLastValidationDateTestData();

		ArrestNotificationRequest request = TestNotificationBuilderUtil.returnArrestNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest_A5008305.xml");

		subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);

	}

	@Test
	@DirtiesContext
	public void testUnsubscribeBySystemId() throws Exception {

		loadBasicTestData();

		Statement s = dataSource.getConnection().createStatement();

		ResultSet rs = s.executeQuery("select * from subscription");

		assertTrue(rs.next());
		int id = rs.getInt("id");
		byte active = rs.getByte("ACTIVE");
		String topic = rs.getString("topic");

		assertEquals(1, active);

		subscriptionSearchQueryDAO
				.unsubscribe("" + id, topic, null, null, null);

		rs = s.executeQuery("select * from subscription where id=" + id);

		assertTrue(rs.next());
		active = rs.getByte("ACTIVE");

		assertEquals(0, active);

	}

	@Test
	@DirtiesContext
	public void testUnsubscribeBySubject() throws Exception {

		loadBasicTestData();

		Statement s = dataSource.getConnection().createStatement();

		ResultSet rs = s
				.executeQuery("select s.topic, s.id, s.ACTIVE, s.subscribingSystemIdentifier, so.federation_id as subscriptionOwner from subscription s, subscription_owner so where s.subscription_owner_id=so.subscription_owner_id and active=1");

		assertTrue(rs.next());
		int id = rs.getInt("id");
		byte active = rs.getByte("ACTIVE");
		String topic = rs.getString("topic");
		String systemName = rs.getString("subscribingSystemIdentifier");
		String owner = rs.getString("subscriptionOwner");

		assertEquals(1, active);

		rs.close();

		rs = s.executeQuery("select * from subscription_subject_identifier where subscriptionid="
				+ id);

		Map<String, String> subjectIdMap = new HashMap<String, String>();

		while (rs.next()) {
			subjectIdMap.put(rs.getString("IdentifierName"),
					rs.getString("IdentifierValue"));
		}

		subscriptionSearchQueryDAO.unsubscribe(null, topic, subjectIdMap,
				systemName, owner);

		rs = s.executeQuery("select * from subscription where id=" + id);

		assertTrue(rs.next());
		active = rs.getByte("ACTIVE");

		assertEquals(0, active);

		s.close();

	}

	@Test
	@DirtiesContext
	public void testWildcardSubscription() throws Exception {

		loadWildcardTestData();

		IncidentNotificationRequest request = returnIncidentNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest-incident.xml");
		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		Set<Long> expectedIds = new HashSet<Long>();
		expectedIds.addAll(Arrays.asList(1l, 2l, 3l, 5l, 7l));
		for (Subscription s : subscriptions) {
			assertTrue(expectedIds.remove(s.getId()));
		}
		assertTrue(expectedIds.isEmpty());

		request = returnIncidentNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest-incident2.xml");
		subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		expectedIds.addAll(Arrays.asList(1l, 3l, 5l, 7l));
		for (Subscription s : subscriptions) {
			assertTrue(expectedIds.remove(s.getId()));
		}
		assertTrue(expectedIds.isEmpty());

		request = returnIncidentNotificationRequest("src/test/resources/xmlInstances/notificationSoapRequest-incident3.xml");
		subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsMatchingNotificationRequest(request);
		expectedIds.addAll(Arrays.asList(1l, 5l, 7l));
		for (Subscription s : subscriptions) {
			assertTrue(expectedIds.remove(s.getId()));
		}
		assertTrue(expectedIds.isEmpty());

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
	public void testSubscribe_multipleEmails() throws Exception {

		loadBasicTestData();

		Statement s = dataSource.getConnection().createStatement();

		Map<String, String> subjectIds = new HashMap<String, String>();
		subjectIds.put(SubscriptionNotificationConstants.SID, "1234");
		subjectIds.put(
				SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
				"ABCDE");

		ResultSet rs;

		Set<String> emailAddyList = new HashSet<String>();
		emailAddyList.addAll(Arrays.asList("p1@none.com", "p2@none.com"));

		SubscriptionRequest request = buildSubscriptionRequest(subjectIds,
				null);
		request.setEmailAddresses(emailAddyList);
		int subscriptionId = subscriptionSearchQueryDAO.subscribe(request,
				new LocalDate()).intValue();

		rs = s.executeQuery("select * from notification_mechanism where subscriptionid="
				+ subscriptionId);

		int recordCount = 0;
		while (rs.next()) {
			recordCount++;
			assertEquals(NotificationConstants.NOTIFICATION_MECHANISM_EMAIL,
					rs.getString("NOTIFICATIONMECHANISMTYPE"));
			String addy = rs.getString("NOTIFICATIONADDRESS");
			assertTrue(emailAddyList.contains(addy));
			emailAddyList.remove(addy);
		}

		assertEquals(2, recordCount);

		rs.close();
		s.close();

	}

	@Test
	@DirtiesContext
	public void testSubscribe_existingSubscriptions() throws Exception {

		loadBasicTestData();

		Statement s = dataSource.getConnection().createStatement();

		Map<String, String> subjectIds = new HashMap<String, String>();
		subjectIds.put(SubscriptionNotificationConstants.SID, "1234");
		subjectIds.put(
				SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
				"ABCDE");

		ResultSet rs;

		LocalDate originalDate = DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseDateTime("2013-01-01").toLocalDate();

		SubscriptionRequest request = buildSubscriptionRequest(subjectIds, null);
		int subscriptionId = subscriptionSearchQueryDAO.subscribe(request, originalDate)
				.intValue();

		rs = s.executeQuery("select * from subscription where id="
				+ subscriptionId);

		int recordCount = 0;
		while (rs.next()) {
			recordCount++;
		}

		assertEquals(1, recordCount);

		int oldSubscriptionId = subscriptionId;

		LocalDate subsequentDate = new LocalDate();

		Map<String, String> subscriptionPropertiesRequest = new HashMap<String, String>();
		
		subscriptionPropertiesRequest = new HashMap<String, String>();
		subscriptionPropertiesRequest.put(FederalTriggeringEventCode.ARREST.toString(), FederalTriggeringEventCode.ARREST.toString());
		subscriptionPropertiesRequest.put(FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"), FederalTriggeringEventCode.NCIC_WARRANT_ENTRY.toString().replace("_", "-"));
		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR, "true");
		subscriptionPropertiesRequest.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, "Bill Padmanabhan");

		SubscriptionRequest subRequest = buildSubscriptionRequest(subjectIds, subscriptionPropertiesRequest);
		subRequest.setEndDateString("2013-01-02");
		subscriptionId = subscriptionSearchQueryDAO
				.subscribe(subRequest, subsequentDate).intValue();

		assertEquals(oldSubscriptionId, subscriptionId); // same id, must have
															// been an update
															// not insert

		rs = s.executeQuery("select s.*, so.EMAIL_ADDRESS as subscriptionOwnerEmailAddress from subscription s, subscription_owner so where s.subscription_owner_id = so.subscription_owner_id and id="
				+ subscriptionId);

		recordCount = 0;
		while (rs.next()) {
			recordCount++;
			DateTime d = new DateTime(rs.getDate("ENDDATE"));
			assertEquals(2013, d.getYear());
			assertEquals(1, d.getMonthOfYear());
			assertEquals(2, d.getDayOfMonth());
			DateTime lastValidationDate = new DateTime(
					rs.getDate("lastValidationDate"));
			assertEquals(subsequentDate.toDateTimeAtStartOfDay().toDate(),
					lastValidationDate.toDate());
			assertTrue(lastValidationDate.isAfter(originalDate
					.toDateTimeAtStartOfDay()));
			assertEquals("0123ABC", rs.getString("agency_case_number"));
			assertEquals("local1@local.gov", rs.getString("subscriptionOwnerEmailAddress"));
		}

		assertEquals(1, recordCount);
		
		Map<String, String> propertiesFromDatabase = subscriptionSearchQueryDAO.getSubscriptionProperties(String.valueOf(subscriptionId));

		assertEquals(4, propertiesFromDatabase.size());	
		
		s.close();

	}

	@Test
	@DirtiesContext
	public void testSubscribe_noExistingSubscriptionsForTopic()
			throws Exception {

		loadAgencyTestData();

		Map<String, String> subjectIds = new HashMap<String, String>();
		subjectIds.put(SubscriptionNotificationConstants.SID, "1234");
		subjectIds.put(
				SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
				"ABCDE");

		LocalDate originalDate = DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseDateTime("2013-01-01").toLocalDate();

		SubscriptionRequest subRequest = buildSubscriptionRequest(subjectIds, null);
		subRequest.setTopic("topic1");
		subRequest.setReasonCategoryCode(null);

		int subscriptionId = subscriptionSearchQueryDAO.subscribe( subRequest, originalDate)
				.intValue();

		List<Subscription> subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM");
		assertEquals(1, subscriptions.size());

		LocalDate subsequentDate = new LocalDate();

		subRequest.setTopic("topic2");
		subRequest.setEndDateString("2013-01-02");
		subRequest.setReasonCategoryCode(null);
		int secondSubscriptionId = subscriptionSearchQueryDAO
				.subscribe(subRequest, subsequentDate).intValue();

		assertFalse(secondSubscriptionId == subscriptionId); // because topic1
																// and topic2
																// are different

		subscriptions = subscriptionSearchQueryDAO
				.searchForSubscriptionsBySubscriptionOwner("SYSTEM");
		assertEquals(2, subscriptions.size());

	}

	@Test
	@DirtiesContext
	public void testBuildWhereClause_single() {
		String expectedResult = " s.id in (select subscriptionId from subscription_subject_identifier where identifierName=? and upper(identifierValue) = upper(?)) ";

		String whereClause = SubscriptionSearchQueryDAO.buildCriteriaSql(1);

		assertThat(whereClause, is(expectedResult));
	}

	@Test
	@DirtiesContext
	public void testBuildWhereClause_multiple() {
		String expectedResult = " s.id in (select subscriptionId from subscription_subject_identifier where identifierName=? and upper(identifierValue) = upper(?)) "
				+ " and s.id in (select subscriptionId from subscription_subject_identifier where identifierName=? and upper(identifierValue) = upper(?)) ";

		String whereClause = SubscriptionSearchQueryDAO.buildCriteriaSql(2);

		assertThat(whereClause, is(expectedResult));
	}

	@Test
	@DirtiesContext
	public void testBuildCriteriaArray() {
		Object[] expectedResult = new Object[] { "subscriptionQualifier", "ABCDE", "SID", "1234" };

		Map<String, String> input = new HashMap<String, String>();
		input.put("SID", "1234");
		input.put("subscriptionQualifier", "ABCDE");

		Object[] result = SubscriptionSearchQueryDAO.buildCriteriaArray(input);

		assertThat(result, is(expectedResult));
	}

	@Test
	@DirtiesContext
	public void testSidConsolidation()
			throws Exception {
		loadManualTestData();
		subscriptionSearchQueryDAO
				.updateSubscriptionSubjectIdentifier("A5008305", "A5008306","1",SubscriptionNotificationConstants.SID);

		subscriptionSearchQueryDAO
				.updateSubscriptionSubjectIdentifier("A5008305", "A5008306","3",SubscriptionNotificationConstants.SID);

		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSidConsolidation.xml");
	}

	private IncidentNotificationRequest returnIncidentNotificationRequest(
			String pathToNotificationRequest) throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		Exchange ex = new DefaultExchange(ctx);

		ex.getIn().setBody(
				TestNotificationBuilderUtil
						.getMessageBody(pathToNotificationRequest));

		Message message = ex.getIn();

		IncidentNotificationRequest request = new IncidentNotificationRequest(
				message);

		return request;
	}

	private void compareDatabaseWithExpectedDataset(String expectedDatasetFileName) throws SQLException,
		Exception, MalformedURLException, DataSetException, DatabaseUnitException {
	
		// Fetch database data after executing your code
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable filteredActualSubscriptionTable = getFilteredTableFromDataset(databaseDataSet, "subscription");
		ITable filteredActualNotficationMechanismTable = getFilteredTableFromDataset(databaseDataSet, "notification_mechanism");
		ITable filteredActualSubjectIdentiferTable = getFilteredTableFromDataset(databaseDataSet, "subscription_subject_identifier");
		
		// Load expected data from an XML dataset
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/xmlInstances/dbUnit/" + expectedDatasetFileName));
		ITable filteredExpectedSubscriptionTable = getFilteredTableFromDataset(expectedDataSet, "subscription");
		ITable filteredExpectedNotficationMechanismTable = getFilteredTableFromDataset(expectedDataSet, "notification_mechanism");
		ITable filteredExpectedSubjectIdentiferTable = getFilteredTableFromDataset(expectedDataSet, "subscription_subject_identifier");
		
		// Assert actual database table match expected table
		Assertion.assertEquals(filteredExpectedSubscriptionTable, filteredActualSubscriptionTable);
		Assertion.assertEquals(filteredExpectedNotficationMechanismTable, filteredActualNotficationMechanismTable);
		Assertion.assertEquals(filteredExpectedSubjectIdentiferTable, filteredActualSubjectIdentiferTable);
	}

	private ITable getFilteredTableFromDataset(IDataSet dataSet, String tableName) throws Exception {
        ITable table = dataSet.getTable(tableName);
        ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(table, new String[]{"id*", "subscriptionId", "*date", "timestamp"});
        
        return filteredTable;
	}
	
	private IDatabaseConnection getConnection() throws Exception {
        Connection con = dataSource.getConnection();  
        IDatabaseConnection connection = new DatabaseConnection(con);

        return connection;  
	}

}
