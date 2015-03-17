package org.ojbc.web.model.subscription.response.common;



public abstract class SubscriptionFaultResponse implements SubscriptionResponse{
	
	private String timestamp;

	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
