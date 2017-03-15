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
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;
import org.w3c.dom.Document;

public class SubscriptionsControllerTest {
		
	private static final Log logger = LogFactory.getLog(SubscriptionsControllerTest.class);
	
	private SubscriptionsController subController;	
		
	@Before
	public void init(){		
		subController = new SubscriptionsController();	
		subController.fbiIdWarning = true; 
	}
	
	
	@Test
	public void testSubscriptionWarnings(){
		
		Subscription sub = new Subscription();
		
		sub.setTopic("{http://ojbc.org/wsn/topics}:person/rapback");
		
		List<String> warningList = subController.getSubscriptionWarnings(sub);
		
		String warning0 = warningList.get(0);
		
		Assert.assertEquals("FBI ID missing. Subscription with the FBI is pending.", warning0);
	}
	
	@Test
	public void testSubscriptionJsonErrors(){
		
		List<String> errorList = Arrays.asList("Error1", "Error2");
		
		List<String> warningList = Arrays.asList("Warn1", "Warn2");
		
		String jsonMsgs = subController.getErrorsWarningsJson(errorList, warningList);
		
		Assert.assertEquals("{\"warnings\":[\"Warn1\",\"Warn2\"],\"errors\":[\"Error1\",\"Error2\"]}", jsonMsgs);
		
		logger.info("json errors:\n" + jsonMsgs);
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
		
		String originalName = subscribedPersonNames.getOriginalName().getFullName();
		Assert.assertEquals("Mary R Billiot", originalName);
		
		String[] aAlternateNames = subscribedPersonNames.getAlternateNamesList()
				.stream().map(personName -> personName.getFullName())
				.toArray(size -> new String[size]);		
		Assert.assertArrayEquals(aExpectedAlternateNames, aAlternateNames);							
	}
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_MultipleResponses() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/MultipleSoapResponsesSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
		
		JSONArray jsA = new JSONArray(errorsList);
		
		Assert.assertEquals(4, jsA.length());
		
		for(int i=0; i< jsA.length(); i++){	
			String errorMsg = jsA.getString(i);
			logger.info(errorMsg);
		}
	}	

	
	@Test
	public void testGetErrorsFromSubscriptionResponse_SubscriptionSuccess() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/SubscriptionSuccessSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
				
		boolean noErrors = errorsList == null || errorsList.isEmpty();
		
		Assert.assertTrue(noErrors);					
	}		
	
	
	@Test
	public void testGetErrorsFromSubscriptionResponse_UnsubscriptionAccessDenial() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/UnsubscriptionAccessDeniedSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
						
		String errorMsg = errorsList.get(0);
		
		Assert.assertTrue(errorMsg.contains("The user is not privileged to delete this subscription"));					
	}	
	
	
	@Test
	public void testGetJsonErrorsFromSubscriptionResponse_RequestError() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/RequestErrorSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
		
		String error0 = errorsList.get(0);
						
		Assert.assertTrue(error0.contains("subscription doesn't contain a start date"));					
	}
	
	
	
	@Test
	public void testGetErrorsFromSubscriptionResponse_InvalidEmail() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
						
		String sFileContents = getFileContents("src/test/resources/subscriptions/InvalidEmailSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
				
		String error0 = errorsList.get(0);
		
		// note the brackets are json syntax
		Assert.assertEquals("Invalid Email(s): chris@gmail.com", error0);				
	}
	
	@Test
	public void testGetErrorsFromSubscriptionResponse_InvalidToken() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
		
		String sFileContents = getFileContents("src/test/resources/subscriptions/InvalidTokenSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);	
		
		String error0 = errorsList.get(0);
				
		boolean hasTitle = error0.contains("Invalid Security Token");
		
		Assert.assertTrue(hasTitle);
		
		boolean hasDesc = error0.contains("Token is missing required information: Federation Identifier");
						
		Assert.assertTrue(hasDesc);
	}
	
	
	@Test
	public void testGetErrorsFromSubscriptionResponse_AccessDenied() throws Exception{
		
		FaultableSoapResponse sampleSoapResponse = new FaultableSoapResponse();
		
		String sFileContents = getFileContents("src/test/resources/subscriptions/AccessDeniedSoapResponseSample.xml");
		
		sampleSoapResponse.setSoapResponse(sFileContents);
		
		List<String> errorsList = subController.getErrorListFromSubscriptionResponse(sampleSoapResponse);
		
		String error0 = errorsList.get(0);
		
		boolean hasAccessSring = error0.contains("Access Denied");
		
		boolean hasDescString = error0.contains("The user is not privileged to create subscriptions");
		
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


