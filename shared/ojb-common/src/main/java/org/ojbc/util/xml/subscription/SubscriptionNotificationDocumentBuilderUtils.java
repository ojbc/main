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
package org.ojbc.util.xml.subscription;

import static org.ojbc.util.helper.UniqueIdUtils.getUniqueId;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SUB_MSG_EXT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SubscriptionNotificationDocumentBuilderUtils {				

	public static final String CRIMINAL_JUSTICE_INVESTIGATIVE = "CI";
	public static final String CRIMINAL_JUSTICE_SUPERVISION = "CS";
	public static final String FIREARMS = "F";
	public static final String NON_CRIMINAL_JUSTICE_EMPLOYMENT = "I";
	public static final String CRIMINAL_JUSTICE_EMPLOYMENT = "J";
	public static final String SECURITY_CLEARANCE_INFORMATION_ACT = "S";
	
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SubscriptionNotificationDocumentBuilderUtils.class);
	
	private static final String SYSTEM_NAME = "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB";
	
	private static final String CONSUMER_REF_ADDRESS = "http://www.ojbc.org/OJB/SubscribeNotify";
	
	private static final String TOPIC_EXPRESSION_DIALECT = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete";	
	
	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Document createSubscriptionModifyRequest(Subscription subscription) throws ParserConfigurationException{
		
        Document doc = createBlankDoc();        
		
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "b-2:Modify");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);        
        
		buildSubscriptionModificationMessageNode(root, subscription, null);
		
		OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		
		return doc;
	}
	
	private static void buildSubscriptionModificationMessageNode(Element parentNode, 
			Subscription subscription, Map<String, String> triggeringEventCodeTranslationMap){
		
        Element subscriptionModificationMessage = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MODIFY_MESSAGE, "smm:SubscriptionModificationMessage");
	
		buildOrganizationOriElement(subscriptionModificationMessage, subscription);
		
		buildCaseIdElement(subscriptionModificationMessage, subscription);
												
		buildSubjectElement(subscriptionModificationMessage,subscription);	
		
		buildSubQualIdNode(subscriptionModificationMessage, subscription);
			
		buildSubscriptionIdNode(subscriptionModificationMessage, subscription);
		
		buildSubscriptionReasonCodeElement(subscriptionModificationMessage, subscription.getSubscriptionPurpose());
		
		buildTriggeringEvents(subscriptionModificationMessage, subscription, triggeringEventCodeTranslationMap);
		
		buildFederalRapSheetDisclosure(subscriptionModificationMessage, subscription);
		
		//Insert FBI ID here
		buildFBISubscriptionIDNode(subscriptionModificationMessage, subscription);
		
		Element subscriptionModification = XmlUtils.appendElement(subscriptionModificationMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:SubscriptionModification");
		
		buildDateRangeNode(subscriptionModification, subscription);
	}	
	
	private static void buildFBISubscriptionIDNode(
			Element subscriptionModificationMessage, Subscription subscription) {
		
		Element fbiSubscription = XmlUtils.appendElement(subscriptionModificationMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:FBISubscription");
		
		Element subscriptionFBIIdentification = XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:SubscriptionFBIIdentification");
		
		Element idNode = XmlUtils.appendElement(subscriptionFBIIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
		
		idNode.setTextContent(subscription.getFbiSubscriptionID());
		
		Element criminalSubscriptionReasonCode = XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:CriminalSubscriptionReasonCode");
		criminalSubscriptionReasonCode.setTextContent(subscription.getSubscriptionPurpose());
		

		
	}
	
	public static Document createSubscriptionRequest(Subscription subscription, 
			Map<String,String> triggeringEventCodeTranslationMap) throws ParserConfigurationException{
		
        Document subMsgDoc = createBlankDoc();        
        Element rootSubscribeElement = getRootSubscribeElement(subMsgDoc); 

		buildConsumerRefElement(rootSubscribeElement);
		
		buildFilterElement(rootSubscribeElement, subscription);
						
		XmlUtils.appendElement(rootSubscribeElement, OjbcNamespaceContext.NS_B2, "SubscriptionPolicy");
				
		buildSubscriptionMessageNode(rootSubscribeElement, subscription, triggeringEventCodeTranslationMap);
		
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
	
	
	private static void buildSubQualIdNode(Element parentNode, Subscription subscription){
		
		Element subQualIdNode =  XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionQualifierIdentification");
		
		Element idNode = XmlUtils.appendElement(subQualIdNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
				
		if (StringUtils.isNotBlank(subscription.getSubscriptionQualificationId()))
		{
			idNode.setTextContent(subscription.getSubscriptionQualificationId());
		}
		else
		{
			idNode.setTextContent( getUniqueId());
		}	
	}
	
	
	private static void buildSubscriptionMessageNode(Element parentNode, 
			Subscription subscription, Map<String, String> triggeringEventCodeTranslationMap){
		
		Element subMsgNode = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXCHANGE, "SubscriptionMessage");
		
		buildCaseIdElement(subMsgNode, subscription);

		buildOrganizationOriElement(subMsgNode, subscription);
		
		buildSubjectElement(subMsgNode,subscription);	
		
		buildEmailElements(subMsgNode, subscription.getEmailList());
		
		Element sysNameNode = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SystemName");
		
		if (StringUtils.isNotBlank(subscription.getSystemName()))
		{
			sysNameNode.setTextContent(subscription.getSystemName());
		}	
		else
		{	
			sysNameNode.setTextContent(SYSTEM_NAME);
		}	
		
		if (StringUtils.isNotBlank(subscription.getTransactionNumber())){
			Element fingerprintIdentificationTransactionIdentification = XmlUtils.appendElement(subMsgNode, NS_SUB_MSG_EXT, 
					"FingerprintIdentificationTransactionIdentification");
			XmlUtils.appendTextElement(fingerprintIdentificationTransactionIdentification, NS_NC, "IdentificationID", subscription.getTransactionNumber());
		}
		
		buildSubQualIdNode(subMsgNode, subscription);
			
		buildDateRangeNode(subMsgNode, subscription);		
		
		buildSubscriptionIdNode(subMsgNode, subscription);
		
		buildSubscriptionReasonCodeElement(subMsgNode, subscription.getSubscriptionPurpose());
		
		buildTriggeringEvents(subMsgNode, subscription, triggeringEventCodeTranslationMap);
		
		buildFederalRapSheetDisclosure(subMsgNode, subscription);
		
	}
	

	private static void buildOrganizationOriElement(Element subMsgNode,
			Subscription subscription) {
		
		if (StringUtils.isNotBlank(subscription.getOri()) || StringUtils.isNotBlank(subscription.getOwnerProgramOca())){
			Element subscribingOrganization = XmlUtils.appendElement(subMsgNode, NS_SUB_MSG_EXT, "SubscribingOrganization");

			if (StringUtils.isNotBlank(subscription.getOwnerProgramOca())){
				Element organizationIdentification = XmlUtils.appendElement(subscribingOrganization, NS_NC, "OrganizationIdentification");
				XmlUtils.appendTextElement(organizationIdentification, NS_NC, "IdentificationID", subscription.getOwnerProgramOca());
			}
			
			if (StringUtils.isNotBlank(subscription.getOri())){
				Element organizationAugmentation = XmlUtils.appendElement(subscribingOrganization, NS_JXDM_41, "OrganizationAugmentation");
				Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_41, "OrganizationORIIdentification");
				XmlUtils.appendTextElement(organizationORIIdentification, NS_NC, "IdentificationID", subscription.getOri());
			}
		}
		
	}


	private static void buildFederalRapSheetDisclosure(Element subMsgNode,
			Subscription subscription) {
//	<smext:FederalRapSheetDisclosure>
//		<smext:FederalRapSheetDisclosureIndicator>true</smext:FederalRapSheetDisclosureIndicator>
//		<smext:FederalRapSheetDisclosureAttentionDesignationText>Detective George Jones</smext:FederalRapSheetDisclosureAttentionDesignationText>
//	</smext:FederalRapSheetDisclosure>    	

    	Boolean federalRapSheetDisclosureIndicator = subscription.getFederalRapSheetDisclosureIndicator();
    	String federalRapSheetDisclosureAttentionDesignationText = subscription.getFederalRapSheetDisclosureAttentionDesignationText();

	    if (BooleanUtils.isTrue(federalRapSheetDisclosureIndicator))
	    {
	    	Element federalRapSheetDisclosureElement = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FederalRapSheetDisclosure");
	    
    		Element federalRapSheetDisclosureIndicatorElement = XmlUtils.appendElement(federalRapSheetDisclosureElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FederalRapSheetDisclosureIndicator");
    		federalRapSheetDisclosureIndicatorElement.setTextContent(BooleanUtils.toStringTrueFalse(federalRapSheetDisclosureIndicator));

    		if (StringUtils.isNotBlank(federalRapSheetDisclosureAttentionDesignationText)){
	    		Element federalRapSheetDisclosureAttentionDesignationTextElement = XmlUtils.appendElement(federalRapSheetDisclosureElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FederalRapSheetDisclosureAttentionDesignationText");
	    		federalRapSheetDisclosureAttentionDesignationTextElement.setTextContent(federalRapSheetDisclosureAttentionDesignationText);
    		}

	    }	
		
	}


	private static void buildTriggeringEvents(Element subMsgNode,
			Subscription subscription, Map<String, String> triggeringEventCodeTranslationMap) {
//	<submsg-ext:TriggeringEvents>
//		<submsg-ext:FederalTriggeringEventCode>ARREST</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>DEATH</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-SOR-ENTRY</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-SOR-MODIFICATION</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-SOR-DELETION</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-WARRANT-ENTRY</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-WARRANT-MODIFICATION</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>NCIC-WARRANT-DELETION</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>DISPOSITION</submsg-ext:FederalTriggeringEventCode>
//		<submsg-ext:FederalTriggeringEventCode>DISPOSITION</submsg-ext:FederalTriggeringEventCode>
//	</submsg-ext:TriggeringEvents>
		
		
		if (subscription.getFederalTriggeringEventCode() != null && !subscription.getFederalTriggeringEventCode().isEmpty()) {	
			Element triggeringEventsElement = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "TriggeringEvents");
			
			if (triggeringEventCodeTranslationMap != null)
			{	
				List<String> translatedTriggeringEventCode = new ArrayList<>();
				subscription.getFederalTriggeringEventCode().stream()
					.map(triggeringEventCodeTranslationMap::get)
					.forEach(code -> translatedTriggeringEventCode.addAll(Arrays.asList(StringUtils.split(code, ','))));
				
		    	for (String triggeringEventCode : translatedTriggeringEventCode) {
	    	    	Element federalTriggeringEventCode = XmlUtils.appendElement(triggeringEventsElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FederalTriggeringEventCode");
	    	    	federalTriggeringEventCode.setTextContent(triggeringEventCode);
		    	}	

			}	
			else
			{
		    	for (String triggeringEventCode : subscription.getFederalTriggeringEventCode()) {
	    	    	Element federalTriggeringEventCode = XmlUtils.appendElement(triggeringEventsElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FederalTriggeringEventCode");
	    	    	federalTriggeringEventCode.setTextContent(triggeringEventCode);
		    	}	
			}
			
		}	
	}


	private static void buildCaseIdElement(Element parentNode, Subscription subscription) {
		
		String caseId = subscription.getCaseId();
		
		if(StringUtils.isNotEmpty(caseId)){
			
			Element subRelCaseIdElement = XmlUtils.appendElement(parentNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionRelatedCaseIdentification");
			
			Element caseIdValElement = XmlUtils.appendElement(subRelCaseIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");		
			
			caseIdValElement.setTextContent(caseId);
		}		
	}
		
	private static void buildSubscriptionReasonCodeElement(Element parentElement, String subscriptionReasonCode){
		
		if(StringUtils.isNotEmpty(subscriptionReasonCode)){
		
			if (CRIMINAL_JUSTICE_EMPLOYMENT.equals(subscriptionReasonCode) || FIREARMS.equals(subscriptionReasonCode) || NON_CRIMINAL_JUSTICE_EMPLOYMENT.equals(subscriptionReasonCode) || SECURITY_CLEARANCE_INFORMATION_ACT.equals(subscriptionReasonCode) )
			{
				Element subReasonCodeElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CivilSubscriptionReasonCode");
				subReasonCodeElement.setTextContent(subscriptionReasonCode);
			}
			if (CRIMINAL_JUSTICE_INVESTIGATIVE.equals(subscriptionReasonCode) || CRIMINAL_JUSTICE_SUPERVISION.equals(subscriptionReasonCode))
			{
				Element subReasonCodeElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
				subReasonCodeElement.setTextContent(subscriptionReasonCode);
			}
		}
	}
		
	private static void buildSubscriptionIdNode(Element subMsgNode, Subscription subscription) {

		if(StringUtils.isNotBlank(subscription.getSystemId()))
		{	
			Element subIdNode = XmlUtils.appendElement(subMsgNode, OjbcNamespaceContext.NS_SUB_MSG_EXT, "smext:SubscriptionIdentification");
			
			String systemId = subscription.getSystemId();
			
			Element subIdValNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationID");
			subIdValNode.setTextContent(systemId);			
					
			Element idSrcTxtNode = XmlUtils.appendElement(subIdNode, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
			idSrcTxtNode.setTextContent("Subscriptions");
		}
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
						
		String topic = subscription.getTopic();
		
		if(StringUtils.isNotBlank(topic)){			
			Element topicExpNode = XmlUtils.appendElement(filterElement, OjbcNamespaceContext.NS_B2, "TopicExpression");		
			XmlUtils.addAttribute(topicExpNode, null, "Dialect", TOPIC_EXPRESSION_DIALECT);			
			topicExpNode.setTextContent(topic);			
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
		boolean containsSubjectData = ((StringUtils.isNotBlank(unsubscription.getFirstName())) || (StringUtils.isNotBlank(unsubscription.getLastName())) || (StringUtils.isNotBlank(unsubscription.getSid())) || (unsubscription.getDateOfBirth()!=null));

		if (containsSubjectData && StringUtils.isNotEmpty(subscriptionIdentificationId))
		{
			throw new Exception("Unsubscription message can have either subject data or Subscription Identification, but not both.");
		}	
		
		Document doc = OJBCXMLUtils.createDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Unsubscribe");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
		
        Element unsubscriptionMessage = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_UNBSUB_MSG_EXCHANGE, "UnsubscriptionMessage");
        
//        <smext:Subject>
//	        <nc20:PersonBirthDate>
//	            <nc20:Date>1998-01-11</nc20:Date>
//	        </nc20:PersonBirthDate>
//	        <nc20:PersonName>
//	            <nc20:PersonGivenName>John</nc20:PersonGivenName>
//	            <nc20:PersonSurName>Doe</nc20:PersonSurName>
//	        </nc20:PersonName>
//	        <jxdm41:PersonAugmentation>
//		        <jxdm41:PersonStateFingerprintIdentification>
//		           <nc20:IdentificationID>A9999999</nc20:IdentificationID>
//		        </jxdm41:PersonStateFingerprintIdentification>
//		     </jxdm41:PersonAugmentation>        
//	    </smext:Subject>
        
        if (containsSubjectData)
        {
        	Element subject = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");
        	
        	if ((unsubscription.getDateOfBirth()!=null))
        	{
        		Element dob = XmlUtils.appendElement(subject, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        		Element dobDate = XmlUtils.appendElement(dob, OjbcNamespaceContext.NS_NC, "Date");
        		dobDate.setTextContent(unsubscription.getDateOfBirth().toString());
        	}	
        	
        	if (StringUtils.isNotBlank(unsubscription.getFirstName()) || StringUtils.isNotBlank(unsubscription.getLastName()))
        	{
        		Element personName = XmlUtils.appendElement(subject, OjbcNamespaceContext.NS_NC, "PersonName");
        		
        		if (StringUtils.isNotBlank(unsubscription.getFirstName()))
				{
            		Element personGivenName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC, "PersonGivenName");
            		personGivenName.setTextContent(unsubscription.getFirstName());
			
				}
        		
        		if (StringUtils.isNotBlank(unsubscription.getLastName()))
				{
            		Element personSurName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC, "PersonSurName");
            		personSurName.setTextContent(unsubscription.getLastName());
			
				}		

        	}
        	
        	if (StringUtils.isNotBlank(unsubscription.getSid()) || StringUtils.isNotBlank(unsubscription.getSid()))
        	{
        		Element personAugmentation = XmlUtils.appendElement(subject, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");

        		if (StringUtils.isNotBlank(unsubscription.getFbiNumber()))
        		{		
	        		Element personStateFingerprintIdentification = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
	        		
	        		Element identificationID = XmlUtils.appendElement(personStateFingerprintIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
	        		identificationID.setTextContent(unsubscription.getFbiNumber());
        		}

        		if (StringUtils.isNotBlank(unsubscription.getSid()))
        		{		
	        		Element personStateFingerprintIdentification = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");
	        		
	        		Element identificationID = XmlUtils.appendElement(personStateFingerprintIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
	        		identificationID.setTextContent(unsubscription.getSid());
        		}
        		
        	}	
        	
        	
        }	
        
//	    <nc20:ContactEmailID>william.francis@maine.gov</nc20:ContactEmailID>
//	    <smext:SystemName>{http://maine.gov/ProbationCase/1.0}MaineDOC</smext:SystemName>
//	    <smext:SubscriptionQualifierIdentification>
//	        <nc20:IdentificationID>128799</nc20:IdentificationID>
//	    </smext:SubscriptionQualifierIdentification>
        if (unsubscription.getEmailAddresses() != null)
        {	
	        for (String emailAddress : unsubscription.getEmailAddresses())
	        {
	        	Element contactEmailID = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_NC, "ContactEmailID");
	        	contactEmailID.setTextContent(emailAddress);
	        }
        }    
        
        if (StringUtils.isNotBlank(unsubscription.getSystemName()))
        {
        	Element systemName = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SystemName");
        	systemName.setTextContent(unsubscription.getSystemName());
        	
        }	

        if (StringUtils.isNotBlank(unsubscription.getSubscriptionQualifierIdentification()))
        {
        	Element subscriptionQualifierIdentification = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionQualifierIdentification");
        	
        	Element identificationID = XmlUtils.appendElement(subscriptionQualifierIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
        	identificationID.setTextContent(unsubscription.getSubscriptionQualifierIdentification());
        	
        }	
        
        if (StringUtils.isNotEmpty(subscriptionIdentificationId))
        {		
	        Element subscriptionIdentification = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionIdentification");
	        
	        Element identificationID = XmlUtils.appendElement(subscriptionIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
	        identificationID.setTextContent(subscriptionIdentificationId);
        }    
	        
		String subscriptionReasonCode = unsubscription.getReasonCode();
		
		buildSubscriptionReasonCodeElement(unsubscriptionMessage, subscriptionReasonCode);
		
		Element topicExpNode = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_B2, "TopicExpression");		
		XmlUtils.addAttribute(topicExpNode, null, "Dialect", TOPIC_EXPRESSION_DIALECT);		
		topicExpNode.setTextContent(unsubscription.getTopic());
		
		OjbcNamespaceContext ojbNamespaceCtxt = new OjbcNamespaceContext();
		ojbNamespaceCtxt.populateRootNamespaceDeclarations(doc.getDocumentElement());
		
		return doc;
	}

}


