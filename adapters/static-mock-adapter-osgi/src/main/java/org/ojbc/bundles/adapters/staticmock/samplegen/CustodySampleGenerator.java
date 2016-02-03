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
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodySampleGenerator extends AbstractSampleGenerator{

	public CustodySampleGenerator() throws ParserConfigurationException,
			IOException {

		super();
	}
	
	
	public List<Document> generateCustodySamples(int recordCount) throws Exception {
		
		List<Document> rCustodyDocList = new ArrayList<Document>(recordCount);
		
		for(int i=0; i < recordCount; i++){
		
			PersonElementWrapper iGeneratedPerson = getRandomIdentity(null);
			
			Document custodyDoc = buildCustodyDetailDoc(iGeneratedPerson);
			
			rCustodyDocList.add(custodyDoc);
		}
		
		return rCustodyDocList;
	}
	
	
	Document buildCustodyDetailDoc(PersonElementWrapper person) throws ParserConfigurationException{
		
		Document rCustodyDetailDoc = getNewDocument();
		
		Element rootCustodyResultsElement = rCustodyDetailDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXCH_DOC, "CustodyQueryResults"); 
		
		rootCustodyResultsElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_QUERY_RESULTS_EXCH_DOC);
		
		rCustodyDetailDoc.appendChild(rootCustodyResultsElement);
		
		Element docCreationDateElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
		
		Element dateTimeElement = XmlUtils.appendElement(docCreationDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		dateTimeElement.setTextContent("TODO");
		
		Element documentIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
		
		Element docIdValElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		docIdValElement.setTextContent("TODO");
		
		Element idCatDescTxtElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		
		idCatDescTxtElement.setTextContent("TODO");
		
		Element sysIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");
		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_INTEL_31, "IdentificationID");
		
		sysIdValElement.setTextContent("TODO");
		
		Element sysIdSystemNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
		
		sysIdSystemNameElement.setTextContent("TODO");
		
		
		Element inmateCustodyElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InmateCustody");
		
		Element personBirthDateElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		
		Element personDobValElement = XmlUtils.appendElement(personBirthDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		personDobValElement.setTextContent("TODO");
		
		
		Element ethnicityElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonEthnicityText");
		
		ethnicityElement.setTextContent("TODO");
		
		
		Element personNameElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		personGivenNameElement.setTextContent("TODO");
		
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		personMiddleNameElement.setTextContent("TODO");
		
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent("TODO");
		
		Element personRaceElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonRaceText");
		personRaceElement.setTextContent("TODO");
		
		Element personSexElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSexText");
		personSexElement.setTextContent("TODO");
		
		Element personSsnElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");		
		Element personSsnValElement = XmlUtils.appendElement(personSsnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSsnValElement.setTextContent("TODO");
		
		Element personSidElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonStateIdentification");		
		Element personSidValElement = XmlUtils.appendElement(personSidElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSidValElement.setTextContent("TODO");
		
		Element bailBondElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
		
		Element activityCatElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		activityCatElement.setTextContent("TODO");
		
		Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondAmount");
		
		Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");
		
		bailBondAmountValElement.setTextContent("TODO");
		
		
		Element bookingElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Booking");
		
		Element fingerprintDateElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "FingerprintDate");
		
		Element fingerprintDateValElement = XmlUtils.appendElement(fingerprintDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		fingerprintDateValElement.setTextContent("TODO");
		
		
		Element bookingSubjectElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
		
		Element bookingSubjectIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");
		
		Element bookingSubjectIdValElement = XmlUtils.appendElement(bookingSubjectIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		bookingSubjectIdValElement.setTextContent("TODO");
		
		
		Element imageElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "Image");
		
		Element imgLocElement = XmlUtils.appendElement(imageElement, OjbcNamespaceContext.NS_NC_30, "ImageLocation");
		
		Element imgLocDescElement = XmlUtils.appendElement(imgLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");
		
		imgLocDescElement.setTextContent("TODO");
		
		Element caseHearingElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "CaseHearing");
		
		Element courtEventScheduleElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventSchedule");
		
		Element scheduleDayStartTimeElement = XmlUtils.appendElement(courtEventScheduleElement, OjbcNamespaceContext.NS_NC_30, "ScheduleDayStartTime");
		
		scheduleDayStartTimeElement.setTextContent("TODO");
		
		
		Element chargeElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");
		
		Element chargeCountQuantityElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");		
		chargeCountQuantityElement.setTextContent("TODO");
		
		Element chargeDescriptionTextElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
		chargeDescriptionTextElement.setTextContent("TODO");
		
		Element chargeStatuteElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element chargeStatuteCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		chargeStatuteCodeIdValElement.setTextContent("TODO");
		
		Element chargeStatuteIdCatDescTxtElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");		
		chargeStatuteIdCatDescTxtElement.setTextContent("TODO");
		
		Element supervisionElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Supervision");
		
		Element activityDateRangeElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_NC_30, "ActivityDateRange");
		
		Element startDateElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC_30, "StartDate");
		
		Element startDateValElement = XmlUtils.appendElement(startDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		startDateValElement.setTextContent("TODO");
		
			
		Element endDateElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC_30, "EndDate");
		
		Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		endDateValElement.setTextContent("TODO");
		
		Element supervisionFacElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_NC_30, "SupervisionFacility");
		
		Element facIdElement = XmlUtils.appendElement(supervisionFacElement, OjbcNamespaceContext.NS_NC_30, "FacilityIdentification");
		
		Element facIdValElement = XmlUtils.appendElement(facIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		facIdValElement.setTextContent("TODO");
		
		Element facIdCatDescTxtElement = XmlUtils.appendElement(facIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		facIdCatDescTxtElement.setTextContent("TODO");
		
		Element supervisionAugmentElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAugmentation");
		
		Element supervisionAreaIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAreaIdentification");
		
		Element supervisionAreaIdValElement = XmlUtils.appendElement(supervisionAreaIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		supervisionAreaIdValElement.setTextContent("TODO");
		
		Element supervisionAreaIdCatDescTxtElement = XmlUtils.appendElement(supervisionAreaIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		supervisionAreaIdCatDescTxtElement.setTextContent("TODO");
		
		Element supervisionBedIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionBedIdentification");
		
		Element supervisionBedIdValElement = XmlUtils.appendElement(supervisionBedIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		supervisionBedIdValElement.setTextContent("TODO");
		
		Element supervisionBedIdCatDescTxtElement = XmlUtils.appendElement(supervisionBedIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		supervisionBedIdCatDescTxtElement.setTextContent("TODO");
		
		Element supervisionCellIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionCellIdentification");
		
		Element supervisionCellIdValElement = XmlUtils.appendElement(supervisionCellIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		supervisionCellIdValElement.setTextContent("TODO");
		
		Element supervisionIdCatDescTxtElement = XmlUtils.appendElement(supervisionCellIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");			
		supervisionIdCatDescTxtElement.setTextContent("TODO");
		
		Element holdForImigElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "HoldForImmigrationIndicator");
		holdForImigElement.setTextContent("TODO");
		
		Element chargeSentenceElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeSentence");
		
		Element sentenceConditionElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_JXDM_51, "SentenceCondition");
		
		Element conditionSetDateElement = XmlUtils.appendElement(sentenceConditionElement, OjbcNamespaceContext.NS_NC_30, "ConditionSetDate");
		
		Element conditionSetDateValElement = XmlUtils.appendElement(conditionSetDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		conditionSetDateValElement.setTextContent("TODO");
		
		Element organizationElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_NC_30, "Organization");
		
		Element organizationNameElement = XmlUtils.appendElement(organizationElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");	
		organizationNameElement.setTextContent("TODO");
		
		Element infoOwningOrgElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InformationOwningOrganization");
		
		Element orgBranchNameElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");		
		orgBranchNameElement.setTextContent("TODO");
		
		Element orgNameElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
		orgNameElement.setTextContent("TODO");
		
		Element metadataElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "Metadata");
		
		Element lastUpdatedDateElement = XmlUtils.appendElement(metadataElement, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
		
		Element lastUpdatedDateValElement = XmlUtils.appendElement(lastUpdatedDateElement, OjbcNamespaceContext.NS_NC_30, "Date");
		
		lastUpdatedDateValElement.setTextContent("TODO");
		
		
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootCustodyResultsElement);
		
		return rCustodyDetailDoc;
	}
	
	
	private Document getNewDocument() throws ParserConfigurationException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();		

		Document doc = docBuilder.newDocument();
		
		return doc;
	}

}



