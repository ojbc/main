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
package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.OjbcWebPortalApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(args = {"--spring.config.additional-location=classpath:/"}, 
	classes = OjbcWebPortalApplication.class)
@ContextConfiguration({"classpath:beans/static-configuration-demostate.xml"})
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class SIDExtractorTest {

	@Autowired
	SIDExtractor unit;
	private PersonSearchRequest personSearchRequest;

	@Before
	public void setup() {
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyInput() {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);

		assertThat(personSearchRequest.getPersonSID(), nullValue());
	}

	@Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("","somestring","12334","A12334B","A23 434","A!123"), personSearchRequest);

		assertThat(personSearchRequest.getPersonSID(), nullValue());
	}

	@Test
	public void SIDMatched() {
		unit.extractTerm(Arrays.asList("A1234"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonSID(),is("A1234"));
		
		String originalRegEx = unit.sidRegex;
		
		unit.sidRegex = "([C/c][O/o]\\d{7}+)";

		unit.extractTerm(Arrays.asList("CO1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonSID(),is("CO1234567"));
		personSearchRequest.setPersonSID(null);

		unit.extractTerm(Arrays.asList("Co1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonSID(),is("CO1234567"));
		personSearchRequest.setPersonSID(null);
		
		unit.extractTerm(Arrays.asList("co1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonSID(),is("CO1234567"));
		personSearchRequest.setPersonSID(null);
		
		unit.extractTerm(Arrays.asList("cO1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonSID(),is("CO1234567"));
		personSearchRequest.setPersonSID(null);

		unit.extractTerm(Arrays.asList("CO12345678"), personSearchRequest);
		assertNull(personSearchRequest.getPersonSID());
		personSearchRequest.setPersonSID(null);
		unit.sidRegex=originalRegEx;

	}

	@Test
	public void SIDOnlyTakesFirstDOBFound() {
		unit.extractTerm(Arrays.asList("A1234","A4567"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonSID(),is("A1234"));
	}

	@Test
	public void removesExtractedToken() {
		List<String> extractTerm = unit.extractTerm(Arrays.asList("A1234","noMatch"), personSearchRequest);
		
		assertThat(extractTerm.size(),is(1));
		assertThat(extractTerm.get(0),is("noMatch"));
	}
}
