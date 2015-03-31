package org.ojbc.web.portal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		Matcher m = pattern.matcher(input);
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
