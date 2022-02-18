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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchResult;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditPersonSearchRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@Profile({"audit-search","standalone"})
@SessionAttributes({ "userAuthenticationSearchRequest", "userAuthenticationSearchResponses", "auditSearchRequest", 
	"clickableQueryRequestListMap", "nonClickableQueryRequestListMap", "queryRequestAccordionHeaderMap", "auditPersonSearchRequest",
	"searchRequestAccordionHeaderMap", "enabledSearchProfiles"})
@RequestMapping("/auditLogs")
public class AuditLogsController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	PeopleControllerConfigInterface peopleQueryConfig;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
	@Resource
	Map<String, String> searchProfilesEnabled;
	
    @Value("${auditSearchDateRange:30}")
    Integer auditSearchDateRange;
    
    @Value("#{propertySplitter.map('${auditQuerySourceSystemMap}', '^')}")
    Map<String, String> auditQuerySourceSystemMap;
    
    @Value("#{propertySplitter.map('${nonClickableSourceSystemMap}', '^')}")
    Map<String, String> nonClickableSourceSystemMap;
    
    Map<String, List<QueryRequest>> clickableQueryRequestListMap;
    Map<String, List<QueryRequest>> nonClickableQueryRequestListMap;
    Map<String, String> queryRequestAccordionHeaderMap;
    Map<String, String> searchRequestAccordionHeaderMap;
    List<String> enabledSearchProfiles;

    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	if (!model.containsAttribute("userAuthenticationSearchRequest")) {
	    	UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
	    	model.addAttribute("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
    	}
    	
    	if (!model.containsAttribute("auditPersonSearchRequest")) {
    		AuditPersonSearchRequest auditPersonSearchRequest = initAuditPersonSearchRequest();
    		model.addAttribute("auditPersonSearchRequest", auditPersonSearchRequest);
    	}
    	
    	Map<String, String> userActionMap = new HashMap<>(); 
    	userActionMap.put("", "User Action"); 
    	userActionMap.put("login", "Login"); 
    	userActionMap.put("logout", "Logout"); 
    	model.addAttribute("userActionMap", userActionMap);
    	
    	
    	if (!model.containsAttribute("queryRequestAccordionHeaderMap")) {
    		queryRequestAccordionHeaderMap = new HashMap<String, String>(); 
    		queryRequestAccordionHeaderMap.put("incident", "INCIDENT"); 
    		queryRequestAccordionHeaderMap.put("criminalHistory", "CRIMINAL HISTORY"); 
    		queryRequestAccordionHeaderMap.put("vehicleCrash", "VEHICLE CRASH"); 
    		queryRequestAccordionHeaderMap.put("professionalLicense", "PROFESSIONAL LICENSE"); 
    		queryRequestAccordionHeaderMap.put("wildlifeLicense", "WILDLIFE LICENSE"); 
    		queryRequestAccordionHeaderMap.put("personToincidentQuery", "PERSON TO INCIDENT"); 
    		queryRequestAccordionHeaderMap.put("personToVehicleQuery", "PERSON TO VEHICLE"); 
    		queryRequestAccordionHeaderMap.put("custody", "CUSTODY"); 
    		queryRequestAccordionHeaderMap.put("warrant", "WARRANT"); 
    		queryRequestAccordionHeaderMap.put("firearm", "FIREARM"); 
    		model.addAttribute("queryRequestAccordionHeaderMap", queryRequestAccordionHeaderMap);
    	}
    	
    	if (!model.containsAttribute("searchRequestAccordionHeaderMap")) {
    		searchRequestAccordionHeaderMap = new HashMap<String, String>(); 
    		searchRequestAccordionHeaderMap.put("people", "PERSON SEARCH REQUEST"); 
    		searchRequestAccordionHeaderMap.put("incident", "INCIDENT SEARCH REQUEST"); 
    		searchRequestAccordionHeaderMap.put("firearm", "FIREARM SEARCH REQUEST"); 
    		searchRequestAccordionHeaderMap.put("vehicle", "VEHICLE SEARCH REQUEST"); 
    		model.addAttribute("searchRequestAccordionHeaderMap", searchRequestAccordionHeaderMap);
    	}
    	
    	if (!model.containsAttribute("enabledSearchProfiles")) {
    		enabledSearchProfiles = searchProfilesEnabled.entrySet()
    				.stream()
    				.filter(entry->entry.getValue().equals("enabled"))
    				.map(entry->entry.getKey())
    				.collect(Collectors.toList());
    		model.addAttribute("enabledSearchProfiles", enabledSearchProfiles);
    	}
    	
	}

	private UserAuthenticationSearchRequest initUserAuthenticationSearchRequest() {
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
    	userAuthenticationSearchRequest.setEndTime(LocalDateTime.now());
    	userAuthenticationSearchRequest.setStartTime(LocalDateTime.now().minusDays(auditSearchDateRange));
		return userAuthenticationSearchRequest;
	}
    
	private AuditPersonSearchRequest initAuditPersonSearchRequest() {
		AuditPersonSearchRequest auditPersonSearchRequest = new AuditPersonSearchRequest();
		auditPersonSearchRequest.setEndTime(LocalDateTime.now());
		auditPersonSearchRequest.setStartTime(LocalDateTime.now().minusDays(auditSearchDateRange));
		return auditPersonSearchRequest;
	}
	
	@GetMapping("searchForm")
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm) {
			UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
			model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		} 

		return "auditLogs/searchForm::searchFormContent";
	}
	
	@RequestMapping(value = "personSearchLogsSearchForm", method = RequestMethod.GET)
	public String personSearchLogsSearchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
			Map<String, Object> model) {
		
		if (resetForm) {
    		AuditPersonSearchRequest auditPersonSearchRequest = initAuditPersonSearchRequest();
    		model.put("auditPersonSearchRequest", auditPersonSearchRequest);
		} 
		
		return "auditLogs/personSearchForm::personSearchFormContent";
	}
	
	@RequestMapping(value="/userLoginSearch", method=RequestMethod.POST)
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute UserAuthenticationSearchRequest userAuthenticationSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "auditLogs/searchForm::searchFormContent";
		}

		log.info("userAuthenticationSearchRequest:" + userAuthenticationSearchRequest );
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = restEnhancedAuditClient.retrieveUserAuthentications(userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchResponses", userAuthenticationSearchResponses); 
		return "auditLogs/userAuthenticationSearchResults::userAuthenticationSearchResultsContent";
	}

	@RequestMapping(value="/personSearchLogs", method=RequestMethod.POST)
	public String getPersonSearchRequestLogs(HttpServletRequest request, @Valid @ModelAttribute AuditPersonSearchRequest auditPersonSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "auditLogs/personSearchForm::personSearchFormContent";
		}
		
		log.info("auditPersonSearchRequest:" + auditPersonSearchRequest );
		List<PersonSearchRequest> personSearchRequests = restEnhancedAuditClient.retrievePersonSearchRequestByPerson(auditPersonSearchRequest);;
		log.info("personSearchRequests:" + personSearchRequests );
		model.put("auditPersonSearchRequest", auditPersonSearchRequest);
		model.put("personSearchRequests", personSearchRequests); 
		return "auditLogs/userPersonSearchActivities::userPersonSearchActivitiesContent";
	}
	
	@RequestMapping(value="/userAuthenticationSearchResults")
	public String getUserAuthenticationSearchResults(HttpServletRequest request,  
			Map<String, Object> model) throws Throwable {
		return "auditLogs/userAuthenticationSearchResults::userAuthenticationSearchResultsContent";
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
		return "auditLogs/userAcitivities::userAcitivitiesContent";
	}
	
	@RequestMapping("/incident" )
	public String getIncidentSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getIncidentSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<IncidentSearchRequest> incidentSearchRequests = restEnhancedAuditClient.retrieveIncidentSearchRequest(auditSearchRequest);
		model.put("incidentSearchRequests", incidentSearchRequests); 
		
		return "auditLogs/userIncidentSearchRequests::userIncidentSearchRequestsContent";
	}
	
	@RequestMapping("/people" )
	public String getPersonSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getIncidentSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<PersonSearchRequest> personSearchRequests = restEnhancedAuditClient.retrievePersonSearchRequest(auditSearchRequest);
		model.put("personSearchRequests", personSearchRequests); 
		
		return "auditLogs/userPersonSearchRequests::userPersonSearchRequestsContent";
	}
	
	@RequestMapping("/firearm" )
	public String getFirearmSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in firearmSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<FirearmsSearchRequest> firearmSearchRequests = restEnhancedAuditClient.retrieveFirearmSearchRequest(auditSearchRequest);
		model.put("firearmSearchRequests", firearmSearchRequests); 
		
		return "auditLogs/userFirearmSearchRequests::userFirearmSearchRequestsContent";
	}
	
	@RequestMapping("/vehicle" )
	public String getVehicleSearchRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in vehicleSearchRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<VehicleSearchRequest> vehicleSearchRequests = restEnhancedAuditClient.retrieveVehicleSearchRequest(auditSearchRequest);
		model.put("vehicleSearchRequests", vehicleSearchRequests); 
		
		return "auditLogs/userVehicleSearchRequests::userVehicleSearchRequestsContent";
	}
	
	@RequestMapping("/queryRequests" )
	public String getQueryRequests(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getQueryRequests");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<QueryRequest> queryRequests = restEnhancedAuditClient.retrieveQueryRequest(auditSearchRequest);

		List<String> sourceTexts = queryRequests.stream()
				.map(QueryRequest::getIdentificationSourceText)
				.filter(Objects::nonNull)
				.map(StringUtils::trimToEmpty)
				.distinct()
				.collect(Collectors.toList()); 
		clickableQueryRequestListMap = new HashMap<>();
		nonClickableQueryRequestListMap = new HashMap<>(); 
		
		log.info("sourceTexts: " + sourceTexts);
		sourceTexts.forEach(this::populateQueryListMaps);
		
		queryRequests.forEach(this::pouplateQueryListMap);
		
		model.put("queryRequests", queryRequests); 
		model.put("clickableQueryRequestListMap", clickableQueryRequestListMap);
		model.put("nonClickableQueryRequestListMap", nonClickableQueryRequestListMap);
		
		return "auditLogs/queryRequests::queryRequestsContent";
	}

	@RequestMapping("/printResults" )
	public String getPrintResults(HttpServletRequest request, 
			Map<String, Object> model) throws Throwable {
		log.info("in getPrintResults");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");		
		List<PrintResults> printResults = restEnhancedAuditClient.retrieveUserPrintRequests(auditSearchRequest);
		
		model.put("printResults", printResults); 
		
		return "auditLogs/printResults::printResultsContent";
	}
	
	@RequestMapping("/personSearchResults" )
	public String getPersonSearchResults(HttpServletRequest request, 
			@RequestParam("personSearchRequestId") Integer personSearchRequestId, 
			Map<String, Object> model) throws Throwable {
		log.info("in getPersonSearchResults");
		List<PersonSearchResult> personSearchResults = restEnhancedAuditClient.retrievePersonSearchResults(personSearchRequestId);
		
		model.put("personSearchResults", personSearchResults); 
		
		return "auditLogs/personSearchResults::personSearchResultsContent";
	}
	
	@RequestMapping("/firearmSearchResults" )
	public String getFirearmSearchResults(HttpServletRequest request, 
			@RequestParam("firearmSearchRequestId") Integer firearmSearchRequestId, 
			Map<String, Object> model) throws Throwable {
		log.info("in getFirearmSearchResults");
		List<FirearmSearchResult> firearmSearchResults = restEnhancedAuditClient.retrieveFirearmSearchResults(firearmSearchRequestId);
		
		model.put("firearmSearchResults", firearmSearchResults); 
		
		return "auditLogs/firearmSearchResults::firearmSearchResultsContent";
	}
	
	@RequestMapping("/vehicleSearchResults" )
	public String getVehicleSearchResults(HttpServletRequest request, 
			@RequestParam("vehicleSearchRequestId") Integer vehicleSearchRequestId, 
			Map<String, Object> model) throws Throwable {
		log.info("in getVehicleSearchResults");
		List<VehicleSearchResult> vehicleSearchResults = restEnhancedAuditClient.retrieveVehicleSearchResults(vehicleSearchRequestId);
		
		model.put("vehicleSearchResults", vehicleSearchResults); 
		
		return "auditLogs/vehicleSearchResults::vehicleSearchResultsContent";
	}
	
	@RequestMapping("/incidentSearchResults" )
	public String getIncidentSearchResults(HttpServletRequest request, 
			@RequestParam("incidentSearchRequestId") Integer incidentSearchRequestId, 
			Map<String, Object> model) throws Throwable {
		log.info("in getIncidentSearchResults");
		List<IncidentSearchResult> incidentSearchResults = restEnhancedAuditClient.retrieveIncidentSearchResults(incidentSearchRequestId);
		
		model.put("incidentSearchResults", incidentSearchResults); 
		
		return "auditLogs/incidentSearchResults::incidentSearchResultsContent";
	}
	
	private void pouplateQueryListMap(QueryRequest queryRequest) {
		String systemName = auditQuerySourceSystemMap.get(queryRequest.getIdentificationSourceText()); 
		if (StringUtils.isNotBlank(systemName)) {
			List<QueryRequest> queryRequestsByKey = clickableQueryRequestListMap.get(systemName); 
			queryRequestsByKey.add(queryRequest); 
		}
		else {
			systemName = nonClickableSourceSystemMap.get(queryRequest.getIdentificationSourceText());
			
			if (StringUtils.isNotBlank(systemName)) {
				List<QueryRequest> queryRequestsByKey = nonClickableQueryRequestListMap.get(systemName); 
				queryRequestsByKey.add(queryRequest); 
			}
		}
	}
	
	
	private void populateQueryListMaps(String sourceText) {
		
		String systemName = auditQuerySourceSystemMap.get(sourceText); 
		
		if (StringUtils.isNotBlank(systemName)) {
			clickableQueryRequestListMap.put(systemName, new ArrayList<>());
		}
		else {
			systemName = nonClickableSourceSystemMap.get(sourceText);
			if (StringUtils.isNotBlank(systemName)) {
				nonClickableQueryRequestListMap.put(systemName, new ArrayList<>()); 
			}
			else {
				log.warn("Did not find the source text, " + sourceText + " in the predefined clickableQueryRequestListMap and nonClickableQueryRequestListMap");
			}
		}
	}
	
	@RequestMapping(value="/personQueryResponse", method=RequestMethod.POST )
	public String getPersonQueryResponse(HttpServletRequest request, 
			@RequestParam("queryRequestId") Integer queryRequestId, 
			@RequestParam("systemName") String systemName,
			Map<String, Object> model) throws Throwable {
		log.info("in getQueryRequests");
		
		switch (systemName) {
		case "incident": 
			IncidentReportQueryResponse incidentReportQueryResponse = restEnhancedAuditClient.retrieveIncidentReportQueryDetail(queryRequestId); 
			model.put("incidentReportQueryResponse", incidentReportQueryResponse); 
			log.info("incidentReportQueryResponse: "+ incidentReportQueryResponse);
			return "auditLogs/incidentReportQueryResponse::incidentReportQueryResponseContent";
		case "criminalHistory": 
			PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = restEnhancedAuditClient.retrieveCriminalHistoryQueryDetail(queryRequestId); 
			model.put("personQueryCriminalHistoryResponse", personQueryCriminalHistoryResponse); 
			log.info("personQueryCriminalHistoryResponse: "+ personQueryCriminalHistoryResponse);
			return "auditLogs/personQueryCriminalHistoryResponse::personQueryCriminalHistoryResponseContent";
		case "vehicleCrash": 
			VehicleCrashQueryResponse vehicleCrashQueryResponse = restEnhancedAuditClient.retrieveVehicleCrashQueryResultsDetail(queryRequestId); 
			model.put("vehicleCrashQueryResponse", vehicleCrashQueryResponse); 
			log.info("vehicleCrashQueryResponse: "+ vehicleCrashQueryResponse);
			return "auditLogs/vehicleCrashQueryResponse::vehicleCrashQueryResponseContent";
		case "professionalLicense": 
			ProfessionalLicensingQueryResponse professionalLicensingQueryResponse = restEnhancedAuditClient.retrieveProfessionalLicensingQueryDetail(queryRequestId); 
			model.put("professionalLicensingQueryResponse", professionalLicensingQueryResponse); 
			log.info("professionalLicensingQueryResponse: "+ professionalLicensingQueryResponse);
			return "auditLogs/professionalLicensingQueryResponse::professionalLicensingQueryResponseContent";
		case "wildlifeLicense": 
			WildlifeQueryResponse wildlifeQueryResponse = restEnhancedAuditClient.retrieveWildlifeQueryDetail(queryRequestId); 
			model.put("wildlifeQueryResponse", wildlifeQueryResponse); 
			log.info("wildlifeQueryResponse: "+ wildlifeQueryResponse);
			return "auditLogs/wildlifeQueryResponse::wildlifeQueryResponseContent";
		case "firearm": 
			FirearmsQueryResponse firearmsQueryResponse = restEnhancedAuditClient.retrieveFirearmQueryDetail(queryRequestId); 
			model.put("firearmsQueryResponse", firearmsQueryResponse); 
			log.info("firearmsQueryResponse: "+ firearmsQueryResponse);
			return "auditLogs/firearmsQueryResponse::firearmsQueryResponseContent";
		case "warrant": 
			PersonQueryWarrantResponse personQueryWarrantResponse = restEnhancedAuditClient.retrieveWarrantQueryDetail(queryRequestId); 
			model.put("personQueryWarrantResponse", personQueryWarrantResponse); 
			log.info("personQueryWarrantResponse: "+ personQueryWarrantResponse);
			return "auditLogs/personQueryWarrantResponse::personQueryWarrantResponseContent";
		default:
			throw new IllegalArgumentException("Invalid identification Source Text"); 
		}
		
	}
	
}

