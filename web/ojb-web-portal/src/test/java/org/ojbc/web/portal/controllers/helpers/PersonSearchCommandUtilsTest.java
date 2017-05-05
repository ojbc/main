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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;

public class PersonSearchCommandUtilsTest {

	private PersonSearchCommandUtils unit;
	private PersonSearchCommand personSearchCommand;
	private PersonSearchRequest personSearchRequest;
	private DateTime currentDateTime;
	private PersonSearchRequest advanceSearch;

	@Before
	public void setup() {
		currentDateTime = new DateTime(2012, 7, 27, 12, 30, 23, 0);

		unit = new PersonSearchCommandUtils() {

			@Override
			public DateTime getCurrentTime() {
				return currentDateTime;
			}
		};
		personSearchCommand = new PersonSearchCommand();

		advanceSearch = new PersonSearchRequest();
		personSearchCommand.setAdvanceSearch(advanceSearch);
		personSearchRequest = new PersonSearchRequest();

	}

	@Test
	public void populateWeightRangeNotSetWhenToleranceIsNull() {
		unit.populateWeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonWeightRangeStart(), nullValue());
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd(), nullValue());
	}

	@Test
	public void populateWeightRangeNotSetWhenToleranceIsZero() {
		personSearchCommand.setWeightTolerance(0);

		unit.populateWeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonWeightRangeStart(), nullValue());
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd(), nullValue());

	}

	@Test
	public void populateWeightRangeSetWhenToleranceIsSet() {
		advanceSearch.setPersonWeight(200);
		personSearchCommand.setWeightTolerance(20);

		unit.populateWeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonWeightRangeStart(), is(180));
		assertThat(personSearchRequest.getPersonWeightRangeEnd(), is(220));

	}

	@Test
	public void populateWeightRangeSetStartDateToZeroIfRangeIsBiggerThanWeight() {
		advanceSearch.setPersonWeight(100);
		personSearchCommand.setWeightTolerance(200);

		unit.populateWeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonWeightRangeStart(), is(0));
	}

	@Test
	public void heightInTotalInchesConverted() {
		unit.setHeightInTotalInches(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInches(), nullValue());
	}

	@Test
	public void heightInTotalInchesConvertedWithOnlyFeetPopulated() {
		personSearchCommand.setHeightInFeet(5);

		unit.setHeightInTotalInches(personSearchCommand, personSearchRequest);
		assertThat(personSearchRequest.getPersonHeightTotalInches(), is(60));
	}

	@Test
	public void heightInTotalInchesConvertedWithOnlyInchesPopulated() {
		personSearchCommand.setHeightInInches(29);

		unit.setHeightInTotalInches(personSearchCommand, personSearchRequest);
		assertThat(personSearchRequest.getPersonHeightTotalInches(), is(29));
	}

	@Test
	public void heightInTotalInchesConvertedWithBothFeetAndInchesPopulated() {
		personSearchCommand.setHeightInFeet(5);
		personSearchCommand.setHeightInInches(29);

		unit.setHeightInTotalInches(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInches(), is(89));
	}

	@Test
	public void populateHeightRangeNotSetWhenToleranceIsNull() {
		unit.populateHeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeStart(), nullValue());
		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeEnd(), nullValue());
	}

	@Test
	public void populateHeightRangeNotSetWhenToleranceIsZero() {
		personSearchCommand.setHeightTolerance(0);

		unit.populateHeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeStart(), nullValue());
		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeEnd(), nullValue());

	}

	@Test
	public void populateHeightRangeSetWhenToleranceIsSet() {
		personSearchRequest.setPersonHeightTotalInches(150);
		personSearchCommand.setHeightTolerance(35);

		unit.populateHeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeStart(), is(115));
		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeEnd(), is(185));

	}

	@Test
	public void populateHeightRangeRangeSetStartDateToZeroIfRangeIsBiggerThanHeight() {
		personSearchRequest.setPersonHeightTotalInches(150);
		personSearchCommand.setHeightTolerance(200);

		unit.populateHeightRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonHeightTotalInchesRangeStart(), is(0));
	}

	@Test
	public void populateDOBWithAgeRangeNullValue() {
		unit.populateDOBWithAgeRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart(), nullValue());
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd(), nullValue());
	}

	@Test
	public void populateDOBWithAgeRangeSet() {
		currentDateTime = new DateTime(2020, 7, 27, 12, 30, 23, 0);

		personSearchCommand.setAgeRangeStart(10);
		personSearchCommand.setAgeRangeEnd(20);

		unit.populateDOBWithAgeRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart().getYear(), is(2000));
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd().getYear(), is(2010));
	}

	@Test
	public void populateDOBWithAgeRangeSetWithMoreRealisticValues() {
		personSearchCommand.setAgeRangeStart(60);
		personSearchCommand.setAgeRangeEnd(70);

		unit.populateDOBWithAgeRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart().getYear(), is(1942));
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd().getYear(), is(1952));
	}

	@Test
	public void populateDOBWithAgeRangeSetEndDateTo1000YearsInPastWhenMissing() {
		personSearchCommand.setAgeRangeStart(60);

		unit.populateDOBWithAgeRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart().getYear(), is(1012));
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd().getYear(), is(1952));
	}

	@Test
	public void populateDOBWithAgeRangeSetStartDateToCurrentWhenMissing() {
		personSearchCommand.setAgeRangeEnd(70);

		unit.populateDOBWithAgeRange(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart().getYear(), is(1942));
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd().getYear(), is(2012));
	}

	@Test
	public void populateDefaultValuesForDOBWithStartDateTo1000YearsInPastWhenMissing() {
		DateTime endDate = new DateTime(2007, 1, 1, 1, 1, 1, 1);
		advanceSearch.setPersonDateOfBirthRangeEnd(endDate);

		unit.populateDefaultValuesForDOB(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart().getYear(), is(1007));
	}

	@Test
	public void populateDefaultValuesForDOBWithEndDateCurrentTimeWhenMissing() {
		DateTime startDate = new DateTime(1999, 1, 1, 1, 1, 1, 1);
		advanceSearch.setPersonDateOfBirthRangeStart(startDate);

		unit.populateDefaultValuesForDOB(personSearchCommand, personSearchRequest);

		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd().getYear(), is(currentDateTime.getYear()));
	}

	@Test
	public void populateDefaultValuesForDOBDoNothingWhenStartAndEndIsMissing() {
		
		unit.populateDefaultValuesForDOB(personSearchCommand, personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDateOfBirthRangeEnd(), nullValue());
		assertThat(personSearchRequest.getPersonDateOfBirthRangeStart(), nullValue());
	}

	@Test
	public void testDeepclone() {
		DateTime personDateOfBirth = new DateTime();
		DateTime personDateOfBirthRangeStart = new DateTime();
		DateTime personDateOfBirthRangeEnd = new DateTime();
		List<String> sourceSystems = Arrays.asList("src1", "src2");

		PersonSearchRequest request = new PersonSearchRequest();
		request.setPersonHairColor("personHairColor");
		request.setPersonEyeColor("personEyeColor");
		request.setPersonGivenName("personGivenName");
		request.setPersonMiddleName("personMiddleName");
		request.setPersonSurName("personSurName");
		request.setPersonSexCode("personSexCode");
		request.setPersonRaceCode("personRaceCode");
		request.setPersonSocialSecurityNumber("personSocialSecurityNumber");
		request.setPersonDriversLicenseNumber("personDriversLicenseNumber");
		request.setPersonFBINumber("personFBINumber");
		request.setPersonSID("personSID");
		request.setPersonGivenNameMetaData(SearchFieldMetadata.ExactMatch);
		request.setPersonSurNameMetaData(SearchFieldMetadata.StartsWith);
		request.setPersonDriversLicenseIssuer("personDriversLicenseIssuer");
		request.setPersonDateOfBirth(personDateOfBirth);
		request.setPersonDateOfBirthRangeStart(personDateOfBirthRangeStart);
		request.setPersonDateOfBirthRangeEnd(personDateOfBirthRangeEnd);
		request.setSourceSystems(sourceSystems);
		request.setPersonWeight(11);
		request.setPersonWeightRangeStart(12);
		request.setPersonWeightRangeEnd(13);
		request.setPersonHeightTotalInches(14);
		request.setPersonHeightTotalInchesRangeStart(15);
		request.setPersonHeightTotalInchesRangeEnd(16);

		PersonSearchRequest clone = unit.clonePersonSearchRequest(request);
		
		assertThat(clone.getPersonHairColor(),is("personHairColor"));
		assertThat(clone.getPersonEyeColor(),is("personEyeColor"));
		assertThat(clone.getPersonGivenName(),is("personGivenName"));
		assertThat(clone.getPersonMiddleName(),is("personMiddleName"));
		assertThat(clone.getPersonSurName(),is("personSurName"));
		assertThat(clone.getPersonSexCode(),is("personSexCode"));
		assertThat(clone.getPersonRaceCode(),is("personRaceCode"));
		assertThat(clone.getPersonSocialSecurityNumber(),is("personSocialSecurityNumber"));
		assertThat(clone.getPersonDriversLicenseNumber(),is("personDriversLicenseNumber"));
		assertThat(clone.getPersonFBINumber(),is("personFBINumber"));
		assertThat(clone.getPersonSID(),is("PERSONSID"));
		assertThat(clone.getPersonGivenNameMetaData(),is(SearchFieldMetadata.ExactMatch));
		assertThat(clone.getPersonSurNameMetaData(),is(SearchFieldMetadata.StartsWith));
		assertThat(clone.getPersonDriversLicenseIssuer(),is("personDriversLicenseIssuer"));
		assertThat(clone.getPersonDateOfBirth(),is(personDateOfBirth));
		assertThat(clone.getPersonDateOfBirthRangeStart(),is(personDateOfBirthRangeStart));
		assertThat(clone.getPersonDateOfBirthRangeEnd(),is(personDateOfBirthRangeEnd));
		assertThat(clone.getSourceSystems(),is(sourceSystems));
		assertThat(clone.getPersonWeight(),is(11));
		assertThat(clone.getPersonWeightRangeStart(),is(12));
		assertThat(clone.getPersonWeightRangeEnd(),is(13));
		assertThat(clone.getPersonHeightTotalInches(),is(14));
		assertThat(clone.getPersonHeightTotalInchesRangeStart(),is(15));
		assertThat(clone.getPersonHeightTotalInchesRangeEnd(),is(16));
		
		assertNotSame(request.getSourceSystems(), clone.getSourceSystems());

	}

	@Test(expected=RuntimeException.class)
	public void cloneFailRethrowAsRuntimeException(){
		unit.clonePersonSearchRequest(null);
	}
	
	@Test
	public void getCurrentTime(){
		unit = new PersonSearchCommandUtils();
		assertNotNull(unit.getCurrentTime());
	}
	
	@Test 
	public void getPersonSearchRequestDeligatesWorkToAllMethods(){
		
		final List<String> calledMethods = new ArrayList<String>();
		
		unit = new PersonSearchCommandUtils(){
			@Override
			PersonSearchRequest clonePersonSearchRequest(PersonSearchRequest personSearchRequest) {
				calledMethods.add("clonePersonSearchRequest");
			    return null;
			}
			@Override
			void setHeightInTotalInches(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
				calledMethods.add("setHeightInTotalInches");
			}
			@Override
			void populateDefaultValuesForDOB(PersonSearchCommand personSearchCommand,
			        PersonSearchRequest personSearchRequest) {
				calledMethods.add("populateDefaultValuesForDOB");
			}
			@Override
			void populateDOBWithAgeRange(PersonSearchCommand personSearchCommand,
			        PersonSearchRequest personSearchRequest) {
				calledMethods.add("populateDOBWithAgeRange");
			}
			@Override
			void populateHeightRange(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
				calledMethods.add("populateHeightRange");
			}
			@Override
			void populateWeightRange(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
				calledMethods.add("populateWeightRange");
			}
		};
		
		unit.getPersonSearchRequest(personSearchCommand);
		assertThat(calledMethods.get(0),is("clonePersonSearchRequest"));
		assertThat(calledMethods.get(1),is("setHeightInTotalInches"));
		assertThat(calledMethods.get(2),is("populateDefaultValuesForDOB"));
		assertThat(calledMethods.get(3),is("populateDOBWithAgeRange"));
		assertThat(calledMethods.get(4),is("populateHeightRange"));
		assertThat(calledMethods.get(5),is("populateWeightRange"));
		
	}
}
