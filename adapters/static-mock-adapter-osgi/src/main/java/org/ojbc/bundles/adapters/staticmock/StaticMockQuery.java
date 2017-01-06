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
package org.ojbc.bundles.adapters.staticmock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.ojbc.bundles.adapters.staticmock.courtcase.CourtCaseSearchResultBuilder;
import org.ojbc.bundles.adapters.staticmock.custody.CustodySearchResultBuilder;
import org.ojbc.util.xml.OjbcNamespaceContext;
import static org.ojbc.util.xml.OjbcNamespaceContext.*;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An OSGi component that performs searches and queries against a set of static instance documents. The envisioned client of this class would be
 * specific search and query services.
 * 
 */
public class StaticMockQuery {

	private static final Log LOG = LogFactory.getLog(StaticMockQuery.class);

	private static final String CRIMINAL_HISTORY_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/CriminalHistory";
	
	private static final String WARRANT_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/Warrant";
	
	private static final String INCIDENT_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/Incident";
	
	private static final String FIREARM_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/FirearmRegistration";
	
	private static final String JUVENILE_HISTORY_SAMPLES_DIRECTORY = "static-instances/JuvenileHistory";	
	
	private static final String CUSTODY_SAMPLES_DIRECTORY = "static-instances/Custody";
	
	private static final String COURT_CASE_SAMPLES_DIRECTORY = "static-instances/CourtCase";

	private static final String VEHICLE_CRASH_SAMPLES_DIRECTORY = "static-instances/VehicleCrash";

	private static final String FIREARM_PROHIBITION_SAMPLES_DIRECTORY = "static-instances/FirearmProhibition";
	
	
	static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");

	
	public static final String CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History";
	
	public static final String CUSTODY_PERSON_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-Jail";
	
