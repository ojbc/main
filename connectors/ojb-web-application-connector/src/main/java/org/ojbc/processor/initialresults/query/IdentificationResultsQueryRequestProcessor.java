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
package org.ojbc.processor.initialresults.query;

import static org.ojbc.util.helper.UniqueIdUtils.getFederatedQueryId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.processor.RequestResponseProcessor;
import org.ojbc.util.camel.helper.MtomUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.IdentificationResultsQueryInterface;
import org.ojbc.web.model.IdentificationResultsQueryResponse;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class IdentificationResultsQueryRequestProcessor extends RequestResponseProcessor implements CamelContextAware, IdentificationResultsQueryInterface {

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor messageProcessor;
	
	private OJBSamlMap OJBSamlMap;
	
	private boolean allowQueriesWithoutSAMLToken;
	
	private static final Log log = LogFactory.getLog( IdentificationResultsQueryRequestProcessor.class );
	
	@Override
	public IdentificationResultsQueryResponse invokeIdentificationResultsQueryRequest(
			String transactionNumber, Element samlToken) throws Exception {
		
		if (StringUtils.isBlank(transactionNumber)){
			throw new IllegalArgumentException("Transaction number should not be null or empty to perform initial results query."); 
		}
		
		if (allowQueriesWithoutSAMLToken)
		{	
			if (samlToken == null)
			{
				//Add SAML token to request call
				samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
			}	
		}
		log.info("Processing initial results request with transaction number: " + StringUtils.trimToEmpty(transactionNumber) );
		Document identificationResultsQueryRequestPayload = RequestMessageBuilderUtilities.createIdentificationResultsQueryRequest(transactionNumber);
		
		//Create exchange
		Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		
		//Set exchange body to XML Request message
		senderExchange.getIn().setBody(identificationResultsQueryRequestPayload);
		
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

	private IdentificationResultsQueryResponse retrieveResponse(
			Exchange exchange) throws Exception {
		IdentificationResultsQueryResponse identificationResultsQueryResponse = 
				new IdentificationResultsQueryResponse();
		String stateSearchResultDocument = getDocument(exchange, 
				"/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/"
				+ "oirq-res-ext:StateIdentificationSearchResultDocument/xop:Include/@href");
		identificationResultsQueryResponse.setStateSearchResultFile(new String(stateSearchResultDocument));
		
		String fbiSearchResultDocument = getDocument(exchange, 
				"/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/"
				+ "oirq-res-ext:FBIIdentificationSearchResultDocument/xop:Include/@href");
		identificationResultsQueryResponse.setFbiSearchResultFile(fbiSearchResultDocument);
		
		List<String> stateCriminalHistoryRecordDocuments = getDocuments(exchange,
				"/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/"
				+ "oirq-res-ext:StateCriminalHistoryRecordDocument/xop:Include");
		identificationResultsQueryResponse.setStateCriminalHistoryRecordDocuments(
				stateCriminalHistoryRecordDocuments);
		
		List<String> fbiIdentityHistorySummaryDocuments = getDocuments(exchange,
				"/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/"
						+ "oirq-res-ext:FBIIdentityHistorySummaryDocument/xop:Include");
		identificationResultsQueryResponse.setFbiIdentityHistorySummaryDocuments(
				fbiIdentityHistorySummaryDocuments);
		log.debug("Identification Results Query Response: " + identificationResultsQueryResponse.toString());
		return identificationResultsQueryResponse;
	}

	private List<String> getDocuments(Exchange exchange, String xPath) throws Exception {
		Document body = OJBUtils.loadXMLFromString( (String) exchange.getIn().getBody()); 
		NodeList documentIdNodes = XmlUtils.xPathNodeListSearch(body, xPath);
		
		List<String> documents = new ArrayList<String>();
	    if (documentIdNodes != null && documentIdNodes.getLength() > 0) {
	        for (int i = 0; i < documentIdNodes.getLength(); i++) {
	            if (documentIdNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element element = (Element) documentIdNodes.item(i);
	                String documentId = element.getAttribute("href");
	                documents.add(new String(MtomUtils.getAttachment(exchange, null, documentId))); 
	            }
	        }
	    }
		return documents;
	}

	private String getDocument(Exchange exchange, String xPath)
			throws Exception, IOException {
		Document body = OJBUtils.loadXMLFromString((String) exchange.getIn().getBody()); 

		String stateSearchResultDocumentId = 
				XmlUtils.xPathStringSearch(body, xPath);
		byte[] stateSearchResultDocument = MtomUtils.getAttachment(exchange, null, stateSearchResultDocumentId);
		return new String(stateSearchResultDocument);
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
