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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.WebUtils;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.WebPortalConstants;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.ojbc.web.portal.services.OTPService;
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
   
    @Value("${requireOtpAuthentication:false}")
    boolean requireOtpAuthentication;
    
	@Resource (name="${otpServiceBean:OTPServiceMemoryImpl}")
	OTPService otpService;
    
    @Value("${requireIdentityBasedAccessControl:false}")
    boolean requireIdentityBasedAccessControl;
    
    @Value("${requireSubscriptionAccessControl:false}")
    boolean requireSubscriptionAccessControl;
    
    @Value("${requirePortalQueryAccessControl:false}")
    boolean requirePortalQueryAccessControl;
    
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

    @Value("#{'${orisWithoutIncidentDetailAccess:}'.split(',')}")
    private List<String> orisWithoutIncidentDetailAccess;
    
    @Value("#{'${fedIdsWithAdminAccess:}'.split(',')}")
    private List<String> fedIdsWithAdminAccess;
    
    @Value("${enableEnhancedAudit:false}")
    Boolean enableEnhancedAudit;

    @Value("${bannerPath:/static/images/banner/Banner.png}")
    String bannerPath;

    @Value("${themePath:/static/css/style.css}")
    String themePath;
    
    @Value("${customStyleCssPath:}")
    String customStyleCssPath;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
    AppProperties appProperties;

    @Autowired(required=false)
    private AccessControlServicesConfig accessControlServicesConfig; 

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
            HttpServletRequest context) {
        
    	log.info("\nEnter portal authentication details source\n");
    	
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>(); 
 
        Element samlAssertion = (Element)context.getAttribute("samlAssertion");
        context.setAttribute("themePath", themePath);
        context.setAttribute("customStyleCssPath", customStyleCssPath);
        
        String ori = null;
        Boolean federatedQueryUserIndicator = false; 
        
        if (samlAssertion == null)
        {
        	log.info("samlAssertion is null ");
        }	
        else {
        	ori = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmployerORI);  
        	federatedQueryUserIndicator = WebUtils.getFederatedQueryUserIndicator(samlAssertion);
        }
        SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        
        if (requireFederatedQueryUserIndicator && BooleanUtils.isNotTrue(federatedQueryUserIndicator)){
        	log.warn("User does not have FederatedQueryUserIndicator");
            return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                    grantedAuthorities);
        }
        
        SimpleGrantedAuthority rolePortalUserOTP = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL_OTP.name());
        boolean rolePortalUserOTPGranted = false; 
        
        if (requireOtpAuthentication)
        {	
        	if (addOtpAuthenticationRole(samlAssertion))
        	{
        		grantedAuthorities.add(rolePortalUserOTP); 	
        		rolePortalUserOTPGranted = true; 
        	}
        	else
        	{
        		 return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
 	                    grantedAuthorities);
        	}	
        }    
        else
        {
        	grantedAuthorities.add(rolePortalUserOTP);
        	rolePortalUserOTPGranted = true; 
        }	
        
        String principal = (String) context.getAttribute("principal");
        log.info("requireIdentityBasedAccessControl:" + requireIdentityBasedAccessControl);
        
        if (requireIdentityBasedAccessControl && !WebPortalConstants.EMPTY_FEDERATION_ID.equals(principal)) {
        	
            String accessControlResponseString = accessControlServicesConfig
                    .getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
                            UUID.randomUUID().toString(), samlAssertion, policyAccessControlResourceURI);
            	Assert.notNull(accessControlResponseString, "The Access Control Response should not be Null"); 
            
            /*
             * Grant the "PortalUser" Role only if accessDenied is "false"
             */
            String accessDenied = getAccessDeniedIndicator(accessControlResponseString, policyAccessControlResourceURI);
                
            if (StringUtils.isNotBlank(accessDenied) && !Boolean.valueOf(accessDenied) && rolePortalUserOTPGranted ) {
                grantedAuthorities.add(rolePortalUser);
            }
            else {
                context.setAttribute("accessControlResponse", accessControlResponseString);
            }
        }
        else if (!requireIdentityBasedAccessControl && rolePortalUserOTPGranted){
            grantedAuthorities.add(rolePortalUser); 
        }
        
        /*
         * Check whether to grant other authorities only when PortalUser access is granted.  
         */
        if (grantedAuthorities.contains(rolePortalUser)) {
        	
        	/*
        	 * Check whether to grant admin role.  
        	 */
        	log.info("fedIdsWithAdminAccess:" + StringUtils.join(fedIdsWithAdminAccess, ","));
        	log.info("principal:" + principal);
        	log.info("fedIdsWithAdminAccess.contains(principal):" + fedIdsWithAdminAccess.contains(principal));
        	if (fedIdsWithAdminAccess.contains(principal)){
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_ADMIN.name()));
        	}
        	
        	String criminalJusticeEmployerIndicator = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.CriminalJusticeEmployerIndicator); 
        	String lawEnforcementEmployerIndicator = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.LawEnforcementEmployerIndicator);

        	log.info("orisWithoutIncidentDetailAccess:" + orisWithoutIncidentDetailAccess);
        	log.info("Employer ORI:" + ori);
        	
        	if (!orisWithoutIncidentDetailAccess.contains(ori)){
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_INCIDENT_DETAIL.name())); 
                log.info("User granted incident detail viewing.");
        	}
        	
            if(requireSubscriptionAccessControl) {
            	
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CIVIL_SUBSCRIPTION.name()));
            	if (BooleanUtils.toBoolean(criminalJusticeEmployerIndicator) || BooleanUtils.toBoolean(lawEnforcementEmployerIndicator)){
            		grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_ID_RESULTS.name()));
            		grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_SUBSCRIPTION.name()));
            	}
                
