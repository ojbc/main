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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.REPORT_FEDERAL_SUBSCRIPTION_CREATION;
import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.REPORT_FEDERAL_SUBSCRIPTION_UPDATE;

import java.time.LocalDate;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import jakarta.annotation.Resource;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"})
public class TestSubscriptionReportingService {
	
	private static final Log log = LogFactory.getLog( TestSubscriptionReportingService.class );

	@Autowired
	RapbackDAO rapbackDAO;

	@Resource
	FbiRapbackDao fbiSubscriptionDao;
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
	@EndpointInject(value = "mock:failedInvocation")
    protected MockEndpoint failedInvocationEndpoint;
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@BeforeEach
	public void setUp() throws Exception {
		
		//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		AdviceWith.adviceWith(context, "subscription_reporting_service", route -> {
			route.replaceFromWith("direct:subscriptionReportingServiceEndpoint");
		});
		
    	context.start();
	}	
	
	
	@Test
	@DirtiesContext
	public void testSubscriptionReportingService() throws Exception
	{
		FbiRapbackSubscription fbiRapbackSubscriptionBeforeCreationReporting = fbiSubscriptionDao.getFbiRapbackSubscription("F", "1234567898"); 
		assertNull(fbiRapbackSubscriptionBeforeCreationReporting);
		
    	Exchange senderExchange = MessageUtils.createSenderExchange(context, 
    			"src/test/resources/xmlInstances/subscriptionReporting/RapBackSubscriptionCreationReport.xml");
	    
    	senderExchange.getIn().setHeader("operationName", REPORT_FEDERAL_SUBSCRIPTION_CREATION);
    	
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionReportingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		FbiRapbackSubscription fbiRapbackSubscriptionAfterReporting = fbiSubscriptionDao.getFbiRapbackSubscription("F", "1234567898");
		assertNotNull(fbiRapbackSubscriptionAfterReporting);
		log.info(fbiRapbackSubscriptionAfterReporting.toString());
		
		assertEquals("123456", fbiRapbackSubscriptionAfterReporting.getFbiSubscriptionId());
		assertEquals("F", fbiRapbackSubscriptionAfterReporting.getRapbackCategory());
		assertEquals("5", fbiRapbackSubscriptionAfterReporting.getSubscriptionTerm());
		assertEquals(LocalDate.of(2010,2,24), fbiRapbackSubscriptionAfterReporting.getRapbackExpirationDate());
		assertEquals(LocalDate.of(2015,1,1), fbiRapbackSubscriptionAfterReporting.getRapbackTermDate());
		assertEquals(LocalDate.of(2011,01,25), fbiRapbackSubscriptionAfterReporting.getRapbackStartDate());
		assertEquals(Boolean.FALSE, fbiRapbackSubscriptionAfterReporting.getRapbackOptOutInState());
		assertEquals("2", fbiRapbackSubscriptionAfterReporting.getRapbackActivityNotificationFormat());
		assertEquals("1234567898", fbiRapbackSubscriptionAfterReporting.getUcn());
		assertEquals(Integer.valueOf(62727), fbiRapbackSubscriptionAfterReporting.getStateSubscriptionId());
		
		List<SubsequentResults> subsequentResults = rapbackDAO.getSubsequentResultsByUcn("1234567898");
		assertEquals(1, subsequentResults.size());
		assertEquals("This is a criminal history", new String(subsequentResults.get(0).getRapSheet()));
		assertEquals("000001820140729014008339998", subsequentResults.get(0).getTransactionNumber());
		log.info("Rap Sheet: " + new String(subsequentResults.get(0).getRapSheet()));
		
    	Exchange fbiSubscriptionUpdateExchange = MessageUtils.createSenderExchange(context, 
    			"src/test/resources/xmlInstances/subscriptionReporting/RapbackSubscriptionUpdateReport.xml");
	    
    	fbiSubscriptionUpdateExchange.getIn().setHeader("operationName", REPORT_FEDERAL_SUBSCRIPTION_UPDATE);
    	
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnUpdateExchange = template.send("direct:subscriptionReportingServiceEndpoint", fbiSubscriptionUpdateExchange);
		
		//Use getException to see if we received an exception
		if (returnUpdateExchange.getException() != null)
		{	
			throw new Exception(returnUpdateExchange.getException());
		}
		
		FbiRapbackSubscription fbiRapbackSubscriptionAfterUpdate = fbiSubscriptionDao.getFbiRapbackSubscription("F", "1234567898");
		assertNotNull(fbiRapbackSubscriptionAfterUpdate);
		log.info(fbiRapbackSubscriptionAfterUpdate.toString());
		
		assertEquals("123456", fbiRapbackSubscriptionAfterUpdate.getFbiSubscriptionId());
		assertEquals("F", fbiRapbackSubscriptionAfterUpdate.getRapbackCategory());
		assertEquals("5", fbiRapbackSubscriptionAfterUpdate.getSubscriptionTerm());
		assertEquals(LocalDate.of(2010,2,24), fbiRapbackSubscriptionAfterUpdate.getRapbackExpirationDate());
		assertEquals(LocalDate.of(2015,1,1), fbiRapbackSubscriptionAfterUpdate.getRapbackTermDate());
		assertEquals(LocalDate.of(2011,1,25), fbiRapbackSubscriptionAfterUpdate.getRapbackStartDate());
		assertEquals(Boolean.FALSE, fbiRapbackSubscriptionAfterUpdate.getRapbackOptOutInState());
		assertEquals("2", fbiRapbackSubscriptionAfterUpdate.getRapbackActivityNotificationFormat());
		assertEquals("1234567898", fbiRapbackSubscriptionAfterUpdate.getUcn());
		assertEquals(Integer.valueOf(62727), fbiRapbackSubscriptionAfterReporting.getStateSubscriptionId());
		
		List<SubsequentResults> subsequentResultsAfterUpdate = rapbackDAO.getSubsequentResultsByUcn("1234567898");
		assertEquals(2, subsequentResultsAfterUpdate.size());
		log.info("Rap Sheet 2: " + new String(subsequentResultsAfterUpdate.get(1).getRapSheet()));
		assertEquals("This is a criminal history", new String(subsequentResultsAfterUpdate.get(1).getRapSheet()));
		assertEquals("000001820140729014008339998", subsequentResultsAfterUpdate.get(0).getTransactionNumber());

	}
	
}
