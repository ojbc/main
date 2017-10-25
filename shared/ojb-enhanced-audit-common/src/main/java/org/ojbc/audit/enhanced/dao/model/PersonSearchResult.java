package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PersonSearchResult {

	private Integer personSearchRequestId;
	private Integer systemSearchResultID;
	private String systemSearchResultURI;
	private Boolean searchResultsErrorIndicator;
	private Integer personSearchResultsId;
	private String searchResultsErrorText;
	private Boolean searchResultsTimeoutIndicator;
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

}
