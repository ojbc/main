package org.ojbc.web.model.subscription.response;

import org.ojbc.web.model.subscription.response.common.SubscriptionResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;


public class UnsubscriptionAccessDenialResponse implements SubscriptionResponse{

		
	private boolean accessDenialIndicator;
	
	private String accessDenyingSystemName;
	
	private String accessDenyingReason;	
	
	private String timestamp;
	
	
	public SubscriptionResponseType getResponseType() {
		
		return SubscriptionResponseType.UNSUBSCRIPTION_ACCESS_DENIAL;
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


	public String getTimestamp() {
		return timestamp;
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


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}	
	
}


