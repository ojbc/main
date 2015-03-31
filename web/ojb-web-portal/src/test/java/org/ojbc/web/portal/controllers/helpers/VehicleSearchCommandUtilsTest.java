package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;

public class VehicleSearchCommandUtilsTest {
	
	private VehicleSearchCommandUtils unit;
	private VehicleSearchCommand vehicleSearchCommand;
	private VehicleSearchRequest advanceSearch;
	
	@Before
	public void setUp() {
		unit = new VehicleSearchCommandUtils();
		
		vehicleSearchCommand = new VehicleSearchCommand();
		advanceSearch = new VehicleSearchRequest();
		vehicleSearchCommand.setAdvanceSearch(advanceSearch);
	}

	@Test
	public void deepClone() {
		List<String> sourceSystems = Arrays.asList("src1", "src2");
		
		VehicleSearchRequest request = new VehicleSearchRequest();
		request.setSourceSystems(sourceSystems);
		request.setVehicleColor("color");
		request.setVehicleMake("make");
		request.setVehicleModel("model");
		request.setVehiclePlateNumber("plateNumber");
		request.setVehicleVIN("vin");
		request.setVehicleYearRangeStart(1999);
		request.setVehicleYearRangeEnd(2002);
		
		VehicleSearchRequest clone = unit.cloneVehicleSearchRequest(request);
		assertThat(clone.getVehicleColor(), is("color"));
		assertThat(clone.getVehicleMake(), is("make"));
		assertThat(clone.getVehicleModel(), is("model"));
		assertThat(clone.getVehiclePlateNumber(), is("plateNumber"));
		assertThat(clone.getVehicleVIN(), is("vin"));
		assertThat(clone.getVehicleYearRangeStart(), is(1999));
		assertThat(clone.getVehicleYearRangeEnd(), is(2002));
		assertThat(clone.getSourceSystems(), is(sourceSystems));
		
		assertNotSame(clone.getSourceSystems(), sourceSystems);
	}

}
