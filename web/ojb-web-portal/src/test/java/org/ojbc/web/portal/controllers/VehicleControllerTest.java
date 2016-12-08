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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
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
import org.ojbc.web.VehicleSearchInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.config.VehiclesControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.controllers.helpers.VehicleSearchCommandUtils;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.VehicleSearchCommandValidator;
import org.springframework.validation.BindingResult;

public class VehicleControllerTest {
	private VehiclesController unit;
	private VehicleSearchCommand vehicleSearchCommand;
	private VehicleSearchInterface vehicleSearchInterface;
	private Map<String, Object> model;
	private String federatedQueryId = "dafdasf-324rfdsaf-vddf";
	private VehicleSearchCommandValidator vehicleSearchCommandValidator;
	private VehicleSearchCommandUtils vehicleSearchCommandUtils;
	private SearchResultConverter searchResultConverter;
	private ArgumentCaptor<Map> paramsCaptor;

	private BindingResult errors;
	private DetailsQueryInterface detailsQueryInterface;

	private UserSession userSession;
	private HttpServletRequest servletRequest;
	private SamlService samlService;
	

	@Before
	public void setup() {
		vehicleSearchCommand = new VehicleSearchCommand();
		model = new HashMap<String, Object>();
		vehicleSearchInterface = mock(VehicleSearchInterface.class);
		vehicleSearchCommandValidator = mock(VehicleSearchCommandValidator.class);
		vehicleSearchCommandUtils = mock(VehicleSearchCommandUtils.class);
		errors = mock(BindingResult.class);
		searchResultConverter = mock(SearchResultConverter.class);
		detailsQueryInterface = mock(DetailsQueryInterface.class);
		userSession = mock(UserSession.class);
		samlService = mock(SamlService.class);
		
		paramsCaptor = ArgumentCaptor.forClass(Map.class);

		
		unit = new VehiclesController() {
			@Override
			String getFederatedQueryId() {
				return federatedQueryId;
			}
		};

		unit.vehicleSearchCommandUtils = vehicleSearchCommandUtils;
		unit.vehicleSearchCommandValidator = vehicleSearchCommandValidator;
		unit.searchResultConverter = searchResultConverter;
		unit.userSession = userSession;
		unit.samlService = samlService;
		unit.vehiclesSearchResultPage="vehicles/_searchResult";
		
		unit.config = new VehiclesControllerConfigInterface() {
			
			@Override
			public VehicleSearchInterface getVehicleSearchBean() {
				return vehicleSearchInterface;
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

		assertThat(viewName, is("vehicles/_searchForm"));

		VehicleSearchCommand initialState = (VehicleSearchCommand) model.get("vehicleSearchCommand");
	}

	@Test
	public void searchFormUsesNewFromWhenResetIsSetToTrueAndMostRecentSearchIsReset() {
		String viewName = unit.searchForm(true, model);

		assertThat(viewName, is("vehicles/_searchForm"));

		VehicleSearchCommand initialState = (VehicleSearchCommand) model.get("vehicleSearchCommand");
		verify(userSession).setMostRecentVehicleSearch(Mockito.any(VehicleSearchCommand.class));
	}

	@Test
	public void searchFormUsesMostRecentSearchFromSession() {

		VehicleSearchCommand mostRecentSearch = new VehicleSearchCommand();
		when(userSession.getMostRecentVehicleSearch()).thenReturn(mostRecentSearch);

		unit.searchForm(false, model);

		assertSame(model.get("vehicleSearchCommand"), mostRecentSearch);
	}

	@Test
	public void advanceSearchSetsSearchToSession() throws Exception {
		unit.advanceSearch(servletRequest, vehicleSearchCommand, errors, model);

		verify(userSession).setMostRecentVehicleSearch(vehicleSearchCommand);
	}

	@Test
	public void advanceSearchSuccess() throws Exception {
		VehicleSearchRequest advanceSearch = new VehicleSearchRequest();
		vehicleSearchCommand.setAdvanceSearch(advanceSearch);

		VehicleSearchRequest newPersonSearchRequest = new VehicleSearchRequest();
		when(errors.hasErrors()).thenReturn(false);
		when(vehicleSearchCommandUtils.getVehicleSearchRequest(vehicleSearchCommand)).thenReturn(newPersonSearchRequest);
		when(vehicleSearchInterface.invokeVehicleSearchRequest(newPersonSearchRequest, federatedQueryId, null))
		        .thenReturn("some xml");
		when(searchResultConverter.convertVehicleSearchResult(eq("some xml"),paramsCaptor.capture())).thenReturn("some html");

		String expectedView = unit.advanceSearch(servletRequest, vehicleSearchCommand, errors, model);

		verify(vehicleSearchCommandValidator).validate(vehicleSearchCommand, errors);
		verify(userSession).setMostRecentVehicleSearchResult("some xml");
		verify(samlService).getSamlAssertion(servletRequest);

		assertThat(expectedView, Matchers.is("vehicles/_searchResult"));
		assertThat((String) model.get("searchContent"), is("some html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(0));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(VehiclesController.ROWS_PER_PAGE));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(VehiclesController.PAGINATE_URL));

	}

	@Test
	public void advanceSearchFails() throws Exception {
		VehicleSearchRequest advanceSearch = new VehicleSearchRequest();
		vehicleSearchCommand.setAdvanceSearch(advanceSearch);

		when(errors.hasErrors()).thenReturn(true);
		when(vehicleSearchInterface.invokeVehicleSearchRequest(advanceSearch, federatedQueryId, null)).thenReturn(
		        "some xml");

		String expectedView = unit.advanceSearch(servletRequest, vehicleSearchCommand, errors, model);

		verify(userSession).setMostRecentVehicleSearchResult(null);
		assertThat(expectedView, Matchers.is("vehicles/_searchForm"));
		assertThat((BindingResult) model.get("errors"), Matchers.is(errors));
	}

	@Test
	public void getFederatedQueryId() {
		VehiclesController unit = new VehiclesController();
		String federatedQueryId1 = unit.getFederatedQueryId();
		String federatedQueryId2 = unit.getFederatedQueryId();
		assertThat(federatedQueryId1, Matchers.notNullValue());

		assertThat(federatedQueryId1.equals(federatedQueryId2), is(false));
	}

	@Test
	public void searchDetails() throws Throwable {
		DetailsRequest detailsRequest = new DetailsRequest();

		when(detailsQueryInterface.invokeRequest(detailsRequest, federatedQueryId, null))
		        .thenReturn("some details xml");
		when(searchResultConverter.convertDetailSearchResult("some details xml", "mySystem", null)).thenReturn(
		        "converted details xml");

		String expectedView = unit.searchDetails(servletRequest, "mySystem", detailsRequest, model);

		verify(samlService).getSamlAssertion(servletRequest);
		assertThat(expectedView, is("vehicles/_searchDetails"));
		assertThat((String) model.get("searchContent"), is("converted details xml"));
	}
	
	@Test
	public void searchDetailsExceptionReturnsErrorView() throws Exception {
		DetailsRequest detailsRequest = new DetailsRequest();

		when(detailsQueryInterface.invokeRequest(detailsRequest, federatedQueryId, null))
		        .thenThrow(new RuntimeException());
		
		String expectedView = unit.searchDetails(servletRequest, "mySystem", detailsRequest, model);
		
		assertThat(expectedView, is("common/_searchDetailsError"));
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
		assertThat(expectedView, is("vehicles/_incidentDetails"));
		assertThat((String) model.get("searchContent"), is("converted details xml"));
		
	}

	@Test
	public void paginateReturnsToSearchWhenSearchIsNull() throws Exception {
		when(userSession.getMostRecentSearchResult()).thenReturn(null);
		
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("redirect: searchForm"));
	}

	@Test
	public void paginateSuccess() throws Exception {
		when(userSession.getMostRecentVehicleSearchResult()).thenReturn("saved search xml");
		
		when(searchResultConverter.convertVehicleSearchResult(eq("saved search xml"), paramsCaptor.capture())).thenReturn("saved search html");
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("vehicles/_searchResult"));
		assertThat((String)model.get("searchContent"),is("saved search html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(33));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(VehiclesController.ROWS_PER_PAGE));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(VehiclesController.PAGINATE_URL));
	}

	@Test
	public void systemsToQueryReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_vehicles = hashMap;
		assertSame(hashMap, unit.getSystemsToQuery());
	}
	
	@Test
	public void systemsToQueryDisabledReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_vehicles_disabled = hashMap;
		assertSame(hashMap, unit.getSystemsToQueryDisabled());
	}
}
