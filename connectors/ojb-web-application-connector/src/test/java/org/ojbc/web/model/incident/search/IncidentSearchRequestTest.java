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
package org.ojbc.web.model.incident.search;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class IncidentSearchRequestTest {

	private static final Log log = LogFactory.getLog( IncidentSearchRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIncidentSearchRequest() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModel();
		
		Assert.assertNotNull(isr);
		
		Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}

	@Test
	public void testIncidentSearchRequestSameDateRange() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModelSameDate();
		
		Assert.assertNotNull(isr);
		
        Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
        
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequestSameDateRange.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}

	@Test
	public void testIncidentSearchRequestOnlyStartDate() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModelOnlyStartDate();
		
		Assert.assertNotNull(isr);
		
        Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
        
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequestOnlyStartDate.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}
}
