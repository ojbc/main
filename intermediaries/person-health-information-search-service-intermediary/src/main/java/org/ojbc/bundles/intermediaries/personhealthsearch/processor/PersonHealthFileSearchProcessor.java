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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.bundles.intermediaries.personhealthsearch.aggregator.PersonHealthResponseAggregator;
import org.ojbc.util.helper.Hash;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class PersonHealthFileSearchProcessor {

	private Logger logger = Logger.getLogger(PersonHealthResponseAggregator.class);
	private String responseRootFilePath;
	
	public boolean searchForPersonHealthResponse(@Body Document requestMessage) throws Exception
	{
		boolean searchResponseFound = false;
		
		String extractedFileName = extractFileName(requestMessage);
		
		logger.info("File name before hashing: " + extractedFileName);
		
		String hashedExtractedFileName = "response_" + Hash.md5(extractedFileName) + ".xml";
		
		logger.info("Hashed File Name to search for: " + hashedExtractedFileName);
		
		File phiResultsFile = new File(responseRootFilePath + "/" + hashedExtractedFileName);
		
		if (phiResultsFile.isFile())
		{
			searchResponseFound = true;
			logger.info("File found, use cached response instead of calling web service.");
		}	
		else
		{
			logger.info("Cached response not found.  Call web service.");
		}	
				
		return searchResponseFound;
	}
	
	public String retreivePersonHealthInfo(@Body Document requestMessage) throws Exception
	{
		String extractedFileName = extractFileName(requestMessage);
		
		String hashedExtractedFileName = "response_" + Hash.md5(extractedFileName) + ".xml";
		
		logger.info(hashedExtractedFileName);
		
		File phiResultsFile = new File(responseRootFilePath + "/" + hashedExtractedFileName);
		
		String content = "";
		
		if (phiResultsFile.isFile())
		{
			content = new String(Files.readAllBytes(Paths.get(responseRootFilePath + "/" + hashedExtractedFileName)));
		}	
		
		return content;
	}

	protected String extractFileName(Document requestMessage) throws Exception {
		
		StringBuffer extractedFilename = new StringBuffer();
		
		String firstName = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/nc30:PersonName/nc30:PersonGivenName");
		logger.info("Person First Name: "  + firstName);
		
		if (StringUtils.isNotBlank(firstName))
		{
			extractedFilename.append(firstName);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}	
		
		String middleName = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/nc30:PersonName/nc30:PersonMiddleName");
		logger.info("Person Middle Name: "  + middleName);
		
		if (StringUtils.isNotBlank(middleName))
		{
			extractedFilename.append(middleName);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}	
		
		String lastName = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/nc30:PersonName/nc30:PersonSurName");
		logger.info("Person Last Name: "  + lastName);

		if (StringUtils.isNotBlank(lastName))
		{
			extractedFilename.append(lastName);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}	
		
		
		String dob = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/nc30:PersonBirthDate/nc30:Date");
		logger.info("Person DOB: "  + dob);
		
		if (StringUtils.isNotBlank(dob))
		{
			extractedFilename.append(dob);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}			
		
		String sex = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/jxdm51:PersonSexCode");
		logger.info("Person Sex: "  + sex);
		
		if (StringUtils.isNotBlank(sex))
		{
			extractedFilename.append(sex);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}			
				
		
		String race = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/pc-phi-codes:PersonRaceCode");
		logger.info("Person Race: "  + race);
		
		if (StringUtils.isNotBlank(race))
		{
			extractedFilename.append(race);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}		

		String personNameNumber = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/phisreq-ext:PersonSystemAssignedIdentification/nc30:IdentificationID");
		logger.info("Person Name Number: "  + personNameNumber);
		
		if (StringUtils.isNotBlank(personNameNumber))
		{
			extractedFilename.append(personNameNumber);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}		

		extractedFilename.deleteCharAt(extractedFilename.lastIndexOf("_"));
		
		return extractedFilename.toString();
		
	}

	public String getResponseRootFilePath() {
		return responseRootFilePath;
	}

	public void setResponseRootFilePath(String responseRootFilePath) {
		this.responseRootFilePath = responseRootFilePath;
	}

}
