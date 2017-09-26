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

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/file-drop-routes.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",  
        "classpath:META-INF/spring/error-handlers.xml",  
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
        
        })
public class TestFbiSubscriptionAuditProcessor {

	@Resource
	private FbiSubscriptionAuditProcessor fbiSubscriptionAuditProcessor;
	
	@Resource
	private EnhancedAuditDAOImpl enhancedAuditDAOImpl;
	
	@Test
	public void testAuditSubscription()
	{
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
	    
	    ex.getIn().setHeader("pathToRequestFile", "/some/path/request");
	    ex.getIn().setHeader("controlID", "123456789");
	    
	    fbiSubscriptionAuditProcessor.auditFBISubscriptionRequest(ex);

	    ex = new DefaultExchange(ctx);

	    ex.getIn().setHeader("pathToResponseFile", "/some/path/response");
	    ex.getIn().setHeader("trxCatCode", "ERRA");
	    ex.getIn().setHeader("transactionControlReferenceIdentification", "123456789");
	    
	    fbiSubscriptionAuditProcessor.auditFBISubscriptionResponse(ex);
	    
	    FederalRapbackSubscription federalRapbackSubscription = enhancedAuditDAOImpl.retrieveFederalRapbackSubscriptionFromTCN("123456789");
	    
	    assertEquals("/some/path/request", federalRapbackSubscription.getPathToRequestFile());
	    assertEquals("/some/path/response", federalRapbackSubscription.getPathToResponseFile());
	    assertEquals("ERRA", federalRapbackSubscription.getTransactionCategoryCode());
	    assertEquals("123456789", federalRapbackSubscription.getTransactionControlReferenceIdentification());
	    
	}
	
}
