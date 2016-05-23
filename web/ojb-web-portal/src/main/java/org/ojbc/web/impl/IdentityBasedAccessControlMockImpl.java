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
package org.ojbc.web.impl;

import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.IdentityBasedAccessControlService;
import org.ojbc.web.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class IdentityBasedAccessControlMockImpl implements IdentityBasedAccessControlService {
    
    private static final String VALID_USER_WITH_SUBSCRIPTION="HIJIS:IDP:HCJDC:USER:demouser"; 
    private static final String VALID_USER_WITHOUT_SUBSCRIPTION="HIJIS:IDP:HCJDC:USER:demouser1"; 
    private static final String NON_CURRENT_USER_ID="HIJIS:IDP:HCJDC:USER:hpotter"; 
    private static final String INVALID_USER_ID="HIJIS:IDP:HCJDC:USER:demouser3";
    
    @Value("${policy.accesscontrol.requestedresource:}")
    private String policyAccessControlResourceURI;
    
    @Value("${criminal.subscription.accesscontrol.requestedresource:}")
    private String criminalSubscriptionAccessControlResourceURI;

    @Value("${civil.subscription.accesscontrol.requestedresource:}")
    private String civilSubscriptionAccessControlResourceURI;
    
    @Value("${criminal.identification.results.requestedresource:}")
    private String criminalIdentificationResultsResourceURI;

    @Override
    public String invokeAccessControlRequest(String federatedQueryID, Element samlToken,
            String... requestedResourceURIs) {
        
    	String requestedResourceURI = requestedResourceURIs[0];
    	
        try {
            String federationId = XmlUtils.xPathStringSearch(samlToken,
                    "/saml2:Assertion/saml2:AttributeStatement[1]/"
                            + "saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue");
            if (VALID_USER_WITH_SUBSCRIPTION.equals(federationId)) {
                if (policyAccessControlResourceURI.equals(requestedResourceURI)) {
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/sampleResponses/identityBasedAccessControl/AccessControlResponseForCurrentUser.xml"));
                }
                else if (criminalSubscriptionAccessControlResourceURI.equals(requestedResourceURI)) {
                	
                	if (requestedResourceURI.length() == 3){
	                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	                            "/sampleResponses/identityBasedAccessControl/CriminalSubscriptionAccessGrantedResponse.xml"));
                	}
                	else {
	                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	                            "/sampleResponses/identityBasedAccessControl/CriminalSubscriptionAccessGrantedResponse.xml"));
                	}
                }
            }
            if (VALID_USER_WITHOUT_SUBSCRIPTION.equals(federationId)) {
                if (policyAccessControlResourceURI.equals(requestedResourceURI)) {
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/sampleResponses/identityBasedAccessControl/AccessControlResponseForCurrentUser.xml"));
                }
                else if (criminalSubscriptionAccessControlResourceURI.equals(requestedResourceURI)) {
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/sampleResponses/identityBasedAccessControl/AccessControlResponseForNonCurrentUser.xml"));
                }
            }
            else if (NON_CURRENT_USER_ID.equals(federationId)) {
                return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                        "/sampleResponses/identityBasedAccessControl/AccessControlResponseForNonCurrentUser.xml"));
                
            }
            else if(INVALID_USER_ID.equals(federationId)) {
                return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                        "/sampleResponses/identityBasedAccessControl/AccessControlResponseForEmptyFedID.xml"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
        
        return null; 
    }

}
