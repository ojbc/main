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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
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

        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
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
            int index = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse);

            appendSubscriptionParentResponse(subscriptionSearchResponse, doc, subscriptionSearchResultElement, index, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT
                    );

        }

        createSubscriptionSubjects(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionEmails(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);

        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
        
    }



    private static final String SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME = "Subscriptions";

    private static Element appendSubscriptionParentResponse(Subscription subscriptionSearchResponse, Document doc, 
    		Element subscriptionSearchResultElement, int searchResponseIndex, String extensionSchema) {

        Element subscriptionElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "Subscription");

        if (subscriptionSearchResponse.getStartDate() != null || subscriptionSearchResponse.getEndDate() != null)
        {	
	        Element activityDateRangeElement = XmlUtils.appendElement(subscriptionElement, OjbcNamespaceContext.NS_NC, "ActivityDateRange");
	        
	        if (subscriptionSearchResponse.getStartDate() != null)
	        {	
		        Element startDateParentElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");
		        Element startDateElement = XmlUtils.appendElement(startDateParentElement, OjbcNamespaceContext.NS_NC, "Date");
		        startDateElement.setTextContent(subscriptionSearchResponse.getStartDate().toString("yyyy-MM-dd"));
	        }    
	
	        if (subscriptionSearchResponse.getEndDate() != null)
	        {	
		        Element endDateParentElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
		        Element endDateElement = XmlUtils.appendElement(endDateParentElement, OjbcNamespaceContext.NS_NC, "Date");
		        endDateElement.setTextContent(subscriptionSearchResponse.getEndDate().toString("yyyy-MM-dd"));
	        }    
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
        return subscriptionElement;
    }

    private static void createSubscriptionEmails(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Set<String> emailAddresses = subscriptionSearchResponse.getEmailAddressesToNotify();

            if (emailAddresses != null) {
                int i = 1;

                for (String emailAddress : emailAddresses) {
                    Element contactInformationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC, "ContactInformation");
                    XmlUtils.addAttribute(contactInformationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "SE" + subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + "CE" + i);

                    Element contactEmailID = XmlUtils.appendElement(contactInformationElement, OjbcNamespaceContext.NS_NC, "ContactEmailID");
                    contactEmailID.setTextContent(emailAddress);

                    i++;
                }

                i = 1;

                for (String emailAddress : emailAddresses) {
                    Element subscribedEntityContactInformationAssociationElement = XmlUtils.appendElement(root, extensionSchema, "SubscribedEntityContactInformationAssociation");

                    Element subscribedEntityReferenceElement = XmlUtils.appendElement(subscribedEntityContactInformationAssociationElement, extensionSchema, "SubscribedEntityReference");
                    XmlUtils.addAttribute(subscribedEntityReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + subscriptionSearchResponseList.indexOf(subscriptionSearchResponse));

                    Element contactInformationReferenceElement = XmlUtils
                            .appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
                    XmlUtils.addAttribute(contactInformationReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + subscriptionSearchResponseList.indexOf(subscriptionSearchResponse)
                            + "CE" + i);

                    i++;
                }
            }
        }
    }

    private static void createSubscriptionSubjects(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Element personElement = XmlUtils.appendElement(root, extensionSchema, "Person");
            XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "P" + subscriptionSearchResponseList.indexOf(subscriptionSearchResponse));

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

            if (StringUtils.isNotBlank(sid)) {
                Element personAugmentationElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");

                Element personStateFingerprintIdentification = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");

                Element identificationIDElement = XmlUtils.appendElement(personStateFingerprintIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
                identificationIDElement.setTextContent(sid);

            }
        }
    }

}
