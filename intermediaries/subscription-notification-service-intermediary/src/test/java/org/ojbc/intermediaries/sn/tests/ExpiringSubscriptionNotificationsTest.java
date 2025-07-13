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

import java.io.FileInputStream;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpiringSubscriptionNotificationsTest extends AbstractSubscriptionNotificationTest {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( ExpiringSubscriptionNotificationsTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @Resource  
    private DataSource dataSource;  
    
    @EndpointInject(value = "mock:smtpEndpoint")
    protected MockEndpoint smtpEndpointMock;
    
    @EndpointInject(value = "mock:direct:processUnsubscription")
    protected MockEndpoint processUnsubscriptionMock;

	@BeforeEach
	public void setUp() throws Exception {
		//We replace the 'from' quartz endpoint with a direct endpoint we call in our test
    	AdviceWith.adviceWith(context, "notifyOfExpiringSubscriptionsRoute", route -> {
    		route.replaceFromWith("quartz://testTimer?trigger.startDelay=0&trigger.repeatCount=0");
    		route.weaveByToString("To[smtpEndpoint]").replace().to(smtpEndpointMock).stop(); 
    	});
    	
    	AdviceWith.adviceWith(context, "cancelExpiredInvalidSubscriptionsRoute", route -> {
    		route.replaceFromWith("quartz://testTimer2?startDelayedSeconds=6&trigger.repeatCount=0");
    		route.weaveByToString("To[direct:processUnsubscription]").replace().to(processUnsubscriptionMock).stop(); 
    	});
    	
    	DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
    	DatabaseOperation.INSERT.execute(getConnection(), getDataSet());
		
		
	}

	private IDataSet getDataSet() throws Exception{  
        // get insert data  
    	return new FlatXmlDataSetBuilder().build(new FileInputStream(
				"src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_expiringNotificationTest.xml"));
    }  
	
	@Test
	public void contextStartup() throws InterruptedException {
    	smtpEndpointMock.reset();
    	smtpEndpointMock.expectedMessageCount(2);
    	processUnsubscriptionMock.reset(); 
    	processUnsubscriptionMock.expectedMessageCount(3);
    	context.start();
    	Thread.sleep(20000);
    	
		smtpEndpointMock.assertIsSatisfied();
		processUnsubscriptionMock.assertIsSatisfied();

	}
	
    @Test
    public void testNotifyOfExpiringSubscriptionsRoute() throws Exception {
    
    	NotifyBuilder notify = new NotifyBuilder(context).whenReceivedSatisfied(smtpEndpointMock).create();
    	
    	//On this initial run, an alert will be sent because no notifications have been sent
		
    }

	private IDatabaseConnection getConnection() throws Exception {
        Connection con = dataSource.getConnection();  
        IDatabaseConnection connection = new DatabaseConnection(con);

        return connection;  
	}



}	
