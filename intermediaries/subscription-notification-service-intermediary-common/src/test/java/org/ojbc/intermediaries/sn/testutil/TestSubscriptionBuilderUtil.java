package org.ojbc.intermediaries.sn.testutil;

import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.util.SubscriptionResponseBuilderUtil;
import org.ojbc.util.xml.XmlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class TestSubscriptionBuilderUtil {

	private static final Log log = LogFactory.getLog(SubscriptionProcessor.class);

	@Test
	public void createSubscribeResponse() throws Exception
	{
		String subscriptionResponse = SubscriptionResponseBuilderUtil.createSubscribeResponse();
		log.debug(subscriptionResponse);
	
		XmlUtils.validateInstanceNonNIEMXsd("../OJB_Resources/src/main/resources/service-specifications/Subscription_Notification_Service/WSDL/wsn/b-2.xsd", subscriptionResponse);
	}
	
}
