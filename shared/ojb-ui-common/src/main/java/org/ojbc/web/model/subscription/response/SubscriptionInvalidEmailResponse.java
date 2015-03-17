package org.ojbc.web.model.subscription.response;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.web.model.subscription.response.common.SubscriptionFaultResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;

public class SubscriptionInvalidEmailResponse extends SubscriptionFaultResponse{

	
	private List<String> invalidEmailList = new ArrayList<String>();	
		
	
	public SubscriptionResponseType getResponseType() {

		return SubscriptionResponseType.SUBSCRIPTION_INVALID_EMAIL;
	}

	public List<String> getInvalidEmailList() {
		return invalidEmailList;
	}

	public void setInvalidEmailList(List<String> invalidEmailList) {
		this.invalidEmailList = invalidEmailList;
	}

	
}
