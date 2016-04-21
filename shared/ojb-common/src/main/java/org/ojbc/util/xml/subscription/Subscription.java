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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Subscription implements Serializable {
		
	private static final long serialVersionUID = 7990280609495398189L;

	
	private String stateId;	
	
	private String fbiId;
	
	private String systemId;
	
	private String fullName;
	
	//note f/l name only used for incident(not arrest)
	private String firstName;	
	
	private String lastName;
	
	private Date dateOfBirth;
	
	private Date subscriptionStartDate;
	
	private Date subscriptionEndDate;
	
	private List<String> emailList = new ArrayList<String>();
	
	private String subscriptionPurpose;
	
	private String caseId;

	private String personNamesJsonArray;
	
	private String systemName;

	private String topic;
	
	//This will create the submsg-ext:SubscriptionQualifierIdentification
	//By default it will be created
	private boolean includeSubscriptionQualificationNode = true;
	
	public String getStateId() {
		return stateId;
	}

	public String getFbiId() {
		return fbiId;
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

	public String getSubscriptionPurpose() {
		return subscriptionPurpose;
	}

	public String getCaseId() {
		return caseId;
	}

	public String getPersonNamesJsonArray() {
		return personNamesJsonArray;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
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

	public void setSubscriptionPurpose(String subscriptionPurpose) {
		this.subscriptionPurpose = subscriptionPurpose;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public void setPersonNamesJsonArray(String personNamesJsonArray) {
		this.personNamesJsonArray = personNamesJsonArray;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Subscription [stateId=" + stateId + ", fbiId=" + fbiId
				+ ", systemId=" + systemId + ", fullName=" + fullName
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfBirth=" + dateOfBirth + ", subscriptionStartDate="
				+ subscriptionStartDate + ", subscriptionEndDate="
				+ subscriptionEndDate + ", emailList=" + emailList
				+ ", subscriptionPurpose=" + subscriptionPurpose + ", caseId="
				+ caseId + ", personNamesJsonArray=" + personNamesJsonArray
				+ ", systemName=" + systemName + ", topic=" + topic + "]";
	}

	public boolean isIncludeSubscriptionQualificationNode() {
		return includeSubscriptionQualificationNode;
	}

	public void setIncludeSubscriptionQualificationNode(
			boolean includeSubscriptionQualificationNode) {
		this.includeSubscriptionQualificationNode = includeSubscriptionQualificationNode;
	}


	
}
