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
package org.ojbc.test.util;

import java.io.File;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Assertions;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

/**
 * A class of utilities to support assertions/comparisons of XML documents.
 *
 */
public class XmlTestUtils {
	
	static {		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	/**
	 * Test whether an XML document matches an expected document
	 * @param expectedXML the "gold standard" expected XML
	 * @param testXML the XML to compare against the expected document
	 * @throws Exception
	 */
	public static final void compareDocuments(String expectedXML, String testXML) throws Exception {
		File xmlFile = new File(expectedXML);
		Document expectedXmlDoc = XmlUtils.toDocument(xmlFile);
		Document actualXmlDoc = XmlUtils.toDocument(testXML);
		compareDocuments(expectedXmlDoc, actualXmlDoc);		
	}	
	
	/**
	 * Test whether an XML document matches an expected document
	 * @param expectedXML the "gold standard" expected XML
	 * @param testXML the XML to compare against the expected document
	 * @throws Exception
	 */
	public static final void compareDocuments(String expectedXML, Document testXML) throws Exception {
		File xmlFile = new File(expectedXML);
		Document expectedXmlDoc = XmlUtils.toDocument(xmlFile);
		compareDocuments(expectedXmlDoc, testXML);		
	}
	
	/**
	 * Compare two documents and assert that the number of differences is zero.
	 * @param expectedDocument the "gold standard" expected XML
	 * @param testDocument the XML to compare against the expected document
	 */
	public static final void compareDocuments(Document expectedDocument, Document testDocument) {

		Diff diff = new Diff(expectedDocument, testDocument);						
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		@SuppressWarnings("all")
		List<Difference> diffList = detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		Assertions.assertEquals(0, diffCount, detailedDiff.toString());
		
	}
	
}
