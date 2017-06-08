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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
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
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
	   // Do any additional configuration here
	  ClientHttpRequestFactory clientHttpRequestFactory = RestFactoryUtils.createHttpClientRequestFactory(truststoreLocation, truststorePassword, keystoreLocation, keystorePassword, keypassword);	
	  return new RestTemplate(clientHttpRequestFactory);
	}	
}