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
package org.ojbc.connectors.subscriptionmanagement;

import static org.junit.Assert.assertTrue;

import java.io.File;
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
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/spring-context.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
@DirtiesContext
public class CamelContextTest {

	private static final Log log = LogFactory.getLog(CamelContextTest.class);

	@Resource
	private ModelCamelContext context;

    @EndpointInject(uri = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerService;

    @Produce
    protected ProducerTemplate template;

	@Before
	public void setUp() throws Exception {
    	context.getRouteDefinition("subscriptionInvocationRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	interceptSendToEndpoint("cxf:bean:notificationBrokerService*").skipSendToOriginalEndpoint().to(notificationBrokerService);
    	    }              
    	});
		
    	notificationBrokerService.expectedMessageCount(4);
    	
		context.start();
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
						
	    //Read the initial extract from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/csvExtracts/sample.csv");
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputFile);
	    
	    //We explicitly set the file name so Camel doesn't set a default named based on the exchange ID
	    senderExchange.getIn().setHeader("CamelFileName", "sample.csv");
	    
	    NotifyBuilder notifyBuilder = new NotifyBuilder(context).whenDone(1).create();
	    
	    //Send the exchange.  Using template.send will send the message
		template.send("inputDirectory", senderExchange);
		
		// waits but allows match to break out and continue before timeout
		boolean done = notifyBuilder.matches(1000, TimeUnit.SECONDS);
		
		notificationBrokerService.assertIsSatisfied();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void contextStartup() {
		assertTrue(true);
	}

}
