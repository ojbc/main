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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticaldatastore.booking.personid;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.adapters.analyticaldatastore.booking.personid.IndexedIdentifierGenerationStrategy;

public class TestIndexedIdentifierGenerationStrategy {
	
	private IndexedIdentifierGenerationStrategy strategy;
	private Map<String, Object> attributeMap;
	private String tempFilePath;
	private static final Log log = LogFactory.getLog(TestIndexedIdentifierGenerationStrategy.class);
	
	@Before
	public void setUp() throws Exception {
		
		//create a temp file
		File temp = File.createTempFile("temp-file-name", ".tmp"); 
		
		log.debug("Temp file : " + temp.getAbsolutePath());
		
		//Get temp file path
		String absolutePath = temp.getAbsolutePath();
		tempFilePath = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		
		if ( !(tempFilePath.endsWith("/") || tempFilePath.endsWith("\\")) )
		{	
			tempFilePath = tempFilePath + System.getProperty("file.separator");
		}
			   
		if (strategy == null)
		{	
			strategy = new IndexedIdentifierGenerationStrategy(tempFilePath + "lucene", tempFilePath + "backup");
		}	
		
		attributeMap = new HashMap<String, Object>();
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "George");
		attributeMap.put(IndexedIdentifierGenerationStrategy.LAST_NAME_FIELD, "Washington");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, "M");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 3));
		
	}
	
	@After
	public void tearDown() throws Exception {
		File directory = new File(tempFilePath + "lucene");
		
		if (directory.exists())
		{	
			FileUtils.deleteDirectory(directory);
		}	
		
		strategy.destroy();
	}
	
	@Test
	public void test() throws Exception {
		
		String id = strategy.generateIdentifier(attributeMap);
		String id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		/*
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Henry");
		id2 = strategy.generateIdentifier(attributeMap);
		assertNotSame(id, id2);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Georg");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Heriberto");
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 13));
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
*/
	}
	
	@Test
	public void testFirstNameResolution() throws Exception {
		
		strategy.setResolveEquivalentNames(false);
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Ezekiel");
		String id = strategy.generateIdentifier(attributeMap);
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Zeke");
		String id2 = strategy.generateIdentifier(attributeMap);
		
		assertNotSame(id, id2);
		
		setUp();
		strategy.setResolveEquivalentNames(true);
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Ezekiel");
		id = strategy.generateIdentifier(attributeMap);
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Ezekiel");
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Zeke");
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);

		setUp();
		strategy.setResolveEquivalentNames(true);
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		id = strategy.generateIdentifier(attributeMap);
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "");
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		setUp();
		strategy.setResolveEquivalentNames(true);
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "M");
		id = strategy.generateIdentifier(attributeMap);
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "M.");
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);

	}
	
	@Test
	public void testOptionalFieldsMissing() throws Exception {
		
		String id = strategy.generateIdentifier(attributeMap);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, null);
		String id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, "123-45-6789");
		String id3 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id3);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, "987-65-4321");
		String id4 = strategy.generateIdentifier(attributeMap);
		assertNotSame(id3, id4);
		assertNotSame(id, id4);
		String id5 = strategy.generateIdentifier(attributeMap);
		assertEquals(id4, id5);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		String id6 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id6);

		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, "123-45-6789");
		String id7 = strategy.generateIdentifier(attributeMap);
		assertEquals(id6, id7);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		String id8 = strategy.generateIdentifier(attributeMap);
		assertEquals(id8, id7);

		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Henry");
		id2 = strategy.generateIdentifier(attributeMap);
		assertNotSame(id, id2);
		
	}

	@Test
	public void testBackup() throws Exception {
		
		String id = strategy.generateIdentifier(attributeMap);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, null);
		String id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		String backupDirectoryPath = strategy.backup();

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
