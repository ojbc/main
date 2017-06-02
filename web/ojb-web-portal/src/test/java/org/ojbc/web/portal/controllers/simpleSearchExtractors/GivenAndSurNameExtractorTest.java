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

import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public class GivenAndSurNameExtractorTest {

	GivenAndSurNameExtractor unit;
	private PersonSearchRequest personSearchRequest;

	@Before
	public void setup() {
		unit = new GivenAndSurNameExtractor();
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyStringInput() {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);

		assertThat(personSearchRequest.getPersonSurName(), nullValue());
		assertThat(personSearchRequest.getPersonGivenName(), nullValue());
	}

	@Test
	public void surNameWhenOneToken() {
		unit.extractTerm(Arrays.asList("lastName"), personSearchRequest);

		assertThat(personSearchRequest.getPersonSurName(), is("lastName"));
		assertThat(personSearchRequest.getPersonSurNameMetaData(), is(SearchFieldMetadata.ExactMatch));

	}

	@Test
	public void surNameAndFirstNameWhenTwoTokens() {
		unit.extractTerm(Arrays.asList("firstName", "lastName"), personSearchRequest);

		assertThat(personSearchRequest.getPersonGivenName(), is("firstName"));
		assertThat(personSearchRequest.getPersonGivenNameMetaData(), is(SearchFieldMetadata.ExactMatch));

		assertThat(personSearchRequest.getPersonSurName(), is("lastName"));
		assertThat(personSearchRequest.getPersonSurNameMetaData(), is(SearchFieldMetadata.ExactMatch));
	}

	@Test
	public void surNameAndFirstNameStartsWith() {
		unit.extractTerm(Arrays.asList("firstName*", "lastName*"), personSearchRequest);

		assertThat(personSearchRequest.getPersonGivenName(), is("firstName"));
		assertThat(personSearchRequest.getPersonGivenNameMetaData(), is(SearchFieldMetadata.StartsWith));

		assertThat(personSearchRequest.getPersonSurName(), is("lastName"));
		assertThat(personSearchRequest.getPersonSurNameMetaData(), is(SearchFieldMetadata.StartsWith));
	}

	@Test
	public void surNameAndFirstNameWithMultipleTokensForSurName() {
		unit.extractTerm(Arrays.asList("firstName*", "two part last", "name"), personSearchRequest);

		assertThat(personSearchRequest.getPersonSurName(), is("two part last name"));
	}

	@Test
	public void surNameAndFirstNameDoesNotContainNumericCharacters() {
		unit.extractTerm(Arrays.asList("222-33-3333", "44", "7eleven", " Mr. 2 "), personSearchRequest);

		assertThat(personSearchRequest.getPersonSurName(), nullValue());
		assertThat(personSearchRequest.getPersonGivenName(), nullValue());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void extractTermLocalNotUsed() {
		unit.extractTermLocal(null, personSearchRequest);
	}
	

	@Test
	public void removesExtractedToken() {
		List<String> extractTerm = unit.extractTerm(Arrays.asList("lastName","noMatch1"), personSearchRequest);
		
		assertThat(extractTerm.size(),is(1));
		assertThat(extractTerm.get(0),is("noMatch1"));
	}

}
