package org.ojbc.web;

import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.w3c.dom.Element;

public interface SubscriptionInterface {

	public FaultableSoapResponse subscribe(SubscriptionAddRequest subAddReq, String federatedQueryID,
			Element samlToken) throws Exception;
	
}
