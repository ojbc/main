package org.ojbc.web;

import org.w3c.dom.Element;

public interface UnsubscriptionInterface {

	public void unsubscribe(String subscriptionIdentificationId, String topic, String federatedQueryID, 
			Element samlToken) throws Exception;
	
}
