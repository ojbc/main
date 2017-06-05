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
package org.ojbc.processor;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.endpoint.Client;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FaultableSynchronousMessageProcessor {

	private static final Log log = LogFactory.getLog( FaultableSynchronousMessageProcessor.class );
	
	private String operationName;
	private String operationNamespace;
	private String destinationEndpoint;
	
	/**
	 * @return
	 * 		The </soap:Envelope> element inside the faultable response which may also contain an exception
	 */
	public FaultableSoapResponse sendSynchronousResponseMessage(CamelContext context, Exchange exchange) throws Exception{
				
		Exchange senderExchange = null;
	
		senderExchange = new DefaultExchange(context, ExchangePattern.InOut);	
		
		//This is used to propogate SAML tokens
		String tokenID = (String)exchange.getIn().getHeader("tokenID");

		if (StringUtils.isNotEmpty(tokenID)){
			
			log.debug("Saml Token ID in Message Processor: " + tokenID);
			senderExchange.getIn().setHeader("tokenID", tokenID);
		}
				
		String requestID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");
		
    	//Create a new map with WS Addressing message properties that we want to override
		HashMap<String, String> wsAddressingMessageProperties = new HashMap<String, String>();
		wsAddressingMessageProperties.put("MessageID",requestID);

		String replyTo = (String)exchange.getIn().getHeader("WSAddressingReplyTo");

		if (StringUtils.isNotEmpty(replyTo)){
			
			log.debug("WS Addressing Reply To Camel Header: " + replyTo);
			wsAddressingMessageProperties.put("ReplyTo",replyTo);
		}
		
		//Call method to create proper request context map
		Map<String, Object> requestContext = OJBUtils.setWSAddressingProperties(wsAddressingMessageProperties);
		
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
        senderExchange.getIn().setBody(exchange.getIn().getBody());
        
	    ProducerTemplate template = context.createProducerTemplate();

	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, getOperationName());
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, getOperationNamespace());
	    
		Exchange returnExchange = template.send("cxf:bean:" + destinationEndpoint + "?dataFormat=PAYLOAD",
				senderExchange);
				
		FaultableSoapResponse faultableSoapResponse = getResponseFromExchangeOutHeaders(returnExchange);
                           	
        log.debug("returning FaultableSoapResponse: " + faultableSoapResponse);
        
		return faultableSoapResponse;
	}	
		
	
	/**
	 * TODO remove soap env string param if locations using it only really need the soap body
	 */
	private FaultableSoapResponse getResponseFromExchangeOutHeaders(Exchange returnExchange) throws Exception{
		
        Map<String, Object> headerMap = returnExchange.getOut().getHeaders();
        
        SoapMessage cxfMessage = (SoapMessage)headerMap.get("camelcxfmessage");
                            
        Node soapEnvelopeNode = cxfMessage.getContent(Node.class);        
        
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(soapEnvelopeNode);
        trans.transform(source, result);
        
        String sSoapEnvelopeMessgae = sw.toString();                  
                                                                  
        Exception exchangeException = returnExchange.getException();
            
        Document soapBodyDoc = getSoapBodyDocFromSoapEnv(soapEnvelopeNode);
        
        FaultableSoapResponse rFaultableSoapResponse = null;
        
        if(exchangeException != null || StringUtils.isNotBlank(sSoapEnvelopeMessgae)){
        	        	        	
        	rFaultableSoapResponse = new FaultableSoapResponse(exchangeException, sSoapEnvelopeMessgae, soapBodyDoc);
        }        
		
        return rFaultableSoapResponse;
	}
	
	
	Document getSoapBodyDocFromSoapEnv(Node soapEnvNode) throws Exception{
		
    	//create convenience Document of soap body.  
        Node soapBodyNode = XmlUtils.xPathNodeSearch(soapEnvNode, "//soap:Body");        
		Document soapBodyDoc = getDocBuilder().newDocument();		
		Node importedSoapBodyNode = soapBodyDoc.importNode(soapBodyNode, true);		
		soapBodyDoc.appendChild(importedSoapBodyNode);  
		
		return soapBodyDoc;
	}
	
	
	static DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder docBuilder;
		docBuilder = fact.newDocumentBuilder();
		
		return docBuilder;
	}		

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationNamespace() {
		return operationNamespace;
	}

	public void setOperationNamespace(String operationNamespace) {
		this.operationNamespace = operationNamespace;
	}

	public String getDestinationEndpoint() {
		return destinationEndpoint;
	}

	public void setDestinationEndpoint(String destinationEndpoint) {
		this.destinationEndpoint = destinationEndpoint;
	}

	
}
