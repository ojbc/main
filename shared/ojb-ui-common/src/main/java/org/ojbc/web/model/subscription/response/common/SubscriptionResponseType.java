package org.ojbc.web.model.subscription.response.common;

public enum SubscriptionResponseType {
	
	SUBSCRIPTION_SUCCESS("Subscription Success"),
	SUBSCRIPTION_ACCESS_DENIAL("Access Denied"),
	SUBSCRIPTION_INVALID_EMAIL("Invalid Email"),
	SUBSCRIPTION_REQUEST_ERROR("Request Error"),		
	SUBSCRIPTION_INVALID_TOKEN("Invalid SAML token"),
	UNSUBSCRIPTION_ACCESS_DENIAL("Unsubscription Access Denied");	
	
	private String responseMessage;
	
	private SubscriptionResponseType(String responseMessage){
		
		this.responseMessage = responseMessage;		
	}
	
	public String getResponseMessage(){
		
		return responseMessage;
	}
	
	@Override
	public String toString() {
		
		return getResponseMessage();
	}
		
}

