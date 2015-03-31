package org.ojbc.web.portal.controllers.helpers;

import java.io.Serializable;

import org.ojbc.web.portal.controllers.PortalController.UserLogonInfo;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserSession implements Serializable{
	
	// note value shouldn't matter so long as we don't require backwards compatibility of 
	// modified UserSession objects that have been saved/loaded to/from disk or we don't 
	// have different compiled versions of UserSession loaded into memory at the same time.
	private static final long serialVersionUID = 1L;
	
	private UserLogonInfo userLogonInfo;
	
	private PersonSearchCommand mostRecentSearch;
	private PersonSearchType mostRecentSearchType;
	private String mostRecentSearchResult;
	private String savedMostRecentSearchResult;	

	private IncidentSearchCommand mostRecentIncidentSearch;
	private String mostRecentIncidentSearchResult;
	private String savedMostRecentIncidentSearchResult;
	
	private VehicleSearchCommand mostRecentVehicleSearch;
	private String mostRecentVehicleSearchResult;
	private String savedMostRecentVehicleSearchResult;
	
	private FirearmSearchCommand mostRecentFirearmSearch;
	private String mostRecentFirearmSearchResult;
	private String savedMostRecentFirearmSearchResult; 
	
	private String mostRecentSubscriptionSearchResult;
	private String savedMostRecentSubscriptionSearchResult;
	
	public void setMostRecentSearchResult(String mostRecentSearchResult) {
	    this.mostRecentSearchResult = mostRecentSearchResult;
    }
	
	public String getMostRecentSearchResult() {
	    return mostRecentSearchResult;
    }
	
	public PersonSearchType getMostRecentSearchType() {
		return mostRecentSearchType;
	}

	public void setMostRecentSearchType(PersonSearchType mostRecentSearchType) {
		this.mostRecentSearchType = mostRecentSearchType;
	}

	public PersonSearchCommand getMostRecentSearch() {
	    return mostRecentSearch;
    }

	public void setMostRecentSearch(PersonSearchCommand mostRecentSearch) {
	    this.mostRecentSearch = mostRecentSearch;
    }

	public String getSavedMostRecentSearchResult() {
		return savedMostRecentSearchResult;
	}

	public void setSavedMostRecentSearchResult(String savedMostRecentSearchResult) {
		this.savedMostRecentSearchResult = savedMostRecentSearchResult;
	}

	public IncidentSearchCommand getMostRecentIncidentSearch() {
		return mostRecentIncidentSearch;
	}

	public void setMostRecentIncidentSearch(
			IncidentSearchCommand mostRecentIncidentSearch) {
		this.mostRecentIncidentSearch = mostRecentIncidentSearch;
	}

	public String getMostRecentIncidentSearchResult() {
		return mostRecentIncidentSearchResult;
	}

	public void setMostRecentIncidentSearchResult(
			String mostRecentIncidentSearchResult) {
		this.mostRecentIncidentSearchResult = mostRecentIncidentSearchResult;
	}

	public String getSavedMostRecentIncidentSearchResult() {
		return savedMostRecentIncidentSearchResult;
	}

	public void setSavedMostRecentIncidentSearchResult(
			String savedMostRecentIncidentSearchResult) {
		this.savedMostRecentIncidentSearchResult = savedMostRecentIncidentSearchResult;
	}

	public VehicleSearchCommand getMostRecentVehicleSearch() {
		return mostRecentVehicleSearch;
	}

	public void setMostRecentVehicleSearch(
			VehicleSearchCommand mostRecentVehicleSearch) {
		this.mostRecentVehicleSearch = mostRecentVehicleSearch;
	}

	public String getMostRecentVehicleSearchResult() {
		return mostRecentVehicleSearchResult;
	}

	public void setMostRecentVehicleSearchResult(
			String mostRecentVehicleSearchResult) {
		this.mostRecentVehicleSearchResult = mostRecentVehicleSearchResult;
	}

	public String getSavedMostRecentVehicleSearchResult() {
		return savedMostRecentVehicleSearchResult;
	}

	public void setSavedMostRecentVehicleSearchResult(
			String savedMostRecentVehicleSearchResult) {
		this.savedMostRecentVehicleSearchResult = savedMostRecentVehicleSearchResult;
	}

	public FirearmSearchCommand getMostRecentFirearmSearch() {
		return mostRecentFirearmSearch;
	}

	public void setMostRecentFirearmSearch(
			FirearmSearchCommand mostRecentFirearmSearch) {
		this.mostRecentFirearmSearch = mostRecentFirearmSearch;
	}

	public String getMostRecentFirearmSearchResult() {
		return mostRecentFirearmSearchResult;
	}

	public void setMostRecentFirearmSearchResult(
			String mostRecentFirearmSearchResult) {
		this.mostRecentFirearmSearchResult = mostRecentFirearmSearchResult;
	}

	public String getSavedMostRecentFirearmSearchResult() {
		return savedMostRecentFirearmSearchResult;
	}

	public void setSavedMostRecentFirearmSearchResult(
			String savedMostRecentFirearmSearchResult) {
		this.savedMostRecentFirearmSearchResult = savedMostRecentFirearmSearchResult;
	}

	public String getMostRecentSubscriptionSearchResult() {
		return mostRecentSubscriptionSearchResult;
	}

	public String getSavedMostRecentSubscriptionSearchResult() {
		return savedMostRecentSubscriptionSearchResult;
	}

	public void setMostRecentSubscriptionSearchResult(
			String mostRecentSubscriptionSearchResult) {
		this.mostRecentSubscriptionSearchResult = mostRecentSubscriptionSearchResult;
	}

	public void setSavedMostRecentSubscriptionSearchResult(
			String savedMostRecentSubscriptionSearchResult) {
		this.savedMostRecentSubscriptionSearchResult = savedMostRecentSubscriptionSearchResult;
	}

	public UserLogonInfo getUserLogonInfo() {
		return userLogonInfo;
	}

	public void setUserLogonInfo(UserLogonInfo userLogonInfo) {
		this.userLogonInfo = userLogonInfo;
	}

}
