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
import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.saml.builder.SAML2Constants;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Element;

public class SAMLTokenUtils {
    private final static Log log = LogFactory.getLog(SAMLTokenUtils.class);
    
    //Constants, static can not be overriden
    static final String SECURITY_CRYPTO_PROVIDER_KEY = "org.apache.ws.security.crypto.provider";
    static final String SECURITY_CRYPTO_PROVIDER_VALUE = "org.apache.ws.security.components.crypto.Merlin";
    static final String MERLIN_KEYSTORE_TYPE_VALUE = "jks";
    
    //Defaults, can be overriden
    static final String USER_HOME=System.getProperty("user.home");
    static final String MERLIN_KEYSTORE_FILE_VALUE = USER_HOME + "/ojb-certs/idp/idp-keystore.jks";
    static final String MERLIN_KEYSTORE_PASSWORD_VALUE = "idp-keystore";
    static final String MERLIN_KEYSTORE_ALIAS_VALUE = "idp-key";
    static final String KEY_PASSWORD_VALUE = "idp-key";
	
    //SAML Token defaults
    static final String DEFAULT_TOKEN_IDP="HIJIS:IDP:HCJDC";
    static final String DEFAULT_FEDERATION_ID="HIJIS:IDP:HCJDC:USER:admin";
    static final String DEFAULT_EMPLOYER_ID="HCJDC ISDI";
    static final String DEFAULT_SURNAME="owen";
    static final String DEFAULT_FEDERATED_QUERY_USER_INDICATOR="true";
    static final String DEFAULT_EMPLOYER_NAME = "Department of Attorney General";
    static final String DEFAULT_EMPLOYER_POSITION = "Sergeant";
    static final String DEFAULT_GIVEN_NAME ="andrew"; 
    static final String DEFAULT_COMMON_NAME ="Andrew Owen";
    static final String DEFAULT_CRIMINAL_JUSTICE_EMPLOYER_INDICATOR="true";
    static final String DEFAULT_LAW_ENFORCEMENT_EMPLOYER_INDICATOR="true";
    static final String DEFAULT_TELEPHONE_NUMBER="916-215-3933";
    static final String DEFAULT_EMAIL_ADDRESS="andrew@search.org";
    static final String DEFAULT_EMPLOYER_ORI="002015Y";
    static final String DEFAULT_FIREARMS_REGISTRATION_RECORDS_PERSONNEL_INDICATOR="false";
    static final String DEFAULT_SUPERVISORY_ROLE_INDICATOR="false";
    
    static final String DEFAULT_IN_RESPONSE_TO="_408184603d310905303442e592991adc";
    static final String DEFAULT_RECIPIENT="https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST";
    static final String DEFAULT_AUDIENCE_RESTRICTION="http://ojbc.org/ADS/WebServiceConsumer";
    
	/**
	 * This method is used to create a static SAML assertion as an element.  It will contain hard coded data and is typically used for testing
	 * or for creating a token in a mock connector or adapter where an IDP or token is unavailable.
	 * 
	 * This is a thin wrapper around createStaticAssertionWithCustomAttributes so users don't need to be aware of OpenSAML
	 * 
	 * 
	 * @param issuerString
	 * @param defaultCanonicalizationAlgorithm
	 * @param defaultRSASignatureAlgorithm
	 * @param createAuthnStatements
	 * @param createAttributeStatements
	 * @param customAttributes
	 * @return
	 * @throws Exception
	 */
	public static Element createStaticAssertionAsElement(String issuerString, String defaultCanonicalizationAlgorithm, String defaultRSASignatureAlgorithm, boolean createAuthnStatements, boolean createAttributeStatements, Map<SamlAttribute, String> customAttributes) throws Exception
	{
		Assertion assertion = SAMLTokenUtils.createStaticAssertionWithCustomAttributes(issuerString, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm, createAuthnStatements, createAttributeStatements, customAttributes);
		return assertion.getDOM();
	}
	
