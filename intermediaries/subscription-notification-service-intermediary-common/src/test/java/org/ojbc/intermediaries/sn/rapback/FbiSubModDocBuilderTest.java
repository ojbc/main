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
package org.ojbc.intermediaries.sn.rapback;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubModDocBuilder;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FbiSubModDocBuilderTest {
	
	@Before
	public void init() {
		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void testFbiSubModDocBuilder() throws Exception{
		
		FbiSubModDocBuilder fbiSubModDocBuilder = new FbiSubModDocBuilder();
		
		Document subscriptionRequestDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/subscribeRequestWithRapbackData.xml"));
		
		Document fbiSubModDoc = fbiSubModDocBuilder.buildFbiSubModDoc(getSampleFbiSubscription(), subscriptionRequestDoc);		
				
		XmlUtils.printNode(fbiSubModDoc.getDocumentElement());
		Document expectedSubModDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/FbiSubMod.xml"));
				
		Diff diff = new Diff(expectedSubModDoc, fbiSubModDoc);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);		
		@SuppressWarnings("unchecked")
		List<Difference> diffList =  detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		if(diffCount > 0){
			XmlUtils.printNode(fbiSubModDoc);
		}		
		
		Assert.assertEquals(detailedDiff.toString(), 0, diffCount);		
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
		fbiRapbackSubscription.setUcn("123456789");
		fbiRapbackSubscription.setStateSubscriptionId(Integer.valueOf(66));
		
		return fbiRapbackSubscription;
	}

}
