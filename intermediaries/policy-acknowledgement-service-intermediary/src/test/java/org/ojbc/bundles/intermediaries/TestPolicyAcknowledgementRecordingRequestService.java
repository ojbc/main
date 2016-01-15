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
package org.ojbc.bundles.intermediaries;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.policyacknowledgement.dao.Policy;
import org.ojbc.policyacknowledgement.dao.PolicyDAOImpl;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
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
public class TestPolicyAcknowledgementRecordingRequestService {

    @Autowired
    private ModelCamelContext context;
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Autowired
    private PolicyDAOImpl policyDAO;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @Before
    public void setUp() throws Exception {
        // Advise the Policy Acknowledgement Recording Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
        context.getRouteDefinition("policyAcknowledgementRecordingRequestRoute")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:acknowlegePolicies");

                        interceptSendToEndpoint(
                                "policyAcknowlegementRecordingResponseServiceEndpoint")
                                .to("mock:result")
                                .log("Called Policy Acknowledgement Recording Response Handler")
                                .stop();
                    }
                });

        context.start();
    }

    @Test
    @DirtiesContext
    public void testAcknowledgePoliciesWithNullCxfMessage() throws Exception {
        // Read the policy acknowledgement recording request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestForAllPolicies.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestWithNoSAMLResponse.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
        
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        
        template.sendBodyAndHeaders("direct:acknowlegePolicies", requestBody,
                headers);
        resultEndpoint.assertIsSatisfied();
        
        Exchange exchange = resultEndpoint.getExchanges().get(0);
        String opName = (String)exchange.getIn().getHeader("operationName");
        assertEquals("SubmitAcknowledgementRecordingResponse", opName);
        
        String opNamespace = (String)exchange.getIn().getHeader("operationNamespace");
        assertEquals("http://ojbc.org/Services/WSDL/PolicyAcknowledgementRecordingResponseService/1.0", opNamespace);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> requestContext = (Map<String, Object>)exchange.getIn().getHeader(Client.REQUEST_CONTEXT);
        AddressingProperties maps = (AddressingProperties) requestContext.get(CLIENT_ADDRESSING_PROPERTIES);
        String messageId = maps.getMessageID().getValue(); 
        
        assertEquals("123456789", messageId);
    }

    @Test
    @DirtiesContext
    public void testAcknowledgePoliciesWithNoFederationId() throws Exception {
        // Read the policy acknowledgement recording request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestForAllPolicies.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "");

        org.apache.cxf.message.Message message = createSamlAssertionMessageWithAttributes(customAttributes);

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestWithEmptyFedIDResponse.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
        
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        headers.put(CxfConstants.CAMEL_CXF_MESSAGE, message);
        template.sendBodyAndHeaders("direct:acknowlegePolicies", requestBody,
                headers);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testAcknowledgePoliciesWithNonCurrentUserId() throws Exception {
        // Read the policy acknowledgement recording request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestForAllPolicies.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:hpotter");
        customAttributes.put(SamlAttribute.EmployerORI, "H00000001");

        org.apache.cxf.message.Message message = createSamlAssertionMessageWithAttributes(customAttributes);
        
        //Pass the ORIs in the SAML assertion to the DAO method. 
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001"); 
        assertEquals(outStandingPoliciesForOwen.size(), 2); 
        assertTrue(outStandingPoliciesForOwen.get(0).getId() == 1); 
        assertTrue(outStandingPoliciesForOwen.get(1).getId() == 3); 

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestForAllPoliciesResponse.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
        
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        headers.put(CxfConstants.CAMEL_CXF_MESSAGE, message);
        template.sendBodyAndHeaders("direct:acknowlegePolicies", requestBody,
                headers);
        //Pass the ORIs in the SAML assertion to the DAO method. 
        List<Policy> outStandingPoliciesForOwenAfter = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001"); 
        assertTrue(outStandingPoliciesForOwenAfter.isEmpty()); 
        
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testAcknowledgePoliciesWithFalseIndicator() throws Exception {
        // Read the policy acknowledgement recording request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestWithFalseIndicator.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:hpotter");
        customAttributes.put(SamlAttribute.EmployerORI, "H00000001");

        org.apache.cxf.message.Message message = createSamlAssertionMessageWithAttributes(customAttributes);
        
        //Pass the ORIs in the SAML assertion to the DAO method. 
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001"); 
        assertEquals(outStandingPoliciesForOwen.size(), 2); 
        assertTrue(outStandingPoliciesForOwen.get(0).getId() == 1); 
        assertTrue(outStandingPoliciesForOwen.get(1).getId() == 3); 

        @SuppressWarnings("unchecked")
        List<String> expectedBody = FileUtils.readLines(new File(
                        "src/test/resources/xml/acknowlegePolicies/AcknowledgementRecordingRequestWithFalseIndicatorResponse.xml"));
        resultEndpoint.expectedBodiesReceived(expectedBody.get(18));
 
        Map<String, Object> headers = SoapMessageUtils.createHeaders();
        headers.put(CxfConstants.CAMEL_CXF_MESSAGE, message);
        template.sendBodyAndHeaders("direct:acknowlegePolicies", requestBody,
                headers);
        
        //Pass the ORIs in the SAML assertion to the DAO method. 
        List<Policy> outStandingPoliciesForOwenAfter = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001"); 
        assertEquals(outStandingPoliciesForOwenAfter.size(), 2); 
        assertTrue(outStandingPoliciesForOwenAfter.get(0).getId() == 1); 
        assertTrue(outStandingPoliciesForOwenAfter.get(1).getId() == 3); 
        resultEndpoint.assertIsSatisfied();

    }
    
    private org.apache.cxf.message.Message createSamlAssertionMessageWithAttributes(
            Map<SamlAttribute, String> customAttributes) throws Exception {
        org.apache.cxf.message.Message message = new MessageImpl();

        Assertion samlToken = SAMLTokenUtils
                .createStaticAssertionWithCustomAttributes(
                        "https://idp.ojbc-local.org:9443/idp/shibboleth",
                        SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS,
                        SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true,
                        true, customAttributes);
        SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(
                new SamlAssertionWrapper(samlToken));
        message.put("wss4j.principal.result", principal);
        return message;
    }

}
