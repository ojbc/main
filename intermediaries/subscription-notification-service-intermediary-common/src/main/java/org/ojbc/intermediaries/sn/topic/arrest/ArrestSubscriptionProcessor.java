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
package org.ojbc.intermediaries.sn.topic.arrest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;
import org.ojbc.intermediaries.sn.util.SubjectIdentifierUtils;

public class ArrestSubscriptionProcessor extends SubscriptionProcessor {

	List<String> activeSubjectIdentifiers = Arrays.asList(SubscriptionNotificationConstants.SID, 
			SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER,
			SubscriptionNotificationConstants.FIRST_NAME, 
			SubscriptionNotificationConstants.LAST_NAME);
	
	@Override
	public SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		ArrestSubscriptionRequest arrestSubscriptionRequest = new ArrestSubscriptionRequest(msg,allowedEmailAddressPatterns);
		
		Map<String, String> allowedSubjectIdentifiers = arrestSubscriptionRequest.getSubjectIdentifiers();

		Map<String, String> finalSubjectIdentifiers = SubjectIdentifierUtils.returnFinalSubjectIdentifiers(allowedSubjectIdentifiers, activeSubjectIdentifiers);	
		arrestSubscriptionRequest.setSubjectIdentifiers(finalSubjectIdentifiers);
		
		return arrestSubscriptionRequest;
	}

	@Override
	public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		UnSubscriptionRequest unSubscriptionRequest = new ArrestUnSubscriptionRequest(msg);
		
		Map<String, String> allowedSubjectIdentifiers = unSubscriptionRequest.getSubjectIdentifiers();
		
		Map<String, String> finalSubjectIdentifiers = SubjectIdentifierUtils.returnFinalSubjectIdentifiers(allowedSubjectIdentifiers, activeSubjectIdentifiers);	
		unSubscriptionRequest.setSubjectIdentifiers(finalSubjectIdentifiers);

		return unSubscriptionRequest;
	}

	@Override
	protected String getTopic() {
		
		return "{http://ojbc.org/wsn/topics}:person/arrest";
	}

	public List<String> getActiveSubjectIdentifiers() {
		return activeSubjectIdentifiers;
	}

	public void setActiveSubjectIdentifiers(List<String> activeSubjectIdentifiers) {
		this.activeSubjectIdentifiers = activeSubjectIdentifiers;
	}

}
