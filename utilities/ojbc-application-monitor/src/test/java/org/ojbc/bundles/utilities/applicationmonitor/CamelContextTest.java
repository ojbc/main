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
package org.ojbc.bundles.utilities.applicationmonitor;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/properties-context.xml",
		})
public class CamelContextTest {

	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:mockMailEndpoint")
    protected MockEndpoint mockMail;
    
    @Before
	public void setUp() throws Exception {

    	context.getRouteDefinition("sendAlertRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the mail notification endpoint
    	    	interceptSendToEndpoint("log:org.ojbc.bundles.utilities.applicationmonitor?level=INFO").to("mock:mockMailEndpoint");
    	    	
    	    }              
    	});
    	
		context.start();		
	}

	@Test
	public void testApplicationMonitor() throws Exception {

		mockMail.reset();
		
		Thread.sleep(2500);
		
		mockMail.expectedMessageCount(1);
		mockMail.assertIsSatisfied();
		
		Exchange mailMessage = mockMail.getExchanges().get(0);
		assertEquals("This is the subject", mailMessage.getIn().getHeader("subject"));
		assertEquals("someone@someemail.com", mailMessage.getIn().getHeader("to"));
		assertEquals("someone_else@someemail.com", mailMessage.getIn().getHeader("from"));
		assertEquals("This is the example body", mailMessage.getIn().getBody());

	}
	
}
