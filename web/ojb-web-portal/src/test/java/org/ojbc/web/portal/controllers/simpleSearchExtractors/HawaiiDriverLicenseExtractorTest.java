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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.annotation.Resource;

import org.ojbc.web.model.person.search.PersonSearchRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/static-configuration-hawaii.xml"})
public class HawaiiDriverLicenseExtractorTest {

	@Resource
    DriverLicenseExtractor unit;
	
	private PersonSearchRequest personSearchRequest;
	
	@Before
	public void setup() {
		personSearchRequest = new PersonSearchRequest();
	}

    @Test
	public void regexMatch () {
		unit.extractTerm(Arrays.asList("X12345678", "H1234567"), personSearchRequest);
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(), nullValue());
	}

	@Test
	public void DLMatched() {
		unit.extractTerm(Arrays.asList("H12345678"), personSearchRequest);
		
		assertThat(personSearchRequest.getPersonDriversLicenseNumber(),is("H12345678"));
		assertThat(personSearchRequest.getPersonDriversLicenseIssuer(),is("HI"));
	}

}