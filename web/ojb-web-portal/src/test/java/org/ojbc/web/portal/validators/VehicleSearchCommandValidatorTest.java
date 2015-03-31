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
