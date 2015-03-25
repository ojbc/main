package org.ojbc.web.model.person.search;


import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class PersonSearchRequestTest {
	
	private static final Log log = LogFactory.getLog( PersonSearchRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPersonSearchRequest() throws Exception
	{
		
		PersonSearchRequest psr = PersonSearchRequestTestUtils.createPersonSearchRequestModel();
		
		Assert.assertNotNull(psr);
		
		Document doc = RequestMessageBuilderUtilities.createPersonSearchRequest(psr);
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/personSearchRequest/personSearchRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(OJBUtils.getStringFromDocument(doc), expectedResponseAsString);
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());		
	}
	
	@Test
	public void testPersonSearchRequestAbsoluteWeightHeight() throws Exception
	{
		
		PersonSearchRequest psr = PersonSearchRequestTestUtils.createPersonSearchRequestModelAbsoluteWeightHeight();
		
		Assert.assertNotNull(psr);
		
		Document doc = RequestMessageBuilderUtilities.createPersonSearchRequest(psr);
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/personSearchRequest/personSearchRequestHeightWeight.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(OJBUtils.getStringFromDocument(doc), expectedResponseAsString);
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());		
	}
	
	
}
