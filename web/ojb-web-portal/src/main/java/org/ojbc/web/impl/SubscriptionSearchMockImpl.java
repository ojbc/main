package org.ojbc.web.impl;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.ojbc.web.SubscriptionSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SubscriptionSearchMockImpl implements SubscriptionSearchInterface{
		
	private Logger logger = Logger.getLogger(SubscriptionSearchMockImpl.class.getName());
	
	@Resource
	SearchResultConverter searchResultConverter;		
	

	@Override
	public String invokeSubscriptionSearchRequest(String federatedQueryID,
			Element samlToken) throws Exception {

		logger.info("in invokeSubscriptionSearchRequest()");
		
		InputStream subResultsIs = getClass().getResourceAsStream("/sampleResponses/subscriptions/subscriptionSearchResult.xml");	
		
		String staticSubResults = WebUtils.returnStringFromFilePath(subResultsIs);				
		
		String subscriptionContent = searchResultConverter.convertDetailSearchResult(staticSubResults, "SubscriptionResults");			

		return subscriptionContent;
	}

	
}
