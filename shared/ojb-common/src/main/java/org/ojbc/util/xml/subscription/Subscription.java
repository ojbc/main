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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
	
	//Email notification recipients
	private List<String> emailList = new ArrayList<String>();
	
	//Category
	private String subscriptionPurpose;
	
	//Agency case number
	private String caseId;

	private String systemName;

	private String topic;

	//If set, the value provided will be used other wise it is autogenerated
	private String subscriptionQualificationID;
	
	private Boolean federalRapSheetDisclosureIndicator;
	
	private String federalRapSheetDisclosureAttentionDesignationText;
	
	private List<String> federalTriggeringEventCode;
	
	private String subscriberOri; 
	
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

	public String getSubscriptionQualificationID() {
		return subscriptionQualificationID;
	}

	public void setSubscriptionQualificationID(String subscriptionQualificationID) {
		this.subscriptionQualificationID = subscriptionQualificationID;
	}

	public Boolean getFederalRapSheetDisclosureIndicator() {
		return federalRapSheetDisclosureIndicator;
	}

	public void setFederalRapSheetDisclosureIndicator(
			Boolean federalRapSheetDisclosureIndicator) {
		this.federalRapSheetDisclosureIndicator = federalRapSheetDisclosureIndicator;
	}

	public String getFederalRapSheetDisclosureAttentionDesignationText() {
		return federalRapSheetDisclosureAttentionDesignationText;
	}

	public void setFederalRapSheetDisclosureAttentionDesignationText(
			String federalRapSheetDisclosureAttentionDesignationText) {
		this.federalRapSheetDisclosureAttentionDesignationText = federalRapSheetDisclosureAttentionDesignationText;
	}

	public List<String> getFederalTriggeringEventCode() {
		return federalTriggeringEventCode;
	}

	public void setFederalTriggeringEventCode(
			List<String> federalTriggeringEventCode) {
		this.federalTriggeringEventCode = federalTriggeringEventCode;
	}
	
	@Override
	 public String toString() {
	  return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
	 }

	public String getSubscriberOri() {
		return subscriberOri;
	}

	public void setSubscriberOri(String subscriberOri) {
		this.subscriberOri = subscriberOri;
	}

}
