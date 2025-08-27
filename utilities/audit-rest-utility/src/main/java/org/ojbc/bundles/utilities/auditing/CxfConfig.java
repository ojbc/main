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
package org.ojbc.bundles.utilities.auditing;
import java.util.Collections;

import org.apache.camel.component.cxf.jaxrs.CxfRsEndpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.ojbc.bundles.utilities.auditing.rest.AuditRestImpl;
import org.ojbc.bundles.utilities.auditing.rest.TotpUserRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

@Configuration
public class CxfConfig {
    @Value("${auditRestUtility.auditRestEndpoint}")
    private String auditServerAddress;

    @Value("${auditRestUtility.totpRestEndpoint}")
    private String totpUserServerAddress;
    
    @Autowired
    private AuditRestImpl auditRestImpl; 
    @Autowired
    private TotpUserRestImpl totpUserRestImpl;
    
    @Bean
    JacksonJsonProvider jacksonJsonProvider(ObjectMapper objectMapper) {
        return new JacksonJsonProvider(objectMapper);
    }

    @Bean
    CxfRsEndpoint auditServer(JacksonJsonProvider jacksonJsonProvider) {
        CxfRsEndpoint endpoint = new CxfRsEndpoint();
        endpoint.setAddress(auditServerAddress);
        endpoint.setResourceClasses(AuditRestImpl.class);
        endpoint.setServiceBeans(java.util.List.of(auditRestImpl));
        endpoint.setProviders(java.util.List.of(jacksonJsonProvider));
        return endpoint;
    }
    
    @Bean
    CxfRsEndpoint totpServer(JacksonJsonProvider jacksonJsonProvider) {
        CxfRsEndpoint endpoint = new CxfRsEndpoint();
        endpoint.setAddress(totpUserServerAddress);
        endpoint.setResourceClasses(AuditRestImpl.class);
        endpoint.setServiceBeans(java.util.List.of(totpUserRestImpl));
        endpoint.setProviders(java.util.List.of(jacksonJsonProvider));
        return endpoint;
    }    
}
