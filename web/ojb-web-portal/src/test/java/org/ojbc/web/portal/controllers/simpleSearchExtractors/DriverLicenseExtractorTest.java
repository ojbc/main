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
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ojbc.web.model.person.search.PersonSearchRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class DriverLicenseExtractorTest {

	private static final Logger logger = Logger.getLogger(DriverLicenseExtractorTest.class);
	
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

	
	/*
	 * Note: was observed to fail sometimes on mac and fedora with:
	 * "expected 2, was 1". The code is intended to only extract "WA1234567",
	 * leaving "noMatch" remaining - but sometimes returns both of them. Maybe a
	 * non thread-safe collection is being modified concurrently. 
	 */
	@Test
	public void removesExtractedTokenTest() {
		
		List<String> termList = Arrays.asList("WA1234567","noMatch");
		
		List<String> extractedTermList = unit.extractTerm(termList, personSearchRequest);

		logger.info("\n\n\n Extracted Term List: \n\n\n" + extractedTermList);
		
		// failed sometimes when both strings are in List
		assertThat(extractedTermList.size(),is(1));
		
		assertThat(extractedTermList.get(0),is("noMatch"));
	}

	@Test
	public void testCustomPattern() {
		unit.setDefaultStateOfIssue("HI");
		unit.setDriversLicenseRegex("([a-zA-Z]{2})-(.+)|([Hh1][0-9]{8})");
		
		unit.extractTerm(Arrays.asList("X12345678", "H1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
		
		unit.extractTerm(Arrays.asList("H12345678"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("H12345678"));
		assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("HI"));
		
		unit.setDefaultStateOfIssue("WA");
		unit.setDriversLicenseRegex("([a-zA-Z]{2}-)(.+)|([Ww][Aa][0-9]{7})");
	}

}
