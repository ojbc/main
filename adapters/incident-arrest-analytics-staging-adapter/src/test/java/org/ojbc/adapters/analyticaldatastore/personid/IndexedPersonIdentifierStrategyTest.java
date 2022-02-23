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
package org.ojbc.adapters.analyticaldatastore.personid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.analyticaldatastore.application.IncidentArrestAnalyticsStagingAdapterApplication;
import org.ojbc.util.lucene.personid.IndexedIdentifierGenerationStrategy;
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
		"classpath:META-INF/spring/dao.xml"
		})
public class IndexedPersonIdentifierStrategyTest {

	@Autowired
	private IndexedPersonIdentifierStrategy indexedIdentifierGenerationStrategy;
	
	private Map<String, Object> attributeMap;

	@BeforeEach
	public void setUp() throws Exception {
		attributeMap = new HashMap<String, Object>();
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "George");
		attributeMap.put(IndexedIdentifierGenerationStrategy.LAST_NAME_FIELD, "Washington");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, "M");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 3));
	}

	@Test
	public void testBackup() throws Exception {
		
		String id = indexedIdentifierGenerationStrategy.generateIdentifier(attributeMap);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, null);
		String id2 = indexedIdentifierGenerationStrategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		String backupDirectoryPath = indexedIdentifierGenerationStrategy.backup();

		File backupDirectory = new File(backupDirectoryPath);
		
		assertTrue(backupDirectory.exists());
		assertTrue(backupDirectory.list().length>0);
		
		if (backupDirectory.exists())
		{	
			FileUtils.deleteDirectory(backupDirectory);
		}	
	}
	
	private Object makeDate(int year, int monthOfYear, int dayOfMonth) {
		LocalDate today = LocalDate.of(year, monthOfYear, dayOfMonth);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		String dateTimeStamp = today.format(dtf);
		
		return dateTimeStamp;
	}
}
