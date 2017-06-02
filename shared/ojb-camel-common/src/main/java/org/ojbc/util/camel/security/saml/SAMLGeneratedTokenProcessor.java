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

import java.util.Map;

import org.apache.camel.Exchange;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.camel.security.saml.SAMLAssertionBuilder;
import org.ojbc.util.model.saml.SamlAttribute;
import org.w3c.dom.Element;

/**
 * This class allows the developer to create a SAML token and customize it by configuring the 
 * samlAssertionBuilder bean accordingly.  Once the token is created, it is added to an internal
 * map so that it can be added to the CXF message using the OJB standard OJBSamlCallbackHandler.
 * 
 * It also provides a convenience method to remove the token from the map once the service call is made.
 * 
 */
public class SAMLGeneratedTokenProcessor {

	private SAMLAssertionBuilder samlAssertionBuilder;
	private OJBSamlMap ojbSamlMap;
	
	private String issuerString;
	private String inResponseTo;
	private String recipient;
	private String audienceRestriction;
	private String authenticationMethod;
	private String defaultCanonicalizationAlgorithm;
	private String defaultRSASignatureAlgorithm;
	
	private Map<SamlAttribute, String> customAttributes;
	
	public void addGeneratedSAMLTokenToExchange(Exchange exchange) throws Exception
	{
		Element assertion = samlAssertionBuilder.createSamlAssertionElement(issuerString, inResponseTo, recipient, audienceRestriction, authenticationMethod, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm, customAttributes);
		
		//Set the token header so that CXF can retrieve this on the outbound call
		String tokenID = exchange.getExchangeId();
		exchange.getIn().setHeader("tokenID", tokenID);
		
		ojbSamlMap.putToken(exchange.getExchangeId(), assertion);
	}

	public void removeSamlTokenFromMap(Exchange exchange) throws Exception
	{
		ojbSamlMap.removeToken(exchange.getExchangeId());
	}

	
	public SAMLAssertionBuilder getSamlAssertionBuilder() {
		return samlAssertionBuilder;
	}

	public void setSamlAssertionBuilder(SAMLAssertionBuilder samlAssertionBuilder) {
		this.samlAssertionBuilder = samlAssertionBuilder;
	}

	public String getIssuerString() {
		return issuerString;
	}

	public void setIssuerString(String issuerString) {
		this.issuerString = issuerString;
	}

	public String getInResponseTo() {
		return inResponseTo;
	}

	public void setInResponseTo(String inResponseTo) {
		this.inResponseTo = inResponseTo;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getAudienceRestriction() {
		return audienceRestriction;
	}

	public void setAudienceRestriction(String audienceRestriction) {
		this.audienceRestriction = audienceRestriction;
	}

	public String getAuthenticationMethod() {
		return authenticationMethod;
	}

	public void setAuthenticationMethod(String authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

	public String getDefaultCanonicalizationAlgorithm() {
		return defaultCanonicalizationAlgorithm;
	}

	public void setDefaultCanonicalizationAlgorithm(
			String defaultCanonicalizationAlgorithm) {
		this.defaultCanonicalizationAlgorithm = defaultCanonicalizationAlgorithm;
	}

	public String getDefaultRSASignatureAlgorithm() {
		return defaultRSASignatureAlgorithm;
	}

	public void setDefaultRSASignatureAlgorithm(String defaultRSASignatureAlgorithm) {
		this.defaultRSASignatureAlgorithm = defaultRSASignatureAlgorithm;
	}

	public Map<SamlAttribute, String> getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(Map<SamlAttribute, String> customAttributes) {
		this.customAttributes = customAttributes;
	}

	public OJBSamlMap getOjbSamlMap() {
		return ojbSamlMap;
	}

	public void setOjbSamlMap(OJBSamlMap ojbSamlMap) {
		this.ojbSamlMap = ojbSamlMap;
	}
	
}
