package org.ojbc.web.portal.controllers.config;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.IncidentSearchInterface;

public interface IncidentsControllerConfigInterface {
	IncidentSearchInterface getIncidentSearchBean();
	DetailsQueryInterface getDetailsQueryBean();
}
