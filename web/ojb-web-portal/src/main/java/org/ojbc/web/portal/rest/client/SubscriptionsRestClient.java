
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
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscriptionDetail;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
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
public class SubscriptionsRestClient {

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired(required=false)
	private RestTemplate restTemplate;
	
	@Value("${enhancedAuditServerBaseUrl:https://localhost:8443/OJB/}")
	private String restServiceBaseUrl;

	public List<Subscription> getExpiringSubscriptions(ExpiringSubscriptionRequest expiringSubscriptionRequest){
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveExpiringSubscriptions";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ExpiringSubscriptionRequest> entity = new HttpEntity<ExpiringSubscriptionRequest>(expiringSubscriptionRequest, headers);

		ResponseEntity<List<Subscription>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Subscription>>() {});
		
		return response.getBody();
	}
	
	public List<Subscription> getExpiredSubscriptions(ExpiringSubscriptionRequest expiringSubscriptionRequest){
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveExpiredSubscriptions";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ExpiringSubscriptionRequest> entity = new HttpEntity<ExpiringSubscriptionRequest>(expiringSubscriptionRequest, headers);
		
		ResponseEntity<List<Subscription>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Subscription>>() {});
		
		return response.getBody();
	}
	
	public List<AgencyProfile> getAllAgencies(){
		String uri = restServiceBaseUrl + "auditServer/audit/agencies";
		
		ResponseEntity<List<AgencyProfile>> response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<AgencyProfile>>() {});
		
		return response.getBody();
	}
	
	public List<FederalRapbackSubscription> getFederalRapbackSubscriptionErrors(){
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveFederalRapbackSubscriptionErrors";
		
		ResponseEntity<List<FederalRapbackSubscription>> response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<FederalRapbackSubscription>>() {});
		
		return response.getBody();
	}
	
	public List<FederalRapbackSubscription> getRapbackNotifications(QueryRequestByDateRange queryRequestByDateRange){
		String uri = restServiceBaseUrl + "auditServer/audit/retrieveRapbackNotifications";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<QueryRequestByDateRange> entity = new HttpEntity<QueryRequestByDateRange>(queryRequestByDateRange, headers);

		ResponseEntity<List<FederalRapbackSubscription>> response = restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<FederalRapbackSubscription>>() {});
		
		return response.getBody();
	}
	
	public FederalRapbackSubscriptionDetail getFederalRapbackSubscriptionDetail(String subscriptionId){
		
		String uri = restServiceBaseUrl + "auditServer/audit/subscriptions/" + subscriptionId + "federalRapbackSubscriptionsDetail";
		FederalRapbackSubscriptionDetail federalRapbackSubscriptionDetail = restTemplate.getForObject(uri, FederalRapbackSubscriptionDetail.class);
		return federalRapbackSubscriptionDetail;

	}
	
}
