package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.List;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public interface SearchTermExtractorInterface {

	List<String> extractTerm(List<String> searchTokens, PersonSearchRequest personSearchRequest);
}
