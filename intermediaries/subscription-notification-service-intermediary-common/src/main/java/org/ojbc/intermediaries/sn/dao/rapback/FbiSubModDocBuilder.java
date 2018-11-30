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
package org.ojbc.intermediaries.sn.dao.rapback;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SUB_MSG_EXT;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSubModDocBuilder {
	@SuppressWarnings("unused")
	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private static final Logger logger = Logger.getLogger(FbiSubModDocBuilder.class);
	
	private OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
	private Map<String, String> reasonCodeTypeMap = new HashMap<>();
	
	public FbiSubModDocBuilder(){
		reasonCodeTypeMap.put("I", "CIVIL");
		reasonCodeTypeMap.put("F", "CIVIL");
		reasonCodeTypeMap.put("J", "CIVIL");
		reasonCodeTypeMap.put("S", "CIVIL");
		reasonCodeTypeMap.put("CS", "CRIMINAL");
		reasonCodeTypeMap.put("CI", "CRIMINAL");
	}
	
	public Document buildFbiSubModDoc( FbiRapbackSubscription fbiRapbackSubscription, Document subscripitonDoc ) throws Exception{
				
		Document doc = OJBCXMLUtils.createDocument();
		
		Element modifyElement = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Modify");

		doc.appendChild(modifyElement);
				
		Element subModMsgElement = XmlUtils.appendElement(modifyElement, OjbcNamespaceContext.NS_SUB_MODIFY_MESSAGE, "SubscriptionModificationMessage");
		
		Element subscriptionMessage = (Element) XmlUtils.xPathNodeSearch(subscripitonDoc, "/b-2:Subscribe/submsg-exch:SubscriptionMessage");
		Node subscriptionRelatedCaseIdentification = XmlUtils.xPathNodeSearch(subscriptionMessage, 
				"submsg-ext:SubscriptionRelatedCaseIdentification"); 
		subModMsgElement.appendChild(doc.importNode(subscriptionRelatedCaseIdentification, true));
		
		Node subscribingOrganization = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:SubscribingOrganization");
		if (subscribingOrganization != null){
			subModMsgElement.appendChild(doc.importNode(subscribingOrganization,true));
		}
		
		Node subject = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:Subject");
		if (subject != null){
			subModMsgElement.appendChild(doc.importNode(subject, true));
		}
				
		Node subscriptionQualifierIdentification = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:SubscriptionQualifierIdentification");
		if (subscriptionQualifierIdentification != null){
			subModMsgElement.appendChild(doc.importNode(subscriptionQualifierIdentification, true));
		}
		
		Element subscriptionIdentification = 
				XmlUtils.appendElement(subModMsgElement, NS_SUB_MSG_EXT, "SubscriptionIdentification");
		XmlUtils.appendTextElement(
				subscriptionIdentification, NS_NC, "IdentificationID", fbiRapbackSubscription.getStateSubscriptionId().toString());

		Node subscriptionReasonCode = XmlUtils.xPathNodeSearch(subscriptionMessage, 
				"submsg-ext:CriminalSubscriptionReasonCode | submsg-ext:CivilSubscriptionReasonCode");
		if (subscriptionReasonCode != null){
			subModMsgElement.appendChild(doc.importNode(subscriptionReasonCode, true));
		}
		
		Node triggeringEvents = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:TriggeringEvents");
		if (triggeringEvents != null){
			subModMsgElement.appendChild(doc.importNode(triggeringEvents, true));
		}
		
		Node federalRapSheetDisclosure = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:FederalRapSheetDisclosure");
		if (federalRapSheetDisclosure != null){
			subModMsgElement.appendChild(doc.importNode(federalRapSheetDisclosure, true));
		}
		
		Element fbiSubscription = buildFBISubscriptionElement(subModMsgElement, fbiRapbackSubscription);
		if (subscriptionReasonCode != null){
			fbiSubscription.appendChild(doc.importNode(subscriptionReasonCode, true));
		}
		
		buildSubModEndDateElement(subModMsgElement, subscripitonDoc);				
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(doc.getDocumentElement());
		return doc;
	}


	private Element buildFBISubscriptionElement(Element parentElement, FbiRapbackSubscription fbiRapbackSubscription){
	
		Element fbiSubscriptionElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FBISubscription");
			
		Element subscriptionFBIIdentification = 
				XmlUtils.appendElement(fbiSubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionFBIIdentification");
		XmlUtils.appendTextElement(subscriptionFBIIdentification, OjbcNamespaceContext.NS_NC, 
				"IdentificationID", fbiRapbackSubscription.getFbiSubscriptionId());
		
		return fbiSubscriptionElement;
	}
	
	
	private void buildSubModEndDateElement(Element parentElement, Document subscriptionDoc){
		
		String subscriptionModificationEndDate = null;
		try {
			subscriptionModificationEndDate = XmlUtils.xPathStringSearch(subscriptionDoc, "/b-2:Subscribe/submsg-exch:SubscriptionMessage/nc:DateRange/nc:EndDate/nc:Date");
		} catch (Exception e1) {
			logger.error("Unable to set FBI subscription end date");
		}
						
		if(StringUtils.isNotEmpty(subscriptionModificationEndDate)){
			
			Element subModElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionModification");
			
			Element dateRangeElement = XmlUtils.appendElement(subModElement, OjbcNamespaceContext.NS_NC, "DateRange");
			
			Element endDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
			
			Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC, "Date");
			
			try{
				endDateValElement.setTextContent(subscriptionModificationEndDate);
				
			}catch(Exception e){
				logger.error("Cannot format date");
			}		
		}				
	}


	public Document buildModifyMessageWithSubscripiton(Subscription subscription, String validationDueDateString) throws Exception {
		
		Document document = OJBCXMLUtils.createDocument();
		
		Element modifyElement = document.createElementNS(OjbcNamespaceContext.NS_B2, "Modify");

		document.appendChild(modifyElement);
				
		Element subModMsgElement = XmlUtils.appendElement(modifyElement, OjbcNamespaceContext.NS_SUB_MODIFY_MESSAGE, "SubscriptionModificationMessage");
		
		if(StringUtils.isNotEmpty(subscription.getAgencyCaseNumber())){
			
			Element subRelCaseIdElement = XmlUtils.appendElement(subModMsgElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionRelatedCaseIdentification");
			
			Element caseIdValElement = XmlUtils.appendElement(subRelCaseIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");		
			
			caseIdValElement.setTextContent(subscription.getAgencyCaseNumber());
		}		

		if (StringUtils.isNotBlank(subscription.getOri())){
			Element subscribingOrganization = XmlUtils.appendElement(subModMsgElement, NS_SUB_MSG_EXT, "SubscribingOrganization");
			Element organizationAugmentation = XmlUtils.appendElement(subscribingOrganization, NS_JXDM_41, "OrganizationAugmentation");
			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_41, "OrganizationORIIdentification");
			Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC, "IdentificationID");
			identificationID.setTextContent(subscription.getOri());
		}

		buildSubjectElement(subModMsgElement, subscription);
				
		buildSubscriptionIdNode(subModMsgElement, subscription);

		buildFBISubscriptionIDNode(subModMsgElement, subscription);
		
		buildSubModEndDateElement(subModMsgElement, validationDueDateString);	
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(document.getDocumentElement());
		return document;
	}
	
	private void buildSubModEndDateElement(Element subModMsgElement,
			String validationDueDateString) {
		Element subscriptionModification = XmlUtils.appendElement(subModMsgElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:SubscriptionModification");
		Element dateRangeNode = XmlUtils.appendElement(subscriptionModification, OjbcNamespaceContext.NS_NC, "DateRange");
		
		Element endDateNode = XmlUtils.appendElement(dateRangeNode, OjbcNamespaceContext.NS_NC, "EndDate");		
		Element endDateValNode = XmlUtils.appendElement(endDateNode, OjbcNamespaceContext.NS_NC, "Date");
		if(StringUtils.isNotBlank(validationDueDateString)){
			endDateValNode.setTextContent(validationDueDateString);			
		}				

		
	}


	private void buildSubjectElement(Element parentNode, Subscription subscription){
		
		Element subjectNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");		

		buildDobNode(subjectNode, subscription);
		
		buildPersonNameNode(subjectNode,subscription);
		
		buildPesonAugmentationElement(subjectNode,subscription);				
	}

	private void buildDobNode(Element subjectNode, Subscription subscription) {

		if(StringUtils.isNotBlank(subscription.getDateOfBirth())){
			Element personBirthDateNode = XmlUtils.appendElement(subjectNode, OjbcNamespaceContext.NS_NC, "PersonBirthDate");	
			
			Element dateNode = XmlUtils.appendElement(personBirthDateNode, OjbcNamespaceContext.NS_NC, "Date");
			
			dateNode.setTextContent(subscription.getDateOfBirth());
		}		
	}
	
	private void buildPersonNameNode(Element parentNode, Subscription subscription){
		
		Element personNameNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_NC, "PersonName");		
				
		String sFirstName = subscription.getPersonFirstName();
		if(StringUtils.isNotBlank(sFirstName)){
			Element firstNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonGivenName");
			firstNameNode.setTextContent(sFirstName);
		}
		
		String sLastName = subscription.getPersonLastName();
		if(StringUtils.isNotBlank(sLastName)){
			Element lastNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonSurName");
			lastNameNode.setTextContent(sLastName);
		}
				
		String fullName = subscription.getPersonFullName();
		if(StringUtils.isNotBlank(fullName)){
			Element fullNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonFullName");
			fullNameNode.setTextContent(fullName);
		}		
	}

	private void buildPesonAugmentationElement(Element parentNode, Subscription subscription){
		
		String sid = subscription.getSubscriptionSubjectIdentifiers().get("SID");
		
		String fbiId = subscription.getFbiRapbackSubscription().getUcn();
		
		boolean hasSid = StringUtils.isNotEmpty(sid);
		
		boolean hasFbiId = StringUtils.isNotEmpty(fbiId);
		
		if(hasSid || hasFbiId){
			
			Element personAugNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");

			if(hasFbiId){
				
				Element fbiIdElement = XmlUtils.appendElement(personAugNode, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
				
				Element fbiIdValElement = XmlUtils.appendElement(fbiIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
				
				fbiIdValElement.setTextContent(fbiId);				
			}						
						
			if(hasSid){
				
				Element sidNode = XmlUtils.appendElement(personAugNode, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");				
				
				Element idNode = XmlUtils.appendElement(sidNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
				
				idNode.setTextContent(sid);			 
			}			
		}		
	}

	private void buildSubscriptionIdNode(Element subMsgNode, Subscription subscription) {

		Element subIdNode = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "smext:SubscriptionIdentification");
		
		Element subIdValNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
		subIdValNode.setTextContent(Long.valueOf(subscription.getId()).toString());			
				
		Element idSrcTxtNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
		idSrcTxtNode.setTextContent("Subscriptions");
	}

	private void buildFBISubscriptionIDNode(
			Element subscriptionModificationMessage, Subscription subscription) throws Exception {
		
		Element fbiSubscription = XmlUtils.appendElement(subscriptionModificationMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:FBISubscription");
		
		Element subscriptionFBIIdentification = XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:SubscriptionFBIIdentification");
		
		Element idNode = XmlUtils.appendElement(subscriptionFBIIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
		
		idNode.setTextContent(subscription.getFbiRapbackSubscription().getFbiSubscriptionId());
		
		switch (reasonCodeTypeMap.get(subscription.getSubscriptionCategoryCode())){
		case "CIVIL":
			Element subCivilReasonCodeElement = XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CivilSubscriptionReasonCode");
			subCivilReasonCodeElement.setTextContent(subscription.getSubscriptionCategoryCode());
			break; 
		case "CRIMINAL": 
			Element subCriminalReasonCodeElement = XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
			subCriminalReasonCodeElement.setTextContent(subscription.getSubscriptionCategoryCode());
			break; 
		default:
			logger.warn("No civil or criminal found on the subscription.");
		}

	}
}
