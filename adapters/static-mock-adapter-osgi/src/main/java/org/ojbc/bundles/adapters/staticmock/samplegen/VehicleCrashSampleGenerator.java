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

import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractSampleGenerator.PersonElementWrapper;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class VehicleCrashSampleGenerator extends AbstractSampleGenerator{
	

	
	public VehicleCrashSampleGenerator() throws ParserConfigurationException,
			IOException {
		
		super();
	}

	
	public List<Document> generateVehicleCrashDetailSamples(int recordCount) throws Exception {
	
		List<Document> rVehicleCrashDocList = new ArrayList<Document>();
						
		for(int i=0; i < recordCount; i++){
			
			PersonElementWrapper iGeneratedPerson = getRandomIdentity(null);
			
			String recordId = String.valueOf(i);
			
			Document vehicleCrashDetailDoc = buildVehicleCrashDetailDoc(iGeneratedPerson, recordId);
						
			rVehicleCrashDocList.add(vehicleCrashDetailDoc);
		}
		
		return rVehicleCrashDocList;
	}


	private Document buildVehicleCrashDetailDoc(
			PersonElementWrapper iGeneratedPerson, String recordId) throws ParserConfigurationException {

		Document rVehicleCrashDetailDoc = getNewDocument();
				
		Element rootVehicCrashElement = rVehicleCrashDetailDoc.createElementNS(OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXCH_DOC, "VehicleCrashQueryResults");		
		rootVehicCrashElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_VEHICLE_CRASH_QUERY_RESULT_EXCH_DOC);
		
						
		Element vehicleCrashReportElement = XmlUtils.appendElement(rootVehicCrashElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "VehicleCrashReport");
						
		Element docCreateDateElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
		
		Element docCreateDateValElement = XmlUtils.appendElement(docCreateDateElement, OjbcNamespaceContext.NS_NC_30, "Date");
		
		docCreateDateValElement.setTextContent("TODO");
		
		
		Element docIdElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
						
		Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		docIdValElement.setTextContent("TODO");
		
		
		
		Element docApprovedDateElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "DocumentApprovedDate");
		
		Element docApprovedDateValElement = XmlUtils.appendElement(docApprovedDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		docApprovedDateValElement.setTextContent("TODO");
		
		
		Element crashElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_JXDM_51, "Crash");
		
		XmlUtils.addAttribute(crashElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Crash_" + recordId);
		
		
		Element activityDateElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
		
		Element dateTimeElement = XmlUtils.appendElement(activityDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		dateTimeElement.setTextContent("TODO");
		
		
		Element activityAugmentElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityAugmentation");
		
		
		Element narrativeElement = XmlUtils.appendElement(activityAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "Narrative");
		
		Element narrativeCommentTxtElement = XmlUtils.appendElement(narrativeElement, OjbcNamespaceContext.NS_NC_30, "CommentText");
		
		narrativeCommentTxtElement.setTextContent("TODO");
		
		
		Element incidentAugmentElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51, "IncidentAugmentation");
		
		Element incidentDamagedItemElement = XmlUtils.appendElement(incidentAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "IncidentDamagedItem");
		
		Element itemDescTxtElement = XmlUtils.appendElement(incidentDamagedItemElement, OjbcNamespaceContext.NS_NC_30, "ItemDescriptionText");
		
		itemDescTxtElement.setTextContent("TODO");
		
		
		Element itemOwnerElement = XmlUtils.appendElement(incidentDamagedItemElement, OjbcNamespaceContext.NS_NC_30, "ItemOwner");
		
		Element itemOwnerEntOrgElement = XmlUtils.appendElement(itemOwnerElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");
		
		XmlUtils.addAttribute(itemOwnerEntOrgElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Property_Owner_" + "TODO");
		
		
		Element entityPersonElement = XmlUtils.appendElement(itemOwnerEntOrgElement, OjbcNamespaceContext.NS_NC_30, "EntityPerson");
		
		XmlUtils.addAttribute(entityPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Property_Owner_" + "TODO");
		
		Element crashCodesElement = XmlUtils.appendElement(incidentDamagedItemElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PropertyCategoryCode");		
		crashCodesElement.setTextContent("TODO");
		
		Element incidentReportingOfficialElement = XmlUtils.appendElement(incidentAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "IncidentReportingOfficial");
		
		Element roleOfPersonElement = XmlUtils.appendElement(incidentReportingOfficialElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		XmlUtils.addAttribute(roleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Reporting_Official_" + recordId);
		
		
		Element badgeIdElement = XmlUtils.appendElement(incidentReportingOfficialElement, OjbcNamespaceContext.NS_JXDM_51, "EnforcementOfficialBadgeIdentification");
				
		Element badgeIdValElement = XmlUtils.appendElement(badgeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		badgeIdValElement.setTextContent("TODO");
		
		Element incidentWitnessElement = XmlUtils.appendElement(incidentAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "IncidentWitness");
		
		Element witnessRolePersonElement = XmlUtils.appendElement(incidentWitnessElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		
		XmlUtils.addAttribute(witnessRolePersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Witness_" + recordId);
		
		
		Element crashInfoSourceElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51, "CrashInformationSource");
		
		Element orgORIIdElement = XmlUtils.appendElement(crashInfoSourceElement, OjbcNamespaceContext.NS_JXDM_51, "OrganizationORIIdentification");
		
		Element orgOriIdValElement = XmlUtils.appendElement(orgORIIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		orgOriIdValElement.setTextContent("TODO");
		
		
		Element crashWorkZoneElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51, "CrashWorkZone");
		
		Element workersPresentElement = XmlUtils.appendElement(crashWorkZoneElement, OjbcNamespaceContext.NS_JXDM_51, "WorkZoneWorkersPresenceIndicationCode");
		
		workersPresentElement.setTextContent("TODO");
		
		
		
		Element copsAtWorkZoneCodeElement = XmlUtils.appendElement(crashWorkZoneElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "LawEnforcementPresentAtWorkZoneCode");		
		copsAtWorkZoneCodeElement.setTextContent("TODO");
				
		Element nearWorkZoneElement = XmlUtils.appendElement(crashWorkZoneElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "NearWorkZoneIndicator");		
		nearWorkZoneElement.setTextContent("TODO");
				

		Element workZoneWorkersPresentIndicator = XmlUtils.appendElement(crashWorkZoneElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "WorkZoneWorkersPresentIndicator");	
		workZoneWorkersPresentIndicator.setTextContent("TODO");
		
		
		
		
		Element crashVehicleElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_JXDM_51, "CrashVehicle");
		
		Element roleOfItemElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_NC_30, "RoleOfItem");
		
		XmlUtils.addAttribute(roleOfItemElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_" + recordId);
		
		
		Element trafficDeviceWorksElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51, "VehicleTrafficControlDeviceOperationalIndicator");  		
		trafficDeviceWorksElement.setTextContent("TODO");
		
		Element crashVehicleSpeedElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51, "CrashVehicleLegalSpeedRateMeasure");
		
		Element vehicleSpeedValElement = XmlUtils.appendElement(crashVehicleSpeedElement, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");		
		vehicleSpeedValElement.setTextContent("TODO");
		
		Element crashDriverElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51, "CrashDriver");
		
		Element crashDriverRoleOfPersonElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");		
		
		XmlUtils.addAttribute(crashDriverRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_" + recordId);
		
		Element driverLicenseElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
		
		XmlUtils.addAttribute(driverLicenseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_License_" + recordId);
		
		Element driverCatCodeElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DriverCategoryCode");		
		driverCatCodeElement.setTextContent("TODO");
		
		Element alcoholTestCatCodeElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "AlcoholTestCategoryCode");
		alcoholTestCatCodeElement.setTextContent("TODO");
		
		Element drugTestCatCodeElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DrugTestCategoryCode");		
		drugTestCatCodeElement.setTextContent("TODO");
		
		Element drugTestResultCodeElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DrugTestResultCode");
		drugTestResultCodeElement.setTextContent("TODO");
		
		Element alcoholTestResultsPendingElement = XmlUtils.appendElement(crashDriverElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "AlcoholTestResultsPendingtIndicator");
		alcoholTestResultsPendingElement.setTextContent("TODO");
		
		Element crashVehicleOccupantElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_JXDM_51, "CrashVehicleOccupant"); 
		
		Element driverRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(driverRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Driver_" + recordId);
		
		Element occupantRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(occupantRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Occupant_" + recordId);
		
		Element vehicleOwnerRoleOfPersonElement = XmlUtils.appendElement(crashVehicleOccupantElement, OjbcNamespaceContext.NS_NC_30, "RoleOfPerson");
		XmlUtils.addAttribute(vehicleOwnerRoleOfPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Vehicle_Owner_" + recordId);
		
		Element hazmatElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "HazmatPlacardIndicator");
		hazmatElement.setTextContent("TODO");
		
		Element carDirectionElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "VehicleTravelDirectionCode");
		carDirectionElement.setTextContent("TODO");
		
		
		Element carWeightElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "GrossVehicleWeightRatingCode");
		carWeightElement.setTextContent("TODO");
		
		Element damageElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "ExtentOfDamageCode");
		damageElement.setTextContent("TODO");
		
		Element speedLimitElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PostedSpeedLimitCode");
		speedLimitElement.setTextContent("TODO");
		
		Element schoolBusElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "SchoolBusRelatedCode");
		schoolBusElement.setTextContent("TODO");
		
		Element damageOverThreshElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "TotalDamageOverThresholdtIndicator");
		damageOverThreshElement.setTextContent("TODO");
		
		Element nineSeatsElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "VehicleNineOrMoreSeatstIndicator");
		nineSeatsElement.setTextContent("TODO");
		
		Element exemptVehicleElement = XmlUtils.appendElement(crashVehicleElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "ExemptVehicleIndicator");
		exemptVehicleElement.setTextContent("TODO");
		
		
		
		
		Element locationElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Incident_Loc_" + recordId);
				
		Element addressElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Address");		
		Element cityElement = XmlUtils.appendElement(addressElement, OjbcNamespaceContext.NS_NC_30, "LocationCityName");		
		cityElement.setTextContent("TODO");
		
		Element hwyElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "AddressHighway");		
		Element hwyFullTxtElement = XmlUtils.appendElement(hwyElement, OjbcNamespaceContext.NS_NC_30, "HighwayFullText");
		hwyFullTxtElement.setTextContent("TODO");
		
		Element loc2DElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Location2DGeospatialCoordinate");
		
		Element latElement = XmlUtils.appendElement(loc2DElement, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLatitude");
		
		Element latValElement = XmlUtils.appendElement(latElement, OjbcNamespaceContext.NS_NC_30, "LatitudeDegreeValue");		
		latValElement.setTextContent("TODO");
		
		Element longitudeElement = XmlUtils.appendElement(loc2DElement, OjbcNamespaceContext.NS_NC_30, "GeographicCoordinateLongitude");
		
		Element longValElement = XmlUtils.appendElement(longitudeElement, OjbcNamespaceContext.NS_NC_30, "LongitudeDegreeValue");		
		longValElement.setTextContent("TODO");
		
		
		Element intersectionElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "NearestIntersectingStreetFullText");
		intersectionElement.setTextContent("TODO");
		
		Element distanceToIntersectionElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, 
				"DistanceFromNearestIntersectionNumberText");
		distanceToIntersectionElement.setTextContent("TODO");
		
		Element directionFromIntersectionElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DirectionFromNearestIntersectionCode");
		directionFromIntersectionElement.setTextContent("TODO");
		
		Element intersectDistUnitsElement = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "DistanceFromNearestIntersectionNumberUnitCode");
		intersectDistUnitsElement.setTextContent("TODO");
		
		
		Element atSceneDateElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "AtSceneDateTime");
		Element atSceneDateValElement = XmlUtils.appendElement(atSceneDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		atSceneDateValElement.setTextContent("TODO");
				
		Element offRoadElement = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "OffRoadIncidentIndicator");
		offRoadElement.setTextContent("TODO");
		
		Element emergVehicle = XmlUtils.appendElement(crashElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "EmergencyVehicleRespondingToScenetIndicator");
		emergVehicle.setTextContent("TODO");
		
		
		Element citationElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_JXDM_51, "Citation");		
		XmlUtils.addAttribute(citationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Citation_" + recordId);
				
		Element activityIdElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element activityIdValElement = XmlUtils.appendElement(activityIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		activityIdValElement.setTextContent("TODO");
		
		
		Element citationNumPendingElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "CitationNumberPendingIndicator");		
		citationNumPendingElement.setTextContent("TODO");
		
		Element firstVilationDescTxtElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "FirstViolationDescriptionText");
		firstVilationDescTxtElement.setTextContent("TODO");
		
		Element secondViolationElement = XmlUtils.appendElement(citationElement, OjbcNamespaceContext.NS_VEHICLE_CRASH_QUERY_RESULT_EXT, "SecondViolationDescriptionText");
		secondViolationElement.setTextContent("TODO");
		
		Element reportOfficialElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "Person");		
		XmlUtils.addAttribute(reportOfficialElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Reporting_Official_" + recordId);
		
		Element reportOfficialNameElement = XmlUtils.appendElement(reportOfficialElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element reportOfficialFullName = XmlUtils.appendElement(reportOfficialNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");		
		reportOfficialFullName.setTextContent("TODO");
				
		Element witnessPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(witnessPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Witness_" + recordId);
		
		Element witnessPersonNameElement = XmlUtils.appendElement(witnessPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element witnessGivenName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
		witnessGivenName.setTextContent("TODO");
		
		Element witnessMiddleName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
		witnessMiddleName.setTextContent("TODO");
		
		Element witnessSurName = XmlUtils.appendElement(witnessPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
		witnessSurName.setTextContent("TODO");
		
		
		Element infoApproverPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(infoApproverPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Information_Approver_" + recordId);
		
		Element infoApproverPersonNameElement = XmlUtils.appendElement(infoApproverPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element personFullNameElement = XmlUtils.appendElement(infoApproverPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonFullName");
		personFullNameElement.setTextContent("TODO");
		
		Element driverPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(driverPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Driver_" + recordId);
		
		Element driverDobElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");				
		Element driverDobValElement = XmlUtils.appendElement(driverDobElement, OjbcNamespaceContext.NS_NC_30, "Date");				
		driverDobValElement.setTextContent("TODO");
		
		Element driverNameElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element driverGivenNameElement = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		driverGivenNameElement.setTextContent("TODO");
		
		Element driverMiddleName = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
		driverMiddleName.setTextContent("TODO");
		
		Element driverSurNameElement = XmlUtils.appendElement(driverNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
		driverSurNameElement.setTextContent("TODO");
		
		Element driverSexCodeElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
		driverSexCodeElement.setTextContent("TODO");
		
		Element driverPersonCatCodeElement = XmlUtils.appendElement(driverPersonElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCode");
		driverPersonCatCodeElement.setTextContent("TODO");
		
		
		Element propOwner1EntOrgElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");		
		XmlUtils.addAttribute(propOwner1EntOrgElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Property_Owner_TODO" + recordId);
		
		Element propOwner1EntOrgNameElement = XmlUtils.appendElement(propOwner1EntOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");		
		propOwner1EntOrgNameElement.setTextContent("TODO");
		
		Element propOwner2EntPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "EntityPerson");
		XmlUtils.addAttribute(propOwner2EntPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Property_Owner_TODO" + recordId);
		
		Element propOwnerEntPersonName = XmlUtils.appendElement(propOwner2EntPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element propOwnerEntFullName = XmlUtils.appendElement(propOwnerEntPersonName, OjbcNamespaceContext.NS_NC_30, "PersonFullName");		
		propOwnerEntFullName.setTextContent("TODO");
		
		
		Element occupant1EntPersonElement = XmlUtils.appendElement(vehicleCrashReportElement, OjbcNamespaceContext.NS_NC_30, "EntityPerson");
		XmlUtils.addAttribute(occupant1EntPersonElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Occupant_01");
		
		Element occupantDobElement = XmlUtils.appendElement(occupant1EntPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");		
		Element occupantDobValElement = XmlUtils.appendElement(occupantDobElement, OjbcNamespaceContext.NS_NC_30, "Date");
		occupantDobValElement.setTextContent("TODO");
		
		Element occupantPersonNameElement = XmlUtils.appendElement(occupant1EntPersonElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element occupantGivenNameElement = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
		occupantGivenNameElement.setTextContent("TODO");
		
		Element occupantMiddleName = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
		occupantMiddleName.setTextContent("TODO");
		
		Element occupantSurName = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
		occupantSurName.setTextContent("TODO");
		
		Element occupantSexCodeElement = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
		occupantSexCodeElement.setTextContent("TODO");
		
		Element occupantCatCodeElement = XmlUtils.appendElement(occupantPersonNameElement, OjbcNamespaceContext.NS_ME_VEHICLE_CRASH_CODES, "PersonCategoryCode");
		occupantCatCodeElement.setTextContent("TODO");
		
		
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootVehicCrashElement);
				
		return rVehicleCrashDetailDoc;
	}

	
	private Document getNewDocument() throws ParserConfigurationException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();		

		Document doc = docBuilder.newDocument();
		
		return doc;
	}	

}


