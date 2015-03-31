package org.ojbc.web.impl;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.ojbc.web.SubscriptionQueryInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SubscriptionQueryMockImpl implements SubscriptionQueryInterface{
		
	@SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(SubscriptionQueryMockImpl.class.getName());
	
	@Resource
	SearchResultConverter searchResultConverter;		
	
    @Override
    public String invokeRequest(DetailsRequest subscriptionQueryRequest, String federatedQueryID,
            Element samlToken) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

	
}
