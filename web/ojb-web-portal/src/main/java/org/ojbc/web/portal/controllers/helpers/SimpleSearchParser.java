package org.ojbc.web.portal.controllers.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;

public class SimpleSearchParser {

	@Resource
	SearchTermsTokenizer searchTermsTokenizer;
	
	private List<SearchTermExtractorInterface> searchTermExtractors;
	
	public SimpleSearchParser() {
		searchTermExtractors = new ArrayList<SearchTermExtractorInterface>();
		searchTermExtractors.add(new SSNExtractor());
		searchTermExtractors.add(new SIDExtractor());
		searchTermExtractors.add(new DOBExtractor());
		searchTermExtractors.add(new FBIIDExtractor());
		searchTermExtractors.add(new GivenAndSurNameExtractor());  //this should run last
	}
	
	public void setDriverLicenseExtractor(DriverLicenseExtractor driverLicenseExtractor) {
	       searchTermExtractors.add(1, driverLicenseExtractor);
	}
	
	public PersonSearchRequest validateAndParseSimpleSearch(PersonSearchCommand personSearchCommand,
	        BindingResult errors) {
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
		
		return personSearchRequest;
	}

	private void validateExtractedTerms(BindingResult errors, PersonSearchRequest personSearchRequest,
            List<String> searchTokens) {
	    if(searchTokens.size() > 0){
			errors.rejectValue("simpleSearch", "invalidTokens", "Unable to parse the following terms: " + searchTokens);
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
