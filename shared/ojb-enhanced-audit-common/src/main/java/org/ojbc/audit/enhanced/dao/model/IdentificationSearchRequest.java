package org.ojbc.audit.enhanced.dao.model;

import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IdentificationSearchRequest {

	private String firstName;
	private String lastName;
	private String reasonCode;
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
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
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
}
