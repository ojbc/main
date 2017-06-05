/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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

