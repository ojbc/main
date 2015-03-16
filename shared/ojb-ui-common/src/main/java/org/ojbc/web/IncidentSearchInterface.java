package org.ojbc.web;

import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.w3c.dom.Element;

public interface IncidentSearchInterface {
	public String invokeIncidentSearchRequest(IncidentSearchRequest incidentSearchRequest, String federatedQueryID,
	        Element samlToken) throws Exception;
}
