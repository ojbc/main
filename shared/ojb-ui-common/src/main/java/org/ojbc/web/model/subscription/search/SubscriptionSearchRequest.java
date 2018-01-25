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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SubscriptionSearchRequest implements Serializable{
	private static final long serialVersionUID = 6272003361243548121L;
	private boolean  adminSearch;
	private String ownerOri; 
	private String ownerLastName; 
	private String ownerFirstName; 
	private Boolean active;
	private String subscriptionCategory;  
	private String sid; 

	public SubscriptionSearchRequest() {
		super();
	}

	public SubscriptionSearchRequest(boolean adminSearch) {
		this();
		this.adminSearch = adminSearch;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean isAdminSearch() {
		return adminSearch;
	}

	public void setAdminSearch(boolean adminSearch) {
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

	public String getSubscriptionCategory() {
		return subscriptionCategory;
	}

	public void setSubscriptionCategory(String subscriptionCategory) {
		this.subscriptionCategory = subscriptionCategory;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
