
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
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
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

	public void auditPrintResults(PrintResults printResults) {
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/printResults", printResults, PrintResults.class);
	}

	public void auditUserAcknowledgement(UserAcknowledgement userAcknowledgement) {
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userAcknowledgement", 
				userAcknowledgement, UserAcknowledgement.class);
	}
	
	public void auditUserLogin(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setUserEmailAddress(emailAddress);
		userInfo.setEmployerName(employerName);
		userInfo.setEmployerSubunitName(employerSubunitName);
		userInfo.setFederationId(federationId);
		userInfo.setUserFirstName(firstName);
		userInfo.setIdentityProviderId(identityProviderId);
		userInfo.setUserLastName(lastName);
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogin", userInfo, UserInfo.class);
		
	}
	
	public void auditUserLogout(String federationId, String employerName, String employerSubunitName, String firstName, String lastName, String emailAddress, String identityProviderId) {
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setUserEmailAddress(emailAddress);
		userInfo.setEmployerName(employerName);
		userInfo.setEmployerSubunitName(employerSubunitName);
		userInfo.setFederationId(federationId);
		userInfo.setUserFirstName(firstName);
		userInfo.setIdentityProviderId(identityProviderId);
		userInfo.setUserLastName(lastName);
		
		restTemplate.postForObject(restServiceBaseUrl + "auditServer/audit/userLogout", userInfo, UserInfo.class);
		
	}

	public Log getLog() {
		return log;
	}
}
