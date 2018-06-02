
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

package org.ojbc.web.portal.rest.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestEnhancedAuditClient {

	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired(required=false)
	private RestTemplate restTemplate;
	
	@Value("${enhancedAuditServerBaseUrl:https://localhost:8443/OJB/}")
	private String restServiceBaseUrl;

	public void auditPrintResults(String description, String messageId,
			String systemName) {
		
		PrintResults printResults = new PrintResults();
		
		printResults.description = description;
		printResults.messageId = messageId;
		printResults.systemName = systemName;
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/printResults", printResults, PrintResults.class);
	}

	public void auditUserAcknowledgement(UserAcknowledgement userAcknowledgement) {
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userAcknowledgement", 
				userAcknowledgement, UserAcknowledgement.class);
	}
	
	public void auditUserLogin(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.userEmailAddress = emailAddress;
		userInfo.employerName = employerName;
		userInfo.employerSubunitName = employerSubunitName;
		userInfo.federationId = federationId;
		userInfo.userFirstName = firstName;
		userInfo.identityProviderId = identityProviderId;
		userInfo.userLastName = lastName;
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogin", userInfo, UserInfo.class);
		
	}
	
	public void auditUserLogout(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.userEmailAddress = emailAddress;
		userInfo.employerName = employerName;
		userInfo.employerSubunitName = employerSubunitName;
		userInfo.federationId = federationId;
		userInfo.userFirstName = firstName;
		userInfo.identityProviderId = identityProviderId;
		userInfo.userLastName = lastName;
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogout", userInfo, UserInfo.class);
		
	}

	//To avoid a dependency on the ojb-audit-common, add UserInfo as a private class
	private static class UserInfo {
	
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
		
		
	}
	
	//To avoid a dependency on the ojb-audit-common, add print results as a private class
	private static class PrintResults {

		private String messageId;
		private String systemName;
		private String description;
		private String printResultsId;
		
		public String getMessageId() {
			return messageId;
		}
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		public String getSystemName() {
			return systemName;
		}
		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPrintResultsId() {
			return printResultsId;
		}
		public void setPrintResultsId(String printResultsId) {
			this.printResultsId = printResultsId;
		}
		
	}


	public Log getLog() {
		return log;
	}
}
