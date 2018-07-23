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
package org.ojbc.connectors.subscriptionmanagement.processor;

import java.io.StringReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.subscription.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

@Service
public class SubscriptionCSVProcessor {

	private static final Log log = LogFactory
			.getLog(SubscriptionCSVProcessor.class);

	private static final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private List<Subscription> subscriptions = new ArrayList<Subscription>();

	@Value("${topic:topics:person/incident}")
	private String topic;
	
	public void clearSubscriptions()
	{
		subscriptions.clear();
	}
	
	public void processCsvEntry(String csvExtractLine) throws Exception {
		// If we see this column header, this is a column header row not data
		// row
		if (StringUtils.contains(csvExtractLine, "First,Last,DOB,E-mail to")) {
			return;
		}

		log.debug("CSV extract entry line as string: " + csvExtractLine);

		if (StringUtils.isBlank(csvExtractLine)) {
			throw new IllegalStateException("Extract entry is blank.");
		}

		Subscription subscriptionEntry = null;

		try {
			subscriptionEntry = returnCSVEntry(csvExtractLine);
			
			String subscriptionQualifier = generateSubscriptionQualifier(subscriptionEntry);
			
			subscriptionEntry.setTopic(topic);
			subscriptionEntry.setFullName(subscriptionEntry.getFirstName() + " " + subscriptionEntry.getLastName());
			subscriptionEntry.setSubscriptionQualificationID(subscriptionQualifier);
			subscriptionEntry.setSubscriptionPurpose("CS");
			
			Calendar today = Calendar.getInstance();
			subscriptionEntry.setSubscriptionStartDate(today.getTime());
			
			log.debug(subscriptionEntry);
			
			subscriptions.add(subscriptionEntry);

		} catch (Exception e) {
			log.error("Unable to process entry: " + csvExtractLine);
			e.printStackTrace();
		}

	}

	String generateSubscriptionQualifier(Subscription subscriptionEntry) throws Exception {
		
		String firstName = subscriptionEntry.getFirstName();
		
		String lastName = subscriptionEntry.getLastName();
		
		String dobString = "";
		
		Date dob = subscriptionEntry.getDateOfBirth();
		
		if (dob != null)
		{	
			dobString = DATE_ONLY_FORMAT.format(dob);
		}	
		
		String entryToHash = firstName + "_" + lastName + "_" + dobString;
		
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(entryToHash.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
	    
	    return myHash;
		
	}

	public Subscription returnCSVEntry(String extractLine) throws Exception {

		CSVReader reader = new CSVReader(new StringReader(extractLine),
				CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER,
				CSVParser.DEFAULT_ESCAPE_CHARACTER);
		String[] csvEntryLineArray = reader.readNext();

		Subscription subscriptionEntry = new Subscription();

		DateTimeFormatter formatterDate = DateTimeFormatter
				.ofPattern("yyyy-MM-dd");

		List<DateTimeFormatter> knownPatterns = new ArrayList<DateTimeFormatter>();
		knownPatterns.add(formatterDate);

		for (int i = 0; i < csvEntryLineArray.length; i++) {
			if (i == 0) {
				if (StringUtils.isNotBlank(csvEntryLineArray[i])) {
					subscriptionEntry.setFirstName(csvEntryLineArray[i].trim());
				}

			}

			if (i == 1) {
				if (StringUtils.isNotBlank(csvEntryLineArray[i])) {
					subscriptionEntry.setLastName(csvEntryLineArray[i].trim());
				}
			}

			if (i == 2) {

				if (StringUtils.isNotEmpty(csvEntryLineArray[i])) {

					try {
						Date dob = DATE_ONLY_FORMAT.parse(csvEntryLineArray[i]);
						subscriptionEntry.setDateOfBirth(dob);
					} catch (Exception e) {
						log.error("Unable to parse date: "
								+ csvEntryLineArray[i]);
					}
				}
			}

			if (i == 3) {
				if (StringUtils.isNotBlank(csvEntryLineArray[i])) {
					List<String> emailAddresses = new ArrayList<String>();
					emailAddresses.add(csvEntryLineArray[i]);

					subscriptionEntry.setEmailList(emailAddresses);
				}
			}
		}

		reader.close();
		return subscriptionEntry;

	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
