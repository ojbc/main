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
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.apache.cxf.helpers.XMLUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.ojbc.bundles.adapters.fbi.ebts.FbiSubscriptionSearchResult;
import org.w3c.dom.Document;


public class FbiSubSearchResultsProcessorTest {
		
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void testFbiSubSearchResults() throws Exception{
		
		FbiSubSearchResultsProcessor fbiSubSearchResultsProcessor = new FbiSubSearchResultsProcessor();		
		
		Document fbiSubResultDoc = XMLUtils.parse(new File("src/test/resources/input/SubscriptionSearchResults-with-FBI-Association.xml"));
				
		FbiSubscriptionSearchResult fbiSubResult = fbiSubSearchResultsProcessor.processFbiSubResults(fbiSubResultDoc);
				
		Date startDate = fbiSubResult.getStartDate();		
		String sStartDate = sdf.format(startDate);		
		Assert.assertEquals("2015-01-01", sStartDate);
		
		Date endDate = fbiSubResult.getEndDate();		
		String sEndDate = sdf.format(endDate);
		Assert.assertEquals("2016-01-01", sEndDate);
		
		String fbiId = fbiSubResult.getFbiId();
		Assert.assertEquals("1234567", fbiId);
		
		String reasonCode = fbiSubResult.getReasonCode();
		Assert.assertEquals("CI", reasonCode);
		
		String termDuration = fbiSubResult.getTermDuration();
		Assert.assertEquals("P1Y", termDuration);
	}

}
