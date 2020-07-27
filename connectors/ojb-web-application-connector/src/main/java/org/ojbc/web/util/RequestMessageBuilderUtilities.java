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
package org.ojbc.web.util;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_FBI_RECORD_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_FBI_RECORD_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.*;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.OjbcWebConstants;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.model.firearm.search.FirearmSearchRequestDomUtils;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.model.incident.search.IncidentSearchRequestDomUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.model.person.search.PersonSearchRequestDomUtils;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.ojbc.web.model.subscription.search.SubscriptionStatus;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequestDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class contains methods to convert POJOs to XML request documents or to 
 * take a few string arguments and make an XML request document out of them.
 *
 */
public class RequestMessageBuilderUtilities {

	private static final String IDENTIFICATION_RESULTS_ARCHIVE_REQUEST_SYSTEM_NAME = 
			"{http://ojbc.org/Services/WSDL/IdentificationResultsModificationRequestService/1.0}SubmitIdentificationResultsArchiveRequest";
	private static final String IDENTIFICATION_RESULTS_UNARCHIVE_REQUEST_SYSTEM_NAME = 
			"{http://ojbc.org/Services/WSDL/IdentificationResultsModificationRequestService/1.0}SubmitIdentificationResultsUnarchiveRequest";

	private static final Log log = LogFactory.getLog( RequestMessageBuilderUtilities.class );
	
	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	private static final String TOPIC_EXPRESSION_DIALECT = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete";
	
