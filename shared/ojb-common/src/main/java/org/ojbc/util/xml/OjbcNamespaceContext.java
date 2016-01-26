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
package org.ojbc.util.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class that contains all the OJBC namespaces used across samples
 * 
 */
public final class OjbcNamespaceContext implements NamespaceContext {

	private static final Log log = LogFactory.getLog(OjbcNamespaceContext.class);
				
	public static final String NS_NIST_BIO = "http://biometrics.nist.gov/standard/2011";
	public static final String NS_NIST_BIO_PREFIX = "nistbio";
	
	public static final String NS_NIEM_BIO = "http://niem.gov/niem/biometrics/1.0";
	public static final String NS_PREFIX_NIEM_BIO = "nbio";
	
	public static final String NS_PERSON_IDENTIFICATION_REPORT_RESPONSE = "http://ojbc.org/IEPD/Exchange/PersonIdentificationReportResponse/1.0";
	public static final String NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE = "pidrepres-doc";
	
	public static final String NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT = "http://ojbc.org/IEPD/Extensions/IdentificationReportResponseExtension/1.0";
	public static final String NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT = "identrepres-ext";

	public static final String NS_DISPOSITION_REPORTS = "http://ojbc.org/DispositionReports/1.0";
	public static final String NS_DISPOSITION_REPORTS_PREFIX = "dr";

	public static final String NS_DISPOSITION_EXCHANGE = "http://ojbc.org/IEPD/Exchange/DispositionReport/1.0";
	public static final String NS_DISPOSITION_EXCHANGE_PREFIX = "disp_exc";

	public static final String NS_DISPOSITION_EXTENSION = "http://ojbc.org/IEPD/Extensions/DispositionReportExtension/1.0";
	public static final String NS_DISPOSITION_EXTENSION_PREFIX = "disp_ext";

	public static final String NS_MAINE_DISPOSITION_CODES = "http://ojbc.org/IEPD/Extensions/Maine/DispositionCodes/1.0";
	public static final String NS_MAINE_DISPOSITION_CODES_PREFIX = "me_disp_codes";

	public static final String NS_MAINE_CHARGE_CODES_V1 = "http://ojbc.org/IEPD/Extensions/Maine/ChargeCodes/1.0";
	public static final String NS_MAINE_CHARGE_CODES_PREFIX_V1 = "me-chrg-codes-v1";
	
	public static final String NS_MAINE_CHARGE_CODES = "http://ojbc.org/IEPD/Extensions/Maine/ChargeCodes/2.0";
	public static final String NS_MAINE_CHARGE_CODES_PREFIX = "me-chrg-codes";

	public static final String NS_SOAP = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String NS_SOAP_PREFIX = "soap";

	public static final String NS_SUB_VALID_MESSAGE = "http://ojbc.org/IEPD/Exchange/SubscriptionValidationMessage/1.0";
	public static final String NS_SUB_VALID_MESSAGE_PREFIX = "svm";
	
	public static final String NS_SUB_MODIFY_MESSAGE = "http://ojbc.org/IEPD/Exchange/SubscriptionModificationMessage/1.0";
	public static final String NS_SUB_MODIFY_MESSAGE_PREFIX = "smm";

	public static final String NS_SAML_ASSERTION = "urn:oasis:names:tc:SAML:2.0:assertion";
	public static final String NS_PREFIX_SAML_ASSERTION = "saml2";

	public static final String NS_SUBSCRIPTION_RESPONSE_EXT = "http://ojbc.org/IEPD/Extension/Subscription_Response/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_RESPONSE_EXT = "subresp-ext";

	public static final String NS_SUBSCRIPTION_RESPONSE_EXCH = "http://ojbc.org/IEPD/Exchange/Subscription_Response/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_RESPONSE_EXCH = "subresp-exch";

	public static final String NS_SUBSCRIPTION_FAULT_RESPONSE_EXCH = "http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXCH = "subfltrsp-exch";

	public static final String NS_SUBSCRIPTION_FAULT_RESPONSE_EXT = "http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXT = "subfltrsp-ext";

	public static final String NS_BF2 = "http://docs.oasis-open.org/wsrf/bf-2";
	public static final String NS_PREFIX_BF2 = "bf2";

	public static final String NS_SUBSCRIPTION_SEARCH_RESULTS = "http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS = "ssr";

	public static final String NS_SUBSCRIPTION_SEARCH_REQUEST = "http://ojbc.org/IEPD/Exchange/SubscriptionSearchRequest/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST = "ssreq";

	public static final String NS_SUBSCRIPTION_SEARCH_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/SubscriptionSearchResults/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS_EXT = "ssr-ext";

	public static final String NS_SUBSCRIPTION_QUERY_RESULTS = "http://ojbc.org/IEPD/Exchange/SubscriptionQueryResults/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS = "sqr";

	public static final String NS_SUBSCRIPTION_QUERY_REQUEST = "http://ojbc.org/IEPD/Exchange/SubscriptionQueryRequest/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST = "sqreq";

	public static final String NS_SUBSCRIPTION_QUERY_REQUEST_EXT = "http://ojbc.org/IEPD/Extension/SubscriptionQueryRequest/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST_EXT = "sqreq-ext";

	public static final String NS_SUBSCRIPTION_QUERY_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/SubscriptionQueryResults/1.0";
	public static final String NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS_EXT = "sqr-ext";

	public static final String NS_B2 = "http://docs.oasis-open.org/wsn/b-2";
	public static final String NS_PREFIX_B2 = "b-2";

	public static final String NS_ADD = "http://www.w3.org/2005/08/addressing";
	public static final String NS_PREFIX_ADD = "add";

	public static final String NS_SUB_MSG_EXCHANGE = "http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0";
	public static final String NS_PREFIX_SUB_MSG_EXCHANGE = "submsg-exch";

	public static final String NS_UNBSUB_MSG_EXCHANGE = "http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0";
	public static final String NS_PREFIX_UNSUB_MSG_EXCHANGE = "unsubmsg-exch";

	public static final String NS_SUB_MSG_EXT = "http://ojbc.org/IEPD/Extensions/Subscription/1.0";
	public static final String NS_PREFIX_SUB_MSG_EXT = "submsg-ext";

	// WSN namespaces
	public static final String NS_WSN_BROKERED = "http://docs.oasis-open.org/wsn/br-2";
	public static final String NS_PREFIX_WSN_BROKERED = "wsn-br";

	// Probation Namespaces
	public static final String NS_PROBATION_CASE_INITIATION = "http://ojbc.org/IEPD/Exchange/ProbationCaseInitiation/1.0";
	public static final String NS_PREFIX_PROBATION_CASE_INITIATION = "pci";

	public static final String NS_PROBATION_CASE_TERMINATION = "http://ojbc.org/IEPD/Exchange/ProbationCaseTermination/1.0";
	public static final String NS_PREFIX_PROBATION_CASE_TERMINATION = "pct";

	// Parole Namespaces
	public static final String NS_PAROLE_CASE_INITIATION = "http://ojbc.org/IEPD/Exchange/ParoleCaseInitiation/1.0";
	public static final String NS_PREFIX_PAROLE_CASE_INITIATION = "prlci";

	public static final String NS_PAROLE_CASE_TERMINATION = "http://ojbc.org/IEPD/Exchange/ParoleCaseTermination/1.0";
	public static final String NS_PREFIX_PAROLE_CASE_TERMINATION = "prlct";

	public static final String NS_PAROLE_CASE = "http://ojbc.org/IEPD/Extensions/ParoleCase/1.0";
	public static final String NS_PREFIX_PAROLE_CASE = "prlcase";

	public static final String NS_PROBATION_EXTENSION = "http://ojbc.org/IEPD/Extensions/ProbationCase/1.0";
	public static final String NS_PREFIX_PROBATION_EXTENSION = "pcext";

	public static final String NS_ENT_MERGE_RESULT_MSG = "http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0";
	public static final String NS_PREFIX_ENT_MERGE_RESULT_MSG = "emrm-exc";

	public static final String NS_ENT_MERGE_RESULT_MSG_EXT = "http://nij.gov/IEPD/Extensions/EntityMergeResultMessageExtensions/1.0";
	public static final String NS_PREFIX_ENT_MERGE_RESULT_MSG_EXT = "emrm-ext";

	public static final String NS_ENT_MERGE_REQUEST_MSG = "http://nij.gov/IEPD/Exchange/EntityMergeRequestMessage/1.0";
	public static final String NS_PREFIX_ENT_MERGE_REQUEST_MSG = "em-req-exc";

	public static final String NS_ER_EXT = "http://nij.gov/IEPD/Extensions/EntityResolutionExtensions/1.0";
	public static final String NS_PREFIX_ER_EXT = "er-ext";

	public static final String NS_FIREARM_DOC = "http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryResults/1.0";
	public static final String NS_FIREARM_EXT = "http://ojbc.org/IEPD/Extensions/FirearmRegistrationQueryResults/1.0";

	public static final String NS_IAD = "http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0";
	public static final String NS_QRER = "http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0";
	public static final String NS_QRM = "http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0";

	public static final String NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryRequest/1.0";
	public static final String NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT = "http://ojbc.org/IEPD/Extension/FirearmRegistrationQueryRequest/1.0";
	public static final String NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC = "firearm-reg-req-doc";
	public static final String NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT = "firearm-reg-req-ext";

	public static final String NS_PREFIX_FIREARM_DOC = "firearm-doc";
	public static final String NS_PREFIX_FIREARM_EXT = "firearm-ext";

	public static final String NS_PREFIX_FIREARM_SEARCH_REQUEST_DOC = "firearm-search-req-doc";
	public static final String NS_PREFIX_FIREARM_SEARCH_REQUEST_EXT = "firearm-search-req-ext";
	public static final String NS_FIREARM_SEARCH_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/FirearmSearchRequest/1.0";
	public static final String NS_FIREARM_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/FirearmSearchRequest/1.0";

	public static final String NS_PREFIX_FIREARM_SEARCH_RESULT_DOC = "firearm-search-resp-doc";
	public static final String NS_PREFIX_FIREARM_SEARCH_RESULT_EXT = "firearm-search-resp-ext";
	public static final String NS_FIREARM_SEARCH_RESULT_DOC = "http://ojbc.org/IEPD/Exchange/FirearmSearchResults/1.0";
	public static final String NS_FIREARM_SEARCH_RESULT_EXT = "http://ojbc.org/IEPD/Extensions/FirearmSearchResults/1.0";

	public static final String NS_FIREARMS_CODES_HAWAII = "http://ojbc.org/IEPD/Extensions/Hawaii/FirearmCodes/1.0";
	public static final String NS_PREFIX_FIREARMS_CODES_HAWAII = "firearms-hi";

	public static final String NS_FIREARMS_CODES_DEMOSTATE = "http://ojbc.org/IEPD/Extensions/demostate/FirearmCodes/1.0";
	public static final String NS_PREFIX_FIREARMS_CODES_DEMOSTATE = "firearms-codes-demostate";

