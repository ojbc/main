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
package org.ojbc.connectors.warrantmod;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_31;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_51;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL_31;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_51;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SCREENING_3_1;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_WARRANT_MOD_DOC_EXCH;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_WARRANT_MOD_REQ_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SCREENING_3_1;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_WARRANT_MOD_DOC_EXCH;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_WARRANT_MOD_REQ_EXT;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.connectors.warrantmod.dao.WarrantsRepositoryBaseDAO;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.warrant.repository.model.Person;
import org.ojbc.warrant.repository.model.PersonVehicle;
import org.ojbc.warrant.repository.model.Warrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
@Scope("prototype")
public class InitiateWarrantModificationRequestProcessor {

	private static final String CASE_01 = "Case_01";

	private static final Log log = LogFactory.getLog(InitiateWarrantModificationRequestProcessor.class);
	
	@Autowired
	private WarrantsRepositoryBaseDAO warrantsRepositoryBaseDAO;
	
	public Document createWarrantModificationRequest(@Body Map<String, Object> data) throws Exception{
		log.info("Creating warrant modification request for Warrant "  + data.get("WARRANTID"));
		
		Integer warrantId = (Integer) data.get("WARRANTID");

		Document document = createWarrantModificationRequestDocument(warrantId);

		return document;
	}

	private Document createWarrantModificationRequestDocument(Integer warrantId) throws Exception {
		Warrant warrant = warrantsRepositoryBaseDAO.retrieveWarrant(warrantId);
		warrant.setPersons(warrantsRepositoryBaseDAO.retrievePersons(warrantId));
		
		Document document = OJBCXMLUtils.createDocument(); 
        Element rootElement = createWarrantModificationRequestRootElement(document);
        appendCaseElement(warrant, rootElement);
        appendWarrantElement(warrant, rootElement);
        appendPersonElements(warrant.getPersons(), rootElement);
        appendVehicleElements(warrant.getPersons(), rootElement);
        
        appendPersonConveyanceAssociation(warrant.getPersons(), rootElement);
        appendActivityWarrantAssociation(rootElement);
		return document;
	}

	private void appendPersonConveyanceAssociation(List<Person> persons,
			Element rootElement) {
		for (int personIndex = 0, vehicleIndex = 1; personIndex < persons.size(); ){
			List<PersonVehicle> vehicles = persons.get(personIndex).getPersonVehicles();
			
			for (int j = 0; j< vehicles.size(); j++){
				Element personConveyanceAssociation = XmlUtils.appendElement(rootElement, NS_NC_30, "PersonConveyanceAssociation");
				Element person = XmlUtils.appendElement(personConveyanceAssociation, NS_NC_30, "Person");
				String personId = "Person_" + StringUtils.leftPad(String.valueOf(++personIndex), 2, '0');
				XmlUtils.addAttribute(person, NS_STRUCTURES_30, "ref", personId); 
				
				Element conveyance = XmlUtils.appendElement(personConveyanceAssociation, NS_NC_30, "Conveyance");
				String vehicleId = "Vehicle_" + StringUtils.leftPad(String.valueOf(vehicleIndex++), 2, '0');
				XmlUtils.addAttribute(conveyance, NS_STRUCTURES_30, "ref", vehicleId);
			}
		}
	}

