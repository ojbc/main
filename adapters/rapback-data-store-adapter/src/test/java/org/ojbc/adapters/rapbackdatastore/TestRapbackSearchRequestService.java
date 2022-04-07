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
package org.ojbc.adapters.rapbackdatastore;

import static org.junit.Assert.assertTrue;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestRapbackSearchRequestService {

    @Autowired
    private ModelCamelContext context;
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @BeforeEach
    public void setUp() throws Exception {
    	
        // Advise the Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
    	AdviceWith.adviceWith(context, "rapbackSearchRequestRoute", route -> {
    		route.replaceFromWith("direct:rapbackSearchRequest");
    		route.interceptSendToEndpoint(
                    "rapbackSearchResponseServiceEndpoint")
                    .to("mock:result")
                    .log("Called Rapback Search Response Service Endpoint")
                    .stop();
    	});
    	
        context.start();
    }

    @Test
    @DirtiesContext
    public void testRoute() throws Exception {
    }

}