	public static final String NS_INCIDENT_LOCATION_CODES_DEMOSTATE = "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0";
	public static final String NS_PREFIX_INCIDENT_LOCATION_CODES_DEMOSTATE = "incident-location-codes-demostate";

	public static final String NS_PREFIX_IAD = "iad";
	public static final String NS_PREFIX_QRER = "qrer";
	public static final String NS_PREFIX_QRM = "qrm";

	public static final String NS_JXDM_40 = "http://niem.gov/niem/domains/jxdm/4.0";
	public static final String NS_JXDM_50 = "http://release.niem.gov/niem/domains/jxdm/5.0/";
	
	public static final String NS_JXDM_51 = "http://release.niem.gov/niem/domains/jxdm/5.1/";
	public static final String NS_PREFIX_JXDM_51 = "jxdm51"; 	
	
	public static final String NS_USPS_STATES = "http://niem.gov/niem/usps_states/2.0";
	public static final String NS_ERROR = "http://ojbc.org/IEPD/Extensions/PersonQueryErrorReporting/1.0";
	public static final String NS_WARRANT_EXT = "http://ojbc.org/IEPD/Extensions/Warrants/1.0";
	public static final String NS_NC = "http://niem.gov/niem/niem-core/2.0";
	public static final String NS_NC_30 = "http://release.niem.gov/niem/niem-core/3.0/";
	public static final String NS_WARRANT = "http://ojbc.org/IEPD/Exchange/Warrants/1.0";
	public static final String NS_STRUCTURES = "http://niem.gov/niem/structures/2.0";
	public static final String NS_STRUCTURES_30 = "http://release.niem.gov/niem/structures/3.0/";

	public static final String NS_PREFIX_JXDM_40 = "jxdm40";
	public static final String NS_PREFIX_JXDM_50 = "jxdm50";
	public static final String NS_PREFIX_USPS_STATES = "usps-states";
	public static final String NS_PREFIX_ERROR = "error";
	public static final String NS_PREFIX_WARRANT_EXT = "warrant-ext";
	public static final String NS_PREFIX_NC = "nc";
	public static final String NS_PREFIX_NC_30 = "nc30";
	public static final String NS_PREFIX_WARRANT = "warrant";
	public static final String NS_PREFIX_STRUCTURES = "s";
	public static final String NS_PREFIX_STRUCTURES_30 = "s30";

	public static final String NS_PREFIX_CH = "ch";
	public static final String NS_CH = "http://ojbc.org/IEPD/Extensions/RapSheet/1.0";
	public static final String NS_PREFIX_CH_DOC = "ch-doc";
	public static final String NS_CH_DOC = "http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0";
	public static final String NS_PREFIX_RAPSHEET_41 = "rap";
	public static final String NS_RAPSHEET_41 = "http://nlets.org/niem2/rapsheet/1.0";
	public static final String NS_PREFIX_CH_EXT = "ch-ext";
	public static final String NS_CH_EXT = "http://ojbc.org/IEPD/Extensions/CriminalHistory/1.0";
	public static final String NS_PREFIX_ANSI_NIST = "ansi-nist";
	public static final String NS_ANSI_NIST = "http://niem.gov/niem/ansi-nist/2.0";
	public static final String NS_PREFIX_SCREENING = "screening";
	public static final String NS_SCREENING = "http://niem.gov/niem/domains/screening/2.0";
	public static final String NS_PREFIX_SCREENING_21 = "screening21";
	public static final String NS_SCREENING_21 = "http://niem.gov/niem/domains/screening/2.1";

	public static final String NS_JXDM_41 = "http://niem.gov/niem/domains/jxdm/4.1";
	public static final String NS_PREFIX_JXDM_41 = "jxdm41";

	public static final String NS_CH_UPDATE = "http://ojbc.org/IEPD/Extensions/CriminalHistoryUpdate/1.0";
	public static final String NS_PREFIX_CH_UPDATE = "chu";

	public static final String NS_INCIDENT_QUERY_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/IncidentReportRequest/1.0";
	public static final String NS_PREFIX_INCIDENT_QUERY_REQUEST_DOC = "iqr-doc";
	public static final String NS_INCIDENT_QUERY_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/IncidentReportRequest/1.0";
	public static final String NS_PREFIX_INCIDENT_QUERY_REQUEST_EXT = "iqr";
	public static final String NS_PERSON_QUERY_REQUEST = "http://ojbc.org/IEPD/Exchange/PersonQueryRequest/1.0";
	public static final String NS_PREFIX_PERSON_QUERY_REQUEST = "pqr";
	public static final String NS_PERSON_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0";
	public static final String NS_PREFIX_PERSON_SEARCH_REQUEST_EXT = "psr";
	
	public static final String NS_PERSON_SEARCH_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/PersonSearchRequest/1.0";	
	public static final String NS_PREFIX_PERSON_SEARCH_REQUEST_DOC = "psr-doc";
					
	public static final String NS_CUSTODY_SEARCH_RES_EXT = "http://ojbc.org/IEPD/Extensions/CustodySearchResultsExtension/1.0";
	public static final String NS_PREFIX_CUSTODY_SEARCH_RES_EXT = "cs-res-ext";		
		
	public static final String NS_CUSTODY_SEARCH_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/CustodySearchRequest/1.0";
	public static final String NS_PREFIX_CUSTODY_SEARCH_REQUEST = "cs-req-doc";
	
	public static final String NS_CUSTODY_QUERY_RESULTS_EXCH_DOC = "http://ojbc.org/IEPD/Exchange/CustodyQueryResults/1.0";	
	public static final String NS_PREFIX_CUSTODY_QUERY_RESULTS_EXCH_DOC = "cq-res-ech";
	
	public static final String NS_CUSTODY_QUERY_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/CustodyQueryResultsExtension/1.0";	
	public static final String NS_PREFIX_CUSTODY_QUERY_RESULTS_EXT = "cq-res-ext";	
		
	public static final String NS_COURT_CASE_SEARCH_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/CourtCaseSearchResultsExtension/1.0";
	public static final String NS_PREFIX_COURT_CASE_SEARCH_RESULTS_EXT = "ccs-res-ext"; 
	
	public static final String NS_COURT_CASE_SEARCH_REQUEST_DOC="http://ojbc.org/IEPD/Exchange/CourtCaseSearchRequest/1.0";
	public static final String NS_PREFIX_COURT_CASE_SEARCH_REQUEST="ccs-req-doc";
	
	public static final String NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC = "http://ojbc.org/IEPD/Exchange/CourtCaseQueryResults/1.0";
	public static final String NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC = "ccq-res-doc";
		
	public static final String NS_COURT_CASE_QUERY_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/CourtCaseQueryResultsExtension/1.0";
	public static final String NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXT = "ccq-res-ext";
		
	public static final String NS_CUSTODY_SEARCH_RESULTS = "http://ojbc.org/IEPD/Exchange/CustodySearchResults/1.0";
	public static final String NS_PREFIX_CUSTODY_SEARCH_RESULTS = "cs-res-doc";
	
	public static final String NS_COURT_CASE_SEARCH_RESULTS = "http://ojbc.org/IEPD/Exchange/CourtCaseSearchResults/1.0";
	public static final String NS_PREFIX_COURT_CASE_SEARCH_RESULTS = "ccs-res-doc";
	
	
	public static final String NS_PERSON_SEARCH_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/PersonSearchResults/1.0";
	public static final String NS_PREFIX_PERSON_SEARCH_RESULTS_EXT = "psres";
	public static final String NS_PERSON_SEARCH_RESULTS_DOC = "http://ojbc.org/IEPD/Exchange/PersonSearchResults/1.0";
	public static final String NS_PREFIX_PERSON_SEARCH_RESULTS_DOC = "psres-doc";

	public static final String NS_VEHICLE_SEARCH_RESULTS = "http://ojbc.org/IEPD/Extensions/VehicleSearchResults/1.0";
	public static final String NS_PREFIX_VEHICLE_SEARCH_RESULTS = "vsres";

	public static final String NS_VEHICLE_SEARCH_RESULTS_EXCHANGE = "http://ojbc.org/IEPD/Exchange/VehicleSearchResults/1.0";
	public static final String NS_PREFIX_VEHICLE_SEARCH_RESULTS_EXCHANGE = "vsres-exch";

	public static final String NS_INTEL = "http://niem.gov/niem/domains/intelligence/2.1";
	public static final String NS_PREFIX_INTEL = "intel";

	public static final String NS_INTEL_30 = "http://release.niem.gov/niem/domains/intelligence/3.0/";
	public static final String NS_PREFIX_INTEL_30 = "intel30";
	
	public static final String NS_INTEL_31 = "http://release.niem.gov/niem/domains/intelligence/3.1/";
	public static final String NS_PREFIX_INTEL_31 = "intel31";		
	
	public static final String NS_SEARCH_RESULTS_METADATA_EXT = "http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0";
	public static final String NS_PREFIX_SEARCH_RESULTS_METADATA_EXT = "srm";
	public static final String NS_SEARCH_REQUEST_ERROR_REPORTING = "http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0";
	public static final String NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING = "srer";

	public static final String NS_PREFIX_IR = "ir";
	public static final String NS_IR = "http://ojbc.org/IEPD/Exchange/IncidentReport/1.0";
	public static final String NS_PREFIX_LEXS = "lexs";
	public static final String NS_LEXS = "http://usdoj.gov/leisp/lexs/3.1";
	public static final String NS_PREFIX_LEXSPD = "lexspd";
	public static final String NS_LEXSPD = "http://usdoj.gov/leisp/lexs/publishdiscover/3.1";
	public static final String NS_PREFIX_LEXSDIGEST = "lexsdigest";
	public static final String NS_LEXSDIGEST = "http://usdoj.gov/leisp/lexs/digest/3.1";
	public static final String NS_PREFIX_LEXSLIB = "lexslib";
	
	public static final String NS_ULEX = "http://ulex.gov/ulex/2.0";
	public static final String NS_PREFIX_ULEX = "ulex";

	public static final String NS_ULEX_SR = "http://ulex.gov/searchretrieve/2.0";
	public static final String NS_PREFIX_ULEX_SR = "ulexsr";
	
	public static final String NS_LEXSLIB = "http://usdoj.gov/leisp/lexs/library/3.1";
	public static final String NS_PREFIX_NDEXIA = "ndexia";
	
	public static final String NS_NDEXIA = "http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1";
	public static final String NS_PREFIX_INC_EXT = "inc-ext";
	public static final String NS_INC_EXT = "http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0";

	public static final String NS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String NS_PREFIX_XSI = "xsi";

	public static final String NS_INCIDENT_SEARCH_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/IncidentSearchRequest/1.0";
	public static final String NS_PREFIX_INCIDENT_SEARCH_REQUEST_DOC = "isr-doc";
	public static final String NS_INCIDENT_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0";
	public static final String NS_PREFIX_INCIDENT_SEARCH_REQUEST_EXT = "isr";
	public static final String NS_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/IncidentVehicleSearchRequest/1.0";
	public static final String NS_PREFIX_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT = "ivsr";

