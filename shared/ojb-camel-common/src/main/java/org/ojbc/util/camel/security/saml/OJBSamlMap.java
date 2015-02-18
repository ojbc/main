package org.ojbc.util.camel.security.saml;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Header;
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
		
		tokens.put(tokenID, token);
	}
	
	public void removeToken(String tokenID)
	{
		tokens.remove(tokenID);
	}
	
	
}
