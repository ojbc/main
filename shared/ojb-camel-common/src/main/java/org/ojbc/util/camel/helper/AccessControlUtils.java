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
 * @author yogeshchawla, haiqiwei
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
                "ac-exchange:AccessControlResponse");
        rootElement
                .setAttribute("xsi:schemaLocation",
                        "http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0 ../xsd/exchange_schema.xsd");
        rootElement.setAttribute("xmlns:s30", OjbcNamespaceContext.NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:ac-exchange",
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE);
        rootElement.setAttribute("xmlns:ac-ext", OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT);
        rootElement.setAttribute("xmlns:ac-p", OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT);
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        document.appendChild(rootElement);
        return rootElement;
    }
    
    private static Element createAccessControlErrorResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXCHANGE, "ac-exchange:AccessControlResponse");
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        rootElement.setAttribute("xmlns:ac-exchange",
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
