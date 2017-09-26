package org.ojbc.audit.enhanced.dao.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FederalRapbackSubscription {
	
	private Integer federalRapbackSubscriptionId;
	
	private String transactionControlReferenceIdentification;
	
	private String transactionCategoryCode;

	private String pathToResponseFile;
	
	private String pathToRequestFile;

	private LocalDateTime requestSentTimestamp;
	
	private LocalDateTime responseRecievedTimestamp;

	public Integer getFederalRapbackSubscriptionId() {
		return federalRapbackSubscriptionId;
	}

	public String getTransactionControlReferenceIdentification() {
		return transactionControlReferenceIdentification;
	}

	public void setTransactionControlReferenceIdentification(
			String transactionControlReferenceIdentification) {
		this.transactionControlReferenceIdentification = transactionControlReferenceIdentification;
	}

	public String getTransactionCategoryCode() {
		return transactionCategoryCode;
	}

	public void setTransactionCategoryCode(String transactionCategoryCode) {
		this.transactionCategoryCode = transactionCategoryCode;
	}

	public String getPathToResponseFile() {
		return pathToResponseFile;
	}

	public void setPathToResponseFile(String pathToResponseFile) {
		this.pathToResponseFile = pathToResponseFile;
	}
	
	public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getPathToRequestFile() {
		return pathToRequestFile;
	}

	public void setPathToRequestFile(String pathToRequestFile) {
		this.pathToRequestFile = pathToRequestFile;
	}

	public LocalDateTime getRequestSentTimestamp() {
		return requestSentTimestamp;
	}

	public void setRequestSentTimestamp(LocalDateTime requestSentTimestamp) {
		this.requestSentTimestamp = requestSentTimestamp;
	}

	public LocalDateTime getResponseRecievedTimestamp() {
		return responseRecievedTimestamp;
	}

	public void setResponseRecievedTimestamp(LocalDateTime responseRecievedTimestamp) {
		this.responseRecievedTimestamp = responseRecievedTimestamp;
	}

	public void setFederalRapbackSubscriptionId(Integer federalRapbackSubscriptionId) {
		this.federalRapbackSubscriptionId = federalRapbackSubscriptionId;
	}
	
}
