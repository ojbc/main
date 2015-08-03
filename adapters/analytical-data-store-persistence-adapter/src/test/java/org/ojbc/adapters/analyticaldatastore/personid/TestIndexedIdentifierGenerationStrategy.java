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
package org.ojbc.adapters.analyticaldatastore.personid;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class TestIndexedIdentifierGenerationStrategy {
	
	private IndexedIdentifierGenerationStrategy strategy;
	private Map<String, Object> attributeMap;
	
	@Before
	public void setUp() throws Exception {
		
		strategy = new IndexedIdentifierGenerationStrategy();
		
		attributeMap = new HashMap<String, Object>();
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "George");
		attributeMap.put(IndexedIdentifierGenerationStrategy.LAST_NAME_FIELD, "Washington");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, "M");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 3));
		
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

		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Henry");
		id2 = strategy.generateIdentifier(attributeMap);
		assertNotSame(id, id2);
		
	}

	private Object makeDate(int year, int monthOfYear, int dayOfMonth) {
		DateTime d = new DateTime(year, monthOfYear, dayOfMonth, 1, 1, 1, 0);
		return d.toDate();
	}

}
