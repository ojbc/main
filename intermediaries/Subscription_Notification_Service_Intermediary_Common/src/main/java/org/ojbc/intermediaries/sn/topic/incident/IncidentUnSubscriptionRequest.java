package org.ojbc.intermediaries.sn.topic.incident;

import java.util.HashMap;
import java.util.List;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Message;

public class IncidentUnSubscriptionRequest extends UnSubscriptionRequest {

	public IncidentUnSubscriptionRequest(Message message) throws Exception{
		super(message);
		
		String firstName = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName");
		String lastName = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName");
		String dateOfBirth = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date");

		
		buildSubjectIdMap(firstName, lastName, dateOfBirth);
	}

	public IncidentUnSubscriptionRequest(String topic, List<String> emailAddresses, String systemName, String subscriptionQualifier, 
			String firstName, String lastName, String dateOfBirth) {
		
		super(topic, emailAddresses, systemName, subscriptionQualifier);
		
		buildSubjectIdMap(firstName, lastName, dateOfBirth);
	}

	private void buildSubjectIdMap(String firstName, String lastName, String dateOfBirth) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, firstName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, lastName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, subscriptionQualifier);
	}

}
