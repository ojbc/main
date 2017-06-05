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

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will handle archive processing.  It will interact with the DAO and then create either a valid response or error response 
 */

@Service
public class ArchiveProcessor {
    private final Log log = LogFactory.getLog(this.getClass());

	@Resource
    private RapbackDAO rapbackDAO;
	
	private DocumentBuilder documentBuilder;

	public ArchiveProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

	public Document processArchiveResult(@Header(value="archiveTransactionNumber") String transactionNumber, @Header(value="systemName") String systemName) throws Exception
	{
		int result = rapbackDAO.archiveIdentificationResult(transactionNumber);
		log.info("archive result: " + result);
		
        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE,
                OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE +":IdentificationResultsModificationResponse");
        
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE );
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT  );
        
        document.appendChild(rootElement);

        Element identificationResultsModificationIndicator = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT, "IdentificationResultsModificationIndicator");
        
        if (result == 0)
        {
        	identificationResultsModificationIndicator.setTextContent("false");
        }	

        if (result == 1)
        {
        	identificationResultsModificationIndicator.setTextContent("true");
        }	

        Element systemNameElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
        systemNameElement.setTextContent(systemName);
        
		return document;
	}
	
	//<irm-resp-doc:IdentificationResultsModificationResponse 
	// xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" 
	// xmlns:irm-resp-doc="http://ojbc.org/IEPD/Exchange/IdentificationResultsModificationResponse/1.0" 
	// xmlns:irm-err-rep="http://ojbc.org/IEPD/Extensions/IdentificationResultsModificationRequestErrorReporting/1.0" 
	// xmlns:irm-rm="http://ojbc.org/IEPD/Extensions/IdentificationResultsModificationResponseMetadata/1.0">
	//	<irm-rm:IdentificationResultsModificationResponseMetadata>
	//		<irm-err-rep:IdentificationResultsModificationRequestError>
	//			<irm-err-rep:ErrorText>Error Text</irm-err-rep:ErrorText>
	//			<nc:SystemName>System_1</nc:SystemName>
	//		</irm-err-rep:IdentificationResultsModificationRequestError>
	//	</irm-rm:IdentificationResultsModificationResponseMetadata>
	//</irm-resp-doc:IdentificationResultsModificationResponse>
	
	public Document processArchiveError(@ExchangeException Exception exception, @Header(value="systemName") String systemName) throws Exception
	{
		Document document = documentBuilder.newDocument();
		
        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE,
                OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE +":IdentificationResultsModificationResponse");

        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_NC_30 );
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE );

        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING );
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_METADATA, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_METADATA );
        
        document.appendChild(rootElement);

        Element identificationResultsModificationResponseMetadata = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_METADATA, "IdentificationResultsModificationResponseMetadata");
        
        Element identificationResultsModificationRequestError = XmlUtils.appendElement(identificationResultsModificationResponseMetadata, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING, "IdentificationResultsModificationRequestError");
        
        Element errorTextElement = XmlUtils.appendElement(identificationResultsModificationRequestError, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_REQUEST_ERROR_REPORTING, "ErrorText");
        errorTextElement.setTextContent(exception.getMessage());
        
        Element systemNameElement = XmlUtils.appendElement(identificationResultsModificationRequestError, OjbcNamespaceContext.NS_NC_30, "SystemName");
        systemNameElement.setTextContent(systemName);
        
		return document;
	}

	public RapbackDAO getRapbackDAO() {
		return rapbackDAO;
	}

	public void setRapbackDAO(RapbackDAO rapbackDAO) {
		this.rapbackDAO = rapbackDAO;
	}
	
}
