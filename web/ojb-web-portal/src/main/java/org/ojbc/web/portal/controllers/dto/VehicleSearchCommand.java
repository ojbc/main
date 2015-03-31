package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;

public class VehicleSearchCommand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private VehicleSearchRequest advanceSearch;

	public VehicleSearchRequest getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(VehicleSearchRequest advanceSearch) {
		this.advanceSearch = advanceSearch;
	}
	
}
