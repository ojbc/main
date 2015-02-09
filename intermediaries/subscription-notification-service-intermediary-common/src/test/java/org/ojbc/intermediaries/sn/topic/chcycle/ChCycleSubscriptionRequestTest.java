package org.ojbc.intermediaries.sn.topic.chcycle;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;

import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

public class ChCycleSubscriptionRequestTest {
		
	Map<String, String> namespaceUris;
	
	@Before	
	public void setup() {
		
		namespaceUris = new HashMap<String, String>();
		
		namespaceUris.put("wsnb2", "http://docs.oasis-open.org/wsn/b-2");
		namespaceUris.put("sm", "http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0");
		namespaceUris.put("smext", "http://ojbc.org/IEPD/Extensions/Subscription/1.0");
		namespaceUris.put("nc20", "http://niem.gov/niem/niem-core/2.0");
	}

	@Test
	public void test() throws Exception {
		
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		ChCycleSubscriptionRequest sub = new ChCycleSubscriptionRequest(message, null);		
		
		assertThat(sub.getSubscriptionQualifier(), is("302593"));
		
		assertThat(sub.getSubjectName(), is("Maggie Simpson"));
		
		//Assert size of set and only entry
		assertThat(sub.getEmailAddresses().size(), is(1));
		assertThat(sub.getEmailAddresses().contains("po6@localhost"), is(true));
		
		assertThat(sub.getSubjectIdentifiers().size(), is(4));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Maggie"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Simpson"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1993-01-01"));
		assertThat(sub.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("302593"));
	}
	
	private Document getMessageBody() throws Exception {

		File inputFile = new File("src/test/resources/xmlInstances/subscribeSoapRequest-chCycle.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;	
	}

}
