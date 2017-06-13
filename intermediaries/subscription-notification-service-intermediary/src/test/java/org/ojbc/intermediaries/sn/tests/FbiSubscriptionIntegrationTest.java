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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationProcessor;

public class FbiSubscriptionIntegrationTest extends AbstractSubscriptionNotificationIntegrationTest {
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(FbiSubscriptionIntegrationTest.class);
    
    @EndpointInject(uri="mock:cxf:bean:fbiEbtsSubscriptionRequestService")
    protected MockEndpoint fbiEbtsSubscriptionMockEndpoint; 
	
	@Resource
	protected IncidentNotificationProcessor incidentNotificationProcessor;
	
	//TODO ensure prod java code uses correct data source
		
	@Resource
	private DataSource rapbackDataSource;
	
	@Before
	public void setUp() throws Exception {
		
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/fbiSubscriptionDataSet.xml"));
		
    	context.getRouteDefinition("sendToFbiEbtsAdapter").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    
    	    	
    	    	mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
    	    	mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionManagerService**");

    	    }              
    	});    	    	
	}		
			
	protected IDatabaseConnection getConnection() throws Exception {
		
		Connection con = rapbackDataSource.getConnection();
		IDatabaseConnection connection = new DatabaseConnection(con);
		return connection;
	}	
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void subscribeArrestWithFbiData() throws Exception {
		
		String response = invokeRequest("fbiSubscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));        

		//Query for subscription just added to confirm validation date
		//DB Unit doesn't have good support for this
		//See: http://stackoverflow.com/questions/2856840/date-relative-to-current-in-the-dbunit-dataset
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://demostate.gov/SystemNames/1.0}SystemA", "SYSTEM", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptions.get(0).getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
	}	
}
