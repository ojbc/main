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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.TriggeringEvents;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FbiNotificationAuditProcessor {

	public EnhancedAuditDAO enhancedAuditDAO;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private HashMap<String, Integer> triggeringEvents = new HashMap<String, Integer>();
	
	private HashMap<String, String> triggeringEventCodeLookup = new HashMap<String, String>();
	
	public void saveTriggeringEvents(Document input, Integer federalRapbackNotificationPk)
	{
		//Load triggering events in database into a map for faster retrieval
		if (triggeringEvents.isEmpty())
		{
			List<TriggeringEvents> triggeringEventsList =  enhancedAuditDAO.retrieveAllTriggeringEvents();
			
			for (TriggeringEvents triggeringEvent : triggeringEventsList)
			{
				triggeringEvents.put(triggeringEvent.getTriggeringEvent(), triggeringEvent.getTriggeringEventsId());
			}	
		}
		
		//xquery to get all triggering events
		
		try {
			NodeList triggeringEventNodes = XmlUtils.xPathNodeListSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData/ebts:TransactionRapBackTriggeringEvent/ebts:RapBackTriggeringEventCode");
			
		    if (triggeringEventNodes != null) {
		        int length = triggeringEventNodes.getLength();
		        for (int i = 0; i < length; i++) {
		            if (triggeringEventNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
		                Element el = (Element) triggeringEventNodes.item(i);

		                String triggerEventCode = el.getTextContent();
		                
		                String triggerEventText = triggeringEventCodeLookup.get(triggerEventCode);
		                
		                if (StringUtils.isNotBlank(triggerEventText))
		                {
			                Integer triggeringEventId = triggeringEvents.get(triggerEventText);
			                
			                if (triggeringEventId != null)
			                {
			                	//save triggering event here
			                	enhancedAuditDAO.saveTriggeringEvent(federalRapbackNotificationPk, triggeringEventId);
			                }
		                }   
		            }
		        }
		    }
			
 		} catch (Exception e) {
			logger.severe("Unable to update rapback notification audit entry.");
		}
		
	}

	public void auditFBIRapbackNotification(Exchange ex)
	{
		try {
			
			Document input = (Document) ex.getIn().getBody(Document.class);
			
			LocalDateTime notificationRecievedTimestamp = LocalDateTime.now();
			
			FederalRapbackNotification federalRapbackNotification = new FederalRapbackNotification();
			
			federalRapbackNotification.setNotificationRecievedTimestamp(notificationRecievedTimestamp);
			
			String pathToNotificationFile = (String) ex.getIn().getHeader("pathToNotificationFile");
			federalRapbackNotification.setPathToNotificationFile(pathToNotificationFile);

			String rbnAction = (String)ex.getIn().getHeader("RBN_Action");
			
			if (StringUtils.isNotBlank(rbnAction))
			{
				federalRapbackNotification.setTransactionType(rbnAction);
				
				if (rbnAction.equals("UCN_CONSOLIDATION"))
				{
					federalRapbackNotification.setOriginalIdentifier((String)ex.getIn().getHeader("deletedIdentity"));
					federalRapbackNotification.setUpdatedIdentifier((String)ex.getIn().getHeader("retainedIdentity"));
				}	

				if (rbnAction.equals("UCN_DELETION"))
				{
					federalRapbackNotification.setOriginalIdentifier((String)ex.getIn().getHeader("deletedIdentity"));
				}	

				if (rbnAction.equals("UCN_RESTORATION"))
				{
					federalRapbackNotification.setOriginalIdentifier((String)ex.getIn().getHeader("restoredIdentity"));
				}	
				
			}	
			
			String transactionType = (String) ex.getIn().getHeader("transactionType");
			
			if (StringUtils.isNotBlank(transactionType))
			{
				federalRapbackNotification.setTransactionType(transactionType);
			}	
			
			Node recordRapBackData = XmlUtils.xPathNodeSearch(input, "//ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData");
			
			String stateSubscriptionId= XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackUserDefinedElement[ebts:UserDefinedElementName/text()='STATE SUBSCRIPTION ID']/ebts:UserDefinedElementText");
			logger.info("State subscription ID: " + stateSubscriptionId);
			federalRapbackNotification.setStateSubscriptionId(stateSubscriptionId);
			
			String rapbackEventText = XmlUtils.xPathStringSearch(input, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageDescriptiveTextRecord/nistbio:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData/ebts:TransactionRapBackTriggeringEvent/ebts:RapBackEventText");
			federalRapbackNotification.setRapBackEventText(rapbackEventText);
			
//          <!-- RBNI 2.2041 -->
//          <ebts:RecordRapBackActivityNotificationID>123456</ebts:RecordRapBackActivityNotificationID>
			String recordRapBackActivityNotificationID = XmlUtils.xPathStringSearch(recordRapBackData, "ebts:RecordRapBackActivityNotificationID");
			federalRapbackNotification.setRecordRapBackActivityNotificationID(recordRapBackActivityNotificationID);
			
			logger.info("Federal rapback subscription request audit entry to save: " + federalRapbackNotification.toString());
			
			Integer federalRapbackNotificationPk = enhancedAuditDAO.saveFederalRapbackNotification(federalRapbackNotification);
			
			saveTriggeringEvents(input, federalRapbackNotificationPk);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to audit FBI subscription");
		}
		
	}	

	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

	public HashMap<String, String> getTriggeringEventCodeLookup() {
		return triggeringEventCodeLookup;
	}

	public void setTriggeringEventCodeLookup(
			HashMap<String, String> triggeringEventCodeLookup) {
		this.triggeringEventCodeLookup = triggeringEventCodeLookup;
	}

}
