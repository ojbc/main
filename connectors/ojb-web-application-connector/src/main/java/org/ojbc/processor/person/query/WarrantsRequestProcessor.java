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
package org.ojbc.processor.person.query;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.RequestResponseProcessor;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class WarrantsRequestProcessor extends RequestResponseProcessor implements CamelContextAware, DetailsQueryInterface {

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor warrantsMessageProcessor;
	
	private OJBSamlMap OJBSamlMap;
	
	private boolean allowQueriesWithoutSAMLToken;
	
	private static final Log log = LogFactory.getLog( WarrantsRequestProcessor.class );
	
	public String invokeRequest(DetailsRequest warrantsRequest, String federatedQueryID, Element samlToken) throws Exception
	{
		if (allowQueriesWithoutSAMLToken)
		{	
			if (samlToken == null)
			{
				//Add SAML token to request call
				samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
			}	
		}

		if (samlToken == null)
		{
			throw new Exception("No SAML token provided. Unable to perform query.");
		}	
		
		//POJO to XML Request
		String warrantsRequestPayload = RequestMessageBuilderUtilities.createPersonQueryRequest(warrantsRequest);
		
		//Create exchange
		Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		
		//Set exchange body to XML Request message
		senderExchange.getIn().setBody(warrantsRequestPayload);
		
		//Set reply to and WS-Addressing message ID
		senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID);
		senderExchange.getIn().setHeader("WSAddressingReplyTo", this.getReplyToAddress());

		//Set the token header so that CXF can retrieve this on the outbound call
		String tokenID = senderExchange.getExchangeId();
		senderExchange.getIn().setHeader("tokenID", tokenID);

		OJBSamlMap.putToken(tokenID, samlToken);

		warrantsMessageProcessor.sendResponseMessage(camelContext, senderExchange);
		
		//Put message ID and "noResponse" place holder.  
		putRequestInMap(federatedQueryID);
		
		String response = pollMap(federatedQueryID);
		
		if (response.length() > 500)
		{	
			log.debug("Here is the response (truncated): " + response.substring(0,500));
		}
		else
		{
			log.debug("Here is the response: " + response);
		}
		
		//return response here
		return response;
	}
	

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}


	public MessageProcessor getWarrantsMessageProcessor() {
		return warrantsMessageProcessor;
	}


	public void setWarrantsMessageProcessor(
			MessageProcessor warrantsMessageProcessor) {
		this.warrantsMessageProcessor = warrantsMessageProcessor;
	}


	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}


	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}


	public boolean isAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}


	public void setAllowQueriesWithoutSAMLToken(boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

}
