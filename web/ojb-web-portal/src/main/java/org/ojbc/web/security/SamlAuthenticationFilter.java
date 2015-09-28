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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.services.SamlService;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.w3c.dom.Element;

public class SamlAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    
    private SamlService samlService;
    
    private Boolean allowQueriesWithoutSAMLToken;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Element samlAssertion = this.extractSAMLAssertion(request);

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
        return federationId;
    }

    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        
        Element samlAssertion = extractSAMLAssertion(request); 
        
        request.setAttribute("samlAssertion", samlAssertion);
        return samlAssertion;
    }

    private Element extractSAMLAssertion(HttpServletRequest request) {
        
        Element samlAssertion = null; 
        
        try {
            samlAssertion = getSamlService().getSamlAssertion(request);
        }
        catch(Exception e) {
            e.printStackTrace(); 
        }
        
        if (samlAssertion == null && isAllowQueriesWithoutSAMLToken()) {
            samlAssertion = createDemoUserSamlAssertion(samlAssertion);
        }
        return samlAssertion;
    }

    private Element createDemoUserSamlAssertion(Element samlAssertion) {
        try {
            Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
            customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
//                customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser4");
            customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
//                customAttributes.put(SamlAttribute.EmployerORI, "H00000001");
            
            samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                    SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return samlAssertion;
    }

    public Boolean isAllowQueriesWithoutSAMLToken() {
        return allowQueriesWithoutSAMLToken;
    }

    public void setAllowQueriesWithoutSAMLToken(Boolean allowQueriesWithoutSAMLToken) {
        this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
    }

    public SamlService getSamlService() {
        return samlService;
    }

    public void setSamlService(SamlService samlService) {
        this.samlService = samlService;
    }

    

}
