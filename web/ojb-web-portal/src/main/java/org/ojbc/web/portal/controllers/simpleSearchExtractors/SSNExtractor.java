package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public class SSNExtractor extends SearchTermExtractorBase {

	//matches 3 digits 2 digits 4 digits separated by '-', i.e. 555-55-5555
	private static final String SSN_REGEX = "\\d{3}-\\d{2}-\\d{4}";

	@Override
	protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
		if (token.matches(SSN_REGEX)) {
			personSearchRequest.setPersonSocialSecurityNumber(token);
			return true;
		}
		return false;
	}

}
