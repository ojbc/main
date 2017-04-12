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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationRequest;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationRequest;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.util.xml.XmlUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.lang.time.DateUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
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
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml", })
@DirtiesContext
public class TestSubscriptionSearchByFbiSubscriptionId {

	private static final Log log = LogFactory
			.getLog(TestSubscriptionSearchByFbiSubscriptionId.class);

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

	private ValidationDueDateStrategy springConfiguredStrategy;

	@Before
	public void setUp() {
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
	public void testGetSubscriptionByFbiSubscriptionId()
			throws Exception {
		
		Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionByFbiSubscriptionId("fbiSubscriptionId_3");
		
		log.info("Subscription:" + subscription);
		assertNotNull(subscription);
	}
	
}
