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
package org.ojbc.bundles.utilities.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="email-rest-utility")
public class AppProperties {
	private String host; 
	private Integer port; 
	private String mailSenderTransportProtocol = "smtp"; 
	private String mailSenderSmtpSSlProtocol="TLSv1.2"; 
	private String mailSenderSmtpAuth = "false"; 
	private String mailSenderSmtpStarttlesEnable = "true"; 
	private String mailSenderDebug="true"; 
	private String emailRestEndpoint="/emailServer/";
	
	public String getMailSenderTransportProtocol() {
		return mailSenderTransportProtocol;
	}

	public void setMailSenderTransportProtocol(String mailSenderTransportProtocol) {
		this.mailSenderTransportProtocol = mailSenderTransportProtocol;
	}

	public String getMailSenderSmtpAuth() {
		return mailSenderSmtpAuth;
	}

	public void setMailSenderSmtpAuth(String mailSenderSmtpAuth) {
		this.mailSenderSmtpAuth = mailSenderSmtpAuth;
	}

	public String getMailSenderSmtpStarttlesEnable() {
		return mailSenderSmtpStarttlesEnable;
	}

	public void setMailSenderSmtpStarttlesEnable(String mailSenderSmtpStarttlesEnable) {
		this.mailSenderSmtpStarttlesEnable = mailSenderSmtpStarttlesEnable;
	}

	public String getMailSenderDebug() {
		return mailSenderDebug;
	}

	public void setMailSenderDebug(String mailSenderDebug) {
		this.mailSenderDebug = mailSenderDebug;
	}

	public String getMailSenderSmtpSSlProtocol() {
		return mailSenderSmtpSSlProtocol;
	}

	public void setMailSenderSmtpSSlProtocol(String mailSenderSmtpSSlProtocol) {
		this.mailSenderSmtpSSlProtocol = mailSenderSmtpSSlProtocol;
	}

	public String getEmailRestEndpoint() {
		return emailRestEndpoint;
	}

	public void setEmailRestEndpoint(String emailRestEndpoint) {
		this.emailRestEndpoint = emailRestEndpoint;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}