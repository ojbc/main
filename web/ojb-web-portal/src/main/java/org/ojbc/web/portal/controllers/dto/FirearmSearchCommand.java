package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import org.ojbc.web.model.firearm.search.FirearmSearchRequest;

public class FirearmSearchCommand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private FirearmSearchRequest advanceSearch = new FirearmSearchRequest();

	public FirearmSearchRequest getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(FirearmSearchRequest advanceSearch) {
		this.advanceSearch = advanceSearch;
	}
	
}
