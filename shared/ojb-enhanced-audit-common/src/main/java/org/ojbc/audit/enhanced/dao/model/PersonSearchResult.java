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
package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PersonSearchResult {

	private Integer personSearchRequestId;
	private Integer systemSearchResultID;
	private String systemSearchResultURI;
	private String systemName;
	private Boolean searchResultsErrorIndicator;
	private Integer personSearchResultsId;
	private String searchResultsErrorText;
	private Boolean searchResultsTimeoutIndicator;
	private Boolean searchResultsAccessDeniedIndicator;
	private String searchResultsAccessDeniedText;
	private Integer searchResultsCount;
	
	public Integer getPersonSearchRequestId() {
		return personSearchRequestId;
	}
	public void setPersonSearchRequestId(Integer personSearchRequestId) {
		this.personSearchRequestId = personSearchRequestId;
	}
	public Boolean getSearchResultsErrorIndicator() {
		return searchResultsErrorIndicator;
	}
	public void setSearchResultsErrorIndicator(Boolean searchResultsErrorIndicator) {
		this.searchResultsErrorIndicator = searchResultsErrorIndicator;
	}
	public Integer getPersonSearchResultsId() {
		return personSearchResultsId;
	}
	public void setPersonSearchResultsId(Integer personSearchResultsId) {
		this.personSearchResultsId = personSearchResultsId;
	}
	public String getSearchResultsErrorText() {
		return searchResultsErrorText;
	}
	public void setSearchResultsErrorText(String searchResultsErrorText) {
		this.searchResultsErrorText = searchResultsErrorText;
	}
	public Boolean getSearchResultsTimeoutIndicator() {
		return searchResultsTimeoutIndicator;
	}
	public void setSearchResultsTimeoutIndicator(
			Boolean searchResultsTimeoutIndicator) {
		this.searchResultsTimeoutIndicator = searchResultsTimeoutIndicator;
	}
	public Integer getSearchResultsCount() {
		return searchResultsCount;
	}
	public void setSearchResultsCount(Integer searchResultsCount) {
		this.searchResultsCount = searchResultsCount;
	}
	public Integer getSystemSearchResultID() {
		return systemSearchResultID;
	}
	public void setSystemSearchResultID(Integer systemSearchResultID) {
		this.systemSearchResultID = systemSearchResultID;
	}
	public String getSystemSearchResultURI() {
		return systemSearchResultURI;
	}
	public void setSystemSearchResultURI(String systemSearchResultURI) {
		this.systemSearchResultURI = systemSearchResultURI;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public Boolean getSearchResultsAccessDeniedIndicator() {
		return searchResultsAccessDeniedIndicator;
	}
	public void setSearchResultsAccessDeniedIndicator(
			Boolean searchResultsAccessDeniedIndicator) {
		this.searchResultsAccessDeniedIndicator = searchResultsAccessDeniedIndicator;
	}
	public String getSearchResultsAccessDeniedText() {
		return searchResultsAccessDeniedText;
	}
	public void setSearchResultsAccessDeniedText(
			String searchResultsAccessDeniedText) {
		this.searchResultsAccessDeniedText = searchResultsAccessDeniedText;
	}

}
