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
package org.ojbc.bundles.utilities.email;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.mail.Email;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/test-beans.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"})
public class TestEmailRestImpl {

	private Logger logger = Logger.getLogger(TestEmailRestImpl.class.getName());
	
    @Resource
    private ModelCamelContext context;

    @Resource
    private RestTemplate restTemplate;
    
	@Before
	public void setUp() throws Exception {
		
    	context.start();
	}
    
	@Test
	public void testEmailRestImplSend() throws Exception
	{
		final String uri = "http://localhost:9898/emailServer/email/sendEmail";
		
		Email emailRequest = new Email();
		emailRequest.setTo("To");
		emailRequest.setBody("body");
		emailRequest.setSubject("subject");
		
		Email emailResult = restTemplate.postForObject(uri, emailRequest, Email.class);
		
		logger.info(emailResult.toString());
		
		assertEquals("To", emailResult.getTo());
		assertEquals("body", emailResult.getBody());
		assertEquals("subject", emailResult.getSubject());
	}
	
}
