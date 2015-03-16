package org.ojbc.web.model.subscription.response;

import org.ojbc.web.model.subscription.response.common.SubscriptionFaultResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;

public class SubscriptionInvalidSecurityTokenResponse extends SubscriptionFaultResponse{
			
	private boolean invalidSecurityTokenIndicator;
	
	private String invalidSecurityTokenDescription;	
		
	
	public SubscriptionResponseType getResponseType() {

		return SubscriptionResponseType.SUBSCRIPTION_INVALID_TOKEN;
	}

	public boolean isInvalidSecurityTokenIndicator() {
		return invalidSecurityTokenIndicator;
	}

	public String getInvalidSecurityTokenDescription() {
		return invalidSecurityTokenDescription;
	}

	public void setInvalidSecurityTokenIndicator(
			boolean invalidSecurityTokenIndicator) {
		this.invalidSecurityTokenIndicator = invalidSecurityTokenIndicator;
	}

	public void setInvalidSecurityTokenDescription(
			String invalidSecurityTokenDescription) {
		this.invalidSecurityTokenDescription = invalidSecurityTokenDescription;
	}
	
}
