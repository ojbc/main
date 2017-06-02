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
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojb.web.portal.WebPortalConstants;
import org.ojbc.web.WebUtils;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.security.config.AccessControlServicesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;
 
@Controller
@SessionAttributes({"samlAssertion"})
public class ErrorController {

    @Value("${policy.url.username:}")
    String policyUrlUserName;

    @Value("${policy.url.password:}")
    String policyUrlPassword;
    
    @Value("${helpdesk.contactInfo:}")
    String helpDeskContactInfo;
    
    @Value("${requireFederatedQueryUserIndicator:true}")
    boolean requireFederatedQueryUserIndicator;
   
    @Resource
    SearchResultConverter searchResultConverter;
    
    @Autowired(required=false)
    private AccessControlServicesConfig accessControlServicesConfig; 
    
    private final Log log = LogFactory.getLog(this.getClass());
    
	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accesssDenied(Principal user, Authentication authentication, HttpServletRequest request,
	        Map<String,Object> model) {
	    if (authentication != null) {
	        model.put("samlAssertion", authentication.getCredentials());
	    }
	    
		SecurityContextHolder.getContext().setAuthentication(null);
		
		Map<String, Object> params = new HashMap<String, Object>(); 
		
	    params.put("policyUrlUserName", policyUrlUserName); 
	    params.put("policyUrlPassword", policyUrlPassword); 
	    params.put("helpDeskContactInfo", helpDeskContactInfo); 
		
		String accessControlResponse = (String) request.getAttribute("accessControlResponse"); 
		
		if (accessControlResponse != null) {
            String convertPersonSearchResult = searchResultConverter.convertIdentityBasedAccessControlResult(
                    accessControlResponse, params);
            model.put("accessControlResponse", convertPersonSearchResult);
		}
		else {
		    model.put("accessControlResponse", getErrorMessage(authentication));
		}
		
		return "/error/403";
 
	}

    private String getErrorMessage(Authentication authentication) {
        StringBuilder sb = new StringBuilder(128); 
        
        Boolean federatedQueryUserIndicator = WebUtils.getFederatedQueryUserIndicator((Element)authentication.getCredentials());
        if ( (requireFederatedQueryUserIndicator && BooleanUtils.isNotTrue(federatedQueryUserIndicator))
        		|| (authentication!= null && WebPortalConstants.EMPTY_FEDERATION_ID.equals(authentication.getName()))){
        	sb.append( "LOGIN ERROR: One or more required user attributes are missing or not valid.  ");
        }
        else {
        	sb.append( "LOGIN ERROR: Failed to get access control response. ");
        }
        sb.append("Please contact your Network Administrator or Help Desk ");
        if (StringUtils.isNotBlank(helpDeskContactInfo)) {
            sb.append(" at " + helpDeskContactInfo ); 
        }
        
        sb.append(" and provide this error message.");
        return sb.toString();
    }
	
    @RequestMapping(value = "/acknowlegePolicies", method = RequestMethod.POST, params="acknowledgeAll")
    public String acknowlegePolicies(HttpServletRequest request, 
            Map<String,Object> model) {
 
        Element samlAssertion = (Element)model.get("samlAssertion"); 
        
        try {
            accessControlServicesConfig.getPolicyBasedAcknowledgementRecordingServiceBean()
                    .invokePolicyAcknowledgementRecordingRequest(UUID.randomUUID().toString(), samlAssertion);
        } catch (Exception e) {
           log.warn("Got exception while calling the Policy Acknowledgement Recording Service Intermediary");
           model.put("accessControlResponse", "Failed to acknowledge the policies! Please try again later");
           return "/error/403"; 
        }
        return "redirect:/portal/index";
    }
	
}