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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IAD;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_31;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_51;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_QRER;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_QRM;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_CYFS_31;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
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
		
		Element rootCourtCaseElement = rCourtCaseDetailDoc.createElementNS(NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC, "CourtCaseQueryResults");
		rootCourtCaseElement.setPrefix(NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC);

		rCourtCaseDetailDoc.appendChild(rootCourtCaseElement);
				
		Element docCreateDateElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "DocumentCreationDate");		
		
		Element docCreateDateValElement = XmlUtils.appendElement(docCreateDateElement, NS_NC_30, "DateTime");
		
		String sCurrentDate = DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss");
		
		docCreateDateValElement.setTextContent(sCurrentDate);

		Element docIdElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "DocumentIdentification");
		Element docIdValElement = XmlUtils.appendElement(docIdElement, NS_NC_30, "IdentificationID");		
		docIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element sysIdElement = XmlUtils.appendElement(rootCourtCaseElement, NS_INTEL_31, "SystemIdentification");				
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, NS_NC_30, "IdentificationID");		
		sysIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element sysNameElement = XmlUtils.appendElement(sysIdElement, NS_NC_30, "SystemName");
		sysNameElement.setTextContent(randomString("CourtCaseSystem", "CourtCaseFinder"));
		
		
		
		Element caseElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Case"); 		
		XmlUtils.addAttribute(caseElement, NS_STRUCTURES_30, "id", "Case_01");
		
		Element activIdElement = XmlUtils.appendElement(caseElement, NS_NC_30, "ActivityIdentification");
		Element activIdValElement = XmlUtils.appendElement(activIdElement, NS_NC_30, "IdentificationID");		
		String sampleCaseId = RandomStringUtils.randomNumeric(8);		
		activIdValElement.setTextContent(sampleCaseId);
		
		Element activStatusElement = XmlUtils.appendElement(caseElement, NS_NC_30, "ActivityStatus");		
		Element actStatDescTxtElement = XmlUtils.appendElement(activStatusElement, NS_NC_30, "StatusDescriptionText");		
		actStatDescTxtElement.setTextContent(randomString("Early", "Outdated", "Complete"));
		
		Element caseDispElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseDisposition");
		Element dispDateElement = XmlUtils.appendElement(caseDispElement, NS_NC_30, "DispositionDate");
		Element caseDispDatValElement = XmlUtils.appendElement(dispDateElement, NS_NC_30, "Date");
		caseDispDatValElement.setTextContent(randomDate());
		
		Element caseCategoryTextElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseCategoryText");
		caseCategoryTextElement.setTextContent(randomString("Sub Type 1", "Sub Type 2", "Sub Type 3"));
		
		Element caseGenCatTxtElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseGeneralCategoryText");
		caseGenCatTxtElement.setTextContent(randomString("Ownership", "Debate", "Arrest"));
		
		Element caseTrackIdElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseTrackingID");
		String sampleCaseTrackId = RandomStringUtils.randomNumeric(8);
		caseTrackIdElement.setTextContent(sampleCaseTrackId);
		
		Element caseDocketIdElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseDocketID");
		
		String sampleDocket = randomString("SC vs NC", "NY vs LA", "North vs South", "City vs State"); 						
		caseDocketIdElement.setTextContent(sampleDocket);
		
		Element caseFilingElement = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseFiling");		
		Element caseFileDocCreateDate = XmlUtils.appendElement(caseFilingElement, NS_NC_30, "DocumentCreationDate");		
		Element caseFileDocCreateDateValElement = XmlUtils.appendElement(caseFileDocCreateDate, NS_NC_30, "DateTime"); 
		caseFileDocCreateDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		
		Element caseAugmentElement = XmlUtils.appendElement(caseElement, NS_JXDM_51, "CaseAugmentation");		
		Element caseAmendChargeElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseAmendedCharge");
		
		Element chargeCountElement = XmlUtils.appendElement(caseAmendChargeElement, NS_JXDM_51, "ChargeCountQuantity"); 		
		String sampleChargeCount = RandomStringUtils.randomNumeric(2);		
		chargeCountElement.setTextContent(sampleChargeCount);
		
		
		Element chargeDescTxtElement = XmlUtils.appendElement(caseAmendChargeElement, NS_JXDM_51, "ChargeDescriptionText");		
		String sampleChargeDesc = randomString("Blocking Traffic", "Speeding", "Turn Signal Broken", "Causing Comotion");		
		chargeDescTxtElement.setTextContent(sampleChargeDesc);
				
		Element chargeFilingDateElement = XmlUtils.appendElement(caseAmendChargeElement, NS_JXDM_51, "ChargeFilingDate");		
		Element chargeFilingDateValElement = XmlUtils.appendElement(chargeFilingDateElement, NS_NC_30, "Date");		
		chargeFilingDateValElement.setTextContent(randomDate());
		
		Element chargeStatuteElement = XmlUtils.appendElement(caseAmendChargeElement, NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, NS_JXDM_51, "StatuteCodeIdentification");
		
		Element statCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, NS_NC_30, "IdentificationID");		
		String sampleChargeStatuteId = RandomStringUtils.randomNumeric(6);	
		statCodeIdValElement.setTextContent(sampleChargeStatuteId);
		
		Element chargeStatIdCatDescTxtElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, NS_NC_30, "IdentificationCategoryDescriptionText");
		
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
		
				
		Element caseCourtElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseCourt");
		
		Element caseCourtOrgAugmentElement = XmlUtils.appendElement(caseCourtElement, NS_JXDM_51, "OrganizationAugmentation");
		
		Element caseCourtOrgJurisdictElement = XmlUtils.appendElement(caseCourtOrgAugmentElement, NS_JXDM_51, "OrganizationJurisdiction");
		
		Element caseCourtOrgAutJurisdicTxtElement = XmlUtils.appendElement(caseCourtOrgJurisdictElement, NS_NC_30, "JurisdictionText");		
		caseCourtOrgAutJurisdicTxtElement.setTextContent(randomString("Highway Patrol", "Sheriff", "City Police", "Mall Cop"));
		
		Element caseCourtNameElement = XmlUtils.appendElement(caseCourtElement, NS_JXDM_51, "CourtName");
		caseCourtNameElement.setTextContent(randomString("Thornton Municipal Court", "Adams County Court", "Adams District Court", "Aura Municipal Court"));
		
		Element caseCourtDivTxtElement = XmlUtils.appendElement(caseCourtElement, NS_JXDM_51, "CourtDivisionText");
		caseCourtDivTxtElement.setTextContent(randomString("District Court DIV A", "County Court DIV 1", "County Court DIV 7", "County Court DIV 2"));
		
		
		Element caseCourtEventElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseCourtEvent");
		
		Element caseCourtEventActivIdElement = XmlUtils.appendElement(caseCourtEventElement, NS_NC_30, "ActivityIdentification");
		
		Element caseCourtActivIdValElement = XmlUtils.appendElement(caseCourtEventActivIdElement, NS_NC_30, "IdentificationID");		
		caseCourtActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseCourtActivDateElement = XmlUtils.appendElement(caseCourtEventElement, NS_NC_30, "ActivityDate");
		
		Element caseCourtActivityDateValElement = XmlUtils.appendElement(caseCourtActivDateElement, NS_NC_30, "Date");		
		caseCourtActivityDateValElement.setTextContent(randomDate());
		
		Element caseCourtActivDescTxxtElement = XmlUtils.appendElement(caseCourtEventElement, NS_NC_30, "ActivityDescriptionText");		
		caseCourtActivDescTxxtElement.setTextContent(randomString("Speeding", "Swerving", "Stealing"));
				
		Element activityNameElement = XmlUtils.appendElement(caseCourtEventElement, NS_NC_30, "ActivityName");
		activityNameElement.setTextContent(randomString("Burglary", "Speaking Falsehood", "Telling Bad Jokes"));
		
		Element courtEventJudgeElement = XmlUtils.appendElement(caseCourtEventElement, NS_JXDM_51, "CourtEventJudge");
		
		Element roleOfPersonElement = XmlUtils.appendElement(courtEventJudgeElement, NS_NC_30, "RoleOfPerson");
		
		Element judgeNameElement = XmlUtils.appendElement(roleOfPersonElement, NS_NC_30, "PersonName");
		
		Element judgeFullNameElement = XmlUtils.appendElement(judgeNameElement, NS_NC_30, "PersonFullName");
		
		judgeFullNameElement.setTextContent(getRandomName());
		
		Element commentsForCourtClerkElement = XmlUtils.appendElement(caseCourtEventElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CommentsForCourtClerk");
		commentsForCourtClerkElement.setTextContent("Comments for Clerk");
		
		Element firstCourtAprncElement = XmlUtils.appendElement(caseCourtEventElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FirstCourtAppearance");
		
		Element courtAprncDatElement = XmlUtils.appendElement(firstCourtAprncElement, NS_JXDM_51, "CourtAppearanceDate");
		
		Element courtAprncDatValElement = XmlUtils.appendElement(courtAprncDatElement, NS_NC_30, "Date");		
		courtAprncDatValElement.setTextContent(randomDate());
		
		Element courtEventCommentsTxtElement = XmlUtils.appendElement(caseCourtEventElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtEventCommentsText");		
		courtEventCommentsTxtElement.setTextContent(randomString("A lot of commotion", "Many reporters present", "Lawyers making up stories"));
		
		Element defendantSelfRepIndicElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseDefendantSelfRepresentationIndicator");		
		defendantSelfRepIndicElement.setTextContent(getRandomBooleanString());
		
		Element caseDefendantPartyElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseDefendantParty");
		
		Element defendantEntOrgElement = XmlUtils.appendElement(caseDefendantPartyElement, NS_NC_30, "EntityOrganization");
		
		Element entOrgNameElement = XmlUtils.appendElement(defendantEntOrgElement, NS_NC_30, "OrganizationName");
		entOrgNameElement.setTextContent(randomString("123 Roofing", "DisneyLand", "Moe's Junk Yard", "Lorax Cleaning"));
		
		Element entPersonElement = XmlUtils.appendElement(caseDefendantPartyElement, NS_NC_30, "EntityPerson");
		
		XmlUtils.addAttribute(entPersonElement, NS_STRUCTURES_30, "ref", "Person_01");
		
		
		Element caseDefenseAtterneyElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseDefenseAttorney");
		
		Element defenseAttortyPersonRoleElement = XmlUtils.appendElement(caseDefenseAtterneyElement, NS_NC_30, "RoleOfPerson");
		
		Element defenseAtrnyPersonNameElement = XmlUtils.appendElement(defenseAttortyPersonRoleElement, NS_NC_30, "PersonName");;
		
		Element defenseAtrnyFullNameElement = XmlUtils.appendElement(defenseAtrnyPersonNameElement, NS_NC_30, "PersonFullName");
		defenseAtrnyFullNameElement.setTextContent(getRandomName());
		
		Element caseDomViolenceElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseDomesticViolenceIndicator");
		caseDomViolenceElement.setTextContent(getRandomBooleanString());
		
		Element caseHearingElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseHearing");
		
		Element caseHearingActivIdElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityIdentification");		
		
		Element caseHearActivIdValElement = XmlUtils.appendElement(caseHearingActivIdElement, NS_NC_30, "IdentificationID");
		
		caseHearActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element activCatTxtElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityCategoryText");
		
		activCatTxtElement.setTextContent(randomString("Prosecution", "Defense", "Evidence Presentation"));
		
		Element caseHearingActivDateRangeElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityDateRange");
		
		Element caseHearingStartDateElement = XmlUtils.appendElement(caseHearingActivDateRangeElement, NS_NC_30, "StartDate");
		
		Element caseHearingStartDateValElement = XmlUtils.appendElement(caseHearingStartDateElement, NS_NC_30, "DateTime");		
		caseHearingStartDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element caseHearingEndDateElement = XmlUtils.appendElement(caseHearingActivDateRangeElement, NS_NC_30, "EndDate");
		
		Element caseHearingEndDateValElement = XmlUtils.appendElement(caseHearingEndDateElement, NS_NC_30, "DateTime");
		caseHearingEndDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element activDescTxtElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityDescriptionText");
		activDescTxtElement.setTextContent(randomString("Jury Deciding Verdict", "Jury meeting"));
		
		Element hearingActivNameElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityName");
		hearingActivNameElement.setTextContent(randomString("Pre-Trial Hearing", "Plea Hearing", "Sentence Hearing"));
		
		 Element activReasonElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityReasonText");
		 activReasonElement.setTextContent(randomString("evidence review", "hear plea agreement", "review presentence investigation report"));
		 
		Element activDispElement = XmlUtils.appendElement(caseHearingElement, NS_NC_30, "ActivityDisposition");
		
		Element activDispDescTxtElement = XmlUtils.appendElement(activDispElement, NS_NC_30, "DispositionDescriptionText");		
		activDispDescTxtElement.setTextContent(randomString("Guilty", "Not Guilty", "Hung"));
		
		
		Element caseHearingCourtEventJudgeElement = XmlUtils.appendElement(caseHearingElement, NS_JXDM_51, "CourtEventJudge");
		
		Element caseHearingJudgeRoleElement = XmlUtils.appendElement(caseHearingCourtEventJudgeElement, NS_NC_30, "RoleOfPerson");
		
		Element caseHearingJudgeNameElement = XmlUtils.appendElement(caseHearingJudgeRoleElement, NS_NC_30, "PersonName");
		
		Element caseJudgeFullNameElement = XmlUtils.appendElement(caseHearingJudgeNameElement, NS_NC_30, "PersonFullName");
		
		caseJudgeFullNameElement.setTextContent(getRandomName());
		
		Element caseHearingCommentsTxtElement = XmlUtils.appendElement(caseHearingElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtEventCommentsText");
		
		caseHearingCommentsTxtElement.setTextContent(randomString("Trial really long", "Hard to find entrance"));
		
		Element caseJudgeElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseJudge");
		
		Element caseJudgeRolePersonElement = XmlUtils.appendElement(caseJudgeElement, NS_NC_30, "RoleOfPerson");
		
		Element caseJudgeNameElement = XmlUtils.appendElement(caseJudgeRolePersonElement, NS_NC_30, "PersonName");		
		
		Element caseJudgePersonNameElement = XmlUtils.appendElement(caseJudgeNameElement, NS_NC_30, "PersonFullName");		
		caseJudgePersonNameElement.setTextContent(getRandomName());
		
		Element judicBarMemberElement = XmlUtils.appendElement(caseJudgeElement, NS_JXDM_51, "JudicialOfficialBarMembership");
		
		Element judicBarIdElement = XmlUtils.appendElement(judicBarMemberElement, NS_JXDM_51, "JudicialOfficialBarIdentification");
		
		Element judicBarIdValElement = XmlUtils.appendElement(judicBarIdElement, NS_NC_30, "IdentificationID");
		
		judicBarIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseLieneageElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseLineageCase");
		
		Element caseTrackingIdElement = XmlUtils.appendElement(caseLieneageElement, NS_NC_30, "CaseTrackingID");		
		caseTrackingIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseOtherIdElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseOtherIdentification");
		Element caseOtherIdIdElement = XmlUtils.appendElement(caseOtherIdElement, NS_NC_30, "IdentificationID");
		caseOtherIdIdElement.setTextContent(RandomStringUtils.randomAlphanumeric(8));
		
		Element caseProsecAtrnyElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseProsecutionAttorney");
		
		Element prosecAtrnyRoleElement = XmlUtils.appendElement(caseProsecAtrnyElement, NS_NC_30, "RoleOfPerson");
		
		Element prosecPersonElement = XmlUtils.appendElement(prosecAtrnyRoleElement, NS_NC_30, "PersonName");
		
		Element prosecAtrnyNameElement = XmlUtils.appendElement(prosecPersonElement, NS_NC_30, "PersonFullName");		
		prosecAtrnyNameElement.setTextContent(getRandomName());
		
		Element prosecutorBarElement = XmlUtils.appendElement(caseProsecAtrnyElement, NS_JXDM_51, "JudicialOfficialBarMembership");
		
		Element prosecutorOfficBarIdElement = XmlUtils.appendElement(prosecutorBarElement, NS_JXDM_51, "JudicialOfficialBarIdentification");
		
		Element prosecutorOfficBarIdValElement = XmlUtils.appendElement(prosecutorOfficBarIdElement, NS_NC_30, "IdentificationID");
		prosecutorOfficBarIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element judicialOfficialCategoryTextElement = XmlUtils.appendElement(caseProsecAtrnyElement, NS_JXDM_51, "JudicialOfficialCategoryText");
		judicialOfficialCategoryTextElement.setTextContent(randomString("Expengement", "Crime", "Traffic Ticket"));
		
//		<j:CaseTrial>
		Element caseTrialElement = XmlUtils.appendElement(caseAugmentElement, NS_JXDM_51, "CaseTrial");
		
		Element trialByTextElement = XmlUtils.appendElement(caseTrialElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "TrialByText");
		trialByTextElement.setTextContent("Jury");
		
		Element speedTrialDateELement = XmlUtils.appendElement(caseTrialElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "SpeedyTrialDate");
		Element speedTrialDateELementDateElement = XmlUtils.appendElement(speedTrialDateELement, NS_NC_30, "Date");
		speedTrialDateELementDateElement.setTextContent(randomDate());
//		<ccq-res-ext:CaseAugmentation>
		Element caseAugmentExtElement = XmlUtils.appendElement(caseElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CaseAugmentation");
		
		Element caseSealedIndicatorElement = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CaseSealedIndicator");		
		caseSealedIndicatorElement.setTextContent(getRandomBooleanString());
		
		Element remandDateElement = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "RemandDate");
		
		Element remandDateValElement = XmlUtils.appendElement(remandDateElement, NS_NC_30, "Date");		
		remandDateValElement.setTextContent(randomDate());
		
		Element caseUnderAdvisementDate = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CaseUnderAdvisementDate");
		Element caseUnderAdvisementDateDate = XmlUtils.appendElement(caseUnderAdvisementDate, NS_NC_30, "Date");
		caseUnderAdvisementDateDate.setTextContent(randomDate());
		
		Element juryVerdictIndicator = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "JuryVerdictIndicator");
		juryVerdictIndicator.setTextContent(randomString("true", "false"));
		
		Element failureToPayHoldIndefiniteIndicator = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FailureToPayHoldIndefiniteIndicator");
		failureToPayHoldIndefiniteIndicator.setTextContent(randomString("true", "false"));
		
		Element failureToPayHoldDate = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FailureToPayHoldDate");
		Element failureToPayHoldDateDate = XmlUtils.appendElement(failureToPayHoldDate, NS_NC_30, "Date");
		failureToPayHoldDateDate.setTextContent(randomDate());
		
		Element failureToAppearHoldDate = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FailureToAppearHoldDate");
		Element failureToAppearHoldDateDate = XmlUtils.appendElement(failureToAppearHoldDate, NS_NC_30, "Date");
		failureToAppearHoldDateDate.setTextContent(randomDate());
		
		Element failureToComplyHoldDate = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FailureToComplyHoldDate");
		Element failureToComplyHoldDateDate = XmlUtils.appendElement(failureToComplyHoldDate, NS_NC_30, "Date");
		failureToComplyHoldDateDate.setTextContent(randomDate());
		
		Element failureToPayVictimHoldDate = XmlUtils.appendElement(caseAugmentExtElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "FailureToPayVictimHoldDate");
		Element failureToPayVictimHoldDateDate = XmlUtils.appendElement(failureToPayVictimHoldDate, NS_NC_30, "Date");
		failureToPayVictimHoldDateDate.setTextContent(randomDate());
		
//		<j:AppellateCase structures:id="Appel_Case_01">
		Element apelateCaseElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "AppellateCase");		
		XmlUtils.addAttribute(apelateCaseElement, NS_STRUCTURES_30, "id", "Appel_Case_01");
		
		Element apelateCaseTrackIdElement = XmlUtils.appendElement(apelateCaseElement, NS_NC_30, "CaseTrackingID");
		apelateCaseTrackIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element apelateCaseFilingElement = XmlUtils.appendElement(apelateCaseElement, NS_NC_30, "CaseFiling");
		
		Element apelateCaseFileDocCreateDateElement = XmlUtils.appendElement(apelateCaseFilingElement, NS_NC_30, "DocumentCreationDate");
		
		Element apelateCaseFileDocDateVal = XmlUtils.appendElement(apelateCaseFileDocCreateDateElement, NS_NC_30, "Date");		
		apelateCaseFileDocDateVal.setTextContent(randomDate());
		
		Element identityElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Identity");		
		XmlUtils.addAttribute(identityElement, NS_STRUCTURES_30, "id", "Identity_01");
		
		
		Element idPersonRepElement = XmlUtils.appendElement(identityElement, NS_NC_30, "IdentityPersonRepresentation");
		
		Element idPersonDobElement = XmlUtils.appendElement(idPersonRepElement, NS_NC_30, "PersonBirthDate");
		
		Element idPersonDobValElement = XmlUtils.appendElement(idPersonDobElement, NS_NC_30, "Date");		
		idPersonDobValElement.setTextContent(randomDate());
		
		
		PersonElementWrapper samplePerson = getRandomIdentity(null);
		
		Element idPersonNameElement = XmlUtils.appendElement(idPersonRepElement, NS_NC_30, "PersonName");
		
		Element idPersonGivenName = XmlUtils.appendElement(idPersonNameElement, NS_NC_30, "PersonGivenName");		
		idPersonGivenName.setTextContent(samplePerson.firstName);
		
		Element idPersonMiddleName = XmlUtils.appendElement(idPersonNameElement, NS_NC_30, "PersonMiddleName");
		idPersonMiddleName.setTextContent(samplePerson.middleName);
		
		Element idPersonSurNameElement = XmlUtils.appendElement(idPersonNameElement, NS_NC_30, "PersonSurName"); 
		idPersonSurNameElement.setTextContent(samplePerson.lastName);
		
		Element idPersonFullNameElement = XmlUtils.appendElement(idPersonNameElement, NS_NC_30, "PersonFullName");
		idPersonFullNameElement.setTextContent(samplePerson.firstName + " " + samplePerson.middleName + " " + samplePerson.lastName);
		
		Element idPersonSsnElement = XmlUtils.appendElement(idPersonRepElement, NS_NC_30, "PersonSSNIdentification");
		
		Element idPersonSsnIdElement = XmlUtils.appendElement(idPersonSsnElement, NS_NC_30, "IdentificationID");		
		idPersonSsnIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element contactInfoElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "ContactInformation");
		XmlUtils.addAttribute(contactInfoElement, NS_STRUCTURES_30, "id", "CI_01");
		
		Element phoneElement = XmlUtils.appendElement(contactInfoElement, NS_NC_30, "ContactTelephoneNumber");
		
		Element fullPhoneElement = XmlUtils.appendElement(phoneElement, NS_NC_30, "FullTelephoneNumber");
		
		Element phoneValElement = XmlUtils.appendElement(fullPhoneElement, NS_NC_30, "TelephoneNumberFullID");
		
		String samplePhone = (String)getRandomIdentity(null).telephoneNumber;
		phoneValElement.setTextContent(samplePhone);
		
		Element elecContactIdElement = XmlUtils.appendElement(contactInfoElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "ElectronicContactIdentification");
		
		Element electContactIdValElement = XmlUtils.appendElement(elecContactIdElement, NS_NC_30, "IdentificationID");
		electContactIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element electContactIdCatDescTxtElement = XmlUtils.appendElement(elecContactIdElement, NS_NC_30, "IdentificationCategoryDescriptionText");
		electContactIdCatDescTxtElement.setTextContent(randomString("Email", "Facebook", "SkypeID"));
		
		Element citationElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "Citation");
		XmlUtils.addAttribute(citationElement, NS_STRUCTURES_30, "id", "Citation_01");
		
		Element citationActivIdElement = XmlUtils.appendElement(citationElement, NS_NC_30, "ActivityIdentification");
		
		Element citationAcivIdVAlElement = XmlUtils.appendElement(citationActivIdElement, NS_NC_30, "IdentificationID");
		citationAcivIdVAlElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element citationViolationElement = XmlUtils.appendElement(citationElement, NS_JXDM_51, "CitationViolation");
		
		Element citationActivDateElement = XmlUtils.appendElement(citationViolationElement, NS_NC_30, "ActivityDate");
		
		Element citationActivDateValElement = XmlUtils.appendElement(citationActivDateElement, NS_NC_30, "Date");		
		citationActivDateValElement.setTextContent(randomDate());
		
		
		Element incidentLocationElement = XmlUtils.appendElement(citationViolationElement, NS_NC_30, "IncidentLocation");
		
		Element incidentAddressElement = XmlUtils.appendElement(incidentLocationElement, NS_NC_30, "Address");
		
		Element incidentAddressUnitElement = XmlUtils.appendElement(incidentAddressElement, NS_NC_30, "AddressSecondaryUnitText");
		incidentAddressUnitElement.setTextContent(randomString("APT 123", "Suite 456", "St 789", "P.O. Box 4528"));
		
		Element addressPointElement = XmlUtils.appendElement(incidentAddressElement, NS_NC_30, "AddressDeliveryPointID");
		addressPointElement.setTextContent(randomString("doorstep", "mailbox", "PO box"));
		
		Element incidentLocStreetElement = XmlUtils.appendElement(incidentAddressElement, NS_NC_30, "LocationStreet");
		
		Element locStreetNameElement = XmlUtils.appendElement(incidentLocStreetElement, NS_NC_30, "StreetName");
		locStreetNameElement.setTextContent(samplePerson.addressStreetName);
		
		Element locStreetCatTxtElement = XmlUtils.appendElement(incidentLocStreetElement, NS_NC_30, "StreetCategoryText");
		locStreetCatTxtElement.setTextContent(randomString("Small Road", "Cul de Sac", "Highway", "Interstate"));
		
		Element locCountyNameElement = XmlUtils.appendElement(incidentAddressElement, NS_NC_30, "LocationCountyName");
		locCountyNameElement.setTextContent(randomString("Rowan", "Pickens", "Union", "Dorchester"));
		
		Element locDescTxtElement = XmlUtils.appendElement(incidentLocationElement, NS_NC_30, "LocationDescriptionText");
		locDescTxtElement.setTextContent(randomString("NY", "LA", "UT", "FL"));
		
		Element locNameElement = XmlUtils.appendElement(incidentLocationElement, NS_NC_30, "LocationName");
		locNameElement.setTextContent(randomString("40N 70W", "City", "County"));
		
		Element violationAugmentElement = XmlUtils.appendElement(citationViolationElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "ViolationAugmentation");
		
		Element drivingPointNumTxtElement = XmlUtils.appendElement(violationAugmentElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "DrivingPointNumberText");
		drivingPointNumTxtElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element drivingPointReducedTxtElement = XmlUtils.appendElement(violationAugmentElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "DrivingPointNumberReducedText");
		drivingPointReducedTxtElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element bacTestRefusedElement = XmlUtils.appendElement(violationAugmentElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "PersonBloodAlcoholContentTestRefusedIndicator");
		bacTestRefusedElement.setTextContent(getRandomBooleanString());
		
		
		Element citationAugElement = XmlUtils.appendElement(citationElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CitationAugmentation");
		
		Element courtAprncDateElement = XmlUtils.appendElement(citationAugElement, NS_JXDM_51, "CourtAppearanceDate");
		
		Element courtApncDateTimeElement = XmlUtils.appendElement(courtAprncDateElement, NS_NC_30, "DateTime");		
		courtApncDateTimeElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
		
		Element crtApncRqdElement = XmlUtils.appendElement(citationAugElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "CourtAppearanceRequiredIndicator");
		crtApncRqdElement.setTextContent(getRandomBooleanString());
		
		Element licSurndrdElement = XmlUtils.appendElement(citationAugElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "LicenseSurrenderedIndicator");
		licSurndrdElement.setTextContent(getRandomBooleanString());
		
		Element drivingIncElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "DrivingIncident");		
		XmlUtils.addAttribute(drivingIncElement, NS_STRUCTURES_30, "id", "Incident_01");
		
		Element incidentDescTxtElement = XmlUtils.appendElement(drivingIncElement, NS_NC_30, "ActivityDescriptionText");
		incidentDescTxtElement.setTextContent(randomString("Speeding", "Driving too slow", "Spinning Out", "Tailgating", "Blowing horn"));
		
		Element driverSpeedElement = XmlUtils.appendElement(drivingIncElement, NS_JXDM_51, "DrivingIncidentRecordedSpeedRateMeasure");
		
		Element speedMeasElement = XmlUtils.appendElement(driverSpeedElement, NS_NC_30, "MeasureValueText");
		speedMeasElement.setTextContent(RandomStringUtils.randomNumeric(3));
		
		Element speedUnitElement = XmlUtils.appendElement(driverSpeedElement, NS_NC_30, "MeasureUnitText");
		speedUnitElement.setTextContent(randomString("kt", "mph", "kph", "mpm"));
		
		Element speedLimitElement = XmlUtils.appendElement(drivingIncElement, NS_JXDM_51, "DrivingIncidentLegalSpeedRateMeasure");
		
		Element speedLimitNumElement = XmlUtils.appendElement(speedLimitElement, NS_NC_30, "MeasureValueText");		
		speedLimitNumElement.setTextContent(RandomStringUtils.randomNumeric(2));
		
		Element speedUnitsElement = XmlUtils.appendElement(speedLimitElement, NS_NC_30, "MeasureUnitText");
		speedUnitsElement.setTextContent(randomString("kt", "mph", "kph", "mpm"));
		
		Element arrestElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "Arrest");
		XmlUtils.addAttribute(arrestElement, NS_STRUCTURES_30, "id", "Arrest_01");
		
		Element arrestActivDateElement = XmlUtils.appendElement(arrestElement, NS_NC_30, "ActivityDate");
		
		Element arrestActivDateValElement = XmlUtils.appendElement(arrestActivDateElement, NS_NC_30, "Date");
		arrestActivDateValElement.setTextContent(randomDate());
		
		Element arrestAgencyRecIdElement = XmlUtils.appendElement(arrestElement, NS_JXDM_51, "ArrestAgencyRecordIdentification");
		
		Element arrestRecIdElement = XmlUtils.appendElement(arrestAgencyRecIdElement, NS_NC_30, "IdentificationID");
		arrestRecIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		
		Element arrestChargeElement = XmlUtils.appendElement(arrestElement, NS_JXDM_51, "ArrestCharge");
		
		Element arrestChargeStatuteElement = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_51, "ChargeStatute");
		
		Element arrestChargeStatCodeIdElement = XmlUtils.appendElement(arrestChargeStatuteElement, NS_JXDM_51, "StatuteCodeIdentification");
		
		Element arrestChargeIdElement = XmlUtils.appendElement(arrestChargeStatCodeIdElement, NS_NC_30, "IdentificationID");		
		arrestChargeIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element arrestLocElement = XmlUtils.appendElement(arrestElement, NS_JXDM_51, "ArrestLocation");
		
		Element arrestLocDescTxtElement = XmlUtils.appendElement(arrestLocElement, NS_NC_30, "LocationDescriptionText");		
		arrestLocDescTxtElement.setTextContent(randomString("NY", "LA", "SF", "FL", "ATL"));
		
		Element arrestOfficialElement = XmlUtils.appendElement(arrestElement, NS_JXDM_51, "ArrestOfficial");		
		XmlUtils.addAttribute(arrestOfficialElement, NS_STRUCTURES_30, "id", "Officer_01");
		
		Element officalRoleElement = XmlUtils.appendElement(arrestOfficialElement, NS_NC_30, "RoleOfPerson");
		
		Element arrestOfficNameElement = XmlUtils.appendElement(officalRoleElement, NS_NC_30, "PersonName");
		
		Element arrestFullNameElement = XmlUtils.appendElement(arrestOfficNameElement, NS_NC_30, "PersonFullName");			
		arrestFullNameElement.setTextContent(getRandomName());
		
		Element leEnforcUnitElement = XmlUtils.appendElement(arrestOfficialElement, NS_JXDM_51, "EnforcementOfficialUnit");
		
		Element leEnforceUnitNameElement = XmlUtils.appendElement(leEnforcUnitElement, NS_JXDM_51, "EnforcementUnitName");		
		leEnforceUnitNameElement.setTextContent(randomString("LAPD", "NYPD", "Walker Texas Ranger"));
		
		Element detentionElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "Detention");
		XmlUtils.addAttribute(detentionElement, NS_STRUCTURES_30, "id", "Detention_01");
				
		Element detentionActivElement = XmlUtils.appendElement(detentionElement, NS_NC_30, "ActivityIdentification");
		
		Element detActivIdElement = XmlUtils.appendElement(detentionActivElement, NS_NC_30, "IdentificationID");
		
		detActivIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
				
		Element detActivCatTxtElement = XmlUtils.appendElement(detentionElement, NS_NC_30, "ActivityCategoryText");
		detActivCatTxtElement.setTextContent(randomString("Jail", "Prison", "Community Service"));
		
		Element detActivDatRangeElement = XmlUtils.appendElement(detentionElement, NS_NC_30, "ActivityDateRange");
		
		Element detStartDateElement = XmlUtils.appendElement(detActivDatRangeElement, NS_NC_30, "StartDate");
		
		Element detStartDateValElement = XmlUtils.appendElement(detStartDateElement, NS_NC_30, "Date");		
		detStartDateValElement.setTextContent(randomDate());
		
		Element detEndDateElement = XmlUtils.appendElement(detActivDatRangeElement, NS_NC_30, "EndDate");
		
		Element endDateValElement = XmlUtils.appendElement(detEndDateElement, NS_NC_30, "Date");		
		endDateValElement.setTextContent(randomDate());
		
		Element superviseCustStatus = XmlUtils.appendElement(detentionElement, NS_NC_30, "SupervisionCustodyStatus");
		
	 	Element superviseStatDescTxtElement = XmlUtils.appendElement(superviseCustStatus, NS_NC_30, "StatusDescriptionText");		
	 	superviseStatDescTxtElement.setTextContent(randomString("Parents Watching", "Big Brother Custody", "Homeless Shelter"));
	 	
	 	Element supervisFacElement = XmlUtils.appendElement(detentionElement, NS_NC_30, "SupervisionFacility");
		
	 	Element facIdElement = XmlUtils.appendElement(supervisFacElement, NS_NC_30, "FacilityIdentification");
	 	
	 	Element facIdValElement = XmlUtils.appendElement(facIdElement, NS_NC_30, "IdentificationID");
	 	facIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element bailBondElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "BailBond");	 	
	 	XmlUtils.addAttribute(bailBondElement, NS_STRUCTURES_30, "id", "Bond_01");
	 	
	 	Element bailBondActivIdElement = XmlUtils.appendElement(bailBondElement, NS_NC_30, "ActivityIdentification");
	 	
	 	Element bailBondActivIdValElement = XmlUtils.appendElement(bailBondActivIdElement, NS_NC_30, "IdentificationID");	 	
	 	bailBondActivIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element bailBondActivCatTxtElement = XmlUtils.appendElement(bailBondElement, NS_NC_30, "ActivityCategoryText");
	 	bailBondActivCatTxtElement.setTextContent(randomString("Bond", "Bail", "Recognizance"));
	 	
	 	Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, NS_JXDM_51, "BailBondAmount");
	 	
	 	Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, NS_NC_30, "Amount");
		
	 	bailBondAmountValElement.setTextContent(RandomStringUtils.randomNumeric(4));
	 	
	 	
	 	Element bondIssuerElement = XmlUtils.appendElement(bailBondElement, NS_JXDM_51, "BailBondIssuerEntity"); 
	 	
	 	Element bondEntOrgElement = XmlUtils.appendElement(bondIssuerElement, NS_NC_30, "EntityOrganization");
	 	
	 	Element bondOrgNameElement = XmlUtils.appendElement(bondEntOrgElement, NS_NC_30, "OrganizationName");
	 	
	 	bondOrgNameElement.setTextContent(randomString("Bonds 4 Cheap", "25% Bonds", "24hr Bonding", "Bonds Advance"));
	 	
