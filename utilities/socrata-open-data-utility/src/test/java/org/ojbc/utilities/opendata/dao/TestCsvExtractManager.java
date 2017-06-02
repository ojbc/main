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
package org.ojbc.utilities.opendata.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.store.fs.FileUtils;
import org.junit.Test;
import org.ojbc.utilities.opendata.IncidentArrestUtils;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;

public class TestCsvExtractManager {

	private static final Log log = LogFactory.getLog(TestCsvExtractManager.class);
	
	@Test
	public void testWriteCSVFile() throws Exception
	{
		String csvFileDirectory = "testCSVfileWrite";
		
		FileUtils.deleteRecursive(csvFileDirectory, false);
		
		FileUtils.createDirectories(csvFileDirectory);
		assertTrue(FileUtils.exists(csvFileDirectory));
		
		CsvExtractManager csvExtractManager = new CsvExtractManager();
		
		csvExtractManager.setCsvFileRootDirectory(csvFileDirectory);
		
		csvExtractManager.setUpCSVFileForWriting();

		IncidentArrest incidentArrest = IncidentArrestUtils.returnExampleIncidentArrest();
		
		csvExtractManager.incidentArrestToCSV(incidentArrest);
		
		csvExtractManager.flushDataAndCloseWriter();
		
		final File folder = new File(csvFileDirectory);
		
		if (folder.listFiles().length != 1)
		{
			throw new Exception("Directory should have one file");
		}	
		
		String csvFileName = "";
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            throw new Exception("Directory should not contain sub directories");
	        } else {
	        	csvFileName = fileEntry.getAbsolutePath();
	        }
	    }
		
	    log.info("CSV File Name: " + csvFileName);
	    
	    assertTrue(StringUtils.contains(csvFileName, csvFileDirectory + "/Incident_Extract_2016"));
	    assertTrue(StringUtils.endsWith(csvFileName, ".csv"));
		
	    int lineNumber = 1;
	    
	    Scanner scan = new Scanner(new File(csvFileName));
	    while(scan.hasNextLine()){
	        String line = scan.nextLine();
	        //Here you can manipulate the string the way you want
	        
	        if (lineNumber == 1)
	        {
	        	assertEquals("row_identifier,incident_case_number,reporting_agency,incident_date_time,incident_type_description,incident_category_description,incident_location_town,incident_location_county,arrest_county,age_in_years,person_race_description,arrest_drug_inolved,arrest_drug_involved_description,arrestee_sex_description,arresting_agency,arrest_charges,incident_load_timestamp", line);
	        }	

	        if (lineNumber == 2)
	        {
	        	assertEquals("1,IC Number,reporting agency,2003-12-05 08:57:12,Desc1|Desc2,Category1|Category2,inc town,incident county,arrest county,29,race,True,marijuana|heroin,Male,Local PD,charge1|charge2,2016-02-19 03:18:03", line);
	        }	
	        
	        if (lineNumber == 3)
	        {
	        	assertEquals("", line);
	        }
	        	
	        lineNumber++;
	    }
	    
	    //Third line is empty
	    assertEquals(3, lineNumber);
	    
		FileUtils.deleteRecursive(csvFileDirectory, false);
		
	}

	
}
