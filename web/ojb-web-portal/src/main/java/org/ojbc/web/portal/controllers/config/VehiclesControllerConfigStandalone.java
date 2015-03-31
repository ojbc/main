package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.VehicleSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("standalone")
public class VehiclesControllerConfigStandalone implements
		VehiclesControllerConfigInterface {
	
	@Resource
	VehicleSearchInterface vehicleSearchInterface;

	@Resource
	DetailsQueryInterface detailsQueryInterface;

	@Override
	public VehicleSearchInterface getVehicleSearchBean() {
		return vehicleSearchInterface;
	}

	@Override
	public DetailsQueryInterface getDetailsQueryBean() {
		return detailsQueryInterface;
	}

}
