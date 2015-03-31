package org.ojbc.web.portal.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;

public abstract class AbstractBaseController {
	private static final int DEFAULT_ROWS_PER_PAGE = 50;
	
	@Resource
	protected SearchResultConverter searchResultConverter;
	
	@Resource
	protected UserSession userSession;
	
	@Resource
	protected SamlService samlService;

	public AbstractBaseController() {
		super();
	}
	
	protected abstract String getUrlRoot();
	
	protected abstract DetailsQueryInterface getDetailsQueryInterface();

	abstract String getMostRecentSearchPurpose();
	
	abstract String getMostRecentSearchOnBehalfOf();
	
	abstract String getMostRecentSearchResult();
	
	abstract String convertSearchResult(String searchResult, Map<String, Object> params);
	
	int getRowsPerPage() {
		return DEFAULT_ROWS_PER_PAGE;
	}
	
	String getPaginateUrl() {
		return "../" + getUrlRoot() + "/paginate";
	}
	
	protected abstract Map<String, String> getSystemsToQueryMap();
	
	protected abstract Map<String, String> getSystemsToQueryDisabledMap();

	@RequestMapping(value = "paginate", method = RequestMethod.GET)
	public String paginate(@RequestParam(value="start",defaultValue="0") int start, Map<String,Object> model) {
		String mostRecentSearch = getMostRecentSearchResult();
		
		if(mostRecentSearch == null){
			return "redirect: searchForm";
		}

		String convertedSearchResult = convertSearchResult(mostRecentSearch, getParams(start, getMostRecentSearchPurpose(), getMostRecentSearchOnBehalfOf()));
		model.put("searchContent", convertedSearchResult);
		
		return getUrlRoot() + "/_searchResult";
	}

	@ModelAttribute("systemsToQuery")
	public Map<String, String> getSystemsToQuery() {
		return getSystemsToQueryMap();
	}
	
	@ModelAttribute("systemsToQuery_disabled")
	public Map<String, String> getSystemsToQueryDisabled() {
		return getSystemsToQueryDisabledMap();
	}

	String getFederatedQueryId() {
		return UUID.randomUUID().toString();
	}

	protected Map<String, Object> getParams(int start, String purpose, String onBehalfOf) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("rows", getRowsPerPage());
		params.put("hrefBase", getPaginateUrl());
		params.put("purpose", purpose);
		params.put("onBehalfOf", onBehalfOf);
		
		return params;
	}

	protected void processDetailRequest(HttpServletRequest request, String systemName, DetailsRequest detailsRequest, Map<String, Object> model)
			throws Exception {
				Element samlAssertion = samlService.getSamlAssertion(request);
				
				String searchContent = getDetailsQueryInterface().invokeRequest(detailsRequest, getFederatedQueryId(), samlAssertion);
				String convertedContent = searchResultConverter.convertDetailSearchResult(searchContent, systemName);
				model.put("searchContent", convertedContent);
	}

	@RequestMapping(value = "searchDetails", method = RequestMethod.GET)
	public String searchDetails(HttpServletRequest request, @RequestParam String systemName, 
			@ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
				try {
					processDetailRequest(request, systemName, detailsRequest, model);
					return getUrlRoot() + "/_searchDetails";
				} catch (Exception e) {
					e.printStackTrace();
					return "common/_searchDetailsError";
				}
			}

	@RequestMapping(value = "incidentDetails", method = RequestMethod.GET)
	public String incidentDetails(HttpServletRequest request, @RequestParam String systemName, @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model)
			throws Exception {
				processDetailRequest(request, systemName, detailsRequest, model);
			
				return getUrlRoot() + "/_incidentDetails";
			}

}