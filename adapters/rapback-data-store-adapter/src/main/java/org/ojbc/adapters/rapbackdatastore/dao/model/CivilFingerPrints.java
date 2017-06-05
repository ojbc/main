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

public class CivilFingerPrints implements Serializable{

	private static final long serialVersionUID = 4995428240327016734L;
	private Long id; //Finger prints ID;
	private String transactionNumber;
	private byte[] fingerPrintsFile; 
	private FingerPrintsType fingerPrintsType; 
	private DateTime timestamp;
	
	public CivilFingerPrints(){
		super();
	}
	
	public CivilFingerPrints(String transactionNumber){
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

	public byte[] getFingerPrintsFile() {
		return fingerPrintsFile;
	}

	public void setFingerPrintsFile(byte[] fingerPrintsFile) {
		this.fingerPrintsFile = fingerPrintsFile;
	}

	public FingerPrintsType getFingerPrintsType() {
		return fingerPrintsType;
	}

	public void setFingerPrintsType(FingerPrintsType fingerPrintsType) {
		this.fingerPrintsType = fingerPrintsType;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

}
