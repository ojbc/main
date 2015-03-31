package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.person.search.PersonSearchRequest;

public class GivenAndSurNameExtractor extends SearchTermExtractorBase {

	//matches any words that doesn't contain any numerics
	private static final String NAME_REGEX = "[^0-9]+";

	@Override
	public List<String> extractTerm(List<String> searchTokens, PersonSearchRequest personSearchRequest) {
		List<String> remainingTokens = new ArrayList<String>(searchTokens);
		if (searchTokens.size() < 1) {
			return remainingTokens;
		}

		List<String> protentialTokens = new ArrayList<String>();
		for (String token : searchTokens) {
			if (token.matches(NAME_REGEX)) {
				protentialTokens.add(token);
				remainingTokens.remove(token);
			}
		}
		
		if(protentialTokens.size() == 1){
			setSurName(personSearchRequest, protentialTokens.get(0));
			
		}
		if(protentialTokens.size() >=2){
			
			setGivenName(personSearchRequest, protentialTokens.get(0));	
			StringBuffer sb = new StringBuffer();
			for(int i = 1;i < protentialTokens.size();i++){
				sb.append(protentialTokens.get(i)).append(" ");
			}
			setSurName(personSearchRequest, sb.toString().trim());			
		}

		
		
		return remainingTokens;

	}

	private void setGivenName(PersonSearchRequest personSearchRequest, String name) {
	    if(name.endsWith("*")){
			personSearchRequest.setPersonGivenName(name.substring(0,name.length()-1));
			personSearchRequest.setPersonGivenNameMetaData(SearchFieldMetadata.StartsWith);
		}else{
			personSearchRequest.setPersonGivenName(name);
			personSearchRequest.setPersonGivenNameMetaData(SearchFieldMetadata.ExactMatch);
		}
    }
	
	private void setSurName(PersonSearchRequest personSearchRequest, String name) {
		if(name.endsWith("*")){
			personSearchRequest.setPersonSurName(name.substring(0,name.length()-1));
			personSearchRequest.setPersonSurNameMetaData(SearchFieldMetadata.StartsWith);
		}else{
			personSearchRequest.setPersonSurName(name);
			personSearchRequest.setPersonSurNameMetaData(SearchFieldMetadata.ExactMatch);
		}
	}

	@Override
	protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
		throw new UnsupportedOperationException("GivenAndSurNameExtractor does not use this method");
	}


}
