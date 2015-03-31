package org.ojbc.web.impl;

import org.ojbc.web.IncidentSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class IncidentSearchMockImpl implements IncidentSearchInterface {

	@Override
	public String invokeIncidentSearchRequest(
			IncidentSearchRequest incidentSearchRequest,
			String federatedQueryID, Element samlToken) throws Exception {

		System.out.println("INCIDENT SEARCH - Federated Query ID: " + federatedQueryID);
		System.out.println("------------------------------------------------------------------");
		System.out.println(incidentSearchRequest);
		System.out.println("------------------------------------------------------------------");

		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
		        "/sampleResponses/incidentSearch/Incident_EntityMergeResultMessage.xml"));
	}

}
