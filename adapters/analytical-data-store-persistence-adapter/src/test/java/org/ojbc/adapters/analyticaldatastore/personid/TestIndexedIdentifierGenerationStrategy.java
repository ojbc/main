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
	
	@Before
	public void setUp() throws Exception {
		strategy = new IndexedIdentifierGenerationStrategy();
	}
	
	@Test
	public void test() throws Exception {
		
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "George");
		attributeMap.put(IndexedIdentifierGenerationStrategy.LAST_NAME_FIELD, "Washington");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, "M");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 3));
		
		String id = strategy.generateIdentifier(attributeMap);
		String id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Henry");
		id2 = strategy.generateIdentifier(attributeMap);
		assertNotSame(id, id2);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "Georg");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Heriberto");
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 13));
		id2 = strategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);

	}

	private Object makeDate(int year, int monthOfYear, int dayOfMonth) {
		DateTime d = new DateTime(year, monthOfYear, dayOfMonth, 1, 1, 1, 0);
		return d.toDate();
	}

}
