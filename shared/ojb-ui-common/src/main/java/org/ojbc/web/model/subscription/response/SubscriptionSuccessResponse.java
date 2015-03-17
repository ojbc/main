package org.ojbc.web.model.subscription.response;

import org.ojbc.web.model.subscription.response.common.SubscriptionResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;

public class SubscriptionSuccessResponse implements SubscriptionResponse{
	
	private String subReferenceAddress;
	
	private String subReferenceCurrentTime;
	
	private boolean subscriptionCreatedIndicator;
	
	public SubscriptionResponseType getResponseType() {
	
		return SubscriptionResponseType.SUBSCRIPTION_SUCCESS;
	}

	public String getSubReferenceAddress() {
		return subReferenceAddress;
	}

	public String getSubReferenceCurrentTime() {
		return subReferenceCurrentTime;
	}

	public boolean isSubscriptionCreatedIndicator() {
		return subscriptionCreatedIndicator;
	}

	public void setSubReferenceAddress(String subReferenceAddress) {
		this.subReferenceAddress = subReferenceAddress;
	}

	public void setSubReferenceCurrentTime(String subReferenceCurrentTime) {
		this.subReferenceCurrentTime = subReferenceCurrentTime;
	}

	public void setSubscriptionCreatedIndicator(boolean subscriptionCreatedIndicator) {
		this.subscriptionCreatedIndicator = subscriptionCreatedIndicator;
	}
	
	
}