appendWarrantElement(rootCourtCaseElement);
	 	
	 	
	 	
	 	Element vehicleElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Vehicle");	 	
	 	XmlUtils.addAttribute(vehicleElement, NS_STRUCTURES_30, "id", "Vehicle_01");
	 	
	 	Element vehicCmvElement = XmlUtils.appendElement(vehicleElement, NS_NC_30, "VehicleCMVIndicator");
	 	vehicCmvElement.setTextContent(getRandomBooleanString());
	 	
	 	Element vehicleIdElement = XmlUtils.appendElement(vehicleElement, NS_NC_30, "VehicleIdentification");
	 	
	 	Element vinValueElement = XmlUtils.appendElement(vehicleIdElement, NS_NC_30, "IdentificationID");	 	
	 	vinValueElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element vehicleMakeCode = XmlUtils.appendElement(vehicleElement, NS_JXDM_51, "VehicleMakeCode");
	 	
	 	vehicleMakeCode.setTextContent(randomString("ALED", "ALEN", "ALEX", "ALEX", "ALEX", "ALFA", "ALFA", "ALFB", 
	 			"ALFL", "ALFO", "ALIE", "ALIS", "ALJO", "ALKO", "ALL", "ALLA", "ALLB", "ALLC", "ALLD"));
	 	
	 	Element vehicleModelElement = XmlUtils.appendElement(vehicleElement, NS_JXDM_51, "VehicleModelCode");
	 	
	 	vehicleModelElement.setTextContent(randomString("J72", "JAC", "JAL", "JAR", "JAV", "JCW", "JEP", "JET", "JET", "JIM", 
	 			"JMY", "JNY", "JTF", "JTS", "JUK", "JUS", "JX3", "K55", "K63", "KA", "KAD", "KAL", "KAP", "KAR", "KAR", "KHA", "KIN", "KIZ", 
	 			"KOM", "KOR", "KR", "KR1", "KRM", "L30", "L43"));
	 	
	 	Element personElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Person");	 	
	 	XmlUtils.addAttribute(personElement, NS_STRUCTURES_30, "id", "Person_01");
	 	
	 	Element personDobElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonBirthDate");
	 	
	 	Element dobValElement = XmlUtils.appendElement(personDobElement, NS_NC_30, "Date");	 
	 	
	 	PersonElementWrapper randomPerson = getRandomIdentity(null);
	 	dobValElement.setTextContent(randomPerson.birthdate.toString("yyyy-MM-dd"));
	 	
	 	Element personDescElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonDescriptionText");
	 	personDescElement.setTextContent(randomString("Really Tall", "Short white female", "Tall black male", "Thin Asian female", "Very Large"));
	 	
	 	Element ethnicityElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonEthnicityText");
	 	ethnicityElement.setTextContent(randomString("W", "B", "A", "L"));
	 	
	 	Element eyeColorElement = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonEyeColorCode");
	 	eyeColorElement.setTextContent(randomString("BLK", "BLU", "BRO", "GRN", "GRY", "HAZ", "MAR", "MUL", "PNK"));
	 	
	 	Element hairElement = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonHairColorCode");
	 	hairElement.setTextContent(randomString("BLD", "BLK", "BLN", "BLU", "BRO", "GRN", "GRY", "ONG", "PLE", "PNK", "RED", "SDY", "WHI"));
	 	
	 	Element heightElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonHeightMeasure");
	 	
	 	Element heightValueElement = XmlUtils.appendElement(heightElement, NS_NC_30, "MeasureValueText");	 	
	 	heightValueElement.setTextContent(RandomStringUtils.randomNumeric(2));
	 	
	 	Element heightUnitElement = XmlUtils.appendElement(heightElement, NS_NC_30, "MeasureUnitText");
	 	heightUnitElement.setTextContent(randomString("in", "cm", "mm", "me", "ft"));
	 	
	 	
	 	PersonElementWrapper samplePerson2 = getRandomIdentity(null);
	 	
	 	Element personNameElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonName");
	 	
	 	Element givenNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonGivenName");
	 		 		 	
	 	givenNameElement.setTextContent(samplePerson2.firstName);
	 		 	
	 	Element middleNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonMiddleName");
	 	
	 	middleNameElement.setTextContent(samplePerson2.middleName);
	 	
	 	Element surNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonSurName");
	 	
	 	surNameElement.setTextContent(samplePerson2.lastName);
	 	
	 	Element personFullNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonFullName");
	 		 		 	
	 	personFullNameElement.setTextContent(samplePerson2.firstName + " " + samplePerson2.middleName + " " + samplePerson2.lastName);

