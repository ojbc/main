package org.ojbc.web.model.subscription.response;

import org.ojbc.web.model.subscription.response.common.SubscriptionFaultResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;

public class SubscriptionAccessDenialResponse extends SubscriptionFaultResponse {
		
	private boolean accessDenialIndicator;
	
	private String accessDenyingSystemName;
	
	private String accessDenyingReason;

	
	public SubscriptionResponseType getResponseType() {

		return SubscriptionResponseType.SUBSCRIPTION_ACCESS_DENIAL;
	}


	public boolean isAccessDenialIndicator() {
		return accessDenialIndicator;
	}


	public String getAccessDenyingSystemName() {
		return accessDenyingSystemName;
	}


	public String getAccessDenyingReason() {
		return accessDenyingReason;
	}

	public void setAccessDenialIndicator(boolean accessDenialIndicator) {
		this.accessDenialIndicator = accessDenialIndicator;
	}


	public void setAccessDenyingSystemName(String accessDenyingSystemName) {
		this.accessDenyingSystemName = accessDenyingSystemName;
	}


	public void setAccessDenyingReason(String accessDenyingReason) {
		this.accessDenyingReason = accessDenyingReason;
	}
	
}