	private void appendVehicleElements(List<Person> persons, Element rootElement) throws Exception {
		
		int vehicleIndex = 1; 
		for (int personIndex = 0; personIndex < persons.size(); personIndex++ ){
			List<PersonVehicle> vehicles = persons.get(personIndex).getPersonVehicles();
			
			for (int i=0; i < vehicles.size(); i++){
				
				PersonVehicle vehicle = vehicles.get(i);
				Element vehicleElement = XmlUtils.appendElement(rootElement, NS_NC_30, "Vehicle");
				String vehicleId = "Vehicle_" + StringUtils.leftPad(String.valueOf(vehicleIndex++), 2, '0');
				XmlUtils.addAttribute(vehicleElement, NS_STRUCTURES_30, "id", vehicleId);
				
				appendTextElement(vehicleElement, NS_NC_30, "ConveyanceColorPrimaryText", vehicle.getVehiclePrimaryColor());
				appendTextElement(vehicleElement, NS_NC_30, "ConveyanceColorSecondaryText", vehicle.getVehicleSecondaryColor());
				appendTextElement(vehicleElement, NS_NC_30, "ItemMakeName", vehicle.getVehicleMake());
				appendTextElement(vehicleElement, NS_NC_30, "ItemModelName", vehicle.getVehicleModel());
				appendTextElement(vehicleElement, NS_NC_30, "ItemModelYearDate", vehicle.getVehicleYear());
				appendTextElement(vehicleElement, NS_NC_30, "ItemStyleText", vehicle.getVehicleStyle());
				appendIdentificationWrapper(vehicleElement, NS_NC_30, "VehicleIdentification", vehicle.getVehicleIdentificationNumber());
			}
			
		}
	}

	private void appendPersonElements(List<Person> persons, Element rootElement) {
		for (int i = 0; i < persons.size(); i++ ){
			Person person = persons.get(i);
			
			Element personElement = XmlUtils.appendElement(rootElement, NS_NC_30, "Person");
			XmlUtils.addAttribute(personElement, NS_STRUCTURES_30, "id", 
					StringUtils.leftPad(String.valueOf(i + 1), 2, '0'));
			appendTextElement(personElement, NS_NC_30, "PersonAgeDescriptionText", person.getPersonAge());
			appendPersonBirthLocation(person, personElement);
			appendTextElement(personElement, NS_NC_30, "PersonCitizenshipText", person.getPersonCitizenshipCountry());
			appendTextElement(personElement, NS_NC_30, "PersonEthnicityText", person.getPersonEthnicityDescription());
			appendTextElement(personElement, NS_NC_30, "PersonEyeColorText", person.getPersonEyeColorDescription());
			appendTextElement(personElement, NS_NC_30, "PersonHairColorText", person.getPersonHairColorDescription());
			appendPersonHeightMeasure(person, personElement);
			appendPersonName(person, personElement);
			
			appendPersonPhysicalFeature(person, personElement);
			appendTextElement(personElement, NS_NC_30, "PersonRaceText", person.getPersonRaceDescription());
			appendTextElement(personElement, NS_NC_30, "PersonSexText", person.getPersonSexDescription());
			appendTextElement(personElement, NS_NC_30, "PersonSkinToneText", person.getPersonSkinToneDescription());
			
			appendIdentificationWrapper(personElement, NS_NC_30, "PersonSSNIdentification", person.getSocialSecurityNumberBase());
			appendIdentificationWrapper(personElement, NS_NC_30, "PersonStateIdentification", person.getPersonStateIdentification());
			
			appendTextElement(personElement, NS_NC_30, "PersonUSCitizenIndicator", BooleanUtils.toStringTrueFalse(person.getUsCitizenshipIndicator()));
			appendPersonWeightMeasure(person, personElement);
			appendPersonAugmentation(person, personElement);
			appendTextElement(personElement, NS_WARRANT_MOD_REQ_EXT, "PersonCautionCodeText", person.getPersonCautionDescription());
			appendIdentificationWrapper(personElement, NS_WARRANT_MOD_REQ_EXT, "PersonMiscellaneousRecordIdentification", person.getMiscellaneousIDBase());
			appendTextElement(personElement, NS_WARRANT_MOD_REQ_EXT, "PersonImmigrationAlienQueryIndicator", BooleanUtils.toStringTrueFalse(person.getPersonImmigrationAlienQueryIndicator()));
		}
		
	}

