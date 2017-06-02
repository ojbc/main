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

import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SIDExtractor extends SearchTermExtractorBase {

	//matches any alpha character follow by at least 1 digit, i.e. A1, V1312312312
    @Value("${sidRegex:([a-zA-Z]\\d+)}")
    String sidRegex;
    

	@Override
	protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
		if (token.matches(sidRegex)) {
			personSearchRequest.setPersonSID(token);
			return true;
		}
		return false;
	}

}
