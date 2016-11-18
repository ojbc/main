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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.rapbackdatastore.dao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.util.model.rapback.IdentificationTransactionState;

public class IdentificationTransaction implements Serializable{

	private static final long serialVersionUID = 1856706945606938607L;
	private String transactionNumber;
	private String otn; //PersonTrackingIdentidication
	private DateTime timestamp;
	private DateTime availableForSubscriptionStartDate;
	private String ownerOri; 
	private String ownerProgramOca;
	private String identificationCategory; 
	private Boolean archived; 

	private Subject subject;
	private IdentificationTransactionState currentState; 
	private Subscription subscription;
	private Boolean havingSubsequentResults; 

	public IdentificationTransaction(){
		super();
	}
	
	public IdentificationTransaction(String transactionNumber){
		this();
		this.setTransactionNumber(transactionNumber); 
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getOtn() {
		return otn;
	}

	public void setOtn(String otn) {
		this.otn = otn;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getOwnerOri() {
		return ownerOri;
	}

	public void setOwnerOri(String ownerOri) {
		this.ownerOri = ownerOri;
	}

	public String getOwnerProgramOca() {
		return ownerProgramOca;
	}

	public void setOwnerProgramOca(String ownerProgramOca) {
		this.ownerProgramOca = ownerProgramOca;
	}

	public String getIdentificationCategory() {
		return identificationCategory;
	}

	public void setIdentificationCategory(String identificationCategory) {
		this.identificationCategory = identificationCategory;
	}
	public IdentificationTransactionState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(IdentificationTransactionState currentState) {
		this.currentState = currentState;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Boolean getHavingSubsequentResults() {
		return havingSubsequentResults;
	}

	public void setHavingSubsequentResults(Boolean havingSubsequentResults) {
		this.havingSubsequentResults = havingSubsequentResults;
	}

	public DateTime getAvailableForSubscriptionStartDate() {
		return availableForSubscriptionStartDate;
	}

	public void setAvailableForSubscriptionStartDate(
			DateTime availableForSubscriptionStartDate) {
		this.availableForSubscriptionStartDate = availableForSubscriptionStartDate;
	}

}
