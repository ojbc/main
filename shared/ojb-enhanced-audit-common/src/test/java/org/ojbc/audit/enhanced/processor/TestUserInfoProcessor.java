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
package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.saml.builder.SAML2Constants;
import org.junit.Test;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.camel.security.saml.SAMLAssertionBuilder;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;

public class TestUserInfoProcessor {

	private static final Log log = LogFactory.getLog(TestUserInfoProcessor.class);
	
	@Test
	public void testProcessPersonSearchRequest() throws Exception
	{
		AbstractUserInfoProcessor userInfoProcessor = new UserInfoNullObjectProcessor();

		SAMLAssertionBuilder sab = new SAMLAssertionBuilder();
		
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		
        customAttributes.put(SamlAttribute.SurName, "smith");
        customAttributes.put(SamlAttribute.EmployerName, "DOJ");
        customAttributes.put(SamlAttribute.GivenName,"Joe");        
        customAttributes.put(SamlAttribute.FederationId,"FedID");
        customAttributes.put(SamlAttribute.EmployerSubUnitName,"sub");
        customAttributes.put(SamlAttribute.EmailAddressText,"1@1.com");
        customAttributes.put(SamlAttribute.EmployerORI,"ORI");
        customAttributes.put(SamlAttribute.IdentityProviderId,"HIJIS");
		
		Assertion assertion = sab.createSamlAssertion("https://idp.ojbc-local.org:9443/idp/shibboleth", "_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", "http://ojbc.org/ADS/WebServiceConsumer", 
				SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT, SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, customAttributes);

		//XmlUtils.printNode(assertion.getDOM());
		
		UserInfo userInfo = userInfoProcessor.processUserInfoRequest(assertion);
		
		assertEquals("DOJ",userInfo.getEmployerName());
		assertEquals("sub",userInfo.getEmployerSubunitName());
		assertEquals("FedID",userInfo.getFederationId());
		assertEquals("HIJIS",userInfo.getIdentityProviderId());
		assertEquals("1@1.com",userInfo.getUserEmailAddress());
		assertEquals("Joe",userInfo.getUserFirstName());
		assertEquals("smith",userInfo.getUserLastName());
	}
	
}
