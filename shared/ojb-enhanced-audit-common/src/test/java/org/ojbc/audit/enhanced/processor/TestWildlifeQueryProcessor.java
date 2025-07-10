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

import org.apache.camel.test.junit5.params.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.w3c.dom.Document;

@CamelSpringTest
@SpringJUnitConfig(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestWildlifeQueryProcessor {

	private static final Log log = LogFactory.getLog(TestWildlifeQueryProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessProfessionalLicensingQueryResponse() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "123456213");
		
		WildlifeQueryResponseSQLProcessor wildlifeQueryResponseSQLProcessor = new WildlifeQueryResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		wildlifeQueryResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/WildlifeLicenseQueryResults.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		WildlifeQueryResponse wildlifeQueryResponse = wildlifeQueryResponseSQLProcessor.processWildlifeQueryResponse(document);
		
		log.info(wildlifeQueryResponse.toString());
		
		assertEquals("MOSES", wildlifeQueryResponse.getSystemName());
		assertEquals("BELMONT", wildlifeQueryResponse.getResidenceCity());
	}
	
}
