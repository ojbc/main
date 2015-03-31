package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public class FBIIDExtractor extends SearchTermExtractorBase {
	
	// FBI ID patterns
	private static final String FBI_ID_PATTERN_ONE_TO_SEVEN_DIGITS = "^\\d{1,7}$";
	private static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_AH = "^\\d{1,6}[A-Ha-h]$";
	private static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_JZ_1OR2CHK = "^\\d{1,6}[J-Nj-nP-Zp-z]([1-9]|1[01])$";
	private static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_2ALPHA_1CHK = "^\\d{1,6}[AaC-Fc-fHhJ-Nj-nPRTprtV-Xv-x][A-Ea-e]\\d$";

	@Override
	protected boolean extractTermLocal(String token,
			PersonSearchRequest personSearchRequest) {
		
		if (token.matches(FBI_ID_PATTERN_ONE_TO_SEVEN_DIGITS) ||
				token.matches(FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_AH) ||
				token.matches(FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_JZ_1OR2CHK) ||
				token.matches(FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_2ALPHA_1CHK)) {
			
			personSearchRequest.setPersonFBINumber(token);
			return true;
		}
		
		return false;
	}

}
