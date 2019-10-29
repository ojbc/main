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

public class SubscriptionSearchResult {

	private Integer subscriptionSearchResultId;
	private Integer subscriptionSearchRequestId;
	private Boolean searchResultsErrorIndicator;
	private String searchResultsErrorText;
	private Boolean searchResultsTimeoutIndicator;
	private Boolean searchResultsAccessDeniedIndicator;
	private String searchResultsAccessDeniedText;
	private Integer searchResultsCount;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getSubscriptionSearchResultId() {
		return subscriptionSearchResultId;
	}

	public void setSubscriptionSearchResultId(Integer subscriptionSearchResultId) {
		this.subscriptionSearchResultId = subscriptionSearchResultId;
	}

	public Integer getSubscriptionSearchRequestId() {
		return subscriptionSearchRequestId;
	}

	public void setSubscriptionSearchRequestId(Integer subscriptionSearchRequestId) {
		this.subscriptionSearchRequestId = subscriptionSearchRequestId;
	}

	public Boolean getSearchResultsErrorIndicator() {
		return searchResultsErrorIndicator;
	}

	public void setSearchResultsErrorIndicator(Boolean searchResultsErrorIndicator) {
		this.searchResultsErrorIndicator = searchResultsErrorIndicator;
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

	public Integer getSearchResultsCount() {
		return searchResultsCount;
	}

	public void setSearchResultsCount(Integer searchResultsCount) {
		this.searchResultsCount = searchResultsCount;
	}

}
