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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestRestController {
	
	private static final String CONSENT_SEARCH_URI = "http://localhost:8080/cm-api/findPendingInmates";
	
	private final Log log = LogFactory.getLog(TestRestController.class);
	
	@Value("${restBaseUrl}")
	private String restBaseUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
    private HttpClient httpClient;
    
	@Value("${truststoreLocation}")
	private String truststoreLocation;
	
	@Value("${truststorePassword}")
	private String truststorePassword;
	
	@Value("${keystoreLocation}")
	private String keystoreLocation;
	
	@Value("${keystorePassword}")
	private String keystorePassword;
	
	@Value("${keyPassword}")
	private String keypassword;    
    
    @Before
    public void setUp() throws Exception {
    	RequestConfig requestConfig = RequestConfig.custom().build();
    	HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    	clientBuilder.setDefaultRequestConfig(requestConfig);
    	httpClient = clientBuilder.build();
    }
    
	@Test
	public void testSearch() throws Exception {
		
		HttpGet getRequest = new HttpGet(CONSENT_SEARCH_URI);
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader(ConsentManagementRestController.DEMODATA_HEADER_NAME, "true");
		HttpResponse response = httpClient.execute(getRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
		StringBuffer content = new StringBuffer();
		String output = null;
		
		while ((output = br.readLine()) != null) {
			content.append(output);
		}
		
		log.info(content.toString());
		
	}

	/**
	 * This test will manually create a REST template to invoke the service
	 * 
	 * @throws Exception
	 */

	@Test
	@Ignore("You can use this test to integrate with a live adapter that is running")
	public void testSearchWithSSL() throws Exception {
		
		 SSLContextBuilder builder = new SSLContextBuilder();
		 //builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		 
			
		File truststoreFile = new File(truststoreLocation);
			
		builder.loadTrustMaterial(truststoreFile, truststorePassword.toCharArray());
		
		builder.loadKeyMaterial(new File(keystoreLocation), keystorePassword.toCharArray(), keypassword.toCharArray());
		
		 SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(builder.build());

		 CloseableHttpClient httpClient = HttpClients.custom()
		         .setSSLSocketFactory(csf)
		         .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
		         .build();

         
		 HttpComponentsClientHttpRequestFactory requestFactory =
		         new HttpComponentsClientHttpRequestFactory(httpClient);
		 
		String ret =  new RestTemplate(requestFactory).getForObject(restBaseUrl  + "/findPendingInmates", String.class);
		
		log.info("Pending inmates: " + ret);
		
	}
	
	/**
	 * This test will use the rest template bean to invoke the service
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore("You can use this test to integrate with a live adapter that is running")
	public void testSearchWithSSLBean() throws Exception {

		String ret =  restTemplate.getForObject(restBaseUrl  + "/findPendingInmates", String.class);
		
		log.info("Pending inmates: " + ret);
		
	}	
}
