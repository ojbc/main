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
package org.ojbc.bundles.adapters.courtview.ecf;

import java.io.File;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XsltTransformer;

public class CourtviewEcfTransformTest {
			
	private XsltTransformer xsltTransformer;
	
	private static final Log log = LogFactory.getLog(CourtviewEcfTransformTest.class);
	
	@Before
	public void setup() throws ParserConfigurationException{

		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
		
		xsltTransformer = new XsltTransformer();		
	}	

	@Test
	public void courtCaseFilingReportTestSingleCharge() throws Exception{		
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xml/input/AK-CaseFilingDecisionReport-Bitlink-singleCharge.xml",
				"src/main/resources/xslt/CourtCaseToECF_Filing.xsl",
				"src/test/resources/xml/output/AK-CaseFilingDecisionReport-Bitlink-singleCharge.out.xml");
        
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());						
	}

	@Test
	public void courtCaseFilingReportTestMultipleCharge() throws Exception{		
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xml/input/AK-CaseFilingDecisionReport-Bitlink-multipleCharge.xml",
				"src/main/resources/xslt/CourtCaseToECF_Filing.xsl",
				"src/test/resources/xml/output/AK-CaseFilingDecisionReport-Bitlink-multipleCharge.out.xml");
        
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());						
	}
	
	private DetailedDiff runTransform(String inputFileClasspath, String xsltClasspath, String expectedOutputFileClasspath) throws Exception{

		File inputFile = new File(inputFileClasspath);		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = OJBUtils.createSaxSource(inputXml);
		
		File expectedOutputFile = new File(expectedOutputFileClasspath);
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File(xsltClasspath);
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
		
		log.debug("Transformed Result: " + actualTransformedResultXml);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));	
		
		return detailedDiff;		
	}
	
	
}
