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
package org.ojbc.web;

public class OJBCWebServiceURIs {
	
	public static final String CRIMINAL_HISTORY_SEARCH = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History";
	public static final String CRIMINAL_HISTORY = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History";
	public static final String WARRANTS = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants";
	public static final String FIREARMS = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequest";	
	public static final String FIREARMS_QUERY_REQUEST_BY_FIREARM = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm";
	public static final String FIREARMS_QUERY_REQUEST_BY_PERSON = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson";	
	public static final String INCIDENT_REPORT = "{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest";
	public static final String PERSON_TO_INCIDENT ="{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentPersonSearchRequest";
	public static final String VEHICLE_TO_INCIDENT = "{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentVehicleSearchRequest";
	public static final String JUVENILE_HISTORY = "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory";
	public static final String JUVENILE_HISTORY_SEARCH = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-JuvenileHistory";
	public static final String COURT_CASE = "{http://ojbc.org/Services/WSDL/CourtCaseSearchRequestService/1.0}SubmitCourtCaseSearchRequest";
	public static final String JAIL = "{http://ojbc.org/Services/WSDL/CustodySearchRequestService/1.0}SubmitCustodySearchRequest";
	public static final String COURT_CASE_DETAIL = "{http://ojbc.org/Services/WSDL/Court_Case_Query_Request_Service/1.0}/SubmitCourtCaseQueryRequest";
	public static final String JAIL_DETAIL = "{http://ojbc.org/Services/WSDL/Custody_Query_Request_Service/1.0}SubmitCustodyQueryRequest";
	public static final String VEHICLE_CRASH = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Vehicle_Crash/1.0}Person-Query-Service---Vehicle-Crash";
	
}