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
package org.ojbc.bundles.adapters.staticmock.custody;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodySearchResultBuilder {
	
	
	 // TODO null check elements before writing them
	 // TOOD trim() all values
	 public static Element buildCustodySearchResultElement(Document custodySearchResultsDoc, Document custodyQueryResult, String resultId) throws Exception{
		 
		CustodyDetail custodyDetail = getCustodyDetail(custodyQueryResult);
		 		 
		Element custodySearchResultElement = custodySearchResultsDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_SEARCH_RESULTS, "CustodySearchResult");
		custodySearchResultElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_SEARCH_RESULTS);
		
		XmlUtils.addAttribute(custodySearchResultElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Result_" + resultId);				
		
		
		// doc creation date
		Element docCreateDate = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
		
		Element docCreateDateTime = XmlUtils.appendElement(docCreateDate, OjbcNamespaceContext.NS_NC_30, "DateTime");
						
		docCreateDateTime.setTextContent(custodyDetail.getDocCreationDate());
		
		// doc id	
		Element docIdElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
		
		Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		docIdValElement.setTextContent(custodyDetail.getDocId());

		// IdentificationCategoryDescriptionText
		
		Element docIdCatDescTxtElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		docIdCatDescTxtElement.setTextContent(custodyDetail.getDocumentIdCategoryDescription());
				
		// Person 		
		Element personElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "Person");
				
		XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Person_" + resultId);		
		XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "M" + resultId);
		
		// DOB
		Element personDob = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		Element dobDateTimeElement = XmlUtils.appendElement(personDob, OjbcNamespaceContext.NS_NC_30, "DateTime");
				
		dobDateTimeElement.setTextContent(custodyDetail.getPersonDob());
		
		// person name
		
		Element personName = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");

		Element personGivenNameElement = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
				
		personGivenNameElement.setTextContent(custodyDetail.getPersonGivenName());
		
				
		Element middleNameElement = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		middleNameElement.setTextContent(custodyDetail.getPersonMiddleName());
				
		Element surName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
		surName.setTextContent(custodyDetail.getPersonSurName());			
						
		Element personSexTxt = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSexText");				
		personSexTxt.setTextContent(custodyDetail.getPersonSex());
		
		Element personSSNIdentification = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");
						
		Element ssnIdVal = XmlUtils.appendElement(personSSNIdentification, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		ssnIdVal.setTextContent(custodyDetail.getPersonSsn());		
		
		Element personStateIdElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonStateIdentification");
		
		Element personStateIdValElement = XmlUtils.appendElement(personStateIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");					
		personStateIdValElement.setTextContent(custodyDetail.getPersonStateId());		
		
		Element booking = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "Booking");		
		XmlUtils.addAttribute(booking, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Booking" + resultId);
						
		Element fingerprintDate = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "FingerprintDate");
		
		Element fingerprintDateTime = XmlUtils.appendElement(fingerprintDate, OjbcNamespaceContext.NS_NC_30, "DateTime");
				
		fingerprintDateTime.setTextContent(custodyDetail.getFingerprintDate());
		
		Element bookingSubjectElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
		Element bookingSubjIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");
		Element bookingSubjIdValElement = XmlUtils.appendElement(bookingSubjIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
					
		bookingSubjIdValElement.setTextContent(custodyDetail.getBookingSubjectId());
		
		Element imageElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_NC_30, "Image");
		Element imgLocElement = XmlUtils.appendElement(imageElement, OjbcNamespaceContext.NS_NC_30, "ImageLocation");
		Element imgLocDescTxtElement = XmlUtils.appendElement(imgLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");
						
		imgLocDescTxtElement.setTextContent(custodyDetail.getImageLocation());

		Element charge = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");		
		XmlUtils.addAttribute(charge, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Charge_" + resultId);
				
		
		Element chargeCountQuantityElement = XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");
		
		chargeCountQuantityElement.setTextContent(custodyDetail.getChargeCount());
		
		Element chargeDescriptionTxtElement = XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
						
		chargeDescriptionTxtElement.setTextContent(custodyDetail.getChargeDescription());
		
		Element chargeStatuteElement = XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element statuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element statCodeIdValElement = XmlUtils.appendElement(statuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			
		statCodeIdValElement.setTextContent(custodyDetail.getChargeStatuteCodeId());
		
		Element statCodeIdCatDescTxtElement = XmlUtils.appendElement(statuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
				
		statCodeIdCatDescTxtElement.setTextContent(custodyDetail.getStatuteIdCategoryDescriptionTxt());		
						
		Element personChargeAssociation = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_JXDM_51, "PersonChargeAssociation");
		
		Element person = XmlUtils.appendElement(personChargeAssociation, OjbcNamespaceContext.NS_NC_30, "Person");		
		XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_" + resultId);
				
		Element personCharge = XmlUtils.appendElement(personChargeAssociation, OjbcNamespaceContext.NS_JXDM_51, "Charge");
		XmlUtils.addAttribute(personCharge, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Charge_" + resultId);
				
		Element activityChargeAssociation = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_JXDM_51, "ActivityChargeAssociation");
				
		Element activity = XmlUtils.appendElement(activityChargeAssociation, OjbcNamespaceContext.NS_NC_30, "Activity");
		XmlUtils.addAttribute(activity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Booking_" + resultId);
		
		Element activityCharge = XmlUtils.appendElement(activityChargeAssociation, OjbcNamespaceContext.NS_JXDM_51, "Charge");
		XmlUtils.addAttribute(activityCharge, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Charge_" + resultId);
		
		
		Element sourceSystemNameTextElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "SourceSystemNameText");
						
		sourceSystemNameTextElement.setTextContent(custodyDetail.getSourceSystemNameText());
		
		Element systemIdentification = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");
		
		Element systemIdVal = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
								
		systemIdVal.setTextContent(custodyDetail.getSystemId());
				
		Element systemName = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "SystemName");
		
		systemName.setTextContent(custodyDetail.getSystemName());
		
		Element searchResultCategoryTxtElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "SearchResultCategoryText");
				  		
		searchResultCategoryTxtElement.setTextContent(custodyDetail.getSearchResultCategoryDescriptionText().trim());
		
		Element infoOwningOrgElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "InformationOwningOrganization");
		
		Element organizationBranchNameElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
				
		organizationBranchNameElement.setTextContent(custodyDetail.getOrganizationBranchName());
		
		Element organizationName = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
		
		organizationName.setTextContent(custodyDetail.getOrganizationName());
		
		Element metaData = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "Metadata");		
		XmlUtils.addAttribute(metaData, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "M" + resultId);		
		
		Element lastUpdatedDate = XmlUtils.appendElement(metaData, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
		
		Element lastUpdatedDateVal = XmlUtils.appendElement(lastUpdatedDate, OjbcNamespaceContext.NS_NC_30, "Date");
		
		lastUpdatedDateVal.setTextContent(custodyDetail.getLastUpdatedDate());		
		
		return custodySearchResultElement;
	}	
	 

	static CustodyDetail getCustodyDetail(Document custodyDetailDoc) throws Exception{
		
		CustodyDetail rCustodyDetail = new CustodyDetail();
		
		String sDocCreationDate = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:DocumentCreationDate/nc30:DateTime");				
		rCustodyDetail.setDocCreationDate(sDocCreationDate);
				
		String documentId = XmlUtils.xPathStringSearch(custodyDetailDoc, "/cq-res-exch:CustodyQueryResults/nc30:DocumentIdentification/nc30:IdentificationID");		
		rCustodyDetail.setDocId(documentId);
				
		String docIdCatDescription = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/nc30:DocumentIdentification/nc30:IdentificationCategoryDescriptionText");		
		rCustodyDetail.setDocumentIdCategoryDescription(docIdCatDescription);
				
		String systemId = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/intel31:SystemIdentification/nc30:IdentificationID");		
		rCustodyDetail.setSystemId(systemId);
				
		String systemName = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/intel31:SystemIdentification/nc30:SystemName");
		rCustodyDetail.setSystemName(systemName);			
		
		String dobVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-ext:InmateCustody/nc30:PersonBirthDate/nc30:DateTime");
		rCustodyDetail.setPersonDob(dobVal);
				
		String personGivenName = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonGivenName");		
		rCustodyDetail.setPersonGivenName(personGivenName);
		
		String middleNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonMiddleName");
		rCustodyDetail.setPersonMiddleName(middleNameVal);
		
		String surNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonSurName");
		rCustodyDetail.setPersonSurName(surNameVal);
		
		String personSexVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonSexText");
		rCustodyDetail.setPersonSex(personSexVal);
		
		String ssnVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonSSNIdentification/nc30:IdentificationID");
		rCustodyDetail.setPersonSsn(ssnVal);
		
		String personSidVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonStateIdentification/nc30:IdentificationID");	
		rCustodyDetail.setPersonStateId(personSidVal);
		
		String fingerprintDateTime = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-ext:Booking/jxdm51:FingerprintDate/nc30:DateTime");
		rCustodyDetail.setFingerprintDate(fingerprintDateTime);
		
		String bookingSubjIdVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");	
		rCustodyDetail.setBookingSubjectId(bookingSubjIdVal);
		
		String imgLocDescTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:Image/nc30:ImageLocation/nc30:LocationDescriptionText");
		rCustodyDetail.setImageLocation(imgLocDescTxt);
		
		String chargeCountQuantVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:Charge/jxdm51:ChargeCountQuantity");		
		rCustodyDetail.setChargeCount(chargeCountQuantVal);
		
		String chargeDescTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:Charge/jxdm51:ChargeDescriptionText");
		rCustodyDetail.setChargeDescription(chargeDescTxt);
		
		String chargeStatuteVal = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//jxdm51:ChargeStatute/jxdm51:StatuteCodeIdentification/nc30:IdentificationID");		
		rCustodyDetail.setChargeStatuteCodeId(chargeStatuteVal);
		
		String idCatDescTxtVal = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//jxdm51:ChargeStatute/jxdm51:StatuteCodeIdentification/nc30:IdentificationCategoryDescriptionText");
		rCustodyDetail.setStatuteIdCategoryDescriptionTxt(idCatDescTxtVal);
		
		String sourceSystemNameText = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-exch:CustodyQueryResults/cq-res-ext:SourceSystemNameText");		
		rCustodyDetail.setSourceSystemNameText(sourceSystemNameText);
		
		String searchResultCatTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//nc30:DocumentIdentification/nc30:IdentificationCategoryDescriptionText");
		rCustodyDetail.setSearchResultCategoryDescriptionText(searchResultCatTxt);
		
		String orgNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:Organization/nc30:OrganizationName");
		rCustodyDetail.setOrganizationName(orgNameVal);
								
		String lastUpdatedDate = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/nc30:Metadata/nc30:LastUpdatedDate/nc30:Date");
		
		rCustodyDetail.setLastUpdatedDate(lastUpdatedDate);
		
		return rCustodyDetail;
	}	 

}

