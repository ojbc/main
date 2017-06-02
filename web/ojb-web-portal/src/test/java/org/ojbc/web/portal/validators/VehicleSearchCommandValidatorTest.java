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
package org.ojbc.web.portal.validators;

import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.springframework.validation.BindingResult;

public class VehicleSearchCommandValidatorTest {
	
	VehicleSearchCommandValidator unit;
	private VehicleSearchCommand vehicleSearchCommand;
	private VehicleSearchRequest advanceSearch;
	
	private BindingResult errors;

	@Before
	public void setUp() throws Exception {
		unit = new VehicleSearchCommandValidator();
		
		vehicleSearchCommand = new VehicleSearchCommand();
		advanceSearch = new VehicleSearchRequest();
		vehicleSearchCommand.setAdvanceSearch(advanceSearch);
		
		errors = mock(BindingResult.class);
	}

	
	@Test
	public void sourceSystemsMustBeSelected() {
		advanceSearch.setVehicleModel("Outback");
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("No Source Systems to search are selected."));
	}
	
	@Test
	public void oneOrMoreCriteriaMustBeSpecified() {
		advanceSearch.setSourceSystems(Arrays.asList("system1"));
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("Search must have either a make, model, color, plate number or VIN, or some combination thereof"));
	}
	
	@Test
	public void endDateMustBeGreaterThanStartDate() {
		advanceSearch.setSourceSystems(Arrays.asList("system1"));
		advanceSearch.setVehicleYearRangeStart(2009);
		advanceSearch.setVehicleYearRangeEnd(2008);
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("End year must be greater than or equal to start year"));
	}

	@Test
	public void startDateMustBeSpecifiedIfEndDateSpecified() {
		advanceSearch.setSourceSystems(Arrays.asList("system1"));
		advanceSearch.setVehicleYearRangeEnd(2008);
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("If end year is specified, start year must also be specified"));
	}
	
	@Test
	public void multipleErrors() {
		advanceSearch.setSourceSystems(Arrays.asList("system1"));
		advanceSearch.setVehicleYearRangeEnd(2008);
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("Search must have either a make, model, color, plate number or VIN, or some combination thereof"));
		Mockito.verify(errors).reject(Mockito.eq("missingRequiredInput"), Mockito.eq("If end year is specified, start year must also be specified"));
	}
	
	@Test
	public void validSearch() {
		advanceSearch.setSourceSystems(Arrays.asList("system1"));
		advanceSearch.setVehicleYearRangeStart(2007);
		advanceSearch.setVehicleYearRangeEnd(2008);
		advanceSearch.setVehicleMake("Ford");
		
		unit.validate(vehicleSearchCommand, errors);
		
		Mockito.verifyZeroInteractions(errors);
	}
}
