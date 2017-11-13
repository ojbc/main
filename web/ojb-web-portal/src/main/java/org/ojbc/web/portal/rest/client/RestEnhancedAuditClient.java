
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
	private String printResultsPostURI;

	public void auditPrintResults(String description, String messageId,
			String systemName) {
		
		PrintResults printResults = new PrintResults();
		
		printResults.description = description;
		printResults.messageId = messageId;
		printResults.systemName = systemName;
		
		restTemplate.postForObject(printResultsPostURI + "auditServer/audit/printResults", printResults, PrintResults.class);
		
		
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

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getPrintResultsPostURI() {
		return printResultsPostURI;
	}

	public void setPrintResultsPostURI(String printResultsPostURI) {
		this.printResultsPostURI = printResultsPostURI;
	}

	public Log getLog() {
		return log;
	}
}
