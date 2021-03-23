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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ARREST_DETAIL_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ARREST_HIDE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ARREST_MODIFY_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ARREST_UNHIDE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_AUDIT_LOG_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_AUDIT_LOG_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_CHARGE_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_DA_CHARGE_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_DA_DEFERRED_DISPO_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_DECLINE_CHARGE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_DELETE_DISPOSITION_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_EXPUNGE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_FBI_RECORD_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_FINALIZE_ARREST_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_60;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_61;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_MUNICIPAL_CHARGE_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_MUNICIPAL_DEFERRED_DISPOSITION_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_40;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ARREST_DETAIL_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ARREST_HIDE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ARREST_UNHIDE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_AUDIT_LOG_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_AUDIT_LOG_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_CHARGE_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_DA_CHARGE_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_DA_DEFERRED_DISPO_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_DECLINE_CHARGE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_DELETE_DISPOSITION_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_EXPUNGE_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_FBI_RECORD_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_FINALIZE_ARREST_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_UA_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_60;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_61;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_MODIFY_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_MUNICIPAL_CHARGE_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_MUNICIPAL_DEFERRED_DISPOSITION_SEARCH_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_40;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_RECORD_REPLICATION_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_RECORD_REPLICATION_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES_40;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XSI;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_RECORD_REPLICATION_REQUEST_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_RECORD_REPLICATION_REQUEST_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_40;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XSI;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.ArrayUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.OjbcWebConstants;
import org.ojbc.web.OjbcWebConstants.ArrestType;
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
import org.ojbc.web.portal.arrest.ArrestCharge;
import org.ojbc.web.portal.arrest.ArrestDetailSearchRequest;
import org.ojbc.web.portal.arrest.ArrestReferral;
import org.ojbc.web.portal.arrest.ArrestSearchRequest;
import org.ojbc.web.portal.arrest.ChargeReferral;
import org.ojbc.web.portal.arrest.Disposition;
import org.ojbc.web.portal.audit.AuditSearchRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class contains methods to convert POJOs to XML request documents or to 
 * take a few string arguments and make an XML request document out of them.
 *
 */
public class RequestMessageBuilderUtilities {

	private static final String AUDIT_LOG_SEARCH_REQUEST_SYSTEM_NAME = "{http://ojbc.org/Services/WSDL/AuditLogSearchRequestService/1.0}SubmitAuditLogSearchRequest";

	private static final String RESTITUTION_01 = "RESTITUTION_01";

	private static final String CHARGE_SENTENCE_01 = "CHARGE_SENTENCE_01";

