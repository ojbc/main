package org.ojbc.web.model.subscription.validation;

public class SubscriptionValidationResponse {
	
	private boolean subscriptionValidated;

	public boolean isSubscriptionValidated() {
		return subscriptionValidated;
	}

	public void setSubscriptionValidated(boolean subscriptionValidated) {
		this.subscriptionValidated = subscriptionValidated;
	}

	@Override
	public String toString() {
		return "SubscriptionValidationResponse [subscriptionValidated="
				+ subscriptionValidated + "]";
	}
	
}
