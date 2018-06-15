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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackRenewalNotification;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FbiRapbackRenewalAuditProcessor {

	public EnhancedAuditDAO enhancedAuditDAO;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void auditFBIRapbackRenewalNotification(Exchange ex)
	{
		try {
			
			Document input = (Document) ex.getIn().getBody(Document.class);
			
			FederalRapbackRenewalNotification federalRapbackRenewalNotification = returnRapbackRenewalNotificaton(
					ex, input);
			
			enhancedAuditDAO.saveFederalRapbackRenewalNotification(federalRapbackRenewalNotification);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to audit FBI subscription");
		}
		
	}

	FederalRapbackRenewalNotification returnRapbackRenewalNotificaton(
			Exchange ex, Document input) throws Exception {
		LocalDateTime notificationRecievedTimestamp = LocalDateTime.now();
		
		FederalRapbackRenewalNotification federalRapbackRenewalNotification = new FederalRapbackRenewalNotification();
		
		federalRapbackRenewalNotification.setNotificationRecievedTimestamp(notificationRecievedTimestamp);
		
		String pathToNotificationFile = (String) ex.getIn().getHeader("pathToNotificationFile");
		federalRapbackRenewalNotification.setPathToNotificationFile(pathToNotificationFile);

		//XmlUtils.printNode(input);
		
		Node recordRapBackData = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData");
		
		federalRapbackRenewalNotification.setStateSubscriptionId(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='STATE SUBSCRIPTION ID']/ebts:UserDefinedElementText"));
		federalRapbackRenewalNotification.setSid(XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='STATE FINGERPRINT ID']/ebts:UserDefinedElementText"));

		Node recordSubject = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject");
		
		String dobString = XmlUtils.xPathStringSearch(recordSubject, "nc:PersonBirthDate/nc:Date");
		
		if (StringUtils.isNotBlank(dobString))
		{
			federalRapbackRenewalNotification.setPersonDob(LocalDate.parse(dobString));
		}	
		
		federalRapbackRenewalNotification.setPersonFirstName(XmlUtils.xPathStringSearch(recordSubject, "ebts:PersonName/nc:PersonGivenName"));
		federalRapbackRenewalNotification.setPersonMiddleName(XmlUtils.xPathStringSearch(recordSubject, "ebts:PersonName/nc:PersonMiddleName"));
		federalRapbackRenewalNotification.setPersonLastName(XmlUtils.xPathStringSearch(recordSubject, "ebts:PersonName/nc:PersonSurName"));
		
		federalRapbackRenewalNotification.setUcn(XmlUtils.xPathStringSearch(recordSubject, "jxdm41:PersonFBIIdentification/nc:IdentificationID"));
		
		String rapbackExpirationDateString = XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackExpirationDate/nc:Date");
		
		if (StringUtils.isNotBlank(rapbackExpirationDateString))
		{
			federalRapbackRenewalNotification.setRapbackExpirationDate(LocalDate.parse(rapbackExpirationDateString));
		}	
		
		federalRapbackRenewalNotification.setTransactionStatusText(XmlUtils.xPathStringSearch(input, "//ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionStatusText"));
		
		federalRapbackRenewalNotification.setRecordControllingAgency(XmlUtils.xPathStringSearch(input, "//ebts:RecordTransactionActivity/ebts:RecordControllingAgency/nc:OrganizationIdentification/nc:IdentificationID"));
		
		logger.info("Federal rapback renewal notification audit entry to save: " + federalRapbackRenewalNotification.toString());
		return federalRapbackRenewalNotification;
	}	

	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

	
}