/*
 *  			Make the access decision in the portal temporarily.  Will need to refactor the code later.
 */
//                String accessControlResponseString = accessControlServicesConfig
//                		.getIdentityBasedAccessControlServiceBean().invokeAccessControlRequest(
//                				UUID.randomUUID().toString(), samlAssertion, criminalSubscriptionAccessControlResourceURI);
//                Assert.notNull(accessControlResponseString); 
                
            }
            else {
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_SUBSCRIPTION.name())); 
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CRIMINAL_ID_RESULTS.name())); 
                grantedAuthorities.add(new SimpleGrantedAuthority(Authorities.AUTHZ_CIVIL_SUBSCRIPTION.name())); 
            }
            
            SimpleGrantedAuthority roleAuthQuery = new SimpleGrantedAuthority(Authorities.AUTHZ_QUERY.name());
            if (requirePortalQueryAccessControl){
            	if (BooleanUtils.toBoolean(criminalJusticeEmployerIndicator) || BooleanUtils.toBoolean(lawEnforcementEmployerIndicator)){
            		grantedAuthorities.add(roleAuthQuery);
            	}
            }
            else {
                grantedAuthorities.add(roleAuthQuery); 
            }
            
            SimpleGrantedAuthority roleAuthIncidentAccess = new SimpleGrantedAuthority(Authorities.AUTHZ_INCIDENT_SEARCH_SOURCES.name());
            if (appProperties.getRequireIncidentAccessControl() && grantedAuthorities.contains(roleAuthQuery)) {
            	String incidentAccessIndicator = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.IncidentAccessIndicator); 
            	if (BooleanUtils.toBoolean(incidentAccessIndicator)) {
            		grantedAuthorities.add(roleAuthIncidentAccess); 
            	}
            }
            else {
            	grantedAuthorities.add(roleAuthIncidentAccess); 
            }
        }
        
        if (enableEnhancedAudit)
        {
        	try {
				String employerName = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmployerName);
				String federationId = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.FederationId);
				String employerSubunitName = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmployerSubUnitName);
				String firstName = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.GivenName);
				String lastName = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.SurName);
				String emailAddress = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);
				String identityProviderId = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.IdentityProviderId);
				
				restEnhancedAuditClient.auditUserLogin(federationId, employerName, employerSubunitName, firstName, lastName, emailAddress, identityProviderId);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Unable to audit user login");
			}
        }	
        
        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                grantedAuthorities);
    }

	private boolean addOtpAuthenticationRole(Element samlAssertion) {
   
		if (otpService != null)
		{
			log.info("Validate OTP using service");
			
			String userEmail = "";
			try {
				userEmail = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);
			} catch (Exception e) {
				log.error("Unable to retrieve SAML assertion");
				return false;
			}

			if (otpService.isUserAuthenticated(userEmail))
			{
				log.info("Role OTP should be granted");
				return true;
			}	
		}
		
		return false;
	}

	@SuppressWarnings("unused")
	private void grantOrDenyAuthority(List<SimpleGrantedAuthority> grantedAuthorities,
			String accessControlResponseString, String resourceURI, Authorities authority) {
		String accessDenied = getAccessDeniedIndicator(accessControlResponseString, resourceURI);
		
		if (StringUtils.isNotBlank(accessDenied) && !Boolean.valueOf(accessDenied)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
		}
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
