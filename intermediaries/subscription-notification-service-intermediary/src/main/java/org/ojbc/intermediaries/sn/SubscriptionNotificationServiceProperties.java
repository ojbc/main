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
package org.ojbc.intermediaries.sn;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "publishSubscribe")
public class SubscriptionNotificationServiceProperties {

	private String dbAuditLog="false";
	private String dbEnhancedAuditLog="false";
	private List<String> authorizedORIList;
	private String portNumber; 

	public String getDbAuditLog() {
		return dbAuditLog;
	}
	
	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	}
	
	public List<String> getAuthorizedORIList() {
		return authorizedORIList;
	}

	public void setAuthorizedORIList(List<String> authorizedORIList) {
		this.authorizedORIList = authorizedORIList;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getDbEnhancedAuditLog() {
		return dbEnhancedAuditLog;
	}

	public void setDbEnhancedAuditLog(String dbEnhancedAuditLog) {
		this.dbEnhancedAuditLog = dbEnhancedAuditLog;
	}

}