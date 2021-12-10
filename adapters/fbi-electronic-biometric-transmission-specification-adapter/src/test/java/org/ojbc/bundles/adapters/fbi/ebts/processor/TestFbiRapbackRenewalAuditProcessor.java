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
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackRenewalNotification;
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
public class TestFbiRapbackRenewalAuditProcessor {

	@Resource
	private FbiRapbackRenewalAuditProcessor fbiRapbackRenewalAuditProcessor;
	
	@Resource
	private EnhancedAuditDAOImpl enhancedAuditDAOImpl;
	
	@Test
	public void testAuditSubscription() throws Exception
	{
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
	    
	    ex.getIn().setHeader("pathToNotificationFile", "/some/path/request");

	    Document input = XmlUtils.parseFileToDocument(new File("src/test/resources/input/Template(RBRN)RapBackRenewalNotification.xml"));
	    assertNotNull(input);
	    ex.getIn().setBody(input);

	    fbiRapbackRenewalAuditProcessor.auditFBIRapbackRenewalNotification(ex);
	    
	    FederalRapbackRenewalNotification federalRapbackRenewalNotification = fbiRapbackRenewalAuditProcessor.returnRapbackRenewalNotificaton(ex, input);
	    
	    assertNotNull(federalRapbackRenewalNotification.getNotificationRecievedTimestamp());
	    assertEquals("/some/path/request", federalRapbackRenewalNotification.getPathToNotificationFile());
	    assertEquals("1977-08-25", federalRapbackRenewalNotification.getPersonDob().toString());
	    assertEquals("ANTHONY", federalRapbackRenewalNotification.getPersonFirstName());
	    assertEquals("PAUL", federalRapbackRenewalNotification.getPersonMiddleName());
	    assertEquals("JONES", federalRapbackRenewalNotification.getPersonLastName());
	    assertEquals("2010-02-24", federalRapbackRenewalNotification.getRapbackExpirationDate().toString());
	    assertEquals("NY0303000", federalRapbackRenewalNotification.getRecordControllingAgency());
	    assertEquals("A398118900", federalRapbackRenewalNotification.getSid());
	    assertEquals("S128483", federalRapbackRenewalNotification.getStateSubscriptionId());
	    assertEquals("MATCH MADE AGAINST SUBJECTS FINGERPRINTS ON 05/01/94. PLEASE NOTIFY SUBMITTING STATE IF MATCH RESULTS", federalRapbackRenewalNotification.getTransactionStatusText());
	    assertEquals("62760NY12", federalRapbackRenewalNotification.getUcn());
	    
	    fbiRapbackRenewalAuditProcessor.auditFBIRapbackRenewalNotification(ex);

	    //TODO: Add DAO to retrieve entry
	    
	}
	
}
