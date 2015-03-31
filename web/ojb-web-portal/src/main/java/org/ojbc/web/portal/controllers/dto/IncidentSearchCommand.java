package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import org.ojbc.web.model.incident.search.IncidentSearchRequest;

public class IncidentSearchCommand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private IncidentSearchRequest advanceSearch;

	public IncidentSearchRequest getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(IncidentSearchRequest advancedSearch) {
		this.advanceSearch = advancedSearch;
	}
	
}
