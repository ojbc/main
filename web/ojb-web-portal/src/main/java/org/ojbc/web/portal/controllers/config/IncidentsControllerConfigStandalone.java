package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.IncidentSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("standalone")
public class IncidentsControllerConfigStandalone implements IncidentsControllerConfigInterface {
	@Resource
	IncidentSearchInterface incidentSearchInterface;

	@Resource
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
