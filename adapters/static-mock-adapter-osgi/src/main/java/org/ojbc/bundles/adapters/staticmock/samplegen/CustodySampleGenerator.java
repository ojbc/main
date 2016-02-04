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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodySampleGenerator extends AbstractSampleGenerator{

	private static final Random RANDOM = new Random();
	
	private static final String CURRENT_DATE = DateTime.now().toString("yyyy-MM-dd");
	
	
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
		dateTimeElement.setTextContent(CURRENT_DATE);
				
		
		
		Element documentIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");		
		Element docIdValElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sDocId = RandomStringUtils.randomNumeric(7);		
		docIdValElement.setTextContent(sDocId);		
		
		
		
		List<String> idCatDescSampleList = Arrays.asList("tall", "short", "bald", "dreadlocks");		
		int idCatDescRandomIndex = RANDOM.nextInt(idCatDescSampleList.size());		
		String idCatDesc = idCatDescSampleList.get(idCatDescRandomIndex);				
		Element idCatDescTxtElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");		
		idCatDescTxtElement.setTextContent(idCatDesc);
		
		
		
		Element sysIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_INTEL_31, "IdentificationID");		
		String systemId = RandomStringUtils.randomNumeric(6);		
		sysIdValElement.setTextContent(systemId);
		
		
		
		
		Element sysIdSystemNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
		
		sysIdSystemNameElement.setTextContent("Custody Detail");
		
		
		Element inmateCustodyElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InmateCustody");
		
		
		DateTime personDob = person.birthdate;
		Element personBirthDateElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");		
		Element personDobValElement = XmlUtils.appendElement(personBirthDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");	
		String sPersonDob = personDob.toString("yyyy-MM-dd");		
		personDobValElement.setTextContent(sPersonDob);			
		
		
		List<String> ethnicitySampleList = Arrays.asList("African American", "Latino", "Caucasion");		
		int ethnicityRandomIndex = RANDOM.nextInt(ethnicitySampleList.size());		
		String ethnicitySample = ethnicitySampleList.get(ethnicityRandomIndex);				
		Element ethnicityElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonEthnicityText");		
		ethnicityElement.setTextContent(ethnicitySample);
				
		
		
		Element personNameElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonName");		
				
		String sPersonFirstName = person.firstName;		
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		personGivenNameElement.setTextContent(sPersonFirstName);
		
		
		String sPersonMiddleName = person.middleName;
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		personMiddleNameElement.setTextContent(sPersonMiddleName);
		
		
		String sPersonLastName = person.lastName;
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent(sPersonLastName);
		
		
		List<String> raceSampleList = Arrays.asList("B", "A", "W");				
		int raceIndex = RANDOM.nextInt(raceSampleList.size());		
		String sRandomRace = raceSampleList.get(raceIndex);				
		Element personRaceElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonRaceText");		
		personRaceElement.setTextContent(sRandomRace);
		
		
		String sPersonSex = person.sex;
		Element personSexElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSexText");
		personSexElement.setTextContent(sPersonSex);
		
		String sPersonSSN = RandomStringUtils.randomNumeric(9);
		Element personSsnElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");		
		Element personSsnValElement = XmlUtils.appendElement(personSsnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSsnValElement.setTextContent(sPersonSSN);
		
		
		String sPersonSid = person.id;
		Element personSidElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonStateIdentification");		
		Element personSidValElement = XmlUtils.appendElement(personSidElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSidValElement.setTextContent(sPersonSid);
		
		Element bailBondElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
		
		Element activityCatElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		
		List<String> bailBondActivitySampleList = Arrays.asList("Traffic Violation", "Speeding", "Noise Violation");				
		int bailBondActivityIndex = RANDOM.nextInt(bailBondActivitySampleList.size());		
		String bailBondActivity = bailBondActivitySampleList.get(bailBondActivityIndex);		
		activityCatElement.setTextContent(bailBondActivity);
		
				
		Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondAmount");		
		Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");				
		String sBailAmount = RandomStringUtils.randomNumeric(4);		
		bailBondAmountValElement.setTextContent(sBailAmount);
		
		
		Element bookingElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Booking");		
		Element fingerprintDateElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "FingerprintDate");		
		Element fingerprintDateValElement = XmlUtils.appendElement(fingerprintDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		fingerprintDateValElement.setTextContent(CURRENT_DATE);
		
						
		Element bookingSubjectElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
		
		Element bookingSubjectIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");		
		Element bookingSubjectIdValElement = XmlUtils.appendElement(bookingSubjectIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");				
		String bookingSubjectId = RandomStringUtils.randomAlphanumeric(8);		
		bookingSubjectIdValElement.setTextContent(bookingSubjectId);
		
		
		
		List<String> imgLocSampleList = Arrays.asList("http://acme.org/profile_123.jpg", "http://pd.gov/profile_abc.jpg");		
		int imgLocRandomIndex = RANDOM.nextInt(imgLocSampleList.size());		
		String imgLocSample = imgLocSampleList.get(imgLocRandomIndex);						
		Element imageElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "Image");		
		Element imgLocElement = XmlUtils.appendElement(imageElement, OjbcNamespaceContext.NS_NC_30, "ImageLocation");		
		Element imgLocDescElement = XmlUtils.appendElement(imgLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");		
		imgLocDescElement.setTextContent(imgLocSample);
		
		
		
		Element caseHearingElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "CaseHearing");
		
		Element courtEventScheduleElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventSchedule");		
		Element scheduleDayStartTimeElement = XmlUtils.appendElement(courtEventScheduleElement, OjbcNamespaceContext.NS_NC_30, "ScheduleDayStartTime");		
		scheduleDayStartTimeElement.setTextContent(CURRENT_DATE);
		
		
		Element chargeElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");		
		Element chargeCountQuantityElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");				
		String sChargeCount = RandomStringUtils.randomNumeric(2);		
		chargeCountQuantityElement.setTextContent(sChargeCount);
		
		
		
		List<String> chargeDescriptionTxtList = Arrays.asList("Intoxicated", "Speeding", "No Seatbelt");		
		int chargeDescRandomIndex = RANDOM.nextInt(chargeDescriptionTxtList.size());		
		
		String sChargeDesc = chargeDescriptionTxtList.get(chargeDescRandomIndex);				
		Element chargeDescriptionTextElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
		chargeDescriptionTextElement.setTextContent(sChargeDesc);
		
		
		
		Element chargeStatuteElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element chargeStatuteCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		String sStatuteCodeId = RandomStringUtils.randomNumeric(6);		
		chargeStatuteCodeIdValElement.setTextContent(sStatuteCodeId);
		
		
		List<String> idCatDescTxtList = Arrays.asList("Driver License", "Gvmt Id Card", "Concealed Weapon Permit");		
		int idCatListIndex = RANDOM.nextInt(idCatDescTxtList.size());		
		String sIdCatDesc = idCatDescTxtList.get(idCatListIndex);
		
		Element chargeStatuteIdCatDescTxtElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		
		chargeStatuteIdCatDescTxtElement.setTextContent(sIdCatDesc);
		
		
		
		Element supervisionElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Supervision");		
		
		Element activityDateRangeElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_NC_30, "ActivityDateRange");
		
		Element startDateElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC_30, "StartDate");
		
		Element startDateValElement = XmlUtils.appendElement(startDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		startDateValElement.setTextContent(CURRENT_DATE);
		
			
		Element endDateElement = XmlUtils.appendElement(activityDateRangeElement, OjbcNamespaceContext.NS_NC_30, "EndDate");
		
		Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		endDateValElement.setTextContent(CURRENT_DATE);
		
		
		
		Element supervisionFacElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_NC_30, "SupervisionFacility");
		
		Element facIdElement = XmlUtils.appendElement(supervisionFacElement, OjbcNamespaceContext.NS_NC_30, "FacilityIdentification");
		
		Element facIdValElement = XmlUtils.appendElement(facIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		String sSupervistionFacilityId = RandomStringUtils.randomNumeric(9);
		
		facIdValElement.setTextContent(sSupervistionFacilityId);
		
		
		List<String> facilityCategoryList = Arrays.asList("Jail", "Prison", "Shelter");		
		int facilityCategoryIndex = RANDOM.nextInt(facilityCategoryList.size());		
		String sFacilityCategory = facilityCategoryList.get(facilityCategoryIndex);
				
		Element facIdCatDescTxtElement = XmlUtils.appendElement(facIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		facIdCatDescTxtElement.setTextContent(sFacilityCategory);
		
		
		Element supervisionAugmentElement = XmlUtils.appendElement(supervisionElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAugmentation");
		
		Element supervisionAreaIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAreaIdentification");
		
		Element supervisionAreaIdValElement = XmlUtils.appendElement(supervisionAreaIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
				
		String supervisionAreaId = RandomStringUtils.randomNumeric(9); 
		
		supervisionAreaIdValElement.setTextContent(supervisionAreaId);
		
		
		
		List<String> bedAreaList = Arrays.asList("Window", "Hallway", "Bunk Beds", "Corner");		
		int bedCategoryIndex = RANDOM.nextInt(bedAreaList.size());		
		String sBedAreaCategory = bedAreaList.get(bedCategoryIndex);
		
		Element supervisionAreaIdCatDescTxtElement = XmlUtils.appendElement(supervisionAreaIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		supervisionAreaIdCatDescTxtElement.setTextContent(sBedAreaCategory);
				
		Element supervisionBedIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionBedIdentification");
		
		Element supervisionBedIdValElement = XmlUtils.appendElement(supervisionBedIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
				
		String sBedId = RandomStringUtils.randomNumeric(3);		
		supervisionBedIdValElement.setTextContent(sBedId);
		
								
		List<String> bedIdCatList = Arrays.asList("Double", "Queen", "King");		
		int bedIdCatIndex = RANDOM.nextInt(bedIdCatList.size());		
		String sBedIdCat = bedIdCatList.get(bedIdCatIndex);				
		Element supervisionBedIdCatDescTxtElement = XmlUtils.appendElement(supervisionBedIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");		
		supervisionBedIdCatDescTxtElement.setTextContent(sBedIdCat);		
		
				
		Element supervisionCellIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionCellIdentification");		
		Element supervisionCellIdValElement = XmlUtils.appendElement(supervisionCellIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");				
		String sCellId = RandomStringUtils.randomNumeric(3);		
		supervisionCellIdValElement.setTextContent(sCellId);		
		
		
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



