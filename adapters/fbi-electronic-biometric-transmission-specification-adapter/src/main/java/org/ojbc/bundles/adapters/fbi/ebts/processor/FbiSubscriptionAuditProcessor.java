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
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
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
			
			XmlUtils.printNode(input);
			
			Node recordRapBackData = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData");
			
			federalRapbackSubscription.setSubscriptonCategoryCode(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackCategoryCode"));
			federalRapbackSubscription.setStateSubscriptionId(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='State Subscription ID']/ebts:UserDefinedElementText"));
			federalRapbackSubscription.setSid(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='State Fingerprint ID']/ebts:UserDefinedElementText"));
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
			
			Document input = (Document) ex.getIn().getBody();
			
			FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
			
			federalRapbackSubscription.setResponseRecievedTimestamp(responseTimestamp);
			federalRapbackSubscription.setPathToResponseFile(pathToResponseFile);
			federalRapbackSubscription.setTransactionCategoryCodeResponse(transactionCategoryCode);
			federalRapbackSubscription.setTransactionStatusText(XmlUtils.xPathStringSearch(input, "//ebts:TransactionStatusText"));
			federalRapbackSubscription.setFbiSubscriptionId(XmlUtils.xPathStringSearch(input, "//ebts:RecordRapBackSubscriptionID"));
			
			logger.info("Federal rapback subscription response audit entry to save: " + federalRapbackSubscription.toString());
			
			if (federalRapbackSubscriptionFromDatabase != null)
			{
				federalRapbackSubscription.setFederalRapbackSubscriptionId(federalRapbackSubscriptionFromDatabase.getFederalRapbackSubscriptionId());
				logger.info("Audit record exists, update it: " + federalRapbackSubscription.getFederalRapbackSubscriptionId());
				enhancedAuditDAO.updateFederalRapbackSubscriptionWithResponse(federalRapbackSubscription);
			}	
			else
			{
				logger.info("Audit record doesn't exist, save new one");
				enhancedAuditDAO.saveFederalRapbackSubscription(federalRapbackSubscription);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Unable to audit federal subscription response.");
			
		}	
		
		
	}

	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

}
