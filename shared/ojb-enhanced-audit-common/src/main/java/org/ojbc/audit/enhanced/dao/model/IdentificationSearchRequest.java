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
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IdentificationSearchRequest {

	private String firstName;
	private String lastName;
	private List<String> reasonCode;
	private String identificationResultsStatus;
	private String otn;
	private Integer identificationSearchRequestId;
	private Integer userInfoId;
	private String messageId;
	private LocalDate reportedToDate;
	private LocalDate reportedFromDate;
	
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
	public String getOtn() {
		return otn;
	}
	public void setOtn(String otn) {
		this.otn = otn;
	}
	public Integer getIdentificationSearchRequestId() {
		return identificationSearchRequestId;
	}
	public void setIdentificationSearchRequestId(
			Integer identificationSearchRequestId) {
		this.identificationSearchRequestId = identificationSearchRequestId;
	}
	public Integer getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(Integer userInfoId) {
		this.userInfoId = userInfoId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public LocalDate getReportedToDate() {
		return reportedToDate;
	}
	public void setReportedToDate(LocalDate reportedToDate) {
		this.reportedToDate = reportedToDate;
	}
	public LocalDate getReportedFromDate() {
		return reportedFromDate;
	}
	public void setReportedFromDate(LocalDate reportedFromDate) {
		this.reportedFromDate = reportedFromDate;
	}
	public String getIdentificationResultsStatus() {
		return identificationResultsStatus;
	}
	public void setIdentificationResultsStatus(String identificationResultsStatus) {
		this.identificationResultsStatus = identificationResultsStatus;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public List<String> getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(List<String> reasonCode) {
		this.reasonCode = reasonCode;
	}
}
