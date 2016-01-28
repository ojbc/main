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
package org.ojbc.bundles.intermediaries.identificationreporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.test.util.IgnoreNamedElementsDifferenceListener;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XsltTransformer;
import org.xml.sax.SAXException;

public class ArrestReportTransformTest {
	
	private XsltTransformer xsltTransformer;
	
	@Before
	public void init(){
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
		xsltTransformer = new XsltTransformer();
	}	
	
	@Test
	public void arrestReportTransform() throws IOException, SAXException{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/xmlInstances/identificationReport/person_identification_search_results_state_criminal.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/arrestReportTransform.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/xmlInstances/arrestReport/arrestReport.xml"));
				
		compareXml(expectedXmlString, actualTransformedXml);							
	}
	
	
	private Map<String, Object> getXsltParamMap(){
	
		Map<String, Object> xsltParamMap = new HashMap<String, Object>();		
		
		return xsltParamMap;
	}
	
	private static void compareXml(String expectedXmlString, String actualTransformedXml) throws SAXException, IOException{
		
		Diff diff = XMLUnit.compareXML(expectedXmlString, actualTransformedXml);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);

		detailedDiff.overrideDifferenceListener(new IgnoreNamedElementsDifferenceListener("lexs:MessageDateTime"));
		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());
	}	
}

