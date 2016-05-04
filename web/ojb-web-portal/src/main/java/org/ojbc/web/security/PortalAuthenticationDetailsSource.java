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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojb.web.portal.WebPortalConstants;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.WebUtils;
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
    
    @Value("${requireFederatedQueryUserIndicator:true}")
    boolean requireFederatedQueryUserIndicator;
    
    @Value("${policy.accesscontrol.requestedresource:}")
    private String policyAccessControlResourceURI;
    
    @Value("${criminal.subscription.accesscontrol.requestedresource:}")
    private String criminalSubscriptionAccessControlResourceURI;

    @Value("${civil.subscription.accesscontrol.requestedresource:}")
    private String civilSubscriptionAccessControlResourceURI;
    
    @Value("${criminal.identification.results.requestedresource:}")
    private String criminalIdentificationResultsResourceURI;

    @Value("#{'${rapbackSearch.agencySuperUserOris:}'.split(',')}")
    private List<String> agencySuperUserOris;

    @Value("#{'${rapbackSearch.superUserOris:}'.split(',')}")
    private List<String> superUserOris;
    
    @Autowired(required=false)
    private AccessControlServicesConfig accessControlServicesConfig; 

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
            HttpServletRequest context) {
        
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>(); 
 
        Element samlAssertion = (Element)context.getAttribute("samlAssertion");
        String ori = getEmployerOri(samlAssertion);  

        SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        
        Boolean federatedQueryUserIndicator = WebUtils.getFederatedQueryUserIndicator(samlAssertion);
        
        if ( BooleanUtils.isNotTrue(federatedQueryUserIndicator)){
            return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                    grantedAuthorities);
        }
    
        String principal = (String) context.getAttribute("principal");
        log.debug("requireIdentityBasedAccessControl:" + requireIdentityBasedAccessControl);
        
        if (requireIdentityBasedAccessControl && !WebPortalConstants.EMPTY_FEDERATION_ID.equals(principal)) {
        	
            String accessControlResponseString = accessControlServicesConfig
                    .getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                            UUID.randomUUID().toString(), samlAssertion, policyAccessControlResourceURI);
            Assert.notNull(accessControlResponseString); 
            
            /*
             * Grant the "PortalUser" Role only if accessDenied is "false"
             */
            String accessDenied = getAccessDeniedIndicator(accessControlResponseString, policyAccessControlResourceURI);
                
            if (accessDenied!="" && !Boolean.valueOf(accessDenied)) {
                grantedAuthorities.add(rolePortalUser);
            }
            else {
                context.setAttribute("accessControlResponse", accessControlResponseString);
            }
        }
        else if (!requireIdentityBasedAccessControl){
            grantedAuthorities.add(rolePortalUser); 
        }

        /*
         * Check whether to grant other authorities only when PortalUser access is granted.  
         */
        if (grantedAuthorities.contains(rolePortalUser)) {
            if(requireSubscriptionAccessControl) {
            	
            	if (superUserOris.contains(ori) || agencySuperUserOris.contains(ori)){
                    grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_ID_RESULTS.name())); 
                    grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CIVIL_SUBSCRIPTION.name()));
                    
                    String accessControlResponseString = accessControlServicesConfig
                    		.getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                    				UUID.randomUUID().toString(), samlAssertion, criminalSubscriptionAccessControlResourceURI);
                    Assert.notNull(accessControlResponseString); 
                    
                    grantOrDenyAuthority(grantedAuthorities, accessControlResponseString,
                    		criminalSubscriptionAccessControlResourceURI, Authorities.AUTHZ_CRIMINAL_SUBSCRIPTION);
            	}
            	else{
                    String accessControlResponseString = accessControlServicesConfig
                    		.getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                    				UUID.randomUUID().toString(), samlAssertion, 
                    				criminalSubscriptionAccessControlResourceURI,
                    				criminalIdentificationResultsResourceURI, 
                    				civilSubscriptionAccessControlResourceURI);
                    Assert.notNull(accessControlResponseString); 
                    
                    /*
                     * Grant the "Criminal Subscription" access only if accessDenied is "false"
                     */
                    grantOrDenyAuthority(grantedAuthorities, accessControlResponseString, 
                    		criminalSubscriptionAccessControlResourceURI, Authorities.AUTHZ_CRIMINAL_SUBSCRIPTION);
                    grantOrDenyAuthority(grantedAuthorities, accessControlResponseString, 
                    		civilSubscriptionAccessControlResourceURI, Authorities.AUTHZ_CIVIL_SUBSCRIPTION);
                    grantOrDenyAuthority(grantedAuthorities, accessControlResponseString, 
                    		criminalIdentificationResultsResourceURI, Authorities.AUTHZ_CRIMINAL_ID_RESULTS);
                    
            	}
            }
            else {
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_SUBSCRIPTION.name())); 
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_ID_RESULTS.name())); 
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CIVIL_SUBSCRIPTION.name())); 
            }
        }
        
        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                grantedAuthorities);
    }

	private void grantOrDenyAuthority(List<SimpleGrantedAuthority> grantedAuthorities,
			String accessControlResponseString, String resourceURI, Authorities authority) {
		String accessDenied = getAccessDeniedIndicator(accessControlResponseString, resourceURI);
		
		if (accessDenied!="" && !Boolean.valueOf(accessDenied)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
		}
	}

	private String getEmployerOri(Element samlAssertion) {
		String ori = null;
		try {
			ori = XmlUtils.xPathStringSearch(samlAssertion, 
			        "/saml2:Assertion/saml2:AttributeStatement[1]/"
			        + "saml2:Attribute[@Name='gfipm:2.0:user:EmployerORI']/saml2:AttributeValue");
		} catch (Exception e) {
			log.error("EmployerORI is missing in the Saml Assertion");
		}
		return ori;
	}

    private String getAccessDeniedIndicator(String accessControlResponseString, String resourceURI) {
        Document responseDocument = DocumentUtils.getDocumentFromXmlString(accessControlResponseString);
        
        String accessDenied = "";
        try {
            accessDenied = XmlUtils.xPathStringSearch(responseDocument.getDocumentElement(), 
                    "/ac-doc:AccessControlResponse/ac-ext:AccessControlDecision[ac-ext:AccessDecisionResourceURI = '" + 
                    		resourceURI + "']/ac-ext:AccessDeniedIndicator");
        } catch (Exception e) {
            log.error("Faild to run xpath /ac-doc:AccessControlResponse/ac-ext:AccessControlDecision/ac-ext:AccessDeniedIndicator "
                    + "on " + accessControlResponseString, e); 
        }
        return accessDenied;
    }
    
}
