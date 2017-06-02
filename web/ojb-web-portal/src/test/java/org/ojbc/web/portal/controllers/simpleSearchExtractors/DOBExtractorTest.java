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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public class DOBExtractorTest {

	DOBExtractor unit;
	private PersonSearchRequest personSearchRequest;

	@Before
	public void setup() {
		unit = new DOBExtractor();
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyInput() {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirth(), nullValue());
	}

	@Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("12-32213","somestring","","2001/12/22", "January 12,1999","2/11/2011","02/12/99","AA02/11/1999"), personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirth(), nullValue());
	}

	@Test
	public void dobMatched() {
		unit.extractTerm(Arrays.asList("02/11/1999"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDateOfBirth().getYear(),Matchers.is(1999));
		assertThat(personSearchRequest.getPersonDateOfBirth().getMonthOfYear(),Matchers.is(2));
		assertThat(personSearchRequest.getPersonDateOfBirth().getDayOfMonth(),Matchers.is(11));
	}

	@Test
	public void dobOnlyTakesFirstDOBFound() {
		unit.extractTerm(Arrays.asList("02/11/1999","05/12/2001"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDateOfBirth().getYear(),Matchers.is(1999));
		assertThat(personSearchRequest.getPersonDateOfBirth().getMonthOfYear(),Matchers.is(2));
		assertThat(personSearchRequest.getPersonDateOfBirth().getDayOfMonth(),Matchers.is(11));
	}

	@Test
	public void removesExtractedToken() {
		List<String> extractTerm = unit.extractTerm(Arrays.asList("02/11/1999","noMatch"), personSearchRequest);
		
		assertThat(extractTerm.size(),is(1));
		assertThat(extractTerm.get(0),is("noMatch"));
	}
}
