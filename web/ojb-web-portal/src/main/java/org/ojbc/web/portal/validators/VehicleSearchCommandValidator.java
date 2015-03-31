package org.ojbc.web.portal.validators;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class VehicleSearchCommandValidator {
	public void validate(VehicleSearchCommand vehicleSearchCommand,
			BindingResult errors) {

		VehicleSearchRequest advanceSearch = vehicleSearchCommand.getAdvanceSearch();
		
		if (StringUtils.isBlank(advanceSearch.getVehicleColor())
				&& StringUtils.isBlank(advanceSearch.getVehicleMake())
				&& StringUtils.isBlank(advanceSearch.getVehicleModel())
				&& StringUtils.isBlank(advanceSearch.getVehiclePlateNumber())
				&& StringUtils.isBlank(advanceSearch.getVehicleVIN())) {
			errors.reject("missingRequiredInput",
			        "Search must have either a make, model, color, plate number or VIN, or some combination thereof");
		}
		
		if (hasYearRange(vehicleSearchCommand)) {
			Integer startYear = vehicleSearchCommand.getAdvanceSearch().getVehicleYearRangeStart();
			Integer endYear =  vehicleSearchCommand.getAdvanceSearch().getVehicleYearRangeEnd();
			
			if (endYear != null && startYear == null) {
				errors.reject("missingRequiredInput", "If end year is specified, start year must also be specified");
			}
			
			if (startYear != null && endYear != null && startYear > endYear) {
				errors.reject("missingRequiredInput", "End year must be greater than or equal to start year");
			}
		}
		
		if(advanceSearch.getSourceSystems() == null || advanceSearch.getSourceSystems().size() == 0){
			errors.reject("missingRequiredInput","No Source Systems to search are selected.");
		}
	}

	private boolean hasYearRange(VehicleSearchCommand vehicleSearchCommand) {
		return (vehicleSearchCommand.getAdvanceSearch().getVehicleYearRangeStart() != null
				|| vehicleSearchCommand.getAdvanceSearch().getVehicleYearRangeEnd() != null);
	}
}
