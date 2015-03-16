package org.ojbc.web;

import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.w3c.dom.Element;

public interface VehicleSearchInterface {
	public String invokeVehicleSearchRequest(VehicleSearchRequest vehicleSearchRequest, String federatedQueryID,
	        Element samlToken) throws Exception;
}