	private static final String CRIMINAL_HISTORY_MODIFICATION_REQUEST = "{http://ojbc.org/Services/WSDL/CriminalHistoryModificationRequestService/1.0}SubmitCriminalHistoryModificationRequest";

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
			Element exactMatchMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, NS_STRUCTURES, SearchFieldMetadata.ExactMatch);
			personSearchRequestElement.appendChild(exactMatchMetaData);
		}	

		if (psr.getPersonSurNameMetaData() == SearchFieldMetadata.StartsWith || psr.getPersonGivenNameMetaData() == SearchFieldMetadata.StartsWith )
		{
			Element startsWithMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, NS_STRUCTURES, SearchFieldMetadata.StartsWith);
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
    
    public static final void appendDateWapper( Element parent, 
    		String elementName, String wrapperElementNS, String dateElementNS, LocalDate localDate) {
    	if (localDate != null){
    		Element dateWrapperElement = XmlUtils.appendElement(parent, wrapperElementNS, elementName);
    		Element date = XmlUtils.appendElement(dateWrapperElement, dateElementNS, "Date");
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

	public static Document createNsorCheckResultsQueryRequest(String transactionNumber) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC, 
                NS_PREFIX_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC 
                +":OrganizationIdentificationNsorQueryRequest");
        document.appendChild(rootElement);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC, 
        		NS_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_REQUEST_DOC);
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
	
	public static Document createArrestSearchRequest(ArrestSearchRequest arrestSearchRequest) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = null;
        switch (arrestSearchRequest.getArrestType()) {
        case MUNI: 
        	if (BooleanUtils.isNotTrue(arrestSearchRequest.getArrestWithDeferredDispositions())) {
        		rootElement = document.createElementNS(NS_MUNICIPAL_CHARGE_SEARCH_REQUEST_DOC, 
        			NS_PREFIX_MUNICIPAL_CHARGE_SEARCH_REQUEST_DOC + ":MunicipalChargesSearchRequest");
        	}
        	else {
        		rootElement = document.createElementNS(NS_MUNICIPAL_DEFERRED_DISPOSITION_SEARCH_REQUEST_DOC, 
        				NS_PREFIX_MUNICIPAL_DEFERRED_DISPOSITION_SEARCH_REQUEST_DOC + ":MunicipalDeferredDispositionSearchRequest");
        	}
        	break; 
        case DA: 
        	if (BooleanUtils.isNotTrue(arrestSearchRequest.getArrestWithDeferredDispositions())) {
	        	rootElement = document.createElementNS(NS_DA_CHARGE_SEARCH_REQUEST_DOC, 
	        			NS_PREFIX_DA_CHARGE_SEARCH_REQUEST_DOC + ":DAChargesSearchRequest");
        	}
        	else {
        		rootElement = document.createElementNS(NS_DA_DEFERRED_DISPO_SEARCH_REQUEST_DOC, 
        				NS_PREFIX_DA_DEFERRED_DISPO_SEARCH_REQUEST_DOC + ":DADeferredDispositionSearchRequest");
        	}
        	break;
		default:
			break; 
        }
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_STRUCTURES_40, NS_STRUCTURES_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);

        XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeOnlyAdminOwnedChargesIndicator", 
			BooleanUtils.toString(arrestSearchRequest.getIncludeOnlyAdminOwnedCharges(), "true", "false", "false"));

        if (arrestSearchRequest.getPendingChargeIndicators() != null && arrestSearchRequest.getPendingChargeIndicators().size()>0){
        	for (String indicator: arrestSearchRequest.getPendingChargeIndicators()) {
        		switch (indicator) {
        		case "deferredDisposition":
        	        XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeDeferredDispositionIndicator", "true");
        			break; 
        		case "filedCharges": 
        	        XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeFiledChargesIndicator", "true");
        			break;
        		case "mentalHealthCourt": 
        	        XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeMentalHealthCourtIndicator", "true");
        			break;
        		case "drugCourt": 
        			XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeDrugCourtIndicator", "true");
        			break;
    			default:
        		}
        	}
        }
        
        if (arrestSearchRequest.isNotEmpty()) {
        	Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest");
        	
        	if (StringUtils.isNotBlank(arrestSearchRequest.getArrestIdentification())) {
        		Element activityIdentification = XmlUtils.appendElement(arrest, NS_NC_40, "ActivityIdentification"); 
        		XmlUtils.appendTextElement(activityIdentification, NS_NC_40, "IdentificationID", arrestSearchRequest.getArrestIdentification());
        	}
        	
        	if (arrestSearchRequest.getArrestDateRangeStartDate() != null || arrestSearchRequest.getArrestDateRangeEndDate() != null) {
        		Element activityDate = XmlUtils.appendElement(arrest, NS_NC_40, "ActivityDate");
        		Element dateRange = XmlUtils.appendElement(activityDate, NS_NC_40, "DateRange");
        		
        		if (arrestSearchRequest.getArrestDateRangeStartDate() != null) {
        			Element startDate = XmlUtils.appendElement(dateRange, NS_NC_40, "StartDate");
        			XmlUtils.appendTextElement(startDate, NS_NC_40, "Date", arrestSearchRequest.getArrestDateRangeStartDate().toString());
        		}
        		if (arrestSearchRequest.getArrestDateRangeEndDate() != null) {
        			Element endDate = XmlUtils.appendElement(dateRange, NS_NC_40, "EndDate");
        			XmlUtils.appendTextElement(endDate, NS_NC_40, "Date", arrestSearchRequest.getArrestDateRangeEndDate().toString());
        		}
        	}
//    	<j:ArrestAgency>
//			<j:OrganizationAugmentation>
//				<j:OrganizationORIIdentification>
//					<nc:IdentificationID>ORI12345</nc:IdentificationID>
//				</j:OrganizationORIIdentification>
//			</j:OrganizationAugmentation>
//		</j:ArrestAgency>
        	if (arrestSearchRequest.getUserSelectedOris() != null && arrestSearchRequest.getUserSelectedOris().size() > 0) {
        		for (String ori: arrestSearchRequest.getUserSelectedOris()) {
        			Element arrestAgency = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgency");
        			Element organizationAugmentation = XmlUtils.appendElement(arrestAgency, NS_JXDM_60, "OrganizationAugmentation");
        			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_60, "OrganizationORIIdentification");
        			XmlUtils.appendTextElement(organizationORIIdentification, NS_NC_40, "IdentificationID", ori);
        		}
        	}
        	else if (arrestSearchRequest.getAuthorizedOris() != null && arrestSearchRequest.getAuthorizedOris().size() > 0) {
        		for (String ori: arrestSearchRequest.getAuthorizedOris()) {
        			Element arrestAgency = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgency");
        			Element organizationAugmentation = XmlUtils.appendElement(arrestAgency, NS_JXDM_60, "OrganizationAugmentation");
        			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_60, "OrganizationORIIdentification");
        			XmlUtils.appendTextElement(organizationORIIdentification, NS_NC_40, "IdentificationID", ori);
        		}
        	}
        	
        	if (arrestSearchRequest.isSubjectInfoNotEmpty()) {
        		Element arrestSubject = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestSubject"); 
        		XmlUtils.addAttribute(arrestSubject, NS_STRUCTURES_40, "ref", "Subject_01");
        		Element subject = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Subject");
        		XmlUtils.addAttribute(subject, NS_STRUCTURES_40, "id", "Subject_01");
        		
        		if (arrestSearchRequest.isPersonInfoNotEmpty()) {
        			Element roleOfPerson = XmlUtils.appendElement(subject, NS_NC_40, "RoleOfPerson");
        			XmlUtils.addAttribute(roleOfPerson, NS_STRUCTURES_40, "ref", "Person_01");
        			
        			Element person = XmlUtils.appendElement(rootElement, NS_NC_40, "Person"); 
        			XmlUtils.addAttribute(person, NS_STRUCTURES_40, "id", "Person_01");
        			
        			if (arrestSearchRequest.getDob() != null) {
        				Element personBirthDate = XmlUtils.appendElement(person, NS_NC_40, "PersonBirthDate");
        				XmlUtils.appendTextElement(personBirthDate, NS_NC_40, "Date", arrestSearchRequest.getDob().toString());
        			}
        			
        			if (StringUtils.isNotBlank(arrestSearchRequest.getFirstName()) ||
        					StringUtils.isNotBlank(arrestSearchRequest.getLastName())) {
        				Element personName = XmlUtils.appendElement(person, NS_NC_40, "PersonName");
        				if (StringUtils.isNotBlank(arrestSearchRequest.getFirstName())) {
        					Element personGivenName = XmlUtils.appendElement(personName, NS_NC_40, "PersonGivenName");
        					personGivenName.setTextContent(arrestSearchRequest.getFirstName());
        					XmlUtils.addAttribute(personGivenName, NS_STRUCTURES_40, "metadata", 
        							NIEMXMLUtils.getMetaDataId(arrestSearchRequest.getFirstNameSearchMetadata()));
        				}
        				if (StringUtils.isNotBlank(arrestSearchRequest.getLastName())) {
        					Element personSurName = XmlUtils.appendElement(personName, NS_NC_40, "PersonSurName");
        					personSurName.setTextContent(arrestSearchRequest.getLastName());
        					XmlUtils.addAttribute(personSurName, NS_STRUCTURES_40, "metadata", 
        							NIEMXMLUtils.getMetaDataId(arrestSearchRequest.getLastNameSearchMetadata()));
        				}
        			}
        			
        			if (StringUtils.isNotBlank(arrestSearchRequest.getSsn())){
        				Element personSSNIdentification = XmlUtils.appendElement(person, NS_NC_40, "PersonSSNIdentification");
        				XmlUtils.appendTextElement(personSSNIdentification, NS_NC_40, "IdentificationID", arrestSearchRequest.getSsn());
        			}
        		}
        		
        		if (StringUtils.isNotBlank(arrestSearchRequest.getOtn())) {
        			Element subjectIdentification = XmlUtils.appendElement(subject, NS_JXDM_60, "SubjectIdentification"); 
        			XmlUtils.appendTextElement(subjectIdentification, NS_NC_40, "IdentificationID", arrestSearchRequest.getOtn());
        		}
        		
        	}
            XmlUtils.appendTextElement(arrest, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "IncludeHiddenArrestIndicator", 
    			BooleanUtils.toString(arrestSearchRequest.getIncludeHiddenArrestIndicator(), "true", "false", "false"));

        	if (BooleanUtils.isTrue(arrestSearchRequest.getArrestWithDeferredDispositions()) 
        			&& (arrestSearchRequest.getDispositionDateRangeStartDate() != null 
        				|| arrestSearchRequest.getDispositionDateRangeEndDate()!= null)) {
        		Element disposition = XmlUtils.appendElement(rootElement, NS_NC_40, "Disposition");
        		Element dispositionDate = XmlUtils.appendElement(disposition, NS_NC_40, "DispositionDate");
        		Element dateRange = XmlUtils.appendElement(dispositionDate, NS_NC_40, "DateRange");
        		
        		if (arrestSearchRequest.getDispositionDateRangeStartDate() != null) {
        			Element startDate = XmlUtils.appendElement(dateRange, NS_NC_40, "StartDate");
        			XmlUtils.appendTextElement(startDate, NS_NC_40, "Date", arrestSearchRequest.getDispositionDateRangeStartDate().toString());
        		}
        		if (arrestSearchRequest.getDispositionDateRangeEndDate() != null) {
        			Element endDate = XmlUtils.appendElement(dateRange, NS_NC_40, "EndDate");
        			XmlUtils.appendTextElement(endDate, NS_NC_40, "Date", arrestSearchRequest.getDispositionDateRangeEndDate().toString());
        		}
        	}
//        		<chsreq-ext:SourceSystemNameText>CH1</chsreq-ext:SourceSystemNameText>
        	XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SourceSystemNameText", 
        			"{http://ojbc.org/Services/WSDL/CriminalHistorySearchRequestService/1.0}SubmitCriminalHistorySearchRequest");
        	Element exactMatchMetaData = XmlUtils.appendElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SearchMetadata");
        	XmlUtils.addAttribute(exactMatchMetaData, NS_STRUCTURES_40, "id", "SM001");
        	XmlUtils.appendTextElement(exactMatchMetaData, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SearchQualifierCode", SearchFieldMetadata.ExactMatch.getMetadata());
        	
        	Element startsWithMetaData = XmlUtils.appendElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SearchMetadata");
        	XmlUtils.addAttribute(startsWithMetaData, NS_STRUCTURES_40, "id", "SM002");
        	XmlUtils.appendTextElement(startsWithMetaData, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SearchQualifierCode", SearchFieldMetadata.StartsWith.getMetadata());
        	
        }
		return document;
	}
