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
package org.ojbc.web.portal.validators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.ojbc.web.portal.controllers.helpers.PersonSearchType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class PersonSearchCommandValidatorTest {
	
	@Resource
	PersonSearchCommandValidator unit;
	private PersonSearchCommand personSearchCommand;
	private BindingResult errors;
	private PersonSearchRequest advanceSearch;

	@Before
	public void setup() {
		personSearchCommand = new PersonSearchCommand();
		advanceSearch = new PersonSearchRequest();
		personSearchCommand.setAdvanceSearch(advanceSearch);
		personSearchCommand.setSearchType(PersonSearchType.ADVANCED);
		
		errors = mock(BindingResult.class);
	}

	@Test
	public void cannotSpecifyAgeAndDOBAtTheSameTime() {
		personSearchCommand.getAdvanceSearch().setPersonDateOfBirth(new DateTime());
		personSearchCommand.setAgeRangeStart(0);
		personSearchCommand.setAgeRangeEnd(12);
		
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors).rejectValue("ageRangeStart", "ageAndDOBAtSameTime","Age and DOB cannot be entered at the same time");
	}

	@Test
	public void cannotSpecifyAgeAndDOBAtTheSameTimeWhenAgeRangeAndDOBRange() {
		personSearchCommand.getAdvanceSearch().setPersonDateOfBirthRangeEnd(new DateTime());
		personSearchCommand.getAdvanceSearch().setPersonDateOfBirthRangeStart(new DateTime());
		personSearchCommand.setAgeRangeStart(0);
		personSearchCommand.setAgeRangeEnd(12);
		
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors).rejectValue("ageRangeStart", "ageAndDOBAtSameTime","Age and DOB cannot be entered at the same time");
	}

	@Test
	public void ssnMustConformToFormat() {
		personSearchCommand.getAdvanceSearch().setPersonSocialSecurityNumber("123456789");		
		unit.validate(personSearchCommand, errors);

		personSearchCommand.getAdvanceSearch().setPersonSocialSecurityNumber("ADFADFD");
		unit.validate(personSearchCommand, errors);

		personSearchCommand.getAdvanceSearch().setPersonSocialSecurityNumber("544-22-1122");
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors,times(2)).rejectValue("advanceSearch.personSocialSecurityNumber", "ssnFormat","SSN must be 9 digits seperated by dashes, (i.e. 999-99-9999)");
	}

	@Test
	public void stateIdentifierMustBeAlphaFollowBySomenumberOfDigitsPassingInputs() {
		
		personSearchCommand.getAdvanceSearch().setSourceSystems(Arrays.asList("sys1"));
		personSearchCommand.getAdvanceSearch().setPersonSID("a123213213");
		unit.validate(personSearchCommand, errors);
		
		personSearchCommand.getAdvanceSearch().setPersonSID("z24324234234");
		unit.validate(personSearchCommand, errors);
		
		personSearchCommand.getAdvanceSearch().setPersonSID("b1");
		unit.validate(personSearchCommand, errors);

		personSearchCommand.getAdvanceSearch().setPersonSID("G1");
		unit.validate(personSearchCommand, errors);
		

		verifyZeroInteractions(errors);
	}
	
	@Test
	public void allSearchMustHaveALastNameOrAnIdentifier() {
		unit.validate(personSearchCommand, errors);
		
		advanceSearch.setPersonSurName("   ");
		unit.validate(personSearchCommand, errors);
		
		advanceSearch.setPersonDriversLicenseNumber("  ");
		unit.validate(personSearchCommand, errors);

		advanceSearch.setPersonFBINumber("  ");
		unit.validate(personSearchCommand, errors);

		advanceSearch.setPersonSocialSecurityNumber("  ");
		unit.validate(personSearchCommand, errors);

		
		Mockito.verify(errors,times(5)).reject("missingRequiredInput", "Search must have either a last name or an identifier (SSN, SID, DL, or FBI Number)");
	}

	@Test
	public void allSearchMustHaveALastNameOrAnIdentifierPassingInputs() {
		personSearchCommand.getAdvanceSearch().setSourceSystems(Arrays.asList("sys1"));
		advanceSearch.setPersonSurName("some sur name");
		
		unit.validate(personSearchCommand, errors);
		
		verifyZeroInteractions(errors);
	}
	
	@Test
	public void endDOBMustbeAfterStartDOB(){
		DateTime firstTime = new DateTime();
		advanceSearch.setPersonDateOfBirthRangeEnd(firstTime);
		advanceSearch.setPersonDateOfBirthRangeStart(firstTime.plusYears(1));
		
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors).rejectValue("advanceSearch.personDateOfBirthRangeStart","endDobBeforeStart", "To DOB range must be after From");
	}

	@Test
	public void endAgeMustBeAfterStartAge(){
		personSearchCommand.setAgeRangeStart(12);
		personSearchCommand.setAgeRangeEnd(11);
		
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors).rejectValue("ageRangeStart","endAgeBeforeStart", "End Age must be after Begin Age");
	}

	@Test
	public void sourceSystemsMustBeSelected(){
		
		unit.validate(personSearchCommand, errors);
		
		Mockito.verify(errors).reject("missingRequiredInput","No Source Systems to search are selected.");
	}
	
	@Test
	@Ignore("Waiting for specification")
	public void driverLicenseConformsToFormat(){
		
	}

	@Test
	@Ignore("Waiting for specification")
	public void FBINumberConformsToFormat(){
		
	}
}
