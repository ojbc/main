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

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ojbc.util.rest.jackson.LocalDateDeserializer;
import org.ojbc.util.rest.jackson.LocalDateSerializer;
import org.ojbc.util.rest.jackson.LocalDateTimeDeserializer;
import org.ojbc.util.rest.jackson.LocalDateTimeSerializer;

public class CannabisLicensingQueryResponse {

	private String messageId;
	private String licenseNumber;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate expirationDate;
	
	private Boolean queryResultsErrorIndicator;
	private Integer queryRequestId;
	private String systemName;
	private Boolean queryResultsAccessDeniedIndicator;
	private String queryResultsErrorText;
	private Boolean queryResultsTimeoutIndicator;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;
	
	public Integer getQueryRequestId() {
		return queryRequestId;
	}
	public void setQueryRequestId(Integer queryRequestId) {
		this.queryRequestId = queryRequestId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public Boolean getQueryResultsAccessDeniedIndicator() {
		return queryResultsAccessDeniedIndicator;
	}
	public void setQueryResultsAccessDeniedIndicator(
			Boolean queryResultsAccessDeniedIndicator) {
		this.queryResultsAccessDeniedIndicator = queryResultsAccessDeniedIndicator;
	}
	public String getQueryResultsErrorText() {
		return queryResultsErrorText;
	}
	public void setQueryResultsErrorText(String queryResultsErrorText) {
		this.queryResultsErrorText = queryResultsErrorText;
	}
	public Boolean getQueryResultsTimeoutIndicator() {
		return queryResultsTimeoutIndicator;
	}
	public void setQueryResultsTimeoutIndicator(Boolean queryResultsTimeoutIndicator) {
		this.queryResultsTimeoutIndicator = queryResultsTimeoutIndicator;
	}
	public Boolean getQueryResultsErrorIndicator() {
		return queryResultsErrorIndicator;
	}
	public void setQueryResultsErrorIndicator(Boolean queryResultsErrorIndicator) {
		this.queryResultsErrorIndicator = queryResultsErrorIndicator;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}	
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}


}
