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
package org.ojbc.web.model.firearm.search;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class FirearmSearchRequestTest {
	private static final Log log = LogFactory.getLog( FirearmSearchRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}
	
	@Test
	public void firearmSearchRequest() throws Exception {
		FirearmSearchRequest fsr = FirearmSearchRequestTestUtils.createFirearmSearchRequestModel(SearchFieldMetadata.ExactMatch);
		
		Assert.assertNotNull(fsr);
		
		Document doc = RequestMessageBuilderUtilities.createFirearmSearchRequest(fsr);
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/firearmSearchRequest/firearmSearchRequest.xml");
		
		//Use XML Unit to compare these files
		XmlTestUtils.compareDocuments(doc, XmlUtils.toDocument(expectedReponseFile));
	}
	
	@Test
	public void firearmSearchRequest_PartialSerialNum() throws Exception {
		FirearmSearchRequest fsr = FirearmSearchRequestTestUtils.createFirearmSearchRequestModel(SearchFieldMetadata.Partial);
		
		Assert.assertNotNull(fsr);
		
		Document doc = RequestMessageBuilderUtilities.createFirearmSearchRequest(fsr);
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/firearmSearchRequest/firearmSearchRequest-partialSerial.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile, Charset.defaultCharset());
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(OJBUtils.getStringFromDocument(doc), expectedResponseAsString);
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());		
	}
}
