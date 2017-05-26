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
package org.ojbc.util.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class RequestFactoryManager {

	 public static ClientHttpRequestFactory createHttpClient(String truststoreLocation, String truststorePassword, String keystoreLocation, String keystorePassword, String keypassword) 
			 throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException 
	 {
		 	KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
			 
		 	truststore.load(new FileInputStream(new File(truststoreLocation)),
	        		truststorePassword.toCharArray());

		 	SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		 	
		 	sslContextBuilder.loadTrustMaterial(truststore);
		 	
		 	if (StringUtils.isNotBlank(keystoreLocation) && StringUtils.isNotBlank(keystorePassword)&& StringUtils.isNotBlank(keypassword))
		 	{	
			 	KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
				 
			 	keystore.load(new FileInputStream(new File(keystoreLocation)),
			 			keystorePassword.toCharArray());
			 	
			 	sslContextBuilder.loadKeyMaterial(keystore, keypassword.toCharArray());
		 	}	
			 	
		 	
			SSLContext sslContext = sslContextBuilder.build();
			
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,new AllowAllHostnameVerifier());
			
			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
			        httpClient);	
			
			return requestFactory;
	 }	 
	
	 public static ClientHttpRequestFactory createHttpClient(String truststoreLocation, String truststorePassword) 
			 throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException 
	 {
		 return createHttpClient(truststoreLocation, truststorePassword);
	 }


}
