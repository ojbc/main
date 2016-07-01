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
	

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	private static final String CURRENT_DATE = DateTime.now().toString(DATE_TIME_FORMAT);
	
	
	public CustodySampleGenerator() throws ParserConfigurationException,
			IOException {

		super();
	}
	
	
	public List<Document> generateCustodySamples(int recordCount) throws Exception {
		
		List<Document> rCustodyDocList = new ArrayList<Document>(recordCount);
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		for(int i=0; i < recordCount; i++){
		
			PersonElementWrapper iGeneratedPerson = getRandomIdentity(null);			
			
			Document custodyDoc = buildCustodyDetailDoc(iGeneratedPerson);
			
			Element custodyDocRootElement = custodyDoc.getDocumentElement();
			
			ojbcNamespaceContext.populateRootNamespaceDeclarations(custodyDocRootElement);
			
			rCustodyDocList.add(custodyDoc);
		}
		
		return rCustodyDocList;
	}
	
	
	
	
	Document buildCustodyDetailDoc(PersonElementWrapper person) throws ParserConfigurationException, IOException{
		
		Document rCustodyDetailDoc = getNewDocument();
		
		String recordId = "1";
		
		String personRecId = "Person_" + recordId;	
		
		
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
		
				
		Element sysIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String systemId = RandomStringUtils.randomNumeric(6);		
		sysIdValElement.setTextContent(systemId);
				
		Element sysIdSystemNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");		
		sysIdSystemNameElement.setTextContent("Custody Detail");
		
		
		Element custodyElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Custody");
						
		Element caseElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Case");		
		String caseId = "Case_" + recordId;
		XmlUtils.addAttribute(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", caseId);
		
		Element caseAugEl = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAugmentation");				
		Element caseCourtEl = XmlUtils.appendElement(caseAugEl, OjbcNamespaceContext.NS_JXDM_51, "CaseCourt");		
		Element courtNameEl = XmlUtils.appendElement(caseCourtEl, OjbcNamespaceContext.NS_JXDM_51, "CourtName");		
		courtNameEl.setTextContent(randomString("Judge Judy", "Matlock", "Walker Texas Ranger"));
		
		Element bookingElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Booking");				
		String bookingId = "Booking_" + recordId;		
		XmlUtils.addAttribute(bookingElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", bookingId);		
		
		
		Element activityDateElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");	
		Element activityDateTimeElement = XmlUtils.appendElement(activityDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		activityDateTimeElement.setTextContent(randomDate(DATE_TIME_FORMAT));
		
				
		Element detentionFacElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingDetentionFacility");		
		Element bookingFacIdElement = XmlUtils.appendElement(detentionFacElement, OjbcNamespaceContext.NS_NC_30, "FacilityIdentification");		
		Element facIdValElement = XmlUtils.appendElement(bookingFacIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");			
		String facIdSample = RandomStringUtils.randomNumeric(6);		
		facIdValElement.setTextContent(facIdSample);
				
		
		Element bookingSubjectElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
		
		Element roleOfPersonElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");		
		XmlUtils.addAttribute(roleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personRecId);
		
		Element bookingSubjectIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");		
		Element bookingSubjectIdValElement = XmlUtils.appendElement(bookingSubjectIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");				
		String bookingSubjectId = RandomStringUtils.randomAlphanumeric(8);		
		bookingSubjectIdValElement.setTextContent(bookingSubjectId);		
		
		
		Element detentionElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Detention");				
		String detentionId = "Detention_" + recordId;		
		XmlUtils.addAttribute(detentionElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", detentionId);
		
		
		Element detentionActivityDate = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");		
		Element detentionDateValElement = XmlUtils.appendElement(detentionActivityDate, OjbcNamespaceContext.NS_NC_30, "Date");		
		detentionDateValElement.setTextContent(randomDate("yyyy-MM-dd"));
		
		
		Element supervisionCustodyStatusElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "SupervisionCustodyStatus");
		
		Element supervCustStatusDescTxtElement = XmlUtils.appendElement(supervisionCustodyStatusElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");		
		List<String> supervCustStatusDecTxtList = Arrays.asList("Pretrial", "Post-Trial", "In-Trial");		
		int custStatusIndex = RANDOM.nextInt(supervCustStatusDecTxtList.size());		
		String custStatusSample = supervCustStatusDecTxtList.get(custStatusIndex);		
		supervCustStatusDescTxtElement.setTextContent(custStatusSample);
		
		Element pretrialCatCodeElement = XmlUtils.appendElement(supervisionCustodyStatusElement, OjbcNamespaceContext.NS_ADAMS_CO_BOOKING_CODES_EXT, "PreTrialCategoryCode");
		
		
		List<String> pretrialCatCodeList = Arrays.asList("SRP", "C-PR", "S-PR", "PR", "C-SRP", "S-SRP", "SUR", "Split", "No-Bond");		
		int pretrialCatCodeIndex = RANDOM.nextInt(pretrialCatCodeList.size());		
		String pretrialCatCodeSample = pretrialCatCodeList.get(pretrialCatCodeIndex);		
		pretrialCatCodeElement.setTextContent(pretrialCatCodeSample);		
		
		Element supervisionAugmentElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAugmentation");
				
		Element supervisionReleaseDateElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, 
				"SupervisionReleaseEligibilityDate");
		
		Element supervisionReleaseDateValElement = XmlUtils.appendElement(supervisionReleaseDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		supervisionReleaseDateValElement.setTextContent(randomDate("yyyy-MM-dd"));
		
		Element supervisionAreaIdElement = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionAreaIdentification");
		
		Element supervisionAreaIdValElement = XmlUtils.appendElement(supervisionAreaIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");				
		String supervisionAreaId = RandomStringUtils.randomNumeric(9); 		
		supervisionAreaIdValElement.setTextContent(supervisionAreaId);		
				
		Element immigrationHoldElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "DetentiontImmigrationHoldIndicator");				
		
		boolean detentImmigHoldSample = RANDOM.nextBoolean();
		String sDetentImmigHoldSample = String.valueOf(detentImmigHoldSample);		
		immigrationHoldElement.setTextContent(sDetentImmigHoldSample);
				
		Element inmateWorkReleaseIndicator = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InmateWorkReleaseIndicator");
		
		boolean inmateWorkReleaseSample = RANDOM.nextBoolean();		
		String sInmateWorkReleaseSample = String.valueOf(inmateWorkReleaseSample);		
		inmateWorkReleaseIndicator.setTextContent(sInmateWorkReleaseSample);
		
		Element inmateWorkerIndicatorElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InmateWorkerIndicator");
		
		boolean bInmateWorkerSample = RANDOM.nextBoolean();		
		String sInmateWorkerSample = String.valueOf(bInmateWorkerSample);		
		inmateWorkerIndicatorElement.setTextContent(sInmateWorkerSample);
		
		
		Element bailBondElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");			
		String bailBondId = "Bond_" + recordId;		
		XmlUtils.addAttribute(bailBondElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", bailBondId);
				
		Element activityCatElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		
		List<String> bailBondActivitySampleList = Arrays.asList("Traffic Violation", "Speeding", "Noise Violation");				
		int bailBondActivityIndex = RANDOM.nextInt(bailBondActivitySampleList.size());		
		String bailBondActivity = bailBondActivitySampleList.get(bailBondActivityIndex);		
		activityCatElement.setTextContent(bailBondActivity);
		
				
		Element activityStatusElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityStatus");
		
		Element activityStatusDescTxtElement = XmlUtils.appendElement(activityStatusElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");
				
		List<String> bailBondStatusList = Arrays.asList("Paid", "Unpaid", "Partial Payment");		
		int bailBondStatusIndex = RANDOM.nextInt(bailBondStatusList.size());		
		String sBailBond = bailBondStatusList.get(bailBondStatusIndex);		
		
		activityStatusDescTxtElement.setTextContent(sBailBond);
		
		
		Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondAmount");		
		Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");				
		String sBailAmount = RandomStringUtils.randomNumeric(4);		
		bailBondAmountValElement.setTextContent(sBailAmount);
		
		
		Element chargeElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");			
		String chargeId = "Charge_" + recordId;		
		XmlUtils.addAttribute(chargeElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", chargeId);
				
		List<String> chargeCatList = Arrays.asList("Speeding", "Seat Belt Usage");		
		int chargeCatIndex = RANDOM.nextInt(chargeCatList.size());		
		String sChargeCatSample = chargeCatList.get(chargeCatIndex);
		
		Element chargeCatDescTxtElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCategoryDescriptionText");
		chargeCatDescTxtElement.setTextContent(sChargeCatSample);
				
		List<String> chargeDescriptionTxtList = Arrays.asList("Intoxicated", "Speeding", "No Seatbelt");		
		int chargeDescRandomIndex = RANDOM.nextInt(chargeDescriptionTxtList.size());
		String sChargeDesc = chargeDescriptionTxtList.get(chargeDescRandomIndex);				
		Element chargeDescriptionTextElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
		chargeDescriptionTextElement.setTextContent(sChargeDesc);
				
		Element chargeHighestIndicatorElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeHighestIndicator");
		
		boolean bChargeHighestIndicatorSample = RANDOM.nextBoolean();
		
		String sChargeHighestIndicatorSample = String.valueOf(bChargeHighestIndicatorSample);
		
		chargeHighestIndicatorElement.setTextContent(sChargeHighestIndicatorSample);
		
		Element chargeSequenceIDElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeSequenceID");
		
		String sChargeSeqId = RandomStringUtils.randomNumeric(7);
		
		chargeSequenceIDElement.setTextContent(sChargeSeqId);
		
		
		Element chargeStatuteElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeSectionIdentification");
		
		Element chargeStatuteCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		String sStatuteCodeId = RandomStringUtils.randomNumeric(6);		
		chargeStatuteCodeIdValElement.setTextContent(sStatuteCodeId);		
		
		
		Element holdForAgencyElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "HoldForAgency");		
		Element orgNameElement = XmlUtils.appendElement(holdForAgencyElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");					
		orgNameElement.setTextContent(randomString("Acme", "Chips"));
		
				
		// Arrest
		
		Element arrestElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Arrest");			
		String arrestId = "Arrest_" + recordId;		
		XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", arrestId);
		
		Element arrestAgencyElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestAgency");
		
		Element arrestOrgName = XmlUtils.appendElement(arrestAgencyElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");	
		
		List<String> arrestAgencyList = Arrays.asList("NYPD", "LAPD", "CHIPS", "Acme", "Matlock");		
		int arrestAgencyIndex = RANDOM.nextInt(arrestAgencyList.size());		
		String sArrestAgency = arrestAgencyList.get(arrestAgencyIndex);		
		arrestOrgName.setTextContent(sArrestAgency);
		
		
		Element arrestLocationElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestLocation");		
		String arrestLocationId = "Loc_" + recordId;		
		XmlUtils.addAttribute(arrestLocationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", arrestLocationId);
								
		Element nextCourtEventElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_CYFS_31, "NextCourtEvent");
		String nextCourtEventId = "Event_" + recordId;
		XmlUtils.addAttribute(nextCourtEventElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", nextCourtEventId);
		
		
		Element nextCourtActivityDateElement = XmlUtils.appendElement(nextCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element activityDateValElement = XmlUtils.appendElement(nextCourtActivityDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		activityDateValElement.setTextContent(randomDate());
		
		
		Element courtEventCourtElement = XmlUtils.appendElement(nextCourtEventElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventCourt");
		
		Element courtNameElement = XmlUtils.appendElement(courtEventCourtElement, OjbcNamespaceContext.NS_JXDM_51, "CourtName");
		
		List<String> courtNameSampleList = Arrays.asList("Matlock", "Supreme Court", "Traffic Court");		
		int courtSampleIndex = RANDOM.nextInt(courtNameSampleList.size());		
		String sCourtSample = courtNameSampleList.get(courtSampleIndex);
		
		courtNameElement.setTextContent(sCourtSample);
		
		
		Element personElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Person");		
			
		XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", personRecId);
		
		DateTime personDob = person.birthdate;
		Element personBirthDateElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");		
		Element personDobValElement = XmlUtils.appendElement(personBirthDateElement, OjbcNamespaceContext.NS_NC_30, "Date");	
		String sPersonDob = personDob.toString("yyyy-MM-dd");		
		personDobValElement.setTextContent(sPersonDob);			
		
		Element personDigitalImgElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonDigitalImage");		
		Element personImgBinElement = XmlUtils.appendElement(personDigitalImgElement, OjbcNamespaceContext.NS_NC_30, "Base64BinaryObject");	
				
		String sSampleImgBin = RandomStringUtils.randomAlphanumeric(20);		
		personImgBinElement.setTextContent(sSampleImgBin);
		
		
		List<String> ethnicitySampleList = Arrays.asList("H", "N", "U");		
		int ethnicityRandomIndex = RANDOM.nextInt(ethnicitySampleList.size());		
		String ethnicitySample = ethnicitySampleList.get(ethnicityRandomIndex);
		
		Element ethnicityElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonEthnicityCode");		
		ethnicityElement.setTextContent(ethnicitySample);						
		
		Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");		
				
		String sPersonFirstName = person.firstName;		
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		personGivenNameElement.setTextContent(sPersonFirstName);
		
		
		String sPersonMiddleName = person.middleName;
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		personMiddleNameElement.setTextContent(sPersonMiddleName);
		
		
		String sPersonLastName = person.lastName;
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent(sPersonLastName);
					
		Element personLangEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonPrimaryLanguage");		
		Element langNamEl = XmlUtils.appendElement(personLangEl, OjbcNamespaceContext.NS_NC_30, "LanguageName");		
		langNamEl.setTextContent(randomString("English", "Francais", "Espagnol", "Norsk", "Ewe", "Catocoli", "Cabiait"));
		
		List<String> raceSampleList = Arrays.asList("B", "A", "W");				
		int raceIndex = RANDOM.nextInt(raceSampleList.size());		
		String sRandomRace = raceSampleList.get(raceIndex);				
		Element personRaceElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonRaceCode");		
		personRaceElement.setTextContent(sRandomRace);
				
		Element personResidentTxtElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonResidentText");		
		String sampleResidentTxt = person.addressStreetName;		
		personResidentTxtElement.setTextContent(sampleResidentTxt);		
		
		String sPersonSex = randomString("M", "F");
		Element personSexElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
		personSexElement.setTextContent(sPersonSex);
		
		
		String sPersonSSN = RandomStringUtils.randomNumeric(9);
		Element personSsnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");		
		Element personSsnValElement = XmlUtils.appendElement(personSsnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSsnValElement.setTextContent(sPersonSSN);
		
		
		Element personAugmentElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");
		
		Element drivLicEl = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
		
		Element drivLicCardIdEl = XmlUtils.appendElement(drivLicEl, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseCardIdentification");
		
		Element drivLicCardIdValEl = XmlUtils.appendElement(drivLicCardIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		drivLicCardIdValEl.setTextContent(RandomStringUtils.randomNumeric(9));
		
		Element drivLicIdSrcTxtEl = XmlUtils.appendElement(drivLicCardIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");		
		String dlStateSample = getRandomIdentity(null).state;		
		drivLicIdSrcTxtEl.setTextContent(dlStateSample);
				
		Element fbiIdEl = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonFBIIdentification");		
		Element fbiIdValEl = XmlUtils.appendElement(fbiIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		fbiIdValEl.setTextContent(RandomStringUtils.randomNumeric(9));
				
		Element personStateFingerIdElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");
		
		Element personStateFingerIdValElement = XmlUtils.appendElement(personStateFingerIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		
		String sPersonSid = RandomStringUtils.randomAlphanumeric(8);
		
		personStateFingerIdValElement.setTextContent(sPersonSid);
		
		
		Element locationElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Location");
		String locationId = "Loc_" + recordId;
		XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", locationId);
		
		Element locationAddressElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Address");
		
		Element locAddressFullElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "AddressFullText");		
		String locAddress = person.addressStreetName;		
		locAddressFullElement.setTextContent(locAddress);	
		
		Element loc2dGeoEl = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Location2DGeospatialCoordinate");		
		Element geoCordLatEl = XmlUtils.appendElement(loc2dGeoEl, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLatitude");		
		Element latDegValEl = XmlUtils.appendElement(geoCordLatEl, OjbcNamespaceContext.NS_NC_30, "LatitudeDegreeValue");					
		latDegValEl.setTextContent(RandomStringUtils.randomNumeric(1));
		
		Element geoCordLongEl = XmlUtils.appendElement(loc2dGeoEl, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLongitude");		
		Element longDegValEl = XmlUtils.appendElement(geoCordLongEl, OjbcNamespaceContext.NS_NC_30, "LongitudeDegreeValue");
		longDegValEl.setTextContent(RandomStringUtils.randomNumeric(1));						
		
		Element activityChargeAssocElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityChargeAssociation");
		
		Element activityCaseEl = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityCaseEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", caseId);
		
		Element activityBookingElement = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityBookingElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", bookingId);

		Element activityEventElement = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityEventElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", nextCourtEventId);

		Element activityDetentionElement = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");		
		XmlUtils.addAttribute(activityDetentionElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", detentionId);
		
		Element activityArrestElement = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityArrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", arrestId);
		
		Element activityChargeElement = XmlUtils.appendElement(activityChargeAssocElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");
		XmlUtils.addAttribute(activityChargeElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeId);		
		
		
		Element bailBondChargeAssocElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondChargeAssociation");
		
		Element bailBondRefElement = XmlUtils.appendElement(bailBondChargeAssocElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
		XmlUtils.addAttribute(bailBondRefElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", bailBondId);
		
		Element bailBondChargeRefElement = XmlUtils.appendElement(bailBondChargeAssocElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");
		XmlUtils.addAttribute(bailBondChargeRefElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeId);
		
		
		Element sourceSysNameTxtElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "SourceSystemNameText");
		sourceSysNameTxtElement.setTextContent("Custody");
		
		
		Element queryResultCatTxtElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "QueryResultCategoryText"); 
		queryResultCatTxtElement.setTextContent("Custody Detail");
				
		Element infoOwningOrgElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "InformationOwningOrganization");		
		
		List<String> orgBranchNameList = Arrays.asList("Police", "Fire Department", "Public Safety", "EMT");		
		int branchNameIndex = RANDOM.nextInt(orgBranchNameList.size());		
		String sBranchName = orgBranchNameList.get(branchNameIndex);
		
		Element orgBranchNameElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
		
		orgBranchNameElement.setTextContent(sBranchName);
				
		Element infoOwnOrgElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
		
		List<String> orgNameList = Arrays.asList("FLPD", "RIPD", "HIPD", "MEPD");
		
		int orgNameIndex = RANDOM.nextInt(orgNameList.size());
		
		String sOrgSample = orgNameList.get(orgNameIndex);
		
		infoOwnOrgElement.setTextContent(sOrgSample);
				
		Element metadataElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "Metadata");		
		Element lastUpdatedDateElement = XmlUtils.appendElement(metadataElement, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");		
		Element lastUpdatedDateValElement = XmlUtils.appendElement(lastUpdatedDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		lastUpdatedDateValElement.setTextContent(randomDate());				
		
				
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
