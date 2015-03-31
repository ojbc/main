package org.ojbc.web.portal.validators;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class FirearmSearchCommandValidator {
	public void validate(FirearmSearchCommand firearmSearchCommand, BindingResult errors) {
		
		FirearmSearchRequest advanceSearch = firearmSearchCommand.getAdvanceSearch();
		
		if (StringUtils.isBlank(advanceSearch.getFirearmCounty())  &&
				StringUtils.isBlank(advanceSearch.getFirearmType()) &&
				StringUtils.isBlank(advanceSearch.getFirearmMake()) &&
				StringUtils.isBlank(advanceSearch.getFirearmModel()) &&
				StringUtils.isBlank(advanceSearch.getFirearmRegistrationNumber()) &&
				StringUtils.isBlank(advanceSearch.getFirearmSerialNumber())) {
			errors.reject("missingRequiredInput",
			        "Search must have either a county, firearm type, make, model, serial number or registration number, or some combination thereof");
		}
		
		if(advanceSearch.getSourceSystems() == null || advanceSearch.getSourceSystems().size() == 0){
			errors.reject("missingRequiredInput","No Source Systems to search are selected.");
		}
	}
}
