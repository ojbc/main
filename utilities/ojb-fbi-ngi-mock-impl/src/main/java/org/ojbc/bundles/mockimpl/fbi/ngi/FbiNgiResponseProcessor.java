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

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;

public class FbiNgiResponseProcessor {
	
	
	public static final String RAP_BACK_MAITENANCE_RESPONSE = "RBMNT";
	public static final String RAP_BACK_SCRIPTION_RESPONSE = "RBSCRM";
	public static final String RAP_BACK_CIVIL_SCRIPTION_RESPONSE = "RBSCVL";
	
	private Logger logger = Logger.getLogger(FbiNgiResponseProcessor.class);
	
	private DocumentBuilder docBuilder;
	
	
	public FbiNgiResponseProcessor() throws Exception {
	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		docBuilder = dbf.newDocumentBuilder();		
	}
	
	
	public String createResponse(Exchange exchange, @Header("transactionCategoryCode") String transactionCategoryCode) throws Exception{
				
		String trxCatCode = exchange.getIn().getHeader("transactionCategoryCode", String.class);
		logger.info("\n\n\n trxCatCode: " + trxCatCode + "\n\n\n");

		String transactionControlIdentification = exchange.getIn().getHeader("transactionControlIdentification", String.class);
		logger.info("\n\n\n transactionControlIdentification: " + transactionControlIdentification + "\n\n\n");
		
		String stateSubscriptionId = exchange.getIn().getHeader("stateSubscriptionId", String.class);
		logger.info("\n\n\n stateSubscriptionId: " + stateSubscriptionId + "\n\n\n");
		
		String stateFingerprintId = exchange.getIn().getHeader("stateFingerprintId", String.class);
		logger.info("\n\n\n stateFingerprintId: " + stateFingerprintId + "\n\n\n");
		
		if (StringUtils.isBlank(transactionCategoryCode)){
			throw new IllegalArgumentException("The transactionCategoryCode is missing."); 
		}
		
		String subAckResponse = getSubAckResponse(transactionControlIdentification, transactionCategoryCode, stateSubscriptionId, stateFingerprintId);
		
		logger.info("\n\n Processor returning subsription aknowledgement response:... \n\n");
				
		return subAckResponse;
	}
	
	
	String getSubAckResponse(String transactionControlIdentification, String transactionCategoryCode, String stateSubscriptionId, String stateFingerprintId) throws Exception{
				
		String sResponseDoc = null;
		
		InputStream inStreamSubResp = null;
		
		if (transactionCategoryCode.equals(RAP_BACK_SCRIPTION_RESPONSE) || transactionCategoryCode.equals(RAP_BACK_CIVIL_SCRIPTION_RESPONSE) ){
						
			inStreamSubResp = getClass().getClassLoader().getResourceAsStream("mockXml/Template(RBSR)RapBackSubscriptionResponse.xml");														    	
		
		}else if (transactionCategoryCode.equals(RAP_BACK_MAITENANCE_RESPONSE)){
			
			inStreamSubResp = getClass().getClassLoader().getResourceAsStream("mockXml/Template(RBMNTR)RapBackMaintenanceResponse.xml"); 									
		}
		
		if (inStreamSubResp != null){
			
			Document responseDoc = docBuilder.parse(inStreamSubResp);	
			
			sResponseDoc = OJBUtils.getStringFromDocument(responseDoc);
			
			sResponseDoc = sResponseDoc.replace("STATE_FINGERPRINT_ID_PLACEHOLDER", stateFingerprintId);
			
			sResponseDoc = sResponseDoc.replace("STATE_SUBSCRIPTION_ID_PLACEHOLDER", stateSubscriptionId);
			sResponseDoc = sResponseDoc.replace("TransactionControlReferenceIdentification_HOLDER", transactionControlIdentification);
			
		}		
		return sResponseDoc;
	}

}
