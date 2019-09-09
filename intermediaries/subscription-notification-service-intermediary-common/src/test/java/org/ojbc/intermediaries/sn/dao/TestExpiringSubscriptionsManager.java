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
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.notification.ExpiringSubscriptionEmail;
import org.ojbc.intermediaries.sn.notification.ExpiringSubscriptionsManager;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml", })
@DirtiesContext
public class TestExpiringSubscriptionsManager {

	private static final Log log = LogFactory.getLog(TestExpiringSubscriptionsManager.class);
	
	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	@Autowired
	private ExpiringSubscriptionsManager expiringSubscriptionsManager;

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
	public void testReturnExpiringNotificationEmails() throws Exception
	{
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_expired.xml");
		
		List<ExpiringSubscriptionEmail> expiringSubscriptions = expiringSubscriptionsManager.returnExpiringNotificationEmails();
		
		log.info(expiringSubscriptions);
		
		assertEquals(2, expiringSubscriptions.size());
		
		assertEquals("You have subscriptions expiring soon", expiringSubscriptions.get(0).getSubject());
		assertEquals("admin1@local.gov", expiringSubscriptions.get(0).getTo());
		assertEquals("admin2@local.gov", expiringSubscriptions.get(1).getTo());
		
		assertTrue(expiringSubscriptions.get(0).getMessageBody().startsWith("You have 2 Rap Back subscription"));
		assertTrue(expiringSubscriptions.get(1).getMessageBody().startsWith("You have 1 Rap Back subscription"));
		
	}
	
	@Test
	public void testReturnExpiredSubscriptionsToUnsubscribe() throws Exception
	{
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_expired.xml");
		
		List<Document> expiringSubscriptionsToUnsubscribe = expiringSubscriptionsManager.returnExpiredSubscriptionsToUnsubscribe();
		
		log.info(expiringSubscriptionsToUnsubscribe);
		
		assertEquals(3, expiringSubscriptionsToUnsubscribe.size());
		
//<b-2:Unsubscribe xmlns:b-2="http://docs.oasis-open.org/wsn/b-2"
//      xmlns:nc="http://niem.gov/niem/niem-core/2.0"
//      xmlns:submsg-ext="http://ojbc.org/IEPD/Extensions/Subscription/1.0"
//      xmlns:unsubmsg-exch="http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0">
//<unsubmsg-exch:UnsubscriptionMessage>
//<submsg-ext:SubscriptionIdentification>
//<nc:IdentificationID>1</nc:IdentificationID>
//</submsg-ext:SubscriptionIdentification>
//<submsg-ext:CriminalSubscriptionReasonCode>CI</submsg-ext:CriminalSubscriptionReasonCode>
//</unsubmsg-exch:UnsubscriptionMessage>
//<b-2:TopicExpression Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete">{http://ojbc.org/wsn/topics}:person/arrest</b-2:TopicExpression>
//</b-2:Unsubscribe>
		boolean assertionsRun = false;
		
		for (Document doc: expiringSubscriptionsToUnsubscribe)
		{
			String subscriptionIdentificationId = XmlUtils.xPathStringSearch(doc, 
					"//submsg-ext:SubscriptionIdentification/nc:IdentificationID");
			
			
			
			if (subscriptionIdentificationId.equals("1"))
			{
				assertEquals("1", subscriptionIdentificationId);	
				
				String topic = XmlUtils.xPathStringSearch(doc, "//b-2:TopicExpression[@Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete']");  
				assertEquals("{http://ojbc.org/wsn/topics}:person/arrest", topic);
				
				String subscriptionReasonCode = XmlUtils.xPathStringSearch(doc, 
						"//submsg-ext:CriminalSubscriptionReasonCode");
				assertEquals("CI", subscriptionReasonCode);
				
				assertionsRun = true;
			}
		}	
		
		assertTrue(assertionsRun);
	}
}
