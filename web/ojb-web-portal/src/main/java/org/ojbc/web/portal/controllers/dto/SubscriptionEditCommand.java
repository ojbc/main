package org.ojbc.web.portal.controllers.dto;

import org.ojbc.web.model.subscription.edit.SubscriptionEditRequest;

public class SubscriptionEditCommand {
	
	private SubscriptionEditRequest subscriptionEditRequest = new SubscriptionEditRequest();

	public SubscriptionEditRequest getSubscriptionEditRequest() {
		return subscriptionEditRequest;
	}

	public void setSubscriptionEditRequest(
			SubscriptionEditRequest subscriptionEditRequest) {
		this.subscriptionEditRequest = subscriptionEditRequest;
	}
	
}
