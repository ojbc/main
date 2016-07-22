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
package org.ojbc.web.portal.validators;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class PersonSearchCommandValidator {
	
	public void validate(PersonSearchCommand personSearchCommand, BindingResult errors) {
		if (hasAgeRange(personSearchCommand) && hasDOB(personSearchCommand)) {
			errors.rejectValue("ageRangeStart", "ageAndDOBAtSameTime", "Age and DOB cannot be entered at the same time");
		}

		PersonSearchRequest advanceSearch = personSearchCommand.getAdvanceSearch();
		if (StringUtils.isNotBlank(advanceSearch.getPersonSocialSecurityNumber())
		        && !advanceSearch.getPersonSocialSecurityNumber().matches("\\d{3}-\\d{2}-\\d{4}")) {
			errors.rejectValue("advanceSearch.personSocialSecurityNumber", "ssnFormat",
			        "SSN must be 9 digits seperated by dashes, (i.e. 999-99-9999)");
		}

		if(hasDOB(personSearchCommand) ){
			DateTime startDob = advanceSearch.getPersonDateOfBirthRangeStart();
			DateTime endDob = advanceSearch.getPersonDateOfBirthRangeEnd();
			if(startDob!= null  && endDob!=null && startDob.isAfter(endDob)){
				errors.rejectValue("advanceSearch.personDateOfBirthRangeStart","endDobBeforeStart", "To DOB range must be after From");
			}
			
		}

		if(hasAgeRange(personSearchCommand) ){
			Integer startAge = personSearchCommand.getAgeRangeStart();
			Integer endAge = personSearchCommand.getAgeRangeEnd();
			if(startAge!= null  && endAge!=null && startAge > endAge ){
				errors.rejectValue("ageRangeStart","endAgeBeforeStart", "End Age must be after Begin Age");
			}
			
		}
		if (StringUtils.isBlank(advanceSearch.getPersonSurName()) //
		        && StringUtils.isBlank(advanceSearch.getPersonSocialSecurityNumber()) //
		        && StringUtils.isBlank(advanceSearch.getPersonSID()) //
		        && StringUtils.isBlank(advanceSearch.getPersonDriversLicenseNumber()) //
		        && StringUtils.isBlank(advanceSearch.getPersonFBINumber())) {
			errors.reject("missingRequiredInput",
			        "Search must have either a last name or an identifier (SSN, SID, DL, or FBI Number)");
		}
		
		if(advanceSearch.getSourceSystems() == null || advanceSearch.getSourceSystems().size() == 0){
			errors.reject("missingRequiredInput","No Source Systems to search are selected.");
		}
	}

	private boolean hasDOB(PersonSearchCommand personSearchCommand) {
		PersonSearchRequest advanceSearch = personSearchCommand.getAdvanceSearch();
		return advanceSearch.getPersonDateOfBirth() != null || advanceSearch.getPersonDateOfBirthRangeEnd() != null
		        || advanceSearch.getPersonDateOfBirthRangeStart() != null;
	}

	private boolean hasAgeRange(PersonSearchCommand personSearchCommand) {
		return personSearchCommand.getAgeRangeStart() != null || personSearchCommand.getAgeRangeEnd() != null;
	}
}
