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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ojb.web.portal.WebPortalConstants;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.w3c.dom.Element;

public class SamlAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    
	private SamlService samlService;
    
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Element samlAssertion = this.extractSAMLAssertion(request);
        request.setAttribute("samlAssertion", samlAssertion);

        String federationId = null;
        if ( samlAssertion != null) {
           try {
            federationId = XmlUtils.xPathStringSearch(samlAssertion,
                        "/saml2:Assertion/saml2:AttributeStatement[1]/"
                        + "saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue");
            
            } catch (Exception e) {
                e.printStackTrace();
            } 

        }
        
        String principal = StringUtils.isNotBlank(federationId)? federationId:WebPortalConstants.EMPTY_FEDERATION_ID;
        
        request.setAttribute("principal", principal);
        return principal; 
    }

    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        
        Element samlAssertion = extractSAMLAssertion(request); 
        
        request.setAttribute("samlAssertion", samlAssertion);
        return samlAssertion;
    }

    private Element extractSAMLAssertion(HttpServletRequest request) {
        return samlService.getSamlAssertion(request); 
    }

    public SamlService getSamlService() {
        return samlService;
    }

    public void setSamlService(SamlService samlService) {
        this.samlService = samlService;
    }

    

}
