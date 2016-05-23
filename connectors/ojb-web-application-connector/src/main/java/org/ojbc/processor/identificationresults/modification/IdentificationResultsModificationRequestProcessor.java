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
package org.ojbc.processor.identificationresults.modification;

import static org.ojbc.util.helper.UniqueIdUtils.getFederatedQueryId;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.processor.RequestResponseProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.IdentificationResultsModificationInterface;
import org.ojbc.web.model.SimpleServiceResponse;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class IdentificationResultsModificationRequestProcessor extends RequestResponseProcessor implements CamelContextAware, IdentificationResultsModificationInterface {

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor messageProcessor;
	
	private OJBSamlMap OJBSamlMap;
	
	private boolean allowQueriesWithoutSAMLToken;
	
	private static final Log log = LogFactory.getLog( IdentificationResultsModificationRequestProcessor.class );
	
	@Override
	public SimpleServiceResponse handleIdentificationResultsModificationRequest(
			String transactionNumber, Element samlToken) throws Exception {
		
		checkTransactionNumber(transactionNumber);
		
		if (allowQueriesWithoutSAMLToken && samlToken == null )
		{	
			//Add SAML token to request call
			samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", 
					SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		}
		log.info("Processing initial results request with transaction number: " + StringUtils.trimToEmpty(transactionNumber) );
		
		Document identificationResultsModificationRequestPayload;
		identificationResultsModificationRequestPayload = RequestMessageBuilderUtilities.createIdentificationResultsModificationRequest(transactionNumber);
		
		//Create exchange
		Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		
		//Set exchange body to XML Request message
		senderExchange.getIn().setBody(identificationResultsModificationRequestPayload);
		
		//Set reply to and WS-Addressing message ID
		String federatedQueryID = getFederatedQueryId();
		senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID );
		senderExchange.getIn().setHeader("WSAddressingReplyTo", this.getReplyToAddress());

		//Set the token header so that CXF can retrieve this on the outbound call
		String tokenID = senderExchange.getExchangeId();
		senderExchange.getIn().setHeader("tokenID", tokenID);

		OJBSamlMap.putToken(tokenID, samlToken);

		messageProcessor.sendResponseMessage(camelContext, senderExchange);
		
		//Put message ID and "noResponse" place holder.  
		putRequestInMap(federatedQueryID);
		
		Exchange responseExchange = pollMapForResponseExchange(federatedQueryID);
		
		return retrieveResponse(responseExchange);
	}

	private void checkTransactionNumber(String transactionNumber) {
		if (StringUtils.isBlank(transactionNumber)){
			throw new IllegalArgumentException("Transaction number should not be null or empty to perform initial results query."); 
		}
	}

	private SimpleServiceResponse retrieveResponse(
			Exchange exchange) throws Exception {
		SimpleServiceResponse serviceResponse = 
				new SimpleServiceResponse();
		Document body = OJBUtils.loadXMLFromString((String) exchange.getIn().getBody()); 
		String status = 
				XmlUtils.xPathStringSearch(body, "/irm-resp-doc:IdentificationResultsModificationResponse/irm-resp-ext:IdentificationResultsModificationIndicator");
		serviceResponse.setSuccess(BooleanUtils.toBooleanObject(status, "true", "false", "false"));
		
		if (!serviceResponse.getSuccess()){
			String errorMessage = XmlUtils.xPathStringSearch(body, 
					"/irm-resp-doc:IdentificationResultsModificationResponse/"
					+ "irm-rm:IdentificationResultsModificationResponseMetadata/"
					+ "irm-err-rep:IdentificationResultsModificationRequestError/irm-err-rep:ErrorText");
			serviceResponse.setErrorMessage(StringUtils.trimToEmpty(errorMessage));
		}

		log.debug("Identification Results Modification Response: " + serviceResponse.toString());
		return serviceResponse;
	}
	
	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}


	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}


	public void setMessageProcessor(
			MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
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
