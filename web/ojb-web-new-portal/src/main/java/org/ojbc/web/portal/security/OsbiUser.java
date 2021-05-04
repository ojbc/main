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

package org.ojbc.web.portal.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ojbc.web.portal.audit.AuditUser;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class OsbiUser extends User implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1606369551188528192L;
	private List<String> oris = new ArrayList<>();
	private String federationId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String status;
	private String organizationName;
	private String agencyOri;
	private String employerOrganizationCategory; 

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 */
	public OsbiUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities, 
			List<String> oris, AuditUser auditUser) {
		super(username, password, true, true, true, true, authorities);
		this.setOris(oris);
		this.setEmailAddress(auditUser.getEmailAddress());
		this.setFederationId(auditUser.getFederationId());
		this.setAgencyOri(auditUser.getAgencyOri());
		this.setFirstName(auditUser.getFirstName());
		this.setLastName(auditUser.getLastName());
		this.setStatus(auditUser.getStatus());
		this.setOrganizationName(auditUser.getOrganizationName());
	}

	public OsbiUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
	}
	
	// ~ Methods
	// ========================================================================================================

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with the
	 * same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username, representing
	 * the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof OsbiUser) {
			return super.equals(rhs);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" ");
		sb.append("oris: ").append(oris).append("; ");
		sb.append("email: ").append(emailAddress).append("; ");
		sb.append("agencyOri: ").append(agencyOri).append("; ");
		sb.append("federationId: ").append(federationId).append("; ");
		sb.append("firstName: ").append(firstName).append("; ");
		sb.append("lastName: ").append(lastName).append("; ");
		sb.append("status: ").append(status).append("; ");
		sb.append("organizationName: ").append(organizationName).append("; ");
		return sb.toString();
	}


	public List<String> getOris() {
		return oris;
	}

	public void setOris(List<String> oris) {
		this.oris = oris;
	}

	public String getFederationId() {
		return federationId;
	}

	public void setFederationId(String federationId) {
		this.federationId = federationId;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getAgencyOri() {
		return agencyOri;
	}

	public void setAgencyOri(String agencyOri) {
		this.agencyOri = agencyOri;
	}

	public String getEmployerOrganizationCategory() {
		return employerOrganizationCategory;
	}

	public void setEmployerOrganizationCategory(String employerOrganizationCategory) {
		this.employerOrganizationCategory = employerOrganizationCategory;
	}

}
