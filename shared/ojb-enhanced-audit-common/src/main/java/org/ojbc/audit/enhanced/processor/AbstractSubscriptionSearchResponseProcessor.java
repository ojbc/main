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
package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.SubscriptionSearchResult;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public abstract class AbstractSubscriptionSearchResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractSubscriptionSearchResponseProcessor.class);
	
	public abstract void auditSubscriptionSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	SubscriptionSearchResult processSubscriptionSearchResponse(Document document) throws Exception
	{
		SubscriptionSearchResult subscriptionSearchResult = new SubscriptionSearchResult();

		NodeList searchResults = XmlUtils.xPathNodeListSearch(document, "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionSearchResult");
		
		String searchResultsCountString = XmlUtils.xPathStringSearch(document, "/ssr:SubscriptionSearchResults/srm:SearchResultsMetadata/srm:TotalAuthorizedSearchResultsQuantity");
		
		if (StringUtils.isNotBlank(searchResultsCountString))
		{
			Integer searchResultsCount = Integer.valueOf(searchResultsCountString);
			subscriptionSearchResult.setSearchResultsCount(searchResultsCount);	
		}	
		
		if (searchResults != null && searchResults.getLength() > 0)
		{
			Integer searchResultsCount = Integer.valueOf(searchResultsCountString);
			
			if (searchResultsCount > searchResults.getLength() )
			{
				subscriptionSearchResult.setSearchResultsErrorIndicator(true);
				subscriptionSearchResult.setSearchResultsErrorText("The search displayed " + searchResults.getLength() + " of " + searchResultsCount + " total results." );
			}
		}	
		
		//Error 
		
		String errorText = XmlUtils.xPathStringSearch(document, "/ssr:SubscriptionSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError/srer:ErrorText");
		
		if (StringUtils.isNotBlank(errorText))
		{
			subscriptionSearchResult.setSearchResultsErrorIndicator(true);
			subscriptionSearchResult.setSearchResultsErrorText(errorText);
		}	
		
		//Access Denied
		String accessDeniedText = XmlUtils.xPathStringSearch(document, "/ssr:SubscriptionSearchResults/srm:SearchResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
		
		if (StringUtils.isNotBlank(accessDeniedText))
		{
			subscriptionSearchResult.setSearchResultsAccessDeniedIndicator(true);
			subscriptionSearchResult.setSearchResultsErrorText(accessDeniedText);
		}	
		
        log.debug("Subscription Search Result: " + subscriptionSearchResult.toString());

        return subscriptionSearchResult;
	}

}
