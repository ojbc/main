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
package org.ojbc.intermediaries.sn.tests;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.FbiSubscriptionProcessor;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubscriptionModification;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class FbiSubscriptionProcessorTest {
	
	private FbiSubscriptionProcessor fbiSubscriptionProcessor = new FbiSubscriptionProcessor();
		
	private FbiRapbackSubscription fbiRapbackSubscription;
		
	@Before
	public void setup(){		
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
    	fbiRapbackSubscription = getSampleFbiSubscription();
	}
		
	
	@Test
	public void prepareSubscriptionMaintenanceMessageTest() throws Exception{
		
		FbiSubscriptionProcessor fbiSubProcessor = new FbiSubscriptionProcessor();
		
		FbiSubscriptionModification fbiSubMod = new FbiSubscriptionModification();		
		fbiSubMod.setPersonFbiUcnId("789");
		fbiSubMod.setReasonCode("CS");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date subModEndDate = sdf.parse("2015-11-09");
				
		fbiSubMod.setSubModEndDate(subModEndDate);
		fbiSubMod.setSubscriptionFbiId("456");		
		
		Document subMaintMsgDoc = fbiSubProcessor.prepareSubscriptionMaintenanceMessage(fbiSubMod);
								
		XmlTestUtils.compareDocs("src/test/resources/xmlInstances/fbi/SubModifyMessageDoc.xml", subMaintMsgDoc);						
	}
	
	
	@Test
	public void getSubReqFromSubDocTest() throws Exception{
		
		FbiSubscriptionProcessor  fbiSubProcessor = new FbiSubscriptionProcessor();
		
		Document subDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document_WithFbiData.xml"));
		
		SubscriptionRequest subReq = fbiSubProcessor.getSubReqFromSubDoc(subDoc);
						
		String catCode = subReq.getReasonCategoryCode();
		Assert.assertEquals("CI", catCode);
		
		String subjName = subReq.getSubjectName();		
		Assert.assertEquals("John Doe", subjName);					
	}
	
	
	@Test
	public void getGreatestEndDateTest(){
		
		Subscription sub1 = new Subscription();
		
		DateTime sub1DateTime = new DateTime(new Date());
		sub1.setEndDate(sub1DateTime);
		
		Subscription sub2 = new Subscription();
		DateTime sub2DateTime = sub1DateTime.plusMillis(1);
		sub2.setEndDate(sub2DateTime);
		
		List<Subscription> subList = Arrays.asList(sub1, sub2);
		
		FbiSubscriptionProcessor  fbiSubProcessor = new FbiSubscriptionProcessor();
		
		DateTime greatestDate = fbiSubProcessor.getGreatestEndDate(subList);
				
		Assert.assertEquals(greatestDate, sub2DateTime);				
	}
	
	
	private FbiRapbackSubscription getSampleFbiSubscription(){
				
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription();
		
		Calendar startCal = Calendar.getInstance();
		startCal.set(2015, 0, 1);				
		DateTime startDate = new DateTime(startCal.getTime());						
		fbiRapbackSubscription.setRapbackStartDate(startDate);
								
		Calendar endCal = Calendar.getInstance();
		endCal.set(2016, 0, 1);						
		DateTime endDate = new DateTime(endCal.getTime());		
		fbiRapbackSubscription.setRapbackExpirationDate(endDate);
						
		fbiRapbackSubscription.setSubscriptionTerm("P1Y");
					
		fbiRapbackSubscription.setRapbackCategory("CI");		
		
		fbiRapbackSubscription.setFbiSubscriptionId("1234567");
		
		return fbiRapbackSubscription;
	}
	
	@Test
	public void testFbiSubscriptionProcessor() throws Exception{
								
		Document arrestSubDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document.xml"));		
		
		Document subXmlDocWithAppendedFbiData = fbiSubscriptionProcessor.appendFbiDataToSubscriptionDoc(arrestSubDoc, fbiRapbackSubscription);						
		
		Document expectedXmlDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document_WithFbiData.xml"));
		
							
		Diff diff = new Diff(expectedXmlDoc, subXmlDocWithAppendedFbiData);						
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		List<Difference> diffList = detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		Assert.assertEquals(detailedDiff.toString(), 0, diffCount);
	}

	@Test
	public void testFbiUnsubscription() throws Exception{
		
		FbiSubscriptionProcessor fbiSubscriptionProcessor = new FbiSubscriptionProcessor();
		
		Document unsubscribeDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/Unsubscription_FBI_Document2.xml"));	
		
		String fbiId = fbiSubscriptionProcessor.getPersonFbiUcnIdFromUnsubscribeDoc(unsubscribeDoc);
		
		Assert.assertEquals("074644NG0", fbiId);
	}
	
	@Test
	public void reasonCodeUnsubscribeTest() throws Exception{
		
		FbiSubscriptionProcessor fbiSubscriptionProcessor = new FbiSubscriptionProcessor();
						
		Document unsubscribeDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/UnsubscriptionWithReasonCode.xml"));
		
		String categoryReasonCode = fbiSubscriptionProcessor.getReasonCodeFromUnsubscribeDoc(unsubscribeDoc);
						
		Assert.assertEquals("CI", categoryReasonCode);		
	}
	
	@Test
	public void getSubReqEndDateTest() throws Exception{
		
		FbiSubscriptionProcessor fbiSubscriptionProcessor = new FbiSubscriptionProcessor();
		
		Document subDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/xmlInstances/fbi/Arrest_Subscription_Document_WithFbiData.xml"));
		
		Date dEndDate = fbiSubscriptionProcessor.getSubReqEndDate(subDoc);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String sEndDate = sdf.format(dEndDate);
		
		Assert.assertEquals("2015-06-20", sEndDate);		
	}
	
	@Test
	public void appendFbiUcnIdToUnsubscribeDocTest() throws Exception{
		
		Document unsubscribeDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/xmlInstances/fbi/input/UnsubscribeHasReasonCodeButNoFbiUcn.xml"));
		
		FbiSubscriptionProcessor fbiSubProcessor = new FbiSubscriptionProcessor();
		
		Document unsubDocWithFbiData = fbiSubProcessor.appendFbiUcnIdToUnsubscribeDoc(unsubscribeDoc, "987");			
		
		XmlTestUtils.compareDocs(unsubscribeDoc, unsubDocWithFbiData);
	}
	
	@Test
	public void appendFbiSubscriptionIdToUnsubscribeDocTest() throws Exception{
		
		Document unsubscribeDoc = XmlUtils.parseFileToDocument(
				new File("src/test/resources/xmlInstances/fbi/input/UnsubscribeHasReasonCodeButNoFbiUcn.xml"));		
		
		FbiSubscriptionProcessor fbiSubProcessor = new FbiSubscriptionProcessor();
		
		Document unsubDocWithFbiId = fbiSubProcessor.appendFbiSubscriptionIdToUnsubscribeDoc(unsubscribeDoc, "789");		
		
		XmlTestUtils.compareDocs("src/test/resources/xmlInstances/fbi/output/UnsubscribeWithFbiId.xml", unsubDocWithFbiId);
	}
			
}