	private void appendPersonAugmentation(Person person, Element personElement) {
		Element personAugmentation = XmlUtils.appendElement(personElement, NS_JXDM_51, "PersonAugmentation");
		
		if (StringUtils.isNotBlank(person.getOperatorLicenseNumberBase()) ||
				StringUtils.isNotBlank(person.getOperatorLicenseStateBase())){
			Element driverLicense = XmlUtils.appendElement(personAugmentation, NS_JXDM_51, "DriverLicense");
			
			Element driverLicenseCardIdentification = 
					XmlUtils.appendElement(driverLicense, NS_JXDM_51, "DriverLicenseCardIdentification");
			appendIdentificationIdElement(driverLicenseCardIdentification, person.getOperatorLicenseNumberBase());
			
			if (StringUtils.isNotBlank(person.getOperatorLicenseStateBase())){
				Element identificationJurisdiction = 
						XmlUtils.appendElement(driverLicenseCardIdentification, NS_NC_30, "IdentificationJurisdiction");
				Element jurisdictionNCICLISCode = 
						XmlUtils.appendElement(identificationJurisdiction, NS_JXDM_51, "JurisdictionNCICLISCode");
				jurisdictionNCICLISCode.setTextContent(person.getOperatorLicenseStateBase());
			}
		}
		appendIdentificationWrapper(personAugmentation, NS_JXDM_51, "PersonFBIIdentification", person.getFbiIdentificationNumber());
	}

	private void appendPersonWeightMeasure(Person person, Element personElement) {
		Element personWeightMeasure = 
				XmlUtils.appendElement(personElement, NS_NC_30, "PersonWeightMeasure");
		appendTextElement(personWeightMeasure, NS_NC_30, "MeasureValueText", person.getPersonWeight());
		//TODO find out what to do with nc:MeasureUnitText
	}

	private void appendIdentificationWrapper(Element parentElement, String namespace, 
			String elementName, String idValue) {
		if (StringUtils.isNotBlank(idValue)){
			Element wrapperElement = 
					XmlUtils.appendElement(parentElement, namespace, elementName);
			appendIdentificationIdElement(wrapperElement, idValue);
		}
	}

	private void appendPersonPhysicalFeature(Person person, Element personElement) {
		if (StringUtils.isNotBlank(person.getPersonScarsMarksTattosBase())){
			Element personPhysicalFeature = 
					XmlUtils.appendElement(personElement, NS_NC_30, "PersonPhysicalFeature");
			appendTextElement(personPhysicalFeature, NS_NC_30, "PhysicalFeatureDescriptionText", person.getPersonScarsMarksTattosBase());
		}
	}

	private void appendPersonHeightMeasure(Person person, Element personElement) {
		Element personHeightMeasure = 
				XmlUtils.appendElement(personElement, NS_NC_30, "PersonHeightMeasure");
		appendTextElement(personHeightMeasure, NS_NC_30, "MeasureValueText", person.getPersonHeight());
		//TODO find out what to do with nc:MeasureUnitText
	}

	private void appendPersonName(Person person, Element personElement) {
		Element personName = XmlUtils.appendElement(personElement, NS_NC_30, "PersonName");
		appendTextElement(personName, NS_NC_30, "PersonGivenName", person.getFirstName());
		appendTextElement(personName, NS_NC_30, "PersonMiddleName", person.getMiddleName());
		appendTextElement(personName, NS_NC_30, "PersonSurName", person.getLastName());
		appendTextElement(personName, NS_NC_30, "PersonNameSuffixText", person.getNameSuffix());
		appendTextElement(personName, NS_NC_30, "PersonFullName", person.getFullPersonName());
	}

	private void appendTextElement(Element parentElement, String namespace,
			String elementName, String textValue) {
		if (StringUtils.isNotBlank(textValue)){
			Element personEthnicityText = 
					XmlUtils.appendElement(parentElement, namespace, elementName);
			personEthnicityText.setTextContent(textValue);
		}
	}

	private void appendPersonBirthLocation(Person person, Element personElement) {
		if (StringUtils.isNoneBlank(person.getPlaceOfBirth())){
			Element personBirthLocation = 
					XmlUtils.appendElement(personElement, NS_NC_30, "PersonBirthLocation");
			Element locationCategoryText = 
					XmlUtils.appendElement(personBirthLocation, NS_NC_30, "LocationCategoryText");
			locationCategoryText.setTextContent(person.getPlaceOfBirth());
		}
	}

