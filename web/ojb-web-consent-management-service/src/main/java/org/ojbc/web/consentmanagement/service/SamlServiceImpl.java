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
package org.ojbc.web.consentmanagement.service;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@Service("samlServiceImpl")
public class SamlServiceImpl {
	
	private static final Log LOG = LogFactory.getLog(SamlServiceImpl.class);

	public Element getSamlAssertion(HttpServletRequest request) {

		Element assertion = null;
		try {
			assertion = retrieveAssertionFromShibboleth(request);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private static final String SAML_XPATH_TEMPLATE="/saml2:Assertion/saml2:AttributeStatement[1]/"
	        + "saml2:Attribute[@Name='gfipm:ext:user:REPLACE_THIS_ATTR']/saml2:AttributeValue";
	
	public Map<String, String> getSamlHeaderInfo(Element samlAssertion) {
		Map<String, String> samlHeaderInfo = new HashMap<String, String>();
		
		XPath xPath = null;
		
		if (xPath == null)
		{	
			xPath = XPathFactory.newInstance().newXPath();
	        xPath.setNamespaceContext(new UniversalNamespaceCache(samlAssertion.getOwnerDocument(), true));
		}    
		
        try {
			String givenName = xPath.evaluate(SAML_XPATH_TEMPLATE.replaceAll("REPLACE_THIS_ATTR", "GivenName"), samlAssertion);
			
			if (StringUtils.isNotBlank(givenName))
			{
				samlHeaderInfo.put("GivenName", givenName);
			}

			String surName = xPath.evaluate(SAML_XPATH_TEMPLATE.replaceAll("REPLACE_THIS_ATTR", "SurName"), samlAssertion);
			
			if (StringUtils.isNotBlank(surName))
			{
				samlHeaderInfo.put("SurName", surName);
			}

			String federationId = xPath.evaluate(SAML_XPATH_TEMPLATE.replaceAll("REPLACE_THIS_ATTR", "FederationId"), samlAssertion);
			
			if (StringUtils.isNotBlank(federationId))
			{
				samlHeaderInfo.put("FederationId", federationId);
			}

        } catch (XPathExpressionException e) {
			e.printStackTrace();
			LOG.error("unable to retrieve SAML attributes");
		}	

		
		return samlHeaderInfo;
	}
	
}
