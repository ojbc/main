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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestPersonQueryWarrantProcessor {

	private static final Log log = LogFactory.getLog(TestPersonQueryWarrantProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonQueryWarrantRequest() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "999888777666555");
		
		PersonQueryWarrantResponseSQLProcessor personQueryResponseProcessor = new PersonQueryWarrantResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		personQueryResponseProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/Person_Query_Results_-_Warrants.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		PersonQueryWarrantResponse personQueryWarrantResponse = personQueryResponseProcessor.processPersonQueryWarrantResponse(document);
		
		log.info(personQueryWarrantResponse.toString());
		
		assertEquals("Mickey", personQueryWarrantResponse.getFirstName());
		assertEquals("M", personQueryWarrantResponse.getMiddleName());
		assertEquals("Mouse", personQueryWarrantResponse.getLastName());
		
		assertEquals("888888888", personQueryWarrantResponse.getFbiId());
		assertEquals("999999999", personQueryWarrantResponse.getSid());
		assertEquals("Warrants", personQueryWarrantResponse.getSystemName());
		
		personQueryResponseProcessor.auditPersonQueryWarrantResponseResponse(document, "999888777666555");
		
		inputFile = new File("src/test/resources/xmlInstances/warrants-access-denied.xml");
		
        document = db.parse(inputFile);
		
		personQueryWarrantResponse = personQueryResponseProcessor.processPersonQueryWarrantResponse(document);
		
		log.info(personQueryWarrantResponse.toString());
		
		assertEquals(true, personQueryWarrantResponse.isQueryResultsAccessDeniedIndicator());
		assertEquals("User does not meet requirements to access warrants.", personQueryWarrantResponse.getQueryResultsErrorText());

	}
	
}
