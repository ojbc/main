package org.ojbc.web.portal.controllers.helpers;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;



public class SubscriptionQueryResultsProcessorTest {
	
	@Test
	public void testParsSubQueryResults() throws Exception{
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		
		Document sampleSubQueryResultsDoc = getSampleSubQueryResultsDoc();		
		
		SubscriptionQueryResultsProcessor subQueryResultsProcessor = new SubscriptionQueryResultsProcessor();
		
		SubscriptionQueryResults subQueryResults = subQueryResultsProcessor.parseSubscriptionQueryResults(sampleSubQueryResultsDoc);
				
		Date dStartDate = subQueryResults.getSubscriptionStartDate();
		String sStartDate = sdf.format(dStartDate);
		assertEquals("2014-04-01", sStartDate);
				
		Date dEndDate = subQueryResults.getSubscriptionEndDate();
		String sEndDate = sdf.format(dEndDate);
		assertEquals("2014-05-01", sEndDate);
				
		String topic = subQueryResults.getSubscriptionType();
		assertEquals("{http://ojbc.org/wsn/topics}:person/arrest", topic);				
				
		String sFullName = subQueryResults.getFullName();
		assertEquals("Mary N Billiot", sFullName);
		
		//TODO assert dob
//		Date dDob = subQueryResults.getDateOfBirth();
				
		List<String> emailList = subQueryResults.getEmailList();				
		boolean hasEmail1 = emailList.contains("officer@gmail.com");
		assertEquals(true, hasEmail1);
				
		String sStateId = subQueryResults.getStateId();
		assertEquals("A2588583", sStateId);	
		
		String systemId = subQueryResults.getSystemId();
		assertEquals("62726", systemId);
	}
	
	private Document getSampleSubQueryResultsDoc() throws Exception{
		
		File rapSheetFile = new File("src/test/resources/subscriptions/SampleSubscriptionQueryResults.xml");
		InputStream rapsheetInStream = new FileInputStream(rapSheetFile);				
		DocumentBuilder docBuilder = getDocBuilder();		
		Document rapSheetDoc = docBuilder.parse(rapsheetInStream);	

		return rapSheetDoc;
	}
	
	private DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder docBuilder;
		docBuilder = fact.newDocumentBuilder();
		
		return docBuilder;
	}	

}


