package org.ojbc.intermediaries.sn.topic.arrest;

import java.util.HashMap;
import java.util.Set;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Message;

public class ArrestSubscriptionRequest extends SubscriptionRequest {

	public ArrestSubscriptionRequest(Message message,
			String allowedEmailAddressPatterns) throws Exception{
		super(message, allowedEmailAddressPatterns);

		String sid = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		buildSubjectIdMap(sid);
	}
	
	public ArrestSubscriptionRequest(String topic, String startDateString, String endDateString, Set<String> emailAddresses, String systemName, 
			String subjectName, String subscriptionQualifier, String subjectIdentifier) {
		
		super(topic, startDateString, endDateString, emailAddresses, systemName, subjectName, subscriptionQualifier);
		
		buildSubjectIdMap(subjectIdentifier);
	}

	private void buildSubjectIdMap(String sid) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.SID, sid);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, subscriptionQualifier);
	}

}
