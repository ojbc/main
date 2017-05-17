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
package org.ojbc.bundles.adapters.consentmanagement.processor;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;

public class TestBookingXmlProcessor {

	@Test
	public void testProcessBookingReport() throws Exception
	{
		File inputFile = new File("src/test/resources/xmlInstances/bookingReport/BookingReportJail.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
		
		Document report = OJBUtils.loadXMLFromString(inputStr);
		assertNotNull(report);
		
		BookingXMLProcessor bookingXMLProcessor = new BookingXMLProcessor();
		
		Consent consent = bookingXMLProcessor.processBookingReport(report);
		
		assertEquals("Given", consent.getPersonFirstName());
		assertEquals("Middle", consent.getPersonMiddleName());
		assertEquals("Last", consent.getPersonLastName());
		
		assertEquals("111111", consent.getBookingNumber());
		assertEquals("222222", consent.getNameNumber());
		
		assertEquals("M", consent.getPersonGender());
		
		assertEquals("1994-01-02", consent.getPersonDOB().toString());
		
	}
	
}
