package org.ojbc.web.model.vehicle.search;

import java.util.Arrays;
import java.util.List;

public class VehicleSearchRequestTestUtils {

	public static VehicleSearchRequest createVehicleSearchRequestModel()
	{
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
		
		return request;

	}
	
}