	public static final String NS_VEHICLE_SEARCH_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/VehicleSearchRequest/1.0";
	public static final String NS_PREFIX_VEHICLE_SEARCH_REQUEST_DOC = "vsr-doc";
	public static final String NS_VEHICLE_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/VehicleSearchRequest/1.0";
	public static final String NS_PREFIX_VEHICLE_SEARCH_REQUEST_EXT = "vsr";

	public static final String NS_INCIDENT_SEARCH_RESULTS_EXT = "http://ojbc.org/IEPD/Extensions/IncidentSearchResults/1.0";
	public static final String NS_PREFIX_INCIDENT_SEARCH_RESULTS_EXT = "isres";
	public static final String NS_INCIDENT_SEARCH_RESULTS_DOC = "http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0";
	public static final String NS_PREFIX_INCIDENT_SEARCH_RESULTS_DOC = "isres-doc";

	public static final String NS_FBI = "http://niem.gov/niem/fbi/2.0";
	public static final String NS_PREFIX_FBI = "fbi";

	public static final String NS_PROXY_XSD = "http://niem.gov/niem/proxy/xsd/2.0";
	public static final String NS_PREFIX_PROXY_XSD = "niem-xs";

	public static final String NS_PROXY_XSD_30 = "http://release.niem.gov/niem/proxy/xsd/3.0/";
	public static final String NS_PREFIX_PROXY_XSD_30 = "niem-xs-30";

	public static final String NS_CJIS_PROBER_DOC = "http://cjis.gov/CJISProberDocument";
	public static final String NS_PREFIX_CJIS_PROBER_DOC = "cjis-prober-doc";

	public static final String NS_JXDM_303 = "http://www.it.ojp.gov/jxdm/3.0.3";
	public static final String NS_PREFIX_JXDM_303 = "jxdm303";

	public static final String NS_NOTIFICATION_MSG_EXCHANGE = "http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0";
	public static final String NS_PREFIX_NOTIFICATION_MSG_EXCHANGE = "notfm-exch";

	public static final String NS_NOTIFICATION_MSG_EXT = "http://ojbc.org/IEPD/Extensions/Notification/1.0";
	public static final String NS_PREFIX_NOTIFICATION_MSG_EXT = "notfm-ext";

	public static final String NS_HAWAII_EXT = "http://ojbc.org/IEPD/Extensions/Hawaii/1.0";
	public static final String NS_PREFIX_HAWAII_EXT = "hawaii-ext";

	public static final String NS_TOPICS = "http://ojbc.org/wsn/topics";
	public static final String NS_PREFIX_TOPICS = "topics";

	public static final String NS_ACCESS_CONTROL_REQUEST = "http://ojbc.org/IEPD/Exchange/AccessControlRequest/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_REQUEST = "acr-doc";

	public static final String NS_ACCESS_CONTROL_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/AccessControlRequestExtension/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_REQUEST_EXT = "acreq-ext";

	public static final String NS_ACCESS_CONTROL_EXCHANGE = "http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_EXCHANGE = "ac-doc";

	public static final String NS_ACCESS_CONTROL_EXT = "http://ojbc.org/IEPD/Extensions/AccessControlResponse/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_EXT = "ac-ext";

	public static final String NS_POLICY_DECISION_CONTEXT = "http://ojbc.org/IEPD/Extensions/AccessControlDecisionContexts/PolicyBasedAccessControlDecisionContext/1.0";
	public static final String NS_PREFIX_POLICY_DECISION_CONTEXT = "ac-p";

	public static final String NS_ACCESS_CONTROL_ERROR_REPORTING = "http://ojbc.org/IEPD/Extensions/AccessControlRequestErrorReporting/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_ERROR_REPORTING = "acrer";

	public static final String NS_ACCESS_CONTROL_RESULT_METADATA = "http://ojbc.org/IEPD/Extensions/AccessControlResultsMetadata/1.0";
	public static final String NS_PREFIX_ACCESS_CONTROL_RESULT_METADATA = "acr-srm";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE = "http://ojbc.org/IEPD/Exchange/AcknowledgementRecordingRequestForAllPolicies/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE = "arreq-exchange";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/AcknowledgementRecordingRequestExtension/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT = "arreq-ext";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE = "http://ojbc.org/IEPD/Exchange/AcknowledgementRecordingResponse/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE = "arr-exchange";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT = "http://ojbc.org/IEPD/Extensions/AcknowledgementRecordingResponse/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT = "arr-ext";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING = "http://ojbc.org/IEPD/Extensions/AcknowledgementRecordingRequestErrorReporting/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING = "arrer";

	public static final String NS_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA = "http://ojbc.org/IEPD/Extensions/AcknowledgementRecordingResponseMetadata/1.0";
	public static final String NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA = "arr-srm";

	public static final String NS_JUVENILE_HISTORY_CONTAINER = "http://ojbc.org/staticmock/JuvenileHistoryContainer/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_CONTAINER = "jh-container";

	public static final String NS_JUVENILE_HISTORY_CASE_PLAN = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileCasePlanHistoryResponse/1.0";
	public static final String NS_JUVENILE_HISTORY_HEARING = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileHearingHistoryResponse/1.0";
	public static final String NS_JUVENILE_HISTORY_INTAKE = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileIntakeHistoryResponse/1.0";
	public static final String NS_JUVENILE_HISTORY_OFFENSE = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileOffenseHistoryResponse/1.0";
	public static final String NS_JUVENILE_HISTORY_PLACEMENT = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenilePlacementHistoryResponse/1.0";
	public static final String NS_JUVENILE_HISTORY_REFERRAL = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileReferralHistoryResponse/1.0";

	public static final String NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN = "jh-case-plan-doc";
	public static final String NS_PREFIX_JUVENILE_HISTORY_HEARING = "jh-hearing-doc";
	public static final String NS_PREFIX_JUVENILE_HISTORY_INTAKE = "jh-intake-doc";
	public static final String NS_PREFIX_JUVENILE_HISTORY_OFFENSE = "jh-offense-doc";
	public static final String NS_PREFIX_JUVENILE_HISTORY_PLACEMENT = "jh-placement-doc";
	public static final String NS_PREFIX_JUVENILE_HISTORY_REFERRAL = "jh-referral-doc";

	public static final String NS_JUVENILE_HISTORY_CASE_PLAN_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileCasePlanHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_HEARING_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHearingHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_INTAKE_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileIntakeHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_OFFENSE_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileOffenseHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_PLACEMENT_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenilePlacementHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_REFERRAL_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileReferralHistoryExtension/1.0";
	public static final String NS_JUVENILE_HISTORY_EXT = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHistoryCommonExtension/1.0";

	public static final String NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN_EXT = "jh-case-plan";
	public static final String NS_PREFIX_JUVENILE_HISTORY_HEARING_EXT = "jh-hearing";
	public static final String NS_PREFIX_JUVENILE_HISTORY_INTAKE_EXT = "jh-intake";
	public static final String NS_PREFIX_JUVENILE_HISTORY_OFFENSE_EXT = "jh-offense";
	public static final String NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_EXT = "jh-placement";
	public static final String NS_PREFIX_JUVENILE_HISTORY_REFERRAL_EXT = "jh-referral";
	public static final String NS_PREFIX_JUVENILE_HISTORY_EXT = "jh-ext";

	public static final String NS_JUVENILE_HISTORY_REFERRAL_CODES = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileReferralHistoryExtension/michigan/codes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_REFERRAL_CODES = "jh-referral-codes";
	public static final String NS_JUVENILE_HISTORY_OFFENSE_CODES = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileOffenseHistoryExtension/michigan/codes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_OFFENSE_CODES = "jh-offense-codes";
	public static final String NS_JUVENILE_HISTORY_PLACEMENT_CODES = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenilePlacementHistoryExtension/michigan/codes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_CODES = "jh-placement-codes";
	public static final String NS_JUVENILE_HISTORY_INTAKE_CODES = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileIntakeHistoryExtension/michigan/codes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_INTAKE_CODES = "jh-intake-codes";
	public static final String NS_JUVENILE_HISTORY_HEARING_CODES = "http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHearingHistoryExtension/michigan/codes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_HEARING_CODES = "jh-hearing-codes";

	public static final String NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC = "jh-req-doc";
	public static final String NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC = "http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileHistoryQuery/1.0";

	public static final String NS_CYFS = "http://release.niem.gov/niem/domains/cyfs/3.0/";
	public static final String NS_PREFIX_CYFS = "cyfs";

	public static final String NS_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES = "http://ojbc.org/IEPD/Extensions/Michigan/PersonSearchRequestCodes/1.0";
	public static final String NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES = "jh-placement-search-codes";
	public static final String NS_CYFS_21 = "http://niem.gov/niem/domains/cyfs/2.1/1";
	public static final String NS_PREFIX_CYFS_21 = "cyfs21";

	public static final String NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE = "http://ojbc.org/IEPD/Exchange/CycleTrackingIdentifierAssignmentReport/1.0";
	public static final String NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE = "crimhistory-update-exch";

	public static final String NS_ARREST_REPORTING_SERVICE = "http://ojbc.org/IEPD/Exchange/ArrestReport/1.0";
	public static final String NS_PREFIX_ARREST_REPORTING_SERVICE = "arrest-exch";
	
    public static final String NS_ARU = "http://ojbc.org/IEPD/Exchange/ArrestReportUpdate/1.0";
    public static final String NS_PREFIX_ARU = "aru";
    
    public static final String NS_AR_EXT = "http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0";
    public static final String NS_PREFIX_AR_EXT = "ar-ext";
   
    public static final String NS_INCIDENT_PERSON_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/IncidentPersonSearchRequest/1.0";
    public static final String NS_PREFIX_INCIDENT_PERSON_SEARCH_REQUEST_EXT = "ipsr";
    
    public static final String NS_PAROLE_CASE_OFFICER_CHANGE = "http://ojbc.org/IEPD/Exchange/ParoleCaseOfficerChange/1.0";
    public static final String NS_PREFIX_PAROLE_CASE_OFFICER_CHANGE = "prlcoc";
    
    public static final String NS_PROBATION_CASE_OFFICER_CHANGE = "http://ojbc.org/IEPD/Exchange/ProbationCaseOfficerChange/1.0";
    public static final String NS_PREFIX_PROBATION_CASE_OFFICER_CHANGE = "pcoc";
    
    public static final String NS_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH = "http://ojbc.org/IEPD/Exchange/Subscription_Validation_Response/1.0";
    public static final String NS_PREFIX_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH = "subvresp-exch";
    
    public static final String NS_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH = "http://ojbc.org/IEPD/Exchange/Subscription_Modification_Response/1.0";
    public static final String NS_PREFIX_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH = "submresp-exch";
    
    public static final String NS_SUBSCRIPTION_SEARCH_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/SubscriptionSearchRequest/1.0";
    public static final String NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST_EXT = "ssreq-ext";
    
    public static final String NS_VEHICLE_SEARCH_REQUEST = "http://ojbc.org/IEPD/Exchange/VehicleSearchRequest/1.0";
    public static final String NS_PREFIX_VEHICLE_SEARCH_REQUEST = "vsreq";
   
