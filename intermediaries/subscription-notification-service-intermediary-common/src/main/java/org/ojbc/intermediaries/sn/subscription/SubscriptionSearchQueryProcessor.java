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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Body;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SubscriptionSearchQueryProcessor extends SubscriptionMessageProcessor{
    private static final Log log = LogFactory.getLog(SubscriptionMessageProcessor.class);

	public final String SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME = "Subscriptions";
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    public SubscriptionSearchQueryProcessor(){
    	super();
    }
    
    public Document searchBySubscripitonSearchRequest(@Body Document request) throws Exception{
    	SubscriptionSearchRequest subscriptionSearchRequest = parseSubscriptionSearchRequest(request);
    	List<Subscription> subscriptions = subscriptionSearchQueryDAO.findBySubscriptionSearchRequest(subscriptionSearchRequest);
    	return buildSubscriptionSearchResponseDoc(subscriptions); 
    }
    
    private SubscriptionSearchRequest parseSubscriptionSearchRequest(
			Document request) throws Exception {
    	SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
    	String adminSearchIndicator = XmlUtils.xPathStringSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:AdminSearchRequestIndicator");
    	subscriptionSearchRequest.setAdminSearch(BooleanUtils.toBooleanObject(adminSearchIndicator));
    	
    	String subscriptionActiveIndicator = XmlUtils.xPathStringSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscriptionActiveIndicator");
    	subscriptionSearchRequest.setActive(BooleanUtils.toBooleanObject(subscriptionActiveIndicator));
    	
    	String ownerFirstName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/nc:EntityPerson/nc:PersonName/nc:PersonGivenName");
    	subscriptionSearchRequest.setOwnerFirstName(ownerFirstName);
    	
    	String ownerLastName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/nc:EntityPerson/nc:PersonName/nc:PersonSurName");
    	subscriptionSearchRequest.setOwnerLastName(ownerLastName);
    	
    	String ownerFederatedId = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/ssreq-ext:SubscribedEntityFederatedIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setOwnerFederatedId(ownerFederatedId);
    	
    	String ownerOri = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/jxdm41:Organization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setOwnerOri(ownerOri);
    	
    	String subjectFirstName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/nc:PersonName/nc:PersonGivenName");
    	subscriptionSearchRequest.setSubjectFirstName(subjectFirstName);
    	if (StringUtils.isNotBlank(subjectFirstName)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("firstName", subjectFirstName);
    	}
    	
    	String subjectLastName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/nc:PersonName/nc:PersonSurName");
    	subscriptionSearchRequest.setSubjectLastName(subjectLastName);
    	if (StringUtils.isNotBlank(subjectLastName)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("lastName", subjectLastName);
    	}
    	
    	String ucn = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setUcn(ucn);
    	
    	if (StringUtils.isNotBlank(ucn)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("FBI_ID", ucn);
    	}
    	
    	String sid = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setSid(sid);
    	if(StringUtils.isNotBlank(sid)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("SID", sid);
    	}
    	
    	NodeList reasonCodeNodeList = XmlUtils.xPathNodeListSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:CriminalSubscriptionReasonCode");
		if (reasonCodeNodeList != null && reasonCodeNodeList.getLength() > 0){
			for (int i = 0; i < reasonCodeNodeList.getLength(); i++) {
                Element reasonCodeElement = (Element) reasonCodeNodeList.item(i);
				if (StringUtils.isNotBlank(reasonCodeElement.getTextContent())){
					subscriptionSearchRequest.getSubscriptionCategories().add(reasonCodeElement.getTextContent());
				}
	        }
		}

    	log.info("Parsed subscriptionSearchRequest " + subscriptionSearchRequest);
		return subscriptionSearchRequest;
	}

	/**
     * Convert the POJO to the equivalent XML document
     */
    public Document buildSubscriptionQueryResponseDoc(Subscription subscription) throws Exception {

        Document doc = null;

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS, "SubscriptionQueryResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS);

        Element subscriptionSearchResultElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT, "SubscriptionQueryResult");

        appendSubscriptionQueryResult(subscription, subscriptionSearchResultElement, 
        		OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);

        createSubscriptionSubjects(Arrays.asList(subscription), root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createOrganization(Arrays.asList(subscription), root);
        
        createSubscriptionEmails(Arrays.asList(subscription), root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscriptionOwnerEmails(Arrays.asList(subscription), root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        
        createSubjectContactInformationAssociations(Arrays.asList(subscription), root);
        createOwnerOrganizationAssociation(Arrays.asList(subscription), root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscribedEntityContactInformationAssociations(Arrays.asList(subscription), root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createStateSubscriptionFBISubscriptionAssociation(Arrays.asList(subscription), root);
        
        ojbcNamespaceContext.populateRootNamespaceDeclarations(root);

        return doc;
    }

    private void createOwnerOrganizationAssociation(List<Subscription> subscriptions, Element root, String extensionNamespace) {
        for (int i=0; i < subscriptions.size(); i++) {
        	Subscription subscription = subscriptions.get(i);

            if (StringUtils.isNotBlank(subscription.getOri())) {
                Element subscribedEntityOrganizationAssociation = XmlUtils.appendElement(root, extensionNamespace, "SubscribedEntityOrganizationAssociatio");

                Element subscribedEntityReference = XmlUtils.appendElement(subscribedEntityOrganizationAssociation, extensionNamespace, "SubscribedEntityReference");
				XmlUtils.addAttribute(subscribedEntityReference, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("SE", i));

                Element organizationReference = XmlUtils
                        .appendElement(subscribedEntityOrganizationAssociation, OjbcNamespaceContext.NS_NC, "OrganizationReference");
                XmlUtils.addAttribute(organizationReference, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("SO", i));
            }
        }
	}

	private void createOrganization(List<Subscription> subscriptions, Element root) {
		for (int i = 0; i < subscriptions.size(); i++) {
			
			Subscription subscription = subscriptions.get(i);
			
			if (StringUtils.isNotBlank(subscription.getOri())) {
				
				Element organizationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_41, "Organization");
				XmlUtils.addAttribute(organizationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", getElementId("SO", i));
				
				if (StringUtils.isNotBlank(subscription.getAgencyName())){
					Element organizationName = XmlUtils.appendElement(organizationElement, OjbcNamespaceContext.NS_NC, "OrganizationName");
					organizationName.setTextContent(subscription.getAgencyName());
				}
				
				Element organizationAugmentationElement = XmlUtils.appendElement(organizationElement, OjbcNamespaceContext.NS_JXDM_41, "OrganizationAugmentation");
				Element organizationORIIdentificationElement = XmlUtils.appendElement(organizationAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "OrganizationORIIdentification");
				Element identificationId = XmlUtils.appendElement(organizationORIIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
				identificationId.setTextContent(subscription.getOri());
					
			}
		}
		
	}

	/**
     * Convert the list of POJOs to the equivalent XML document
     */
    public Document buildSubscriptionSearchResponseDoc(List<Subscription> subscriptions) throws Exception {

        Document doc = null;

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS, "SubscriptionSearchResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS);

        for (Subscription subscriptionSearchResponse : subscriptions) {

            Element subscriptionSearchResultElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, "SubscriptionSearchResult");
            int index = subscriptions.indexOf(subscriptionSearchResponse) + 1;

            appendSubscriptionParentResponse(subscriptionSearchResponse, doc, subscriptionSearchResultElement, index, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT
                    );

        }

        createFbiSubscriptions(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscribedEntity(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionSubjects(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionEmails(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionOwnerEmails(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubjectContactInformationAssociations(subscriptions, root);
        createSubscribedEntityContactInformationAssociations(subscriptions, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createStateSubscriptionFBISubscriptionAssociation(subscriptions, root);
        
        ojbcNamespaceContext.populateRootNamespaceDeclarations(root);

        return doc;
        
    }

    private void createSubscribedEntityContactInformationAssociations(List<Subscription> subscriptions, Element root,
			String extensionSchema) {
        for (int i=0; i < subscriptions.size(); i++) {
        	Subscription subscription = subscriptions.get(i);

            String ownerEmailAddress = subscription.getSubscriptionOwnerEmailAddress();
            if (StringUtils.isNotBlank(ownerEmailAddress)) {

                    Element subscribedEntityContactInformationAssociationElement = XmlUtils.appendElement(root, extensionSchema, "SubscribedEntityContactInformationAssociation");

                    Element subscribedEntityReferenceElement = XmlUtils.appendElement(subscribedEntityContactInformationAssociationElement, extensionSchema, "SubscribedEntityReference");
					XmlUtils.addAttribute(subscribedEntityReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("SE", i));

                    Element contactInformationReferenceElement = XmlUtils
                            .appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
                    XmlUtils.addAttribute(contactInformationReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("SE", i)
                            + getElementId("CE", i));
            }
        }
		
	}

	private void createSubscribedEntity(List<Subscription> subscriptions, Element parent, String extensionSchema){
        for (int i=0; i<subscriptions.size(); i++) {
        	Subscription subscription = subscriptions.get(i);

        	String subscribedEntityId = "SE" + StringUtils.leftPad(String.valueOf(i+1), 3, '0');
	        Element subscribedEntityElement = XmlUtils.appendElement(parent, extensionSchema, "SubscribedEntity");
	        XmlUtils.addAttribute(subscribedEntityElement, OjbcNamespaceContext.NS_STRUCTURES, "id",  subscribedEntityId);
	        
	        Element entityPersonElement = XmlUtils.appendElement(subscribedEntityElement, OjbcNamespaceContext.NS_NC, "EntityPerson");
	        
	        Element personNameElement = XmlUtils.appendElement(entityPersonElement, OjbcNamespaceContext.NS_NC, "PersonName");
	        Element ownerGivenName = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
	        ownerGivenName.setTextContent(subscription.getSubscriptionOwnerFirstName());
	        
	        Element ownerLastName = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
	        ownerLastName.setTextContent(subscription.getSubscriptionOwnerLastName());

        }   
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

	private void createFbiSubscriptions( List<Subscription> subscriptions, Element parent, String nsSubscriptionSearchResultsExt) {
		for (int i = 0; i < subscriptions.size(); i++){
			
			Subscription subscription = subscriptions.get(i);
			int subscriptionIndex = i + 1;
			
			if (subscription.getFbiRapbackSubscription() != null){
				
				FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
				
		        Element fbiSubscriptionElement = XmlUtils.appendElement(parent, nsSubscriptionSearchResultsExt, "FBISubscription");
		        XmlUtils.addAttribute(fbiSubscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", 
		        		"FBI" + StringUtils.leftPad(String.valueOf(subscriptionIndex), 3, '0'));
		        
		        XmlUtils.appendActivityDateRangeElement(fbiSubscriptionElement,  OjbcNamespaceContext.NS_NC,
		        		fbiRapbackSubscription.getRapbackStartDate(), 
		        		fbiRapbackSubscription.getRapbackExpirationDate()); 
		        
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

    private Element appendSubscriptionParentResponse(Subscription subscription, Document doc, 
    		Element subscriptionSearchResultElement, int searchResponseIndex, String extensionSchema) {

        Element subscriptionElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "Subscription");
        XmlUtils.addAttribute(subscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "S" + StringUtils.leftPad(String.valueOf(searchResponseIndex), 3, '0'));

        XmlUtils.appendActivityDateRangeElement(subscriptionElement,  OjbcNamespaceContext.NS_NC,
        		subscription.getStartDate(), subscription.getEndDate());

        appendSubscriptionActiveIndicator(subscription, extensionSchema, subscriptionElement);

//		<sqr-ext:SubscriptionRelatedCaseIdentification>
//			<nc:IdentificationID>0123ABC</nc:IdentificationID>
//		</sqr-ext:SubscriptionRelatedCaseIdentification>
        appendSubscriptionRelatedCaseIdentification(subscriptionElement, extensionSchema, subscription);
        
        Element subscriptionSubjectElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionSubject");
        Element roleOfPersonReferenceElement = XmlUtils.appendElement(subscriptionSubjectElement, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(roleOfPersonReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "P" + StringUtils.leftPad(String.valueOf(searchResponseIndex), 3, '0'));

        appendTopic(subscription, subscriptionElement);

        appendSubscriptionOriginator(subscription, extensionSchema, subscriptionElement);

        Element sourceSystemNameTextElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "SourceSystemNameText");
        sourceSystemNameTextElement.setTextContent(subscription.getSubscribingSystemIdentifier());

        Element systemIdentifierElement = XmlUtils.appendElement(subscriptionSearchResultElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");

        Element sysIdentificationIDElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        sysIdentificationIDElement.setTextContent(subscription.getSubscriptionIdentifier());

        Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
        systemNameElement.setTextContent(SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME);
        
        appendSubscriptionValidation(subscription, extensionSchema,
				subscriptionElement);
        
        appendGracePeriod(subscription, extensionSchema,subscriptionElement);
        appendSubscritionReasonCode(subscription, extensionSchema,subscriptionElement);        
        createSubscriptionTriggeringEvents(subscriptionElement, extensionSchema, subscription);
        createFederalRapSheetDisclosure(subscriptionElement, extensionSchema, subscription);
        
        return subscriptionElement;
    }

	private void appendSubscriptionActiveIndicator(Subscription subscription, String extensionSchema,
			Element subscriptionElement) {
		if(subscription.getActive()!= null){
	        Element subscriptionActiveIndicatorElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionActiveIndicator");
	        subscriptionActiveIndicatorElement.setTextContent(subscription.getActive().toString());
        }
	}

	private void appendSubscriptionQualifierIdentification(Subscription subscriptionSearchResponse,
			String extensionSchema, Element subscriptionElement) {
		Map<String, String> identifiers = subscriptionSearchResponse.getSubscriptionSubjectIdentifiers();
        String subscriptionQualifier = identifiers.get("subscriptionQualifier");
        
        if (StringUtils.isNotBlank(subscriptionQualifier))
        {
        	Element subscriptionQualifierIdentificationElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionQualifierIdentification");
        	Element identificationID = XmlUtils.appendElement(subscriptionQualifierIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        	identificationID.setTextContent(subscriptionQualifier);
        }
	}

	private void appendCreationDate(Subscription subscriptionSearchResponse,
			String extensionSchema, Element subscriptionElement) {
		DateTime creationDate = subscriptionSearchResponse.getCreationDate();
        
        if (creationDate != null) {
            if (creationDate != null) {
                Element e = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionCreationDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(creationDate.toString("yyyy-MM-dd"));
            }

        }
	}

	private void appendSubscriptionValidation(Subscription subscriptionSearchResponse,
			String extensionSchema, Element subscriptionElement) {
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
	}

	private void appendGracePeriod(Subscription subscriptionSearchResponse,
			String extensionSchema, Element subscriptionElement) {
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
	}

    private Element appendSubscriptionQueryResult(Subscription subscription,
    		Element subscriptionQueryResultElement, String extensionSchema) {

        Element subscriptionElement = XmlUtils.appendElement(subscriptionQueryResultElement, extensionSchema, "Subscription");
        XmlUtils.addAttribute(subscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "S001");

        XmlUtils.appendActivityDateRangeElement(subscriptionElement,  OjbcNamespaceContext.NS_NC,
        		subscription.getStartDate(), subscription.getEndDate());

        appendSubscriptionActiveIndicator(subscription, extensionSchema, subscriptionElement);
        
        appendSubscriptionQualifierIdentification(subscription, extensionSchema, subscriptionElement);	
        
        appendCreationDate(subscription, extensionSchema, subscriptionElement);

        appendLastUpdateDate(subscription, extensionSchema, subscriptionElement);
//		<sqr-ext:SubscriptionRelatedCaseIdentification>
//			<nc:IdentificationID>0123ABC</nc:IdentificationID>
//		</sqr-ext:SubscriptionRelatedCaseIdentification>
        appendSubscriptionRelatedCaseIdentification(subscriptionElement, extensionSchema, subscription);
        createFbiSubscriptions(Arrays.asList(subscription), subscriptionElement, extensionSchema);
        Element subscriptionSubjectElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionSubject");
        Element roleOfPersonReferenceElement = XmlUtils.appendElement(subscriptionSubjectElement, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(roleOfPersonReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "P001");

        appendTopic(subscription, subscriptionElement);
        
        createSubscribedEntity(Arrays.asList(subscription), subscriptionElement, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        appendSubscriptionOriginator(subscription, extensionSchema, subscriptionElement);

        appendSubscriptionValidation(subscription, extensionSchema, subscriptionElement);
        appendGracePeriod(subscription, extensionSchema, subscriptionElement);
        appendSubscritionReasonCode(subscription, extensionSchema, subscriptionElement);        
        
        createSubscriptionTriggeringEvents(subscriptionElement, extensionSchema, subscription);
        createFederalRapSheetDisclosure(subscriptionElement, extensionSchema, subscription);
        
        Element sourceSystemNameTextElement = XmlUtils.appendElement(subscriptionQueryResultElement, extensionSchema, "SourceSystemNameText");
        sourceSystemNameTextElement.setTextContent(subscription.getSubscribingSystemIdentifier());

        Element systemIdentifierElement = XmlUtils.appendElement(subscriptionQueryResultElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");

        Element sysIdentificationIDElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        sysIdentificationIDElement.setTextContent(subscription.getSubscriptionIdentifier());

        Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
        systemNameElement.setTextContent(SUBSCRIPTION_SEARCH_RESPONSE_SYSTEM_NAME);
        

        return subscriptionElement;
    }

	private void appendLastUpdateDate(Subscription subscription, String extensionSchema,
			Element subscriptionElement) {
		DateTime lastUpdatedDate = subscription.getLastUpdatedDate();
        
        if (lastUpdatedDate != null) {
            if (lastUpdatedDate != null) {
                Element e = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionLastUpdatedDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(lastUpdatedDate.toString("yyyy-MM-dd"));
            }

        }
	}

	private void appendTopic(Subscription subscription,
			Element subscriptionElement) {
		Element subscriptionTopicElement = XmlUtils.appendElement(subscriptionElement, OjbcNamespaceContext.NS_WSN_BROKERED, "Topic");
        subscriptionTopicElement.setAttribute("Dialect", "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete");
        subscriptionTopicElement.setTextContent(subscription.getTopic());
	}

	private void appendSubscriptionOriginator(Subscription subscription, String extensionSchema,
			Element subscriptionElement) {
		Element subscriptionOriginatorElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionOriginator");

        Element subscriptionOriginatorIdentificationElement = XmlUtils.appendElement(subscriptionOriginatorElement, extensionSchema, "SubscriptionOriginatorIdentification");

        Element identificationIDElement = XmlUtils.appendElement(subscriptionOriginatorIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
        identificationIDElement.setTextContent(subscription.getSubscriptionOwner());
	}

	private void appendSubscritionReasonCode(Subscription subscription, String extensionSchema,
			Element subscriptionElement) {
		String categoryReasonCode = subscription.getSubscriptionCategoryCode();
        if(StringUtils.isNotEmpty(categoryReasonCode)){
        	//TODO based on the reason code type, create either civil or criminal reason code element
            Element reasonCodeElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "CriminalSubscriptionReasonCode");
            reasonCodeElement.setTextContent(categoryReasonCode);        	
        }
	}
    
	private void createSubscriptionEmails(List<Subscription> subscriptions, Element root, String extensionSchema) {
        for (int i = 0; i < subscriptions.size(); i++) {

        	Subscription subscription = subscriptions.get(i);
            List<String> emailAddresses = new ArrayList<>( subscription.getEmailAddressesToNotify() );

            if (emailAddresses != null) {

                for (int j=0; j<emailAddresses.size(); j++) {
                    Element contactInformationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC, "ContactInformation");
                    XmlUtils.addAttribute(contactInformationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", getElementId("P", i) + getElementId("CE", j));

                    Element contactEmailID = XmlUtils.appendElement(contactInformationElement, OjbcNamespaceContext.NS_NC, "ContactEmailID");
                    contactEmailID.setTextContent(emailAddresses.get(j));

                }
            }
        }
    }

	private void createSubscriptionOwnerEmails(List<Subscription> subscriptions, Element root, String extensionSchema) {
		for (int i = 0; i < subscriptions.size(); i++) {
			
			Subscription subscription = subscriptions.get(i);
			
			if (StringUtils.isNotBlank(subscription.getSubscriptionOwnerEmailAddress())) {
				
				Element contactInformationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC, "ContactInformation");
				XmlUtils.addAttribute(contactInformationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", getElementId("SE", i) + getElementId("CE", i));
				
				Element contactEmailID = XmlUtils.appendElement(contactInformationElement, OjbcNamespaceContext.NS_NC, "ContactEmailID");
				contactEmailID.setTextContent(subscription.getSubscriptionOwnerEmailAddress());
					
			}
		}
	}
	
    private void createSubjectContactInformationAssociations(List<Subscription> subscriptions, Element root) {
        for (int i=0; i < subscriptions.size(); i++) {
        	Subscription subscription = subscriptions.get(i);

            Set<String> emailAddresses = subscription.getEmailAddressesToNotify();

            if (emailAddresses != null) {

                for (int j= 0; j < emailAddresses.size(); j++) {
                    Element subscribedEntityContactInformationAssociationElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC, "PersonContactInformationAssociation");

                    Element subscribedEntityReferenceElement = XmlUtils.appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "PersonReference");
					XmlUtils.addAttribute(subscribedEntityReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("P", i));

                    Element contactInformationReferenceElement = XmlUtils
                            .appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
                    XmlUtils.addAttribute(contactInformationReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", getElementId("P", i)
                            + getElementId("CE", j));

                }
            }
        }
    }
    
    private void createSubscriptionSubjects(List<Subscription> subscriptions, Element root, String extensionSchema) {
        for (int i= 0;  i<subscriptions.size(); i++) {
        	Subscription subscription = subscriptions.get(i);
            Element personElement = XmlUtils.appendElement(root, extensionSchema, "Person");
			XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", getElementId("P", i));

			appendPersonInfo(personElement, subscription);
        }
    }

	private String getElementId(String prefix,  int i) {
		return prefix + StringUtils.leftPad(String.valueOf(i+1), 3, '0');
	}

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

}
