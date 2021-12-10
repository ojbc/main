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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.bundles.adapters.fbi.ebts.application.FbiElectronicBiometricTransmissionSpecificationAdapterApplication;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;

@CamelSpringBootTest
@SpringBootTest(classes=FbiElectronicBiometricTransmissionSpecificationAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class TestFbiSubscriptionAuditProcessor {

	@Resource
	private FbiSubscriptionAuditProcessor fbiSubscriptionAuditProcessor;
	
	@Resource
	private EnhancedAuditDAOImpl enhancedAuditDAOImpl;
	
	@Test
	public void testAuditSubscription() throws Exception
	{
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
	    
	    ex.getIn().setHeader("pathToRequestFile", "/some/path/request");
	    ex.getIn().setHeader("controlID", "123456789");
	    
	    Document input = XmlUtils.parseFileToDocument(new File("src/test/resources/output/EBTS-RapBack-Criminal-Subscription-Request.xml"));
	    assertNotNull(input);
	    ex.getIn().setBody(input);
	    
	    fbiSubscriptionAuditProcessor.auditFBISubscriptionRequest(ex);

	    ex = new DefaultExchange(ctx);

	    ex.getIn().setHeader("pathToResponseFile", "/some/path/response");
	    ex.getIn().setHeader("trxCatCode", "ERRA");
	    ex.getIn().setHeader("transactionControlReferenceIdentification", "123456789");
	    
	    input = XmlUtils.parseFileToDocument(new File("src/test/resources/input/FBI_SUBSCRIPTION_RESPONSE_ERRA.xml"));
	    assertNotNull(input);
	    ex.getIn().setBody(input);

	    fbiSubscriptionAuditProcessor.auditFBISubscriptionResponse(ex);
	    
	    FederalRapbackSubscription federalRapbackSubscription = enhancedAuditDAOImpl.retrieveFederalRapbackSubscriptionFromTCN("123456789");
	    
	    Integer errorEntry = enhancedAuditDAOImpl.retrieveFederalRapbackSubscriptionError("S128483");
	    assertNotNull(errorEntry);
	    
	    assertEquals("/some/path/request", federalRapbackSubscription.getPathToRequestFile());
	    assertEquals("/some/path/response", federalRapbackSubscription.getPathToResponseFile());
	    assertEquals("ERRA", federalRapbackSubscription.getTransactionCategoryCodeResponse());
	    assertEquals("RBSCRM", federalRapbackSubscription.getTransactionCategoryCodeRequest());
	    assertEquals("123456789", federalRapbackSubscription.getTransactionControlReferenceIdentification());
	    assertEquals("A398118900", federalRapbackSubscription.getSid());
	    assertEquals("S128483", federalRapbackSubscription.getStateSubscriptionId());
	    assertEquals("CI", federalRapbackSubscription.getSubscriptonCategoryCode());
	    assertEquals("This is the transaction text: RB001: some error | This is the second transaction text", federalRapbackSubscription.getTransactionStatusText());
	    
	    ex.getIn().setHeader("trxCatCode", "RBMNTR");
	    
	    //This response will 'resolve' errors by sending back a good response
	    input = XmlUtils.parseFileToDocument(new File("src/test/resources/input/FBI_Subscription_Response_C99999999.xml"));
	    assertNotNull(input);
	    ex.getIn().setBody(input);

	    fbiSubscriptionAuditProcessor.auditFBISubscriptionResponse(ex);

	    errorEntry = enhancedAuditDAOImpl.retrieveFederalRapbackSubscriptionError("S128483");
	    assertNull(errorEntry);

	    
	}
	
}
