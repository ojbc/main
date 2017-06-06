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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.ojbc.util.helper.UniqueIdUtils;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.FirearmSearchInterface;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.controllers.config.FirearmsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;
import org.ojbc.web.portal.controllers.helpers.FirearmSearchCommandUtils;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.FirearmSearchCommandValidator;
import org.springframework.validation.BindingResult;

public class FirearmControllerTest {
	private FirearmsController unit;
	private FirearmSearchCommand firearmSearchCommand;
	private FirearmSearchInterface firearmSearchInterface;
	private Map<String, Object> model;
	private String federatedQueryId = "dafdasf-324rfdsaf-vddf";
	private FirearmSearchCommandValidator firearmSearchCommandValidator;
	private FirearmSearchCommandUtils firearmSearchCommandUtils;
	private SearchResultConverter searchResultConverter;
	private ArgumentCaptor<Map> paramsCaptor;

	private BindingResult errors;
	private DetailsQueryInterface detailsQueryInterface;

	private UserSession userSession;
	private HttpServletRequest servletRequest;
	private SamlService samlService;
	

	@Before
	public void setup() {
		firearmSearchCommand = new FirearmSearchCommand();
		model = new HashMap<String, Object>();
		firearmSearchInterface = mock(FirearmSearchInterface.class);
		firearmSearchCommandValidator = mock(FirearmSearchCommandValidator.class);
		firearmSearchCommandUtils = mock(FirearmSearchCommandUtils.class);
		errors = mock(BindingResult.class);
		searchResultConverter = mock(SearchResultConverter.class);
		detailsQueryInterface = mock(DetailsQueryInterface.class);
		userSession = mock(UserSession.class);
		servletRequest = mock(HttpServletRequest.class);
		samlService = mock(SamlService.class);
		
		paramsCaptor = ArgumentCaptor.forClass(Map.class);

		
		unit = new FirearmsController() {
			@Override
			String getFederatedQueryId() {
				return federatedQueryId;
			}
		};

		unit.firearmSearchCommandUtils = firearmSearchCommandUtils;
		unit.firearmSearchCommandValidator = firearmSearchCommandValidator;
		unit.searchResultConverter = searchResultConverter;
		unit.userSession = userSession;
		unit.samlService = samlService;
		
		unit.config = new FirearmsControllerConfigInterface() {
			
			@Override
			public FirearmSearchInterface getFirearmSearchBean() {
				return firearmSearchInterface;
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

		assertThat(viewName, is("firearms/_searchForm"));

		FirearmSearchCommand initialState = (FirearmSearchCommand) model.get("firearmSearchCommand");

		assertThat(initialState.getAdvanceSearch().getFirearmSerialNumberMetaData(), is(SearchFieldMetadata.ExactMatch));
		assertNull(initialState.getAdvanceSearch().getFirearmCurrentRegOnly());
	}

	@Test
	public void searchFormUsesNewFromWhenResetIsSetToTrueAndMostRecentSearchIsReset() {
		String viewName = unit.searchForm(true, model);

		assertThat(viewName, is("firearms/_searchForm"));

		FirearmSearchCommand initialState = (FirearmSearchCommand) model.get("firearmSearchCommand");
		assertThat(initialState.getAdvanceSearch().getFirearmSerialNumberMetaData(), is(SearchFieldMetadata.ExactMatch));
		assertNull(initialState.getAdvanceSearch().getFirearmCurrentRegOnly());

		verify(userSession).setMostRecentFirearmSearch(Mockito.any(FirearmSearchCommand.class));
	}

	@Test
	public void searchFormUsesMostRecentSearchFromSession() {

		FirearmSearchCommand mostRecentSearch = new FirearmSearchCommand();
		when(userSession.getMostRecentFirearmSearch()).thenReturn(mostRecentSearch);

		unit.searchForm(false, model);

		assertSame(model.get("firearmSearchCommand"), mostRecentSearch);
	}

	@Test
	public void advanceSearchSetsSearchToSession() throws Exception {
		FirearmSearchRequest newFirearmSearchRequest = makeFirearmSearchRequest();
		firearmSearchCommand.setAdvanceSearch(newFirearmSearchRequest);
		when(userSession.getMostRecentFirearmSearch()).thenReturn(firearmSearchCommand);
		
		unit.advanceSearch(servletRequest, firearmSearchCommand, errors, model);

		verify(userSession).setMostRecentFirearmSearch(firearmSearchCommand);
	}
	
	private FirearmSearchRequest makeFirearmSearchRequest() {
		FirearmSearchRequest request = new FirearmSearchRequest();
		request.setOnBehalfOf("onBehalfOf");
		request.setPurpose("purpose");
		
		return request;
	}

	@Test
	public void advanceSearchSuccess() throws Exception {
		FirearmSearchRequest advanceSearch = makeFirearmSearchRequest();
		firearmSearchCommand.setAdvanceSearch(advanceSearch);

		FirearmSearchRequest newFirearmSearchRequest = advanceSearch;
		when(errors.hasErrors()).thenReturn(false);
		when(firearmSearchCommandUtils.getFirearmSearchRequest(firearmSearchCommand)).thenReturn(newFirearmSearchRequest);
		when(firearmSearchInterface.invokeFirearmSearchRequest(newFirearmSearchRequest, federatedQueryId, null))
		        .thenReturn("some xml");
		when(searchResultConverter.convertFirearmSearchResult(eq("some xml"),paramsCaptor.capture())).thenReturn("some html");
		when(userSession.getMostRecentFirearmSearch()).thenReturn(firearmSearchCommand);

		String expectedView = unit.advanceSearch(servletRequest, firearmSearchCommand, errors, model);

		verify(firearmSearchCommandValidator).validate(firearmSearchCommand, errors);
		verify(userSession).setMostRecentFirearmSearchResult("some xml");
		verify(samlService).getSamlAssertion(servletRequest);

		assertThat(expectedView, Matchers.is("firearms/_searchResult"));
		assertThat((String) model.get("searchContent"), is("some html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(0));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(unit.getRowsPerPage()));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(unit.getPaginateUrl()));
		assertThat((String)paramsCaptor.getValue().get("purpose"),is("purpose"));
		assertThat((String)paramsCaptor.getValue().get("onBehalfOf"),is("onBehalfOf"));

	}

	@Test
	public void advanceSearchFails() throws Exception {
		FirearmSearchRequest advanceSearch = new FirearmSearchRequest();
		firearmSearchCommand.setAdvanceSearch(advanceSearch);

		when(errors.hasErrors()).thenReturn(true);
		when(firearmSearchInterface.invokeFirearmSearchRequest(advanceSearch, federatedQueryId, null)).thenReturn(
		        "some xml");

		String expectedView = unit.advanceSearch(servletRequest, firearmSearchCommand, errors, model);

		verify(userSession).setMostRecentFirearmSearchResult(null);
		assertThat(expectedView, Matchers.is("firearms/_searchForm"));
		assertThat((BindingResult) model.get("errors"), Matchers.is(errors));
	}

	@Test
	public void getFederatedQueryId() {
		String federatedQueryId1 = UniqueIdUtils.getFederatedQueryId();
		String federatedQueryId2 = UniqueIdUtils.getFederatedQueryId();
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
		assertThat(expectedView, is("firearms/_searchDetails"));
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

	@Test(expected=NotImplementedException.class)
	public void incidentDetails() throws Throwable {
		DetailsRequest detailsRequest = new DetailsRequest();
		
		when(detailsQueryInterface.invokeRequest(detailsRequest, federatedQueryId, null))
		.thenReturn("some details xml");
		when(searchResultConverter.convertDetailSearchResult("some details xml", "mySystem",null)).thenReturn(
				"converted details xml");
		
		unit.incidentDetails(servletRequest, "mySystem", detailsRequest, model);
	}

	@Test
	public void paginateReturnsToSearchWhenSearchIsNull() throws Exception {
		when(userSession.getMostRecentSearchResult()).thenReturn(null);
		
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("redirect: searchForm"));
	}

	@Test
	public void paginateSuccess() throws Exception {
		FirearmSearchRequest request = new FirearmSearchRequest();
		firearmSearchCommand.setAdvanceSearch(request);
		when(userSession.getMostRecentFirearmSearchResult()).thenReturn("saved search xml");
		when(userSession.getMostRecentFirearmSearch()).thenReturn(firearmSearchCommand);
		
		when(searchResultConverter.convertFirearmSearchResult(eq("saved search xml"), paramsCaptor.capture())).thenReturn("saved search html");
		String expectedView = unit.paginate(33,model);
		
		assertThat(expectedView,is("firearms/_searchResult"));
		assertThat((String)model.get("searchContent"),is("saved search html"));
		
		assertThat((Integer)paramsCaptor.getValue().get("start"),is(33));
		assertThat((Integer)paramsCaptor.getValue().get("rows"),is(unit.getRowsPerPage()));
		assertThat((String)paramsCaptor.getValue().get("hrefBase"),is(unit.getPaginateUrl()));
	}

	@Test
	public void systemsToQueryReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_firearms = hashMap;
		assertSame(hashMap, unit.getSystemsToQuery());
	}
	
	@Test
	public void systemsToQueryDisabledReturnsValueFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.systemsToQuery_firearms_disabled = hashMap;
		assertSame(hashMap, unit.getSystemsToQueryDisabled());
	}
}
