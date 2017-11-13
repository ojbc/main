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
package org.ojbc.bundles.utilities.auditing;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
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
public class TestAuditRestImpl {

	private Logger logger = Logger.getLogger(TestAuditRestImpl.class.getName());
	
    @Resource
    private ModelCamelContext context;

    @Resource
    private RestTemplate restTemplate;
    
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
    
	@Before
	public void setUp() throws Exception {
		
    	context.start();
	}
    
	@Test
	public void testAuditRestImplSend() throws Exception
	{
		final String uri = "http://localhost:9898/auditServer/audit/printResults";
		
		PrintResults printResults = new PrintResults();
		
		printResults.setDescription("description");
		printResults.setMessageId("12345");
		printResults.setSystemName("system name");
		
		PrintResults printResultsResponse = restTemplate.postForObject(uri, printResults, PrintResults.class);
		
		logger.info(printResultsResponse.toString());
		
		assertEquals("description", printResultsResponse.getDescription());
		assertEquals("12345", printResultsResponse.getMessageId());
		assertEquals("system name", printResultsResponse.getSystemName());
		
		PrintResults printResultsFromDB = enhancedAuditDao.retrievePrintResultsfromMessageID("12345");
		
		assertEquals("description", printResultsFromDB.getDescription());
		assertEquals("12345", printResultsFromDB.getMessageId());
		assertEquals("system name", printResultsFromDB.getSystemName());
	}
	
}
