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
package org.ojbc.util.statemanager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LastUpdateFileManager implements LastUpdateManager {

	/**
	 * This is the Date variable holding the Date / Time of the last episode processed
	 */
	private Date lastUpdateDate;
	
	/**
	 * The date format required by the SQL Server queries for SimpleDateFormat
	 */
	private final String LAST_UPDATE_DATE_PATTERN_QUERY;
	
	/**
	 * The date format for queries
	 */
	private SimpleDateFormat queryFormat;

	/**
	 * The location of the configuration file that the date is persisted to
	 */
	private String configurationFileLocation;
	
	/**
	 * logger
	 */
	private static final Log log = LogFactory.getLog( LastUpdateFileManager.class );	
	
	/**
	 * This constructure receives the properties file location from Spring and will set the 
	 * value of the lastUpdateDate
	 * 
	 * @param configurationFileLocationArgument
	 */
	public LastUpdateFileManager(String configurationFileLocationArgument, String lastUpdateDatePattern) throws Exception
	{
		LAST_UPDATE_DATE_PATTERN_QUERY = lastUpdateDatePattern;
		//Set the location of the properties file
		configurationFileLocation = configurationFileLocationArgument;
		
		//Create both the query and record set data format
		queryFormat = new SimpleDateFormat(LAST_UPDATE_DATE_PATTERN_QUERY);
		
		//If last update date is not set, read from file		
		if (lastUpdateDate == null)
		{
			// Read properties file.
			Properties properties = new Properties();
			    properties.load(new FileInputStream(configurationFileLocation));

			//This file has only one property: lastDatabaseRecordProcessedDateTime    
			String lastUpdateString = properties.getProperty("lastDatabaseRecordProcessedDateTime");
			
			//The property write escapes the colons with a backslash, remove it here for the queries
			StringUtils.remove(lastUpdateString, "\\");
			
			//Set the last update date
			lastUpdateDate = queryFormat.parse(lastUpdateString);
		}	
	}
	
	/**
	 * This method will return the last update date in the query format so it can be used to create the Access database query
	 */
	@Override
	public String getLastUpdateTime() {
		return queryFormat.format(lastUpdateDate);
	}

	/**
	 * This method will update the lastUpdateDate and update the properties file with this value.
	 * The database return the date in a different format that we want to persist it so this method
	 * will do the appropriate conversions.
	 */
	@Override
	public void saveLastUpdateTime(String lastUpdateArgument) {
		
		log.debug("Last Update Date Argument to persist to file system: " + lastUpdateArgument);
		
    	Properties prop = new Properties();
    	 
    	try {
    		//update lastUpdateDate using the format returned by a record set
    		lastUpdateDate = queryFormat.parse(lastUpdateArgument);
    		
    		//set the properties value using the query format for date
    		prop.setProperty("lastDatabaseRecordProcessedDateTime", queryFormat.format(lastUpdateDate));
 
    		//save properties to project root folder
    		prop.store(new FileOutputStream(configurationFileLocation), "This file contains the timestamp of the last episode that was processed");
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	catch (ParseException e) {
			e.printStackTrace();
		}
    	
	}

	public void setConfigurationFileLocation(String configurationFileLocation) {
		this.configurationFileLocation = configurationFileLocation;
	}

	public String getConfigurationFileLocation() {
		return configurationFileLocation;
	}

}
