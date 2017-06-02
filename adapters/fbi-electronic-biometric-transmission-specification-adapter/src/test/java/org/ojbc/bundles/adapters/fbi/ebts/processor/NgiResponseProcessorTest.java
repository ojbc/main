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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NgiResponseProcessorTest {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(NgiResponseProcessorTest.class);
	
	@Before
	public void setup(){
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
	}
	
	@Test
	public void voidResponseTest() throws Exception{
		
		NgiResponseProcessor ngiResponseProcessor = new NgiResponseProcessor();
		
		String voidResponse = ngiResponseProcessor.getVoidResponse();
						
		//assert the transformed xml against expected xml output doc				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/NgiVoidResponse.xml"));
							
		Diff diff = XMLUnit.compareXML(expectedXmlString, voidResponse);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());
	}

}
