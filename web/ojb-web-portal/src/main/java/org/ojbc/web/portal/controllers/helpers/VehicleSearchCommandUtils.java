package org.ojbc.web.portal.controllers.helpers;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.springframework.stereotype.Service;

@Service
public class VehicleSearchCommandUtils {
	public VehicleSearchRequest getVehicleSearchRequest(VehicleSearchCommand vehicleSearchCommand) {
		VehicleSearchRequest request = cloneVehicleSearchRequest(vehicleSearchCommand.getAdvanceSearch());
		
		return request;
	}

	VehicleSearchRequest cloneVehicleSearchRequest(
			VehicleSearchRequest originalRequest) {

		try {
			VehicleSearchRequest cloneBean = (VehicleSearchRequest) BeanUtils.cloneBean(originalRequest);
			
			cloneBean.setSourceSystems(new ArrayList<String>(originalRequest.getSourceSystems()));
			return cloneBean;
		} catch (Exception ex) {
			throw new RuntimeException("Unable to clone VehicleSearchRequest", ex);
		}
	}
	
	
}
