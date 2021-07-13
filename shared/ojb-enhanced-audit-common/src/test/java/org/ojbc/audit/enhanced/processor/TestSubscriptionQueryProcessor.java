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
package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestSubscriptionQueryProcessor {

	private static final Log log = LogFactory.getLog(TestSubscriptionQueryProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonQueryWarrantRequest() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "123456645");
		
		SubscriptionQueryResponseSQLProcessor subscriptionQueryResponseSQLProcessor = new SubscriptionQueryResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		subscriptionQueryResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/SubscriptionQueryResults.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		SubscriptionQueryResponse subscriptionQueryResponse = subscriptionQueryResponseSQLProcessor.processSubscriptionQueryResponse(document);
		
		log.info(subscriptionQueryResponse.toString());
		
		assertEquals("7777777", subscriptionQueryResponse.getFbiSubscriptionId());
		assertEquals("99999999", subscriptionQueryResponse.getSubscriptionQualifierId());
		
		subscriptionQueryResponseSQLProcessor.auditSubscriptionQueryResponse(document, "123456645");
		
		inputFile = new File("src/test/resources/xmlInstances/SubscriptionQueryResults-AccessDenied.xml");
		
        document = db.parse(inputFile);
		
        subscriptionQueryResponse = subscriptionQueryResponseSQLProcessor.processSubscriptionQueryResponse(document);
		
		log.info(subscriptionQueryResponse.toString());
		
		assertEquals(true, subscriptionQueryResponse.getQueryResultsAccessDeniedIndicator());
		assertEquals("String", subscriptionQueryResponse.getQueryResultsErrorText());

		inputFile = new File("src/test/resources/xmlInstances/SubscriptionQueryResults-Error.xml");
		
        document = db.parse(inputFile);
		
        subscriptionQueryResponse = subscriptionQueryResponseSQLProcessor.processSubscriptionQueryResponse(document);
		
		log.info(subscriptionQueryResponse.toString());
		
		assertEquals(true, subscriptionQueryResponse.getQueryResultsErrorIndicator());
		assertEquals("String", subscriptionQueryResponse.getQueryResultsErrorText());

	}
	
}
