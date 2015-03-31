package org.ojbc.web.portal.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.controllers.config.FirearmsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;
import org.ojbc.web.portal.controllers.helpers.FirearmSearchCommandUtils;
import org.ojbc.web.portal.validators.FirearmSearchCommandValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;

@Controller
@Profile({"firearms-search", "standalone"})
@RequestMapping("/firearms/*")
public class FirearmsController extends AbstractBaseController {

	private static final String URL_ROOT = "firearms";

	@Resource
	FirearmsControllerConfigInterface config;
	
	@Resource
	Map<String, String> systemsToQuery_firearms;
	
	@Resource
	Map<String, String> systemsToQuery_firearms_disabled;
	
	@Resource
	Map<String, String> firearmTypes;
	
	@Resource
	FirearmSearchCommandUtils firearmSearchCommandUtils;
	
	@Resource
	FirearmSearchCommandValidator firearmSearchCommandValidator;
	
	@Override
	protected String getUrlRoot() {
		return URL_ROOT;
	}
	
	@Override
	protected Map<String, String> getSystemsToQueryMap() {
		return systemsToQuery_firearms;
	}

	@Override
	protected Map<String, String> getSystemsToQueryDisabledMap() {
		return systemsToQuery_firearms_disabled;
	}

	@Override
	protected DetailsQueryInterface getDetailsQueryInterface() {
		return config.getDetailsQueryBean();
	}

	@Override
	String getMostRecentSearchPurpose() {
		return userSession.getMostRecentFirearmSearch().getAdvanceSearch().getPurpose();
	}

	@Override
	String getMostRecentSearchOnBehalfOf() {
		return userSession.getMostRecentFirearmSearch().getAdvanceSearch().getOnBehalfOf();
	}

	@Override
	String getMostRecentSearchResult() {
		return userSession.getMostRecentFirearmSearchResult();
	}

	@Override
	String convertSearchResult(String searchResult, Map<String, Object> params) {
		return searchResultConverter.convertFirearmSearchResult(searchResult, params);
	}

	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm || userSession.getMostRecentFirearmSearch() == null) {
			FirearmSearchCommand firearmSearchCommand = new FirearmSearchCommand();
			setInitialState(firearmSearchCommand);
			userSession.setMostRecentFirearmSearch(firearmSearchCommand);
			model.put("firearmSearchCommand", firearmSearchCommand);
		} else {
			model.put("firearmSearchCommand", userSession.getMostRecentFirearmSearch());
		}

		return "firearms/_searchForm";
	}
	
	@RequestMapping(value = "advanceSearch", method = RequestMethod.POST)
	public String advanceSearch(HttpServletRequest request, @ModelAttribute("firearmSearchCommand") FirearmSearchCommand firearmSearchCommand,
	        BindingResult errors, Map<String, Object> model) throws Exception {
		userSession.setMostRecentFirearmSearch(firearmSearchCommand);

		firearmSearchCommandValidator.validate(firearmSearchCommand, errors);

		if (errors.hasErrors()) {
			model.put("errors", errors);
			userSession.setMostRecentFirearmSearchResult(null);
			return "firearms/_searchForm";
		}

		FirearmSearchRequest firearmSearchRequest = firearmSearchCommandUtils.getFirearmSearchRequest(firearmSearchCommand);
		
		return performSearchAndReturnResults(request, model, firearmSearchRequest);
	}
	
	@Override
	@RequestMapping(value = "incidentDetails", method = RequestMethod.GET)
	public String incidentDetails(HttpServletRequest request, @RequestParam String systemName,
			@ModelAttribute("detailsRequest") DetailsRequest detailsRequest,
			Map<String, Object> model) throws Exception {

		throw new NotImplementedException("Incident details not supported for firearm search profile");
	}

	@ModelAttribute("types")
	public Map<String, String> getFirearmTypes() {
		return firearmTypes;
	}
	
	private void setInitialState(FirearmSearchCommand firearmSearchCommand) {
		firearmSearchCommand.getAdvanceSearch().setFirearmSerialNumberMetaData(SearchFieldMetadata.ExactMatch);
	}
	
	private String performSearchAndReturnResults(HttpServletRequest request, Map<String, Object> model, FirearmSearchRequest firearmSearchRequest)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String searchContent = config.getFirearmSearchBean().invokeFirearmSearchRequest(firearmSearchRequest,
				getFederatedQueryId(), samlAssertion);
		
		userSession.setMostRecentFirearmSearchResult(searchContent);
		userSession.setSavedMostRecentFirearmSearchResult(null);
		String convertFirearmSearchResult = searchResultConverter.convertFirearmSearchResult(searchContent,getParams(0, getMostRecentSearchPurpose(), getMostRecentSearchOnBehalfOf()));
		model.put("searchContent", convertFirearmSearchResult);
		
		return "firearms/_searchResult";
	}
}
