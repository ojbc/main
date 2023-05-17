package org.ojbc.booking.common.dao.model;

import java.util.ArrayList;
import java.util.List;


public class Arrest {
	
	private Integer id;
	
	private String arrestAgency;
	
	private String arrestUniqueIdentifier;
	
	private Location location;
	
	private List<Charge> chargeList = new ArrayList<Charge>();

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArrestAgency() {
		return arrestAgency;
	}

	public void setArrestAgency(String arrestAgency) {
		this.arrestAgency = arrestAgency;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Charge> getChargeList() {
		return chargeList;
	}

	public void setChargeList(List<Charge> chargeList) {
		this.chargeList = chargeList;
	}

	@Override
	public String toString() {
		return "Arrest [id=" + id + ", arrestAgency=" + arrestAgency
				+ ", location=" + location + ", chargeList=" + chargeList + "]";
	}

	public String getArrestUniqueIdentifier() {
		return arrestUniqueIdentifier;
	}

	public void setArrestUniqueIdentifier(String arrestUniqueIdentifier) {
		this.arrestUniqueIdentifier = arrestUniqueIdentifier;
	}
	
}
