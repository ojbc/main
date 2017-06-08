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

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class RestFactoryUtils {

	 public static ClientHttpRequestFactory createHttpClientRequestFactory(String truststoreLocation, String truststorePassword, String keystoreLocation, String keystorePassword, String keypassword) 
			 throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException 
	 {

		SSLContextBuilder builder = new SSLContextBuilder();
			
		File truststoreFile = new File(truststoreLocation);
			
		builder.loadTrustMaterial(truststoreFile,truststorePassword.toCharArray());
		
		builder.loadKeyMaterial(new File(keystoreLocation), keystorePassword.toCharArray(), keypassword.toCharArray());
		
		 SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(builder.build());

		 CloseableHttpClient httpClient = HttpClients.custom()
		         .setSSLSocketFactory(csf)
		         .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
		         .build();

         
		 HttpComponentsClientHttpRequestFactory requestFactory =
		         new HttpComponentsClientHttpRequestFactory(httpClient);

		 
		 return requestFactory;
	 }
}
