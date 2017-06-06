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

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

public class PersonFilterCommand {

	private Integer filterAgeRangeStart;
	private Integer filterAgeRangeEnd;

	private String filterPersonRaceCode;
	private String filterPersonEyeColor;
	private String filterPersonHairColor;

	private Integer filterHeightTolerance;
	private Integer filterHeightInFeet;
	private Integer filterHeightInInches;

	private Integer filterWeight;
	private Integer filterWeightTolerance;

	private String filterDOBStart;
	private String filterDOBEnd;
	
	private Boolean filterFirearmCurrentRegOnly;

	public Integer getFilterAgeRangeStart() {
		return filterAgeRangeStart;
	}

	public void setFilterAgeRangeStart(Integer filterAgeRangeStart) {
		this.filterAgeRangeStart = filterAgeRangeStart;
	}

	public Integer getFilterAgeRangeEnd() {
		return filterAgeRangeEnd;
	}

	public void setFilterAgeRangeEnd(Integer filterAgeRangeEnd) {
		this.filterAgeRangeEnd = filterAgeRangeEnd;
	}

	public String getFilterPersonRaceCode() {
		return filterPersonRaceCode;
	}

	public void setFilterPersonRaceCode(String filterPersonRaceCode) {
		this.filterPersonRaceCode = filterPersonRaceCode;
	}

	public String getFilterPersonEyeColor() {
		return filterPersonEyeColor;
	}

	public void setFilterPersonEyeColor(String filterPersonEyeColor) {
		this.filterPersonEyeColor = filterPersonEyeColor;
	}

	public String getFilterPersonHairColor() {
		return filterPersonHairColor;
	}

	public void setFilterPersonHairColor(String filterPersonHairColor) {
		this.filterPersonHairColor = filterPersonHairColor;
	}

	public Integer getFilterHeightTolerance() {
		return filterHeightTolerance;
	}

	public void setFilterHeightTolerance(Integer filterHeightTolerance) {
		this.filterHeightTolerance = filterHeightTolerance;
	}

	public Integer getFilterHeightInFeet() {
		return filterHeightInFeet;
	}

	public void setFilterHeightInFeet(Integer filterHeightInFeet) {
		this.filterHeightInFeet = filterHeightInFeet;
	}

	public Integer getFilterHeightInInches() {
		return filterHeightInInches;
	}

	public void setFilterHeightInInches(Integer filterHeightInInches) {
		this.filterHeightInInches = filterHeightInInches;
	}

	public Integer getFilterWeight() {
		return filterWeight;
	}

	public void setFilterWeight(Integer filterWeight) {
		this.filterWeight = filterWeight;
	}

	public Integer getFilterWeightTolerance() {
		return filterWeightTolerance;
	}

	public void setFilterWeightTolerance(Integer filterWeightTolerance) {
		this.filterWeightTolerance = filterWeightTolerance;
	}

	public Boolean getFilterFirearmCurrentRegOnly() {
		return filterFirearmCurrentRegOnly;
	}

	public void setFilterFirearmCurrentRegOnly(Boolean filterFirearmCurrentRegOnly) {
		this.filterFirearmCurrentRegOnly = filterFirearmCurrentRegOnly;
	}

	public Map<String, Object> getParamsMap() {

		computeDOBRangeFromAgeRange();
		Map<String, Object> parmsMap = new HashMap<String, Object>();

		if (filterAgeRangeStart == null)
			parmsMap.put("filterAgeRangeStart", 0);
		else
			parmsMap.put("filterAgeRangeStart", filterAgeRangeStart);

		if (filterAgeRangeEnd == null)
			parmsMap.put("filterAgeRangeEnd", 0);
		else
			parmsMap.put("filterAgeRangeEnd", filterAgeRangeEnd);

		parmsMap.put("filterPersonRaceCode", filterPersonRaceCode);
		parmsMap.put("filterPersonEyeColor", filterPersonEyeColor);
		parmsMap.put("filterPersonHairColor", filterPersonHairColor);

		if (filterHeightInInches == null)
			parmsMap.put("filterHeightInInches", 0);
		else
			parmsMap.put("filterHeightInInches", filterHeightInInches);

		if (filterHeightInFeet == null)
			parmsMap.put("filterHeightInFeet", 0);
		else
			parmsMap.put("filterHeightInFeet", filterHeightInFeet);

		if (filterHeightTolerance == null)
			parmsMap.put("filterHeightTolerance", 0);
		else
			parmsMap.put("filterHeightTolerance", filterHeightTolerance);

		if (filterWeight == null)
			parmsMap.put("filterWeight", 0);
		else
			parmsMap.put("filterWeight", filterWeight);

		if (filterWeightTolerance == null)
			parmsMap.put("filterWeightTolerance", 0);
		else
			parmsMap.put("filterWeightTolerance", filterWeightTolerance);
		
		if (filterFirearmCurrentRegOnly == null)
			parmsMap.put("filterFirearmCurrentRegOnly", false);
		else
			parmsMap.put("filterFirearmCurrentRegOnly", filterFirearmCurrentRegOnly);

		parmsMap.put("filterDOBStart", filterDOBStart);
		parmsMap.put("filterDOBEnd", filterDOBEnd);

		return parmsMap;
	}

	private void computeDOBRangeFromAgeRange() {

		if (filterAgeRangeStart == null) {
			filterDOBStart = "";
			filterDOBEnd = "";
		} else {
			LocalDate today = new LocalDate();
			
			LocalDate computedStartDOB = today.minusYears(filterAgeRangeEnd).minusYears(1).plusDays(1);
			filterDOBStart = computedStartDOB.toString("yyyy-MM-dd");
			
			LocalDate computedEndDOB = today.minusYears(filterAgeRangeStart);
			filterDOBEnd = computedEndDOB.toString("yyyy-MM-dd"); 
		}
	}

}
