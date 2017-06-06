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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.WebUtils;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Element;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
//        "classpath:/META-INF/spring/demostate/routes-demostate.xml",
//        "classpath:/META-INF/spring/spring-beans-ojb-web-application-connector-context.xml" 
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class PortalAccessControlTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilter;
    
    @Autowired
    private PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
    
    @Value("${webapplication.allowQueriesWithoutSAMLToken:false}")
    private boolean allowQueriesWithoutSAMLToken;
    
    @Autowired
    private MockHttpServletRequest request;
    private MockMvc mockMvc;

    private final String SECURED_URI = "/portal/index";

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac)
        // Enable Spring Security
                .addFilters(springSecurityFilter).alwaysDo(print()).build();
        
        portalAuthenticationDetailsSource.requireOtpAuthentication=false;
    }
    
    @Test
    public void itShouldDenyAnonymousAccess() throws Exception {
        mockMvc.perform(get(SECURED_URI)).andExpect(status().isForbidden()).andReturn();
    }
    
    @Test
    public void allowQueriesWithoutSAMLTokenShouldBeTrue() throws Exception {
        Assert.assertTrue(allowQueriesWithoutSAMLToken);
    }
    
    @Test
    public void accessControlFlagsShouldBeTrue() throws Exception {
        Assert.assertTrue(portalAuthenticationDetailsSource.requireIdentityBasedAccessControl);
        Assert.assertTrue(portalAuthenticationDetailsSource.requireSubscriptionAccessControl);
    }

    @Test
    @DirtiesContext
    public void allowAccessToPortalAndSubscription() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
        customAttributes.put(SamlAttribute.EmployerORI, "H00000001");
        
        Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
            .andExpect(status().isOk()).andReturn();
        
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"subscriptionsLink\" class=\"leftMenuLink\" "
                + "href=\"#\" target=\"_blank\"><div></div>Subscriptions </a>")); 
        
    }
    
    @Test
    @DirtiesContext
    public void allowAccessToPortalButNoSubscription() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser1");
        customAttributes.put(SamlAttribute.EmployerORI, "H00000001");
    	customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator, "false");
    	customAttributes.put(SamlAttribute.CriminalJusticeEmployerIndicator, "false");
  
        Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
                .andExpect(status().isOk()).andReturn();
        
        Assert.assertFalse(result.getResponse().getContentAsString().contains("<a id=\"subscriptionsLink\" class=\"leftMenuLink\" "
                + "href=\"#\" target=\"_blank\"><div></div>Subscriptions </a>")); 
        
    }
    
    @Test
    @DirtiesContext
    public void denyAccessToNonCurrentUser() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:hpotter");
        customAttributes.put(SamlAttribute.EmployerORI, "H00000001");
        
        Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/403"))
                .andReturn();

        MockHttpServletRequest request = result.getRequest(); 

        String expectedAccessControlResponse = WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                "/sampleResponses/identityBasedAccessControl/AccessControlResponseForNonCurrentUser.xml"));
        Assert.assertTrue(expectedAccessControlResponse.equals(request.getAttribute("accessControlResponse")));

        
        result = mockMvc.perform(get("/403")
            .requestAttr("accessControlResponse", request.getAttribute("accessControlResponse")))
            .andExpect(status().isOk()).andReturn(); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<input name=\"acknowledgeAll\""
                + " id=\"acknowledgeAll\" type=\"submit\" value=\"Acknowledge All\" class=\"blueButton\">")); 
    }
    
    @Test
    @DirtiesContext
    public void denyAccessToInvalidUser() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser3");
        customAttributes.put(SamlAttribute.EmployerORI, "H0000000");
        
        Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/403"))
                .andReturn();
        
        MockHttpServletRequest request = result.getRequest(); 
        
        String expectedAccessControlResponse = WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                "/sampleResponses/identityBasedAccessControl/AccessControlResponseForEmptyFedID.xml"));
        Assert.assertTrue(expectedAccessControlResponse.equals(request.getAttribute("accessControlResponse")));
        
        result = mockMvc.perform(get("/403")
                .requestAttr("accessControlResponse", request.getAttribute("accessControlResponse")))
                .andExpect(status().isOk()).andReturn(); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("Login Error:  "
                + "One or more required user attributes are missing or not valid")); 
        
    }

    @Test
    @DirtiesContext
    public void allowAccessToPortalAndAllSubscriptions() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
        customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
        
        Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
            .andExpect(status().isOk()).andReturn();
        
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"subscriptionsLink\" class=\"leftMenuLink\" "
                + "href=\"#\" target=\"_blank\"><div></div>Subscriptions </a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"rapbackLink\" class=\"leftMenuLink\" "
        		+ "href=\"#\" target=\"_blank\"><div></div>Applicant Rap Back </a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"criminalIdLink\" class=\"leftMenuLink\" "
        		+ "href=\"#\" target=\"_blank\"><div></div>Criminal Identification </a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"queryLink\" class=\"leftMenuLink\" "
        		+ "href=\"#\" target=\"_blank\"><div></div>Query </a>")); 
    }
    
    @Test
    @DirtiesContext
    public void allowAccessToPortalAndAllSubscriptionsAndQueryButton() throws Exception {
    	Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
    	customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
    	customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
    	customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator, "false");
    	
    	Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
    			SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
    			SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
    	MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
    			.andExpect(status().isOk()).andReturn();
    	
    	Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"subscriptionsLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Subscriptions </a>")); 
    	Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"rapbackLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Applicant Rap Back </a>")); 
    	Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"criminalIdLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Criminal Identification </a>")); 
    	Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"queryLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Query </a>")); 
    	
    }
    
    @Test
    @DirtiesContext
    public void allowAccessToPortalAndAllSubscriptionsButQueryButton() throws Exception {
    	Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
    	customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
    	customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
    	customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator, "false");
    	customAttributes.put(SamlAttribute.CriminalJusticeEmployerIndicator, "false");
    	
    	Element samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
    			SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
    			SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
    	MvcResult result = mockMvc.perform(get(SECURED_URI).requestAttr("samlAssertion", samlAssertion))
    			.andExpect(status().isOk()).andReturn();
    	
    	Assert.assertFalse(result.getResponse().getContentAsString().contains("<a id=\"subscriptionsLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Subscriptions </a>")); 
    	Assert.assertTrue(result.getResponse().getContentAsString().contains("<a id=\"rapbackLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Applicant Rap Back </a>")); 
    	Assert.assertFalse(result.getResponse().getContentAsString().contains("<a id=\"criminalIdLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Criminal Identification </a>")); 
    	Assert.assertFalse(result.getResponse().getContentAsString().contains("<a id=\"queryLink\" class=\"leftMenuLink\" "
    			+ "href=\"#\" target=\"_blank\"><div></div>Query </a>")); 
    	
    }
    

}