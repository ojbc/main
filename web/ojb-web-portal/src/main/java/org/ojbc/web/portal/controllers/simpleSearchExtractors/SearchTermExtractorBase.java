package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.web.model.person.search.PersonSearchRequest;

public abstract class SearchTermExtractorBase implements SearchTermExtractorInterface {

	public List<String> extractTerm(List<String> searchTokens, PersonSearchRequest personSearchRequest){
		List<String> remainingTokens = new ArrayList<String>(searchTokens);
		
		for (String token : searchTokens) {
			if(extractTermLocal(token, personSearchRequest)){
				remainingTokens.remove(token);
				break;
			}
		}
		return remainingTokens;
		
	}

	protected abstract boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest);
	
}
