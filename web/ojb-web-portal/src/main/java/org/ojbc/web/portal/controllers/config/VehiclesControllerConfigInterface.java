package org.ojbc.web.portal.controllers.config;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.VehicleSearchInterface;

public interface VehiclesControllerConfigInterface {
	VehicleSearchInterface getVehicleSearchBean();
	DetailsQueryInterface getDetailsQueryBean();
}
