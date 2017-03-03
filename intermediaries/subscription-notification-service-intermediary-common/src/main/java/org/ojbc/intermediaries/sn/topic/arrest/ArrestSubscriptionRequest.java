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

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ArrestSubscriptionRequest extends SubscriptionRequest {

	public ArrestSubscriptionRequest(Message message,
			String allowedEmailAddressPatterns) throws Exception{
		
		super(message, allowedEmailAddressPatterns);

		final Log log = LogFactory.getLog(ArrestSubscriptionRequest.class);
		
		String sid = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		String fbiNumber = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
		
		String firstName = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonGivenName");
		String lastName = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonName/nc:PersonSurName");
		String dateOfBirth = XmlUtils.xPathStringSearch(document,"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/nc:PersonBirthDate/nc:Date");
		
		buildSubjectIdMap(sid, firstName, lastName, dateOfBirth, fbiNumber);
		
		subscriptionProperties = new HashMap<String, String>();
		
		NodeList triggeringEvents = XmlUtils.xPathNodeListSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:TriggeringEvents/submsg-ext:FederalTriggeringEventCode");
		
	    if (triggeringEvents != null) {
	        int length = triggeringEvents.getLength();
	        for (int i = 0; i < length; i++) {
	            if (triggeringEvents.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element triggeringEventCode = (Element) triggeringEvents.item(i);
	                
	                log.info("Triggering Event Code: " + triggeringEventCode.getTextContent());
	                
	                //Determine if there is a better key value to use, This is used to make it unique
	                subscriptionProperties.put(FederalTriggeringEventCode.valueOf(triggeringEventCode.getTextContent().replace("-", "_")).toString(), triggeringEventCode.getTextContent());
	            }
	        }
	    }    
	    
	    String federalRapSheetDisclosureIndicator = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureIndicator");
	    
	    if (StringUtils.isNotBlank(federalRapSheetDisclosureIndicator))
	    {
	    	subscriptionProperties.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR, federalRapSheetDisclosureIndicator);
	    }	
	    
	    String federalRapSheetDisclosureAttentionDesignationText = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureAttentionDesignationText");
	    
	    if (StringUtils.isNotBlank(federalRapSheetDisclosureAttentionDesignationText))
	    {
	    	subscriptionProperties.put(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT, federalRapSheetDisclosureAttentionDesignationText);
	    }	
	    
	    if (StringUtils.isNotBlank(getAgencyCaseNumber()))
	    {
	    	subscriptionProperties.put(SubscriptionNotificationConstants.AGENCY_CASE_NUMBER, getAgencyCaseNumber());
	    }	
	    
	}

	private void buildSubjectIdMap(String sid,String firstName, String lastName, String dateOfBirth, String fbiNumber) {
		subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put(SubscriptionNotificationConstants.SID, sid);
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, firstName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, lastName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, subscriptionQualifier);
		
		if (StringUtils.isNotEmpty(fbiNumber))
		{
			subjectIdentifiers.put(SubscriptionNotificationConstants.FBI_ID, fbiNumber);
		}	
		
	}

}
