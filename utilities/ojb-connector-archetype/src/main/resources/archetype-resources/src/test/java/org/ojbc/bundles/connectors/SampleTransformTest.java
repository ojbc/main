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
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.ojbc.bundles.connectors;

import java.io.File;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XsltTransformer;
import org.ojbc.util.xml.XmlUtils;

/**
 * Demonstrates a typical unit test that transforms xml input with an xslt and 
 * asserts the expected xml output.
 * 
 *  XmlUnit is used to compare all important aspects of xml files.  Potential differences 
 *  are output in log messages
 * 
 * @author SEARCH
 */
public class SampleTransformTest {
			
	private XsltTransformer xsltTransformer;
	
	/**
	 * Configures the XmlUnit library to ignore trivial differences of xml output 
	 * against expected xml output
	 */
	@Before
	public void setup() throws ParserConfigurationException{

		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
		
		xsltTransformer = new XsltTransformer();		
	}	

	/**
	 * Transforms a simple xml input file and asserts the expected output
	 */
	@Test
	public void sampleTransformTest() throws Exception{		
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/SampleXmlMessageInput.xml",
				"src/main/resources/xsl/ConnectorTransform.xsl",
				"src/test/resources/xmlInstances/ExpectedTransformedXmlMessage.xml");
        
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());						
	}
	
	
	/**
	 * Returns a diff object of differences(if any) after comparing expected xml output of an 
	 * xsl transform with the actual xml output.
	 */
	private DetailedDiff runTransform(String inputFileClasspath, String xsltClasspath, 
			String expectedOutputFileClasspath) throws Exception{
				
		File inputFile = new File(inputFileClasspath);
		Source inputXmlSource = XmlUtils.getDomSourceIgnoringDtd(inputFile);
						
		File xsltFile = new File(xsltClasspath);
		StreamSource xsltFileStreamSource = new StreamSource(xsltFile);
				
		String actualTransformedResultXml = xsltTransformer.transform(inputXmlSource, xsltFileStreamSource, null);

		File expectedOutputFile = new File(expectedOutputFileClasspath);
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);
		
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));	
		
		return detailedDiff;		
	}		
	
}