//<adsreq-doc:ArrestDetailSearchRequest
//	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/ArrestDetailSearchRequest/1.0 ../xsd/ArrestDetail_search_request.xsd"
//	xmlns:adsreq-doc="http://ojbc.org/IEPD/Exchange/ArrestDetailSearchRequest/1.0"
//	xmlns:chsreq-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchRequestExtension/1.0"
//	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
//	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
//	<j:Arrest>
//		<nc:ActivityIdentification>
//			<nc:IdentificationID>A34567890</nc:IdentificationID>
//		</nc:ActivityIdentification>
//	</j:Arrest>
//</adsreq-doc:ArrestDetailSearchRequest>
	public static Document createArrestDetailSearchRequest(ArrestDetailSearchRequest arrestDetailSearchRequest) throws Exception {
		if (arrestDetailSearchRequest == null) return null;
		
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ARREST_DETAIL_SEARCH_REQUEST_DOC, 
        		NS_PREFIX_ARREST_DETAIL_SEARCH_REQUEST_DOC + ":ArrestDetailSearchRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ARREST_DETAIL_SEARCH_REQUEST_DOC, 
        		NS_ARREST_DETAIL_SEARCH_REQUEST_DOC);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);
    
    	Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest");
    	
		Element activityIdentification = XmlUtils.appendElement(arrest, NS_NC_40, "ActivityIdentification"); 
		XmlUtils.appendTextElement(activityIdentification, NS_NC_40, "IdentificationID", arrestDetailSearchRequest.getArrestIdentification());
		
        for (String chargeId : arrestDetailSearchRequest.getChargeIds()) {
            Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
            Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, 
            		NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "ChargePrimarySystemIdentification");
            XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeId);
        }

//    		<chsreq-ext:SourceSystemNameText>CH1</chsreq-ext:SourceSystemNameText>
		XmlUtils.appendTextElement(rootElement, NS_CRIMINAL_HISTORY_SEARCH_REQUEST_EXT, "SourceSystemNameText", 
			"{http://ojbc.org/Services/WSDL/CriminalHistorySearchRequestService/1.0}SubmitCriminalHistorySearchRequest");
		
		
        return document;
	}
