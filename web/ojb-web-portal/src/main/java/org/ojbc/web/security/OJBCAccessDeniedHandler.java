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
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.totp.CredentialRepository;
import org.ojbc.web.portal.totp.TotpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service("ojbcAccessDeniedHandler")
public class OJBCAccessDeniedHandler implements AccessDeniedHandler {
	private final Log log = LogFactory.getLog(this.getClass());

	@Value("${requireOtpAuthentication:false}")
    Boolean requireOtpAuthentication;

    @Value("${requireFederatedQueryUserIndicator:true}")
    boolean requireFederatedQueryUserIndicator;

    @Resource
    AppProperties appProperties;
    
    @Autowired
    private CredentialRepository credentialRepository;
    
	@Resource
	TotpUserService totpUserService;
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		
    	@SuppressWarnings("unchecked")
		Collection<SimpleGrantedAuthority> existingGrantedAuthorities = 
			(Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    	

    	log.info("Current granted authorities: " + existingGrantedAuthorities);
    	
    	boolean otpRoleGranted = existingGrantedAuthorities.stream().anyMatch(item->item.getAuthority().equalsIgnoreCase(Authorities.AUTHZ_PORTAL_OTP.name())); 
    	log.info("requireOtpAuthentication:" + BooleanUtils.isTrue(requireOtpAuthentication));

    	if (BooleanUtils.isTrue(requireOtpAuthentication) && !otpRoleGranted)
		{
    		Element samlAssertion = (Element)request.getAttribute("samlAssertion");
			if (requireFederatedQueryUserIndicator)
			{
		        Boolean federatedQueryUserIndicator =
		        		BooleanUtils.toBooleanObject(
		        				SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.FederatedQueryUserIndicator));
		        
		        if ( BooleanUtils.isNotTrue(federatedQueryUserIndicator)){
		        	log.warn("User does not have FederatedQueryUserIndicator");
		        	request.getRequestDispatcher("/403").forward(request, response);
		        }
			}
			String userEmail = getUserEmail(samlAssertion);
			log.info("User doesn't have OTP role.");
			log.info("User Email: " + userEmail);
			
			Boolean isGoogleAuthUser = isGoogleAuthUser(userEmail);
			
			if (BooleanUtils.isTrue(isGoogleAuthUser)) {
				if (credentialRepository.getUser(userEmail) != null) {
					request.getRequestDispatcher("/code/inputForm").forward(request, response);
				}
				else {  //Show the QR code for user to register
					request.getRequestDispatcher("/code/qrCodePreparation/" + userEmail)
						.forward(request, response);
				}
			}
			else {
				request.getRequestDispatcher("/otp/inputForm").forward(request, response);
			}
			return;

		}	
		
		if (!request.isUserInRole(Authorities.AUTHZ_PORTAL.name()))
		{
			log.info("User doesn't have portal role.");
			request.getRequestDispatcher("/403").forward(request, response);
			return;
		}	

	}

	private Boolean isGoogleAuthUser(String userEmail) {
		Boolean isGoogleAuthUser = false; 
		switch (appProperties.getTwoFactorAuthType()) {
		case EMAIL: 
			isGoogleAuthUser = false; 
			break; 
		case GOOGLE_AUTH: 
			isGoogleAuthUser = true; 
			break; 
		case USER_CONFIG: 
			isGoogleAuthUser = totpUserService.isGoogleAuthUser(userEmail); 
			break; 
		}
		return isGoogleAuthUser;
	}
	
	private String getUserEmail(Element samlAssertion) {
        String userEmail = StringUtils.EMPTY;
		try {
			userEmail = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userEmail;
	}

}
