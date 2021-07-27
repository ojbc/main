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
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

public class CustodySampleGenerator extends AbstractSampleGenerator{

	private static final Random RANDOM = new Random();
	

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	private static final String CURRENT_DATE = DateTime.now().toString(DATE_TIME_FORMAT);
		
	public static final String CUSTODY_QUERY_SYSTEM_ID = 
			"{http://ojbc.org/Services/WSDL/Custody_Query_Request_Service/1.0}SubmitCustodyQueryRequest";
	
	private int arrestCount = 2;
	
	private int chargesPerArrest = 3;
	
	private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	
	
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
		
		
		Element bookingAgencyRecIdEl = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, 
				"BookingAgencyRecordIdentification");		
		Element bookingAgencyRecIdValEl = XmlUtils.appendElement(bookingAgencyRecIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleBookingNumber = RandomStringUtils.randomNumeric(7);		
		bookingAgencyRecIdValEl.setTextContent(sampleBookingNumber);		
		
				
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
		
		
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){

			String arrestElementId = "Arrest_" + String.valueOf(arrestIndex);
			
			Element arrestRefElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "Arrest");
			
			XmlUtils.addAttribute(arrestRefElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", arrestElementId);			
		}			
				
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
		
		Element bedIdEl = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionBedIdentification");
		Element bedIdValEl = XmlUtils.appendElement(bedIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		bedIdValEl.setTextContent(RandomStringUtils.randomNumeric(3));
		
		Element supervisCellIdEl = XmlUtils.appendElement(supervisionAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionCellIdentification");	
		Element supervisCellIdValEl = XmlUtils.appendElement(supervisCellIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		supervisCellIdValEl.setTextContent(RandomStringUtils.randomNumeric(5));		
		
		
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
				
		Element allowDepositEl = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "AllowAccountDepositIndicator");
		allowDepositEl.setTextContent(getRandomBooleanString());				
		
		String actualReleaseDateElementId = "Release_0";
		
		Element releaseElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Release");									
		XmlUtils.addAttribute(releaseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", actualReleaseDateElementId);
		
		Element activDateEl = XmlUtils.appendElement(releaseElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element dateTimeEl = XmlUtils.appendElement(activDateEl, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		dateTimeEl.setTextContent(CURRENT_DATE);
		
		
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){

			for(int bondIndex=0; bondIndex < chargesPerArrest; bondIndex++){
				
				String bondElementId = "Arrest_" + String.valueOf(arrestIndex) + "Bond_" + String.valueOf(bondIndex);
				
				Element bailBondElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
							
				XmlUtils.addAttribute(bailBondElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", bondElementId);
						
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
			}			
		}
		

		
		
								
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){
			
			for(int chargeIndex = 0; chargeIndex < chargesPerArrest; chargeIndex++){
				
				String chargeElementId = "Arrest_" + String.valueOf(arrestIndex) + "Charge_" + String.valueOf(chargeIndex);
				
				Element chargeElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");			
						
				XmlUtils.addAttribute(chargeElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", chargeElementId);
						
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
			}		
		}
		
		
						
		// Arrest				
		for(int arrestIndex=0; arrestIndex<arrestCount; arrestIndex++){

			Element arrestElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "Arrest");			
			
			String arrestId = "Arrest_" + String.valueOf(arrestIndex);		
			
			XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", arrestId);
			
			Element arrestAgencyElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestAgency");
			
			Element arrestOrgName = XmlUtils.appendElement(arrestAgencyElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");	
			
			List<String> arrestAgencyList = Arrays.asList("NYPD", "LAPD", "CHIPS", "Acme", "Matlock");		
			int arrestAgencyIndex = RANDOM.nextInt(arrestAgencyList.size());		
			String sArrestAgency = arrestAgencyList.get(arrestAgencyIndex);		
			arrestOrgName.setTextContent(sArrestAgency);
				
			for(int chargeIndex=0; chargeIndex < chargesPerArrest; chargeIndex++){
				
				String chargeElementId = "Arrest_" + String.valueOf(arrestIndex) + "Charge_" + String.valueOf(chargeIndex);
				
				Element arrestChargeRefEl = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestCharge");
				XmlUtils.addAttribute(arrestChargeRefEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeElementId);			
			}
			
			Element arrestLocationElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestLocation");		
			String arrestLocationId = "Loc_" + recordId;		
			XmlUtils.addAttribute(arrestLocationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", arrestLocationId);			
		}		
		
		
		String chSumElId = "CHS_1";
		Element personChSumEl = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "PersonCriminalHistorySummary");
		XmlUtils.addAttribute(personChSumEl, OjbcNamespaceContext.NS_STRUCTURES_30, "id", chSumElId);
		
		Element regSexOffEl = XmlUtils.appendElement(personChSumEl, OjbcNamespaceContext.NS_JXDM_51, "RegisteredSexualOffenderIndicator");
		regSexOffEl.setTextContent(getRandomBooleanString());		
		
		
		
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){
			
			String eventElementId = "Event_" + String.valueOf(arrestIndex); 
			
			Element nextCourtEventElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_CYFS_31, "NextCourtEvent");
			
			XmlUtils.addAttribute(nextCourtEventElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", eventElementId);
			
			
			Element nextCourtActivityDateElement = XmlUtils.appendElement(nextCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
			
			Element activityDateValElement = XmlUtils.appendElement(nextCourtActivityDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
			activityDateValElement.setTextContent(randomDate());
			
			
			Element courtEventCourtElement = XmlUtils.appendElement(nextCourtEventElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventCourt");
			
			Element courtNameElement = XmlUtils.appendElement(courtEventCourtElement, OjbcNamespaceContext.NS_JXDM_51, "CourtName");
			
			List<String> courtNameSampleList = Arrays.asList("Matlock", "Supreme Court", "Traffic Court");		
			int courtSampleIndex = RANDOM.nextInt(courtNameSampleList.size());		
			String sCourtSample = courtNameSampleList.get(courtSampleIndex);
			
			courtNameElement.setTextContent(sCourtSample);			
		}
		

		
		
		
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
		
		Element educationEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonEducationLevelText");
		educationEl.setTextContent(randomString("Middle Scool", "Highschool", "College"));
		
		Element ethnicityElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonEthnicityCode");		
		ethnicityElement.setTextContent(randomString("H", "N", "U"));						
		
		
		Element eyeColorEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonEyeColorCode");
		eyeColorEl.setTextContent(randomString("BLK", "BLU", "BRO", "GRN", "GRY", "HAZ", "MAR", "MUL", "PNK", "XXX"));
		
		Element hairColorEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonHairColorCode");
		hairColorEl.setTextContent(randomString("BLK", "BLN", "BLU", "BRO", "GRN", "GRY", "ONG", "PLE", "PNK", "RED", "SDY", "WHI", "XXX"));		
		
		Element heightElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonHeightMeasure");
		
		Element heightMeasureElement = XmlUtils.appendElement(heightElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");		
		heightMeasureElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element heightUnitsElement = XmlUtils.appendElement(heightElement, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");		
		heightUnitsElement.setTextContent("in");		
		

		Element militarySumEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonMilitarySummary");
		
		Element milServiceStatusCode = XmlUtils.appendElement(militarySumEl, OjbcNamespaceContext.NS_ADAMS_CO_BOOKING_CODES_EXT, "MilitaryServiceStatusCode");
		
		milServiceStatusCode.setTextContent(randomString("NACT", "ACT"));			
						
		Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");		
						
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		personGivenNameElement.setTextContent(getFirstNameSample(person));
				
		String sPersonMiddleName = person.middleName;
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		personMiddleNameElement.setTextContent(sPersonMiddleName);
				
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent(getLastNameSample(person));
					
		
		Element physicalFeatureEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonPhysicalFeature");
		
		Element physFeatDescEl = XmlUtils.appendElement(physicalFeatureEl, OjbcNamespaceContext.NS_NC_30, "PhysicalFeatureDescriptionText");
		
		physFeatDescEl.setTextContent(randomString("Tatoo", "Missing left arm", "Very Tall"));
		
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
		
		
		Element personWeightElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonWeightMeasure");
		
		Element weightValEl = XmlUtils.appendElement(personWeightElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");
		
		weightValEl.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element weightUnitElement = XmlUtils.appendElement(personWeightElement, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");
		
		weightUnitElement.setTextContent("lbs");		
				
		
		Element personAugmentElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");
		
		Element drivLicEl = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
		
		Element drivLicCardIdEl = XmlUtils.appendElement(drivLicEl, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseIdentification");
		
		Element drivLicCardIdValEl = XmlUtils.appendElement(drivLicCardIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		drivLicCardIdValEl.setTextContent(RandomStringUtils.randomNumeric(9));
		
		Element drivLicIdSrcTxtEl = XmlUtils.appendElement(drivLicCardIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");		
		
		String dlStateSample  = randomString("AA", "AE", "AK", "AP", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", 
				"FL", "FM", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", 
				"MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", 
				"OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY");		
		
		drivLicIdSrcTxtEl.setTextContent(dlStateSample);
				
		
		Element occupationEl = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_NC_30, "EmployeeOccupationCategoryText");		
		occupationEl.setTextContent(randomString("Food/Bev", "Software", "Hospitality", "Public Safety", "Military", "Acting"));
		
		
		Element fbiIdEl = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonFBIIdentification");		
		Element fbiIdValEl = XmlUtils.appendElement(fbiIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		fbiIdValEl.setTextContent(RandomStringUtils.randomNumeric(9));
				
		Element personStateFingerIdElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");
		
		Element personStateFingerIdValElement = XmlUtils.appendElement(personStateFingerIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		
		String sPersonSid = RandomStringUtils.randomAlphanumeric(8);
		
		personStateFingerIdValElement.setTextContent(sPersonSid);
				
		
		int aliasCount = 2;
		
		for(int aliasIndex = 0; aliasIndex < aliasCount; aliasIndex++){
			
			String sAliasIndex = String.valueOf(aliasIndex);
			
			Element identityEl = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Identity");
			
			XmlUtils.addAttribute(identityEl, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Alias_" + sAliasIndex);
			
			Element idPersonRepEl = XmlUtils.appendElement(identityEl, OjbcNamespaceContext.NS_NC_30, "IdentityPersonRepresentation");
			
			Date aliasDob = new Date();
			
			Element personDobEl = XmlUtils.appendElement(idPersonRepEl, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");			
			Element personDobValEl = XmlUtils.appendElement(personDobEl, OjbcNamespaceContext.NS_NC_30, "Date");
						
			String sDob = SDF_DATE.format(aliasDob);			
			personDobValEl.setTextContent(sDob);
			
			Element personNameEl = XmlUtils.appendElement(idPersonRepEl, OjbcNamespaceContext.NS_NC_30, "PersonName");
												
			String aliasfName = getRandomIdentity(null).firstName;						
			aliasfName = aliasfName.trim();			
			Element givenNameEl = XmlUtils.appendElement(personNameEl, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");			
			givenNameEl.setTextContent(aliasfName);							
								
			String aliasLName = getRandomIdentity(null).lastName;														
			aliasLName = aliasLName.trim();			
			Element surNameEl = XmlUtils.appendElement(personNameEl, OjbcNamespaceContext.NS_NC_30, "PersonSurName");			
			surNameEl.setTextContent(aliasLName);				
									
			String aliasSexCode = randomString("M", "F", "U");							
			aliasSexCode = aliasSexCode.trim();			
			Element aliasSexCodeEl = XmlUtils.appendElement(idPersonRepEl, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");			
			aliasSexCodeEl.setTextContent(aliasSexCode);			
		}
								
		
		Element locationElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "Location");
		
		String locationElementId = "Loc_" + recordId; 
					
		XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", locationElementId);
																	
		Element locationAddressElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Address");
												
		String address2ndaryUnit = randomString("APT 2", "STE C", "APT 7C");
		
		
		if(StringUtils.isNotEmpty(address2ndaryUnit)){
			Element adrsScndryUnitElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "AddressSecondaryUnitText");						
			adrsScndryUnitElement.setTextContent(address2ndaryUnit);						
		}
		
		Element locationStreetElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		
		String arrestLocStreetNumber = person.addressStreetNumber;
		
		if(StringUtils.isNotEmpty(arrestLocStreetNumber)){
			Element streetNumberElement = XmlUtils.appendElement(locationStreetElement, OjbcNamespaceContext.NS_NC_30, "StreetNumberText");
			streetNumberElement.setTextContent(arrestLocStreetNumber);						
		}
		
		String arrestLocStreetName = person.addressStreetName;

		if(StringUtils.isNotEmpty(arrestLocStreetName)){
			Element streetNameElement = XmlUtils.appendElement(locationStreetElement, OjbcNamespaceContext.NS_NC_30, "StreetName");
			streetNameElement.setTextContent(arrestLocStreetName);						
		}
											
		String arrestLocCity = person.city;
		
		if(StringUtils.isNotEmpty(arrestLocCity)){
			Element cityElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationCityName");
			cityElement.setTextContent(arrestLocCity);						
		}
		
		String arrestLocState = randomString("AA", "AE", "AK", "AP", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", 
				"FL", "FM", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", 
				"MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", 
				"OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY");			
		
		if(StringUtils.isNotEmpty(arrestLocState)){
			Element stateCodeElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationStateUSPostalServiceCode");
			stateCodeElement.setTextContent(arrestLocState);						
		}
												
		String arrestLocZipCode = person.zipCode;
		
		if(StringUtils.isNotEmpty(arrestLocZipCode)){
			Element postalCodeElement = XmlUtils.appendElement(locationAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode");
			postalCodeElement.setTextContent(arrestLocZipCode);								
		}				
		
		Element loc2dGeoEl = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Location2DGeospatialCoordinate");		
		Element geoCordLatEl = XmlUtils.appendElement(loc2dGeoEl, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLatitude");		
		Element latDegValEl = XmlUtils.appendElement(geoCordLatEl, OjbcNamespaceContext.NS_NC_30, "LatitudeDegreeValue");					
		latDegValEl.setTextContent(RandomStringUtils.randomNumeric(1));
		
		Element geoCordLongEl = XmlUtils.appendElement(loc2dGeoEl, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLongitude");		
		Element longDegValEl = XmlUtils.appendElement(geoCordLongEl, OjbcNamespaceContext.NS_NC_30, "LongitudeDegreeValue");
		longDegValEl.setTextContent(RandomStringUtils.randomNumeric(1));						
		
		
		
		Element activCaseAssocEl = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityCaseAssociation");

		Element activityBookingElement = XmlUtils.appendElement(activCaseAssocEl, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityBookingElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", bookingId);
		
		Element activityDetentionElement = XmlUtils.appendElement(activCaseAssocEl, OjbcNamespaceContext.NS_NC_30, "Activity");		
		XmlUtils.addAttribute(activityDetentionElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", detentionId);
		
		Element activityReleaseElement = XmlUtils.appendElement(activCaseAssocEl, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activityReleaseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", actualReleaseDateElementId);
				
		Element activityCaseEl = XmlUtils.appendElement(activCaseAssocEl, OjbcNamespaceContext.NS_NC_30, "Case");
		XmlUtils.addAttribute(activityCaseEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", caseId);


		
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){
						
			for(int chargeIndex = 0; chargeIndex < chargesPerArrest; chargeIndex++){
				
				Element activChargeAssocEl = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityChargeAssociation");
				
				String eventElementId = "Event_" + String.valueOf(arrestIndex);
				
				Element eventActivityEl = XmlUtils.appendElement(activChargeAssocEl, OjbcNamespaceContext.NS_NC_30, "Activity");		
				XmlUtils.addAttribute(eventActivityEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", eventElementId);				

				String chargeElementId = "Arrest_" + String.valueOf(arrestIndex) + "Charge_" + String.valueOf(chargeIndex);
				
				Element activChargeAssocChargeEl = XmlUtils.appendElement(activChargeAssocEl, OjbcNamespaceContext.NS_JXDM_51, "Charge");
				XmlUtils.addAttribute(activChargeAssocChargeEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeElementId);				
			}			
		}
											
						
		for(int arrestIndex=0; arrestIndex < arrestCount; arrestIndex++){
			
			for(int chargeIndex=0; chargeIndex < chargesPerArrest; chargeIndex++){
				
				Element bailBondChargeAssocElement = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondChargeAssociation");
				
				String bailBondElementId = "Arrest_" + String.valueOf(arrestIndex) + "Bond_" + String.valueOf(chargeIndex);
				
				Element bailBondRefElement = XmlUtils.appendElement(bailBondChargeAssocElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
				XmlUtils.addAttribute(bailBondRefElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", bailBondElementId);				
								
				String chargeElementId = "Arrest_" + String.valueOf(arrestIndex) + "Charge_" + String.valueOf(chargeIndex);
				
				Element bailBondChargeRefElement = XmlUtils.appendElement(bailBondChargeAssocElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");
				XmlUtils.addAttribute(bailBondChargeRefElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeElementId);				
			}
		}
				
				
		Element personAliasIdentityAssocEl = XmlUtils.appendElement(custodyElement, 
				OjbcNamespaceContext.NS_NC_30, "PersonAliasIdentityAssociation");
		
		Element mainPersonEl = XmlUtils.appendElement(personAliasIdentityAssocEl, OjbcNamespaceContext.NS_NC_30, "Person");
		
		XmlUtils.addAttribute(mainPersonEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personRecId);
				
		for(int i = 0; i< aliasCount; i++){
			
			String sAliasAssocIndex = String.valueOf(i);
			
			Element identityEl = XmlUtils.appendElement(personAliasIdentityAssocEl, OjbcNamespaceContext.NS_NC_30, "Identity");
			
			String aliasElId = "Alias_" + sAliasAssocIndex; 
			
			XmlUtils.addAttribute(identityEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", aliasElId);			
		}		
						
		
		Element activPersonAssocEl = XmlUtils.appendElement(custodyElement, OjbcNamespaceContext.NS_NC_30, "ActivityPersonAssociation");
		
		Element chsSumActivityEl = XmlUtils.appendElement(activPersonAssocEl, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(chsSumActivityEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chSumElId);
		
		Element activPersonAssocPersonEl = XmlUtils.appendElement(activPersonAssocEl, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(activPersonAssocPersonEl, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personRecId);
		
		
		Element sourceSysNameTxtElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "SourceSystemNameText");
		sourceSysNameTxtElement.setTextContent(CUSTODY_QUERY_SYSTEM_ID);
		
		
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
