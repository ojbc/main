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
package org.ojbc.intermediaries.sn.subscription;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.SubscriptionAction;
import org.ojbc.audit.enhanced.processor.AbstractSubscriptionActionAuditProcessor;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.util.SubscriptionActionUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class with handle Subscription Validation Requests.  If the subscription is found,
 * the lastValidatedDate column will be updated with the current date stamp.
 * 
 */
public class SubscriptionAuditProcessor {

	private static final Log log = LogFactory.getLog( SubscriptionAuditProcessor.class );
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private AbstractSubscriptionActionAuditProcessor subscriptionActionAuditProcessor;  
	
	public void auditSubscriptionRequest(Exchange exchange, @Header("subscriptionId") String subscriptionId) throws Exception 
	{
		//Create Subscription Action here
		SubscriptionAction subscriptionAction = new SubscriptionAction();
		
		if (StringUtils.isNotBlank(subscriptionId))
		{	
			subscriptionAction.setAction(SubscriptionAction.SUBSCRIPTION_MODIFICATION_ACTION);
			exchange.setProperty("subscriptionAction", SubscriptionAction.SUBSCRIPTION_MODIFICATION_ACTION);
			
			Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionWithFbiInfoBySubscriptionId(subscriptionId);
			
			if (subscription != null)
			{	
				FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
				
				if (fbiRapbackSubscription != null)
				{	
					String fbiSubscriptionId = subscription.getFbiRapbackSubscription().getFbiSubscriptionId();
					
					if (StringUtils.isNotBlank(fbiSubscriptionId))
					{	
						subscriptionAction.setFbiSubscriptionId(fbiSubscriptionId);
					}
				}
				
				subscriptionAction.setStateSubscriptionId(String.valueOf(subscription.getId()));
				
				SubscriptionActionUtils.setSubscriptionActionDateParams(subscriptionAction, subscription);

			}
		}
		else
		{
			subscriptionAction.setAction(SubscriptionAction.SUBSCRIBE_ACTION);
			exchange.setProperty("subscriptionAction", SubscriptionAction.SUBSCRIBE_ACTION);
			
		}	

		//Do auditing here
		Integer subscriptionActionPk = subscriptionActionAuditProcessor.auditSubcriptionRequestAction(exchange, subscriptionAction);
		exchange.setProperty("subscriptionActionPk", subscriptionActionPk);

	}
	
	public void auditSubscriptionResponse(Exchange exchange) throws Exception 
	{
		SubscriptionAction subscriptionAction = new SubscriptionAction();
		
		Document subscriptionResponseDoc = (Document)exchange.getIn().getBody();
		
		String subscriptionCreatedIndicator =XmlUtils.xPathStringSearch(subscriptionResponseDoc, "/b-2:SubscribeResponse/subresp-exch:SubscriptionResponseMessage/subresp-ext:SubscriptionCreatedIndicator");

		boolean successIndicator = false;
		
		if (StringUtils.isNotBlank(subscriptionCreatedIndicator) && (subscriptionCreatedIndicator.equals("true")))
		{
			successIndicator = true;
		}	
		
		subscriptionAction.setSuccessIndicator(successIndicator);
		
		Integer subscriptionActionPk = (Integer) exchange.getProperty("subscriptionActionPk");
		
		subscriptionAction.setSubscriptionActionId(subscriptionActionPk);
		
		String subscriptionSubscribeAction = (String) exchange.getProperty("subscriptionAction");
		
		if (subscriptionSubscribeAction.equals(SubscriptionAction.SUBSCRIBE_ACTION))
		{
			Integer subscriptionId = (Integer) exchange.getIn().getHeader("subscriptionId");
			subscriptionAction.setStateSubscriptionId(String.valueOf(subscriptionId));
		}	
		
		//Call processor here to make update
		subscriptionActionAuditProcessor.auditSubcriptionResponseAction(subscriptionAction);
		
	}

	public void auditUnsubscriptionRequest(Exchange exchange, @Header("subscriptionId") String subscriptionId) throws Exception 
	{
		//Create Subscription Action here
		SubscriptionAction subscriptionAction = new SubscriptionAction();
		
		subscriptionAction.setAction(SubscriptionAction.UNSUBSCRIBE_ACTION);
		
		Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionWithFbiInfoBySubscriptionId(subscriptionId);
		
		if (subscription != null)
		{	
			FbiRapbackSubscription fbiRapbackSubscription = subscription.getFbiRapbackSubscription();
			
			if (fbiRapbackSubscription != null)
			{	
				String fbiSubscriptionId = subscription.getFbiRapbackSubscription().getFbiSubscriptionId();
				
				if (StringUtils.isNotBlank(fbiSubscriptionId))
				{	
					subscriptionAction.setFbiSubscriptionId(fbiSubscriptionId);
				}
			}
			
			subscriptionAction.setStateSubscriptionId(String.valueOf(subscription.getId()));
			
			SubscriptionActionUtils.setSubscriptionActionDateParams(subscriptionAction, subscription);

			//Do auditing here
			Integer subscriptionActionPk = subscriptionActionAuditProcessor.auditSubcriptionRequestAction(exchange, subscriptionAction);
			exchange.setProperty("subscriptionActionPk", subscriptionActionPk);
		}
		else
		{
			throw new Exception("Unable to retrieve subscription from database for unsubscription auditing.");
		}	
	}

	public void auditUnsubscriptionResponse(Exchange exchange) throws Exception 
	{
		SubscriptionAction subscriptionAction = new SubscriptionAction();
		
		Document subscriptionResponseDoc = (Document)exchange.getIn().getBody();
		
		Node unsubscribeResponse =XmlUtils.xPathNodeSearch(subscriptionResponseDoc, "/b-2:UnsubscribeResponse");

		boolean successIndicator = false;
		
		if (unsubscribeResponse != null)
		{
			successIndicator = true;
		}	
		
		subscriptionAction.setSuccessIndicator(successIndicator);
		
		Integer subscriptionActionPk = (Integer) exchange.getProperty("subscriptionActionPk");
		
		subscriptionAction.setSubscriptionActionId(subscriptionActionPk);
		
		//Call processor here to make update
		subscriptionActionAuditProcessor.auditSubcriptionResponseAction(subscriptionAction);
		
	}	
	
	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}


	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

	public AbstractSubscriptionActionAuditProcessor getSubscriptionActionAuditProcessor() {
		return subscriptionActionAuditProcessor;
	}

	public void setSubscriptionActionAuditProcessor(
			AbstractSubscriptionActionAuditProcessor subscriptionActionAuditProcessor) {
		this.subscriptionActionAuditProcessor = subscriptionActionAuditProcessor;
	}

}
