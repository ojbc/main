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
import org.ojbc.web.portal.controllers.config.IncidentsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.ojbc.web.portal.controllers.helpers.IncidentSearchCommandUtils;
import org.ojbc.web.portal.controllers.helpers.SimpleSearchParser;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.IncidentSearchCommandValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;

@Controller
@Profile({"incident-search", "standalone"})
@RequestMapping("/incidents/*")
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

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateTime.class, new DateTimePropertyEditor());
	}

	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm || userSession.getMostRecentIncidentSearch() == null) {
			IncidentSearchCommand incidentSearchCommand = new IncidentSearchCommand();
			//setInitialState(incidentSearchCommand);
			userSession.setMostRecentIncidentSearch(incidentSearchCommand);
			model.put("incidentSearchCommand", incidentSearchCommand);
		} else {
			model.put("incidentSearchCommand", userSession.getMostRecentIncidentSearch());
		}

		return "incidents/_searchForm";
	}
	
	@RequestMapping(value = "advanceSearch", method = RequestMethod.POST)
	public String advanceSearch(HttpServletRequest request, @ModelAttribute("incidentSearchCommand") IncidentSearchCommand incidentSearchCommand,
	        BindingResult errors, Map<String, Object> model) throws Exception {
		userSession.setMostRecentIncidentSearch(incidentSearchCommand);

		incidentSearchCommandValidator.validate(incidentSearchCommand, errors);

		if (errors.hasErrors()) {
			model.put("errors", errors);
			userSession.setMostRecentIncidentSearchResult(null);
			return "incidents/_searchForm";
		}

		IncidentSearchRequest incidentSearchRequest = incidentSearchCommandUtils.getIncidentSearchRequest(incidentSearchCommand);
		
		return performSearchAndReturnResults(request, model, incidentSearchRequest);
	}

    /**
     * Not needed since jQuery dataTable is used to accomplish pagination. 
     * @param start
     * @param model
     * @return
     */
    @Deprecated
	@RequestMapping(value="paginate", method = RequestMethod.GET)
	public String paginate(@RequestParam(value="start",defaultValue="0") int start, Map<String,Object> model){
		String mostRecentSearch = userSession.getMostRecentIncidentSearchResult();
		
		if(mostRecentSearch == null){
			return "redirect: searchForm";
		}
		String convertIncidentSearchResult = searchResultConverter.convertIncidentSearchResult(mostRecentSearch,getParams(start, getMostRecentSearchPurpose(), getMostRecentSearchOnBehalfOf()));
		model.put("searchContent", convertIncidentSearchResult);
		
		return "incidents/_searchResult";
	}

	@RequestMapping(value = "incidentDetails", method = RequestMethod.GET)
	public String incidentDetails(HttpServletRequest request, @RequestParam String systemName,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
		try {
			processDetailRequest(request, systemName, detailsRequest, model);
			return "incidents/_incidentDetails";
		} catch (Exception e) {
			e.printStackTrace();
			return "common/_searchDetailsError";
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
	public Map<String, String> getSystemsToQuery() {
		return systemsToQuery_incidents;
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

	private void processDetailRequest(HttpServletRequest request, String systemName, DetailsRequest detailsRequest, Map<String, Object> model)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		String searchContent = config.getDetailsQueryBean().invokeRequest(detailsRequest, getFederatedQueryId(), samlAssertion);
		String convertedContent = searchResultConverter.convertDetailSearchResult(searchContent, systemName,null);
		model.put("searchContent", convertedContent);
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
		
		return "incidents/_searchResult";
	}
	
	String getFederatedQueryId() {
		return UniqueIdUtils.getFederatedQueryId();
	}

}
