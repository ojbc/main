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
