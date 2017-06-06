/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
