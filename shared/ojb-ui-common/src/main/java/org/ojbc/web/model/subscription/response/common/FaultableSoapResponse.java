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
package org.ojbc.web.model.subscription.response.common;

import org.w3c.dom.Document;

/**
 * TODO remove soap env string param if locations using it only really need the soap body
 */
public class FaultableSoapResponse {
	
	private Exception exception;
	private String soapResponse;
	private Document soapBodyDoc;
	private boolean success;
	private String errorMessage;
	
	public FaultableSoapResponse() {	
	}
		
	public FaultableSoapResponse(Exception exception, String soapResponse) {	
		this.exception = exception;
		this.soapResponse = soapResponse;
	}
		
	public FaultableSoapResponse(Exception exception, String soapResponse,
			Document soapBodyDoc) {
		
		super();
		this.exception = exception;
		this.soapResponse = soapResponse;
		this.soapBodyDoc = soapBodyDoc;
		this.success = (exception == null);
		
	}

	public Exception getException() {
		return exception;
	}

	public String getSoapResponse() {
		return soapResponse;
	}

	public Document getSoapBodyDoc() {
		return soapBodyDoc;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public void setSoapResponse(String soapResponse) {
		this.soapResponse = soapResponse;
	}

	public void setSoapBodyDoc(Document soapBodyDoc) {
		this.soapBodyDoc = soapBodyDoc;
	}

	@Override
	public String toString() {
		return "FaultableSoapResponse [exception=" + exception
				+ ", soapResponse=" + soapResponse + ", soapBodyDoc="
				+ soapBodyDoc + "]";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

