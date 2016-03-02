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

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * This class checks see if a directory exists and there are files inside of it.
 * This is typically used to verify that an adapter or intermediary
 * has received files in a given from a web service client.
 * 
 * It can be invoked in a camel route from a quartz route for example.
 * 
 */
public class FileAndDirectoryVerificationProcessor {

	private String directoryPath;
	
	public boolean doesDirectoryWithFilesExist()
	{
		File directory = new File(directoryPath);  
		File[] files = directory.listFiles();
		
		DateTime today = new DateTime();
		
		boolean areThereFilesFromToday=false;
				
		if (files != null)
		{	
			for (File file : files)
			{
				long timesteamp = file.lastModified();
				
				DateTime fileTimeStamp = new DateTime(timesteamp);
				
				if (Days.daysBetween(today, fileTimeStamp).getDays() == 0)
				{
					areThereFilesFromToday=true;
					break;
				}	
				
			}	
		}	
		
		return areThereFilesFromToday;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	
}
