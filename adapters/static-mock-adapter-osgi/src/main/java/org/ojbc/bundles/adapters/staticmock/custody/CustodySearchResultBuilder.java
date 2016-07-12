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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ojbc.bundles.adapters.staticmock.IdentifiableDocumentWrapper;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodySearchResultBuilder {
	
	private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	 public static Element buildCustodySearchResultElement(Document custodySearchResultsDoc, 
			 IdentifiableDocumentWrapper custodyDetailResultWrapper,  String resultId) throws Exception{
		 
		Document custodyQueryResult = custodyDetailResultWrapper.getDocument();
		CustodyDetail custodyDetail = getCustodyDetail(custodyQueryResult);
		 		 
		Element custodySearchResultElement = custodySearchResultsDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "CustodySearchResult");
		custodySearchResultElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_SEARCH_RES_EXT);
		
		XmlUtils.addAttribute(custodySearchResultElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Result_" + resultId);				
		
		
		String sDocCreationDate = custodyDetail.getDocCreationDate();
		
		if(StringUtils.isNotBlank(sDocCreationDate)){

			Element docCreateDate = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
			
			Element docCreateDateTime = XmlUtils.appendElement(docCreateDate, OjbcNamespaceContext.NS_NC_30, "DateTime");
							
			sDocCreationDate = sDocCreationDate.trim();
			
			docCreateDateTime.setTextContent(sDocCreationDate);
		}
		
		
		String docId = custodyDetail.getDocId();		
		
		boolean hasDocId = StringUtils.isNotBlank(docId);
					
		if(hasDocId){
		
			Element docIdElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
			
			if(hasDocId){
				
				Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
				
				docId = docId.trim();
				
				docIdValElement.setTextContent(docId);			
			}									
		}
		
				
		Element personElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "Person");
				
		XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Person_" + resultId);		
		XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "M" + resultId);
		

		String sPersonDob = custodyDetail.getPersonDob();
		
		if(StringUtils.isNotBlank(sPersonDob)){					
			
			Element personDob = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
			Element dobDateTimeElement = XmlUtils.appendElement(personDob, OjbcNamespaceContext.NS_NC_30, "Date");
					
			sPersonDob = sPersonDob.trim();
			
			dobDateTimeElement.setTextContent(sPersonDob);						
		}
		
		String sPersonGivenName = custodyDetail.getPersonGivenName();		
		boolean hasPersonGivenName = StringUtils.isNotBlank(sPersonGivenName);
		
		String sPersonMiddleName = custodyDetail.getPersonMiddleName();		
		boolean hasPersonMiddleName = StringUtils.isNotBlank(sPersonMiddleName);
		
		String sPersonSurName = custodyDetail.getPersonSurName();
		boolean hasPersonSurName = StringUtils.isNotBlank(sPersonSurName);
		
		if(hasPersonGivenName || hasPersonMiddleName || hasPersonSurName){
			
			Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
			
			if(hasPersonGivenName){
				
				Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
				
				sPersonGivenName = sPersonGivenName.trim();
				
				personGivenNameElement.setTextContent(sPersonGivenName);
			}
			
			if(hasPersonMiddleName){
				
				Element middleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
				
				sPersonMiddleName = sPersonMiddleName.trim();
				
				middleNameElement.setTextContent(sPersonMiddleName);				
			}
			
			if(hasPersonSurName){
				
				Element surNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
				
				sPersonSurName = sPersonSurName.trim();
				
				surNameElement.setTextContent(sPersonSurName);
			}
			
		}
				
		String sPersonRaceCode = custodyDetail.getPersonRace();
		
		if(StringUtils.isNotBlank(sPersonRaceCode)){
			
			Element personRaceCodeElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonRaceCode");
			
			sPersonRaceCode = sPersonRaceCode.trim();
			
			personRaceCodeElement.setTextContent(sPersonRaceCode);			
		}		
		
		String sPersonSex = custodyDetail.getPersonSex();
		
		if(StringUtils.isNotBlank(sPersonSex)){
			
			Element personSexCodeEl = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
			
			sPersonSex = sPersonSex.trim();
			
			personSexCodeEl.setTextContent(sPersonSex);			
		}
						
		String sPersonSSNId = custodyDetail.getPersonSsn();
		
		if(StringUtils.isNotBlank(sPersonSSNId)){
			
			Element personSSNIdentification = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");
			
			Element ssnIdVal = XmlUtils.appendElement(personSSNIdentification, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			
			sPersonSSNId = sPersonSSNId.trim();
			
			ssnIdVal.setTextContent(sPersonSSNId);				
		}

		String personStateId = custodyDetail.getPersonStateId();
		
		if(StringUtils.isNotBlank(personStateId)){
			
			Element personAugElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");			
						
			Element personStateFingerIdEl = XmlUtils.appendElement(personAugElement, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");
			
			Element personStateFingerIdValEl = XmlUtils.appendElement(personStateFingerIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");

			personStateId = personStateId.trim();
			
			personStateFingerIdValEl.setTextContent(personStateId);												
		}
		
		Element bookingElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "Booking");		
		XmlUtils.addAttribute(bookingElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Booking_" + resultId);						
		
		Date bookingActivityDate = custodyDetail.getBookingActivityDate();
		
		
		if(bookingActivityDate != null){
		
			String sBookingActivityDate = SDF_DATE_TIME.format(bookingActivityDate);
			
			Element bookingActivityDateEl = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
			
			Element bookingActivityDateTimeEl = XmlUtils.appendElement(bookingActivityDateEl, OjbcNamespaceContext.NS_NC_30, "DateTime");
			
			bookingActivityDateTimeEl.setTextContent(sBookingActivityDate);	
		}				
		
		Integer iBookingNumber = custodyDetail.getBookingNumber();
		
		String sBookingNumber = iBookingNumber == null ? null : String.valueOf(iBookingNumber);
		
		if(StringUtils.isNotEmpty(sBookingNumber)){

			Element bookingAgencyRecIdEl = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, 
					"BookingAgencyRecordIdentification");
			
			Element bookingAgencyRecIdValEl = XmlUtils.appendElement(bookingAgencyRecIdEl, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			
			sBookingNumber = sBookingNumber.trim();
			
			bookingAgencyRecIdValEl.setTextContent(sBookingNumber);
		}		
						
		
		String bookingSubjId = custodyDetail.getBookingSubjectId();
		
		if(StringUtils.isNotBlank(bookingSubjId)){
			
			Element bookingSubjectElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
			
			Element bookingSubjIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");
			
			Element bookingSubjIdValElement = XmlUtils.appendElement(bookingSubjIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
						
			bookingSubjId = bookingSubjId.trim();
			
			bookingSubjIdValElement.setTextContent(bookingSubjId);						
		}
		
		// TODO confirm/fix
		String sImgLoc = custodyDetail.getImageLocation();
		
		if(StringUtils.isNotBlank(sImgLoc)){
			
			Element imageElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "Image");
			
			Element imgLocElement = XmlUtils.appendElement(imageElement, OjbcNamespaceContext.NS_NC_30, "ImageLocation");
			
			Element imgLocDescTxtElement = XmlUtils.appendElement(imgLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");
							
			sImgLoc = sImgLoc.trim();
			
			imgLocDescTxtElement.setTextContent(sImgLoc);			
		}


		Element chargeElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_JXDM_51, "Charge");		
		XmlUtils.addAttribute(chargeElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Charge_" + resultId);
				
		
		String sChargeCount = custodyDetail.getChargeCount();
		
		if(StringUtils.isNotBlank(sChargeCount)){
			
			Element chargeCountQuantityElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");
			
			sChargeCount = sChargeCount.trim();
			
			chargeCountQuantityElement.setTextContent(sChargeCount);			
		}		
		
		String sChargeDesc = custodyDetail.getChargeDescription();
		
		if(StringUtils.isNotBlank(sChargeDesc)){
			
			Element chargeDescriptionTxtElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
			
			sChargeDesc = sChargeDesc.trim();
			
			chargeDescriptionTxtElement.setTextContent(sChargeDesc);			
		}
		
		String sChargeStatuteCodeId = custodyDetail.getChargeStatuteCodeId();
		
		String sStatuteIdCatDescTxt = custodyDetail.getStatuteIdCategoryDescriptionTxt();
		
		boolean hasChargeStatuteCodeId = StringUtils.isNotBlank(sChargeStatuteCodeId);
		
		boolean hasStatuteIdCatDescTxt = StringUtils.isNotBlank(sStatuteIdCatDescTxt);
						
		if(hasChargeStatuteCodeId || hasStatuteIdCatDescTxt){
			
			Element chargeStatuteElement = XmlUtils.appendElement(chargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
							
			Element statuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
						
			if(hasChargeStatuteCodeId){
				
				Element statCodeIdValElement = XmlUtils.appendElement(statuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
				
				sChargeStatuteCodeId = sChargeStatuteCodeId.trim();
				
				statCodeIdValElement.setTextContent(sChargeStatuteCodeId);					
			}
			
			if(hasStatuteIdCatDescTxt){

				Element statCodeIdCatDescTxtElement = XmlUtils.appendElement(statuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
				
				sStatuteIdCatDescTxt = sStatuteIdCatDescTxt.trim();
				
				statCodeIdCatDescTxtElement.setTextContent(sStatuteIdCatDescTxt);
			}
					
		}		
						
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
		
		sourceSystemNameTextElement.setTextContent(StaticMockQuery.CUSTODY_SEARCH_SYSTEM_ID);			
		

		String sSystemName = custodyDetail.getSystemName();
		
		boolean hasSystemName = StringUtils.isNotBlank(sSystemName);
				
		Element systemIdentification = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");
		Element systemIdVal = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		systemIdVal.setTextContent(custodyDetailResultWrapper.getId());					
		
		if(hasSystemName){
			
			Element systemName = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "SystemName");
			
			sSystemName = sSystemName.trim();
			
			systemName.setTextContent(sSystemName);				
		}								
		
		
		String sSearchResultCatTxt = custodyDetail.getSearchResultCategoryDescriptionText();
		
		if(StringUtils.isNotBlank(sSearchResultCatTxt)){

			Element searchResultCategoryTxtElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "SearchResultCategoryText");
	  		
			sSearchResultCatTxt = sSearchResultCatTxt.trim();
			
			searchResultCategoryTxtElement.setTextContent(sSearchResultCatTxt);			
		}
		
				
		String sBranchName = custodyDetail.getOrganizationBranchName();
		
		String sOrgName = custodyDetail.getOrganizationName();
		
		boolean hasBranchName = StringUtils.isNotBlank(sBranchName);
		
		boolean hasOrgName = StringUtils.isNotBlank(sOrgName);
		
		if(hasBranchName || hasOrgName){
			
			Element infoOwningOrgElement = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_CUSTODY_SEARCH_RES_EXT, "InformationOwningOrganization");
			
			if(hasBranchName){
								
				Element organizationBranchNameElement = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
						
				sBranchName = sBranchName.trim();
				
				organizationBranchNameElement.setTextContent(sBranchName);				
			}
			
			if(hasOrgName){
				
				Element organizationName = XmlUtils.appendElement(infoOwningOrgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName");
				
				sOrgName = sOrgName.trim();
				
				organizationName.setTextContent(sOrgName);				
			}			
		}

		
		String sLastUpdatedDate = custodyDetail.getLastUpdatedDate();
		
		if(StringUtils.isNotBlank(sLastUpdatedDate)){
			
			Element metaData = XmlUtils.appendElement(custodySearchResultElement, OjbcNamespaceContext.NS_NC_30, "Metadata");		
			XmlUtils.addAttribute(metaData, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "M" + resultId);		
			
			Element lastUpdatedDate = XmlUtils.appendElement(metaData, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
			
			Element lastUpdatedDateVal = XmlUtils.appendElement(lastUpdatedDate, OjbcNamespaceContext.NS_NC_30, "Date");
			
			sLastUpdatedDate = sLastUpdatedDate.trim();
			
			lastUpdatedDateVal.setTextContent(sLastUpdatedDate);				
		}
			
		return custodySearchResultElement;
	}	
	 

	static CustodyDetail getCustodyDetail(Document custodyDetailDoc) throws Exception{
		
		CustodyDetail rCustodyDetail = new CustodyDetail();
		
		String sDocCreationDate = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:DocumentCreationDate/nc30:DateTime");				
		rCustodyDetail.setDocCreationDate(sDocCreationDate);
				
		String documentId = XmlUtils.xPathStringSearch(custodyDetailDoc, "/cq-res-exch:CustodyQueryResults/nc30:DocumentIdentification/nc30:IdentificationID");		
		rCustodyDetail.setDocId(documentId);				
				
		String systemId = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/intel31:SystemIdentification/nc30:IdentificationID");		
		rCustodyDetail.setSystemId(systemId);
				
		String systemName = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/intel31:SystemIdentification/nc30:SystemName");
		rCustodyDetail.setSystemName(systemName);			
		
		String dobVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:Person/nc30:PersonBirthDate/nc30:Date");
		rCustodyDetail.setPersonDob(dobVal);
				
		String personGivenName = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonGivenName");		
		rCustodyDetail.setPersonGivenName(personGivenName);
		
		String middleNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonMiddleName");
		rCustodyDetail.setPersonMiddleName(middleNameVal);
		
		String surNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonName/nc30:PersonSurName");
		rCustodyDetail.setPersonSurName(surNameVal);
		
		String personSexVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:PersonSexCode");
		rCustodyDetail.setPersonSex(personSexVal);
		
		String sPersonRaceCode = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:PersonRaceCode");
		rCustodyDetail.setPersonRace(sPersonRaceCode);
		
		String ssnVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:PersonSSNIdentification/nc30:IdentificationID");
		rCustodyDetail.setPersonSsn(ssnVal);
		
		
		String personSidVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID");	
		rCustodyDetail.setPersonStateId(personSidVal);
		
		
		String sBookingNumber = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID");
		
		if(StringUtils.isNotEmpty(sBookingNumber)){
			
			sBookingNumber = sBookingNumber.trim();
			
			int iBookingNumber = Integer.parseInt(sBookingNumber);
			
			rCustodyDetail.setBookingNumber(iBookingNumber);
		}
						
		String sBookingActivityDate = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/jxdm51:Booking/nc30:ActivityDate/nc30:DateTime");
		
		if(StringUtils.isNotEmpty(sBookingActivityDate)){

			sBookingActivityDate = sBookingActivityDate.trim();
			
			Date bookingActivityDate = SDF_DATE_TIME.parse(sBookingActivityDate);
			
			rCustodyDetail.setBookingActivityDate(bookingActivityDate);			
		}		
		
		String bookingSubjIdVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");	
		rCustodyDetail.setBookingSubjectId(bookingSubjIdVal);
		
		//TODO FIXME
		String imgLocDescTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, "//nc30:Image/nc30:ImageLocation/nc30:LocationDescriptionText");
		rCustodyDetail.setImageLocation(imgLocDescTxt);
		
		//TODO FIXME
		String chargeCountQuantVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:Charge/jxdm51:ChargeCountQuantity");		
		rCustodyDetail.setChargeCount(chargeCountQuantVal);
		
		String chargeDescTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, "//jxdm51:Charge/jxdm51:ChargeDescriptionText");
		rCustodyDetail.setChargeDescription(chargeDescTxt);
		
		String chargeStatuteVal = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//jxdm51:ChargeStatute/jxdm51:StatuteCodeSectionIdentification/nc30:IdentificationID");		
		rCustodyDetail.setChargeStatuteCodeId(chargeStatuteVal);
		
		String idCatDescTxtVal = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//jxdm51:ChargeStatute/jxdm51:StatuteCodeIdentification/nc30:IdentificationCategoryDescriptionText");
		rCustodyDetail.setStatuteIdCategoryDescriptionTxt(idCatDescTxtVal);
		
		String sourceSystemNameText = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-exch:CustodyQueryResults/cq-res-ext:SourceSystemNameText");		
		rCustodyDetail.setSourceSystemNameText(sourceSystemNameText);
		
		String searchResultCatTxt = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"//cq-res-ext:QueryResultCategoryText");
		rCustodyDetail.setSearchResultCategoryDescriptionText(searchResultCatTxt);
		
		String orgBranchName = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-ext:InformationOwningOrganization/nc30:OrganizationBranchName");
		rCustodyDetail.setOrganizationBranchName(orgBranchName);		
		
		String orgNameVal = XmlUtils.xPathStringSearch(custodyDetailDoc, "//cq-res-ext:InformationOwningOrganization//nc30:OrganizationName");
		rCustodyDetail.setOrganizationName(orgNameVal);
								
		String lastUpdatedDate = XmlUtils.xPathStringSearch(custodyDetailDoc, 
				"/cq-res-exch:CustodyQueryResults/nc30:Metadata/nc30:LastUpdatedDate/nc30:Date");
		
		rCustodyDetail.setLastUpdatedDate(lastUpdatedDate);
		
		return rCustodyDetail;
	}	 

}

