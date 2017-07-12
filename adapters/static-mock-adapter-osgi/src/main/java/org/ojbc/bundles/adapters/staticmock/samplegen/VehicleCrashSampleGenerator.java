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
import java.util.Arrays;
import java.util.List;

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

public class VehicleCrashSampleGenerator extends AbstractSampleGenerator {

	private static final String CURRENT_DATE = DateTime.now().toString("yyyy-MM-dd");

	public VehicleCrashSampleGenerator() throws ParserConfigurationException, IOException {

		super();
	}

	public List<Document> generateVehicleCrashDetailSamples(int recordCount) throws Exception {

		List<Document> rVehicleCrashDocList = new ArrayList<Document>();

		for (int i = 0; i < recordCount; i++) {

			PersonElementWrapper iGeneratedPerson = getRandomIdentity(null);

			Document vehicleCrashDetailDoc = buildVehicleCrashDetailDoc(iGeneratedPerson);

			rVehicleCrashDocList.add(vehicleCrashDetailDoc);
		}

		return rVehicleCrashDocList;
	}

	private Document buildVehicleCrashDetailDoc(PersonElementWrapper iGeneratedPerson)
			throws ParserConfigurationException, IOException {

		PersonElementWrapper randomPerson2 = getRandomIdentity(null);

		Document rVehicleCrashDetailDoc = getNewDocument();

		Element rootVehicCrashElement = rVehicleCrashDetailDoc.createElementNS(
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXCH_DOC, "VehicleCrashQueryResults");
		rootVehicCrashElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_VEHICLE_CRASH_QUERY_RESULT_EXCH_DOC);

		rVehicleCrashDetailDoc.appendChild(rootVehicCrashElement);

		Element vehicleCrashReportElement = XmlUtils.appendElement(rootVehicCrashElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "VehicleCrashReport");

