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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.services.CodeTableService;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/audit")
@SessionAttributes({"auditSearchRequest", "agencyOriMapping", "auditSearchResults", "auditSearchContent", "auditActivityTypeMapping"})
public class AuditController {
	private static final Log log = LogFactory.getLog(AuditController.class);

	@Autowired
	AuditService auditService;
	
	@Resource
	SearchResultConverter searchResultConverter;

	@Resource
	SamlService samlService;
	
	@Resource
	CodeTableService codeTableService;
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
		if (!model.containsAttribute("agencyOriMapping")) {
			model.addAttribute("agencyOriMapping", codeTableService.getAgencies());
		}
		if (!model.containsAttribute("auditActivityTypeMapping")) {
			model.addAttribute("auditActivityTypeMapping", codeTableService.getAuditActivityTypes());
		}
    }	

	@GetMapping("/searchForm")
	public String getAuditLogsSearchForm(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		log.info("presenting Audit Logs Search Form ");
		AuditSearchRequest auditSearchRequest = (AuditSearchRequest) model.get("auditSearchRequest");
		
		if (auditSearchRequest == null) {
			auditSearchRequest = new AuditSearchRequest();
			model.put("auditSearchRequest", auditSearchRequest);
		}
		return "audit/auditLogs::resultsPage";
	}

	@GetMapping("resetSearchForm")
	public String resetSearchForm(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		model.put("auditSearchRequest", new AuditSearchRequest());
		return "audit/auditSearchForm::auditSearchForm";
	}

	@PostMapping("/auditLogs")
	public String findAuditLogs(HttpServletRequest request, AuditSearchRequest auditSearchRequest, Map<String, Object> model) throws Throwable {
		
		getAuditSearchResults(request, auditSearchRequest, model);
		return "audit/auditLogs::resultsList";
		
	}

	@GetMapping("/users")
	public String getUsers( Map<String, Object> model) throws Throwable {
		
		List<AuditUser> users = codeTableService.getUsers();
		model.put("users", users);
		return "audit/users::resultsPage";
		
	}
	
	private void getAuditSearchResults(HttpServletRequest request, AuditSearchRequest auditSearchRequest,
			Map<String, Object> model) throws Throwable {
		auditSearchRequest.setFirstNameSearchMetaData(); 
		auditSearchRequest.setLastNameSearchMetaData();
		log.debug("Find audit logs with : " + auditSearchRequest);
		String searchContent = auditService.findAuditLogs(auditSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertAuditSearchResult(searchContent);
		model.put("auditSearchResults", searchContent); 
		model.put("auditSearchContent", transformedResults); 
		model.put("auditSearchRequest", auditSearchRequest);
	}
	

}