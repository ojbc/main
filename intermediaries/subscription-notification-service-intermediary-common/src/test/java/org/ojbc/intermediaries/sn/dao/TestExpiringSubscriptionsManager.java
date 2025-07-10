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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.ojbc.intermediaries.sn.notification.ExpiringSubscriptionEmail;
import org.ojbc.intermediaries.sn.notification.ExpiringSubscriptionsManager;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.w3c.dom.Document;

@CamelSpringTest
@SpringJUnitConfig(locations = {
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class TestExpiringSubscriptionsManager {

	private static final Log log = LogFactory.getLog(TestExpiringSubscriptionsManager.class);
	
	@Resource(name = "rapbackDataSource")
	private DataSource dataSource;

	@Autowired
	private ExpiringSubscriptionsManager expiringSubscriptionsManager;

	private void loadTestData(final String manualTestFileName)
			throws FileNotFoundException, DatabaseUnitException, SQLException,
			DataSetException {
		FileInputStream manualTestFile = new FileInputStream(manualTestFileName);
		Connection conn = dataSource.getConnection();
		conn.createStatement().execute("use rapback_datastore");

		IDatabaseConnection connection = new DatabaseConnection(
				conn);
		FileInputStream emptyDataSetFile = new FileInputStream("src/test/resources/xmlInstances/dbUnit/emptyDataSet.xml");
		DatabaseOperation.DELETE_ALL.execute(connection, new FlatXmlDataSetBuilder().build(emptyDataSetFile));
		
		DatabaseOperation.CLEAN_INSERT.execute(connection,
				new FlatXmlDataSetBuilder().build(manualTestFile));
	}
	
	@Test
	public void testReturnExpiringNotificationEmails() throws Exception
	{
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_expired.xml");
		
		List<ExpiringSubscriptionEmail> expiringSubscriptions = expiringSubscriptionsManager.returnExpiringNotificationEmails("Junit Test");
		
		log.info(expiringSubscriptions);
		
		assertEquals(2, expiringSubscriptions.size());
		
		assertEquals("You have Criminal subscriptions expiring soon on Junit Test", expiringSubscriptions.get(0).getSubject());
		assertEquals("You have Rap Back subscriptions expiring soon on Junit Test", expiringSubscriptions.get(1).getSubject());
		assertEquals("admin1@local.gov", expiringSubscriptions.get(0).getTo());
		assertEquals("admin1@local.gov", expiringSubscriptions.get(1).getTo());
		
		assertTrue(expiringSubscriptions.get(0).getMessageBody().startsWith("You have 1 Criminal subscription(s)"));
		assertTrue(expiringSubscriptions.get(1).getMessageBody().startsWith("You have 1 Rap Back subscription(s) "));
		
	}
	
	@Test
	public void testReturnExpiredSubscriptionsToUnsubscribe() throws Exception
	{
		loadTestData("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_expired.xml");
		
		List<Document> expiredSubscriptionsToUnsubscribe = expiringSubscriptionsManager.returnExpiredSubscriptionsToUnsubscribe();
		
		log.info(expiredSubscriptionsToUnsubscribe);
		
		assertEquals(3, expiredSubscriptionsToUnsubscribe.size());
		
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
		
		for (Document doc: expiredSubscriptionsToUnsubscribe)
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
