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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ojbc.bundles.adapters.fbi.ebts.FbiSubscriptionSearchResult;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FbiSubSearchResultsProcessor {
		
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
	public FbiSubscriptionSearchResult processFbiSubResults(Document fbiSubResultDoc) throws Exception{
								
		FbiSubscriptionSearchResult fbiSubResult = new FbiSubscriptionSearchResult();
				
		Node fbiSubNode = XmlUtils.xPathNodeSearch(fbiSubResultDoc, 
				"/ssr:SubscriptionSearchResults/ssr-ext:FBISubscription");
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(fbiSubNode, "nc:ActivityDateRange");
				
		String startDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");
						
		if(StringUtils.isNotEmpty(startDate)){			
			Date dStartDate = sdf.parse(startDate);			
			fbiSubResult.setStartDate(dStartDate);
		}
						
		String endDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate/nc:Date");
		
		if(StringUtils.isNotEmpty(endDate)){
			Date dEndDate = sdf.parse(endDate);
			fbiSubResult.setEndDate(dEndDate);
		}
		
		String fbiId = XmlUtils.xPathStringSearch(fbiSubNode, 
				"ssr-ext:SubscriptionFBIIdentification/nc:IdentificationID");
		
		if(StringUtils.isNotEmpty(fbiId)){
			fbiSubResult.setFbiId(fbiId);
		}
		
		String reasonCode = XmlUtils.xPathStringSearch(fbiSubNode, "ssr-ext:CriminalSubscriptionReasonCode");
		if(StringUtils.isNotEmpty(reasonCode)){
			fbiSubResult.setReasonCode(reasonCode);
		}
		
		String subTermDuration = XmlUtils.xPathStringSearch(fbiSubNode, 
				"ssr-ext:SubscriptionTerm/jxdm41:TermDuration");
		
		if(StringUtils.isNotEmpty(subTermDuration)){
			fbiSubResult.setTermDuration(subTermDuration);
		}
		
		return fbiSubResult;
	}

}
