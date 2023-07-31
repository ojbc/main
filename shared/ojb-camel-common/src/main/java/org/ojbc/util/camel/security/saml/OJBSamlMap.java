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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OJBSamlMap {

	private static final Log log = LogFactory.getLog(OJBSamlMap.class);
	
	private Map<String, Object> tokens = new HashMap<String, Object>();

	public Object getToken(String tokenID)
	{
		return tokens.get(tokenID);
	}
	
	public void putToken(@Header(value = "tokenID")String tokenID, @Header(value = "token") Object token)
	{
		log.debug("Token class name: " + token.getClass().getName());
		log.debug("Token ID: " + tokenID);
		log.debug("OJBSamlMap size before adding new entry: " + tokens.size());
		
		tokens.put(tokenID, token);
	}
	
	public void removeToken(@Header(value = "tokenID")String tokenID)
	{
		if (StringUtils.isNotBlank(tokenID)) {
			log.debug("removing tokenID from the OJBSamlMap: " + tokenID);
			tokens.remove(tokenID);
		}
	}


	public int getMapSize() {
		return tokens.size(); 
	}
	
	public Set<String> getMapkeys() {
		return tokens.keySet(); 
	}
	
}
