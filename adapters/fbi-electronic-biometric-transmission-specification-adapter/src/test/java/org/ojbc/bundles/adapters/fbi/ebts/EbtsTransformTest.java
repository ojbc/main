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
package org.ojbc.bundles.adapters.fbi.ebts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.util.Base64;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XsltTransformer;
import org.xml.sax.SAXException;

public class EbtsTransformTest {
	
	private XsltTransformer xsltTransformer;
	
	private static final Log log = LogFactory.getLog( EbtsTransformTest.class );
	
	@Before
	public void init(){
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
		xsltTransformer = new XsltTransformer();
	}	
	
	@Test
	public void newCriminalSubscriptionTestEbtsTransform() throws Exception{
								
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/OJBC_Criminal_Subscription_Request_Document.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/ojbSubscriptionToEBTS.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();	
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Criminal-Subscription-Request.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);					
	}
	
	/*

	@Test
	public void newCivilSubscriptionTestEbtsTransform() throws Exception{
								
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/OJBC_Civil_Subscription_Request_Document.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/ojbSubscriptionToEBTS.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();	
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Civil-Subscription-Request.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);					
	}
	
	*/
	
	@Test
	public void modifyCriminalSubscriptionTestEbtsTransform() throws IOException, SAXException{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/OJBC_Subscription_Modify_Document.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/ojbSubscriptionToEBTS.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Subscription-Maintenance-Replace-Request.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);							
	}
	
	/*
	
	@Test
	public void cancelSubscriptionTestEbtsTransform() throws IOException, SAXException{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/OJBC_unsubscribe.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/ojbSubscriptionToEBTS.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Subscription-Maintenance-Cancel-Request.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);							
	}
	
	*/
		
	@Test
	public void RapbackSubscriptionResponseTransform() throws IOException, SAXException{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/Template(RBSR)RapBackSubscriptionResponse.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/RapBackSubscriptionResponseToSubscriptionCreationReport.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/RapBackSubscriptionCreationReport.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);							
	}
	
	@Test
	public void RapbackMaintenanceResponseTransform() throws IOException, SAXException{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/Template(RBMNTR)RapBackMaintenanceResponse.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
								
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/RapBackMaintenanceResponseToSubscriptionUpdateReport.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/RapbackSubscriptionUpdateReport.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);							
	}
	
	@Test
	public void CriminalHistoryReportTestEbtsTransform() throws Exception{
		
		InputStream inputFileStream = new FileInputStream("src/test/resources/input/FBI_Rapback_Activity_Notification.xml");
		Source inputFileSource = OJBUtils.createSaxSource(inputFileStream);
		
		InputStream xsltFileInStream = new FileInputStream("src/main/resources/xsl/Federal_To_CH_Report_Transform.xsl"); 				
		Source xsltSource = OJBUtils.createSaxSource(xsltFileInStream);
		
		Map<String, Object> xsltParamMap = getXsltParamMap();
		String rapsheetString = "Subject's Rap Sheet goes here";
		xsltParamMap.put("base64Rapsheet", Base64.encode(rapsheetString.getBytes()));
			
		String actualTransformedXml = xsltTransformer.transform(inputFileSource, xsltSource, xsltParamMap);		
				
		log.debug("Tranformed XML: " + actualTransformedXml);
		
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/Federal_Rapback_CH_Report.xml"));
							
		compareXml(expectedXmlString, actualTransformedXml);							
	} 
	

	
	private Map<String, Object> getXsltParamMap(){
	
		Map<String, Object> xsltParamMap = new HashMap<String, Object>();
	/*
		xsltParamMap.put("rapBackTransactionDate", "2015-07-14");		
		xsltParamMap.put("rapBackNotificatonFormat", 3);
		xsltParamMap.put("rapBackInStateOptOutIndicator", true);
		xsltParamMap.put("rapBackTriggeringEvent", 1);		
		xsltParamMap.put("destinationOrganizationORI", "WVIAFIS0Z");
		xsltParamMap.put("originatorOrganizationORI", "HI002595Y");
		xsltParamMap.put("controlID", "9876500000");
		xsltParamMap.put("domainVersion", "EBTS 10.0");
		xsltParamMap.put("domainName", "NORAM");
		xsltParamMap.put("transactionMajorVersion", "05");
		xsltParamMap.put("transactionMinorVersion", "00");
		xsltParamMap.put("rapSheetRequestIndicator", "true");
		xsltParamMap.put("rapBackRecipient", "HI002595Y");
		xsltParamMap.put("controllingAgencyID", "HI002595Y");
		xsltParamMap.put("originatingAgencyCaseNumber", "HCJDC-CASE");
		xsltParamMap.put("nativeScanningResolution", "00.00");
		xsltParamMap.put("nominalTransmittingResolution", "00.00");
		xsltParamMap.put("transactionContentSummaryContentFirstRecordCategoryCode", "1");
		xsltParamMap.put("transactionContentSummaryContentRecordCountCriminal", "01");					
		xsltParamMap.put("transactionContentSummaryContentRecordCountCivil", "03");				
		xsltParamMap.put("rapBackDisclosureIndicator", "false");
		xsltParamMap.put("civilRapBackSubscriptionTerm", "L");
		*/
		return xsltParamMap;
	} 
	

	
	private static void compareXml(String expectedXmlString, String actualTransformedXml) throws SAXException, IOException{
		
		Diff diff = XMLUnit.compareXML(expectedXmlString, actualTransformedXml);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());
	}	
}

