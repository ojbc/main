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
package org.ojbc.adapters.rapbackdatastore.processor.demographics;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalHistoryDemographicsUpdateRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestCriminalHistoryDemographicUpdateProcessor {

	@Test
	public void testReturnCriminalHistoryDemographicsUpdateRequest() throws Exception
	{
		Document doc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/criminalHistoryDemographicsUpdateReport/CriminalHistory-DemographicsUpdate-Report.xml"));
		
		CriminalHistoryDemographicUpdateProcessor criminalHistoryDemographicUpdateProcessor = new CriminalHistoryDemographicUpdateProcessor();
		
		CriminalHistoryDemographicsUpdateRequest criminalHistoryDemographicsUpdateRequest = criminalHistoryDemographicUpdateProcessor.returnCriminalHistoryDemographicsUpdateRequest(doc);
		assertNotNull(criminalHistoryDemographicsUpdateRequest);
		
		assertEquals("1962-10-16", criminalHistoryDemographicsUpdateRequest.getPostUpdateDOB().toString());
		
		assertEquals("A123459", criminalHistoryDemographicsUpdateRequest.getPreUpdateCivilSID());
		assertEquals("A123459", criminalHistoryDemographicsUpdateRequest.getPostUpdateCivilSID());

		assertEquals("Bart", criminalHistoryDemographicsUpdateRequest.getPostUpdateGivenName());
		
		assertEquals("B", criminalHistoryDemographicsUpdateRequest.getPostUpdateMiddleName());

		assertEquals("Simpsonsonia", criminalHistoryDemographicsUpdateRequest.getPostUpdateSurName());
		
		assertEquals("0400025", criminalHistoryDemographicsUpdateRequest.getPostUpdateOTN());
		assertEquals("0400025", criminalHistoryDemographicsUpdateRequest.getPreUpdateOTN());
		
	}
	
}