    public static final String NS_VERMONT_DISPOSITION_CODES = "http://ojbc.org/IEPD/Extensions/Vermont/DispositionCodes/1.0";
    public static final String NS_PREFIX_VERMONT_DISPOSITION_CODES = "vt_disp_codes";
    
    public static final String NS_OJBC_DISPOSITION_CODES ="http://ojbc.org/IEPD/Extensions/OJBC/DispositionCodes/1.0";
    public static final String NS_PREFIX_OJBC_DISPOSITION_CODES = "ojbc_disp_codes";
    	
    public static final String NS_HAWAII_JIMS_DISPOSITION_CODES = "http://ojbc.org/IEPD/Extensions/Hawaii/JIMS/DispositionCodes/1.0";
    public static final String NS_PREFIX_HAWAII_JIMS_DISPOSITION_CODES = "hi_jims_disp_codes";
    
    public static final String NS_HAWAII_HAJIS_DISPOSITION_CODES = "http://ojbc.org/IEPD/Extensions/Hawaii/HAJIS/DispositionCodes/1.0";
    public static final String NS_PREFIX_HAWAII_HAJIS_DISPOSITION_CODES = "hi_hajis_disp_codes";
	    
    public static final String NS_AR_HAWAII_EXT = "http://ojbc.org/IEPD/Extensions/Hawaii/IncidentReportStructuredPayload/1.0";
    public static final String NS_PREFIX_AR_HAWAII_EXT = "ar-hi-ext";
    
	public static final String NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT = "http://ojbc.org/IEPD/Extension/CycleTrackingIdentifierAssignmentReport/1.0";
	public static final String NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT = "crimhistory-update-ext";
	
	public static final String NS_HAWAII_BOOKING_REPORT_EXT = "http://hijis.hawaii.gov/BookingReportExtension/1.0";
	public static final String NS_PREFIX_HAWAII_BOOKING_REPORT_EXT = "hi-br-ext";
	
	public static final String NS_CRIMINAL_RECORD = "http://ojbc.org/IEPD/CriminalRecord/1.0";
	public static final String NS_PREFIX_CRIMINAL_RECORD = "cr";
	
	public static final String NS_CRIMINAL_RECORD_EXT = "http://ojbc.org/IEPD/Extensions/CriminalRecordExtension/1.0";
	public static final String NS_PREFIX_CRIMINAL_RECORD_EXT = "cr-ext";
	
	public static final String NS_PERSON_IDENTIFICATION_REQUEST = "http://ojbc.org/IEPD/Exchange/PersonIdentificationRequest/1.0";
    public static final String NS_PREFIX_PERSON_IDENTIFICATION_REQUEST  = "pidreq";

    public static final String NS_PERSON_IDENTIFICATION_RESULTS = "http://ojbc.org/IEPD/Exchange/PersonIdentificationResults/1.0";
    public static final String NS_PREFIX_PERSON_IDENTIFICATION_RESULTS  = "pidresults";

    public static final String NS_IDENTIFICATION_EXT = "http://ojbc.org/IEPD/Extensions/IdentificationExtension/1.0";
    public static final String NS_PREFIX_IDENTIFICATION_EXT  = "ident-ext";

    public static final String NS_ARREST_QUERY_REQUEST = "http://ojbc.org/IEPD/Exchange/ArrestQueryRequest/1.0";
    public static final String NS_PREFIX_ARREST_QUERY_REQUEST  = "aqr";

    public static final String NS_ARREST_QUERY_REQUEST_EXT = "http://ojbc.org/IEPD/Extensions/ArrestQueryRequestExtension/1.0";
    public static final String NS_PREFIX_ARREST_QUERY_REQUEST_EXT  = "aqr-ext";

    public static final String NS_XMIME = "http://www.w3.org/2005/05/xmlmime";
    public static final String NS_PREFIX_XMIME  = "xmime";

    public static final String NS_XOP = "http://www.w3.org/2004/08/xop/include";
    public static final String NS_PREFIX_XOP  = "xop";
    
    public static final String NS_WSOMA = "http://www.w3.org/2007/08/soap12-mtom-policy";
    public static final String NS_PREFIX_WSOMA  = "wsoma";
	
    public static final String NS_PREFIX_CHARGE_REFERRAL_DOC = "cr-doc";
    public static final String NS_CHARGE_REFERRAL_DOC ="http://ojbc.org/IEPD/Exchange/ChargeReferral/1.0";
    
    public static final String NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST = "ira-req-doc";
    public static final String NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST ="http://ojbc.org/IEPD/Exchange/IdentificationResultsArchiveRequest/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST = "oirs-req-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationResultsSearchRequest/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT = "oirs-req-ext";
    public static final String NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT ="http://ojbc.org/IEPD/Extensions/OrganizationIdentificationResultsSearchRequest/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS = "oirs-res-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationResultsSearchResults/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT = "oirs-res-ext";
    public static final String NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT ="http://ojbc.org/IEPD/Extensions/OrganizationIdentificationResultsSearchResults/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST = "oiirq-req-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationInitialResultsQueryRequest/1.0";

    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST = "oisrq-req-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationSubsequentResultsQueryRequest/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS = "oiirq-res-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationInitialResultsQueryResults/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT = "oirq-res-ext";
    public static final String NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT ="http://ojbc.org/IEPD/Extensions/OrganizationIdentificationResultsQueryResults/1.0";
    
    public static final String NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS = "oisrq-res-doc";
    public static final String NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS ="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationSubsequentResultsQueryResults/1.0";

    public static final String NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE = "irm-resp-doc";
    public static final String NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE ="http://ojbc.org/IEPD/Exchange/IdentificationResultsModificationResponse/1.0";

    public static final String NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT = "irm-resp-ext";
    public static final String NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT ="http://ojbc.org/IEPD/Extensions/IdentificationResultsModificationResponse/1.0";

    public static final String NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING = "irm-err-rep";
    public static final String NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING ="http://ojbc.org/IEPD/Extensions/IdentificationResultsModificationRequestErrorReporting/1.0";

    public static final String NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_METADATA = "irm-rm";
    public static final String NS_IDENTIFICATION_RESULTS_MODIFICATION_METADATA ="http://ojbc.org/IEPD/Extensions/IdentificationResultsModificationResponseMetadata/1.0";

    public static final String NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE = "pse-doc";
    public static final String NS_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE ="http://ojbc.org/IEPD/Exchange/PretrialServiceEnrollment/1.0";
    
    public static final String NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXT = "pse-ext";
    public static final String NS_PRETRIAL_SERVICE_ENROLLMENT_EXT ="http://ojbc.org/IEPD/Extensions/PretrialServiceEnrollment/1.0";
    
    public static final String NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT = "cdr-report-doc";
    public static final String NS_COURT_DISPOSITION_RECORDING_REPORT ="http://ojbc.org/IEPD/Exchange/CourtDispositionRecordingReport/1.0";
    
    public static final String NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT_EXT = "cdr-report-ext";
    public static final String NS_COURT_DISPOSITION_RECORDING_REPORT_EXT ="http://ojbc.org/IEPD/Extension/CourtDispositionRecordingReport/1.0";
    
    public static final String NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT = "pdr-report-doc";
    public static final String NS_PROSECUTION_DECISION_RECORDING_REPORT ="http://ojbc.org/IEPD/Exchange/ProsecutionDecisionRecordingReport/1.0";
    
    public static final String NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT_EXT = "pdr-report-ext";
    public static final String NS_PROSECUTION_DECISION_RECORDING_REPORT_EXT ="http://ojbc.org/IEPD/Extension/ProsecutionDecisionRecordingReport/1.0";
    
    public static final String NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT = "fed_subcr-doc";
    public static final String NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT ="http://ojbc.org/IEPD/Exchange/FederalSubscriptionCreationReport/1.0";
    
    public static final String NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT = "fed_subcr-ext";
    public static final String NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT ="http://ojbc.org/IEPD/Extensions/FederalSubscriptionCreationReportExtension/1.0";
    
    public static final String NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT = "fed_subcr_upd-doc";
    public static final String NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT ="http://ojbc.org/IEPD/Exchange/FederalSubscriptionUpdateReport/1.0";
    
    public static final String NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT = "fed_subcr_upd-ext";
    public static final String NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT ="http://ojbc.org/IEPD/Extensions/FederalSubscriptionUpdateReportExtension/1.0";
    
	private Map<String, String> prefixToUriMap;
	private Map<String, String> uriToPrefixMap;
	
	
	public OjbcNamespaceContext() {
		
		prefixToUriMap = new HashMap<String, String>();
		uriToPrefixMap = new HashMap<String, String>();			
		
		prefixToUriMap.put(NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXT, NS_COURT_CASE_QUERY_RESULTS_EXT);
		uriToPrefixMap.put(NS_COURT_CASE_QUERY_RESULTS_EXT, NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXT);		
		
		prefixToUriMap.put(NS_PREFIX_CUSTODY_QUERY_RESULTS_EXT, NS_CUSTODY_QUERY_RESULTS_EXT);
		uriToPrefixMap.put(NS_CUSTODY_QUERY_RESULTS_EXT, NS_PREFIX_CUSTODY_QUERY_RESULTS_EXT);
		
		prefixToUriMap.put(NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC, NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC);
		uriToPrefixMap.put(NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC, NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC);		
				
		prefixToUriMap.put(NS_PREFIX_CUSTODY_QUERY_RESULTS_EXCH_DOC, NS_CUSTODY_QUERY_RESULTS_EXCH_DOC);		
		uriToPrefixMap.put(NS_CUSTODY_QUERY_RESULTS_EXCH_DOC, NS_PREFIX_CUSTODY_QUERY_RESULTS_EXCH_DOC);		
		
		prefixToUriMap.put(NS_PREFIX_CUSTODY_SEARCH_RES_EXT, NS_CUSTODY_SEARCH_RES_EXT);
		uriToPrefixMap.put(NS_CUSTODY_SEARCH_RES_EXT, NS_PREFIX_CUSTODY_SEARCH_RES_EXT);		
		
		prefixToUriMap.put(NS_PREFIX_INTEL_31, NS_INTEL_31);
		uriToPrefixMap.put(NS_INTEL_31, NS_PREFIX_INTEL_31);
		
		prefixToUriMap.put(NS_PREFIX_COURT_CASE_SEARCH_RESULTS_EXT, NS_COURT_CASE_SEARCH_RESULTS_EXT);
		uriToPrefixMap.put(NS_COURT_CASE_SEARCH_RESULTS_EXT, NS_PREFIX_COURT_CASE_SEARCH_RESULTS_EXT);		
		
		prefixToUriMap.put(NS_PREFIX_JXDM_51, NS_JXDM_51);
		uriToPrefixMap.put(NS_JXDM_51, NS_PREFIX_JXDM_51);
				
		prefixToUriMap.put(NS_PREFIX_CUSTODY_SEARCH_RESULTS, NS_CUSTODY_SEARCH_RESULTS);
		uriToPrefixMap.put(NS_CUSTODY_SEARCH_RESULTS, NS_PREFIX_CUSTODY_SEARCH_RESULTS);
		
		prefixToUriMap.put(NS_PREFIX_COURT_CASE_SEARCH_RESULTS, NS_COURT_CASE_SEARCH_RESULTS);
		uriToPrefixMap.put(NS_COURT_CASE_SEARCH_RESULTS, NS_PREFIX_COURT_CASE_SEARCH_RESULTS);
				
		prefixToUriMap.put(NS_PREFIX_CUSTODY_SEARCH_REQUEST, NS_CUSTODY_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_CUSTODY_SEARCH_REQUEST_DOC, NS_PREFIX_CUSTODY_SEARCH_REQUEST);
				
		prefixToUriMap.put(NS_PREFIX_COURT_CASE_SEARCH_REQUEST, NS_COURT_CASE_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_COURT_CASE_SEARCH_REQUEST_DOC, NS_PREFIX_COURT_CASE_SEARCH_REQUEST);		
				
		prefixToUriMap.put(NS_NIST_BIO_PREFIX, NS_NIST_BIO);
		uriToPrefixMap.put(NS_NIST_BIO, NS_NIST_BIO_PREFIX);
				
		prefixToUriMap.put(NS_PREFIX_NIEM_BIO, NS_NIEM_BIO);
		uriToPrefixMap.put(NS_NIEM_BIO, NS_PREFIX_NIEM_BIO);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT);
		
		prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE);
		
		prefixToUriMap.put(NS_DISPOSITION_REPORTS_PREFIX, NS_DISPOSITION_REPORTS);
		uriToPrefixMap.put(NS_DISPOSITION_REPORTS, NS_DISPOSITION_REPORTS_PREFIX);

		prefixToUriMap.put(NS_PREFIX_CHARGE_REFERRAL_DOC, NS_CHARGE_REFERRAL_DOC);
		uriToPrefixMap.put(NS_CHARGE_REFERRAL_DOC, NS_PREFIX_CHARGE_REFERRAL_DOC);

		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC, NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC, NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_CONTAINER, NS_JUVENILE_HISTORY_CONTAINER);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_CONTAINER, NS_PREFIX_JUVENILE_HISTORY_CONTAINER);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES, NS_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES, NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_SEARCH_CODES);
		prefixToUriMap.put(NS_PREFIX_CYFS_21, NS_CYFS_21);
		uriToPrefixMap.put(NS_CYFS_21, NS_PREFIX_CYFS_21);

		prefixToUriMap.put(NS_PREFIX_CYFS, NS_CYFS);
		uriToPrefixMap.put(NS_CYFS, NS_PREFIX_CYFS);

		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_REFERRAL_CODES, NS_JUVENILE_HISTORY_REFERRAL_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_REFERRAL_CODES, NS_PREFIX_JUVENILE_HISTORY_REFERRAL_CODES);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_OFFENSE_CODES, NS_JUVENILE_HISTORY_OFFENSE_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_OFFENSE_CODES, NS_PREFIX_JUVENILE_HISTORY_OFFENSE_CODES);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_CODES, NS_JUVENILE_HISTORY_PLACEMENT_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_PLACEMENT_CODES, NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_CODES);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_INTAKE_CODES, NS_JUVENILE_HISTORY_INTAKE_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_INTAKE_CODES, NS_PREFIX_JUVENILE_HISTORY_INTAKE_CODES);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_HEARING_CODES, NS_JUVENILE_HISTORY_HEARING_CODES);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_HEARING_CODES, NS_PREFIX_JUVENILE_HISTORY_HEARING_CODES);

		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN, NS_JUVENILE_HISTORY_CASE_PLAN);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_CASE_PLAN, NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_HEARING, NS_JUVENILE_HISTORY_HEARING);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_HEARING, NS_PREFIX_JUVENILE_HISTORY_HEARING);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_INTAKE, NS_JUVENILE_HISTORY_INTAKE);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_INTAKE, NS_PREFIX_JUVENILE_HISTORY_INTAKE);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_OFFENSE, NS_JUVENILE_HISTORY_OFFENSE);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_OFFENSE, NS_PREFIX_JUVENILE_HISTORY_OFFENSE);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_PLACEMENT, NS_JUVENILE_HISTORY_PLACEMENT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_PLACEMENT, NS_PREFIX_JUVENILE_HISTORY_PLACEMENT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_REFERRAL, NS_JUVENILE_HISTORY_REFERRAL);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_REFERRAL, NS_PREFIX_JUVENILE_HISTORY_REFERRAL);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN_EXT, NS_JUVENILE_HISTORY_CASE_PLAN_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_CASE_PLAN_EXT, NS_PREFIX_JUVENILE_HISTORY_CASE_PLAN_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_HEARING_EXT, NS_JUVENILE_HISTORY_HEARING_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_HEARING_EXT, NS_PREFIX_JUVENILE_HISTORY_HEARING_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_INTAKE_EXT, NS_JUVENILE_HISTORY_INTAKE_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_INTAKE_EXT, NS_PREFIX_JUVENILE_HISTORY_INTAKE_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_OFFENSE_EXT, NS_JUVENILE_HISTORY_OFFENSE_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_OFFENSE_EXT, NS_PREFIX_JUVENILE_HISTORY_OFFENSE_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_EXT, NS_JUVENILE_HISTORY_PLACEMENT_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_PLACEMENT_EXT, NS_PREFIX_JUVENILE_HISTORY_PLACEMENT_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_REFERRAL_EXT, NS_JUVENILE_HISTORY_REFERRAL_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_REFERRAL_EXT, NS_PREFIX_JUVENILE_HISTORY_REFERRAL_EXT);
		prefixToUriMap.put(NS_PREFIX_JUVENILE_HISTORY_EXT, NS_JUVENILE_HISTORY_EXT);
		uriToPrefixMap.put(NS_JUVENILE_HISTORY_EXT, NS_PREFIX_JUVENILE_HISTORY_EXT);

		prefixToUriMap.put(NS_SOAP_PREFIX, NS_SOAP);
		uriToPrefixMap.put(NS_SOAP, NS_SOAP_PREFIX);
		prefixToUriMap.put(NS_PREFIX_JXDM_40, NS_JXDM_40);
		uriToPrefixMap.put(NS_JXDM_40, NS_PREFIX_JXDM_40);
		prefixToUriMap.put(NS_PREFIX_JXDM_50, NS_JXDM_50);
		uriToPrefixMap.put(NS_JXDM_50, NS_PREFIX_JXDM_50);
		prefixToUriMap.put(NS_PREFIX_JXDM_41, NS_JXDM_41);
		uriToPrefixMap.put(NS_JXDM_41, NS_PREFIX_JXDM_41);
		prefixToUriMap.put(NS_PREFIX_USPS_STATES, NS_USPS_STATES);
		uriToPrefixMap.put(NS_USPS_STATES, NS_PREFIX_USPS_STATES);
		prefixToUriMap.put(NS_PREFIX_ERROR, NS_ERROR);
		uriToPrefixMap.put(NS_ERROR, NS_PREFIX_ERROR);
		prefixToUriMap.put(NS_PREFIX_WARRANT_EXT, NS_WARRANT_EXT);
		uriToPrefixMap.put(NS_WARRANT_EXT, NS_PREFIX_WARRANT_EXT);
		prefixToUriMap.put(NS_PREFIX_NC, NS_NC);
		uriToPrefixMap.put(NS_NC, NS_PREFIX_NC);
		prefixToUriMap.put(NS_PREFIX_NC_30, NS_NC_30);
		uriToPrefixMap.put(NS_NC_30, NS_PREFIX_NC_30);
		prefixToUriMap.put(NS_PREFIX_WARRANT, NS_WARRANT);
		uriToPrefixMap.put(NS_WARRANT, NS_PREFIX_WARRANT);
		prefixToUriMap.put(NS_PREFIX_STRUCTURES, NS_STRUCTURES);
		uriToPrefixMap.put(NS_STRUCTURES, NS_PREFIX_STRUCTURES);
		prefixToUriMap.put(NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
		uriToPrefixMap.put(NS_STRUCTURES_30, NS_PREFIX_STRUCTURES_30);
		prefixToUriMap.put(NS_PREFIX_CH, NS_CH);
		uriToPrefixMap.put(NS_CH, NS_PREFIX_CH);
		prefixToUriMap.put(NS_PREFIX_RAPSHEET_41, NS_RAPSHEET_41);
		uriToPrefixMap.put(NS_RAPSHEET_41, NS_PREFIX_RAPSHEET_41);
		prefixToUriMap.put(NS_PREFIX_CH_DOC, NS_CH_DOC);
		uriToPrefixMap.put(NS_CH_DOC, NS_PREFIX_CH_DOC);
		prefixToUriMap.put(NS_PREFIX_CH_EXT, NS_CH_EXT);
		uriToPrefixMap.put(NS_CH_EXT, NS_PREFIX_CH_EXT);
		prefixToUriMap.put(NS_PREFIX_ANSI_NIST, NS_ANSI_NIST);
		uriToPrefixMap.put(NS_ANSI_NIST, NS_PREFIX_ANSI_NIST);
		prefixToUriMap.put(NS_PREFIX_SCREENING, NS_SCREENING);
		uriToPrefixMap.put(NS_SCREENING, NS_PREFIX_SCREENING);
		prefixToUriMap.put(NS_PREFIX_SCREENING_21, NS_SCREENING_21);
		uriToPrefixMap.put(NS_SCREENING_21, NS_PREFIX_SCREENING_21);
		prefixToUriMap.put(NS_DISPOSITION_EXCHANGE_PREFIX, NS_DISPOSITION_EXCHANGE);
		uriToPrefixMap.put(NS_DISPOSITION_EXCHANGE, NS_DISPOSITION_EXCHANGE_PREFIX);
		prefixToUriMap.put(NS_DISPOSITION_EXTENSION_PREFIX, NS_DISPOSITION_EXTENSION);
		uriToPrefixMap.put(NS_DISPOSITION_EXTENSION, NS_DISPOSITION_EXTENSION_PREFIX);
		prefixToUriMap.put(NS_MAINE_DISPOSITION_CODES_PREFIX, NS_MAINE_DISPOSITION_CODES);
		uriToPrefixMap.put(NS_MAINE_DISPOSITION_CODES, NS_MAINE_DISPOSITION_CODES_PREFIX);

		prefixToUriMap.put(NS_SUB_VALID_MESSAGE_PREFIX, NS_SUB_VALID_MESSAGE);
		uriToPrefixMap.put(NS_SUB_VALID_MESSAGE, NS_SUB_VALID_MESSAGE_PREFIX);
		
		prefixToUriMap.put(NS_SUB_MODIFY_MESSAGE_PREFIX, NS_SUB_MODIFY_MESSAGE);
		uriToPrefixMap.put(NS_SUB_MODIFY_MESSAGE, NS_SUB_MODIFY_MESSAGE_PREFIX);

		prefixToUriMap.put(NS_PREFIX_INCIDENT_QUERY_REQUEST_DOC, NS_INCIDENT_QUERY_REQUEST_DOC);
		uriToPrefixMap.put(NS_INCIDENT_QUERY_REQUEST_DOC, NS_PREFIX_INCIDENT_QUERY_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_QUERY_REQUEST_EXT, NS_INCIDENT_QUERY_REQUEST_EXT);
		uriToPrefixMap.put(NS_INCIDENT_QUERY_REQUEST_EXT, NS_PREFIX_INCIDENT_QUERY_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_PERSON_QUERY_REQUEST, NS_PERSON_QUERY_REQUEST);
		uriToPrefixMap.put(NS_PERSON_QUERY_REQUEST, NS_PREFIX_PERSON_QUERY_REQUEST);
		prefixToUriMap.put(NS_PREFIX_PERSON_SEARCH_REQUEST_EXT, NS_PERSON_SEARCH_REQUEST_EXT);
		uriToPrefixMap.put(NS_PERSON_SEARCH_REQUEST_EXT, NS_PREFIX_PERSON_SEARCH_REQUEST_EXT);
		prefixToUriMap.put(NS_PREFIX_PERSON_SEARCH_REQUEST_DOC, NS_PERSON_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_PERSON_SEARCH_REQUEST_DOC, NS_PREFIX_PERSON_SEARCH_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_PERSON_SEARCH_RESULTS_EXT, NS_PERSON_SEARCH_RESULTS_EXT);
		uriToPrefixMap.put(NS_PERSON_SEARCH_RESULTS_EXT, NS_PREFIX_PERSON_SEARCH_RESULTS_EXT);
		prefixToUriMap.put(NS_PREFIX_PERSON_SEARCH_RESULTS_DOC, NS_PERSON_SEARCH_RESULTS_DOC);
		uriToPrefixMap.put(NS_PERSON_SEARCH_RESULTS_DOC, NS_PREFIX_PERSON_SEARCH_RESULTS_DOC);
		prefixToUriMap.put(NS_PREFIX_INTEL, NS_INTEL);
		uriToPrefixMap.put(NS_INTEL, NS_PREFIX_INTEL);
		prefixToUriMap.put(NS_PREFIX_IR, NS_IR);
		uriToPrefixMap.put(NS_IR, NS_PREFIX_IR);
		prefixToUriMap.put(NS_PREFIX_LEXS, NS_LEXS);
		uriToPrefixMap.put(NS_LEXS, NS_PREFIX_LEXS);
		prefixToUriMap.put(NS_PREFIX_LEXSPD, NS_LEXSPD);
		uriToPrefixMap.put(NS_LEXSPD, NS_PREFIX_LEXSPD);
		prefixToUriMap.put(NS_PREFIX_LEXSDIGEST, NS_LEXSDIGEST);
		uriToPrefixMap.put(NS_LEXSDIGEST, NS_PREFIX_LEXSDIGEST);
		prefixToUriMap.put(NS_PREFIX_LEXSLIB, NS_LEXSLIB);
		uriToPrefixMap.put(NS_LEXSLIB, NS_PREFIX_LEXSLIB);

		prefixToUriMap.put(NS_PREFIX_ULEX, NS_ULEX);
		uriToPrefixMap.put(NS_ULEX, NS_PREFIX_ULEX);

		prefixToUriMap.put(NS_PREFIX_ULEX_SR, NS_ULEX_SR);
		uriToPrefixMap.put(NS_ULEX_SR, NS_PREFIX_ULEX_SR);
		
		prefixToUriMap.put(NS_PREFIX_NDEXIA, NS_NDEXIA);
		uriToPrefixMap.put(NS_NDEXIA, NS_PREFIX_NDEXIA);
		prefixToUriMap.put(NS_PREFIX_INC_EXT, NS_INC_EXT);
		uriToPrefixMap.put(NS_INC_EXT, NS_PREFIX_INC_EXT);
		prefixToUriMap.put(NS_PREFIX_XSI, NS_XSI);
		uriToPrefixMap.put(NS_XSI, NS_PREFIX_XSI);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_SEARCH_REQUEST_DOC, NS_INCIDENT_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_INCIDENT_SEARCH_REQUEST_DOC, NS_PREFIX_INCIDENT_SEARCH_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_SEARCH_REQUEST_EXT, NS_INCIDENT_SEARCH_REQUEST_EXT);
		uriToPrefixMap.put(NS_INCIDENT_SEARCH_REQUEST_EXT, NS_PREFIX_INCIDENT_SEARCH_REQUEST_EXT);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT, NS_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT);
		uriToPrefixMap.put(NS_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT, NS_PREFIX_INCIDENT_VEHICLE_SEARCH_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_VEHICLE_SEARCH_REQUEST_DOC, NS_VEHICLE_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_VEHICLE_SEARCH_REQUEST_DOC, NS_PREFIX_VEHICLE_SEARCH_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_VEHICLE_SEARCH_REQUEST_EXT, NS_VEHICLE_SEARCH_REQUEST_EXT);
		uriToPrefixMap.put(NS_VEHICLE_SEARCH_REQUEST_EXT, NS_PREFIX_VEHICLE_SEARCH_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_INCIDENT_SEARCH_RESULTS_EXT, NS_INCIDENT_SEARCH_RESULTS_EXT);
		uriToPrefixMap.put(NS_INCIDENT_SEARCH_RESULTS_EXT, NS_PREFIX_INCIDENT_SEARCH_RESULTS_EXT);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_SEARCH_RESULTS_DOC, NS_INCIDENT_SEARCH_RESULTS_DOC);
		uriToPrefixMap.put(NS_INCIDENT_SEARCH_RESULTS_DOC, NS_PREFIX_INCIDENT_SEARCH_RESULTS_DOC);

		prefixToUriMap.put(NS_PREFIX_FIREARM_DOC, NS_FIREARM_DOC);
		uriToPrefixMap.put(NS_FIREARM_DOC, NS_PREFIX_FIREARM_DOC);
		prefixToUriMap.put(NS_PREFIX_FIREARM_EXT, NS_FIREARM_EXT);
		uriToPrefixMap.put(NS_FIREARM_EXT, NS_PREFIX_FIREARM_EXT);

		prefixToUriMap.put(NS_PREFIX_FIREARM_SEARCH_REQUEST_DOC, NS_FIREARM_SEARCH_REQUEST_DOC);
		uriToPrefixMap.put(NS_FIREARM_SEARCH_REQUEST_DOC, NS_PREFIX_FIREARM_SEARCH_REQUEST_DOC);

		prefixToUriMap.put(NS_PREFIX_FIREARM_SEARCH_REQUEST_EXT, NS_FIREARM_SEARCH_REQUEST_EXT);
		uriToPrefixMap.put(NS_FIREARM_SEARCH_REQUEST_EXT, NS_PREFIX_FIREARM_SEARCH_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_FIREARM_SEARCH_RESULT_DOC, NS_FIREARM_SEARCH_RESULT_DOC);
		uriToPrefixMap.put(NS_FIREARM_SEARCH_RESULT_DOC, NS_PREFIX_FIREARM_SEARCH_RESULT_DOC);

		prefixToUriMap.put(NS_PREFIX_FIREARM_SEARCH_RESULT_EXT, NS_FIREARM_SEARCH_RESULT_EXT);
		uriToPrefixMap.put(NS_FIREARM_SEARCH_RESULT_EXT, NS_PREFIX_FIREARM_SEARCH_RESULT_EXT);

		prefixToUriMap.put(NS_PREFIX_FIREARMS_CODES_HAWAII, NS_FIREARMS_CODES_HAWAII);
		uriToPrefixMap.put(NS_FIREARMS_CODES_HAWAII, NS_PREFIX_FIREARMS_CODES_HAWAII);

		prefixToUriMap.put(NS_PREFIX_IAD, NS_IAD);
		uriToPrefixMap.put(NS_IAD, NS_PREFIX_IAD);
		prefixToUriMap.put(NS_PREFIX_QRER, NS_QRER);
		uriToPrefixMap.put(NS_QRER, NS_PREFIX_QRER);
		prefixToUriMap.put(NS_PREFIX_QRM, NS_QRM);
		uriToPrefixMap.put(NS_QRM, NS_PREFIX_QRM);

		prefixToUriMap.put(NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC, NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC);
		uriToPrefixMap.put(NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC, NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC);
		prefixToUriMap.put(NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT, NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT);
		uriToPrefixMap.put(NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT, NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_FBI, NS_FBI);
		prefixToUriMap.put(NS_PREFIX_PROXY_XSD, NS_PROXY_XSD);
		uriToPrefixMap.put(NS_FBI, NS_PREFIX_FBI);
		uriToPrefixMap.put(NS_PROXY_XSD, NS_PREFIX_PROXY_XSD);

		uriToPrefixMap.put(NS_FIREARMS_CODES_DEMOSTATE, NS_PREFIX_FIREARMS_CODES_DEMOSTATE);
		prefixToUriMap.put(NS_PREFIX_FIREARMS_CODES_DEMOSTATE, NS_FIREARMS_CODES_DEMOSTATE);

		uriToPrefixMap.put(NS_INCIDENT_LOCATION_CODES_DEMOSTATE, NS_PREFIX_INCIDENT_LOCATION_CODES_DEMOSTATE);
		prefixToUriMap.put(NS_PREFIX_INCIDENT_LOCATION_CODES_DEMOSTATE, NS_INCIDENT_LOCATION_CODES_DEMOSTATE);

		uriToPrefixMap.put(NS_PROBATION_CASE_INITIATION, NS_PREFIX_PROBATION_CASE_INITIATION);
		prefixToUriMap.put(NS_PREFIX_PROBATION_CASE_INITIATION, NS_PROBATION_CASE_INITIATION);

		uriToPrefixMap.put(NS_PAROLE_CASE_INITIATION, NS_PREFIX_PAROLE_CASE_INITIATION);
		prefixToUriMap.put(NS_PREFIX_PAROLE_CASE_INITIATION, NS_PAROLE_CASE_INITIATION);

		uriToPrefixMap.put(NS_PROBATION_EXTENSION, NS_PREFIX_PROBATION_EXTENSION);
		prefixToUriMap.put(NS_PREFIX_PROBATION_EXTENSION, NS_PROBATION_EXTENSION);

		uriToPrefixMap.put(NS_PROBATION_CASE_TERMINATION, NS_PREFIX_PROBATION_CASE_TERMINATION);
		prefixToUriMap.put(NS_PREFIX_PROBATION_CASE_TERMINATION, NS_PROBATION_CASE_TERMINATION);

		uriToPrefixMap.put(NS_PAROLE_CASE_TERMINATION, NS_PREFIX_PAROLE_CASE_TERMINATION);
		prefixToUriMap.put(NS_PREFIX_PAROLE_CASE_TERMINATION, NS_PAROLE_CASE_TERMINATION);

		uriToPrefixMap.put(NS_PAROLE_CASE, NS_PREFIX_PAROLE_CASE);
		prefixToUriMap.put(NS_PREFIX_PAROLE_CASE, NS_PAROLE_CASE);

		uriToPrefixMap.put(NS_ENT_MERGE_RESULT_MSG, NS_PREFIX_ENT_MERGE_RESULT_MSG);
		prefixToUriMap.put(NS_PREFIX_ENT_MERGE_RESULT_MSG, NS_ENT_MERGE_RESULT_MSG);

		uriToPrefixMap.put(NS_ENT_MERGE_RESULT_MSG_EXT, NS_PREFIX_ENT_MERGE_RESULT_MSG_EXT);
		prefixToUriMap.put(NS_PREFIX_ENT_MERGE_RESULT_MSG_EXT, NS_ENT_MERGE_RESULT_MSG_EXT);
		uriToPrefixMap.put(NS_ENT_MERGE_REQUEST_MSG, NS_PREFIX_ENT_MERGE_REQUEST_MSG);
		prefixToUriMap.put(NS_PREFIX_ENT_MERGE_REQUEST_MSG, NS_ENT_MERGE_REQUEST_MSG);

		uriToPrefixMap.put(NS_BF2, NS_PREFIX_BF2);
		prefixToUriMap.put(NS_PREFIX_BF2, NS_BF2);

		uriToPrefixMap.put(NS_SUBSCRIPTION_RESPONSE_EXT, NS_PREFIX_SUBSCRIPTION_RESPONSE_EXT);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_RESPONSE_EXT, NS_SUBSCRIPTION_RESPONSE_EXT);

		uriToPrefixMap.put(NS_SUBSCRIPTION_RESPONSE_EXCH, NS_PREFIX_SUBSCRIPTION_RESPONSE_EXCH);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_RESPONSE_EXCH, NS_SUBSCRIPTION_RESPONSE_EXCH);

		uriToPrefixMap.put(NS_SUBSCRIPTION_FAULT_RESPONSE_EXT, NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXT);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXT, NS_SUBSCRIPTION_FAULT_RESPONSE_EXT);

		uriToPrefixMap.put(NS_SUBSCRIPTION_FAULT_RESPONSE_EXCH, NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXCH);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_FAULT_RESPONSE_EXCH, NS_SUBSCRIPTION_FAULT_RESPONSE_EXCH);

		uriToPrefixMap.put(NS_SUBSCRIPTION_SEARCH_REQUEST, NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST, NS_SUBSCRIPTION_SEARCH_REQUEST);

		uriToPrefixMap.put(NS_SUBSCRIPTION_SEARCH_RESULTS, NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS, NS_SUBSCRIPTION_SEARCH_RESULTS);

		uriToPrefixMap.put(NS_SUBSCRIPTION_SEARCH_RESULTS_EXT, NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS_EXT);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_SEARCH_RESULTS_EXT, NS_SUBSCRIPTION_SEARCH_RESULTS_EXT);

		uriToPrefixMap.put(NS_SUBSCRIPTION_QUERY_RESULTS, NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS, NS_SUBSCRIPTION_QUERY_RESULTS);

		uriToPrefixMap.put(NS_SUBSCRIPTION_QUERY_RESULTS_EXT, NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS_EXT);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_QUERY_RESULTS_EXT, NS_SUBSCRIPTION_QUERY_RESULTS_EXT);

		uriToPrefixMap.put(NS_SUBSCRIPTION_QUERY_REQUEST, NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST, NS_SUBSCRIPTION_QUERY_REQUEST);

		uriToPrefixMap.put(NS_SUBSCRIPTION_QUERY_REQUEST_EXT, NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST_EXT);
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_QUERY_REQUEST_EXT, NS_SUBSCRIPTION_QUERY_REQUEST_EXT);

		uriToPrefixMap.put(NS_B2, NS_PREFIX_B2);
		prefixToUriMap.put(NS_PREFIX_B2, NS_B2);

		uriToPrefixMap.put(NS_ADD, NS_PREFIX_ADD);
		prefixToUriMap.put(NS_PREFIX_ADD, NS_ADD);

		uriToPrefixMap.put(NS_WSN_BROKERED, NS_PREFIX_WSN_BROKERED);
		prefixToUriMap.put(NS_PREFIX_WSN_BROKERED, NS_WSN_BROKERED);

		uriToPrefixMap.put(NS_SEARCH_RESULTS_METADATA_EXT, NS_PREFIX_SEARCH_RESULTS_METADATA_EXT);
		prefixToUriMap.put(NS_PREFIX_SEARCH_RESULTS_METADATA_EXT, NS_SEARCH_RESULTS_METADATA_EXT);
		uriToPrefixMap.put(NS_SEARCH_REQUEST_ERROR_REPORTING, NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING);
		prefixToUriMap.put(NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING, NS_SEARCH_REQUEST_ERROR_REPORTING);

		uriToPrefixMap.put(NS_SUB_MSG_EXCHANGE, NS_PREFIX_SUB_MSG_EXCHANGE);
		prefixToUriMap.put(NS_PREFIX_SUB_MSG_EXCHANGE, NS_SUB_MSG_EXCHANGE);

		uriToPrefixMap.put(NS_SUB_MSG_EXT, NS_PREFIX_SUB_MSG_EXT);
		prefixToUriMap.put(NS_PREFIX_SUB_MSG_EXT, NS_SUB_MSG_EXT);

		uriToPrefixMap.put(NS_UNBSUB_MSG_EXCHANGE, NS_PREFIX_UNSUB_MSG_EXCHANGE);
		prefixToUriMap.put(NS_PREFIX_UNSUB_MSG_EXCHANGE, NS_UNBSUB_MSG_EXCHANGE);

		uriToPrefixMap.put(NS_ER_EXT, NS_PREFIX_ER_EXT);
		prefixToUriMap.put(NS_PREFIX_ER_EXT, NS_ER_EXT);

		uriToPrefixMap.put(NS_SAML_ASSERTION, NS_PREFIX_SAML_ASSERTION);
		prefixToUriMap.put(NS_PREFIX_SAML_ASSERTION, NS_SAML_ASSERTION);

		uriToPrefixMap.put(NS_VEHICLE_SEARCH_RESULTS, NS_PREFIX_VEHICLE_SEARCH_RESULTS);
		prefixToUriMap.put(NS_PREFIX_VEHICLE_SEARCH_RESULTS, NS_VEHICLE_SEARCH_RESULTS);

		uriToPrefixMap.put(NS_VEHICLE_SEARCH_RESULTS_EXCHANGE, NS_PREFIX_VEHICLE_SEARCH_RESULTS_EXCHANGE);
		prefixToUriMap.put(NS_PREFIX_VEHICLE_SEARCH_RESULTS_EXCHANGE, NS_VEHICLE_SEARCH_RESULTS_EXCHANGE);

		uriToPrefixMap.put(NS_CJIS_PROBER_DOC, NS_PREFIX_CJIS_PROBER_DOC);
		prefixToUriMap.put(NS_PREFIX_CJIS_PROBER_DOC, NS_CJIS_PROBER_DOC);

		uriToPrefixMap.put(NS_JXDM_303, NS_PREFIX_JXDM_303);
		prefixToUriMap.put(NS_PREFIX_JXDM_303, NS_JXDM_303);

		uriToPrefixMap.put(NS_NOTIFICATION_MSG_EXCHANGE, NS_PREFIX_NOTIFICATION_MSG_EXCHANGE);
		prefixToUriMap.put(NS_PREFIX_NOTIFICATION_MSG_EXCHANGE, NS_NOTIFICATION_MSG_EXCHANGE);

		uriToPrefixMap.put(NS_NOTIFICATION_MSG_EXT, NS_PREFIX_NOTIFICATION_MSG_EXT);
		prefixToUriMap.put(NS_PREFIX_NOTIFICATION_MSG_EXT, NS_NOTIFICATION_MSG_EXT);

		uriToPrefixMap.put(NS_HAWAII_EXT, NS_PREFIX_HAWAII_EXT);
		prefixToUriMap.put(NS_PREFIX_HAWAII_EXT, NS_HAWAII_EXT);

		prefixToUriMap.put(NS_PREFIX_CH_UPDATE, NS_CH_UPDATE);
		uriToPrefixMap.put(NS_CH_UPDATE, NS_PREFIX_CH_UPDATE);

		prefixToUriMap.put(NS_MAINE_CHARGE_CODES_PREFIX, NS_MAINE_CHARGE_CODES);
		uriToPrefixMap.put(NS_MAINE_CHARGE_CODES, NS_MAINE_CHARGE_CODES_PREFIX);

		prefixToUriMap.put(NS_PREFIX_TOPICS, NS_TOPICS);
		uriToPrefixMap.put(NS_TOPICS, NS_PREFIX_TOPICS);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_EXCHANGE, NS_ACCESS_CONTROL_EXCHANGE);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_EXCHANGE, NS_PREFIX_ACCESS_CONTROL_EXCHANGE);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_EXT, NS_ACCESS_CONTROL_EXT);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_EXT, NS_PREFIX_ACCESS_CONTROL_EXT);

		prefixToUriMap.put(NS_PREFIX_POLICY_DECISION_CONTEXT, NS_POLICY_DECISION_CONTEXT);
		uriToPrefixMap.put(NS_POLICY_DECISION_CONTEXT, NS_PREFIX_POLICY_DECISION_CONTEXT);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_ERROR_REPORTING, NS_ACCESS_CONTROL_ERROR_REPORTING);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_ERROR_REPORTING, NS_PREFIX_ACCESS_CONTROL_ERROR_REPORTING);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_RESULT_METADATA, NS_ACCESS_CONTROL_RESULT_METADATA);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_RESULT_METADATA, NS_PREFIX_ACCESS_CONTROL_RESULT_METADATA);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE, NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXCHANGE);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT, NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE, NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT, NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING, NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING);

		prefixToUriMap.put(NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA, NS_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA);
		uriToPrefixMap.put(NS_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA, NS_PREFIX_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_REQUEST_EXT, NS_ACCESS_CONTROL_REQUEST_EXT);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_REQUEST_EXT, NS_PREFIX_ACCESS_CONTROL_REQUEST_EXT);

		prefixToUriMap.put(NS_PREFIX_ACCESS_CONTROL_REQUEST, NS_ACCESS_CONTROL_REQUEST);
		uriToPrefixMap.put(NS_ACCESS_CONTROL_REQUEST, NS_PREFIX_ACCESS_CONTROL_REQUEST);

		prefixToUriMap.put(NS_PREFIX_PROXY_XSD_30, NS_PROXY_XSD_30);
		uriToPrefixMap.put(NS_PROXY_XSD_30, NS_PREFIX_PROXY_XSD_30);

		prefixToUriMap.put(NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE, NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE);
		uriToPrefixMap.put(NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE, NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE);

		prefixToUriMap.put(NS_PREFIX_ARREST_REPORTING_SERVICE, NS_ARREST_REPORTING_SERVICE);
		uriToPrefixMap.put(NS_ARREST_REPORTING_SERVICE, NS_PREFIX_ARREST_REPORTING_SERVICE);
		
	    //
	    
		prefixToUriMap.put(NS_PREFIX_ARU, NS_ARU);
	    uriToPrefixMap.put(NS_ARU, NS_PREFIX_ARU);
	    
		prefixToUriMap.put(NS_PREFIX_AR_EXT, NS_AR_EXT);
	    uriToPrefixMap.put(NS_AR_EXT, NS_PREFIX_AR_EXT);
	    
		prefixToUriMap.put(NS_PREFIX_INCIDENT_PERSON_SEARCH_REQUEST_EXT, NS_INCIDENT_PERSON_SEARCH_REQUEST_EXT);
	    uriToPrefixMap.put(NS_INCIDENT_PERSON_SEARCH_REQUEST_EXT, NS_PREFIX_INCIDENT_PERSON_SEARCH_REQUEST_EXT);
	    
		prefixToUriMap.put(NS_PREFIX_PAROLE_CASE_OFFICER_CHANGE, NS_PAROLE_CASE_OFFICER_CHANGE);
	    uriToPrefixMap.put(NS_PAROLE_CASE_OFFICER_CHANGE, NS_PREFIX_PAROLE_CASE_OFFICER_CHANGE);
	    
		prefixToUriMap.put(NS_PREFIX_PROBATION_CASE_OFFICER_CHANGE, NS_PROBATION_CASE_OFFICER_CHANGE);
	    uriToPrefixMap.put(NS_PROBATION_CASE_OFFICER_CHANGE, NS_PREFIX_PROBATION_CASE_OFFICER_CHANGE);
	    
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH, NS_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH);
	    uriToPrefixMap.put(NS_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH, NS_PREFIX_SUBSCRIPTION_VALIDATION_RESPONSE_EXCH);
	    
	    prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH, NS_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH);
	    uriToPrefixMap.put(NS_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH, NS_PREFIX_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH);
	    
		prefixToUriMap.put(NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST_EXT, NS_SUBSCRIPTION_SEARCH_REQUEST_EXT);
	    uriToPrefixMap.put(NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST_EXT);
	    
		prefixToUriMap.put(NS_PREFIX_VEHICLE_SEARCH_REQUEST, NS_VEHICLE_SEARCH_REQUEST);
	    uriToPrefixMap.put(NS_VEHICLE_SEARCH_REQUEST, NS_PREFIX_VEHICLE_SEARCH_REQUEST);
	    
		prefixToUriMap.put(NS_PREFIX_VERMONT_DISPOSITION_CODES, NS_VERMONT_DISPOSITION_CODES);
	    uriToPrefixMap.put(NS_VERMONT_DISPOSITION_CODES, NS_PREFIX_VERMONT_DISPOSITION_CODES);

		prefixToUriMap.put(NS_PREFIX_OJBC_DISPOSITION_CODES, NS_OJBC_DISPOSITION_CODES);
	    uriToPrefixMap.put(NS_OJBC_DISPOSITION_CODES, NS_PREFIX_OJBC_DISPOSITION_CODES);

		prefixToUriMap.put(NS_PREFIX_HAWAII_JIMS_DISPOSITION_CODES, NS_HAWAII_JIMS_DISPOSITION_CODES);
	    uriToPrefixMap.put(NS_HAWAII_JIMS_DISPOSITION_CODES, NS_PREFIX_HAWAII_JIMS_DISPOSITION_CODES);
	    
		prefixToUriMap.put(NS_PREFIX_HAWAII_HAJIS_DISPOSITION_CODES, NS_HAWAII_HAJIS_DISPOSITION_CODES);
	    uriToPrefixMap.put(NS_HAWAII_HAJIS_DISPOSITION_CODES, NS_PREFIX_HAWAII_HAJIS_DISPOSITION_CODES);
	    
	    //
	    
		prefixToUriMap.put(NS_PREFIX_AR_HAWAII_EXT, NS_AR_HAWAII_EXT);
	    uriToPrefixMap.put(NS_AR_HAWAII_EXT, NS_PREFIX_AR_HAWAII_EXT);
	    
		prefixToUriMap.put(NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT, NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT);
	    uriToPrefixMap.put(NS_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT, NS_PREFIX_CRIMINAL_HISTORY_UPDATE_REPORTING_SERVICE_EXT);

		prefixToUriMap.put(NS_PREFIX_HAWAII_BOOKING_REPORT_EXT, NS_HAWAII_BOOKING_REPORT_EXT);
	    uriToPrefixMap.put(NS_HAWAII_BOOKING_REPORT_EXT, NS_PREFIX_HAWAII_BOOKING_REPORT_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_CRIMINAL_RECORD, NS_CRIMINAL_RECORD);
	    uriToPrefixMap.put(NS_CRIMINAL_RECORD, NS_PREFIX_CRIMINAL_RECORD);
	    
	    prefixToUriMap.put(NS_PREFIX_CRIMINAL_RECORD_EXT, NS_CRIMINAL_RECORD_EXT);
	    uriToPrefixMap.put(NS_CRIMINAL_RECORD_EXT, NS_PREFIX_CRIMINAL_RECORD_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_REQUEST, NS_PERSON_IDENTIFICATION_REQUEST);
	    uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_REQUEST, NS_PREFIX_PERSON_IDENTIFICATION_REQUEST);
	    
	    prefixToUriMap.put(NS_PREFIX_PERSON_IDENTIFICATION_RESULTS, NS_PERSON_IDENTIFICATION_RESULTS);
	    uriToPrefixMap.put(NS_PERSON_IDENTIFICATION_RESULTS, NS_PREFIX_PERSON_IDENTIFICATION_RESULTS);
	    
	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_EXT, NS_IDENTIFICATION_EXT);
	    uriToPrefixMap.put(NS_IDENTIFICATION_EXT, NS_PREFIX_IDENTIFICATION_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_ARREST_QUERY_REQUEST, NS_ARREST_QUERY_REQUEST);
	    uriToPrefixMap.put(NS_ARREST_QUERY_REQUEST, NS_PREFIX_ARREST_QUERY_REQUEST);
	    
	    prefixToUriMap.put(NS_PREFIX_ARREST_QUERY_REQUEST_EXT, NS_ARREST_QUERY_REQUEST_EXT);
	    uriToPrefixMap.put(NS_ARREST_QUERY_REQUEST_EXT, NS_PREFIX_ARREST_QUERY_REQUEST_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_XMIME, NS_XMIME);
	    uriToPrefixMap.put(NS_XMIME, NS_PREFIX_XMIME);
	    
	    prefixToUriMap.put(NS_PREFIX_XOP, NS_XOP);
	    uriToPrefixMap.put(NS_XOP, NS_PREFIX_XOP);
	    
	    prefixToUriMap.put(NS_PREFIX_WSOMA, NS_WSOMA);
	    uriToPrefixMap.put(NS_WSOMA, NS_PREFIX_WSOMA);
	    
	    //
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST, NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST);
	   
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT, NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_REQUEST_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
	   
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);

	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE, NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE);
	    uriToPrefixMap.put(NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE, NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE);

	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT, NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT);
	    uriToPrefixMap.put(NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT, NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING, NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING);
	    uriToPrefixMap.put(NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING, NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING);

	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_METADATA, NS_IDENTIFICATION_RESULTS_MODIFICATION_METADATA);
	    uriToPrefixMap.put(NS_IDENTIFICATION_RESULTS_MODIFICATION_METADATA, NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_METADATA);
	    
	    prefixToUriMap.put(NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE, NS_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE);
	    uriToPrefixMap.put(NS_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE, NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXCHANGE);
	    
	    prefixToUriMap.put(NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXT, NS_PRETRIAL_SERVICE_ENROLLMENT_EXT);
	    uriToPrefixMap.put(NS_PRETRIAL_SERVICE_ENROLLMENT_EXT, NS_PREFIX_PRETRIAL_SERVICE_ENROLLMENT_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT, NS_COURT_DISPOSITION_RECORDING_REPORT);
	    uriToPrefixMap.put(NS_COURT_DISPOSITION_RECORDING_REPORT, NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT);
	    
	    prefixToUriMap.put(NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT_EXT, NS_COURT_DISPOSITION_RECORDING_REPORT_EXT);
	    uriToPrefixMap.put(NS_COURT_DISPOSITION_RECORDING_REPORT_EXT, NS_PREFIX_COURT_DISPOSITION_RECORDING_REPORT_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT, NS_PROSECUTION_DECISION_RECORDING_REPORT);
	    uriToPrefixMap.put(NS_PROSECUTION_DECISION_RECORDING_REPORT, NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT);
	    
	    prefixToUriMap.put(NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT_EXT, NS_PROSECUTION_DECISION_RECORDING_REPORT_EXT);
	    uriToPrefixMap.put(NS_PROSECUTION_DECISION_RECORDING_REPORT_EXT, NS_PREFIX_PROSECUTION_DECISION_RECORDING_REPORT_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT, NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT);
	    uriToPrefixMap.put(NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT, NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT);
	    
	    prefixToUriMap.put(NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT, NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT);
	    uriToPrefixMap.put(NS_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT, NS_PREFIX_FEDERAL_SUBSCRIPTION_CREATION_REPORT_EXT);
	      
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST, NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_REQUEST);
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST, NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST, NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_REQUEST);
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS, NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS);
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS, NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS, NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS);
	    
	    prefixToUriMap.put(NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
	    uriToPrefixMap.put(NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);

	    prefixToUriMap.put(NS_PREFIX_INTEL_30, NS_INTEL_30);
	    uriToPrefixMap.put(NS_INTEL_30, NS_PREFIX_INTEL_30);
	      
	    prefixToUriMap.put(NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT, NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT);
	    uriToPrefixMap.put(NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT, NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT);
	    
	    prefixToUriMap.put(NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT, NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT);
	    uriToPrefixMap.put(NS_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT, NS_PREFIX_FEDERAL_SUBSCRIPTION_UPDATE_REPORT_EXT);
	    
	    prefixToUriMap.put(NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST, NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST);
	    uriToPrefixMap.put(NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST, NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST);
	}

	@Override
	public String getNamespaceURI(String prefix) {
		return prefixToUriMap.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return uriToPrefixMap.get(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(String arg0) {
		return null;
	}

	/**
	 * This method collects all of the namespaces that are actually used, in this element and all of its descendants, and declares the proper prefixes
	 * on this element.
	 * 
	 * @param e
	 *            the element on which to declare the namespace prefixes for itself and its descendants
	 */
	public void populateRootNamespaceDeclarations(Element e) {
		Set<String> namespaceURIs = collectNamespaceURIs(e);
		for (String uri : namespaceURIs) {
			String prefix = getPrefix(uri);
			if (prefix == null) {
				if (!"http://www.w3.org/2000/xmlns/".equals(uri)) {
					log.warn("Namespace URI " + uri + " not found in OjbcNamespaceContext");
				}
			} else {
				e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, uri);
			}
		}
	}

	private Set<String> collectNamespaceURIs(Node e) {
		Set<String> ret = new HashSet<String>();
		String uri = e.getNamespaceURI();
		if (uri != null) {
			ret.add(uri);
		}
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				ret.addAll(collectNamespaceURIs(child));
			}
		}
		NamedNodeMap attributeMap = e.getAttributes();
		for (int i = 0; i < attributeMap.getLength(); i++) {
			Node attr = attributeMap.item(i);
			uri = attr.getNamespaceURI();
			if (uri != null) {
				ret.add(uri);
			}
		}
		return ret;
	}

}