//	<amr-req-doc:ArrestModifyRequest
//	xmlns:amr-req-doc="http://ojbc.org/IEPD/Exchange/ArrestModifyRequest/1.0"
//	xmlns:chm-req-ext="http://ojbc.org/IEPD/Extensions/CriminalHistoryModificationRequest/1.0"
//	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
//	xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
//	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/ArrestModifyRequest/1.0 ../xsd/arrest_modify_request.xsd ">
//	<j:Arrest>
//		<j:ArrestAgencyRecordIdentification>
//			<nc:IdentificationID>1004233</nc:IdentificationID>
//			<nc:IdentificationSourceText>System</nc:IdentificationSourceText>
//		</j:ArrestAgencyRecordIdentification>
//		<j:ArrestCharge>
//			<j:ChargeDisposition>
//				<nc:DispositionDate>
//					<nc:Date>2016-06-15</nc:Date>
//				</nc:DispositionDate>
//				<chm-req-ext:DispositionIdentification>
//					<nc:IdentificationID>D96487</nc:IdentificationID>
//				</chm-req-ext:DispositionIdentification>
//				<chm-req-ext:AmendedCharge>
//					<j:ChargeDescriptionText>POSSESSION OF PARAPHERNALIA</j:ChargeDescriptionText>
//					<chm-req-ext:ChargeMunicipalCodeText>4567-8</chm-req-ext:ChargeMunicipalCodeText>
//					<chm-req-ext:ChargeMunicipalCodeDescriptionText>Possession of paraph</chm-req-ext:ChargeMunicipalCodeDescriptionText>
//				</chm-req-ext:AmendedCharge>
//				<chm-req-ext:FiledCharge>
//					<j:ChargeDescriptionText>POSSESSION OF PARAPHERNALIA</j:ChargeDescriptionText>
//					<chm-req-ext:ChargeMunicipalCodeText>4567-8</chm-req-ext:ChargeMunicipalCodeText>
//					<chm-req-ext:ChargeMunicipalCodeDescriptionText>Possession of paraph</chm-req-ext:ChargeMunicipalCodeDescriptionText>
//				</chm-req-ext:FiledCharge>
//				<chm-req-ext:DispositionCodeText>389</chm-req-ext:DispositionCodeText>
//				<chm-req-ext:DispositionCodeDescriptionText>Disposition Code Description</chm-req-ext:DispositionCodeDescriptionText>
//				<chm-req-ext:DispositionProvisionText>Provision</chm-req-ext:DispositionProvisionText>
//				<chm-req-ext:DispositionStatusCode>Modified</chm-req-ext:DispositionStatusCode>
//				<chm-req-ext:DispositionDismissalReasonText>Reason for disposition dismissal</chm-req-ext:DispositionDismissalReasonText>
//				<chm-req-ext:DispositionDismissalReasonCodeText>DISCODE</chm-req-ext:DispositionDismissalReasonCodeText>
//			</j:ChargeDisposition>
//			<j:ChargeSentence structures:id="csentence_01">
//				<j:SentenceDeferredTerm>
//					<nc:ActivityDate>
//						<nc:Date>2001-12-31</nc:Date>
//					</nc:ActivityDate>
//					<j:TermDuration>P2Y1D</j:TermDuration>
//				</j:SentenceDeferredTerm>
//				<j:SentenceSuspendedTerm>
//					<j:TermDuration>P4Y6D</j:TermDuration>
//				</j:SentenceSuspendedTerm>
//				<j:SentenceTerm>
//					<nc:ActivityCategoryText>JAIL</nc:ActivityCategoryText>
//					<j:TermDuration>P4Y6D</j:TermDuration>
//				</j:SentenceTerm>
//				<j:SupervisionFineAmount>
//					<nc:Amount>200.00</nc:Amount>
//				</j:SupervisionFineAmount>
//				<chm-req-ext:FineSuspendedAmount>
//					<nc:Amount>100.00</nc:Amount>
//				</chm-req-ext:FineSuspendedAmount>
//				<chm-req-ext:AlternateSentenceCodeText>Alternate Sentence Code</chm-req-ext:AlternateSentenceCodeText>
//				<chm-req-ext:AlternateSentenceCodeDescriptionText>Alternate Sentence Code Description
//				</chm-req-ext:AlternateSentenceCodeDescriptionText>
//			</j:ChargeSentence>
//			<chm-req-ext:ChargeStatusCode>Modified</chm-req-ext:ChargeStatusCode>
//			<chm-req-ext:ChargePrimarySystemIdentification>
//				<nc:IdentificationID>C4567893</nc:IdentificationID>
//			</chm-req-ext:ChargePrimarySystemIdentification>
//		</j:ArrestCharge>
//		<j:ArrestCharge>
//			<j:ChargeDisposition>
//				<nc:DispositionDate>
//					<nc:Date>2016-06-15</nc:Date>
//				</nc:DispositionDate>
//				<chm-req-ext:DispositionIdentification>
//					<nc:IdentificationID>D1234</nc:IdentificationID>
//				</chm-req-ext:DispositionIdentification>
//				<chm-req-ext:AmendedCharge>
//					<j:ChargeDescriptionText>POSSESSION OF MARIJUANA</j:ChargeDescriptionText>
//					<chm-req-ext:ChargeMunicipalCodeText>4567-8</chm-req-ext:ChargeMunicipalCodeText>
//					<chm-req-ext:ChargeMunicipalCodeDescriptionText>Possession of marijuana</chm-req-ext:ChargeMunicipalCodeDescriptionText>
//				</chm-req-ext:AmendedCharge>
//				<chm-req-ext:FiledCharge>
//					<j:ChargeDescriptionText>POSSESSION OF MARIJUANA</j:ChargeDescriptionText>
//					<chm-req-ext:ChargeMunicipalCodeText>4567-9</chm-req-ext:ChargeMunicipalCodeText>
//					<chm-req-ext:ChargeMunicipalCodeDescriptionText>Possession of marijuana</chm-req-ext:ChargeMunicipalCodeDescriptionText>
//				</chm-req-ext:FiledCharge>
//				<chm-req-ext:DispositionCodeText>390</chm-req-ext:DispositionCodeText>
//				<chm-req-ext:DispositionCodeDescriptionText>Disposition Code Description</chm-req-ext:DispositionCodeDescriptionText>
//				<chm-req-ext:DispositionProvisionText>Provision</chm-req-ext:DispositionProvisionText>
//				<chm-req-ext:DispositionStatusCode>Modified</chm-req-ext:DispositionStatusCode>
//				<chm-req-ext:DispositionDismissalReasonText>Reason for disposition dismissal</chm-req-ext:DispositionDismissalReasonText>
//				<chm-req-ext:DispositionDismissalReasonCodeText>DISCODE</chm-req-ext:DispositionDismissalReasonCodeText>
//			</j:ChargeDisposition>
//			<j:ChargeSentence>
//				<j:SentenceDeferredTerm>
//					<nc:ActivityDate>
//						<nc:Date>2001-12-31</nc:Date>
//					</nc:ActivityDate>
//					<j:TermDuration>P2Y1D</j:TermDuration>
//				</j:SentenceDeferredTerm>
//				<j:SentenceSuspendedTerm>
//					<j:TermDuration>P4Y6D</j:TermDuration>
//				</j:SentenceSuspendedTerm>
//				<j:SentenceTerm>
//					<nc:ActivityCategoryText>JAIL</nc:ActivityCategoryText>
//					<j:TermDuration>P4Y6D</j:TermDuration>
//				</j:SentenceTerm>
//				<j:SupervisionFineAmount>
//					<nc:Amount>200.00</nc:Amount>
//				</j:SupervisionFineAmount>
//				<chm-req-ext:FineSuspendedAmount>
//					<nc:Amount>100.00</nc:Amount>
//				</chm-req-ext:FineSuspendedAmount>
//				<chm-req-ext:AlternateSentenceCodeText>Alternate Sentence</chm-req-ext:AlternateSentenceCodeText>
//				<chm-req-ext:AlternateSentenceCodeDescriptionText>Alternate Sentence Code Description
//				</chm-req-ext:AlternateSentenceCodeDescriptionText>
//			</j:ChargeSentence>
//			<chm-req-ext:ChargeStatusCode>Modified</chm-req-ext:ChargeStatusCode>
//			<chm-req-ext:ChargePrimarySystemIdentification>
//				<nc:IdentificationID>C13245</nc:IdentificationID>
//			</chm-req-ext:ChargePrimarySystemIdentification>
//		</j:ArrestCharge>
//		<chm-req-ext:ArrestPrimarySystemIdentification>
//			<nc:IdentificationID>A3245667</nc:IdentificationID>
//		</chm-req-ext:ArrestPrimarySystemIdentification>
//	</j:Arrest>
//	<j:Restitution structures:id="restitution_01">
//		<nc:ObligationDueAmount>
//			<nc:Amount>500.00</nc:Amount>
//		</nc:ObligationDueAmount>
//	</j:Restitution>
//	<j:ActivityObligationAssociation>
//		<nc:Activity structures:ref="csentence_01" />
//		<nc:Obligation structures:ref="restitution_01" />
//	</j:ActivityObligationAssociation>
//</amr-req-doc:ArrestModifyRequest>
	
	
	public static Document createArrestModifyRequest(Disposition disposition) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ARREST_MODIFY_REQUEST_DOC, 
        		NS_PREFIX_MODIFY_REQUEST_DOC + ":ArrestModifyRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_MODIFY_REQUEST_DOC, 
        		NS_ARREST_MODIFY_REQUEST_DOC);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_STRUCTURES_40, NS_STRUCTURES_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);
    
        Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest");
        Element arrestAgencyRecordIdentification = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgencyRecordIdentification");
        XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationID", disposition.getArrestIdentification());
        XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationSourceText", CRIMINAL_HISTORY_MODIFICATION_REQUEST);
        
        Element arrestCharge = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
        Element chargeDisposition = XmlUtils.appendElement(arrestCharge, NS_JXDM_60, "ChargeDisposition");
        
        if (disposition.getDispositionDate() != null) {
	        Element dispositionDate = XmlUtils.appendElement(chargeDisposition, NS_NC_40, "DispositionDate"); 
	        XmlUtils.appendTextElement(dispositionDate, NS_NC_40, "Date", disposition.getDispositionDate().toString());
        }
        
        if (StringUtils.isNotBlank(disposition.getDispositionIdentification())) {
        	Element dispositionIdentification = 
        			XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "DispositionIdentification");
        	XmlUtils.appendTextElement(dispositionIdentification, NS_NC_40, "IdentificationID", disposition.getDispositionIdentification());
        }
        
        if (StringUtils.isNotBlank(disposition.getAmendedCharge()) || StringUtils.isNotBlank(disposition.getAmendedChargeLiteral())) {
        	Element amendedCharge = XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "AmendedCharge");
        	
        	if (StringUtils.isNotBlank(disposition.getAmendedChargeLiteral())) {
        		XmlUtils.appendTextElement(amendedCharge, NS_JXDM_60, "ChargeDescriptionText", disposition.getAmendedChargeLiteral());
        	}
        	if (disposition.getDispositionType() == ArrestType.MUNI) {
	        	XmlUtils.appendTextElement(amendedCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
	        			"ChargeMunicipalCodeText", disposition.getAmendedCharge());
	        	XmlUtils.appendTextElement(amendedCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
	        			"ChargeMunicipalCodeDescriptionText", disposition.getAmendedChargeDescription());
        	}
        	else if (disposition.getDispositionType() == ArrestType.DA) {
        		XmlUtils.appendTextElement(amendedCharge, NS_JXDM_60, 
        				"ChargeSeverityText", disposition.getAmendedChargeSeverityCode());
        		createChargeStatuteElement(disposition.getAmendedCharge(), disposition.getAmendedChargeDescription(), amendedCharge);
        	}
        }
        
    	Element filedCharge = XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "FiledCharge");
    	
    	if (StringUtils.isNotBlank(disposition.getFiledChargeLiteral())) {
    		XmlUtils.appendTextElement(filedCharge, NS_JXDM_60, "ChargeDescriptionText", disposition.getFiledChargeLiteral());
    	}
    	if (disposition.getDispositionType() == ArrestType.MUNI) {
	    	XmlUtils.appendTextElement(filedCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
	    			"ChargeMunicipalCodeText", disposition.getFiledCharge());
	    	XmlUtils.appendTextElement(filedCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
	    			"ChargeMunicipalCodeDescriptionText", disposition.getFiledChargeDescription());
    	}    	
    	else if (disposition.getDispositionType() == ArrestType.DA) {
    		XmlUtils.appendTextElement(filedCharge, NS_JXDM_60, 
    				"ChargeSeverityText", disposition.getChargeSeverityCode());
    		createChargeStatuteElement(disposition.getFiledCharge(), disposition.getFiledChargeDescription(), filedCharge);
    	}
    	
    	if (StringUtils.isNotBlank(disposition.getCourtCaseNumber()) || 
    			StringUtils.isNotBlank(disposition.getMuniCourtCaseNumber())) {
    		Element courtCase = XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "CourtCase");
    		Element activityIdentification = XmlUtils.appendElement(courtCase, NS_NC_40, "ActivityIdentification");
    		Element daCourtCaseIndicator = XmlUtils.appendElement(courtCase, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "DACourtCaseIndicator"); 
    		if (StringUtils.isNotBlank(disposition.getCourtCaseNumber())) {
    			XmlUtils.appendTextElement(activityIdentification, NS_NC_40, "IdentificationID", disposition.getCourtCaseNumber());
    			daCourtCaseIndicator.setTextContent("true");
    		}
    		else {
    			XmlUtils.appendTextElement(activityIdentification, NS_NC_40, "IdentificationID", disposition.getMuniCourtCaseNumber());
    			daCourtCaseIndicator.setTextContent("false");
    		}
    	}
    	
    	XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    			"DispositionCodeText", disposition.getDispositionCode());
    	XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    			"DispositionCodeDescriptionText", disposition.getDispositionDescription());
    	
    	XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    			"DispositionProvisionText", disposition.getProvisionCode());
    	
    	if (StringUtils.isNotBlank(disposition.getDispositionIdentification())) {
    		XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    				"DispositionStatusCode", "Modified");
    	}
    	else {
    		XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    				"DispositionStatusCode", "New");
    	}
    	
    	if (StringUtils.isNotBlank(disposition.getReasonForDismissal())) {
    		XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    				"DispositionDismissalReasonText", disposition.getReasonForDismissalDescripiton());
    		XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
    				"DispositionDismissalReasonCodeText", disposition.getReasonForDismissal());
    	}

    	XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "DispositionChargeCountQuantity", disposition.getCounts().toString());
    	XmlUtils.appendTextElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ChargeModificationDescriptionCodeText", disposition.getGeneralOffenseCode());
    	if (disposition.containsSentenceInfo()) {
    		Element chargeSentence = XmlUtils.appendElement(arrestCharge, NS_JXDM_60, "ChargeSentence");
    		XmlUtils.addAttribute(chargeSentence, NS_STRUCTURES_40, "id", CHARGE_SENTENCE_01);
    		
    		if (ArrayUtils.hasPositiveValue(disposition.getDeferredDays(), disposition.getDeferredYears())) {
    			Element sentenceDeferredTerm = XmlUtils.appendElement(chargeSentence, NS_JXDM_60, "SentenceDeferredTerm");
    			String termDurationString = getTermDurationString(disposition.getDeferredYears(), disposition.getDeferredDays());
    			XmlUtils.appendTextElement(sentenceDeferredTerm, NS_JXDM_60, "TermDuration", termDurationString);
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays(), disposition.getSuspendedYears())) {
    			Element sentenceSuspendedTerm = XmlUtils.appendElement(chargeSentence, NS_JXDM_60, "SentenceSuspendedTerm");
    			String termDurationString = getTermDurationString(disposition.getSuspendedYears(), disposition.getSuspendedDays());
    			XmlUtils.appendTextElement(sentenceSuspendedTerm, NS_JXDM_60, "TermDuration", termDurationString);
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getJailYears(), disposition.getJailDays())) {
    			Element sentenceTerm = XmlUtils.appendElement(chargeSentence, NS_JXDM_60, "SentenceTerm");
    			XmlUtils.appendTextElement(sentenceTerm, NS_NC_40, "ActivityCategoryText", "JAIL");
    			String termDurationString = getTermDurationString(disposition.getJailYears(), disposition.getJailDays());
    			XmlUtils.appendTextElement(sentenceTerm, NS_JXDM_60, "TermDuration", termDurationString);
    		}
    		
    		if (ArrayUtils.hasPositiveValue(disposition.getPrisonYears(), disposition.getPrisonDays())) {
    			Element sentenceTerm = XmlUtils.appendElement(chargeSentence, NS_JXDM_60, "SentenceTerm");
    			XmlUtils.appendTextElement(sentenceTerm, NS_NC_40, "ActivityCategoryText", "PRISON");
    			String termDurationString = getTermDurationString(disposition.getPrisonYears(), disposition.getPrisonDays());
    			XmlUtils.appendTextElement(sentenceTerm, NS_JXDM_60, "TermDuration", termDurationString);
    		}
    		
    		if (ArrayUtils.hasPositiveValue(disposition.getFineAmount())) {
    			Element supervisionFineAmount = XmlUtils.appendElement(chargeSentence, NS_JXDM_60, "SupervisionFineAmount");
    			XmlUtils.appendTextElement(supervisionFineAmount, NS_NC_40, "Amount", disposition.getFineAmount().toString());
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getFineSuspended())) {
    			Element fineSuspendedAmount = XmlUtils.appendElement(chargeSentence, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "FineSuspendedAmount");
    			XmlUtils.appendTextElement(fineSuspendedAmount, NS_NC_40, "Amount", disposition.getFineSuspended().toString());
    		}
    		
    		if (disposition.getAlternateSentences() != null)
    		{	
	    		disposition.getAlternateSentences().forEach(code->{
	    			Element alternateSentence = XmlUtils.appendElement(chargeSentence, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "AlternateSentence");
	    			XmlUtils.appendTextElement(alternateSentence, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
	    					"AlternateSentenceCodeText", code);
	    		});
    		}
    	}

    	XmlUtils.appendTextElement(arrestCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ChargeStatusCode", "Unchanged");
    	Element chargePrimarySystemIdentification = 
    			XmlUtils.appendElement(arrestCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ChargePrimarySystemIdentification");
    	XmlUtils.appendTextElement(chargePrimarySystemIdentification, NS_NC_40, "IdentificationID", disposition.getArrestChargeIdentification());

    	if (ArrayUtils.hasPositiveValue(disposition.getRestitution())) {
    		Element restitution = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Restitution");
    		XmlUtils.addAttribute(restitution, NS_STRUCTURES_40, "id", RESTITUTION_01);
    		Element obligationDueAmount = XmlUtils.appendElement(restitution, NS_NC_40, "ObligationDueAmount");
    		XmlUtils.appendTextElement(obligationDueAmount, NS_NC_40, "Amount", disposition.getRestitution().toString());
    		
    		Element activityObligationAssociation = XmlUtils.appendElement(rootElement, 
    				NS_JXDM_60, "ActivityObligationAssociation");
    		Element activity = XmlUtils.appendElement(activityObligationAssociation, NS_NC_40, "Activity");
    		XmlUtils.addAttribute(activity, NS_STRUCTURES_40, "ref", CHARGE_SENTENCE_01);
    		Element obligation = XmlUtils.appendElement(activityObligationAssociation, NS_NC_40, "Obligation");
    		XmlUtils.addAttribute(obligation, NS_STRUCTURES_40, "ref", RESTITUTION_01); 
    	}
    	
		return document;
	}

	private static void createChargeStatuteElement(String statuteCode, String statuteCodeDscription, Element parent) {
		Element chargeStatute = XmlUtils.appendElement(parent, NS_JXDM_60, "ChargeStatute");
		Element statuteCodeIdentification = XmlUtils.appendElement(chargeStatute, NS_JXDM_60, "StatuteCodeIdentification");
		XmlUtils.appendTextElement(statuteCodeIdentification, NS_NC_40, "IdentificationID", statuteCode);
		XmlUtils.appendTextElement(chargeStatute, NS_JXDM_60, "StatuteDescriptionText", statuteCodeDscription);
	}

	private static String getTermDurationString(Integer years, Integer days) {
		return "P"+ (Objects.isNull(years)? "0":years.toString()) 
				+ "Y"+ (Objects.isNull(days)?"0":days.toString()) + "D";
	}


	public static Document createArrestHideRequest(String id, List<String> chargeIds) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ARREST_HIDE_REQUEST_DOC, 
        		NS_PREFIX_ARREST_HIDE_REQUEST_DOC + ":ArrestHideRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ARREST_HIDE_REQUEST_DOC, 
        		NS_ARREST_HIDE_REQUEST_DOC);
        
        Element arrest = createArrestModifyRequestArrestElement(id, document, rootElement);
		Element arrestHideDate = XmlUtils.appendElement(arrest, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ArrestHideDate");
		for (String chargeId : chargeIds) {
			Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
			Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_60, "ChargeIdentification");
			XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeId);
		}
		XmlUtils.appendTextElement(arrestHideDate, NS_NC_40, "Date", LocalDate.now().toString());
		XmlUtils.appendTextElement(arrest, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ArrestHideIndicator", "true");
        

        return document;
	}

	private static Element createArrestModifyRequestArrestElement(String id, Document document, Element rootElement) {
		rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);
    
    	Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest");
    	
		Element arrestAgencyRecordIdentification = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgencyRecordIdentification"); 
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationID", id);
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationSourceText", CRIMINAL_HISTORY_MODIFICATION_REQUEST);		
        return arrest;
	}
