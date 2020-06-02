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
package org.ojbc.web.portal.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.web.portal.controllers.config.PeopleControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@Profile({"audit-search","standalone"})
@SessionAttributes({ "userAuthenticationSearchRequest", "userAuthenticationSearchResponses", "auditSearchRequest"})
@RequestMapping("/auditLogs")
public class AuditLogsController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	PeopleControllerConfigInterface peopleQueryConfig;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
    @Value("${auditSearchDateRange:30}")
    Integer auditSearchDateRange;
    
    @Value("#{propertySplitter.map('${auditQuerySourceSystemMap}', '^')}")
    Map<String, String> auditQuerySourceSystemMap;

    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	if (!model.containsAttribute("userAuthenticationSearchRequest")) {
	    	UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
	    	model.addAttribute("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
    	}
    	
    	Map<String, String> userActionMap = new HashMap<>(); 
    	userActionMap.put("", "User Action"); 
    	userActionMap.put("login", "Login"); 
    	userActionMap.put("logout", "Logout"); 
    	model.addAttribute("userActionMap", userActionMap); 
    	log.info("auditQuerySourceSystemMap: " + auditQuerySourceSystemMap);
	}

	private UserAuthenticationSearchRequest initUserAuthenticationSearchRequest() {
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
    	userAuthenticationSearchRequest.setEndTime(LocalDateTime.now());
    	userAuthenticationSearchRequest.setStartTime(LocalDateTime.now().minusDays(auditSearchDateRange));
		return userAuthenticationSearchRequest;
	}
    
	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm) {
			UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
			model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		} 

		return "auditLogs/_searchForm";
	}
	
	@RequestMapping(value="/userLoginSearch", method=RequestMethod.POST)
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute UserAuthenticationSearchRequest userAuthenticationSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "auditLogs/_searchForm";
		}

		log.info("userAuthenticationSearchRequest:" + userAuthenticationSearchRequest );
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = restEnhancedAuditClient.retrieveUserAuthentications(userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchResponses", userAuthenticationSearchResponses); 
		return "auditLogs/_userAuthenticationSearchResults";
	}

	@RequestMapping(value="/userAuthenticationSearchResults")
	public String getUserAuthenticationSearchResults(HttpServletRequest request,  
			Map<String, Object> model) throws Throwable {
		return "auditLogs/_userAuthenticationSearchResults";
	}
	
	@RequestMapping("/userActivities" )
	public String userActivitiesSearch(HttpServletRequest request, @RequestParam Integer userInfoId, 
			Map<String, Object> model) throws Throwable {
		
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = (UserAuthenticationSearchRequest) model.get("userAuthenticationSearchRequest");
		@SuppressWarnings("unchecked")
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = (List<UserAuthenticationSearchResponse>) model.get("userAuthenticationSearchResponses");

		UserAuthenticationSearchResponse userAuthenticationSearchResponse = userAuthenticationSearchResponses
				.stream().filter(i-> Objects.equals(i.getUserInfoId(), userInfoId)).findFirst().orElse(null);
		model.put("userAuthenticationSearchResponse", userAuthenticationSearchResponse);
		
		AuditSearchRequest auditSearchRequest = new AuditSearchRequest(); 
		auditSearchRequest.setEndTime(userAuthenticationSearchRequest.getEndTime());
		auditSearchRequest.setStartTime(userAuthenticationSearchRequest.getStartTime());
		auditSearchRequest.setUserInfoId(userInfoId);
		
		model.put("auditSearchRequest", auditSearchRequest);
		List<PersonSearchRequest> personSearchRequests = restEnhancedAuditClient.retrievePersonSearchRequest(auditSearchRequest);
		model.put("personSearchRequests", personSearchRequests); 
		return "auditLogs/_userAcitivities";
	}
	
	@RequestMapping("/incidentSearchRequests" )
	public String getIncidentSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getIncidentSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<IncidentSearchRequest> incidentSearchRequests = restEnhancedAuditClient.retrieveIncidentSearchRequest(auditSearchRequest);
		model.put("incidentSearchRequests", incidentSearchRequests); 
		
		return "auditLogs/_userIncidentSearchRequests";
	}
	
	@RequestMapping("/personSearchRequests" )
	public String getPersonSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getIncidentSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<PersonSearchRequest> personSearchRequests = restEnhancedAuditClient.retrievePersonSearchRequest(auditSearchRequest);
		model.put("personSearchRequests", personSearchRequests); 
		
		return "auditLogs/_userPersonSearchRequests";
	}
	
	@RequestMapping("/queryRequests" )
	public String getQueryRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getQueryRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<QueryRequest> queryRequests = restEnhancedAuditClient.retrieveQueryRequest(auditSearchRequest);
		model.put("queryRequests", queryRequests); 
		
		return "auditLogs/_queryRequests";
	}
	
	@RequestMapping(value="/personQueryResponse", method=RequestMethod.POST )
	public String getPersonQueryResponse(HttpServletRequest request, 
			@RequestParam("queryRequestId") Integer queryRequestId, 
			@RequestParam("identificationSourceText") String identificationSourceText,
			Map<String, Object> model) throws Throwable {
		log.info("in getQueryRequests");
		
		String system = auditQuerySourceSystemMap.get(identificationSourceText); 
		switch (system) {
		case "incident": 
			throw new NotImplementedException();
		case "criminalHistory": 
			PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = restEnhancedAuditClient.retrieveCriminalHistoryQueryDetail(queryRequestId); 
			model.put("personQueryCriminalHistoryResponse", personQueryCriminalHistoryResponse); 
			log.info("personQueryCriminalHistoryResponse: "+ personQueryCriminalHistoryResponse);
			return "auditLogs/_personQueryCriminalHistoryResponse";
		case "vehicleCrash": 
			VehicleCrashQueryResponse vehicleCrashQueryResponse = restEnhancedAuditClient.retrieveVehicleCrashQueryResultsDetail(queryRequestId); 
			model.put("vehicleCrashQueryResponse", vehicleCrashQueryResponse); 
			log.info("vehicleCrashQueryResponse: "+ vehicleCrashQueryResponse);
			return "auditLogs/_vehicleCrashQueryResponse";
		default:
			throw new IllegalArgumentException("Invalid identification Source Text"); 
		}
		
	}
	
}

