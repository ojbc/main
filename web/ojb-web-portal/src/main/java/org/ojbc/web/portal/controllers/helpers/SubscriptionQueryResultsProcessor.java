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
package org.ojbc.web.portal.controllers.helpers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class SubscriptionQueryResultsProcessor {
	
	@Value("#{getObject('triggeringEventCodeTranslationInverseMap')}")
	private Map<String, String> triggeringEventCodeTranslationInverseMap;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	
	public Subscription parseSubscriptionQueryResults(Document subscriptionQueryResponseDoc) throws Exception{
		
		Subscription subscription = new Subscription();
		
		Node rootSubQueryResultsNode = XmlUtils.xPathNodeSearch(subscriptionQueryResponseDoc, "sqr:SubscriptionQueryResults");
							
		Node subQueryResultNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult");
		parseSubscriptionQueryResultNode(subQueryResultNode, subscription);	
						
		Node personNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:Person");
		parsePersonNode(personNode, subscription);
		
		parseSubscriptionContactInfoNode(rootSubQueryResultsNode, subscription);
		
		parseSubscriptionOwnerInfo(rootSubQueryResultsNode, subscription);
		parseFbiSubscriptionInfo(rootSubQueryResultsNode, subscription);
		parseFederalInformationNodes(subQueryResultNode, subscription);
		
		return subscription;
	}		
	
	private void parseFbiSubscriptionInfo(Node rootSubQueryResultsNode,
			Subscription subscription) throws Exception{
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription(); 
		Node fbiSubscriptionNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:FBISubscription");
		
		if (fbiSubscriptionNode != null){
			String fbiSubscriptionId = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:SubscriptionFBIIdentification/nc:IdentificationID");
			fbiRapbackSubscription.setFbiSubscriptionId(fbiSubscriptionId);
			
			String startDateString = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "nc:ActivityDateRange/nc:StartDate/nc:Date");
			fbiRapbackSubscription.setRapbackStartDate(Optional.ofNullable(startDateString).map(LocalDate::parse).orElse(null));
			
			String endDateString = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "nc:ActivityDateRange/nc:EndDate/nc:Date");
			fbiRapbackSubscription.setRapbackExpirationDate(Optional.ofNullable(endDateString).map(LocalDate::parse).orElse(null));
			
			String ucn = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:SubscriptionFBIIdentification/nc:IdentificationID");
			fbiRapbackSubscription.setUcn(ucn);
			
			String subscriptionReasonCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:CriminalSubscriptionReasonCode|sqr-ext:CivilSubscriptionReasonCode");
			fbiRapbackSubscription.setRapbackCategory(subscriptionReasonCode);
			
			String termCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:RapBackSubscriptionTermCode");
			fbiRapbackSubscription.setSubscriptionTerm(termCode);
			
			String rapBackActivityNotificationFormatCode = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:RapBackActivityNotificationFormatCode");
			fbiRapbackSubscription.setRapbackActivityNotificationFormat(rapBackActivityNotificationFormatCode);
			
			String rapbackOptOutInState = XmlUtils.xPathStringSearch(fbiSubscriptionNode, "sqr-ext:RapBackInStateOptOutIndicator");
			fbiRapbackSubscription.setRapbackOptOutInState(BooleanUtils.toBooleanObject(rapbackOptOutInState));

		}
		
		subscription.setFbiRapbackSubscription(fbiRapbackSubscription);
	}

	private void parseSubscriptionOwnerInfo(Node rootSubQueryResultsNode,
			Subscription subscription) throws Exception {
		
		Node subscribedEntity = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:SubscribedEntity");
		String subscribedEntityId = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:SubscribedEntity/@s:id");
		String ownerContactInfoId = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, 
				"sqr-ext:SubscribedEntityContactInformationAssociation[sqr-ext:SubscribedEntityReference/@s:ref='" + subscribedEntityId+ "']"
						+ "/nc:ContactInformationReference/@s:ref");
		String ownerEmailAddress = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, "nc:ContactInformation[@s:id='"+ ownerContactInfoId +"']/nc:ContactEmailID");
		subscription.setOwnerEmailAddress(ownerEmailAddress);
		
		String ownerFirstName = XmlUtils.xPathStringSearch(subscribedEntity, "nc:EntityPerson/nc:PersonName/nc:PersonGivenName");
		subscription.setOwnerFirstName(ownerFirstName);
		String ownerLastName = XmlUtils.xPathStringSearch(subscribedEntity, "nc:EntityPerson/nc:PersonName/nc:PersonSurName");
		subscription.setOwnerLastName(ownerLastName);
		
		String ownerFederationId = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, 
				"sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:SubscriptionOriginator/sqr-ext:SubscriptionOriginatorIdentification/nc:IdentificationID");
		subscription.setOwnerFederationId(ownerFederationId);
		
		String ori = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, "jxdm41:Organization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
		subscription.setOri(ori);
		String agencyName = XmlUtils.xPathStringSearch(rootSubQueryResultsNode, "jxdm41:Organization/nc:OrganizationName");
		subscription.setAgencyName(agencyName);
	}

	private void parseFederalInformationNodes(Node subQueryResultNode,
			Subscription subscription) throws Exception {
		
		Node subscriptionNode = XmlUtils.xPathNodeSearch(subQueryResultNode, "sqr-ext:Subscription");
		
		String federalRapSheetDisclosureIndicator = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:FederalRapSheetDisclosure/sqr-ext:FederalRapSheetDisclosureIndicator");
		
		subscription.setFederalRapSheetDisclosureIndicator(BooleanUtils.toBooleanObject(federalRapSheetDisclosureIndicator));
		
		String federalRapSheetDisclosureAttentionDesignationText = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:FederalRapSheetDisclosure/sqr-ext:FederalRapSheetDisclosureAttentionDesignationText");
		
		subscription.setFederalRapSheetDisclosureAttentionDesignationText(federalRapSheetDisclosureAttentionDesignationText);
		
		NodeList triggeringEventCodes =XmlUtils.xPathNodeListSearch(subscriptionNode, "sqr-ext:TriggeringEvents/sqr-ext:FederalTriggeringEventCode");
		
		if (triggeringEventCodes != null)
		{	
			Set<String> triggeringEventCodesList = new HashSet<String>();
			
			for(int i=0; i<triggeringEventCodes.getLength(); i++){			
				Node triggeringEventCode = triggeringEventCodes.item(i);
				
				if (triggeringEventCodeTranslationInverseMap != null){
					triggeringEventCodesList.add(triggeringEventCodeTranslationInverseMap.get(triggeringEventCode.getTextContent().trim()));
				}
				else{
					triggeringEventCodesList.add(triggeringEventCode.getTextContent().trim());
				}
			}

			subscription.getFederalTriggeringEventCode().addAll(triggeringEventCodesList);
		}	
		
		String subscriptionPurpose = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:CriminalSubscriptionReasonCode|sqr-ext:CivilSubscriptionReasonCode");
		subscription.setSubscriptionPurpose(subscriptionPurpose);
		
		String caseId = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID");
		subscription.setCaseId(caseId);
	}

	private void parseSubscriptionQueryResultNode(Node subQueryResultNode, 
			Subscription subscription) throws Exception{
					
		Node subscriptionNode = XmlUtils.xPathNodeSearch(subQueryResultNode, "sqr-ext:Subscription");
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subscriptionNode, "nc:ActivityDateRange");		
		parseSubscriptionDateNode(dateRangeNode, subscription);				
				
		String topic = XmlUtils.xPathStringSearch(subscriptionNode, "wsn-br:Topic");
		subscription.setTopic(topic.trim());

		String systemId = XmlUtils.xPathStringSearch(subQueryResultNode, "intel:SystemIdentifier/nc:IdentificationID");
		subscription.setSystemId(systemId);		
		
		String systemName = XmlUtils.xPathStringSearch(subQueryResultNode, "intel:SystemIdentifier/intel:SystemName");
		subscription.setSystemName(systemName);		
		
		String activeString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionActiveIndicator");
		subscription.setActive(BooleanUtils.toBooleanObject(activeString));
		
		String subscriptionQualifierId = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		subscription.setSubscriptionQualificationId(subscriptionQualifierId);
		
		String creationDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionCreationDate/nc:Date");
		subscription.setCreationDate(Optional.ofNullable(creationDateString).map(LocalDate::parse).orElse(null));
		
		String lastUpdateDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionLastUpdatedDate/nc:Date");
		subscription.setLastUpdatedDate(Optional.ofNullable(lastUpdateDateString).map(LocalDate::parse).orElse(null));
		
		String validationDueDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidationDueDate/nc:Date");
		subscription.setValidationDueDate(Optional.ofNullable(validationDueDateString).map(LocalDate::parse).orElse(null));
		
		String lastValidatedDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionValidation/sqr-ext:SubscriptionValidatedDate/nc:Date");
		subscription.setLastValidationDate(Optional.ofNullable(lastValidatedDateString).map(LocalDate::parse).orElse(null));
		
		String gracePeriodStartDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionGracePeriod/sqr-ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date");
		subscription.setGracePeriodStartDate(Optional.ofNullable(gracePeriodStartDateString).map(LocalDate::parse).orElse(null));
		
		String gracePeriodEndDateString = XmlUtils.xPathStringSearch(subscriptionNode, "sqr-ext:SubscriptionGracePeriod/sqr-ext:SubscriptionGracePeriodDateRange/nc:EndDate/nc:Date");
		subscription.setGracePeriodEndDate(Optional.ofNullable(gracePeriodEndDateString).map(LocalDate::parse).orElse(null));
	}	
	
	private void parseSubscriptionDateNode(Node dateRangeNode, Subscription subscription) throws Exception{				

		String sStartDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");			
		if(StringUtils.isNotEmpty(sStartDate)){
			Date dStartDate = sdf.parse(sStartDate.trim());		
			subscription.setSubscriptionStartDate(dStartDate);			
		}		
		
		String sEndDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate");		
		if(StringUtils.isNotEmpty(sEndDate)){
			Date dEndDate = sdf.parse(sEndDate.trim());
			subscription.setSubscriptionEndDate(dEndDate);			
		}
	}
	
	
	private void parsePersonNode(Node personNode, Subscription subscription) throws Exception{		
		      
		String sDob = XmlUtils.xPathStringSearch(personNode, "nc:PersonBirthDate/nc:Date");
		if(StringUtils.isNotBlank(sDob)){
			Date dDob = sdf.parse(sDob);
			subscription.setDateOfBirth(dDob);			
		}
		      
		String sFullName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonFullName");
		subscription.setFullName(sFullName);
		
		String sFirstName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonGivenName");
		subscription.setFirstName(sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonSurName");
		subscription.setLastName(sLastName);
				
		String sid = XmlUtils.xPathStringSearch(personNode, 
				"jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		subscription.setStateId(sid);
		
		String fbiId = XmlUtils.xPathStringSearch(personNode, 
				"jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
		
		subscription.setFbiId(fbiId);
	}
	
	
	private void parseSubscriptionContactInfoNode(Node rootSubQueryResultsNode, Subscription subscription) throws Exception{
		
		NodeList contactInfoNodeList = XmlUtils.xPathNodeListSearch(rootSubQueryResultsNode, "nc:ContactInformation[@s:id=following-sibling::sqr-ext:SubscriptionContactInformationAssociation/nc:ContactInformationReference/@s:ref]");
		
		for(int i=0; i<contactInfoNodeList.getLength(); i++){
			
			Node iContactInfoNode = contactInfoNodeList.item(i);			
			String iEmail = XmlUtils.xPathStringSearch(iContactInfoNode, "nc:ContactEmailID");		
			
			if(StringUtils.isNotBlank(iEmail)){
				subscription.getEmailList().add(iEmail.trim());			
			}							
		}		
	}

}


