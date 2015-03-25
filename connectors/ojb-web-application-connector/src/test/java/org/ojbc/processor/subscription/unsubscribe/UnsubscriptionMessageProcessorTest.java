package org.ojbc.processor.subscription.unsubscribe;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class UnsubscriptionMessageProcessorTest {

	@Test
	
	public void testCreateUnsubscriptionMessage() throws Exception
	{
		Document doc = RequestMessageBuilderUtilities.createUnubscriptionRequest("123456", "topic");
		
		String subscriptionIdentificationId = XmlUtils.xPathStringSearch(doc, "b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		assertEquals("123456", subscriptionIdentificationId);
	}
	
}
