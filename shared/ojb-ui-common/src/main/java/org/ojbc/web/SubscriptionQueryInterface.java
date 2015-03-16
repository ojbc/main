package org.ojbc.web;

import org.ojbc.web.model.person.query.DetailsRequest;
import org.w3c.dom.Element;

public interface SubscriptionQueryInterface {
	
//	SubscriptionQueryRequestProcessor
	
	public String invokeRequest(DetailsRequest subscriptionQueryRequest, String federatedQueryID, 
			Element samlToken) throws Exception;

}
