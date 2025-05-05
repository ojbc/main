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

import static org.junit.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.crimhistoryupdate.CriminalHistoryUpdateReportingServiceApplication;
import org.ojbc.test.util.MessageUtils;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=CriminalHistoryUpdateReportingServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestCriminalHistoryUpdateReportingServiceRoute {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( TestCriminalHistoryUpdateReportingServiceRoute.class );
	
    @Autowired
    private CamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject("mock:result")
    private MockEndpoint mockEndpoint;

    @Value("${criminalHistoryReportingServiceJuvenileAdapterEndpoint}")
    public String criminalHistoryReportingServiceJuvenileAdapterEndpoint;

    @Value("${criminalHistoryReportingServiceAdapterEndpoint}")
    public String criminalHistoryReportingServiceAdapterEndpoint;

	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@BeforeEach
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
        AdviceWith.adviceWith(context, "CriminalHistoryUpdateReportingServiceDirectRoute", route -> {
            route.weaveByToUri("criminalhistoryUpdateReportingAdapterServiceEndpoint").replace().to("mock:result");
            route.weaveByToUri("direct:callNotificationBroker").remove();
        });
    	
    	context.start();
	}

    @Test
    public void testCriminalHistoryUpdateReportingServiceRoute() throws Exception {
    	
    	//Create a new exchange
    	Exchange senderExchange = MessageUtils.createSenderExchange(context, 
    			"src/test/resources/xmlInstances/prosecutionDecisionRecordingReport/juvenile.xml");
		
    	template.send("direct:processCriminalHistory", senderExchange);
    	mockEndpoint.expectedMessageCount(1);

    	Exchange receivedExchange = mockEndpoint.getReceivedExchanges().get(0);
    	
    	Assertions.assertEquals(criminalHistoryReportingServiceJuvenileAdapterEndpoint, 
    	        receivedExchange.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
    	mockEndpoint.reset(); 
        senderExchange = MessageUtils.createSenderExchange(context, 
                "src/test/resources/xmlInstances/prosecutionDecisionRecordingReport/Prosecution-Decision-Recording-Report.xml");
        
        template.send("direct:processCriminalHistory", senderExchange);
        receivedExchange = mockEndpoint.getReceivedExchanges().get(0);
        
        Assertions.assertEquals(criminalHistoryReportingServiceAdapterEndpoint, 
                receivedExchange.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
    }	
    
}
