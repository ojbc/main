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

package org.ojbc.util.camel.processor;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileResendProcessor {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
	
	private final static Log log = LogFactory.getLog(FileResendProcessor.class);
	
	public static void resendFile(String recordNumber, String pathToInputFolder, String pathToProcessedFolder, String fileNamePrefix) throws Exception
	{
		List<File> files = FileResendProcessor.findFailedFile(recordNumber, pathToProcessedFolder, fileNamePrefix);
		
		if (files == null || files.size() != 1)
		{
			log.error("Unable to find file");
			throw new IllegalStateException("Unable to find file");
		}	
		
		LocalDateTime now = LocalDateTime.now();
		String formatedDateTime = now.format(formatter);
		
		if (!StringUtils.endsWith(pathToInputFolder, "/"))
		{
			pathToInputFolder = pathToInputFolder + "/";
		}	
		
		//Start resent file name with RESEND_ so it doesn't match the fileNamePrefix
		File destinationFolderAndFileName = new File(pathToInputFolder + "input/RESEND_" + fileNamePrefix + "_" + formatedDateTime + ".xml");
		
		File fileToResend = files.get(0);
		
		log.info("Resending file: " + destinationFolderAndFileName);
		fileToResend.renameTo(destinationFolderAndFileName);
		
	}

	@SuppressWarnings("unchecked")
	public static List<File> findFailedFile(String recordNumber, String pathToProcessedFolder, String fileNamePrefix) {

		File directory = new File(pathToProcessedFolder);
		
		String fileNameFilter = fileNamePrefix + "_" + recordNumber + "_*_*.xml";
		
		log.info("File name filter: " + fileNameFilter);
		
		//For example: booking_170322072_20170323_110011192.xml
		List<File> files = (List<File>)FileUtils.listFiles(directory, new WildcardFileFilter(fileNameFilter), null);
	    
	    return files;
	    
	}
}
