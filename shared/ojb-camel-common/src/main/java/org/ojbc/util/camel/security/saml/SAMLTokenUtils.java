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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.ws.security.SAMLTokenPrincipal;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.saml.ext.OpenSAMLBootstrap;
import org.apache.ws.security.saml.ext.bean.AttributeBean;
import org.apache.ws.security.saml.ext.bean.AttributeStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.apache.ws.security.saml.ext.builder.SAML2ComponentBuilder;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.joda.time.DateTime;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Element;

public class SAMLTokenUtils {
    private final static Log log = LogFactory.getLog(SAMLTokenUtils.class);
    
    static final String SECURITY_CRYPTO_PROVIDER_KEY = "org.apache.ws.security.crypto.provider";
    static final String SECURITY_CRYPTO_PROVIDER_VALUE = "org.apache.ws.security.components.crypto.Merlin";
    static final String MERLIN_KEYSTORE_TYPE_VALUE = "jks";
    static final String USER_HOME=System.getProperty("user.home");
    static final String MERLIN_KEYSTORE_FILE_VALUE = USER_HOME + "/ojb-certs/idp/idp-keystore.jks";
    static final String MERLIN_KEYSTORE_PASSWORD_VALUE = "idp-keystore";
    static final String MERLIN_KEYSTORE_ALIAS_VALUE = "idp-key";
    static final String KEY_PASSWORD_VALUE = "idp-key";
	
