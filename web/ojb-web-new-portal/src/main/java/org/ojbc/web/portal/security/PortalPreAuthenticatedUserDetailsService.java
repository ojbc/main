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

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

@Component
public class PortalPreAuthenticatedUserDetailsService implements
		AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
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
		return new User(userName, "N/A", true, true, true, true, authorities);
	}
}
