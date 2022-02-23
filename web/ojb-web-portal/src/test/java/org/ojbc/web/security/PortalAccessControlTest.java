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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.WebUtils;
import org.ojbc.web.portal.OjbcWebPortalApplication;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Element;

@SpringBootTest(args = {"--spring.config.additional-location=classpath:/"}, 
	classes = OjbcWebPortalApplication.class)
@ContextConfiguration({"classpath:beans/static-configuration-demostate.xml"})
@DirtiesContext
@ActiveProfiles("standalone")
public class PortalAccessControlTest {
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
    
    @Value("${webapplication.allowQueriesWithoutSAMLToken:false}")
    private boolean allowQueriesWithoutSAMLToken;
    
    private MockMvc mockMvc;

    private final String SECURED_URI = "/";

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
    @Test
    public void itShouldDenyAnonymousAccess() throws Exception {
        mockMvc.perform(get(SECURED_URI))
        	.andExpect(status().isForbidden()).andReturn();
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
        
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link subscriptionsLink\" id=\"subscriptionsLink\" href=\"#\">Subscriptions</a>" )); 
        
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
        
        Assert.assertFalse(result.getResponse().getContentAsString().contains("<a class=\"nav-link subscriptionsLink\" id=\"subscriptionsLink\" href=\"#\">Subscriptions</a>")); 
        
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
        MvcResult result = mockMvc.perform(get(SECURED_URI)
        		.requestAttr("samlAssertion", samlAssertion))
                .andExpect(MockMvcResultMatchers.forwardedUrl("/403"))
                .andReturn();

        MockHttpServletRequest request = result.getRequest(); 

        String expectedAccessControlResponse = WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                "/sampleResponses/identityBasedAccessControl/AccessControlResponseForNonCurrentUser.xml"));
        Assert.assertTrue(expectedAccessControlResponse.equals(request.getAttribute("accessControlResponse")));

        
        result = mockMvc.perform(get("/403")
            .requestAttr("accessControlResponse", request.getAttribute("accessControlResponse")))
            .andExpect(status().isOk()).andReturn(); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<input type=\"submit\" value=\"Acknowledge All\" class=\"btn btn-primary btn-sm mt-3\">")); 
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
        
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link subscriptionsLink\" id=\"subscriptionsLink\" href=\"#\">Subscriptions</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link rapbackLink\" id=\"rapbackLink\" href=\"#\">Applicant Rap Back</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link criminalIdLink\" id=\"criminalIdLink\" href=\"#\">Criminal Identification</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"dropdownQuery\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">Query</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link adminLink\" id=\"adminLink\" href=\"#\">Admin</a>")); 
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
    	
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link subscriptionsLink\" id=\"subscriptionsLink\" href=\"#\">Subscriptions</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link rapbackLink\" id=\"rapbackLink\" href=\"#\">Applicant Rap Back</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link criminalIdLink\" id=\"criminalIdLink\" href=\"#\">Criminal Identification</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"dropdownQuery\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">Query</a>")); 
    	
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
    	
        Assert.assertFalse(result.getResponse().getContentAsString().contains("<a class=\"nav-link subscriptionsLink\" id=\"subscriptionsLink\" href=\"#\">Subscriptions</a>")); 
        Assert.assertTrue(result.getResponse().getContentAsString().contains("<a class=\"nav-link rapbackLink\" id=\"rapbackLink\" href=\"#\">Applicant Rap Back</a>")); 
        Assert.assertFalse(result.getResponse().getContentAsString().contains("<a class=\"nav-link criminalIdLink\" id=\"criminalIdLink\" href=\"#\">Criminal Identification</a>")); 
        Assert.assertFalse(result.getResponse().getContentAsString().contains("<a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"dropdownQuery\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">Query</a>")); 
    	
    }
    

}