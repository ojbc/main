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
import java.util.Map;

import org.apache.camel.Body;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.connectors.warrantmod.dao.WarrantsRepositoryBaseDAO;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.warrant.repository.model.Warrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
@Scope("prototype")
public class InitiateWarrantModificationRequestProcessor {

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
		Document document = OJBCXMLUtils.createDocument(); 
        Element rootElement = createWarrantModificationRequestRootElement(document);
        //TODO add <nc:Case> element here
        appendWarrantElement(warrant, rootElement);
		return document;
	}

	private void appendWarrantElement(Warrant warrant, Element rootElement) {
		Element warrantElement = XmlUtils.appendElement(rootElement, NS_JXDM_51, "Warrant");
        XmlUtils.addAttribute(warrantElement, NS_STRUCTURES_30, "id", "Warrant_01");
        //TODO add <j:CourtOrderDesignatedSubject> element here
        
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
        //TODO find out the mapping for transaction control number and add the element here. 
        
        for (String warrantRemark: warrant.getWarrantRemarkStrings()){
        	Element warrantCommentText = 
        			XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "WarrantCommentText");
        	warrantCommentText.setTextContent(warrantRemark);
        }
        
        Element warrantEntryCategoryCodeText = 
        		XmlUtils.appendElement(warrantAugmentation, NS_WARRANT_MOD_REQ_EXT, "WarrantEntryCategoryCodeText");
        warrantEntryCategoryCodeText.setTextContent(warrant.getWarrantEntryType());
        
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
		Element identificationId = 
        		XmlUtils.appendElement(parentElement, NS_NC_30, "IdentificationID");
        identificationId.setTextContent(textContent);
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
