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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.portal.AppProperties;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class PortalAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {
    private final Log log = LogFactory.getLog(this.getClass());
    
	@Resource
	AppProperties appProperties;

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
            HttpServletRequest context) {
        
    	log.debug("Enter portal authentication details source with request: " + context.getRequestURL());
    	
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>(); 
 
        Element samlAssertion = (Element)context.getAttribute("samlAssertion");
        
        if (samlAssertion == null){
        	log.info("samlAssertion is null ");
        }
        else {
        	SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        	grantedAuthorities.add(rolePortalUser);
        }
        
//        String principal = (String) context.getAttribute("principal");
//        String ori = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerORI);  
        String employerOrganizationCategoryText = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerOrganizationCategoryText);
        
        switch (employerOrganizationCategoryText) {
        	case "District Attorney": 
            	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_DA.name()));
        		break; 
        	case "Municipal Court": 
        		grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_MUNI.name()));
        		break; 
        }
        
        if (appProperties.getFedIdsWithAuditPrivilege().contains(SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.FederationId))) {
        	grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_AUDIT.name()) );
        }
        
        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                grantedAuthorities);
    }

}
