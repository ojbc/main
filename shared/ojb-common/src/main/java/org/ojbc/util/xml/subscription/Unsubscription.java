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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.util.xml.subscription;

public class Unsubscription {
		       
	private String subscriptionId;
	
	private String topic;
	
	private String reasonCode;	 
	
	public Unsubscription(String subscriptionId, String topic, String reasonCode) {
		this.subscriptionId = subscriptionId;
		this.topic = topic;
		this.reasonCode = reasonCode;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public String getTopic() {
		return topic;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	@Override
	public String toString() {
		return "Unsubscription [subscriptionId=" + subscriptionId + ", topic="
				+ topic + ", reasonCode=" + reasonCode + "]";
	}				

}
