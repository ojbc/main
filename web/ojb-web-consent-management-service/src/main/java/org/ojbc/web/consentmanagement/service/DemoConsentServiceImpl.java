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

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoConsentServiceImpl {
	
	private final Log log = LogFactory.getLog(DemoConsentServiceImpl.class);
	
	private static final DemoConsentServiceImpl INSTANCE = new DemoConsentServiceImpl();
	private static final int INITIAL_RECORD_COUNT = 10;
	private static final int MAX_RECORD_COUNT = 25;
	private static final int RECORD_MOD_HEARTBEAT_MILLIS = 8000;
	
	private Random randomNumberGenerator = new Random();
	private List<Map<String, String>> records;
	
	private DemoConsentServiceImpl() {
		records = new ArrayList<>(INITIAL_RECORD_COUNT);
		for (int i=0;i < INITIAL_RECORD_COUNT;i++) {
			records.add(generateRandomDemoRecord());
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				boolean writeSkipMessage = true;
				while (true) {
					int size = records.size();
					if (size < MAX_RECORD_COUNT) {
						records.add(generateRandomDemoRecord());
						log.info("Generated demo record, current size=" + size);
						writeSkipMessage = true;
					} else {
						if (writeSkipMessage) {
							log.info("Maximum record count of " + MAX_RECORD_COUNT + " reached, not creating any more records.");
							writeSkipMessage = false;
						}
					}
					try {
						Thread.sleep(RECORD_MOD_HEARTBEAT_MILLIS);
					} catch (InterruptedException e) {
						log.warn("Demo record generation thread interrupted, exiting.");
					}
				}
			}}).start();
	}

	private Map<String, String> generateRandomDemoRecord() {
		int consentId = getNextConsentId();
		Map<String, String> record = new HashMap<>();
		record.put("consentId", String.valueOf(consentId));
		record.put("bookingNumber", RandomStringUtils.randomAlphanumeric(8));
		record.put("nameNumber", RandomStringUtils.randomAlphanumeric(8));
		record.put("personFirstName", getRandomValueFromArray(RandomInmateAttributes.FIRST_NAMES));
		record.put("personMiddleName", getRandomValueFromArray(RandomInmateAttributes.MIDDLE_NAMES));
		record.put("personLastName", getRandomValueFromArray(RandomInmateAttributes.LAST_NAMES));
		record.put("personGender", getRandomValueFromArray(RandomInmateAttributes.GENDERS));
		record.put("personDOBString", getRandomBirthdate());
		return record;
	}

	private int getNextConsentId() {
		return randomNumberGenerator.nextInt();
	}
	
	public static final DemoConsentServiceImpl getInstance() {
		return INSTANCE;
	}
	
	public String getDemoConsentRecords() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(records);
	}
	
	public int removeRecord(String jsonMessage) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> messageRecord = mapper.readValue(jsonMessage, new TypeReference<HashMap<String,String>>() {});
		String consentIdS = String.valueOf(messageRecord.get("consentId"));
		int idx = 0;
		for (Map<String, String> record : records) {
			if (consentIdS.equals(record.get("consentId"))) {
				records.remove(idx);
				log.info("Removed record with consentId=" + consentIdS);
				return 1;
			}
			idx++;
		}
		return 0;
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
		public static final String[] MIDDLE_NAMES = {"Thomas", "Richard", "Harrison", "Matthew", null, null, null, "Sally", "Sue", "Jane", "Jennifer"};
		public static final String[] LAST_NAMES = {"Smith", "Jones", "Thompson", "Anderson", "Simpson", "Franklin", "Johnson"};
		public static final String[] GENDERS = {"M","F"};
	}

}