//<dd-req-doc:DeleteDispositionRequest
//	xmlns:dd-req-doc="http://ojbc.org/IEPD/Exchange/DeleteDispositionRequest/1.0"
//	xmlns:chm-req-ext="http://ojbc.org/IEPD/Extensions/CriminalHistoryModificationRequest/1.0"
//	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
//	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
//	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/DeleteDispositionRequest/1.0 ../xsd/delete_disposition_request.xsd">
//	<j:Arrest>
//		<j:ArrestAgencyRecordIdentification>
//			<nc:IdentificationID>25052</nc:IdentificationID>
//			<nc:IdentificationSourceText>System</nc:IdentificationSourceText>
//		</j:ArrestAgencyRecordIdentification>
//		<j:ArrestCharge>
//			<j:ChargeDisposition>
//				<chm-req-ext:DispositionIdentification>
//					<nc:IdentificationID>48514</nc:IdentificationID>
//				</chm-req-ext:DispositionIdentification>
//			</j:ChargeDisposition>
//			<chm-req-ext:ChargePrimarySystemIdentification>
//				<nc:IdentificationID>5866299</nc:IdentificationID>
//			</chm-req-ext:ChargePrimarySystemIdentification>
//		</j:ArrestCharge>
//	</j:Arrest>
//</dd-req-doc:DeleteDispositionRequest>
	public static Document createDeleteDispositionRequest(Disposition disposition) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_DELETE_DISPOSITION_REQUEST_DOC, 
        		NS_PREFIX_DELETE_DISPOSITION_REQUEST_DOC + ":DeleteDispositionRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_DELETE_DISPOSITION_REQUEST_DOC, 
        		NS_DELETE_DISPOSITION_REQUEST_DOC);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);
        
    	Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest");
    	
		Element arrestAgencyRecordIdentification = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgencyRecordIdentification"); 
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationID", disposition.getArrestIdentification());
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationSourceText", CRIMINAL_HISTORY_MODIFICATION_REQUEST);

		Element arrestCharge = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
		Element chargeDisposition = XmlUtils.appendElement(arrestCharge, NS_JXDM_60, "ChargeDisposition");
		Element dispositionIdentification = XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "DispositionIdentification"); 
		XmlUtils.appendTextElement(dispositionIdentification, NS_NC_40, "IdentificationID", disposition.getDispositionIdentification());
		
		Element chargePrimarySystemIdentification = XmlUtils.appendElement(arrestCharge, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ChargePrimarySystemIdentification"); 
		XmlUtils.appendTextElement(chargePrimarySystemIdentification, NS_NC_40, "IdentificationID", disposition.getArrestChargeIdentification());
		
		return document;
	}

