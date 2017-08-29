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
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.DOBExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.DriverLicenseExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.FBIIDExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.GivenAndSurNameExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.SIDExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.SSNExtractor;
import org.ojbc.web.portal.controllers.simpleSearchExtractors.SearchTermExtractorInterface;
import org.ojbc.web.portal.services.SearchTermsTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

@Resource
public class SimpleSearchParser {

	@Resource
	SearchTermsTokenizer searchTermsTokenizer;
	
	private List<SearchTermExtractorInterface> searchTermExtractors;
	
	@Autowired
	public SimpleSearchParser(SIDExtractor sidExtractor, 
			SSNExtractor ssnExtractor,
			DOBExtractor dobExtractor,
			FBIIDExtractor fbiIdExtractor,
			GivenAndSurNameExtractor givenAndSurNameExtractor) {
		searchTermExtractors = new ArrayList<SearchTermExtractorInterface>();
		searchTermExtractors.add(ssnExtractor);
		searchTermExtractors.add(sidExtractor);
		searchTermExtractors.add(dobExtractor);
		searchTermExtractors.add(fbiIdExtractor);
		searchTermExtractors.add(givenAndSurNameExtractor);  //this should run last
	}
	
	public void setDriverLicenseExtractor(DriverLicenseExtractor driverLicenseExtractor) {
	       searchTermExtractors.add(1, driverLicenseExtractor);
	}
	
	public void validateAndParseSimpleSearch(PersonSearchCommand personSearchCommand,
	        Errors errors) {
		String simpleSearch = personSearchCommand.getSimpleSearch();

		if (StringUtils.isBlank(simpleSearch)) {
			errors.rejectValue("simpleSearch", "emptySearch", "Search terms cannot be empty");
		}
		
		PersonSearchRequest personSearchRequest = new PersonSearchRequest();
		List<String> searchTokens = searchTermsTokenizer.tokenize(simpleSearch);
		
		for(SearchTermExtractorInterface extractor: searchTermExtractors){
			searchTokens = extractor.extractTerm(searchTokens, personSearchRequest);
		}

        personSearchRequest.setPurpose(personSearchCommand.getAdvanceSearch().getPurpose());
		personSearchRequest.setOnBehalfOf(personSearchCommand.getAdvanceSearch().getOnBehalfOf());
		if(personSearchCommand.getAdvanceSearch().getSourceSystems() != null){
			personSearchRequest.setSourceSystems(new ArrayList<String>(personSearchCommand.getAdvanceSearch().getSourceSystems()));
		}
		validateExtractedTerms(errors, personSearchRequest, searchTokens);

		personSearchCommand.setParsedPersonSearchRequest(personSearchRequest);
	}

	private void validateExtractedTerms(Errors errors, PersonSearchRequest personSearchRequest,
            List<String> searchTokens) {
	    if(searchTokens.size() > 0){
			errors.rejectValue("simpleSearch", "invalidTokens", "Unable to parse the following terms: " + StringEscapeUtils.escapeHtml(searchTokens.toString()));
		}
		
		if (StringUtils.isBlank(personSearchRequest.getPersonSurName()) //
		        && StringUtils.isBlank(personSearchRequest.getPersonSocialSecurityNumber()) //
		        && StringUtils.isBlank(personSearchRequest.getPersonSID()) //
		        && StringUtils.isBlank(personSearchRequest.getPersonDriversLicenseNumber()) //
		        && StringUtils.isBlank(personSearchRequest.getPersonFBINumber())) {
			errors.rejectValue("simpleSearch","missingRequiredInput",
			        "Search must have either a last name or an identifier (SSN, SID, DL, or FBI Number)");
		}
		
		if(personSearchRequest.getSourceSystems() == null || personSearchRequest.getSourceSystems().size() == 0){
			errors.rejectValue("simpleSearch", "missingSourceSystems",
                        "No Source Systems to search are selected.");
		}
    }

}
