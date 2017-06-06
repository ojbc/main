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
package org.ojbc.processor.policyacknowledgement;

import java.util.List;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.ojbc.policyacknowledgement.dao.Policy;
import org.ojbc.policyacknowledgement.dao.PolicyDAO;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class PolicyAcknowledgementProcessor {
    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private PolicyDAO policyDAO;

    private DocumentBuilder documentBuilder;

    @Value("${system.source}")
    private String systemSource;

    @Value("${system.name}")
    private String systemName;

    private final String ACCESS_DENIED = "Access Denied";
    private final String ACCESS_GRANTED = "Access Granted";

    public PolicyAcknowledgementProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    /**
     * 
     * @param federationId
     * @return a XML string response abide by the Access Control Response
     *         schema.
     */
    public Document returnAccessControlResponse(@Header("federationId") String federationId,
            @Header("requestedResourceUri") String requestedResourceUri, @Header("ori") String ori) {

        log.debug("Processing access control request for federationId:" + StringUtils.trimToEmpty(federationId));
        log.debug("Employer ORI List:" + StringUtils.trimToEmpty(ori));

        if (!systemSource.equals(requestedResourceUri)) {
            String errorMessage = "The requested Resource "
                    + StringUtils.trimToEmpty(requestedResourceUri) + " is not available.";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        List<Policy> outstandingPolicies = policyDAO.getOutstandingPoliciesForUser(federationId, ori);

        AccessControlResponse accessControlResponse = new AccessControlResponse(
                outstandingPolicies, systemSource);
        log.debug(accessControlResponse);

        Document accessControlResponseDocument = buildAccessControlResponse(accessControlResponse);

        return accessControlResponseDocument;
    }
    
    private Document buildAccessControlResponse(AccessControlResponse accessControlResponse) {
        // public static final String NS_NC_30 =
        // "http://release.niem.gov/niem/niem-core/3.0/";

        Document document = documentBuilder.newDocument();
        Element rootElement = createAccessControlResponseRootElement(document);

        Element accessControlDecision = XmlUtils.appendElement(rootElement,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessControlDecision");
        XmlUtils.addAttribute(accessControlDecision, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "DECISION_01");

        Element accessDeniedIndicator = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDeniedIndicator");
        accessDeniedIndicator.setTextContent(accessControlResponse.getAccessDenied().toString());

        Element accessDecisionDescriptionText = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDecisionDescriptionText");
        if (accessControlResponse.getAccessDenied()) {
            accessDecisionDescriptionText.setTextContent(ACCESS_DENIED);
        } else {
            accessDecisionDescriptionText.setTextContent(ACCESS_GRANTED);
        }

        Element accessDecisionResourceURI = XmlUtils.appendElement(accessControlDecision,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "AccessDecisionResourceURI");
        accessDecisionResourceURI.setTextContent(systemSource);

        if (accessControlResponse.getAccessDenied()) {
            createAccessControlDecisionContext(accessControlDecision,
                    accessControlResponse.getOutstandingPolicies());
        }

        return document;
    }

    public Document buildAccessControlErrorResponse(@ExchangeException Exception exception) {
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
        errorTextNode.setTextContent(exception.getMessage());

        Element systemNameNode = XmlUtils.appendElement(accessControlRequestErrorNode, OjbcNamespaceContext.NS_NC_30,
                "SystemName");
        systemNameNode.setTextContent(systemName);
        return document;
    }

    private Element createAccessControlResponseRootElement(Document document) {
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

    private Element createAccessControlErrorResponseRootElement(Document document) {
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

    private Element createAccessControlDecisionContext(Element parent,
            List<Policy> outstandingPolicies) {
        Element accessControlDecisionContext = XmlUtils.appendElement(parent,
                OjbcNamespaceContext.NS_ACCESS_CONTROL_EXT, "ac-ext:AccessControlDecisionContext");
        Element policyBasedAccessControlDecisionContext = XmlUtils.appendElement(
                accessControlDecisionContext, OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT,
                "PolicyBasedAccessControlDecisionContext");

        for (Policy policy : outstandingPolicies) {
            Element policyNode = XmlUtils.appendElement(policyBasedAccessControlDecisionContext,
                    OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT, "Policy");
            Element policyUri = XmlUtils.appendElement(policyNode,
                    OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT, "PolicyURI");
            policyUri.setTextContent(policy.getPolicyUri());

            Element policyLocationUrl = XmlUtils.appendElement(policyNode,
                    OjbcNamespaceContext.NS_POLICY_DECISION_CONTEXT, "PolicyLocationURL");
            policyLocationUrl.setTextContent(policy.getPolicyLocation());
        }

        return accessControlDecisionContext;
    }

    public Document acknowlegePolicies(
            @Header("policyAcknowledgementIndicator") String acknowledgeIndicator,
            @Header(CxfConstants.CAMEL_CXF_MESSAGE) Message cxfMessage) {

        if (Boolean.valueOf(acknowledgeIndicator)) {
            String federationId = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.FederationId);
            String employerOri = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.EmployerORI); 

            if (StringUtils.isNotBlank(federationId)) {
                //Pass the ORIs in the SAML assertion to the DAO method. 
                policyDAO.acknowledgeOutstandingPolicies(federationId, employerOri);
                log.info("Acknowleged all outstanding Policies for " + federationId);
            } else {
                throw new IllegalArgumentException(
                        "Either request is missing SAML assertion or the federation ID is missing in the SAML assertion. ");
            }
        } else {
            throw new IllegalArgumentException(
                    "PolicyAcknowledgementIndicator in the request is set to 'false'. No action is taken.");
        }

        Document document = createAcknowledgementRecordingSuccessResponse();
        return document;
    }

    private Document createAcknowledgementRecordingSuccessResponse() {

        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE,
                "arr-exchange:AcknowledgementRecordingResponse");
        rootElement
                .setAttribute("xsi:schemaLocation",
                        "http://ojbc.org/IEPD/Exchange/AcknowledgementRecordingResponse/1.0 ../xsd/exchange_schema.xsd");
        rootElement.setAttribute("xmlns:arr-exchange",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE);
        rootElement.setAttribute("xmlns:arr-ext",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT);
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        document.appendChild(rootElement);

        Element policyAcknowledgementRecordingIndicator = XmlUtils.appendElement(rootElement,
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT,
                "PolicyAcknowledgementRecordingIndicator");
        policyAcknowledgementRecordingIndicator.setTextContent("true");
        return document;
    }

    public Document createAcknowledgementRecordingErrorResponse(
            @ExchangeException Exception exception) {
        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE,
                "arr-exchange:AcknowledgementRecordingResponse");
        rootElement
                .setAttribute("xsi:schemaLocation",
                        "http://ojbc.org/IEPD/Exchange/AcknowledgementRecordingResponse/1.0 ../xsd/exchange_schema.xsd");
        rootElement.setAttribute("xmlns:nc30", OjbcNamespaceContext.NS_NC_30);
        rootElement.setAttribute("xmlns:arr-exchange",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXCHANGE);
        rootElement.setAttribute("xmlns:arrer",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING);
        rootElement.setAttribute("xmlns:arr-ext",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESPONSE_EXT);
        rootElement.setAttribute("xmlns:arr-srm",
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA);
        rootElement.setAttribute("xmlns:xsi", OjbcNamespaceContext.NS_XSI);
        document.appendChild(rootElement);

        Element acknowledgementRecordingResponseMetadata = XmlUtils.appendElement(rootElement,
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_RESULT_METADATA,
                "AcknowledgementRecordingResponseMetadata");

        Element acknowledgementRecordingRequestError = XmlUtils.appendElement(
                acknowledgementRecordingResponseMetadata,
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING,
                "AcknowledgementRecordingRequestError");

        Element errorText = XmlUtils.appendElement(acknowledgementRecordingRequestError,
                OjbcNamespaceContext.NS_ACKNOWLEGEMENT_RECORDING_ERROR_REPORTING, "ErrorText");
        errorText.setTextContent(exception.getMessage());

        Element systemNameNode = XmlUtils.appendElement(acknowledgementRecordingRequestError,
                OjbcNamespaceContext.NS_NC_30, "SystemName");
        systemNameNode.setTextContent(systemName);

        return document;
    }


}
