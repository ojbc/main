package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.helpers.PersonSearchType;

public class PersonSearchCommand implements Serializable{
	
    private static final long serialVersionUID = 1148893855640045881L;
    
    private PersonSearchRequest advanceSearch = new PersonSearchRequest();
    private PersonSearchType searchType; 
	private Integer weightTolerance;
	private Integer heightTolerance;
	private Integer heightInFeet;
	private Integer heightInInches;

	private String simpleSearch;
	private Integer ageRangeStart;
	private Integer ageRangeEnd;

	public PersonSearchRequest getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(PersonSearchRequest advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public String getSimpleSearch() {
		return simpleSearch;
	}

	public void setSimpleSearch(String simpleSearch) {
		this.simpleSearch = simpleSearch;
	}

	public void setWeightTolerance(Integer weightTolerance) {
		this.weightTolerance = weightTolerance;
	}

	public Integer getWeightTolerance() {
		return weightTolerance;
	}

	public void setHeightTolerance(Integer heightTolerance) {
		this.heightTolerance = heightTolerance;
	}

	public Integer getHeightTolerance() {
		return heightTolerance;
	}

	public void setHeightInFeet(Integer heightInFeet) {
		this.heightInFeet = heightInFeet;
	}

	public void setHeightInInches(Integer heightInInches) {
		this.heightInInches = heightInInches;
	}

	public Integer getHeightInFeet() {
		return heightInFeet;
	}

	public Integer getHeightInInches() {
		return heightInInches;
	}

	public void setAgeRangeStart(Integer ageRangeStart) {
		this.ageRangeStart = ageRangeStart;
	}

	public Integer getAgeRangeStart() {
		return ageRangeStart;
	}

	public void setAgeRangeEnd(Integer ageRangeEnd) {
		this.ageRangeEnd = ageRangeEnd;
	}

	public Integer getAgeRangeEnd() {
		return ageRangeEnd;
	}
	
	public boolean hasDOB() {
        PersonSearchRequest advanceSearch = this.getAdvanceSearch();
        return advanceSearch.getPersonDateOfBirth() != null || advanceSearch.getPersonDateOfBirthRangeEnd() != null
                || advanceSearch.getPersonDateOfBirthRangeStart() != null;
    }

    public boolean hasAgeRange() {
        return ageRangeStart != null || ageRangeEnd != null;
    }

    public PersonSearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(PersonSearchType searchType) {
        this.searchType = searchType;
    }

}