//<mpar-req-doc:MunicipalProsecutorArrestReferralRequest
//	xmlns:mpar-req-doc="http://ojbc.org/IEPD/Exchange/MunicipalProsecutorArrestReferralRequest/1.0"
//	xmlns:chm-req-ext="http://ojbc.org/IEPD/Extensions/CriminalHistoryModificationRequest/1.0"
//	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
//	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
//	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/MunicipalProsecutorArrestReferralRequest/1.0 ../xsd/municipal_prosecutor_arrest_referral_request.xsd">
//	<j:Arrest>
//		<j:ArrestAgencyRecordIdentification>
//			<nc:IdentificationID>1004233</nc:IdentificationID>
//			<nc:IdentificationSourceText>System</nc:IdentificationSourceText>
//		</j:ArrestAgencyRecordIdentification>
//	</j:Arrest>
//</mpar-req-doc:MunicipalProsecutorArrestReferralRequest>	
	public static Document createMunicipalProsecutorArrestReferralRequest(String id) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC, 
        		NS_PREFIX_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC + ":MunicipalProsecutorArrestReferralRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC, 
        		NS_MUNICIPAL_PROSECUTOR_ARREST_REFERRAL_REQUEST_DOC);
        createArrestModifyRequestArrestElement(id, document, rootElement);
		return document;

	}	
	public static Document createDistrictAttorneyArrestReferralRequest(String id) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
		Element rootElement = document.createElementNS(NS_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC, 
				NS_PREFIX_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC + ":DistrictAttorneyArrestReferralRequest");
		rootElement.setAttribute("xmlns:" + NS_PREFIX_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC, 
				NS_DISTRICT_ATTORNEY_ARREST_REFERRAL_REQUEST_DOC);
        createArrestModifyRequestArrestElement(id, document, rootElement);
		return document;
	}

	public static Document createExpungeRequest(Disposition disposition) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
		Element rootElement = document.createElementNS(NS_EXPUNGE_REQUEST_DOC, NS_PREFIX_EXPUNGE_REQUEST_DOC + ":ExpungeRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, 
        		NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_60, NS_JXDM_60);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_STRUCTURES_40, NS_STRUCTURES_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_XSI, NS_XSI);
        document.appendChild(rootElement);
        
        Element arrest = XmlUtils.appendElement(rootElement, NS_JXDM_60, "Arrest"); 
        Element arrestAgencyRecordIdentification = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestAgencyRecordIdentification");
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationID", disposition.getArrestIdentification());
		XmlUtils.appendTextElement(arrestAgencyRecordIdentification, NS_NC_40, "IdentificationSourceText", CRIMINAL_HISTORY_MODIFICATION_REQUEST);

		Element arrestCharge = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
		Element chargeDisposition = XmlUtils.appendElement(arrestCharge, NS_JXDM_60, "ChargeDisposition");
		Element dispositionDate = XmlUtils.appendElement(chargeDisposition, NS_NC_40, "nc:DispositionDate");
		XmlUtils.appendTextElement(dispositionDate, NS_NC_40, "Date", disposition.getDispositionDate().toString());
		
		Element dispositionIdentification = XmlUtils.appendElement(chargeDisposition, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "DispositionIdentification");
		XmlUtils.appendTextElement(dispositionIdentification, NS_NC_40, "IdentificationID", disposition.getDispositionIdentification());
		
		Element chargeIdentification = XmlUtils.appendElement(arrestCharge, NS_JXDM_60, "ChargeIdentification");
		XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", disposition.getArrestChargeIdentification());
		return document;
	}

	public static Document createRecordReplicationRequest(String otn) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
		Element rootElement = document.createElementNS(NS_RECORD_REPLICATION_REQUEST_DOC, NS_PREFIX_RECORD_REPLICATION_REQUEST_DOC + ":RecordReplicationRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_RECORD_REPLICATION_REQUEST_EXT, 
        		NS_RECORD_REPLICATION_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        document.appendChild(rootElement);
        
        Element recordReplicationRecordIdentification = XmlUtils.appendElement(rootElement, NS_RECORD_REPLICATION_REQUEST_EXT, "RecordReplicationRecordIdentification");
        XmlUtils.appendTextElement(recordReplicationRecordIdentification, NS_NC_40, "IdentificationSourceText", "{http://ojbc.org/Services/WSDL/RecordReplicationRequestService/1.0}SubmitRecordReplicationRequest");
        
        Element person = XmlUtils.appendElement(rootElement, NS_NC_40, "Person"); 
        Element personTrackingIdentification = XmlUtils.appendElement(person, NS_RECORD_REPLICATION_REQUEST_EXT, "PersonTrackingIdentification");
        XmlUtils.appendTextElement(personTrackingIdentification, NS_NC_40, "IdentificationID", otn);
		return document;
	}
