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
package org.ojbc.util.sn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Parsed from the subscription search request message and be passed to the DAO method to 
 * run the search query.  
 */
public class SubscriptionSearchRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	private String ownerFedId;
	private Boolean adminSearch;
	private String ownerOri; 
	private String ownerLastName; 
	private String ownerFirstName; 
	private String ownerFederatedId; 
	private String subjectLastName; 
	private String subjectFirstName; 
	private Boolean active;
	private List<String> subscriptionCategories;  
	private String sid;
	private String ucn; 
	private Map<String, String> subjectIdentifiers;
	private boolean includeExpiredSubscriptions;
	private String subscribingSystemIdentifier;
	private Integer userInfoFk;
	private Integer subscriptionSearchRequestPk;
	private String messageId;
	
	public SubscriptionSearchRequest(){
		super();
		subscriptionCategories = new ArrayList<>();
		subjectIdentifiers = new HashMap<>();
	}

	public Map<String, String> getSubjectIdentifiers() {
		return subjectIdentifiers;
	}

	public void setSubjectIdentifiers(Map<String, String> subjectIdentifiers) {
		this.subjectIdentifiers = subjectIdentifiers;
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public String getOwnerFedId() {
		return ownerFedId;
	}

	public void setOwnerFedId(String ownerFedId) {
		this.ownerFedId = ownerFedId;
	}

	public Boolean getAdminSearch() {
		return adminSearch;
	}

	public void setAdminSearch(Boolean adminSearch) {
		this.adminSearch = adminSearch;
	}

	public String getOwnerOri() {
		return ownerOri;
	}

	public void setOwnerOri(String ownerOri) {
		this.ownerOri = ownerOri;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerFederatedId() {
		return ownerFederatedId;
	}

	public void setOwnerFederatedId(String ownerFederatedId) {
		this.ownerFederatedId = ownerFederatedId;
	}

	public String getSubjectLastName() {
		return subjectLastName;
	}

	public void setSubjectLastName(String subjectLastName) {
		this.subjectLastName = subjectLastName;
	}

	public String getSubjectFirstName() {
		return subjectFirstName;
	}

	public void setSubjectFirstName(String subjectFirstName) {
		this.subjectFirstName = subjectFirstName;
	}

	public List<String> getSubscriptionCategories() {
		return subscriptionCategories;
	}

	public void setSubscriptionCategories(List<String> subscriptionCategories) {
		this.subscriptionCategories = subscriptionCategories;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUcn() {
		return ucn;
	}

	public void setUcn(String ucn) {
		this.ucn = ucn;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public boolean isIncludeExpiredSubscriptions() {
		return includeExpiredSubscriptions;
	}

	public void setIncludeExpiredSubscriptions(boolean includeExpiredSubscriptions) {
		this.includeExpiredSubscriptions = includeExpiredSubscriptions;
	}

	public String getSubscribingSystemIdentifier() {
		return subscribingSystemIdentifier;
	}

	public void setSubscribingSystemIdentifier(String subscribingSystemIdentifier) {
		this.subscribingSystemIdentifier = subscribingSystemIdentifier;
	}

	public Integer getUserInfoFk() {
		return userInfoFk;
	}

	public void setUserInfoFk(Integer userInfoFk) {
		this.userInfoFk = userInfoFk;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Integer getSubscriptionSearchRequestPk() {
		return subscriptionSearchRequestPk;
	}

	public void setSubscriptionSearchRequestPk(Integer subscriptionSearchRequestPk) {
		this.subscriptionSearchRequestPk = subscriptionSearchRequestPk;
	}

}
