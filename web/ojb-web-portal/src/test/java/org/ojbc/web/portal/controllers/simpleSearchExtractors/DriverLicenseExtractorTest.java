package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.ojbc.web.model.person.search.PersonSearchRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/static-configuration-demostate.xml"})
public class DriverLicenseExtractorTest {

	@Resource
    DriverLicenseExtractor unit;
	
	private PersonSearchRequest personSearchRequest;
	
	@Before
	public void setup() {
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyInput() {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
	}

	@Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("A1234","s2omestring","12334","A12334B","33-3445555","","WA234-34a"), personSearchRequest);
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
	}

    @Test
    public void InStateDLMatched() {
        unit.extractTerm(Arrays.asList("WA1234567"), personSearchRequest);
        assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("WA1234567"));
        assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("WA"));
        unit.extractTerm(Arrays.asList("wa1234567"), personSearchRequest);
        assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("wa1234567"));
    }
    
    @Test
    public void OutOfStateDLMatched() {
        unit.extractTerm(Arrays.asList("WA-CAME*SM11122"), personSearchRequest);
        assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("CAME*SM11122"));
        assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("WA"));
    }

    @Test
    public void DLNotMatched() {
        unit.extractTerm(Arrays.asList("X12345678"), personSearchRequest);
        assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
        assertThat(personSearchRequest.getPersonDriversLicenseIssuer(), nullValue());
    }

	@Test
	public void DLOnlyTakesFirstDLFound() {
		unit.extractTerm(Arrays.asList("WA1234567","D87654321"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("WA1234567"));
		assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("WA"));
	}

	@Test
	public void removesExtractedToken() {
		List<String> extractTerm = unit.extractTerm(Arrays.asList("WA1234567","noMatch"), personSearchRequest);
		
		assertThat(extractTerm.size(),is(1));
		assertThat(extractTerm.get(0),is("noMatch"));
	}
}
