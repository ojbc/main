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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.saml.ext.OpenSAMLBootstrap;
import org.apache.ws.security.saml.ext.bean.AttributeBean;
import org.apache.ws.security.saml.ext.bean.AttributeStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.builder.SAML2ComponentBuilder;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.joda.time.DateTime;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.w3c.dom.Element;

/**
 * This class will create a SAML assertion either as an OpenSAML assertion or as a DOM element.
 * If keystore information is provided properly (keystore location, keystore password, key alias with provided key as PFX file, and key password)
 * then a signed assertin will be created.
 * 
 */

public class SAMLAssertionBuilder {
	
    public static final String SECURITY_CRYPTO_PROVIDER_KEY = "org.apache.ws.security.crypto.provider";
    public static final String SECURITY_CRYPTO_PROVIDER_VALUE = "org.apache.ws.security.components.crypto.Merlin";
    public static final String MERLIN_KEYSTORE_TYPE_VALUE = "jks";
    
    /**
     * Location of keystore on file system
     */
    private String keystoreLocation;
    
    /**
     * Keystore password, note that this is different from the key password.
     */
    private String keystorePassword;
    
    /**
     * Alias of private key provided as a PFX file
     */
    private String keyAlias;
    
    /**
     * Password for private key
     */
    private String keyPassword;
    
