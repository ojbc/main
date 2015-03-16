package org.ojbc.web;

import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.w3c.dom.Element;

public interface SubscriptionValidationInterface {
	
	public FaultableSoapResponse validate(String subscriptionIdentificationId, String topic, String federatedQueryID, 
			Element samlToken) throws Exception;

}
