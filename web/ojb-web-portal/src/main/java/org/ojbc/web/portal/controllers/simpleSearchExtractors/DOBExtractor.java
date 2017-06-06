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
package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.springframework.stereotype.Component;

@Component
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
