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
package org.ojbc.intermediaries.sn.subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SubscriptionSearchQueryProcessor {

    private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();

    /**
     * Convert the POJO to the equivalent XML document
     */
    public Document buildSubscriptionQueryResponseDoc(Subscription subscriptionSearchResponse) throws Exception {

        Document doc = null;

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS, "SubscriptionQueryResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS);

        Element subscriptionSearchResultElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT, "SubscriptionQueryResult");

        appendSubscriptionParentResponse(subscriptionSearchResponse, doc, subscriptionSearchResultElement, 0, 
        		OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);

        List<Subscription> searchResponse = new ArrayList<Subscription>();
        searchResponse.add(subscriptionSearchResponse);

        createSubscriptionSubjects(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscriptionEmails(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscribedEntityContactInformationAssociations(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);

        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
    }

    private static void createSubscriptionTriggeringEvents(Subscription subscription, Element root, String extensionSchema)
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
    	    		triggeringEventsElement = XmlUtils.appendElement(root, extensionSchema, "TriggeringEvents");
    	    	}	
    	    	
    	    	Element federalTriggeringEventCode = XmlUtils.appendElement(triggeringEventsElement, extensionSchema, "FederalTriggeringEventCode");
    	    	federalTriggeringEventCode.setTextContent(entry.getValue());
    	    	
    	    }	
    	    
    	}
    }
    
    private static void createFederalRapSheetDisclosure(Subscription subscription, Element root, String extensionSchema)
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
    
    private static boolean inFederalTriggeringEventCodeEnum(String test) {

        for (FederalTriggeringEventCode c : FederalTriggeringEventCode.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Convert the list of POJOs to the equivalent XML document
     */
    public Document buildSubscriptionSearchResponseDoc(List<Subscription> subscriptionSearchResponseList) throws Exception {

        Document doc = null;

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS, "SubscriptionSearchResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS);

        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Element subscriptionSearchResultElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, "SubscriptionSearchResult");
            int index = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;

            appendSubscriptionParentResponse(subscriptionSearchResponse, doc, subscriptionSearchResultElement, index, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT
                    );

        }

        createFbiSubscriptions(subscriptionSearchResponseList, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionSubjects(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionEmails(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscribedEntityContactInformationAssociations(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createStateSubscriptionFBISubscriptionAssociation(subscriptionSearchResponseList, root);
        
        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
        
    }

    private void createStateSubscriptionFBISubscriptionAssociation(
			List<Subscription> subscriptionList, Element root) {
    	
		for (Subscription subscription : subscriptionList){
			FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
			
			if (fbiRapbackSubscription != null ){
				int subscriptionIndex = subscriptionList.indexOf(subscription) + 1;
				String subscriptionId = "S"+ StringUtils.leftPad(String.valueOf(subscriptionIndex), 3, '0'); 
				String fbiSubscriptionId = "FBI" + StringUtils.leftPad(String.valueOf(subscriptionIndex), 3, '0'); 
				
				Element stateSubscriptionFBISubscriptionAssociation = XmlUtils.appendElement(root, 
						OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, "StateSubscriptionFBISubscriptionAssociation");
				
				Element stateSubscriptionReference = XmlUtils.appendElement(stateSubscriptionFBISubscriptionAssociation, 
						OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, "StateSubscriptionReference");
				XmlUtils.addAttribute(stateSubscriptionReference, OjbcNamespaceContext.NS_STRUCTURES, "ref", subscriptionId);
				
				Element fbiSubscriptionReference = XmlUtils.appendElement(stateSubscriptionFBISubscriptionAssociation, 
						OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, "FBISubscriptionReference");
				XmlUtils.addAttribute(fbiSubscriptionReference, OjbcNamespaceContext.NS_STRUCTURES, "ref", fbiSubscriptionId);
			}
		}
	}

	private void createFbiSubscriptions( List<Subscription> subscriptionList,
			Element root, String nsSubscriptionSearchResultsExt) {
		for (Subscription subscription : subscriptionList){
			
			int subscriptionIndex = subscriptionList.indexOf(subscription) + 1;
			
			if (subscription.getFbiRapbackSubscription() != null){
				
				FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
				
		        Element fbiSubscriptionElement = XmlUtils.appendElement(root, nsSubscriptionSearchResultsExt, "FBISubscription");
		        XmlUtils.addAttribute(fbiSubscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", 
		        		"FBI" + StringUtils.leftPad(String.valueOf(subscriptionIndex), 3, '0'));
		        
		        appendActivityDateRangeElement(fbiRapbackSubscription.getRapbackStartDate(), 
		        		fbiRapbackSubscription.getRapbackExpirationDate(), 
		        		fbiSubscriptionElement); 
		        
		        Element subscriptionFBIIdentification = XmlUtils.appendElement(fbiSubscriptionElement, 
		        		nsSubscriptionSearchResultsExt, "SubscriptionFBIIdentification");
		        Element identificationId = XmlUtils.appendElement(subscriptionFBIIdentification, 
		        		OjbcNamespaceContext.NS_NC, "IdentificationID");
		        identificationId.setTextContent(fbiRapbackSubscription.getFbiSubscriptionId());

		        Element criminalSubscriptionReasonCode = XmlUtils.appendElement(fbiSubscriptionElement, 
		        		nsSubscriptionSearchResultsExt, "CriminalSubscriptionReasonCode");
		        criminalSubscriptionReasonCode.setTextContent(fbiRapbackSubscription.getRapbackCategory());
		        
		        Element rapBackSubscriptionTermCode = XmlUtils.appendElement(fbiSubscriptionElement, 
		        		nsSubscriptionSearchResultsExt, "RapBackSubscriptionTermCode");
		        rapBackSubscriptionTermCode.setTextContent(fbiRapbackSubscription.getSubscriptionTerm());

		        Element rapBackActivityNotificationFormatCode = XmlUtils.appendElement(fbiSubscriptionElement, 
		        		nsSubscriptionSearchResultsExt, "RapBackActivityNotificationFormatCode");
		        rapBackActivityNotificationFormatCode.setTextContent(fbiRapbackSubscription.getRapbackActivityNotificationFormat());
		        
		        if (fbiRapbackSubscription.getRapbackOptOutInState() != null){
			        Element rapBackInStateOptOutIndicator = XmlUtils.appendElement(fbiSubscriptionElement, 
			        		nsSubscriptionSearchResultsExt, "RapBackInStateOptOutIndicator");
			        rapBackInStateOptOutIndicator.setTextContent(
			        		BooleanUtils.toStringTrueFalse(fbiRapbackSubscription.getRapbackOptOutInState()));
		        }
		        
			}
		}
		
	}



	private static final String SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME = "Subscriptions";

    private static Element appendSubscriptionParentResponse(Subscription subscriptionSearchResponse, Document doc, 
    		Element subscriptionSearchResultElement, int searchResponseIndex, String extensionSchema) {

        Element subscriptionElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "Subscription");
        XmlUtils.addAttribute(subscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "S" + StringUtils.leftPad(String.valueOf(searchResponseIndex), 3, '0'));

        appendActivityDateRangeElement(subscriptionSearchResponse.getStartDate(), subscriptionSearchResponse.getEndDate(), 
        		subscriptionElement);    
        
//		<sqr-ext:SubscriptionRelatedCaseIdentification>
//			<nc:IdentificationID>0123ABC</nc:IdentificationID>
//		</sqr-ext:SubscriptionRelatedCaseIdentification>
        if (StringUtils.isNotBlank(subscriptionSearchResponse.getAgencyCaseNumber()))
        {
            Element subscriptionRelatedCaseIdentification = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionRelatedCaseIdentification");
            
            Element identificationIDElement = XmlUtils.appendElement(subscriptionRelatedCaseIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
            identificationIDElement.setTextContent(subscriptionSearchResponse.getAgencyCaseNumber());
        }	
        
        Element subscriptionSubjectElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionSubject");

        Element roleOfPersonReferenceElement = XmlUtils.appendElement(subscriptionSubjectElement, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(roleOfPersonReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "P" + searchResponseIndex);

        Element subscriptionTopicElement = XmlUtils.appendElement(subscriptionElement, OjbcNamespaceContext.NS_WSN_BROKERED, "Topic");
        subscriptionTopicElement.setAttribute("Dialect", "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete");
        subscriptionTopicElement.setTextContent(subscriptionSearchResponse.getTopic());

        Element subscribedEntityElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscribedEntity");
        XmlUtils.addAttribute(subscribedEntityElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "SE" + searchResponseIndex);

        Element subscriptionOriginatorElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionOriginator");

        Element subscriptionOriginatorIdentificationElement = XmlUtils.appendElement(subscriptionOriginatorElement, extensionSchema, "SubscriptionOriginatorIdentification");

        Element identificationIDElement = XmlUtils.appendElement(subscriptionOriginatorIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        identificationIDElement.setTextContent(subscriptionSearchResponse.getSubscriptionOwner());

        Element sourceSystemNameTextElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "SourceSystemNameText");
        sourceSystemNameTextElement.setTextContent(subscriptionSearchResponse.getSubscribingSystemIdentifier());

        Element systemIdentifierElement = XmlUtils.appendElement(subscriptionSearchResultElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");

        Element sysIdentificationIDElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        sysIdentificationIDElement.setTextContent(subscriptionSearchResponse.getSubscriptionIdentifier());

        Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
        systemNameElement.setTextContent(SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME);
        
        DateTime validationDueDate = subscriptionSearchResponse.getValidationDueDate();
        DateTime lastValidatedDate = subscriptionSearchResponse.getLastValidationDate();

        if (validationDueDate != null || lastValidatedDate != null) {
            Element subscriptionValidationElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionValidation");
            if (validationDueDate != null) {
                Element e = XmlUtils.appendElement(subscriptionValidationElement, extensionSchema, "SubscriptionValidationDueDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(validationDueDate.toString("yyyy-MM-dd"));
            }
            if (lastValidatedDate != null) {
                Element e = XmlUtils.appendElement(subscriptionValidationElement, extensionSchema, "SubscriptionValidatedDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(lastValidatedDate.toString("yyyy-MM-dd"));
            }
        }
        
        Interval gracePeriodInterval = subscriptionSearchResponse.getGracePeriod();
        
        if (gracePeriodInterval != null) {
            Element gracePeriodElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionGracePeriod");
            Element gracePeriodDateRangeElement = XmlUtils.appendElement(gracePeriodElement, extensionSchema, "SubscriptionGracePeriodDateRange");
            Element e = XmlUtils.appendElement(gracePeriodDateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            e.setTextContent(gracePeriodInterval.getStart().toString("yyyy-MM-dd"));
            e = XmlUtils.appendElement(gracePeriodDateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            e.setTextContent(gracePeriodInterval.getEnd().toString("yyyy-MM-dd"));
        }

        String categoryReasonCode = subscriptionSearchResponse.getSubscriptionCategoryCode();
        
        if(StringUtils.isNotEmpty(categoryReasonCode)){
        	
            Element reasonCodeElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "CriminalSubscriptionReasonCode");
            reasonCodeElement.setTextContent(categoryReasonCode);        	
        }        
        
        createSubscriptionTriggeringEvents(subscriptionSearchResponse, subscriptionElement, extensionSchema);
        createFederalRapSheetDisclosure(subscriptionSearchResponse, subscriptionElement, extensionSchema);
        
        return subscriptionElement;
    }

	private static void appendActivityDateRangeElement(DateTime startDate, DateTime endDate, 
			Element parentElement) {
		if (startDate != null || endDate != null)
        {	
	        Element activityDateRangeElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_NC, "ActivityDateRange");
	        
	        if (startDate != null)
	        {	
		        Element startDateParentElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");
		        Element startDateElement = XmlUtils.appendElement(startDateParentElement, OjbcNamespaceContext.NS_NC, "Date");
		        startDateElement.setTextContent(startDate.toString("yyyy-MM-dd"));
	        }    
	
	        if (endDate != null)
	        {	
		        Element endDateParentElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
		        Element endDateElement = XmlUtils.appendElement(endDateParentElement, OjbcNamespaceContext.NS_NC, "Date");
		        endDateElement.setTextContent(endDate.toString("yyyy-MM-dd"));
	        }    
        }
	}

    private static void createSubscriptionEmails(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Set<String> emailAddresses = subscriptionSearchResponse.getEmailAddressesToNotify();

            if (emailAddresses != null) {
                int i = 1;

                for (String emailAddress : emailAddresses) {
                    Element contactInformationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC, "ContactInformation");
                    XmlUtils.addAttribute(contactInformationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "SE" + (subscriptionSearchResponseList.indexOf(subscriptionSearchResponse)+1) + "CE" + i);

                    Element contactEmailID = XmlUtils.appendElement(contactInformationElement, OjbcNamespaceContext.NS_NC, "ContactEmailID");
                    contactEmailID.setTextContent(emailAddress);

                    i++;
                }
            }
        }
    }

    private static void createSubscribedEntityContactInformationAssociations(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

        	int index = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;
            Set<String> emailAddresses = subscriptionSearchResponse.getEmailAddressesToNotify();

            if (emailAddresses != null) {
                int i = 1;

                for (String emailAddress : emailAddresses) {
                    Element subscribedEntityContactInformationAssociationElement = XmlUtils.appendElement(root, extensionSchema, "SubscribedEntityContactInformationAssociation");

                    Element subscribedEntityReferenceElement = XmlUtils.appendElement(subscribedEntityContactInformationAssociationElement, extensionSchema, "SubscribedEntityReference");
					XmlUtils.addAttribute(subscribedEntityReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + index);

                    Element contactInformationReferenceElement = XmlUtils
                            .appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
                    XmlUtils.addAttribute(contactInformationReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + index
                            + "CE" + i);

                    i++;
                }
            }
        }
    }
    
    private static void createSubscriptionSubjects(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Element personElement = XmlUtils.appendElement(root, extensionSchema, "Person");
            int subscriptionIndex = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;
			XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "P" + subscriptionIndex);

            String dateOfBirth = subscriptionSearchResponse.getDateOfBirth();

            if (StringUtils.isNotBlank(dateOfBirth)) {
                Element personDOBElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
                Element personDOBDateElement = XmlUtils.appendElement(personDOBElement, OjbcNamespaceContext.NS_NC, "Date");
                personDOBDateElement.setTextContent(dateOfBirth);
            }

            Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");

            // We set either person full name or first/last name

            if (StringUtils.isNotBlank(subscriptionSearchResponse.getPersonFullName())) {
                Element personFullNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonFullName");
                personFullNameElement.setTextContent(subscriptionSearchResponse.getPersonFullName());
            }

            if (StringUtils.isNotBlank(subscriptionSearchResponse.getPersonFirstName()) && StringUtils.isNotBlank(subscriptionSearchResponse.getPersonLastName())) {
                Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
                personGivenNameElement.setTextContent(subscriptionSearchResponse.getPersonFirstName());

                Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
                personSurNameElement.setTextContent(subscriptionSearchResponse.getPersonLastName());
            }

            String sid = subscriptionSearchResponse.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.SID);
            String fbiNumber = subscriptionSearchResponse.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.FBI_ID);
            

            if (StringUtils.isNotBlank(sid) || StringUtils.isNotBlank(fbiNumber)) {
                Element personAugmentationElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");

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

}
