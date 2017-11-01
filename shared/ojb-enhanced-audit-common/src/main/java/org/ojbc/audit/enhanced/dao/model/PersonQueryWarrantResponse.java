package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PersonQueryWarrantResponse {

	private Integer queryRequestId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String queryResultsErrorText;
	private boolean queryResultsTimeoutIndicator;
	private boolean queryResultsErrorIndicator;
	private String fbiId;
	private String sid;
	private String systemName;
	private String messageId;
	
	public Integer getQueryRequestId() {
		return queryRequestId;
	}
	public void setQueryRequestId(Integer queryRequestId) {
		this.queryRequestId = queryRequestId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getQueryResultsErrorText() {
		return queryResultsErrorText;
	}
	public void setQueryResultsErrorText(String queryResultsErrorText) {
		this.queryResultsErrorText = queryResultsErrorText;
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
	public String getFbiId() {
		return fbiId;
	}
	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
