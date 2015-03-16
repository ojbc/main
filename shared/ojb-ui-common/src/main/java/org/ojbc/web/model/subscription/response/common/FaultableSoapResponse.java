package org.ojbc.web.model.subscription.response.common;

import org.w3c.dom.Document;

/**
 * TODO remove soap env string param if locations using it only really need the soap body
 */
public class FaultableSoapResponse {
	
	private Exception exception;
	
	private String soapResponse;
	
	private Document soapBodyDoc;
	
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

}

