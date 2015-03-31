package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public class DOBExtractor extends SearchTermExtractorBase {

	private Pattern pattern;
	//i.e. 02/11/1999
	private static final String DOB_REGEX = "(\\d{2})/(\\d{2})/(\\d{4})";

	public DOBExtractor() {
		pattern = Pattern.compile(DOB_REGEX);
	}

	@Override
	protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
		Matcher matcher = pattern.matcher(token);
		if (matcher.matches()) {
			DateTime dob = new DateTime(Integer.valueOf(matcher.group(3)), Integer.valueOf(matcher.group(1)),
			        Integer.valueOf(matcher.group(2)), 0, 0, 0, 0);
			personSearchRequest.setPersonDateOfBirth(dob);
			return true;
		}
		return false;

	}

}
