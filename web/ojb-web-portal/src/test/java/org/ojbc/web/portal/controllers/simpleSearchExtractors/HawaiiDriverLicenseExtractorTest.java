package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.annotation.Resource;

import org.ojbc.web.model.person.search.PersonSearchRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/static-configuration-hawaii.xml"})
public class HawaiiDriverLicenseExtractorTest {

	@Resource
    DriverLicenseExtractor unit;
	
	private PersonSearchRequest personSearchRequest;
	
	@Before
	public void setup() {
		personSearchRequest = new PersonSearchRequest();
	}

    @Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("X12345678", "H1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
	}

	@Test
	public void DLMatched() {
		unit.extractTerm(Arrays.asList("H12345678"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("H12345678"));
		assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("HI"));
	}

}