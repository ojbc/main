package org.ojbc.web.model.subscription.response;

import org.ojbc.web.model.subscription.response.common.SubscriptionFaultResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;

public class SubscriptionRequestErrorResponse extends SubscriptionFaultResponse{
		
	private String requestErrorTxt;
	
	private String requestErrorSystemName;	
		
	public SubscriptionResponseType getResponseType() {

		return SubscriptionResponseType.SUBSCRIPTION_REQUEST_ERROR;
	}


	public String getRequestErrorTxt() {
		return requestErrorTxt;
	}


	public String getRequestErrorSystemName() {
		return requestErrorSystemName;
	}

	public void setRequestErrorTxt(String requestErrorTxt) {
		this.requestErrorTxt = requestErrorTxt;
	}


	public void setRequestErrorSystemName(String requestErrorSystemName) {
		this.requestErrorSystemName = requestErrorSystemName;
	}
		
}

