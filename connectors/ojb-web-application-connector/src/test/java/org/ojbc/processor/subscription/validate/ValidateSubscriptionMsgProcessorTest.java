package org.ojbc.processor.subscription.validate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class ValidateSubscriptionMsgProcessorTest {

	@Test	
	public void testCreateUnsubscriptionMessage() throws Exception{
		
		Document doc = RequestMessageBuilderUtilities.createValidateSubscriptionRequest("123456", "topic");
		
		String subscriptionIdentificationId = XmlUtils.xPathStringSearch(doc, 
				"b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		
		assertEquals("123456", subscriptionIdentificationId);	
		
		String topic = XmlUtils.xPathStringSearch(doc, "b-2:Validate/b-2:TopicExpression[@Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete']");  
		
		assertEquals("topic", topic);	
	}
		
}
