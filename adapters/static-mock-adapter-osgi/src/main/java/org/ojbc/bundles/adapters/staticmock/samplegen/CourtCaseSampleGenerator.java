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
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CourtCaseSampleGenerator extends AbstractSampleGenerator {
		
	
	public CourtCaseSampleGenerator() throws ParserConfigurationException,
			IOException {
		
		super();
	}
	
		
	public List<Document> generateCourtCaseSamples(int recordCount) throws IOException, ParserConfigurationException{
		
		List<Document> rCourtCaseDocList = new ArrayList<Document>(recordCount);
		
		for(int i=0; i<recordCount; i++){
			
			PersonElementWrapper iPerson = getRandomIdentity(null);
			
			Document courtCaseDoc = buildCourtCaseDetailDoc(iPerson);
			
			rCourtCaseDocList.add(courtCaseDoc);
		}
		
		return rCourtCaseDocList;
	}
	
	
	Document buildCourtCaseDetailDoc(PersonElementWrapper person) throws ParserConfigurationException, IOException{
			
		Document rCourtCaseDetailDoc = getNewDocument();
		
		Element rootCourtCaseElement = rCourtCaseDetailDoc.createElementNS(OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC, "CourtCaseQueryResults");
		rootCourtCaseElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC);

		rCourtCaseDetailDoc.appendChild(rootCourtCaseElement);
				
		Element docCreateDateElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");		
		
		Element docCreateDateValElement = XmlUtils.appendElement(docCreateDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		String sCurrentDate = DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss");
		
		docCreateDateValElement.setTextContent(sCurrentDate);

		Element docIdElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
		Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		docIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element sysIdElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");				
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		sysIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element sysNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
		sysNameElement.setTextContent(randomString("CourtCaseSystem", "CourtCaseFinder"));
		
		
		
		Element caseElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "Case"); 		
		XmlUtils.addAttribute(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Case_01");
		
		Element activIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		Element activIdValElement = XmlUtils.appendElement(activIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleCaseId = RandomStringUtils.randomNumeric(8);		
		activIdValElement.setTextContent(sampleCaseId);
		
		Element activStatusElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "ActivityStatus");		
		Element actStatDescTxtElement = XmlUtils.appendElement(activStatusElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");		
		actStatDescTxtElement.setTextContent(randomString("Early", "Outdated", "Complete"));
		
		Element caseDispElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseDisposition");
		Element dispDateElement = XmlUtils.appendElement(caseDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDate");
		Element caseDispDatValElement = XmlUtils.appendElement(dispDateElement, OjbcNamespaceContext.NS_NC_30, "Date");
		caseDispDatValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element caseGenCatTxtElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseGeneralCategoryText");
		caseGenCatTxtElement.setTextContent(randomString("Ownership", "Debate", "Arrest"));
		
		Element caseTrackIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseTrackingID");
		String sampleCaseTrackId = RandomStringUtils.randomNumeric(8);
		caseTrackIdElement.setTextContent(sampleCaseTrackId);
		
		Element caseDocketIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseDocketID");
		
		String sampleDocket = randomString("SC vs NC", "NY vs LA", "North vs South", "City vs State"); 						
		caseDocketIdElement.setTextContent(sampleDocket);
		
		Element caseFilingElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseFiling");		
		Element caseFileDocCreateDate = XmlUtils.appendElement(caseFilingElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");		
		Element caseFileDocCreateDateValElement = XmlUtils.appendElement(caseFileDocCreateDate, OjbcNamespaceContext.NS_NC_30, "DateTime"); 
		caseFileDocCreateDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		Element caseAugmentElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAugmentation");		
		Element caseAmendChargeElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAmendedCharge");
		
		Element chargeCountElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity"); 		
		String sampleChargeCount = RandomStringUtils.randomNumeric(2);		
		chargeCountElement.setTextContent(sampleChargeCount);
		
		
		Element chargeDescTxtElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");		
		String sampleChargeDesc = randomString("Blocking Traffic", "Speeding", "Turn Signal Broken", "Causing Comotion");		
		chargeDescTxtElement.setTextContent(sampleChargeDesc);
				
		Element chargeFilingDateElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeFilingDate");		
		Element chargeFilingDateValElement = XmlUtils.appendElement(chargeFilingDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeFilingDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element chargeStatuteElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element statCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleChargeStatuteId = RandomStringUtils.randomNumeric(6);	
		statCodeIdValElement.setTextContent(sampleChargeStatuteId);
		
		Element chargeStatIdCatDescTxtElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		
		String sampleCatDescTxt = randomString("Driver License", 
				"Concealed Weapon Permit",
				"Government ID",
				"Motorcycle License");		
		chargeStatIdCatDescTxtElement.setTextContent(sampleCatDescTxt);
		
		
		
		int caseChargeCount = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		
		Logger.getLogger(this.getClass()).info("\n\n CaseCharge Count: " + caseChargeCount + "\n\n");

		for(int i=0; i < caseChargeCount; i++){
			
			appendCaseChargeSampleElement(caseAugmentElement);
		}
		
				
		Element caseCourtElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseCourt");
		
		Element caseCourtOrgAugmentElement = XmlUtils.appendElement(caseCourtElement, OjbcNamespaceContext.NS_JXDM_51, "OrganizationAugmentation");
		
		Element caseCourtOrgJurisdictElement = XmlUtils.appendElement(caseCourtOrgAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "OrganizationJurisdiction");
		
		Element caseCourtOrgAutJurisdicTxtElement = XmlUtils.appendElement(caseCourtOrgJurisdictElement, OjbcNamespaceContext.NS_NC_30, "JurisdictionText");		
		caseCourtOrgAutJurisdicTxtElement.setTextContent(randomString("Highway Patrol", "Sheriff", "City Police", "Mall Cop"));
		
		Element caseCourtNameElement = XmlUtils.appendElement(caseCourtElement, OjbcNamespaceContext.NS_JXDM_51, "CourtName");
		caseCourtNameElement.setTextContent(randomString("Judge Judy", "Matlock"));
		
		Element caseCourtDivTxtElement = XmlUtils.appendElement(caseCourtElement, OjbcNamespaceContext.NS_JXDM_51, "CourtDivisionText");
		caseCourtDivTxtElement.setTextContent(randomString("County", "State", "Highway Patrol", "Sherif"));
		
		
		Element caseCourtEventElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseCourtEvent");
		
		Element caseCourtEventActivIdElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element caseCourtActivIdValElement = XmlUtils.appendElement(caseCourtEventActivIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		caseCourtActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseCourtActivDateElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element caseCourtActivityDateValElement = XmlUtils.appendElement(caseCourtActivDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		caseCourtActivityDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element caseCourtActivDescTxxtElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityDescriptionText");		
		caseCourtActivDescTxxtElement.setTextContent(randomString("Speeding", "Swerving", "Stealing"));
				
		Element activityNameElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_NC_30, "ActivityName");
		activityNameElement.setTextContent(randomString("Burglary", "Speaking Falsehood", "Telling Bad Jokes"));
		
		Element courtEventJudgeElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventJudge");
		
		Element roleOfPersonElement = XmlUtils.appendElement(courtEventJudgeElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element judgeNameElement = XmlUtils.appendElement(roleOfPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element judgeFullNameElement = XmlUtils.appendElement(judgeNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
		
		judgeFullNameElement.setTextContent(getRandomName());
		
		Element firstCourtAprncElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "FirstCourtAppearance");
		
		Element courtAprncDatElement = XmlUtils.appendElement(firstCourtAprncElement, OjbcNamespaceContext.NS_JXDM_51, "CourtAppearanceDate");
		
		Element courtAprncDatValElement = XmlUtils.appendElement(courtAprncDatElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		courtAprncDatValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element courtEventCommentsTxtElement = XmlUtils.appendElement(caseCourtEventElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtEventCommentsText");		
		courtEventCommentsTxtElement.setTextContent(randomString("A lot of commotion", "Many reporters present", "Lawyers making up stories"));
		
		Element defendantSelfRepIndicElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseDefendantSelfRepresentationIndicator");		
		defendantSelfRepIndicElement.setTextContent(getRandomBooleanString());
		
		Element caseDefendantPartyElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseDefendantParty");
		
		Element defendantEntOrgElement = XmlUtils.appendElement(caseDefendantPartyElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");
		
		Element entOrgNameElement = XmlUtils.appendElement(defendantEntOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
		entOrgNameElement.setTextContent(randomString("Police", "Fire Department", "EMS", "Public Safety"));
		
		Element entPersonElement = XmlUtils.appendElement(caseDefendantPartyElement, OjbcNamespaceContext.NS_NC_30, "EntityPerson");
		
		XmlUtils.addAttribute(entPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
		
		
		Element caseDefenseAtterneyElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseDefenseAttorney");
		
		Element defenseAttortyPersonRoleElement = XmlUtils.appendElement(caseDefenseAtterneyElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element defenseAtrnyPersonNameElement = XmlUtils.appendElement(defenseAttortyPersonRoleElement, OjbcNamespaceContext.NS_NC_30, "PersonName");;
		
		Element defenseAtrnyFullNameElement = XmlUtils.appendElement(defenseAtrnyPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
		defenseAtrnyFullNameElement.setTextContent(getRandomName());
		
		Element caseDomViolenceElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseDomesticViolenceIndicator");
		caseDomViolenceElement.setTextContent(getRandomBooleanString());
		
		Element caseHearingElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseHearing");
		
		Element caseHearingActivIdElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");		
		
		Element caseHearActivIdValElement = XmlUtils.appendElement(caseHearingActivIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		caseHearActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element activCatTxtElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		
		activCatTxtElement.setTextContent(randomString("Prosecution", "Defense", "Evidence Presentation"));
		
		Element caseHearingActivDateRangeElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDateRange");
		
		Element caseHearingStartDateElement = XmlUtils.appendElement(caseHearingActivDateRangeElement, OjbcNamespaceContext.NS_NC_30, "StartDate");
		
		Element caseHearingStartDateValElement = XmlUtils.appendElement(caseHearingStartDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		caseHearingStartDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element caseHearingEndDateElement = XmlUtils.appendElement(caseHearingActivDateRangeElement, OjbcNamespaceContext.NS_NC_30, "EndDate");
		
		Element caseHearingEndDateValElement = XmlUtils.appendElement(caseHearingEndDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		caseHearingEndDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element activDescTxtElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDescriptionText");
		activDescTxtElement.setTextContent(randomString("Jury Deciding Verdict", "Jury meeting"));
		
		Element hearingActivNameElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityName");
		hearingActivNameElement.setTextContent(randomString("Jury", "Trial by Jury"));
		
		 Element activReasonElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityReasonText");
		 activReasonElement.setTextContent(randomString("Defendant requested trial", "Sherif requested trial", "LAPD requested trial"));
		 
		Element activDispElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDisposition");
		
		Element activDispDescTxtElement = XmlUtils.appendElement(activDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDescriptionText");		
		activDispDescTxtElement.setTextContent(randomString("Guilty", "Not Guilty", "Deciding Verdict"));
		
		
		Element caseHearingCourtEventJudgeElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_JXDM_51, "CourtEventJudge");
		
		Element caseHearingJudgeRoleElement = XmlUtils.appendElement(caseHearingCourtEventJudgeElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element caseHearingJudgeNameElement = XmlUtils.appendElement(caseHearingJudgeRoleElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element caseJudgeFullNameElement = XmlUtils.appendElement(caseHearingJudgeNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
		
		caseJudgeFullNameElement.setTextContent(getRandomName());
		
		Element caseHearingCommentsTxtElement = XmlUtils.appendElement(caseHearingElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtEventCommentsText");
		
		caseHearingCommentsTxtElement.setTextContent(randomString("Trial really long", "Hard to find entrance"));
		
		Element caseJudgeElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseJudge");
		
		Element caseJudgeRolePersonElement = XmlUtils.appendElement(caseJudgeElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element caseJudgeNameElement = XmlUtils.appendElement(caseJudgeRolePersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");		
		
		Element caseJudgePersonNameElement = XmlUtils.appendElement(caseJudgeNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");		
		caseJudgePersonNameElement.setTextContent(getRandomName());
		
		Element judicBarMemberElement = XmlUtils.appendElement(caseJudgeElement, OjbcNamespaceContext.NS_JXDM_51, "JudicialOfficialBarMembership");
		
		Element judicBarIdElement = XmlUtils.appendElement(judicBarMemberElement, OjbcNamespaceContext.NS_JXDM_51, "JudicialOfficialBarIdentification");
		
		Element judicBarIdValElement = XmlUtils.appendElement(judicBarIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		judicBarIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseLieneageElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseLineageCase");
		
		Element caseTrackingIdElement = XmlUtils.appendElement(caseLieneageElement, OjbcNamespaceContext.NS_NC_30, "CaseTrackingID");		
		caseTrackingIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseProsecAtrnyElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseProsecutionAttorney");
		
		Element prosecAtrnyRoleElement = XmlUtils.appendElement(caseProsecAtrnyElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element prosecPersonElement = XmlUtils.appendElement(prosecAtrnyRoleElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element prosecAtrnyNameElement = XmlUtils.appendElement(prosecPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");		
		prosecAtrnyNameElement.setTextContent(getRandomName());
		
		Element prosecutorBarElement = XmlUtils.appendElement(caseProsecAtrnyElement, OjbcNamespaceContext.NS_JXDM_51, "JudicialOfficialBarMembership");
		
		Element prosecutorOfficBarIdElement = XmlUtils.appendElement(prosecutorBarElement, OjbcNamespaceContext.NS_JXDM_51, "JudicialOfficialBarIdentification");
		
		Element prosecutorOfficBarIdValElement = XmlUtils.appendElement(prosecutorOfficBarIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		prosecutorOfficBarIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
						
		Element caseAugmentExtElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CaseAugmentation");
		
		Element caseSealedIndicatorElement = XmlUtils.appendElement(caseAugmentExtElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CaseSealedIndicator");		
		caseSealedIndicatorElement.setTextContent(getRandomBooleanString());
		
		Element remandDateElement = XmlUtils.appendElement(caseAugmentExtElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "RemandDate");
		
		Element remandDateValElement = XmlUtils.appendElement(remandDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		remandDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		
		Element apelateCaseElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_JXDM_51, "AppellateCase");		
		XmlUtils.addAttribute(apelateCaseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Appel_Case_01");
		
		Element apelateCaseTrackIdElement = XmlUtils.appendElement(apelateCaseElement, OjbcNamespaceContext.NS_NC_30, "CaseTrackingID");
		apelateCaseTrackIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element apelateCaseFilingElement = XmlUtils.appendElement(apelateCaseElement, OjbcNamespaceContext.NS_NC_30, "CaseFiling");
		
		Element apelateCaseFileDocCreateDateElement = XmlUtils.appendElement(apelateCaseFilingElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
		
		Element apelateCaseFileDocDateVal = XmlUtils.appendElement(apelateCaseFileDocCreateDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		apelateCaseFileDocDateVal.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element identityElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "Identity");		
		XmlUtils.addAttribute(identityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Identity_01");
		
		
		Element idPersonRepElement = XmlUtils.appendElement(identityElement, OjbcNamespaceContext.NS_NC_30, "IdentityPersonRepresentation");
		
		Element idPersonDobElement = XmlUtils.appendElement(idPersonRepElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		
		Element idPersonDobValElement = XmlUtils.appendElement(idPersonDobElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		idPersonDobValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		PersonElementWrapper samplePerson = getRandomIdentity(null);
		
		Element idPersonNameElement = XmlUtils.appendElement(idPersonRepElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element idPersonGivenName = XmlUtils.appendElement(idPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		idPersonGivenName.setTextContent(samplePerson.firstName);
		
		Element idPersonMiddleName = XmlUtils.appendElement(idPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
		idPersonMiddleName.setTextContent(samplePerson.middleName);
		
		Element idPersonSurNameElement = XmlUtils.appendElement(idPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName"); 
		idPersonSurNameElement.setTextContent(samplePerson.lastName);
		
		Element idPersonFullNameElement = XmlUtils.appendElement(idPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
		idPersonFullNameElement.setTextContent(samplePerson.firstName + " " + samplePerson.middleName + " " + samplePerson.lastName);
		
		Element idPersonSsnElement = XmlUtils.appendElement(idPersonRepElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");
		
		Element idPersonSsnIdElement = XmlUtils.appendElement(idPersonSsnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		idPersonSsnIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element contactInfoElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "ContactInformation");
		XmlUtils.addAttribute(contactInfoElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "CI_01");
		
		Element phoneElement = XmlUtils.appendElement(contactInfoElement, OjbcNamespaceContext.NS_NC_30, "ContactTelephoneNumber");
		
		Element fullPhoneElement = XmlUtils.appendElement(phoneElement, OjbcNamespaceContext.NS_NC_30, "FullTelephoneNumber");
		
		Element phoneValElement = XmlUtils.appendElement(fullPhoneElement, OjbcNamespaceContext.NS_NC_30, "TelephoneNumberFullID");
		
		String samplePhone = (String)getRandomIdentity(null).telephoneNumber;
		phoneValElement.setTextContent(samplePhone);
		
		Element elecContactIdElement = XmlUtils.appendElement(contactInfoElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "ElectronicContactIdentification");
		
		Element electContactIdValElement = XmlUtils.appendElement(elecContactIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		electContactIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element electContactIdCatDescTxtElement = XmlUtils.appendElement(elecContactIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		electContactIdCatDescTxtElement.setTextContent(randomString("ID Card", "Passport", "Driver License"));
		
		Element citationElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_JXDM_51, "Citation");
		XmlUtils.addAttribute(citationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Citation_01");
		
		Element citationActivIdElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element citationAcivIdVAlElement = XmlUtils.appendElement(citationActivIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		citationAcivIdVAlElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element citationViolationElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_JXDM_51, "CitationViolation");
		
		Element citationActivDateElement = XmlUtils.appendElement(citationViolationElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element citationActivDateValElement = XmlUtils.appendElement(citationActivDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		citationActivDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		Element incidentLocationElement = XmlUtils.appendElement(citationViolationElement, OjbcNamespaceContext.NS_NC_30, "IncidentLocation");
		
		Element incidentAddressElement = XmlUtils.appendElement(incidentLocationElement, OjbcNamespaceContext.NS_NC_30, "Address");
		
		Element incidentAddressUnitElement = XmlUtils.appendElement(incidentAddressElement, OjbcNamespaceContext.NS_NC_30, "AddressSecondaryUnitText");
		incidentAddressUnitElement.setTextContent(randomString("APT 123", "Suite 456", "St 789", "P.O. Box 4528"));
		
		Element addressPointElement = XmlUtils.appendElement(incidentAddressElement, OjbcNamespaceContext.NS_NC_30, "AddressDeliveryPointID");
		addressPointElement.setTextContent(randomString("doorstep", "mailbox", "PO box"));
		
		Element incidentLocStreetElement = XmlUtils.appendElement(incidentAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		
		Element locStreetNameElement = XmlUtils.appendElement(incidentLocStreetElement, OjbcNamespaceContext.NS_NC_30, "StreetName");
		locStreetNameElement.setTextContent(samplePerson.addressStreetName);
		
		Element locStreetCatTxtElement = XmlUtils.appendElement(incidentLocStreetElement, OjbcNamespaceContext.NS_NC_30, "StreetCategoryText");
		locStreetCatTxtElement.setTextContent(randomString("Small Road", "Cul de Sac", "Highway", "Interstate"));
		
		Element locCountyNameElement = XmlUtils.appendElement(incidentAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationCountyName");
		locCountyNameElement.setTextContent(randomString("Rowan", "Pickens", "Union", "Dorchester"));
		
		Element locDescTxtElement = XmlUtils.appendElement(incidentLocationElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");
		locDescTxtElement.setTextContent(randomString("NY", "LA", "UT", "FL"));
		
		Element locNameElement = XmlUtils.appendElement(incidentLocationElement, OjbcNamespaceContext.NS_NC_30, "LocationName");
		locNameElement.setTextContent(randomString("40N 70W", "City", "County"));
		
		Element violationAugmentElement = XmlUtils.appendElement(citationViolationElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "ViolationAugmentation");
		
		Element drivingPointNumTxtElement = XmlUtils.appendElement(violationAugmentElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "DrivingPointNumberText");
		drivingPointNumTxtElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element drivingPointReducedTxtElement = XmlUtils.appendElement(violationAugmentElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "DrivingPointNumberReducedText");
		drivingPointReducedTxtElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element bacTestRefusedElement = XmlUtils.appendElement(violationAugmentElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "PersonBloodAlcoholContentTestRefusedIndicator");
		bacTestRefusedElement.setTextContent(getRandomBooleanString());
		
		
		Element citationAugElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CitationAugmentation");
		
		Element courtAprncDateElement = XmlUtils.appendElement(citationAugElement, OjbcNamespaceContext.NS_JXDM_51, "CourtAppearanceDate");
		
		Element courtApncDateTimeElement = XmlUtils.appendElement(courtAprncDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		courtApncDateTimeElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element crtApncRqdElement = XmlUtils.appendElement(citationAugElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtAppearanceRequiredIndicator");
		crtApncRqdElement.setTextContent(getRandomBooleanString());
		
		Element licSurndrdElement = XmlUtils.appendElement(citationAugElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "LicenseSurrenderedIndicator");
		licSurndrdElement.setTextContent(getRandomBooleanString());
		
		Element drivingIncElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_JXDM_51, "DrivingIncident");		
		XmlUtils.addAttribute(drivingIncElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Incident_01");
		
		Element incidentDescTxtElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "ActivityDescriptionText");
		incidentDescTxtElement.setTextContent(randomString("Speeding", "Driving too slow", "Spinning Out", "Tailgating", "Blowing horn"));
		
		Element driverSpeedElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "DrivingIncidentRecordedSpeedRateMeasure");
		
		Element speedMeasElement = XmlUtils.appendElement(driverSpeedElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");
		speedMeasElement.setTextContent(RandomStringUtils.randomNumeric(3));
		
		Element speedUnitElement = XmlUtils.appendElement(driverSpeedElement, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");
		speedUnitElement.setTextContent(randomString("kt", "mph", "kph", "mpm"));
		
		Element speedLimitElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "DrivingIncidentLegalSpeedRateMeasure");
		
		Element speedLimitNumElement = XmlUtils.appendElement(speedLimitElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");		
		speedLimitNumElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element speedUnitsElement = XmlUtils.appendElement(speedLimitElement, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");
		speedUnitsElement.setTextContent(randomString("kt", "mph", "kph", "mpm"));
		
		Element arrestElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "Arrest");
		XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Arrest_01");
		
		Element arrestActivDateElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element arrestActivDateValElement = XmlUtils.appendElement(arrestActivDateElement, OjbcNamespaceContext.NS_NC_30, "Date");
		arrestActivDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element arrestAgencyRecIdElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestAgencyRecordIdentification");
		
		Element arrestRecIdElement = XmlUtils.appendElement(arrestAgencyRecIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		arrestRecIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		
		Element arrestChargeElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestCharge");
		
		Element arrestChargeStatuteElement = XmlUtils.appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element arrestChargeStatCodeIdElement = XmlUtils.appendElement(arrestChargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element arrestChargeIdElement = XmlUtils.appendElement(arrestChargeStatCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		arrestChargeIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element arrestLocElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestLocation");
		
		Element arrestLocDescTxtElement = XmlUtils.appendElement(arrestLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");		
		arrestLocDescTxtElement.setTextContent(randomString("NY", "LA", "SF", "FL", "ATL"));
		
		Element arrestOfficialElement = XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "ArrestOfficial");		
		XmlUtils.addAttribute(arrestOfficialElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Officer_01");
		
		Element officalRoleElement = XmlUtils.appendElement(arrestOfficialElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		Element arrestOfficNameElement = XmlUtils.appendElement(officalRoleElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element arrestFullNameElement = XmlUtils.appendElement(arrestOfficNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");			
		arrestFullNameElement.setTextContent(getRandomName());
		
		Element leEnforcUnitElement = XmlUtils.appendElement(arrestOfficialElement, OjbcNamespaceContext.NS_JXDM_51, "EnforcementOfficialUnit");
		
		Element leEnforceUnitNameElement = XmlUtils.appendElement(leEnforcUnitElement, OjbcNamespaceContext.NS_JXDM_51, "EnforcementUnitName");		
		leEnforceUnitNameElement.setTextContent(randomString("LAPD", "NYPD", "Walker Texas Ranger"));
		
		Element detentionElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "Detention");
		XmlUtils.addAttribute(detentionElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Detention_01");
				
		Element detentionActivElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element detActivIdElement = XmlUtils.appendElement(detentionActivElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		detActivIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
				
		Element detActivCatTxtElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		detActivCatTxtElement.setTextContent(randomString("Jail", "Prison", "Community Service"));
		
		Element detActivDatRangeElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "ActivityDateRange");
		
		Element detStartDateElement = XmlUtils.appendElement(detActivDatRangeElement, OjbcNamespaceContext.NS_NC_30, "StartDate");
		
		Element detStartDateValElement = XmlUtils.appendElement(detStartDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		detStartDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element detEndDateElement = XmlUtils.appendElement(detActivDatRangeElement, OjbcNamespaceContext.NS_NC_30, "EndDate");
		
		Element endDateValElement = XmlUtils.appendElement(detEndDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		endDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element superviseCustStatus = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "SupervisionCustodyStatus");
		
	 	Element superviseStatDescTxtElement = XmlUtils.appendElement(superviseCustStatus, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");		
	 	superviseStatDescTxtElement.setTextContent(randomString("Parents Watching", "Big Brother Custody", "Homeless Shelter"));
	 	
	 	Element supervisFacElement = XmlUtils.appendElement(detentionElement, OjbcNamespaceContext.NS_NC_30, "SupervisionFacility");
		
	 	Element facIdElement = XmlUtils.appendElement(supervisFacElement, OjbcNamespaceContext.NS_NC_30, "FacilityIdentification");
	 	
	 	Element facIdValElement = XmlUtils.appendElement(facIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
	 	facIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element bailBondElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");	 	
	 	XmlUtils.addAttribute(bailBondElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Bond_01");
	 	
	 	Element bailBondActivIdElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
	 	
	 	Element bailBondActivIdValElement = XmlUtils.appendElement(bailBondActivIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	bailBondActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element bailBondActivCatTxtElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
	 	bailBondActivCatTxtElement.setTextContent(randomString("Large Price", "Small Price", "Bond across street"));
	 	
	 	Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondAmount");
	 	
	 	Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");
		
	 	bailBondAmountValElement.setTextContent(RandomStringUtils.randomNumeric(4));
	 	
	 	
	 	Element bondIssuerElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondIssuerEntity"); 
	 	
	 	Element bondEntOrgElement = XmlUtils.appendElement(bondIssuerElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");
	 	
	 	Element bondOrgNameElement = XmlUtils.appendElement(bondEntOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
	 	
	 	bondOrgNameElement.setTextContent(randomString("Bonds 4 Cheap", "25% Bonds", "24hr Bonding", "Bonds Advance"));
	 	
	 	Element vehicleElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "Vehicle");	 	
	 	XmlUtils.addAttribute(vehicleElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Vehicle_01");
	 	
	 	Element vehicCmvElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_NC_30, "VehicleCMVIndicator");
	 	vehicCmvElement.setTextContent(getRandomBooleanString());
	 	
	 	Element vehicleIdElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_NC_30, "VehicleIdentification");
	 	
	 	Element vinValueElement = XmlUtils.appendElement(vehicleIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	vinValueElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element vehicleMakeCode = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_JXDM_51, "VehicleMakeCode");
	 	vehicleMakeCode.setTextContent(RandomStringUtils.randomNumeric(3));
	 	
	 	Element vehicleModelElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_JXDM_51, "VehicleModelCode");
	 	vehicleModelElement.setTextContent(randomString("Chevy", "Ford", "GM", "Ferari", "Pontiac", "Kia", "VW"));
	 	
	 	Element personElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "Person");	 	
	 	XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Person_01");
	 	
	 	Element personDobElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
	 	
	 	Element dobValElement = XmlUtils.appendElement(personDobElement, OjbcNamespaceContext.NS_NC_30, "Date");	 	
	 	dobValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 	Element personDescElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonDescriptionText");
	 	personDescElement.setTextContent(randomString("Really Tall", "Short white female", "Tall black male", "Thin Asian female", "Very Large"));
	 	
	 	Element ethnicityElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonEthnicityText");
	 	ethnicityElement.setTextContent(randomString("W", "B", "A", "L"));
	 	
	 	Element eyeColorElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonEyeColorCode");
	 	eyeColorElement.setTextContent(randomString("BLU", "BWN", "GRN", "BLK"));
	 	
	 	Element hairElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonHairColorCode");
	 	hairElement.setTextContent(randomString("BLO", "BWN", "RED"));
	 	
	 	Element heightElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonHeightMeasure");
	 	
	 	Element heightValueElement = XmlUtils.appendElement(heightElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");	 	
	 	heightValueElement.setTextContent(RandomStringUtils.randomNumeric(2));
	 	
	 	Element heightUnitElement = XmlUtils.appendElement(heightElement, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");
	 	heightUnitElement.setTextContent(randomString("in", "cm", "mm", "me", "ft"));
	 	
	 	
	 	PersonElementWrapper samplePerson2 = getRandomIdentity(null);
	 	
	 	Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
	 	
	 	Element givenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
	 		 		 	
	 	givenNameElement.setTextContent(samplePerson2.firstName);
	 		 	
	 	Element middleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
	 	
	 	middleNameElement.setTextContent(samplePerson2.middleName);
	 	
	 	Element surNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
	 	
	 	surNameElement.setTextContent(samplePerson2.lastName);
	 	
	 	Element personFullNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
	 		 		 	
	 	personFullNameElement.setTextContent(samplePerson2.firstName + " " + samplePerson2.middleName + " " + samplePerson2.lastName);
	 	
	 	Element personRaceElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonRaceText");
	 	
	 	personRaceElement.setTextContent(randomString("WHT", "BLK", "ASN", "LAT", "PAC"));
	 	
	 	Element personSexCodeElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSexText");
	 	personSexCodeElement.setTextContent(samplePerson2.sex);
	 		 	
	 	Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");

	 	Element ssnValElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	ssnValElement.setTextContent(RandomStringUtils.randomNumeric(9));
	 	
	 	Element weightElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonWeightMeasure");
	 	
	 	Element weightMeasureElement = XmlUtils.appendElement(weightElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");	 	
	 	weightMeasureElement.setTextContent(RandomStringUtils.randomNumeric(3));
	 	
	 	
	 	Element personAugmentElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");
	 	
	 	Element drivLicElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
	 	
	 	Element drivLicIdElement = XmlUtils.appendElement(drivLicElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseIdentification");
	 	
	 	Element drivLicIdValElement = XmlUtils.appendElement(drivLicIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
	 	drivLicIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element drivLicIdSrcElement = XmlUtils.appendElement(drivLicIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
	 	drivLicIdSrcElement.setTextContent(randomString("SC", "NC", "GA", "FL", "ME", "MA", "VT", "TX"));
	 	
	 	Element drivLicExpDateElement = XmlUtils.appendElement(drivLicElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseExpirationDate");
	 	
	 	Element drivLicExpDateValElement = XmlUtils.appendElement(drivLicExpDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
	 	drivLicExpDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 		 	
	 	Element drivLicIssDateElement = XmlUtils.appendElement(drivLicElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseIssueDate");
	 	
	 	Element drivLicIssDatValElement = XmlUtils.appendElement(drivLicIssDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");	 	
	 	drivLicIssDatValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 	Element drivLicClassElement = XmlUtils.appendElement(drivLicElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseNonCommercialClassText");	 	
	 	drivLicClassElement.setTextContent(randomString("C", "M", "L", "D", "S"));
	 	
	 	Element fbiElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonFBIIdentification");
	 	
	 	Element fbiIdValElement = XmlUtils.appendElement(fbiElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	fbiIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	
	 	Element sidElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");
	 	
	 	Element sidValElement = XmlUtils.appendElement(sidElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	sidValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element locElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "Location");	 	
	 	XmlUtils.addAttribute(locElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Location_01");
	 	
	 	Element locAddressElement = XmlUtils.appendElement(locElement, OjbcNamespaceContext.NS_NC_30, "Address");
	 	
	 	Element addressUnitElement = XmlUtils.appendElement(locAddressElement, OjbcNamespaceContext.NS_NC_30, "AddressSecondaryUnitText");
	 	addressUnitElement.setTextContent(randomString("APT 234", "STE 567", "PO Box 789", "2nd Floor", "Door on left"));
	 	
	 	Element locStreetElement = XmlUtils.appendElement(locAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
	 	
	 	Element locStreetFullElement = XmlUtils.appendElement(locStreetElement, OjbcNamespaceContext.NS_NC_30, "StreetFullText");
	 	
	 	locStreetFullElement.setTextContent(samplePerson2.addressStreetName);
	 	
	 	Element locCityElement = XmlUtils.appendElement(locAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationCityName");
	 	locCityElement.setTextContent(samplePerson2.city);
	 	
	 	Element locStateElement = XmlUtils.appendElement(locAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationStateName");
	 	locStateElement.setTextContent(samplePerson2.state);
	 	
	 	Element locZipElement = XmlUtils.appendElement(locAddressElement, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode");
	 	locZipElement.setTextContent(samplePerson2.zipCode);
	 	
	 	Element locCatTxtElement = XmlUtils.appendElement(locElement, OjbcNamespaceContext.NS_NC_30, "LocationCategoryText");
	 	locCatTxtElement.setTextContent(randomString("around corner", "North", "East", "South", "West"));
	 	
	 	Element activCaseAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityCaseAssociation");
	 	
	 	Element citationActivElement = XmlUtils.appendElement(activCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(citationActivElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Citation_01");	 	
	 	
	 	Element arrestActivityElement = XmlUtils.appendElement(activCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(arrestActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Arrest_01");
	 	
	 	Element detentionActivityElement = XmlUtils.appendElement(activCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(detentionActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Detention_01");
	 	
	 	Element bondActivityElement = XmlUtils.appendElement(activCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(bondActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Bond_01");
	 	
	 	Element caseAssocElement = XmlUtils.appendElement(activCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Case");
	 	XmlUtils.addAttribute(caseAssocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Case_01");
	 	
	 	Element relatedCaseAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "RelatedCaseAssociation");
	 	
	 	Element relatedCaseActivityElement = XmlUtils.appendElement(relatedCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");	 	
	 	XmlUtils.addAttribute(relatedCaseActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Case_01");
	 	
	 	Element appelCaseActivElement = XmlUtils.appendElement(relatedCaseAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(appelCaseActivElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Appel_Case_01");
	 	
	 	
	 	Element activIncidentAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityIncidentAssociation");
	 	
	 	Element incidentCitationActivityElement = XmlUtils.appendElement(activIncidentAssocElement, OjbcNamespaceContext.NS_NC_30, "Activity");	 	
	 	XmlUtils.addAttribute(incidentCitationActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Citation_01");
	 	
	 	Element activIncidentIncElement = XmlUtils.appendElement(activIncidentAssocElement, OjbcNamespaceContext.NS_NC_30, "Incident");
	 	XmlUtils.addAttribute(activIncidentIncElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Incident_01");
	 	
	 	Element contactInfoAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "ContactInformationAssociation");
	 	
	 	Element contactEntElement = XmlUtils.appendElement(contactInfoAssocElement, OjbcNamespaceContext.NS_NC_30, "ContactEntity");
	 	XmlUtils.addAttribute(contactEntElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element contactAssocInfoElement = XmlUtils.appendElement(contactInfoAssocElement, OjbcNamespaceContext.NS_NC_30, "ContactInformation");	 	
	 	XmlUtils.addAttribute(contactAssocInfoElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "CI_01");
	 	
	 	
	 	Element personConveyAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "PersonConveyanceAssociation");
	 	
	 	Element conveyAssocPersonElement = XmlUtils.appendElement(personConveyAssocElement, OjbcNamespaceContext.NS_NC_30, "Person");
	 	XmlUtils.addAttribute(conveyAssocPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
	 		 	
	 	Element conveyAssocVehicleElement = XmlUtils.appendElement(personConveyAssocElement, OjbcNamespaceContext.NS_NC_30, "Conveyance");	 	
	 	XmlUtils.addAttribute(conveyAssocVehicleElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_01");
	 	
	 	
	 	Element personResAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
	 	
	 	Element resAssocPersonElement = XmlUtils.appendElement(personResAssocElement, OjbcNamespaceContext.NS_NC_30, "Person");	 	
	 	XmlUtils.addAttribute(resAssocPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element resLocElement = XmlUtils.appendElement(personResAssocElement, OjbcNamespaceContext.NS_NC_30, "Location");
	 	XmlUtils.addAttribute(resLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Location_01");
	 	
	 	Element aliasIdentAssocElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "PersonAliasIdentityAssociation");
	 	
	 	Element aliasPerson = XmlUtils.appendElement(aliasIdentAssocElement, OjbcNamespaceContext.NS_NC_30, "Person");
	 	XmlUtils.addAttribute(aliasPerson, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element aliasIdentElement = XmlUtils.appendElement(aliasIdentAssocElement, OjbcNamespaceContext.NS_NC_30, "Identity");
	 	XmlUtils.addAttribute(aliasIdentElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Identity_01");
	 	
	 	Element srcSysNamTxtElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "SourceSystemNameText");

	 	//TODO correct sys name values
	 	srcSysNamTxtElement.setTextContent(randomString("Court Case System", "Court Case DB", "Court Case Lookup"));
	 		 	
	 	Element queryResCatTxtElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "QueryResultCategoryText");
	 	queryResCatTxtElement.setTextContent(randomString("Results", "Result List", "Results Returned"));
	 	
	 	Element infoOwnOrgElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT, "InformationOwningOrganization");
	 	
	 	Element orgBranchNameElement = XmlUtils.appendElement(infoOwnOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
	 	orgBranchNameElement.setTextContent(randomString("Large Search Company", "Acme", "Acme Search Finder", "Acme people lookup finder", "Cops", "Walker Texas Ranger"));
	 	
	 	Element orgNameElement = XmlUtils.appendElement(infoOwnOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
	 	orgNameElement.setTextContent(randomString("COPS", "Walker Texas Ranger", "Matlock"));
	 		 	
	 	Element metadataElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_NC_30, "Metadata");
	 	
	 	Element lastUpdateDateElement = XmlUtils.appendElement(metadataElement, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
	 	
	 	Element lastUpdateDateValElement = XmlUtils.appendElement(lastUpdateDateElement, OjbcNamespaceContext.NS_NC_30, "Date");	 	
	 	lastUpdateDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 	
	 	Element queryRestMetaElement = XmlUtils.appendElement(drivingIncElement, OjbcNamespaceContext.NS_QRM, "QueryResultsMetadata");
	 	
	 	Element infoAccessDenialElement = XmlUtils.appendElement(queryRestMetaElement, OjbcNamespaceContext.NS_IAD, "InformationAccessDenial");
	 	
	 	Element infoAccessDenialIndicElement = XmlUtils.appendElement(infoAccessDenialElement, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialIndicator");	 	
	 	infoAccessDenialIndicElement.setTextContent(getRandomBooleanString());
	 		 	
	 	Element infoAccessDenySysTxtElement = XmlUtils.appendElement(infoAccessDenialElement, OjbcNamespaceContext.NS_IAD, "InformationAccessDenyingSystemNameText");
	 	infoAccessDenySysTxtElement.setTextContent(randomString("BG Checker", "Background Checkers", "Rapback Deniers", "FBI", "Clearance Beakers"));
	 	
	 	Element infoAccDenySysNamElement = XmlUtils.appendElement(infoAccessDenialElement, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialReasonText");
	 	infoAccDenySysNamElement.setTextContent(randomString("On deny list", "On no-fly list", "Gossips", "Sells info to others", "Can't keep a secret"));
	 	
	 		 	
	 	Element queryRequestErrorElement = XmlUtils.appendElement(queryRestMetaElement, OjbcNamespaceContext.NS_QRER, "QueryRequestError");
	 	
	 	Element errorTxtElement = XmlUtils.appendElement(queryRequestErrorElement, OjbcNamespaceContext.NS_QRER, "ErrorText");
	 	errorTxtElement.setTextContent(randomString("Can't do that query", "Can't find anyone", "Looked too long"));
	 	
	 	Element queryReqIdElement = XmlUtils.appendElement(queryRequestErrorElement, OjbcNamespaceContext.NS_QRER, "QueryRequestIdentification");
	 	
	 	Element queryReqIdValElement = XmlUtils.appendElement(queryReqIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");	 	
	 	queryReqIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element idCatDescElment = XmlUtils.appendElement(queryReqIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
	 	idCatDescElment.setTextContent(randomString("Driver License", "Gvmt Id", "Passport", "Concealed Weapons Permit"));
	 	
	 	Element idSrcTxtElement = XmlUtils.appendElement(queryReqIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
	 	idSrcTxtElement.setTextContent(samplePerson.state);
	 	
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootCourtCaseElement);
		
		return rCourtCaseDetailDoc;
	}
	
	
	private Element appendCaseChargeSampleElement(Element parentCaseAugmentElement) throws DOMException, IOException {

		Element caseChargeElement = XmlUtils.appendElement(parentCaseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseCharge");
		
		Element caseChargeCountElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");	
		String sampleCaseChargeCount = RandomStringUtils.randomNumeric(1);		
		caseChargeCountElement.setTextContent(sampleCaseChargeCount);
		
		Element caseChargeDescTxtElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
		
		String sampleCaseChargeDesc = randomString("Not using wipers", 
				"Not using headlights",
				"Not wearing seatbelt",
				"Swerving");
		
		caseChargeDescTxtElement.setTextContent(sampleCaseChargeDesc);
		
		Element chargeDispElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDisposition");
		
		Element chargeDispDateElement = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDate");		
		Element chargeDispDateValElement = XmlUtils.appendElement(chargeDispDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeDispDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		Element chargeDispDescTxt = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDescriptionText");
		
		String sampleDispDesc = randomString("Plea agreement", 
				"Plea disagreement");
		
		chargeDispDescTxt.setTextContent(sampleDispDesc);
		
		Element chargeDispOtherTxtElement = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDispositionOtherText");
		
		String sampleDispOtherTxt = randomString("Assault on a Police Officer", 
				"Kicked neighbor's dog",
				"Threw neighbor's cat");
		
		chargeDispOtherTxtElement.setTextContent(sampleDispOtherTxt);
		
		Element caseChargeFilingDateElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeFilingDate");		
		Element caseChargeFilingDateValElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		caseChargeFilingDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element chargePleaElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargePlea");
		
		Element chargePleaActivityDateElement = XmlUtils.appendElement(chargePleaElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");		
		Element chargePleaActivityDateValElement = XmlUtils.appendElement(chargePleaActivityDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargePleaActivityDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element pleaCatCodeElement = XmlUtils.appendElement(chargePleaElement, OjbcNamespaceContext.NS_JXDM_51, "PleaCategoryCode");		
		String samplePleaCatCode = RandomStringUtils.randomAlphabetic(1);		
		pleaCatCodeElement.setTextContent(samplePleaCatCode);
		
		Element chargeSentenceElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeSentence");
		
		Element chargeSentActivityIdElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element chargeSentActivIdValElement = XmlUtils.appendElement(chargeSentActivityIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		String sampleChargeActivId = RandomStringUtils.randomNumeric(8);		
		chargeSentActivIdValElement.setTextContent(sampleChargeActivId);
		
		Element chargeSentenceActivDateElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");		
		Element chargeSentActivDateValElement = XmlUtils.appendElement(chargeSentenceActivDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeSentActivDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
				
		Element chargeSentActivStatElement= XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityStatus");		
		Element chargeSentActivStatDescTxtElement = XmlUtils.appendElement(chargeSentActivStatElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");
		
		String sampleActivStatus = randomString("Still deciding", 
				"Descision Pending",
				"Final Decision");
		
		chargeSentActivStatDescTxtElement.setTextContent(sampleActivStatus);
		
		Element chargeSentenceChargeElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_JXDM_51, "SentenceCharge");		
		Element sentChargeStatElement = XmlUtils.appendElement(chargeSentenceChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element sentStatCodeIdElement = XmlUtils.appendElement(sentChargeStatElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification"); 
		
		Element sentStatCodeIdValElement = XmlUtils.appendElement(sentStatCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleSentStatId = RandomStringUtils.randomNumeric(8);		
		sentStatCodeIdValElement.setTextContent(sampleSentStatId);
		
		Element chargeSentenceTermElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_JXDM_51, "SentenceTerm");
		
		Element chargeSentMaxTermElement = XmlUtils.appendElement(chargeSentenceTermElement, OjbcNamespaceContext.NS_JXDM_51, "TermMaximumDuration");
		
		String sampleMaxTerm = randomString("P2Y4M3DT08H30M0S", 
				"P4Y7M8DT10H30M0S");	
		
		chargeSentMaxTermElement.setTextContent(sampleMaxTerm);
		
		Element chargeSentMinTermElement = XmlUtils.appendElement(chargeSentenceTermElement, OjbcNamespaceContext.NS_JXDM_51, "TermMinimumDuration");
		
		String sampleMinTerm = randomString("P2Y4M3DT08H30M0S", 
				"P1Y4M3DT08H30M0S");
		
		chargeSentMinTermElement.setTextContent(sampleMinTerm);
		
		Element chargeSentSuperviseFineAmountElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_JXDM_51, "SupervisionFineAmount");
		
		Element chargeSentFineAmountValElement = XmlUtils.appendElement(chargeSentSuperviseFineAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");		
		chargeSentFineAmountValElement.setTextContent(randomString("25.70", "9800.99", "5.43", "99.99"));
		
		Element caseChargeSeqIdElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeSequenceID");		
		caseChargeSeqIdElement.setTextContent(RandomStringUtils.randomNumeric(2));
				
		Element caseChargeStatuteElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element caseChargeStatuteCodeIdElement = XmlUtils.appendElement(caseChargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element caseChargeStatCodeIdValElement = XmlUtils.appendElement(caseChargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		caseChargeStatCodeIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseChargeStatIdCatDescTxtElement = XmlUtils.appendElement(caseChargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		caseChargeStatIdCatDescTxtElement.setTextContent(randomString("Driving", "Running", "Drinking"));
	
		return caseChargeElement;
	}


	private Document getNewDocument() throws ParserConfigurationException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();		

		Document doc = docBuilder.newDocument();
		
		return doc;
	}	

	
}