		Element docCreateDateElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"DocumentCreationDate");

		Element docCreateDateValElement = XmlUtils.appendElement(docCreateDateElement, OjbcNamespaceContext.NS_NC_30,
				"Date");

		docCreateDateValElement.setTextContent(CURRENT_DATE);

		Element docIdElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"DocumentIdentification");

		Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleDocId = RandomStringUtils.randomNumeric(9);
		docIdValElement.setTextContent(sampleDocId);

		Element docApprovedDateElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "DocumentApprovedDate");

		Element docApprovedDateValElement = XmlUtils.appendElement(docApprovedDateElement,
				OjbcNamespaceContext.NS_NC_30, "Date");
		docApprovedDateValElement.setTextContent(CURRENT_DATE);

		Element crashElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_JXDM_51,
				"Crash");

		XmlUtils.addAttribute(crashElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Crash_01");

		Element activityDateElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_NC_30,
				"ActivityDate");

		Element dateTimeElement = XmlUtils.appendElement(activityDateElement, OjbcNamespaceContext.NS_NC_30,
				"DateTime");
		dateTimeElement.setTextContent(DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss"));

		Element activityAugmentElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"ActivityAugmentation");

		Element narrativeElement = XmlUtils.appendElement(activityAugmentElement, OjbcNamespaceContext.NS_JXDM_51,
				"Narrative");

		Element narrativeCommentTxtElement = XmlUtils.appendElement(narrativeElement, OjbcNamespaceContext.NS_NC_30,
				"CommentText");

		List<String> sampleNarrativeList = Arrays.asList("Pileup", "Rush Hour", "Car hit a truck");

		String sampleNarrative = (String) generateRandomValueFromList(sampleNarrativeList);

		narrativeCommentTxtElement.setTextContent(sampleNarrative);

		Element incidentAugmentElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"IncidentAugmentation");

		Element incidentDamagedItemElement = XmlUtils.appendElement(incidentAugmentElement,
				OjbcNamespaceContext.NS_JXDM_51, "IncidentDamagedItem");

		Element itemDescTxtElement = XmlUtils.appendElement(incidentDamagedItemElement, OjbcNamespaceContext.NS_NC_30,
				"ItemDescriptionText");

		List<String> sampleDamagedItemList = Arrays.asList("Utility Pole #256", "Structure", "Roadway Sign");

		String sampleDamagedItem = (String) generateRandomValueFromList(sampleDamagedItemList);

		itemDescTxtElement.setTextContent(sampleDamagedItem);

		Element itemOwnerElement = XmlUtils.appendElement(incidentDamagedItemElement, OjbcNamespaceContext.NS_NC_30,
				"ItemOwner");

		Element itemOwnerEntOrgElement = XmlUtils.appendElement(itemOwnerElement, OjbcNamespaceContext.NS_NC_30,
				"EntityOrganization");

		XmlUtils.addAttribute(itemOwnerEntOrgElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Property_Owner_01");

		Element entityPersonElement = XmlUtils.appendElement(itemOwnerElement, OjbcNamespaceContext.NS_NC_30,
				"EntityPerson");

		XmlUtils.addAttribute(entityPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Property_Owner_02");

		Element propertyCategoryCodeEl = XmlUtils.appendElement(incidentDamagedItemElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DamagedPropertyOwnerCategoryCodeText");

		List<String> propCatCodeList = Arrays.asList("State", "City or Town", "Utilities", "Private");

		String propCatCodeSample = (String) generateRandomValueFromList(propCatCodeList);

		propertyCategoryCodeEl.setTextContent(propCatCodeSample);

		Element incidentReportingOfficialElement = XmlUtils.appendElement(incidentAugmentElement,
				OjbcNamespaceContext.NS_JXDM_51, "IncidentReportingOfficial");

		Element roleOfPersonElement = XmlUtils.appendElement(incidentReportingOfficialElement,
				OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");

		XmlUtils.addAttribute(roleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Reporting_Official_01");

		Element badgeIdElement = XmlUtils.appendElement(incidentReportingOfficialElement,
				OjbcNamespaceContext.NS_JXDM_51, "EnforcementOfficialBadgeIdentification");

		Element badgeIdValElement = XmlUtils.appendElement(badgeIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleBadgeId = RandomStringUtils.randomNumeric(8);

		badgeIdValElement.setTextContent(sampleBadgeId);

		Element incidentWitnessElement = XmlUtils.appendElement(incidentAugmentElement, OjbcNamespaceContext.NS_JXDM_51,
				"IncidentWitness");

		Element witnessRolePersonElement = XmlUtils.appendElement(incidentWitnessElement, OjbcNamespaceContext.NS_NC_30,
				"RoleOfPerson");

		XmlUtils.addAttribute(witnessRolePersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Witness_01");

		Element crashInfoSourceElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashInformationSource");

		Element orgORIIdElement = XmlUtils.appendElement(crashInfoSourceElement, OjbcNamespaceContext.NS_JXDM_51,
				"OrganizationORIIdentification");

		Element orgOriIdValElement = XmlUtils.appendElement(orgORIIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleOriId = RandomStringUtils.randomNumeric(8);

		orgOriIdValElement.setTextContent(sampleOriId);

		Element crashWorkZoneElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashWorkZone");

		Element workersPresentElement = XmlUtils.appendElement(crashWorkZoneElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "WorkZoneWorkersPresentCodeText");

		workersPresentElement.setTextContent(randomString("Yes", "No", "Unknown"));

		Element copsAtWorkZoneCodeElement = XmlUtils.appendElement(crashWorkZoneElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "LawEnforcementPresentAtWorkZoneCodeText");

		copsAtWorkZoneCodeElement.setTextContent(randomString("Yes", "No", "Unknown"));

		Element nearWorkZoneElement = XmlUtils.appendElement(crashWorkZoneElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "NearWorkZoneIndicator");

		nearWorkZoneElement.setTextContent(getRandomBooleanString());

		/*
		 * Element workZoneWorkersPresentIndicator =
		 * XmlUtils.appendElement(crashWorkZoneElement,
		 * OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT,
		 * "WorkZoneWorkersPresentIndicator");
		 * 
		 * workZoneWorkersPresentIndicator.setTextContent(getRandomBooleanString
		 * ());
		 */

		Element crashVehicleElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashVehicle");

		Element roleOfItemElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_NC_30,
				"RoleOfItem");

		XmlUtils.addAttribute(roleOfItemElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_01");

		Element trafficDeviceWorksElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"VehicleTrafficControlDeviceOperationalIndicator");

		trafficDeviceWorksElement.setTextContent(getRandomBooleanString());

		Element crashVehicleSpeedElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashVehicleLegalSpeedRateMeasure");

		Element vehicleSpeedValElement = XmlUtils.appendElement(crashVehicleSpeedElement, OjbcNamespaceContext.NS_NC_30,
				"MeasureValueText");

		String sampleSpeed = RandomStringUtils.randomNumeric(3);

		vehicleSpeedValElement.setTextContent(sampleSpeed);

		Element crashDriverElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashDriver");

		Element crashDriverRoleOfPersonElement = XmlUtils.appendElement(crashDriverElement,
				OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");

		XmlUtils.addAttribute(crashDriverRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Driver_01");

		Element driverLicenseElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_JXDM_51,
				"DriverLicense");

		XmlUtils.addAttribute(driverLicenseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_License_01");

		Element firstVilationDescTxtElement = XmlUtils.appendElement(crashDriverElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "FirstViolationDescriptionText");

		List<String> sampleCitationList = Arrays.asList("Speeding", "J Walking", "No Seatbelt", "Loud Music",
				"Ran Stop Sign", "Reckless Driving");

		String sampleCitation = (String) generateRandomValueFromList(sampleCitationList);

		firstVilationDescTxtElement.setTextContent(sampleCitation);

		Element secondViolationElement = XmlUtils.appendElement(crashDriverElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "SecondViolationDescriptionText");

		String sampleCitation2 = (String) generateRandomValueFromList(sampleCitationList);

		secondViolationElement.setTextContent(sampleCitation2);

		Element crashVehicleOccupantElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_JXDM_51, "CrashVehicleOccupant");

		Element driverRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement,
				OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(driverRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_01");

		Element occupantRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement,
				OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(occupantRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Occupant_01");

		Element vehicleOwnerRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement,
				OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(vehicleOwnerRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Vehicle_Owner_01");

		Element carDirectionElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"TravelDirectionBeforeCrashCode");

		carDirectionElement.setTextContent(
				randomString("0", "1", "2", "3", "4", "9"));

		Element damageElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "VehicleDamageExtentCodeText");

		damageElement.setTextContent(randomString("No Damage Observed", "Minor Damage", "Functional Damage",
				"Towed Due To Disabling Damage"));

		Element hazmatElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "HazmatPlacardIndicator");
		hazmatElement.setTextContent(getRandomBooleanString());

		Element speedLimitElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PostedSpeedLimitCodeText");

		speedLimitElement.setTextContent(randomString("Unknown", "65 Mph", "Not Posted 25 Zone", "Not Posted 45 Zone"));

		Element damageOverThreshElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "TotalDamageOverThresholdtIndicator");

		damageOverThreshElement.setTextContent(getRandomBooleanString());

		Element nineSeatsElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "VehicleNineOrMoreSeatstIndicator");

		nineSeatsElement.setTextContent(getRandomBooleanString());

		Element exemptVehicleElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "ExemptVehicleIndicator");

		exemptVehicleElement.setTextContent(getRandomBooleanString());

		Element locationElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Incident_Loc_01");

		Element addressElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Address");
		Element cityElement = XmlUtils.appendElement(addressElement, OjbcNamespaceContext.NS_NC_30, "LocationCityName");

		String sampleCity = iGeneratedPerson.city;

		cityElement.setTextContent(sampleCity);

		Element hwyElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "AddressHighway");
		Element hwyFullTxtElement = XmlUtils.appendElement(hwyElement, OjbcNamespaceContext.NS_NC_30,
				"HighwayFullText");

		hwyFullTxtElement.setTextContent(iGeneratedPerson.streetAddress);

		Element loc2DElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30,
				"Location2DGeospatialCoordinate");

		Element latElement = XmlUtils.appendElement(loc2DElement, OjbcNamespaceContext.NS_NC_30,
				"GeographicCoordinateLatitude");

		Element latValElement = XmlUtils.appendElement(latElement, OjbcNamespaceContext.NS_NC_30,
				"LatitudeDegreeValue");

		// maxInclusive '90.0' for type 'LatitudeDegreeSimpleType'
		String sampleLatitude = RandomStringUtils.randomNumeric(1);

		latValElement.setTextContent(sampleLatitude);

		Element longitudeElement = XmlUtils.appendElement(loc2DElement, OjbcNamespaceContext.NS_NC_30,
				"GeographicCoordinateLongitude");

		Element longValElement = XmlUtils.appendElement(longitudeElement, OjbcNamespaceContext.NS_NC_30,
				"LongitudeDegreeValue");

		String sampleLongitude = RandomStringUtils.randomNumeric(2);

		longValElement.setTextContent(sampleLongitude);

		Element intersectionElement = XmlUtils.appendElement(locationElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "NearestIntersectingStreetFullText");

		String sampleIntersection = randomPerson2.streetAddress;

		intersectionElement.setTextContent(sampleIntersection);

		Element distanceToIntersectionElement = XmlUtils.appendElement(locationElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "DistanceFromNearestIntersectionNumberText");

		String sampleDistance = RandomStringUtils.randomNumeric(2);

		distanceToIntersectionElement.setTextContent(sampleDistance);

		Element directionFromIntersectionElement = XmlUtils.appendElement(locationElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DirectionFromNearestIntersectionCodeText");

		directionFromIntersectionElement
				.setTextContent(randomString("At Intersection", "North", "South", "East", "West"));

		Element intersectDistUnitsElement = XmlUtils.appendElement(locationElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DistanceFromNearestIntersectionNumberUnitCodeText");

		intersectDistUnitsElement.setTextContent(randomString("Feet", "Miles"));

		Element schoolBusElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51,
				"CrashSchoolBusRelatedCode");

		schoolBusElement.setTextContent(randomString("0", "1", "2"));

		Element atSceneDateElement = XmlUtils.appendElement(crashElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "SceneArrivalDateTime");

		Element atSceneDateValElement = XmlUtils.appendElement(atSceneDateElement, OjbcNamespaceContext.NS_NC_30,
				"DateTime");

		atSceneDateValElement.setTextContent(DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss"));

		Element offRoadElement = XmlUtils.appendElement(crashElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "OffRoadIncidentIndicator");

		offRoadElement.setTextContent(getRandomBooleanString());

		Element emergVehicle = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "EmergencyVehicleRespondingToSceneIndicator");

		emergVehicle.setTextContent(getRandomBooleanString());

		Element citationElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_JXDM_51,
				"Citation");
		XmlUtils.addAttribute(citationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Citation_01");

		Element activityIdElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_NC_30,
				"ActivityIdentification");

		Element activityIdValElement = XmlUtils.appendElement(activityIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleCitationId = RandomStringUtils.randomNumeric(8);

		activityIdValElement.setTextContent(sampleCitationId);

		Element citationNumPendingElement = XmlUtils.appendElement(citationElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "CitationNumberPendingIndicator");

		citationNumPendingElement.setTextContent(getRandomBooleanString());

		Element driverPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(driverPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Driver_01");

		PersonElementWrapper reportOfficalRandomPerson = getRandomIdentity(null);

		Element reportOfficialElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(reportOfficialElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Reporting_Official_01");

		Element reportOfficialNameElement = XmlUtils.appendElement(reportOfficialElement, OjbcNamespaceContext.NS_NC_30,
				"PersonName");

		Element reportOfficialFullName = XmlUtils.appendElement(reportOfficialNameElement,
				OjbcNamespaceContext.NS_NC_30, "PersonFullName");

		String sampleReportOfficialFullName = reportOfficalRandomPerson.firstName + " "
				+ reportOfficalRandomPerson.lastName;

		reportOfficialFullName.setTextContent(sampleReportOfficialFullName);

		Element witnessPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(witnessPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Witness_01");

		Element witnessPersonNameElement = XmlUtils.appendElement(witnessPersonElement, OjbcNamespaceContext.NS_NC_30,
				"PersonName");

		Element witnessGivenName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonGivenName");

		witnessGivenName.setTextContent(getFirstNameSample(reportOfficalRandomPerson));

		Element witnessMiddleName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonMiddleName");

		witnessMiddleName.setTextContent(reportOfficalRandomPerson.middleName);

		Element witnessSurName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonSurName");

		String sWitnessSurName = getLastNameSample(iGeneratedPerson);
		witnessSurName.setTextContent(sWitnessSurName);
		
		Element witnessPersonCatCodeElement = XmlUtils.appendElement(witnessPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCodeText");

		witnessPersonCatCodeElement.setTextContent(randomString("Witness"));

		PersonElementWrapper infoApprovPerson = getRandomIdentity(null);

		Element infoApproverPersonElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "Person");

		XmlUtils.addAttribute(infoApproverPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Information_Approver_01");

		Element infoApproverPersonNameElement = XmlUtils.appendElement(infoApproverPersonElement,
				OjbcNamespaceContext.NS_NC_30, "PersonName");

		Element personFullNameElement = XmlUtils.appendElement(infoApproverPersonNameElement,
				OjbcNamespaceContext.NS_NC_30, "PersonFullName");

		String infoApprovFullName = infoApprovPerson.firstName + " " + infoApprovPerson.lastName;

		personFullNameElement.setTextContent(infoApprovFullName);

		PersonElementWrapper sampleDriverPerson = getRandomIdentity(null);

		Element driverDobElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_NC_30,
				"PersonBirthDate");

		Element driverDobValElement = XmlUtils.appendElement(driverDobElement, OjbcNamespaceContext.NS_NC_30, "Date");

		driverDobValElement.setTextContent(sampleDriverPerson.birthdate.toString(DATE_FORMATTER_YYYY_MM_DD));

		Element driverNameElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_NC_30,
				"PersonName");

		Element driverGivenNameElement = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonGivenName");

		String sampleDriverFirstName = getFirstNameSample(sampleDriverPerson);

		driverGivenNameElement.setTextContent(sampleDriverFirstName);

		Element driverMiddleName = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonMiddleName");

		String sampleDriverMiddleName = sampleDriverPerson.middleName;

		driverMiddleName.setTextContent(sampleDriverMiddleName);

		Element driverSurNameElement = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonSurName");

		String sampleDriverLastName = getLastNameSample(sampleDriverPerson);

		driverSurNameElement.setTextContent(sampleDriverLastName);

		Element driverSexCodeElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_JXDM_51,
				"PersonSexCode");

		driverSexCodeElement.setTextContent(randomString("F", "M", "U"));

		Element driverPersonCatCodeElement = XmlUtils.appendElement(driverPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCodeText");

		driverPersonCatCodeElement.setTextContent(randomString("Driver", "Driver/Owner"));

		Element alcoholTestCatCodeElement = XmlUtils.appendElement(driverPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "AlcoholTestCategoryCodeText");

		alcoholTestCatCodeElement.setTextContent(
				randomString("Test Not Given", "Test Refused", "Blood", "Breath", "Urine", "Other Chemical Test (Not Field Sobriety or PBT)"));

		Element drugTestCatCodeElement = XmlUtils.appendElement(driverPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DrugTestCategoryCodeText");

		drugTestCatCodeElement.setTextContent(
				randomString("Test Not Given", "Test Refused", "Blood", "Urine", "Other"));

		Element drugTestResultCodeElement = XmlUtils.appendElement(driverPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DrugTestResultCodeText");

		drugTestResultCodeElement.setTextContent(randomString("Positive", "Negative", "Pending"));

		PersonElementWrapper sampleVehicleOwner = getRandomIdentity(null);

		Element vehicleOwnerElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Person");

		XmlUtils.addAttribute(vehicleOwnerElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Vehicle_Owner_01");

		Element vehicleOwnerDobElement = XmlUtils.appendElement(vehicleOwnerElement, OjbcNamespaceContext.NS_NC_30,
				"PersonBirthDate");

		Element vehicleOwnerDobValElement = XmlUtils.appendElement(vehicleOwnerDobElement,
				OjbcNamespaceContext.NS_NC_30, "Date");

		vehicleOwnerDobValElement.setTextContent(sampleVehicleOwner.birthdate.toString(DATE_FORMATTER_YYYY_MM_DD));

		Element vehicleOwnerNameElement = XmlUtils.appendElement(vehicleOwnerElement, OjbcNamespaceContext.NS_NC_30,
				"PersonName");

		Element vehicleOwnerGivenNameElement = XmlUtils.appendElement(vehicleOwnerNameElement,
				OjbcNamespaceContext.NS_NC_30, "PersonGivenName");

		vehicleOwnerGivenNameElement.setTextContent(getFirstNameSample(sampleVehicleOwner));

		Element vehicleOwnerMiddleName = XmlUtils.appendElement(vehicleOwnerNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonMiddleName");
		vehicleOwnerMiddleName.setTextContent(sampleVehicleOwner.middleName);

		Element vehicleOwnerSurNameElement = XmlUtils.appendElement(vehicleOwnerNameElement,
				OjbcNamespaceContext.NS_NC_30, "PersonSurName");

		String vehicOwnerLastName = getLastNameSample(sampleVehicleOwner);

		vehicleOwnerSurNameElement.setTextContent(vehicOwnerLastName);

		Element vehicleOwnerSexCodeElement = XmlUtils.appendElement(vehicleOwnerElement,
				OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");

		vehicleOwnerSexCodeElement.setTextContent(randomString("F", "M", "U"));

		Element vehicleOwnerCatCodeElement = XmlUtils.appendElement(vehicleOwnerElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCodeText");

		vehicleOwnerCatCodeElement.setTextContent(randomString("Owner", "Driver/Owner"));

		Element propOwner1EntOrgElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "EntityOrganization");

		XmlUtils.addAttribute(propOwner1EntOrgElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Property_Owner_01");

		Element propOwner1EntOrgNameElement = XmlUtils.appendElement(propOwner1EntOrgElement,
				OjbcNamespaceContext.NS_NC_30, "OrganizationName");

		propOwner1EntOrgNameElement.setTextContent(sampleVehicleOwner.company);

		PersonElementWrapper samplePropertyOwner = getRandomIdentity(null);

		Element propOwner2EntPersonElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "EntityPerson");

		XmlUtils.addAttribute(propOwner2EntPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Property_Owner_02");

		Element propOwnerEntPersonName = XmlUtils.appendElement(propOwner2EntPersonElement,
				OjbcNamespaceContext.NS_NC_30, "PersonName");

		Element propOwnerEntFullName = XmlUtils.appendElement(propOwnerEntPersonName, OjbcNamespaceContext.NS_NC_30,
				"PersonFullName");

		String samplePropOwnerFullName = samplePropertyOwner.firstName + "" + samplePropertyOwner.lastName;

		propOwnerEntFullName.setTextContent(samplePropOwnerFullName);

		Element occupant1EntPersonElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "EntityPerson");

		XmlUtils.addAttribute(occupant1EntPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Occupant_01");

		Element occupantDobElement = XmlUtils.appendElement(occupant1EntPersonElement, OjbcNamespaceContext.NS_NC_30,
				"PersonBirthDate");

		Element occupantDobValElement = XmlUtils.appendElement(occupantDobElement, OjbcNamespaceContext.NS_NC_30,
				"Date");

		occupantDobValElement.setTextContent(samplePropertyOwner.birthdate.toString(DATE_FORMATTER_YYYY_MM_DD));

		Element occupantPersonNameElement = XmlUtils.appendElement(occupant1EntPersonElement,
				OjbcNamespaceContext.NS_NC_30, "PersonName");

		Element occupantGivenNameElement = XmlUtils.appendElement(occupantPersonNameElement,
				OjbcNamespaceContext.NS_NC_30, "PersonGivenName");

		occupantGivenNameElement.setTextContent(getFirstNameSample(samplePropertyOwner));

		Element occupantMiddleName = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonMiddleName");

		occupantMiddleName.setTextContent(samplePropertyOwner.middleName);

		Element occupantSurNameEl = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_NC_30,
				"PersonSurName");

		String sOccupantSurName = getLastNameSample(samplePropertyOwner);

		occupantSurNameEl.setTextContent(sOccupantSurName);

		Element occupantSexCodeElement = XmlUtils.appendElement(occupant1EntPersonElement,
				OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");

		occupantSexCodeElement.setTextContent(randomString("F", "M", "U"));

		Element occupantCatCodeElement = XmlUtils.appendElement(occupant1EntPersonElement,
				OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCodeText");

		occupantCatCodeElement.setTextContent(randomString("Driver", "Passenger", "Owner", "Driver/Owner", "Passenger/Owner"));

		PersonElementWrapper sampleDriver = getRandomIdentity(null);

		Element driverLocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Location");

		XmlUtils.addAttribute(driverLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Driver_Loc_01");

		Element driverLocAddressElement = XmlUtils.appendElement(driverLocElement, OjbcNamespaceContext.NS_NC_30,
				"Address");

		Element driverLocStreetElement = XmlUtils.appendElement(driverLocAddressElement, OjbcNamespaceContext.NS_NC_30,
				"LocationStreet");

		Element driverLocStreetValElement = XmlUtils.appendElement(driverLocStreetElement,
				OjbcNamespaceContext.NS_NC_30, "StreetFullText");

		driverLocStreetValElement.setTextContent(sampleDriver.streetAddress);

		Element driverLocCityElement = XmlUtils.appendElement(driverLocAddressElement, OjbcNamespaceContext.NS_NC_30,
				"LocationCityName");
		driverLocCityElement.setTextContent(sampleDriver.city);

		Element driverLocStateElement = XmlUtils.appendElement(driverLocAddressElement, OjbcNamespaceContext.NS_JXDM_51,
				"LocationStateNCICLISCode");

		driverLocStateElement.setTextContent(sampleDriver.state);

		Element driverLocZipElement = XmlUtils.appendElement(driverLocAddressElement, OjbcNamespaceContext.NS_NC_30,
				"LocationPostalCode");

		driverLocZipElement.setTextContent(sampleDriver.zipCode);

		PersonElementWrapper sampleWitness = getRandomIdentity(null);

		Element witnessLocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Location");

		XmlUtils.addAttribute(witnessLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Witness_Loc_01");

		Element witnessLocAddressElement = XmlUtils.appendElement(witnessLocElement, OjbcNamespaceContext.NS_NC_30,
				"Address");

		Element witnessLocStreetElement = XmlUtils.appendElement(witnessLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationStreet");

		Element witnessLocStreetValElement = XmlUtils.appendElement(witnessLocStreetElement,
				OjbcNamespaceContext.NS_NC_30, "StreetFullText");

		witnessLocStreetValElement.setTextContent(sampleWitness.streetAddress);

		Element witnessLocCityElement = XmlUtils.appendElement(witnessLocAddressElement, OjbcNamespaceContext.NS_NC_30,
				"LocationCityName");

		witnessLocCityElement.setTextContent(sampleWitness.city);

		Element witnessLocStateElement = XmlUtils.appendElement(witnessLocAddressElement,
				OjbcNamespaceContext.NS_JXDM_51, "LocationStateNCICLISCode");

		witnessLocStateElement.setTextContent(sampleWitness.state);

		Element witnessLocZipElement = XmlUtils.appendElement(witnessLocAddressElement, OjbcNamespaceContext.NS_NC_30,
				"LocationPostalCode");

		witnessLocZipElement.setTextContent(sampleWitness.zipCode);

		PersonElementWrapper propOwnerSample = getRandomIdentity(null);

		Element propOwnerLocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Location");

		XmlUtils.addAttribute(propOwnerLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Property_Owner_Loc_01");

		Element propOwnerLocAddressElement = XmlUtils.appendElement(propOwnerLocElement, OjbcNamespaceContext.NS_NC_30,
				"Address");

		Element propOwnerLocStreetElement = XmlUtils.appendElement(propOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationStreet");

		Element propOwnerLocStreetValElement = XmlUtils.appendElement(propOwnerLocStreetElement,
				OjbcNamespaceContext.NS_NC_30, "StreetFullText");
		propOwnerLocStreetValElement.setTextContent(propOwnerSample.streetAddress);

		Element propOwnerLocCityElement = XmlUtils.appendElement(propOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationCityName");
		propOwnerLocCityElement.setTextContent(propOwnerSample.city);

		Element propOwnerLocStateElement = XmlUtils.appendElement(propOwnerLocAddressElement,
				OjbcNamespaceContext.NS_JXDM_51, "LocationStateNCICLISCode");
		propOwnerLocStateElement.setTextContent(propOwnerSample.state);

		Element propOwnerLocZipElement = XmlUtils.appendElement(propOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationPostalCode");
		propOwnerLocZipElement.setTextContent(propOwnerSample.zipCode);

		PersonElementWrapper vehicOwnerSample = getRandomIdentity(null);

		Element vehicleOwnerLocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "Location");

		XmlUtils.addAttribute(vehicleOwnerLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Vehicle_Owner_Loc_01");

		Element vehicleOwnerLocAddressElement = XmlUtils.appendElement(vehicleOwnerLocElement,
				OjbcNamespaceContext.NS_NC_30, "Address");

		Element vehicleOwnerLocStreetElement = XmlUtils.appendElement(vehicleOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationStreet");

		Element vehicleOwnerLocStreetValElement = XmlUtils.appendElement(vehicleOwnerLocStreetElement,
				OjbcNamespaceContext.NS_NC_30, "StreetFullText");
		vehicleOwnerLocStreetValElement.setTextContent(vehicOwnerSample.addressStreetNumber);

		Element vehicleOwnerLocCityElement = XmlUtils.appendElement(vehicleOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationCityName");
		vehicleOwnerLocCityElement.setTextContent(vehicOwnerSample.city);

		Element vehicleOwnerLocStateElement = XmlUtils.appendElement(vehicleOwnerLocAddressElement,
				OjbcNamespaceContext.NS_JXDM_51, "LocationStateNCICLISCode");
		vehicleOwnerLocStateElement.setTextContent(vehicOwnerSample.state);

		Element vehicleOwnerLocZipElement = XmlUtils.appendElement(vehicleOwnerLocAddressElement,
				OjbcNamespaceContext.NS_NC_30, "LocationPostalCode");
		vehicleOwnerLocZipElement.setTextContent(vehicOwnerSample.zipCode);

		Element vehicleElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Vehicle");

		XmlUtils.addAttribute(vehicleElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Vehicle_01");

		Element vehicleColorElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"ConveyanceColorPrimaryCode");

		vehicleColorElement
				.setTextContent(randomString("AME", "BGE", "BLK", "BLU", "BRO", "BRZ", "CAM", "COM", "COM", "CPR"));

		List<String> sampleYearList = Arrays.asList("1970", "1989", "1996", "2016");

		String sampleYear = (String) generateRandomValueFromList(sampleYearList);

		Element vehcicleYearElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_NC_30,
				"ItemModelYearDate");

		vehcicleYearElement.setTextContent(sampleYear);

		Element vehicleIdElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_NC_30,
				"VehicleIdentification");

		Element vehicleIdValElement = XmlUtils.appendElement(vehicleIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleVin = RandomStringUtils.randomAlphanumeric(10);

		vehicleIdValElement.setTextContent(sampleVin);

		Element vehicleIdJurisdictionElement = XmlUtils.appendElement(vehicleIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationJurisdiction");

		Element vehicleIdStateCodeElement = XmlUtils.appendElement(vehicleIdJurisdictionElement,
				OjbcNamespaceContext.NS_JXDM_51, "LocationStateNCICLISCode");

		vehicleIdStateCodeElement.setTextContent(vehicOwnerSample.state);

		Element vehicleMakeElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"VehicleMakeCode");

		vehicleMakeElement
				.setTextContent(randomString("AAA", "AAB", "AACC", "AACC", "AACO", "AALI", "AAPX", "AARC", "AARD"));

		Element vehicleModelElement = XmlUtils.appendElement(vehicleElement, OjbcNamespaceContext.NS_JXDM_51,
				"VehicleModelCode");

		vehicleModelElement.setTextContent(randomString("100", "110", "120", "328"));

		Element vehicleUnitIdElement = XmlUtils.appendElement(vehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "VehicleUnitIdentification");

		Element vehicleUnitIdValElement = XmlUtils.appendElement(vehicleUnitIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String sampleVehicUnitId = RandomStringUtils.randomAlphanumeric(8);

		vehicleUnitIdValElement.setTextContent(sampleVehicUnitId);

		Element hitRunIndicatorElement = XmlUtils.appendElement(crashVehicleElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "HitRunIndicator");

		hitRunIndicatorElement.setTextContent(getRandomBooleanString());

		Element conveyRegElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_JXDM_51,
				"ConveyanceRegistration");

		XmlUtils.addAttribute(conveyRegElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id",
				"Conveyance_Registration_01");

		Element conveyRegIdElement = XmlUtils.appendElement(conveyRegElement, OjbcNamespaceContext.NS_JXDM_51,
				"ConveyanceRegistrationPlateIdentification");

		Element conveyRegIdValElement = XmlUtils.appendElement(conveyRegIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String samplePlateId = RandomStringUtils.randomAlphanumeric(7);

		conveyRegIdValElement.setTextContent(samplePlateId);

		Element conveyIdRegJurisdictElement = XmlUtils.appendElement(conveyRegIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationJurisdiction");

		Element conveyIdRegStateCodeElement = XmlUtils.appendElement(conveyIdRegJurisdictElement,
				OjbcNamespaceContext.NS_JXDM_51, "LocationStateNCICLISCode");

		conveyIdRegStateCodeElement.setTextContent(sampleVehicleOwner.state);

		Element insuranceElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"Insurance");

		XmlUtils.addAttribute(insuranceElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Insurance_01");

		Element insPolicyIdElement = XmlUtils.appendElement(insuranceElement, OjbcNamespaceContext.NS_NC_30,
				"InsurancePolicyIdentification");

		Element insPolicyIdValElement = XmlUtils.appendElement(insPolicyIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String insPolicyId = RandomStringUtils.randomAlphanumeric(10);

		insPolicyIdValElement.setTextContent(insPolicyId);

		Element insCarrierNameElement = XmlUtils.appendElement(insuranceElement, OjbcNamespaceContext.NS_NC_30,
				"InsuranceCarrierName");

		List<String> insCarrierList = Arrays.asList("Geico", "Allstate", "Progressive");

		String insCarrier = (String) generateRandomValueFromList(insCarrierList);

		insCarrierNameElement.setTextContent(insCarrier);

		Element noInsuranceElement = XmlUtils.appendElement(insuranceElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "NoInsuranceIndicator");

		noInsuranceElement.setTextContent(getRandomBooleanString());

		Element crashDriverLicElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_JXDM_51, "CrashDriverLicense");

		XmlUtils.addAttribute(crashDriverLicElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Driver_License_01");

		Element dlCardIdElement = XmlUtils.appendElement(crashDriverLicElement, OjbcNamespaceContext.NS_JXDM_51,
				"DriverLicenseIdentification");

		Element dlCardIdValueElement = XmlUtils.appendElement(dlCardIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationID");

		String dlId = RandomStringUtils.randomAlphanumeric(9);

		dlCardIdValueElement.setTextContent(dlId);

		Element dlJurisdictElement = XmlUtils.appendElement(dlCardIdElement, OjbcNamespaceContext.NS_NC_30,
				"IdentificationJurisdiction");

		Element dlStateElement = XmlUtils.appendElement(dlJurisdictElement, OjbcNamespaceContext.NS_JXDM_51,
				"LocationStateNCICLISCode");

		dlStateElement.setTextContent(getRandomIdentity(null).state);

		Element dlRestrictElement = XmlUtils.appendElement(crashDriverLicElement, OjbcNamespaceContext.NS_JXDM_51,
				"DriverLicenseRestriction");

		Element dlRestrictTxtElement = XmlUtils.appendElement(dlRestrictElement, OjbcNamespaceContext.NS_JXDM_51,
				"DrivingRestrictionText");

		List<String> dlRestrictList = Arrays.asList("Glasses", "Daylight", "Evening");

		String sampleRestriction = (String) generateRandomValueFromList(dlRestrictList);

		dlRestrictTxtElement.setTextContent(sampleRestriction);

		Element dlEndorsementElement = XmlUtils.appendElement(crashDriverLicElement, OjbcNamespaceContext.NS_JXDM_51,
				"DriverLicenseEndorsement");
		Element dlEndorsementTxtElement = XmlUtils.appendElement(dlEndorsementElement, OjbcNamespaceContext.NS_JXDM_51,
				"DriverLicenseEndorsementText");

		List<String> endorsementList = Arrays.asList("motorcycle", "trailor", "commercial");

		String sampleEndorsement = (String) generateRandomValueFromList(endorsementList);

		dlEndorsementTxtElement.setTextContent(sampleEndorsement);

		Element dlClassCodeElement = XmlUtils.appendElement(crashDriverLicElement,
				OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "DriverLicenseClassCodeText");

		String dlClass = RandomStringUtils.randomAlphabetic(1);

		dlClassCodeElement.setTextContent(dlClass);

		Element activityInfoApproverAssocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_JXDM_51, "ActivityInformationApproverAssociation");

		Element aprovAsocActivElement = XmlUtils.appendElement(activityInfoApproverAssocElement,
				OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(aprovAsocActivElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Crash_01");

		Element activInfoAprovAsocPersonElement = XmlUtils.appendElement(activityInfoApproverAssocElement,
				OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(activInfoAprovAsocPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Information_Approver_01");

		Element bloodAlcAssocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_JXDM_51, "PersonBloodAlcoholContentAssociation");

		Element bloodTxtElement = XmlUtils.appendElement(bloodAlcAssocElement, OjbcNamespaceContext.NS_JXDM_51,
				"PersonBloodAlcoholContentNumberText");

		String sampleBloodContent = RandomStringUtils.randomNumeric(2);

		bloodTxtElement.setTextContent(sampleBloodContent);

		Element bloodActivityElement = XmlUtils.appendElement(bloodAlcAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Activity");

		XmlUtils.addAttribute(bloodActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Crash_01");

		Element bloodPersonElement = XmlUtils.appendElement(bloodAlcAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(bloodPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_01");

		Element itemInsAssocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"ItemInsuranceAssociation");

		Element itemAssocInsElement = XmlUtils.appendElement(itemInsAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Insurance");
		XmlUtils.addAttribute(itemAssocInsElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Insurance_01");

		Element insuranceItemElement = XmlUtils.appendElement(itemInsAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Item");
		XmlUtils.addAttribute(insuranceItemElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_01");

		Element personResAsocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"PersonResidenceAssociation");

		Element prsnResAssocPrsnElement = XmlUtils.appendElement(personResAsocElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(prsnResAssocPrsnElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_01");

		Element prsnRestAsocLocElement = XmlUtils.appendElement(personResAsocElement, OjbcNamespaceContext.NS_NC_30,
				"Location");
		XmlUtils.addAttribute(prsnRestAsocLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_Loc_01");

		Element witnessAssocElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30,
				"PersonResidenceAssociation");

		Element witnessPersonResElement = XmlUtils.appendElement(witnessAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(witnessPersonResElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Witness_01");

		Element witnessPrsnResLocElement = XmlUtils.appendElement(witnessAssocElement, OjbcNamespaceContext.NS_NC_30,
				"nc:Location");
		XmlUtils.addAttribute(witnessPrsnResLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Witness_Loc_01");

		Element propOwnerResAsocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");

		Element propOwnerPersonElement = XmlUtils.appendElement(propOwnerResAsocElement, OjbcNamespaceContext.NS_NC_30,
				"Person");
		XmlUtils.addAttribute(propOwnerPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Property_Owner_01");

		Element propOwnerResLocElement = XmlUtils.appendElement(propOwnerResAsocElement, OjbcNamespaceContext.NS_NC_30,
				"Location");
		XmlUtils.addAttribute(propOwnerResLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Property_Owner_Loc_01");

		Element vehicOwnerResAsocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");

		Element vehicOwnerPersonElement = XmlUtils.appendElement(vehicOwnerResAsocElement,
				OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(vehicOwnerPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Vehicle_Owner_01");

		Element vehicOwnerLocElement = XmlUtils.appendElement(vehicOwnerResAsocElement, OjbcNamespaceContext.NS_NC_30,
				"Location");
		XmlUtils.addAttribute(vehicOwnerLocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Vehicle_Owner_Loc_01");

		Element activityDocAssocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_NC_30, "ActivityDocumentAssociation");

		Element actDocAssocActivityElement = XmlUtils.appendElement(activityDocAssocElement,
				OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(actDocAssocActivityElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Crash_01");

		Element activDocAssocDocElement = XmlUtils.appendElement(activityDocAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Document");
		XmlUtils.addAttribute(activDocAssocDocElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Citation_01");

		Element conveyRegAssocElement = XmlUtils.appendElement(vehicleCrashReportElement,
				OjbcNamespaceContext.NS_JXDM_51, "ConveyanceRegistrationAssociation");

		Element conveyItemRegElement = XmlUtils.appendElement(conveyRegAssocElement, OjbcNamespaceContext.NS_JXDM_51,
				"ItemRegistration");
		XmlUtils.addAttribute(conveyItemRegElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref",
				"Conveyance_Registration_01");

		Element conveyRetAsocItemElement = XmlUtils.appendElement(conveyRegAssocElement, OjbcNamespaceContext.NS_NC_30,
				"Item");
		XmlUtils.addAttribute(conveyRetAsocItemElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_01");

		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();

		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootVehicCrashElement);

		return rVehicleCrashDetailDoc;
	}

	private Document getNewDocument() throws ParserConfigurationException {

		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();

		DocumentBuilder docBuilder = dbf.newDocumentBuilder();

		Document doc = docBuilder.newDocument();

		return doc;
	}

}
