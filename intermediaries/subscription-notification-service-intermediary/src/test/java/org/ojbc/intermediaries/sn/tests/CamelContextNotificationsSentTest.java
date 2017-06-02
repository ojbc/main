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

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.notification.NotificationsSentStrategy;

public class CamelContextNotificationsSentTest extends AbstractSubscriptionNotificationTest {

	private static final Log log = LogFactory.getLog( CamelContextNotificationsSentTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:smtpEndpoint")
    protected MockEndpoint smtpEndpointMock;

	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' quartz endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("checkNotificationsSentRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:checkNotificationsSent");
    	    	
    	    	interceptSendToEndpoint("smtpEndpoint").skipSendToOriginalEndpoint().to("mock:smtpEndpoint");
    	    }              
    	});

		context.start();		
		
	}
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}
	
    @Test
    public void testSubscriptionInvalidEmail() throws Exception {
    
    	NotifyBuilder notify = new NotifyBuilder(context).whenReceivedSatisfied(smtpEndpointMock).create();
    	
    	//On this initial run, an alert will be sent because no notifications have been sent
    	smtpEndpointMock.reset();
    	smtpEndpointMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:checkNotificationsSent", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		// waits but allows match to break out and continue before timeout
		boolean done = notify.matches(10, TimeUnit.SECONDS);
		
		smtpEndpointMock.assertIsSatisfied();
		
		//Get the notification sent strategy bean and updated the timestamp
		NotificationsSentStrategy notificationsSentStrategy = (NotificationsSentStrategy)context.getRegistry().lookupByName("notificationsSentStrategy");
		notificationsSentStrategy.updateNotificationSentTimestamp();
		
		//Now we expect no alert to be sent because the notification timestamp is updated
    	smtpEndpointMock.reset();
    	
    	notify = new NotifyBuilder(context).whenReceivedSatisfied(smtpEndpointMock).create();
    	smtpEndpointMock.expectedMessageCount(0);
    	
    	//Create a new exchange
    	senderExchange = new DefaultExchange(context);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:checkNotificationsSent", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		notify.matches(10, TimeUnit.SECONDS);
		
		smtpEndpointMock.assertIsSatisfied();
		
    }



}	
