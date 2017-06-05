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
package org.ojbc.util.camel.saml;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.wss4j.common.saml.builder.SAML2Constants;
import org.junit.Test;
import org.ojbc.util.camel.security.saml.SAMLAssertionBuilder;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.signature.SignatureConstants;

public class TestSAMLAssertionBuilder {

    private static final String USER_HOME=System.getProperty("user.home");
    private static final String MERLIN_KEYSTORE_FILE_VALUE = USER_HOME + "/ojb-certs/idp/idp-keystore.jks";
    private static final String MERLIN_KEYSTORE_PASSWORD_VALUE = "idp-keystore";
    private static final String MERLIN_KEYSTORE_ALIAS_VALUE = "idp-key";
    private static final String KEY_PASSWORD_VALUE = "idp-key";
    
	@Test
	public void testCreateAssertionWithNullCustomAttributes() throws Exception
	{
		SAMLAssertionBuilder sab = new SAMLAssertionBuilder();
		
		sab.setKeyPassword(KEY_PASSWORD_VALUE);
		sab.setKeyAlias(MERLIN_KEYSTORE_ALIAS_VALUE);
		sab.setKeystoreLocation(MERLIN_KEYSTORE_FILE_VALUE);
		sab.setKeystorePassword(MERLIN_KEYSTORE_PASSWORD_VALUE);
		
		Assertion assertion = sab.createSamlAssertion("https://idp.ojbc-local.org:9443/idp/shibboleth", "_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", "http://ojbc.org/ADS/WebServiceConsumer", 
				SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT, SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, null);

		assertNotNull(assertion);
		
		assertEquals(0,assertion.getAttributeStatements().size());
		
		confirmAssertionInfo(assertion);
	}

	@Test
	public void testCreateAssertionWithCustomAttributes() throws Exception
	{
		SAMLAssertionBuilder sab = new SAMLAssertionBuilder();
		
		sab.setKeyPassword(KEY_PASSWORD_VALUE);
		sab.setKeyAlias(MERLIN_KEYSTORE_ALIAS_VALUE);
		sab.setKeystoreLocation(MERLIN_KEYSTORE_FILE_VALUE);
		sab.setKeystorePassword(MERLIN_KEYSTORE_PASSWORD_VALUE);
		
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		
        customAttributes.put(SamlAttribute.EmployeePositionName, "detective");
        customAttributes.put(SamlAttribute.SurName, "smith");
        customAttributes.put(SamlAttribute.FederatedQueryUserIndicator, "false");
        customAttributes.put(SamlAttribute.EmployerName, "DOJ");
        customAttributes.put(SamlAttribute.GivenName,"Joe");
        customAttributes.put(SamlAttribute.CommonName,"Joe Smith");
        
        customAttributes.put(SamlAttribute.CriminalJusticeEmployerIndicator,"false");
        customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator,"false");
        customAttributes.put(SamlAttribute.SupervisoryRoleIndicator,"true");
        customAttributes.put(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator,"true");
        customAttributes.put(SamlAttribute.FederationId,"FedID");
        customAttributes.put(SamlAttribute.TelephoneNumber,"999 999-9999");
        customAttributes.put(SamlAttribute.EmployerSubUnitName,"sub");
        customAttributes.put(SamlAttribute.EmailAddressText,"1@1.com");
        customAttributes.put(SamlAttribute.EmployerORI,"ORI");
        customAttributes.put(SamlAttribute.IdentityProviderId,"HIJIS");
		
		Assertion assertion = sab.createSamlAssertion("https://idp.ojbc-local.org:9443/idp/shibboleth", "_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", "http://ojbc.org/ADS/WebServiceConsumer", 
				SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT, SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, customAttributes);

		assertNotNull(assertion);
		
		confirmAssertionInfo(assertion);
		
		assertEquals(16, assertion.getAttributeStatements().get(0).getAttributes().size());
		
		for (Attribute attributes : assertion.getAttributeStatements().get(0).getAttributes())
		{
			//System.out.println(attributes.getName());
			//System.out.println(attributes.getAttributeValues().get(0).getDOM().getTextContent());
			
			if (attributes.getName().equals("gfipm:2.0:user:EmployeePositionName"))
			{
				assertEquals("detective", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:SurName"))
			{
				assertEquals("smith", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:FederatedQueryUserIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerName"))
			{
				assertEquals("DOJ", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:GivenName"))
			{
				assertEquals("Joe", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:CommonName"))
			{
				assertEquals("Joe Smith", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:CriminalJusticeEmployerIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:LawEnforcementEmployerIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals(SamlAttribute.SupervisoryRoleIndicator.getAttibuteName()))
			{
			    assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator.getAttibuteName()))
			{
			    assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals("gfipm:2.0:user:FederationId"))
			{
				assertEquals("FedID", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:TelephoneNumber"))
			{
				assertEquals("999 999-9999", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerSubUnitName"))
			{
				assertEquals("sub", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmailAddressText"))
			{
				assertEquals("1@1.com", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerORI"))
			{
				assertEquals("ORI", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:IdentityProviderId"))
			{
				assertEquals("HIJIS", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
		}		
	}


	protected void confirmAssertionInfo(Assertion assertion) {
		assertEquals("https://idp.ojbc-local.org:9443/idp/shibboleth", assertion.getIssuer().getValue());
		assertEquals("_408184603d310905303442e592991adc", assertion.getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData().getInResponseTo());
		assertEquals("https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", assertion.getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData().getRecipient());
		assertEquals("http://ojbc.org/ADS/WebServiceConsumer", assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI());
		assertEquals(SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT, assertion.getAuthnStatements().get(0).getAuthnContext().getAuthnContextClassRef().getDOM().getTextContent());
		assertEquals(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, assertion.getSignature().getSignatureAlgorithm());
		assertEquals(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, assertion.getSignature().getCanonicalizationAlgorithm());
	}
	
}
