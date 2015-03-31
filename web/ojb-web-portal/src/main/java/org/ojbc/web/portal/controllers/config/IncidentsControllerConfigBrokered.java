package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.IncidentSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"incident-search"})
public class IncidentsControllerConfigBrokered implements
		IncidentsControllerConfigInterface {
	
	@Resource (name="incidentSearchRequestProcessor") 
	IncidentSearchInterface incidentSearchInterface;

	@Resource (name="detailQueryDispatcher") 
	DetailsQueryInterface detailsQueryInterface;

	@Override
	public IncidentSearchInterface getIncidentSearchBean() {
		return incidentSearchInterface;
	}

	@Override
	public DetailsQueryInterface getDetailsQueryBean() {
		return detailsQueryInterface;
	}
}
