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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.IncidentSearchInterface;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.controllers.config.IncidentsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.ojbc.web.portal.controllers.helpers.IncidentSearchCommandUtils;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.IncidentSearchCommandValidator;
import org.springframework.validation.BindingResult;

public class IncidentControllerTest {
	private IncidentsController unit;
	private IncidentSearchCommand incidentSearchCommand;
	private IncidentSearchInterface incidentSearchInterface;
	private Map<String, Object> model;
	private String federatedQueryId = "dafdasf-324rfdsaf-vddf";
	private IncidentSearchCommandValidator incidentSearchCommandValidator;
	private IncidentSearchCommandUtils incidentSearchCommandUtils;
	private SearchResultConverter searchResultConverter;
	private ArgumentCaptor<Map> paramsCaptor;

	private BindingResult errors;
	private DetailsQueryInterface detailsQueryInterface;

	private UserSession userSession;
	private HttpServletRequest servletRequest;
	private SamlService samlService;

	@Before
	public void setup() {
		incidentSearchCommand = new IncidentSearchCommand();
		model = new HashMap<String, Object>();
		incidentSearchInterface = mock(IncidentSearchInterface.class);
		incidentSearchCommandValidator = mock(IncidentSearchCommandValidator.class);
		incidentSearchCommandUtils = mock(IncidentSearchCommandUtils.class);
		errors = mock(BindingResult.class);
		searchResultConverter = mock(SearchResultConverter.class);
		detailsQueryInterface = mock(DetailsQueryInterface.class);
		userSession = mock(UserSession.class);
		servletRequest = mock(HttpServletRequest.class);
		samlService = mock(SamlService.class);
		
		paramsCaptor = ArgumentCaptor.forClass(Map.class);

		
		unit = new IncidentsController() {
			@Override
			String getFederatedQueryId() {
				return federatedQueryId;
			}
		};

		unit.incidentSearchCommandUtils = incidentSearchCommandUtils;
		unit.incidentSearchCommandValidator = incidentSearchCommandValidator;
		unit.searchResultConverter = searchResultConverter;
		unit.userSession = userSession;
		unit.samlService = samlService;
		
		unit.config = new IncidentsControllerConfigInterface() {
			
			@Override
			public IncidentSearchInterface getIncidentSearchBean() {
				return incidentSearchInterface;
			}
			
			@Override
			public DetailsQueryInterface getDetailsQueryBean() {
				return detailsQueryInterface;
			}
		};
	}

	@Test
	public void searchFormReturnsCorrectViewNameAndInitialData() {
		String viewName = unit.searchForm(false, model);

		assertThat(viewName, is("incidents/_searchForm"));

		IncidentSearchCommand initialState = (IncidentSearchCommand) model.get("incidentSearchCommand");
	}

	@Test
	public void searchFormUsesNewFromWhenResetIsSetToTrueAndMostRecentSearchIsReset() {
		String viewName = unit.searchForm(true, model);

		assertThat(viewName, is("incidents/_searchForm"));

		IncidentSearchCommand initialState = (IncidentSearchCommand) model.get("incidentSearchCommand");
		verify(userSession).setMostRecentIncidentSearch(Mockito.any(IncidentSearchCommand.class));
	}

	@Test
	public void searchFormUsesMostRecentSearchFromSession() {

		IncidentSearchCommand mostRecentSearch = new IncidentSearchCommand();
		when(userSession.getMostRecentIncidentSearch()).thenReturn(mostRecentSearch);

		unit.searchForm(false, model);

		assertSame(model.get("incidentSearchCommand"), mostRecentSearch);
	}

	@Test
	public void advanceSearchSetsSearchToSession() throws Exception {
		IncidentSearchRequest newIncidentSearchRequest = makeIncidentSearchRequest();
		incidentSearchCommand.setAdvanceSearch(newIncidentSearchRequest);
		when(incidentSearchCommandUtils.getIncidentSearchRequest(any(IncidentSearchCommand.class))).thenReturn(newIncidentSearchRequest);
		when(userSession.getMostRecentIncidentSearch()).thenReturn(incidentSearchCommand);
		
		unit.advanceSearch(servletRequest, incidentSearchCommand, errors, model);

		verify(userSession).setMostRecentIncidentSearch(incidentSearchCommand);
		verify(samlService).getSamlAssertion(servletRequest);
	}
	
	private IncidentSearchRequest makeIncidentSearchRequest() {
		IncidentSearchRequest request = new IncidentSearchRequest();
		request.setPurpose("purpose");
		request.setOnBehalfOf("onBehalfOf");
		
		return request;
	}

