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
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.util.FaultMessageBuilderUtil;
import org.ojbc.intermediaries.sn.util.SubscriptionResponseBuilderUtil;

/**
 * The abstract base class for beans that process subscription and unsubscription requests.  There will be a concrete derivation of this class for each topic.
 *
 */
public abstract class SubscriptionProcessor {

    private static final Log log = LogFactory.getLog(SubscriptionProcessor.class);

    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    private FaultMessageProcessor faultMessageProcessor;

    /**
     * Determine the patterns of email addresses allowed by this processor
     */
    protected String allowedEmailAddressPatterns;

    /**
     * Create the topic-specific unsubscription request from the inbound message
     * @param msg the Camel message object
     * @return the unsubscription request
     * @throws Exception
     */
    abstract protected UnSubscriptionRequest makeUnSubscriptionRequestFromIncomingMessage(Message msg) throws Exception;

    /**
     * Create the topic-specific subscription request from the inbound message
     * @param msg the Camel message object
     * @return the subscription request
     * @throws Exception
     */
    abstract protected SubscriptionRequest makeSubscriptionRequestFromIncomingMessage(Message msg) throws Exception;

    /**
     * Get the topic (string) that's the subject of this processor (i.e., what types of subscriptions/unsubscriptions does it deal with)
     * @return
     */
    abstract protected String getTopic();

    /**
     * Main behavior method, invoked from the Camel route, to subscribe
     * @param exchange the Camel exchange object containing the subscription request
     * @throws Exception
     */
    public void subscribe(Exchange exchange) throws Exception {
        Message incomingMsg = exchange.getIn();
        SubscriptionRequest request = makeSubscriptionRequestFromIncomingMessage(incomingMsg);
        
        subscriptionSearchQueryDAO.subscribe(request, new LocalDate());
        
        exchange.getOut().setBody(SubscriptionResponseBuilderUtil.createSubscribeResponse());
    }

    /**
     * Main behavior method, invoked from the Camel route, to unsubscribe
     * @param exchange the Camel exchange object containing the unsubscription request
     * @throws Exception
     */
    public void unsubscribe(Exchange exchange) throws Exception {
        Message incomingMsg = exchange.getIn();
        UnSubscriptionRequest request = makeUnSubscriptionRequestFromIncomingMessage(incomingMsg);        
                
        Message out = exchange.getOut();
        
//		Note "Camel" prefix in spelling required for cxf "drop headers" hack which only removes http headers 
//		beginning with "Camel"
        out.setHeader("OJBCunsubscribeRequestMessage", incomingMsg.getHeader("OJBCunsubscribeRequestMessage"));        
                
        String subscriptionOwner = (String) incomingMsg.getHeader("subscriptionOwner");
        
        processUnSubscriptionRequest(request, out, subscriptionOwner);
    }

    public FaultMessageProcessor getFaultMessageProcessor() {
        return faultMessageProcessor;
    }

    public void setFaultMessageProcessor(FaultMessageProcessor faultMessageProcessor) {
        this.faultMessageProcessor = faultMessageProcessor;
    }

    public String getAllowedEmailAddressPatterns() {
        return allowedEmailAddressPatterns;
    }

    public void setAllowedEmailAddressPatterns(String allowedEmailAddressPatterns) {
        this.allowedEmailAddressPatterns = allowedEmailAddressPatterns;
    }

    /**
     * Get the subscriptions for the specified parameters
     * @param subscriptionId
     * @param subjectIds
     * @param subscribingSystemId
     * @param subscriptionOwner
     * @return
    
    List<Subscription> getSubscriptions(String subscriptionId, Map<String, String> subjectIds, String subscribingSystemId, String subscriptionOwner) {

        List<Subscription> ret = null;

        if (StringUtils.isNotEmpty(subscriptionId)) {
            ret = subscriptionSearchQueryDAO.queryForSubscription(subscriptionId);
        } else {
            ret = subscriptionSearchQueryDAO.queryForSubscription(subscribingSystemId, subscriptionOwner, subjectIds);
        }
        
        return ret;

    } */

    void processUnSubscriptionRequest(UnSubscriptionRequest request, Message outMessage, String subscriptionOwner) throws Exception {

        // If this is not a valid arrest topic, throw a fault and exit
        if (!getTopic().equals(request.getTopic())) {
            outMessage.setHeader(org.apache.cxf.message.Message.RESPONSE_CODE, new Integer(500));
            outMessage.setBody(FaultMessageBuilderUtil.createFault("ResourceUnknownFault", "http://docs.oasis-open.org/wsrf/bf-2"));
            return;
        }

        log.debug("This is the topic:" + request.getTopic());
        log.debug("This is the Subject Identifier map:" + request.getSubjectIdentifiers());
        log.debug("This is the Email Address:" + request.getEmailAddresses());
        log.debug("This is the System Name:" + request.getSystemName());
        log.debug("This is the subscription qualifier:" + request.getSubscriptionQualifier());
        log.debug("This is the subscription system identifer:" + request.getSubscriptionSystemId());

        // Attempt to unsubscribe user
        int rowsUnsubscribed = subscriptionSearchQueryDAO.unsubscribe(request.getSubscriptionSystemId(), request.getTopic(), request.getSubjectIdentifiers(), request.getSystemName(), subscriptionOwner);

        log.debug("Unsubscribed this many rows: " + rowsUnsubscribed);

        // If no rows are returned, return fault
        if (rowsUnsubscribed == 0) {
            faultMessageProcessor.createFault(outMessage.getExchange());
            return;
        }

        if (rowsUnsubscribed > 0) {
            outMessage.setBody(SubscriptionResponseBuilderUtil.createUnsubscribeResponse());
        }

    }

    	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

}
