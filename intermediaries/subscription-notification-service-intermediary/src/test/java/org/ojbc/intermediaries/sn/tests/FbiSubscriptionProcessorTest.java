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
package org.ojbc.intermediaries.sn.tests;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.FbiSubscription;
import org.ojbc.intermediaries.sn.FbiSubscriptionProcessor;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class FbiSubscriptionProcessorTest {
	
	private FbiSubscriptionProcessor fbiSubscriptionProcessor = new FbiSubscriptionProcessor();
	
	private FbiSubscription sampleFbiSubscription;
		
	@Before
	public void setup(){		
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
    	sampleFbiSubscription = getSampleFbiSubscription();
	}
		
	
	private FbiSubscription getSampleFbiSubscription(){
		
		FbiSubscription fbiSubscription = new FbiSubscription();
		
		Calendar startCal = Calendar.getInstance();
		startCal.set(2015, 0, 1);		
		fbiSubscription.setStartDate(startCal.getTime());
		
		Calendar endCal = Calendar.getInstance();
		endCal.set(2016, 0, 1);				
		fbiSubscription.setEndDate(endCal.getTime());
		
		fbiSubscription.setTermDuration("P1Y");		
		fbiSubscription.setFbiId("1234567");		
		fbiSubscription.setCrimSubReasonCode("CI");	
		
		return fbiSubscription;
	}
	
	@Test
	public void testFbiSubscriptionProcessor() throws Exception{
								
		Document arrestSubDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document.xml"));		
		
		String subXmlWithAppendedFbiData = fbiSubscriptionProcessor.appendFbiDataToSubscriptionDoc(arrestSubDoc, sampleFbiSubscription);						
												
		String expectedXml = XmlUtils.getRootNodeAsString(
				"src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document_WithFbiData.xml");
							
		Diff diff = new Diff(expectedXml, subXmlWithAppendedFbiData);						
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		List<Difference> diffList = detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		Assert.assertEquals(detailedDiff.toString(), 0, diffCount);
	}

}

