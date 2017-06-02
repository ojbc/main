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
package org.ojbc.util.fedquery.error;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IncidentErrorProcessorTest {
			
	private static final Logger logger = Logger.getLogger(IncidentErrorProcessorTest.class.getName());
		
	@Before
	public void setup() throws ParserConfigurationException{

		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");			
	}	

	@Test
	public void vehicleToIncidentErrorMessageTest() throws Exception{			

		File expectedVehicleMessageFile = new File("src/test/resources/xml/VehicleToIncidentErrorMessage.xml");			
		
		String sExpectedVehicleXmlMessage = FileUtils.readFileToString(expectedVehicleMessageFile);
		
		String sActualVehicleXmlMessage = IncidentSearchErrorProcessor.returnVehicleToIncidentErrorMessage();						
														
		Diff diff = XMLUnit.compareXML(sExpectedVehicleXmlMessage, sActualVehicleXmlMessage);								
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		List<Difference> differenceList = detailedDiff.getAllDifferences();		
		
		Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());						
	}	
	
	@Test
	public void personToIncidentErrorMessageTest() throws Exception{			

		File expectedPersonMessageFile = new File("src/test/resources/xml/PersonToIncidentErrorMessage.xml");			
		
		String sExpectedPersonMessage = FileUtils.readFileToString(expectedPersonMessageFile);
		
		String sActualPersonMessage = IncidentSearchErrorProcessor.returnPersonToIncidentErrorMessage();						
														
		Diff diff = XMLUnit.compareXML(sExpectedPersonMessage, sActualPersonMessage);								
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		List<Difference> differenceList = detailedDiff.getAllDifferences();		
		
		Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());						
	}	
	 
}