	/**
	 * This method is used to create a static SAML assertion as an element.  It will contain hard coded data and is typically used for testing
	 * or for creating a token in a mock connector or adapter where an IDP or token is unavailable.
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
	public static Element createStaticAssertionAsElement(String issuerString, String defaultCanonicalizationAlgorithm, String defaultRSASignatureAlgorithm, boolean createAuthnStatements, boolean createAttributeStatements, Map<String, String> customAttributes) throws Exception
	{
		Assertion assertion = createStaticAssertionWithCustomAttributes(issuerString, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm, createAuthnStatements, createAttributeStatements, customAttributes);
		return assertion.getDOM();
	}
	
	/**
	 * This function will create/sign an assertion.
	 * You can pass in a customAttributes map to set the GFIPM attributes as you wish, NULL is accepted for the map as well.
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
	public static Assertion createStaticAssertionWithCustomAttributes(String issuerString, String defaultCanonicalizationAlgorithm, String defaultRSASignatureAlgorithm, boolean createAuthnStatements, boolean createAttributeStatements, Map<String, String> customAttributes) throws Exception
	{
		return createStaticAssertionWithCustomAttributes(issuerString, defaultCanonicalizationAlgorithm, defaultRSASignatureAlgorithm,
				createAuthnStatements, createAttributeStatements, customAttributes, "HIJIS:IDP:HCJDC", "HIJIS:IDP:HCJDC:USER:admin", "HCJDC ISDI");
	}
	
	public static Assertion createStaticAssertionWithCustomAttributes(String issuerString, String defaultCanonicalizationAlgorithm, String defaultRSASignatureAlgorithm,
			boolean createAuthnStatements, boolean createAttributeStatements, Map<String, String> customAttributes, String idpID, String federationID, String employerID) throws Exception {
	
		OpenSAMLBootstrap.bootstrap();
		
		//Defensive check to allow for nulls and still produce attributes
		if (customAttributes == null)
		{
			customAttributes = new HashMap<String, String>();
		}	
		
		//Create assertion
		Assertion assertion = SAML2ComponentBuilder.createAssertion();
		
		//create issuer
		Issuer issuer = SAML2ComponentBuilder.createIssuer(issuerString);
		assertion.setIssuer(issuer);
		
		//create subject
		DateTime currentDateTime = new DateTime();
		
		Subject subject = GFIPM_SAML2ComponentBuilder.createSaml2Subject("_408184603d310905303442e592991adc", "https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST", currentDateTime, SAML2Constants.CONF_BEARER);
        SubjectBean subjectBean = new SubjectBean();
        
        subjectBean.setSubjectNameIDFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:transient");
        subjectBean.setSubjectNameQualifier("https://idp.ojbc-local.org:9443/idp/shibboleth");
        subjectBean.setSubjectName("_387c25449c33c64f8fef276365872728");
        
		NameID nameID = SAML2ComponentBuilder.createNameID(subjectBean);
        subject.setNameID(nameID);

		assertion.setSubject(subject);
	
		//create conditions
		Conditions conditions = GFIPM_SAML2ComponentBuilder.createConditions("http://ojbc.org/ADS/WebServiceConsumer", "https://sp.ojbc-local.org/shibboleth", null);
		assertion.setConditions(conditions);
		
		//create attribute statements and attributes
		if (createAttributeStatements)
		{	
			List<AttributeBean> attributes = new ArrayList<AttributeBean>();
			
			addAttributeToList(customAttributes, "owen", SamlAttribute.SurName, attributes);	
			addAttributeToList(customAttributes, "true", SamlAttribute.FederatedQueryUserIndicator,attributes);	
			addAttributeToList(customAttributes, "Department of Attorney General", SamlAttribute.EmployerName,attributes);	
			addAttributeToList(customAttributes, "Sergeant", SamlAttribute.EmployeePositionName,attributes);	
			addAttributeToList(customAttributes, "andrew", SamlAttribute.GivenName,attributes);	
			addAttributeToList(customAttributes, "Andrew Owen", SamlAttribute.CommonName,attributes);	
			addAttributeToList(customAttributes, "true", SamlAttribute.CriminalJusticeEmployerIndicator,attributes);	
			addAttributeToList(customAttributes, "true", SamlAttribute.LawEnforcementEmployerIndicator,attributes);	
			addAttributeToList(customAttributes, federationID, SamlAttribute.FederationId,attributes);	
			addAttributeToList(customAttributes, "916-215-3933", SamlAttribute.TelephoneNumber,attributes);	
			addAttributeToList(customAttributes, employerID, SamlAttribute.EmployerSubUnitName,attributes);	
			addAttributeToList(customAttributes, "andrew@search.org", SamlAttribute.EmailAddressText,attributes);	
			addAttributeToList(customAttributes, "002015Y", SamlAttribute.EmployerORI,attributes);	
			addAttributeToList(customAttributes, idpID, SamlAttribute.IdentityProviderId,attributes);	
			addAttributeToList(customAttributes, "false", SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator,attributes);	
			addAttributeToList(customAttributes, "false", SamlAttribute.SupervisoryRoleIndicator,attributes);	
			
			List<AttributeStatementBean> attributeStatementBeans = new ArrayList<AttributeStatementBean>();
			
			AttributeStatementBean attributeStatementBean = new AttributeStatementBean();
			attributeStatementBean.setSamlAttributes(attributes);
			
			attributeStatementBeans.add(attributeStatementBean);
			
			List<AttributeStatement> attributeStatements = SAML2ComponentBuilder.createAttributeStatement(attributeStatementBeans);
			
			assertion.getAttributeStatements().addAll(attributeStatements);
		}	
		
		if (createAuthnStatements)
		{	
			//create authn statements with a AuthnContextClassRef of PasswordProtectedTransport
			List<AuthenticationStatementBean> authBeans = new ArrayList<AuthenticationStatementBean>();
			
			AuthenticationStatementBean authenticationStatementBean = new AuthenticationStatementBean();
			authenticationStatementBean.setAuthenticationMethod(SAML2Constants.AUTH_CONTEXT_CLASS_REF_PASSWORD_PROTECTED_TRANSPORT);
			
			authBeans.add(authenticationStatementBean);
			
			List<AuthnStatement> authnStatments = SAML2ComponentBuilder.createAuthnStatement(authBeans);
			assertion.getAuthnStatements().addAll(authnStatments);
		}	
			
		//Sign the assertion, we use the AssertionWrapper provides by WSS4J to do the signing
		//The SAMLTokenProvider shows how to do this
		AssertionWrapper assertionWrapper = new AssertionWrapper(assertion);
	
		Properties sigProperties = new Properties();
		
		sigProperties.put(SECURITY_CRYPTO_PROVIDER_KEY, SECURITY_CRYPTO_PROVIDER_VALUE);
		sigProperties.put(Merlin.KEYSTORE_TYPE, MERLIN_KEYSTORE_TYPE_VALUE);
		sigProperties.put(Merlin.KEYSTORE_ALIAS, MERLIN_KEYSTORE_ALIAS_VALUE);
		sigProperties.put(Merlin.KEYSTORE_PASSWORD, MERLIN_KEYSTORE_PASSWORD_VALUE );
		sigProperties.put(Merlin.KEYSTORE_FILE, MERLIN_KEYSTORE_FILE_VALUE);
        
		Crypto signatureCrypto = CryptoFactory.getInstance(sigProperties);
		
		String alias = sigProperties.getProperty(Merlin.KEYSTORE_ALIAS);

		String password = KEY_PASSWORD_VALUE;
		
		assertionWrapper.signAssertion(
				alias, password, signatureCrypto, false, defaultCanonicalizationAlgorithm,
	            defaultRSASignatureAlgorithm
	        );
		
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
    private static void addAttributeToList(Map<String, String> customAttributes,
            String defaultValue, SamlAttribute samlAttribute, List<AttributeBean> attributes) {
        AttributeBean attributeBean = getNewAttributeBean(customAttributes, defaultValue, samlAttribute);  
        attributes.add(attributeBean);
    }

    private static AttributeBean getNewAttributeBean(Map<String, String> customAttributes, String defaultValue, SamlAttribute samlAttribute) {
        if (customAttributes.containsKey(samlAttribute.getAttibuteName())){	
        	return createAttributeBean(samlAttribute.getAttibuteName(), customAttributes.get(samlAttribute.getAttibuteName()));
        }
        else{
        	return createAttributeBean(samlAttribute.getAttibuteName(), defaultValue);
        }
    }

	
	private static AttributeBean createAttributeBean(String qualifiedName, String value) {
		AttributeBean attributeBean = new AttributeBean();
		attributeBean.setQualifiedName(qualifiedName);
		
		List<String> values = new ArrayList<String>();
		values.add(value);
		attributeBean.setAttributeValues(values);
		return attributeBean;
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
