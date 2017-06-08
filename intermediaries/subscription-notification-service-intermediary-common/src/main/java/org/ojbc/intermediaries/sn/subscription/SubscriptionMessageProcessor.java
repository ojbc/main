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
package org.ojbc.intermediaries.sn.subscription;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;

public class SubscriptionMessageProcessor {

    public OjbcNamespaceContext ojbcNamespaceContext;
   
    public SubscriptionMessageProcessor(){
    	ojbcNamespaceContext = new OjbcNamespaceContext();
    }

    public void createSubscriptionTriggeringEvents(Element parent, String extensionSchema, Subscription subscription)
    {
//		<smext:TriggeringEvents>
//			<smext:FederalTriggeringEventCode>ARREST</smext:FederalTriggeringEventCode>
//		</smext:TriggeringEvents>
    	
    	Map<String, String> subscriptionProperties = subscription.getSubscriptionProperties();
    	
    	if (subscriptionProperties == null)
    	{
    		//No properties, just return
    		return;
    	}	
    		
    	boolean createWrapperElement = true;
    	
    	Element triggeringEventsElement = null;
    	
    	for (Map.Entry<String, String> entry : subscriptionProperties.entrySet()) {
    	    if (inFederalTriggeringEventCodeEnum(entry.getKey().replace("-", "_")))
    	    {

    	    	if (createWrapperElement)
    	    	{
    	    		createWrapperElement = false;
    	    		triggeringEventsElement = XmlUtils.appendElement(parent, extensionSchema, "TriggeringEvents");
    	    	}	
    	    	
    	    	Element federalTriggeringEventCode = XmlUtils.appendElement(triggeringEventsElement, extensionSchema, "FederalTriggeringEventCode");
    	    	federalTriggeringEventCode.setTextContent(entry.getValue());
    	    	
    	    }	
    	    
    	}
    }
    
    public void createFederalRapSheetDisclosure( Element root, String extensionSchema, Subscription subscription )
    {
//	<smext:FederalRapSheetDisclosure>
//		<smext:FederalRapSheetDisclosureIndicator>true</smext:FederalRapSheetDisclosureIndicator>
//		<smext:FederalRapSheetDisclosureAttentionDesignationText>Detective George Jones</smext:FederalRapSheetDisclosureAttentionDesignationText>
//	</smext:FederalRapSheetDisclosure>    	

    	Map<String, String> subscriptionProperties = subscription.getSubscriptionProperties();
    	
    	if (subscriptionProperties == null)
    	{
    		//No properties, just return
    		return;
    	}	
    	
    	String federalRapSheetDisclosureIndicator = "";
    	String federalRapSheetDisclosureAttentionDesignationText = "";

    	for (Map.Entry<String, String> entry : subscriptionProperties.entrySet()) {
    		
    		if (entry.getKey().equals(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR))
    		{
    			federalRapSheetDisclosureIndicator = entry.getValue();
    		}	

    		if (entry.getKey().equals(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT))
    		{
    			federalRapSheetDisclosureAttentionDesignationText = entry.getValue();
    		}	

    	}
    	
	    if (StringUtils.isNotBlank(federalRapSheetDisclosureIndicator) || StringUtils.isNotBlank(federalRapSheetDisclosureAttentionDesignationText))
	    {
	    	Element federalRapSheetDisclosureElement = XmlUtils.appendElement(root, extensionSchema, "FederalRapSheetDisclosure");
	    
	    	if (StringUtils.isNotBlank(federalRapSheetDisclosureIndicator))
	    	{		
	    		Element federalRapSheetDisclosureIndicatorElement = XmlUtils.appendElement(federalRapSheetDisclosureElement, extensionSchema, "FederalRapSheetDisclosureIndicator");
	    		federalRapSheetDisclosureIndicatorElement.setTextContent(federalRapSheetDisclosureIndicator);
	    	}	

	    	if (StringUtils.isNotBlank(federalRapSheetDisclosureAttentionDesignationText))
	    	{		
	    		Element federalRapSheetDisclosureAttentionDesignationTextElement = XmlUtils.appendElement(federalRapSheetDisclosureElement, extensionSchema, "FederalRapSheetDisclosureAttentionDesignationText");
	    		federalRapSheetDisclosureAttentionDesignationTextElement.setTextContent(federalRapSheetDisclosureAttentionDesignationText);
	    	}	

	    }	
    }    
    
    public boolean inFederalTriggeringEventCodeEnum(String test) {

        for (FederalTriggeringEventCode c : FederalTriggeringEventCode.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
    
	public void appendSubscriptionRelatedCaseIdentification(Element parent, String extensionSchema,  Subscription subscription) {
		if (StringUtils.isNotBlank(subscription.getAgencyCaseNumber()))
        {
            Element subscriptionRelatedCaseIdentification = XmlUtils.appendElement(parent, extensionSchema, "SubscriptionRelatedCaseIdentification");
            
            Element identificationIDElement = XmlUtils.appendElement(subscriptionRelatedCaseIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
            identificationIDElement.setTextContent(subscription.getAgencyCaseNumber());
        }
	}

	public void appendPersonInfo( Element parent, Subscription subscription) {
		String dateOfBirth = subscription.getDateOfBirth();

		if (StringUtils.isNotBlank(dateOfBirth)) {
		    Element personDOBElement = XmlUtils.appendElement(parent, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
		    Element personDOBDateElement = XmlUtils.appendElement(personDOBElement, OjbcNamespaceContext.NS_NC, "Date");
		    personDOBDateElement.setTextContent(dateOfBirth);
		}

		Element personNameElement = XmlUtils.appendElement(parent, OjbcNamespaceContext.NS_NC, "PersonName");

		// We set either person full name or first/last name

		if (StringUtils.isNotBlank(subscription.getPersonFullName())) {
		    Element personFullNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonFullName");
		    personFullNameElement.setTextContent(subscription.getPersonFullName());
		}

		if (StringUtils.isNotBlank(subscription.getPersonFirstName()) && StringUtils.isNotBlank(subscription.getPersonLastName())) {
		    Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
		    personGivenNameElement.setTextContent(subscription.getPersonFirstName());

		    Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
		    personSurNameElement.setTextContent(subscription.getPersonLastName());
		}

		String sid = subscription.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.SID);
		String fbiNumber = subscription.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.FBI_ID);
		

		if (StringUtils.isNotBlank(sid) || StringUtils.isNotBlank(fbiNumber)) {
		    Element personAugmentationElement = XmlUtils.appendElement(parent, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");

		    if (StringUtils.isNotBlank(fbiNumber))
		    {		
		        Element personFBIIdentification = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");

		        Element identificationIDElement = XmlUtils.appendElement(personFBIIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
		        identificationIDElement.setTextContent(fbiNumber);
		    }   
		    
		    if (StringUtils.isNotBlank(sid))
		    {		
		        Element personStateFingerprintIdentification = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");

		        Element identificationIDElement = XmlUtils.appendElement(personStateFingerprintIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
		        identificationIDElement.setTextContent(sid);
		    }    

		}
	}

}
