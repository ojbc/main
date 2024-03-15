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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.UniqueIdUtils;
import org.ojbc.web.model.consealedCarry.search.ConcealedCarrySearchRequest;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.controllers.config.IncidentsControllerConfigInterface;
import org.ojbc.web.portal.controllers.helpers.SimpleSearchParser;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@Profile({"consealed-carry-search", "standalone"})
@SessionAttributes({"mostRecentConcealedCarrySearch", "mostRecentConcealedCarrySearchResult", "savedMostRecentConcealedCarrySearchResult"})
@RequestMapping("/concealedCarry")
public class ConsealedCarryController {

	public static final int ROWS_PER_PAGE = 50;

	@Resource
	IncidentsControllerConfigInterface config;

	@Resource
	Map<String, String> systemsToQuery_concealedCarry;
	
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

	@GetMapping(value = "searchForm")
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		ConcealedCarrySearchRequest mostRecentConcealedCarrySearch = (ConcealedCarrySearchRequest) model.get("mostRecentConcealedCarrySearch"); 
		
		if (resetForm || mostRecentConcealedCarrySearch == null) {
			ConcealedCarrySearchRequest concealedCarrySearchCommand = new ConcealedCarrySearchRequest();
			model.put("mostRecentConcealedCarrySearch", concealedCarrySearchCommand); 
			model.put("concealedCarrySearchCommand", concealedCarrySearchCommand);
		} else {
			model.put("concealedCarrySearchCommand", mostRecentConcealedCarrySearch);
		}

		return "concealedCarry/searchForm::searchFormContent";
	}
	
	@PostMapping(value = "advanceSearch")
	public String advanceSearch(HttpServletRequest request, @ModelAttribute("concealedCarrySearchCommand") ConcealedCarrySearchRequest concealedCarrySearchCommand,
	        BindingResult errors, Map<String, Object> model) throws Exception {

		if (errors.hasErrors()) {
			model.put("errors", errors);
			model.put("mostRecentConcealedCarrySearch", null); 
			return "concealedCarry/searchForm::searchFormContent";
		}
		model.put("mostRecentConcealedCarrySearch", concealedCarrySearchCommand); 

		return performSearchAndReturnResults(request, model, concealedCarrySearchCommand);
	}

	@GetMapping(value = "concealedCarryDetails")
	public String concealedCarryDetails(HttpServletRequest request, @RequestParam String systemName,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model, 
	        Authentication authentication) throws InterruptedException {
		try {
			
			processDetailRequest(request, systemName, detailsRequest, model, authentication);
			
			return "concealedCarry/concealedCarryDetails";
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}

	@ModelAttribute("systemsToQuery")
	public Map<String, String> getSystemsToQuery(Authentication authentication) {
		
		Map<String, String> systemsToQuery = new LinkedHashMap<>();
		systemsToQuery.putAll(systemsToQuery_concealedCarry);
		return systemsToQuery;
	}
	
	
	String getMostRecentSearchPurpose() {
		return userSession.getMostRecentIncidentSearch().getAdvanceSearch().getPurpose();
	}
	
	String getMostRecentSearchOnBehalfOf() {
		return userSession.getMostRecentIncidentSearch().getAdvanceSearch().getOnBehalfOf();
	}

	private void processDetailRequest(HttpServletRequest request, String systemName, DetailsRequest detailsRequest, Map<String, Object> model, Authentication authentication)
			throws Exception {

		Element samlAssertion = samlService.getSamlAssertion(request);		
		
//		String searchContent = config.getDetailsQueryBean().invokeRequest(detailsRequest, getFederatedQueryId(), samlAssertion);
		String searchContent = "<ConcealedCarryDetails>ConcealedCarryDetails</ConcealedCarryDetails>";
		String convertedContent = searchResultConverter.convertConcealedCarryDetailSearchResult(searchContent, systemName);
		Thread.sleep(500);
		model.put("searchContent", convertedContent);

	}

	private Map<String, Object> getParams(int start, String purpose, String onBehalfOf) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("rows", ROWS_PER_PAGE);
		params.put("purpose", purpose);
		params.put("onBehalfOf", onBehalfOf);
		return params;
	}

	private String performSearchAndReturnResults(HttpServletRequest request, Map<String, Object> model, ConcealedCarrySearchRequest concealedCarrySearchRequest)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		if("On behalf of".equals(concealedCarrySearchRequest.getOnBehalfOf())) {
			concealedCarrySearchRequest.setOnBehalfOf(StringUtils.EMPTY);
		}
		
//		String searchContent = config.getIncidentSearchBean().invokeIncidentSearchRequest(concealedCarrySearchRequest,
//				getFederatedQueryId(), samlAssertion);
		String searchContent = "<ConcealedCarrySearchResults></ConcealedCarrySearchResults>";
		
		model.put("mostRecentConcealedCarrySearchResult", searchContent);
		model.put("savedMostRecentConcealedCarrySearchResult", null);
		
		String convertIncidentSearchResult = searchResultConverter.convertConcealedCarrySearchResult(searchContent,
				getParams(0, concealedCarrySearchRequest.getPurpose(), concealedCarrySearchRequest.getOnBehalfOf()));
		model.put("searchContent", convertIncidentSearchResult);
		
		return "concealedCarry/searchResult::searchResultContent";
	}
	
	String getFederatedQueryId() {
		return UniqueIdUtils.getFederatedQueryId();
	}

}
