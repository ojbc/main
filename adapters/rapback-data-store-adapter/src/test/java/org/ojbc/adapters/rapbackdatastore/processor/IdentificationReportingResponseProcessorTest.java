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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.test.util.IgnoreNamedElementsDifferenceListener;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"})
public class IdentificationReportingResponseProcessorTest {
	
	@Autowired
	IdentificationReportingResponseProcessor identificationReportingResponseProcessor;

	@BeforeEach
	public void setUp() throws Exception {
		assertNotNull(identificationReportingResponseProcessor);
	}

	@Test
	public void testCreateErrorResponse() throws SAXException, IOException {
		Document document = identificationReportingResponseProcessor
				.createErrorResponse("000001820140729014008340000", "Task ID-REPLY");
		String documentString = OJBUtils.getStringFromDocument(document); 
		System.out.println("Error doc: \n" + documentString );
		
        assertAsExpected(documentString, "src/test/resources/xmlInstances/"
        		+ "identificationReportingResponse/person_identification_report_failure_response.xml");   

	}

	public static void assertAsExpected(String documentString, String expectedFileLocation) throws IOException,
			SAXException {
		File expectedReponseFile = new File(expectedFileLocation);
        String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile, StandardCharsets.UTF_8);
        
        //Use XML Unit to compare these files
        Diff myDiff = new Diff(documentString, expectedResponseAsString);
        myDiff.overrideDifferenceListener(new IgnoreNamedElementsDifferenceListener("nc30:Date"));
        Assert.assertTrue("XML identical " + myDiff.toString(),
                       myDiff.identical());
	}

	@Test
	public void testCreateSuccessResponse() throws IOException, SAXException {
		Document document = identificationReportingResponseProcessor.createSuccessResponse("000001820140729014008340000", "Task ID-REPLY");
		String documentString = OJBUtils.getStringFromDocument(document); 
		System.out.println("Success doc: \n" + documentString );
		assertAsExpected(documentString, "src/test/resources/xmlInstances/"
        		+ "identificationReportingResponse/person_identification_report_success_response.xml");
	}

}
