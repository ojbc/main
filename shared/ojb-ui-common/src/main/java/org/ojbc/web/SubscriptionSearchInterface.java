package org.ojbc.web;

import org.w3c.dom.Element;

public interface SubscriptionSearchInterface {

	public String invokeSubscriptionSearchRequest(String federatedQueryID,
			Element samlToken) throws Exception;

}
