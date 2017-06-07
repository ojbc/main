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
