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
package org.ojbc.web.portal.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.audit.AuditUser;
import org.ojbc.web.portal.services.CodeTableService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

@Component
public class PortalPreAuthenticatedUserDetailsService implements
		AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
	
	@Resource
	AppProperties appProperties;
	@Resource
	CodeTableService codeTableService;

    private final Log log = LogFactory.getLog(this.getClass());
	/**
	 * Get a UserDetails object based on the user name contained in the given token, and
	 * the GrantedAuthorities as returned by the GrantedAuthoritiesContainer
	 * implementation as returned by the token.getDetails() method.
	 */
	public final UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
			throws AuthenticationException {
		Assert.notNull(token.getDetails(), "token.getDetails() cannot be null");
		Assert.isInstanceOf(GrantedAuthoritiesContainer.class, token.getDetails());
		Collection<? extends GrantedAuthority> authorities = ((GrantedAuthoritiesContainer) token
				.getDetails()).getGrantedAuthorities();
		return createUserDetails(token, authorities);
	}

	/**
	 * Creates the final <tt>UserDetails</tt> object. Can be overridden to customize the
	 * contents.
	 *
	 * @param token the authentication request token
	 * @param authorities the pre-authenticated authorities.
	 */
	protected UserDetails createUserDetails(Authentication token,
			Collection<? extends GrantedAuthority> authorities) {
		String userName = "anonymous"; 
		Element samlAssertion = (Element)token.getPrincipal();
		try {
			String givenName = XmlUtils.xPathStringSearch(samlAssertion,
			        "/saml2:Assertion/saml2:AttributeStatement[1]/"
			        + "saml2:Attribute[@Name='gfipm:2.0:user:GivenName']/saml2:AttributeValue");
			String surName = XmlUtils.xPathStringSearch(samlAssertion,
					"/saml2:Assertion/saml2:AttributeStatement[1]/"
							+ "saml2:Attribute[@Name='gfipm:2.0:user:SurName']/saml2:AttributeValue");
			String fullName = StringUtils.join(Arrays.asList(givenName, surName), ' ');
			if (StringUtils.isNotBlank(fullName)) {
				userName = fullName; 
			}
		} catch (Exception e) {
			log.error("Failed to retrieve the user name from the SAML token.", e);
		}
		
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>();

        if (samlAssertion == null){
        	log.info("samlAssertion is null ");
        }
        else {
        	SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        	grantedAuthorities.add(rolePortalUser);
        }
        
        String employerOrganizationCategoryText = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerOrganizationCategoryText);
        
        
        AuditUser userInfo = getUserInfo(samlAssertion);
        UserAttributes userAttributes = codeTableService.auditUserLoginReturnUserAttributes(userInfo);
        
        for (String roleName : userAttributes.getRoles()) {
        	SimpleGrantedAuthority role = new SimpleGrantedAuthority(roleName);
        	grantedAuthorities.add(role);
        }
        switch (StringUtils.trimToEmpty(employerOrganizationCategoryText)) {
        case "District Attorney": 
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_DA.name()));
        	break; 
        case "Municipal Court": 
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_MUNI.name()));
        	break; 
        case "Criminal History Repository":
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_DA.name()));
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_MUNI.name()));
        	userAttributes.getOris().clear();
        	break;
        }
        
        if (appProperties.getFedIdsWithAuditPrivilege().contains(userInfo.getFederationId())) {
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_AUDIT.name()) );
        }

        //TODO call the rest service to get the list ORIs and access roles. 
    	
    	OsbiUser osbiUser = new OsbiUser(userName, "N/A", grantedAuthorities, userAttributes.getOris(), userInfo);
		osbiUser.setEmployerOrganizationCategory(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerOrganizationCategoryText));
		return osbiUser;
	}

	private AuditUser getUserInfo(Element samlAssertion) {
		AuditUser userInfo = new AuditUser();
		userInfo.setAgencyOri(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerORI));
		userInfo.setFederationId(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.FederationId));
		userInfo.setEmailAddress(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText));
		userInfo.setFirstName(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.GivenName));
		userInfo.setLastName(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.SurName));
		userInfo.setOrganizationName(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerName));
		return userInfo;
	}
}
