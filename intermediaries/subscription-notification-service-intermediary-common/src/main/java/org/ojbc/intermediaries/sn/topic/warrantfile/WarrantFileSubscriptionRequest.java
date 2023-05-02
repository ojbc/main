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
package org.ojbc.intermediaries.sn.topic.warrantfile;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;

public class WarrantFileSubscriptionRequest extends SubscriptionRequest{
	public WarrantFileSubscriptionRequest(Message message, String allowedEmailAddressPatterns) throws Exception {

		super(message, allowedEmailAddressPatterns);
		
		subscriptionProperties = new HashMap<String, String>();

		String firstName = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName");
		String lastName = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName");
		String dateOfBirth = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date");
		String gender = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:Sex");
		String race = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:Race");
		String fbiNum = XmlUtils.xPathStringSearch(document, "//jxdm41:PersonFBIIdentification/nc:IdentificationID");
		String sendingStatePO = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:SendingStatePO");
		String receivingStatePO = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:ReceivingStatePO");
		String offenderId = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonOtherIdentification/nc:IdentificationID");
		String sendingState = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:SendingState");
		String receivingState = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:ReceivingState");
		subscriptionProperties.put("OffenderID", offenderId);

		buildSubjectIdMap(firstName, lastName, dateOfBirth, gender, race, sendingStatePO, receivingStatePO, fbiNum, sendingState, receivingState);

		
	}
	
	private void buildSubjectIdMap(String firstName, String lastName, String dateOfBirth, String gender, String race, String sendingStatePO, String receivingStatePO, String fbiNum,
			String sendingState, String receivingState) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, firstName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, lastName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, getSubscriptionQualifier());
		subjectIdentifiers.put("sex", gender);
		subjectIdentifiers.put("race", race);
		if(StringUtils.isNotEmpty(fbiNum)) {
			subjectIdentifiers.put("fbiNum", fbiNum);
		}
		else { 
			subjectIdentifiers.put("fbiNum", "N/A");
		}
		subjectIdentifiers.put("sendingStatePO", sendingStatePO);
		subjectIdentifiers.put("receivingStatePO", receivingStatePO);
		subjectIdentifiers.put("sendingState", sendingState);
		subjectIdentifiers.put("receivingState", receivingState);
	}
}
