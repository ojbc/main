package org.ojbc.intermediaries.sn.topic.incident;

import java.util.HashMap;
import java.util.Set;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Message;

public class IncidentSubscriptionRequest extends SubscriptionRequest {

	public IncidentSubscriptionRequest(Message message,
			String allowedEmailAddressPatterns) throws Exception{
		super(message, allowedEmailAddressPatterns);
				
		String firstName = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName");
		String lastName = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName");
		String dateOfBirth = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date");
		
	
		buildSubjectIdMap(firstName, lastName, dateOfBirth);
	}
	
	public IncidentSubscriptionRequest(String topic, String startDateString, String endDateString, Set<String> emailAddresses, String systemName, 
			String subjectName, String firstName, String lastName, String dateOfBirth, String subscriptionQualifier, String subjectIdentifier) {
		
		super(topic, startDateString, endDateString, emailAddresses, systemName, subjectName, subscriptionQualifier);
		
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
