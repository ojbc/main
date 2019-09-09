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

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ojbc.util.rest.jackson.LocalDateTimeDeserializer;
import org.ojbc.util.rest.jackson.LocalDateTimeSerializer;

public class FederalRapbackSubscription implements Serializable {
	
	private static final long serialVersionUID = 444000465826777573L;

	private Integer federalRapbackSubscriptionId;
	
	private String transactionControlReferenceIdentification;
	
	private String transactionCategoryCodeRequest;
	
	private String transactionCategoryCodeResponse;
	
	private String pathToResponseFile;
	
	private String pathToRequestFile;
	
	private String stateSubscriptionId;
	
	private String fbiSubscriptionId;
	
	private String sid;
	
	private String subscriptonCategoryCode;
	
	//In case of error, the error message will be in this field
	private String transactionStatusText;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime requestSentTimestamp;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

	public String getStateSubscriptionId() {
		return stateSubscriptionId;
	}

	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTransactionStatusText() {
		return transactionStatusText;
	}

	public void setTransactionStatusText(String transactionStatusText) {
		this.transactionStatusText = transactionStatusText;
	}

	public String getSubscriptonCategoryCode() {
		return subscriptonCategoryCode;
	}

	public void setSubscriptonCategoryCode(String subscriptonCategoryCode) {
		this.subscriptonCategoryCode = subscriptonCategoryCode;
	}

	public String getFbiSubscriptionId() {
		return fbiSubscriptionId;
	}

	public void setFbiSubscriptionId(String fbiSubscriptionId) {
		this.fbiSubscriptionId = fbiSubscriptionId;
	}

	public String getTransactionCategoryCodeRequest() {
		return transactionCategoryCodeRequest;
	}

	public void setTransactionCategoryCodeRequest(
			String transactionCategoryCodeRequest) {
		this.transactionCategoryCodeRequest = transactionCategoryCodeRequest;
	}

	public String getTransactionCategoryCodeResponse() {
		return transactionCategoryCodeResponse;
	}

	public void setTransactionCategoryCodeResponse(
			String transactionCategoryCodeResponse) {
		this.transactionCategoryCodeResponse = transactionCategoryCodeResponse;
	}
	
}
