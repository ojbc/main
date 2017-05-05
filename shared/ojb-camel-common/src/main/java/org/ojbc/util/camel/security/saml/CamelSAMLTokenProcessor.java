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
package org.ojbc.util.camel.security.saml;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.w3c.dom.Element;

public class CamelSAMLTokenProcessor {

	private OJBSamlMap OJBSamlMap;
	
	private static final Log log = LogFactory.getLog(CamelSAMLTokenProcessor.class);
	
	public void retrieveSAMLTokenFromMessage(Exchange exchange)
	{
		Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
		SAMLTokenPrincipal token = (SAMLTokenPrincipal)cxfMessage.get("wss4j.principal.result");
		Element assertionElement = null;
		
		if (token != null)
		{	
			assertionElement = token.getToken().getSaml2().getDOM();
		}
			
		if (assertionElement != null)
		{
			//Set the token header so that CXF can retrieve this on the outbound call
			String tokenID = exchange.getExchangeId();
			exchange.getIn().setHeader("tokenID", tokenID);

			log.info("Retrieved SAML token from inbound message and adding to token map with ID: " + tokenID);
			
			OJBSamlMap.putToken(tokenID, assertionElement);
		}	
		
	}

	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}

	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}
	
}
