
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

package org.ojbc.web.portal.rest.client;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.FirearmSearchResult;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IncidentReportQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchResult;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.ProfessionalLicensingQueryResponse;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchResult;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditPersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestEnhancedAuditClient {

	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired(required=false)
	private RestTemplate restTemplate;
	
	@Value("${enhancedAuditServerBaseUrl:https://localhost:8443/OJB/}")
	private String restServiceBaseUrl;

	public void auditPrintResults(PrintResults printResults) {
//		this.webClient.post().uri("/auditServer/audit/printResults")
//			.body(BodyInserters.fromValue(printResults));
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/printResults", printResults, PrintResults.class);
	}

	public void auditUserAcknowledgement(UserAcknowledgement userAcknowledgement) {
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userAcknowledgement", 
				userAcknowledgement, UserAcknowledgement.class);
//		this.webClient.post().uri("auditServer/audit/userAcknowledgement")
//				.body(BodyInserters.fromValue(userAcknowledgement))
//				.retrieve();
	}
	
	public void auditUserLogin(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setUserEmailAddress(emailAddress);
		userInfo.setEmployerName(employerName);
		userInfo.setEmployerSubunitName(employerSubunitName);
		userInfo.setFederationId(federationId);
		userInfo.setUserFirstName(firstName);
		userInfo.setIdentityProviderId(identityProviderId);
		userInfo.setUserLastName(lastName);
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogin", userInfo, UserInfo.class);
		
	}
	
	public void auditUserLogout(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setUserEmailAddress(emailAddress);
		userInfo.setEmployerName(employerName);
		userInfo.setEmployerSubunitName(employerSubunitName);
		userInfo.setFederationId(federationId);
		userInfo.setUserFirstName(firstName);
		userInfo.setIdentityProviderId(identityProviderId);
		userInfo.setUserLastName(lastName);
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogout", userInfo, UserInfo.class);
		
	}

	public List<UserAuthenticationSearchResponse> retrieveUserAuthentications(UserAuthenticationSearchRequest authenticationSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserAuthenticationSearchRequest> entity = new HttpEntity<UserAuthenticationSearchRequest>(authenticationSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveUserAuthentications";

		ResponseEntity<List<UserAuthenticationSearchResponse>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<UserAuthenticationSearchResponse>>() {});
		
		return response.getBody();
	}
	
	public List<PersonSearchRequest> retrievePersonSearchRequest(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditSearchRequest> entity = new HttpEntity<AuditSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrievePersonSearchRequest";
		
		ResponseEntity<List<PersonSearchRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<PersonSearchRequest>>() {});
		
		return response.getBody();
	}
	
	public List<QueryRequest> retrieveQueryRequest(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditSearchRequest> entity = new HttpEntity<AuditSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveQueryRequest";
		
		ResponseEntity<List<QueryRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<QueryRequest>>() {});
		
		return response.getBody();
	}
	
	public List<IncidentSearchRequest> retrieveIncidentSearchRequest(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditSearchRequest> entity = new HttpEntity<AuditSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveIncidentSearchRequest";
		
		ResponseEntity<List<IncidentSearchRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<IncidentSearchRequest>>() {});
		
		return response.getBody();
	}
	
	public List<FirearmsSearchRequest> retrieveFirearmSearchRequest(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditSearchRequest> entity = new HttpEntity<AuditSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveFirearmSearchRequest";
		
		ResponseEntity<List<FirearmsSearchRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<FirearmsSearchRequest>>() {});
		
		return response.getBody();
	}
	
	public List<VehicleSearchRequest> retrieveVehicleSearchRequest(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditSearchRequest> entity = new HttpEntity<AuditSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveVehicleSearchRequest";
		
		ResponseEntity<List<VehicleSearchRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<VehicleSearchRequest>>() {});
		
		return response.getBody();
	}
	
	
   public PersonQueryCriminalHistoryResponse retrieveCriminalHistoryQueryDetail(Integer queryRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveCriminalHistoryQueryDetail";
		
		ResponseEntity<PersonQueryCriminalHistoryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<PersonQueryCriminalHistoryResponse>() {});
		
		return response.getBody();
   }

   public FirearmsQueryResponse retrieveFirearmQueryDetail(Integer queryRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveFirearmQueryDetail";
		
		ResponseEntity<FirearmsQueryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<FirearmsQueryResponse>() {});
		
		return response.getBody();
   }

   public PersonQueryWarrantResponse retrieveWarrantQueryDetail(Integer queryRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveWarrantQueryDetail";
		
		ResponseEntity<PersonQueryWarrantResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<PersonQueryWarrantResponse>() {});
		
		return response.getBody();
   }
	   
   public VehicleCrashQueryResponse retrieveVehicleCrashQueryResultsDetail(Integer queryRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveVehicleCrashQueryResultsDetail";
		
		ResponseEntity<VehicleCrashQueryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<VehicleCrashQueryResponse>() {});
		
		return response.getBody();
   }
	   
   public ProfessionalLicensingQueryResponse retrieveProfessionalLicensingQueryDetail(Integer queryRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveProfessionalLicensingQueryDetail";
	   
	   ResponseEntity<ProfessionalLicensingQueryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<ProfessionalLicensingQueryResponse>() {});
	   
	   return response.getBody();
   }
   
   public WildlifeQueryResponse retrieveWildlifeQueryDetail(Integer queryRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveWildlifeQueryDetail";
	   
	   ResponseEntity<WildlifeQueryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<WildlifeQueryResponse>() {});
	   
	   return response.getBody();
   }
   
   public IncidentReportQueryResponse retrieveIncidentReportQueryDetail(Integer queryRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(queryRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveIncidentReportQueryDetail";
	   
	   ResponseEntity<IncidentReportQueryResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<IncidentReportQueryResponse>() {});
	   
	   return response.getBody();
   }

   public List<PrintResults> retrieveUserPrintRequests(AuditSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<Integer>(auditSearchRequest.getUserInfoId(), headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveUserPrintRequests";
		
		ResponseEntity<List<PrintResults>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<PrintResults>>() {});
		
		return response.getBody();
	}

   public List<PersonSearchResult> retrievePersonSearchResults(Integer personSearchRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(personSearchRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrievePersonSearchResults";
	   
	   ResponseEntity<List<PersonSearchResult>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<PersonSearchResult>>() {});
	   
	   return response.getBody();
   }
   
   public List<FirearmSearchResult> retrieveFirearmSearchResults(Integer firearmSearchRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(firearmSearchRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveFirearmSearchResults";
	   
	   ResponseEntity<List<FirearmSearchResult>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<FirearmSearchResult>>() {});
	   
	   return response.getBody();
   }
   
   public List<VehicleSearchResult> retrieveVehicleSearchResults(Integer vehicleSearchRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(vehicleSearchRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveVehicleSearchResults";
	   
	   ResponseEntity<List<VehicleSearchResult>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<VehicleSearchResult>>() {});
	   
	   return response.getBody();
   }
   
   public List<IncidentSearchResult> retrieveIncidentSearchResults(Integer incidentSearchRequestId) {
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<Integer> entity = new HttpEntity<Integer>(incidentSearchRequestId, headers);
	   
	   String uri = restServiceBaseUrl + "auditServer/audit/retrieveIncidentSearchResults";
	   
	   ResponseEntity<List<IncidentSearchResult>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<IncidentSearchResult>>() {});
	   
	   return response.getBody();
   }

	public List<PersonSearchRequest> retrievePersonSearchRequestByPerson(AuditPersonSearchRequest auditSearchRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AuditPersonSearchRequest> entity = new HttpEntity<AuditPersonSearchRequest>(auditSearchRequest, headers);
		
		String uri = restServiceBaseUrl + "auditServer/audit/retrievePersonSearchRequestByPerson";
		
		ResponseEntity<List<PersonSearchRequest>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<PersonSearchRequest>>() {});
		
		return response.getBody();
	}
	
	public Log getLog() {
		return log;
	}
}
