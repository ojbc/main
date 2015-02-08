package org.ojbc.intermediaries.sn.topic.chcycle;

import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

import org.apache.camel.Message;

public class ChCycleSubscriptionProcessor extends SubscriptionProcessor{
	
	private static final String CH_CYCLE_TOPIC = "{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment";

	@Override
	protected SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new ChCycleSubscriptionRequest(msg, allowedEmailAddressPatterns);
	}
	
	@Override
	protected UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new ChCycleUnsubscriptionRequest(msg);
	}

	@Override
	protected String getTopic() {

		return CH_CYCLE_TOPIC;
	}

}
