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

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FederalRapbackRenewalNotification {

	private String ucn; 
	private String personFirstName; 
	private String personMiddleName; 
	private String personLastName; 
	private LocalDate personDob; 
	private String recordControllingAgency; 
	private LocalDate rapbackExpirationDate; 
	private String stateSubscriptionId; 
	private String transactionStatusText; 
	private String sid; 
	private String pathToNotificationFile; 
	private LocalDateTime notificationRecievedTimestamp;
	
	public String getUcn() {
		return ucn;
	}
	public void setUcn(String ucn) {
		this.ucn = ucn;
	}
	public String getPersonFirstName() {
		return personFirstName;
	}
	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}
	public String getPersonMiddleName() {
		return personMiddleName;
	}
	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}
	public String getPersonLastName() {
		return personLastName;
	}
	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}
	public LocalDate getPersonDob() {
		return personDob;
	}
	public void setPersonDob(LocalDate personDob) {
		this.personDob = personDob;
	}
	public String getRecordControllingAgency() {
		return recordControllingAgency;
	}
	public void setRecordControllingAgency(String recordControllingAgency) {
		this.recordControllingAgency = recordControllingAgency;
	}
	public LocalDate getRapbackExpirationDate() {
		return rapbackExpirationDate;
	}
	public void setRapbackExpirationDate(LocalDate rapbackExpirationDate) {
		this.rapbackExpirationDate = rapbackExpirationDate;
	}
	public String getStateSubscriptionId() {
		return stateSubscriptionId;
	}
	public void setStateSubscriptionId(String stateSubscriptionId) {
		this.stateSubscriptionId = stateSubscriptionId;
	}
	public String getTransactionStatusText() {
		return transactionStatusText;
	}
	public void setTransactionStatusText(String transactionStatusText) {
		this.transactionStatusText = transactionStatusText;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getPathToNotificationFile() {
		return pathToNotificationFile;
	}
	public void setPathToNotificationFile(String pathToNotificationFile) {
		this.pathToNotificationFile = pathToNotificationFile;
	}
	public LocalDateTime getNotificationRecievedTimestamp() {
		return notificationRecievedTimestamp;
	}
	public void setNotificationRecievedTimestamp(
			LocalDateTime notificationRecievedTimestamp) {
		this.notificationRecievedTimestamp = notificationRecievedTimestamp;
	}
	
}
