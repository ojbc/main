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
package org.ojbc.util.camel.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

/**
 * This method will set the Crypto properties for a WSS4J interceptor.  This is for an WSS4J out interceptor that performs encryption and signature.  The reason to do this is so you can dynamically
 * set the Encryption Alias.  Since we are encrypting with the server public key, we don't want a properties object per endpoint.
 * 
 * In the Camel route, you need to set the header 'endpointEncryptionAlias'.  That will have a corresponding entry in the encryptionKeystoreAlias with the actual alias.
 */

public class CryptoPropertiesMessageProcessor {

	private String cryptoProvider;
	private String keystoreType;
	
	private String signingKeystore;
	private String signingKeystorePassword;
	private String signingKeystoreAlias;
	
	private String encryptionKeystore;
	private String encryptionKeystorePassword;
	
	
	private Map<String, String> encryptionKeystoreAlias = new HashMap<String, String>();
	
	
	public void addCryptoProperties(Exchange exchange) throws Exception
	{
        Map<String, Object> requestContext = new HashMap<String, Object>();
        
        if (StringUtils.isNotBlank(signingKeystorePassword))
        {	
	        Properties cryptoPropertiesSignature = new Properties();
	        cryptoPropertiesSignature.put("org.apache.ws.security.crypto.provider",cryptoProvider);
	        cryptoPropertiesSignature.put("org.apache.ws.security.crypto.merlin.keystore.password", signingKeystorePassword);
	        cryptoPropertiesSignature.put("org.apache.ws.security.crypto.merlin.file",signingKeystore);
	        cryptoPropertiesSignature.put("org.apache.ws.security.crypto.merlin.keystore.type", keystoreType);
	        cryptoPropertiesSignature.put("org.apache.ws.security.crypto.merlin.keystore.alias", signingKeystoreAlias);
	        
	        requestContext.put("cryptoPropertiesSignature", cryptoPropertiesSignature);
	        
	        /** IMPORTANT, the below configuration will only work with WSS4J 1.6, you need to use 'SignaturePropRefId' for 1.5 **/
	        requestContext.put("signaturePropRefId","cryptoPropertiesSignature"); 
        }
        
        if (StringUtils.isNotBlank(encryptionKeystorePassword))
        {	
	        Properties cryptoPropertiesEncryption = new Properties();
	        cryptoPropertiesEncryption.put("org.apache.ws.security.crypto.provider",cryptoProvider);
	        cryptoPropertiesEncryption.put("org.apache.ws.security.crypto.merlin.keystore.password", encryptionKeystorePassword);
	        cryptoPropertiesEncryption.put("org.apache.ws.security.crypto.merlin.file",encryptionKeystore);
	        cryptoPropertiesEncryption.put("org.apache.ws.security.crypto.merlin.keystore.type", keystoreType);
	        
	        String encryptionAliasEndpoint = (String)exchange.getIn().getHeader("endpointEncryptionAlias");
	        String encryptionAlias = null;
	        
	        if (StringUtils.isNotBlank(encryptionAliasEndpoint))
	        {
	        	encryptionAlias = encryptionKeystoreAlias.get(encryptionAliasEndpoint);
	        }	
	        else
	        {
	        	throw new Exception("Camel Header endpointEncryptionAlias is not set");
	        }	
	        
	        cryptoPropertiesEncryption.put("org.apache.ws.security.crypto.merlin.keystore.alias", encryptionAlias);
	        
	        requestContext.put("cryptoPropertiesEncryption", cryptoPropertiesEncryption);
	        requestContext.put(WSHandlerConstants.ENC_PROP_REF_ID,"cryptoPropertiesEncryption"); 
        }
        
        exchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
	}


	public String getCryptoProvider() {
		return cryptoProvider;
	}


	public void setCryptoProvider(String cryptoProvider) {
		this.cryptoProvider = cryptoProvider;
	}


	public String getSigningKeystore() {
		return signingKeystore;
	}


	public void setSigningKeystore(String signingKeystore) {
		this.signingKeystore = signingKeystore;
	}


	public String getSigningKeystorePassword() {
		return signingKeystorePassword;
	}


	public void setSigningKeystorePassword(String signingKeystorePassword) {
		this.signingKeystorePassword = signingKeystorePassword;
	}


	public String getSigningKeystoreAlias() {
		return signingKeystoreAlias;
	}


	public void setSigningKeystoreAlias(String signingKeystoreAlias) {
		this.signingKeystoreAlias = signingKeystoreAlias;
	}


	public String getEncryptionKeystore() {
		return encryptionKeystore;
	}


	public void setEncryptionKeystore(String encryptionKeystore) {
		this.encryptionKeystore = encryptionKeystore;
	}


	public String getEncryptionKeystorePassword() {
		return encryptionKeystorePassword;
	}


	public void setEncryptionKeystorePassword(String encryptionKeystorePassword) {
		this.encryptionKeystorePassword = encryptionKeystorePassword;
	}

	public String getKeystoreType() {
		return keystoreType;
	}


	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}


	public Map<String, String> getEncryptionKeystoreAlias() {
		return encryptionKeystoreAlias;
	}


	public void setEncryptionKeystoreAlias(
			Map<String, String> encryptionKeystoreAlias) {
		this.encryptionKeystoreAlias = encryptionKeystoreAlias;
	}
	
}