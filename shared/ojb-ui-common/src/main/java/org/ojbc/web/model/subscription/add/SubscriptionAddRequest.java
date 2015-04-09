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
package org.ojbc.web.model.subscription.add;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SubscriptionAddRequest implements Serializable {
					
    private static final long serialVersionUID = 4793073362467448668L;

    private String subscriptionType;
	
	private String stateId;	
	
	private String systemId;
	
	private String fullName;
	
	//note f/l name only used for incident(not arrest)
	private String firstName;	
	private String lastName;
	
	private Date dateOfBirth;
	
	private Date subscriptionStartDate;
	
	private Date subscriptionEndDate;
	
	private List<String> emailList = new ArrayList<String>();

	
	
	public String getSubscriptionType() {
		return subscriptionType;
	}

	public String getStateId() {
		return stateId;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public List<String> getEmailList() {
		return emailList;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}

	@Override
	public String toString() {
		return "SubscriptionAddRequest [subscriptionType=" + subscriptionType
				+ ", stateId=" + stateId + ", systemId=" + systemId
				+ ", fullName=" + fullName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
				+ ", subscriptionStartDate=" + subscriptionStartDate
				+ ", subscriptionEndDate=" + subscriptionEndDate
				+ ", emailList=" + emailList + "]";
	}

	
}