	public static String createPersonQueryRequest(DetailsRequest detailsRequest)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<pqr:PersonRecordRequest xmlns:pqr=\"" + OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST + "\" xmlns:nc20=\"" + OjbcNamespaceContext.NS_NC + "\">");
		sb.append("    <pqr:PersonRecordRequestIdentification >");
		sb.append("        <nc20:IdentificationID>" + detailsRequest.getIdentificationID() + "</nc20:IdentificationID>");
		sb.append("        <nc20:IdentificationSourceText>" + detailsRequest.getIdentificationSourceText() + "</nc20:IdentificationSourceText>");
		sb.append("    </pqr:PersonRecordRequestIdentification>");
		
		if (BooleanUtils.isTrue(detailsRequest.getCivilPurposeRequest())){
			sb.append("    <pqr:CivilPurposeRequestIndicator>true</pqr:CivilPurposeRequestIndicator>");
		}
		if (BooleanUtils.isTrue(detailsRequest.getTextRapsheetRequest())){
			sb.append("    <pqr:TextRapSheetRequestIndicator>true</pqr:TextRapSheetRequestIndicator>");
		}
		sb.append("</pqr:PersonRecordRequest>");
		
		return sb.toString();
	}

	public static String createPersonQueryWildlifeRequest(DetailsRequest detailsRequest)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<wlq-req-doc:WildlifeLicenseQueryRequest xmlns:wlq-req-doc=\"http://ojbc.org/IEPD/Exchange/WildlifeLicenseQueryRequest/1.0\" ");
		sb.append("		xmlns:wlq-req-ext=\"http://ojbc.org/IEPD/Extensions/WildlifeLicenseQueryRequestExtension/1.0\" xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\" ");
		sb.append("		> ");
		sb.append("			<wlq-req-ext:WildlifeLicenseIdentification> ");
		sb.append("						<nc:IdentificationID>" + detailsRequest.getIdentificationID() + "</nc:IdentificationID> ");
		sb.append("						<nc:IdentificationSourceText>" + detailsRequest.getIdentificationSourceText() + "</nc:IdentificationSourceText> ");
		sb.append("	</wlq-req-ext:WildlifeLicenseIdentification> ");
		sb.append("</wlq-req-doc:WildlifeLicenseQueryRequest> ");
		
		return sb.toString();
	}
	
	public static String createPersonQueryProfessionalRequest(DetailsRequest detailsRequest)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<rlq-req-doc:RegulatoryLicenseQueryRequest \n");
		sb.append("	xmlns:rlq-req-doc=\"http://ojbc.org/IEPD/Exchange/RegulatoryLicenseQueryRequest/1.0\" \n");
		sb.append("	xmlns:rlq-req-ext=\"http://ojbc.org/IEPD/Extensions/RegulatoryLicenseQueryRequestExtension/1.0\" \n");
		sb.append(" xmlns:nc=\"http://release.niem.gov/niem/niem-core/4.0/\" \n");
		sb.append("	xmlns:structures=\"http://release.niem.gov/niem/structures/4.0/\"> \n");
		sb.append("		<nc:Person structures:id=\"Person_01\"> \n");
		sb.append("			<nc:PersonLicenseIdentification> \n");
		sb.append("				<nc:IdentificationID>" + detailsRequest.getIdentificationID() + "</nc:IdentificationID> \n");
		sb.append("				<nc:IdentificationSourceText>" + detailsRequest.getIdentificationSourceText() + "</nc:IdentificationSourceText> \n");
		sb.append("			</nc:PersonLicenseIdentification> \n");
		sb.append("		</nc:Person> \n");
		sb.append("	</rlq-req-doc:RegulatoryLicenseQueryRequest> ");
		
		return sb.toString();
	}	
	
	public static String createCannabisLicenseProfessionalRequest(DetailsRequest detailsRequest)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("	<clq-req-doc:CannabisLicenseQueryRequest \n");
		sb.append("		xmlns:clq-req-doc=\"http://ojbc.org/IEPD/Exchange/CannabisLicenseQueryRequest/1.0\" \n");
		sb.append("		xmlns:nc=\"http://release.niem.gov/niem/niem-core/4.0/\"> \n");
		sb.append("		<nc:Person> \n");
		sb.append("			<nc:PersonLicenseIdentification> \n");
		sb.append("				<nc:IdentificationID>" + detailsRequest.getIdentificationID() + "</nc:IdentificationID> \n");
		sb.append("				<nc:IdentificationSourceText>" + detailsRequest.getIdentificationSourceText() + "</nc:IdentificationSourceText> \n");
		sb.append("			</nc:PersonLicenseIdentification> \n");
		sb.append("		</nc:Person> \n");
		sb.append("	</clq-req-doc:CannabisLicenseQueryRequest> \n");
		
		return sb.toString();
	}	
	
	public static String createPersonToIncidentQueryRequest(String identificationID, String identificationSourceText)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:IncidentPersonSearchRequest");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchRequest/1.0\"");
		sb.append("	xmlns:ext=\"http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0\"");
		sb.append("	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\">");
		sb.append("	<nc:Person>");
		sb.append("		<nc:PersonOtherIdentification>");
		sb.append("			<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("		</nc:PersonOtherIdentification>");
		sb.append("	</nc:Person>");
		sb.append("	<ext:SourceSystemNameText>" + identificationSourceText + "</ext:SourceSystemNameText>");
		sb.append("</exchange:IncidentPersonSearchRequest>");

		
		return sb.toString();
	}

	public static String createVehicleToIncidentQueryRequest(String identificationID, String identificationSourceText)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:IncidentVehicleSearchRequest");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchRequest/1.0\"");
		sb.append("	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\"");
		sb.append("	xmlns:extVehicle=\"http://ojbc.org/IEPD/Extensions/IncidentVehicleSearchRequest/1.0\"");
		sb.append("	xmlns:ext=\"http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0\">");
		sb.append("		<extVehicle:Vehicle>");
		sb.append("			<extVehicle:VehicleSystemIdentification>");
		sb.append("				<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("			</extVehicle:VehicleSystemIdentification>");
		sb.append("		</extVehicle:Vehicle>");
		sb.append("		<ext:SourceSystemNameText>" + identificationSourceText + "</ext:SourceSystemNameText>");
		sb.append("</exchange:IncidentVehicleSearchRequest>");


		
		return sb.toString();
	}
	
	public static String createIncidentReportRequest(String identificationID, String identificationSourceText, String categoryCode)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:IncidentIdentifierIncidentReportRequest"); 
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentReportRequest/1.0\""); 
		sb.append("	xmlns:extension=\"http://ojbc.org/IEPD/Extensions/IncidentReportRequest/1.0\""); 
		sb.append("	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\">");
		sb.append("	<extension:Incident>");
		sb.append("	    <nc:ActivityIdentification>");
		sb.append("	    	<nc:IdentificationID>"+ identificationID + "</nc:IdentificationID>");
		sb.append("	    </nc:ActivityIdentification>");
		sb.append("	    <extension:IncidentCategoryCode>" + categoryCode + "</extension:IncidentCategoryCode>");
		sb.append("	</extension:Incident>");
		sb.append(" <extension:SourceSystemNameText>" + identificationSourceText + "</extension:SourceSystemNameText>");
		sb.append("</exchange:IncidentIdentifierIncidentReportRequest>");

		
		return sb.toString();
	}
	
	public static Document createPersonSearchRequest(PersonSearchRequest psr) throws Exception
	{
		Document doc = OJBCXMLUtils.createDocument();
		
		Element personSearchRequestElement = NIEMXMLUtils.createPersonsSearchRequestElement(doc, "SM003");

		Element personElement = PersonSearchRequestDomUtils.createPersonsElement(doc, psr);
		
		personSearchRequestElement.appendChild(personElement);
		doc.appendChild(personSearchRequestElement);
		
		if (psr.getSourceSystems()==null || psr.getSourceSystems().size()==0)
		{
			throw new IllegalStateException("No source systems specified");
		}	
		
		for (String sourceSystemName : psr.getSourceSystems())
		{
			Element sourceSystemNameElement = NIEMXMLUtils.createSourceSystemElement(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, sourceSystemName);
			personSearchRequestElement.appendChild(sourceSystemNameElement);
		}	
		
		if (psr.getPersonSurNameMetaData() == SearchFieldMetadata.ExactMatch || psr.getPersonGivenNameMetaData() == SearchFieldMetadata.ExactMatch )
		{
			Element exactMatchMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, SearchFieldMetadata.ExactMatch);
			personSearchRequestElement.appendChild(exactMatchMetaData);
		}	

		if (psr.getPersonSurNameMetaData() == SearchFieldMetadata.StartsWith || psr.getPersonGivenNameMetaData() == SearchFieldMetadata.StartsWith )
		{
			Element startsWithMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, SearchFieldMetadata.StartsWith);
			personSearchRequestElement.appendChild(startsWithMetaData);
		}	
		
		if (StringUtils.isNotBlank(psr.getOnBehalfOf()) || StringUtils.isNotBlank(psr.getPurpose()))
		{
			Element auditMetaData = NIEMXMLUtils.createSearchMetaDataPurposeOnBehalfOf(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "OBO3", psr.getOnBehalfOf(), psr.getPurpose());
			personSearchRequestElement.appendChild(auditMetaData);
		}	

		log.debug("Person Search Request Document: " + OJBUtils.getStringFromDocument(doc));
		
		return doc;
	}
	
	public static Document createVehicleSearchRequest(VehicleSearchRequest vsr) throws Exception
	{
		Document doc = OJBCXMLUtils.createDocument();
		
		Element vehicleSearchRequestElement = VehicleSearchRequestDomUtils.createVehicleSearchRequestElement(doc);

		Element vehicleElment = VehicleSearchRequestDomUtils.createVehicleElement(doc, vsr);

		vehicleSearchRequestElement.appendChild(vehicleElment);
		doc.appendChild(vehicleSearchRequestElement);		
		
		if (vsr.getSourceSystems()==null || vsr.getSourceSystems().size()==0)
		{
			throw new IllegalStateException("No source systems specified");
		}	

		for (String sourceSystemName : vsr.getSourceSystems())
		{
			Element sourceSystemNameElement = NIEMXMLUtils.createSourceSystemElement(doc, OjbcNamespaceContext.NS_VEHICLE_SEARCH_REQUEST_EXT, sourceSystemName);
			vehicleSearchRequestElement.appendChild(sourceSystemNameElement);
		}	

		log.debug("Vehicle Search Request Document: " + OJBUtils.getStringFromDocument(doc));
		
		return doc;
	}
	
	public static Document createIncidentSearchRequest(IncidentSearchRequest isr, String cityTownCodelistNamespace, String cityTownCodelistElementName) throws Exception {
		Document doc = OJBCXMLUtils.createDocument();
		
		Element incidentSearchRequestElement = IncidentSearchRequestDomUtils.createIncidentSearchRequestElement(doc);
		
		Element incidentElement = IncidentSearchRequestDomUtils.createIncidentElement(doc, isr);
		
		Element locationElement = IncidentSearchRequestDomUtils.createLocationElement(doc, isr, cityTownCodelistNamespace, cityTownCodelistElementName);
		
		Element activityLocationAssociation = NIEMXMLUtils.createActivityLocationAssociationElement(doc, "I001", "L001");
		
		incidentSearchRequestElement.appendChild(incidentElement);
		incidentSearchRequestElement.appendChild(locationElement);
		incidentSearchRequestElement.appendChild(activityLocationAssociation);

		if (isr.getSourceSystems()==null || isr.getSourceSystems().size()==0)
		{
			throw new IllegalStateException("No source systems specified");
		}	

		for (String sourceSystemName : isr.getSourceSystems())
		{
			Element sourceSystemNameElement = NIEMXMLUtils.createSourceSystemElement(doc, OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_EXT, sourceSystemName);
			incidentSearchRequestElement.appendChild(sourceSystemNameElement);
		}	
		
		if (StringUtils.isNotBlank(isr.getOnBehalfOf()) || StringUtils.isNotBlank(isr.getPurpose()))
		{
			Element auditMetaData = NIEMXMLUtils.createSearchMetaDataPurposeOnBehalfOf(doc, OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_EXT, "SM003", isr.getOnBehalfOf(), isr.getPurpose());
			incidentSearchRequestElement.appendChild(auditMetaData);
		}	
		
		doc.appendChild(incidentSearchRequestElement);
		
		return doc;
	}
	
	public static Document createFirearmSearchRequest(FirearmSearchRequest fsr) throws Exception {
		Document doc = OJBCXMLUtils.createDocument();
		
		Element firearmSearchRequestElement = FirearmSearchRequestDomUtils.createFirearmSearchRequestElement(doc);
		
		Element firearm = FirearmSearchRequestDomUtils.createFirearmElement(doc, fsr);
		firearmSearchRequestElement.appendChild(firearm);
		
		Element firearmItemRegistration = NIEMXMLUtils.createFirearmItemRegistration(doc, "REGISTRATION", fsr.getFirearmRegistrationNumber(), fsr.getFirearmCounty(), fsr.getFirearmCurrentRegOnly());
		firearmSearchRequestElement.appendChild(firearmItemRegistration);
	
		Element propertyRegistrationAssociation = NIEMXMLUtils.createPropertyRegistrationAssociationElement(doc, "REGISTRATION", "FIREARM");
		firearmSearchRequestElement.appendChild(propertyRegistrationAssociation);
		
		if (fsr.getSourceSystems()==null || fsr.getSourceSystems().size()==0)
		{
			throw new IllegalStateException("No source systems specified");
		}	

		for (String sourceSystemName : fsr.getSourceSystems())
		{
			Element sourceSystemNameElement = NIEMXMLUtils.createSourceSystemElement(doc, OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_EXT, sourceSystemName);
			firearmSearchRequestElement.appendChild(sourceSystemNameElement);
		}	
		
		Element searchhMetaData = NIEMXMLUtils.createFirearmSearchMetaData(doc, OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_EXT, fsr.getFirearmSerialNumberMetaData());
		firearmSearchRequestElement.appendChild(searchhMetaData);
		
		if (StringUtils.isNotBlank(fsr.getOnBehalfOf()) || StringUtils.isNotBlank(fsr.getPurpose()))
		{
			Element auditMetaData = NIEMXMLUtils.createSearchMetaDataPurposeOnBehalfOf(doc, OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_EXT, "SM003", fsr.getOnBehalfOf(), fsr.getPurpose());
			firearmSearchRequestElement.appendChild(auditMetaData);
		}	
		
		doc.appendChild(firearmSearchRequestElement);
		
		return doc;
	}

	public static String createFirearmQueryRequest(
			DetailsRequest firearmQueryRequest) {
		
		String requestIdSrcTxt = firearmQueryRequest.getIdentificationSourceText().trim();
		
		String rootNode = null;
		String idNode = null;
		
		if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_PERSON)){
			
			rootNode = "PersonFirearmRegistrationRequest";
			idNode = "PersonFirearmRegistrationIdentification";
			
		}else if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_FIREARM)){
			rootNode = "FirearmRegistrationRequest";
			idNode = "FirearmRegistrationIdentification";
			
		}
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:" + rootNode);
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryRequest/1.0\""); 
		sb.append("	xmlns:extension=\"http://ojbc.org/IEPD/Extension/FirearmRegistrationQueryRequest/1.0\""); 
		sb.append("	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\">");
		sb.append("    <extension:" + idNode + ">");
		sb.append("        <nc:IdentificationID>" + firearmQueryRequest.getIdentificationID() + "</nc:IdentificationID>");
		sb.append("        <nc:IdentificationSourceText>" + firearmQueryRequest.getIdentificationSourceText() + "</nc:IdentificationSourceText>");
		sb.append("    </extension:" + idNode + ">");
		
		if (StringUtils.isNotBlank(firearmQueryRequest.getOnBehalfOf()) || StringUtils.isNotBlank(firearmQueryRequest.getPurpose()))
		{
			if (StringUtils.isNotBlank(firearmQueryRequest.getOnBehalfOf()))
			{
				log.debug("Add on behalf to message here");
			}	
					
			if (StringUtils.isNotBlank(firearmQueryRequest.getPurpose()))
			{
				log.debug("Add purpose to message here");
			}	
		}	
		
		sb.append("</exchange:" + rootNode + ">");
		
		return sb.toString();
	}
	
	public static Document createSubscriptionSearchRequest(SubscriptionSearchRequest subscriptionSearchRequest) throws Exception {
		log.info("Create Subscription search request for " + subscriptionSearchRequest); 
		Document doc = OJBCXMLUtils.createDocument();

        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST, "SubscriptionSearchRequest");
        doc.appendChild(root);
        root.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST, 
                OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST);
        root.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST_EXT, 
        		OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT);
        root.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_JXDM_41, 
        		OjbcNamespaceContext.NS_JXDM_41);
        root.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_NC, 
        		OjbcNamespaceContext.NS_NC);

        if (StringUtils.isNotBlank(subscriptionSearchRequest.getSubscribingSystemIdentifier()))
        {
        	Element subscribedEntity = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "SubscribedEntity");
        	XmlUtils.appendTextElement(subscribedEntity,  OjbcNamespaceContext.NS_NC, "IdentificationID", subscriptionSearchRequest.getSubscribingSystemIdentifier());
        }	
        
        if (subscriptionSearchRequest.getAdminSearch() != null){
        	Element adminSearchRequestIndicator = 
        			XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "AdminSearchRequestIndicator");
        	adminSearchRequestIndicator.setTextContent(BooleanUtils.toString(subscriptionSearchRequest.getAdminSearch(), "true", "false"));
        }
        
        if (subscriptionSearchRequest.getStatus() != null && subscriptionSearchRequest.getStatus().size() > 0 ){
        	if (subscriptionSearchRequest.getStatus().contains(SubscriptionStatus.ACTIVE.name())){
        		XmlUtils.appendTextElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "RequestActiveSubscriptionsIndicator", "true");
        	}
        	
        	if (subscriptionSearchRequest.getStatus().contains(SubscriptionStatus.INACTIVE.name())){
        		XmlUtils.appendTextElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "RequestInactiveSubscriptionsIndicator", "true");
        	}
        }
        
        if (StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerFederatedId())
        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerFirstName()) 
        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerLastName())){
        
        	Element subscribedEntity = 
        			XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "SubscribedEntity");
        	
	        if (StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerFirstName()) 
	        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerLastName())  ){
	        	Element entityPerson = 
	        			XmlUtils.appendElement(subscribedEntity, OjbcNamespaceContext.NS_NC, "EntityPerson");
	        	appendPersonNameElement(entityPerson, subscriptionSearchRequest.getOwnerFirstName(), subscriptionSearchRequest.getOwnerLastName());
	        }
	        
	        if (StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerFederatedId())){
	        	Element federatedIdentification = 
	        			XmlUtils.appendElement(subscribedEntity, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "SubscribedEntityFederatedIdentification");
	        	Element identificationId = 
	        			XmlUtils.appendElement(federatedIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
	        	identificationId.setTextContent(subscriptionSearchRequest.getOwnerFederatedId());
	        }
        }
        if (StringUtils.isNotBlank(subscriptionSearchRequest.getOwnerOri())){
        	Element organization = 
        			XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_41, "Organization");
            Element organizationAugmentation = XmlUtils.appendElement(organization, 
                    OjbcNamespaceContext.NS_JXDM_41, "OrganizationAugmentation"); 
            Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, 
                    OjbcNamespaceContext.NS_JXDM_41, "OrganizationORIIdentification"); 
            Element oriIdentificationID = XmlUtils.appendElement(organizationORIIdentification, 
                    OjbcNamespaceContext.NS_NC, "IdentificationID"); 
            oriIdentificationID.setTextContent(subscriptionSearchRequest.getOwnerOri());
        }
		
        if (StringUtils.isNotBlank(subscriptionSearchRequest.getSid())
        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getUcn())
        		|| (subscriptionSearchRequest.getSubscriptionCategories() != null && 
        			subscriptionSearchRequest.getSubscriptionCategories().size()>0)
        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getSubjectFirstName())
        		|| StringUtils.isNotBlank(subscriptionSearchRequest.getSubjectLastName())){
        	Element fbiSubscription = 
        			XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "FBISubscription");
        	
        	Element subscriptionSubject = 
        			XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "SubscriptionSubject");
        	
    		appendPersonNameElement(subscriptionSubject, subscriptionSearchRequest.getSubjectFirstName(), subscriptionSearchRequest.getSubjectLastName());
        	
        	if (StringUtils.isNotBlank(subscriptionSearchRequest.getSid())
        			|| StringUtils.isNotBlank(subscriptionSearchRequest.getUcn())){
        		Element personAugmentation = 
        				XmlUtils.appendElement(subscriptionSubject, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        		if (StringUtils.isNotBlank(subscriptionSearchRequest.getUcn())){
	        		Element personFBIIdentification = 
	        				XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
	                Element identificationID = XmlUtils.appendElement(personFBIIdentification, 
	                        OjbcNamespaceContext.NS_NC, "IdentificationID"); 
	                identificationID.setTextContent(subscriptionSearchRequest.getUcn());
        		}
        		
        		if (StringUtils.isNotBlank(subscriptionSearchRequest.getSid())){
	        		Element personStateFingerprintIdentification = 
	        				XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");
	                Element identificationID = XmlUtils.appendElement(personStateFingerprintIdentification, 
	                        OjbcNamespaceContext.NS_NC, "IdentificationID"); 
	                identificationID.setTextContent(subscriptionSearchRequest.getSid());
        		}
        	}
        	
        	if (subscriptionSearchRequest.getSubscriptionCategories() != null){
	        	for (String category : subscriptionSearchRequest.getSubscriptionCategories()){
	        		Element criminalSubscriptionReasonCode = 
	        				XmlUtils.appendElement(fbiSubscription, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "CriminalSubscriptionReasonCode");
	        		criminalSubscriptionReasonCode.setTextContent(category);
	        	}
        	}
        }
        
		return doc;

	}

	private static void appendPersonNameElement(
			Element entityPerson, String firstName, String lastName) {
    	if ( StringUtils.isNotBlank(firstName)
        		|| StringUtils.isNotBlank(lastName)){

			Element personName = 
					XmlUtils.appendElement(entityPerson, OjbcNamespaceContext.NS_NC, "PersonName");
			
			if (StringUtils.isNotBlank(firstName)){
				Element personGivenName = 
						XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC, "PersonGivenName");
				personGivenName.setTextContent(firstName);
			}
			if (StringUtils.isNotBlank(lastName)){
				Element personSurName = 
						XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC, "PersonSurName");
				personSurName.setTextContent(lastName);
			}
    	}
	}

	public static Document createSubscriptionQueryRequest(
			DetailsRequest subscriptionQueryRequest) throws Exception {
		Document doc = OJBCXMLUtils.createDocument();

        Element root = doc.createElementNS(OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_REQUEST, "SubscriptionQueryRequest");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST);
        
        if (BooleanUtils.isTrue(subscriptionQueryRequest.getAdmin())){
        	Element adminQueryRequestIndicator = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_REQUEST_EXT, "AdminQueryRequestIndicator");
        	adminQueryRequestIndicator.setTextContent("true");
        }
        
        Element subIdentification = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_QUERY_REQUEST_EXT, "SubscriptionIdentification");
        
        Element identificationId = XmlUtils.appendElement(subIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
        identificationId.setTextContent(subscriptionQueryRequest.getIdentificationID());
        
        Element identificationSourceText = XmlUtils.appendElement(subIdentification, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        identificationSourceText.setTextContent(subscriptionQueryRequest.getIdentificationSourceText());

        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		return doc;
	}

	public static Document createValidateSubscriptionRequest(String subscriptionId, String topic, String reasonCode) throws Exception{
		
		Document doc = OJBCXMLUtils.createDocument();		
		Element rootElement = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Validate");
		doc.appendChild(rootElement);
		rootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
		
		Element subValidMsgElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_SUB_VALID_MESSAGE, "SubscriptionValidationMessage");
		
		Element subIdElement = XmlUtils.appendElement(subValidMsgElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionIdentification");
		
		Element identificationIDElement = XmlUtils.appendElement(subIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
		
		identificationIDElement.setTextContent(subscriptionId);
		
		if (StringUtils.isNotBlank(reasonCode)){
			if (OjbcWebConstants.CRIMINAL_JUSTICE_EMPLOYMENT.equals(reasonCode) || OjbcWebConstants.FIREARMS.equals(reasonCode) || OjbcWebConstants.NON_CRIMINAL_JUSTICE_EMPLOYMENT.equals(reasonCode) || OjbcWebConstants.SECURITY_CLEARANCE_INFORMATION_ACT.equals(reasonCode) ){
				Element civilSubscriptionReasonCode = XmlUtils.appendElement(subValidMsgElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CivilSubscriptionReasonCode");
				civilSubscriptionReasonCode.setTextContent(reasonCode);
			}
			
			if (OjbcWebConstants.CRIMINAL_JUSTICE_INVESTIGATIVE.equals(reasonCode) || OjbcWebConstants.CRIMINAL_JUSTICE_SUPERVISION.equals(reasonCode)){
				Element criminalSubscriptionReasonCode = XmlUtils.appendElement(subValidMsgElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
				criminalSubscriptionReasonCode.setTextContent(reasonCode);
			}
		}
						
		Element topicElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_B2, "TopicExpression");
		XmlUtils.addAttribute(topicElement, null, "Dialect", TOPIC_EXPRESSION_DIALECT);
		
		topicElement.setTextContent(topic);
						
		return doc;
	}
	
    public static Document createPolicyAcknowledgementRecordingRequest() throws Exception {
        Document doc = OJBCXMLUtils.createDocument();       
        Element rootElement = doc.createElementNS(OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE, 
                OjbcNamespaceContext.NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE 
                +":AcknowledgementRecordingRequestForAllPolicies");
        doc.appendChild(rootElement);
        
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE, 
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT, 
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_NC_30);
        
        Element documentCreationDate = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
        Element date = XmlUtils.appendElement(documentCreationDate, OjbcNamespaceContext.NS_NC_30, "Date"); 
        date.setTextContent(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        Element policyAcknowledgementIndicator = XmlUtils.appendElement(rootElement, 
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT, 
                "PolicyAcknowledgementIndicator");
        policyAcknowledgementIndicator.setTextContent("true");
        return doc;
    }
    
    public static Document createJuvenileQueryRequest(String identificationID, String identificationSourceText) throws Exception
    {
    	Document document = OJBCXMLUtils.createDocument();
    	
        Element root = document.createElementNS(OjbcNamespaceContext.NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC, "JuvenileHistoryQuery");
        document.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC);
        
        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		
        Element juvenileHistoryQueryCriteria = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryQueryCriteria");
        
        Element juvenileInformationRecordID = XmlUtils.appendElement(juvenileHistoryQueryCriteria, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
        
        Element identificationIDElement = XmlUtils.appendElement(juvenileInformationRecordID, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
        identificationIDElement.setTextContent(identificationID);

        Element identificationSourceTextElement = XmlUtils.appendElement(juvenileInformationRecordID, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
        identificationSourceTextElement.setTextContent(identificationSourceText);
        
		return document;
    	
    }
    

    public static Document createPolicyBasedAccessControlRequest(Element samlToken, String... requestedResources) throws Exception {
    	
        Document document = OJBCXMLUtils.createDocument();       
        Element rootElement = document.createElementNS(OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST, 
                OjbcNamespaceContext.NS_PREFIX_ACCESS_CONTROL_REQUEST 
                +":IdentityBasedAccessControlRequest");
        document.appendChild(rootElement); 
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_ACCESS_CONTROL_REQUEST, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_ACCESS_CONTROL_REQUEST_EXT, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_PROXY_XSD_30, 
                OjbcNamespaceContext.NS_PROXY_XSD_30);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30, 
                OjbcNamespaceContext.NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_NC_30);
        rootElement.setAttribute("xmlns:"+ OjbcNamespaceContext.NS_PREFIX_JXDM_50, OjbcNamespaceContext.NS_JXDM_50);
        
        Element personIdentity = XmlUtils.appendElement(rootElement, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "PersonIdentity"); 
        
        Element identityPersonRepresentation = XmlUtils.appendElement(personIdentity, 
                OjbcNamespaceContext.NS_NC_30, "IdentityPersonRepresentation"); 
        XmlUtils.addAttribute(identityPersonRepresentation, 
                OjbcNamespaceContext.NS_STRUCTURES_30, "id", "P001"); 
        Element personName = XmlUtils.appendElement(identityPersonRepresentation, 
                OjbcNamespaceContext.NS_NC_30, "PersonName"); 
        Element personGivenName = XmlUtils.appendElement(personName, 
                OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
        String userFirstName = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:GivenName']/saml2:AttributeValue"); 
        personGivenName.setTextContent(userFirstName);
        
        Element personSurName = XmlUtils.appendElement(personName, 
                OjbcNamespaceContext.NS_NC_30, "PersonSurName");
        String userLastName = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:SurName']/saml2:AttributeValue"); 
        personSurName.setTextContent(userLastName);
        
        Element identityIdentification = XmlUtils.appendElement(personIdentity, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "IdentityIdentification");
        Element identificationID = XmlUtils.appendElement(identityIdentification, 
                OjbcNamespaceContext.NS_NC_30, "IdentificationID");
        String federationId = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue"); 
        identificationID.setTextContent(federationId);
        
        Element federatedQueryUserIndicator = XmlUtils.appendElement(personIdentity, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "FederatedQueryUserIndicator");
        String federatedQueryUserIndicatorValue = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:ext:user:FederatedQueryUserIndicator']/saml2:AttributeValue"); 
        federatedQueryUserIndicator.setTextContent(federatedQueryUserIndicatorValue);
        
        Element contactInformationAssociation = XmlUtils.appendElement(personIdentity, 
                OjbcNamespaceContext.NS_NC_30, "ContactInformationAssociation");
        Element contactEntity = XmlUtils.appendElement(contactInformationAssociation, 
                OjbcNamespaceContext.NS_NC_30, "ContactEntity");
        Element entityPerson = XmlUtils.appendElement(contactEntity, 
                OjbcNamespaceContext.NS_NC_30, "EntityPerson");
        XmlUtils.addAttribute(entityPerson, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "P001");
        
        Element contactInformation = XmlUtils.appendElement(contactInformationAssociation, 
                OjbcNamespaceContext.NS_NC_30, "ContactInformation");
        Element contactEmailID = XmlUtils.appendElement(contactInformation, 
                OjbcNamespaceContext.NS_NC_30, "ContactEmailID");
        String email = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue");
        contactEmailID.setTextContent(email);
        
        Element contactTelephoneNumber = XmlUtils.appendElement(contactInformation, 
                OjbcNamespaceContext.NS_NC_30, "ContactTelephoneNumber");
        Element fullTelephoneNumber = XmlUtils.appendElement(contactTelephoneNumber, 
                OjbcNamespaceContext.NS_NC_30, "FullTelephoneNumber");
        Element telephoneNumberFullID = XmlUtils.appendElement(fullTelephoneNumber, 
                OjbcNamespaceContext.NS_NC_30, "TelephoneNumberFullID");
        String telephoneNumber = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:TelephoneNumber']/saml2:AttributeValue"); 
        telephoneNumberFullID.setTextContent(telephoneNumber);
        
        Element personEmploymentAssociation = XmlUtils.appendElement(personIdentity, 
                OjbcNamespaceContext.NS_NC_30, "PersonEmploymentAssociation"); 
        Element employee  = XmlUtils.appendElement(personEmploymentAssociation, 
                OjbcNamespaceContext.NS_NC_30, "Employee"); 
        XmlUtils.addAttribute(employee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "P001");
        
        Element employer = XmlUtils.appendElement(personEmploymentAssociation, 
                OjbcNamespaceContext.NS_NC_30, "Employer"); 
        Element entityOrganization = XmlUtils.appendElement(employer, 
                OjbcNamespaceContext.NS_NC_30, "EntityOrganization"); 
        Element organizationName = XmlUtils.appendElement(entityOrganization, 
                OjbcNamespaceContext.NS_NC_30, "OrganizationName"); 
        String userAgency = XmlUtils.xPathStringSearch(samlToken,
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:EmployerName']/saml2:AttributeValue"); 
        organizationName.setTextContent(userAgency);
        
        Element organizationUnitName = XmlUtils.appendElement(entityOrganization, 
                OjbcNamespaceContext.NS_NC_30, "OrganizationUnitName"); 
        String employerSubUnitName = XmlUtils.xPathStringSearch(samlToken, 
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:EmployerSubUnitName']/saml2:AttributeValue");  
        organizationUnitName.setTextContent(employerSubUnitName);
        
        Element organizationAugmentation = XmlUtils.appendElement(entityOrganization, 
                OjbcNamespaceContext.NS_JXDM_50, "OrganizationAugmentation"); 
        Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, 
                OjbcNamespaceContext.NS_JXDM_50, "OrganizationORIIdentification"); 
        Element oriIdentificationID = XmlUtils.appendElement(organizationORIIdentification, 
                OjbcNamespaceContext.NS_NC_30, "IdentificationID"); 
        String ori = XmlUtils.xPathStringSearch(samlToken, 
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:EmployerORI']/saml2:AttributeValue");  
        oriIdentificationID.setTextContent(ori);

        Element criminalJusticeOrganizationIndicator = XmlUtils.appendElement(entityOrganization, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "CriminalJusticeOrganizationIndicator"); 
        String criminalJusticeOrganizationIndicatorValue = XmlUtils.xPathStringSearch(samlToken, 
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:ext:user:CriminalJusticeEmployerIndicator']/saml2:AttributeValue"); 
        criminalJusticeOrganizationIndicator.setTextContent(criminalJusticeOrganizationIndicatorValue);

        Element lawEnforcementOrganizationIndicator = XmlUtils.appendElement(entityOrganization, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "LawEnforcementOrganizationIndicator"); 
        String lawEnforcementOrganizationIndicatorValue = XmlUtils.xPathStringSearch(samlToken, 
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                        + "saml2:Attribute[@Name='gfipm:ext:user:LawEnforcementEmployerIndicator']/saml2:AttributeValue");
        lawEnforcementOrganizationIndicator.setTextContent(lawEnforcementOrganizationIndicatorValue);
        
        Element employeePositionName  = XmlUtils.appendElement(personEmploymentAssociation, 
                OjbcNamespaceContext.NS_NC_30, "EmployeePositionName"); 
        String employeePositionNameValue = XmlUtils.xPathStringSearch(samlToken, 
                "/saml2:Assertion/saml2:AttributeStatement[1]/"
                + "saml2:Attribute[@Name='gfipm:2.0:user:EmployeePositionName']/saml2:AttributeValue");
        employeePositionName.setTextContent(employeePositionNameValue);
        
        for (String requestedResource : requestedResources){
	        Element requestedResourceURI = XmlUtils.appendElement(rootElement, 
	                OjbcNamespaceContext.NS_ACCESS_CONTROL_REQUEST_EXT, "RequestedResourceURI"); 
	        requestedResourceURI.setTextContent(requestedResource);
        }
        
        log.debug("\nCreated Request:\n" + OJBUtils.getStringFromDocument(document));
        return document;
    }

	public static Document createRapbackSearchRequest(IdentificationResultSearchRequest searchRequest) throws Exception {
        Document document = OJBCXMLUtils.createDocument();       
        Element rootElement = document.createElementNS(OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST, 
                OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST 
                +":OrganizationIdentificationResultsSearchRequest");
        document.appendChild(rootElement); 
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST, 
                OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST);
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
        		OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30, 
        		OjbcNamespaceContext.NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_NC_30, 
        		OjbcNamespaceContext.NS_NC_30);

        appendIdentificationSearchRequestPerson(searchRequest, rootElement);
        appendIdentificationReportedDateRange(searchRequest, rootElement);
        
        if (searchRequest.getIdentificationTransactionStatus() != null){
	        for ( String identificationTransactionState: searchRequest.getIdentificationTransactionStatus()){
	        	XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
	        			"IdentificationResultStatusCode", identificationTransactionState);
	        }
        }
        
        XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
        		"IdentificationResultsCategoryCode", searchRequest.getIdentificationResultCategory());
        
        if (searchRequest.getCivilIdentificationReasonCodes() != null){
	        for ( String civilIdentificationReasonCode: searchRequest.getCivilIdentificationReasonCodes()){
	        	XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
	        			"CivilIdentificationReasonCode", civilIdentificationReasonCode);
	        }
        }

        if (searchRequest.getCriminalIdentificationReasonCodes() != null){
	        for ( String criminalIdentificationReasonCode: searchRequest.getCriminalIdentificationReasonCodes()){
	        	XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
	        			"CriminalIdentificationReasonCode", criminalIdentificationReasonCode);
	        }
        }
        
		return document;
	}

	private static void appendIdentificationReportedDateRange(
			IdentificationResultSearchRequest searchRequest, Element rootElement) {
		if (searchRequest.getReportedDateStartDate() != null || 
        		searchRequest.getReportedDateEndDate() != null){
        	Element identificationReportedDateRange = 
        			XmlUtils.appendElement(rootElement, 
        					OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, 
        					"IdentificationReportedDateRange");
        	
    		appendDateWapper(identificationReportedDateRange, "StartDate", NS_NC_30, searchRequest.getReportedDateStartLocalDate()); 
    		appendDateWapper(identificationReportedDateRange, "EndDate", NS_NC_30, searchRequest.getReportedDateEndLocalDate()); 
        }
	}

    public static final void appendDateWapper( Element parent, 
			String elementName, String wrapperElementNS, LocalDate localDate) {
		if (localDate != null){
			Element dateWrapperElement = XmlUtils.appendElement(parent, wrapperElementNS, elementName);
			Element date = XmlUtils.appendElement(dateWrapperElement, NS_NC_30, "Date");
			date.setTextContent(localDate.toString());
		}
	}

	private static void appendIdentificationSearchRequestPerson(
			IdentificationResultSearchRequest searchRequest, Element rootElement) {
		if (StringUtils.isNotBlank(searchRequest.getFirstName()) 
        		|| StringUtils.isNotBlank(searchRequest.getLastName()) 
        		|| StringUtils.isNotBlank(searchRequest.getOtn())
        		|| StringUtils.isNotBlank(searchRequest.getSid())
        		|| StringUtils.isNotBlank(searchRequest.getUcn())){
        	Element person = XmlUtils.appendElement(rootElement, NS_NC_30, "Person");
        	
        	if (StringUtils.isNotBlank(searchRequest.getFirstName())
        			|| StringUtils.isNotBlank(searchRequest.getLastName())){
        		Element personName = XmlUtils.appendElement(person, NS_NC_30, "PersonName");
        		if (StringUtils.isNotBlank(searchRequest.getFirstName())){
        			Element personGivenName = XmlUtils.appendElement(personName, NS_NC_30, "PersonGivenName");
        			personGivenName.setTextContent(searchRequest.getFirstName().trim());
        		}
        		if (StringUtils.isNotBlank(searchRequest.getLastName())){
        			Element personSurName = XmlUtils.appendElement(personName, NS_NC_30, "PersonSurName");
        			personSurName.setTextContent(searchRequest.getLastName().trim());
        		}
        	}
        	if (StringUtils.isNotBlank(searchRequest.getSid()) 
        			|| StringUtils.isNotBlank(searchRequest.getUcn())){
        		Element personAugmentation = XmlUtils.appendElement(person, NS_JXDM_50, "PersonAugmentation");
        		if (StringUtils.isNotBlank(searchRequest.getUcn())){
        			Element personFBIIdentification = XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonFBIIdentification");
        			XmlUtils.appendTextElement(personFBIIdentification, NS_NC_30, "IdentificationID", searchRequest.getUcn());
        		}
        		if (StringUtils.isNotBlank(searchRequest.getSid())){
        			Element personStateFingerprintIdentification = XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonStateFingerprintIdentification");
        			XmlUtils.appendTextElement(personStateFingerprintIdentification, NS_NC_30, "IdentificationID", searchRequest.getSid());
        		}
        	}
        	if (StringUtils.isNotBlank(searchRequest.getOtn())){
        		Element identifiedPersonTrackingIdentification = 
        				XmlUtils.appendElement(person, 
        						OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT,
        						"IdentifiedPersonTrackingIdentification");
        		Element identificationId = 
        				XmlUtils.appendElement(identifiedPersonTrackingIdentification, NS_NC_30, "IdentificationID");
        		identificationId.setTextContent(searchRequest.getOtn());
        	}
        }
	}

	public static Document createIdentificationInitialResultsQueryRequest(
			String transactionNumber) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST, 
                NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST 
                +":OrganizationIdentificationInitialResultsQueryRequest");
        document.appendChild(rootElement);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST, 
                NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST);
        buildIdentificationResultsQueryRequest(transactionNumber, rootElement);
        
		return document;
	}

	private static void buildIdentificationResultsQueryRequest(String transactionNumber, Element rootElement) {
		rootElement.setAttribute("xmlns:" + NS_PREFIX_INTEL_30, NS_INTEL_30);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_30, NS_NC_30);
        
        Element systemIdentification = 
        		XmlUtils.appendElement(rootElement, NS_INTEL_30, "SystemIdentification");
        Element identificationId = XmlUtils.appendElement(systemIdentification, NS_NC_30, "IdentificationID");
        identificationId.setTextContent(transactionNumber);
        
        Element systemName = XmlUtils.appendElement(systemIdentification, NS_NC_30, "SystemName"); 
        systemName.setTextContent("rap-back-data-store");
	}

	public static Document createIdentificationSubsequentResultsQueryRequest(
			String transactionNumber) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST, 
                NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST 
                +":OrganizationIdentificationSubsequentResultsQueryRequest");
        document.appendChild(rootElement);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST, 
                NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST);
        buildIdentificationResultsQueryRequest(transactionNumber, rootElement);
        
		return document;
	}

	public static Document createIdentificationResultsModificationRequest(
			String transactionNumber, boolean archive) throws Exception {
        Document document = OJBCXMLUtils.createDocument();
        
        String docNamespace = null; 
        String docPrefix = null; 
        String systemName = null;
        Element rootElement = null;
        if (archive){
        	docNamespace = NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;  
        	docPrefix =	NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;
        	systemName = IDENTIFICATION_RESULTS_ARCHIVE_REQUEST_SYSTEM_NAME; 
            rootElement = document.createElementNS(docNamespace, 
            		docPrefix + ":IdentificationResultsArchiveRequest");
        }
        else{
        	docNamespace = NS_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST;  
        	docPrefix =	NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST; 
        	systemName = IDENTIFICATION_RESULTS_UNARCHIVE_REQUEST_SYSTEM_NAME;
            rootElement = document.createElementNS(docNamespace, 
            		docPrefix + ":IdentificationResultsUnarchiveRequest");
        }
        
    	rootElement.setAttribute("xmlns:" + docPrefix, docNamespace);
        
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_30, NS_NC_30);
        document.appendChild(rootElement);

        Element identificationResultsIdentification = 
        		XmlUtils.appendElement(rootElement, docNamespace, "IdentificationResultsIdentification");
        
        Element identificationId = XmlUtils.appendElement(identificationResultsIdentification, NS_NC_30, "IdentificationID");
        identificationId.setTextContent(transactionNumber);
        
        Element systemNameElement = XmlUtils.appendElement(rootElement, NS_NC_30, "SystemName"); 
        systemNameElement.setTextContent(systemName);
        
		return document;
	}

	public static String createPersonToCourtCaseSearchRequest(
			String identificationID, String identificationSourceText) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<ccs-req-doc:CourtCaseSearchRequest");
		sb.append("	xmlns:ccs-req-doc=\"http://ojbc.org/IEPD/Exchange/CourtCaseSearchRequest/1.0\"");
		sb.append("	xmlns:ccs-req-ext=\"http://ojbc.org/IEPD/Extensions/CourtCaseSearchRequestExtension/1.0\"");
		sb.append("	xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\">");
		sb.append("	<nc:Person>");
		sb.append("		<ccs-req-ext:PersonRecordIdentification>");
		sb.append("			<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("		</ccs-req-ext:PersonRecordIdentification>");
		sb.append("	</nc:Person>");
		sb.append("	<ccs-req-ext:SourceSystemNameText>" + identificationSourceText + "</ccs-req-ext:SourceSystemNameText>");
		sb.append("</ccs-req-doc:CourtCaseSearchRequest>");
		
		return sb.toString();
	}

	public static String createCourtCaseQueryRequest(String identificationID,
			String identificationSourceText) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ccq-req-doc:CourtCaseQueryRequest");
		sb.append("	xmlns:ccq-req-doc=\"http://ojbc.org/IEPD/Exchange/CourtCaseQueryRequest/1.0\"");
		sb.append("	xmlns:ccq-req-ext=\"http://ojbc.org/IEPD/Extensions/CourtCaseQueryRequestExtension/1.0\"");
		sb.append("	xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\">");
		sb.append("	<ccq-req-ext:CourtCaseRecordIdentification>");
		sb.append("		<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("		<nc:IdentificationSourceText>" + identificationSourceText + "</nc:IdentificationSourceText>");
		sb.append("	</ccq-req-ext:CourtCaseRecordIdentification>");
		sb.append("</ccq-req-doc:CourtCaseQueryRequest>");
		return sb.toString();
	}

	public static String createPersonToCustodySearchRequest(
			String identificationID, String identificationSourceText) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<cs-req-doc:CustodySearchRequest");
		sb.append("	xmlns:cs-req-doc=\"http://ojbc.org/IEPD/Exchange/CustodySearchRequest/1.0\"");
		sb.append("	xmlns:cs-req-ext=\"http://ojbc.org/IEPD/Extensions/CustodySearchRequestExtension/1.0\"");
		sb.append("	xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\">");
		sb.append("	<nc:Person>");
		sb.append("		<cs-req-ext:PersonRecordIdentification>");
		sb.append("			<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("		</cs-req-ext:PersonRecordIdentification>");
		sb.append("	</nc:Person>");
		sb.append("	<cs-req-ext:SourceSystemNameText>" + identificationSourceText + "</cs-req-ext:SourceSystemNameText>");
		sb.append("</cs-req-doc:CustodySearchRequest>");
		
		return sb.toString();
	}

	public static String createCustodyQueryRequest(String identificationID,
			String identificationSourceText) {
		StringBuilder sb = new StringBuilder();
		sb.append("<cq-req-doc:CustodyQueryRequest");
		sb.append("	xmlns:cq-req-doc=\"http://ojbc.org/IEPD/Exchange/CustodyQueryRequest/1.0\"");
		sb.append("	xmlns:cq-req-ext=\"http://ojbc.org/IEPD/Extensions/CustodyQueryRequestExtension/1.0\"");
		sb.append("	xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\">");
		sb.append("	<cq-req-ext:CustodyRecordIdentification>");
		sb.append("		<nc:IdentificationID>" + identificationID + "</nc:IdentificationID>");
		sb.append("		<nc:IdentificationSourceText>" + identificationSourceText + "</nc:IdentificationSourceText>");
		sb.append("	</cq-req-ext:CustodyRecordIdentification>");
		sb.append("</cq-req-doc:CustodyQueryRequest>");
		return sb.toString();
	}

	public static String createVehicleCrashQueryRequest(
			DetailsRequest detailRequest) {
		StringBuilder sb = new StringBuilder();
		sb.append("<vcq-req-doc:VehicleCrashQueryRequest");
		sb.append("	xmlns:vcq-req-doc=\"http://ojbc.org/IEPD/Exchange/VehicleCrashQueryRequest/1.0\"");
		sb.append("	xmlns:vcq-req-ext=\"http://ojbc.org/IEPD/Extensions/VehicleCrashQueryRequestExtension/1.0\"");
		sb.append("	xmlns:nc=\"http://release.niem.gov/niem/niem-core/3.0/\">");
		sb.append("	<vcq-req-ext:VehicleCrashIdentification>");
		sb.append("		<nc:IdentificationID>" + detailRequest.getIdentificationID() + "</nc:IdentificationID>");
		sb.append("		<nc:IdentificationSourceText>" + detailRequest.getIdentificationSourceText() + "</nc:IdentificationSourceText>");
		sb.append("	</vcq-req-ext:VehicleCrashIdentification>");
		sb.append("</vcq-req-doc:VehicleCrashQueryRequest>");
		return sb.toString();
	}
	
	//	<fppq-req-doc:FirearmPurchaseProhibitionQueryRequest
	//		xmlns:fppq-req-doc="http://ojbc.org/IEPD/Exchange/FirearmPurchaseProhibitionQueryRequest/1.0"
	//		xmlns:fppq-req-ext="http://ojbc.org/IEPD/Extensions/FirearmPurchaseProhibitionQueryRequestExtension/1.0"
	//		xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/">
	//		<fppq-req-ext:FirearmPurchaseProhibitionSystemIdentification>
	//			<nc:IdentificationID>System Record ID</nc:IdentificationID>
	//			<nc:SystemName>System Name</nc:SystemName>
	//		</fppq-req-ext:FirearmPurchaseProhibitionSystemIdentification>
	//	</fppq-req-doc:FirearmPurchaseProhibitionQueryRequest>
	public static Document createfirearmsPurchaseProhibition(
			DetailsRequest firearmsPurchaseProhibitionRequest) throws Exception {
		
        Document doc = OJBCXMLUtils.createDocument();       
        Element rootElement = doc.createElementNS(OjbcNamespaceContext.NS_FIREARMS_PROHIBITION_DOC, 
                "FirearmPurchaseProhibitionQueryRequest");
        doc.appendChild(rootElement);
        
        Element firearmPurchaseProhibitionSystemIdentification = XmlUtils.appendElement(rootElement, 
                OjbcNamespaceContext.NS_FIREARMS_PROHIBITION_EXT, 
                "FirearmPurchaseProhibitionSystemIdentification");
        
        Element identificationId = XmlUtils.appendElement(firearmPurchaseProhibitionSystemIdentification, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
        identificationId.setTextContent(firearmsPurchaseProhibitionRequest.getIdentificationID());
        
        Element identificationSourceText = XmlUtils.appendElement(firearmPurchaseProhibitionSystemIdentification, OjbcNamespaceContext.NS_NC_30, "SystemName");
        identificationSourceText.setTextContent(OJBCWebServiceURIs.FIREARMS_PURCHASE_PROHIBITION_QUERY);

        return doc;
	}

	public static Document createFbiPersonQueryRequest(
			DetailsRequest detailsRequest) throws Exception {
        Document document = OJBCXMLUtils.createDocument();       
        Element rootElement = document.createElementNS(NS_FBI_RECORD_REQUEST, 
                NS_PREFIX_FBI_RECORD_REQUEST 
                +":FBIRecordRequest");
        document.appendChild(rootElement); 
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_NC, OjbcNamespaceContext.NS_NC);
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_JXDM_41, OjbcNamespaceContext.NS_JXDM_41);

        Element personRecordRequestIdentification = 
        		XmlUtils.appendElement(rootElement, NS_FBI_RECORD_REQUEST, "PersonRecordRequestIdentification");
        XmlUtils.appendTextElement(personRecordRequestIdentification, NS_NC, "IdentificationID", detailsRequest.getIdentificationID());
        XmlUtils.appendTextElement(personRecordRequestIdentification, NS_NC, "IdentificationSourceText", detailsRequest.getIdentificationSourceText());
        
        Element person = XmlUtils.appendElement(rootElement, NS_JXDM_41, "Person");
        Element personAugmentation = XmlUtils.appendElement(person, NS_JXDM_41, "PersonAugmentation");
        Element personFBIIdentification = XmlUtils.appendElement(personAugmentation, NS_JXDM_41, "PersonFBIIdentification");
        XmlUtils.appendTextElement(personFBIIdentification, NS_NC, "IdentificationID", detailsRequest.getFbiId());
        
        Element rapBackSubscriptionIdentification = XmlUtils.appendElement(rootElement, NS_FBI_RECORD_REQUEST, "RapBackSubscriptionIdentification");
        XmlUtils.appendTextElement(rapBackSubscriptionIdentification, NS_NC, "IdentificationID", detailsRequest.getRapbackSubscriptionId());
        
        Element rapBackActivityNotificationIdentification = XmlUtils.appendElement(rootElement, NS_FBI_RECORD_REQUEST, "RapBackActivityNotificationIdentification");
        XmlUtils.appendTextElement(rapBackActivityNotificationIdentification, NS_NC, "IdentificationID", detailsRequest.getRapbackActivityNotificationId());
        
        return document;
	}	
    
}
