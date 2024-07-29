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
package org.ojbc.intermediaries.sn.topic.chcycle;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

public class ChCycleUnsubscriptionRequest extends UnSubscriptionRequest {

	public ChCycleUnsubscriptionRequest(Message message) throws Exception{
		super(message);
		String firstName = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName");
		String lastName = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName");
		String dateOfBirth = XmlUtils.xPathStringSearch(document,"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date");
		
		buildSubjectIdMap(firstName, lastName, dateOfBirth);
	}

	public ChCycleUnsubscriptionRequest(String topic, List<String> emailAddresses, String systemName, String subscriptionQualifier, 
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
