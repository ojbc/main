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
package org.ojbc.web.portal.controllers.dto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class PersonFilterCommandTest {

	PersonFilterCommand unit;
	
	@Before
	public void setup() {
		unit = new PersonFilterCommand();
	}
	
	@Test
	public void getParamsMap() {
		unit.setFilterAgeRangeStart(14);
		unit.setFilterAgeRangeEnd(18);
		unit.setFilterPersonRaceCode("some Race Code");
		unit.setFilterPersonEyeColor("some Eye Color Code");
		unit.setFilterPersonHairColor("brown");
		unit.setFilterHeightInFeet(5);
		unit.setFilterHeightInInches(11);	
		unit.setFilterHeightTolerance(3);			
		unit.setFilterWeight(100);
		unit.setFilterWeightTolerance(70);
		
		//act
		Map<String, Object> map = unit.getParamsMap();
		
		assertThat((Integer)map.get("filterAgeRangeStart"),is(14));
		assertThat((Integer)map.get("filterAgeRangeEnd"),is(18));
		assertThat((String)map.get("filterPersonRaceCode"),is("some Race Code"));		
		assertThat((String)map.get("filterPersonEyeColor"),is("some Eye Color Code"));			
		assertThat((String)map.get("filterPersonHairColor"),is("brown"));			
		assertThat((Integer)map.get("filterHeightInFeet"),is(5));			
		assertThat((Integer)map.get("filterHeightInInches"),is(11));			
		assertThat((Integer)map.get("filterHeightTolerance"),is(3));			
		assertThat((Integer)map.get("filterWeight"),is(100));			
		assertThat((Integer)map.get("filterWeightTolerance"),is(70));	
		
		LocalDate today = new LocalDate();
		LocalDate expectedStartDOB = today.minusYears((Integer)map.get("filterAgeRangeEnd")).minusYears(1).plusDays(1);
		LocalDate expectedEndDOB = today.minusYears((Integer)map.get("filterAgeRangeStart"));
		
		assertThat((String)map.get("filterDOBStart"),is(expectedStartDOB.toString("yyyy-MM-dd")));
		assertThat((String)map.get("filterDOBEnd"),is(expectedEndDOB.toString("yyyy-MM-dd")));
		
	}

}
