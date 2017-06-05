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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PERSON_IDENTIFICATION_REPORT_RESPONSE;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class IdentificationReportingResponseProcessor{

	private static final Log log = LogFactory.getLog( IdentificationReportingResponseProcessor.class );
	
    private DocumentBuilder documentBuilder;
    
    @Value("${system.name}")
    private String systemName;

    private enum IdentificationReportStatus {success, failure};
    
    private final static String ERROR_STATUS_COMMENT_TEXT = "report failed to process";

    public IdentificationReportingResponseProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
    
	@Transactional
	public Document createErrorResponse(@Header("identificationID") String identificationID, 
			@Header("transactionCategoryText") String transactionCategoryText) 
	{
		return createResponse(identificationID, transactionCategoryText, IdentificationReportStatus.failure);
	}

	private Element createRootElement(Document document) {
        Element rootElement = document.createElementNS(
                NS_PERSON_IDENTIFICATION_REPORT_RESPONSE,
                NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE +":PersonIdentificationReportResponse");
        rootElement.setAttribute("xmlns:" + NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE );
        rootElement.setAttribute("xmlns:" + NS_PREFIX_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT );
        rootElement.setAttribute("xmlns:nc30", NS_NC_30);
        document.appendChild(rootElement);
        return rootElement;
	}

	@Transactional
	public Document createSuccessResponse(@Header("identificationID") String identificationID, 
			@Header("transactionCategoryText") String transactionCategoryText) 
	{
		return createResponse(identificationID, transactionCategoryText, IdentificationReportStatus.success);
	}

	private Document createResponse(String identificationID,
			String transactionCategoryText, IdentificationReportStatus identificationReportStatus) {
		
		log.info("Creating identification report response with ID '" + StringUtils.trimToEmpty(identificationID)
				+ "', TransactionCategoryText '" + StringUtils.trimToEmpty(transactionCategoryText)
				+ "' and status " + identificationReportStatus.name());
		
		Document document = documentBuilder.newDocument();
        Element rootElement = createRootElement(document);
        
        Element transactionIdentificationElement = 
        		XmlUtils.appendElement(rootElement, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, "TransactionIdentification"); 
        Element identificationIdElement = XmlUtils.appendElement(transactionIdentificationElement, NS_NC_30, "IdentificationID");
        identificationIdElement.setTextContent(identificationID);
        
        Element transactionCategoryTextElement = 
        		XmlUtils.appendElement(rootElement, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, "TransactionCategoryText");
        transactionCategoryTextElement.setTextContent(transactionCategoryText);
        
        Element identificationReportStatusElement = 
        		XmlUtils.appendElement(rootElement, NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, "IdentificationReportStatus");
        
        if (identificationReportStatus == IdentificationReportStatus.failure){
            Element statusCommentTextElement = 
            		XmlUtils.appendElement(identificationReportStatusElement, NS_NC_30, "StatusCommentText");
            statusCommentTextElement.setTextContent(ERROR_STATUS_COMMENT_TEXT);
        }
        
        Element statusIssuerTextElement = 
        		XmlUtils.appendElement(identificationReportStatusElement, NS_NC_30, "StatusIssuerText");
        statusIssuerTextElement.setTextContent(systemName);
        
        Element identificationReportStatusCode = 
        		XmlUtils.appendElement(identificationReportStatusElement, 
        				NS_PERSON_IDENTIFICATION_REPORT_RESPONSE_EXT, "IdentificationReportStatusCode");
        identificationReportStatusCode.setTextContent(identificationReportStatus.toString());
        
		return document;
	}
}
