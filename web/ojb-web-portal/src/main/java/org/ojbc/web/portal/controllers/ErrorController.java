package org.ojbc.web.portal.controllers;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.security.config.AccessControlServicesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;
 
@Controller
@SessionAttributes({"samlAssertion"})
public class ErrorController {
    @Value("${memberContext}")
    String stateContext;

    @Value("${policy.url.username:}")
    String policyUrlUserName;

    @Value("${policy.url.password:}")
    String policyUrlPassword;
    
    @Value("${helpdesk.contactInfo:}")
    String helpDeskContactInfo;
    
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
		    model.put("accessControlResponse", getErrorMessage());
		}
		
		return "/error/403";
 
	}

    private String getErrorMessage() {
        StringBuilder sb = new StringBuilder(128); 
        sb.append( "LOGIN ERROR: Failed to get access control response. Please contact your Network Administrator or Help Desk"); 
        if (StringUtils.isNotBlank(helpDeskContactInfo)) {
            sb.append(" at " + helpDeskContactInfo ); 
        }
        sb.append(" .");
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
	
    @ModelAttribute("stateContext")
    public String getStateContext() {
        return stateContext;
    }
     
}