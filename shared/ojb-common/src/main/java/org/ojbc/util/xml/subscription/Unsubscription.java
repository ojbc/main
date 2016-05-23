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
package org.ojbc.util.xml.subscription;

import java.time.LocalDate;
import java.util.List;

public class Unsubscription {
		       
	private String subscriptionId;
	
	private String subscriptionQualifierIdentification;
	
	private String topic;
	
	private String reasonCode;	 
	
	private String firstName;
	
	private String lastName;

	private String sid;
	
	private LocalDate dateOfBirth;
	
	private List<String> emailAddresses;
	
	private String systemName;
	
	public Unsubscription(String subscriptionId, String topic, String reasonCode, String firstName, String lastName, String sid, LocalDate dateOfBirth) {
		this.subscriptionId = subscriptionId;
		this.topic = topic;
		this.reasonCode = reasonCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sid = sid;
		this.dateOfBirth = dateOfBirth;
	}

	public Unsubscription() {

	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public String getTopic() {
		return topic;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	@Override
	public String toString() {
		return "Unsubscription [subscriptionId=" + subscriptionId + ", topic="
				+ topic + ", reasonCode=" + reasonCode + "]";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<String> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(List<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSubscriptionQualifierIdentification() {
		return subscriptionQualifierIdentification;
	}

	public void setSubscriptionQualifierIdentification(
			String subscriptionQualifierIdentification) {
		this.subscriptionQualifierIdentification = subscriptionQualifierIdentification;
	}				

}
