package org.ojbc.web.impl;

import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SubscriptionSubscribeMockImpl implements SubscriptionInterface{

	@Override
	public FaultableSoapResponse subscribe(SubscriptionAddRequest subAddReq,
			String federatedQueryID, Element samlToken) throws Exception {
   
		throw new UnsupportedOperationException("Method not implemented yet");
	}

}
