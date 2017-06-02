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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class SearchTermsTokenizerTest {

	SearchTermsTokenizer unit;
	
	@Before
	public void setup(){
		unit = new SearchTermsTokenizer();
	}
	
	@Test
	public void forNull() {
		List<String> tokenize = unit.tokenize(null);
		assertThat(tokenize.size(), is(0));
	}

	@Test
	public void forEmpty() {
		List<String> tokenize = unit.tokenize("");
		assertThat(tokenize.size(), is(0));
	}

	@Test
	public void stringOfSpaces() {
		List<String> tokenize = unit.tokenize("   ");
		assertThat(tokenize.size(), is(0));
	}

	@Test
	public void singleToken() {
		List<String> tokenize = unit.tokenize("cat");
		
		assertThat(tokenize.toString(),is("[cat]"));
	}

	@Test
	public void trimSpaces() {
		List<String> tokenize = unit.tokenize("   cat   ");
		
		assertThat(tokenize.toString(),is("[cat]"));
	}

	@Test
	public void twoTokens() {
		List<String> tokenize = unit.tokenize("   cat   dog ");
		
		assertThat(tokenize.toString(),is("[cat, dog]"));
	}

	@Test
	public void tokensWithQuotes() {
		List<String> tokenize = unit.tokenize("   cat   dog \"cats and dogs\" ");
		
		assertThat(tokenize.toString(),is("[cat, dog, cats and dogs]"));
	}

	@Test
	public void tokensDoesNotSupportEscapes() {
		List<String> tokenize = unit.tokenize("   cat   dog \"cats \"and\" dogs\" ");
		
		assertThat(tokenize.toString(),is("[cat, dog, cats, and\", dogs\"]"));
	}

	@Test
	public void tokensDoesNotMisMatchQuotes() {
		List<String> tokenize = unit.tokenize(" first \"second third");
		
		assertThat(tokenize.toString(),is("[first, \"second, third]"));
	}
	
	@Test
	public void supportsDashesAndOtherCharacters() {
		List<String> tokenize = unit.tokenize(" f!r$t s-e-c-o-n-d ");
		assertThat(tokenize.size(), is(2));
		assertThat(tokenize.toString(),is("[f!r$t, s-e-c-o-n-d]"));

		tokenize = unit.tokenize("One~`!@#$%^&*()_+=-|\\}]{[:;'/?>.<,Token");
		assertThat(tokenize.size(), is(1));
	}
	
	@Test
	public void fullExampleOfSearchTerms(){
		List<String> tokenize = unit.tokenize("firstName \"Sir Name\" 555-22-1234 \"777-77-7777\" @ASDF 12/24/2012 A123456789");
		assertThat(tokenize.size(), is(7));
		assertThat(tokenize.toString(), is("[firstName, Sir Name, 555-22-1234, 777-77-7777, @ASDF, 12/24/2012, A123456789]"));
	}

}
