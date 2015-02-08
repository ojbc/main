package org.ojbc.intermediaries.sn.topic.arrest;

import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

import org.apache.camel.Message;

public class ArrestSubscriptionProcessor extends SubscriptionProcessor {

	@Override
	public SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		return new ArrestSubscriptionRequest(msg,allowedEmailAddressPatterns);
	}

	@Override
	public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		return new ArrestUnSubscriptionRequest(msg);
	}

	@Override
	protected String getTopic() {
		
		return "{http://ojbc.org/wsn/topics}:person/arrest";
	}

}
