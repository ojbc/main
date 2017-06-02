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
package org.ojbc.bundles.mockimpl.fbi.ngi;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FbiNgiUserServiceProcessorTest {
			
	private static final String SAMPLE_INPUT_FILE= "src/test/resources/input/EBTS-RapBack-Criminal-Subscription-Request.xml";
	
	private static final String EXPECTED_OUTPUT_FILE  = "src/test/resources/output/FbiNgiUserServiceControlNumber.xml";
	
	@Test
	public void userServiceRequestTest() throws Exception{
				
		FbiNgiUserServiceProcessor userServiceProcessor = new FbiNgiUserServiceProcessor();
		
		Exchange sampleExchange = getSampleInputExchange(SAMPLE_INPUT_FILE);
		
		String controlNumResponseXml = userServiceProcessor.getControlNumResponseMessage(sampleExchange);
		
		//assert the transformed xml against expected xml output doc				
		String expectedXmlString = FileUtils.readFileToString(
				new File(EXPECTED_OUTPUT_FILE));
							
		Diff diff = XMLUnit.compareXML(expectedXmlString, controlNumResponseXml);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());		
	}

	
	private Exchange getSampleInputExchange(String inputFileClasPath) throws Exception{
		
		CamelContext sampleContext = new DefaultCamelContext();
		
		Exchange sampleExchange = new DefaultExchange(sampleContext);
		
		Document sampleFbiNgiSubDoc = XmlUtils.parseFileToDocument(new File(inputFileClasPath));		
		
		sampleExchange.getIn().setBody(sampleFbiNgiSubDoc);	
		
		return sampleExchange;
	}
	
}
