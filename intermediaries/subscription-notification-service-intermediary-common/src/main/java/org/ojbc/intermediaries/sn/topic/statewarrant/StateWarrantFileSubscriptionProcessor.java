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
package org.ojbc.intermediaries.sn.topic.statewarrant;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

public class StateWarrantFileSubscriptionProcessor extends SubscriptionProcessor{
	private static final String WARRANT_FILE_TOPIC = "{http://ojbc.org/wsn/topics}:person/nletsWarrant";
	
	@Override
	protected SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new StateWarrantFileSubscriptionRequest(msg, allowedEmailAddressPatterns);
	}
	
	@Override
	protected UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new StateWarrantFileUnsubscriptionRequest(msg);
	}

	@Override
	protected String getTopic() {

		return WARRANT_FILE_TOPIC;
	}
}
