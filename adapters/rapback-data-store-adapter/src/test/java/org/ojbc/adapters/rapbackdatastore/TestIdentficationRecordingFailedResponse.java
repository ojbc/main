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

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.processor.IdentificationRequestReportProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.xml.sax.SAXException;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestIdentficationRecordingFailedResponse {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( TestIdentficationRecordingFailedResponse.class );

	private static final String TRANSACTION_NUMBER = "000001820140729014008340000";
	
	@Autowired
	RapbackDAO rapbackDAO;

	@Autowired
	IdentificationRequestReportProcessor identificationRequestReportProcessor;
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
	@EndpointInject(value = "mock:failedInvocation")
    protected MockEndpoint failedInvocationEndpoint;
	
	@Value("${rapbackDatastoreAdapter.IdentificationRecordingInputDirectory}")
	private String failedFolderDirectory; 
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@BeforeEach
	public void setUp() throws Exception {
		
		assertNotNull(identificationRequestReportProcessor);
		
		//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		AdviceWith.adviceWith(context, "identification_recording_service", route -> {
			route.replaceFromWith("direct:identificationRecordingServiceEndpoint");
		});
    	
    	context.start();
	}	
	
	
	@Test
	@DirtiesContext
	@Disabled
	public void testIdentificationRecordingRequestAndResultsSuccess() throws Exception
	{
		civilRecordingRequestSuccessResponseSentToFailedFolder(); 
	}

	private void civilRecordingRequestSuccessResponseSentToFailedFolder() throws Exception, IOException,
			InterruptedException, SAXException {
		
		File failedResponseFolder = new File (failedFolderDirectory + "/failed/identificationResponse");
		FileUtils.cleanDirectory(new File (failedFolderDirectory + "/failed/"));
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(TRANSACTION_NUMBER); 
		assertNull(identificationTransaction);
		
		Exchange senderExchange = MessageUtils.createSenderExchange(context, 
				"src/test/resources/xmlInstances/identificationReport/person_identification_request_state_civil.xml");
		
		senderExchange.getIn().setHeader("operationName", "RecordPersonFederalIdentificationRequest");
		
		//Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:identificationRecordingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}
		
		
		assertEquals(FileUtils.listFiles(failedResponseFolder, null, false).size(), Long.valueOf(1).longValue());
		
	}
	
}
