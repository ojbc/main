package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IdentificationSearchResult {

	private Integer identificationSearchRequestId;
	private Integer availableResults;
	private String messageId;
	
	public Integer getIdentificationSearchRequestId() {
		return identificationSearchRequestId;
	}


	public void setIdentificationSearchRequestId(
			Integer identificationSearchRequestId) {
		this.identificationSearchRequestId = identificationSearchRequestId;
	}


	public Integer getAvailableResults() {
		return availableResults;
	}


	public void setAvailableResults(Integer availableResults) {
		this.availableResults = availableResults;
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
