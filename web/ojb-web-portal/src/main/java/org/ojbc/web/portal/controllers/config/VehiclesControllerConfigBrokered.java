package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.VehicleSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"vehicle-search"})
public class VehiclesControllerConfigBrokered implements
		VehiclesControllerConfigInterface {
	
    /*
     * If vehicleSearchRequestProcessorBean is defined in cfg file, use the property value, 
     * Otherwise, use the default bean "vehicleSearchRequestProcessor"
     */
	@Resource (name="${vehicleSearchRequestProcessorBean:vehicleSearchRequestProcessor}")
	VehicleSearchInterface vehicleSearchInterface;

	@Resource (name="detailQueryDispatcher")
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
