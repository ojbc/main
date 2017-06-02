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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ConsentManagementRestController {
	
	private final Log log = LogFactory.getLog(ConsentManagementRestController.class);
	
	private static final String SAML_HEADER_NAME = "saml";
	public static final String DEMODATA_HEADER_NAME = "demodata-ok";
	
	private Random randomNumberGenerator = new Random();

	@RequestMapping(value="/cm-api/search", method=RequestMethod.GET, produces="application/json")
	public String search(HttpServletRequest request) throws IOException {
		
		String ret = null;
		
		Map<String, String> samlHeaderInfo = getSamlHeaderInfo(request.getHeader(SAML_HEADER_NAME));
		String demodataHeaderValue = request.getHeader(DEMODATA_HEADER_NAME);
		
		if (!samlHeaderInfo.isEmpty()) {
			
			// todo: hit adapter
			
		} else if ("true".equals(demodataHeaderValue)) {
			
			ServletContext context = request.getServletContext();
			InputStream resourceContent = context.getResourceAsStream("/WEB-INF/demodata.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceContent));
			StringBuffer sb = new StringBuffer(1024);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			ret = sb.toString();
			
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
			
			
		} else {
			// error?
			log.error("No SAML assertion in request, and not allowing demo data to be returned");
		}
		
		return ret;
		
	}
	
	private Map<String, String> getSamlHeaderInfo(String headerValue) {
		// todo: get it for real (using ShibbolethSamlAssertionRetriever), then parse the xml with xpath etc.
		// consider adding an enhancement to the ShibbolethSamlAssertionRetriever to do the parsing there to get the most common assertion info
		return new HashMap<>();
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