	@Test
	public void advanceSearchSuccess() throws Exception {
		IncidentSearchRequest advanceSearch = new IncidentSearchRequest();
		incidentSearchCommand.setAdvanceSearch(advanceSearch);

		IncidentSearchRequest newPersonSearchRequest = makeIncidentSearchRequest();
		when(errors.hasErrors()).thenReturn(false);
		when(incidentSearchCommandUtils.getIncidentSearchRequest(incidentSearchCommand)).thenReturn(newPersonSearchRequest);
		when(incidentSearchInterface.invokeIncidentSearchRequest(newPersonSearchRequest, federatedQueryId, null))
		        .thenReturn("some xml");
		when(searchResultConverter.convertIncidentSearchResult(eq("some xml"),paramsCaptor.capture())).thenReturn("some html");
		when(userSession.getMostRecentIncidentSearch()).thenReturn(incidentSearchCommand);
		
		String expectedView = unit.advanceSearch(servletRequest, incidentSearchCommand, errors, model);

		verify(incidentSearchCommandValidator).validate(incidentSearchCommand, errors);
		verify(userSession).setMostRecentIncidentSearchResult("some xml");
		verify(samlService).getSamlAssertion(servletRequest);

		assertThat(expectedView, Matchers.is("incidents/_searchResult"));
		assertThat((String) model.get("searchContent"), is("some html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(0));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(IncidentsController.ROWS_PER_PAGE));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(IncidentsController.PAGINATE_URL));

	}

	@Test
	public void advanceSearchFails() throws Exception {
		IncidentSearchRequest advanceSearch = new IncidentSearchRequest();
		incidentSearchCommand.setAdvanceSearch(advanceSearch);

		when(errors.hasErrors()).thenReturn(true);
		when(incidentSearchInterface.invokeIncidentSearchRequest(advanceSearch, federatedQueryId, null)).thenReturn(
		        "some xml");

		String expectedView = unit.advanceSearch(servletRequest, incidentSearchCommand, errors, model);

		verify(userSession).setMostRecentIncidentSearchResult(null);
		assertThat(expectedView, Matchers.is("incidents/_searchForm"));
		assertThat((BindingResult) model.get("errors"), Matchers.is(errors));
	}

	@Test
	public void getFederatedQueryId() {
		IncidentsController unit = new IncidentsController();
		String federatedQueryId1 = unit.getFederatedQueryId();
		String federatedQueryId2 = unit.getFederatedQueryId();
		assertThat(federatedQueryId1, Matchers.notNullValue());

		assertThat(federatedQueryId1.equals(federatedQueryId2), is(false));
	}

	@Test
	public void incidentDetails() throws Throwable {
		DetailsRequest detailsRequest = new DetailsRequest();
		
		when(detailsQueryInterface.invokeRequest(detailsRequest, federatedQueryId, null))
		.thenReturn("some details xml");
		when(searchResultConverter.convertDetailSearchResult("some details xml", "mySystem", null)).thenReturn(
				"converted details xml");
		
		String expectedView = unit.incidentDetails(servletRequest, "mySystem", detailsRequest, model);
		
		verify(samlService).getSamlAssertion(servletRequest);
		assertThat(expectedView, is("incidents/_incidentDetails"));
		assertThat((String) model.get("searchContent"), is("converted details xml"));
	}
	
	@Test
	public void incidentDetailsExceptionReturnsErrorView() throws Exception {
		DetailsRequest detailsRequest = new DetailsRequest();
		
		when(detailsQueryInterface.invokeRequest(detailsRequest, federatedQueryId, null))
			.thenThrow(new RuntimeException());
		
		String expectedView = unit.incidentDetails(servletRequest, "mySystem", detailsRequest, model);
		
		assertThat(expectedView, is("common/_searchDetailsError"));
	}

	@Test
	public void paginateReturnsToSearchWhenSearchIsNull() throws Exception {
		when(userSession.getMostRecentSearchResult()).thenReturn(null);
		
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("redirect: searchForm"));
	}

	@Test
	public void paginateSuccess() throws Exception {
		IncidentSearchRequest newIncidentSearchRequest = makeIncidentSearchRequest();
		incidentSearchCommand.setAdvanceSearch(newIncidentSearchRequest);
		when(userSession.getMostRecentIncidentSearchResult()).thenReturn("saved search xml");
		when(userSession.getMostRecentIncidentSearch()).thenReturn(incidentSearchCommand);
		
		when(searchResultConverter.convertIncidentSearchResult(eq("saved search xml"), paramsCaptor.capture())).thenReturn("saved search html");
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("incidents/_searchResult"));
		assertThat((String)model.get("searchContent"),is("saved search html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(33));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(IncidentsController.ROWS_PER_PAGE));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(IncidentsController.PAGINATE_URL));
	}
	
	@Test
	public void systemsToQueryReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_incidents = hashMap;
		assertSame(hashMap, unit.getSystemsToQuery());
	}
	
	@Test
	public void systemsToQueryDisabledReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_incidents_disabled = hashMap;
		assertSame(hashMap, unit.getSystemsToQueryDisabled());
	}
}
