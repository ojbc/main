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
package org.ojbc.web.impl;

import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.SubscriptionSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SubscriptionSearchMockImpl implements SubscriptionSearchInterface{
		
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Resource
	SearchResultConverter searchResultConverter;		
	

	@Override
	public String invokeSubscriptionSearchRequest(String federatedQueryID,
			Element samlToken) throws Exception {

		logger.info("in invokeSubscriptionSearchRequest()");
		
		InputStream subResultsIs = getClass().getResourceAsStream("/sampleResponses/subscriptions/subscriptionSearchResult.xml");	
		
		String staticSubResults = WebUtils.returnStringFromFilePath(subResultsIs);				
		
		String subscriptionContent = searchResultConverter.convertDetailSearchResult(staticSubResults, "SubscriptionResults", null);			

		return subscriptionContent;
	}

	
}
