package org.ojbc.intermediaries.sn.subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.annotation.Resource;

import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestUnSubscriptionRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
		})
@DirtiesContext
public class SubscriptionProcessorTest {

    private static final class TestSubscriptionProcessor extends SubscriptionProcessor {
        private String topic;
        @Override
        public UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(Message msg) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        protected SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(Message msg) throws Exception {
            // TODO Auto-generated method stub
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

    private static final Log log = LogFactory.getLog(SubscriptionProcessorTest.class);

    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    private TestSubscriptionProcessor unit;
    private FaultMessageProcessor faultProcessor;

    @Before
    public void setUp() throws Exception {
        faultProcessor = new FaultMessageProcessor();
        unit = new TestSubscriptionProcessor();
        unit.setFaultMessageProcessor(faultProcessor);
        unit.setSubscriptionSearchQueryDAO(subscriptionSearchQueryDAO);
    }

    @Test
    public void testProcessUnSubscriptionRequest_noRowsUnsubscribed() throws Exception {
        Exchange e = new DefaultExchange((CamelContext) null);
        Message m = e.getIn();
        m.setHeader("operationName", "Unsubscribe");
        ArrestUnSubscriptionRequest request = new ArrestUnSubscriptionRequest("{http://ojbc.org/wsn/topics}:person/arrest", Arrays.asList("emailAddress"), "systemName", "12345", "subjectId");
        unit.setTopic(request.getTopic());
        unit.processUnSubscriptionRequest(request, m, "SYSTEM");
        assertTrue(e.getOut().isFault());
    }

    @Test
    public void testProcessUnSubscriptionRequest_invalidTopic() throws Exception {
        Message m = new DefaultMessage();
        ArrestUnSubscriptionRequest request = new ArrestUnSubscriptionRequest("arbitraryTopic", Arrays.asList("emailAddress"), "systemName", "12345", "subjectId");
        unit.setTopic("validTopic");
        unit.processUnSubscriptionRequest(request, m, "SYSTEM");
        assertEquals("<ResourceUnknownFault xmlns=\"http://docs.oasis-open.org/wsrf/bf-2\"/>", m.getBody());
    }

}
