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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubModDocBuilder;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubscriptionModification;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FbiSubModDocBuilderTest {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");		
	
	@Before
	public void init() {
		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void testFbiSubModDocBuilder() throws Exception{
		
		FbiSubscriptionModification fbiSubMod = getSampleFbiSubMod();
		
		FbiSubModDocBuilder fbiSubModDocBuilder = new FbiSubModDocBuilder();
		
		Document fbiSubModDoc = fbiSubModDocBuilder.buildFbiSubModDoc(fbiSubMod);		
				
		Document expectedSubModDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/FbiSubMod.xml"));
				
		Diff diff = new Diff(expectedSubModDoc, fbiSubModDoc);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);		
		List<Difference> diffList =  detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		if(diffCount > 0){
			XmlUtils.printNode(fbiSubModDoc);
		}		
		
		Assert.assertEquals(detailedDiff.toString(), 0, diffCount);		
	}
		
	
	private FbiSubscriptionModification getSampleFbiSubMod() throws Exception{
		
		FbiSubscriptionModification fbiSubMod = new FbiSubscriptionModification();
		
		fbiSubMod.setPersonFbiUcnId("123456789");		
		fbiSubMod.setReasonCode("CI");		
		fbiSubMod.setSubscriptionFbiId("1234567");
				
		Date subModEndDate = SDF.parse("2015-04-01");		
		fbiSubMod.setSubModEndDate(subModEndDate);	
		
		return fbiSubMod;
	}

}
