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
package org.ojbc.web.portal.services;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@Service("samlService")
public class SamlServiceImpl implements SamlService{
	
	private static final Log LOG = LogFactory.getLog(SamlServiceImpl.class);

    @Value("${webapplication.allowQueriesWithoutSAMLToken:false}")
    private Boolean allowQueriesWithoutSAMLToken;

	public Element getSamlAssertion(HttpServletRequest request) {

		Element assertion = null;
		try {
			assertion = retrieveAssertionFromShibboleth(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (assertion == null && getAllowQueriesWithoutSAMLToken()){
			assertion = createDemoUserSamlAssertion();
		}
		
		return assertion;
	}
	
	Element retrieveAssertionFromShibboleth(HttpServletRequest request) throws Exception
	{
		// Note: pulled this straight from Andrew's demo JSP that displays the assertion and http request...
		
		/*
		 *  fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
		 *       sun.security.validator.ValidatorException:
		 *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
		 *               unable to find valid certification path to requested target
		 */
		 TrustManager[] trustAllCerts = new TrustManager[]{
				    new X509TrustManager() {
				        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				            return null;
				        }
				        public void checkClientTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				        public void checkServerTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				    }
				};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;  // andrew had this as false...dont know how that would work...
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		/*
		 * end of the fix
		 */
		 //Hard coded to pick up a single assertion...could loop through assertion headers if there will  be more than one
		String assertionHttpHeaderName = request.getHeader("Shib-Assertion-01");
		LOG.info("Loading assertion from: " + assertionHttpHeaderName);
		
		if(assertionHttpHeaderName == null){
			LOG.warn("Shib-Assertion-01 header was null, Returning null asssertion document element");
			return null;
		}
		
		URL url = new URL(assertionHttpHeaderName);
		URLConnection con = url.openConnection();

		InputStream is = con.getInputStream();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document assertionDoc = db.parse(is);
		
		return assertionDoc.getDocumentElement();
		
	}
	
    private Element createDemoUserSamlAssertion() {
    	
    	Element samlAssertion = null;
        try {
            Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
//            customAttributes.put(SamlAttribute.FederationId, "");
            customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
//                customAttributes.put(SamlAttribute.FederationId.getAttibuteName(), "HIJIS:IDP:HCJDC:USER:demouser4");
            customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
            customAttributes.put(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator, "true");
            customAttributes.put(SamlAttribute.SupervisoryRoleIndicator, "true");
//            customAttributes.put(SamlAttribute.FederatedQueryUserIndicator, "");
//                customAttributes.put("gfipm:2.0:user:EmployerORI", "H00000001");
            
            samlAssertion = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                    SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return samlAssertion;
    }

	public Boolean getAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}

	public void setAllowQueriesWithoutSAMLToken(
			Boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

}
