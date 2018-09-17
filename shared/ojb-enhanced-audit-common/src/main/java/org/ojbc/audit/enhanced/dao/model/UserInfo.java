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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UserInfo {

	private Integer userInfoId;
	
	private String userFirstName;
	private String identityProviderId;
	private String employerName;
	private String userEmailAddress;
	private String userLastName;
	private String employerSubunitName;
	private String federationId;
	private String employerOri;
	
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getIdentityProviderId() {
		return identityProviderId;
	}
	public void setIdentityProviderId(String identityProviderId) {
		this.identityProviderId = identityProviderId;
	}
	public String getEmployerName() {
		return employerName;
	}
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	public String getUserEmailAddress() {
		return userEmailAddress;
	}
	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getEmployerSubunitName() {
		return employerSubunitName;
	}
	public void setEmployerSubunitName(String employerSubunitName) {
		this.employerSubunitName = employerSubunitName;
	}
	public String getFederationId() {
		return federationId;
	}
	public void setFederationId(String federationId) {
		this.federationId = federationId;
	}
	public Integer getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(Integer userInfoId) {
		this.userInfoId = userInfoId;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getEmployerOri() {
		return employerOri;
	}
	public void setEmployerOri(String employerOri) {
		this.employerOri = employerOri;
	}
}
