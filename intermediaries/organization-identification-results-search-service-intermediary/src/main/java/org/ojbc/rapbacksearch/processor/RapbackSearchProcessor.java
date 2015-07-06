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
package org.ojbc.rapbacksearch.processor;

import java.security.Policy;
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
import org.ojbc.rapbacksearch.dao.RapbackDAO;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class RapbackSearchProcessor {
    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private RapbackDAO rapbackDAO;

    private DocumentBuilder documentBuilder;

    @Value("${system.searchResultsExceedThreshold}")
    private Integer maxResultCount;

    @Value("${system.name}")
    private String systemName;

    public RapbackSearchProcessor() throws ParserConfigurationException {
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
    public Document returnRapbackSearchResponse(@Header(CxfConstants.CAMEL_CXF_MESSAGE) Message cxfMessage) {
        if (cxfMessage != null) {
            String federationId = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.FederationId);
            String employerOri = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.EmployerORI); 

            log.debug("Processing rapback search request for federationId:" + StringUtils.trimToEmpty(federationId));
            log.debug("Employer ORI : " + StringUtils.trimToEmpty(employerOri)); 
            
            if (StringUtils.isNotBlank(federationId)) {
                //Pass the ORIs in the SAML assertion to the DAO method. 
                rapbackDAO.getRapbackReports(federationId, employerOri);
                log.info("Get rapback search results count");
            } else {
                throw new IllegalArgumentException(
                        "Either request is missing SAML assertion or the federation ID or Employer ORI is missing in the SAML assertion. ");
            }
        } else {
            throw new IllegalArgumentException(
                    "Invalid request. CXF message is not found.");
        }

        Document rapbackSearchResponseDocument = buildRapbackSearchResponse();

        return rapbackSearchResponseDocument;
    }
    
    private Document buildRapbackSearchResponse() {

        Document document = documentBuilder.newDocument();
        Element rootElement = createRapbackSearchResponseRootElement(document);

        return document;
    }

    private Element createRapbackSearchResponseRootElement(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO build rapback error response
    public Document buildErrorResponse(@ExchangeException Exception exception) {
        Document document = documentBuilder.newDocument();
        Element rootElement = createErrorResponseRootElement(document);

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

    //TODO create rapback error response root element
    private Element createErrorResponseRootElement(Document document) {
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


}
