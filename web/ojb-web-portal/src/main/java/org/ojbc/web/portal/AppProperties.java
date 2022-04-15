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

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("portal")
public class AppProperties {
	public static final String STATE_LINK_ID = "stateGovLink";
    public static final String QUERY_LINK_ID = "queryLink";
	public static final String SUBSCRIPTIONS_LINK_ID = "subscriptionsLink";
	public static final String RAPBACK_LINK_ID = "rapbackLink";
	public static final String CRIMINAL_ID_LINK_ID = "criminalIdLink";
	private static final String ADMIN_LINK_ID = "adminLink";
	private static final String AUDIT_LINK_ID = "auditLink";
	public static final String HELP_LINK_ID = "helpLink";
	public static final String HELP_LINK_EXTERNAL_ID = "helpLinkExternal";
    public static final String PRIVACY_LINK_ID = "privacyPolicyLink";
    public static final String FAQ_LINK_ID = "faqLink";
    public static final String SUGGESTIONFORM_LINK_ID = "suggestionFormLink";
		
	public static final String STATE_LINK_TITLE = "State.gov";
    public static final String QUERY_LINK_TITLE = "Query";
	public static final String SUBSCRIPTION_LINK_TITLE = "Subscriptions";
	public static final String RAPBACK_LINK_TITLE = "Applicant Rap Back";
	public static final String CRIMINAL_ID_LINK_TITLE = "Criminal Identification";
	public static final String ADMIN_LINK_TITLE = "Admin";
	public static final String AUDIT_LINK_TITLE = "Audit";
	public static final String HELP_LINK_TITLE = "Help";
	public static final String PRIVACY_LINK_TITLE = "Privacy Policies";
	public static final String FAQ_LINK_TITLE = "FAQ";
	public static final String SUGGESTIONFORM_LINK_TITLE = "Suggestions/Report a Problem";
	

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
	private String bannerPath="/static/images/banner/Banner.png";
	private String bannerInitial="OJBC";
	private String bannerFullname="Federated Query";

	private String enhancedAuditServerBaseUrl = "https://localhost:8443/OJB/";
	
	private final Map<String, String> leftMenuLinkTitles = new HashMap<>();
	/*
	 * demo user Saml attributes
	 */
	
	public AppProperties() {
		super();
		leftMenuLinkTitles.put(STATE_LINK_ID, STATE_LINK_TITLE);
		leftMenuLinkTitles.put(QUERY_LINK_ID, QUERY_LINK_TITLE);
		leftMenuLinkTitles.put(SUBSCRIPTIONS_LINK_ID, SUBSCRIPTION_LINK_TITLE);
		leftMenuLinkTitles.put(RAPBACK_LINK_ID, RAPBACK_LINK_TITLE);
		leftMenuLinkTitles.put(CRIMINAL_ID_LINK_ID, CRIMINAL_ID_LINK_TITLE);
		leftMenuLinkTitles.put(ADMIN_LINK_ID, ADMIN_LINK_TITLE);
		leftMenuLinkTitles.put(AUDIT_LINK_ID, AUDIT_LINK_TITLE);
		leftMenuLinkTitles.put(HELP_LINK_ID, HELP_LINK_TITLE);
		leftMenuLinkTitles.put(HELP_LINK_EXTERNAL_ID, HELP_LINK_TITLE);
		leftMenuLinkTitles.put(PRIVACY_LINK_ID, PRIVACY_LINK_TITLE);
		leftMenuLinkTitles.put(FAQ_LINK_ID, FAQ_LINK_TITLE);
		leftMenuLinkTitles.put(SUGGESTIONFORM_LINK_ID, SUGGESTIONFORM_LINK_TITLE);
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

	public String getEnhancedAuditServerBaseUrl() {
		return enhancedAuditServerBaseUrl;
	}

	public void setEnhancedAuditServerBaseUrl(String enhancedAuditServerBaseUrl) {
		this.enhancedAuditServerBaseUrl = enhancedAuditServerBaseUrl;
	}

	public Map<String, String> getLeftMenuLinkTitles() {
		return leftMenuLinkTitles;
	}

}