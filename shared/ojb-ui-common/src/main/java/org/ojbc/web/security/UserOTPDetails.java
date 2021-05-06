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
package org.ojbc.web.security;

import java.time.LocalDateTime;

public class UserOTPDetails {

	private String oneTimePassword;
	private LocalDateTime expirationTimestamp;
	private String emailAddress;
	private boolean isUserAuthenticated;
	
	public boolean isUserAuthenticated() {
		return isUserAuthenticated;
	}
	public void setUserAuthenticated(boolean isUserAuthenticated) {
		this.isUserAuthenticated = isUserAuthenticated;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getOneTimePassword() {
		return oneTimePassword;
	}
	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	public LocalDateTime getExpirationTimestamp() {
		return expirationTimestamp;
	}
	public void setExpirationTimestamp(LocalDateTime expirationTimestamp) {
		this.expirationTimestamp = expirationTimestamp;
	}
	@Override
	public String toString() {
		return "UserOTPDetails [oneTimePassword=" + oneTimePassword
				+ ", expirationTimestamp=" + expirationTimestamp
				+ ", emailAddress=" + emailAddress + ", isUserAuthenticated="
				+ isUserAuthenticated + "]";
	}

	
}
