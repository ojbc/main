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


