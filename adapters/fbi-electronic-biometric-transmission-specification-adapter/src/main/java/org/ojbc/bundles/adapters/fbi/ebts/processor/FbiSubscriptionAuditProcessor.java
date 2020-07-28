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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.bundles.adapters.fbi.ebts.util.FBIEbtsUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FbiSubscriptionAuditProcessor {

	public EnhancedAuditDAO enhancedAuditDAO;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void auditFBISubscriptionRequest(Exchange ex)
	{
		logger.info("Entering method to audit FBI subscription request");
		
		LocalDateTime requestTimestamp = LocalDateTime.now();
		
		String pathToRequestFile = (String) ex.getIn().getHeader("pathToRequestFile");
		String transactionControlReferenceID = (String) ex.getIn().getHeader("controlID");
		
		FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
		
		federalRapbackSubscription.setRequestSentTimestamp(requestTimestamp);
		federalRapbackSubscription.setPathToRequestFile(pathToRequestFile);
		federalRapbackSubscription.setTransactionControlReferenceIdentification(transactionControlReferenceID);
		
		Document input = (Document) ex.getIn().getBody(Document.class);
		
		try {
			
			//XmlUtils.printNode(input);
			
			Node recordRapBackData = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData");
			
			federalRapbackSubscription.setSubscriptonCategoryCode(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackCategoryCode"));
			federalRapbackSubscription.setStateSubscriptionId(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='STATE SUBSCRIPTION ID']/ebts:UserDefinedElementText"));
			federalRapbackSubscription.setSid(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='STATE FINGERPRINT ID']/ebts:UserDefinedElementText"));
			federalRapbackSubscription.setTransactionCategoryCodeRequest(XmlUtils.xPathStringSearch(recordRapBackData, "//ebts:TransactionCategoryCode"));
			
			logger.info("Federal rapback subscription request audit entry to save: " + federalRapbackSubscription.toString());
			
			enhancedAuditDAO.saveFederalRapbackSubscription(federalRapbackSubscription);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to audit FBI subscription");
		}
		
	}

	public void auditFBISubscriptionResponse(Exchange ex) 
	{
		logger.info("Entering method to audit FBI subscription response.");
		
		try {
			LocalDateTime responseTimestamp = LocalDateTime.now();
			
			String pathToResponseFile = (String) ex.getIn().getHeader("pathToResponseFile");
			
			String transactionCategoryCode = (String) ex.getIn().getHeader("trxCatCode");
			
			String transactionControlReferenceIdentification = (String) ex.getIn().getHeader("transactionControlReferenceIdentification"); 
			
			logger.info("Transaction Control Reference ID: " + transactionControlReferenceIdentification);
			
			FederalRapbackSubscription federalRapbackSubscriptionFromDatabase = enhancedAuditDAO.retrieveFederalRapbackSubscriptionFromTCN(transactionControlReferenceIdentification);
			
			logger.info("Audit entry returned from database:" + federalRapbackSubscriptionFromDatabase);
			
			Document input = (Document) ex.getIn().getBody(Document.class);
			//XmlUtils.printNode(input.getDocumentElement());
			
			FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
			
			federalRapbackSubscription.setResponseRecievedTimestamp(responseTimestamp);
			federalRapbackSubscription.setPathToResponseFile(pathToResponseFile);
			federalRapbackSubscription.setTransactionCategoryCodeResponse(transactionCategoryCode);
			
			String transactionStatusText = FBIEbtsUtils.processTransactionStatusText(input);
			
			Node recordRapBackData = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData");
			
			if (recordRapBackData != null)
			{	
				String stateSubscriptionId = XmlUtils.xPathStringSearch(recordRapBackData, 
						"ebts:RecordRapBackUserDefinedElement[upper-case(ebts:UserDefinedElementName/text())='STATE SUBSCRIPTION ID']/ebts:UserDefinedElementText");
				federalRapbackSubscription.setStateSubscriptionId(stateSubscriptionId);
				
				String stateFingerPrintId= XmlUtils.xPathStringSearch(recordRapBackData, 
						"ebts:RecordRapBackUserDefinedElement[upper-case(ebts:UserDefinedElementName/text())='STATE FINGERPRINT ID']/ebts:UserDefinedElementText");
				
				federalRapbackSubscription.setSid(stateFingerPrintId);
			}	
			
			boolean errorIndicator = false;
			
			//Always consider an ERRA an error
			if (transactionCategoryCode.equals("ERRA"))
			{
				federalRapbackSubscription.setTransactionStatusText(transactionStatusText);
				errorIndicator = true;
			}
			
			if (StringUtils.isNotBlank(transactionStatusText))
			{
				//RB001, RB002, RB003, RB004, RB005, RB006, RB007, RB013, RB014 or RB015 in the transaction status text indicate an error
				if (StringUtils.contains(transactionStatusText, "RB001") || StringUtils.contains(transactionStatusText, "RB002") || StringUtils.contains(transactionStatusText, "RB003") 
					|| StringUtils.contains(transactionStatusText, "RB004") || StringUtils.contains(transactionStatusText, "RB005") || StringUtils.contains(transactionStatusText, "RB006")
					|| StringUtils.contains(transactionStatusText, "RB007")	|| StringUtils.contains(transactionStatusText, "RB013") || StringUtils.contains(transactionStatusText, "RB014")
					|| StringUtils.contains(transactionStatusText, "RB015")
				   )
				{	
					federalRapbackSubscription.setTransactionStatusText(transactionStatusText);
					errorIndicator = true;
				}
			}	
			
			federalRapbackSubscription.setFbiSubscriptionId(XmlUtils.xPathStringSearch(input, "//ebts:RecordRapBackSubscriptionID"));
			
			logger.info("Federal rapback subscription response audit entry to save: " + federalRapbackSubscription.toString());
			
			Integer federalRapbackSubscriptionPk = null;
			
			if (federalRapbackSubscriptionFromDatabase != null)
			{
				federalRapbackSubscriptionPk =  federalRapbackSubscriptionFromDatabase.getFederalRapbackSubscriptionId();
				
				federalRapbackSubscription.setFederalRapbackSubscriptionId(federalRapbackSubscriptionFromDatabase.getFederalRapbackSubscriptionId());
				logger.info("Audit record exists, update it: " + federalRapbackSubscription.getFederalRapbackSubscriptionId());
				enhancedAuditDAO.updateFederalRapbackSubscriptionWithResponse(federalRapbackSubscription);
			}	
			else
			{
				logger.info("Audit record doesn't exist, save new one");
				federalRapbackSubscriptionPk = enhancedAuditDAO.saveFederalRapbackSubscription(federalRapbackSubscription);	
			}
			
			if (errorIndicator)
			{
				//FBI reported error, check first to see if there is an existing error this state subscription
				//If there is, delete it and this will supercede it
				//If not, insert this new error
				
				if ( federalRapbackSubscriptionFromDatabase != null ){
					enhancedAuditDAO.deleteFederalRapbackSubscriptionError(federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
				}
				
				enhancedAuditDAO.saveFederalRapbackSubscriptionError(federalRapbackSubscriptionPk, federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
				
			}	
			else
			{
				//No error reported by, check error table to see if STATE SUBSCRIPTION ID is in error state and resolve it
				Integer existingErrorEntryPK = enhancedAuditDAO.retrieveFederalRapbackSubscriptionError(federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
				
				if (existingErrorEntryPK != null)
				{
					enhancedAuditDAO.resolveFederalRapbackSubscriptionError(federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
				}	
				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Unable to audit federal subscription response.");
			
		}	
		
		
	}

	public String retrieveStateSubscriptionIDFromTransactionControlReferenceId(@Header("transactionControlReferenceIdentification") String transactionControlReferenceId)
	{
		return enhancedAuditDAO.retrieveStateSubscriptionIDFromTransactionControlReferenceId(transactionControlReferenceId);
	}
	
	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

}
