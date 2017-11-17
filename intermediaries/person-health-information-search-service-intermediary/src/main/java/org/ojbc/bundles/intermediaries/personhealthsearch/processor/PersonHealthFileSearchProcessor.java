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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

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
	private String requestFileRepositoryPath;
	private String requestFilePath;
	private Integer requestFileDelayInMillis;
	private Integer startHour;
	private Integer endHour;
	private boolean checkTime;
	
	public void processRequestFileRepository() throws Exception
	{
		final File folder = new File(requestFileRepositoryPath);
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	        	
	            if (outsideAllowedTimeInterval())
	            {
	            	logger.info("\n\n\n\n\nOutside allowed time interval, exit!!\n\n\n\n");
	            	return;
	            }	
	            
	            logger.info("File Name: " + fileEntry.getName());
	            
	            boolean isMoved = fileEntry.renameTo(new File(requestFilePath + "/" + fileEntry.getName()));
	            if (isMoved) 
	            {
	                logger.info("File Moved to: " + requestFilePath + "/" + fileEntry.getName());
	            }
	            else
	            {
	            	logger.error("Unable to create .done file: " + requestFilePath + "/" + fileEntry.getName() );
	            }	
	            
	            Thread.sleep(requestFileDelayInMillis);

	        }
	    }
	}
	
	private boolean outsideAllowedTimeInterval() {
		LocalTime now = LocalTime.now();
		
		logger.info("Current hour: " + now.getHour() + ", start hour: " + startHour + " end hour: " + endHour);
		
		if (checkTime)
		{	
			if (now.getHour() < startHour && now.getHour() >= endHour)
			{
				return true;
			}	
		}
		
		return false;
	}
	
	public boolean searchForPersonHealthResponse(@Body Document requestMessage) throws Exception
	{
		boolean searchResponseFound = false;
		
		searchResponseFound = findPersonHealthResponse(requestMessage, "");	
		
		if (searchResponseFound)
		{
			return true;
		}	

		for (int i=1; i<6; i++)
		{
			logger.info("Search for race with code: " + String.valueOf(i));
			
			searchResponseFound = findPersonHealthResponse(requestMessage, String.valueOf(i));

			if (searchResponseFound)
			{
				return searchResponseFound;
			}	

		}	
		
		return searchResponseFound;
	}

	private boolean findPersonHealthResponse(Document requestMessage,
			String race) throws Exception {
		
		boolean searchResponseFound = false;
		
		String extractedFileName = extractFileName(requestMessage,race);
		
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
		String content = returnPersonHealthInfoMessage(requestMessage,"");	
		
		if (StringUtils.isNotBlank(content))
		{
			return content;
		}	
		
		for (int i=1; i<6; i++)
		{
			logger.info("Search for race with code: " + String.valueOf(i));
			
			String response = returnPersonHealthInfoMessage(requestMessage, String.valueOf(i));

			if (StringUtils.isNotBlank(response))
			{
				return response;
			}	

		}	
		
		//Allow timer to be the first exchange
		Thread.sleep(1000);
		
		return content;
	}

	private String returnPersonHealthInfoMessage(Document requestMessage, String race)
			throws Exception, IOException {
		String extractedFileName = extractFileName(requestMessage,race);
		
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

	protected String extractFileName(Document requestMessage, String race) throws Exception {
		
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
		logger.info("Person Sex Code: "  + sex);

		if (StringUtils.isBlank(sex))
		{
			sex = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/nc30:PersonSexText");
			logger.info("Person Sex Text: "  + sex);
		}	
		
		if (StringUtils.isNotBlank(sex))
		{
			extractedFilename.append(sex);
			extractedFilename.append("_");
		}	
		else
		{
			extractedFilename.append("._");
		}			
				

		if (StringUtils.isBlank(race))
		{
			race = XmlUtils.xPathStringSearch(requestMessage, "/phisreq-doc:PersonHealthInformationSearchRequest/nc30:Person/pc-phi-codes:PersonRaceCode");
			logger.info("Person Race: "  + race);
		}	
		
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

	public String getRequestFileRepositoryPath() {
		return requestFileRepositoryPath;
	}

	public void setRequestFileRepositoryPath(String requestFileRepositoryPath) {
		this.requestFileRepositoryPath = requestFileRepositoryPath;
	}

	public Integer getRequestFileDelayInMillis() {
		return requestFileDelayInMillis;
	}

	public void setRequestFileDelayInMillis(Integer requestFileDelayInMillis) {
		this.requestFileDelayInMillis = requestFileDelayInMillis;
	}

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public String getRequestFilePath() {
		return requestFilePath;
	}

	public void setRequestFilePath(String requestFilePath) {
		this.requestFilePath = requestFilePath;
	}

	public boolean isCheckTime() {
		return checkTime;
	}

	public void setCheckTime(boolean checkTime) {
		this.checkTime = checkTime;
	}

}
