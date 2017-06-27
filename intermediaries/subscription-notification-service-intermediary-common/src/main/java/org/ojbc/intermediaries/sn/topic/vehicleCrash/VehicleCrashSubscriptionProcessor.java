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
package org.ojbc.intermediaries.sn.topic.vehicleCrash;

import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

import org.apache.camel.Message;

public class VehicleCrashSubscriptionProcessor extends SubscriptionProcessor {

	@Override
	public SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		return new VehicleCrashSubscriptionRequest(msg, allowedEmailAddressPatterns);
	}

	@Override
	public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new VehicleCrashUnSubscriptionRequest(msg);
	}

	@Override
	protected String getTopic() {

		return "{http://ojbc.org/wsn/topics}:person/vehicleCrash";
	}

}
