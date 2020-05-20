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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FirearmsQueryResponse {

	private Integer firearmsQueryResponseId;
	private Integer queryRequestId;
	private String systemName;
	private String messageId;
	private boolean queryResultsTimeoutIndicator;
	private boolean queryResultsErrorIndicator;
	private boolean queryResultsAccessDeniedIndicator;
	private String queryResultsErrorText;
	private String firstName;
	private String lastName;
	private String middleName;
	private String county;
	private String registrationNumber;
	private LocalDateTime timestamp;
	
	public Integer getFirearmsQueryResponseId() {
		return firearmsQueryResponseId;
	}
	public void setFirearmsQueryResponseId(Integer firearmsQueryResponseId) {
		this.firearmsQueryResponseId = firearmsQueryResponseId;
	}
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
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public boolean isQueryResultsTimeoutIndicator() {
		return queryResultsTimeoutIndicator;
	}
	public void setQueryResultsTimeoutIndicator(boolean queryResultsTimeoutIndicator) {
		this.queryResultsTimeoutIndicator = queryResultsTimeoutIndicator;
	}
	public boolean isQueryResultsErrorIndicator() {
		return queryResultsErrorIndicator;
	}
	public void setQueryResultsErrorIndicator(boolean queryResultsErrorIndicator) {
		this.queryResultsErrorIndicator = queryResultsErrorIndicator;
	}
	public String getQueryResultsErrorText() {
		return queryResultsErrorText;
	}
	public void setQueryResultsErrorText(String queryResultsErrorText) {
		this.queryResultsErrorText = queryResultsErrorText;
	}
	public boolean isQueryResultsAccessDeniedIndicator() {
		return queryResultsAccessDeniedIndicator;
	}
	public void setQueryResultsAccessDeniedIndicator(
			boolean queryResultsAccessDeniedIndicator) {
		this.queryResultsAccessDeniedIndicator = queryResultsAccessDeniedIndicator;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}	
	
}
