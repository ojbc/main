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
package org.ojbc.bundles.intermediaries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.probation.ProbationEventHandlerServiceApplication;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

import jakarta.annotation.Resource;

@CamelSpringBootTest
@SpringBootTest(classes=ProbationEventHandlerServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/jetty-server.xml"}) 
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
    
    @EndpointInject(value = "mock:cxf:bean:subscriptionManagerService")
    protected MockEndpoint subscriptionManagerServiceMockEndpoint;

    @EndpointInject(value = "mock:cxf:bean:notificationBrokerService")
    protected MockEndpoint notificationBrokerServiceMockEndpoint;
    
    @EndpointInject(value = "mock:cxf:bean:probationAnalyticsAdapterService")
    protected MockEndpoint probationAnalyticsAdapterServiceMockEndpoint;
    
    @EndpointInject(value = "mock:direct:transformAndInvokeUnsubscriptionProbationProcessor")
    protected MockEndpoint derivedUnsubscriptionEndpoint;
    
    @EndpointInject(value = "mock:direct:transformAndInvokeSubscriptionProcessor")
    protected MockEndpoint derivedSubscriptionEndpoint;
    
	@BeforeEach
	public void setUp() throws Exception {
		
		//We mock the 'transformAndInvokeUnsubscriptionProbationProcessor' and 'direct:transformAndInvokeSubscriptionProcessor' endpoint to test against.
		AdviceWith.adviceWith(context, "transformAndInvokeSubscriptionProcessorRoute", route -> {
			route.weaveById("notificationBrokerServiceEndpoint").replace().to(notificationBrokerServiceMockEndpoint);
		});
		
		AdviceWith.adviceWith(context, "transformAndInvokeUnsubscriptionProbationProcessorRoute", route -> { 
			route.weaveById("subscriptionManagerServiceEndpoint").replace().to(subscriptionManagerServiceMockEndpoint);
		});
		
		
    	//We mock the web service endpoints that we call here for unsubscriptions
		AdviceWith.adviceWith(context, "transformAndInvokeUnsubscriptionProbationProcessorRoute", route -> {
			route.mockEndpointsAndSkip("cxf:bean:subscriptionManagerService*");
		});

    	//We mock the web service endpoints that we call here for subscriptions
		AdviceWith.adviceWith(context, "transformAndInvokeSubscriptionProcessorRoute", route -> {
			route.mockEndpointsAndSkip("cxf:bean:notificationBrokerService*");
		});

    	//We mock the web service endpoints that we call here for analytics
		AdviceWith.adviceWith(context, "callProbationAnalyticsAdapterRoute", route -> {
			route.mockEndpointsAndSkip("cxf:bean:probationAnalyticsAdapterService*");
		});
    	
		context.start();		
	}
    
	@Test
	@Disabled
	public void testUnsubscriptionRoute() throws Exception{
		
		probationAnalyticsAdapterServiceMockEndpoint.reset();
		probationAnalyticsAdapterServiceMockEndpoint.expectedMessageCount(1);
		
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
		
		//XmlUtils.printNode(unsubscriptionDocument);

		String topic = XmlUtils.xPathStringSearch(unsubscriptionDocument, "/b-2:Unsubscribe/b-2:TopicExpression");
		
		//Assert that the topic is set correctly (the XSLT tests test the other xpaths)
		assertEquals("topics:person/arrest", topic);
		
		log.info("Unsubscription topic is: " + topic);
		
		probationAnalyticsAdapterServiceMockEndpoint.assertIsSatisfied();
		
		ex = probationAnalyticsAdapterServiceMockEndpoint.getExchanges().get(0);
		
		Document analyticsDocument = ex.getIn().getBody(Document.class);
		
		assertNotNull(analyticsDocument);
		
		assertEquals("5c72b646d5570db45f98ad3287a23acaff6f2c21922096c714ced37d3163f838", XmlUtils.xPathStringSearch(analyticsDocument, "/pct:ProbationCaseTermination/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/pcext:PersonPersistentIdentification/nc:IdentificationID"));
		
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pct:ProbationCaseTermination/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc:PersonName"));
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pct:ProbationCaseTermination/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc:PersonSSNIdentification"));
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pct:ProbationCaseTermination/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification"));
		
	}
	
	@Test
	@Disabled
	public void testSubscriptionRoute() throws Exception{
		
		probationAnalyticsAdapterServiceMockEndpoint.reset();
		probationAnalyticsAdapterServiceMockEndpoint.expectedMessageCount(1);
		
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
		
		//XmlUtils.printNode(subscriptionDocument);

		String topic = XmlUtils.xPathStringSearch(subscriptionDocument, "/b-2:Subscribe/b-2:Filter/b-2:TopicExpression");
		
		//Assert that the topic is set correctly (the XSLT tests test the other xpaths)
		assertEquals("topics:person/arrest", topic);
		
		log.info("Subscription topic is: " + topic);
		
		probationAnalyticsAdapterServiceMockEndpoint.assertIsSatisfied();
		
		ex = probationAnalyticsAdapterServiceMockEndpoint.getExchanges().get(0);
		
		Document analyticsDocument = ex.getIn().getBody(Document.class);
		
		assertNotNull(analyticsDocument);
		
		//XmlUtils.printNode(analyticsDocument);
		
		assertEquals("5b57c0c45e42a4ef4e6c28e78dc1c555d6b943ea3287353445ff80cc516627db", XmlUtils.xPathStringSearch(analyticsDocument, "/pci:ProbationCaseInitiation/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/pcext:PersonPersistentIdentification/nc:IdentificationID"));
		
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pci:ProbationCaseInitiation/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc:PersonName"));
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pci:ProbationCaseInitiation/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc:PersonSSNIdentification"));
		assertNull(XmlUtils.xPathStringSearch(analyticsDocument, "/pci:ProbationCaseInitiation/pcext:ProbationCase/pcext:Supervision/pcext:Probationer/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification"));
		
	}	
	
}
