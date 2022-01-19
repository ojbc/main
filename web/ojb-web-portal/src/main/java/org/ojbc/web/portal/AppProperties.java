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
package org.ojbc.web.portal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("portal")
public class AppProperties {

	private String restServiceBaseUrl = "http://localhost:9898";
	private String externalTemplatesFolder ;

	private Boolean allowQueriesWithoutSAMLToken = true; 
	private Boolean inactivityTimeout=true;
	private Integer inactivityTimeoutInSeconds = 1800; 
	private Integer arrestSearchDateRange = 30;
	
	private String mailSenderHost; 
	private Integer mailSenderPort; 
	private String mailSenderTransportProtocol; 
	private String mailSenderSmtpAuth; 
	private String mailSenderSmtpStarttlesEnable; 
	private String mailSenderDebug; 
	
	private Integer ajpPort = 9090; 
	private Boolean ajpEnabled = true; 
	private String signOutUrl = "/portal/defaultLogout"; 
	
	private String footerText="Copyright &copy; 2021 OJBC. All rights reserved.";
	private String bannerPath="/images/banner/Banner.png";
	private String bannerInitial="OJBC";
	private String bannerFullname="Federated Query";

	/*
	 * demo user Saml attributes
	 */
	
	public AppProperties() {
		super();
	}

	public String getRestServiceBaseUrl() {
		return restServiceBaseUrl;
	}

	public void setRestServiceBaseUrl(String restServiceBaseUrl) {
		this.restServiceBaseUrl = restServiceBaseUrl;
	}

	public Boolean getAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}

	public void setAllowQueriesWithoutSAMLToken(Boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

	public Boolean getAjpEnabled() {
		return ajpEnabled;
	}

	public void setAjpEnabled(Boolean ajpEnabled) {
		this.ajpEnabled = ajpEnabled;
	}

	public Integer getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(Integer ajpPort) {
		this.ajpPort = ajpPort;
	}

	public String getExternalTemplatesFolder() {
		return externalTemplatesFolder;
	}

	public void setExternalTemplatesFolder(String externalTemplatesFolder) {
		this.externalTemplatesFolder = externalTemplatesFolder;
	}

	public String getSignOutUrl() {
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}

	public Boolean getInactivityTimeout() {
		return inactivityTimeout;
	}

	public void setInactivityTimeout(Boolean inactivityTimeout) {
		this.inactivityTimeout = inactivityTimeout;
	}

	public Integer getInactivityTimeoutInSeconds() {
		return inactivityTimeoutInSeconds;
	}

	public void setInactivityTimeoutInSeconds(Integer inactivityTimeoutInSeconds) {
		this.inactivityTimeoutInSeconds = inactivityTimeoutInSeconds;
	}

	public Integer getArrestSearchDateRange() {
		return arrestSearchDateRange;
	}

	public void setArrestSearchDateRange(Integer arrestSearchDateRange) {
		this.arrestSearchDateRange = arrestSearchDateRange;
	}

	public String getMailSenderHost() {
		return mailSenderHost;
	}

	public void setMailSenderHost(String mailSenderHost) {
		this.mailSenderHost = mailSenderHost;
	}

	public Integer getMailSenderPort() {
		return mailSenderPort;
	}

	public void setMailSenderPort(Integer mailSenderPort) {
		this.mailSenderPort = mailSenderPort;
	}

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

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public String getBannerPath() {
		return bannerPath;
	}

	public void setBannerPath(String bannerPath) {
		this.bannerPath = bannerPath;
	}

	public String getBannerInitial() {
		return bannerInitial;
	}

	public void setBannerInitial(String bannerInitial) {
		this.bannerInitial = bannerInitial;
	}

	public String getBannerFullname() {
		return bannerFullname;
	}

	public void setBannerFullname(String bannerFullname) {
		this.bannerFullname = bannerFullname;
	}

}