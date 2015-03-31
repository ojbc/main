package org.ojbc.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.security.config.AccessControlServicesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class PortalAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {
    private final Log log = LogFactory.getLog(this.getClass());
   
    @Value("${requireIdentityBasedAccessControl:false}")
    boolean requireIdentityBasedAccessControl;
    
    @Value("${requireSubscriptionAccessControl:false}")
    boolean requireSubscriptionAccessControl;
    
    @Value("${policy.accesscontrol.requestedresource:}")
    private String policyAccessControlResourceURI;
    
    @Value("${subscription.accesscontrol.requestedresource:}")
    private String subscriptionAccessControlResourceURI;

    @Autowired(required=false)
    private AccessControlServicesConfig accessControlServicesConfig; 

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
            HttpServletRequest context) {
        
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>(); 
 
        Element samlAssertion = (Element)context.getAttribute("samlAssertion");
        SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        
        log.info("requireIdentityBasedAccessControl:" + requireIdentityBasedAccessControl);
        if (requireIdentityBasedAccessControl) {
            String accessControlResponseString = accessControlServicesConfig
                    .getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                            UUID.randomUUID().toString(), samlAssertion, policyAccessControlResourceURI);
            Assert.notNull(accessControlResponseString); 
            
            /*
             * Grant the "PortalUser" Role only if accessDenied is "false"
             */
            String accessDenied = getAccessDeniedIndicator(accessControlResponseString);
                
            if (accessDenied!="" && !Boolean.valueOf(accessDenied)) {
                grantedAuthorities.add(rolePortalUser);
            }
            else {
                context.setAttribute("accessControlResponse", accessControlResponseString);
            }
        }
        else {
            grantedAuthorities.add(rolePortalUser); 
        }

        /*
         * Check whether to grant other authorities only when PortalUser access is granted.  
         */
        if (grantedAuthorities.contains(rolePortalUser)) {
            if(requireSubscriptionAccessControl) {
                String accessControlResponseString = accessControlServicesConfig
                        .getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                                UUID.randomUUID().toString(), samlAssertion, subscriptionAccessControlResourceURI);
                Assert.notNull(accessControlResponseString); 
                
                /*
                 * Grant the "Subscription" access only if accessDenied is "false"
                 */
                String accessDenied = getAccessDeniedIndicator(accessControlResponseString);
                    
                if (accessDenied!="" && !Boolean.valueOf(accessDenied)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_SUBSCRIPTION.name()));
                }
                
            }
            else {
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_SUBSCRIPTION.name())); 
            }
        }
        
        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                grantedAuthorities);
    }

    private String getAccessDeniedIndicator(String accessControlResponseString) {
        Document responseDocument = DocumentUtils.getDocumentFromXmlString(accessControlResponseString);
        
        String accessDenied = "";
        try {
            accessDenied = XmlUtils.xPathStringSearch(responseDocument.getDocumentElement(), 
                    "/ac-exchange:AccessControlResponse/ac-ext:AccessControlDecision/ac-ext:AccessDeniedIndicator");
        } catch (Exception e) {
            log.error("Faild to run xpath /ac-exchange:AccessControlResponse/ac-ext:AccessControlDecision/ac-ext:AccessDeniedIndicator "
                    + "on " + accessControlResponseString, e); 
        }
        return accessDenied;
    }
    
}