	private Element appendActivityWarrantAssociation(Element rootElement) {
		Element activityWarrantAssociation = 
        		XmlUtils.appendElement(rootElement, NS_JXDM_51, "ActivityWarrantAssociation");
        Element activity = 
        		XmlUtils.appendElement(activityWarrantAssociation, NS_NC_30, "Activity");
        XmlUtils.addAttribute(activity, NS_STRUCTURES_30, "ref", CASE_01);
        Element warrant = 
        		XmlUtils.appendElement(activityWarrantAssociation, NS_JXDM_51, "Warrant");
        XmlUtils.addAttribute(warrant, NS_STRUCTURES_30, "ref", "Warrant_01");
        return activityWarrantAssociation;
	}

	private void appendCaseElement(Warrant warrant, Element rootElement) {
		Element caseElement = XmlUtils.appendElement(rootElement, NS_NC_30, "Case");
        XmlUtils.addAttribute(caseElement, NS_STRUCTURES_30, "id", CASE_01);
        //TODO add <nc:ActivityIdentification> element here
        
        Element caseDocketID = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseDocketID");
        caseDocketID.setTextContent(warrant.getCourtDocketNumber());
        
        Element criminalTrackingNumber = 
        		XmlUtils.appendElement(caseElement, NS_WARRANT_MOD_REQ_EXT, "CriminalTrackingNumber"); 
        criminalTrackingNumber.setTextContent(warrant.getCriminalTrackingNumber());
        
        Element caseAugmentation = XmlUtils.appendElement(caseElement, NS_JXDM_51, "CaseAugmentation");
        Element caseCharge = XmlUtils.appendElement(caseAugmentation, NS_JXDM_51, "CaseCharge");
        Element generalOffenseCharacterDescriptionText = 
        		XmlUtils.appendElement(caseCharge, NS_WARRANT_MOD_REQ_EXT, "GeneralOffenseCharacterDescriptionText");
        generalOffenseCharacterDescriptionText.setTextContent(warrant.getGeneralOffenseCharacter());
        Element chargeCodeText = 
        		XmlUtils.appendElement(caseCharge, NS_WARRANT_MOD_REQ_EXT, "ChargeCodeText");
        chargeCodeText.setTextContent(warrant.getOffenseCode());
        Element originalOffenseCodeText = 
        		XmlUtils.appendElement(caseCharge, NS_WARRANT_MOD_REQ_EXT, "OriginalOffenseCodeText");
        originalOffenseCodeText.setTextContent(warrant.getOriginalOffenseCode());
        Element prosecutionChargeCodeText = 
        		XmlUtils.appendElement(caseCharge, NS_WARRANT_MOD_REQ_EXT, "ProsecutionChargeCodeText");
        prosecutionChargeCodeText.setTextContent(warrant.getPaccCode());
	}

	private void appendWarrantElement(Warrant warrant, Element rootElement) {
		Element warrantElement = XmlUtils.appendElement(rootElement, NS_JXDM_51, "Warrant");
        XmlUtils.addAttribute(warrantElement, NS_STRUCTURES_30, "id", "Warrant_01");
        
        appendCourtOrderDesignatedSubjects(warrant.getPersons(), warrantElement);
        
        appendCourtOrderEnforcementAgency(warrant, warrantElement);
        appendCourtOrderIssuingCourt(warrant, warrantElement);
        appendCourtIssuingDate(warrant, warrantElement);
        appendCourtOrderRequestEntity(warrant, warrantElement);
        appendWarrantAppearanceBail(warrant, warrantElement);
        
        Element warrantAugmentation = 
        		XmlUtils.appendElement(warrantElement, NS_WARRANT_MOD_REQ_EXT, "WarrantAugmentation");
        Element stateWarrantRepositoryIdentification = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "StateWarrantRepositoryIdentification");
        appendIdentificationIdElement(stateWarrantRepositoryIdentification, warrant.getStateWarrantRepositoryID());
        
