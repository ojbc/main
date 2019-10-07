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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.portal.security.OsbiUser;
import org.ojbc.web.portal.services.CodeTableService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@ControllerAdvice
@SessionAttributes({"agencyOriMapping", "authorizedOriMapping", "osbiUser"})
public class GlobalControllerAdvice {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
    AppProperties appProperties;
	
	@Resource
	CodeTableService codeTableService;

    @ModelAttribute
    public void setupModelAttributes(Model model, Authentication authentication) {
        model.addAttribute("inactivityTimeout", appProperties.getInactivityTimeout());
        model.addAttribute("inactivityTimeoutInSeconds", appProperties.getInactivityTimeoutInSeconds());
        
        if (authentication != null) {
	        OsbiUser osbiUser = (OsbiUser) authentication.getPrincipal();
	        model.addAttribute("osbiUser", osbiUser);
	        
	        if (!model.containsAttribute("agencyOriMapping")) {
		        Map<String, String> agencyOriMapping = codeTableService.getAgencies();
				model.addAttribute("agencyOriMapping", agencyOriMapping);
				if (ArrestType.OSBI.getDescription().equals(osbiUser.getEmployerOrganizationCategory())) {
					model.addAttribute("authorizedOriMapping", agencyOriMapping);
				}
				else {
					Map<String, String> authorizedOriMapping = new LinkedHashMap<>();
					for (String ori: osbiUser.getOris()) {
						authorizedOriMapping.put(ori, agencyOriMapping.get(ori));
					}
					model.addAttribute("authorizedOriMapping", authorizedOriMapping);
				}
	        }
        }

    }
    
}
