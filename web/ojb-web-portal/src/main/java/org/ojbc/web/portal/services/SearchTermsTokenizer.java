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
package org.ojbc.web.portal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class SearchTermsTokenizer {
	//split tokens bases on white spaces, if terms are within quotes then it is one token
	String regex = "\"([^\"]*)\"|(\\S+)";
	private Pattern pattern;

	public SearchTermsTokenizer() {
		pattern = Pattern.compile(regex);
	}

	public List<String> tokenize(String input) {
		List<String> tokens = new ArrayList<String>();

		if (input == null) {
			return tokens;
		}

		Matcher m = pattern.matcher(StringEscapeUtils.unescapeHtml(input));
		while (m.find()) {
			if (m.group(1) != null) {
				tokens.add(m.group(1).trim());
			} 
			if(m.group(2) != null){
				tokens.add(m.group(2).trim());
			}
		}
		return tokens;
	}
}
