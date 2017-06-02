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
package org.ojbc.web.impl;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.PersonSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class JuvenilePersonSearchMockImpl implements PersonSearchInterface {

	@Override
	public String invokePersonSearchRequest(PersonSearchRequest personSearchRequest, String federatedQueryID,
	        Element samlToken) throws Exception {
		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		System.out.println("PEOPLE SEARCH - Federated Query ID: " + federatedQueryID);
		System.out.println("------------------------------------------------------------------");
		System.out.println(personSearchRequest);
		System.out.println("------------------------------------------------------------------");
		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
		        "/sampleResponses/personSearch/Juvenile_EntityMergeResultMessage.xml"));
	}

}
