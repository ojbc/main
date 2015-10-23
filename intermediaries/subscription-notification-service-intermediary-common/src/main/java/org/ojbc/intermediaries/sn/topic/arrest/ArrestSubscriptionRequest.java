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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
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
			String subjectName, String subscriptionQualifier, String subjectIdentifier, String reasonCategoryCode) {
		
		super(topic, startDateString, endDateString, emailAddresses, systemName, subjectName, subscriptionQualifier);
		
		setReasonCategoryCode(reasonCategoryCode);
		
		buildSubjectIdMap(subjectIdentifier);
	}

	private void buildSubjectIdMap(String sid) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.SID, sid);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, subscriptionQualifier);
	}

}
