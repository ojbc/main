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
package org.ojbc.intermediaries.sn.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.sn.FbiSubscriptionProcessor;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationProcessor;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.w3c.dom.Document;

public class FbiSubscriptionIntegrationTest extends AbstractSubscriptionNotificationIntegrationTest {
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(FbiSubscriptionIntegrationTest.class);
    
    @EndpointInject(value="mock:cxf:bean:fbiEbtsSubscriptionRequestService")
    protected MockEndpoint fbiEbtsSubscriptionMockEndpoint; 
	
	@Resource
	protected IncidentNotificationProcessor incidentNotificationProcessor;
	
	@Resource
	protected FbiSubscriptionProcessor fbiSubscriptionProcessor;
	
	//TODO ensure prod java code uses correct data source
		
	@Resource
	private DataSource dataSource;
	
	@BeforeEach
	public void setUp() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/fbiSubscriptionDataSet.xml"));
		
    	AdviceWith.adviceWith(context, "sendToFbiEbtsAdapter", route -> {
    		route.mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
    		route.mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionManagerService*");
    	});
    	context.start();

	}		
			
	protected IDatabaseConnection getConnection() throws Exception {
		
		Connection con = dataSource.getConnection();
//		con.createStatement().execute("use rapback_datastore;"); 
		IDatabaseConnection connection = new DatabaseConnection(con);
		return connection;
	}	
	
	@AfterEach
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void subscribeArrestWithFbiData() throws Exception {
		
		String response = invokeRequest("fbiSubscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));    
		
		Thread.sleep(3000);

		//Query for subscription just added to confirm validation date
		//DB Unit doesn't have good support for this
		//See: http://stackoverflow.com/questions/2856840/date-relative-to-current-in-the-dbunit-dataset
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://demostate.gov/SystemNames/1.0}SystemA", "SYSTEM", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(2, subscriptions.size());
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptions.get(1).getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
	}
	
	@Test
	@DirtiesContext
	public void testPrepareUnsubscribeMessageForFbiEbts() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/fbiSubscriptionDataSet.xml"));

		assertNotNull(fbiSubscriptionProcessor);
		Document originalUnsubscribeRequest = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/unSubscribeFbiSubscriptionRequest.xml"));
		Document preparedRequest = fbiSubscriptionProcessor.prepareUnsubscribeMessageForFbiEbts(originalUnsubscribeRequest);
		
		XmlTestUtils.compareDocuments("src/test/resources/xmlInstances/expectedPreparedUnsubscribeFbiRequest.xml", preparedRequest);
	}	
}
