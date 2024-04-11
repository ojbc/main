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
package org.ojbc.web.security;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.ojbc.web.portal.services.OTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	private static final Log log = LogFactory.getLog(CustomLogoutSuccessHandler.class);

	@Value("${userSignOutUrl:http://localhost:8080/ojb-web-portal/portal/defaultLogout}")
    String userSignOutUrl;
	
	@Value("#{getObject('entityLogoutReturnUrlMap')}")
	Map<String, String> entityLogoutReturnUrlMap;
    
    @Value("${enableEnhancedAudit:false}")
    Boolean enableEnhancedAudit;
    
	@Resource
	OTPService otpService;
	
	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;

    @Value("${requireOtpAuthentication:false}")
    Boolean requireOtpAuthentication;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
    	String redirectUrl = userSignOutUrl;
    	if (authentication != null) {
    		Element samlAssertion = (Element)authentication.getCredentials();
    		
    		if (enableEnhancedAudit)
    		{
            	try {
    				restEnhancedAuditClient.auditUserLogout(samlAssertion);
    			} catch (Exception e) {
    				e.printStackTrace();
    				log.error("Unable to audit user logout");
    			}
    		}	
    		if (requireOtpAuthentication)
    		{
    			String userEmail = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);;

    			log.info("User email address to remove OTP authentication: " + userEmail);
    			
    			if (StringUtils.isNotBlank(userEmail))
    			{	
    				log.info("Unauthenticate user.");
    				otpService.unauthenticateUser(userEmail);
    			}	
    		}	
    		redirectUrl = getUserSignoutUrl( samlAssertion );
    	}
    	
    	log.info("logout redirect URL: " + redirectUrl);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.sendRedirect(redirectUrl);
    }
    
	private String getUserSignoutUrl(Element samlAssertion){
		StringBuilder sb = new StringBuilder();
		
    	sb.append(userSignOutUrl);
    	
    	if (samlAssertion!= null && entityLogoutReturnUrlMap != null){
    		
    		String samlTokenIssuer;
			try {
				samlTokenIssuer = XmlUtils.xPathStringSearch( samlAssertion, "/saml2:Assertion/saml2:Issuer");
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException("Failed to get samlTokenIssuer");
			}
    		
    		log.info("Test login with the new jquery click");
    		log.info("Saml Token Issuer: " + samlTokenIssuer);
    		
    		String logoutReturnUrl = entityLogoutReturnUrlMap.get(samlTokenIssuer);
    		
    		log.info("Logout return URL: " + logoutReturnUrl);
    		
    		if (StringUtils.isNotBlank(logoutReturnUrl)){
    			sb.append("?return=");
    			sb.append(logoutReturnUrl);
    		}
    	}
    	
    	return sb.toString();
	}

    
}