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
import org.joda.time.DateTime;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class IncidentSearchCommandValidator {
	public void validate(IncidentSearchCommand incidentSearchCommand, BindingResult errors) {
		IncidentSearchRequest advanceSearch = incidentSearchCommand.getAdvanceSearch();

		if (StringUtils.isBlank(advanceSearch.getIncidentCityTown())
				&& StringUtils.isBlank(advanceSearch.getIncidentNumber())
				&& StringUtils.isBlank(advanceSearch.getIncidentType())) {
			errors.reject("missingRequiredInput",
			        "Search must have either a city/town, incident # or type, or some combination thereof");
		}
		
		if (hasDateRange(incidentSearchCommand)) {
			DateTime start = advanceSearch.getIncidentDateRangeStart();
			DateTime end = advanceSearch.getIncidentDateRangeEnd();
			
			if (start != null && end !=null && start.isAfter(end)) {
				//errors.rejectValue("advanceSearch.incidentDateRangeStart", "Incident end date must be greater than or equal to start date");
				errors.reject("missingRequiredInput", "Incident end date must be greater than or equal to start date");
			}
		}
		
		if(advanceSearch.getSourceSystems() == null || advanceSearch.getSourceSystems().size() == 0){
			errors.reject("missingRequiredInput","No Source Systems to search are selected.");
		}
	}

	private boolean hasDateRange(IncidentSearchCommand command) {
		return (command.getAdvanceSearch().getIncidentDateRangeStart() != null ||
				command.getAdvanceSearch().getIncidentDateRangeEnd() != null);
	}
}
