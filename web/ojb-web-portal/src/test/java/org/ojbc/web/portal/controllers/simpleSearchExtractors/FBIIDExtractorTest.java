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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.web.model.person.search.PersonSearchRequest;

@RunWith(JUnitParamsRunner.class)
public class FBIIDExtractorTest {

	FBIIDExtractor unit;
	private PersonSearchRequest personSearchRequest;
	
	@Before
	public void setUp() throws Exception {
		unit = new FBIIDExtractor();
		personSearchRequest = new PersonSearchRequest();
	}

	@Test
	public void emptyInput() throws Exception {
		unit.extractTerm(new ArrayList<String>(), personSearchRequest);

		assertThat(personSearchRequest.getPersonFBINumber(), nullValue());
	}

	@Test
	public void regexMatch() {
		unit.extractTerm(Arrays.asList("12-32213","somestring","","V1234"), personSearchRequest);

		assertThat(personSearchRequest.getPersonFBINumber(), nullValue());
	}

	@Test
	@Parameters({ 
          "1", 
          "12",
          "123",
          "1234",
          "12345",
          "123456",
          "1234567"})
	public void fbiIdMatched_OneToSevenDigitsNoAlpha(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	public void fbiIdNotMatched_EightDigitsNoAlpha() {
		unit.extractTerm(Arrays.asList("12345678"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), nullValue());
	}
	
	@Test
	@Parameters({
	    "123456h",
	    "1aa0",
        "22ca1",
        "1aA0",
        "22Ca1",})
	public void testLowercaseParameter(String param) {
        unit.extractTerm(Arrays.asList(param), personSearchRequest);
        assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	@Parameters({ 
        "1A", 
        "12B",
        "123C",
        "1234D",
        "12345E",
        "123456F",
        "123456G",
        "123456H",
        "9H",
        "88G",
        "777F",
        "6666E",
        "55555D",
        "444444C",
        "333333B",
        "222222A",
        "000000H"})
	public void fbIdMatched_OneToSixDigitsPlusAlphaAthruH(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	@Parameters({
		"1I",
		"22J",
		"333K",
		"4444L",
		"55555M",
		"666666N",
		"0P",
		"0Q",
		"0O",
		"0I",
		"K"
		})
	public void fbiIdNotMatched_OneToSixDigitsPlusAlphaAthruH_InvalidAlpha(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(nullValue()));
	}
	
	@Test
	@Parameters({
		"1J1",
		"22K2",
		"333L3",
		"4444M4",
		"55555N5",
		"666666P6",
		"777777Q7",
		"88888R8",
		"9999S9",
		"000T10",
		"11U11",
		"12V1",
		"13W2",
		"24X10",
		"35Y4",
		"044445Z9"
		})
	public void fbiIdMatched_OneToSixDigitsPlusAlphaJthruZPlus1Or2CheckDigits(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	@Parameters({
		"1J0",
		"22K12",
		"333L13",
		"4444M14",
		"55555N15",
		"666666P16",
		"777777Q17",
		"88888R18",
		"9999S19",
		"000T0",
		"11U00",
		"12V01",
		"13W02",
		"24X030",
		"35Y04",
		"044445Z05",
		"044445Z06",
		"044445Z07",
		"044445Z08",
		"044445Z09"
		})
	public void fbiIdNotMatched_OneToSixDigitsPlusAlphaJthruZPlus1Or2CheckDigits_InvalidCheckDigits(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(nullValue()));
	}
	
	@Test
	@Parameters({
		"1AA0",
		"22CA1",
		"333DB2",
		"4444EC3",
		"55555FD4",
		"666666HE5",
		"0JA6",
		"01KB7",
		"012LC8",
		"0123MD9",
		"01234NE0",
		"012345PA1",
		"123456RB2",
		"234567TC3",
		"345678VD4",
		"456789WE5",
		"567890XA6"
		})
	public void fbiIdMatched_OneToSixDigitsPlusTwoAlphaPlusOneCheckDigit(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	@Parameters({
		"1BA0",
		"22GA1",
		"333IB2",
		"4444OC3",
		"55555QD4",
		"666666SE5",
		"0UA6",
		"0YB7",
		"012ZC8",
		"0123AF9",
		"01234CG0",
		"012345AH1",
		"123456RI2",
		"234567TJ3",
		"345678VK4",
		"456789WL5",
		"567890XM6",
		"567890XN6",
		"567890XO6",
		"567890XP6",
		"567890XQ6",
		"567890XR6",
		"567890XS6",
		"567890XT6",
		"567890XU6",
		"567890XV6",
		"567890XW6",
		"567890XX6",
		"567890XY6",
		"567890XZ6",
		})
	public void fbiIdNotMatched_OneToSixDigitsPlusTwoAlphaPlusOneCheckDigit_invalidCharacters(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(nullValue()));
	}
	
	@Test
	@Parameters({
		"1AA10",
		"22CA11",
		"333DB12",
		"4444EC13",
		"55555FD14",
		"666666HE15",
		"0JA16",
		"01KB17",
		"012LC18",
		"0123MD19",
		"01234NE20",
		"012345PA31",
		"123456RB42",
		"234567TC53",
		"345678VD64",
		"456789WE75",
		"567890XA86",
		"567890XA97"
		})
	public void fbiIdNotMatched_OneToSixDigitsPlusTwoAlphaPlusOneCheckDigit_invalidCheckDigit(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(nullValue()));
	}
	
	@Test
	@Parameters({
		"0", // 1 to 7 digits only
		"1234567", // 1 to 7 digits only
		"123456B", // 1 to 6 digits plus alpha A-H and check 
		"09H", // 1 to 6 digits plus alpha A-H
		"23456DB1", // 1 to 6 digits plus two alpha (with some limitations) and check digit 0-9
		"55555FD0", // 1 to 6 digits plus two alpha (with some limitations) and check digit 0-9
		"999999Z9", // 1 to 6 digits plus alpha J-Z and check digit(s) 1-11
		"0Y10", // 1 to 6 digits plus alpha J-Z and check digit(s) 1-11
		})
	public void fbiIdMatched_mixedFormats(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(param));
	}
	
	@Test
	@Parameters({
		"A",
		"AB", 
		"0DA11",  
		"0GF0", 
		"11111111", 
		"90I",
		"88O0",
		"999K12",
		"A0DA11",
		"69D4",
		"K9"
		})
	public void fbiIdNotMatched_mixedFormats(String param) {
		unit.extractTerm(Arrays.asList(param), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonFBINumber(), is(nullValue()));
	}
}
