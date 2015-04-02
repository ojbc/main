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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;
import org.w3c.dom.Document;

public class SubscriptionsControllerTest {
		
	private Logger logger = Logger.getLogger(SubscriptionsControllerTest.class.getName());
	
	private SubscriptionsController subController;	
		
	@Before
	public void init(){		
		subController = new SubscriptionsController();		
	}
	
	
	@Test
	public void testGetValidIndicatorFromValidateResponse() throws Exception{
		
		FaultableSoapResponse faultableSoapResponse = new FaultableSoapResponse();
		
		Document isValidInputDoc = getDocFromTestXmlFile("src/test/resources/subscriptions/IsValidSoapResponse.xml");
		
		faultableSoapResponse.setSoapBodyDoc(isValidInputDoc);
		
		boolean isValid = subController.getValidIndicatorFromValidateResponse(faultableSoapResponse);	
		
		Assert.assertEquals(true, isValid);
	}
	
		
	@Test
	public void testJsonPersonNameGeneration() throws Exception{
							
		String[] aExpectedAlternateNames = {"Mary N Billiot", "Maria Billiot", "Marie laure Billaume"};
		
		Document rapSheetDoc = getDocFromTestXmlFile("src/test/resources/subscriptions/MultipleAltNamesRapSheet.xml");		
						
		SubscribedPersonNames subscribedPersonNames = subController.getAllPersonNamesFromRapsheet(rapSheetDoc);
		
		String originalName = subscribedPersonNames.getOriginalName();
		Assert.assertEquals("Mary R Billiot", originalName);
		
		String[] aAlternateNames = subscribedPersonNames.getAlternateNamesList().toArray(new String[]{});		
		Assert.assertArrayEquals(aExpectedAlternateNames, aAlternateNames);								
	}
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_MultipleResponses() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/MultipleSoapResponsesSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
		
		JSONArray jsA = new JSONArray(sErrorsJsonArray);
		
		Assert.assertEquals(4, jsA.length());
		
		for(int i=0; i< jsA.length(); i++){	
			String errorMsg = jsA.getString(i);
			logger.info(errorMsg);
		}
	}	

	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_SubscriptionSuccess() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/SubscriptionSuccessSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
						
		Assert.assertNull(sErrorsJsonArray);					
	}		
	
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_UnsubscriptionAccessDenial() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/UnsubscriptionAccessDeniedSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
						
		Assert.assertTrue(sErrorsJsonArray.contains("The user is not privileged to delete this subscription"));					
	}	
	
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_RequestError() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/RequestErrorSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
						
		Assert.assertTrue(sErrorsJsonArray.contains("subscription doesn't contain a start date"));					
	}
	
	
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_InvalidEmail() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/InvalidEmailSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
				
		// note the brackets are json syntax
		Assert.assertEquals("[\"Invalid Email(s): chris@gmail.com\"]", sErrorsJsonArray);				
	}
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_InvalidToken() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
		
		String sFileContents = getFileContents("src/test/resources/subscriptions/InvalidTokenSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);	
				
		boolean hasTitle = sErrorsJsonArray.contains("Invalid Security Token");
		
		Assert.assertTrue(hasTitle);
		
		boolean hasDesc = sErrorsJsonArray.contains("Token is missing required information: Federation Identifier");
						
		Assert.assertTrue(hasDesc);
	}
	
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_AccessDenied() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
		
		String sFileContents = getFileContents("src/test/resources/subscriptions/AccessDeniedSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		String sErrorsJsonArray = subController.getJsonErrorsFromSubscriptionResponse(sampleSoapResponse);
		
		boolean hasAccessSring = sErrorsJsonArray.contains("Access Denied");
		
		boolean hasDescString = sErrorsJsonArray.contains("The user is not privileged to create subscriptions");
		
		Assert.assertTrue(hasAccessSring);
		
		Assert.assertTrue(hasDescString);
	}
	
	
	private String getFileContents(String fileClassPath) throws Exception{
		
		File sampleFile = new File(fileClassPath);		
		
		InputStream inStream = new FileInputStream(sampleFile);		
		
		String result = WebUtils.returnStringFromFilePath(inStream);
		
		return result;
	}
	
	private Document getDocFromTestXmlFile(String testXmlFile) throws Exception{
		
		File inputFile = new File(testXmlFile);
		
		InputStream fileInStream = new FileInputStream(inputFile);				
		
		DocumentBuilder docBuilder = SubscriptionsController.getDocBuilder();	
		
		Document rDocument = docBuilder.parse(fileInStream);	
		
		return rDocument;
	}
	
}


