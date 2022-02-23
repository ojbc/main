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
package org.ojbc.adapters.analyticaldatastore.service;



import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.analyticaldatastore.application.IncidentArrestAnalyticsStagingAdapterApplication;
import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.processor.PretrialEnrollmentReportProcessor.AssessedNeeds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@CamelSpringBootTest
@SpringBootTest(classes=IncidentArrestAnalyticsStagingAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
@ContextConfiguration(locations={
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/dao.xml"
		})
public class DescriptionCodeLookupServiceTest {
	//private final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DescriptionCodeLookupService descriptionCodeLookupService;
	
	@BeforeEach
	public void setUp() throws Exception {
		assertNotNull(descriptionCodeLookupService);
	}

	@Test
	public void test(){
		assertEquals(Integer.valueOf(1), descriptionCodeLookupService.retrieveCode(CodeTable.PersonRace, "A"));
		assertEquals(Integer.valueOf(2), descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, "F"));
		assertNull(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, "W"));
		assertEquals(Integer.valueOf(1), descriptionCodeLookupService.retrieveCode(CodeTable.County, "Harrison"));
		assertEquals(Integer.valueOf(2), descriptionCodeLookupService.retrieveCode(CodeTable.AssessedNeed, AssessedNeeds.SubstanceAbuse.toString()));
		assertEquals(Integer.valueOf(1),descriptionCodeLookupService.retrieveCode(CodeTable.Agency, "Placeholder Agency Name"));
	}

}
