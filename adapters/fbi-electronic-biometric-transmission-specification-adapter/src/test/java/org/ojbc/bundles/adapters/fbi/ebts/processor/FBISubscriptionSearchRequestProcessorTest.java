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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.bundles.adapters.fbi.ebts.FBISubscriptionSearchRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FBISubscriptionSearchRequestProcessorTest {
	
	@Before
	public void setup(){		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
	}	
	
	@Test
	public void createMessageTest() throws Exception{
		
		FBISubscriptionSearchRequestProcessor fbiSubSearchReqProcessor = new FBISubscriptionSearchRequestProcessor();
		
		FBISubscriptionSearchRequest fbiSubscriptionSearchRequest = new FBISubscriptionSearchRequest();
		
		fbiSubscriptionSearchRequest.setCriminalSubscriptionReasonCode("CI");
		fbiSubscriptionSearchRequest.setPersonStateFingerprintId("A1234567");
		
		Document doc = fbiSubSearchReqProcessor.createFBISubscriptionSearchRequestDoc(fbiSubscriptionSearchRequest);
		
		String sFbiSubSearchReq = XmlUtils.getStringFromNode(doc);
		
		XmlUtils.printNode(doc);	
				
		//assert the transformed xml against expected xml output doc				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/FBISubscriptionSearchRequest.xml"));
							
		Diff diff = XMLUnit.compareXML(expectedXmlString, sFbiSubSearchReq);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());		
	}
	
}


