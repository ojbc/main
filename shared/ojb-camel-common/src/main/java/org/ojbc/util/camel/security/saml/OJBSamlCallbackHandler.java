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
package org.ojbc.util.camel.security.saml;

import java.io.IOException;
import java.util.Objects;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.wss4j.common.saml.SAMLCallback;
import org.w3c.dom.Element;

/**
 * This callback handler will retrieve a SAML token from the OJBSamlMap
 * using a 'tokenID' as the key.  Token ID is in the CXF headers as Camel
 * copies headers to the CXF message.  See here for more information:
 * 
 * @see http://cxf.547215.n5.nabble.com/WS-Security-Policy-with-Existing-SAML-Token-tc5720608.html
 * 
 */
public class OJBSamlCallbackHandler implements CallbackHandler{

	private static final Log log = LogFactory.getLog(OJBSamlCallbackHandler.class);
	
	private OJBSamlMap OJBSamlMap;
	
	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof SAMLCallback) {
				SAMLCallback callback = (SAMLCallback) callbacks[i];

				//Get CXF message from Phase Interceptor Chain
				Message message = PhaseInterceptorChain.getCurrentMessage(); 
				
				//Get Token ID from CXF message
				String tokenID = SAMLTokenUtils.returnCamelPropertyFromCXFMessage(message, "tokenID");
				log.error("tokenID in the OJBSamlCallBackHandler: " + Objects.toString(tokenID));
				
				if (StringUtils.isNotBlank(tokenID))
				{
					//Get SAML token from hashmap
					Element assertionElement = (Element)OJBSamlMap.getToken(tokenID);
					
					//Throw exception if SAML token is null
					if (assertionElement == null)
					{
						log.error("Unable to find assertion element in token map.");
						throw new IOException("Unable to find assertion element");
					}	
					
					//Set assertion element on callback object
					callback.setAssertionElement(assertionElement);
					
					//Remove SAML token from map, it is only needed once
					//TODO: We need a way to determine how many endpoints to call and allow the SAML token to be retrieved that many times
					//OJBSamlMap.removeToken(tokenID);
				}
			}
		}

	}
	
	
	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}

	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}

}
