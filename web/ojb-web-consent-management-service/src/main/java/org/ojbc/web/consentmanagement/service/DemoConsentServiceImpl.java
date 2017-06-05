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
package org.ojbc.web.consentmanagement.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoConsentServiceImpl {
	
	private static final DemoConsentServiceImpl INSTANCE = new DemoConsentServiceImpl();
	
	private Random randomNumberGenerator = new Random();
	
	private DemoConsentServiceImpl() {
	}
	
	public static final DemoConsentServiceImpl getInstance() {
		return INSTANCE;
	}
	
	public String getDemoConsentRecords() throws JsonProcessingException {
		String ret;
		int numRecords = randomNumberGenerator.nextInt(14) + 1; // so we don't get zero
		List<Map<String, String>> records = new ArrayList<>(numRecords);
		for (int i=0;i < numRecords;i++) {
			Map<String, String> record = new HashMap<>();
			record.put("consentId", String.valueOf(i));
			record.put("bookingNumber", RandomStringUtils.randomAlphanumeric(8));
			record.put("nameNumber", RandomStringUtils.randomAlphanumeric(8));
			record.put("personFirstName", getRandomValueFromArray(RandomInmateAttributes.FIRST_NAMES));
			record.put("personMiddleName", getRandomValueFromArray(RandomInmateAttributes.FIRST_NAMES));
			record.put("personLastName", getRandomValueFromArray(RandomInmateAttributes.LAST_NAMES));
			record.put("personGender", getRandomValueFromArray(RandomInmateAttributes.GENDERS));
			record.put("personDOBString", getRandomBirthdate());
			records.add(record);
		}
		ObjectMapper mapper = new ObjectMapper();
		ret = mapper.writeValueAsString(records);
		return ret;
	}
	
	private String getRandomBirthdate() {
		LocalDate d = LocalDate.of(1970, Month.JANUARY, 1);
		d = d.plusDays(randomNumberGenerator.nextInt(365*30));
		return d.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
	private String getRandomValueFromArray(String[] values) {
		return values[randomNumberGenerator.nextInt(values.length-1)];
	}
	
	private static final class RandomInmateAttributes {
		public static final String[] FIRST_NAMES = {"Thomas", "Richard", "Harrison", "Matthew", "John", "Mark", "Andrew", "Sally", "Sue", "Jane", "Jennifer"};
		public static final String[] LAST_NAMES = {"Smith", "Jones", "Thompson", "Anderson", "Simpson", "Franklin", "Johnson"};
		public static final String[] GENDERS = {"M","F"};
	}

}
