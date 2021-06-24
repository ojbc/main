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
package org.ojbc.web.model.subscription.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SubscriptionSearchRequest implements Serializable{
	private static final long serialVersionUID = 6272003361243548121L;
	private Boolean adminSearch;
	private String ownerOri; 
	private String ownerLastName; 
	private String ownerFirstName; 
	private String ownerFederatedId; 
	private String subjectLastName; 
	private String subjectFirstName; 
	private List<String> status;
	private List<String> subscriptionCategories;  
	private String sid;
	private String ucn; 
	private String subscribingSystemIdentifier;

	public SubscriptionSearchRequest() {
		super();
		status = new ArrayList<>();
		subscriptionCategories = new ArrayList<>();
	}

	public SubscriptionSearchRequest(Boolean adminSearch) {
		this();
		this.adminSearch = adminSearch;
		status = Arrays.asList(SubscriptionStatus.ACTIVE.name());
		this.subscribingSystemIdentifier = "{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB";
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
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

	public String getUcn() {
		return ucn;
	}

	public void setUcn(String ucn) {
		this.ucn = ucn;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getSubscriptionCategories() {
		return subscriptionCategories;
	}

	public void setSubscriptionCategories(List<String> subscriptionCategories) {
		this.subscriptionCategories = subscriptionCategories;
	}

	public String getOwnerFederatedId() {
		return ownerFederatedId;
	}

	public void setOwnerFederatedId(String ownerFederatedId) {
		this.ownerFederatedId = ownerFederatedId;
	}

	public String getSubscribingSystemIdentifier() {
		return subscribingSystemIdentifier;
	}

	public void setSubscribingSystemIdentifier(String subscribingSystemIdentifier) {
		this.subscribingSystemIdentifier = subscribingSystemIdentifier;
	}
}
