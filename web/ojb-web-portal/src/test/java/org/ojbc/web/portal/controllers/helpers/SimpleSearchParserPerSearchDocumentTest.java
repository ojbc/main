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
package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.web.portal.OjbcWebPortalApplication;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

// Tests based on Simple Search Requirements Doc
@RunWith(SpringRunner.class)
@SpringBootTest(args = {"--spring.config.additional-location=classpath:/"}, 
	classes = OjbcWebPortalApplication.class)
@ContextConfiguration({"classpath:beans/static-configuration-demostate.xml"})
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
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Chow"));
	}

	@Test
	public void searchForFirstNameAndLastName() {
		personSearchCommand.setSimpleSearch("Norm Chow*");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonGivenName(), is("Norm"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Chow"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurNameMetaData().toString(), is("StartsWith"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameTwo() {
		personSearchCommand.setSimpleSearch("Van Halen");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonGivenName(), is("Van"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Halen"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameWithQuotes() {
		personSearchCommand.setSimpleSearch("Eddie \"Van Halen\"");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonGivenName(), is("Eddie"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Van Halen"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameWithNoQuotes() {
		personSearchCommand.setSimpleSearch("Eddie Van Halen");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);
		
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonGivenName(), is("Eddie"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Van Halen"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForFirstNameAndLastNameAndDOB() {
		personSearchCommand.setSimpleSearch("Norm Chow 05/03/1946 ");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonGivenName(), is("Norm"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Chow"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonDateOfBirth().getMonthOfYear(), is(5));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonDateOfBirth().getDayOfMonth(), is(3));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonDateOfBirth().getYear(), is(1946));
		verifyNoInteractions(errors);
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
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSocialSecurityNumber(), is("123-45-6789"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForSID() {
		personSearchCommand.setSimpleSearch("A123456");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSID(), is("A123456"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForSIDAndSSN() {
		personSearchCommand.setSimpleSearch("A123456 123-45-6788");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSocialSecurityNumber(), is("123-45-6788"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSID(), is("A123456"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchForLastNameAndSSN() {
		personSearchCommand.setSimpleSearch("Chow 123-45-6789");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSurName(), is("Chow"));
		assertThat(personSearchCommand.getParsedPersonSearchRequest().getPersonSocialSecurityNumber(), is("123-45-6789"));
		verifyNoInteractions(errors);
	}

	@Test
	public void searchInvalidTerm() {
		personSearchCommand.setSimpleSearch("123-45");
		unit.validateAndParseSimpleSearch(personSearchCommand, errors);

		verify(errors).rejectValue("simpleSearch", "invalidTokens", "Unable to parse the following terms: [123-45]");
	}

}
