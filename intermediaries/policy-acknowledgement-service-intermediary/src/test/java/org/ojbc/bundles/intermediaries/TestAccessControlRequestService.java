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
package org.ojbc.bundles.intermediaries;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@UseAdviceWith
// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",
        "classpath:META-INF/spring/extensible-beans.xml",
        "classpath:META-INF/spring/local-osgi-context.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-policy-acknowledgement.xml",
		})
@DirtiesContext
public class TestAccessControlRequestService {

    @Autowired
    private ModelCamelContext context;

    @Produce
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @Before
    public void setUp() throws Exception {
        // Advise the Access Control Request Service endpoint and replace it
        // with a mock endpoint.We then will test this mock endpoint to see if 
        // it gets the proper payload.
        context.getRouteDefinition("accessControlRequestRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:accessControlRequest");

                        interceptSendToEndpoint(
                                "accessControlResponseServiceEndpoint")
                                .to("mock:result")
                                .log("Called Access Control Response Handler")
                                .stop();
                    }
                });

        context.start();
    }

    @Test
    @DirtiesContext
    public void testSendCurrentUser() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestCurrent.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                        "src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForCurrentUser.xml"));
        // The first 18 lines of the file contain license info. 
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));

        Map<String, Object> headers = SoapMessageUtils.createHeaders();

        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        resultEndpoint.assertIsSatisfied();
        
        Exchange exchange = resultEndpoint.getExchanges().get(0);
        String opName = (String)exchange.getIn().getHeader("operationName");
        assertEquals("SubmitAccessControlResponse", opName);
        
        String opNamespace = (String)exchange.getIn().getHeader("operationNamespace");
        assertEquals("http://ojbc.org/Services/WSDL/AccessControlResponseService/1.0", opNamespace);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> requestContext = (Map<String, Object>)exchange.getIn().getHeader(Client.REQUEST_CONTEXT);
        AddressingProperties maps = (AddressingProperties) requestContext.get(CLIENT_ADDRESSING_PROPERTIES);
        String messageId = maps.getMessageID().getValue(); 
        
        assertEquals("123456789", messageId);

    }

    @Test
    @DirtiesContext
    public void testSendNonCurrentUser() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestNonCurrent.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                        "src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForNonCurrentUser.xml"));
        
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
 
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testSendNonCurrentUserFromOriWithoutPrivacyPolicy() throws Exception {
    	// Read the access control request file from the file system
    	File inputFile = new File(
    			"src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestNonCurrentNoPolicyOri.xml");
    	String requestBody = FileUtils.readFileToString(inputFile);
    	
    	@SuppressWarnings("unchecked")
    	List<String> expectedBody = FileUtils.readLines(new File(
    			"src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForNonCurrentUserNoPolicyOri.xml"));
    	
    	resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
    	
    	Map<String, Object> headers = SoapMessageUtils.createHeaders();
    	template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);
    	resultEndpoint.assertIsSatisfied();
    }
    
    @Test
    @DirtiesContext
    public void testSendEmptyUser() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestEmpty.xml");
        String requestBody = FileUtils.readFileToString(inputFile);
        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                        "src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForEmptyFedID.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));

        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testSendNewUser() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestNewUser.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                "src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForNewUser.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
        
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        resultEndpoint.assertIsSatisfied();
    }
    
    @Test
    @DirtiesContext
    public void testSendRequestedResourceNotAvailable() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/policyBasedAccessControl/IdentityBasedAccessControlRequestResourceNotAvailable.xml");
        String requestBody = FileUtils.readFileToString(inputFile);
        
        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                "src/test/resources/xml/policyBasedAccessControl/AccessControlResponseForResoureNotAvailableError.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));

        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody,headers);
        
        resultEndpoint.assertIsSatisfied();
    }

}
