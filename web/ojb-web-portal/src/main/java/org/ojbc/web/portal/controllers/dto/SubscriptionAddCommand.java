package org.ojbc.web.portal.controllers.dto;

import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;

public class SubscriptionAddCommand {
	
	private SubscriptionAddRequest subscriptionAddRequest = new SubscriptionAddRequest();

	public SubscriptionAddRequest getSubscriptionAddRequest() {
		return subscriptionAddRequest;
	}

	public void setSubscriptionAddRequest(
			SubscriptionAddRequest subscriptionAddRequest) {
		this.subscriptionAddRequest = subscriptionAddRequest;
	}
		
}
