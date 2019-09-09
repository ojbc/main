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

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackIdentityHistory;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/file-drop-routes.xml",
        "classpath:META-INF/spring/criminal-history-routes.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",  
        "classpath:META-INF/spring/error-handlers.xml",  
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
        
        })
public class TestFbiRapbackIdentityHistoryAuditProcessor {

	@Resource
	private FbiRapbackIdentityHistoryAuditProcessor fbiRapbackIdentityHistoryAuditProcessor;
	
	@Resource
	private EnhancedAuditDAOImpl enhancedAuditDAOImpl;
	
	@Test
	public void testAuditSubscription() throws Exception
	{
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
	    
	    ex.getIn().setHeader("pathToRequestFile", "/some/path/request");

	    Document input = XmlUtils.parseFileToDocument(new File("src/test/resources/input/Template(RBIHS)RapBackIdentityHistorySummaryRequest.xml"));
	    assertNotNull(input);
	    ex.getIn().setBody(input);

	    fbiRapbackIdentityHistoryAuditProcessor.auditFbiRapbackIdentityHistoryRequest(ex);
	    
	    FederalRapbackIdentityHistory federalRapbackIdentityHistory = fbiRapbackIdentityHistoryAuditProcessor.returnFederalRapbackIdentityHistory(ex, input);
	    
	    assertNotNull(federalRapbackIdentityHistory.getRequestSentTimestamp());
	    
	    assertEquals("/some/path/request", federalRapbackIdentityHistory.getPathToRequestFile());
	    assertEquals("62760NY12", federalRapbackIdentityHistory.getUcn());
	    assertEquals("123456", federalRapbackIdentityHistory.getFbiNotificationId());
	    assertEquals("123456", federalRapbackIdentityHistory.getFbiSubscriptionId());
	    assertNotNull(federalRapbackIdentityHistory.getRequestSentTimestamp());
	    assertEquals("RBIHS", federalRapbackIdentityHistory.getTransactionCategoryCodeRequest());
	    assertEquals("5683956839", federalRapbackIdentityHistory.getTransactionControlReferenceIdentification());
	    
	    fbiRapbackIdentityHistoryAuditProcessor.auditFbiRapbackIdentityHistoryRequest(ex);

	    //TODO: Add DAO to retrieve entry
	    
	}
	
}
