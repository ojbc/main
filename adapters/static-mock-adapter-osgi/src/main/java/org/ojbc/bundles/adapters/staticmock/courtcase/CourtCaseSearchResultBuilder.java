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
package org.ojbc.bundles.adapters.staticmock.courtcase;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CourtCaseSearchResultBuilder {

	public static CourtCaseDetail getCourtCaseDetail(Document courtCaseDetailDoc) throws Exception{
	
		CourtCaseDetail courtCaseDetail = new CourtCaseDetail();
		
		String caseGenCatTxt = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Case/nc30:CaseGeneralCategoryText");		
		courtCaseDetail.setCaseGeneralCategoryText(caseGenCatTxt);
				
		String caseTrackingId = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Case/nc30:CaseTrackingID");	
		courtCaseDetail.setCaseTrackingID(caseTrackingId);	
		
		String sDocketIdVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Case/nc30:CaseDocketID");	
		courtCaseDetail.setCaseDocketID(sDocketIdVal);
		
		String sJurisdictionTxtVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//jxdm51:CaseCourt/jxdm51:OrganizationAugmentation/jxdm51:OrganizationJurisdiction/nc30:JurisdictionText");	
		courtCaseDetail.setJurisdictionText(sJurisdictionTxtVal);
		
		String idCatDescTxtVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//jxdm51:StatuteCodeIdentification/nc30:IdentificationCategoryDescriptionText");		
		courtCaseDetail.setStatuteCodeIdCatDescText(idCatDescTxtVal);
		
		String dobVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Identity/nc30:IdentityPersonRepresentation/nc30:PersonBirthDate/nc30:Date");
		courtCaseDetail.setPersonBirthDate(dobVal);
		
		String eyeColorVal =  XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/jxdm51:PersonEyeColorCode");		
		courtCaseDetail.setPersonEyeColor(eyeColorVal);
		
		String hairColorVal =  XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/jxdm51:PersonHairColorCode");
		courtCaseDetail.setPersonHairColor(hairColorVal);
		
		String heightTxt = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/nc30:PersonHeightMeasure/nc30:MeasureValueText");
		courtCaseDetail.setPersonHeight(heightTxt);
		
		String heightUnitTxt = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/nc30:PersonHeightMeasure/nc30:MeasureUnitText");
		courtCaseDetail.setPersonHeightUnits(heightUnitTxt);
		
		String givenNameVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:PersonName/nc30:PersonGivenName");
		courtCaseDetail.setPersonGivenName(givenNameVal);
		
		String middleNameVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:PersonName/nc30:PersonMiddleName");
		courtCaseDetail.setPersonMiddleName(middleNameVal);
		
		String personSurNameVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:PersonName/nc30:PersonSurName");
		courtCaseDetail.setPersonSurName(personSurNameVal);
		
		String raceVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/nc30:PersonRaceText");
		courtCaseDetail.setPersonRaceCode(raceVal);
		
		String personSexVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/nc30:PersonSexText");
		courtCaseDetail.setPersonSexCode(personSexVal);
		
		String weightVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//nc30:Person/nc30:PersonWeightMeasure/nc30:MeasureValueText");			
		courtCaseDetail.setPersonWeight(weightVal);
		
		String drivLicVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//jxdm51:DriverLicense/jxdm51:DriverLicenseIdentification/nc30:IdentificationID");
		courtCaseDetail.setDriverLicenseId(drivLicVal);
		
		String dlSourceTxt = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//jxdm51:DriverLicense/jxdm51:DriverLicenseIdentification/nc30:IdentificationSourceText");		
		courtCaseDetail.setDriverLicenseIdSourceTxt(dlSourceTxt);
		
		String fbiId = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//jxdm51:PersonFBIIdentification/nc30:IdentificationID");
		courtCaseDetail.setPersonFBIId(fbiId);
		
		String sidVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Person/nc30:PersonStateIdentification");
		courtCaseDetail.setPersonSid(sidVal);
		
		String srcSysNameTxtVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//intel:SystemIdentification/nc30:SystemName");
		courtCaseDetail.setSourceSystemNameText(srcSysNameTxtVal);
		
		String sysIdVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//intel:SystemIdentification/nc30:IdentificationID");
		courtCaseDetail.setSystemId(sysIdVal);
		
		String srchResCatTxtVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//ccq-res-ext:QueryResultCategoryText");			
		courtCaseDetail.setSearchResultCategoryText(srchResCatTxtVal);
		
		String orgBranchNameVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, 
				"//ccq-res-ext:InformationOwningOrganization/nc30:OrganizationBranchName");			
		courtCaseDetail.setOrganizationBranchName(orgBranchNameVal);
		
		String sOrgName = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//ccq-res-ext:InformationOwningOrganization/nc30:OrganizationName");
		courtCaseDetail.setOrganizationName(sOrgName);
		
		String lastUpdatedDateVal = XmlUtils.xPathStringSearch(courtCaseDetailDoc, "//nc30:Metadata/nc30:LastUpdatedDate/nc30:Date");
		courtCaseDetail.setLastUpdatedDate(lastUpdatedDateVal);
		
		return courtCaseDetail;
	}
	
	
	public static Element buildCourtCaseSearchResultElement(Document courtCaseSearchResultsDocument, Document courtCaseDetailDoc, String resultId) throws Exception{
		
		CourtCaseDetail courtCaseDetail = getCourtCaseDetail(courtCaseDetailDoc);
		
		Element courtCaseSearchResultElement = courtCaseSearchResultsDocument.createElementNS(OjbcNamespaceContext.NS_COURT_CASE_SEARCH_RESULTS, "CourtCaseSearchResult");	
		
		courtCaseSearchResultElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_COURT_CASE_SEARCH_RESULTS);
		
		XmlUtils.addAttribute(courtCaseSearchResultElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Result_" + resultId);
											
		// ********* CASE ***************************		
		Element caseElement = XmlUtils.appendElement(courtCaseSearchResultElement, OjbcNamespaceContext.NS_NC_30, "Case");		
		XmlUtils.addAttribute(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Case_" + resultId);
		
		
		Element caseGenCatTxtElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseGeneralCategoryText");
						
		caseGenCatTxtElement.setTextContent(courtCaseDetail.getCaseGeneralCategoryText());
		
		Element caseTrackId = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseTrackingID");					
		caseTrackId.setTextContent(courtCaseDetail.getCaseTrackingID());								
		
		Element caseDocketIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseDocketID");		
		caseDocketIdElement.setTextContent(courtCaseDetail.getCaseDocketID());						
		
		Element caseAugment =  XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAugmentation");
		
		Element caseCourt = XmlUtils.appendElement(caseAugment, OjbcNamespaceContext.NS_JXDM_51, "CaseCourt");			
					
		Element orgAugment = XmlUtils.appendElement(caseCourt, OjbcNamespaceContext.NS_JXDM_51, "OrganizationAugmentation");
		
		Element orgJurisdiction = XmlUtils.appendElement(orgAugment, OjbcNamespaceContext.NS_JXDM_51, "OrganizationJurisdiction");
		
		Element jurisdictionTxtElement = XmlUtils.appendElement(orgJurisdiction, OjbcNamespaceContext.NS_NC_30, "JurisdictionText");				
		jurisdictionTxtElement.setTextContent(courtCaseDetail.getJurisdictionText());
				
		Element caseOtherId = XmlUtils.appendElement(caseAugment, OjbcNamespaceContext.NS_JXDM_51, "CaseOtherIdentification");
		
		Element idCatDescTxt = XmlUtils.appendElement(caseOtherId, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText"); 				
		idCatDescTxt.setTextContent(courtCaseDetail.getStatuteCodeIdCatDescText());
		
		// ********** PERSON ************
		
		Element person =  XmlUtils.appendElement(courtCaseSearchResultElement, OjbcNamespaceContext.NS_NC_30, "Person");			
		XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Person_" + resultId);		
		XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "M" + resultId);
		
		
		Element personDobElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		
		Element personDobValue = XmlUtils.appendElement(personDobElement, OjbcNamespaceContext.NS_NC_30, "Date");						
		personDobValue.setTextContent(courtCaseDetail.getPersonBirthDate());
		
		Element eyeColorElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonEyeColorText");
		eyeColorElement.setTextContent(courtCaseDetail.getPersonEyeColor());
					
		Element hairColor = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonHairColorText");				
		hairColor.setTextContent(courtCaseDetail.getPersonHairColor());
		
		Element height = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonHeightMeasure");
		
		Element measureValueTxt = XmlUtils.appendElement(height, OjbcNamespaceContext.NS_NC_30, "MeasureValueText"); 		
		measureValueTxt.setTextContent(courtCaseDetail.getPersonHeight());
				
		Element lengthUnitCode = XmlUtils.appendElement(height, OjbcNamespaceContext.NS_NC_30, "LengthUnitCode");		
		lengthUnitCode.setTextContent(courtCaseDetail.getPersonHeightUnits());
		
		Element personNameElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");					
		personGivenNameElement.setTextContent(courtCaseDetail.getPersonGivenName());
		
		Element personMiddleName = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");						
		personMiddleName.setTextContent(courtCaseDetail.getPersonMiddleName());
		
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent(courtCaseDetail.getPersonSurName());			
		
		Element personRaceCode = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonRaceCode");						
		personRaceCode.setTextContent(courtCaseDetail.getPersonRaceCode());
					
		Element personSexCode = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");						
		personSexCode.setTextContent(courtCaseDetail.getPersonSexCode());
		
		Element personWeightMeasure = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonWeightMeasure");
		Element measureValTxt = XmlUtils.appendElement(personWeightMeasure, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");
					
		measureValTxt.setTextContent(courtCaseDetail.getPersonWeight());		
		
		Element personAugmentation = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");
		
		Element driverLicense = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
		
		Element driverLicenseId = XmlUtils.appendElement(driverLicense, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseIdentification");
		
		Element driverLicenseIdVal = XmlUtils.appendElement(driverLicenseId, OjbcNamespaceContext.NS_NC_30, "IdentificationID"); 					
		driverLicenseIdVal.setTextContent(courtCaseDetail.getDriverLicenseId());
				
		Element drivLicIdSrcTxtElement = XmlUtils.appendElement(driverLicenseId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");					
		drivLicIdSrcTxtElement.setTextContent(courtCaseDetail.getDriverLicenseIdSourceTxt());
		
		Element personFBIId = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "PersonFBIIdentification");
		
		Element personFBIIdVal = XmlUtils.appendElement(personFBIId, OjbcNamespaceContext.NS_NC_30, "IdentificationID");				
		personFBIIdVal.setTextContent(courtCaseDetail.getPersonFBIId());
		
		Element personSid = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");
		Element personSidVal = XmlUtils.appendElement(personSid, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
						
		personSidVal.setTextContent(courtCaseDetail.getPersonSid());			
		
		Element personCaseAssociation = XmlUtils.appendElement(courtCaseSearchResultElement, OjbcNamespaceContext.NS_CYFS, "PersonCaseAssociation");			
		
		Element personAssoc = XmlUtils.appendElement(personCaseAssociation, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(personAssoc, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_" + resultId);
		
		Element caseAssoc = XmlUtils.appendElement(personCaseAssociation, OjbcNamespaceContext.NS_NC_30, "Case");
		XmlUtils.addAttribute(caseAssoc, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Case_" + resultId);
		
		
		Element srcSysNameTxtElement = XmlUtils.appendElement(courtCaseSearchResultElement, 
				OjbcNamespaceContext.NS_COURT_CASE_SEARCH_RESULTS_EXT, "SourceSystemNameText");
						
		srcSysNameTxtElement.setTextContent(courtCaseDetail.getSourceSystemNameText());
		
		Element sysIdElement = XmlUtils.appendElement(courtCaseSearchResultElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");
		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
				
		sysIdValElement.setTextContent(courtCaseDetail.getSystemId());
		
		Element sysName = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
						
		sysName.setTextContent(courtCaseDetail.getSystemName());
		
		Element srchResCatTxtElement = XmlUtils.appendElement(courtCaseSearchResultElement, 
				OjbcNamespaceContext.NS_COURT_CASE_SEARCH_RESULTS_EXT, "SearchResultCategoryText");
				
		srchResCatTxtElement.setTextContent(courtCaseDetail.getSearchResultCategoryText());
		
		Element infoOwnOrgElement =  XmlUtils.appendElement(courtCaseSearchResultElement, 
				OjbcNamespaceContext.NS_COURT_CASE_SEARCH_RESULTS_EXT, "InformationOwningOrganization");		
		
		Element orgBranchNameElement = XmlUtils.appendElement(infoOwnOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");				
		orgBranchNameElement.setTextContent(courtCaseDetail.getOrganizationBranchName());
				
		Element orgNameElement = XmlUtils.appendElement(infoOwnOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");		
		orgNameElement.setTextContent(courtCaseDetail.getOrganizationName());
		
		Element metadata = XmlUtils.appendElement(courtCaseSearchResultElement, OjbcNamespaceContext.NS_NC_30, "Metadata");
		XmlUtils.addAttribute(metadata, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "M" + resultId);		
		
		Element lastUpdatedDate = XmlUtils.appendElement(metadata, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
		
		Element lastUpdateValElement = XmlUtils.appendElement(lastUpdatedDate, OjbcNamespaceContext.NS_NC_30, "Date");									
						
		lastUpdateValElement.setTextContent(courtCaseDetail.getLastUpdatedDate());		
						
		return courtCaseSearchResultElement;
	}	

}

