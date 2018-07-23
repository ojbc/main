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
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestIdentificationQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(TestIdentificationQueryResponseProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessIdentificationQueryResponse() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "999888777666555");
		
		IdentificationQueryResponseSQLProcessor identificationQueryResponseSQLProcessor = new IdentificationQueryResponseSQLProcessor();
		identificationQueryResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/OrganizationIdentificationInitialResultsQueryResults.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
        IdentificationQueryResponse identificationQueryResponse = identificationQueryResponseSQLProcessor.processIdentificationQueryResponse(document);
		
		log.info(identificationQueryResponse.toString());
		
		assertEquals("Michael", identificationQueryResponse.getPersonFirstName());
		assertEquals("Aaron", identificationQueryResponse.getPersonMiddleName());
		assertEquals("Scott", identificationQueryResponse.getPersonLastName());
		
		assertEquals("F987654321", identificationQueryResponse.getFbiId());
		assertEquals("A123456", identificationQueryResponse.getSid());
		
		assertEquals("A1000999", identificationQueryResponse.getOtn());
		
		assertEquals("ORI1234", identificationQueryResponse.getOri());

		identificationQueryResponseSQLProcessor.auditIdentificationQueryResponse(document, "999888777666555");
		

	}
	
}
