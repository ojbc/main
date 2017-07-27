/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.portal.controllers.helpers.PersonSearchType;

public class PersonSearchCommand implements Serializable{
	
    private static final long serialVersionUID = 1148893855640045881L;
    
    @Valid
    private PersonSearchRequest advanceSearch = new PersonSearchRequest();
    private PersonSearchType searchType; 
	private Integer weightTolerance;
	private Integer heightTolerance;
	private Integer heightInFeet;
	private Integer heightInInches;

	@Pattern(regexp="[A-Za-z0-9* \"-]*")
	private String simpleSearch;
	private Integer ageRangeStart;
	private Integer ageRangeEnd;
	
	private PersonSearchRequest parsedPersonSearchRequest;

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
		this.simpleSearch = StringEscapeUtils.escapeHtml(simpleSearch);
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

	public PersonSearchRequest getParsedPersonSearchRequest() {
		return parsedPersonSearchRequest;
	}

	public void setParsedPersonSearchRequest(PersonSearchRequest parsedPersonSearchRequest) {
		this.parsedPersonSearchRequest = parsedPersonSearchRequest;
	}

	@Override
	public String toString() {
		return "PersonSearchCommand [advanceSearch=" + advanceSearch
				+ ", searchType=" + searchType + ", weightTolerance="
				+ weightTolerance + ", heightTolerance=" + heightTolerance
				+ ", heightInFeet=" + heightInFeet + ", heightInInches="
				+ heightInInches + ", simpleSearch=" + simpleSearch
				+ ", ageRangeStart=" + ageRangeStart + ", ageRangeEnd="
				+ ageRangeEnd + ", parsedPersonSearchRequest="
				+ parsedPersonSearchRequest + "]";
	}

}