	/**
	 * This function will create/sign an assertion.
	 * You can pass in a customAttributes map to set the GFIPM attributes as you wish, NULL is accepted for the map as well.
	 * If you don't set any custom attributes, you can set createAttributeStatements to true and default attribute will
	 * be created.
	 * 
	 * @param issuerString
	 * @param defaultCanonicalizationAlgorithm
	 * @param defaultRSASignatureAlgorithm
	 * @param createAuthnStatements
	 * @param createAttributeStatements
	 * @param customAttributes
	 * @return
	 * @throws Exception
	 */
	public static Assertion createStaticAssertionWithCustomAttributes(String issuerString, String defaultCanonicalizationAlgorithm, String defaultRSASignatureAlgorithm, boolean createAuthnStatements, boolean createAttributeStatements, Map<SamlAttribute, String> customAttributes) throws Exception
	{
		
		SAMLAssertionBuilder samlAssertionBuilder = new SAMLAssertionBuilder();
		
		samlAssertionBuilder.setKeyPassword(KEY_PASSWORD_VALUE);
		samlAssertionBuilder.setKeyAlias(MERLIN_KEYSTORE_ALIAS_VALUE);
		samlAssertionBuilder.setKeystoreLocation(MERLIN_KEYSTORE_FILE_VALUE);
		samlAssertionBuilder.setKeystorePassword(MERLIN_KEYSTORE_PASSWORD_VALUE);
		
		String authenticationMethod="";
		
		if (createAuthnStatements)
		{	
			//if Custom attributes is null, declare map
			if (customAttributes == null)
			{
				customAttributes = new HashMap<SamlAttribute, String>();
			}	
			
			populateEmptyCustomAttributesWithDefaultValues(customAttributes);
		}	
		
		if (createAttributeStatements)
		{
			authenticationMethod = SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT;
		}	
		
		Assertion assertion = samlAssertionBuilder.createSamlAssertion(issuerString, "_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", "http://ojbc.org/ADS/WebServiceConsumer", 
				authenticationMethod, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm, customAttributes);
		
		return assertion;
	}

    private static void populateEmptyCustomAttributesWithDefaultValues(
			Map<SamlAttribute, String> customAttributes) {

    	if (!customAttributes.containsKey(SamlAttribute.SurName))
    	{
    		customAttributes.put(SamlAttribute.SurName, "owen");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.FederatedQueryUserIndicator))
    	{
    		customAttributes.put(SamlAttribute.FederatedQueryUserIndicator, "true");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.EmployerName))
    	{
    		customAttributes.put(SamlAttribute.EmployerName, "Department of Attorney General");
    	}
    	
    	if (!customAttributes.containsKey(SamlAttribute.EmployeePositionName))
    	{
    		customAttributes.put(SamlAttribute.EmployeePositionName, "Sergeant");
    	}
    	
    	if (!customAttributes.containsKey(SamlAttribute.GivenName))
    	{
    		customAttributes.put(SamlAttribute.GivenName, "andrew");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.CommonName))
    	{
    		customAttributes.put(SamlAttribute.CommonName, "Andrew Owen");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.CriminalJusticeEmployerIndicator))
    	{
    		customAttributes.put(SamlAttribute.CriminalJusticeEmployerIndicator, "true");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.LawEnforcementEmployerIndicator))
    	{
    		customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator, "true");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.FederationId))
    	{
    		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:admin");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.TelephoneNumber))
    	{
    		customAttributes.put(SamlAttribute.TelephoneNumber, "916-215-3933");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.EmployerSubUnitName))
    	{
    		customAttributes.put(SamlAttribute.EmployerSubUnitName, "HCJDC ISDI");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.EmailAddressText))
    	{
    		customAttributes.put(SamlAttribute.EmailAddressText, "andrew@search.org");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.EmployerORI))
    	{
    		customAttributes.put(SamlAttribute.EmployerORI, "002015Y");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.IdentityProviderId))
    	{
    		customAttributes.put(SamlAttribute.IdentityProviderId, "HIJIS:IDP:HCJDC");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator))
    	{
    		customAttributes.put(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator, "false");
    	}

    	if (!customAttributes.containsKey(SamlAttribute.SupervisoryRoleIndicator))
    	{
    		customAttributes.put(SamlAttribute.SupervisoryRoleIndicator, "false");
    	}

	}

	public static String getSamlAttributeFromCxfMessage(Message cxfMessage,
            SamlAttribute samlAttribute) {

        if (cxfMessage != null) {
            SAMLTokenPrincipal token = (SAMLTokenPrincipal) cxfMessage
                    .get("wss4j.principal.result");
            return getAttributeValueFromSamlToken(token, samlAttribute); 
        }

        return null;
    }

    public static String getAttributeValueFromSamlToken(SAMLTokenPrincipal token,
            SamlAttribute samlAttribute) {

        if (token != null) {
            Assertion assertion = token.getToken().getSaml2();

            if (assertion != null) {
                List<AttributeStatement> attributeStatements = assertion.getAttributeStatements();

                AttributeStatement attributeStatement = attributeStatements.get(0);
                List<Attribute> attributes = attributeStatement.getAttributes();

                for (Attribute attribute : attributes) {
                    String attributeName = attribute.getName();

                    if (attributeName.equals(samlAttribute.getAttibuteName())) {
                        XMLObject attributeValue = attribute.getAttributeValues().get(0);
                        String attributeValueAsString = attributeValue.getDOM().getTextContent();

                        log.debug(samlAttribute + " in SAML assertion: "
                                + attributeValueAsString);
                        return attributeValueAsString;
                    }
                }
            }
        }
        return null;
    }

}
