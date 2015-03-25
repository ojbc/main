package org.ojbc.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubscriptionDocumentBuilderTest {
		
	
	@Test
	public void testDocIsValid() throws Exception{
		
		Document subDoc = getSampleSubDoc();
		
		String b2RootXsdPath = "service-specifications/Subscription_Notification_Service/WSDL/wsn/b-2.xsd";		
						
		// directories needed for locating different xsd's needed in validating xml doc
		String b2RootXsdDir =  "service-specifications/Subscription_Notification_Service/WSDL/wsn";
		String subscribeDocXsdDirPath = "service-specifications/Subscription_Notification_Service/information_model/Subscription_Document-IEPD/xsd";		
		String neimXsdDirPath = "service-specifications/Subscription_Notification_Service/information_model/Subscription_Document-IEPD/xsd/NIEM_2.1";
		
		//note these 2 dirs are needed to pass test from mvn install but not when running from the ide
		String wsdlDir = "service-specifications/Subscription_Notification_Service/WSDL";
		String subNotDir = "service-specifications/Subscription_Notification_Service";
		
		
		List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, subscribeDocXsdDirPath, neimXsdDirPath, wsdlDir, subNotDir);		
				
		XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, subDoc);
	}
	
	

	@Test
	public void testSubMessageProcessor() throws Exception {
		
		Document subDoc = getSampleSubDoc();								
					
		Node rootSubscribeNode = XmlUtils.xPathNodeSearch(subDoc, "b-2:Subscribe");		
		assertNotNull(rootSubscribeNode);
		
		String addAddress = XmlUtils.xPathStringSearch(rootSubscribeNode, 
				"b-2:ConsumerReference/add:Address");
		assertEquals("http://www.ojbc.org/OJB/SubscribeNotify", addAddress);
	
	
		String topicExp = XmlUtils.xPathStringSearch(rootSubscribeNode, "b-2:Filter/b-2:TopicExpression");
		assertEquals("topics:person/arrest", topicExp);
		
		Node subMsgNode = XmlUtils.xPathNodeSearch(rootSubscribeNode, "submsg-exch:SubscriptionMessage");
		assertNotNull(subMsgNode);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(subMsgNode, "submsg-ext:Subject");
		assertNotNull(subjectNode);
		
		String sDob = XmlUtils.xPathStringSearch(subjectNode, "nc:PersonBirthDate/nc:Date");
		assertEquals("2000-12-25", sDob);
		
		Node personNameNode = XmlUtils.xPathNodeSearch(subjectNode, "nc:PersonName");	
		assertNotNull(personNameNode);				
		
		String sFirstName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonGivenName");
		assertEquals("Marie-laure", sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonSurName");
		assertEquals("Guillaume", sLastName);
		
		String fullName = XmlUtils.xPathStringSearch(personNameNode, "nc:PersonFullName");
		assertEquals("Marie-laure Guillaume", fullName);
		
		Node personAugNode = XmlUtils.xPathNodeSearch(subjectNode, "jxdm41:PersonAugmentation");
		assertNotNull(personAugNode);		
		
		String sid = XmlUtils.xPathStringSearch(personAugNode, "jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		assertEquals("787200", sid);
		
		NodeList emailNodeList = XmlUtils.xPathNodeListSearch(subMsgNode, "nc:ContactEmailID");
		
		List<String> retrievedEmailList = new ArrayList<String>();
		
		for(int i=0; i<emailNodeList.getLength(); i++){			
			Node iEmailNode = emailNodeList.item(i);	
			retrievedEmailList.add(iEmailNode.getTextContent());
		}		
		assertEquals(true, retrievedEmailList.contains("sam@hipd.gov"));
		assertEquals(true, retrievedEmailList.contains("sally@hipd.gov"));
		
		String sysName = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SystemName");
		assertEquals("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", sysName);
				
		String subQualId = XmlUtils.xPathStringSearch(subMsgNode, 
				"submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		assertNotNull(subQualId);
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subMsgNode, "nc:DateRange");
		
		String startDate =  XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");
		assertEquals("2014-04-08", startDate);
		
		String endDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate/nc:Date");
		assertEquals("2015-04-08", endDate);	
		
		String systemId = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		assertEquals("777400", systemId);
		
		String idSrcTxt = XmlUtils.xPathStringSearch(subMsgNode, "submsg-ext:SubscriptionIdentification/nc:IdentificationSourceText");
		assertEquals("Subscriptions", idSrcTxt);
	}
	
	
	private Document getSampleSubDoc() throws Exception{
		
		SubscriptionDocumentBuilder subMsgProcessor = new SubscriptionDocumentBuilder();
		
		SubscriptionAddRequest subAddRequest = new SubscriptionAddRequest();		
				
		List<String> emailList = Arrays.asList("sam@hipd.gov", "sally@hipd.gov");
		subAddRequest.setEmailList(emailList);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		String sDob = "2000-12-25";
		Date dDob = sdf.parse(sDob);
		subAddRequest.setDateOfBirth(dDob);
		
		String sSubStarDate = "2014-04-08";
		Date dSubStartDate = sdf.parse(sSubStarDate);		
		subAddRequest.setSubscriptionStartDate(dSubStartDate);
		
		String sSubEndDate = "2015-04-08";
		Date dSubEndDate = sdf.parse(sSubEndDate);
		subAddRequest.setSubscriptionEndDate(dSubEndDate);
		
		subAddRequest.setSubscriptionType("topics:person/arrest");
		
		subAddRequest.setFirstName("Marie-laure");
		subAddRequest.setLastName("Guillaume");
		subAddRequest.setFullName("Marie-laure Guillaume");
		subAddRequest.setStateId("787200");
		
		subAddRequest.setSystemId("777400");
		
		Document subMsgDoc = subMsgProcessor.buildSubscribeDoc(subAddRequest);		
		
		return subMsgDoc;		
	}

}
