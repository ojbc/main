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
}
