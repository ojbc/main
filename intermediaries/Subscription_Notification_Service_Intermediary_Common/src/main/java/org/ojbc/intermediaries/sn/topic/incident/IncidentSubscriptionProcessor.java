package org.ojbc.intermediaries.sn.topic.incident;

import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

import org.apache.camel.Message;

public class IncidentSubscriptionProcessor extends SubscriptionProcessor {

	@Override
	public SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception{

		return new IncidentSubscriptionRequest(msg, allowedEmailAddressPatterns);
	}

	@Override
	public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new IncidentUnSubscriptionRequest(msg);
	}

	@Override
	protected String getTopic() {

		return "{http://ojbc.org/wsn/topics}:person/incident";
	}

}
