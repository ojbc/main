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
package org.ojbc.util.xml.subscription;

import static org.ojbc.util.helper.UniqueIdUtils.getUniqueId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SubscriptionNotificationDocumentBuilderUtils {				

	private static final Logger logger = Logger.getLogger(SubscriptionNotificationDocumentBuilderUtils.class.getName());
	
	private static final String SYSTEM_NAME = "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB";
	
	private static final String CONSUMER_REF_ADDRESS = "http://www.ojbc.org/OJB/SubscribeNotify";
	
	private static final String TOPIC_EXPRESSION_DIALECT = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete";	
	
	public static final String CIVIL_SUBSCRIPTION_REASON_CODE="I";
	
	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Document createSubscriptionRequest(Subscription subscription) throws ParserConfigurationException{
		
        Document subMsgDoc = createBlankDoc();        
        Element rootSubscribeElement = getRootSubscribeElement(subMsgDoc); 

		buildConsumerRefElement(rootSubscribeElement);
		
		buildFilterElement(rootSubscribeElement, subscription);
						
		XmlUtils.appendElement(rootSubscribeElement, OjbcNamespaceContext.NS_B2, "SubscriptionPolicy");
				
		buildSubscriptionMessageNode(rootSubscribeElement, subscription);
		
		OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(rootSubscribeElement);
		
		return subMsgDoc;
	}
	
	
	private static void buildEmailElements(Element parentNode, List<String> emailList){
		
		for(String iEmail : emailList){			
			if(StringUtils.isNotBlank(iEmail)){
				Element emailElement = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_NC, "ContactEmailID");
				emailElement.setTextContent(iEmail);						
			}				
		}		
	}
	
	private static void buildDateRangeNode(Element parentNode, Subscription subscription){
						
		Element dateRangeNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_NC, "DateRange");
		
		Element startDateNode = XmlUtils.appendElement(dateRangeNode, OjbcNamespaceContext.NS_NC, "StartDate");		
		Element startDateValNode = XmlUtils.appendElement(startDateNode, OjbcNamespaceContext.NS_NC, "Date");
				
		Date subStartDate = subscription.getSubscriptionStartDate();							
		if(subStartDate != null){
			String sSubStartDate = sdf.format(subStartDate);
			startDateValNode.setTextContent(sSubStartDate);			
		}		
		
		Element endDateNode = XmlUtils.appendElement(dateRangeNode, OjbcNamespaceContext.NS_NC, "EndDate");		
		Element endDateValNode = XmlUtils.appendElement(endDateNode, OjbcNamespaceContext.NS_NC, "Date");
		
		Date subEndDate = subscription.getSubscriptionEndDate();					
		if(subEndDate != null){
			String sSubEndDate = sdf.format(subEndDate);	
			endDateValNode.setTextContent(sSubEndDate);			
		}				
	}
	
	
	private static void buildSubQualIdNode(Element parentNode){
		
		Element subQualIdNode =  XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionQualifierIdentification");
		
		Element idNode = XmlUtils.appendElement(subQualIdNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
				
		idNode.setTextContent( getUniqueId());
	}
	
	
	private static void buildSubscriptionMessageNode(Element parentNode, Subscription subscription){
		
		Element subMsgNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXCHANGE, "SubscriptionMessage");
		
		buildCaseIdElement(subMsgNode, subscription);
												
		buildSubjectElement(subMsgNode,subscription);	
		
		buildEmailElements(subMsgNode, subscription.getEmailList());
		
		Element sysNameNode = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SystemName");				
		sysNameNode.setTextContent(SYSTEM_NAME); 
		
		buildSubQualIdNode(subMsgNode);
		
		buildDateRangeNode(subMsgNode, subscription);		
		
		buildSubscriptionIdNode(subMsgNode, subscription);
		
		buildSubscriptionReasonCodeElement(subMsgNode, subscription);
	}
	
	private static void buildCaseIdElement(Element parentNode, Subscription subscription){
		
		String caseId = subscription.getCaseId();
		
		if(StringUtils.isNotEmpty(caseId)){
			
			Element subRelCaseIdElement = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionRelatedCaseIdentification");
			
			Element caseIdValElement = XmlUtils.appendElement(subRelCaseIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");		
			
			caseIdValElement.setTextContent(caseId);
		}		
	}
		
	private static void buildSubscriptionReasonCodeElement(Element parentElement, Subscription subscription){

		String subscriptionReasonCode = subscription.getSubscriptionPurpose();
		
		if(StringUtils.isNotEmpty(subscriptionReasonCode)){
		
			if (CIVIL_SUBSCRIPTION_REASON_CODE.equals(subscriptionReasonCode)){
				Element subReasonCodeElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CivilSubscriptionReasonCode");
				subReasonCodeElement.setTextContent(subscriptionReasonCode);
			}
			else{
				Element subReasonCodeElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
				subReasonCodeElement.setTextContent(subscriptionReasonCode);
			}
		}
	}
		
	private static void buildSubscriptionIdNode(Element subMsgNode, Subscription subscription) {

		Element subIdNode = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "smext:SubscriptionIdentification");
		
		String systemId = subscription.getSystemId();
		
		if(StringUtils.isNotBlank(systemId)){
			Element subIdValNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
			subIdValNode.setTextContent(systemId);			
		}
				
		Element idSrcTxtNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
		idSrcTxtNode.setTextContent("Subscriptions");		
	}


	private static void buildSubjectElement(Element parentNode, Subscription subscription){
		
		Element subjectNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");		

		buildDobNode(subjectNode, subscription);
		
		buildPersonNameNode(subjectNode,subscription);
		
		buildPesonAugmentationElement(subjectNode,subscription);				
	}
	
	
	private static void buildDobNode(Element subjectNode, Subscription subscription) {

		Date dob = subscription.getDateOfBirth();
		
		if(dob != null){
			Element personBirthDateNode = XmlUtils.appendElement(subjectNode, OjbcNamespaceContext.NS_NC, "PersonBirthDate");	
			
			Element dateNode = XmlUtils.appendElement(personBirthDateNode, OjbcNamespaceContext.NS_NC, "Date");
			
			String sDob = sdf.format(dob);
			dateNode.setTextContent(sDob);
		}		
	}


	private static void buildPesonAugmentationElement(Element parentNode, Subscription subscription){
		
		String sid = subscription.getStateId();
		
		String fbiId = subscription.getFbiId();
		
		boolean hasSid = StringUtils.isNotEmpty(sid);
		
		boolean hasFbiId = StringUtils.isNotEmpty(fbiId);
		
		logger.info("\n\n subscription = \n" + subscription + "\n\n\n");
		
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
	
	private static void buildPersonNameNode(Element parentNode, Subscription subscription){
		
		Element personNameNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_NC, "PersonName");		
				
		String sFirstName = subscription.getFirstName();
		if(StringUtils.isNotBlank(sFirstName)){
			Element firstNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonGivenName");
			firstNameNode.setTextContent(sFirstName);
		}
		
		String sLastName = subscription.getLastName();
		if(StringUtils.isNotBlank(sLastName)){
			Element lastNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonSurName");
			lastNameNode.setTextContent(sLastName);
		}
				
		String fullName = subscription.getFullName();
		if(StringUtils.isNotBlank(fullName)){
			Element fullNameNode = XmlUtils.appendElement(personNameNode, OjbcNamespaceContext.NS_NC, "PersonFullName");
			fullNameNode.setTextContent(fullName);
		}		
	}

	
	
	private static void buildFilterElement(Element parentElement, Subscription subscription){
		
		Element filterElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_B2, "Filter");
						
		String subscriptionType = subscription.getSubscriptionType();
		
		if(StringUtils.isNotBlank(subscriptionType)){			
			Element topicExpNode = XmlUtils.appendElement(filterElement, OjbcNamespaceContext.NS_B2, "TopicExpression");		
			XmlUtils.addAttribute(topicExpNode, null, "Dialect", TOPIC_EXPRESSION_DIALECT);			
			topicExpNode.setTextContent(subscriptionType);			
		}		
	}
	

	private static Element buildConsumerRefElement(Element parentElement){
		
		Element consumerRefElement = XmlUtils.appendElement(parentElement, 
				OjbcNamespaceContext.NS_B2, "ConsumerReference");
		
		Element addrNode =  XmlUtils.appendElement(consumerRefElement, OjbcNamespaceContext.NS_ADD, "Address");
		addrNode.setTextContent(CONSUMER_REF_ADDRESS);		
		consumerRefElement.appendChild(addrNode);
		
		Element refParamsNode = XmlUtils.appendElement(consumerRefElement, OjbcNamespaceContext.NS_ADD, "ReferenceParameters");
		consumerRefElement.appendChild(refParamsNode);
		
		Element metaDataNode = XmlUtils.appendElement(consumerRefElement, OjbcNamespaceContext.NS_ADD, "Metadata");
		consumerRefElement.appendChild(metaDataNode);
				
		return consumerRefElement;
	}
	
	
	private static Element getRootSubscribeElement(Document responseDoc){
		
		 Element root = responseDoc.createElementNS(OjbcNamespaceContext.NS_B2, "Subscribe");
	        
	        responseDoc.appendChild(root);
	        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
	        	        
	        return root;
	}
	
	private static Document createBlankDoc() throws ParserConfigurationException{
	
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        
        return docBuilder.newDocument();
	}
	
	public static Document createUnubscriptionRequest(Unsubscription unsubscription) throws Exception{
		
		String subscriptionIdentificationId = unsubscription.getSubscriptionId();
		
		Document doc = OJBCXMLUtils.createDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Unsubscribe");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
		
        Element unsubscriptionMessage = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_UNBSUB_MSG_EXCHANGE, "UnsubscriptionMessage");
        
        Element subscriptionIdentification = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionIdentification");
        
        Element identificationID = XmlUtils.appendElement(subscriptionIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
        identificationID.setTextContent(subscriptionIdentificationId);
                
		String reasonCode = unsubscription.getReasonCode();
        if (CIVIL_SUBSCRIPTION_REASON_CODE.equals(reasonCode)){
	        Element reasonCodeElement = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CivilSubscriptionReasonCode");
	        reasonCodeElement.setTextContent(reasonCode);
        }
        else{
	        Element reasonCodeElement = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
	        reasonCodeElement.setTextContent(reasonCode);
        }
        
		Element topicExpNode = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_B2, "TopicExpression");		
		XmlUtils.addAttribute(topicExpNode, null, "Dialect", TOPIC_EXPRESSION_DIALECT);		
		topicExpNode.setTextContent(unsubscription.getTopic());
		
		OjbcNamespaceContext ojbNamespaceCtxt = new OjbcNamespaceContext();
		ojbNamespaceCtxt.populateRootNamespaceDeclarations(doc.getDocumentElement());
		
		return doc;
	}

}


