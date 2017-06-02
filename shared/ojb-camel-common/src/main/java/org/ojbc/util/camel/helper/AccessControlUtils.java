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
package org.ojbc.util.camel.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will make Access Control XML documents based on the SSP.  Services providing these messages
 * can used the static methods to create valid and error responses.
 * 
 */
public class AccessControlUtils {

    public static final String ACCESS_DENIED = "Access Denied";
    public static final String ACCESS_GRANTED = "Access Granted";

    public static Document buildAccessControlErrorResponse(@ExchangeException Exception exception, 
    		@Header("AccessControlRequestingSystemName") String accessControlRequestingSystemName) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    	
        Document document = documentBuilder.newDocument();
        Element rootElement = createAccessControlErrorResponseRootElement(document);

        Element accessControlResultsMetadataNode = XmlUtils.appendElement(rootElement,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_RESULT_METADATA,
                "AccessControlResultsMetadata");

        Element accessControlRequestErrorNode = XmlUtils.appendElement(accessControlResultsMetadataNode, 
                OjbcNamespaceContext.NS_ACCESS_CONTROL_ERROR_REPORTING,
                "AccessControlRequestError");

        Element errorTextNode = XmlUtils.appendElement(accessControlRequestErrorNode,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_ERROR_REPORTING, "ErrorText");
        
        if (exception != null)
        {	
        	errorTextNode.setTextContent(exception.getMessage());
        }
        else
        {
        	errorTextNode.setTextContent("Unable to process request.");
        }	

        Element systemNameNode = XmlUtils.appendElement(accessControlRequestErrorNode, OjbcNamespaceContext.NS_NC_30,
                "SystemName");
        systemNameNode.setTextContent(accessControlRequestingSystemName);
        return document;
    }

    public static Document buildAccessControlResponse(boolean isAccessDenied, String systemSource) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

    	
        Document document = documentBuilder.newDocument();
        Element rootElement = createAccessControlResponseRootElement(document);

        Element accessControlDecision = XmlUtils.appendElement(rootElement,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessControlDecision");
        XmlUtils.addAttribute(accessControlDecision, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "DECISION_01");

        Element accessDeniedIndicator = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDeniedIndicator");
        accessDeniedIndicator.setTextContent(String.valueOf(isAccessDenied));

        Element accessDecisionDescriptionText = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDecisionDescriptionText");
        if (isAccessDenied) {
            accessDecisionDescriptionText.setTextContent(ACCESS_DENIED);
        } else {
            accessDecisionDescriptionText.setTextContent(ACCESS_GRANTED);
        }

        Element accessDecisionResourceURI = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDecisionResourceURI");
        accessDecisionResourceURI.setTextContent(systemSource);

        return document;
    }

    private static Element createAccessControlResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE,
                "ac-doc:AccessControlResponse");
        rootElement
                .setAttribute("xsi:schemaLocation",
                        "http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0 ../xsd/exchange_schema.xsd");
        rootElement.setAttribute("xmlns:s30", OjbcNamespaceContext.NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:ac-doc",
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE);
        rootElement.setAttribute("xmlns:ac-ext", OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT);
        rootElement.setAttribute("xmlns:ac-p", OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT);
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        document.appendChild(rootElement);
        return rootElement;
    }
    
    private static Element createAccessControlErrorResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE, "ac-doc:AccessControlResponse");
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        rootElement.setAttribute("xmlns:ac-doc",
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE);
        rootElement.setAttribute("xmlns:acrer",
                OjbcNamespaceContext.NS_ACCESS_CONTROL_ERROR_REPORTING);
        rootElement.setAttribute("xmlns:acr-srm",
                OjbcNamespaceContext.NS_ACCESS_CONTROL_RESULT_METADATA);
        rootElement.setAttribute("xmlns:nc30", OjbcNamespaceContext.NS_NC_30);
        rootElement
                .setAttribute("xsi:schemaLocation",
                        "http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0 ../xsd/exchange_schema.xsd");
        document.appendChild(rootElement);
        return rootElement;
    }
}
