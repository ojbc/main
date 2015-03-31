package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public class SIDExtractor extends SearchTermExtractorBase {

	//matches any alpha character follow by at least 1 digit, i.e. A1, V1312312312
	private static final String SID_REGEX = "[a-zA-Z]\\d+";

	@Override
	protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
		if (token.matches(SID_REGEX)) {
			personSearchRequest.setPersonSID(token);
			return true;
		}
		return false;
	}

}
