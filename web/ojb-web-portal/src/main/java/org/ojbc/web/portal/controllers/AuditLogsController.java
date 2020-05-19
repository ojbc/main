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
package org.ojbc.web.portal.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.web.portal.controllers.config.PeopleControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@Profile({"audit-search","standalone"})
@SessionAttributes({ "userAuthenticationSearchRequest", "userAuthenticationSearchResponses"})
@RequestMapping("/auditLogs")
public class AuditLogsController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	PeopleControllerConfigInterface peopleQueryConfig;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
    @Value("${auditSearchDateRange:30}")
    Integer auditSearchDateRange;

    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	if (!model.containsAttribute("userAuthenticationSearchRequest")) {
	    	UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
	    	model.addAttribute("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
    	}
    	
    	Map<String, String> userActionMap = new HashMap<>(); 
    	userActionMap.put("", "User Action"); 
    	userActionMap.put("login", "Login"); 
    	userActionMap.put("logout", "Logout"); 
    	model.addAttribute("userActionMap", userActionMap); 
	}

	private UserAuthenticationSearchRequest initUserAuthenticationSearchRequest() {
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
    	userAuthenticationSearchRequest.setEndTime(LocalDateTime.now());
    	userAuthenticationSearchRequest.setStartTime(LocalDateTime.now().minusDays(auditSearchDateRange));
		return userAuthenticationSearchRequest;
	}
    
	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm) {
			UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
			model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		} 

		return "auditLogs/_searchForm";
	}
	
	@RequestMapping("/userLoginSearch")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute UserAuthenticationSearchRequest userAuthenticationSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "auditLogs/_searchForm";
		}

		log.info("userAuthenticationSearchRequest:" + userAuthenticationSearchRequest );
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = restEnhancedAuditClient.retrieveUserAuthentications(userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		model.put("userAuthenticationSearchResponses", userAuthenticationSearchResponses); 
		return "auditLogs/_userAuthenticationSearchResults";
	}

}

