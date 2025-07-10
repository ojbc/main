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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Basic unit test for the subscription search query DAO.
 *
 */
@CamelSpringTest
@SpringJUnitConfig(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class TestSubscriptionSearchByFbiSubscriptionId {

	private static final Log log = LogFactory
			.getLog(TestSubscriptionSearchByFbiSubscriptionId.class);

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

	private ValidationDueDateStrategy springConfiguredStrategy;

	@BeforeEach
	public void setUp() {
		springConfiguredStrategy = subscriptionSearchQueryDAO
				.getValidationDueDateStrategy();
	}

	@AfterEach
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
