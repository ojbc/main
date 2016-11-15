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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public abstract class SearchTermExtractorBase implements SearchTermExtractorInterface {

	private Logger logger = Logger.getLogger(SearchTermExtractorBase.class);
	
	public List<String> extractTerm(List<String> pSearchTokensList, PersonSearchRequest pPersonSearchRequest){
		
		List<String> rRemainingTokenList = new ArrayList<String>(pSearchTokensList);
		
		for (String iSearchToken : pSearchTokensList) {
			
			// bug: driver license scenario not removing here sometimes
			if(extractTermLocal(iSearchToken, pPersonSearchRequest)){
				
				rRemainingTokenList.remove(iSearchToken);
				
				logger.info("\n\n\n ****  Removed Token, list now: " 
						+ rRemainingTokenList + "****\n\n\n");
				
				break;
			}
		}
		return rRemainingTokenList;
		
	}

	protected abstract boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest);
	
}
