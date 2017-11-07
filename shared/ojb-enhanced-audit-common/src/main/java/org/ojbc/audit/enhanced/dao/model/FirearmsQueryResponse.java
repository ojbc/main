package org.ojbc.audit.enhanced.dao.model;

public class FirearmsQueryResponse {

	private Integer firearmsQueryResponseId;
	private Integer queryRequestId;
	private String systemName;
	private String messageId;
	private boolean queryResultsTimeoutIndicator;
	private boolean queryResultsErrorIndicator;
	private String queryResultsErrorText;
	
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
	
	
}
