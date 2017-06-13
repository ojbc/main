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
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;



public class SubModifyProcessorTest {
		
	@Before
	public void init() {	
		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
		
	@Test
	public void testSubModifyProcessor() throws Exception{
		
		FbiEbtsResponseProcessor respProcessor = new FbiEbtsResponseProcessor();
		
		Document generatedModRespDoc = respProcessor.getModifyResponseDoc();
				
		Document expectedSubModRespDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/output/SubscriptionModifyResponse.xml"));
		
		Diff diff = new Diff(expectedSubModRespDoc, generatedModRespDoc);
		DetailedDiff detailDiff = new DetailedDiff(diff);
		
		@SuppressWarnings("unchecked")
		List<Difference> difList = detailDiff.getAllDifferences();
		int diffCount = difList == null ? 0 : difList.size();
		
		Assert.assertEquals(difList.toString(), 0, diffCount);
	}
	

}
