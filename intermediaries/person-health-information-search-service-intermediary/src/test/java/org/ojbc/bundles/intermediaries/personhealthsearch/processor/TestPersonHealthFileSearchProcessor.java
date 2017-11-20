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
package org.ojbc.bundles.intermediaries.personhealthsearch.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.ojbc.bundles.intermediaries.personhealthsearch.aggregator.PersonHealthResponseAggregator;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.Hash;
import org.w3c.dom.Document;

public class TestPersonHealthFileSearchProcessor {

	private Logger logger = Logger.getLogger(PersonHealthResponseAggregator.class);

	@Test
	@Ignore
	public void testProcessRequestFileRepository() throws Exception
	{
		PersonHealthFileSearchProcessor personHealthFileSearchProcessor = new PersonHealthFileSearchProcessor();
		
		personHealthFileSearchProcessor.setRequestFileRepositoryPath("/tmp/ojb/demo/custodyBooking/booking/PHI/inputRepository");
		
		personHealthFileSearchProcessor.setRequestFilePath("/tmp/ojb/demo/custodyBooking/booking/PHI/input");
		
		personHealthFileSearchProcessor.setRequestFileDelayInMillis(3000);
		
		personHealthFileSearchProcessor.setStartHour(12);
		personHealthFileSearchProcessor.setEndHour(1);
		
		personHealthFileSearchProcessor.processRequestFileRepository();
	}
	
	@Test
	public void testExtractFileName() throws Exception
	{
		PersonHealthFileSearchProcessor personHealthFileSearchProcessor = new PersonHealthFileSearchProcessor();

		personHealthFileSearchProcessor.setResponseRootFilePath("src/test/resources/xmlInstances/responses");
		
		String xml = new String(Files.readAllBytes(Paths.get("src/test/resources/xmlInstances/PersonHealthInformationSearchRequest-PII.xml")));
		
		Document requestMessage = OJBUtils.loadXMLFromString(xml);
		
		String fileName = personHealthFileSearchProcessor.extractFileName(requestMessage,"");
		
		logger.info("Extracted file name: " + fileName);
		
		//We explicitly remove race and expect the algorithm to find it by trying the value 1
		assertEquals("Walter_Hartwell_White_1959-09-07_M_._1234567890", fileName);
		
		String hashedExtractedFileName = "response_" + Hash.md5("Walter_Hartwell_White_1959-09-07_M_._1234567890") + ".xml";
		
		logger.info("Hashed File Name: " + hashedExtractedFileName);
		
		assertTrue(personHealthFileSearchProcessor.searchForPersonHealthResponse(requestMessage));
		
		String responseFile = personHealthFileSearchProcessor.retreivePersonHealthInfo(requestMessage);
		
		logger.info("Response File from file system: " + responseFile);
		
		assertTrue(responseFile.endsWith("</phisres-doc:PersonHealthInformationSearchResults>"));
	}
}
