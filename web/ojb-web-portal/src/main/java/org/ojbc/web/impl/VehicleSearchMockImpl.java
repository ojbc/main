package org.ojbc.web.impl;

import org.ojbc.web.VehicleSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service("vehicleSearchMockImpl")
public class VehicleSearchMockImpl implements VehicleSearchInterface {

	@Override
	public String invokeVehicleSearchRequest(
			VehicleSearchRequest vehicleSearchRequest, String federatedQueryID,
			Element samlToken) throws Exception {
		System.out.println("VEHICLE SEARCH - Federated Query ID: " + federatedQueryID);
		System.out.println("------------------------------------------------------------------");
		System.out.println(vehicleSearchRequest);
		System.out.println("------------------------------------------------------------------");

		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
		        "/sampleResponses/vehicleSearch/VehicleSearchResults_demostate.xml"));
//		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
//		        "/sampleResponses/vehicleSearch/er_VehicleSearchResults.xml"));
	}

}
