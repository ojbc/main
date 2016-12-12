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
package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;

import javax.annotation.Resource;

import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;

// Tests based on Simple Search Requirements Doc
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
//        "classpath:/META-INF/spring/demostate/routes-demostate.xml",
//        "classpath:/META-INF/spring/spring-beans-ojb-web-application-connector-context.xml" 
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class SimpleSearchParserPerSearchDocumentTest {

    @Resource
	SimpleSearchParser unit;
	private BindingResult errors;
	private PersonSearchCommand personSearchCommand;

	@Before
	public void setup() {
		errors = mock(BindingResult.class);
		personSearchCommand = new PersonSearchCommand();
		personSearchCommand.getAdvanceSearch().setSourceSystems(Arrays.asList("sys1"));
	}

	@Test
	public void searchForLastName() {
		personSearchCommand.setSimpleSearch("Chow");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Chow"));
	}

	@Test
	public void searchForFirstNameAndLastName() {
		personSearchCommand.setSimpleSearch("Norm Chow");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonGivenName(), is("Norm"));
		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Chow"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameTwo() {
		personSearchCommand.setSimpleSearch("Van Halen");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonGivenName(), is("Van"));
		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Halen"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameWithQuotes() {
		personSearchCommand.setSimpleSearch("Eddie \"Van Halen\"");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonGivenName(), is("Eddie"));
		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Van Halen"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameWithNoQuotes() {
		personSearchCommand.setSimpleSearch("Eddie Van Halen");
		PersonSearchRequest expectedPersonSearchRequest = unit
				.validateAndParseSimpleSearch(personSearchCommand, errors);
		
		assertThat(expectedPersonSearchRequest.getPersonGivenName(), is("Eddie"));
		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Van Halen"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameAndDOB() {
		personSearchCommand.setSimpleSearch("Norm Chow 05/03/1946 ");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonGivenName(), is("Norm"));
		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Chow"));
		assertThat(expectedPersonSearchRequest.getPersonDateOfBirth().getMonthOfYear(), is(5));
		assertThat(expectedPersonSearchRequest.getPersonDateOfBirth().getDayOfMonth(), is(3));
		assertThat(expectedPersonSearchRequest.getPersonDateOfBirth().getYear(), is(1946));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForDOBOnlyResultsInError() {
		personSearchCommand.setSimpleSearch("05/03/1946");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		verify(errors).rejectValue("simpleSearch", "missingRequiredInput",
		        "Search must have either a last name or an identifier (SSN, SID, DL, or FBI Number)");
	}

	@Test
	public void searchForSSN() {
		personSearchCommand.setSimpleSearch("123-45-6789");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonSocialSecurityNumber(), is("123-45-6789"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForSID() {
		personSearchCommand.setSimpleSearch("A123456");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonSID(), is("A123456"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForSIDAndSSN() {
		personSearchCommand.setSimpleSearch("A123456 123-45-6788");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonSocialSecurityNumber(), is("123-45-6788"));
		assertThat(expectedPersonSearchRequest.getPersonSID(), is("A123456"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchForLastNameAndSSN() {
		personSearchCommand.setSimpleSearch("Chow 123-45-6789");
		PersonSearchRequest expectedPersonSearchRequest = unit
		        .validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(expectedPersonSearchRequest.getPersonSurName(), is("Chow"));
		assertThat(expectedPersonSearchRequest.getPersonSocialSecurityNumber(), is("123-45-6789"));
		verifyZeroInteractions(errors);
	}

	@Test
	public void searchInvalidTerm() {
		personSearchCommand.setSimpleSearch("123-45");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		verify(errors).rejectValue("simpleSearch", "invalidTokens", "Unable to parse the following terms: [123-45]");
	}

}
