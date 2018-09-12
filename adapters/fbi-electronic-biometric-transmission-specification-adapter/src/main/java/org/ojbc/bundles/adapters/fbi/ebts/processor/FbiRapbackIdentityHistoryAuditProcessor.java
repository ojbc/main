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
import org.ojbc.audit.enhanced.dao.model.FederalRapbackIdentityHistory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FbiRapbackIdentityHistoryAuditProcessor {

	public EnhancedAuditDAO enhancedAuditDAO;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void auditFbiRapbackIdentityHistoryRequest(Exchange ex)
	{
		try {
			
			Document input = (Document) ex.getIn().getBody(Document.class);
			
			FederalRapbackIdentityHistory federalRapbackIdentityHistory = returnFederalRapbackIdentityHistory(
					ex, input);
			
			enhancedAuditDAO.saveFederalRapbackIdentityHistory(federalRapbackIdentityHistory);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to audit FBI rapback identity history request");
		}
		
	}

	public void auditFbiRapbackIdentityHistoryResponse(Exchange ex)
	{
		try {
			
			Document input = (Document) ex.getIn().getBody(Document.class);

			FederalRapbackIdentityHistory frihUpdate = new FederalRapbackIdentityHistory();
			
			String transactionControlReferenceIdentification = (String) ex.getIn().getHeader("transactionControlReferenceIdentification"); 
			
			logger.info("Transaction Control Reference Identification: " + transactionControlReferenceIdentification);
			
			frihUpdate.setTransactionControlReferenceIdentification(transactionControlReferenceIdentification);
			frihUpdate.setResponseReceivedTimestamp(LocalDateTime.now());
			frihUpdate.setTransactionCategoryCodeResponse(XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/ebts:TransactionCategoryCode"));
			
			String pathToResponseFile = (String) ex.getIn().getHeader("pathToResponseFile");
			frihUpdate.setPathToResponseFile(pathToResponseFile);

			
			enhancedAuditDAO.updateFederalRapbackIdentityHistoryWithResponse(frihUpdate);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to audit FBI rapback identity history response");
		}
		
	}

	
	FederalRapbackIdentityHistory returnFederalRapbackIdentityHistory(
			Exchange ex, Document input) throws Exception {
		LocalDateTime requestSentTimestamp = LocalDateTime.now();
		
		FederalRapbackIdentityHistory federalRapbackIdentityHistory = new FederalRapbackIdentityHistory();
		
		federalRapbackIdentityHistory.setRequestSentTimestamp(requestSentTimestamp);
		
		String pathToRequestFile = (String) ex.getIn().getHeader("pathToRequestFile");
		federalRapbackIdentityHistory.setPathToRequestFile(pathToRequestFile);

		federalRapbackIdentityHistory.setUcn(XmlUtils.xPathStringSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject/jxdm41:PersonFBIIdentification/nc:IdentificationID"));
		federalRapbackIdentityHistory.setTransactionCategoryCodeRequest(XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/ebts:TransactionCategoryCode"));
		federalRapbackIdentityHistory.setTransactionType(XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/ebts:TransactionCategoryCode"));
		federalRapbackIdentityHistory.setFbiNotificationId(XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageDescriptiveTextRecord/nistbio:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData/ebts:RecordRapBackActivityNotificationID"));
		federalRapbackIdentityHistory.setFbiSubscriptionId(XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageDescriptiveTextRecord/nistbio:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData/ebts:RecordRapBackSubscriptionID"));
		federalRapbackIdentityHistory.setTransactionControlReferenceIdentification((XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/nbio:TransactionControlIdentification/nc:IdentificationID")));
				
		logger.info("Federal federal rapback identity history audit entry to save: " + federalRapbackIdentityHistory.toString());
		return federalRapbackIdentityHistory;
	}	

	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

	
}
