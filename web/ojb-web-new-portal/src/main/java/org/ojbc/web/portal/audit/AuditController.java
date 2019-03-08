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
package org.ojbc.web.portal.audit;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.arrest.ArrestService;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/audit")
@SessionAttributes({"auditSearchRequest"})
public class AuditController {
	private static final Log log = LogFactory.getLog(AuditController.class);

	@Autowired
	ArrestService arrestService;
	
	@Resource
	SamlService samlService;
	
	@GetMapping("/searchForm")
	public String getAuditLogsSearchForm(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		log.info("presenting Audit Logs Search Form ");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");
		
		if (auditSearchRequest == null) {
			auditSearchRequest = new AuditSearchRequest();
			model.put("auditSearchRequest", auditSearchRequest);
		}
		return "audit/auditSearchForm::auditSearchForm";
	}

	@PostMapping("/auditLogs")
	public String findAuditLogs(HttpServletRequest request, AuditSearchRequest auditSearchRequest, Map<String, Object> model) throws Throwable {
		log.info("Find audit logs with : " + auditSearchRequest);
//		String response = arrestService.lookupOtn(otn, samlService.getSamlAssertion(request)); 
//		String recordFound = getResponseMessage(otn, response); 
		return null;
	}

//	private String getResponseMessage(String otn, String response) throws Exception {
//		Document responseDocument = XmlUtils.toDocument(response); 
//		return XmlUtils.xPathStringSearch(responseDocument, "/rr-resp-doc:RecordReplicationResponse/rr-resp-ext:RecordFoundIndicator");
//	}
	


}