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
package org.ojbc.test.util;

import java.util.Map;

import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.apache.cxf.message.Message; 

public class SAMLTokenTestUtils {
	
    public static Message createSamlAssertionMessageWithAttributes(
            Map<SamlAttribute, String> customAttributes) throws Exception {
        org.apache.cxf.message.Message message = new MessageImpl();

        SAMLTokenPrincipal principal = createSAMLTokenPrincipalWithAttributes(customAttributes);
        message.put("wss4j.principal.result", principal);
        return message;
    }

    public static SAMLTokenPrincipal createSAMLTokenPrincipalWithAttributes(
    		Map<SamlAttribute, String> customAttributes) throws Exception {
    	
    	Assertion samlToken = SAMLTokenUtils
    			.createStaticAssertionWithCustomAttributes(
    					"https://idp.ojbc-local.org:9443/idp/shibboleth",
    					SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS,
    					SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true,
    					true, customAttributes);
    	SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(
    			new SamlAssertionWrapper(samlToken));
    	return principal;
    }
    

}
