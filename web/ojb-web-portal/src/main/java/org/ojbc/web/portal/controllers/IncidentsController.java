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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.helper.UniqueIdUtils;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.controllers.config.IncidentsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.ojbc.web.portal.controllers.helpers.IncidentSearchCommandUtils;
import org.ojbc.web.portal.controllers.helpers.SimpleSearchParser;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.IncidentSearchCommandValidator;
import org.ojbc.web.security.Authorities;
import org.ojbc.web.security.SecurityContextUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@Profile({"incident-search", "standalone"})
@SessionAttributes({"incidentSystemsToQuery"})
@RequestMapping("/incidents")
public class IncidentsController {
	public static final String PAGINATE_URL = "../incidents/paginate";

	public static final int ROWS_PER_PAGE = 50;

	@Resource
	IncidentsControllerConfigInterface config;

	@Resource
	Map<String, String> incidentTypes;

	@Resource
	Map<String, String> cityTowns;
	
	@Resource
	Map<String, String> systemsToQuery_incidents;
	
	@Resource
	Map<String, String> systemsToQuery_incidents_disabled;

	@Resource
	IncidentSearchCommandValidator incidentSearchCommandValidator;

	@Resource
	IncidentSearchCommandUtils incidentSearchCommandUtils;

	@Resource
	SearchResultConverter searchResultConverter;

	@Resource
	UserSession userSession;

	@Resource
	SimpleSearchParser simpleSearchParser;
	
	@Resource
	SamlService samlService;

	@Resource
    AppProperties appProperties;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateTime.class, new DateTimePropertyEditor());
	}

	@GetMapping(value = "searchForm")
	public String searchForm(@RequestParam(required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm || userSession.getMostRecentIncidentSearch() == null) {
			IncidentSearchCommand incidentSearchCommand = new IncidentSearchCommand();
			//setInitialState(incidentSearchCommand);
			userSession.setMostRecentIncidentSearch(incidentSearchCommand);
			model.put("incidentSearchCommand", incidentSearchCommand);
		} else {
			model.put("incidentSearchCommand", userSession.getMostRecentIncidentSearch());
		}

		return "incidents/searchForm::searchFormContent";
	}
	
	@PostMapping(value = "advanceSearch")
	public String advanceSearch(HttpServletRequest request, @ModelAttribute IncidentSearchCommand incidentSearchCommand,
	        BindingResult errors, Map<String, Object> model) throws Exception {
		userSession.setMostRecentIncidentSearch(incidentSearchCommand);

		incidentSearchCommandValidator.validate(incidentSearchCommand, errors);

		if (errors.hasErrors()) {
			model.put("errors", errors);
			userSession.setMostRecentIncidentSearchResult(null);
			return "incidents/searchForm::searchFormContent";
		}

		IncidentSearchRequest incidentSearchRequest = incidentSearchCommandUtils.getIncidentSearchRequest(incidentSearchCommand);
		
		return performSearchAndReturnResults(request, model, incidentSearchRequest);
	}

	@GetMapping(value = "incidentDetails")
	public String incidentDetails(HttpServletRequest request, @RequestParam String systemName,
	        @ModelAttribute DetailsRequest detailsRequest, Map<String, Object> model, 
	        Authentication authentication) throws InterruptedException {
		try {
			
			processDetailRequest(request, systemName, detailsRequest, model, authentication);
			
			return "incidents/incidentDetails";
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}

	@ModelAttribute("incidentTypes")
	public Map<String, String> getIncidentTypes() {
		return incidentTypes;
	}

	@ModelAttribute("cityTowns")
	public Map<String, String> getCityTowns() {
		return cityTowns;
	}
	
	@ModelAttribute("systemsToQuery")
	public Map<String, String> getSystemsToQuery(Map<String, Object> model) {
		@SuppressWarnings("unchecked")
		Map<String, String> systemsToQuery = (Map<String, String>) model.get("incidentSystemsToQuery");
		return systemsToQuery;
	}
	
	@ModelAttribute("systemsToQuery_disabled")
	public Map<String, String> getSystemsToQueryDisabled() {
		return systemsToQuery_incidents_disabled;
	}

	String getMostRecentSearchPurpose() {
		return userSession.getMostRecentIncidentSearch().getAdvanceSearch().getPurpose();
	}
	
	String getMostRecentSearchOnBehalfOf() {
		return userSession.getMostRecentIncidentSearch().getAdvanceSearch().getOnBehalfOf();
	}

	private void processDetailRequest(HttpServletRequest request, String systemName, DetailsRequest detailsRequest, Map<String, Object> model, Authentication authentication)
			throws Exception {

		if (authentication == null || SecurityContextUtils.hasAuthority(authentication, Authorities.AUTHZ_INCIDENT_DETAIL))
		{
			Element samlAssertion = samlService.getSamlAssertion(request);		
			
			String searchContent = config.getDetailsQueryBean().invokeRequest(detailsRequest, getFederatedQueryId(), samlAssertion);
			String convertedContent = searchResultConverter.convertDetailSearchResult(searchContent, systemName,null);
			model.put("searchContent", convertedContent);
		}
		else
		{
			model.put("searchContent", "<span class='error'>User is not authorized to see incident detail.</span>");
			Thread.sleep(500);
		}

	}

	private Map<String, Object> getParams(int start, String purpose, String onBehalfOf) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("rows", ROWS_PER_PAGE);
		params.put("hrefBase", PAGINATE_URL);
		params.put("purpose", purpose);
		params.put("onBehalfOf", onBehalfOf);
		return params;
	}

	private String performSearchAndReturnResults(HttpServletRequest request, Map<String, Object> model, IncidentSearchRequest incidentSearchRequest)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		if("On behalf of".equals(incidentSearchRequest.getOnBehalfOf())) {
			incidentSearchRequest.setOnBehalfOf(StringUtils.EMPTY);
		}
		
		String searchContent = config.getIncidentSearchBean().invokeIncidentSearchRequest(incidentSearchRequest,
				getFederatedQueryId(), samlAssertion);
		
		userSession.setMostRecentIncidentSearchResult(searchContent);
		userSession.setSavedMostRecentIncidentSearchResult(null);
		String convertIncidentSearchResult = searchResultConverter.convertIncidentSearchResult(searchContent,getParams(0, getMostRecentSearchPurpose(), getMostRecentSearchOnBehalfOf()));
		model.put("searchContent", convertIncidentSearchResult);
		
		return "incidents/searchResult::searchResultContent";
	}
	
	String getFederatedQueryId() {
		return UniqueIdUtils.getFederatedQueryId();
	}

}