//	 	TODO fix this
//	 	Element perosnOtherIdElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonOtherIdentification");
//	 	Element personOtherIdIdElement = XmlUtils.appendElement(perosnOtherIdElement, NS_NC_30, "Identification"); 
//	 	personOtherIdIdElement.setTextContent(RandomStringUtils.randomAlphanumeric(8));
//	 	
	 	Element personPhysicalFeatureElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonPhysicalFeature");
	 	Element physicalFeatureDescriptionText = XmlUtils.appendElement(personPhysicalFeatureElement, NS_NC_30, "PhysicalFeatureDescriptionText");
	 	physicalFeatureDescriptionText.setTextContent(randomString("Scars", "Marks", "Tattoos"));
	 	
	 	Element personRaceElement = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonRaceCode");
	 	
	 	personRaceElement.setTextContent(randomString("A", "B", "I", "U", "W"));
	 	
	 	Element personSexCodeElement = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonSexCode");
	 	personSexCodeElement.setTextContent(randomString("M", "F"));	 		 	
	 	
	 	Element ssnElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonSSNIdentification");

	 	Element ssnValElement = XmlUtils.appendElement(ssnElement, NS_NC_30, "IdentificationID");	 	
	 	ssnValElement.setTextContent(RandomStringUtils.randomNumeric(9));
	 	
	 	Element weightElement = XmlUtils.appendElement(personElement, NS_NC_30, "PersonWeightMeasure");
	 	
	 	Element weightMeasureElement = XmlUtils.appendElement(weightElement, NS_NC_30, "MeasureValueText");	 	
	 	weightMeasureElement.setTextContent(RandomStringUtils.randomNumeric(3));

