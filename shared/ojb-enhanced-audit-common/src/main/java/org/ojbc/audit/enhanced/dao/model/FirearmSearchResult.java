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

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class FirearmSearchResult {

	private Integer firearmSearchRequestId;
	private Integer systemSearchResultId;
	private String systemSearchResultURI;
	private String systemName;
	private String systemURI;
	private Boolean searchResultsErrorIndicator;
	private Integer firearmSearchResultsId;
	private String searchResultsErrorText;
	private Boolean searchResultsTimeoutIndicator;
	private Boolean searchResultsAccessDeniedIndicator;
	private String searchResultsAccessDeniedText;
	private Integer searchResultsCount;
	
	@JsonFormat(shape = Shape.STRING)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getFirearmSearchRequestId() {
		return firearmSearchRequestId;
	}

	public void setFirearmSearchRequestId(Integer firearmSearchRequestId) {
		this.firearmSearchRequestId = firearmSearchRequestId;
	}

	public Integer getSystemSearchResultId() {
		return systemSearchResultId;
	}

	public void setSystemSearchResultId(Integer systemSearchResultId) {
		this.systemSearchResultId = systemSearchResultId;
	}

	public String getSystemSearchResultURI() {
		return systemSearchResultURI;
	}

	public void setSystemSearchResultURI(String systemSearchResultURI) {
		this.systemSearchResultURI = systemSearchResultURI;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Boolean getSearchResultsErrorIndicator() {
		return searchResultsErrorIndicator;
	}

	public void setSearchResultsErrorIndicator(Boolean searchResultsErrorIndicator) {
		this.searchResultsErrorIndicator = searchResultsErrorIndicator;
	}

	public Integer getFirearmSearchResultsId() {
		return firearmSearchResultsId;
	}

	public void setFirearmSearchResultsId(Integer firearmSearchResultsId) {
		this.firearmSearchResultsId = firearmSearchResultsId;
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

	public String getSystemURI() {
		return systemURI;
	}

	public void setSystemURI(String systemURI) {
		this.systemURI = systemURI;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
