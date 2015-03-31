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
    private static final String NON_CURRENT_USER_ID="HIJIS:IDP:HCJDC:USER:aowen"; 
    private static final String INVALID_USER_ID="HIJIS:IDP:HCJDC:USER:demouser3";
    
    @Value("${policy.accesscontrol.requestedresource:}")
    private String policyAccessControlResourceURI;
    
    @Value("${subscription.accesscontrol.requestedresource:}")
    private String subscriptionAccessControlResourceURI;

    @Override
    public String invokeAccessControlRequest(String federatedQueryID, Element samlToken,
            String requestedResourceURI) {
        
        try {
            String federationId = XmlUtils.xPathStringSearch(samlToken,
                    "/saml2:Assertion/saml2:AttributeStatement[1]/"
                            + "saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue");
            if (VALID_USER_WITH_SUBSCRIPTION.equals(federationId)) {
                return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                        "/sampleResponses/identityBasedAccessControl/AccessControlResponseForCurrentUser.xml"));
            }
            if (VALID_USER_WITHOUT_SUBSCRIPTION.equals(federationId)) {
                if (policyAccessControlResourceURI.equals(requestedResourceURI)) {
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/sampleResponses/identityBasedAccessControl/AccessControlResponseForCurrentUser.xml"));
                }
                else if (subscriptionAccessControlResourceURI.equals(requestedResourceURI)) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } 
        
        return null; 
    }

}
