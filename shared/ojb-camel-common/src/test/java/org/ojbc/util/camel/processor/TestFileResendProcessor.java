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
package org.ojbc.util.camel.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class TestFileResendProcessor {

	private static final Log log = LogFactory.getLog(TestFileResendProcessor.class);
	
	@Test
	public void testFindFailedFiles()
	{
		List<File> files = FileResendProcessor.findFailedFile("9999999", "src/test/resources/xmlInstances/booking", "booking");
		
		assertNotNull(files);
		assertEquals(2, files.size());
		
	}	

	/**
	 * This test will return the file with the latest timestamp
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testFileToReturn() throws Exception
	{
		//Explicity set the last modified date on the file to ensure the test passes
    	try{

    		File file = new File("src/test/resources/xmlInstances/booking/booking_9999999_20170323_110011192.xml");

    		//print the original last modified date
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    		System.out.println("Original Last Modified Date : "
    				+ sdf.format(file.lastModified()));

    		//set this date
    		String newLastModified = "01/31/1998";

    		//need convert the above date to milliseconds in long value
    		Date newDate = sdf.parse(newLastModified);
    		file.setLastModified(newDate.getTime());

    		//print the latest last modified date
    		log.info("Lastest Last Modified Date : "
    				+ sdf.format(file.lastModified()));

    	}catch(ParseException e){

    		e.printStackTrace();

    	}
		
		List<File> files = FileResendProcessor.findFailedFile("9999999", "src/test/resources/xmlInstances/booking", "booking");
		
		File fileToResend = FileResendProcessor.returnFileToSend(files);
		
		assertNotNull(fileToResend);
		assertEquals("booking_9999999_20170324_110011192.xml", fileToResend.getAbsoluteFile().getName());
	}	

	@Test(expected=IllegalStateException.class)
	public void testFileToReturnException() throws Exception
	{
		List<File> files = null;
		
		File fileToResend = FileResendProcessor.returnFileToSend(files);
	}	

}

