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

public class SIDExtractorTest {

	SIDExtractor unit;
	private PersonSearchRequest personSearchRequest;

	@Before
	public void setup() {
		unit = new SIDExtractor();
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
