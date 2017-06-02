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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JuvenileHistorySampleGenerator extends AbstractSampleGenerator {

	private static final String ID_SOURCE_TEXT = "CMS";

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(JuvenileHistorySampleGenerator.class);

	private static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");

	private static final String[] OFFENSE_CODES = new String[] { "13.1017", "324.74203", "324.801261", "324.8015", "324.801771C-A", "29.29", "324.811296", "324.81133U", "324.811462", "324.82126C1", "324.821521", "328.2281E", "333.28359",
			"333.7340A", "333.74012F", "55.3014", "333.74071A", "333.74101-E", "333.7417", "333.12531A", "125.14471B2", "333.17766B", "333.201992", "338.835", "338.10693", "125.2321", "380.18093B", "399.1585", "400.6073", "408.66", "168.4992",
			"421.54D-A", "431.3304", "432.1102", "432.2183", "168.759", "436.19042B", "436.20253", "445.2A", "445.72B3C", "168.931A", "445.574A3C", "445.20812", "451.2501", "462.391", "480.17C3-A", "487.21421", "500.2062", "500.7034", "565.371",
			"600.87279", "722.115F8A", "722.675", "750.164-A", "750.49-B", "750.50C2", "750.731", "168.933", "750.81C3", "750.9", "750.94", "750.110A3", "169.23312", "750.131A2", "750.14", "750.145D2F", "750.153", "169.243", "750.160C2B-",
			"750.1671K", "750.1744C", "750.174A7B", "169.255", "750.1952", "750.200J1A-A", "750.2072D", "750.211A2A", "750.217B", "750.2187A", "750.2232-A", "750.227B-B", "750.232A4", "205.106-A", "750.26", "750.282A1A", "750.300A1C", "750.316-A",
			"750.329", "750.349", "750.356A2A", "750.360A2A", "750.3632B", "750.3682", "750.377C", "750.3821D2", "750.393", "750.397", "750.411A4A", "750.411R1", "750.416", "750.428", "750.439", "750.462B2", "750.462H1B3", "750.469", "750.479A3",
			"750.49", "750.493E", "750.511", "750.520C1C", "750.520D1F", "750.529", "750.536A1", "750.5405B", "750.543K1B", "752.62", "752.3653", "752.7971C2", "752.862-B", "764.25B", "780.79", "801.2622", "257.2551", "257.312A", "257.3291",
			"257.613D ", "28.2951C", "257.6251", "257.6264", "257.722", "257.9047-A", "257.13542", "259.181", "280.602", "286.214", "287.118", "287.335A", "287.9675", "290.631C", "290.673", "324.21571C1", "324.411", "4.83", "324.111512-A",
			"324.11714", "324.172041", "324.315255-A", "28.729", "324.413093B", "324.42505", "324.435241", "324.435581E", "28.7352A", "324.459082", "324.47323", "324.48707", "324.4873", "29.5G", "324.61511" };

	public JuvenileHistorySampleGenerator() throws ParserConfigurationException, IOException {
		super();
	}

	/**
	 * Generate a specified number of sample histories.
	 * 
	 * @param recordCount
	 *            the number of histories to create
	 * @param baseDate
	 *            the base date to use for computing age, etc.
	 * @param stateParam
	 *            the state to restrict instances, or null for any state
	 * @return the list of generated instances
	 * @throws Exception
	 */
	public List<Document> generateSample(int recordCount, DateTime baseDate, String stateParam) throws Exception {

		List<Document> ret = new ArrayList<Document>(recordCount);

		List<PersonElementWrapper> kids = loadIdentities(recordCount, 2000, baseDate, stateParam);

		while (ret.size() < recordCount) {
			ret.addAll(buildJuvenileHistoryInstanceDocuments(kids, baseDate, stateParam));
		}
		int size = ret.size();
		if (size > recordCount) {
			List<Document> trimmed = new ArrayList<Document>(recordCount);
			for (int i = 0; i < recordCount; i++) {
				trimmed.add(ret.get(i));
			}
			ret = trimmed;
		}
		return ret;
	}

	private List<Document> buildJuvenileHistoryInstanceDocuments(List<PersonElementWrapper> kids, DateTime baseDate, String stateParam) throws Exception {
		List<Document> ret = new ArrayList<Document>();
		for (PersonElementWrapper kid : kids) {
			ret.add(createJuvenileHistoryInstanceDocument(kid, baseDate, stateParam));
		}
		return ret;
	}

	Document createJuvenileHistoryInstanceDocument(PersonElementWrapper kid, DateTime baseDate, String stateParam) throws Exception {
		JuvenileHistory history = createJuvenileHistory(kid, baseDate, stateParam);
		Document ret = writeHistoryToDocument(history, baseDate);
		return ret;
	}

	private Document writeHistoryToDocument(JuvenileHistory history, DateTime baseDate) throws Exception {
		// call a method to build a sub-element structure for each component of the history.  write unit tests that validate each of those sub-elements.
		// then assemble them all into a container document, with the search params in a made-up structure at the top.  we can't validate that container
		// document because we won't have a schema for it.
		Document ret = documentBuilder.newDocument();

		Element root = ret.createElementNS(OjbcNamespaceContext.NS_JUVENILE_HISTORY_CONTAINER, "JuvenileHistoryContainer");
		root.setPrefix(OjbcNamespaceContext.NS_PREFIX_JUVENILE_HISTORY_CONTAINER);
		ret.appendChild(root);

		appendAvailabilityMetadata(ret, baseDate, history);
		appendPeople(ret, baseDate, history);
		appendLocations(ret, baseDate, history);
		appendParentChildAssociations(ret, baseDate, history);

		appendReferrals(ret, baseDate, history);
		appendOffenseCharges(ret, baseDate, history);
		appendOffenseLocations(ret, baseDate, history);
		appendCasePlans(ret, baseDate, history);
		appendPlacements(ret, baseDate, history);
		appendIntakes(ret, baseDate, history);
		appendHearings(ret, baseDate, history);

		new OjbcNamespaceContext().populateRootNamespaceDeclarations(root);

		return ret;

	}

	private void appendHearings(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		if (history.hearings.size() > 0) {

			Element root = d.getDocumentElement();
			Element caseElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "CourtCase");
			Element caseAugmentationElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_JXDM_50, "CaseAugmentation");
			Element e = XmlUtils.appendElement(caseAugmentationElement, OjbcNamespaceContext.NS_JXDM_50, "CaseDefendantParty");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");

			for (Hearing hearing : history.hearings) {

				Element hearingElement = XmlUtils.appendElement(caseAugmentationElement, OjbcNamespaceContext.NS_JXDM_50, "CaseHearing");
				e = XmlUtils.appendElement(hearingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(hearing.date));

				Element aug = XmlUtils.appendElement(hearingElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_EXT, "CourtEventAugmentation");
				XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_EXT, "ContemptOfCourtIndicator").setTextContent(String.valueOf(hearing.contemptOfCourtIndicator));
				XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_CODES, "HearingCategoryCode").setTextContent(hearing.hearingCategory);
				XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_CODES, "HearingDispositionCode").setTextContent(hearing.disposition);
				XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_EXT, "ProbationViolationIndicator").setTextContent(String.valueOf(hearing.probationViolationIndicator));
				
				appendRelatedRecordsStructure(hearing, aug);
				
			}
		}

	}

	private void appendIntakes(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		Element root = d.getDocumentElement();

		for (Intake intake : history.intakes) {

			Element intakeElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "JuvenileIntakeAssessment");
			Element e = XmlUtils.appendElement(intakeElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(intake.date));

			e = XmlUtils.appendElement(intakeElement, OjbcNamespaceContext.NS_CYFS, "AssessmentRecommendation");
			e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "AssessmentRecommendedCourseOfAction");
			e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_EXT, "AssessmentRecommendedCourseOfActionAugmentation");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_CODES, "AssessmentRecommendedCourseOfActionCode").setTextContent(intake.recommendedCourseOfAction);

			e = XmlUtils.appendElement(intakeElement, OjbcNamespaceContext.NS_CYFS, "Juvenile");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");

			Element aug = XmlUtils.appendElement(intakeElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_EXT, "JuvenileAssessmentAugmentation");
			XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_CODES, "JuvenileIntakeAssessmentCategoryCode").setTextContent(intake.assessmentCategory);

			appendRelatedRecordsStructure(intake, aug);

		}

	}

	private void appendPlacements(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		Element root = d.getDocumentElement();

		for (Placement placement : history.placements) {

			Element placementElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "JuvenilePlacement");
			Element dateRangeElement = XmlUtils.appendElement(placementElement, OjbcNamespaceContext.NS_NC_30, "ActivityDateRange");
			Element e = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC_30, "StartDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(placement.startDate));
			e = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC_30, "EndDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(placement.endDate));
			XmlUtils.appendElement(placementElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_PLACEMENT_CODES, "PlacementCategoryCode").setTextContent(placement.placementCategory);
			Element aug = XmlUtils.appendElement(placementElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_PLACEMENT_EXT, "JuvenilePlacementAugmentation");
			Element facilityAssociationElement = XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_CYFS, "JuvenilePlacementFacilityAssociation");
			e = XmlUtils.appendElement(facilityAssociationElement, OjbcNamespaceContext.NS_CYFS, "PlacedJuvenile");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");
			Element facilityElement = XmlUtils.appendElement(facilityAssociationElement, OjbcNamespaceContext.NS_CYFS, "PlacementFacility");
			XmlUtils.appendElement(facilityElement, OjbcNamespaceContext.NS_NC_30, "FacilityName").setTextContent(placement.facilityName);
			XmlUtils.appendElement(facilityElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_PLACEMENT_CODES, "FacilitySecurityCode").setTextContent(placement.securityCode);
			e = XmlUtils.appendElement(facilityElement, OjbcNamespaceContext.NS_NC_30, "FacilityLocation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", placement.facilityLocation.id);
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");
			appendRelatedRecordsStructure(placement, aug);

		}

	}

	private void appendCasePlans(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		Element root = d.getDocumentElement();

		Element casePlanElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "CasePlan");
		XmlUtils.appendElement(casePlanElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "AssessmentIndicator").setTextContent(String.valueOf(history.casePlan.assessmentIndicator));
		XmlUtils.appendElement(casePlanElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "CasePlanIndicator").setTextContent(String.valueOf(history.casePlan.casePlanIndicator));
		Element aug = XmlUtils.appendElement(casePlanElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "CasePlanAugmentation");
		appendRelatedRecordsStructure(history.casePlan, aug);
	}

	private void appendOffenseLocations(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();


		for (OffenseCharge offense : history.offenseCharges) {

			Element assn = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_50, "OffenseLocationAssociation");
			Element e = XmlUtils.appendElement(assn, OjbcNamespaceContext.NS_JXDM_50, "Offense");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", offense.id);
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");
			e = XmlUtils.appendElement(assn, OjbcNamespaceContext.NS_NC_30, "Location");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", offense.location.id);
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");

		}

	}

	private void appendOffenseCharges(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		Element root = d.getDocumentElement();

		for (OffenseCharge offense : history.offenseCharges) {

			Element offenseChargeElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_50, "OffenseChargeAssociation");
			Element offenseElement = XmlUtils.appendElement(offenseChargeElement, OjbcNamespaceContext.NS_JXDM_50, "Offense");
			XmlUtils.addAttribute(offenseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", offense.id);

			XmlUtils.appendElement(offenseElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_CODES, "OffensePACCCode").setTextContent(offense.pacCode);
			Element aug = XmlUtils.appendElement(offenseElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_EXT, "OffenseAugmentation");
			appendRelatedRecordsStructure(offense, aug);

			Element chargeElement = XmlUtils.appendElement(offenseChargeElement, OjbcNamespaceContext.NS_JXDM_50, "Charge");
			Element e = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_50, "ChargeDisposition");
			e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "DispositionDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(offense.dispositionDate));
			e = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_50, "ChargeFilingDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(offense.filingDate));
			if (offense.sanctionDate != null) {
				Element sanctionElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_50, "ChargeSanction");
				e = XmlUtils.appendElement(sanctionElement, OjbcNamespaceContext.NS_JXDM_50, "SanctionSetDate");
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(offense.sanctionDate));
				aug = XmlUtils.appendElement(sanctionElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_EXT, "SanctionAugmentation");
				for (String sanctionString : offense.sanctions) {
					XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_CODES, "DispositionSanctionCode").setTextContent(sanctionString);
				}
			}

			e = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_50, "ChargeSubject");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");
			if (offense.verdict != null) {
				Element verdictElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_50, "ChargeVerdict");
				e = XmlUtils.appendElement(verdictElement, OjbcNamespaceContext.NS_JXDM_50, "VerdictDispositionDate");
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(offense.verdictDate));
				aug = XmlUtils.appendElement(verdictElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_EXT, "VerdictAugmentation");
				XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_CODES, "ChargeVerdictCode").setTextContent(offense.verdict);
			}

		}

	}

	private void appendReferrals(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {

		Element root = d.getDocumentElement();

		for (Referral referral : history.referrals) {

			Element referralElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Referral");
			XmlUtils.addAttribute(referralElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", referral.id);
			XmlUtils.addAttribute(referralElement, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element e = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(referral.date));

			Element issuerElement = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ReferralIssuer");
			Element orgElement = XmlUtils.appendElement(issuerElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");
			XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_CODES, "ReferralIssuerCategoryCode").setTextContent(referral.issuerCategory);
			Element orgLocation = XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationLocation");
			e = XmlUtils.appendElement(orgLocation, OjbcNamespaceContext.NS_NC_30, "Address");
			Element streetElement = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
			XmlUtils.appendElement(streetElement, OjbcNamespaceContext.NS_NC_30, "StreetFullText").setTextContent(referral.issuerStreet);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(referral.issuerCity);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStateFIPS5-2AlphaCode").setTextContent(referral.issuerState);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(referral.issuerZip);
			XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName").setTextContent(referral.issuerName);

			e = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ReferralPerson");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");

			Element aug = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_EXT, "ReferralAugmentation");
			XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_CODES, "ReferralCategoryCode").setTextContent(referral.category);
			appendRelatedRecordsStructure(referral, aug);

		}

	}

	public void appendRelatedRecordsStructure(IdentifiableHistoryComponent component, Element parentElement) throws InstantiationException, IllegalAccessException {
		Element idElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(component.id);
		XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(component.getSource());
		if (component.relatedComponents.size() > 0 || component.relatedUnsupportedClass != null) {
			Element relatedRecordsElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecords");
			for (IdentifiableHistoryComponent related : component.relatedComponents) {
				Element relatedRecordElement = XmlUtils.appendElement(relatedRecordsElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecord");
				XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryCategoryCode").setTextContent(related.getCategory());
				idElement = XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
				XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(related.id);
				XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(related.getSource());
			}
			if (component.relatedUnsupportedClass != null) {
				Element relatedRecordElement = XmlUtils.appendElement(relatedRecordsElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecord");
				IdentifiableHistoryComponent fakeRelated = component.relatedUnsupportedClass.newInstance();
				XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryCategoryCode").setTextContent(fakeRelated.getCategory());
				XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecordNotSupportedIndicator").setTextContent("true");
			}
		}
	}

	private void appendParentChildAssociations(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();

		if (history.mother != null) {
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "ParentChildAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Parent");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "mother");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

		if (history.father != null) {
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "ParentChildAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Parent");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "father");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

	}

	private void appendLocations(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();

		for (Location location : history.getLocations()) {
			appendLocation(root, location);
		}

		Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
		XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
		Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", history.kidResidence.id);
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");

		if (history.motherResidence != null) {
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "mother");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", history.motherResidence.id);
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

		if (history.fatherResidence != null) {
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "father");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", history.fatherResidence.id);
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

	}

	protected void appendLocation(Element root, Location location2) {
		Element location = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(location, OjbcNamespaceContext.NS_STRUCTURES_30, "id", location2.id);
		XmlUtils.addAttribute(location, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
		Element address = XmlUtils.appendElement(location, OjbcNamespaceContext.NS_NC_30, "Address");
		Element e = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "StreetFullText").setTextContent(location2.street);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(location2.city);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationStateFIPS5-2AlphaCode").setTextContent(location2.state);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(location2.zip);
	}

	private void appendPeople(Document d, DateTime baseDate, JuvenileHistory history) throws IOException {

		Element root = d.getDocumentElement();

		Element p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "child");
		XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");

		Element e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.kid.firstName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.kid.middleName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.kid.lastName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");

		if (coinFlip(.2)) {
			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			if (coinFlip(.5)) {
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(getRandomIdentity(history.kid.state).firstName);
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.kid.middleName);
			} else {
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.kid.firstName);
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(getRandomIdentity(history.kid.state).middleName);
			}
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.kid.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("alias");
		}

		Element recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonOtherIdentification");
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.personId);
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);

		if (history.mother != null) {

			p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "mother");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");

			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.mother.firstName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.mother.middleName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.mother.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");

			recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonOtherIdentification");
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.mother.personId);
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);

		}

		if (history.father != null) {

			p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "father");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");

			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.father.firstName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.father.middleName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.father.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");

			recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonOtherIdentification");
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.father.personId);
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);

		}

		e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CONTAINER, "AdditionalChildInformation");
		Element birthdate = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		XmlUtils.appendElement(birthdate, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(history.kid.birthdate));
		Element ssn = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");
		XmlUtils.appendElement(ssn, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.nationalID);
		if (coinFlip(.5)) {
			Element sid = appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonStateFingerprintIdentification");
			XmlUtils.appendElement(sid, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(generateRandomID("A", 7));
		}
		Element h = appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonHeightMeasure");
        XmlUtils.appendElement(h, OjbcNamespaceContext.NS_NC_30, "MeasurePointValue").setTextContent(String.valueOf(Math.round(Double.parseDouble(history.kid.centimeters) * .39)));
        XmlUtils.appendElement(h, OjbcNamespaceContext.NS_NC_30, "LengthUnitCode").setTextContent("INH");
        Element w = appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonWeightMeasure");
        XmlUtils.appendElement(w, OjbcNamespaceContext.NS_NC_30, "MeasurePointValue").setTextContent(String.valueOf(Math.round(Double.parseDouble(history.kid.pounds))));
        XmlUtils.appendElement(w, OjbcNamespaceContext.NS_NC_30, "WeightUnitCode").setTextContent("LBR");
        XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSexCode").setTextContent(history.kid.sex.substring(0, 1).toUpperCase());

	}

	private void appendAvailabilityMetadata(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();

		Element md = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationAvailabilityMetadata");
		XmlUtils.addAttribute(md, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "metadata");

		Element e = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
		e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date");
		e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(baseDate));

		Element owner = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationOwnerOrganization");
		e = XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
		e.setTextContent(history.court.branchName);

		e = XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationLocation");
		e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Address");
		Element street = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		XmlUtils.appendElement(street, OjbcNamespaceContext.NS_NC_30, "StreetFullText").setTextContent(history.court.addressStreetFullText);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(history.court.city);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStateFIPS5-2AlphaCode").setTextContent(history.court.state);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(history.court.zip);

		XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationName").setTextContent(history.court.name);

		Element recordId = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.personId);
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);

	}

	@SuppressWarnings("unchecked")
	JuvenileHistory createJuvenileHistory(PersonElementWrapper kid, DateTime baseDate, String stateParam) throws IOException {
		
		// make these people kids
		int birthYearSubtraction = randomGenerator.nextInt(12, 17);
		kid.birthdate = kid.birthdate.withYear(baseDate.getYear() - birthYearSubtraction);
		
		kid.centimeters = String.valueOf(Double.parseDouble(kid.centimeters) * .75);
		kid.pounds = String.valueOf(Double.parseDouble(kid.pounds) * .5);
		
		String state = stateParam == null ? kid.state : stateParam;
		JuvenileHistory ret = new JuvenileHistory();
		ret.kid = kid;
		ret.kid.personId = "P" + ret.kid.id;
		String county = getRandomCounty(state);
		List<Court> courts = new ArrayList<Court>();
		int baseZip = 43100;
		for (int i = 0; i < 9; i++) {
			courts.add(new Court(county + " County Circuit Court", "Juvenile Division", "123 Any Street", getRandomCity(state), state, "" + (baseZip + i)));
		}
		ret.court = (Court) generateRandomValueFromList(courts.toArray());
		ret.court.city = getRandomCity(state);
		if (coinFlip(.7)) {
			PersonElementWrapper mother = getRandomIdentity(state);
			while (!("female".equals(mother.sex))) {
				mother = getRandomIdentity(state);
			}
			mother.personId = "P" + mother.id;
			ret.mother = mother;
			Residence r = new Residence();
			r.street = buildRandomStreet();
			r.city = getRandomCity(state);
			r.state = state;
			r.zip = "12345";
			ret.motherResidence = r;
			if (coinFlip(.5)) {
				kid.lastName = mother.lastName;
			}
		}
		if (ret.motherResidence == null || coinFlip(.7)) {
			PersonElementWrapper father = getRandomIdentity(state);
			while (!("male".equals(father.sex))) {
				father = getRandomIdentity(state);
			}
			father.personId = "P" + father.id;
			ret.father = father;
			Residence r = ret.motherResidence;
			if (r == null || coinFlip(.5)) {
				r = new Residence();
				r.street = buildRandomStreet();
				r.city = getRandomCity(state);
				r.state = state;
				r.zip = "12345";
			}
			ret.fatherResidence = r;
			if (coinFlip(.7)) {
				kid.lastName = father.lastName;
			}
		}
		ret.kidResidence = ret.motherResidence;
		if (ret.kidResidence == null || (ret.fatherResidence != null && coinFlip(.6))) {
			ret.kidResidence = ret.fatherResidence;
		}
		if (ret.mother != null && ret.father != null && ret.motherResidence == ret.fatherResidence) {
			if (coinFlip(.75)) {
				ret.mother.lastName = ret.father.lastName;
				ret.kid.lastName = ret.father.lastName;
			}
		}
		if (ret.fatherResidence != null) {
			ret.fatherResidence.id = "Residence-F";
		}
		if (ret.motherResidence != null) {
			ret.motherResidence.id = "Residence-M";
		}
		ret.kidResidence.id = "Residence-K";
		int referralCount = generatePoissonInt(.8, true);
		for (int i = 0; i < referralCount; i++) {
			Referral referral = new Referral();
			referral.id = "Referral-" + i;
			referral.date = generateNormalRandomDateBefore(baseDate, 150);
			referral.issuerCategory = generateRandomCodeFromList("Police", "Prosecutor", "School", "Court/Probation", "Social Services", "Other");
			referral.category = generateRandomCodeFromList("Complaint", "Citation or Appearance Ticket", "Petition", "Community Referral");
			if ("Police".equals(referral.issuerCategory)) {
				referral.issuerName = ret.kidResidence.city + " Police Department";
			} else if ("Prosecutor".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Prosecutor";
			} else if ("School".equals(referral.issuerCategory)) {
				referral.issuerName = generateRandomCodeFromList(ret.kidResidence.city, "Washington", "Lincoln", "Jefferson") + " High School";
			} else if ("Court/Probation".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Courts";
			} else if ("Social Services".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Social Services";
			} else {
				referral.issuerName = "Other Referral Source";
			}
			referral.issuerStreet = buildRandomStreet();
			referral.issuerCity = getRandomCity(state);
			referral.issuerState = state;
			referral.issuerZip = "12345";
			ret.referrals.add(referral);
		}
		CasePlan casePlan = new CasePlan();
		ret.casePlan = casePlan;
		casePlan.casePlanIndicator = coinFlip(.6);
		casePlan.assessmentIndicator = coinFlip(.8);
		int hearingCount = generatePoissonInt(1.2, true);
		for (int i = 0; i < hearingCount; i++) {
			Hearing h = new Hearing();
			h.id = "Hearing-" + i;
			h.date = generateNormalRandomDateBefore(baseDate, 200);
			h.contemptOfCourtIndicator = coinFlip(.2);
			h.probationViolationIndicator = coinFlip(.4);
			h.hearingCategory = generateRandomCodeFromList("Preliminary hearing", "Preliminary inquiry", "Pretrial Conference", "Adjudication", "Bench Trial", "Jury Trial", "Competency Hearing", "Disposition Hearing", "Dispositional Review",
					"Detention Hearing", "Probation Violation Hearing", "Phase I Waiver Hearing", "Phase II Waiver Hearing", "Designation Arraignment", "Designation Preliminary Examination", "Designation Hearing", "Designation adjudication or trial",
					"Designation Sentencing", "Contempt of Court (on a Motion to Show Cause)", "Dismissal");
			h.disposition = generateRandomCodeFromList("Petition authorized", "Petition not authorized", "Refer to alternative services", "Placed on the Consent Calendar", "Placed on diversion", "Attorney appointed", "Placement determination",
					"Title IV-E language", "Bond may be set if out of home placement is ordered", "Next hearing scheduled", "Determine notice issues", "American Indian tribe or band notified", "Adjournment", "Adjudication hearing set",
					"Bench trial set", "Jury trial set", "Case dismissed", "Other stipulations between the parties noted", "Order for competency evaluation requested", "Plea is accepted", "Plea is rejected", "Plea is taken under advisement",
					"Order fingerprinting", "Order an abstract to Sec. of State", "Order licensing sanctions", "Placement determination", "Title IV-E language (if necessary)", "Bond may be set if out of home placement is ordered",
					"Guilty or not guilty determination by judge or referee", "The juvenile is competent", "The juvenile is not competent to proceed and will remain incompetent",
					"The juvenile is not competent to proceed but may be restored to competency", "The proceedings on the charges shall proceed immediately", "The proceedings on the charges are suspended pending restoration efforts",
					"Warn and dismiss", "Probation", "Refer to Michigan DHS for placement and care", "Order HIV, Sex offender registration and/DNA testing", "Order costs, fees, etc.", "Continue Probation", "Modify previous dispositional orders",
					"Release of jurisdiction", "Plea accepted to probation violation", "Modify previous dispositional orders", "Set a probation violation hearing", "Probation violation dismissed", "Finding the juvenile violated probation",
					"Finding the juvenile did not violate probation", "Determine probable cause", "Phase I waived by parties", "Deny the motion to waive", "Schedule Phase II hearing",
					"The motion to waive jurisdiction is denied and the case shall proceed under the juvenile code", "The motion to waive jurisdiction is granted and the case is transferred to the court having general criminal jurisdiction",
					"Preliminary examination waived", "Probable cause does exist", "Probable cause does not exist", "The request for designation is granted", "The request for designation is not granted", "Schedule sentencing hearing",
					"Sentence as an adult", "Blended sentence", "Juvenile disposition", "The person is guilty of contempt of court", "The person is not guilty of contempt of court", "The motion is denied", "Sentencing factors listed");
			ret.hearings.add(h);
		}
		int intakeCount = generatePoissonInt(1.2, true);
		for (int i = 0; i < intakeCount; i++) {
			Intake intake = new Intake();
			intake.id = "Intake-" + i;
			intake.date = generateNormalRandomDateBefore(baseDate, 150);
			intake.assessmentCategory = generateRandomCodeFromList("Assessment", "Screening", "Interview", "Other");
			intake.recommendedCourseOfAction = generateRandomCodeFromList("Court diversion", "Consent calendar", "Formal calendar", "Transfer");
			ret.intakes.add(intake);
		}
		int offenseCount = generatePoissonInt(1.6, true);
		for (int i = 0; i < offenseCount; i++) {
			OffenseCharge offenseCharge = new OffenseCharge();
			offenseCharge.id = "Offense-" + i;
			offenseCharge.pacCode = generateRandomCodeFromList(OFFENSE_CODES);
			DateTime offenseDate = generateNormalRandomDateBefore(baseDate, 400);
			offenseCharge.offenseDate = offenseDate;
			offenseCharge.filingDate = offenseDate.plusDays(randomGenerator.nextInt(2, 14));
			offenseCharge.dispositionDate = offenseCharge.filingDate.plusDays(randomGenerator.nextInt(30, 180));
			offenseCharge.verdictDate = offenseCharge.dispositionDate.plusDays(randomGenerator.nextInt(0, 30));
			offenseCharge.verdict = generateRandomCodeFromList("Not responsible", "Responsible", "Dismissed");
			if ("Responsible".equals(offenseCharge.verdict)) {
				offenseCharge.sanctionDate = offenseCharge.verdictDate.plusDays(randomGenerator.nextInt(0, 30));
				int sanctionCount = generatePoissonInt(.5, true);
				for (int j = 0; j < sanctionCount; j++) {
					offenseCharge.sanctions.add(generateRandomCodeFromList("Warning", "Probation", "Community service", "Payment of fines, fees, restitution", "Electronic tether", "Drug or other testing or screening",
							"Participation in Drug or other Specialty Court", "Detention", "Boot Camp", "Courtesy supervision", "Residential Placements", "Court/county operated treatment facility", "In-state private operated treatment facility",
							"Out-of-state operated treatment facility", "State or public treatment/residential facility", "A referral or commitment to human services", "County jail", "Sentence as an Adult"));
				}
			}
			offenseCharge.location = (Location) generateRandomValueFromList(ret.getLocations());
			if (coinFlip(.4)) {
				offenseCharge.location = new Location();
				offenseCharge.location.street = buildRandomStreet();
				offenseCharge.location.city = getRandomCity(state);
				offenseCharge.location.state = state;
				offenseCharge.location.zip = "12345";
				offenseCharge.location.id = "OffenseLocation-" + i;
			}
			ret.offenseCharges.add(offenseCharge);
		}
		int placementCount = generatePoissonInt(.8, true);
		for (int i = 0; i < placementCount; i++) {
			Placement placement = new Placement();
			placement.id = "Placement-" + i;
			placement.startDate = generateNormalRandomDateBefore(baseDate, 365);
			placement.endDate = placement.startDate.plusDays(randomGenerator.nextInt(10, 180));
			placement.placementCategory = generateRandomCodeFromList("Juvenile Detention Facility", "Foster Home", "Group Home", "Residential", "Mother", "Father", "Stepfather", "Stepmother", "Relative/Fictive Kin", "Jail");
			if ("Juvenile Detention Facility".equals(placement.placementCategory) || "Jail".equals(placement.placementCategory)) {
				placement.securityCode = "Secure";
			} else {
				placement.securityCode = "Non-secure";
			}
			placement.facilityName = "Facility Name: " + placement.placementCategory;
			if ("Mother".equals(placement.placementCategory) && coinFlip(.8)) {
				placement.facilityName = "Mother's residence";
				placement.facilityLocation = ret.motherResidence;
			}
			if ("Father".equals(placement.placementCategory) && coinFlip(.8)) {
				placement.facilityName = "Father's residence";
				placement.facilityLocation = ret.fatherResidence;
			}
			if (placement.facilityLocation == null) {
				placement.facilityLocation = new Location();
				placement.facilityLocation.street = buildRandomStreet();
				placement.facilityLocation.city = getRandomCity(state);
				placement.facilityLocation.state = state;
				placement.facilityLocation.zip = "12345";
				placement.facilityLocation.id = "FacilityLocation-" + i;
			}
			ret.placements.add(placement);
		}
		for (IdentifiableHistoryComponent component : ret.getIdentifiableComponents()) {
			Class<IdentifiableHistoryComponent> unsupportedRelatedComponentClass = null;
			if (coinFlip(.5)) {
				while (unsupportedRelatedComponentClass == null || unsupportedRelatedComponentClass.isAssignableFrom(component.getClass())) {
					unsupportedRelatedComponentClass = (Class<IdentifiableHistoryComponent>) generateRandomValueFromList(Referral.class, Placement.class, Intake.class, OffenseCharge.class, Hearing.class);
				}
			}
			component.relatedUnsupportedClass = unsupportedRelatedComponentClass;
			List<IdentifiableHistoryComponent> others = new ArrayList<IdentifiableHistoryComponent>();
			for (IdentifiableHistoryComponent innerComponent : ret.getIdentifiableComponents()) {
				if (!(innerComponent.getClass() == component.getClass() || innerComponent.getClass() == unsupportedRelatedComponentClass)) {
					others.add(innerComponent);
				}
			}
			if (!others.isEmpty()) {
				Collections.shuffle(others);
				int relatedCount = randomGenerator.nextInt(0, others.size() - 1);
				if (relatedCount > 0) {
					component.relatedComponents = others.subList(0, relatedCount);
				}
			}
		}
		return ret;
	}

	protected String buildRandomStreet() {
		return randomGenerator.nextInt(100, 5000) + " " + generateRandomCodeFromList("First", "Elm", "Center", "32nd", "Martin", "Main") + " " + generateRandomCodeFromList("St.", "Ave.", "Rd.");
	}

	static final class JuvenileHistory {
		public PersonElementWrapper kid;
		public PersonElementWrapper mother;
		public PersonElementWrapper father;
		public Residence motherResidence;
		public Residence fatherResidence;
		public Residence kidResidence;
		public Court court;
		public List<Referral> referrals = new ArrayList<Referral>();
		public List<Hearing> hearings = new ArrayList<Hearing>();
		public List<Intake> intakes = new ArrayList<Intake>();
		public List<OffenseCharge> offenseCharges = new ArrayList<OffenseCharge>();
		public List<Placement> placements = new ArrayList<Placement>();
		public CasePlan casePlan;

		public List<IdentifiableHistoryComponent> getIdentifiableComponents() {
			List<IdentifiableHistoryComponent> ret = new ArrayList<IdentifiableHistoryComponent>();
			ret.addAll(referrals);
			ret.addAll(hearings);
			ret.addAll(intakes);
			ret.addAll(offenseCharges);
			ret.addAll(placements);
			ret.add(casePlan);
			return Collections.unmodifiableList(ret);
		}

		public List<Location> getLocations() {
			List<Location> locations = new ArrayList<Location>();
			locations.add(kidResidence);
			if (motherResidence != null && !locations.contains(motherResidence)) {
				locations.add(motherResidence);
			}
			if (fatherResidence != null && !locations.contains(fatherResidence)) {
				locations.add(fatherResidence);
			}
			for (OffenseCharge offense : offenseCharges) {
				if (offense.location != null && !locations.contains(offense.location)) {
					locations.add(offense.location);
				}
			}
			for (Placement p : placements) {
				if (p.facilityLocation != null && !locations.contains(p.facilityLocation)) {
					locations.add(p.facilityLocation);
				}
			}
			return locations;
		}

	}

	static abstract class IdentifiableHistoryComponent {
		public String id;
		public List<IdentifiableHistoryComponent> relatedComponents = new ArrayList<IdentifiableHistoryComponent>();

		public abstract String getSource();

		public abstract String getCategory();

		public Class<IdentifiableHistoryComponent> relatedUnsupportedClass;
	}

	static final class CasePlan extends IdentifiableHistoryComponent {
		public boolean casePlanIndicator;
		public boolean assessmentIndicator;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}CasePlanHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenileCasePlanHistory";
		}
	}

	static final class Placement extends IdentifiableHistoryComponent {
		public DateTime startDate;
		public DateTime endDate;
		public String placementCategory;
		public String securityCode;
		public String facilityName;
		public Location facilityLocation;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}PlacementHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenilePlacementHistory";
		}
	}

	static final class OffenseCharge extends IdentifiableHistoryComponent {
		public String pacCode;
		public DateTime offenseDate;
		public DateTime filingDate;
		public DateTime sanctionDate;
		public DateTime verdictDate;
		public DateTime dispositionDate;
		public String verdict;
		public List<String> sanctions = new ArrayList<String>();
		public Location location;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}OffenseHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenileOffenseHistory";
		}
	}

	static final class Intake extends IdentifiableHistoryComponent {
		public DateTime date;
		public String assessmentCategory;
		public String recommendedCourseOfAction;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}IntakeHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenileIntakeHistory";
		}
	}

	static final class Hearing extends IdentifiableHistoryComponent {
		public DateTime date;
		public boolean contemptOfCourtIndicator;
		public boolean probationViolationIndicator;
		public String hearingCategory;
		public String disposition;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}HearingHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenileHearingHistory";
		}
	}

	static final class Referral extends IdentifiableHistoryComponent {
		public DateTime date;
		public String category;
		public String issuerCategory;
		public String issuerName;
		public String issuerStreet;
		public String issuerCity;
		public String issuerState;
		public String issuerZip;

		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}ReferralHistory";
		}

		@Override
		public String getCategory() {
			return "JuvenileReferralHistory";
		}
	}

	static final class Residence extends Location {
	}

	static class Location {
		public String street;
		public String city;
		public String state;
		public String zip;
		public String id;
	}

	static final class Court {

		public Court(String name, String branchName, String addressStreetFullText, String city, String state, String zip) {
			this.name = name;
			this.branchName = branchName;
			this.addressStreetFullText = addressStreetFullText;
			this.city = city;
			this.state = state;
			this.zip = zip;
		}

		public String name;
		public String branchName;
		public String addressStreetFullText;
		public String city;
		public String state;
		public String zip;
	}

}
