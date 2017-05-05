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
package org.ojbc.bundles.intermediaries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/extensible-beans.xml",	
		"classpath:META-INF/spring/jetty-server.xml",
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
public class CamelContextTest {

	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_UNSUBSCRIBE_OPERATION_NAME = "Unsubscribe";
	public static final String CXF_UNSUBSCRIPTION_OPERATION_NAMESPACE = "http://docs.oasis-open.org/wsn/bw-2";
	
	public static final String CXF_SUBSCRIBE_OPERATION_NAME = "Subscribe";
	public static final String CXF_SUBSCRIPTION_OPERATION_NAMESPACE = "http://docs.oasis-open.org/wsn/brw-2";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:subscriptionManagerService")
    protected MockEndpoint subscriptionManagerServiceMockEndpoint;

    @EndpointInject(uri = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerServiceMockEndpoint;
    
    @EndpointInject(uri = "mock:direct:transformAndInvokeUnsubscriptionProbationProcessor")
    protected MockEndpoint derivedUnsubscriptionEndpoint;
    
    @EndpointInject(uri = "mock:direct:transformAndInvokeSubscriptionProcessor")
    protected MockEndpoint derivedSubscriptionEndpoint;
    
	@Before
	public void setUp() throws Exception {
		
		//We mock the 'transformAndInvokeUnsubscriptionProbationProcessor' and 'direct:transformAndInvokeSubscriptionProcessor' endpoint to test against.
    	context.getRouteDefinition("processProbationDocRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	mockEndpoints("direct:transformAndInvokeUnsubscriptionProbationProcessor*");
    	    	mockEndpoints("direct:transformAndInvokeSubscriptionProcessor*");
    	    	
    	    }              
    	});
    	
    	//We mock the web service endpoints that we call here for unsubscriptions
    	context.getRouteDefinition("transformAndInvokeUnsubscriptionProbationProcessorRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the subscription manager endpoint
    	    	mockEndpointsAndSkip("cxf:bean:subscriptionManagerService*");
    	    	
    	    }              
    	});

    	//We mock the web service endpoints that we call here for subscriptions
    	context.getRouteDefinition("transformAndInvokeSubscriptionProcessorRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the subscription manager endpoint
    	    	mockEndpointsAndSkip("cxf:bean:notificationBrokerService*");
    	    	
    	    }              
    	});

    	
		context.start();		
	}
    
	@Test
	public void testUnsubscriptionRoute() throws Exception{
		
    	//Subscription Manager will get one message - an unsubscribe message
		subscriptionManagerServiceMockEndpoint.expectedMessageCount(1);
		
		//In the main bundle, the derived route we call is a 'direct' message to the unsubscription endpoint
		//When extending this bundle, the unsubscription message can be enhanced with additional info and a 
		//'direct-vm' endpoint can be called while will turn around and call this direct endpoint
		derivedUnsubscriptionEndpoint.expectedMessageCount(1);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

	    //Read the case termination from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/probation/probationCaseTermination.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body and add the web service operation name
	    senderExchange.getIn().setBody(inputStr);
	    senderExchange.getIn().setHeader("operationName", "Report-Probation-Case-Termination");
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:processProbationDocument", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);

		//Assert that the mock endpoint expectations are satisfied
		subscriptionManagerServiceMockEndpoint.assertIsSatisfied();
		derivedUnsubscriptionEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = subscriptionManagerServiceMockEndpoint.getExchanges().get(0);
		
		//Confirm operation names and namespaces
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals(CXF_UNSUBSCRIBE_OPERATION_NAME, opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals(CXF_UNSUBSCRIPTION_OPERATION_NAMESPACE, opNamespace);

		Document unsubscriptionDocument = ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(unsubscriptionDocument);

		String topic = XmlUtils.xPathStringSearch(unsubscriptionDocument, "/b-2:Unsubscribe/b-2:TopicExpression");
		
		//Assert that the topic is set correctly (the XSLT tests test the other xpaths)
		assertEquals("topics:person/arrest", topic);
		
		log.info("Unsubscription topic is: " + topic);
		
		
	}
	
	@Test
	public void testSubscriptionRoute() throws Exception{
		
    	//Subscription Manager will get one message - a subscribe message
		notificationBrokerServiceMockEndpoint.expectedMessageCount(1);
		
		//In the main bundle, the derived route we call is a 'direct' message to the subscription endpoint
		//When extending this bundle, the subscription message can be enhanced with additional info and a 
		//'direct-vm' endpoint can be called while will turn around and call this direct endpoint
		derivedSubscriptionEndpoint.expectedMessageCount(1);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

	    //Read the case initiation from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/probation/probationCaseInitiation.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body and add the web service operation name
	    senderExchange.getIn().setBody(inputStr);
	    senderExchange.getIn().setHeader("operationName", "Report-Probation-Case-Initiation");
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:processProbationDocument", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);

		//Assert that the mock endpoint expectations are satisfied
		notificationBrokerServiceMockEndpoint.assertIsSatisfied();
		derivedSubscriptionEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = notificationBrokerServiceMockEndpoint.getExchanges().get(0);
		
		//Confirm operation names and namespaces
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals(CXF_SUBSCRIBE_OPERATION_NAME, opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals(CXF_SUBSCRIPTION_OPERATION_NAMESPACE, opNamespace);

		Document subscriptionDocument = ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(subscriptionDocument);

		String topic = XmlUtils.xPathStringSearch(subscriptionDocument, "/b-2:Subscribe/b-2:Filter/b-2:TopicExpression");
		
		//Assert that the topic is set correctly (the XSLT tests test the other xpaths)
		assertEquals("topics:person/arrest", topic);
		
		log.info("Subscription topic is: " + topic);
		
		
	}	
	
}