        if (warrant.getDateOfExpiration() != null){
	        Element expirationDate = 
	        		XmlUtils.appendElement(warrantAugmentation, NS_NC_30, "ExpirationDate");
	        appendNcDate(expirationDate, warrant.getDateOfExpiration());
        }
        
        Element extraditionIndicator = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "ExtraditionIndicator");
        extraditionIndicator.setTextContent(Boolean.toString(warrant.isExtradite()));
        
        Element extradictionLimitCodeText = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "ExtradictionLimitCodeText");
        extradictionLimitCodeText.setTextContent(warrant.getExtraditionLimits());
        
        Element subjectPickupRadiusCodeText = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "SubjectPickupRadiusCodeText");
        subjectPickupRadiusCodeText.setTextContent(warrant.getPickupLimits());
        
        Element warrantBroadcastCodeText = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "WarrantBroadcastCodeText");
        warrantBroadcastCodeText.setTextContent(warrant.getBroadcastArea());
        
        appendTransactionControlNumbers(warrant, warrantAugmentation);
        
        for (String warrantRemark: warrant.getWarrantRemarkStrings()){
        	Element warrantCommentText = 
        			XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "WarrantCommentText");
        	warrantCommentText.setTextContent(warrantRemark);
        }
        
        Element warrantEntryCategoryCodeText = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "WarrantEntryCategoryCodeText");
        warrantEntryCategoryCodeText.setTextContent(warrant.getWarrantEntryType());
        
	}

	private void appendCourtOrderDesignatedSubjects(List<Person> persons, Element warrantElement) {
		
		for (int i=0; i<persons.size(); i++){
        	Element courtCorderDesignatedSubject = 
        			XmlUtils.appendElement(warrantElement, NS_JXDM_51, "CourtOrderDesignatedSubject");
        	Element roleOfPerson = 
        			XmlUtils.appendElement(courtCorderDesignatedSubject, NS_NC_30, "RoleOfPerson");
        	XmlUtils.addAttribute(roleOfPerson, NS_STRUCTURES_30, "ref", 
        			"Person_" + StringUtils.leftPad(String.valueOf(i + 1), 2, '0'));
        	Element subjectCorrectionsIdentification = 
        			XmlUtils.appendElement(courtCorderDesignatedSubject, NS_JXDM_51, "SubjectCorrectionsIdentification");
        	
        	appendIdentificationIdElement(subjectCorrectionsIdentification, persons.get(i).getPrisonRecordNumber());
        }
	}

	private void appendTransactionControlNumbers(Warrant warrant, Element warrantAugmentation) {
		List<String> transactionControlNumbers = 
        		warrantsRepositoryBaseDAO.getTransactionControlNumbers(warrant.getWarrantID());
        for (String transactionControlNumber: transactionControlNumbers){
        	Element transactionControlNumberIdentification = 
        		XmlUtils.appendElement(warrantAugmentation, NS_SCREENING_3_1, "TransactionControlNumberIdentification");
        	appendIdentificationIdElement(transactionControlNumberIdentification, transactionControlNumber);
        }
	}

	private void appendWarrantAppearanceBail(Warrant warrant, Element warrantElement) {
		if (StringUtils.isNotBlank(warrant.getBondAmount())){
        	Element warrantAppearanceBail = 
        			XmlUtils.appendElement(warrantElement, NS_JXDM_51, "WarrantAppearanceBail");
        	Element bailSetAmount = 
        			XmlUtils.appendElement(warrantAppearanceBail, NS_JXDM_51, "BailSetAmount");
        	Element amount = XmlUtils.appendElement(bailSetAmount, NS_NC_30, "Amount");
        	amount.setTextContent(warrant.getBondAmount());
        }
	}

	private void appendCourtOrderRequestEntity(Warrant warrant, Element warrantElement) {
		if (StringUtils.isNoneBlank(warrant.getOperator())){
        	Element courtOrderRequestEntity = 
        			XmlUtils.appendElement(warrantElement, NS_JXDM_51, "CourtOrderRequestEntity");
        	Element entityPerson = 
        			XmlUtils.appendElement(courtOrderRequestEntity, NS_NC_30, "EntityPerson");
        	Element personEmployeeIdentification = 
        			XmlUtils.appendElement(entityPerson, NS_WARRANT_MOD_REQ_EXT, "PersonEmployeeIdentification");
        	appendIdentificationIdElement(personEmployeeIdentification, warrant.getOperator());
        }
	}

	private void appendCourtIssuingDate(Warrant warrant, Element warrantElement) {
		if (warrant.getDateOfWarrantRequest() != null){
        	Element courtOrderIssuingDate =
        			XmlUtils.appendElement(warrantElement, NS_JXDM_51, "CourtOrderIssuingDate");
        	appendNcDate(courtOrderIssuingDate, warrant.getDateOfWarrantRequest());
        }
	}

	private void appendNcDate(Element parentElement, LocalDate localDate) {
		Element dateElement = 
				XmlUtils.appendElement(parentElement, NS_NC_30, "Date");
		dateElement.setTextContent( localDate.toString());
	}

	private void appendCourtOrderIssuingCourt(Warrant warrant, Element warrantElement) {
		if (StringUtils.isNotBlank(warrant.getCourtAgencyORI())){
			Element courtOrderIssuingCourt = 
	        		XmlUtils.appendElement(warrantElement, NS_JXDM_51, "CourtOrderIssuingCourt");
	        Element organizationAugmentation = 
	        		XmlUtils.appendElement(courtOrderIssuingCourt, NS_JXDM_51, "OrganizationAugmentation");
	        Element organizationORIIdentification = 
	        		XmlUtils.appendElement(organizationAugmentation, NS_JXDM_51, "OrganizationORIIdentification");
	        appendIdentificationIdElement(organizationORIIdentification, warrant.getCourtAgencyORI());
		}
	}

	private void appendCourtOrderEnforcementAgency(Warrant warrant, Element warrantElement) {
		Element courtOrderEnforcementAgency = 
        		XmlUtils.appendElement(warrantElement, NS_JXDM_51, "CourtOrderEnforcementAgency");
        Element organizationAugmentation = 
        		XmlUtils.appendElement(courtOrderEnforcementAgency, NS_JXDM_51, "OrganizationAugmentation");
        Element organizationORIIdentification = 
        		XmlUtils.appendElement(organizationAugmentation, NS_JXDM_51, "OrganizationORIIdentification");
        appendIdentificationIdElement(organizationORIIdentification, warrant.getLawEnforcementORI());

        Element agencyRecordIdentification = 
        		XmlUtils.appendElement(courtOrderEnforcementAgency, NS_WARRANT_MOD_REQ_EXT, "AgencyRecordIdentification");
        appendIdentificationIdElement(agencyRecordIdentification, warrant.getOcaComplaintNumber());
	}

	private void appendIdentificationIdElement(
			Element parentElement, String textContent) {
		if (StringUtils.isNotBlank(textContent)){
			Element identificationId = 
	        		XmlUtils.appendElement(parentElement, NS_NC_30, "IdentificationID");
	        identificationId.setTextContent(textContent);
		}
	}

	private Element createWarrantModificationRequestRootElement(
			Document document) {
        Element rootElement = document.createElementNS(
        		NS_WARRANT_MOD_DOC_EXCH,
        		NS_PREFIX_WARRANT_MOD_DOC_EXCH +":WarrantModificationRequest");
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_WARRANT_MOD_DOC_EXCH, 
        		NS_WARRANT_MOD_DOC_EXCH);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_WARRANT_MOD_REQ_EXT, 
        		NS_WARRANT_MOD_REQ_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL_31, NS_INTEL_31);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_JXDM_51, NS_JXDM_51);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_NC_30, NS_NC_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_SCREENING_3_1, NS_SCREENING_3_1);
        document.appendChild(rootElement);
		return rootElement;
	}
}
