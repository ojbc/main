package org.ojbc.web.model.firearm.search;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class FirearmSearchRequestTest {
	private static final Log log = LogFactory.getLog( FirearmSearchRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}
	
	@Test
	public void firearmSearchRequest() throws Exception {
		FirearmSearchRequest fsr = FirearmSearchRequestTestUtils.createFirearmSearchRequestModel(SearchFieldMetadata.ExactMatch);
		
		Assert.assertNotNull(fsr);
		
		Document doc = RequestMessageBuilderUtilities.createFirearmSearchRequest(fsr);
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/firearmSearchRequest/firearmSearchRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(OJBUtils.getStringFromDocument(doc), expectedResponseAsString);
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());		
	}
	
	@Test
	public void firearmSearchRequest_PartialSerialNum() throws Exception {
		FirearmSearchRequest fsr = FirearmSearchRequestTestUtils.createFirearmSearchRequestModel(SearchFieldMetadata.Partial);
		
		Assert.assertNotNull(fsr);
		
		Document doc = RequestMessageBuilderUtilities.createFirearmSearchRequest(fsr);
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/firearmSearchRequest/firearmSearchRequest-partialSerial.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(OJBUtils.getStringFromDocument(doc), expectedResponseAsString);
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());		
	}
}
