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
package org.ojbc.adapters.identifier.processor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will handle archive processing.  It will interact with the DAO and then create either a valid response or error response 
 */

@Service
public class IdentifierRequestProcessor {
    private final Log log = LogFactory.getLog(this.getClass());

	private DocumentBuilder documentBuilder;

	public IdentifierRequestProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

	public Document returnIdentifierResponse(@Body Document request) throws Exception
	{
        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElementNS(
                OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE,
                OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE +":IdentificationResultsModificationResponse");
        
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE );
        rootElement.setAttribute("xmlns:" + OjbcNamespaceContext.NS_PREFIX_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT, OjbcNamespaceContext.NS_IDENTIFICATION_RESULTS_MODIFICATION_RESPONSE_EXT  );
        
        document.appendChild(rootElement);

		return document;
	}
}
