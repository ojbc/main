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
package org.ojbc.adapters.rapbackdatastore.dao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;

public class CriminalInitialResults implements Serializable{

	private static final long serialVersionUID = -8697166964476446066L;
	private Long id; //Criminal Initial Results ID;
	private String transactionNumber;
	private byte[] searchResultFile; 
	private ResultSender resultsSender; 
	
	private DateTime timestamp;
	
	private Subject subject; 
	
	public CriminalInitialResults(){
		super();
	}
	
	public CriminalInitialResults(String transactionNumber){
		this();
		this.setTransactionNumber(transactionNumber); 
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public ResultSender getResultsSender() {
		return resultsSender;
	}

	public void setResultsSender(ResultSender resultsSender) {
		this.resultsSender = resultsSender;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public byte[] getSearchResultFile() {
		return searchResultFile;
	}

	public void setSearchResultFile(byte[] searchResultFile) {
		this.searchResultFile = searchResultFile;
	}

}
