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
package org.ojbc.web;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * Utility class that pulls the SAML assertion out of an HTTP Request.  It handles both methods that Shibboleth supports...environment variable passing
 * and HTTP header.
 *
 */
public class ShibbolethSamlAssertionRetriever {
    
    //TODO:  Need to refactor this out into a utility bundle and share this code with the OJBC Query Controller, which also needs to do this (note
    //       that we will need this same functionality in any webapp that is protected behind a SAML SP...

    private static final String SHIB_ASSERTION_KEY = "Shib-Assertion-01";
    public static final String HEADER_MODE = "header";
    public static final String ENVIRONMENT_VARIABLE_MODE = "variable";
    public static final String DEFAULT_MODE = HEADER_MODE;
    public static final String MODE_KEY = "mode";
    
    /**
     * Retrieve a SAML assertion from this HTTP Request
     * @param request the request
     * @return the SAML assertion as a formatted string
     * @throws Exception if there is a problem retrieving the assertion
     */
    public static final String retrieveAssertion(HttpServletRequest request) throws Exception {
        
        fixCertificatePathError();
        
        String mode = request.getParameter(MODE_KEY);
        
        if (mode == null)
        {
            mode = DEFAULT_MODE;
        }
        
        if (!(HEADER_MODE.equals(mode) || ENVIRONMENT_VARIABLE_MODE.equals(mode)))
        {
            throw new IllegalArgumentException("Illegal mode value of " + mode);
        }
        
        // Hard coded to pick up a single assertion...could loop through assertion headers if there will be more than one
        String assertionHttpHeaderName = (HEADER_MODE.equals(mode) ? request.getHeader(SHIB_ASSERTION_KEY) : (String) request.getAttribute(SHIB_ASSERTION_KEY));
        
        if (assertionHttpHeaderName == null)
        {
            return "No Shibboleth Assertion " + mode + " available.  Perhaps you're running in the other mode, or in an environment not protected by Shibboleth";
        }
        
        URL url = new URL(assertionHttpHeaderName);
        URLConnection con = url.openConnection();

        InputStream is = con.getInputStream();
        //BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        Source s = new StreamSource(is);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        //tf.setAttribute("indent-amount", new Integer(4));
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        
        StringWriter sw = new StringWriter();
        
        t.transform(s, new StreamResult(sw));
        /*
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        */
        String displayAssertion = sw.toString();
        String formattedAssertion = displayAssertion.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        
        return formattedAssertion;
        
    }

    private static void fixCertificatePathError() throws GeneralSecurityException {
        /*
         * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed:
         * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
         */
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
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
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
