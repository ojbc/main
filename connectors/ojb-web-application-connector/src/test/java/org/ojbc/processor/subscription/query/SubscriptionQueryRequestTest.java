package org.ojbc.processor.subscription.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SubscriptionQueryRequestTest {

	
	@Test
	public void subscriptionQueryRequest() throws Exception {
		
    	//create and populate a person search request POJO
		DetailsRequest subscriptionQueryRequest = new DetailsRequest();
		
		subscriptionQueryRequest.setIdentificationID("62720");
		subscriptionQueryRequest.setIdentificationSourceText("subscriptions");

		Document subscriptionQueryDocument = RequestMessageBuilderUtilities.createSubscriptionQueryRequest(subscriptionQueryRequest);
		
		XmlUtils.printNode(subscriptionQueryDocument);
		
		assertNotNull(subscriptionQueryDocument);
		
        Node rootResultsNode = XmlUtils.xPathNodeSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest");
        assertNotNull(rootResultsNode);        
        
        String identificationId = XmlUtils.xPathStringSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest/sqreq-ext:SubscriptionIdentification/nc:IdentificationID");
        assertEquals("62720", identificationId);
        
        String identificationSourceText = XmlUtils.xPathStringSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest/sqreq-ext:SubscriptionIdentification/nc:IdentificationSourceText");
        assertEquals("subscriptions", identificationSourceText);
        
	}	
	
}
