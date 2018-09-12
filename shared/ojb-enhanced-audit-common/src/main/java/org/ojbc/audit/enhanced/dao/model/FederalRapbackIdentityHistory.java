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

public class FederalRapbackIdentityHistory {

	private String transactionCategoryCodeRequest;
	
	private String transactionCategoryCodeResponse;
	
	private LocalDateTime requestSentTimestamp;
	
	private LocalDateTime responseReceivedTimestamp;
	
	private String transactionType;

	private String transactionControlReferenceIdentification;
	
	private String pathToRequestFile;
	
	private String pathToResponseFile;
	
	private String fbiNotificationId;
	
	private String fbiSubscriptionId;
	
	private String ucn;

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

	public LocalDateTime getRequestSentTimestamp() {
		return requestSentTimestamp;
	}

	public void setRequestSentTimestamp(LocalDateTime requestSentTimestamp) {
		this.requestSentTimestamp = requestSentTimestamp;
	}

	public LocalDateTime getResponseReceivedTimestamp() {
		return responseReceivedTimestamp;
	}

	public void setResponseReceivedTimestamp(LocalDateTime responseReceivedTimestamp) {
		this.responseReceivedTimestamp = responseReceivedTimestamp;
	}


	public String getTransactionControlReferenceIdentification() {
		return transactionControlReferenceIdentification;
	}

	public void setTransactionControlReferenceIdentification(
			String transactionControlReferenceIdentification) {
		this.transactionControlReferenceIdentification = transactionControlReferenceIdentification;
	}

	public String getFbiNotificationId() {
		return fbiNotificationId;
	}

	public void setFbiNotificationId(String fbiNotificationId) {
		this.fbiNotificationId = fbiNotificationId;
	}

	public String getFbiSubscriptionId() {
		return fbiSubscriptionId;
	}

	public void setFbiSubscriptionId(String fbiSubscriptionId) {
		this.fbiSubscriptionId = fbiSubscriptionId;
	}

	public String getUcn() {
		return ucn;
	}

	public void setUcn(String ucn) {
		this.ucn = ucn;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPathToRequestFile() {
		return pathToRequestFile;
	}

	public void setPathToRequestFile(String pathToRequestFile) {
		this.pathToRequestFile = pathToRequestFile;
	}

	public String getPathToResponseFile() {
		return pathToResponseFile;
	}

	public void setPathToResponseFile(String pathToResponseFile) {
		this.pathToResponseFile = pathToResponseFile;
	} 
	

}
