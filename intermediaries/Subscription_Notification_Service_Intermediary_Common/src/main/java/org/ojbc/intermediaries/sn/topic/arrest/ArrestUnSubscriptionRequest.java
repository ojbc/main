package org.ojbc.intermediaries.sn.topic.arrest;

import java.util.HashMap;
import java.util.List;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Message;

public class ArrestUnSubscriptionRequest extends UnSubscriptionRequest {

	public ArrestUnSubscriptionRequest(Message message) throws Exception {
		super(message);

		String sid = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
	
		buildSubjectIdMap(sid);
	}
	
	public ArrestUnSubscriptionRequest(String topic, List<String> emailAddresses, String systemName, String subscriptionQualifier, String subjectIdentifier) {
		
		super(topic, emailAddresses, systemName, subscriptionQualifier);
		
		buildSubjectIdMap(subjectIdentifier);
	}

	private void buildSubjectIdMap(String sid) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.SID, sid);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, subscriptionQualifier);
	}

}
