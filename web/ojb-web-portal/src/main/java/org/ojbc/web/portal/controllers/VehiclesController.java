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
package org.ojbc.web.portal.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ojbc.util.helper.UniqueIdUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.config.VehiclesControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.controllers.helpers.VehicleSearchCommandUtils;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.VehicleSearchCommandValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;

@Controller
@Profile({"vehicle-search", "standalone"})
@RequestMapping("/vehicles/*")
public class VehiclesController {
	public static final String PAGINATE_URL = "../vehicles/paginate";

	public static final int ROWS_PER_PAGE = 50;

    @Value("${vehiclesSearchResultPage:vehicles/_searchResult}")
    String vehiclesSearchResultPage;

	@Resource
	VehiclesControllerConfigInterface config;

	@Resource
	Map<String, String> makes;

	@Resource
	Map<String, String> colors;
	
	@Resource
	Map<String, String> systemsToQuery_vehicles;
	
	@Resource
	Map<String, String> systemsToQuery_vehicles_disabled;

	@Resource
	VehicleSearchCommandValidator vehicleSearchCommandValidator;

	@Resource
	VehicleSearchCommandUtils vehicleSearchCommandUtils;

	@Resource
	SearchResultConverter searchResultConverter;

	@Resource
	UserSession userSession;

	@Resource
	SamlService samlService;
	
	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm || userSession.getMostRecentVehicleSearch() == null) {
			VehicleSearchCommand vehicleSearchCommand = new VehicleSearchCommand();
			userSession.setMostRecentVehicleSearch(vehicleSearchCommand);
			model.put("vehicleSearchCommand", vehicleSearchCommand);
		} else {
			model.put("vehicleSearchCommand", userSession.getMostRecentVehicleSearch());
		}

		return "vehicles/_searchForm";
	}
	
	@RequestMapping(value = "advanceSearch", method = RequestMethod.POST)
	public String advanceSearch(HttpServletRequest request, @ModelAttribute("vehicleSearchCommand") VehicleSearchCommand vehicleSearchCommand,
	        BindingResult errors, Map<String, Object> model) throws Exception {
		userSession.setMostRecentVehicleSearch(vehicleSearchCommand);

		vehicleSearchCommandValidator.validate(vehicleSearchCommand, errors);

		if (errors.hasErrors()) {
			model.put("errors", errors);
			userSession.setMostRecentVehicleSearchResult(null);
			return "vehicles/_searchForm";
		}

		VehicleSearchRequest vehicleSearchRequest = vehicleSearchCommandUtils.getVehicleSearchRequest(vehicleSearchCommand);
		
		return performSearchAndReturnResults(request, model, vehicleSearchRequest);
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
		String mostRecentSearch = userSession.getMostRecentVehicleSearchResult();
		
		if(mostRecentSearch == null){
			return "redirect: searchForm";
		}
		String convertVehicleSearchResult = searchResultConverter.convertVehicleSearchResult(mostRecentSearch,getParams(start));
		model.put("searchContent", convertVehicleSearchResult);
		
		return vehiclesSearchResultPage;
	}

	@RequestMapping(value = "searchDetails", method = RequestMethod.GET)
	public String searchDetails(HttpServletRequest request, @RequestParam String systemName,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
		try {
			processDetailRequest(request, systemName, detailsRequest, model);
			return "vehicles/_searchDetails";
		} catch (Exception e) {
			e.printStackTrace();
			return "common/_searchDetailsError";
		}

	}

	@RequestMapping(value = "incidentDetails", method = RequestMethod.GET)
	public String incidentDetails(HttpServletRequest request, @RequestParam String systemName,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model)
	        throws Exception {
		processDetailRequest(request, systemName, detailsRequest, model);

		return "vehicles/_incidentDetails";
	}

	@ModelAttribute("makes")
	public Map<String, String> getMakes() {
		return makes;
	}

	@ModelAttribute("colors")
	public Map<String, String> getColors() {
		return colors;
	}
	
	@ModelAttribute("systemsToQuery")
	public Map<String, String> getSystemsToQuery() {
		return systemsToQuery_vehicles;
	}
	
	@ModelAttribute("systemsToQuery_disabled")
	public Map<String, String> getSystemsToQueryDisabled() {
		return systemsToQuery_vehicles_disabled;
	}

	private void processDetailRequest(HttpServletRequest request, String systemName, DetailsRequest detailsRequest, Map<String, Object> model)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String searchContent = config.getDetailsQueryBean().invokeRequest(detailsRequest, getFederatedQueryId(), samlAssertion);
		String convertedContent = searchResultConverter.convertDetailSearchResult(searchContent, systemName,null);
		model.put("searchContent", convertedContent);
	}

	private Map<String, Object> getParams(int start) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("rows", ROWS_PER_PAGE);
		params.put("hrefBase", PAGINATE_URL);
		return params;
	}

	private String performSearchAndReturnResults(HttpServletRequest request, Map<String, Object> model, VehicleSearchRequest vehicleSearchRequest)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String searchContent = config.getVehicleSearchBean().invokeVehicleSearchRequest(vehicleSearchRequest,
				getFederatedQueryId(), samlAssertion);
		
		userSession.setMostRecentVehicleSearchResult(searchContent);
		userSession.setSavedMostRecentVehicleSearchResult(null);
		String convertVehicleSearchResult = searchResultConverter.convertVehicleSearchResult(searchContent,getParams(0));
		model.put("searchContent", convertVehicleSearchResult);
		
		return vehiclesSearchResultPage;
	}
	
	String getFederatedQueryId() {
		return UniqueIdUtils.getFederatedQueryId();
	}

}
