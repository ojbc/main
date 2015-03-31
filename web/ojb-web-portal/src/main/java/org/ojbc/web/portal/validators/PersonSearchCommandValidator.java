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

		if (StringUtils.isNotBlank(advanceSearch.getPersonSID()) && !advanceSearch.getPersonSID().matches("[a-zA-Z]\\d+")) {
			errors.rejectValue("advanceSearch.personSID", "sidFormat",
			        "SID must be a letter followed by digits , (i.e. A123456789)");
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