	public static final String CUSTODY_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/CustodySearchRequestService/1.0}SubmitCustodySearchRequest";	
	
	public static final String CUSTODY_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Custody_Query_Request_Service/1.0}SubmitCustodyQueryRequest";
	
	public static final String COURT_CASE_PERSON_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-Court";
	
	public static final String COURT_CASE_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/CourtCaseSearchRequestService/1.0}SubmitCourtCaseSearchRequest";
	
	public static final String COURT_CASE_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Court_Case_Query_Request_Service/1.0}/SubmitCourtCaseQueryRequest";
	
	public static final String VEHICLE_CRASH_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-Vehicle-Crash";
	
	public static final String VEHICLE_CRASH_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Vehicle_Crash/1.0}Person-Query-Service---Vehicle-Crash";			
	
	public static final String WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants";
	
	public static final String CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History";
	
	public static final String WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants";
	
	public static final String INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS";
	
	public static final String INCIDENT_MOCK_ADAPTER_VEHICLE_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/VehicleSearchRequestService/1.0}SubmitVehicleSearchRequest-RMS";
	
	public static final String INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentPersonSearchRequest-RMS";
	
	public static final String INCIDENT_MOCK_ADAPTER_INCIDENT_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentSearchRequest-RMS";
	
	public static final String INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-RMS";
	
	public static final String FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Firearms/1.0}Submit-Person-Search---Firearms";
		
	public static final String FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0}SubmitFirearmSearchRequest";
	
	public static final String JUVENILE_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-JuvenileHistory";
	
	public static final String JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory";

	public static final String FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson";
	
	public static final String FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm";;

	public static final String FIREARM_PROHIBITION_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-Firearms-Prohibition";

	public static final String FIREARM_PROHIBITION_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Firearm_Purchase_Prohibition_Query_Request_Service/1.0}SubmitFirearmPurchaseProhibitionQueryRequest";
	
	private ClasspathXmlDataSource criminalHistoryDataSource;	
	
	private ClasspathXmlDataSource custodyDataSource;
	
	private ClasspathXmlDataSource courtCaseDataSource;	
	
	private ClasspathXmlDataSource warrantDataSource;
	
	private ClasspathXmlDataSource incidentDataSource;
	
	private ClasspathXmlDataSource firearmRegistrationDataSource;
	
	private ClasspathXmlDataSource juvenileHistoryDataSource;
	
	private ClasspathXmlDataSource vehicleCrashDataSource;
	
	private ClasspathXmlDataSource firearmProhibitionDataSource;
		
	private OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext(); 

	public StaticMockQuery() {
		
		this(CRIMINAL_HISTORY_PRODUCTION_SAMPLES_DIRECTORY, WARRANT_PRODUCTION_SAMPLES_DIRECTORY, INCIDENT_PRODUCTION_SAMPLES_DIRECTORY, 
				FIREARM_PRODUCTION_SAMPLES_DIRECTORY, JUVENILE_HISTORY_SAMPLES_DIRECTORY, CUSTODY_SAMPLES_DIRECTORY, COURT_CASE_SAMPLES_DIRECTORY, 
				VEHICLE_CRASH_SAMPLES_DIRECTORY, FIREARM_PROHIBITION_SAMPLES_DIRECTORY);
	}

	StaticMockQuery(String criminalHistorySampleInstanceDirectoryRelativePath, String warrantSampleInstanceDirectoryRelativePath, 
			String incidentSampleInstanceDirectoryRelativePath, String firearmSampleInstanceDirectoryRelativePath,
			String juvenileHistorySampleInstanceDirectoryRelativePath, String custodySampleDir, String courtCaseSampleDir, 
			String vehicleCrashSampleDir, 
			String firearmProhibitionSampleDir) {
		
		criminalHistoryDataSource = new ClasspathXmlDataSource(criminalHistorySampleInstanceDirectoryRelativePath);
		
		warrantDataSource = new ClasspathXmlDataSource(warrantSampleInstanceDirectoryRelativePath);
		
		incidentDataSource = new ClasspathXmlDataSource(incidentSampleInstanceDirectoryRelativePath);
		
		firearmRegistrationDataSource = new ClasspathXmlDataSource(firearmSampleInstanceDirectoryRelativePath);
		
		juvenileHistoryDataSource = new ClasspathXmlDataSource(juvenileHistorySampleInstanceDirectoryRelativePath);		
		
		custodyDataSource = new ClasspathXmlDataSource(custodySampleDir);
		
		courtCaseDataSource = new ClasspathXmlDataSource(courtCaseSampleDir);
		
		vehicleCrashDataSource = new ClasspathXmlDataSource(vehicleCrashSampleDir);
		
		firearmProhibitionDataSource = new ClasspathXmlDataSource(firearmProhibitionSampleDir);
	}

	/**
	 * Get the total number of available juvenile case plan documents.
	 * 
	 * @return the document count
	 * @throws Exception
	 */
	public int getJuvenileHistoryDocumentCount() throws Exception {
		return juvenileHistoryDataSource.getDocuments().size();
	}
	
	/**
	 * Gets the total number of available Custody documents
	 * @return document count
	 * @throws Exception
	 */
	public int getCustodyDocumentCount() throws Exception{
		return custodyDataSource.getDocuments().size();
	}
	
	/**
	 * Gets the total number of available court case documents
	 * @return document count
	 * @throws Exception
	 */
	public int getCourtCaseDocumentCount() throws Exception{
		return courtCaseDataSource.getDocuments().size();
	}
	
	/**
	 * Gets the total number of available vehicle crash documents
	 * @return document count
	 * @throws Exception
	 */
	public int getVehicleCrashDocumentCount() throws Exception{
		return vehicleCrashDataSource.getDocuments().size();
	}

	/**
	 * Get the total number of available criminal history documents.
	 * 
	 * @return the document count
	 * @throws Exception
	 */
	public int getCriminalHistoryDocumentCount() throws Exception {
		return criminalHistoryDataSource.getDocuments().size();
	}

	/**
	 * Get the total number of available warrant documents.
	 * 
	 * @return the document count
	 * @throws Exception
	 */
	public int getWarrantDocumentCount() throws Exception {
		return warrantDataSource.getDocuments().size();
	}

	/**
	 * Get the total number of available incident documents.
	 * 
	 * @return the document count
	 * @throws Exception
	 */
	public int getIncidentDocumentCount() throws Exception {
		return incidentDataSource.getDocuments().size();
	}

	/**
	 * Get the total number of available firearm registration documents.
	 * 
	 * @return the document count
	 * @throws Exception
	 */
	public int getFirearmRegistrationDocumentCount() throws Exception {
		return firearmRegistrationDataSource.getDocuments().size();
	}

	/**
	 * Perform a query (retrieving instances that match a (usually unique) identifier.
	 * 
	 * @param queryRequestMessage
	 *            the request message, assumed to be valid against some Query Service IEPD
	 * @return the matching document(s)
	 * @throws Exception
	 */
	public List<IdentifiableDocumentWrapper> queryDocuments(Document queryRequestMessage) throws Exception {
		return queryDocuments(queryRequestMessage, null);
	}

	/**
	 * Perform a query (retrieving instances that match a (usually unique) identifier.
	 * 
	 * @param queryRequestMessage
	 *            the request message, assumed to be valid against some Query Service IEPD
	 * @param context
	 *            an arbitrary object that callers can pass in to provide some context for the query; this will be specific to each type of query
	 * @return the matching document(s)
	 * @throws Exception
	 */
	public List<IdentifiableDocumentWrapper> queryDocuments(Document queryRequestMessage, Object context) throws Exception {
		
		Element rootElement = queryRequestMessage.getDocumentElement();

		String rootElementNamespace = rootElement.getNamespaceURI();
		String rootElementLocalName = rootElement.getLocalName();

		LOG.info("rootElementNamespace: " + rootElementNamespace);
		LOG.info("rootElementLocalName: " + rootElementLocalName);
		
		Element systemElement = null;
		Element systemIdElement = null;
		
		switch (rootElementNamespace + ":" + rootElementLocalName){
		case NS_INCIDENT_QUERY_REQUEST_DOC + ":IncidentIdentifierIncidentReportRequest":
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, "iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:SourceSystemNameText");
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, "iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:Incident/nc:ActivityIdentification/nc:IdentificationID");
			break;
		case NS_COURT_CASE_QUERY_REQUEST_DOC + ":CourtCaseQueryRequest": {
			String xPath = NS_PREFIX_COURT_CASE_QUERY_REQUEST_DOC + ":CourtCaseQueryRequest/" + NS_PREFIX_COURT_CASE_QUERY_REQ_EXT
				+ ":CourtCaseRecordIdentification/" + NS_PREFIX_NC_30 + ":IdentificationSourceText";
	
			systemElement= (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_COURT_CASE_QUERY_REQUEST_DOC + ":CourtCaseQueryRequest/"
				+ NS_PREFIX_COURT_CASE_QUERY_REQ_EXT + ":CourtCaseRecordIdentification/" + NS_PREFIX_NC_30 + ":IdentificationID");
	
			LOG.info("Processing Court Case Query Request");
			break; 
		}
		case NS_CUSTODY_QUERY_REQUEST_EXCH + ":CustodyQueryRequest": { 
			LOG.info("Processing Custody Query Request"); 
			String xPath = NS_PREFIX_CUSTODY_QUERY_REQUEST_EXCH + ":CustodyQueryRequest/" + NS_PREFIX_CUSTODY_QUERY_REQUEST_EXT
					+ ":CustodyRecordIdentification/" + NS_PREFIX_NC_30 + ":IdentificationSourceText";
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_CUSTODY_QUERY_REQUEST_EXCH + ":CustodyQueryRequest/"
					+ NS_PREFIX_CUSTODY_QUERY_REQUEST_EXT + ":CustodyRecordIdentification/" + NS_PREFIX_NC_30 + ":IdentificationID");
			break;
		}
			
		case NS_VEHICLE_CRASH_QUERY_REQUEST_DOC + ":VehicleCrashQueryRequest":{
			String xPath = NS_PREFIX_VEHICLE_CRASH_QUERY_REQUEST_DOC + ":VehicleCrashQueryRequest/" + NS_PREFIX_VEHICLE_CRASH_QUERY_REQUEST_EXT
					+ ":VehicleCrashIdentification/" + NS_PREFIX_NC_30 + ":IdentificationSourceText";
			LOG.info("System identification source text xPath: " + xPath);
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_VEHICLE_CRASH_QUERY_REQUEST_DOC + ":VehicleCrashQueryRequest/" + NS_PREFIX_VEHICLE_CRASH_QUERY_REQUEST_EXT
					+ ":VehicleCrashIdentification/" + NS_PREFIX_NC_30 + ":IdentificationID");
			break;
		}
		case NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":PersonFirearmRegistrationRequest":{
			String xPath = NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":PersonFirearmRegistrationRequest/" + NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT
					+ ":PersonFirearmRegistrationIdentification/" + NS_PREFIX_NC + ":IdentificationSourceText";
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":PersonFirearmRegistrationRequest/"
					+ NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":PersonFirearmRegistrationIdentification/" + NS_PREFIX_NC + ":IdentificationID");
			break;
		}
		case NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":FirearmRegistrationRequest": {
			String xPath = NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":FirearmRegistrationRequest/" + NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":FirearmRegistrationIdentification/"
					+ NS_PREFIX_NC + ":IdentificationSourceText";
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":FirearmRegistrationRequest/"
					+ NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":FirearmRegistrationIdentification/" + NS_PREFIX_NC + ":IdentificationID");
			break;
		}
		case NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC + ":JuvenileHistoryQuery":{
			String xPath = NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC + ":JuvenileHistoryQuery/" +
				NS_PREFIX_JUVENILE_HISTORY_EXT + ":JuvenileHistoryQueryCriteria/" +
				NS_PREFIX_JUVENILE_HISTORY_EXT + ":JuvenileInformationRecordID/" +
				NS_PREFIX_NC_30 + ":IdentificationSourceText";
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC + ":JuvenileHistoryQuery/" +
				NS_PREFIX_JUVENILE_HISTORY_EXT + ":JuvenileHistoryQueryCriteria/" +
				NS_PREFIX_JUVENILE_HISTORY_EXT + ":JuvenileInformationRecordID/" +
				NS_PREFIX_NC_30 + ":IdentificationID");
			break;
			
		}
		case NS_FIREARMS_PROHIBITION_DOC + ":FirearmPurchaseProhibitionQueryRequest": {
			systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, 
				NS_PREFIX_FIREARMS_PROHIBITION_DOC + ":FirearmPurchaseProhibitionQueryRequest/"
				+ NS_PREFIX_FIREARMS_PROHIBITION_EXT + ":FirearmPurchaseProhibitionSystemIdentification/"
				+ NS_PREFIX_NC_30 + ":SystemName");
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, 
				NS_PREFIX_FIREARMS_PROHIBITION_DOC + ":FirearmPurchaseProhibitionQueryRequest/"
				+ NS_PREFIX_FIREARMS_PROHIBITION_EXT + ":FirearmPurchaseProhibitionSystemIdentification/"
				+ NS_PREFIX_NC_30 + ":IdentificationID");
			break;
		}
		default: 
			systemElement = (Element) XmlUtils.xPathNodeSearch(rootElement, "pqr:PersonRecordRequestIdentification/nc:IdentificationSourceText");
			systemIdElement = (Element) XmlUtils.xPathNodeSearch(rootElement, "pqr:PersonRecordRequestIdentification/nc:IdentificationID");
		}

		if (systemElement == null) {
			throw new IllegalArgumentException("Invalid query request message:  must specify the system to query.");
		}
		
		if (systemIdElement == null) {
			throw new IllegalArgumentException("Invalid query request message:  must specify the systemId to query.");
		}

		String documentId = systemIdElement.getTextContent();
		String systemId = systemElement.getTextContent();

		LOG.info("documentId: " + StringUtils.trimToEmpty(documentId));
		LOG.info("systemId: " + StringUtils.trimToEmpty(systemId));
		
		switch (systemId) {
		case CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID: 
			return queryCriminalHistoryDocuments(documentId);
		case WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID:
			return queryWarrantDocuments(documentId);
		case INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID:
			return queryIncidentDocuments(documentId);
		case FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID: 
			return queryPersonFirearmRegistrationDocuments(documentId);
		case FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID:
			return queryPersonFirearmRegistrationDocuments(documentId);
		case JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID: 
			return queryJuvenileHistoryDocuments(documentId, context);
		case CUSTODY_QUERY_SYSTEM_ID:
			return queryCustodyDocuments(documentId);
		case COURT_CASE_QUERY_SYSTEM_ID:
			return queryCourtCaseDocuments(documentId);
		case VEHICLE_CRASH_QUERY_SYSTEM_ID:			
			return queryVehicleCrashDocuments(documentId);
		case FIREARM_PROHIBITION_QUERY_SYSTEM_ID:
			return queryFirearmProhibitionDocuments(documentId);
		default:
			XmlUtils.printNode(queryRequestMessage);
			throw new IllegalArgumentException("Unknown system name: " + systemId);
		}

	}

	/**
	 * Perform a search (searching for instances that match a set of criteria) and returning the data in those matching instances as a Search Results
	 * message. The type of result message will depend on the type of request message passed in.
	 * 
	 * @param searchRequestMessage
	 *            the request message, assumed to be valid against some Search Service's IEPD
	 * @return the search results
	 * @throws Exception
	 */
	public Document searchDocuments(Document searchRequestMessage) throws Exception {
		DateTime baseDate = new DateTime();
		return searchDocuments(searchRequestMessage, baseDate);
	}

	/**
	 * Perform a search (searching for instances that match a set of criteria) and returning the data in those matching instances as a Search Results
	 * message. The type of result message will depend on the type of request message passed in.
	 * 
	 * @param searchRequestMessage
	 *            the request message, assumed to be valid against some Search Service's IEPD
	 * @param context
	 *            an arbitrary object that a caller can pass in to provide context for the request; specific to each type of search
	 * @return the search results
	 * @throws Exception
	 */
	public Document searchDocuments(Document searchRequestMessage, Object context) throws Exception {
		DateTime baseDate = new DateTime();
		return searchDocuments(searchRequestMessage, baseDate, context);
	}

	Document searchDocuments(Document searchRequestMessage, DateTime baseDate) throws Exception {
		return searchDocuments(searchRequestMessage, baseDate, null);
	}

	Document searchDocuments(Document searchRequestMessage, DateTime baseDate, Object context) throws Exception {
		
		Element rootElement = searchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		
		if (NS_PERSON_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "PersonSearchRequest".equals(rootLocalName)) {
			return personSearchDocuments(searchRequestMessage, baseDate);
								
		}else if(NS_CUSTODY_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "CustodySearchRequest".equals(rootLocalName)){			
			return custodySearchDocuments(searchRequestMessage, baseDate);			
		
		}else if(NS_COURT_CASE_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "CourtCaseSearchRequest".equals(rootLocalName)){			
			return courtCaseSearchDocuments(searchRequestMessage, baseDate);
						
		} else if (NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentPersonSearchRequest".equals(rootLocalName)) {
			return incidentPersonSearchDocuments(searchRequestMessage, baseDate);
			
		} else if (NS_FIREARM_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "FirearmSearchRequest".equals(rootLocalName)) {
			return firearmSearchDocuments(searchRequestMessage);
			
		} else if (NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentSearchRequest".equals(rootLocalName)) {
			return incidentSearchDocuments(searchRequestMessage, baseDate);
			
		} else if (NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentVehicleSearchRequest".equals(rootLocalName)) {
			return incidentVehicleSearchDocuments(searchRequestMessage, baseDate);
			
		} else if (NS_VEHICLE_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "VehicleSearchRequest".equals(rootLocalName)) {
			return vehicleSearchDocuments(searchRequestMessage, baseDate);
		}
		throw new IllegalArgumentException("Invalid message: {" + rootNamespaceURI + "}" + rootLocalName);
	}

	Document incidentVehicleSearchDocuments(Document incidentSearchRequestMessage, DateTime baseDate) throws Exception {

		Document errorReturn = getIncidentSearchStaticErrorResponse(incidentSearchRequestMessage);

		if (errorReturn != null) {
			return errorReturn;
		}

		List<IdentifiableDocumentWrapper> instanceWrappers = incidentVehicleSearchDocumentsAsList(incidentSearchRequestMessage, baseDate);
		Document ret = buildIncidentSearchResultsMessage(instanceWrappers, "IncidentVehicleSearchResults", "IncidentVehicleSearchResult", INCIDENT_MOCK_ADAPTER_INCIDENT_SEARCH_SYSTEM_ID);
		return ret;

	}

	Document incidentSearchDocuments(Document incidentSearchRequestMessage, DateTime baseDate) throws Exception {

		Document errorReturn = getIncidentSearchStaticErrorResponse(incidentSearchRequestMessage);

		if (errorReturn != null) {
			return errorReturn;
		}

		List<IdentifiableDocumentWrapper> instanceWrappers = incidentSearchDocumentsAsList(incidentSearchRequestMessage, baseDate);
		Document ret = buildIncidentSearchResultsMessage(instanceWrappers, "IncidentSearchResults", "IncidentSearchResult", INCIDENT_MOCK_ADAPTER_INCIDENT_SEARCH_SYSTEM_ID);
		return ret;

	}

	private static final boolean isMissing(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	List<IdentifiableDocumentWrapper> incidentVehicleSearchDocumentsAsList(Document incidentVehicleSearchRequestMessage, DateTime baseDate) throws Exception {

		Element rootElement = incidentVehicleSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		if (!(NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentVehicleSearchRequest".equals(rootLocalName))) {
			throw new IllegalArgumentException("Invalid message, must have {" + NS_INCIDENT_SEARCH_REQUEST_DOC + "}IncidentVehicleSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}

		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "isr:SourceSystemNameText");
		int systemElementCount;
		if (systemElements == null || (systemElementCount = systemElements.getLength()) == 0) {
			throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
		}

		Element vehicleIdElement = (Element) XmlUtils.xPathNodeSearch(incidentVehicleSearchRequestMessage, "/isr-doc:IncidentVehicleSearchRequest/ivsr:Vehicle/ivsr:VehicleSystemIdentification/nc:IdentificationID");

		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		
		if (vehicleIdElement != null) {

			String id = vehicleIdElement.getTextContent();

			for (int i = 0; i < systemElementCount; i++) {
				for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

					Document d = dw.getDocument();
					Element documentVehicleIdElement = (Element) XmlUtils
							.xPathNodeSearch(d,
									"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:VehicleIdentification/nc:IdentificationID");
					String documentPersonId = documentVehicleIdElement.getTextContent();
					if (id.equals(documentPersonId)) {
						ret.add(dw);
					}
				}
			}

		}

		return ret;

	}
	
	List<IdentifiableDocumentWrapper> vehicleSearchDocumentsAsList(Document vehicleSearchRequestMessage, DateTime baseDate) throws Exception {
		
		Element rootElement = vehicleSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		if (!(NS_VEHICLE_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "VehicleSearchRequest".equals(rootLocalName))) {
			throw new IllegalArgumentException("Invalid message, must have {" + NS_VEHICLE_SEARCH_REQUEST_DOC + "}VehicleSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}

		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "vsr:SourceSystemNameText");
		if (systemElements == null || systemElements.getLength() != 1) {
			throw new IllegalArgumentException("Invalid query request message:  must specify exactly one system to query.");
		}
		Node systemElementNode = systemElements.item(0);
		String systemId = systemElementNode.getTextContent();
		if (!INCIDENT_MOCK_ADAPTER_VEHICLE_SEARCH_SYSTEM_ID.equals(systemId)) {
			throw new IllegalArgumentException("Vehicle searches only allowed against static mock incident store.  Illegal system: " + systemId);
		}
		
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		
		String searchXPath = buildIncidentSearchXPathFromVehicleSearchMessage(vehicleSearchRequestMessage);

		String modelYearLowerString = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/vsr:VehicleYearRange/nc:StartDate/nc:Year");
		String modelYearUpperString = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/vsr:VehicleYearRange/nc:EndDate/nc:Year");
		
		if (searchXPath == null && isMissing(modelYearLowerString) && isMissing(modelYearUpperString)) {
			return ret;
		}
		
		for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

			Document d = dw.getDocument();
			LOG.debug("Searching document " + dw.getId());

			boolean match = (searchXPath == null || XmlUtils.xPathNodeSearch(d, searchXPath) != null);
			LOG.debug("Match=" + match + " based on xpath eval");

			if (match && (!(isMissing(modelYearLowerString) && isMissing(modelYearUpperString)))) {
				
				Integer modelYearLower = Integer.parseInt(modelYearLowerString);
				Integer modelYearUpper = Integer.parseInt(modelYearUpperString);
				
				String incidentVehicleModelYearString = XmlUtils.xPathStringSearch(d, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:ItemModelYearDate");
				Integer incidentVehicleModelYear = null;
				
				try {
					incidentVehicleModelYear = Integer.parseInt(incidentVehicleModelYearString);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					LOG.warn("Unparsable vehicle model year: " + incidentVehicleModelYearString);
				}
				
				if (incidentVehicleModelYear == null) {
					match = false;
				} else {
					if (modelYearLower != null) {
						match = match && incidentVehicleModelYear >= modelYearLower;
					}
					if (modelYearUpper != null) {
						match = match && incidentVehicleModelYear <= modelYearUpper;
					}
				}
				
			}
			
			if (match) {
				ret.add(dw);
			}
			
		}

		return ret;
		
	}

	List<IdentifiableDocumentWrapper> incidentSearchDocumentsAsList(Document incidentSearchRequestMessage, DateTime baseDate) throws Exception {

		Element rootElement = incidentSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		if (!(NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentSearchRequest".equals(rootLocalName))) {
			throw new IllegalArgumentException("Invalid message, must have {" + NS_INCIDENT_SEARCH_REQUEST_DOC + "}IncidentSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}

		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "isr:SourceSystemNameText");
		if (systemElements == null || systemElements.getLength() == 0) {
			throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
		}

		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

		String searchXPath = buildIncidentSearchXPathFromIncidentSearchMessage(incidentSearchRequestMessage);

		String dateLowerString = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
		String dateUpperString = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:EndDate/nc:DateTime");

		LOG.debug("dateLowerString=" + dateLowerString);
		LOG.debug("dateUpperString=" + dateUpperString);

		if (searchXPath == null && isMissing(dateLowerString) && isMissing(dateUpperString)) {
			return ret;
		}

		DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();

		for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

			Document d = dw.getDocument();
			LOG.debug("Searching document " + dw.getId());

			boolean match = (searchXPath == null || XmlUtils.xPathNodeSearch(d, searchXPath) != null);
			LOG.debug("Match=" + match + " based on xpath eval");

			if (match && (!(isMissing(dateLowerString) && isMissing(dateUpperString)))) {

				LOG.debug("In date evaluation");

				String incidentStartDateString = XmlUtils
						.xPathStringSearch(d,
								"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
				String incidentEndDateString = XmlUtils
						.xPathStringSearch(d,
								"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:EndDate/nc:DateTime");

				if (isMissing(incidentStartDateString) && isMissing(incidentEndDateString)) {
					match = false;
				} else {
					DateTime incidentStartDateTime = null;
					DateTime incidentEndDateTime = null;
					try {
						incidentStartDateTime = dateTimeFormatter.parseDateTime(incidentStartDateString);
						incidentEndDateTime = dateTimeFormatter.parseDateTime(incidentEndDateString);
					} catch (Exception e) {
						e.printStackTrace();
						LOG.warn("Unable to parse date (either " + incidentStartDateString + " or " + incidentEndDateString + "). Note that null dates are ok...just means it's not part of the incident");
					}
					LOG.debug("incidentStartDateTime=" + incidentStartDateTime);
					LOG.debug("incidentEndDateTime=" + incidentEndDateTime);
					if (incidentStartDateTime == null && incidentEndDateTime == null) {
						match = false;
					} else {
						if (!isMissing(dateLowerString)) {
							DateTime lowerDateTime = dateTimeFormatter.parseDateTime(dateLowerString);
							if (incidentEndDateTime != null && incidentEndDateTime.isBefore(lowerDateTime)) {
								match = false;
							} else if (incidentEndDateTime == null && incidentStartDateTime.isBefore(lowerDateTime)) {
								match = false;
							}
						}
						if (!isMissing(dateUpperString)) {
							DateTime upperDateTime = dateTimeFormatter.parseDateTime(dateUpperString);
							if (incidentStartDateTime != null && incidentStartDateTime.isAfter(upperDateTime)) {
								match = false;
							} else if (incidentStartDateTime == null && incidentEndDateTime.isBefore(upperDateTime)) {
								match = false;
							}
						}
					}
				}

			}

			if (match) {
				ret.add(dw);
			}

		}

		return ret;

	}

	Document incidentPersonSearchDocuments(Document incidentPersonSearchRequestMessage, DateTime baseDate) throws Exception {
		List<IdentifiableDocumentWrapper> instanceWrappers = incidentPersonSearchDocumentsAsList(incidentPersonSearchRequestMessage, baseDate);
		Document ret = buildIncidentSearchResultsMessage(instanceWrappers, "IncidentPersonSearchResults", "IncidentPersonSearchResult", INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID);
		return ret;
	}

	private Document buildIncidentSearchResultsMessage(List<IdentifiableDocumentWrapper> instanceWrappers, String rootElementName, String resultElementName, String sourceSystemName) throws ParserConfigurationException, Exception {

		Document ret = createNewDocument();

		Element root = ret.createElementNS(NS_INCIDENT_SEARCH_RESULTS_DOC, rootElementName);
		ret.appendChild(root);
		String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(NS_INCIDENT_SEARCH_RESULTS_DOC);
		root.setPrefix(prefix);

		int incidentSequence = 1;

		for (IdentifiableDocumentWrapper instanceWrapper : instanceWrappers) {

			Document instance = instanceWrapper.getDocument();

			Element isrElement = XmlUtils.appendElement(root, NS_INCIDENT_SEARCH_RESULTS_EXT, resultElementName);
			Element incidentElement = XmlUtils.appendElement(isrElement, NS_INCIDENT_SEARCH_RESULTS_EXT, "Incident");
			String incidentId = "I" + incidentSequence;
			XmlUtils.addAttribute(incidentElement, NS_STRUCTURES, "id", incidentId);
			Element e = XmlUtils.appendElement(incidentElement, NS_NC, "ActivityIdentification");
			e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
			String pathToInstanceIncident = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']";
			Element instanceIncidentIdElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToInstanceIncident + "/nc:ActivityIdentification/nc:IdentificationID");
			e.setTextContent(instanceIncidentIdElement.getTextContent());
			Element instanceIncidentDateElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToInstanceIncident + "/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
			e = XmlUtils.appendElement(incidentElement, NS_NC, "ActivityDate");
			e = XmlUtils.appendElement(e, NS_NC, "DateTime");
			e.setTextContent(instanceIncidentDateElement.getTextContent());
			e = XmlUtils.appendElement(incidentElement, NS_INCIDENT_SEARCH_RESULTS_EXT, "IncidentCategoryCode");
			e.setTextContent("Law");

			String pathToLocation = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id='incident-location']";

			e = XmlUtils.appendElement(isrElement, NS_NC, "Location");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "id", incidentId + "-L1");
			e = XmlUtils.appendElement(e, NS_NC, "LocationAddress");
			Element structuredAddressElement = XmlUtils.appendElement(e, NS_INCIDENT_SEARCH_RESULTS_EXT, "StructuredAddress");
			Element locationStreetElement = XmlUtils.appendElement(structuredAddressElement, NS_NC, "LocationStreet");
			e = XmlUtils.appendElement(locationStreetElement, NS_NC, "StreetNumberText");
			Element instanceStreetNumberElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText");
			e.setTextContent(instanceStreetNumberElement.getTextContent());
			e = XmlUtils.appendElement(locationStreetElement, NS_NC, "StreetName");
			Element instanceStreetElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName");
			e.setTextContent(instanceStreetElement.getTextContent());
			e = XmlUtils.appendElement(structuredAddressElement, NS_NC, "LocationCityName");
			Element instanceCityElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName");
			e.setTextContent(instanceCityElement.getTextContent());
			Element instanceStateElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode");
			e = XmlUtils.appendElement(structuredAddressElement, NS_NC, "LocationStateUSPostalServiceCode");
			e.setTextContent(instanceStateElement.getTextContent());
			Element instanceZipElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode");
			e = XmlUtils.appendElement(structuredAddressElement, NS_NC, "LocationPostalCode");
			e.setTextContent(instanceZipElement.getTextContent());

			e = XmlUtils.appendElement(isrElement, NS_NC, "Organization");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "id", incidentId + "-O1");
			Element instanceOrganizationElement = (Element) XmlUtils.xPathNodeSearch(instance,
					"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityOrganization/nc:Organization/nc:OrganizationName");
			e = XmlUtils.appendElement(e, NS_NC, "OrganizationName");
			e.setTextContent(instanceOrganizationElement.getTextContent());

			Element a = XmlUtils.appendElement(isrElement, NS_NC, "ActivityReportingOrganizationAssociation");
			e = XmlUtils.appendElement(a, NS_NC, "ActivityReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", incidentId);
			e = XmlUtils.appendElement(a, NS_NC, "OrganizationReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", incidentId + "-O1");

			a = XmlUtils.appendElement(isrElement, NS_JXDM_41, "ActivityLocationAssociation");
			e = XmlUtils.appendElement(a, NS_NC, "ActivityReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", incidentId);
			e = XmlUtils.appendElement(a, NS_NC, "LocationReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", incidentId + "-L1");

			e = XmlUtils.appendElement(isrElement, NS_INCIDENT_SEARCH_RESULTS_EXT, "SourceSystemNameText");
			e.setTextContent(sourceSystemName);

			Element systemIdentifierElement = XmlUtils.appendElement(isrElement, NS_INTEL, "SystemIdentifier");
			e = XmlUtils.appendElement(systemIdentifierElement, NS_NC, "IdentificationID");
			e.setTextContent(instanceWrapper.getId());
			e = XmlUtils.appendElement(systemIdentifierElement, NS_INTEL, "SystemName");
			e.setTextContent("Incident System");

			incidentSequence++;

		}
		ojbcNamespaceContext.populateRootNamespaceDeclarations(root);
		return ret;
	}

	private Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilder db = getNewDocumentBuilder();
		Document ret = db.newDocument();
		return ret;
	}

	private DocumentBuilder getNewDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db;
	}

	
	Document courtCaseSearchDocuments(Document courtCaseSearchRequestMessage, DateTime baseDate) throws Exception {
				
		// get matching results
		List<IdentifiableDocumentWrapper> courtCaseDetailResultList = courtCaseSearchDocumentsAsList(courtCaseSearchRequestMessage);
		
		Document rCourtCaseSearchResultsDoc = createNewDocument();
		
		Element courtCaseSearchResultsRootElement = rCourtCaseSearchResultsDoc.createElementNS(NS_COURT_CASE_SEARCH_RESULTS, "CourtCaseSearchResults");
		
		courtCaseSearchResultsRootElement.setPrefix(NS_PREFIX_COURT_CASE_SEARCH_RESULTS);
		
		rCourtCaseSearchResultsDoc.appendChild(courtCaseSearchResultsRootElement);						
				
		int detailResultIndex = 0;
		
		// loop through detail result matches, generate a search results doc
		for (IdentifiableDocumentWrapper courtCaseDetailResultWrapper : courtCaseDetailResultList) {			
			
			String sResultIndex = String.valueOf(detailResultIndex);
			
			Element courtCaseSearchResultElement = CourtCaseSearchResultBuilder 					
					.buildCourtCaseSearchResultElement(rCourtCaseSearchResultsDoc, courtCaseDetailResultWrapper, sResultIndex);
			
			courtCaseSearchResultsRootElement.appendChild(courtCaseSearchResultElement);
			
			detailResultIndex++;
		}		
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(courtCaseSearchResultsRootElement);
		
		return rCourtCaseSearchResultsDoc;
	}	

	
	Document custodySearchDocuments(Document custodySearchRequestMessage, DateTime baseDate) throws Exception {
						
		// Get a List of individual Custody Query Documents that match search criteria
		List<IdentifiableDocumentWrapper> custodyDetailResultDocList = custodySearchDocumentsAsList(custodySearchRequestMessage, baseDate);
				
		Document rCustodySearchResultsDoc = createNewDocument();
				
		Element custodySearchResultsRootElement = rCustodySearchResultsDoc.createElementNS(NS_CUSTODY_SEARCH_RESULTS, "CustodySearchResults");	
		
		custodySearchResultsRootElement.setPrefix(NS_PREFIX_CUSTODY_SEARCH_RESULTS);	
		
		rCustodySearchResultsDoc.appendChild(custodySearchResultsRootElement);							
		
		int resultIndex = 0;
		
		// loop through matches, gather them and generate a search results document with the matches
		for(IdentifiableDocumentWrapper custodyDetailResultWrapper : custodyDetailResultDocList) {	
			
			String sResultId = String.valueOf(resultIndex);
			
			Element custodySearchResultElement = CustodySearchResultBuilder
					.buildCustodySearchResultElement(rCustodySearchResultsDoc, custodyDetailResultWrapper, sResultId);
			
			custodySearchResultsRootElement.appendChild(custodySearchResultElement);
			
			resultIndex++;
		}				
				
		ojbcNamespaceContext.populateRootNamespaceDeclarations(custodySearchResultsRootElement);
		
		return rCustodySearchResultsDoc;
	}	
	

	Document personSearchDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {

		Document errorReturn = getPersonSearchStaticErrorResponse(personSearchRequestMessage);

		if (errorReturn != null) {
			return errorReturn;
		}

		// gets documents from each source system requested
		List<IdentifiableDocumentWrapper> instanceWrappers = personSearchDocumentsAsList(personSearchRequestMessage, baseDate);

		Document ret = createNewDocument();

		Element root = ret.createElementNS(NS_PERSON_SEARCH_RESULTS_DOC, "PersonSearchResults");
		ret.appendChild(root);
		String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(NS_PERSON_SEARCH_RESULTS_DOC);
		root.setPrefix(prefix);

		for (IdentifiableDocumentWrapper instanceWrapper : instanceWrappers) {

			Document specificDetailSourceDoc = instanceWrapper.getDocument();

			Element psrElement = XmlUtils.appendElement(root, NS_PERSON_SEARCH_RESULTS_EXT, "PersonSearchResult");
			Element personElement = XmlUtils.appendElement(psrElement, NS_PERSON_SEARCH_RESULTS_EXT, "Person");
			Element documentRootElement = specificDetailSourceDoc.getDocumentElement();

			String rootNamespace = documentRootElement.getNamespaceURI();
			
			String rootLocalName = documentRootElement.getLocalName();
			
			SearchValueXPaths xPaths = null;
			
			if (NS_CH_DOC.equals(rootNamespace) && "CriminalHistory".equals(rootLocalName)) {
				xPaths = getCriminalHistoryXPaths();
				
			} else if (NS_WARRANT.equals(rootNamespace) && "Warrants".equals(rootLocalName)) {
				xPaths = getWarrantXPaths();
				
			} else if (NS_FIREARM_DOC.equals(rootNamespace) && "PersonFirearmRegistrationQueryResults".equals(rootLocalName)) {
				xPaths = getFirearmRegistrationXPaths();
				
			} else if (NS_IR.equals(rootNamespace) && "IncidentReport".equals(rootLocalName)) {
				xPaths = getIncidentXPaths();
				
			} else if (NS_JUVENILE_HISTORY_CONTAINER.equals(rootNamespace) && "JuvenileHistoryContainer".equals(rootLocalName)) {
				xPaths = getJuvenileHistoryXPaths();
				
			}else if(NS_CUSTODY_QUERY_RESULTS_EXCH_DOC.equals(rootNamespace) && "CustodyQueryResults".equals(rootLocalName)){								
				xPaths = getCustodyXPaths();
				
			}else if(NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC.equals(rootNamespace) && "CourtCaseQueryResults".equals(rootLocalName)){
				xPaths = getCourtCaseXPaths();
				
			}else if(NS_VEHICLE_CRASH_QUERY_RESULT_EXCH_DOC.equals(rootNamespace) && "VehicleCrashQueryResults".equals(rootLocalName)){
				xPaths = getVehicleCrashXPaths();				
				
			}else if(NS_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS.equals(rootNamespace) && "FirearmPurchaseProhibitionQueryResults".equals(rootLocalName)){
				xPaths = getFirearmProhibitionXPaths();				
				
			} else {
				throw new IllegalStateException("Unsupported document root element: " + rootLocalName);
			}

			Element dobElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.birthdateXPath);
			Element ageElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.ageXPath);
			
			if (dobElement != null) {
				
				Element e = XmlUtils.appendElement(personElement, NS_NC, "PersonAgeMeasure");
				e = XmlUtils.appendElement(e, NS_NC, "MeasurePointValue");
				
				String dob = dobElement.getTextContent();
				
				dob = dob.trim();				
				
				e.setTextContent(String.valueOf(Years.yearsBetween(DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dob), baseDate).getYears()));				
			}
							
					
			NodeList altNameIdentNodeList = xPaths.alternateNameIdentityNodeListXpath == null ? null : 
				XmlUtils.xPathNodeListSearch(specificDetailSourceDoc, xPaths.alternateNameIdentityNodeListXpath);
			
						
			for(int i=0; altNameIdentNodeList != null &&  i < altNameIdentNodeList.getLength(); i++ ){
				
			 	Element iIdentityEl = (Element)altNameIdentNodeList.item(i);
			 	
			 	String altFirstName = XmlUtils.xPathStringSearch(iIdentityEl, 
			 			"nc30:IdentityPersonRepresentation/nc30:PersonName/nc30:PersonGivenName");
			 				 	
			 	altFirstName = altFirstName == null ? null : altFirstName.trim();
			 	
			 	String altLastName = XmlUtils.xPathStringSearch(iIdentityEl, 
			 			"nc30:IdentityPersonRepresentation/nc30:PersonName/nc30:PersonSurName");
			 	
			 	altLastName = altLastName == null ? null : altLastName.trim();
			 	
			 	String altFullName = altFirstName + " " + altLastName;
			 	
			 	altFullName = altFullName == null ? null : altFullName.trim();
			 	
			 	if(StringUtils.isNotEmpty(altFullName)){
			 		
			 		Element altNameEl = XmlUtils.appendElement(personElement, NS_NC, "PersonAlternateName");
			 		
					Element altFullNameEl = XmlUtils.appendElement(altNameEl, NS_NC, "PersonFullName");
					
					altFullNameEl.setTextContent(altFullName);
			 	}			 	
			}
			
			
			if(dobElement != null){
				
				Element birthDateEl = XmlUtils.appendElement(personElement, NS_NC, "PersonBirthDate");
				
				Element birthDateValEl = XmlUtils.appendElement(birthDateEl, NS_NC, "Date");
				
				String dob = dobElement.getTextContent();				
				dob = dob.trim();
				
				birthDateValEl.setTextContent(dob);
				
			} else if (ageElement != null) {
				
				Element e = XmlUtils.appendElement(personElement, NS_NC, "PersonAgeMeasure");
				e = XmlUtils.appendElement(e, NS_NC, "MeasurePointValue");
				e.setTextContent(ageElement.getTextContent());
			}

			
			String sEyeColor = xPaths.eyeColorCodeXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.eyeColorCodeXPath);
			
			if(StringUtils.isNotEmpty(sEyeColor)){
				
				Element eyeColorElement = XmlUtils.appendElement(personElement, NS_NC, "PersonEyeColorCode");
				
				eyeColorElement.setTextContent(sEyeColor);				
			}
			
			String sHairColor = xPaths.hairColorCodeXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.hairColorCodeXPath);
			
			if(StringUtils.isNotEmpty(sHairColor)){
				
				Element hairColorElement = XmlUtils.appendElement(personElement, NS_NC, "PersonHairColorCode");
				
				hairColorElement.setTextContent(sHairColor);
			}			
			
			Element heightElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.heightXPath);
			if (heightElement != null) {
				Element phm = XmlUtils.appendElement(personElement, NS_NC, "PersonHeightMeasure");
				Element e = XmlUtils.appendElement(phm, NS_NC, "MeasurePointValue");
				e.setTextContent(heightElement.getTextContent());
				e = XmlUtils.appendElement(phm, NS_NC, "LengthUnitCode");
				e.setTextContent("INH");
			}
			Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.lastNameXPath);
			Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.firstNameXPath);
			Element middleNameElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.middleNameXPath);
			if (lastNameElement != null || firstNameElement != null || middleNameElement != null) {
				Element nameElement = XmlUtils.appendElement(personElement, NS_NC, "PersonName");
				Element e = null;
				if (firstNameElement != null) {
					e = XmlUtils.appendElement(nameElement, NS_NC, "PersonGivenName");
					e.setTextContent(firstNameElement.getTextContent());
				}
				if (middleNameElement != null) {
					e = XmlUtils.appendElement(nameElement, NS_NC, "PersonMiddleName");
					e.setTextContent(middleNameElement.getTextContent());
				}
				if (lastNameElement != null) {
					e = XmlUtils.appendElement(nameElement, NS_NC, "PersonSurName");
					e.setTextContent(lastNameElement.getTextContent());
				}
			}
			
			
			String sPhysicalFeature = xPaths.physicalFeatureXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.physicalFeatureXPath);
			
			if(StringUtils.isNotEmpty(sPhysicalFeature)){

				sPhysicalFeature = sPhysicalFeature.trim();
				
				Element physFeatureEl = XmlUtils.appendElement(personElement, NS_NC, "PersonPhysicalFeature");
				
				Element physFeatDescEl = XmlUtils.appendElement(physFeatureEl, NS_NC, "PhysicalFeatureDescriptionText");
				
				physFeatDescEl.setTextContent(sPhysicalFeature);				
			}												
			
			
			Element raceElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.raceXPath);
			if (raceElement != null) {
				Element e = XmlUtils.appendElement(personElement, NS_NC, "PersonRaceCode");
				e.setTextContent(raceElement.getTextContent());
			}
			
			Element sexElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.sexXPath);
			if (sexElement != null) {
				Element e = XmlUtils.appendElement(personElement, NS_NC, "PersonSexCode");
				e.setTextContent(sexElement.getTextContent());
			}
			Element ssnElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.ssnXPath);
			if (ssnElement != null) {
				Element e = XmlUtils.appendElement(personElement, NS_NC, "PersonSSNIdentification");
				e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
				e.setTextContent(ssnElement.getTextContent());
			}
			Element weightElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.weightXPath);
			if (weightElement != null) {
				Element phm = XmlUtils.appendElement(personElement, NS_NC, "PersonWeightMeasure");
				Element e = XmlUtils.appendElement(phm, NS_NC, "MeasurePointValue");
				e.setTextContent(weightElement.getTextContent());
				e = XmlUtils.appendElement(phm, NS_NC, "WeightUnitCode");
				e.setTextContent("LBR");
			}
			Element dlElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.dlXPath);
			Element fbiElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.fbiXPath);
			Element sidElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.sidXPath);
			if (dlElement != null || fbiElement != null || sidElement != null) {
				Element personAugElement = XmlUtils.appendElement(personElement, NS_JXDM_41, "PersonAugmentation");
				if (dlElement != null) {
					Element dlJurisdictionElement = (Element) XmlUtils.xPathNodeSearch(specificDetailSourceDoc, xPaths.dlJurisdictionXPath);
					Element e = XmlUtils.appendElement(personAugElement, NS_NC, "DriverLicense");
					Element dlIdElement = XmlUtils.appendElement(e, NS_NC, "DriverLicenseIdentification");
					e = XmlUtils.appendElement(dlIdElement, NS_NC, "IdentificationID");
					e.setTextContent(dlElement.getTextContent());
					if (dlJurisdictionElement != null) {
						e = XmlUtils.appendElement(dlIdElement, NS_NC, "IdentificationSourceText");
						e.setTextContent(dlJurisdictionElement.getTextContent());
					}
				}
				if (fbiElement != null) {
					Element e = XmlUtils.appendElement(personAugElement, NS_JXDM_41, "PersonFBIIdentification");
					e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
					e.setTextContent(fbiElement.getTextContent());
				}				
				
				if (sidElement != null) {
					Element e = XmlUtils.appendElement(personAugElement, NS_JXDM_41, "PersonStateFingerprintIdentification");
					e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
					e.setTextContent(sidElement.getTextContent());
				}
			}

						
			Element locationElement = XmlUtils.appendElement(psrElement, NS_NC, "Location");
			
			Element locAddressEl = XmlUtils.appendElement(locationElement, NS_NC, "LocationAddress");
			
			Element structAddressEl = XmlUtils.appendElement(locAddressEl, NS_NC, "StructuredAddress");

			
			String streetNumber = xPaths.addressStreetNumberXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.addressStreetNumberXPath);			
			
			String sAddressStreetName = xPaths.addressStreetNameXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.addressStreetNameXPath);			

			
			boolean hasStreetNumber = StringUtils.isNotEmpty(streetNumber);
			
			boolean hasStreetName = StringUtils.isNotEmpty(sAddressStreetName);						
			
			if(hasStreetNumber && hasStreetName){

				Element locStreetEl = XmlUtils.appendElement(structAddressEl, NS_NC, 
						"LocationStreet");
								
				Element streetNumEl = XmlUtils.appendElement(locStreetEl, NS_NC, 
						"StreetNumberText");				
				streetNumber = streetNumber.trim();				
				streetNumEl.setTextContent(streetNumber);
				
								
				Element streetNameEl = XmlUtils.appendElement(locStreetEl, NS_NC, 
						"StreetName");				
				sAddressStreetName = sAddressStreetName.trim();				
				streetNameEl.setTextContent(sAddressStreetName);								
			}

			
			String sCity = xPaths.addressCityXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.addressCityXPath);
			
			if(StringUtils.isNotEmpty(sCity)){
			
				sCity = sCity.trim();
				
				Element cityEl = XmlUtils.appendElement(structAddressEl, NS_NC, "LocationCityName");
				
				cityEl.setTextContent(sCity);
			}
			
			String sStateCode = xPaths.addressStateXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.addressStateXPath);
			
			if(StringUtils.isNotEmpty(sStateCode)){
				
				sStateCode = sStateCode.trim();
				
				Element stateEl = XmlUtils.appendElement(structAddressEl, NS_NC, "LocationStateFIPS5-2AlphaCode");
				
				stateEl.setTextContent(sStateCode);
			}
			
			String sZipCode = xPaths.addressZipXPath == null ? null : 
				XmlUtils.xPathStringSearch(specificDetailSourceDoc, xPaths.addressZipXPath);
			
			if(StringUtils.isNotEmpty(sZipCode)){
				
				sZipCode = sZipCode.trim();
				
				Element postalCodeEl = XmlUtils.appendElement(structAddressEl, NS_NC, "LocationPostalCode");
				
				postalCodeEl.setTextContent(sZipCode);
			}
			
			Element sourceSystem = XmlUtils.appendElement(psrElement, NS_PERSON_SEARCH_RESULTS_EXT, "SourceSystemNameText");
			sourceSystem.setTextContent(xPaths.searchSystemId);
			Element sourceSystemIdentifierParentElement = XmlUtils.appendElement(psrElement, NS_INTEL, "SystemIdentifier");
			Element e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_NC, "IdentificationID");
			e.setTextContent(xPaths.getSystemIdentifier(instanceWrapper));
			e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_INTEL, "SystemName");
			e.setTextContent(xPaths.systemName);
			e = XmlUtils.appendElement(psrElement, NS_PERSON_SEARCH_RESULTS_EXT, "SearchResultCategoryText");
			e.setTextContent(xPaths.recordType);

		}

		XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		return ret;

	}

	private Document getPersonSearchStaticErrorResponse(Document personSearchRequestMessage) throws Exception {
		PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
		Document ret = null;
		ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
		if ("AccessDenied".equals(psp.lastName)) {
			ret = errorResourceRetriever.getPersonSearchAccessDeniedDocument();
		} else if ("ErrorReported".equals(psp.lastName)) {
			ret = errorResourceRetriever.getPersonSearchErrorReportedDocument();
		}
		return ret;
	}

	private Document getIncidentSearchStaticErrorResponse(Document incidentSearchRequestMessage) throws Exception {
		String incidentNumber = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
		Document ret = null;
		ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
		if ("AccessDenied".equals(incidentNumber)) {
			ret = errorResourceRetriever.getIncidentSearchAccessDeniedDocument();
		}
		return ret;
	}

	private Document getFirearmSearchStaticErrorResponse(Document firearmSearchRequestMessage) throws Exception {
		String firearmSerialNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
		Document ret = null;
		ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
		if ("AccessDenied".equals(firearmSerialNumber)) {
			ret = errorResourceRetriever.getFirearmSearchAccessDeniedDocument();
		}
		return ret;
	}

	List<IdentifiableDocumentWrapper> incidentPersonSearchDocumentsAsList(Document incidentPersonSearchRequestMessage, DateTime baseDate) throws Exception {

		Element rootElement = incidentPersonSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		if (!(NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentPersonSearchRequest".equals(rootLocalName))) {
			throw new IllegalArgumentException("Invalid message, must have {" + NS_INCIDENT_SEARCH_REQUEST_DOC + "}IncidentPersonSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}

		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "isr:SourceSystemNameText");
		int systemElementCount;
		if (systemElements == null || (systemElementCount = systemElements.getLength()) == 0) {
			throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
		}

		Element personIdElement = (Element) XmlUtils.xPathNodeSearch(incidentPersonSearchRequestMessage, "/isr-doc:IncidentPersonSearchRequest/nc:Person/nc:PersonOtherIdentification/nc:IdentificationID");

		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

		if (personIdElement != null) {

			String id = personIdElement.getTextContent();

			for (int i = 0; i < systemElementCount; i++) {
				for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

					Document d = dw.getDocument();
					Element documentPersonIdElement = (Element) XmlUtils
							.xPathNodeSearch(d,
									"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person/nc:PersonOtherIdentification/nc:IdentificationID");
					String documentPersonId = documentPersonIdElement.getTextContent();
					if (id.equals(documentPersonId)) {
						ret.add(dw);
					}
				}
			}

		}

		return ret;

	}

	static String buildIncidentSearchXPathFromIncidentSearchMessage(Document incidentSearchRequestMessage) throws Exception {

		String incidentNumber = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
		String incidentCategory = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
		String city = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location/nc:LocationAddress/isr:StructuredAddress/incident-location-codes-demostate:LocationCityTownCode");

		List<String> conditions = new ArrayList<String>();

		if (incidentNumber != null && incidentNumber.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityIdentification/nc:IdentificationID='" + incidentNumber + "'");
		}

		if (city != null && city.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityLocation/nc:Location[nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName='" + city
					+ "' and @s:id=/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref]");
		}

		if (incidentCategory != null && incidentCategory.trim().length() > 0) {
			conditions.add("lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Incident[inc-ext:IncidentCategoryCode='" + incidentCategory + "']");
		}

		if (conditions.isEmpty()) {
			return null;
		}

		StringBuffer xPath = new StringBuffer();
		xPath.append("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[");

		for (String condition : conditions) {
			xPath.append(condition).append(" and ");
		}
		xPath.setLength(xPath.length() - 5);

		xPath.append("]");

		return xPath.toString();
	}

	
	static String buildCustodySearchXpathFromMessage(Document custodySearchReqDoc) throws Exception{
	
		String sid = XmlUtils.xPathStringSearch(custodySearchReqDoc, 
				"//cs-req-doc:CustodySearchRequest/cs-req-ext:Person/jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID");		
		
		String xpath = "/cq-res-exch:CustodyQueryResults[cq-res-ext:InmateCustody/nc30:PersonStateIdentification/nc30:IdentificationID='" + sid + "']";		
		
		return xpath;
	}
	
	
	static String buildIncidentSearchXPathFromVehicleSearchMessage(Document vehicleSearchRequestMessage) throws Exception {

		String vin = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/nc:VehicleIdentification/nc:IdentificationID");
		String color = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/nc:VehicleColorPrimaryCode");
		String model = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/nc:ItemModelName");
		String plate = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationID");
		String make = XmlUtils.xPathStringSearch(vehicleSearchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/vsr:VehicleMakeCode");

		List<String> conditions = new ArrayList<String>();

		if (vin != null && vin.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:VehicleIdentification/nc:IdentificationID='" + vin + "'");
		}
		if (plate != null && plate.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationID='" + plate + "'");
		}
		if (color != null && color.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:VehicleColorPrimaryCode='" + color + "'");
		}
		if (model != null && model.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:VehicleModelCode='" + model + "'");
		}
		if (make != null && make.trim().length() > 0) {
			conditions.add("lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/nc:VehicleMakeCode='" + make + "'");
		}

		if (conditions.isEmpty()) {
			return null;
		}

		StringBuffer xPath = new StringBuffer();
		xPath.append("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[");

		for (String condition : conditions) {
			xPath.append(condition).append(" and ");
		}
		xPath.setLength(xPath.length() - 5);

		xPath.append("]");

		return xPath.toString();
	}

	static String buildFirearmSearchXPathFromMessage(Document firearmSearchRequestMessage) throws Exception {

		String firearmSerialNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
		String firearmMakeCode = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
		String firearmMakeText = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
		String firearmModel = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
		String firearmCategoryCode = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
		String registrationNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
		String registrationCounty = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:LocationCountyName");

		List<String> firearmConditions = new ArrayList<String>();
		List<String> firearmMakeConditions = new ArrayList<String>();
		List<String> registrationConditions = new ArrayList<String>();

		if (firearmSerialNumber != null && firearmSerialNumber.trim().length() > 0) {
			firearmConditions.add("nc:ItemSerialIdentification/nc:IdentificationID='" + firearmSerialNumber + "'");
		}
		if (firearmMakeCode != null && firearmMakeCode.trim().length() > 0) {
			firearmMakeConditions.add("firearms-codes-demostate:FirearmMakeCode='" + firearmMakeCode + "'");
		}
		if (firearmMakeText != null && firearmMakeText.trim().length() > 0) {
			firearmMakeConditions.add("firearm-ext:FirearmMakeText='" + firearmMakeText + "'");
		}
		if (firearmModel != null && firearmModel.trim().length() > 0) {
			firearmConditions.add("nc:ItemModelName='" + firearmModel + "'");
		}
		if (firearmCategoryCode != null && firearmCategoryCode.trim().length() > 0) {
			firearmConditions.add("nc:FirearmCategoryCode='" + firearmCategoryCode + "'");
		}

		if (registrationNumber != null && registrationNumber.trim().length() > 0) {
			registrationConditions.add("nc:RegistrationIdentification/nc:IdentificationID='" + registrationNumber + "'");
		}
		if (registrationCounty != null && registrationCounty.trim().length() > 0) {
			registrationConditions.add("nc:LocationCountyName='" + registrationCounty + "'");
		}

		if (firearmConditions.isEmpty() && registrationConditions.isEmpty()) {
			// no search parameters specified
			return null;
		}

		StringBuffer searchXPath = new StringBuffer("/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[");

		if (!firearmConditions.isEmpty()) {
			for (String condition : firearmConditions) {
				searchXPath.append(condition).append(" and ");
			}
			searchXPath.setLength(searchXPath.length() - 5);
		}

		if (!firearmMakeConditions.isEmpty()) {
			if (!firearmConditions.isEmpty()) {
				searchXPath.append(" and ");
			}
			searchXPath.append("(");
			for (String condition : firearmMakeConditions) {
				searchXPath.append(condition).append(" or ");
			}
			searchXPath.setLength(searchXPath.length() - 4);
			searchXPath.append(")");
		}

		if (!registrationConditions.isEmpty()) {
			if (!firearmMakeConditions.isEmpty() || !firearmConditions.isEmpty()) {
				searchXPath.append(" and ");
			}
			searchXPath.append("@s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[");
			for (String condition : registrationConditions) {
				searchXPath.append(condition).append(" and ");
			}
			searchXPath.setLength(searchXPath.length() - 5);
			searchXPath.append("]/@s:id]/nc:ItemReference/@s:ref");
		}

		searchXPath.append("]");
		
		String sSearchXpath = searchXPath.toString();
				
		return sSearchXpath;

	}
	
	Document vehicleSearchDocuments(Document searchRequestMessage, DateTime baseDate) throws Exception {
		
		Document errorReturn = getVehicleSearchAccessDeniedDocument(searchRequestMessage);

		if (errorReturn != null) {
			return errorReturn;
		}

		Document ret = createNewDocument();

		List<IdentifiableDocumentWrapper> searchResultsList = vehicleSearchDocumentsAsList(searchRequestMessage, baseDate);
		
		Element root = ret.createElementNS(NS_VEHICLE_SEARCH_RESULTS_EXCHANGE, "VehicleSearchResults");
		ret.appendChild(root);
		String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(NS_VEHICLE_SEARCH_RESULTS_EXCHANGE);
		root.setPrefix(prefix);
		
		for (IdentifiableDocumentWrapper instanceWrapper : searchResultsList) {

			Document instance = instanceWrapper.getDocument();
			
			Element incidentVehicle = (Element) XmlUtils.xPathNodeSearch(instance, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle");
			
			Element vehicleSearchResultElement = XmlUtils.appendElement(root, NS_VEHICLE_SEARCH_RESULTS, "VehicleSearchResult");
			Element vehicleElement = XmlUtils.appendElement(vehicleSearchResultElement, NS_VEHICLE_SEARCH_RESULTS, "Vehicle");
			
			XmlUtils.appendElement(vehicleElement, NS_NC, "ItemCategoryText").setTextContent("Passenger Vehicle");
			XmlUtils.appendElement(vehicleElement, NS_NC, "VehicleColorPrimaryCode").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:VehicleColorPrimaryCode"));
			XmlUtils.appendElement(vehicleElement, NS_NC, "ItemModelName").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:VehicleModelCode"));
			XmlUtils.appendElement(vehicleElement, NS_NC, "ItemModelYearDate").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ItemModelYearDate"));
			Element e = XmlUtils.appendElement(vehicleElement, NS_NC, "ConveyanceRegistrationPlateIdentification");
			XmlUtils.appendElement(e, NS_NC, "IdentificationID").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationID"));
			Element expDate = XmlUtils.appendElement(e, NS_NC, "IdentificationExpirationDate");
			XmlUtils.appendElement(expDate, NS_NC, "Date").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationExpirationDate/nc:Date"));
			XmlUtils.appendElement(e, NS_JXDM_41, "IdentificationJurisdictionUSPostalServiceCode").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ConveyanceRegistrationPlateIdentification/jxdm40:IdentificationJurisdictionUSPostalServiceCode"));
			XmlUtils.appendElement(vehicleElement, NS_NC, "VehicleDoorQuantity").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:VehicleDoorQuantity"));
			e = XmlUtils.appendElement(vehicleElement, NS_NC, "VehicleIdentification");
			String vin = XmlUtils.xPathStringSearch(incidentVehicle, "nc:VehicleIdentification/nc:IdentificationID");
			XmlUtils.appendElement(e, NS_NC, "IdentificationID").setTextContent(vin);
			e = XmlUtils.appendElement(vehicleElement, NS_NC, "ConveyanceRegistration");
			XmlUtils.appendElement(e, NS_NC, "ConveyanceRegistrationPlateCategoryCode").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ConveyanceRegistration/nc:ConveyanceRegistrationPlateCategoryCode"));
			XmlUtils.appendElement(e, NS_NC, "ConveyanceRegistrationPlateCategoryText").setTextContent(XmlUtils.xPathStringSearch(incidentVehicle, "nc:ConveyanceRegistration/nc:ConveyanceRegistrationPlateCategoryText"));
			
			// we cannot populate vehicle make because of a mismatch of codes
			
			Element sourceSystem = XmlUtils.appendElement(vehicleSearchResultElement, NS_VEHICLE_SEARCH_RESULTS, "SourceSystemNameText");
			sourceSystem.setTextContent(INCIDENT_MOCK_ADAPTER_VEHICLE_SEARCH_SYSTEM_ID);
			Element sourceSystemIdentifierParentElement = XmlUtils.appendElement(vehicleSearchResultElement, NS_INTEL, "SystemIdentifier");
			XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_NC, "IdentificationID").setTextContent(vin);
			XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_INTEL, "SystemName").setTextContent("Demo RMS");
		}

		XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		
		return ret;
		
	}

	private Document getVehicleSearchAccessDeniedDocument(Document searchRequestMessage) throws Exception {
		String vin = XmlUtils.xPathStringSearch(searchRequestMessage, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle/nc:VehicleIdentification/nc:IdentificationID");
		Document ret = null;
		ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
		if ("AccessDenied".equals(vin)) {
			ret = errorResourceRetriever.getVehicleSearchAccessDeniedDocument();
		}
		return ret;
	}

	Document firearmSearchDocuments(Document firearmSearchRequestMessage) throws Exception {

		Document errorReturn = getFirearmSearchStaticErrorResponse(firearmSearchRequestMessage);

		if (errorReturn != null) {
			return errorReturn;
		}

		Document ret = createNewDocument();

		List<IdentifiableDocumentWrapper> searchResultsList = firearmSearchDocumentsAsList(firearmSearchRequestMessage);

		Element root = ret.createElementNS(NS_FIREARM_SEARCH_RESULT_DOC, "FirearmSearchResults");
		ret.appendChild(root);
		String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(NS_FIREARM_SEARCH_RESULT_DOC);
		root.setPrefix(prefix);

		int index = 1;

		for (IdentifiableDocumentWrapper instanceWrapper : searchResultsList) {

			Document instance = instanceWrapper.getDocument();

			Element fsrElement = XmlUtils.appendElement(root, NS_FIREARM_SEARCH_RESULT_EXT, "FirearmSearchResult");

			Element personElement = XmlUtils.appendElement(fsrElement, NS_NC, "Person");
			XmlUtils.addAttribute(personElement, NS_STRUCTURES, "id", createFirearmPersonElementID(index));
			Element personNameElement = XmlUtils.appendElement(personElement, NS_NC, "PersonName");
			String s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonGivenName");
			Element e = null;
			if (s != null) {
				e = XmlUtils.appendElement(personNameElement, NS_NC, "PersonGivenName");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonMiddleName");
			if (s != null) {
				e = XmlUtils.appendElement(personNameElement, NS_NC, "PersonMiddleName");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonSurName");
			if (s != null) {
				e = XmlUtils.appendElement(personNameElement, NS_NC, "PersonSurName");
				e.setTextContent(s);
			}

			Element firearmElement = XmlUtils.appendElement(fsrElement, NS_FIREARM_SEARCH_RESULT_EXT, "Firearm");
			XmlUtils.addAttribute(firearmElement, NS_STRUCTURES, "id", createFirearmElementID(index));

			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "ItemSerialIdentification");
				e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemModelName");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "ItemModelName");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCategoryCode");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "FirearmCategoryCode");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCategoryDescriptionCode");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "FirearmCategoryDescriptionCode");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCaliberCode");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "FirearmCaliberCode");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmGaugeText");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_NC, "FirearmGaugeText");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_FIREARMS_CODES_DEMOSTATE, "FirearmMakeCode");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearm-ext:FirearmStatus/firearm-ext:FirearmStatusText");
			if (s != null) {
				e = XmlUtils.appendElement(firearmElement, NS_FIREARM_SEARCH_RESULT_EXT, "FirearmStatus");
				e = XmlUtils.appendElement(e, NS_FIREARM_SEARCH_RESULT_EXT, "FirearmStatusText");
				e.setTextContent(s);
			}

			Element registrationElement = XmlUtils.appendElement(fsrElement, NS_FIREARM_SEARCH_RESULT_EXT, "ItemRegistration");
			XmlUtils.addAttribute(registrationElement, NS_STRUCTURES, "id", createFirearmRegistrationElementID(index));
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
			if (s != null) {
				e = XmlUtils.appendElement(registrationElement, NS_NC, "RegistrationIdentification");
				e = XmlUtils.appendElement(e, NS_NC, "IdentificationID");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:LocationCountyName");
			if (s != null) {
				e = XmlUtils.appendElement(registrationElement, NS_NC, "LocationCountyName");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationEffectiveDate/nc:Date");
			if (s != null) {
				e = XmlUtils.appendElement(registrationElement, NS_NC, "RegistrationEffectiveDate");
				e = XmlUtils.appendElement(e, NS_NC, "Date");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/firearm-ext:RegistrationStatus/firearm-ext:FirearmRegistrationStatusText");
			if (s != null) {
				e = XmlUtils.appendElement(registrationElement, NS_FIREARM_SEARCH_RESULT_EXT, "RegistrationStatus");
				e = XmlUtils.appendElement(e, NS_FIREARM_SEARCH_RESULT_EXT, "FirearmRegistrationStatusText");
				e.setTextContent(s);
			}
			s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/firearm-ext:RegistrationNotesText");
			if (s != null) {
				e = XmlUtils.appendElement(registrationElement, NS_FIREARM_SEARCH_RESULT_EXT, "RegistrationNotesText");
				e.setTextContent(s);
			}

			Element propertyRegistrationAssociationElement = XmlUtils.appendElement(fsrElement, NS_NC, "PropertyRegistrationAssociation");
			e = XmlUtils.appendElement(propertyRegistrationAssociationElement, NS_NC, "ItemRegistrationReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", createFirearmRegistrationElementID(index));
			e = XmlUtils.appendElement(propertyRegistrationAssociationElement, NS_NC, "ItemReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", createFirearmElementID(index));
			e = XmlUtils.appendElement(propertyRegistrationAssociationElement, NS_NC, "ItemRegistrationHolderReference");
			XmlUtils.addAttribute(e, NS_STRUCTURES, "ref", createFirearmPersonElementID(index));

			Element sourceSystem = XmlUtils.appendElement(fsrElement, NS_FIREARM_SEARCH_RESULT_EXT, "SourceSystemNameText");
			sourceSystem.setTextContent(FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID);
			Element sourceSystemIdentifierParentElement = XmlUtils.appendElement(fsrElement, NS_INTEL, "SystemIdentifier");
			e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_NC, "IdentificationID");
			e.setTextContent(instanceWrapper.getId());
			e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, NS_INTEL, "SystemName");
			e.setTextContent("Statewide Firearms Registry");

			index++;

		}

		XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		return ret;

	}

	static String createFirearmElementID(int positionInDocument) {
		return "F" + positionInDocument;
	}

	static String createFirearmPersonElementID(int positionInDocument) {
		return "P" + positionInDocument;
	}

	static String createFirearmRegistrationElementID(int positionInDocument) {
		return "R" + positionInDocument;
	}

	List<IdentifiableDocumentWrapper> firearmSearchDocumentsAsList(Document firearmSearchRequestMessage) throws Exception {

		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

		Element rootElement = firearmSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		if (!(NS_FIREARM_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "FirearmSearchRequest".equals(rootLocalName))) {
			throw new IllegalArgumentException("Invalid message, must have {" + NS_FIREARM_SEARCH_REQUEST_DOC + "}FirearmSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}
		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "firearm-search-req-ext:SourceSystemNameText");
		if (systemElements == null || (systemElements.getLength()) == 0) {
			throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
		}

		String searchXPath = buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
		//LOG.info(searchXPath);

		if (searchXPath == null) {
			return ret;
		}

		for (IdentifiableDocumentWrapper dw : firearmRegistrationDataSource.getDocuments()) {

			Document d = dw.getDocument();
			LOG.debug("Searching document " + dw.getId());

			NodeList matches = XmlUtils.xPathNodeListSearch(d, searchXPath);
			for (int i = 0; i < matches.getLength(); i++) {
				Node match = matches.item(i);
				String firearmId = XmlUtils.xPathStringSearch(match, "@s:id");
				ret.add(new IdentifiableDocumentWrapper(createFirearmRegistrationDocument(dw.getDocument(), firearmId), dw.getId() + ":" + firearmId));
			}

		}
		return ret;
	}

	List<IdentifiableDocumentWrapper> personSearchDocumentsAsList(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		
		Element rootElement = personSearchRequestMessage.getDocumentElement();
		String rootNamespaceURI = rootElement.getNamespaceURI();
		String rootLocalName = rootElement.getLocalName();
		
		if (!(NS_PERSON_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "PersonSearchRequest".equals(rootLocalName))) {
			
			throw new IllegalArgumentException("Invalid message, must have {" + NS_PERSON_SEARCH_REQUEST_DOC 
					+ "}PersonSearchRequest as the root " + "instead of {" + rootNamespaceURI + "}" + rootLocalName);
		}
		
		NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "psr:SourceSystemNameText");
		int systemElementCount;
		
		if (systemElements == null || (systemElementCount = systemElements.getLength()) == 0) {
			throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
		}
		
		List<IdentifiableDocumentWrapper> rDocList = new ArrayList<IdentifiableDocumentWrapper>();
		
		for (int i = 0; i < systemElementCount; i++) {
			
			Element systemElement = (Element) systemElements.item(i);
			
			String systemId = systemElement.getTextContent();
			
			if (CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
				rDocList.addAll(personSearchCriminalHistoryDocuments(personSearchRequestMessage, baseDate));
				
			} else if (WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
				rDocList.addAll(personSearchWarrantDocuments(personSearchRequestMessage, baseDate));
				
			} else if (FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
				rDocList.addAll(personSearchFirearmRegistrationDocuments(personSearchRequestMessage, baseDate));
				
			} else if (INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
				rDocList.addAll(personSearchIncidentDocuments(personSearchRequestMessage, baseDate));
				
			} else if (JUVENILE_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
				rDocList.addAll(personSearchJuvenileHistoryDocuments(personSearchRequestMessage, baseDate));
				
			}else if(CUSTODY_PERSON_SEARCH_SYSTEM_ID.equals(systemId)){	
				rDocList.addAll(personSearchCustodyDocuments(personSearchRequestMessage, baseDate));								
				
			}else if(COURT_CASE_PERSON_SEARCH_SYSTEM_ID.equals(systemId)){
				rDocList.addAll(personSearchCourtCaseDocuments(personSearchRequestMessage, baseDate));
				
			}else if(VEHICLE_CRASH_SEARCH_SYSTEM_ID.equals(systemId)){
				rDocList.addAll(personSearchVehicleCrashDocuments(personSearchRequestMessage, baseDate));
				
			}else if(FIREARM_PROHIBITION_SEARCH_SYSTEM_ID.equals(systemId)){
				rDocList.addAll(personSearchFirearmProhibitionDocuments(personSearchRequestMessage, baseDate));
				
			} else {
				throw new IllegalArgumentException("Unsupported system name: " + systemId);
			}
		}
		return rDocList;
	}

	private List<IdentifiableDocumentWrapper> personSearchJuvenileHistoryDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getJuvenileHistoryXPaths(), juvenileHistoryDataSource);
	}

	private List<IdentifiableDocumentWrapper> personSearchIncidentDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getIncidentXPaths(), incidentDataSource);
	}

	private List<IdentifiableDocumentWrapper> personSearchWarrantDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getWarrantXPaths(), warrantDataSource);
	}

	private List<IdentifiableDocumentWrapper> personSearchFirearmRegistrationDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getFirearmRegistrationXPaths(), firearmRegistrationDataSource);
	}

	private List<IdentifiableDocumentWrapper> personSearchCriminalHistoryDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getCriminalHistoryXPaths(), criminalHistoryDataSource);
	}
	
	private List<IdentifiableDocumentWrapper> personSearchCustodyDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getCustodyXPaths(), custodyDataSource);
	}	
	
	private List<IdentifiableDocumentWrapper> personSearchCourtCaseDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
		
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getCourtCaseXPaths(), courtCaseDataSource);
	}		
	
	private List<IdentifiableDocumentWrapper> personSearchVehicleCrashDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception{
		
		// loops through all VehicleCrashDetail docs in VehicleCrashDataSource, using VehicleCrashXPaths against them to pull their 
		//	 vehicle values - and compares them against the same values in the personSearchRequestMessage
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getVehicleCrashXPaths(), vehicleCrashDataSource);
	}
	
	private List<IdentifiableDocumentWrapper> personSearchFirearmProhibitionDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception{
		return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getFirearmProhibitionXPaths(), firearmProhibitionDataSource);
	}
	

	private SearchValueXPaths getIncidentXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths() {
			public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
				Document d = documentWrapper.getDocument();
				try {
					Element e = (Element) XmlUtils
							.xPathNodeSearch(d,
									"/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person/nc:PersonOtherIdentification/nc:IdentificationID");
					return e.getTextContent();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		String rootXPath = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person";
		xPaths.ageXPath = rootXPath + "/nc:PersonAgeMeasure/nc:MeasurePointValue";
		xPaths.birthdateXPath = rootXPath + "/nc:PersonBirthDate/nc:Date";
		xPaths.ssnXPath = rootXPath + "/nc:PersonSSNIdentification/nc:IdentificationID";
		xPaths.sidXPath = rootXPath + "/jxdm40:PersonAugmentation/jxdm40:PersonStateFingerprintIdentification/nc:IdentificationID";
		xPaths.fbiXPath = rootXPath + "/jxdm40:PersonAugmentation/jxdm40:PersonFBIIdentification/nc:IdentificationID";
		xPaths.dlXPath = rootXPath + "/jxdm40:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID";
		xPaths.dlJurisdictionXPath = rootXPath + "/jxdm40:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/jxdm40:IdentificationJurisdictionNCICLISCode";
		xPaths.lastNameXPath = rootXPath + "/nc:PersonAlternateName/nc:PersonSurName";
		xPaths.middleNameXPath = null;
		xPaths.firstNameXPath = rootXPath + "/nc:PersonName/nc:PersonGivenName";
		xPaths.eyeColorXPath = rootXPath + "/nc:PersonEyeColorText";
		xPaths.hairColorXPath = rootXPath + "/nc:PersonHairColorText";
		xPaths.raceXPath = rootXPath + "/nc:PersonRaceText";
		xPaths.sexXPath = rootXPath + "/nc:PersonSexCode";
		xPaths.heightXPath = rootXPath + "/nc:PersonHeightMeasure/nc:MeasurePointValue";
		xPaths.weightXPath = rootXPath + "/nc:PersonWeightMeasure/nc:MeasurePointValue";
		xPaths.searchSystemId = INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Demo RMS";
		xPaths.recordType = "Incident";
		return xPaths;
	}

	private SearchValueXPaths getFirearmProhibitionXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths();
		String rootXPath = "/fppq-res-doc:FirearmPurchaseProhibitionQueryResults/fppq-res-ext:FirearmPurchaseProhibitionReport/nc30:Person"
				+ "[@s30:id = /fppq-res-doc:FirearmPurchaseProhibitionQueryResults/fppq-res-ext:FirearmPurchaseProhibitionReport/jxdm51:ActivityCourtOrderAssociation/jxdm51:Subject/@s30:ref]";
		xPaths.ageXPath = null;
		xPaths.birthdateXPath = rootXPath + "/nc30:PersonBirthDate/nc30:Date";
		xPaths.ssnXPath = null;
		xPaths.sidXPath = null;
		xPaths.fbiXPath = null;
		xPaths.dlXPath = null;
		xPaths.dlJurisdictionXPath = null;
		xPaths.lastNameXPath = rootXPath + "/nc30:PersonName/nc30:PersonSurName";
		xPaths.middleNameXPath = rootXPath + "/nc30:PersonName/nc30:PersonMiddleName";
		xPaths.firstNameXPath = rootXPath + "/nc30:PersonName/nc30:PersonGivenName";
		xPaths.eyeColorXPath = null;
		xPaths.hairColorXPath = null;
		xPaths.raceXPath = null;
		xPaths.sexXPath = rootXPath + "/jxdm51:PersonSexCode";
		xPaths.heightXPath = null;
		xPaths.weightXPath = null;
		xPaths.searchSystemId = FIREARM_PROHIBITION_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Firearm Purchase Prohibition";
		xPaths.recordType = "Firearm Purchase Prohibition";
		return xPaths;
	}
	
	private SearchValueXPaths getJuvenileHistoryXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths();
		xPaths.ageXPath = null;
		xPaths.birthdateXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/nc30:PersonBirthDate/nc30:Date";
		xPaths.ssnXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/nc30:PersonSSNIdentification/nc30:IdentificationID";
		xPaths.sidXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/jxdm50:PersonStateFingerprintIdentification/nc30:IdentificationID";
		xPaths.fbiXPath = null;
		xPaths.dlXPath = null;
		xPaths.dlJurisdictionXPath = null;
		xPaths.lastNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='provided']/nc30:PersonSurName";
		xPaths.middleNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='provided']/nc30:PersonMiddleName";
		xPaths.firstNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='provided']/nc30:PersonGivenName";
		xPaths.eyeColorXPath = null;
		xPaths.hairColorXPath = null;
		xPaths.raceXPath = null;
		xPaths.sexXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/nc30:PersonSexCode";
		xPaths.heightXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/nc30:PersonHeightMeasure/nc30:MeasurePointValue";
		xPaths.weightXPath = "/jh-container:JuvenileHistoryContainer/jh-container:AdditionalChildInformation/nc30:PersonWeightMeasure/nc30:MeasurePointValue";

		xPaths.juvenilePlacementsXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement/jh-placement-codes:PlacementCategoryCode";
		xPaths.addressStreetXPath = "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id='Residence-K']/nc30:Address/nc30:LocationStreet/nc30:StreetFullText";
		xPaths.addressCityXPath = "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id='Residence-K']/nc30:Address/nc30:LocationCityName";
		xPaths.addressStateXPath = "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id='Residence-K']/nc30:Address/nc30:LocationStateFIPS5-2AlphaCode";
		xPaths.addressZipXPath = "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id='Residence-K']/nc30:Address/nc30:LocationPostalCode";
		xPaths.aliasFirstNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='alias']/nc30:PersonGivenName";
		xPaths.aliasMiddleNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='alias']/nc30:PersonMiddleName";
		xPaths.aliasLastNameXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='child']/nc30:PersonName[jxdm50:PersonNameCategoryCode='alias']/nc30:PersonSurName";
		xPaths.parentFirstNamesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='mother' or @s30:id='father']/nc30:PersonName/nc30:PersonGivenName";
		xPaths.parentMiddleNamesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='mother' or @s30:id='father']/nc30:PersonName/nc30:PersonMiddleName";
		xPaths.parentLastNamesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id='mother' or @s30:id='father']/nc30:PersonName/nc30:PersonSurName";

		xPaths.searchSystemId = JUVENILE_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Juvenile Court Case Management System";
		xPaths.recordType = "Juvenile History";
		return xPaths;
	}

	private SearchValueXPaths getWarrantXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths();
		xPaths.ageXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonAgeMeasure/nc:MeasurePointValue";
		xPaths.birthdateXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonBirthDate/nc:Date";
		xPaths.ssnXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonSSNIdentification/nc:IdentificationID";
		xPaths.sidXPath = null;
		xPaths.fbiXPath = null;
		xPaths.dlXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID";
		xPaths.dlJurisdictionXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText";
		xPaths.lastNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonSurName";
		xPaths.middleNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonMiddleName";
		xPaths.firstNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonGivenName";
		xPaths.eyeColorXPath = null;
		xPaths.hairColorXPath = null;
		xPaths.raceXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonRaceCode";
		xPaths.sexXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonSexCode";
		xPaths.heightXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonHeightMeasure/nc:MeasurePointValue";
		xPaths.weightXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonWeightMeasure/nc:MeasurePointValue";
		xPaths.searchSystemId = WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Statewide Warrant System";
		xPaths.recordType = "Bench Warrant";
		return xPaths;
	}

	private SearchValueXPaths getFirearmRegistrationXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths();
		xPaths.ageXPath = null;
		xPaths.birthdateXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonBirthDate/nc:Date";
		xPaths.ssnXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonSSNIdentification/nc:IdentificationID";
		xPaths.sidXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonStateIdentification/nc:IdentificationID";
		xPaths.fbiXPath = null;
		xPaths.dlXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID";
		xPaths.dlJurisdictionXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText";
		xPaths.lastNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonSurName";
		xPaths.middleNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonMiddleName";
		xPaths.firstNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonGivenName";
		xPaths.eyeColorXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonEyeColorCode";
		xPaths.hairColorXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonHairColorCode";
		xPaths.raceXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonRaceCode";
		xPaths.sexXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonSexCode";
		xPaths.heightXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonHeightMeasure/nc:MeasureText";
		xPaths.weightXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonWeightMeasure/nc:MeasureText";
		xPaths.searchSystemId = FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Statewide Firearm Registry";
		xPaths.recordType = "Firearm Registration";
		return xPaths;
	}

	private SearchValueXPaths getCriminalHistoryXPaths() {
		SearchValueXPaths xPaths = new SearchValueXPaths();
		xPaths.ageXPath = null; // CH does not have age, only birthdate
		xPaths.birthdateXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date";
		xPaths.ssnXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID";
		xPaths.sidXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID";
		xPaths.fbiXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID";
		xPaths.dlXPath = null; // ch does not have DL
		xPaths.dlJurisdictionXPath = null; // ch does not have DL
		xPaths.lastNameXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonName/nc:PersonSurName";
		xPaths.middleNameXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonName/nc:PersonMiddleName";
		xPaths.firstNameXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonName/nc:PersonGivenName";
		xPaths.eyeColorXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText";
		xPaths.hairColorXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText";
		xPaths.raceXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonRaceCode";
		xPaths.sexXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSexCode";
		xPaths.heightXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonHeightMeasure/nc:MeasurePointValue";
		xPaths.weightXPath = "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue";
		xPaths.searchSystemId = CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Criminal History";
		xPaths.recordType = "Criminal History";
		return xPaths;
	}
	
	SearchValueXPaths getCustodyXPaths(){
		
		SearchValueXPaths xPaths = new SearchValueXPaths(){
			public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
				Document d = documentWrapper.getDocument();
				try {
					Element e = (Element) XmlUtils
							.xPathNodeSearch(d,
									"/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID");
					return e.getTextContent();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
				
		xPaths.ageXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/nc30:PersonAgeMeasure/nc30:MeasureValueText";
		xPaths.birthdateXPath = "//nc30:PersonBirthDate";
		xPaths.ssnXPath = "//nc30:PersonSSNIdentification/nc30:IdentificationID";		
		xPaths.sidXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID";		
		xPaths.fbiXPath = "//jxdm51:PersonAugmentation/jxdm51:PersonFBIIdentification/nc30:IdentificationID";		
		xPaths.dlXPath = "//jxdm51:PersonAugmentation/jxdm51:DriverLicense/jxdm51:DriverLicenseCardIdentification/nc30:IdentificationID";		
		xPaths.dlJurisdictionXPath = "//jxdm51:PersonAugmentation/jxdm51:DriverLicense/jxdm51:DriverLicenseCardIdentification/nc30:IdentificationSourceText";		
		xPaths.lastNameXPath = "//nc30:PersonName/nc30:PersonSurName";
		xPaths.middleNameXPath = "//nc30:PersonName/nc30:PersonMiddleName";
		xPaths.firstNameXPath = "//nc30:PersonName/nc30:PersonGivenName";		
		xPaths.alternateNameIdentityNodeListXpath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Identity";				
		xPaths.eyeColorCodeXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonEyeColorCode";
		xPaths.hairColorCodeXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonHairColorCode";								
		xPaths.physicalFeatureXPath = 
				"/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/nc30:PersonPhysicalFeature/nc30:PhysicalFeatureDescriptionText";				
		xPaths.raceXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonRaceCode";  
		xPaths.sexXPath =  "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonSexCode"; 		
		xPaths.heightXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/nc30:PersonHeightMeasure/nc30:MeasureValueText";
		xPaths.weightXPath = "/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/nc30:PersonWeightMeasure/nc30:MeasureValueText";		
		xPaths.searchSystemId = CUSTODY_PERSON_SEARCH_SYSTEM_ID;		
		xPaths.systemName = "Custody";		
		xPaths.recordType = "Custody";			
		xPaths.addressStreetNumberXPath = 
				"//nc30:Location/nc30:Address/nc30:LocationStreet/nc30:StreetNumberText";		
		xPaths.addressStreetNameXPath = 
				"//nc30:Location/nc30:Address/nc30:LocationStreet/nc30:StreetName";		
		xPaths.addressCityXPath = 
				"//nc30:Location/nc30:Address/nc30:LocationCityName";				
		xPaths.addressStateXPath = 
				"//nc30:Location/nc30:Address/nc30:LocationStateUSPostalServiceCode";		
		xPaths.addressZipXPath = 
				"//nc30:Location/nc30:Address/nc30:LocationPostalCode";
		
		return xPaths;
	}
	
	private SearchValueXPaths getCourtCaseXPaths() {
		
		SearchValueXPaths xPaths = new SearchValueXPaths(){
			
			public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
				
				Document doc = documentWrapper.getDocument();
				
				try {
					String personRecId = XmlUtils.xPathStringSearch(doc, "//ccq-res-ext:PersonRecordIdentification/nc30:IdentificationID");
					
					return personRecId;
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};			
		
		xPaths.ageXPath = null;
		
		xPaths.birthdateXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonBirthDate";
		xPaths.ssnXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonSSNIdentification/nc30:IdentificationID";
		xPaths.sidXPath = "//jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID";
		xPaths.fbiXPath = "//jxdm51:PersonFBIIdentification/nc30:IdentificationID";
		xPaths.dlXPath = "//jxdm51:PersonAugmentation/jxdm51:DriverLicense/jxdm51:DriverLicenseIdentification/nc30:IdentificationID";
		xPaths.dlJurisdictionXPath = "//jxdm51:PersonAugmentation/jxdm51:DriverLicense/jxdm51:DriverLicenseIdentification/nc30:IdentificationSourceText";
		xPaths.lastNameXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonName/nc30:PersonSurName";
		xPaths.middleNameXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonName/nc30:PersonMiddleName";
		xPaths.firstNameXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonName/nc30:PersonGivenName";
		xPaths.eyeColorXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/jxdm51:PersonEyeColorCode";
		xPaths.hairColorXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/jxdm51:PersonHairColorCode";		
		xPaths.hairColorCodeXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/jxdm51:PersonHairColorCode";		
		xPaths.raceXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/jxdm51:PersonRaceCode";
		xPaths.sexXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/jxdm51:PersonSexCode";
		xPaths.heightXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonHeightMeasure/nc30:MeasureValueText";
		xPaths.weightXPath = "/ccq-res-doc:CourtCaseQueryResults/nc30:Person/nc30:PersonWeightMeasure/nc30:MeasureValueText";
		xPaths.searchSystemId = COURT_CASE_PERSON_SEARCH_SYSTEM_ID;
		xPaths.systemName = "Court Case";
		xPaths.recordType = "Court Case";

		return xPaths;

	}
	
	private SearchValueXPaths getVehicleCrashXPaths() throws Exception{
			
		SearchValueXPaths vehicleCrashDetailXpaths = new SearchValueXPaths();				
		
		vehicleCrashDetailXpaths.ageXPath = null;		
		vehicleCrashDetailXpaths.ssnXPath = null;
		vehicleCrashDetailXpaths.sidXPath = null;
		vehicleCrashDetailXpaths.fbiXPath = null;
						
		vehicleCrashDetailXpaths.dlXPath = 
				"//jxdm51:CrashDriverLicense[@s30:id=//jxdm51:CrashDriver/jxdm51:DriverLicense/@s30:ref]/jxdm51:DriverLicenseCardIdentification/nc30:IdentificationID";
						
		vehicleCrashDetailXpaths.dlJurisdictionXPath = 
				"//jxdm51:CrashDriverLicense[@s30:id=//jxdm51:CrashDriver/jxdm51:DriverLicense/@s30:ref]/jxdm51:DriverLicenseCardIdentification//nc30:IdentificationJurisdiction/jxdm51:LocationStateNCICLISCode";
				
		vehicleCrashDetailXpaths.lastNameXPath = 
				"//nc30:Person[@s30:id=//jxdm51:CrashDriver/nc30:RoleOfPerson/@s30:ref]/nc30:PersonName/nc30:PersonSurName"; 
						
		vehicleCrashDetailXpaths.middleNameXPath = 
				"//nc30:Person[@s30:id=//jxdm51:CrashDriver/nc30:RoleOfPerson/@s30:ref]/nc30:PersonName/nc30:PersonMiddleName";		
		
		vehicleCrashDetailXpaths.firstNameXPath = 
				"//nc30:Person[@s30:id=//jxdm51:CrashDriver/nc30:RoleOfPerson/@s30:ref]/nc30:PersonName/nc30:PersonGivenName";
		
		vehicleCrashDetailXpaths.eyeColorXPath = null;
		vehicleCrashDetailXpaths.hairColorXPath = null;		
		vehicleCrashDetailXpaths.raceXPath = null;
						
		vehicleCrashDetailXpaths.birthdateXPath = "//nc30:Person[@s30:id=//jxdm51:CrashDriver/nc30:RoleOfPerson/@s30:ref]/nc30:PersonBirthDate/nc30:Date";
		
		vehicleCrashDetailXpaths.sexXPath = "//nc30:Person[@s30:id=//jxdm51:CrashDriver/nc30:RoleOfPerson/@s30:ref]/jxdm51:PersonSexCode";
		
		vehicleCrashDetailXpaths.heightXPath = null;
		vehicleCrashDetailXpaths.weightXPath = null;	
		
		vehicleCrashDetailXpaths.searchSystemId = VEHICLE_CRASH_SEARCH_SYSTEM_ID;
		
		vehicleCrashDetailXpaths.systemName = "Vehicle Crash";
		
		vehicleCrashDetailXpaths.recordType = "Vehicle Crash";		
		
		return vehicleCrashDetailXpaths;				
	}

	
	private List<IdentifiableDocumentWrapper> courtCaseSearchDocumentsAsList(Document courtCaseSearchRequestMessage) 
			throws Exception {
		
		List<IdentifiableDocumentWrapper> courtCaseSearchDocMatchesList = new ArrayList<IdentifiableDocumentWrapper>();
		
		String ccSearchPersonRecId = XmlUtils.xPathStringSearch(courtCaseSearchRequestMessage, "//ccs-req-ext:PersonRecordIdentification/nc30:IdentificationID");		
		
		LOG.info("\n\n\n Court Case searching person rec id:" + ccSearchPersonRecId + "\n\n\n");
		
		for (IdentifiableDocumentWrapper identifyableCourtCaseDoc : courtCaseDataSource.getDocuments()) {
								
			String ccDetailPersonRecId = XmlUtils.xPathStringSearch(identifyableCourtCaseDoc.getDocument(), 
					"//ccq-res-ext:PersonRecordIdentification/nc30:IdentificationID");
				
			if(StringUtils.isNotEmpty(ccDetailPersonRecId) && ccDetailPersonRecId.equals(ccSearchPersonRecId)){
			
				courtCaseSearchDocMatchesList.add(identifyableCourtCaseDoc);						
 			}		
		}
		
		return courtCaseSearchDocMatchesList;		
	}
	
	
	private List<IdentifiableDocumentWrapper> custodySearchDocumentsAsList(Document custodySearchRequestMessage, DateTime baseDate) 
			throws Exception {
		
		List<IdentifiableDocumentWrapper> custodySearchResultMatchList = new ArrayList<IdentifiableDocumentWrapper>();		
		
		String custodySearchRequestSid = XmlUtils.xPathStringSearch(custodySearchRequestMessage, 
				"/cs-req-doc:CustodySearchRequest/nc30:Person/cs-req-ext:PersonRecordIdentification/nc30:IdentificationID");		
		
		LOG.info("\n\n\n Using sid: " + custodySearchRequestSid + " \n\n\n");
		
		if(StringUtils.isBlank(custodySearchRequestSid)){
			return custodySearchResultMatchList;
		}
			
		for (IdentifiableDocumentWrapper identifyableCustodyDetailDoc : custodyDataSource.getDocuments()) {
			
			Document custodyDetailDoc = identifyableCustodyDetailDoc.getDocument();						

			String custodyDetailDocSid = XmlUtils.xPathStringSearch(custodyDetailDoc, 
					"/cq-res-exch:CustodyQueryResults/cq-res-ext:Custody/nc30:Person/jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID");
			
			if(StringUtils.isNotBlank(custodyDetailDocSid) && custodySearchRequestSid.equals(custodyDetailDocSid)){
				
				custodySearchResultMatchList.add(identifyableCustodyDetailDoc);
			}			
		}						
		return custodySearchResultMatchList;		
	}
	
	
	
	private List<IdentifiableDocumentWrapper> personSearchDocumentsAsList(Document personSearchRequestMessage, DateTime baseDate, 
			SearchValueXPaths xPaths, ClasspathXmlDataSource dataSource) throws Exception {

		PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
		LOG.info("PersonSearchParameters: " + psp.toString());
		
		List<IdentifiableDocumentWrapper> matches = new ArrayList<IdentifiableDocumentWrapper>();

		for (IdentifiableDocumentWrapper dw : dataSource.getDocuments()) {

			Document d = dw.getDocument();

			// to compute age, we use birthdate if it's there, otherwise we use age
			Element birthdateElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.birthdateXPath);
			Integer age = null;
			DateTime birthdate = null;
			
			if (birthdateElement != null) {
				
				String sDob = birthdateElement.getTextContent();
				
				sDob = sDob.trim();
				
				birthdate = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(sDob);
				
				age = Years.yearsBetween(birthdate, baseDate).getYears();
				
			} else {
				Element ageElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.ageXPath);
				if (ageElement != null) {
					age = Integer.parseInt(ageElement.getTextContent());
				}
			}

			// there has to be a better way to do this, but I couldn't think of one... hey...it works! -sc
			List<Boolean> orStack = new ArrayList<Boolean>();
			boolean checkSSN = xPaths.ssnXPath != null && checkFieldEqual(d, psp.SSN, xPaths.ssnXPath);
			if (psp.SSN != null) {
				orStack.add(new Boolean(checkSSN));
			}
			boolean checkSID = xPaths.sidXPath != null && checkFieldEqual(d, psp.SID, xPaths.sidXPath);
			if (psp.SID != null) {
				orStack.add(new Boolean(checkSID));
			}
			boolean checkFBI = xPaths.fbiXPath != null && checkFieldEqual(d, psp.FBI, xPaths.fbiXPath);
			if (psp.FBI != null) {
				orStack.add(new Boolean(checkFBI));
			}
			boolean checkDL = xPaths.dlXPath != null && checkFieldEqual(d, psp.DL, xPaths.dlXPath);
			if (psp.DL != null) {
				orStack.add(new Boolean(checkDL));
			}
			boolean include = orStack.isEmpty() ? true : orStack.get(0);
			if (orStack.size() > 1) {
				for (int i = 1; i < orStack.size(); i++) {
					include |= orStack.get(i);
				}
			}

			boolean checkLastName = checkStringFieldMatches(d, psp.lastName, xPaths.lastNameXPath, psp.lastNameSearchStartsWith);										
			boolean checkFirstName = checkStringFieldMatches(d, psp.firstName, xPaths.firstNameXPath, psp.firstNameSearchStartsWith);
			boolean checkEyeColor = checkFieldEqual(d, psp.eyeColor, xPaths.eyeColorXPath);
			boolean checkHairColor = checkFieldEqual(d, psp.hairColor, xPaths.hairColorXPath);
			boolean checkRace = checkFieldEqual(d, psp.race, xPaths.raceXPath);
			boolean checkSex = checkFieldEqual(d, psp.sex, xPaths.sexXPath);
			boolean checkDateOfBirth = checkRange(d, psp.dateOfBirth, psp.dateOfBirthMin, psp.dateOfBirthMax, birthdate);
			boolean checkAge = checkRange(d, psp.age, psp.ageMin, psp.ageMax, age);
			boolean checkHeight = checkIntegerRange(d, psp.height, psp.heightMin, psp.heightMax, xPaths.heightXPath);
			boolean checkWeight = checkIntegerRange(d, psp.weight, psp.weightMin, psp.weightMax, xPaths.weightXPath);

			boolean checkAliasLastName = checkStringFieldMatches(d, psp.aliasLastName, xPaths.aliasLastNameXPath, psp.aliasLastNameStartsWith);
			boolean checkAliasFirstName = checkStringFieldMatches(d, psp.aliasFirstName, xPaths.aliasFirstNameXPath, psp.aliasFirstNameStartsWith);
			boolean checkAddressCity = checkFieldEqual(d, psp.addressCity, xPaths.addressCityXPath);
			boolean checkAddressState = checkFieldEqual(d, psp.addressState, xPaths.addressStateXPath);
			boolean checkAddressZip = checkFieldEqual(d, psp.addressZip, xPaths.addressZipXPath);

			boolean checkAddressStreet = checkFieldContains(d, psp.addressStreet, xPaths.addressStreetXPath);

			boolean checkPlacement = checkFieldMatchesList(d, psp.juvenilePlacement, xPaths.juvenilePlacementsXPath, false);
			boolean checkParentLastName = checkFieldMatchesList(d, psp.parentLastName, xPaths.parentLastNamesXPath, psp.parentLastNameStartsWith);
			boolean checkParentFirstName = checkFieldMatchesList(d, psp.parentFirstName, xPaths.parentFirstNamesXPath, psp.parentFirstNameStartsWith);

			include = include && checkLastName && checkFirstName && checkEyeColor && checkHairColor && checkRace && checkSex;
			include = include && checkDateOfBirth && checkAge && checkHeight && checkWeight;
			include = include && checkAliasFirstName && checkAliasLastName && checkPlacement && checkAddressCity && checkAddressState;
			include = include && checkAddressStreet && checkAddressZip && checkParentFirstName && checkParentLastName;

			if (include) {
				matches.add(dw);
			}
		}
		return matches;
	}

	private boolean checkFieldMatchesList(Document d, String valueParam, String xPathToDocumentValues, boolean startsWithCheck) throws Exception {
		if (valueParam != null) {
			NodeList documentElements = XmlUtils.xPathNodeListSearch(d, xPathToDocumentValues);
			for (int i = 0; i < documentElements.getLength(); i++) {
				Element documentElement = (Element) documentElements.item(i);
				if (documentElement != null) {
					String documentValue = documentElement.getTextContent();
					if (documentValue != null && (startsWithCheck ? documentValue.toUpperCase().startsWith(valueParam.toUpperCase()) : documentValue.toUpperCase().equals(valueParam.toUpperCase()))) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	private boolean checkFieldContains(Document d, String valueParam, String xPathToDocumentValue) throws Exception {
		boolean ret = true;
		if (valueParam != null) {
			Element documentElement = (Element) XmlUtils.xPathNodeSearch(d, xPathToDocumentValue);
			if (documentElement != null) {
				String documentValue = documentElement.getTextContent();
				if (documentValue != null) {
					ret = documentValue.toUpperCase().contains(valueParam.toUpperCase());
				}
			}
		}
		return ret;
	}

	private boolean checkIntegerRange(Document personSearchRequestMessage, Integer valueParam, Integer minValueParam, Integer maxValueParam, String xPathToDocumentValue) throws Exception {
		if (valueParam == null && minValueParam == null && maxValueParam == null) {
			// skipping this query parameter
			return true;
		}
		Element documentValueElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, xPathToDocumentValue);
		if (documentValueElement == null) {
			return false;
		}
		int documentValue = (int) Double.parseDouble(documentValueElement.getTextContent());
		return checkRange(personSearchRequestMessage, valueParam, minValueParam, maxValueParam, documentValue);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkRange(Document personSearchRequestMessage, Comparable valueParam, Comparable minValueParam, Comparable maxValueParam, Comparable documentValue) throws Exception {
		if (valueParam == null && minValueParam == null && maxValueParam == null) {
			// skipping this query parameter
			return true;
		}
		if (valueParam != null) {
			// single-value test
			return valueParam.equals(documentValue);
		} else {
			// range test
			return minValueParam.compareTo(documentValue) != 1 && documentValue.compareTo(maxValueParam) != 1;
		}

	}

	private boolean checkStringFieldMatches(Document d, String value, String xPath, boolean startsWithCheck) throws Exception {
		boolean ret = true;
		if (value != null) {
			if (!startsWithCheck) {
				ret = checkFieldEqual(d, value, xPath);
			} else {
				
				LOG.debug("Looking up field for: " + xPath);
				XmlUtils.printNode(d);
				
				
				Element documentElement = (Element) XmlUtils.xPathNodeSearch(d, xPath);
				// LOG.info("xPath=" + xPath + ", documentElement=" + documentElement);
				if (documentElement != null) {
					String documentValue = documentElement.getTextContent();
					if (documentValue != null) {
						// LOG.info("documentvalue=" + documentValue);
						ret = documentValue.toUpperCase().startsWith(value.toUpperCase());
					} else {
						ret = false;
					}
				} else {
					ret = false;
				}
			}
		}
		// LOG.info("Comparing string field match: value=" + value + ", xPathToParent=" + xPathToParent + ", elementToCheck=" + elementToCheck + ", returning " + ret);
		return ret;
	}

	private boolean checkFieldEqual(Document d, Object value, String xPath) throws Exception {
		boolean ret = true;
		if (value != null) {
			Element documentElement = (Element) XmlUtils.xPathNodeSearch(d, xPath);
			if (documentElement != null) {
				String documentValue = documentElement.getTextContent();
								
				if (documentValue != null) {
					String valueS = value.toString().toUpperCase();
					ret = documentValue.toUpperCase().startsWith(valueS);
				} else {
					ret = false;
				}
			} else {
				ret = false;
			}
		}
		// LOG.info("Comparing object field match: value=" + value + ", xPathToParent=" + xPathToParent + ", elementToCheck=" + elementToCheck + ", returning " + ret);
		return ret;
	}

	private List<IdentifiableDocumentWrapper> queryWarrantDocuments(String documentId) throws Exception {
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		IdentifiableDocumentWrapper document = warrantDataSource.getDocument(documentId);
		if (document != null) {
			ret.add(document);
		}
		return ret;
	}

	private List<IdentifiableDocumentWrapper> queryIncidentDocuments(String documentId) throws Exception {
		// LOG.info("query incident documents, documentId=" + documentId);
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		IdentifiableDocumentWrapper document = incidentDataSource.getDocument(documentId);
		if (document != null) {
			ret.add(document);
		}
		return ret;
	}

	private List<IdentifiableDocumentWrapper> queryJuvenileHistoryDocuments(String documentId, Object context) throws Exception {
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		IdentifiableDocumentWrapper containerDocument = juvenileHistoryDataSource.getDocument(documentId);
		if (containerDocument != null) {
			Document d = containerDocument.getDocument();
			JuvenileHistoryContainer container = new JuvenileHistoryContainer(d);
			d = container.getDocumentForContext(context);
			String ns = null;
			if ("JuvenileCasePlanHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_CASE_PLAN;
			}
			if ("JuvenileHearingHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_HEARING;
			}
			if ("JuvenileIntakeHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_INTAKE;
			}
			if ("JuvenileOffenseHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_OFFENSE;
			}
			if ("JuvenilePlacementHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_PLACEMENT;
			}
			if ("JuvenileReferralHistory".equals(context)) {
				ns = NS_JUVENILE_HISTORY_REFERRAL;
			}
			Element newRoot = d.createElementNS(ns, ((String) context) + "Response");
			XmlUtils.appendElement(newRoot, NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryCategoryCode").setTextContent((String) context);
			Element e = XmlUtils.appendElement(newRoot, NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryQueryCriteria");
			e = XmlUtils.appendElement(e, NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
			XmlUtils.appendElement(e, NS_NC_30, "IdentificationID").setTextContent(documentId);
			XmlUtils.appendElement(e, NS_NC_30, "IdentificationSourceText").setTextContent(JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID);
			Element oldRoot = d.getDocumentElement();
			newRoot.appendChild(oldRoot);
			Element metadataRecordIDElement = (Element) XmlUtils.xPathNodeSearch(oldRoot, "jh-ext:JuvenileInformationAvailabilityMetadata/jh-ext:JuvenileInformationRecordID");
			XmlUtils.xPathNodeSearch(metadataRecordIDElement, "nc30:IdentificationID").setTextContent(documentId);
			XmlUtils.xPathNodeSearch(metadataRecordIDElement, "nc30:IdentificationSourceText").setTextContent(JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID);
			newRoot.setPrefix(ojbcNamespaceContext.getPrefix(ns));
			d.appendChild(newRoot);
			
			ojbcNamespaceContext.populateRootNamespaceDeclarations(newRoot);
			ret.add(new IdentifiableDocumentWrapper(d, documentId));
		}
		return ret;
	}

	private List<IdentifiableDocumentWrapper> queryPersonFirearmRegistrationDocuments(String identifier) throws Exception {
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		String documentIdentifier = null;
		String firearmId = null;
		if (identifier.contains(":")) {
			String[] ss = identifier.split(":");
			documentIdentifier = ss[0];
			firearmId = ss[1];
		} else {
			documentIdentifier = identifier;
		}
		IdentifiableDocumentWrapper document = firearmRegistrationDataSource.getDocument(documentIdentifier);
		if (document != null) {
			if (firearmId != null) {
				document = new IdentifiableDocumentWrapper(createFirearmRegistrationDocument(document.getDocument(), firearmId), identifier);
			}
			ret.add(document);
		}
		return ret;
	}

	private Document createFirearmRegistrationDocument(Document document, String firearmId) throws Exception {
		Document copy = createNewDocument();
		copy.appendChild(copy.importNode(document.getDocumentElement(), true));
		Node rootElement = XmlUtils.xPathNodeSearch(copy, "/*");
		LOG.debug("Keeper: " + firearmId);
		NodeList otherFirearmNodes = XmlUtils.xPathNodeListSearch(rootElement, "firearm-ext:Firearm[@s:id != '" + firearmId + "']");
		for (int i = 0; i < otherFirearmNodes.getLength(); i++) {
			Node goner = otherFirearmNodes.item(i);
			LOG.debug("Goner: " + XmlUtils.xPathStringSearch(goner, "@s:id"));
			rootElement.removeChild(goner);
		}
		NodeList otherItemRegNodes = XmlUtils.xPathNodeListSearch(rootElement, "firearm-ext:ItemRegistration[@s:id != /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemReference/@s:ref='" + firearmId
				+ "']/nc:ItemRegistrationReference/@s:ref]");
		for (int i = 0; i < otherItemRegNodes.getLength(); i++) {
			rootElement.removeChild(otherItemRegNodes.item(i));
		}
		NodeList otherRegAssociationNodes = XmlUtils.xPathNodeListSearch(rootElement, "nc:PropertyRegistrationAssociation[nc:ItemReference/@s:ref != '" + firearmId + "']");
		for (int i = 0; i < otherRegAssociationNodes.getLength(); i++) {
			rootElement.removeChild(otherRegAssociationNodes.item(i));
		}
		copy.renameNode(rootElement, rootElement.getNamespaceURI(), "FirearmRegistrationQueryResults");
		Node documentRootElement = XmlUtils.xPathNodeSearch(document, "/*");
		rootElement.setPrefix(documentRootElement.getPrefix());
		return copy;
	}

	private List<IdentifiableDocumentWrapper> queryCriminalHistoryDocuments(String documentId) throws Exception {
		List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
		IdentifiableDocumentWrapper document = criminalHistoryDataSource.getDocument(documentId);
		if (document != null) {
			ret.add(document);
		}
		return ret;
	}
	
	private List<IdentifiableDocumentWrapper> queryCustodyDocuments(String documentId) throws Exception {
		
		List<IdentifiableDocumentWrapper> custodyDetailDocWrapperList = new ArrayList<IdentifiableDocumentWrapper>();
		
		IdentifiableDocumentWrapper custodyDetailWrapper = custodyDataSource.getDocument(documentId);
		
		if (custodyDetailWrapper != null) {
			
			custodyDetailDocWrapperList.add(custodyDetailWrapper);
		}		
		return custodyDetailDocWrapperList;
	}
	
	private List<IdentifiableDocumentWrapper> queryCourtCaseDocuments(String documentId) throws Exception {
		
		List<IdentifiableDocumentWrapper> rCourtCaseDetailWrapperList = new ArrayList<IdentifiableDocumentWrapper>();
		
		IdentifiableDocumentWrapper courtCaseDetailWrapper = courtCaseDataSource.getDocument(documentId);
		
		if (courtCaseDetailWrapper != null) {
			
			rCourtCaseDetailWrapperList.add(courtCaseDetailWrapper);
		}		
		return rCourtCaseDetailWrapperList;
	}		
	
	
	private List<IdentifiableDocumentWrapper> queryVehicleCrashDocuments(String documentId) throws Exception {
		
		List<IdentifiableDocumentWrapper> rVehicleCrashDetailWrapperList = new ArrayList<IdentifiableDocumentWrapper>();
		
		IdentifiableDocumentWrapper vehicleCrashDetailWrapper = vehicleCrashDataSource.getDocument(documentId);
		
		if(vehicleCrashDetailWrapper != null){
			
			rVehicleCrashDetailWrapperList.add(vehicleCrashDetailWrapper);			
		}
		
		return rVehicleCrashDetailWrapperList;
	}
	
	private List<IdentifiableDocumentWrapper> queryFirearmProhibitionDocuments(String documentId) throws Exception {
		
		List<IdentifiableDocumentWrapper> rFirearmProhibitionDetailWrapperList = new ArrayList<IdentifiableDocumentWrapper>();
		
		IdentifiableDocumentWrapper firearmProhibitionDetailWrapper = firearmProhibitionDataSource.getDocument(documentId);
		
		if(firearmProhibitionDetailWrapper != null){
			
			rFirearmProhibitionDetailWrapperList.add(firearmProhibitionDetailWrapper);			
		}
		
		return rFirearmProhibitionDetailWrapperList;
	}
	
	

	static final class PersonSearchParameters {

		private Integer ageMin;
		private Integer ageMax;
		private Integer age;
		private DateTime dateOfBirth;
		private String eyeColor;
		private String hairColor;
		private Integer heightMin;
		private Integer heightMax;
		private Integer height;
		private String lastName;
		private String firstName;
		private String race;
		private String sex;
		private String SSN;
		private Integer weightMin;
		private Integer weightMax;
		private Integer weight;
		private DateTime dateOfBirthMin;
		private DateTime dateOfBirthMax;
		private String DL;
		private String FBI;
		private String SID;
		private boolean firstNameSearchStartsWith;
		private boolean lastNameSearchStartsWith;
		private String juvenilePlacement;
		private String addressStreet;
		private String addressCity;
		private String addressState;
		private String addressZip;
		private String aliasFirstName;
		private String aliasLastName;
		private String parentFirstName;
		private String parentLastName;
		private boolean aliasFirstNameStartsWith;
		private boolean aliasLastNameStartsWith;
		private boolean parentFirstNameStartsWith;
		private boolean parentLastNameStartsWith;
		
		public String toString(){
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}

		public PersonSearchParameters(Document personSearchRequestMessage) throws Exception {
			
			juvenilePlacement = getElementContent(personSearchRequestMessage, "cyfs21:Placement/jh-placement-search-codes:PlacementCategoryCode");
			addressStreet = getElementContent(personSearchRequestMessage, "nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText");
			addressCity = getElementContent(personSearchRequestMessage, "nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName");
			addressState = getElementContent(personSearchRequestMessage, "nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateFIPS5-2AlphaCode");
			addressZip = getElementContent(personSearchRequestMessage, "nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode");
			aliasFirstName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAlternateName/nc:PersonGivenName");
			Element e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonAlternateName/nc:PersonGivenName");
			aliasFirstNameStartsWith = getStartsWithForElement(personSearchRequestMessage, e);
			aliasLastName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAlternateName/nc:PersonSurName");
			e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonAlternateName/nc:PersonSurName");
			aliasLastNameStartsWith = getStartsWithForElement(personSearchRequestMessage, e);
			parentFirstName = getElementContent(personSearchRequestMessage, "psr:Parent/nc:PersonName/nc:PersonGivenName");
			e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Parent/nc:PersonName/nc:PersonGivenName");
			parentFirstNameStartsWith = getStartsWithForElement(personSearchRequestMessage, e);
			parentLastName = getElementContent(personSearchRequestMessage, "psr:Parent/nc:PersonName/nc:PersonSurName");
			e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Parent/nc:PersonName/nc:PersonSurName");
			parentLastNameStartsWith = getStartsWithForElement(personSearchRequestMessage, e);
			String ageS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureText");
			if (ageS != null) {
				age = Integer.parseInt(ageS);
			}
			String ageMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
			if (ageMinS != null) {
				ageMin = Integer.parseInt(ageMinS);
			}
			String ageMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
			if (ageMaxS != null) {
				ageMax = Integer.parseInt(ageMaxS);
			}
			String dateOfBirthS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonBirthDate/nc:Date");
			if (dateOfBirthS != null) {
				dateOfBirth = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthS);
			}
			eyeColor = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonEyeColorCode");
			hairColor = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHairColorCode");
			String heightMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
			if (heightMinS != null) {
				heightMin = Integer.parseInt(heightMinS);
			}
			String heightMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
			if (heightMaxS != null) {
				heightMax = Integer.parseInt(heightMaxS);
			}
			String heightS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureText");
			if (heightS != null) {
				height = Integer.parseInt(heightS);
			}
			lastName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonName/nc:PersonSurName");
			Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonName/nc:PersonSurName");
			lastNameSearchStartsWith = getStartsWithForElement(personSearchRequestMessage, lastNameElement);
			firstName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonName/nc:PersonGivenName");
			Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonName/nc:PersonGivenName");
			firstNameSearchStartsWith = getStartsWithForElement(personSearchRequestMessage, firstNameElement);
			race = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonRaceCode");
			sex = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonSexCode");
			SSN = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonSSNIdentification/nc:IdentificationID");
			String weightMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
			if (weightMinS != null) {
				weightMin = Integer.parseInt(weightMinS);
			}
			String weightMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
			if (weightMaxS != null) {
				weightMax = Integer.parseInt(weightMaxS);
			}
			String weightS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureText");
			if (weightS != null) {
				weight = Integer.parseInt(weightS);
			}
			String dateOfBirthMinS = getElementContent(personSearchRequestMessage, "psr:Person/psr:PersonBirthDateRange/nc:StartDate/nc:Date");
			if (dateOfBirthMinS != null) {
				dateOfBirthMin = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthMinS);
			}
			String dateOfBirthMaxS = getElementContent(personSearchRequestMessage, "psr:Person/psr:PersonBirthDateRange/nc:EndDate/nc:Date");
			if (dateOfBirthMaxS != null) {
				dateOfBirthMax = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthMaxS);
			}
			DL = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID");
			FBI = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
			SID = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");

			if (lastName == null && SSN == null && FBI == null && SID == null && DL == null) {
				throw new IllegalArgumentException("Searches must have a last name or an identifier");
			}

			if (ageMin != null && ageMax == null) {
				ageMax = Integer.MAX_VALUE;
			}
			if (ageMax != null && ageMin == null) {
				ageMin = 0;
			}
			if (weightMin != null && weightMax == null) {
				weightMax = Integer.MAX_VALUE;
			}
			if (weightMax != null && weightMin == null) {
				weightMin = 0;
			}
			if (heightMin != null && heightMax == null) {
				heightMax = Integer.MAX_VALUE;
			}
			if (heightMax != null && heightMin == null) {
				heightMin = 0;
			}
			if (dateOfBirthMin == null && dateOfBirthMax != null) {
				dateOfBirthMin = DATE_FORMATTER_YYYY_MM_DD.parseDateTime("1800-01-01");
			}
			if (dateOfBirthMin != null && dateOfBirthMax == null) {
				dateOfBirthMax = DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2200-12-31");
			}

		}

		public boolean getStartsWithForElement(Document personSearchRequestMessage, Element stringElement) throws Exception {
			boolean b = false;
			if (stringElement != null) {
				String metadataAttribute = stringElement.getAttributeNS(NS_STRUCTURES, "metadata");
				Element metadataElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:SearchMetadata[@s:id='" + metadataAttribute + "']");
				if (metadataElement != null) {
					Element qualifierCodeElement = (Element) XmlUtils.xPathNodeSearch(metadataElement, "psr:SearchQualifierCode");
					b = "startsWith".equals(qualifierCodeElement.getTextContent());
				}
			}
			return b;
		}

		public String getJuvenilePlacement() {
			return juvenilePlacement;
		}

		public String getAddressStreet() {
			return addressStreet;
		}

		public String getAddressCity() {
			return addressCity;
		}

		public String getAddressState() {
			return addressState;
		}

		public String getAddressZip() {
			return addressZip;
		}

		public String getAliasFirstName() {
			return aliasFirstName;
		}

		public String getAliasLastName() {
			return aliasLastName;
		}

		public String getParentFirstName() {
			return parentFirstName;
		}

		public String getParentLastName() {
			return parentLastName;
		}

		public boolean isAliasFirstNameStartsWith() {
			return aliasFirstNameStartsWith;
		}

		public boolean isAliasLastNameStartsWith() {
			return aliasLastNameStartsWith;
		}

		public boolean isParentFirstNameStartsWith() {
			return parentFirstNameStartsWith;
		}

		public boolean isParentLastNameStartsWith() {
			return parentLastNameStartsWith;
		}

		public DateTime getDateOfBirth() {
			return dateOfBirth;
		}

		public Integer getAge() {
			return age;
		}

		public Integer getWeight() {
			return weight;
		}

		public Integer getHeight() {
			return height;
		}

		public String getLastName() {
			return lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getSSN() {
			return SSN;
		}

		public String getSID() {
			return SID;
		}

		public String getDL() {
			return DL;
		}

		public String getFBI() {
			return FBI;
		}

		public String getEyeColor() {
			return eyeColor;
		}

		public String getHairColor() {
			return hairColor;
		}

		public String getRace() {
			return race;
		}

		public String getSex() {
			return sex;
		}

		public DateTime getDateOfBirthMin() {
			return dateOfBirthMin;
		}

		public DateTime getDateOfBirthMax() {
			return dateOfBirthMax;
		}

		public Integer getAgeMin() {
			return ageMin;
		}

		public Integer getAgeMax() {
			return ageMax;
		}

		public Integer getWeightMin() {
			return weightMin;
		}

		public Integer getWeightMax() {
			return weightMax;
		}

		public Integer getHeightMin() {
			return heightMin;
		}

		public Integer getHeightMax() {
			return heightMax;
		}

		public boolean isFirstNameSearchStartsWith() {
			return firstNameSearchStartsWith;
		}

		public boolean isLastNameSearchStartsWith() {
			return lastNameSearchStartsWith;
		}

		private String getElementContent(Document personSearchRequestMessage, String xpath) throws Exception {
			Element e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), xpath);
			return e == null ? null : e.getTextContent().trim();
		}

	}

	static class SearchValueXPaths {
		
		 String birthdateXPath;
		 String ssnXPath;
		 String sidXPath;
		 String fbiXPath;
		 String dlXPath;
		 String dlJurisdictionXPath;
		 String lastNameXPath;
		 String middleNameXPath;
		 String firstNameXPath;
		 String alternateNameIdentityNodeListXpath;
		 String eyeColorXPath;
		 String eyeColorCodeXPath;
		 String hairColorXPath;
		 String hairColorCodeXPath;
		 String physicalFeatureXPath;
		 String raceXPath;
		 String sexXPath;
		 String ageXPath;		 		 
		 String heightXPath;
		 String weightXPath;
		 String juvenilePlacementsXPath;
		 String addressStreetXPath;		 
		 String addressStreetNumberXPath;
		 String addressStreetNameXPath;		 
		 String addressCityXPath;
		 String addressStateXPath;
		 String addressZipXPath;
		 String aliasFirstNameXPath;
		 String aliasMiddleNameXPath;
		 String aliasLastNameXPath;
		 String parentFirstNamesXPath;
		 String parentMiddleNamesXPath;
		 String parentLastNamesXPath;
		 String searchSystemId;
		 String systemName;
		 String recordType;

		public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
			return documentWrapper.getId();
		}
	}
	
}
