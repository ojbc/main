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

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.springframework.stereotype.Service;

@Service
public class PersonSearchCommandUtils {

	private static final int DEFAULT_START_RANGE_IN_YEARS = 1000;

	
	// by the time we get here validation and range cannot be negative and
	// weight is not missing
	public PersonSearchRequest getPersonSearchRequest(PersonSearchCommand personSearchCommand){
		PersonSearchRequest personSearchRequest = clonePersonSearchRequest(personSearchCommand.getAdvanceSearch());
		
		setHeightInTotalInches(personSearchCommand,personSearchRequest);
		populateDefaultValuesForDOB(personSearchCommand,personSearchRequest);
		populateDOBWithAgeRange(personSearchCommand,personSearchRequest);
		populateHeightRange(personSearchCommand,personSearchRequest);
		populateWeightRange(personSearchCommand,personSearchRequest);
		
		return personSearchRequest;
	}
	
	 void populateWeightRange(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
		Integer tolerance = personSearchCommand.getWeightTolerance();
		PersonSearchRequest advanceSearch = personSearchCommand.getAdvanceSearch();
		if (tolerance == null || tolerance == 0) {
			return;
		}
		int startRange = advanceSearch.getPersonWeight() - tolerance;
		personSearchRequest.setPersonWeightRangeStart(startRange < 0 ? 0 : startRange);
		personSearchRequest.setPersonWeightRangeEnd(advanceSearch.getPersonWeight() + tolerance);
	}

	 void populateHeightRange(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
		Integer tolerance = personSearchCommand.getHeightTolerance();
		if (tolerance == null || tolerance == 0) {
			return;
		}
		int startRange = personSearchRequest.getPersonHeightTotalInches() - tolerance;
		personSearchRequest.setPersonHeightTotalInchesRangeStart(startRange < 0 ? 0 : startRange);
		personSearchRequest.setPersonHeightTotalInchesRangeEnd(personSearchRequest.getPersonHeightTotalInches() + tolerance);

	}

	 void setHeightInTotalInches(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
		Integer heightInFeet = personSearchCommand.getHeightInFeet() == null
		        || personSearchCommand.getHeightInFeet() < 0 ? 0 : personSearchCommand.getHeightInFeet();
		Integer heightInInches = personSearchCommand.getHeightInInches() == null
		        || personSearchCommand.getHeightInInches() < 0 ? 0 : personSearchCommand.getHeightInInches();
		Integer totalHeight = heightInFeet * 12 + heightInInches;

		if (totalHeight == 0) {
			return;
		}

		personSearchRequest.setPersonHeightTotalInches(totalHeight);
	}

	 void populateDefaultValuesForDOB(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest){
		DateTime startDate = personSearchCommand.getAdvanceSearch().getPersonDateOfBirthRangeStart();
		DateTime endDate = personSearchCommand.getAdvanceSearch().getPersonDateOfBirthRangeEnd();
		
		if(startDate == null && endDate == null) {
			return;
		}
		
		if(startDate == null){
			personSearchRequest.setPersonDateOfBirthRangeStart(endDate.minusYears(DEFAULT_START_RANGE_IN_YEARS));
		}
		
		if(endDate == null){
			personSearchRequest.setPersonDateOfBirthRangeEnd(getCurrentTime());
		}
	}
	
	 void populateDOBWithAgeRange(PersonSearchCommand personSearchCommand, PersonSearchRequest personSearchRequest) {
		Integer ageRangeStart = personSearchCommand.getAgeRangeStart();
		Integer ageRangeEnd = personSearchCommand.getAgeRangeEnd();
		if (ageRangeStart == null && ageRangeEnd == null) {
			return;
		}
		
		if(ageRangeStart == null){
			ageRangeStart = 0;
		}
		
		if(ageRangeEnd == null){
			ageRangeEnd = DEFAULT_START_RANGE_IN_YEARS;
		}

		DateTime currentDateTime = getCurrentTime();
		DateTime dobStart = currentDateTime.minusYears(ageRangeEnd);
		DateTime dobEnd = currentDateTime.minusYears(ageRangeStart);
		
		personSearchRequest.setPersonDateOfBirthRangeStart(dobStart);
		personSearchRequest.setPersonDateOfBirthRangeEnd(dobEnd);

	}

	DateTime getCurrentTime() {
		return new DateTime();
	}

	
	PersonSearchRequest clonePersonSearchRequest(PersonSearchRequest personSearchRequest){
		try {
	        PersonSearchRequest cloneBean = (PersonSearchRequest) BeanUtils.cloneBean(personSearchRequest);
	        
	        cloneBean.setSourceSystems(new ArrayList<String>(personSearchRequest.getSourceSystems()));
			return cloneBean;
        } catch (Exception ex){
        	throw new RuntimeException("Unable to clone PersonSearchRequest", ex);
        }
	}
}
