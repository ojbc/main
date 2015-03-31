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