//	<auhr-req-doc:ArrestUnhideRequest xmlns:auhr-req-doc="http://ojbc.org/IEPD/Exchange/ArrestUnhideRequest/1.0"
//			xmlns:chm-req-ext="http://ojbc.org/IEPD/Extensions/CriminalHistoryModificationRequest/1.0"
//			xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
//			xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
//			xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//			xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/ArrestUnhideRequest/1.0 ../xsd/arrest_unhide_request.xsd ">
//			<j:Arrest>
//				<j:ArrestAgencyRecordIdentification>
//					<nc:IdentificationID>1004233</nc:IdentificationID>
//					<nc:IdentificationSourceText>System</nc:IdentificationSourceText>
//				</j:ArrestAgencyRecordIdentification>
//				<chm-req-ext:ArrestUnhideDate>
//					<nc:Date>2018-07-12</nc:Date>
//				</chm-req-ext:ArrestUnhideDate>
//				<chm-req-ext:ArrestUnhideIndicator>true</chm-req-ext:ArrestUnhideIndicator>
//			</j:Arrest>
//		</auhr-req-doc:ArrestUnhideRequest>
	public static Document createArrestUnhideRequest(String id, List<String> chargeIds ) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ARREST_UNHIDE_REQUEST_DOC, 
        		NS_PREFIX_ARREST_UNHIDE_REQUEST_DOC + ":ArrestUnhideRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ARREST_UNHIDE_REQUEST_DOC, 
        		NS_ARREST_UNHIDE_REQUEST_DOC);
        Element arrest = createArrestModifyRequestArrestElement(id, document, rootElement);
        
		for (String chargeId : chargeIds) {
			Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
			Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_60, "ChargeIdentification");
			XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeId);
		}

		Element arrestUnhideDate = XmlUtils.appendElement(arrest, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ArrestUnhideDate");
		XmlUtils.appendTextElement(arrestUnhideDate, NS_NC_40, "Date", LocalDate.now().toString());
		XmlUtils.appendTextElement(arrest, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ArrestUnhideIndicator", "true");

        return document;
	}
	public static Document createAuditSearchRequest(AuditSearchRequest auditSearchRequest) throws Exception {
        Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_AUDIT_LOG_SEARCH_REQUEST_DOC, 
        		NS_PREFIX_AUDIT_LOG_SEARCH_REQUEST_DOC + ":AuditLogSearchRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_AUDIT_LOG_SEARCH_REQUEST_DOC, 
        		NS_AUDIT_LOG_SEARCH_REQUEST_DOC);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_AUDIT_LOG_SEARCH_REQUEST_EXT, 
        		NS_AUDIT_LOG_SEARCH_REQUEST_EXT);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_40, NS_NC_40);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_JXDM_61, NS_JXDM_61);
        rootElement.setAttribute("xmlns:" + NS_PREFIX_STRUCTURES_40, NS_STRUCTURES_40);
        document.appendChild(rootElement);
        
        if (auditSearchRequest.getAuditDateRangeStartDate() != null 
        		|| auditSearchRequest.getAuditDateRangeEndDate() != null) {
        	Element dateRange = XmlUtils.appendElement(rootElement, NS_NC_40, "DateRange");
    		appendDateWapper(dateRange, "StartDate", NS_NC_40, NS_NC_40, auditSearchRequest.getAuditDateRangeStartDate());
    		appendDateWapper(dateRange, "EndDate", NS_NC_40, NS_NC_40, auditSearchRequest.getAuditDateRangeEndDate());
        }
		if (StringUtils.isNotBlank(auditSearchRequest.getFirstName()) ||
				StringUtils.isNotBlank(auditSearchRequest.getLastName())) {
			Element personName = XmlUtils.appendElement(rootElement, NS_NC_40, "UserPersonName");
			XmlUtils.addAttribute(personName, NS_STRUCTURES_40, "id", "UP001");
			if (StringUtils.isNotBlank(auditSearchRequest.getFirstName())) {
				Element personGivenName = XmlUtils.appendElement(personName, NS_NC_40, "PersonGivenName");
				personGivenName.setTextContent(auditSearchRequest.getFirstName());
				XmlUtils.addAttribute(personGivenName, NS_STRUCTURES_40, "metadata", 
						NIEMXMLUtils.getMetaDataId(auditSearchRequest.getFirstNameSearchMetadata()));
			}
			if (StringUtils.isNotBlank(auditSearchRequest.getLastName())) {
				Element personSurName = XmlUtils.appendElement(personName, NS_NC_40, "PersonSurName");
				personSurName.setTextContent(auditSearchRequest.getLastName());
				XmlUtils.addAttribute(personSurName, NS_STRUCTURES_40, "metadata", 
						NIEMXMLUtils.getMetaDataId(auditSearchRequest.getLastNameSearchMetadata()));
			}
		}
		
		if (StringUtils.isNotBlank(auditSearchRequest.getActivityTypeId())) {
			Element userActionPerformed = XmlUtils.appendElement(rootElement, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "UserActionPerformed");
			Element activityIdentification = XmlUtils.appendElement(userActionPerformed, NS_NC_40, "ActivityIdentification");
			XmlUtils.appendTextElement(activityIdentification, NS_NC_40, "IdentificationID", auditSearchRequest.getActivityTypeId());
			XmlUtils.appendTextElement(userActionPerformed, NS_NC_40, "ActivityDescriptionText", auditSearchRequest.getActivityTypeDescription());
		}
		if (StringUtils.isNotBlank(auditSearchRequest.getOri())) {
			Element organization = XmlUtils.appendElement(rootElement, NS_NC_40, "Organization");
			XmlUtils.addAttribute(organization, NS_STRUCTURES_40, "id", "ORG01");
			
			Element organizationAugmentation = XmlUtils.appendElement(organization, NS_JXDM_61, "OrganizationAugmentation");
			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_61, "OrganizationORIIdentification");
			XmlUtils.appendTextElement(organizationORIIdentification, NS_NC_40, "IdentificationID", auditSearchRequest.getOri());
		}
		
		if (StringUtils.isNotBlank(auditSearchRequest.getOri()) 
				&& (StringUtils.isNotBlank(auditSearchRequest.getFirstName()) || StringUtils.isNotBlank(auditSearchRequest.getLastName()))) {
			Element personOrganizationAssociation = XmlUtils.appendElement(rootElement, NS_NC_40, "PersonOrganizationAssociation");
			Element person = XmlUtils.appendElement(personOrganizationAssociation, NS_NC_40, "Person");
			XmlUtils.addAttribute(person, NS_STRUCTURES_40, "ref", "UP01");
			Element organization = XmlUtils.appendElement(personOrganizationAssociation, NS_NC_40, "Organization");
			XmlUtils.addAttribute(organization, NS_STRUCTURES_40, "ref", "ORG01");
		}
		
    	XmlUtils.appendTextElement(rootElement, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "SourceSystemNameText", 
    			AUDIT_LOG_SEARCH_REQUEST_SYSTEM_NAME); 
    	Element exactMatchMetaData = XmlUtils.appendElement(rootElement, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "SearchMetadata");
    	XmlUtils.addAttribute(exactMatchMetaData, NS_STRUCTURES_40, "id", "SM001");
    	XmlUtils.appendTextElement(exactMatchMetaData, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "SearchQualifierCode", SearchFieldMetadata.ExactMatch.getMetadata());
    	
    	Element startsWithMetaData = XmlUtils.appendElement(rootElement, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "SearchMetadata");
    	XmlUtils.addAttribute(startsWithMetaData, NS_STRUCTURES_40, "id", "SM002");
    	XmlUtils.appendTextElement(startsWithMetaData, NS_AUDIT_LOG_SEARCH_REQUEST_EXT, "SearchQualifierCode", SearchFieldMetadata.StartsWith.getMetadata());
    	

		return document;
	}

	public static Document createArrestFinalizeRequest(String id, String[] chargeIds) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_FINALIZE_ARREST_REQUEST_DOC, 
        		NS_PREFIX_FINALIZE_ARREST_REQUEST_DOC + ":FinalizeArrestRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_FINALIZE_ARREST_REQUEST_DOC, 
        		NS_FINALIZE_ARREST_REQUEST_DOC);
        
        Element arrest = createArrestModifyRequestArrestElement(id, document, rootElement);
        
        for (String chargeId : chargeIds) {
            Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
            Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ChargePrimarySystemIdentification");
            XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeId);
        }
        return document;
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

	public static Document createChargeDeclineRequest(ArrestCharge arrestCharge) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_DECLINE_CHARGE_REQUEST_DOC, 
        		NS_PREFIX_DECLINE_CHARGE_REQUEST_DOC + ":DeclineChargeRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_DECLINE_CHARGE_REQUEST_DOC, 
        		NS_DECLINE_CHARGE_REQUEST_DOC);
        
        Element arrest = createArrestModifyRequestArrestElement(arrestCharge.getArrestIdentification(), document, rootElement);
        
        Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
        XmlUtils.appendTextElement(arrestChargeElement, NS_JXDM_60, "ChargeDescriptionText", arrestCharge.getArrestChargeDescription());
        
        Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_60, "ChargeIdentification");
        XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", arrestCharge.getArrestChargeIdentification());
        return document;
	}

	public static Document createChargeReferralRequest(ChargeReferral chargeReferral) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_CHARGE_REFERRAL_REQUEST_DOC, 
        		NS_PREFIX_CHARGE_REFERRAL_REQUEST_DOC + ":ChargeReferralRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_CHARGE_REFERRAL_REQUEST_DOC, 
        		NS_CHARGE_REFERRAL_REQUEST_DOC);
        
        Element arrest = createArrestModifyRequestArrestElement(chargeReferral.getArrestIdentification(), document, rootElement);
        Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
        
        Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_60, "ChargeIdentification");
        XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeReferral.getArrestChargeIdentification());
        
        Element referToAgency = XmlUtils.appendElement(rootElement, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ReferToAgency");
        
        if (chargeReferral.getTargetOwnerType() != null) {
        	XmlUtils.appendTextElement(referToAgency, NS_NC_40, "OrganizationCategoryText", chargeReferral.getTargetOwnerType().getDescription());
        }
        
        if (StringUtils.isNotBlank(chargeReferral.getTargetOri())) {
            Element organizationAugmentation = XmlUtils.appendElement(referToAgency, 
                    OjbcNamespaceContext.NS_JXDM_60, "OrganizationAugmentation"); 
            Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, 
                    OjbcNamespaceContext.NS_JXDM_60, "OrganizationORIIdentification"); 
            XmlUtils.appendTextElement(organizationORIIdentification, 
                    OjbcNamespaceContext.NS_NC_40, "IdentificationID", chargeReferral.getTargetOri()); 
        }
        
		return document;
	}

	public static Document createArrestReferralRequest(ArrestReferral arrestReferral) throws Exception {
		Document document = OJBCXMLUtils.createDocument();  
        Element rootElement = document.createElementNS(NS_ARREST_REFERRAL_REQUEST_DOC, 
        		NS_PREFIX_ARREST_REFERRAL_REQUEST_DOC + ":ArrestReferralRequest");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_ARREST_REFERRAL_REQUEST_DOC, 
        		NS_ARREST_REFERRAL_REQUEST_DOC);
        
        Element arrest = createArrestModifyRequestArrestElement(arrestReferral.getArrestIdentification(), document, rootElement);
        
        for (String chargeId : arrestReferral.getArrestChargeIds()) {
            Element arrestChargeElement = XmlUtils.appendElement(arrest, NS_JXDM_60, "ArrestCharge");
	        Element chargeIdentification = XmlUtils.appendElement(arrestChargeElement, NS_JXDM_60, "ChargeIdentification");
	        XmlUtils.appendTextElement(chargeIdentification, NS_NC_40, "IdentificationID", chargeId);
        }
        
        Element referToAgency = XmlUtils.appendElement(rootElement, NS_CRIMINAL_HISTORY_MODIFICATION_REQUEST_EXT, "ReferToAgency");
        
        if (arrestReferral.getTargetOwnerType() != null) {
        	XmlUtils.appendTextElement(referToAgency, NS_NC_40, "OrganizationCategoryText", arrestReferral.getTargetOwnerType().getDescription());
        }
        
        if (StringUtils.isNotBlank(arrestReferral.getTargetOri())) {
            Element organizationAugmentation = XmlUtils.appendElement(referToAgency, 
                    OjbcNamespaceContext.NS_JXDM_60, "OrganizationAugmentation"); 
            Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, 
                    OjbcNamespaceContext.NS_JXDM_60, "OrganizationORIIdentification"); 
            XmlUtils.appendTextElement(organizationORIIdentification, 
                    OjbcNamespaceContext.NS_NC_40, "IdentificationID", arrestReferral.getTargetOri()); 
        }
        
		return document;
	}	
    
}
