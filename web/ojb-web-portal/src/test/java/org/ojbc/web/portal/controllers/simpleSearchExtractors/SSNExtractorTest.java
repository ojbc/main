package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public class SSNExtractorTest {

	SSNExtractor unit;
	private PersonSearchRequest personSearchRequest;

	@Before
	public void setup() {
		unit = new SSNExtractor();
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyInput() {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);

		assertThat(personSearchRequest.getPersonSocialSecurityNumber(), nullValue());
	}

	@Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("","somestring","12334","A12334B","333445555","555 55 5555"), personSearchRequest);

		assertThat(personSearchRequest.getPersonSocialSecurityNumber(), nullValue());
	}

	@Test
	public void SSNMatched() {
		unit.extractTerm(Arrays.asList("555-55-5555"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonSocialSecurityNumber(),is("555-55-5555"));
	}

	@Test
	public void SSNOnlyTakesFirstDOBFound() {
		unit.extractTerm(Arrays.asList("666-66-6666","555-55-5555"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonSocialSecurityNumber(),is("666-66-6666"));
	}

	@Test
	public void removesExtractedToken() {
		List<String> extractTerm = unit.extractTerm(Arrays.asList("555-55-5555","noMatch"), personSearchRequest);
		
		assertThat(extractTerm.size(),is(1));
		assertThat(extractTerm.get(0),is("noMatch"));
	}
}
