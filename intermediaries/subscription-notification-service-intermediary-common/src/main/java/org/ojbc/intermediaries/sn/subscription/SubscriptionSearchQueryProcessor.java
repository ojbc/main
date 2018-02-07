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

        createFbiSubscriptions(searchResponse, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscribedEntity(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscriptionSubjects(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscriptionEmails(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createSubscribedEntityContactInformationAssociations(searchResponse, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_RESULTS_EXT);
        createStateSubscriptionFBISubscriptionAssociation(searchResponse, root);
        
        ojbcNamespaceContext.populateRootNamespaceDeclarations(root);

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
            int index = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;

            appendSubscriptionParentResponse(subscriptionSearchResponse, doc, subscriptionSearchResultElement, index, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT
                    );

        }

        createFbiSubscriptions(subscriptionSearchResponseList, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscribedEntity(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionSubjects(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscriptionEmails(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createSubscribedEntityContactInformationAssociations(subscriptionSearchResponseList, doc, root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);
        createStateSubscriptionFBISubscriptionAssociation(subscriptionSearchResponseList, root);
        
        ojbcNamespaceContext.populateRootNamespaceDeclarations(root);

        return doc;
        
    }

    private void createSubscribedEntity(List<Subscription> subscriptionSearchResponseList, Document doc, 
    		Element root, String extensionSchema)
    {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

        	int index = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;

	        Element subscribedEntityElement = XmlUtils.appendElement(root, extensionSchema, "SubscribedEntity");
	        XmlUtils.addAttribute(subscribedEntityElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "SE" + index);
	        
	        Element entityPersonElement = XmlUtils.appendElement(subscribedEntityElement, OjbcNamespaceContext.NS_NC, "EntityPerson");
	        
	        Element personNameElement = XmlUtils.appendElement(entityPersonElement, OjbcNamespaceContext.NS_NC, "PersonName");
	        Element ownerGivenName = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
	        ownerGivenName.setTextContent(subscriptionSearchResponse.getSubscriptionOwnerFirstName());
	        
	        Element ownerLastName = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
	        ownerLastName.setTextContent(subscriptionSearchResponse.getSubscriptionOwnerLastName());

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

	private void createFbiSubscriptions( List<Subscription> subscriptionList,
			Element root, String nsSubscriptionSearchResultsExt) {
		for (Subscription subscription : subscriptionList){
			
			int subscriptionIndex = subscriptionList.indexOf(subscription) + 1;
			
			if (subscription.getFbiRapbackSubscription() != null){
				
				FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
				
		        Element fbiSubscriptionElement = XmlUtils.appendElement(root, nsSubscriptionSearchResultsExt, "FBISubscription");
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

    private Element appendSubscriptionParentResponse(Subscription subscriptionSearchResponse, Document doc, 
    		Element subscriptionSearchResultElement, int searchResponseIndex, String extensionSchema) {

        Element subscriptionElement = XmlUtils.appendElement(subscriptionSearchResultElement, extensionSchema, "Subscription");
        XmlUtils.addAttribute(subscriptionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "S" + StringUtils.leftPad(String.valueOf(searchResponseIndex), 3, '0'));

        XmlUtils.appendActivityDateRangeElement(subscriptionElement,  OjbcNamespaceContext.NS_NC,
        		subscriptionSearchResponse.getStartDate(), subscriptionSearchResponse.getEndDate());

//		<sqr-ext:SubscriptionActiveIndicator>true</sqr-ext:SubscriptionActiveIndicator>
//		<sqr-ext:SubscriptionQualifierIdentification>
//			<nc:IdentificationID>Q123456</nc:IdentificationID>
//		</sqr-ext:SubscriptionQualifierIdentification>
//		<sqr-ext:SubscriptionCreationDate>
//			<nc:Date>2014-03-12</nc:Date>
//		</sqr-ext:SubscriptionCreationDate>
//		<sqr-ext:SubscriptionLastUpdatedDate>
//			<nc:Date>2014-05-20</nc:Date>
//		</sqr-ext:SubscriptionLastUpdatedDate>
        if(subscriptionSearchResponse.getActive()!= null){
	        Element subscriptionActiveIndicatorElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionActiveIndicator");
	        subscriptionActiveIndicatorElement.setTextContent(subscriptionSearchResponse.getActive().toString());
        }
        
        Map<String, String> identifiers = subscriptionSearchResponse.getSubscriptionSubjectIdentifiers();
        String subscriptionQualifier = identifiers.get("subscriptionQualifier");
        
        if (StringUtils.isNotBlank(subscriptionQualifier))
        {
        	Element subscriptionQualifierIdentificationElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionQualifierIdentification");
        	subscriptionQualifierIdentificationElement.setTextContent(subscriptionQualifier);
        }	
        
        DateTime creationDate = subscriptionSearchResponse.getCreationDate();
        
        if (creationDate != null) {
            if (creationDate != null) {
                Element e = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionCreationDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(creationDate.toString("yyyy-MM-dd"));
            }

        }

        DateTime lastUpdatedDate = subscriptionSearchResponse.getLastUpdatedDate();
        
        if (lastUpdatedDate != null) {
            if (lastUpdatedDate != null) {
                Element e = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionLastUpdatedDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(lastUpdatedDate.toString("yyyy-MM-dd"));
            }

        }

//		<sqr-ext:SubscriptionRelatedCaseIdentification>
//			<nc:IdentificationID>0123ABC</nc:IdentificationID>
//		</sqr-ext:SubscriptionRelatedCaseIdentification>
        appendSubscriptionRelatedCaseIdentification(subscriptionElement, extensionSchema, subscriptionSearchResponse);
        
        Element subscriptionSubjectElement = XmlUtils.appendElement(subscriptionElement, extensionSchema, "SubscriptionSubject");

        Element roleOfPersonReferenceElement = XmlUtils.appendElement(subscriptionSubjectElement, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(roleOfPersonReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "P" + searchResponseIndex);

        Element subscriptionTopicElement = XmlUtils.appendElement(subscriptionElement, OjbcNamespaceContext.NS_WSN_BROKERED, "Topic");
        subscriptionTopicElement.setAttribute("Dialect", "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete");
        subscriptionTopicElement.setTextContent(subscriptionSearchResponse.getTopic());

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
        
        createSubscriptionTriggeringEvents(subscriptionElement, extensionSchema, subscriptionSearchResponse);
        createFederalRapSheetDisclosure(subscriptionElement, extensionSchema, subscriptionSearchResponse);
        
        return subscriptionElement;
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

                for (int i= 1; i <= emailAddresses.size(); i++) {
                    Element subscribedEntityContactInformationAssociationElement = XmlUtils.appendElement(root, extensionSchema, "SubscribedEntityContactInformationAssociation");

                    Element subscribedEntityReferenceElement = XmlUtils.appendElement(subscribedEntityContactInformationAssociationElement, extensionSchema, "SubscribedEntityReference");
					XmlUtils.addAttribute(subscribedEntityReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + index);

                    Element contactInformationReferenceElement = XmlUtils
                            .appendElement(subscribedEntityContactInformationAssociationElement, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
                    XmlUtils.addAttribute(contactInformationReferenceElement, OjbcNamespaceContext.NS_STRUCTURES, "ref", "SE" + index
                            + "CE" + i);

                }
            }
        }
    }
    
    private void createSubscriptionSubjects(List<Subscription> subscriptionSearchResponseList, Document doc, Element root, String extensionSchema) {
        for (Subscription subscriptionSearchResponse : subscriptionSearchResponseList) {

            Element personElement = XmlUtils.appendElement(root, extensionSchema, "Person");
            int subscriptionIndex = subscriptionSearchResponseList.indexOf(subscriptionSearchResponse) + 1;
			XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "P" + subscriptionIndex);

			appendPersonInfo(personElement, subscriptionSearchResponse);
        }
    }

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

}
