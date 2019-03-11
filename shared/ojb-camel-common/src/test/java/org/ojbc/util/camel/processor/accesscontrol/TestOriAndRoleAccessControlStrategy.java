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
package org.ojbc.util.camel.processor.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;

public class TestOriAndRoleAccessControlStrategy {


	@Test
	public void testAuthorized() throws Exception
	{
		//SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator
    	//SamlAttribute.SupervisoryRoleIndicator
		List<String> authorizedORIs = new ArrayList<String>();
		List<String> unAuthorizedORIs = new ArrayList<String>();
		
		authorizedORIs.add("ORI9999999");
		
		OriAndRoleAccessControlStrategy oriAndRoleAccessControlStrategy = new OriAndRoleAccessControlStrategy(authorizedORIs, unAuthorizedORIs);
		
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "ORI9999999");
		customAttributes.put(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator, "true");

	    Exchange senderExchange = createExchange(customAttributes);
		
		AccessControlResponse acr =  oriAndRoleAccessControlStrategy.authorize(senderExchange);
		
		assertTrue(acr.isAuthorized());
		assertEquals("Users in the ORI: ORI9999999 are authorized to run this query.", acr.getAccessControlResponseMessage());	

    	
		customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "ORI9999999");
		customAttributes.put(SamlAttribute.SupervisoryRoleIndicator, "true");

	    senderExchange = createExchange(customAttributes);
		
		acr =  oriAndRoleAccessControlStrategy.authorize(senderExchange);
		
		assertTrue(acr.isAuthorized());
		assertEquals("Users in the ORI: ORI9999999 are authorized to run this query.", acr.getAccessControlResponseMessage());	

	}

	private Exchange createExchange(Map<SamlAttribute, String> customAttributes) throws Exception {
		CamelContext ctx = new DefaultCamelContext(); 
	    Exchange senderExchange = new DefaultExchange(ctx);
	    
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		org.apache.cxf.message.Message message = new MessageImpl();
		
		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken(customAttributes);
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
		return senderExchange;
	}
	
	@Test
	public void testUnauthorized() throws Exception
	{
		List<String> authorizedORIs = new ArrayList<String>();
		List<String> unAuthorizedORIs = new ArrayList<String>();
		
		unAuthorizedORIs.add("ORI9999999");
		
		OriAndRoleAccessControlStrategy oriAndRoleAccessControlStrategy = new OriAndRoleAccessControlStrategy(authorizedORIs, unAuthorizedORIs);
		
		//Add SAML token to request call
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "ORI9999999");

		Exchange senderExchange = createExchange(customAttributes);
		
		AccessControlResponse acr =  oriAndRoleAccessControlStrategy.authorize(senderExchange);
		
		assertFalse(acr.isAuthorized());
		assertEquals("Users in the ORI: ORI9999999 are NOT authorized to run this query. The ORI is as an unauthorized ORI.", acr.getAccessControlResponseMessage());	
		
	}
	
	private SAMLTokenPrincipal createSAMLToken(Map<SamlAttribute, String> customAttributes) throws Exception {
		
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth",
				SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		return principal;
	}
}