//	 	<cyfs:PersonAugmentation>
	 	long age = getAge(randomPerson);
	 	if (age <= 18){
		 	Element cyfsPersonAugmentElement = XmlUtils.appendElement(personElement, NS_CYFS_31, "PersonAugmentation");
		 	Element studentIdElement = XmlUtils.appendElement(cyfsPersonAugmentElement, NS_CYFS_31, "StudentIdentification");
		 	Element studentIdIdElement = XmlUtils.appendElement(studentIdElement, NS_NC_30, "IdentificationID");
		 	studentIdIdElement.setTextContent(RandomStringUtils.randomNumeric(7));
	 	}
//	 	<intel:PersonAugmentation> 
	 	Element intelPersonAugmentElement = XmlUtils.appendElement(personElement, NS_INTEL_31, "PersonAugmentation");
	 	Element personSystemId = XmlUtils.appendElement(intelPersonAugmentElement, NS_INTEL_31, "PersonSystemIdentification");
	 	Element personSystemIdIdElement = XmlUtils.appendElement(personSystemId, NS_NC_30, "IdentificationID");
	 	personSystemIdIdElement.setTextContent(RandomStringUtils.randomNumeric(7));
	 	
	 	
	 	Element personAugmentElement = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonAugmentation");
	 	
	 	Element drivLicElement = XmlUtils.appendElement(personAugmentElement, NS_JXDM_51, "DriverLicense");
	 	
	 	Element drivLicIdElement = XmlUtils.appendElement(drivLicElement, NS_JXDM_51, "DriverLicenseIdentification");
	 	
	 	Element drivLicIdValElement = XmlUtils.appendElement(drivLicIdElement, NS_NC_30, "IdentificationID");
	 	drivLicIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element drivLicIdSrcElement = XmlUtils.appendElement(drivLicIdElement, NS_NC_30, "IdentificationSourceText");
	 	drivLicIdSrcElement.setTextContent(randomString("SC", "NC", "GA", "FL", "ME", "MA", "VT", "TX"));
	 	
	 	Element drivLicExpDateElement = XmlUtils.appendElement(drivLicElement, NS_JXDM_51, "DriverLicenseExpirationDate");
	 	
	 	Element drivLicExpDateValElement = XmlUtils.appendElement(drivLicExpDateElement, NS_NC_30, "DateTime");
	 	drivLicExpDateValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 		 	
	 	Element drivLicIssDateElement = XmlUtils.appendElement(drivLicElement, NS_JXDM_51, "DriverLicenseIssueDate");
	 	
	 	Element drivLicIssDatValElement = XmlUtils.appendElement(drivLicIssDateElement, NS_NC_30, "DateTime");	 	
	 	drivLicIssDatValElement.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 	String commercialClassString = randomString("", "A", "B", "C");
	 	if (StringUtils.isNotBlank(commercialClassString)){
	 		Element commercialClassTextElement = XmlUtils.appendElement(drivLicElement, NS_JXDM_51, "DriverLicenseCommercialClassText");
	 		commercialClassTextElement.setTextContent(commercialClassString);
	 	}
	 	
	 	Element drivLicClassElement = XmlUtils.appendElement(drivLicElement, NS_JXDM_51, "DriverLicenseNonCommercialClassText");	 	
	 	drivLicClassElement.setTextContent(randomString("C", "M", "L", "D", "S"));
	 	
	 	Element fbiElement = XmlUtils.appendElement(personAugmentElement, NS_JXDM_51, "PersonFBIIdentification");
	 	
	 	Element fbiIdValElement = XmlUtils.appendElement(fbiElement, NS_NC_30, "IdentificationID");	 	
	 	fbiIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	
	 	Element sidElement = XmlUtils.appendElement(personAugmentElement, NS_JXDM_51, "PersonStateFingerprintIdentification");
	 	
	 	Element sidValElement = XmlUtils.appendElement(sidElement, NS_NC_30, "IdentificationID");	 	
	 	sidValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element personInmateIdElement = XmlUtils.appendElement(personElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "PersonInmateIdentification");	 	
	 	Element personInmateIdIdElement = XmlUtils.appendElement(personInmateIdElement, NS_NC_30, "IdentificationID");	 	
	 	personInmateIdIdElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	Element personRecIdEl = XmlUtils.appendElement(personElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "PersonRecordIdentification");	 	
	 	Element personRecIdValEl = XmlUtils.appendElement(personRecIdEl, NS_NC_30, "IdentificationID");	 	
	 	personRecIdValEl.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	
	 	Element locElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Location");	 	
	 	XmlUtils.addAttribute(locElement, NS_STRUCTURES_30, "id", "Location_01");
	 	
	 	Element locAddressElement = XmlUtils.appendElement(locElement, NS_NC_30, "Address");
	 	
	 	Element addressUnitElement = XmlUtils.appendElement(locAddressElement, NS_NC_30, "AddressSecondaryUnitText");
	 	addressUnitElement.setTextContent(randomString("APT 234", "STE 567", "PO Box 789", "2nd Floor", "Door on left"));
	 	
	 	Element locStreetElement = XmlUtils.appendElement(locAddressElement, NS_NC_30, "LocationStreet");
	 	
	 	Element locStreetFullElement = XmlUtils.appendElement(locStreetElement, NS_NC_30, "StreetFullText");
	 	
	 	locStreetFullElement.setTextContent(samplePerson2.addressStreetName);
	 	
	 	Element locCityElement = XmlUtils.appendElement(locAddressElement, NS_NC_30, "LocationCityName");
	 	locCityElement.setTextContent(samplePerson2.city);
	 	
	 	Element locStateElement = XmlUtils.appendElement(locAddressElement, NS_NC_30, "LocationStateName");
	 	locStateElement.setTextContent(samplePerson2.state);
	 	
	 	Element locZipElement = XmlUtils.appendElement(locAddressElement, NS_NC_30, "LocationPostalCode");
	 	locZipElement.setTextContent(samplePerson2.zipCode);
	 	
	 	Element locCatTxtElement = XmlUtils.appendElement(locElement, NS_NC_30, "LocationCategoryText");
	 	locCatTxtElement.setTextContent(randomString("around corner", "North", "East", "South", "West"));
	 	
	 	Element activCaseAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "ActivityCaseAssociation");
	 	
	 	Element citationActivElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(citationActivElement, NS_STRUCTURES_30, "ref", "Citation_01");	 	
	 	
	 	Element warrantActivityElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(warrantActivityElement, NS_STRUCTURES_30, "ref", "Warrant_01");	 	
	 	
	 	Element arrestActivityElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(arrestActivityElement, NS_STRUCTURES_30, "ref", "Arrest_01");
	 	
	 	Element detentionActivityElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(detentionActivityElement, NS_STRUCTURES_30, "ref", "Detention_01");
	 	
	 	Element bondActivityElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(bondActivityElement, NS_STRUCTURES_30, "ref", "Bond_01");
	 	
	 	Element caseAssocElement = XmlUtils.appendElement(activCaseAssocElement, NS_NC_30, "Case");
	 	XmlUtils.addAttribute(caseAssocElement, NS_STRUCTURES_30, "ref", "Case_01");
	 	
	 	Element relatedCaseAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "RelatedCaseAssociation");
	 	
	 	Element relatedCaseActivityElement = XmlUtils.appendElement(relatedCaseAssocElement, NS_NC_30, "Activity");	 	
	 	XmlUtils.addAttribute(relatedCaseActivityElement, NS_STRUCTURES_30, "ref", "Case_01");
	 	
	 	Element appelCaseActivElement = XmlUtils.appendElement(relatedCaseAssocElement, NS_NC_30, "Activity");
	 	XmlUtils.addAttribute(appelCaseActivElement, NS_STRUCTURES_30, "ref", "Appel_Case_01");
	 	
	 	
	 	Element activIncidentAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "ActivityIncidentAssociation");
	 	
	 	Element incidentCitationActivityElement = XmlUtils.appendElement(activIncidentAssocElement, NS_NC_30, "Activity");	 	
	 	XmlUtils.addAttribute(incidentCitationActivityElement, NS_STRUCTURES_30, "ref", "Citation_01");
	 	
	 	Element activIncidentIncElement = XmlUtils.appendElement(activIncidentAssocElement, NS_NC_30, "Incident");
	 	XmlUtils.addAttribute(activIncidentIncElement, NS_STRUCTURES_30, "ref", "Incident_01");
	 	
	 	Element contactInfoAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "ContactInformationAssociation");
	 	
	 	Element contactEntElement = XmlUtils.appendElement(contactInfoAssocElement, NS_NC_30, "ContactEntity");
	 	XmlUtils.addAttribute(contactEntElement, NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element contactAssocInfoElement = XmlUtils.appendElement(contactInfoAssocElement, NS_NC_30, "ContactInformation");	 	
	 	XmlUtils.addAttribute(contactAssocInfoElement, NS_STRUCTURES_30, "ref", "CI_01");
	 	
	 	
	 	Element personConveyAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "PersonConveyanceAssociation");
	 	
	 	Element conveyAssocPersonElement = XmlUtils.appendElement(personConveyAssocElement, NS_NC_30, "Person");
	 	XmlUtils.addAttribute(conveyAssocPersonElement, NS_STRUCTURES_30, "ref", "Person_01");
	 		 	
	 	Element conveyAssocVehicleElement = XmlUtils.appendElement(personConveyAssocElement, NS_NC_30, "Conveyance");	 	
	 	XmlUtils.addAttribute(conveyAssocVehicleElement, NS_STRUCTURES_30, "ref", "Vehicle_01");
	 	
	 	
	 	Element personResAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "PersonResidenceAssociation");
	 	
	 	Element resAssocPersonElement = XmlUtils.appendElement(personResAssocElement, NS_NC_30, "Person");	 	
	 	XmlUtils.addAttribute(resAssocPersonElement, NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element resLocElement = XmlUtils.appendElement(personResAssocElement, NS_NC_30, "Location");
	 	XmlUtils.addAttribute(resLocElement, NS_STRUCTURES_30, "ref", "Location_01");
	 	
	 	Element aliasIdentAssocElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "PersonAliasIdentityAssociation");
	 	
	 	Element aliasPerson = XmlUtils.appendElement(aliasIdentAssocElement, NS_NC_30, "Person");
	 	XmlUtils.addAttribute(aliasPerson, NS_STRUCTURES_30, "ref", "Person_01");
	 	
	 	Element aliasIdentElement = XmlUtils.appendElement(aliasIdentAssocElement, NS_NC_30, "Identity");
	 	XmlUtils.addAttribute(aliasIdentElement, NS_STRUCTURES_30, "ref", "Identity_01");
	 	
	 	Element srcSysNamTxtElement = XmlUtils.appendElement(rootCourtCaseElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "SourceSystemNameText");

	 	srcSysNamTxtElement.setTextContent(randomString("Thornton Municipal FullCourt", "Adams County County Court FullCourt", "Pima County District Court Tyler Odysee"));
	 		 	
	 	Element queryResCatTxtElement = XmlUtils.appendElement(rootCourtCaseElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "QueryResultCategoryText");
	 	queryResCatTxtElement.setTextContent(randomString("Results", "Result List", "Results Returned"));
	 	
	 	Element infoOwnOrgElement = XmlUtils.appendElement(rootCourtCaseElement, NS_COURT_CASE_QUERY_RESULTS_EXT, "InformationOwningOrganization");
	 	
	 	Element orgBranchNameElement = XmlUtils.appendElement(infoOwnOrgElement, NS_NC_30, "OrganizationBranchName");
	 	orgBranchNameElement.setTextContent(randomString("Large Search Company", "Acme", "Acme Search Finder", "Acme people lookup finder", "Cops", "Walker Texas Ranger"));
	 	
	 	Element orgNameElement = XmlUtils.appendElement(infoOwnOrgElement, NS_NC_30, "OrganizationName");
	 	orgNameElement.setTextContent(randomString("COPS", "Walker Texas Ranger", "Matlock"));
	 		 	
	 	Element metadataElement = XmlUtils.appendElement(rootCourtCaseElement, NS_NC_30, "Metadata");
	 	
	 	Element lastUpdateDateElement = XmlUtils.appendElement(metadataElement, NS_NC_30, "LastUpdatedDate");
	 	
	 	Element lastUpdateDateValElement = XmlUtils.appendElement(lastUpdateDateElement, NS_NC_30, "Date");	 	
	 	lastUpdateDateValElement.setTextContent(randomDate());
	 	
	 	
	 	Element queryRestMetaElement = XmlUtils.appendElement(rootCourtCaseElement, NS_QRM, "QueryResultsMetadata");
	 	
	 	Element infoAccessDenialElement = XmlUtils.appendElement(queryRestMetaElement, NS_IAD, "InformationAccessDenial");
	 	
	 	Element infoAccessDenialIndicElement = XmlUtils.appendElement(infoAccessDenialElement, NS_IAD, "InformationAccessDenialIndicator");	 	
	 	infoAccessDenialIndicElement.setTextContent(getRandomBooleanString());
	 		 	
	 	Element infoAccessDenySysTxtElement = XmlUtils.appendElement(infoAccessDenialElement, NS_IAD, "InformationAccessDenyingSystemNameText");
	 	infoAccessDenySysTxtElement.setTextContent(randomString("BG Checker", "Background Checkers", "Rapback Deniers", "FBI", "Clearance Beakers"));
	 	
	 	Element infoAccDenySysNamElement = XmlUtils.appendElement(infoAccessDenialElement, NS_IAD, "InformationAccessDenialReasonText");
	 	infoAccDenySysNamElement.setTextContent(randomString("On deny list", "On no-fly list", "Gossips", "Sells info to others", "Can't keep a secret"));
	 	
	 		 	
	 	Element queryRequestErrorElement = XmlUtils.appendElement(queryRestMetaElement, NS_QRER, "QueryRequestError");
	 	
	 	Element errorTxtElement = XmlUtils.appendElement(queryRequestErrorElement, NS_QRER, "ErrorText");
	 	errorTxtElement.setTextContent(randomString("Can't do that query", "Can't find anyone", "Looked too long"));
	 	
	 	Element queryReqIdElement = XmlUtils.appendElement(queryRequestErrorElement, NS_QRER, "QueryRequestIdentification");
	 	
	 	Element queryReqIdValElement = XmlUtils.appendElement(queryReqIdElement, NS_NC_30, "IdentificationID");	 	
	 	queryReqIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
	 	
	 	//TODO maybe correct sample data
	 	Element idCatDescElment = XmlUtils.appendElement(queryReqIdElement, NS_NC_30, "IdentificationCategoryDescriptionText");
	 	idCatDescElment.setTextContent(randomString("Driver License", "Gvmt Id", "Passport", "Concealed Weapons Permit"));
	 	
	 	Element idSrcTxtElement = XmlUtils.appendElement(queryReqIdElement, NS_NC_30, "IdentificationSourceText");
	 	idSrcTxtElement.setTextContent(samplePerson.state);
	 	
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootCourtCaseElement);
		
		return rCourtCaseDetailDoc;
	}


	private void appendWarrantElement(Element rootCourtCaseElement) throws DOMException, IOException {
	 	Element warrantElment = XmlUtils.appendElement(rootCourtCaseElement, NS_JXDM_51, "Warrant");
	 	XmlUtils.addAttribute(warrantElment, NS_STRUCTURES_30, "id", "Warrant_01");
	 	
	 	Element activityIdElement = XmlUtils.appendElement(warrantElment, NS_NC_30, "ActivityIdentification");
	 	Element identificationId = XmlUtils.appendElement(activityIdElement, NS_NC_30, "IdentificationID");
	 	identificationId.setTextContent(RandomStringUtils.randomAlphanumeric(8));
	 	
	 	Element activityDescriptionText = XmlUtils.appendElement(warrantElment, NS_NC_30, "ActivityDescriptionText");
	 	activityDescriptionText.setTextContent("Warrant description");
	 	
	 	Element courtOrderIssuingDate = XmlUtils.appendElement(warrantElment, NS_JXDM_51, "CourtOrderIssuingDate");
	 	Element dateTime = XmlUtils.appendElement(courtOrderIssuingDate, NS_NC_30, "DateTime");
	 	dateTime.setTextContent(randomDate("yyyy-MM-dd'T'HH:mm:ss"));
	 	
	 	Element warrantCategoryText = XmlUtils.appendElement(warrantElment, NS_JXDM_51, "WarrantCategoryText"); 
	 	warrantCategoryText.setTextContent(randomString("Arrest Warrant", "Warrant of Committal", "Warrant of Execution", "Execution Warrant"));
	}


	private long getAge(PersonElementWrapper randomPerson) {
		DateTime birthDate = randomPerson.birthdate; 
	 	java.time.LocalDate birthDay = java.time.LocalDate.of(birthDate.getYear(), birthDate.getMonthOfYear(), birthDate.getDayOfMonth());
	 	long age = birthDay.until(java.time.LocalDate.now(), ChronoUnit.YEARS );
		return age;
	}
	
	
	private Element appendCaseChargeSampleElement(Element parentCaseAugmentElement) throws DOMException, IOException {

		Element caseChargeElement = XmlUtils.appendElement(parentCaseAugmentElement, NS_JXDM_51, "CaseCharge");
		
		Element caseChargeCountElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeCountQuantity");	
		String sampleCaseChargeCount = RandomStringUtils.randomNumeric(1);		
		caseChargeCountElement.setTextContent(sampleCaseChargeCount);
		
		Element caseChargeDescTxtElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeDescriptionText");
		
		String sampleCaseChargeDesc = randomString("Not using wipers", 
				"Not using headlights",
				"Not wearing seatbelt",
				"Swerving");
		
		caseChargeDescTxtElement.setTextContent(sampleCaseChargeDesc);
		
		Element chargeDispElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeDisposition");
		
		Element chargeDispDateElement = XmlUtils.appendElement(chargeDispElement, NS_NC_30, "DispositionDate");		
		Element chargeDispDateValElement = XmlUtils.appendElement(chargeDispDateElement, NS_NC_30, "Date");		
		chargeDispDateValElement.setTextContent(randomDate());
		
		
		Element chargeDispDescTxt = XmlUtils.appendElement(chargeDispElement, NS_NC_30, "DispositionDescriptionText");
		
		String sampleDispDesc = randomString("Plea agreement", 
				"Plea disagreement");
		
		chargeDispDescTxt.setTextContent(sampleDispDesc);
		
		Element chargeDispOtherTxtElement = XmlUtils.appendElement(chargeDispElement, NS_JXDM_51, "ChargeDispositionOtherText");
		
		String sampleDispOtherTxt = randomString("Assault on a Police Officer", 
				"Kicked neighbor's dog",
				"Threw neighbor's cat");
		
		chargeDispOtherTxtElement.setTextContent(sampleDispOtherTxt);
		
		Element caseChargeFilingDateElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeFilingDate");		
		Element caseChargeFilingDateValElement = XmlUtils.appendElement(caseChargeFilingDateElement, NS_NC_30, "Date");		
		caseChargeFilingDateValElement.setTextContent(randomDate());
		
		Element chargePleaElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargePlea");
		
		Element chargePleaActivityDateElement = XmlUtils.appendElement(chargePleaElement, NS_NC_30, "ActivityDate");		
		Element chargePleaActivityDateValElement = XmlUtils.appendElement(chargePleaActivityDateElement, NS_NC_30, "Date");		
		chargePleaActivityDateValElement.setTextContent(randomDate());
		
		Element pleaCatCodeElement = XmlUtils.appendElement(chargePleaElement, NS_JXDM_51, "PleaCategoryCode");				
		pleaCatCodeElement.setTextContent(randomString("A", "C", "G", "I", "M", "N", "U", "X"));
		
		Element chargeSentenceElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeSentence");
		
		Element chargeSentActivityIdElement = XmlUtils.appendElement(chargeSentenceElement, NS_NC_30, "ActivityIdentification");
		
		Element chargeSentActivIdValElement = XmlUtils.appendElement(chargeSentActivityIdElement, NS_NC_30, "IdentificationID");
		
		String sampleChargeActivId = RandomStringUtils.randomNumeric(8);		
		chargeSentActivIdValElement.setTextContent(sampleChargeActivId);
		
		Element chargeSentenceActivDateElement = XmlUtils.appendElement(chargeSentenceElement, NS_NC_30, "ActivityDate");		
		Element chargeSentActivDateValElement = XmlUtils.appendElement(chargeSentenceActivDateElement, NS_NC_30, "Date");		
		chargeSentActivDateValElement.setTextContent(randomDate());
				
		Element chargeSentActivStatElement= XmlUtils.appendElement(chargeSentenceElement, NS_NC_30, "ActivityStatus");		
		Element chargeSentActivStatDescTxtElement = XmlUtils.appendElement(chargeSentActivStatElement, NS_NC_30, "StatusDescriptionText");
		
		String sampleActivStatus = randomString("Still deciding", 
				"Descision Pending",
				"Final Decision");
		
		chargeSentActivStatDescTxtElement.setTextContent(sampleActivStatus);
		
		Element chargeSentenceChargeElement = XmlUtils.appendElement(chargeSentenceElement, NS_JXDM_51, "SentenceCharge");		
		Element sentChargeStatElement = XmlUtils.appendElement(chargeSentenceChargeElement, NS_JXDM_51, "ChargeStatute");
		
		Element sentStatCodeIdElement = XmlUtils.appendElement(sentChargeStatElement, NS_JXDM_51, "StatuteCodeIdentification"); 
		
		Element sentStatCodeIdValElement = XmlUtils.appendElement(sentStatCodeIdElement, NS_NC_30, "IdentificationID");		
		String sampleSentStatId = RandomStringUtils.randomNumeric(8);		
		sentStatCodeIdValElement.setTextContent(sampleSentStatId);
		
		Element chargeSentenceTermElement = XmlUtils.appendElement(chargeSentenceElement, NS_JXDM_51, "SentenceTerm");
		
		Element chargeSentMaxTermElement = XmlUtils.appendElement(chargeSentenceTermElement, NS_JXDM_51, "TermMaximumDuration");
		
		String sampleMaxTerm = randomString("P2Y4M3DT08H30M0S", 
				"P4Y7M8DT10H30M0S");	
		
		chargeSentMaxTermElement.setTextContent(sampleMaxTerm);
		
		Element chargeSentMinTermElement = XmlUtils.appendElement(chargeSentenceTermElement, NS_JXDM_51, "TermMinimumDuration");
		
		String sampleMinTerm = randomString("P2Y4M3DT08H30M0S", 
				"P1Y4M3DT08H30M0S");
		
		chargeSentMinTermElement.setTextContent(sampleMinTerm);
		
		Element chargeSentSuperviseFineAmountElement = XmlUtils.appendElement(chargeSentenceElement, NS_JXDM_51, "SupervisionFineAmount");
		
		Element chargeSentFineAmountValElement = XmlUtils.appendElement(chargeSentSuperviseFineAmountElement, NS_NC_30, "Amount");		
		chargeSentFineAmountValElement.setTextContent(randomString("25.70", "9800.99", "5.43", "99.99"));
		
		// TODO confirm unneeded
		//		<ccq-res-ext:SentenceDeferredDate>
		//		<nc:Date>2001-12-17</nc:Date>
		//	</ccq-res-ext:SentenceDeferredDate>
		//	<ccq-res-ext:SentenceConfinementCategoryText>confinement type
		//	</ccq-res-ext:SentenceConfinementCategoryText>
		//	<ccq-res-ext:SentenceConfinementComment>confinement comment
		//	</ccq-res-ext:SentenceConfinementComment>
		//	<ccq-res-ext:SentenceWorkReleaseIndicator>true
		//	</ccq-res-ext:SentenceWorkReleaseIndicator>
		
		
		Element caseChargeSeqIdElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeSequenceID");		
		caseChargeSeqIdElement.setTextContent(RandomStringUtils.randomNumeric(2));
				
		Element caseChargeStatuteElement = XmlUtils.appendElement(caseChargeElement, NS_JXDM_51, "ChargeStatute");
		
		Element caseChargeStatuteCodeIdElement = XmlUtils.appendElement(caseChargeStatuteElement, NS_JXDM_51, "StatuteCodeIdentification");
		
		Element caseChargeStatCodeIdValElement = XmlUtils.appendElement(caseChargeStatuteCodeIdElement, NS_NC_30, "IdentificationID");
		caseChargeStatCodeIdValElement.setTextContent(RandomStringUtils.randomNumeric(8));
		
		Element caseChargeStatIdCatDescTxtElement = XmlUtils.appendElement(caseChargeStatuteCodeIdElement, NS_NC_30, "IdentificationCategoryDescriptionText");
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