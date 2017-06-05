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
package org.ojbc.bundles.mockimpl.fbi.ngi;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.helpers.XMLUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FbiNgiUserServiceProcessor {
	
	private Logger logger = Logger.getLogger(FbiNgiUserServiceProcessor.class);
	
	
	public void handleUserServiceRequest(Exchange exchange) throws Exception{
		
		String responseXml = getControlNumResponseMessage(exchange);
		
		logger.info("\n\n Returning Control# doc...\n\n");
		
		//TODO see how to avoid copying header like this
		String oJBCfbiUserRequest =  exchange.getIn().getHeader("OJBCfbiUserRequest", String.class);		
		exchange.getOut().setHeader("OJBCfbiUserRequest", oJBCfbiUserRequest);
		
		exchange.getIn().removeHeader("OJBCfbiUserRequest");
		
		exchange.getOut().setBody(responseXml);
	}
	
	
	String getControlNumResponseMessage(Exchange inputExchange) throws Exception{
		
		Document fbiNgiSubscribeDoc = inputExchange.getIn().getBody(Document.class);		

		String controlNumResponseDoc = null;
		
		String controlNumberReceived = XmlUtils.xPathStringSearch(fbiNgiSubscribeDoc,
				"/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/nbio:TransactionControlIdentification/nc:IdentificationID");
						
		List<String> errorList = getValidationErrors(controlNumberReceived);
				
		if(errorList != null && errorList.size() > 0){
			
			for(String errorMsg : errorList){
				logger.error(errorMsg);
			}
			
		}else{
			
			logger.info("\n\n\n FBI Mock Impl: Using Control Number: " + controlNumberReceived + " \n\n\n");
			
			Document doc = XMLUtils.newDocument();
			
			Element root = XMLUtils.createElementNS(doc, "http://ws.cjis.gov/2014/08/01/ngi/core/xsd", "ngi-core:NGIControlNumber");		
			root.setTextContent(controlNumberReceived);		
			doc.appendChild(root);		
			
			controlNumResponseDoc = OJBUtils.getStringFromDocument(doc);				
		}		
		
		return controlNumResponseDoc;
	}
	
	
	List<String> getValidationErrors(String controlNumber){
		
		List<String> errorList = new ArrayList<String>();
		
		if(StringUtils.isEmpty(controlNumber)){			
			errorList.add("\n\n\n ERROR:  FBI Mock Impl did not receive Transaction Control Number!! \n\n\n");			
		
		}else if(controlNumber.length() > 40){
			errorList.add("\n\n\n ERROR: FBI Mock Impl received Transaction Control Number of length exceeding 40(max allowed)! \n\n\n");
		
		}else if(controlNumber.length() < 10){
			errorList.add("\n\n\n ERROR: FBI Mock Impl received Transaction Control Number of length less than 10(min allowed)! \n\n\n");
		}
		
		return errorList;
	}
	
}
