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
package org.ojbc.intermediaries.sn.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.cxf.binding.soap.SoapFault;
import org.junit.jupiter.api.BeforeEach;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestUnSubscriptionRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@CamelSpringTest
@SpringJUnitConfig(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class SubscriptionProcessorTest {

    private static final class TestSubscriptionProcessor extends SubscriptionProcessor {
        private String topic;
        @Override
        public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(Message msg) throws Exception {
            return null;
        }
        @Override
        protected SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(Message msg) throws Exception {
            return null;
        }
        @Override
        protected String getTopic() {
            return topic;
        }
        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    private TestSubscriptionProcessor unit;
    private FaultMessageProcessor faultProcessor;

    @BeforeEach
    public void setUp() throws Exception {
        faultProcessor = new FaultMessageProcessor();
        unit = new TestSubscriptionProcessor();
        unit.setFaultMessageProcessor(faultProcessor);
        unit.setSubscriptionSearchQueryDAO(subscriptionSearchQueryDAO);
    }

    @Test
    public void testProcessUnSubscriptionRequest_noRowsUnsubscribed() throws Exception {
        Exchange e = new DefaultExchange( new DefaultCamelContext());
        Message m = e.getIn();
        m.setHeader("operationName", "Unsubscribe");
        ArrestUnSubscriptionRequest request = new ArrestUnSubscriptionRequest("{http://ojbc.org/wsn/topics}:person/arrest", Arrays.asList("emailAddress"), "systemName", "12345", "subjectId","first", "last", "10-10-2001");
        unit.setTopic(request.getTopic());
        unit.processUnSubscriptionRequest(request, m, "SYSTEM");
        assertTrue(e.getMessage().getBody() instanceof SoapFault);
    }

    @Test
    public void testProcessUnSubscriptionRequest_invalidTopic() throws Exception {
        Message m = new DefaultMessage(new DefaultCamelContext());
        ArrestUnSubscriptionRequest request = new ArrestUnSubscriptionRequest("arbitraryTopic", Arrays.asList("emailAddress"), "systemName", "12345", "subjectId","first", "last", "10-10-2001");
        unit.setTopic("validTopic");
        unit.processUnSubscriptionRequest(request, m, "SYSTEM");
        assertEquals("<ResourceUnknownFault xmlns=\"http://docs.oasis-open.org/wsrf/bf-2\"/>", m.getBody());
    }

}