    /**
     * This method will provide a SAML assertion as a DOM element.  The assertion will be assigned if the keystore information is properly specified.
     * 
     * @param issuerString
     * @param inResponseTo
     * @param recipient
     * @param audienceRestriction
     * @param authenticationMethod
     * @param defaultCanonicalizationAlgorithm
     * @param defaultRSASignatureAlgorithm
     * @param customAttributes - Map using SamlAttribute as key, and string as custom attribute value
     * @return
     * @throws Exception
     */
	public Element createSamlAssertionElement(String issuerString, String inResponseTo, String recipient, String audienceRestriction, String authenticationMethod, String defaultCanonicalizationAlgorithm,
            String defaultRSASignatureAlgorithm, Map<SamlAttribute, String> customAttributes) throws Exception
	{
		Assertion assertion = createSamlAssertion(issuerString, "_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", "http://ojbc.org/ADS/WebServiceConsumer", 
				SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm, customAttributes);
		
		return assertion.getDOM();

	}
	
    
    /**
     * This method will provide a SAML assertion as an OpenSAML assertion.  The assertion will be assigned if the keystore information is properly specified.
     * 
     * @param issuerString
     * @param inResponseTo
     * @param recipient
     * @param audienceRestriction
     * @param authenticationMethod
     * @param defaultCanonicalizationAlgorithm
     * @param defaultRSASignatureAlgorithm
     * @param customAttributes - Map using SamlAttribute as key, and string as custom attribute value
     * @return
     * @throws Exception
     */
	public Assertion createSamlAssertion(String issuerString, String inResponseTo, String recipient, String audienceRestriction, String authenticationMethod, String defaultCanonicalizationAlgorithm,
            String defaultRSASignatureAlgorithm, Map<SamlAttribute, String> customAttributes) throws Exception
	{
		OpenSAMLBootstrap.bootstrap();
		
		//Defensive check to allow for nulls and still produce attributes
		if (customAttributes == null)
		{
			customAttributes = new HashMap<SamlAttribute, String>();
		}	
		
		//Create assertion
		Assertion assertion = SAML2ComponentBuilder.createAssertion();
		
		//create issuer
		Issuer issuer = SAML2ComponentBuilder.createIssuer(issuerString);
		assertion.setIssuer(issuer);
		
		//create subject
		DateTime currentDateTime = new DateTime();
		
		//In Response To
		//Recipient
		Subject subject = GFIPM_SAML2ComponentBuilder.createSaml2Subject(inResponseTo, recipient, currentDateTime, SAML2Constants.CONF_BEARER);
		assertion.setSubject(subject);

		//Audience Restriction
		Conditions conditions = GFIPM_SAML2ComponentBuilder.createConditions(audienceRestriction, null, null);
		assertion.setConditions(conditions);
		
		//create attribute statements and attributes
		if (customAttributes != null && customAttributes.size() > 0)
		{	
			List<AttributeBean> attributes = new ArrayList<AttributeBean>();
			
			addAttributeToList(customAttributes, SamlAttribute.SurName, attributes);	
			addAttributeToList(customAttributes, SamlAttribute.FederatedQueryUserIndicator,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.EmployerName,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.EmployeePositionName,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.GivenName,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.CommonName,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.CriminalJusticeEmployerIndicator,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.LawEnforcementEmployerIndicator,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.FederationId,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.TelephoneNumber,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.EmployerSubUnitName,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.EmailAddressText,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.EmployerORI,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.IdentityProviderId,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator,attributes);	
			addAttributeToList(customAttributes, SamlAttribute.SupervisoryRoleIndicator,attributes);	
			
			List<AttributeStatementBean> attributeStatementBeans = new ArrayList<AttributeStatementBean>();
			
			AttributeStatementBean attributeStatementBean = new AttributeStatementBean();
			attributeStatementBean.setSamlAttributes(attributes);
			
			attributeStatementBeans.add(attributeStatementBean);
			
			List<AttributeStatement> attributeStatements = SAML2ComponentBuilder.createAttributeStatement(attributeStatementBeans);
			
			assertion.getAttributeStatements().addAll(attributeStatements);
		}	
		
		if (StringUtils.isNotEmpty(authenticationMethod))
		{	
			//create authn statements with a AuthnContextClassRef of PasswordProtectedTransport
			List<AuthenticationStatementBean> authBeans = new ArrayList<AuthenticationStatementBean>();
			
			AuthenticationStatementBean authenticationStatementBean = new AuthenticationStatementBean();
			
			//Maybe allow this to be configurable
			authenticationStatementBean.setAuthenticationMethod(authenticationMethod);
			
			authBeans.add(authenticationStatementBean);
			
			List<AuthnStatement> authnStatments = SAML2ComponentBuilder.createAuthnStatement(authBeans);
			assertion.getAuthnStatements().addAll(authnStatments);
		}	
			
		//Sign the assertion, we use the AssertionWrapper provides by WSS4J to do the signing
		//The SAMLTokenProvider shows how to do this
		AssertionWrapper assertionWrapper = new AssertionWrapper(assertion);
	
		if (StringUtils.isNotEmpty(keyAlias) 
			&& 	StringUtils.isNotEmpty(keystorePassword)
			&& 	StringUtils.isNotEmpty(keystoreLocation)
			&& 	StringUtils.isNotEmpty(keyPassword)
				)
		{	
		
			Properties sigProperties = new Properties();
			
			sigProperties.put(SECURITY_CRYPTO_PROVIDER_KEY, SECURITY_CRYPTO_PROVIDER_VALUE);
			sigProperties.put(Merlin.KEYSTORE_TYPE, MERLIN_KEYSTORE_TYPE_VALUE);
			sigProperties.put(Merlin.KEYSTORE_ALIAS, keyAlias);
			sigProperties.put(Merlin.KEYSTORE_PASSWORD, keystorePassword );
			sigProperties.put(Merlin.KEYSTORE_FILE, keystoreLocation);
	        
			Crypto signatureCrypto = CryptoFactory.getInstance(sigProperties);
			
			String alias = sigProperties.getProperty(Merlin.KEYSTORE_ALIAS);
	
			String password = keyPassword;
			
			assertionWrapper.signAssertion(
					alias, password, signatureCrypto, false, defaultCanonicalizationAlgorithm,
		            defaultRSASignatureAlgorithm
		        );
		}	
		
		// if you don't do this, it appears that the assertion object does not get fully created. uncomment the second line if you want to display it to stdout
		assertionWrapper.assertionToString();
		//System.out.println(assertionWrapper.assertionToString());
		
		return assertionWrapper.getSaml2();

	}
	
	/**
	 * Create new AttributeBean instance based customeAttributes, defaultValue and samlAttribute, 
	 * and add it to the the attributes List. 
	 * @param customAttributes
	 * @param defaultValue
	 * @param samlAttribute
	 * @param attributes
	 */
    private void addAttributeToList(Map<SamlAttribute, String> customAttributes,
            SamlAttribute samlAttribute, List<AttributeBean> attributes) {
    	if (StringUtils.isNotBlank(customAttributes.get(samlAttribute)))
    	{	
    		AttributeBean attributeBean = createAttributeBean(samlAttribute.getAttibuteName(), customAttributes.get(samlAttribute));
    		attributes.add(attributeBean);
    	}	
    }

	private AttributeBean createAttributeBean(String qualifiedName, String value) {
		AttributeBean attributeBean = new AttributeBean();
		attributeBean.setQualifiedName(qualifiedName);
		
		List<String> values = new ArrayList<String>();
		values.add(value);
		attributeBean.setAttributeValues(values);
		return attributeBean;
	}

	public String getKeystoreLocation() {
		return keystoreLocation;
	}

	public void setKeystoreLocation(String keystoreLocation) {
		this.keystoreLocation = keystoreLocation;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	
}
